package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.List;
import java.util.Queue;

class PdfCleanUpRenderListener implements ExtRenderListener {

    private static final Color CLEANED_AREA_FILL_COLOR = Color.WHITE;

    private PdfStamper pdfStamper;
    private List<PdfCleanUpRegionFilter> filters;
    private List<PdfCleanUpContentChunk> chunks = new ArrayList<PdfCleanUpContentChunk>();
    private Stack<PdfCleanUpContext> contextStack = new Stack<PdfCleanUpContext>();
    private int strNumber = 1; // Represents ordinal number of string under processing. Needed for processing TJ operator.

    private Path unfilteredCurrentPath = new Path(); // Represents current path as if there were no segments to cut
    private Path currentStrokePath = new Path(); // Represents actual current path ("actual" means that it is filtered current path)

    /**
     * Represents actual current path to be filled. In general case in context of cleanup tool it completely differs from
     * the current path with implicitly closed subpaths.
     */
    private Path currentFillPath = new Path();

    private boolean clipPath = false;

    public PdfCleanUpRenderListener(PdfStamper pdfStamper, List<PdfCleanUpRegionFilter> filters) {
        this.pdfStamper = pdfStamper;
        this.filters = filters;
    }

    public void renderText(TextRenderInfo renderInfo) {
        if (renderInfo.getPdfString().toUnicodeString().length() == 0) {
            return;
        }

        for (TextRenderInfo ri : renderInfo.getCharacterRenderInfos()) {
            boolean textIsInsideRegion = textIsInsideRegion(ri);
            LineSegment baseline = ri.getUnscaledBaseline();

            chunks.add(new PdfCleanUpContentChunk.Text(ri.getPdfString(), baseline.getStartPoint(), baseline.getEndPoint(), !textIsInsideRegion, strNumber));
        }

        ++strNumber;
    }

