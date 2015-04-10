/*
 * $Id: PdfXConformanceImp.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ExtendedColor;
import com.itextpdf.text.pdf.PatternColor;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfXConformanceException;
import com.itextpdf.text.pdf.ShadingColor;
import com.itextpdf.text.pdf.SpotColor;
import com.itextpdf.text.pdf.interfaces.PdfXConformance;

public class PdfXConformanceImp implements PdfXConformance {

    /**
     * The value indicating if the PDF has to be in conformance with PDF/X.
     */
    protected int pdfxConformance = PdfWriter.PDFXNONE;
    protected PdfWriter writer;

    public PdfXConformanceImp(PdfWriter writer) {
        this.writer = writer;
    }

    /**
     * @see com.itextpdf.text.pdf.interfaces.PdfXConformance#setPDFXConformance(int)
     */
    public void setPDFXConformance(int pdfxConformance) {
        this.pdfxConformance = pdfxConformance;
    }

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfXConformance#getPDFXConformance()
	 */
	public int getPDFXConformance() {
		return pdfxConformance;
	}

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfIsoConformance#isPdfIso()
	 */
    public boolean isPdfIso() {
        return isPdfX();
    }

    /**
     * Checks if the PDF/X Conformance is necessary.
     * @return true if the PDF has to be in conformance with any of the PDF/X specifications
     */
    public boolean isPdfX() {
    	return pdfxConformance != PdfWriter.PDFXNONE;
    }
    /**
     * Checks if the PDF has to be in conformance with PDF/X-1a:2001
     * @return true of the PDF has to be in conformance with PDF/X-1a:2001
     */
    public boolean isPdfX1A2001() {
    	return pdfxConformance == PdfWriter.PDFX1A2001;
    }
    /**
     * Checks if the PDF has to be in conformance with PDF/X-3:2002
     * @return true of the PDF has to be in conformance with PDF/X-3:2002
     */
    public boolean isPdfX32002() {
    	return pdfxConformance == PdfWriter.PDFX32002;
    }

    /**
	 * Business logic that checks if a certain object is in conformance with PDF/X.
     * @param key		the type of PDF ISO conformance that has to be checked
     * @param obj1		the object that is checked for conformance
     */
    public void checkPdfIsoConformance(int key, Object obj1) {
        if (writer == null || !writer.isPdfX())
            return;
        int conf = writer.getPDFXConformance();
        switch (key) {
            case PdfIsoKeys.PDFISOKEY_COLOR:
                switch (conf) {
                    case PdfWriter.PDFX1A2001:
                        if (obj1 instanceof ExtendedColor) {
                            ExtendedColor ec = (ExtendedColor)obj1;
                            switch (ec.getType()) {
                                case ExtendedColor.TYPE_CMYK:
                                case ExtendedColor.TYPE_GRAY:
                                    return;
                                case ExtendedColor.TYPE_RGB:
                                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                                case ExtendedColor.TYPE_SEPARATION:
                                    SpotColor sc = (SpotColor)ec;
                                    checkPdfIsoConformance(PdfIsoKeys.PDFISOKEY_COLOR, sc.getPdfSpotColor().getAlternativeCS());
                                    break;
                                case ExtendedColor.TYPE_SHADING:
                                    ShadingColor xc = (ShadingColor)ec;
                                    checkPdfIsoConformance(PdfIsoKeys.PDFISOKEY_COLOR, xc.getPdfShadingPattern().getShading().getColorSpace());
                                    break;
                                case ExtendedColor.TYPE_PATTERN:
                                    PatternColor pc = (PatternColor)ec;
                                    checkPdfIsoConformance(PdfIsoKeys.PDFISOKEY_COLOR, pc.getPainter().getDefaultColor());
                                    break;
                            }
                        }
                        else if (obj1 instanceof BaseColor)
                            throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                        break;
                }
                break;
            case PdfIsoKeys.PDFISOKEY_CMYK:
                break;
            case PdfIsoKeys.PDFISOKEY_RGB:
                if (conf == PdfWriter.PDFX1A2001)
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                break;
            case PdfIsoKeys.PDFISOKEY_FONT:
                if (!((BaseFont)obj1).isEmbedded())
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont)obj1).getPostscriptFontName()));
                break;
            case PdfIsoKeys.PDFISOKEY_IMAGE:
                PdfImage image = (PdfImage)obj1;
                if (image.get(PdfName.SMASK) != null)
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.images"));
                switch (conf) {
                    case PdfWriter.PDFX1A2001:
                        PdfObject cs = image.get(PdfName.COLORSPACE);
                        if (cs == null)
                            return;
                        if (cs.isName()) {
                            if (PdfName.DEVICERGB.equals(cs))
                                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.rgb.is.not.allowed"));
                        }
                        else if (cs.isArray()) {
                            if (PdfName.CALRGB.equals(((PdfArray)cs).getPdfObject(0)))
                                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("colorspace.calrgb.is.not.allowed"));
                        }
                        break;
                }
                break;
            case PdfIsoKeys.PDFISOKEY_GSTATE:
                PdfDictionary gs = (PdfDictionary)obj1;
                // The example PdfXPdfA threw a NullPointerException because gs was null
                if (gs == null)
                	break;
                PdfObject obj = gs.get(PdfName.BM);
                if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj))
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
                obj = gs.get(PdfName.CA);
                double v = 0.0;
                if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0)
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
                obj = gs.get(PdfName.ca);
                v = 0.0;
                if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0)
                    throw new PdfXConformanceException(MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
                break;
            case PdfIsoKeys.PDFISOKEY_LAYER:
                throw new PdfXConformanceException(MessageLocalization.getComposedMessage("layers.are.not.allowed"));
        }
    }
}
