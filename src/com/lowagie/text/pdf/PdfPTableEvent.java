/*
 * PdfPTableEvent.java
 *
 * Created on October 17, 2001, 3:06 PM
 */

package com.lowagie.text.pdf;

/** An interface that can be used to retrieve the position of cells in <CODE>PdfPTable</CODE>.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public interface PdfPTableEvent {
    
    /** This method is called at the end of the table rendering. The text or graphics are added to
     * one of the 4 <CODE>PdfContentByte</CODE> contained in
     * <CODE>canvases</CODE>.<br>
     * The indexes to <CODE>canvases</CODE> are:<p>
     * <ul>
     * <li><CODE>PdfPtable.BASECANVAS</CODE> - the original <CODE>PdfContentByte</CODE>. Anything placed here
     * will be under the table.
     * <li><CODE>PdfPtable.BACKGROUNDCANVAS</CODE> - the layer where the background goes to.
     * <li><CODE>PdfPtable.LINECANVAS</CODE> - the layer where the lines go to.
     * <li><CODE>PdfPtable.TEXTCANVAS</CODE> - the layer where the text go to. Anything placed here
     * will be over the table.
     * </ul>
     * The layers are placed in sequence on top of each other.
     * <p>
     * The <CODE>widths</CODE> and <CODE>heights</CODE> have the coordinates of the cells.<br>
     * For the <CODE>widths</CODE> the first element is the x coordinate of the left table border and the last
     * element is the x coordinate of the right table border.<br>
     * For the <CODE>heights</CODE> the first element is the y coordinate of the top table border and the last
     * element is the y coordinate of the bottom table border.
     * @param table the <CODE>PdfPTable</CODE> in use
     * @param widths an array with the cells' x positions. It has a length of number
     * of columns + 1
     * @param heights an array with the cells' y positions. It has a length of number
     * of rows + 1
     * @param headerRows the number of rows defined for the header.
     * It is always 0 if the table is not rendered with <CODE>Document.add</CODE>
     * @param rowStart the first row number after the header
     * @param canvases an array of <CODE>PdfContentByte</CODE>
     */    
    public void tableLayout(PdfPTable table, float widths[], float heights[], int headerRows, int rowStart, PdfContentByte[] canvases);

}

