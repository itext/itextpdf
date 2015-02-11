package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.*;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class PdfCleanUpProcessor {

    private static final String XOBJ_NAME_PREFIX = "Fm";

    private static final String STROKE_COLOR = "StrokeColor";
    private static final String FILL_COLOR = "FillColor";

    private int currentXObjNum = 0;

    private Map<Integer, List<PdfCleanUpLocation>> pdfCleanUpLocations; // key - page number
    private PdfStamper pdfStamper;

    private Map<Integer, Set<String>> redactAnnotIndirRefs; // key - number of page containing redact annotations
    private Map<Integer, List<Rectangle>> clippingRects; // stores list of rectangles for annotation identified by it's index in Annots array

    /**
     * Create clean up processor.
     *
     * @param pdfCleanUpLocations list of locations to be cleaned up {@see PdfCleanUpLocation}
     * @param pdfStamper
     */
    public PdfCleanUpProcessor(List<PdfCleanUpLocation> pdfCleanUpLocations, PdfStamper pdfStamper) {
        this.pdfCleanUpLocations = organizeLocationsByPage(pdfCleanUpLocations);
        this.pdfStamper = pdfStamper;
    }

    /**
     * CleanUp locations are extracted from Redact annotations.
     *
     * @param pdfStamper
     */
    public PdfCleanUpProcessor(PdfStamper pdfStamper) {
        this.redactAnnotIndirRefs = new HashMap<Integer, Set<String>>();
        this.clippingRects = new HashMap<Integer, List<Rectangle>>();
        this.pdfStamper = pdfStamper;
        extractLocationsFromRedactAnnots();
    }

    public void cleanUp() throws IOException, DocumentException {
        for (Map.Entry<Integer, List<PdfCleanUpLocation>> entry : pdfCleanUpLocations.entrySet()) {
            cleanUpPage(entry.getKey(), entry.getValue());
        }

        pdfStamper.getReader().removeUnusedObjects();
    }

    private void cleanUpPage(int pageNum, List<PdfCleanUpLocation> cleanUpLocations) throws IOException, DocumentException {
        if (cleanUpLocations.size() == 0) {
            return;
        }

        PdfReader pdfReader = pdfStamper.getReader();
        PdfDictionary page = pdfReader.getPageN(pageNum);
        PdfContentByte canvas = pdfStamper.getUnderContent(pageNum);
        byte[] pageContentInput = ContentByteUtils.getContentBytesForPage(pdfReader, pageNum);
        page.remove(PdfName.CONTENTS);

        canvas.saveState();

        List<PdfCleanUpRegionFilter> filters = createFilters(cleanUpLocations);
        PdfCleanUpRenderListener pdfCleanUpRenderListener = new PdfCleanUpRenderListener(pdfStamper, filters);
        pdfCleanUpRenderListener.registerNewContext(pdfReader.getPageResources(page), canvas);

        PdfContentStreamProcessor contentProcessor = new PdfContentStreamProcessor(pdfCleanUpRenderListener);
        PdfCleanUpContentOperator.populateOperators(contentProcessor, pdfCleanUpRenderListener);
        contentProcessor.processContent(pageContentInput, page.getAsDict(PdfName.RESOURCES));
        pdfCleanUpRenderListener.popContext();

        canvas.restoreState();

        colorCleanedLocations(canvas, cleanUpLocations);

        if (redactAnnotIndirRefs != null) { // if it isn't null, then we are in "extract locations from redact annots" mode
            deleteRedactAnnots(pageNum);
        }
    }

    private List<PdfCleanUpRegionFilter> createFilters(List<PdfCleanUpLocation> cleanUpLocations) {
        List<PdfCleanUpRegionFilter> filters = new ArrayList<PdfCleanUpRegionFilter>();

        for (PdfCleanUpLocation cleanUpLocation : cleanUpLocations) {
            Rectangle region = cleanUpLocation.getRegion();
            filters.add(new PdfCleanUpRegionFilter(region));
        }

        return filters;
    }

    private void colorCleanedLocations(PdfContentByte canvas, List<PdfCleanUpLocation> cleanUpLocations) {
        for (PdfCleanUpLocation location : cleanUpLocations) {
            if (location.getCleanUpColor() != null) {
                addColoredRectangle(canvas, location);
            }
        }
    }

    private void addColoredRectangle(PdfContentByte canvas, PdfCleanUpLocation cleanUpLocation) {
        Rectangle cleanUpRegion = cleanUpLocation.getRegion();

        canvas.saveState();
        canvas.setColorFill(cleanUpLocation.getCleanUpColor());
        canvas.moveTo(cleanUpRegion.getLeft(), cleanUpRegion.getBottom());
        canvas.lineTo(cleanUpRegion.getRight(), cleanUpRegion.getBottom());
        canvas.lineTo(cleanUpRegion.getRight(), cleanUpRegion.getTop());
        canvas.lineTo(cleanUpRegion.getLeft(), cleanUpRegion.getTop());
        canvas.closePath();
        canvas.fill();
        canvas.restoreState();
    }

    private Map<Integer, List<PdfCleanUpLocation>> organizeLocationsByPage(Collection<PdfCleanUpLocation> pdfCleanUpLocations) {
        Map<Integer, List<PdfCleanUpLocation>> organizedLocations = new HashMap<Integer, List<PdfCleanUpLocation>>();

        for (PdfCleanUpLocation location : pdfCleanUpLocations) {
            Integer page = location.getPage();

            if (!organizedLocations.containsKey(page)) {
                organizedLocations.put(page, new ArrayList<PdfCleanUpLocation>());
            }

            organizedLocations.get(page).add(location);
        }

        return organizedLocations;
    }

    private void extractLocationsFromRedactAnnots() {
        this.pdfCleanUpLocations = new HashMap<Integer, List<PdfCleanUpLocation>>();
        PdfReader reader = pdfStamper.getReader();

        for (int i = 1; i <= reader.getNumberOfPages(); ++i) {
            PdfDictionary pageDict = reader.getPageN(i);
            this.pdfCleanUpLocations.put(i, extractLocationsFromRedactAnnots(i, pageDict));
        }
    }

    private List<PdfCleanUpLocation> extractLocationsFromRedactAnnots(int page, PdfDictionary pageDict) {
        List<PdfCleanUpLocation> locations = new ArrayList<PdfCleanUpLocation>();

        if (pageDict.contains(PdfName.ANNOTS)) {
            PdfArray annotsArray = pageDict.getAsArray(PdfName.ANNOTS);

            for (int i = 0; i < annotsArray.size(); ++i) {
                PdfIndirectReference annotIndirRef = annotsArray.getAsIndirectObject(i);
                PdfDictionary annotDict = annotsArray.getAsDict(i);
                PdfName annotSubtype = annotDict.getAsName(PdfName.SUBTYPE);

                if (annotSubtype.equals(PdfName.REDACT)) {
                    saveRedactAnnotIndirRef(page, annotIndirRef.toString());
                    locations.addAll(extractLocationsFromRedactAnnot(page, i, annotDict));
                }
            }
        }

        return locations;
    }

    private void saveRedactAnnotIndirRef(int page, String indRefStr) {
        if (!redactAnnotIndirRefs.containsKey(page)) {
            redactAnnotIndirRefs.put(page, new HashSet<String>());
        }

        redactAnnotIndirRefs.get(page).add(indRefStr);
    }

    private List<PdfCleanUpLocation> extractLocationsFromRedactAnnot(int page, int annotIndex, PdfDictionary annotDict) {
        List<PdfCleanUpLocation> locations = new ArrayList<PdfCleanUpLocation>();
        List<Rectangle> markedRectangles = new ArrayList<Rectangle>();
        PdfArray quadPoints = annotDict.getAsArray(PdfName.QUADPOINTS);

        if (quadPoints.size() != 0) {
            markedRectangles.addAll( quadPointsToRectangles(quadPoints) );
        } else {
            PdfArray annotRect = annotDict.getAsArray(PdfName.RECT);
            markedRectangles.add(new Rectangle(annotRect.getAsNumber(0).floatValue(),
                                               annotRect.getAsNumber(1).floatValue(),
                                               annotRect.getAsNumber(2).floatValue(),
                                               annotRect.getAsNumber(3).floatValue()));
        }

        clippingRects.put(annotIndex, markedRectangles);

        PdfArray ic = annotDict.getAsArray(PdfName.IC);
        BaseColor cleanUpColor = new BaseColor(ic.getAsNumber(0).floatValue(),
                                               ic.getAsNumber(1).floatValue(),
                                               ic.getAsNumber(2).floatValue());


        PdfStream ro = annotDict.getAsStream(PdfName.RO);

        if (ro != null) {
            cleanUpColor = null;
        }

        for (Rectangle rect : markedRectangles) {
            locations.add(new PdfCleanUpLocation(page, rect, cleanUpColor));
        }

        return locations;
    }

    private List<Rectangle> quadPointsToRectangles(PdfArray quadPoints) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();

        for (int i = 0; i < quadPoints.size(); i += 8) {
            rectangles.add(new Rectangle(quadPoints.getAsNumber(i + 4).floatValue(), // QuadPoints have "Z" order
                                         quadPoints.getAsNumber(i + 5).floatValue(),
                                         quadPoints.getAsNumber(i + 2).floatValue(),
                                         quadPoints.getAsNumber(i + 3).floatValue()));
        }

        return rectangles;
    }

    private void deleteRedactAnnots(int pageNum) throws IOException, DocumentException {
        Set<String> indirRefs = redactAnnotIndirRefs.get(pageNum);

        if (indirRefs == null || indirRefs.isEmpty()) {
            return;
        }

        PdfReader reader = pdfStamper.getReader();
        PdfContentByte canvas = pdfStamper.getOverContent(pageNum);
        PdfDictionary pageDict = reader.getPageN(pageNum);
        PdfArray annotsArray = pageDict.getAsArray(PdfName.ANNOTS);

        // j is for access annotRect (i can be decreased, so we need to store additional index,
        // indicating current position in array in case if we don't remove anything
        for (int i = 0, j = 0; i < annotsArray.size(); ++i, ++j) {
            PdfIndirectReference annotIndRef = annotsArray.getAsIndirectObject(i);
            PdfDictionary annotDict = annotsArray.getAsDict(i);

            if (indirRefs.contains(annotIndRef.toString()) || indirRefs.contains(getParentIndRefStr(annotDict))) {
                PdfStream formXObj = annotDict.getAsStream(PdfName.RO);
                PdfString overlayText = annotDict.getAsString(PdfName.OVERLAYTEXT);

                if (formXObj != null) {
                    PdfArray rectArray = annotDict.getAsArray(PdfName.RECT);
                    Rectangle annotRect = new Rectangle(rectArray.getAsNumber(0).floatValue(),
                                                        rectArray.getAsNumber(1).floatValue(),
                                                        rectArray.getAsNumber(2).floatValue(),
                                                        rectArray.getAsNumber(3).floatValue());

                    insertFormXObj(canvas, pageDict, formXObj, clippingRects.get(j), annotRect);
                } else if (overlayText != null && overlayText.toUnicodeString().length() > 0) {
                    drawOverlayText(canvas, clippingRects.get(j), overlayText,
                                    annotDict.getAsString(PdfName.DA),
                                    annotDict.getAsNumber(PdfName.Q),
                                    annotDict.getAsBoolean(PdfName.REPEAT));
                }

                annotsArray.remove(i--); // array size is changed, so we need to decrease i
            }
        }

        if (annotsArray.size() == 0) {
            pageDict.remove(PdfName.ANNOTS);
        }
    }

    private void insertFormXObj(PdfContentByte canvas, PdfDictionary pageDict, PdfStream formXObj, List<Rectangle> clippingRects, Rectangle annotRect) throws IOException {
        PdfName xobjName = generateNameForXObj(pageDict);
        canvas.saveState();

        for (Rectangle rect : clippingRects) {
            canvas.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
        }

        canvas.clip();
        canvas.newPath();

        canvas.addFormXObj(formXObj, xobjName, 1, 0, 0, 1, annotRect.getLeft(), annotRect.getBottom());

        canvas.restoreState();
    }

    private void drawOverlayText(PdfContentByte canvas, List<Rectangle> textRectangles, PdfString overlayText,
                                 PdfString otDA, PdfNumber otQ, PdfBoolean otRepeat) throws DocumentException, IOException {
        ColumnText ct = new ColumnText(canvas);
        ct.setLeading(0, 1.2F);
        ct.setUseAscender(true);

        String otStr = overlayText.toUnicodeString();

        canvas.saveState();
        Map<String, List> parsedDA = parseDAParam(otDA);

        Font font = null;

        if (parsedDA.containsKey(STROKE_COLOR)) {
            List strokeColorArgs = parsedDA.get(STROKE_COLOR);
            setStrokeColor(canvas, strokeColorArgs);
        }

        if (parsedDA.containsKey(FILL_COLOR)) {
            List fillColorArgs = parsedDA.get(FILL_COLOR);
            setFillColor(canvas, fillColorArgs);
        }

        if (parsedDA.containsKey("Tf")) {
            List tfArgs = parsedDA.get("Tf");
            font = retrieveFontFromAcroForm((PdfName) tfArgs.get(0), (PdfNumber) tfArgs.get(1));
        }

        for (Rectangle textRect : textRectangles) {
            ct.setSimpleColumn(textRect);

            if (otQ != null) {
                ct.setAlignment(otQ.intValue());
            }

            Phrase otPhrase;

            if (font != null) {
                otPhrase = new Phrase(otStr, font);
            } else {
                otPhrase = new Phrase(otStr);
            }

            float y = ct.getYLine();

            if (otRepeat != null && otRepeat.booleanValue()) {
                int status = ct.go(true);

                while (!ColumnText.hasMoreText(status)) {
                    otPhrase.add(otStr);
                    ct.setText(otPhrase);
                    ct.setYLine(y);
                    status = ct.go(true);
                }
            }

            ct.setText(otPhrase);
            ct.setYLine(y);
            ct.go();
        }

        canvas.restoreState();
    }

    private Font retrieveFontFromAcroForm(PdfName fontName, PdfNumber size) {
        PdfIndirectReference fontIndirReference = pdfStamper.getReader().getAcroForm().getAsDict(PdfName.DR).getAsDict(PdfName.FONT).getAsIndirectObject(fontName);
        BaseFont bfont = BaseFont.createFont((PRIndirectReference) fontIndirReference);

        return new Font(bfont, size.floatValue());
    }

    Map<String, List> parseDAParam(PdfString DA) throws IOException {
        Map<String, List> commandArguments = new HashMap<String, List>();

        PRTokeniser tokeniser = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(DA.getBytes())));
        List currentArguments = new ArrayList();

        while (tokeniser.nextToken()) {
            if (tokeniser.getTokenType() == PRTokeniser.TokenType.OTHER) {
                String key = tokeniser.getStringValue();

                if (key.equals("RG") || key.equals("G") || key.equals("K")) {
                    key = STROKE_COLOR;
                } else if (key.equals("rg") || key.equals("g") || key.equals("k")) {
                    key = FILL_COLOR;
                }

                commandArguments.put(key, currentArguments);
                currentArguments = new ArrayList();
            } else {
                switch (tokeniser.getTokenType()) {
                    case NUMBER:
                        currentArguments.add(new PdfNumber(tokeniser.getStringValue()));
                        break;

                    case NAME:
                        currentArguments.add(new PdfName(tokeniser.getStringValue()));
                        break;

                    default:
                        currentArguments.add(tokeniser.getStringValue());
                }
            }
        }

        return commandArguments;
    }

    private String getParentIndRefStr(PdfDictionary dict) {
        return dict.getAsIndirectObject(PdfName.PARENT).toString();
    }

    private PdfName generateNameForXObj(PdfDictionary pageDict) {
        PdfDictionary resourcesDict = pageDict.getAsDict(PdfName.RESOURCES);
        PdfDictionary xobjDict = resourcesDict.getAsDict(PdfName.XOBJECT);

        if (xobjDict != null) {
            for (PdfName xobjName : xobjDict.getKeys()) {
                int xobjNum = getXObjNum(xobjName);

                if (currentXObjNum <= xobjNum) {
                    currentXObjNum = xobjNum + 1;
                }
            }
        }

        return new PdfName(XOBJ_NAME_PREFIX + currentXObjNum++);
    }

    private int getXObjNum(PdfName xobjName) {
        String decodedPdfName = PdfName.decodeName(xobjName.toString());

        if (decodedPdfName.lastIndexOf(XOBJ_NAME_PREFIX) == -1) {
            return 0;
        }

        String numStr = decodedPdfName.substring( decodedPdfName.lastIndexOf(XOBJ_NAME_PREFIX) + XOBJ_NAME_PREFIX.length() );
        return Integer.parseInt(numStr);
    }

    private void setFillColor(PdfContentByte canvas, List fillColorArgs) {
        switch (fillColorArgs.size()) {
            case 1:
                canvas.setGrayFill(((PdfNumber) fillColorArgs.get(0)).floatValue());
                break;

            case 3:
                canvas.setRGBColorFillF(((PdfNumber) fillColorArgs.get(0)).floatValue(),
                                        ((PdfNumber) fillColorArgs.get(1)).floatValue(),
                                        ((PdfNumber) fillColorArgs.get(2)).floatValue());
                break;

            case 4:
                canvas.setCMYKColorFillF(((PdfNumber) fillColorArgs.get(0)).floatValue(),
                                         ((PdfNumber) fillColorArgs.get(1)).floatValue(),
                                         ((PdfNumber) fillColorArgs.get(2)).floatValue(),
                                         ((PdfNumber) fillColorArgs.get(3)).floatValue());
                break;

        }
    }

    private void setStrokeColor(PdfContentByte canvas, List strokeColorArgs) {
        switch (strokeColorArgs.size()) {
            case 1:
                canvas.setGrayStroke(((PdfNumber) strokeColorArgs.get(0)).floatValue());
                break;

            case 3:
                canvas.setRGBColorStrokeF(((PdfNumber) strokeColorArgs.get(0)).floatValue(),
                                          ((PdfNumber) strokeColorArgs.get(1)).floatValue(),
                                          ((PdfNumber) strokeColorArgs.get(2)).floatValue());
                break;

            case 4:
                canvas.setCMYKColorFillF(((PdfNumber) strokeColorArgs.get(0)).floatValue(),
                                         ((PdfNumber) strokeColorArgs.get(1)).floatValue(),
                                         ((PdfNumber) strokeColorArgs.get(2)).floatValue(),
                                         ((PdfNumber) strokeColorArgs.get(3)).floatValue());
                break;

        }
    }
}