    public void renderImage(ImageRenderInfo renderInfo) {
        List<Rectangle> areasToBeCleaned = getImageAreasToBeCleaned(renderInfo);

        if (areasToBeCleaned == null) {
            chunks.add(new PdfCleanUpContentChunk.Image(false, null));
        } else {
            try {
                PdfImageObject pdfImage = renderInfo.getImage();
                byte[] imageBytes = processImage(pdfImage.getImageAsBytes(), areasToBeCleaned);

                if (renderInfo.getRef() == null && pdfImage != null) { // true => inline image
                    PdfDictionary dict = pdfImage.getDictionary();
                    PdfObject imageMask = dict.get(PdfName.IMAGEMASK);
                    Image image = Image.getInstance(imageBytes);

                    if (imageMask == null) {
                        imageMask = dict.get(PdfName.IM);
                    }

                    if (imageMask != null && imageMask.equals(PdfBoolean.PDFTRUE)) {
                        image.makeMask();
                    }

                    PdfContentByte canvas = getContext().getCanvas();
                    canvas.addImage(image, 1, 0, 0, 1, 0, 0, true);
                } else if (pdfImage != null && imageBytes != pdfImage.getImageAsBytes()) {
                    chunks.add(new PdfCleanUpContentChunk.Image(true, imageBytes));
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

    public void modifyPath(PathConstructionRenderInfo renderInfo) {
        List<Float> segmentData = renderInfo.getSegmentData();

        switch (renderInfo.getOperation()) {
            case PathConstructionRenderInfo.MOVETO:
                unfilteredCurrentPath.moveTo(segmentData.get(0), segmentData.get(1));
                break;

            case PathConstructionRenderInfo.LINETO:
                unfilteredCurrentPath.lineTo(segmentData.get(0), segmentData.get(1));
                break;

            case PathConstructionRenderInfo.CURVE_123:
                unfilteredCurrentPath.curveTo(segmentData.get(0), segmentData.get(1), segmentData.get(2),
                        segmentData.get(3), segmentData.get(4), segmentData.get(5));
                break;

            case PathConstructionRenderInfo.CURVE_23:
                unfilteredCurrentPath.curveTo(segmentData.get(0), segmentData.get(1), segmentData.get(2), segmentData.get(3));
                break;

            case PathConstructionRenderInfo.CURVE_13:
                unfilteredCurrentPath.curveFromTo(segmentData.get(0), segmentData.get(1), segmentData.get(2), segmentData.get(3));
                break;

            case PathConstructionRenderInfo.CLOSE:
                unfilteredCurrentPath.closeSubpath();
                break;

            case PathConstructionRenderInfo.RECT:
                unfilteredCurrentPath.rectangle(segmentData.get(0), segmentData.get(1), segmentData.get(2), segmentData.get(3));
                break;
        }
    }

    public Path renderPath(PathPaintingRenderInfo renderInfo) {
        if ((renderInfo.getOperation() & PathPaintingRenderInfo.STROKE) != 0) {
            currentStrokePath = filterCurrentPath(renderInfo.getCtm(), true);
        }

        if ((renderInfo.getOperation() & PathPaintingRenderInfo.FILL) != 0 || clipPath) {
            currentFillPath = filterCurrentPath(renderInfo.getCtm(), false);
        }

        unfilteredCurrentPath = new Path();
        return currentFillPath;
    }

    public void clipPath(byte rule) {
        clipPath = true;
    }

    public void setClipped(boolean clipped) {
        clipPath = clipped;
    }

    public boolean isClipped() {
        return clipPath;
    }

    public Path getCurrentStrokePath() {
        return currentStrokePath;
    }

    public Path getCurrentFillPath() {
        return currentFillPath;
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

    public void clearChunks() {
        chunks.clear();
        strNumber = 1;
    }

    private boolean textIsInsideRegion(TextRenderInfo renderInfo) {
        for (PdfCleanUpRegionFilter filter : filters) {
            if (filter.allowText(renderInfo)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return null if the image is not allowed (either it is fully covered or ctm == null).
     * List of covered image areas otherwise.
     */
    private List<Rectangle> getImageAreasToBeCleaned(ImageRenderInfo renderInfo) {
        List<Rectangle> areasToBeCleaned = new ArrayList<Rectangle>();

        for (PdfCleanUpRegionFilter filter : filters) {
            PdfCleanUpCoveredArea coveredArea = filter.intersection(renderInfo);

            if (coveredArea == null || coveredArea.matchesObjRect()) {
                return null;
            } else if (coveredArea.getRect() != null) {
                areasToBeCleaned.add( coveredArea.getRect() );
            }
        }

        return areasToBeCleaned;
    }

    private byte[] processImage(byte[] imageBytes, List<Rectangle> areasToBeCleaned) {
        if (areasToBeCleaned.isEmpty()) {
            return imageBytes;
        }

        try {
            BufferedImage image = Imaging.getBufferedImage(imageBytes);
            ImageInfo imageInfo = Imaging.getImageInfo(imageBytes);
            cleanImage(image, areasToBeCleaned);

            // Apache can only read JPEG, so we should use awt for writing in this format
            if (imageInfo.getFormat() == ImageFormats.JPEG) {
                return getJPGBytes(image);
            } else {
                Map<String, Object> params = new HashMap<String, Object>();

                if (imageInfo.getFormat() == ImageFormats.TIFF) {
                    params.put(ImagingConstants.PARAM_KEY_COMPRESSION, TiffConstants.TIFF_COMPRESSION_LZW);
                }

                return Imaging.writeImageToBytes(image, imageInfo.getFormat(), params);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanImage(BufferedImage image, List<Rectangle> areasToBeCleaned) {
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(CLEANED_AREA_FILL_COLOR);

        // A rectangle in the areasToBeCleaned list is treated to be in standard [0, 1]x[0,1] image space
        // (y varies from bottom to top and x from left to right), so we should scale the rectangle and also
        // invert and shear the y axe
        for (Rectangle rect : areasToBeCleaned) {
            int scaledBottomY = (int) Math.ceil(rect.getBottom() * image.getHeight());
            int scaledTopY = (int) Math.floor(rect.getTop() * image.getHeight());

            int x = (int) Math.ceil(rect.getLeft() * image.getWidth());
            int y = scaledTopY * -1 + image.getHeight();
            int width = (int) Math.floor(rect.getRight() * image.getWidth()) - x;
            int height = scaledTopY - scaledBottomY;

            graphics.fillRect(x, y, width, height);
        }

        graphics.dispose();
    }

    private byte[] getJPGBytes(BufferedImage image) {
        ByteArrayOutputStream outputStream = null;

        try {
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpgWriteParam.setCompressionQuality(1.0f);

            outputStream = new ByteArrayOutputStream();
            jpgWriter.setOutput(new MemoryCacheImageOutputStream((outputStream)));
            IIOImage outputImage = new IIOImage(image, null, null);

            jpgWriter.write(null, outputImage, jpgWriteParam);
            jpgWriter.dispose();
            outputStream.flush();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeOutputStream(outputStream);
        }
    }

    private Path filterCurrentPath(Matrix ctm, boolean isContour) {
        Queue<Subpath> filteredSubpaths = new ArrayDeque<Subpath>(unfilteredCurrentPath.getSubpaths());

        for (PdfCleanUpRegionFilter filter : filters) {
            int queueSize = filteredSubpaths.size();

            while (queueSize-- != 0) {
                Subpath subpath = filteredSubpaths.poll();

                if (!isContour) {
                    subpath = new Subpath(subpath);
                    subpath.setClosed(true);
                }

                List<Subpath> filteredSubpath = filter.filterSubpath(subpath, ctm, isContour);
                filteredSubpaths.addAll(filteredSubpath);
            }
        }

        return new Path(filteredSubpaths);
    }

    private void closeOutputStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
