package com.lowagie.text.pdf.internal;

import java.awt.Color;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ExtendedColor;
import com.lowagie.text.pdf.PatternColor;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfImage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfXConformanceException;
import com.lowagie.text.pdf.ShadingColor;
import com.lowagie.text.pdf.SpotColor;
import com.lowagie.text.pdf.interfaces.PdfXConformance;

public class PdfXConformanceImp implements PdfXConformance {

    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_COLOR = 1;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_CMYK = 2;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_RGB = 3;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_FONT = 4;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_IMAGE = 5;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_GSTATE = 6;
    /** A key for an aspect that can be checked for PDF/X Conformance. */
    public static final int PDFXKEY_LAYER = 7;
    
    /**
     * The value indicating if the PDF has to be in conformance with PDF/X.
     */
    protected int pdfxConformance = PdfWriter.PDFXNONE;
    
    /**
     * @see com.lowagie.text.pdf.interfaces.PdfXConformance#setPDFXConformance(int)
     */
    public void setPDFXConformance(int pdfxConformance) {
        this.pdfxConformance = pdfxConformance;
    }

	/**
	 * @see com.lowagie.text.pdf.interfaces.PdfXConformance#getPDFXConformance()
	 */
	public int getPDFXConformance() {
		return pdfxConformance;
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
    
    public void completeInfoDictionary(PdfDictionary info) {
        if (isPdfX()) {
            if (info.get(PdfName.GTS_PDFXVERSION) == null) {
                if (isPdfX1A2001()) {
                    info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-1:2001"));
                    info.put(new PdfName("GTS_PDFXConformance"), new PdfString("PDF/X-1a:2001"));
                }
                else if (isPdfX32002())
                    info.put(PdfName.GTS_PDFXVERSION, new PdfString("PDF/X-3:2002"));
            }
            if (info.get(PdfName.TITLE) == null) {
                info.put(PdfName.TITLE, new PdfString("Pdf document"));
            }
            if (info.get(PdfName.CREATOR) == null) {
                info.put(PdfName.CREATOR, new PdfString("Unknown"));
            }
            if (info.get(PdfName.TRAPPED) == null) {
                info.put(PdfName.TRAPPED, new PdfName("False"));
            }
        }
    }
    
    public void completeExtraCatalog(PdfDictionary extraCatalog) {
        if (extraCatalog.get(PdfName.OUTPUTINTENTS) == null) {
            PdfDictionary out = new PdfDictionary(PdfName.OUTPUTINTENT);
            out.put(PdfName.OUTPUTCONDITION, new PdfString("SWOP CGATS TR 001-1995"));
            out.put(PdfName.OUTPUTCONDITIONIDENTIFIER, new PdfString("CGATS TR 001"));
            out.put(PdfName.REGISTRYNAME, new PdfString("http://www.color.org"));
            out.put(PdfName.INFO, new PdfString(""));
            out.put(PdfName.S, PdfName.GTS_PDFX);
            extraCatalog.put(PdfName.OUTPUTINTENTS, new PdfArray(out));
        }
    }
    
    /**
	 * Business logic that checks if a certain object is in conformance with PDF/X.
     * @param writer	the writer that is supposed to write the PDF/X file
     * @param key		the type of PDF/X conformance that has to be checked
     * @param obj1		the object that is checked for conformance
     */
    public static void checkPDFXConformance(PdfWriter writer, int key, Object obj1) {
        if (writer == null || !writer.isPdfX())
            return;
        int conf = writer.getPDFXConformance();
        switch (key) {
            case PDFXKEY_COLOR:
                switch (conf) {
                    case PdfWriter.PDFX1A2001:
                        if (obj1 instanceof ExtendedColor) {
                            ExtendedColor ec = (ExtendedColor)obj1;
                            switch (ec.getType()) {
                                case ExtendedColor.TYPE_CMYK:
                                case ExtendedColor.TYPE_GRAY:
                                    return;
                                case ExtendedColor.TYPE_RGB:
                                    throw new PdfXConformanceException("Colorspace RGB is not allowed.");
                                case ExtendedColor.TYPE_SEPARATION:
                                    SpotColor sc = (SpotColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, sc.getPdfSpotColor().getAlternativeCS());
                                    break;
                                case ExtendedColor.TYPE_SHADING:
                                    ShadingColor xc = (ShadingColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, xc.getPdfShadingPattern().getShading().getColorSpace());
                                    break;
                                case ExtendedColor.TYPE_PATTERN:
                                    PatternColor pc = (PatternColor)ec;
                                    checkPDFXConformance(writer, PDFXKEY_COLOR, pc.getPainter().getDefaultColor());
                                    break;
                            }
                        }
                        else if (obj1 instanceof Color)
                            throw new PdfXConformanceException("Colorspace RGB is not allowed.");
                        break;
                }
                break;
            case PDFXKEY_CMYK:
                break;
            case PDFXKEY_RGB:
                if (conf == PdfWriter.PDFX1A2001)
                    throw new PdfXConformanceException("Colorspace RGB is not allowed.");
                break;
            case PDFXKEY_FONT:
                if (!((BaseFont)obj1).isEmbedded())
                    throw new PdfXConformanceException("All the fonts must be embedded.");
                break;
            case PDFXKEY_IMAGE:
                PdfImage image = (PdfImage)obj1;
                if (image.get(PdfName.SMASK) != null)
                    throw new PdfXConformanceException("The /SMask key is not allowed in images.");
                switch (conf) {
                    case PdfWriter.PDFX1A2001:
                        PdfObject cs = image.get(PdfName.COLORSPACE);
                        if (cs == null)
                            return;
                        if (cs.isName()) {
                            if (PdfName.DEVICERGB.equals(cs))
                                throw new PdfXConformanceException("Colorspace RGB is not allowed.");
                        }
                        else if (cs.isArray()) {
                            if (PdfName.CALRGB.equals(((PdfArray)cs).getArrayList().get(0)))
                                throw new PdfXConformanceException("Colorspace CalRGB is not allowed.");
                        }
                        break;
                }
                break;
            case PDFXKEY_GSTATE:
                PdfDictionary gs = (PdfDictionary)obj1;
                PdfObject obj = gs.get(PdfName.BM);
                if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj))
                    throw new PdfXConformanceException("Blend mode " + obj.toString() + " not allowed.");
                obj = gs.get(PdfName.CA);
                double v = 0.0;
                if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0)
                    throw new PdfXConformanceException("Transparency is not allowed: /CA = " + v);
                obj = gs.get(PdfName.ca);
                v = 0.0;
                if (obj != null && (v = ((PdfNumber)obj).doubleValue()) != 1.0)
                    throw new PdfXConformanceException("Transparency is not allowed: /ca = " + v);
                break;
            case PDFXKEY_LAYER:
                throw new PdfXConformanceException("Layers are not allowed.");
        }
    }
}
