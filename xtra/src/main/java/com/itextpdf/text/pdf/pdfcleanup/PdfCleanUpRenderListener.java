/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
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
    private Deque<PdfCleanUpContext> contextStack = new ArrayDeque<PdfCleanUpContext>();
    private int strNumber = 1; // Represents ordinal number of string under processing. Needed for processing TJ operator.

    // Represents current path as if there were no segments to cut
    private Path unfilteredCurrentPath = new Path();

    // Represents actual current path to be stroked ("actual" means that it is filtered current path)
    private Path currentStrokePath = new Path();

    // Represents actual current path to be filled.
    private Path currentFillPath = new Path();

    // Represents new clipping path
    // In general case, after redaction, it isn't the same as the currentFillPath because of the
    // possibility to apply different filling rules for clipping and filling.
    private Path newClippingPath;

    private boolean clipPath;
    private int clippingRule;

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
        boolean stroke = (renderInfo.getOperation() & PathPaintingRenderInfo.STROKE) != 0;
        boolean fill = (renderInfo.getOperation() & PathPaintingRenderInfo.FILL) != 0;

        if (stroke) {
            currentStrokePath = filterCurrentPath(renderInfo.getCtm(), true, -1);
        }

        if (fill) {
            currentFillPath = filterCurrentPath(renderInfo.getCtm(), false, renderInfo.getRule());
        }

        if (clipPath) {
            if (fill && renderInfo.getRule() == clippingRule) {
                newClippingPath = currentFillPath;
            } else {
                newClippingPath = filterCurrentPath(renderInfo.getCtm(), false, clippingRule);
            }
        }

        unfilteredCurrentPath = new Path();
        return newClippingPath;
    }

    public void clipPath(int rule) {
        clipPath = true;
        clippingRule = rule;
    }

    public boolean isClipped() {
        return clipPath;
    }

    public void setClipped(boolean clipPath) {
        this.clipPath = clipPath;
    }

    public int getClippingRule() {
        return clippingRule;
    }

    public Path getCurrentStrokePath() {
        return currentStrokePath;
    }

    public Path getCurrentFillPath() {
        return currentFillPath;
    }

    public Path getNewClipPath() {
        return newClippingPath;
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

    /**
     * @param fillingRule If the path is contour, pass any value.
     */
    private Path filterCurrentPath(Matrix ctm, boolean isContour, int fillingRule) {
        Path path = new Path(unfilteredCurrentPath.getSubpaths());

        if (isContour) {
            // TODO: This block is going to be replaced in the future
            path.replaceCloseWithLine();
        } else {
            path.closeAllSubpaths();
        }

        for (PdfCleanUpRegionFilter filter : filters) {
            path = filter.filterPath(path, ctm, isContour, fillingRule);
        }

        return path;
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
