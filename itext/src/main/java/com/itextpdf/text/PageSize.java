/*
 * $Id: PageSize.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.text;

import java.lang.reflect.Field;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * The <CODE>PageSize</CODE>-object contains a number of rectangles representing the most common paper sizes.
 *
 * @see		Rectangle
 */

public class PageSize {

    // membervariables

	/** This is the letter format */
    public static final Rectangle LETTER = new RectangleReadOnly(612,792);

    /** This is the note format */
    public static final Rectangle NOTE = new RectangleReadOnly(540,720);

    /** This is the legal format */
    public static final Rectangle LEGAL = new RectangleReadOnly(612,1008);

    /** This is the tabloid format */
    public static final Rectangle TABLOID = new RectangleReadOnly(792,1224);

    /** This is the executive format */
    public static final Rectangle EXECUTIVE = new RectangleReadOnly(522,756);

    /** This is the postcard format */
    public static final Rectangle POSTCARD = new RectangleReadOnly(283,416);

    /** This is the a0 format */
    public static final Rectangle A0 = new RectangleReadOnly(2384,3370);

    /** This is the a1 format */
    public static final Rectangle A1 = new RectangleReadOnly(1684,2384);

    /** This is the a2 format */
    public static final Rectangle A2 = new RectangleReadOnly(1191,1684);

    /** This is the a3 format */
    public static final Rectangle A3 = new RectangleReadOnly(842,1191);

    /** This is the a4 format */
    public static final Rectangle A4 = new RectangleReadOnly(595,842);

    /** This is the a5 format */
    public static final Rectangle A5 = new RectangleReadOnly(420,595);

    /** This is the a6 format */
    public static final Rectangle A6 = new RectangleReadOnly(297,420);

    /** This is the a7 format */
    public static final Rectangle A7 = new RectangleReadOnly(210,297);

    /** This is the a8 format */
    public static final Rectangle A8 = new RectangleReadOnly(148,210);

    /** This is the a9 format */
    public static final Rectangle A9 = new RectangleReadOnly(105,148);

    /** This is the a10 format */
    public static final Rectangle A10 = new RectangleReadOnly(73,105);

    /** This is the b0 format */
    public static final Rectangle B0 = new RectangleReadOnly(2834,4008);

    /** This is the b1 format */
    public static final Rectangle B1 = new RectangleReadOnly(2004,2834);

    /** This is the b2 format */
    public static final Rectangle B2 = new RectangleReadOnly(1417,2004);

    /** This is the b3 format */
    public static final Rectangle B3 = new RectangleReadOnly(1000,1417);

    /** This is the b4 format */
    public static final Rectangle B4 = new RectangleReadOnly(708,1000);

    /** This is the b5 format */
    public static final Rectangle B5 = new RectangleReadOnly(498,708);

    /** This is the b6 format */
    public static final Rectangle B6 = new RectangleReadOnly(354,498);

    /** This is the b7 format */
    public static final Rectangle B7 = new RectangleReadOnly(249,354);

    /** This is the b8 format */
    public static final Rectangle B8 = new RectangleReadOnly(175,249);

    /** This is the b9 format */
    public static final Rectangle B9 = new RectangleReadOnly(124,175);

    /** This is the b10 format */
    public static final Rectangle B10 = new RectangleReadOnly(87,124);

    /** This is the archE format */
    public static final Rectangle ARCH_E = new RectangleReadOnly(2592,3456);

    /** This is the archD format */
    public static final Rectangle ARCH_D = new RectangleReadOnly(1728,2592);

    /** This is the archC format */
    public static final Rectangle ARCH_C = new RectangleReadOnly(1296,1728);

    /** This is the archB format */
    public static final Rectangle ARCH_B = new RectangleReadOnly(864,1296);

    /** This is the archA format */
    public static final Rectangle ARCH_A = new RectangleReadOnly(648,864);

    /** This is the American Foolscap format */
    public static final Rectangle FLSA = new RectangleReadOnly(612,936);

    /** This is the European Foolscap format */
    public static final Rectangle FLSE = new RectangleReadOnly(648,936);

