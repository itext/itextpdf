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
 * Class that can be used to check if a free text subject code is valid.
 */
public class FreeTextSubjectCode extends CodeValidation {
    // header 
    public static final String REGULATORY_INFORMATION = "REG";
    public static final String PRICE_CONDITIONS = "AAK";
    public static final String ADDITIONAL_SALES_CONDITIONS = "AAJ";
    public static final String PAYMENT_INFORMATION = "PMT";
    // line
    public static final String PRICE_CALCULATION_FORMULA = "PRF";
    public static final String PRODUCT_INFORMATION = "PRD";
    public static final String CERTIFICATION_STATEMENTS = "AAY";
    
    public static final int HEADER = 1;
    public static final int LINE = 2;
    
    protected int level;
    
    public FreeTextSubjectCode(int level) {
        this.level = level;
    }
    
    public boolean isValid(String code) {
        switch(level) {
            case HEADER:
                return isHeaderLevel(code);
            case LINE:
                return isLineLevel(code);
        }
        return true;
    }
    
    public static boolean isHeaderLevel(String code) {
        return code.equals(REGULATORY_INFORMATION)
            || code.equals(PRICE_CONDITIONS)
            || code.equals(ADDITIONAL_SALES_CONDITIONS)
            || code.equals(PAYMENT_INFORMATION);
    }
    
    public static boolean isLineLevel(String code) {
        return code.equals(PRICE_CALCULATION_FORMULA)
            || code.equals(PRODUCT_INFORMATION)
            || code.equals(CERTIFICATION_STATEMENTS);
    }
}
