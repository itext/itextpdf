/*
 * PdfImportedPage.java
 *
 * Created on December 23, 2001, 6:24 PM
 */

package com.lowagie.text.pdf;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import java.io.IOException;

/** Represents an imported page.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfImportedPage extends com.lowagie.text.pdf.PdfTemplate {

    PdfReaderInstance readerInstance;
    int pageNumber;
    
    PdfImportedPage(PdfReaderInstance readerInstance, PdfWriter writer, int pageNumber) {
        this.readerInstance = readerInstance;
        this.pageNumber = pageNumber;
        thisReference = writer.getPdfIndirectReference();
        bBox = readerInstance.getReader().getPageSize(pageNumber);
    }

    /** Always throws an error. This operation is not allowed.
     * @param image dummy
     * @param a dummy
     * @param b dummy
     * @param c dummy
     * @param d dummy
     * @param e dummy
     * @param f dummy
     * @throws DocumentException  dummy */    
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        throwError();
    }
    
    /** Always throws an error. This operation is not allowed.
     * @param template dummy
     * @param a dummy
     * @param b dummy
     * @param c dummy
     * @param d dummy
     * @param e dummy
     * @param f  dummy */    
    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        throwError();
    }
    
    /** Always throws an error. This operation is not allowed.
     * @return  dummy */    
    public PdfContentByte getDuplicate() {
        throwError();
        return null;
    }
    
    public void setColorFill(PdfSpotColor sp, float tint) {
        throwError();
    }
    
    public void setColorStroke(PdfSpotColor sp, float tint) {
        throwError();
    }
    
    PdfObject getResources() {
        return readerInstance.getResources(pageNumber);
    }
    
    /** Always throws an error. This operation is not allowed.
     * @param bf dummy
     * @param size dummy */    
    public void setFontAndSize(BaseFont bf, float size) {
        throwError();
    }
    
    void throwError() {
        throw new RuntimeException("Content can not be added to a PdfImportedPage.");
    }
    
    /** Always return <CODE>true</CODE>.
     * @return always return <CODE>true</CODE>
     */    
    public boolean isImportedPage() {
        return true;
    }
    
    PdfReaderInstance getPdfReaderInstance() {
        return readerInstance;
    }
}
