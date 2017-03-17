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
package com.itextpdf.text.zugferd.checkers.comfort;

import com.itextpdf.text.zugferd.checkers.CodeValidation;

/**
 * Class that can be used to check if a global identifier code is well-formed.
 * IMPORTANT: this doesn't check if the code is valid in the sense that
 * it doesn't check the code against a list of global identifier codes.
 */
public class GlobalIdentifierCode extends CodeValidation {

    public static final String SWIFT = "0021";
    public static final String DUNS = "0060";
    public static final String GLN = "0088";
    public static final String GTIN = "0160";
    public static final String ODETTE = "0177";
    
    /**
     * The code list provided with the ZUGFeRD standard only lists five codes.
     * There are more codes available (in ISO 6523).
     * We won't check the presence of a code in ISO 6523, but we'll check if
     * the code consists of four numbers.
     * @param code the code to be tested
     * @return true if the code has the correct format
     */
    public boolean isValid(String code) {
        return isNumeric(code, 4);
    }

}
