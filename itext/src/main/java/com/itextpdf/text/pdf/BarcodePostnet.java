/*
 * $Id$
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

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.BaseColor;

/** Implements the Postnet and Planet barcodes. The default parameters are:
 * <pre>
 *n = 72f / 22f; // distance between bars
 *x = 0.02f * 72f; // bar width
 *barHeight = 0.125f * 72f; // height of the tall bars
 *size = 0.05f * 72f; // height of the short bars
 *codeType = POSTNET; // type of code
 * </pre>
 *
 * @author Paulo Soares
 */
public class BarcodePostnet extends Barcode{

    /** The bars for each character.
     */    
	private static final byte BARS[][] = 
    {
        {1,1,0,0,0},
        {0,0,0,1,1},
        {0,0,1,0,1},
        {0,0,1,1,0},
        {0,1,0,0,1},
        {0,1,0,1,0},
        {0,1,1,0,0},
        {1,0,0,0,1},
        {1,0,0,1,0},
        {1,0,1,0,0}
    };
    
    /** Creates new BarcodePostnet */
    public BarcodePostnet() {
        n = 72f / 22f; // distance between bars
        x = 0.02f * 72f; // bar width
        barHeight = 0.125f * 72f; // height of the tall bars
        size = 0.05f * 72f; // height of the short bars
        codeType = POSTNET; // type of code
    }
    
    /** Creates the bars for Postnet.
     * @param text the code to be created without checksum
     * @return the bars
     */    
    public static byte[] getBarsPostnet(String text) {
        int total = 0;
        for (int k = text.length() - 1; k >= 0; --k) {
            int n = text.charAt(k) - '0';
            total += n;
        }
        text += (char)(((10 - (total % 10)) % 10) + '0');
        byte bars[] = new byte[text.length() * 5 + 2];
        bars[0] = 1;
        bars[bars.length - 1] = 1;
        for (int k = 0; k < text.length(); ++k) {
            int c = text.charAt(k) - '0';
            System.arraycopy(BARS[c], 0, bars, k * 5 + 1, 5);
        }
        return bars;
    }

    /** Gets the maximum area that the barcode and the text, if
     * any, will occupy. The lower left corner is always (0, 0).
     * @return the size the barcode occupies.
     */
    public Rectangle getBarcodeSize() {
        float width = ((code.length() + 1) * 5 + 1) * n + x;
        return new Rectangle(width, barHeight);
    }
    
    /** Places the barcode in a <CODE>PdfContentByte</CODE>. The
     * barcode is always placed at coordinates (0, 0). Use the
     * translation matrix to move it elsewhere.<p>
     * The bars and text are written in the following colors:<p>
     * <P><TABLE BORDER=1>
     * <TR>
     *   <TH><P><CODE>barColor</CODE></TH>
     *   <TH><P><CODE>textColor</CODE></TH>
     *   <TH><P>Result</TH>
     *   </TR>
     * <TR>
     *   <TD><P><CODE>null</CODE></TD>
     *   <TD><P><CODE>null</CODE></TD>
     *   <TD><P>bars and text painted with current fill color</TD>
     *   </TR>
     * <TR>
     *   <TD><P><CODE>barColor</CODE></TD>
     *   <TD><P><CODE>null</CODE></TD>
     *   <TD><P>bars and text painted with <CODE>barColor</CODE></TD>
     *   </TR>
     * <TR>
     *   <TD><P><CODE>null</CODE></TD>
     *   <TD><P><CODE>textColor</CODE></TD>
     *   <TD><P>bars painted with current color<br>text painted with <CODE>textColor</CODE></TD>
     *   </TR>
     * <TR>
     *   <TD><P><CODE>barColor</CODE></TD>
     *   <TD><P><CODE>textColor</CODE></TD>
     *   <TD><P>bars painted with <CODE>barColor</CODE><br>text painted with <CODE>textColor</CODE></TD>
     *   </TR>
     * </TABLE>
     * @param cb the <CODE>PdfContentByte</CODE> where the barcode will be placed
     * @param barColor the color of the bars. It can be <CODE>null</CODE>
     * @param textColor the color of the text. It can be <CODE>null</CODE>
     * @return the dimensions the barcode occupies
     */
    public Rectangle placeBarcode(PdfContentByte cb, BaseColor barColor, BaseColor textColor) {
        if (barColor != null)
            cb.setColorFill(barColor);
        byte bars[] = getBarsPostnet(code);
        byte flip = 1;
        if (codeType == PLANET) {
            flip = 0;
            bars[0] = 0;
            bars[bars.length - 1] = 0;
        }
        float startX = 0;
        for (int k = 0; k < bars.length; ++k) {
            cb.rectangle(startX, 0, x - inkSpreading, bars[k] == flip ? barHeight : size);
            startX += n;
        }
        cb.fill();
        return getBarcodeSize();
    }


}