/*
 * $Id: Type1Font.java 5756 2013-04-12 12:39:00Z michaeldemey $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, DB Systel, et al.
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
 
/**
 * Copyright 2013, DB Systel GmbH
 *
 * Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz)
 * Deutsche Bahn signed an MIT-style agreement with 1T3XT BVBA
 * regarding the use of this class.
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
