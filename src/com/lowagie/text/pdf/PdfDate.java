/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;

import java.util.GregorianCalendar;

/**
 * <CODE>PdfDate</CODE> is the PDF date object.
 * <P>
 * PDF defines a standard date format. The PDF date format closely follows the format
 * defined by the international standard ASN.1 (Abstract Syntax Notation One, defined
 * in CCITT X.208 or ISO/IEC 8824). A date is a <CODE>PdfString</CODE> of the form:
 * <P><BLOCKQUOTE>
 * (D: YYYYMMDDHHmmSSOHH'mm')
 * </BLOCKQUOTE><P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.2 (page 183-184)
 *
 * @see		PdfString
 * @see		java.util.GregorianCalendar
 */

class PdfDate extends PdfString {
    
    // constructors
    
/**
 * Constructs a <CODE>PdfDate</CODE>-object.
 *
 * @param		d			the date that has to be turned into a <CODE>PdfDate</CODE>-object
 */
    
    PdfDate(GregorianCalendar d) {
        super();
        StringBuffer date = new StringBuffer("D:");
        date.append(setLength(d.get(GregorianCalendar.YEAR), 4));
        date.append(setLength(d.get(GregorianCalendar.MONTH) + 1, 2));
        date.append(setLength(d.get(GregorianCalendar.DATE), 2));
        date.append(setLength(d.get(GregorianCalendar.HOUR_OF_DAY), 2));
        date.append(setLength(d.get(GregorianCalendar.MINUTE), 2));
        date.append(setLength(d.get(GregorianCalendar.SECOND), 2));
        int timezone = d.get(GregorianCalendar.ZONE_OFFSET) / (60 * 60 * 1000);
        if (timezone == 0) {
            date.append("Z");
        }
        else if (timezone < 0) {
            date.append("-");
            timezone = -timezone;
        }
        else {
            date.append("+");
        }
        date.append(setLength(timezone, 2)).append("'");
        int zone = Math.abs(d.get(GregorianCalendar.ZONE_OFFSET) / (60 * 1000)) - (timezone * 60);
        date.append(setLength(zone, 2)).append("'");
        value = date.toString();
        setContent(value);
    }
    
/**
 * Constructs a <CODE>PdfDate</CODE>-object, representing the current day and time.
 */
    
    PdfDate() {
        this(new GregorianCalendar());
    }
    
/**
 * Adds a number of leading zeros to a given <CODE>String</CODE> in order to get a <CODE>String</CODE>
 * of a certain length.
 *
 * @param		tmp			a given <CODE>String</CODE>
 * @param		length		the length of the resulting <CODE>String</CODE>
 * @return		the resulting <CODE>String</CODE>
 */
    
    private StringBuffer setLength(int i, int length) {
        StringBuffer tmp = new StringBuffer();
        tmp.append(i);
        while (tmp.length() < length) {
            tmp.insert(0, "0");
        }
        tmp.setLength(length);
        return tmp;
    }
}