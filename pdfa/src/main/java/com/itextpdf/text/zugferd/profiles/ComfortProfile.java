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
 * invoice that conforms with the Comfort profile.
 */
public interface ComfortProfile extends BasicProfile {
    public String[] getNotesCodes();
    public String getBuyerReference();
    public String getSellerID();
    public String[] getSellerGlobalID();
    public String[] getSellerGlobalSchemeID();
    public String getBuyerID();
    public String[] getBuyerGlobalID();
    public String[] getBuyerGlobalSchemeID();
    public Date getBuyerOrderReferencedDocumentIssueDateTime();
    public String getBuyerOrderReferencedDocumentIssueDateTimeFormat();
    public String getBuyerOrderReferencedDocumentID();
    public Date getContractReferencedDocumentIssueDateTime();
    public String getContractReferencedDocumentIssueDateTimeFormat();
    public String getContractReferencedDocumentID();
    public Date getCustomerOrderReferencedDocumentIssueDateTime();
    public String getCustomerOrderReferencedDocumentIssueDateTimeFormat();
    public String getCustomerOrderReferencedDocumentID();
    public Date getDeliveryNoteReferencedDocumentIssueDateTime();
    public String getDeliveryNoteReferencedDocumentIssueDateTimeFormat();
    public String getDeliveryNoteReferencedDocumentID();
    public String getInvoiceeID();
    public String[] getInvoiceeGlobalID();
    public String[] getInvoiceeGlobalSchemeID();
    public String getInvoiceeName();
    public String getInvoiceePostcode();
    public String getInvoiceeLineOne();
    public String getInvoiceeLineTwo();
    public String getInvoiceeCityName();
    public String getInvoiceeCountryID();
    public String[] getInvoiceeTaxRegistrationID();
    public String[] getInvoiceeTaxRegistrationSchemeID();
    public String[] getPaymentMeansTypeCode();
    public String[][] getPaymentMeansInformation();
    public String[] getPaymentMeansPayerAccountIBAN();
    public String[] getPaymentMeansPayerAccountProprietaryID();
    public String[] getPaymentMeansPayerFinancialInstitutionBIC();
    public String[] getPaymentMeansPayerFinancialInstitutionGermanBankleitzahlID();
    public String[] getPaymentMeansPayerFinancialInstitutionName();
    public String[] getTaxExemptionReason();
    public String[] getTaxCategoryCode();
    public Date getBillingStartDateTime();
    public String getBillingStartDateTimeFormat();
    public Date getBillingEndDateTime();
    public String getBillingEndDateTimeFormat();
    public Boolean[] getSpecifiedTradeAllowanceChargeIndicator();
    public String[] getSpecifiedTradeAllowanceChargeActualAmount();
    public String[] getSpecifiedTradeAllowanceChargeActualAmountCurrency();
    public String[] getSpecifiedTradeAllowanceChargeReason();
    public String[][] getSpecifiedTradeAllowanceChargeTaxTypeCode();
    public String[][] getSpecifiedTradeAllowanceChargeTaxCategoryCode();
    public String[][] getSpecifiedTradeAllowanceChargeTaxApplicablePercent();
    public String[][] getSpecifiedLogisticsServiceChargeDescription();
    public String[] getSpecifiedLogisticsServiceChargeAmount();
    public String[] getSpecifiedLogisticsServiceChargeAmountCurrency();
    public String[][] getSpecifiedLogisticsServiceChargeTaxTypeCode();
    public String[][] getSpecifiedLogisticsServiceChargeTaxCategoryCode();
    public String[][] getSpecifiedLogisticsServiceChargeTaxApplicablePercent();
    public String[][] getSpecifiedTradePaymentTermsDescription();
    public Date[] getSpecifiedTradePaymentTermsDueDateTime();
    public String[] getSpecifiedTradePaymentTermsDueDateTimeFormat();
    public String getTotalPrepaidAmount();
    public String getTotalPrepaidAmountCurrencyID();
    public String getDuePayableAmount();
    public String getDuePayableAmountCurrencyID();
    public String[] getLineItemLineID();
    public String[][][] getLineItemIncludedNote();
    public String[] getLineItemGrossPriceChargeAmount();
    public String[] getLineItemGrossPriceChargeAmountCurrencyID();
    public String[] getLineItemGrossPriceBasisQuantity();
    public String[] getLineItemGrossPriceBasisQuantityCode();
    public Boolean[][] getLineItemGrossPriceTradeAllowanceChargeIndicator();
    public String[][] getLineItemGrossPriceTradeAllowanceChargeActualAmount();
    public String[][] getLineItemGrossPriceTradeAllowanceChargeActualAmountCurrencyID();
    public String[][] getLineItemGrossPriceTradeAllowanceChargeReason();
    public String[] getLineItemNetPriceChargeAmount();
    public String[] getLineItemNetPriceChargeAmountCurrencyID();
    public String[] getLineItemNetPriceBasisQuantity();
    public String[] getLineItemNetPriceBasisQuantityCode();
    public String[][] getLineItemSettlementTaxTypeCode();
    public String[][] getLineItemSettlementTaxExemptionReason();
    public String[][] getLineItemSettlementTaxCategoryCode();
    public String[][] getLineItemSettlementTaxApplicablePercent();
    public String[] getLineItemLineTotalAmount();
    public String[] getLineItemLineTotalAmountCurrencyID();
    public String[] getLineItemSpecifiedTradeProductGlobalID();
    public String[] getLineItemSpecifiedTradeProductSchemeID();
    public String[] getLineItemSpecifiedTradeProductSellerAssignedID();
    public String[] getLineItemSpecifiedTradeProductBuyerAssignedID();
    public String[] getLineItemSpecifiedTradeProductDescription();
}

