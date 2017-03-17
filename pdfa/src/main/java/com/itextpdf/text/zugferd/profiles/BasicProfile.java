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
package com.itextpdf.text.zugferd.profiles;

import java.util.Date;

/**
 * If you implement this interface correctly, you provide all the data that
 * is necessary for iText to create an XML that can be used in a ZUGFeRD
 * invoice that conforms with the Basic profile.
 */
public interface BasicProfile {
    public boolean getTestIndicator();
    public String getId();
    public String getName();
    public String getTypeCode();
    public Date getDateTime();
    public String getDateTimeFormat();
    public String[][] getNotes();
    public String getSellerName();
    public String getSellerPostcode();
    public String getSellerLineOne();
    public String getSellerLineTwo();
    public String getSellerCityName();
    public String getSellerCountryID();
    public String[] getSellerTaxRegistrationID();
    public String[] getSellerTaxRegistrationSchemeID();
    public String getBuyerName();
    public String getBuyerPostcode();
    public String getBuyerLineOne();
    public String getBuyerLineTwo();
    public String getBuyerCityName();
    public String getBuyerCountryID();
    public String[] getBuyerTaxRegistrationID();
    public String[] getBuyerTaxRegistrationSchemeID();
    public Date getDeliveryDateTime();
    public String getDeliveryDateTimeFormat();
    public String getPaymentReference();
    public String getInvoiceCurrencyCode();
    public String[] getPaymentMeansID();
    public String[] getPaymentMeansSchemeAgencyID();
    public String[] getPaymentMeansPayeeAccountIBAN();
    public String[] getPaymentMeansPayeeAccountAccountName();
    public String[] getPaymentMeansPayeeAccountProprietaryID();
    public String[] getPaymentMeansPayeeFinancialInstitutionBIC();
    public String[] getPaymentMeansPayeeFinancialInstitutionGermanBankleitzahlID();
    public String[] getPaymentMeansPayeeFinancialInstitutionName();
    public String[] getTaxCalculatedAmount();
    public String[] getTaxCalculatedAmountCurrencyID();
    public String[] getTaxTypeCode();
    public String[] getTaxBasisAmount();
    public String[] getTaxBasisAmountCurrencyID();
    public String[] getTaxApplicablePercent();
    public String getLineTotalAmount();
    public String getLineTotalAmountCurrencyID();
    public String getChargeTotalAmount();
    public String getChargeTotalAmountCurrencyID();
    public String getAllowanceTotalAmount();
    public String getAllowanceTotalAmountCurrencyID();
    public String getTaxBasisTotalAmount();
    public String getTaxBasisTotalAmountCurrencyID();
    public String getTaxTotalAmount();
    public String getTaxTotalAmountCurrencyID();
    public String getGrandTotalAmount();
    public String getGrandTotalAmountCurrencyID();
    public String[] getLineItemBilledQuantity();
    public String[] getLineItemBilledQuantityUnitCode();
    public String[] getLineItemSpecifiedTradeProductName();
}

