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

/**
 * Class that can be used to check if a document type code is valid for
 * use in the context of a specific format.
 */
public class DocumentTypeCode extends CodeValidation {
    public static final String COMMERCIAL_INVOICE = "380";
    public static final String DEBIT_NOTE_FINANCIAL_ADJUSTMENT = "38";
    public static final String SELF_BILLED_INVOICE = "389";
    
    public static final int BASIC = 0;
    public static final int COMFORT = 1;
    public static final int EXTENDED = 2;
    
    protected int profile;
    
    public DocumentTypeCode(int profile) {
        this.profile = profile;
    }
    
    public boolean isValid(String code) {
        switch(profile) {
            case BASIC:
                return isValidBasic(code);
            case COMFORT:
                return isValidComfort(code);
            default:
                return isValidExtended(code);
        }
    }
    
    public static boolean isValidBasic(String code) {
        return COMMERCIAL_INVOICE.equals(code);
    }
    
    public static boolean isValidComfort(String code) {
        return COMMERCIAL_INVOICE.equals(code)
            || DEBIT_NOTE_FINANCIAL_ADJUSTMENT.equals(code);
    }
    
    public static boolean isValidExtended(String code) {
        return COMMERCIAL_INVOICE.equals(code)
            || DEBIT_NOTE_FINANCIAL_ADJUSTMENT.equals(code)
            || SELF_BILLED_INVOICE.equals(code);
    }
}
