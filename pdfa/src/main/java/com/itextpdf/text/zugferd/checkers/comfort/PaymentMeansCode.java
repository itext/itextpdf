/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
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
 * Class that can be used to check if a payment means code is valid.
 * @deprecated For internal use only. If you want to use iText, please use a dependency on iText 7.
 */
@Deprecated
public class PaymentMeansCode extends CodeValidation {
    
    public static final String NOT_DEFINED = "1";
    public static final String AUTOMATED_CLEARING_HOUSE = "3";
    public static final String CASH = "10";
    public static final String CHEQUE = "20";
    public static final String DEBIT_TRANSFER = "31";
    public static final String PAYMENT_TO_BANK_ACCOUNT = "42";
    public static final String BANK_CARD = "48";
    public static final String DIRECT_DEBIT = "49";
    public static final String CLEARING = "97";
    
    public boolean isValid(String code) {
        return code.equals(NOT_DEFINED)
                || code.equals(AUTOMATED_CLEARING_HOUSE)
                || code.equals(CASH)
                || code.equals(CHEQUE)
                || code.equals(DEBIT_TRANSFER)
                || code.equals(PAYMENT_TO_BANK_ACCOUNT)
                || code.equals(BANK_CARD)
                || code.equals(DIRECT_DEBIT)
                || code.equals(CLEARING);
    }
}
