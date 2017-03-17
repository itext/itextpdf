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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This implementation of the BasicProfile contains member-variables that store
 * all the data needed to create an XML attachment for a ZUGFeRD invoice that
 * conforms with the Comfort profile.
 */
public class ComfortProfileImp extends BasicProfileImp implements ComfortProfile {
    
    protected List<String> notesCodes = new ArrayList<String>();
    protected String buyerReference;
    protected String sellerID;
    protected List<String> sellerGlobalID = new ArrayList<String>();
    protected List<String> sellerGlobalSchemeID = new ArrayList<String>();
    protected String buyerID;
    protected List<String> buyerGlobalID = new ArrayList<String>();
    protected List<String> buyerGlobalSchemeID = new ArrayList<String>();
    protected Date buyerOrderReferencedDocumentIssueDateTime;
    protected String buyerOrderReferencedDocumentIssueDateTimeFormat;
    protected String buyerOrderReferencedDocumentID;
    protected Date contractReferencedDocumentIssueDateTime;
    protected String contractReferencedDocumentIssueDateTimeFormat;
    protected String contractReferencedDocumentID;
    protected Date customerOrderReferencedDocumentIssueDateTime;
    protected String customerOrderReferencedDocumentIssueDateTimeFormat;
    protected String customerOrderReferencedDocumentID;
    protected Date deliveryNoteReferencedDocumentIssueDateTime;
    protected String deliveryNoteReferencedDocumentIssueDateTimeFormat;
    protected String deliveryNoteReferencedDocumentID;
    protected String invoiceeID;
    protected List<String> invoiceeGlobalID = new ArrayList<String>();
    protected List<String> invoiceeGlobalSchemeID = new ArrayList<String>();
    protected String invoiceeName;
    protected String invoiceePostcode;
    protected String invoiceeLineOne;
    protected String invoiceeLineTwo;
    protected String invoiceeCityName;
    protected String invoiceeCountryID;
    protected List<String> invoiceeTaxRegistrationID = new ArrayList<String>();
    protected List<String> invoiceeTaxRegistrationSchemeID = new ArrayList<String>();
    protected List<String> paymentMeansTypeCode = new ArrayList<String>();
    protected List<String[]> paymentMeansInformation = new ArrayList<String[]>();
    protected List<String> paymentMeansPayerAccountIBAN = new ArrayList<String>();
    protected List<String> paymentMeansPayerAccountProprietaryID = new ArrayList<String>();
    protected List<String> paymentMeansPayerFinancialInstitutionBIC = new ArrayList<String>();
    protected List<String> paymentMeansPayerFinancialInstitutionGermanBankleitzahlID = new ArrayList<String>();
    protected List<String> paymentMeansPayerFinancialInstitutionName = new ArrayList<String>();
    protected List<String> taxExemptionReason = new ArrayList<String>();
    protected List<String> taxCategoryCode = new ArrayList<String>();
    protected Date billingStartDateTime;
    protected String billingStartDateTimeFormat;
    protected Date billingEndDateTime;
    protected String billingEndDateTimeFormat;
    protected List<Boolean> tradeAllowanceChargeIndicator = new ArrayList<Boolean>();
    protected List<String> tradeAllowanceChargeActualAmount = new ArrayList<String>();
    protected List<String> tradeAllowanceChargeActualAmountCurrency = new ArrayList<String>();
    protected List<String> tradeAllowanceChargeReason = new ArrayList<String>();
    protected List<String[]> tradeAllowanceChargeTaxTypeCode = new ArrayList<String[]>();
    protected List<String[]> tradeAllowanceChargeTaxCategoryCode = new ArrayList<String[]>();
    protected List<String[]> tradeAllowanceChargeTaxApplicablePercent = new ArrayList<String[]>();
    protected List<String[]> logisticsServiceChargeDescription = new ArrayList<String[]>();
    protected List<String> logisticsServiceChargeAmount = new ArrayList<String>();
    protected List<String> logisticsServiceChargeAmountCurrency = new ArrayList<String>();
    protected List<String[]> logisticsServiceChargeTaxTypeCode = new ArrayList<String[]>();
    protected List<String[]> logisticsServiceChargeTaxCategoryCode = new ArrayList<String[]>();
    protected List<String[]> logisticsServiceChargeTaxApplicablePercent = new ArrayList<String[]>();
    protected List<String[]> tradePaymentTermsInformation = new ArrayList<String[]>();
    protected List<Date> tradePaymentTermsDueDateTime = new ArrayList<Date>();
    protected List<String> tradePaymentTermsDueDateTimeFormat = new ArrayList<String>();
    protected String totalPrepaidAmount;
    protected String totalPrepaidAmountCurrencyID;
    protected String duePayableAmount;
    protected String duePayableAmountCurrencyID;
    protected List<String> lineItemLineID = new ArrayList<String>();
    protected List<String[][]> lineItemIncludedNote = new ArrayList<String[][]>();
    protected List<String> lineItemGrossPriceChargeAmount = new ArrayList<String>();
    protected List<String> lineItemGrossPriceChargeAmountCurrencyID = new ArrayList<String>();
    protected List<String> lineItemGrossPriceBasisQuantity = new ArrayList<String>();
    protected List<String> lineItemGrossPriceBasisQuantityCode = new ArrayList<String>();
    protected List<Boolean[]> lineItemGrossPriceTradeAllowanceChargeIndicator = new ArrayList<Boolean[]>();
    protected List<String[]> lineItemGrossPriceTradeAllowanceChargeActualAmount = new ArrayList<String[]>();
    protected List<String[]> lineItemGrossPriceTradeAllowanceChargeActualAmountCurrencyID = new ArrayList<String[]>();
    protected List<String[]> lineItemGrossPriceTradeAllowanceChargeReason = new ArrayList<String[]>();
    protected List<String> lineItemNetPriceChargeAmount = new ArrayList<String>();
    protected List<String> lineItemNetPriceChargeAmountCurrencyID = new ArrayList<String>();
    protected List<String> lineItemNetPriceBasisQuantity = new ArrayList<String>();
    protected List<String> lineItemNetPriceBasisQuantityCode = new ArrayList<String>();
    protected List<String[]> lineItemSettlementTaxTypeCode = new ArrayList<String[]>();
    protected List<String[]> lineItemSettlementTaxExemptionReason = new ArrayList<String[]>();
    protected List<String[]> lineItemSettlementTaxCategoryCode = new ArrayList<String[]>();
    protected List<String[]> lineItemSettlementTaxApplicablePercent = new ArrayList<String[]>();
    protected List<String> lineItemLineTotalAmount = new ArrayList<String>();
    protected List<String> lineItemLineTotalAmountCurrencyID = new ArrayList<String>();
    protected List<String> lineItemSpecifiedTradeProductGlobalID = new ArrayList<String>();
    protected List<String> lineItemSpecifiedTradeProductSchemeID = new ArrayList<String>();
    protected List<String> lineItemSpecifiedTradeProductSellerAssignedID = new ArrayList<String>();
    protected List<String> lineItemSpecifiedTradeProductBuyerAssignedID = new ArrayList<String>();
    protected List<String> lineItemSpecifiedTradeProductDescription = new ArrayList<String>();
            
