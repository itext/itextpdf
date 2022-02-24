/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
package com.itextpdf.text.pdf;

import java.util.StringTokenizer;

/**
 * A <CODE>PdfDestination</CODE> is a reference to a location in a PDF file.
 */

public class PdfDestination extends PdfArray {
    
    // public static final member-variables
    
/** This is a possible destination type */
    public static final int XYZ = 0;
    
/** This is a possible destination type */
    public static final int FIT = 1;
    
/** This is a possible destination type */
    public static final int FITH = 2;
    
/** This is a possible destination type */
    public static final int FITV = 3;
    
/** This is a possible destination type */
    public static final int FITR = 4;
    
/** This is a possible destination type */
    public static final int FITB = 5;
    
/** This is a possible destination type */
    public static final int FITBH = 6;
    
/** This is a possible destination type */
    public static final int FITBV = 7;
    
    // member variables
    
/** Is the indirect reference to a page already added? */
    private boolean status = false;
    
    // constructors
    
    public PdfDestination(PdfDestination d) {
        super(d);
        this.status = d.status;
    }
    
/**
 * Constructs a new <CODE>PdfDestination</CODE>.
 * <P>
 * If <VAR>type</VAR> equals <VAR>FITB</VAR>, the bounding box of a page
 * will fit the window of the Reader. Otherwise the type will be set to
 * <VAR>FIT</VAR> so that the entire page will fit to the window.
 *
 * @param		type		The destination type
 */
    
    public PdfDestination(int type) {
        super();
        if (type == FITB) {
            add(PdfName.FITB);
        }
        else {
            add(PdfName.FIT);
        }
    }
    
/**
 * Constructs a new <CODE>PdfDestination</CODE>.
 * <P>
 * If <VAR>type</VAR> equals <VAR>FITBH</VAR> / <VAR>FITBV</VAR>,
 * the width / height of the bounding box of a page will fit the window
 * of the Reader. The parameter will specify the y / x coordinate of the
 * top / left edge of the window. If the <VAR>type</VAR> equals <VAR>FITH</VAR>
 * or <VAR>FITV</VAR> the width / height of the entire page will fit
 * the window and the parameter will specify the y / x coordinate of the
 * top / left edge. In all other cases the type will be set to <VAR>FITH</VAR>.
 *
 * @param		type		the destination type
 * @param		parameter	a parameter to combined with the destination type
 */
    
    public PdfDestination(int type, float parameter) {
        super(new PdfNumber(parameter));
        switch(type) {
            default:
                addFirst(PdfName.FITH);
                break;
            case FITV:
                addFirst(PdfName.FITV);
                break;
            case FITBH:
                addFirst(PdfName.FITBH);
                break;
            case FITBV:
                addFirst(PdfName.FITBV);
        }
    }
    
/** Constructs a new <CODE>PdfDestination</CODE>.
 * <P>
 * Display the page, with the coordinates (left, top) positioned
 * at the top-left corner of the window and the contents of the page magnified
 * by the factor zoom. A negative value for any of the parameters left or top, or a
 * zoom value of 0 specifies that the current value of that parameter is to be retained unchanged.
 * @param type must be a <VAR>PdfDestination.XYZ</VAR>
 * @param left the left value. Negative to place a null
 * @param top the top value. Negative to place a null
 * @param zoom The zoom factor. A value of 0 keeps the current value
 */
    
    public PdfDestination(int type, float left, float top, float zoom) {
        super(PdfName.XYZ);
        if (left < 0)
            add(PdfNull.PDFNULL);
        else
            add(new PdfNumber(left));
        if (top < 0)
            add(PdfNull.PDFNULL);
        else
            add(new PdfNumber(top));
        add(new PdfNumber(zoom));
    }
    
/** Constructs a new <CODE>PdfDestination</CODE>.
 * <P>
 * Display the page, with its contents magnified just enough
 * to fit the rectangle specified by the coordinates left, bottom, right, and top
 * entirely within the window both horizontally and vertically. If the required
 * horizontal and vertical magnification factors are different, use the smaller of
 * the two, centering the rectangle within the window in the other dimension.
 *
 * @param type must be PdfDestination.FITR
 * @param left a parameter
 * @param bottom a parameter
 * @param right a parameter
 * @param top a parameter
 * @since iText0.38
 */
    
    public PdfDestination(int type, float left, float bottom, float right, float top) {
        super(PdfName.FITR);
        add(new PdfNumber(left));
        add(new PdfNumber(bottom));
        add(new PdfNumber(right));
        add(new PdfNumber(top));
    }
    
    /**
     * Creates a PdfDestination based on a String.
     * Valid Strings are for instance the values returned by SimpleNamedDestination:
     * "Fit", "XYZ 36 806 0",...
     * @param	dest	a String notation of a destination.
     * @since	iText 5.0
     */
    public PdfDestination(String dest) {
    	super();
    	StringTokenizer tokens = new StringTokenizer(dest);
    	if (tokens.hasMoreTokens()) {
    		add(new PdfName(tokens.nextToken()));
    	}
    	while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if ("null".equals(token))
                add(new PdfNull());
            else {
                try {
                    add(new PdfNumber(token));
                } catch (RuntimeException e) {
                    add(new PdfNull());
                }
            }
    	}
    }
    
    // methods

/**
 * Checks if an indirect reference to a page has been added.
 *
 * @return	<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public boolean hasPage() {
        return status;
    }
    
/** Adds the indirect reference of the destination page.
 *
 * @param page	an indirect reference
 * @return true if the page reference was added
 */
    
    public boolean addPage(PdfIndirectReference page) {
        if (!status) {
            addFirst(page);
            status = true;
            return true;
        }
        return false;
    }
}
