/*
 * PdfPageEvent.java
 *
 * Created on April 10, 2001, 11:21 AM
 */

package com.lowagie.text.pdf;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;

/** Allows a class to catch several document events.
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */
public interface PdfPageEvent
{
    /** Called when the document is opened.
     *
     * @param writer the <CODE>PdfWriter</CODE> for this document
     * @param document the document
     */
    public void onOpenDocument(PdfWriter writer, Document document);

    /** Called when a page is initialized.
     * <P>
     * Note that if even if a page is not written this method is still
     * called. It is preferable to use <CODE>onEndPage</CODE> to avoid
     * infinite loops.
     *
     * @param writer the <CODE>PdfWriter</CODE> for this document
     * @param document the document
     */
    public void onStartPage(PdfWriter writer, Document document);

    /** Called when a page is finished, just before being written to the document.
     *
     * @param writer the <CODE>PdfWriter</CODE> for this document
     * @param document the document
     */
    public void onEndPage(PdfWriter writer, Document document);

    /** Called when the document is closed.
     * <P>
     * Note that this method is called with the page number equal
     * to the last page plus one.
     *
     * @param writer the <CODE>PdfWriter</CODE> for this document
     * @param document the document
     */
    public void onCloseDocument(PdfWriter writer, Document document);
    
    /** Called when a Paragraph is written.
     * <P>
     * <CODE>paragraphPosition</CODE> will hold the height at which the
     * paragraph will be written to. This is useful to insert bookmarks with
     * more control.
     *
     * @param writer the <CODE>PdfWriter</CODE> for this document
     * @param document the document
     * @param paragraphPosition the position the paragraph will be written to
     */
    public void onParagraph(PdfWriter writer, Document document, float paragraphPosition);
    
    /** Called when a <CODE>Chunk</CODE> with a generic tag is written.
     * <P>
     * It is usefull to pinpoint the <CODE>Chunk</CODE> location to generate
     * bookmarks, for example.
     *
     * @param writer the <CODE>PdfWriter</CODE> for this document
     * @param document the document
     * @param rect the <CODE>Rectangle</CODE> containing the <CODE>Chunk</CODE>
     * @param text the text of the tag
     */
    public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text);
}

