package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PdfCleanUpRenderListener implements RenderListener {

    private List<PdfCleanUpRegionFilter> filters;
    protected List<PdfCleanUpContentChunk> chunks = new ArrayList<PdfCleanUpContentChunk>();
    private PdfStamper pdfStamper;
    private Stack<PdfCleanUpContext> contextStack = new Stack<PdfCleanUpContext>();

    public PdfCleanUpRenderListener(PdfStamper pdfStamper, List<PdfCleanUpRegionFilter> filters) {
        this.pdfStamper = pdfStamper;
        this.filters = filters;
    }

    public void renderText(TextRenderInfo renderInfo) {
        for (TextRenderInfo ri : renderInfo.getCharacterRenderInfos()) {
            boolean chunkIsInsideRegion = chunkIsInsideRegion(ri);
            LineSegment baseline = ri.getBaseline();
            LineSegment ascent = ri.getAscentLine();
            LineSegment descent = ri.getDescentLine();
            float y1 = descent.getStartPoint().get(1);
            float y2 = ascent.getStartPoint().get(1);
            chunks.add(new PdfCleanUpContentChunk(ri.getPdfString(), baseline.getStartPoint(), baseline.getEndPoint(), Math.abs(y2 - y1), !chunkIsInsideRegion));
        }
    }

    public void renderImage(ImageRenderInfo renderInfo) {
        boolean chunkIsInsideRegion = chunkIsInsideRegion(renderInfo);
        if (chunkIsInsideRegion) {
            chunks.add(new PdfCleanUpContentChunk(false));
        } else {
            try {
                PdfImageObject pdfImage = renderInfo.getImage();
                if (renderInfo.getRef() == null && pdfImage != null &&
                        pdfImage.getDictionary() != null) {
                    Image image = Image.getInstance(pdfImage.getImageAsBytes());
                    PdfDictionary dict = pdfImage.getDictionary();
                    PdfObject imageMask = dict.get(PdfName.IMAGEMASK);
                    if (imageMask == null)
                        imageMask = dict.get(PdfName.IM);
                    if (imageMask != null && imageMask.equals(PdfBoolean.PDFTRUE))
                        image.makeMask();
                    PdfContentByte canvas = getContext().getCanvas();
                    canvas.addImage(image, 1, 0, 0, 1, 0, 0, true);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void beginTextBlock() {
    }

    public void endTextBlock() {
    }

    public List<PdfCleanUpContentChunk> getChunks() {
        return chunks;
    }

    public PdfCleanUpContext getContext() {
        return contextStack.peek();
    }

    public void registerNewContext(PdfDictionary resources, PdfContentByte canvas) {
        canvas = canvas == null ? new PdfContentByte(pdfStamper.getWriter()) : canvas;
        contextStack.push(new PdfCleanUpContext(resources, canvas));
    }

    public void popContext() {
        contextStack.pop();
    }

    private boolean chunkIsInsideRegion(Object renderInfo) {
        boolean chunkIsInsideRegion = false;
        for (PdfCleanUpRegionFilter filter : filters) {
            if (filter.allowObject(renderInfo)) {
                chunkIsInsideRegion = true;
                break;
            }
        }
        return chunkIsInsideRegion;
    }

    static class PdfCleanUpContext {
        protected PdfDictionary resources = null;
        protected PdfContentByte canvas = null;
        protected PdfNumber charSpacing = new PdfNumber(0);

        public PdfCleanUpContext(PdfDictionary resources, PdfContentByte canvas) {
            this.resources = resources;
            this.canvas = canvas;
        }

        public PdfDictionary getResources() {
            return resources;
        }

        public PdfContentByte getCanvas() {
            return canvas;
        }

        public PdfNumber getCharSpacing() {
            return charSpacing;
        }

        public void setCharSpacing(PdfNumber charSpacing) {
            this.charSpacing = charSpacing;
        }
    }


}
