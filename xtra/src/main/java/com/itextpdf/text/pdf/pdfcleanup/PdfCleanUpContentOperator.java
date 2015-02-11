package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.ContentOperator;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

class PdfCleanUpContentOperator implements ContentOperator {

    static private final byte[] TStar = DocWriter.getISOBytes("T*\n");
    static private final byte[] Tw = DocWriter.getISOBytes(" Tw ");
    static private final byte[] TcTStar = DocWriter.getISOBytes(" Tc T*\n");
    static private final byte[] TJ = DocWriter.getISOBytes("] TJ\n");
    static private final byte[] Tc = DocWriter.getISOBytes(" Tc\n");

    static private final Set<String> textShowingOperators = new HashSet<String>() {{
        add("TJ");
        add("Tj");
        add("'");
        add("\"");
    }};

    protected PdfCleanUpRenderListener cleanUpStrategy;
    protected ContentOperator originalContentOperator;

    public static void populateOperators(PdfContentStreamProcessor contentProcessor,
                                         PdfCleanUpRenderListener pdfCleanUpRenderListener) {
        String[] operators = {
                PdfContentStreamProcessor.DEFAULTOPERATOR, "q", "Q", "g", "G", "rg", "RG", "k", "K",
                "cs", "CS", "sc", "SC", "scn", "SCN", "cm", "gs", "Tc", "Tw", "Tz", "TL", "Tf", "Tr",
                "Ts", "BT", "ET", "BMC", "BDC", "EMC", "Td", "TD", "Tm", "T*", "Tj", "'", "\"", "TJ", "Do"
        };

        for (String operator : operators) {
            PdfCleanUpContentOperator contentOperator = new PdfCleanUpContentOperator(pdfCleanUpRenderListener);
            contentOperator.originalContentOperator = contentProcessor.registerContentOperator(operator, contentOperator);
        }
    }

    public PdfCleanUpContentOperator(PdfCleanUpRenderListener cleanUpStrategy) {
        this.cleanUpStrategy = cleanUpStrategy;
    }

    public void invoke(PdfContentStreamProcessor pdfContentStreamProcessor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
        String operatorStr = operator.toString();
        PdfContentByte canvas = cleanUpStrategy.getContext().getCanvas();
        PRStream xFormStream = null;

        // key - number of a string in the TJ operator, value - number following the string; the first number without string (if it's presented) is stored under 0.
        // BE AWARE: zero-length strings are ignored!!!
        Map<Integer, Float> structuredTJoperands = null;

        if ("Do".equals(operatorStr)) {
            if (operands.size() == 2 && operands.get(0).isName()) {
                PdfDictionary xObjResources = cleanUpStrategy.getContext().getResources().getAsDict(PdfName.XOBJECT);

                if (xObjResources != null) {
                    PdfStream xObj = xObjResources.getAsStream((PdfName) operands.get(0));

                    if (xObj instanceof PRStream && xObj.getAsName(PdfName.SUBTYPE) != null &&
                            xObj.getAsName(PdfName.SUBTYPE).compareTo(PdfName.FORM) == 0) {
                        xFormStream = (PRStream) xObj;
                        cleanUpStrategy.registerNewContext(xObj.getAsDict(PdfName.RESOURCES), null);
                    }
                }
            }
        }

        originalContentOperator.invoke(pdfContentStreamProcessor, operator, operands);
        List<PdfCleanUpContentChunk> chunks = cleanUpStrategy.getChunks();
        boolean disableOutput = false;

        if (xFormStream != null) {
            xFormStream.setData(cleanUpStrategy.getContext().getCanvas().toPdf(cleanUpStrategy.getContext().getCanvas().getPdfWriter()));
            cleanUpStrategy.popContext();
            canvas = cleanUpStrategy.getContext().getCanvas();
        }

        if ("Do".equals(operatorStr)) {
            if (chunks.size() > 0 && chunks.get(0).isImage()) {
                PdfCleanUpContentChunk chunk = chunks.get(0);

                if (chunk.isVisible()) {
                    PdfDictionary xObjResources = cleanUpStrategy.getContext().getResources().getAsDict(PdfName.XOBJECT);
                    PRStream imageStream = (PRStream) xObjResources.getAsStream((PdfName) operands.get(0));
                    updateImage(imageStream, chunk.getNewImageData());
                } else {
                    disableOutput = true;
                }
            }
        } else if ("q".equals(operatorStr)) {
            cleanUpStrategy.getContext().saveGraphicsState();
        } else if ("Q".equals(operatorStr)) {
            cleanUpStrategy.getContext().restoreGraphicsState();
        } else if ("Tf".equals(operatorStr)) {
            cleanUpStrategy.getContext().setFontSize(((PdfNumber) operands.get(1)).floatValue());
        } else if ("Tc".equals(operatorStr)) {
            cleanUpStrategy.getContext().setCharacterSpacing(((PdfNumber) operands.get(0)).floatValue());
        } else if ("Tw".equals(operatorStr)) {
            cleanUpStrategy.getContext().setWordSpacing(((PdfNumber) operands.get(0)).floatValue());
        } else if ("Tz".equals(operatorStr)) {
            cleanUpStrategy.getContext().setHorizontalScaling(((PdfNumber) operands.get(0)).floatValue());
        } else if (textShowingOperators.contains(operatorStr) && !allChunksAreVisible(cleanUpStrategy.getChunks())) {
            disableOutput = true;

            if ("'".equals(operatorStr)) {
                canvas.getInternalBuffer().append(TStar);
            } else if ("\"".equals(operatorStr)) {
                operands.get(0).toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
                canvas.getInternalBuffer().append(Tw);

                operands.get(1).toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
                canvas.getInternalBuffer().append(TcTStar);

                cleanUpStrategy.getContext().setCharacterSpacing(((PdfNumber) operands.get(1)).floatValue());
            } else if ("TJ".equals(operatorStr)) {
                structuredTJoperands = structureTJarray((PdfArray) operands.get(0));
            }

            renderChunks(structuredTJoperands, chunks, canvas);
        } else if ("\"".equals(operatorStr)) {
            cleanUpStrategy.getContext().setCharacterSpacing(((PdfNumber) operands.get(1)).floatValue());
        }

        if (!disableOutput) {
            int index = 0;

            for (PdfObject o : operands) {
                toPdf(o, canvas.getPdfWriter(), canvas.getInternalBuffer());
                canvas.getInternalBuffer().append(operands.size() > ++index ? (byte) ' ' : (byte) '\n');
            }
        }

        cleanUpStrategy.clearChunks();
    }

