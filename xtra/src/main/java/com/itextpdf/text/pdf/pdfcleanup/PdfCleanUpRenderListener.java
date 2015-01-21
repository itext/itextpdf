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

public class PdfCleanUpRenderListener implements RenderListener {

    private static final Color CLEANED_AREA_FILL_COLOR = Color.WHITE;

    private List<PdfCleanUpRegionFilter> filters;
    protected List<PdfCleanUpContentChunk> chunks = new ArrayList<PdfCleanUpContentChunk>();
    private PdfStamper pdfStamper;
    private Stack<PdfCleanUpContext> contextStack = new Stack<PdfCleanUpContext>();

    public PdfCleanUpRenderListener(PdfStamper pdfStamper, List<PdfCleanUpRegionFilter> filters) {
        this.pdfStamper = pdfStamper;
        this.filters = filters;
    }

    public void renderText(TextRenderInfo renderInfo) {
        PdfCleanUpContext context = contextStack.peek();

        for (TextRenderInfo ri : renderInfo.getCharacterRenderInfos()) {
            boolean textIsInsideRegion = textIsInsideRegion(ri);
            LineSegment baseline = ri.getBaseline();
            float chunkSize = context.getFontSize() * context.getTextMatrixElement22();

            chunks.add(new PdfCleanUpContentChunk(ri.getPdfString(), baseline.getStartPoint(), baseline.getEndPoint(), chunkSize, !textIsInsideRegion));
        }
    }

    public void renderImage(ImageRenderInfo renderInfo) {
        List<Rectangle> areasToBeCleaned = getImageAreasToBeCleaned(renderInfo);

        if (areasToBeCleaned == null) {
            chunks.add(new PdfCleanUpContentChunk(false, null));
        } else {
            try {
                PdfImageObject pdfImage = renderInfo.getImage();
                byte[] imageBytes = processImage(pdfImage.getImageAsBytes(), areasToBeCleaned);

                if (renderInfo.getRef() == null && pdfImage != null) { // true => inline image
                    Image image = Image.getInstance(imageBytes);

                    PdfDictionary dict = pdfImage.getDictionary();
                    PdfObject imageMask = dict.get(PdfName.IMAGEMASK);
                    if (imageMask == null)
                        imageMask = dict.get(PdfName.IM);
                    if (imageMask != null && imageMask.equals(PdfBoolean.PDFTRUE))
                        image.makeMask();
                    PdfContentByte canvas = getContext().getCanvas();
                    canvas.addImage(image, 1, 0, 0, 1, 0, 0, true);
                } else if (renderInfo.getRef() != null && pdfImage != null && imageBytes != pdfImage.getImageAsBytes()) {
                    chunks.add(new PdfCleanUpContentChunk(true, imageBytes));
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

    private boolean textIsInsideRegion(TextRenderInfo renderInfo) {
        for (PdfCleanUpRegionFilter filter : filters) {
            if (filter.allowText(renderInfo)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return null if the image is not allowed (either it is fully covered or ctm == null or something else...).
     *         List of covered image areas otherwise.
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
            cleanImage(image, areasToBeCleaned);

            ImageInfo imageInfo = Imaging.getImageInfo(imageBytes);
            Map<String, Object> params = new HashMap<String, Object>();

            if (imageInfo.getFormat().getName().equals(ImageFormats.TIFF.getName())) {
                params.put(ImagingConstants.PARAM_KEY_COMPRESSION, getTiffCompressionAlgoConst(imageInfo.getCompressionAlgorithm()));
            }

            // Apache can only read JPEG, so we should use awt for writing in this format
            if (imageInfo.getFormat().getName().equals(ImageFormats.JPEG.getName())) {
                return getJPGBytes(image);
            } else {
                return Imaging.writeImageToBytes(image, imageInfo.getFormat(), params);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void cleanImage(BufferedImage image, List<Rectangle> areasToBeCleaned) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(CLEANED_AREA_FILL_COLOR);

        for (Rectangle rect : areasToBeCleaned) {
            int x = (int) Math.ceil(rect.getLeft());
            int y = (int) Math.ceil(rect.getTop());
            int width = (int) Math.floor(rect.getRight()) - x;
            int height = (int) Math.floor(rect.getBottom()) - y;

            graphics.fillRect(x, y, width, height);
        }
    }

    private int getTiffCompressionAlgoConst(ImageInfo.CompressionAlgorithm compressionAlgorithm) {
        switch (compressionAlgorithm) {
            case NONE:
                return TiffConstants.TIFF_COMPRESSION_UNCOMPRESSED;

            case LZW:
                return TiffConstants.TIFF_COMPRESSION_LZW;
        }

        throw new RuntimeException("Unknown compression");
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
