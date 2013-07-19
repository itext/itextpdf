/**
 * Copyright 2013, DB Systel GmbH
 *
 * Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz)
 *
 */
package com.itextpdf.text.pdf;


public interface PdfPTableEventAfterSplit extends PdfPTableEventSplit {

    /**
     * This method is called to indicate that table has been split. It's called after the <CODE>tableLayout</CODE>
     * method and after the table has been drawn on the previous page but before the rest of the table is laid out on
     * the following page.
     * 
     * It is meant to allow modifications of the table, e.g. by changing cells. This is useful for situations when some
     * information has to be repeated, like putting "still <some category>" in the top cell of a column where
     * categorizations for blocks of rows are placed, e.g.
     * 
     * <PRE>
     * 2012  | Jan | 1000 $ | 2000 $ 
     *       | Feb |  900 $ | 2100 $
     * 
     * -------8<----- Page break -------------
     * 
     * still | Mar | 1100 $ | 1900 $ 
     * 2012  | Apr | 1200 $ | 1800 $ 
     *       | May | 1200 $ | 2200 $
     * ...
     * </PRE>
     * 
     * While this might be emulated by just stamping "still 2012" on the page using the currently available event
     * callback <CODE>tableLayout</CODE>, that would fail in the case of the page break happening after the November
     * line, because the text "still 2012" would then overlap with the new entry "2013" in the 2013 January line.
     * 
     * This problem does not exist when modifying the first cell on the new page because that cell will the be laid out
     * to have sufficient height so that no overlaps occur.
     * 
     * Example: 
     * <PRE>
     * public void afterSplitTable(PdfPTable table, PdfPRow startRow, int startIdx) { 
     *     PdfPCell cell = startRow.getCells()[0]; 
     *     cell.addElement(new Paragraph("still " + currentYear)); 
     * }
     * </PRE>
     * 
     * Note that determining the value of <CODE>currentYear</CODE> can be done in <CODE>tableLayout</CODE> 
     * by noting the sizes of the tables laid out there and comparing them with the tracked row indices
     * of the years (via table.getRows()). 
     * 
     * 
     * @param table
     *            the <CODE>PdfPTable</CODE> in use
     * @param startRow
     *            the first <CODE>PdfPRow</CODE> of the table body on the following page
     * @param startIdx
     *            the index of that row
     *
     * @since iText 5.4.3
     */
    public void afterSplitTable(PdfPTable table, PdfPRow startRow, int startIdx);

}