    private boolean allChunksAreVisible(List<PdfCleanUpContentChunk> chunks) {
        for (PdfCleanUpContentChunk chunk : chunks) {
            if (!chunk.isVisible()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Overriding standard PdfObject.toPdf because we need sorted PdfDictionaries.
     */
    static private void toPdf(PdfObject object, PdfWriter writer, OutputStream os) throws IOException {
        if (object instanceof PdfDictionary) {
            os.write('<');
            os.write('<');

            List<PdfName> keys = new ArrayList<PdfName>(((PdfDictionary) object).getKeys());
            Collections.sort(keys);

            for (PdfName key : keys) {
                toPdf(key, writer, os);
                PdfObject value = ((PdfDictionary) object).get(key);
                int type = value.type();

                if (type != PdfObject.ARRAY && type != PdfObject.DICTIONARY && type != PdfObject.NAME &&
                        type != PdfObject.STRING) {
                    os.write(' ');
                }

                toPdf(value, writer, os);
            }

            os.write('>');
            os.write('>');
        } else {
            object.toPdf(writer, os);
        }
    }

    private Map<Integer, Float> structureTJarray(PdfArray array) {
        Map<Integer, Float> structuredTJoperands = new HashMap<Integer, Float>();

        if (array.size() == 0) {
            return structuredTJoperands;
        }

        Integer previousStrNum = 0;
        structuredTJoperands.put(previousStrNum, 0f);

        for (int i = 0; i < array.size(); ++i) {
            PdfObject currentObj = array.getPdfObject(i);

            if (currentObj instanceof PdfString && ((PdfString) currentObj).toUnicodeString().length() > 0) {
                ++previousStrNum;
                structuredTJoperands.put(previousStrNum, 0f);
            } else {
                Float oldOffset = structuredTJoperands.get(previousStrNum);
                structuredTJoperands.put(previousStrNum, oldOffset + ((PdfNumber) currentObj).floatValue());
            }
        }

        return structuredTJoperands;
    }

    private void renderChunks(Map<Integer, Float> structuredTJoperands, List<PdfCleanUpContentChunk> chunks, PdfContentByte canvas) throws IOException {
        canvas.setCharacterSpacing(0);
        canvas.setWordSpacing(0);
        canvas.getInternalBuffer().append((byte) '[');

        float characterSpacing = cleanUpStrategy.getContext().getCharacterSpacing();
        float convertedCharacterSpacing = -characterSpacing * 1000f / cleanUpStrategy.getContext().getFontSize();

        float wordSpacing = cleanUpStrategy.getContext().getWordSpacing();
        float convertedWordSpacing = -wordSpacing * 1000f / cleanUpStrategy.getContext().getFontSize();

        float shift = structuredTJoperands != null ? structuredTJoperands.get(0) : 0;
        PdfCleanUpContentChunk prevChunk = null;

        for (PdfCleanUpContentChunk chunk : chunks) {
            if (prevChunk != null && prevChunk.getNumOfStrChunkBelongsTo() != chunk.getNumOfStrChunkBelongsTo() &&
                    structuredTJoperands != null) {
                shift += structuredTJoperands.get(prevChunk.getNumOfStrChunkBelongsTo());
            }

            if (chunk.isVisible()) {
                if (Float.compare(shift, 0.0f) != 0 && Float.compare(shift, -0.0f) != 0) {
                    canvas.getInternalBuffer().append(shift).append(' ');
                }

                chunk.getText().toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
                canvas.getInternalBuffer().append(' ');

                shift = convertedCharacterSpacing + (isSpace(chunk) ? convertedWordSpacing : 0);
            } else {
                float unscaledChunkWidth = getUnscaledChunkWidth(chunk);
                shift += unscaledChunkWidth;
            }

            prevChunk = chunk;
        }

        if (Float.compare(shift, 0.0f) != 0 && Float.compare(shift, -0.0f) != 0) {
            canvas.getInternalBuffer().append(shift);
        }

        canvas.getInternalBuffer().append(TJ);

        if (Float.compare(characterSpacing, 0.0f) != 0 && Float.compare(characterSpacing, -0.0f) != 0) {
            new PdfNumber(characterSpacing).toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
            canvas.getInternalBuffer().append(Tc);
        }

        if (Float.compare(wordSpacing, 0.0f) != 0 && Float.compare(wordSpacing, -0.0f) != 0) {
            new PdfNumber(wordSpacing).toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
            canvas.getInternalBuffer().append(Tw);
        }
    }

    /**
     * We get into this method when the current chunk is not visible.
     * Here we are calculating Tj coefficient for previous chunk
     * For details see PDF spec., Text Space Details, formula for "tx" coefficient
     * and TextRenderInfo class (getUnscaledBaseline)
     */
    private float getUnscaledChunkWidth(PdfCleanUpContentChunk chunk) {
        PdfCleanUpContext context = cleanUpStrategy.getContext();
        float fontSize = context.getFontSize();
        float characterSpacing = context.getCharacterSpacing();
        float wordSpacing = context.getWordSpacing();
        float horizontalScaling = context.getHorizontalScaling();

        // we should multiply by 100 because iText stores horizontal scaling as the value in [0, 1] interval;
        // also we need to add character and word spaces because TextRenderInfo class truncates them from the end of the string
        // (single character string in our case is also truncated)
        float scaledChunkWidth = (chunk.getEndX() - chunk.getStartX()) * 100f +
                (characterSpacing + (isSpace(chunk) ? wordSpacing : 0)) * horizontalScaling;

        return -scaledChunkWidth * 1000f / (horizontalScaling * fontSize);
    }

    private boolean isSpace(PdfCleanUpContentChunk chunk) {
        return chunk.getText().toUnicodeString().equals(" ");
    }

    private void updateImage(PRStream imageStream, byte[] newData) throws BadElementException, IOException, BadPdfFormatException {
        PdfImage image = new PdfImage(Image.getInstance(newData), "", null);

        if (imageStream.contains(PdfName.SMASK)) {
            image.put(PdfName.SMASK, imageStream.get(PdfName.SMASK));
        }

        if (imageStream.contains(PdfName.MASK)) {
            image.put(PdfName.MASK, imageStream.get(PdfName.MASK));
        }

        if (imageStream.contains(PdfName.SMASKINDATA)) {
            image.put(PdfName.SMASKINDATA, imageStream.get(PdfName.SMASKINDATA));
        }

        imageStream.clear();
        imageStream.putAll(image);
        imageStream.setDataRaw(image.getBytes());
    }
}
