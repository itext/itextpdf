package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

/**
 * Created by IntelliJ IDEA.
 * User: denis.koleda
 * Date: 10/25/12
 * Time: 9:33 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IPdfStructureElement {
    public PdfObject getAttribute(PdfName name);
    public void setAttribute(PdfName name, PdfObject obj);
}
