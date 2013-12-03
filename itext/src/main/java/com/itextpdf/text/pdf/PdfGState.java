/*
 * $Id: PdfGState.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.internal.PdfIsoKeys;

import java.io.IOException;
import java.io.OutputStream;

/** The graphic state dictionary.
 *
 * @author Paulo Soares
 */
public class PdfGState extends PdfDictionary {
    /** A possible blend mode */
    public static final PdfName BM_NORMAL = new PdfName("Normal");
    /** A possible blend mode */
    public static final PdfName BM_COMPATIBLE = new PdfName("Compatible");
    /** A possible blend mode */
    public static final PdfName BM_MULTIPLY = new PdfName("Multiply");
    /** A possible blend mode */
    public static final PdfName BM_SCREEN = new PdfName("Screen");
    /** A possible blend mode */
    public static final PdfName BM_OVERLAY = new PdfName("Overlay");
    /** A possible blend mode */
    public static final PdfName BM_DARKEN = new PdfName("Darken");
    /** A possible blend mode */
    public static final PdfName BM_LIGHTEN = new PdfName("Lighten");
    /** A possible blend mode */
    public static final PdfName BM_COLORDODGE = new PdfName("ColorDodge");
    /** A possible blend mode */
    public static final PdfName BM_COLORBURN = new PdfName("ColorBurn");
    /** A possible blend mode */
    public static final PdfName BM_HARDLIGHT = new PdfName("HardLight");
    /** A possible blend mode */
    public static final PdfName BM_SOFTLIGHT = new PdfName("SoftLight");
    /** A possible blend mode */
    public static final PdfName BM_DIFFERENCE = new PdfName("Difference");
    /** A possible blend mode */
    public static final PdfName BM_EXCLUSION = new PdfName("Exclusion");
    
    /**
     * Sets the flag whether to apply overprint for stroking.
     * @param ov
     */
    public void setOverPrintStroking(boolean ov) {
        put(PdfName.OP, ov ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Sets the flag whether to apply overprint for non stroking painting operations.
     * @param ov
     */
    public void setOverPrintNonStroking(boolean ov) {
        put(PdfName.op, ov ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Sets the flag whether to toggle knockout behavior for overprinted objects.
     * @param ov - accepts 0 or 1
     */
    public void setOverPrintMode(int ov) {
        put(PdfName.OPM, new PdfNumber(ov==0 ? 0 : 1));
    }
    
    /**
     * Sets the current stroking alpha constant, specifying the constant shape or
     * constant opacity value to be used for stroking operations in the transparent
     * imaging model.
     * @param n
     */
    public void setStrokeOpacity(float n) {
        put(PdfName.CA, new PdfNumber(n));
    }
    
    /**
     * Sets the current stroking alpha constant, specifying the constant shape or
     * constant opacity value to be used for nonstroking operations in the transparent
     * imaging model.
     * @param n
     */
    public void setFillOpacity(float n) {
        put(PdfName.ca, new PdfNumber(n));
    }
    
    /**
     * The alpha source flag specifying whether the current soft mask
     * and alpha constant are to be interpreted as shape values (true)
     * or opacity values (false). 
     * @param v
     */
    public void setAlphaIsShape(boolean v) {
        put(PdfName.AIS, v ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }
    
    /**
     * Determines the behavior of overlapping glyphs within a text object
     * in the transparent imaging model.
     * @param v
     */
    public void setTextKnockout(boolean v) {
        put(PdfName.TK, v ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }
    
    /**
     * The current blend mode to be used in the transparent imaging model.
     * @param bm
     */
    public void setBlendMode(PdfName bm) {
        put(PdfName.BM, bm);
    }
    
    /**
     * Set the rendering intent, possible values are: PdfName.ABSOLUTECOLORIMETRIC,
     * PdfName.RELATIVECOLORIMETRIC, PdfName.SATURATION, PdfName.PERCEPTUAL.
     * @param ri
     * @since 5.0.2
     */
    public void setRenderingIntent(PdfName ri) {
    	put(PdfName.RI, ri);
    }

    @Override
    public void toPdf(final PdfWriter writer, final OutputStream os) throws IOException {
        PdfWriter.checkPdfIsoConformance(writer, PdfIsoKeys.PDFISOKEY_GSTATE, this);
        super.toPdf(writer, os);
    }
}