    /** This is the halfletter format */
    public static final Rectangle HALFLETTER = new RectangleReadOnly(396,612);

    /** This is the 11x17 format */
    public static final Rectangle _11X17 = new RectangleReadOnly(792,1224);

    /** This is the ISO 7810 ID-1 format (85.60 x 53.98 mm or 3.370 x 2.125 inch) */
    public static final Rectangle ID_1 = new RectangleReadOnly(242.65f,153);

    /** This is the ISO 7810 ID-2 format (A7 rotated) */
    public static final Rectangle ID_2 = new RectangleReadOnly(297,210);

    /** This is the ISO 7810 ID-3 format (B7 rotated) */
    public static final Rectangle ID_3 = new RectangleReadOnly(354,249);

    /** This is the ledger format */
    public static final Rectangle LEDGER = new RectangleReadOnly(1224,792);

    /** This is the Crown Quarto format */
    public static final Rectangle CROWN_QUARTO = new RectangleReadOnly(535,697);

    /** This is the Large Crown Quarto format */
    public static final Rectangle LARGE_CROWN_QUARTO = new RectangleReadOnly(569,731);

    /** This is the Demy Quarto format. */
    public static final Rectangle DEMY_QUARTO = new RectangleReadOnly(620,782);

    /** This is the Royal Quarto format. */
    public static final Rectangle ROYAL_QUARTO = new RectangleReadOnly(671,884);

    /** This is the Crown Octavo format */
    public static final Rectangle CROWN_OCTAVO = new RectangleReadOnly(348,527);

    /** This is the Large Crown Octavo format */
    public static final Rectangle LARGE_CROWN_OCTAVO = new RectangleReadOnly(365,561);

    /** This is the Demy Octavo format */
    public static final Rectangle DEMY_OCTAVO = new RectangleReadOnly(391,612);

    /** This is the Royal Octavo format. */
    public static final Rectangle ROYAL_OCTAVO = new RectangleReadOnly(442,663);

    /** This is the small paperback format. */
    public static final Rectangle SMALL_PAPERBACK = new RectangleReadOnly(314,504);

    /** This is the Pengiun small paperback format. */
    public static final Rectangle PENGUIN_SMALL_PAPERBACK = new RectangleReadOnly(314,513);

    /** This is the Penguin large paperback format. */
    public static final Rectangle PENGUIN_LARGE_PAPERBACK = new RectangleReadOnly(365,561);

    // Some extra shortcut values for pages in Landscape

	/**
	 * This is the letter format
	 * @since iText 5.0.6
	 * @deprecated
	 */
    public static final Rectangle LETTER_LANDSCAPE = new RectangleReadOnly(612, 792, 90);

    /**
     * This is the legal format
     * @since iText 5.0.6
     * @deprecated
     */
    public static final Rectangle LEGAL_LANDSCAPE = new RectangleReadOnly(612, 1008, 90);

    /**
     * This is the a4 format
     * @since iText 5.0.6
     * @deprecated
     */
    public static final Rectangle A4_LANDSCAPE = new RectangleReadOnly(595, 842, 90);



    /**
     * This method returns a Rectangle based on a String.
     * Possible values are the the names of a constant in this class
     * (for instance "A4", "LETTER",...) or a value like "595 842"
     * @param name the name as defined by the constants of PageSize or a numeric pair string
     * @return the rectangle
     */
    public static Rectangle getRectangle(String name)  {
    	name = name.trim().toUpperCase();
    	int pos = name.indexOf(' ');
        if (pos == -1) {
            try {
                Field field = PageSize.class.getDeclaredField(name.toUpperCase());
                return (Rectangle) field.get(null);
            } catch (Exception e) {
                throw new RuntimeException(MessageLocalization.getComposedMessage("can.t.find.page.size.1", name));
            }
        }
        else {
        	try {
        		String width = name.substring(0, pos);
        		String height = name.substring(pos + 1);
        		return new Rectangle(Float.parseFloat(width), Float.parseFloat(height));
        	} catch(Exception e) {
        		throw new RuntimeException(MessageLocalization.getComposedMessage("1.is.not.a.valid.page.size.format.2", name, e.getMessage()));
        	}
        }
    }
}
