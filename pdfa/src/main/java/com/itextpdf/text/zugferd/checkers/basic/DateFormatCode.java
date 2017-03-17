/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, et al.
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
package com.itextpdf.text.zugferd.checkers.basic;

import com.itextpdf.text.zugferd.checkers.CodeValidation;
import com.itextpdf.text.zugferd.exceptions.InvalidCodeException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class that can be used to check a code for a date format.
 * Additionally, this class also contains some methods that allow you
 * to convert dates to strings and vice-versa based on a given format.
 */
public class DateFormatCode extends CodeValidation {
    public static final String YYYYMMDD = "102";
    public static final String YYYYMM = "610";
    public static final String YYYYWW = "616";
    
    public boolean isValid(String format) {
        return format.equals(YYYYMMDD)
            || format.equals(YYYYMM)
            || format.equals(YYYYWW);
    }
    
    public String convertToString(Date d, String format) throws InvalidCodeException {
        return getDateFormat(format).format(d);
    }
    
    public Date convertToDate(String d, String format) throws InvalidCodeException, ParseException {
        return getDateFormat(format).parse(d);
    }
    
    public static SimpleDateFormat getDateFormat(String format) throws InvalidCodeException {
        if (YYYYMMDD.equals(format)) {
            return new SimpleDateFormat("yyyyMMdd");
        }
        else if (YYYYMM.equals(format)) {
            return new SimpleDateFormat("yyyyMM");
        }
        else if (YYYYWW.equals(format)) {
            return new SimpleDateFormat("yyyyww");
        }
        throw new InvalidCodeException(format, "date format");
    }
}
