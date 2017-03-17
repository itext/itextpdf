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
package com.itextpdf.text.zugferd.checkers.extended;

import com.itextpdf.text.zugferd.checkers.CodeValidation;

/**
 * Series of codes that can be used for additional referenced documents.
 * These codes are used only in the context of the Extended profile.
 */
public class AdditionalReferencedDocumentsCode extends CodeValidation {
    
    public static final String ORDER_ACKNOWLEDGEMENT = "AAA";
    public static final String PROFORMA_INVOICE = "AAB"; 
    public static final String OFFER = "AAG"; 
    public static final String DELIVERY_ORDER = "AAJ"; 
    public static final String DRAWING = "AAL"; 
    public static final String WAYBILL = "AAM"; 
    public static final String TRANSPORT_CONTRACT = "AAS"; 
    public static final String GOODS_DECLARATION = "ABT"; 
    public static final String PROJECT_SPECIFICATION = "AER"; 
    public static final String DISPUTE = "AGG";
    public static final String AGREEMENT = "AJS"; 
    public static final String RETURNS_NOTICE = "ALQ"; 
    public static final String RECEIVING_ADVICE = "ALO"; 
    public static final String INVENTORY_REPORT = "API";
    public static final String PROOF_OF_DELIVERY = "ASI"; 
    public static final String COLLECTION_REF = "AUD"; 
    public static final String DOCUMENT_REF = "AWR"; 
    public static final String BLANKET_ORDER = "BO";
    public static final String BUYERS_CONTRACT = "BC"; 
    public static final String CREDIT_NOTE = "CD"; 
    public static final String DEBIT_NOTE = "DL"; 
    public static final String METER_UNIT = "MG"; 
    public static final String PREVIOUS_INVOICE = "OI"; 
    public static final String PRICE_LIST = "PL"; 
    public static final String PACKING_LIST = "PK"; 
    public static final String PURCHASE_ORDER_RESPONSE = "POR"; 
    public static final String PURCHASE_ORDER_CHANGE = "PP"; 
    public static final String TRANSPORT_INSTRUCTION = "TIN"; 
    public static final String VENDOR_ORDER = "VN";     
    
    public boolean isValid(String code) {
        return code.equals(ORDER_ACKNOWLEDGEMENT) ||
               code.equals(PROFORMA_INVOICE) ||
               code.equals(OFFER) ||
               code.equals(DELIVERY_ORDER) ||
               code.equals(DRAWING) ||
               code.equals(WAYBILL) ||
               code.equals(TRANSPORT_CONTRACT) ||
               code.equals(GOODS_DECLARATION) ||
               code.equals(PROJECT_SPECIFICATION) ||
               code.equals(DISPUTE) ||
               code.equals(AGREEMENT) ||
               code.equals(RETURNS_NOTICE) ||
               code.equals(RECEIVING_ADVICE) ||
               code.equals(INVENTORY_REPORT) ||
               code.equals(PROOF_OF_DELIVERY) ||
               code.equals(COLLECTION_REF) ||
               code.equals(DOCUMENT_REF) ||
               code.equals(BLANKET_ORDER) ||
               code.equals(BUYERS_CONTRACT) ||
               code.equals(CREDIT_NOTE) ||
               code.equals(DEBIT_NOTE) ||
               code.equals(METER_UNIT) ||
               code.equals(PREVIOUS_INVOICE) ||
               code.equals(PRICE_LIST) ||
               code.equals(PACKING_LIST) ||
               code.equals(PURCHASE_ORDER_RESPONSE) ||
               code.equals(PURCHASE_ORDER_CHANGE) ||
               code.equals(TRANSPORT_INSTRUCTION) ||
               code.equals(VENDOR_ORDER);
    }
    
}