    public String[] getNotesCodes() {
        return to1DArray(notesCodes);
    }

    public String getBuyerReference() {
        return buyerReference;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String[] getSellerGlobalID() {
        return to1DArray(sellerGlobalID);
    }

    public String[] getSellerGlobalSchemeID() {
        return to1DArray(sellerGlobalSchemeID);
    }

    public String getBuyerID() {
        return buyerID;
    }

    public String[] getBuyerGlobalID() {
        return to1DArray(buyerGlobalID);
    }

    public String[] getBuyerGlobalSchemeID() {
        return to1DArray(buyerGlobalSchemeID);
    }

    public Date getBuyerOrderReferencedDocumentIssueDateTime() {
        return buyerOrderReferencedDocumentIssueDateTime;
    }

    public String getBuyerOrderReferencedDocumentIssueDateTimeFormat() {
        return buyerOrderReferencedDocumentIssueDateTimeFormat;
    }

    public String getBuyerOrderReferencedDocumentID() {
        return buyerOrderReferencedDocumentID;
    }

    public Date getContractReferencedDocumentIssueDateTime() {
        return contractReferencedDocumentIssueDateTime;
    }

    public String getContractReferencedDocumentIssueDateTimeFormat() {
        return contractReferencedDocumentIssueDateTimeFormat;
    }

    public String getContractReferencedDocumentID() {
        return contractReferencedDocumentID;
    }

    public Date getCustomerOrderReferencedDocumentIssueDateTime() {
        return customerOrderReferencedDocumentIssueDateTime;
    }

    public String getCustomerOrderReferencedDocumentIssueDateTimeFormat() {
        return customerOrderReferencedDocumentIssueDateTimeFormat;
    }

    public String getCustomerOrderReferencedDocumentID() {
        return customerOrderReferencedDocumentID;
    }

    public Date getDeliveryNoteReferencedDocumentIssueDateTime() {
        return deliveryNoteReferencedDocumentIssueDateTime;
    }

    public String getDeliveryNoteReferencedDocumentIssueDateTimeFormat() {
        return deliveryNoteReferencedDocumentIssueDateTimeFormat;
    }

    public String getDeliveryNoteReferencedDocumentID() {
        return deliveryNoteReferencedDocumentID;
    }
    public String getInvoiceeID() {
        return invoiceeID;
    }
    
    public String[] getInvoiceeGlobalID() {
        return to1DArray(invoiceeGlobalID);
    }

    public String[] getInvoiceeGlobalSchemeID() {
        return to1DArray(invoiceeGlobalSchemeID);
    }
    
    public String getInvoiceeName() {
        return invoiceeName;
    }
    
    public String getInvoiceePostcode() {
        return invoiceePostcode;
    }
    
    public String getInvoiceeLineOne() {
        return invoiceeLineOne;
    }
    
    public String getInvoiceeLineTwo() {
        return invoiceeLineTwo;
    }
    
    public String getInvoiceeCityName() {
        return invoiceeCityName;
    }
    
    public String getInvoiceeCountryID() {
        return invoiceeCountryID;
    }
    
    public String[] getInvoiceeTaxRegistrationID() {
        return to1DArray(invoiceeTaxRegistrationID);
    }
    
    public String[] getInvoiceeTaxRegistrationSchemeID() {
        return to1DArray(invoiceeTaxRegistrationSchemeID);
    }

    public String[] getPaymentMeansTypeCode() {
        return to1DArray(paymentMeansTypeCode);
    }
    
    public String[][] getPaymentMeansInformation() {
        return to2DArray(paymentMeansInformation);
    }
    
    public String[] getPaymentMeansPayerAccountIBAN() {
        return to1DArray(paymentMeansPayerAccountIBAN);
    }
    
    public String[] getPaymentMeansPayerAccountProprietaryID() {
        return to1DArray(paymentMeansPayerAccountProprietaryID);
    }
    
    public String[] getPaymentMeansPayerFinancialInstitutionBIC() {
        return to1DArray(paymentMeansPayerFinancialInstitutionBIC);
    }
    
    public String[] getPaymentMeansPayerFinancialInstitutionGermanBankleitzahlID() {
        return to1DArray(paymentMeansPayerFinancialInstitutionGermanBankleitzahlID);
    }
    
    public String[] getPaymentMeansPayerFinancialInstitutionName() {
        return to1DArray(paymentMeansPayerFinancialInstitutionName);
    }

    public String[] getTaxExemptionReason() {
        return to1DArray(taxExemptionReason);
    }

    public String[] getTaxCategoryCode() {
        return to1DArray(taxCategoryCode);
    }
    
    public Date getBillingStartDateTime() {
        return billingStartDateTime;
    }
    
    public String getBillingStartDateTimeFormat() {
        return billingStartDateTimeFormat;
    }
    
    public Date getBillingEndDateTime() {
        return billingEndDateTime;
    }
    
    public String getBillingEndDateTimeFormat() {
        return billingEndDateTimeFormat;
    }
    
    public Boolean[] getSpecifiedTradeAllowanceChargeIndicator() {
        return to1DArrayB(tradeAllowanceChargeIndicator);
    }
    
    public String[] getSpecifiedTradeAllowanceChargeActualAmount() {
        return to1DArray(tradeAllowanceChargeActualAmount);
    }
    
    public String[] getSpecifiedTradeAllowanceChargeActualAmountCurrency() {
        return to1DArray(tradeAllowanceChargeActualAmountCurrency);
    }
    
    public String[] getSpecifiedTradeAllowanceChargeReason() {
        return to1DArray(tradeAllowanceChargeReason);
    }

    public String[][] getSpecifiedTradeAllowanceChargeTaxTypeCode() {
        return to2DArray(tradeAllowanceChargeTaxTypeCode);
    }
    
    public String[][] getSpecifiedTradeAllowanceChargeTaxCategoryCode() {
        return to2DArray(tradeAllowanceChargeTaxCategoryCode);
    }
    
    public String[][] getSpecifiedTradeAllowanceChargeTaxApplicablePercent() {
        return to2DArray(tradeAllowanceChargeTaxApplicablePercent);
    }
    
    public String[][] getSpecifiedLogisticsServiceChargeDescription() {
        return to2DArray(logisticsServiceChargeDescription);
    }
    public String[] getSpecifiedLogisticsServiceChargeAmount() {
        return to1DArray(logisticsServiceChargeAmount);
    }
    public String[] getSpecifiedLogisticsServiceChargeAmountCurrency() {
        return to1DArray(logisticsServiceChargeAmountCurrency);
    }
    public String[][] getSpecifiedLogisticsServiceChargeTaxTypeCode() {
        return to2DArray(logisticsServiceChargeTaxTypeCode);
    }
    public String[][] getSpecifiedLogisticsServiceChargeTaxCategoryCode() {
        return to2DArray(logisticsServiceChargeTaxCategoryCode);
    }
    public String[][] getSpecifiedLogisticsServiceChargeTaxApplicablePercent() {
        return to2DArray(logisticsServiceChargeTaxApplicablePercent);
    }
    
    public String[][] getSpecifiedTradePaymentTermsDescription() {
        return to2DArray(tradePaymentTermsInformation);
    }
    public Date[] getSpecifiedTradePaymentTermsDueDateTime() {
        return (Date[]) tradePaymentTermsDueDateTime.toArray(new Date[tradePaymentTermsDueDateTime.size()]);
    }
    public String[] getSpecifiedTradePaymentTermsDueDateTimeFormat() {
        return to1DArray(tradePaymentTermsDueDateTimeFormat);
    }
    public String getTotalPrepaidAmount() {
        return totalPrepaidAmount;
    }
    public String getTotalPrepaidAmountCurrencyID() {
        return totalPrepaidAmountCurrencyID;
    }
    public String getDuePayableAmount() {
        return duePayableAmount;
    }
    public String getDuePayableAmountCurrencyID() {
        return duePayableAmountCurrencyID;
    }
    public String[] getLineItemLineID() {
        return to1DArray(lineItemLineID);
    }
    public String[][][] getLineItemIncludedNote() {
        return to3DArray(lineItemIncludedNote);
    }
    
    public String[] getLineItemGrossPriceChargeAmount() {
        return to1DArray(lineItemGrossPriceChargeAmount);
    }
    public String[] getLineItemGrossPriceChargeAmountCurrencyID() {
        return to1DArray(lineItemGrossPriceChargeAmountCurrencyID);
    }
    public String[] getLineItemGrossPriceBasisQuantity() {
        return to1DArray(lineItemGrossPriceBasisQuantity);
    }
    public String[] getLineItemGrossPriceBasisQuantityCode() {
        return to1DArray(lineItemGrossPriceBasisQuantityCode);
    }
    public Boolean[][] getLineItemGrossPriceTradeAllowanceChargeIndicator() {
        return to2DArrayB(lineItemGrossPriceTradeAllowanceChargeIndicator);
    }
    public String[][] getLineItemGrossPriceTradeAllowanceChargeActualAmount() {
        return to2DArray(lineItemGrossPriceTradeAllowanceChargeActualAmount);
    }
    public String[][] getLineItemGrossPriceTradeAllowanceChargeActualAmountCurrencyID() {
        return to2DArray(lineItemGrossPriceTradeAllowanceChargeActualAmountCurrencyID);
    }
    public String[][] getLineItemGrossPriceTradeAllowanceChargeReason() {
        return to2DArray(lineItemGrossPriceTradeAllowanceChargeReason);
    }
    public String[] getLineItemNetPriceChargeAmount() {
        return to1DArray(lineItemNetPriceChargeAmount);
    }
    public String[] getLineItemNetPriceChargeAmountCurrencyID() {
        return to1DArray(lineItemNetPriceChargeAmountCurrencyID);
    }
    public String[] getLineItemNetPriceBasisQuantity() {
        return to1DArray(lineItemNetPriceBasisQuantity);
    }
    public String[] getLineItemNetPriceBasisQuantityCode() {
        return to1DArray(lineItemNetPriceBasisQuantityCode);
    }
    
    public String[][] getLineItemSettlementTaxTypeCode() {
        return to2DArray(lineItemSettlementTaxTypeCode);
    }
    public String[][] getLineItemSettlementTaxExemptionReason() {
        return to2DArray(lineItemSettlementTaxExemptionReason);
    }
    public String[][] getLineItemSettlementTaxCategoryCode() {
        return to2DArray(lineItemSettlementTaxCategoryCode);
    }
    public String[][] getLineItemSettlementTaxApplicablePercent() {
        return to2DArray(lineItemSettlementTaxApplicablePercent);
    }
    public String[] getLineItemLineTotalAmount() {
        return to1DArray(lineItemLineTotalAmount);
    }
    public String[] getLineItemLineTotalAmountCurrencyID() {
        return to1DArray(lineItemLineTotalAmountCurrencyID);
    }
    public String[] getLineItemSpecifiedTradeProductGlobalID() {
        return to1DArray(lineItemSpecifiedTradeProductGlobalID);
    }
    public String[] getLineItemSpecifiedTradeProductSchemeID() {
        return to1DArray(lineItemSpecifiedTradeProductSchemeID);
    }
    public String[] getLineItemSpecifiedTradeProductSellerAssignedID() {
        return to1DArray(lineItemSpecifiedTradeProductSellerAssignedID);
    }
    public String[] getLineItemSpecifiedTradeProductBuyerAssignedID() {
        return to1DArray(lineItemSpecifiedTradeProductBuyerAssignedID);
    }
    public String[] getLineItemSpecifiedTradeProductDescription() {
        return to1DArray(lineItemSpecifiedTradeProductDescription);
    }
    
    @Override
    public void addNote(String[] note) {
        throw new UnsupportedOperationException("This method can only be used for the BASIC level.");
    }
    
    public void addNote(String[] note, String code) {
        notes.add(note);
        notesCodes.add(code);
    }

    public void setBuyerReference(String buyerReference) {
        this.buyerReference = buyerReference;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public void addSellerGlobalID(String sellerGlobalSchemeID, String sellerGlobalID) {
        this.sellerGlobalID.add(sellerGlobalID);
        this.sellerGlobalSchemeID.add(sellerGlobalSchemeID);
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public void addBuyerGlobalID(String buyerGlobalSchemeID, String buyerGlobalID) {
        this.buyerGlobalID.add(buyerGlobalID);
        this.buyerGlobalSchemeID.add(buyerGlobalSchemeID);
    }
    
    public void setBuyerOrderReferencedDocumentIssueDateTime(Date buyerOrderReferencedDocumentIssueDateTime, String buyerOrderReferencedDocumentIssueDateTimeFormat) {
        this.buyerOrderReferencedDocumentIssueDateTime = buyerOrderReferencedDocumentIssueDateTime;
        this.buyerOrderReferencedDocumentIssueDateTimeFormat = buyerOrderReferencedDocumentIssueDateTimeFormat;
    }

    public void setBuyerOrderReferencedDocumentID(String buyerOrderReferencedDocumentID) {
        this.buyerOrderReferencedDocumentID = buyerOrderReferencedDocumentID;
    }

    public void setContractReferencedDocumentIssueDateTime(Date contractReferencedDocumentIssueDateTime, String contractReferencedDocumentIssueDateTimeFormat) {
        this.contractReferencedDocumentIssueDateTime = contractReferencedDocumentIssueDateTime;
        this.contractReferencedDocumentIssueDateTimeFormat = contractReferencedDocumentIssueDateTimeFormat;
    }

    public void setContractReferencedDocumentID(String contractReferencedDocumentID) {
        this.contractReferencedDocumentID = contractReferencedDocumentID;
    }

    public void setCustomerOrderReferencedDocumentIssueDateTime(Date customerOrderReferencedDocumentIssueDateTime, String customerOrderReferencedDocumentIssueDateTimeFormat) {
        this.customerOrderReferencedDocumentIssueDateTime = customerOrderReferencedDocumentIssueDateTime;
        this.customerOrderReferencedDocumentIssueDateTimeFormat = customerOrderReferencedDocumentIssueDateTimeFormat;
    }

    public void setCustomerOrderReferencedDocumentID(String customerOrderReferencedDocumentID) {
        this.customerOrderReferencedDocumentID = customerOrderReferencedDocumentID;
    }

    public void setDeliveryNoteReferencedDocumentIssueDateTime(Date deliveryNoteReferencedDocumentIssueDateTime, String deliveryNoteReferencedDocumentIssueDateTimeFormat) {
        this.deliveryNoteReferencedDocumentIssueDateTime = deliveryNoteReferencedDocumentIssueDateTime;
        this.deliveryNoteReferencedDocumentIssueDateTimeFormat = deliveryNoteReferencedDocumentIssueDateTimeFormat;
    }

    public void setDeliveryNoteReferencedDocumentID(String deliveryNoteReferencedDocumentID) {
        this.deliveryNoteReferencedDocumentID = deliveryNoteReferencedDocumentID;
    }
    public void setInvoiceeID(String invoiceeID) {
        this.invoiceeID = invoiceeID;
    }
    
    public void addInvoiceeGlobalID(String invoiceeGlobalSchemeID, String invoiceeGlobalID) {
        this.invoiceeGlobalSchemeID.add(invoiceeGlobalSchemeID);
        this.invoiceeGlobalID.add(invoiceeGlobalID);
    }
    
    public void setInvoiceeName(String invoiceeName) {
        this.invoiceeName = invoiceeName;
    }
    
    public void setInvoiceePostcode(String invoiceePostcode) {
        this.invoiceePostcode = invoiceePostcode;
    }
    
    public void setInvoiceeLineOne(String invoiceeLineOne) {
        this.invoiceeLineOne = invoiceeLineOne;
    }
    
    public void setInvoiceeLineTwo(String invoiceeLineTwo) {
        this.invoiceeLineTwo = invoiceeLineTwo;
    }
    
    public void setInvoiceeCityName(String invoiceeCityName) {
        this.invoiceeCityName = invoiceeCityName;
    }
    
    public void setInvoiceeCountryID(String invoiceeCountryID) {
        this.invoiceeCountryID = invoiceeCountryID;
    }
    
    public void addInvoiceeTaxRegistration(String schemeID, String taxId) {
        invoiceeTaxRegistrationSchemeID.add(schemeID);
        invoiceeTaxRegistrationID.add(taxId);
    }

    @Override
    public void addPaymentMeans(String schemeAgencyID, String id, String iban, String accountname, String proprietaryID, String bic, String germanBankleitzahlID, String institutionname) {
        throw new UnsupportedOperationException("This method can only be used for the BASIC level.");
    }

    public void addPaymentMeans(
            String typeCode, String[] information,
            String schemeAgencyID, String id,
            String ibanDebtor, String proprietaryIDDebtor,
            String ibanCreditor, String accountnameCreditor, String proprietaryIDCreditor,
            String bicDebtor, String germanBankleitzahlIDDebtor, String institutionnameDebtor,
            String bicCreditor, String germanBankleitzahlIDCreditor, String institutionnameCreditor
    ) {
        paymentMeansTypeCode.add(typeCode);
        paymentMeansInformation.add(information);
        paymentMeansID.add(id);
        paymentMeansSchemeAgencyID.add(schemeAgencyID);
        paymentMeansPayerAccountIBAN.add(ibanDebtor);
        paymentMeansPayerAccountProprietaryID.add(proprietaryIDDebtor);
        paymentMeansPayeeAccountIBAN.add(ibanCreditor);
        paymentMeansPayeeAccountName.add(accountnameCreditor);
        paymentMeansPayeeAccountProprietaryID.add(proprietaryIDCreditor);
        paymentMeansPayerFinancialInstitutionBIC.add(bicDebtor);
        paymentMeansPayerFinancialInstitutionGermanBankleitzahlID.add(germanBankleitzahlIDDebtor);
        paymentMeansPayerFinancialInstitutionName.add(institutionnameDebtor);
        paymentMeansPayeeFinancialInstitutionBIC.add(bicCreditor);
        paymentMeansPayeeFinancialInstitutionGermanBankleitzahlID.add(germanBankleitzahlIDCreditor);
        paymentMeansPayeeFinancialInstitutionName.add(institutionnameCreditor);
    }

    @Override
    public void addApplicableTradeTax(String calculatedAmount, String calculatedAmountCurrencyID, String typeCode,
            String basisAmount, String basisAmountCurrencyID, String applicablePercent) {
        throw new UnsupportedOperationException("This method can only be used for the BASIC level.");
    }

    public void addApplicableTradeTax(String calculatedAmount, String calculatedAmountCurrencyID, String typeCode,
            String exemptionReason, String basisAmount, String basisAmountCurrencyID, String categoryCode, String applicablePercent) {
        taxCalculatedAmount.add(calculatedAmount);
        taxCalculatedAmountCurrencyID.add(calculatedAmountCurrencyID);
        taxTypeCode.add(typeCode);
        taxExemptionReason.add(exemptionReason);
        taxBasisAmount.add(basisAmount);
        taxBasisAmountCurrencyID.add(basisAmountCurrencyID);
        taxCategoryCode.add(categoryCode);
        taxApplicablePercent.add(applicablePercent);
    }
    
    public void setBillingStartEnd(Date billingStartDateTime, String billingStartDateTimeFormat,
            Date billingEndDateTime, String billingEndDateTimeFormat) {
        this.billingStartDateTime = billingStartDateTime;
        this.billingStartDateTimeFormat = billingStartDateTimeFormat;
        this.billingEndDateTime = billingEndDateTime;
        this.billingEndDateTimeFormat = billingEndDateTimeFormat;
    }
    
    public void addSpecifiedTradeAllowanceCharge(boolean indicator, String actualAmount, String actualAmountCurrency,
            String reason, String[] typeCodes, String[] categoryCodes, String[] applicablePercent) {
        this.tradeAllowanceChargeIndicator.add(indicator);
        this.tradeAllowanceChargeActualAmount.add(actualAmount);
        this.tradeAllowanceChargeActualAmountCurrency.add(actualAmountCurrency);
        this.tradeAllowanceChargeReason.add(reason);
        this.tradeAllowanceChargeTaxTypeCode.add(typeCodes);
        this.tradeAllowanceChargeTaxCategoryCode.add(categoryCodes);
        this.tradeAllowanceChargeTaxApplicablePercent.add(applicablePercent);
    }
    
    public void addSpecifiedLogisticsServiceCharge(String[] description, String actualAmount, String actualAmountCurrency,
            String[] typeCodes, String[] categoryCodes, String[] applicablePercent) {
        this.logisticsServiceChargeDescription.add(description);
        this.logisticsServiceChargeAmount.add(actualAmount);
        this.logisticsServiceChargeAmountCurrency.add(actualAmountCurrency);
        this.logisticsServiceChargeTaxTypeCode.add(typeCodes);
        this.logisticsServiceChargeTaxCategoryCode.add(categoryCodes);
        this.logisticsServiceChargeTaxApplicablePercent.add(applicablePercent);
    }
    
    public void addSpecifiedTradePaymentTerms(String[] information, Date dateTime, String dateTimeFormat) {
        this.tradePaymentTermsInformation.add(information);
        this.tradePaymentTermsDueDateTime.add(dateTime);
        this.tradePaymentTermsDueDateTimeFormat.add(dateTimeFormat);
    }
    
    public void setTotalPrepaidAmount(String totalPrepaidAmount, String totalPrepaidCurrencyID) {
        this.totalPrepaidAmount = totalPrepaidAmount;
        this.totalPrepaidAmountCurrencyID = totalPrepaidCurrencyID;
    }
    public void setDuePayableAmount(String duePayableAmount, String duePayableAmountCurrencyID) {
        this.duePayableAmount = duePayableAmount;
        this.duePayableAmountCurrencyID = duePayableAmountCurrencyID;
    }
    
    @Override
    public void addIncludedSupplyChainTradeLineItem(String billedQuantity, String billedQuantityUnitCode, String specifiedTradeProductName) {
        throw new UnsupportedOperationException("This method can only be used for the BASIC level.");
    }
    
    public void addIncludedSupplyChainTradeLineItem(String id, String[][] notes,
            String grossPriceChargeAmount, String grossPriceChargeAmountCurrencyID,
            String grossPriceBasisQuantity, String grossPriceBasisQuantityCode,
            Boolean[] grossPriceTradeAllowanceChargeIndicator,
            String[] grossPriceTradeAllowanceChargeActualAmount,
            String[] grossPriceTradeAllowanceChargeActualAmountCurrencyID,
            String[] grossPriceTradeAllowanceChargeReason,
            String netPriceChargeAmount, String netPriceChargeAmountCurrencyID,
            String netPriceBasisQuantity, String netPriceBasisQuantityCode,
            String billedQuantity, String billedQuantityUnitCode,
            String[] lineItemSettlementTaxTypeCode,
            String[] lineItemSettlementTaxExemptionReason,
            String[] lineItemSettlementTaxCategoryCode,
            String[] lineItemSettlementTaxApplicablePercent,
            String lineItemLineTotalAmount,
            String lineItemLineTotalAmountCurrencyID,
            String lineItemSpecifiedTradeProductGlobalID,
            String lineItemSpecifiedTradeProductSchemeID,
            String lineItemSpecifiedTradeProductSellerAssignedID,
            String lineItemSpecifiedTradeProductBuyerAssignedID,
            String lineItemSpecifiedTradeProductName,
            String lineItemSpecifiedTradeProductDescription) {
        this.lineItemLineID.add(id);
        this.lineItemIncludedNote.add(notes);
        this.lineItemGrossPriceChargeAmount.add(grossPriceChargeAmount);
        this.lineItemGrossPriceChargeAmountCurrencyID.add(grossPriceChargeAmountCurrencyID);
        this.lineItemGrossPriceBasisQuantity.add(grossPriceBasisQuantity);
        this.lineItemGrossPriceBasisQuantityCode.add(grossPriceBasisQuantityCode);
        this.lineItemGrossPriceTradeAllowanceChargeIndicator.add(grossPriceTradeAllowanceChargeIndicator);
        this.lineItemGrossPriceTradeAllowanceChargeActualAmount.add(grossPriceTradeAllowanceChargeActualAmount);
        this.lineItemGrossPriceTradeAllowanceChargeActualAmountCurrencyID.add(grossPriceTradeAllowanceChargeActualAmountCurrencyID);
        this.lineItemGrossPriceTradeAllowanceChargeReason.add(grossPriceTradeAllowanceChargeReason);
        this.lineItemNetPriceChargeAmount.add(netPriceChargeAmount);
        this.lineItemNetPriceChargeAmountCurrencyID.add(netPriceChargeAmountCurrencyID);
        this.lineItemNetPriceBasisQuantity.add(netPriceBasisQuantity);
        this.lineItemNetPriceBasisQuantityCode.add(netPriceBasisQuantityCode);
        this.lineItemBilledQuantity.add(billedQuantity);
        this.lineItemBilledQuantityUnitCode.add(billedQuantityUnitCode);
        this.lineItemSettlementTaxTypeCode.add(lineItemSettlementTaxTypeCode);
        this.lineItemSettlementTaxExemptionReason.add(lineItemSettlementTaxExemptionReason);
        this.lineItemSettlementTaxCategoryCode.add(lineItemSettlementTaxCategoryCode);
        this.lineItemSettlementTaxApplicablePercent.add(lineItemSettlementTaxApplicablePercent);
        this.lineItemLineTotalAmount.add(lineItemLineTotalAmount);
        this.lineItemLineTotalAmountCurrencyID.add(lineItemLineTotalAmountCurrencyID);
        this.lineItemSpecifiedTradeProductGlobalID.add(lineItemSpecifiedTradeProductGlobalID);
        this.lineItemSpecifiedTradeProductSchemeID.add(lineItemSpecifiedTradeProductSchemeID);
        this.lineItemSpecifiedTradeProductSellerAssignedID.add(lineItemSpecifiedTradeProductSellerAssignedID);
        this.lineItemSpecifiedTradeProductBuyerAssignedID.add(lineItemSpecifiedTradeProductBuyerAssignedID);
        this.lineItemSpecifiedTradeProductName.add(lineItemSpecifiedTradeProductName);
        this.lineItemSpecifiedTradeProductDescription.add(lineItemSpecifiedTradeProductDescription);
    }
}

