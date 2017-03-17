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
 * conforms with the Basic profile.
 */
public class BasicProfileImp implements BasicProfile {
    
    // member-variables storing all the data
    protected boolean test = true;
    protected String id;
    protected String name;
    protected String typeCode;
    protected Date date;
    protected String dateFormat;
    protected List<String[]> notes = new ArrayList<String[]>();
    protected String sellerName;
    protected String sellerPostcode;
    protected String sellerLineOne;
    protected String sellerLineTwo;
    protected String sellerCityName;
    protected String sellerCountryID;
    protected List<String> sellerTaxRegistrationID = new ArrayList<String>();
    protected List<String> sellerTaxRegistrationSchemeID = new ArrayList<String>();
    protected String buyerName;
    protected String buyerPostcode;
    protected String buyerLineOne;
    protected String buyerLineTwo;
    protected String buyerCityName;
    protected String buyerCountryID;
    protected List<String> buyerTaxRegistrationID = new ArrayList<String>();
    protected List<String> buyerTaxRegistrationSchemeID = new ArrayList<String>();
    protected Date deliveryDate;
    protected String deliveryDateFormat;
    protected String paymentReference;
    protected String invoiceCurrencyCode;
    protected List<String> paymentMeansID = new ArrayList<String>();
    protected List<String> paymentMeansSchemeAgencyID = new ArrayList<String>();
    protected List<String> paymentMeansPayeeAccountIBAN = new ArrayList<String>();
    protected List<String> paymentMeansPayeeAccountName = new ArrayList<String>();
    protected List<String> paymentMeansPayeeAccountProprietaryID = new ArrayList<String>();
    protected List<String> paymentMeansPayeeFinancialInstitutionBIC = new ArrayList<String>();
    protected List<String> paymentMeansPayeeFinancialInstitutionGermanBankleitzahlID = new ArrayList<String>();
    protected List<String> paymentMeansPayeeFinancialInstitutionName = new ArrayList<String>();
    protected List<String> taxCalculatedAmount = new ArrayList<String>();
    protected List<String> taxCalculatedAmountCurrencyID = new ArrayList<String>();
    protected List<String> taxTypeCode = new ArrayList<String>();
    protected List<String> taxBasisAmount = new ArrayList<String>();
    protected List<String> taxBasisAmountCurrencyID = new ArrayList<String>();
    protected List<String> taxApplicablePercent = new ArrayList<String>();
    protected String lineTotalAmount;
    protected String lineTotalAmountCurrencyID;
    protected String chargeTotalAmount;
    protected String chargeTotalAmountCurrencyID;
    protected String allowanceTotalAmount;
    protected String allowanceTotalAmountCurrencyID;
    protected String taxBasisTotalAmount;
    protected String taxBasisTotalAmountCurrencyID;
    protected String taxTotalAmount;
    protected String taxTotalAmountCurrencyID;
    protected String grandTotalAmount;
    protected String grandTotalAmountCurrencyID;
    protected List<String> lineItemBilledQuantity = new ArrayList<String>();
    protected List<String> lineItemBilledQuantityUnitCode = new ArrayList<String>();
    protected List<String> lineItemSpecifiedTradeProductName = new ArrayList<String>();
    
    // implementation of the getters
    public boolean getTestIndicator() {
        return test;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public Date getDateTime() {
        return date;
    }

    public String getDateTimeFormat() {
        return dateFormat;
    }

    public String[][] getNotes() {
        return to2DArray(notes);
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getSellerPostcode() {
        return sellerPostcode;
    }

    public String getSellerLineOne() {
        return sellerLineOne;
    }

    public String getSellerLineTwo() {
        return sellerLineTwo;
    }

    public String getSellerCityName() {
        return sellerCityName;
    }

    public String getSellerCountryID() {
        return sellerCountryID;
    }

    public String[] getSellerTaxRegistrationID() {
        return to1DArray(sellerTaxRegistrationID);
    }

    public String[] getSellerTaxRegistrationSchemeID() {
        return to1DArray(sellerTaxRegistrationSchemeID);
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerPostcode() {
        return buyerPostcode;
    }

    public String getBuyerLineOne() {
        return buyerLineOne;
    }

    public String getBuyerLineTwo() {
        return buyerLineTwo;
    }

    public String getBuyerCityName() {
        return buyerCityName;
    }

    public String getBuyerCountryID() {
        return buyerCountryID;
    }

    public String[] getBuyerTaxRegistrationID() {
        return to1DArray(buyerTaxRegistrationID);
    }

    public String[] getBuyerTaxRegistrationSchemeID() {
        return to1DArray(buyerTaxRegistrationSchemeID);
    }

    public Date getDeliveryDateTime() {
        return deliveryDate;
    }

    public String getDeliveryDateTimeFormat() {
        return deliveryDateFormat;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public String getInvoiceCurrencyCode() {
        return invoiceCurrencyCode;
    }

    public String[] getPaymentMeansID() {
        return to1DArray(paymentMeansID);
    }

    public String[] getPaymentMeansSchemeAgencyID() {
        return to1DArray(paymentMeansSchemeAgencyID);
    }

    public String[] getPaymentMeansPayeeAccountIBAN() {
        return to1DArray(paymentMeansPayeeAccountIBAN);
    }

    public String[] getPaymentMeansPayeeAccountAccountName() {
        return to1DArray(paymentMeansPayeeAccountName);
    }

    public String[] getPaymentMeansPayeeAccountProprietaryID() {
        return to1DArray(paymentMeansPayeeAccountProprietaryID);
    }

    public String[] getPaymentMeansPayeeFinancialInstitutionBIC() {
        return to1DArray(paymentMeansPayeeFinancialInstitutionBIC);
    }

    public String[] getPaymentMeansPayeeFinancialInstitutionGermanBankleitzahlID() {
        return to1DArray(paymentMeansPayeeFinancialInstitutionGermanBankleitzahlID);
    }

    public String[] getPaymentMeansPayeeFinancialInstitutionName() {
        return to1DArray(paymentMeansPayeeFinancialInstitutionName);
    }

    public String[] getTaxCalculatedAmount() {
        return to1DArray(taxCalculatedAmount);
    }

    public String[] getTaxCalculatedAmountCurrencyID() {
        return to1DArray(taxCalculatedAmountCurrencyID);
    }

    public String[] getTaxTypeCode() {
        return to1DArray(taxTypeCode);
    }

    public String[] getTaxBasisAmount() {
        return to1DArray(taxBasisAmount);
    }

    public String[] getTaxBasisAmountCurrencyID() {
        return to1DArray(taxBasisAmountCurrencyID);
    }

    public String[] getTaxApplicablePercent() {
        return to1DArray(taxApplicablePercent);
    }

    public String getLineTotalAmount() {
        return lineTotalAmount;
    }

    public String getLineTotalAmountCurrencyID() {
        return lineTotalAmountCurrencyID;
    }

    public String getChargeTotalAmount() {
        return chargeTotalAmount;
    }

    public String getChargeTotalAmountCurrencyID() {
        return chargeTotalAmountCurrencyID;
    }

    public String getAllowanceTotalAmount() {
        return allowanceTotalAmount;
    }

    public String getAllowanceTotalAmountCurrencyID() {
        return allowanceTotalAmountCurrencyID;
    }

    public String getTaxBasisTotalAmount() {
        return taxBasisTotalAmount;
    }

    public String getTaxBasisTotalAmountCurrencyID() {
        return taxBasisTotalAmountCurrencyID;
    }

    public String getTaxTotalAmount() {
        return taxTotalAmount;
    }

    public String getTaxTotalAmountCurrencyID() {
        return taxTotalAmountCurrencyID;
    }

    public String getGrandTotalAmount() {
        return grandTotalAmount;
    }

    public String getGrandTotalAmountCurrencyID() {
        return grandTotalAmountCurrencyID;
    }

    public String[] getLineItemBilledQuantity() {
        return to1DArray(lineItemBilledQuantity);
    }

    public String[] getLineItemBilledQuantityUnitCode() {
        return to1DArray(lineItemBilledQuantityUnitCode);
    }

    public String[] getLineItemSpecifiedTradeProductName() {
        return to1DArray(lineItemSpecifiedTradeProductName);
    }

    // implementation of the setters
    public void setTest(boolean test) {
        this.test = test;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public void setDate(Date date, String dateFormat) {
        this.date = date;
        this.dateFormat = dateFormat;
    }

    public void addNote(String[] note) {
        notes.add(note);
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setSellerPostcode(String sellerPostcode) {
        this.sellerPostcode = sellerPostcode;
    }

    public void setSellerLineOne(String sellerLineOne) {
        this.sellerLineOne = sellerLineOne;
    }

    public void setSellerLineTwo(String sellerLineTwo) {
        this.sellerLineTwo = sellerLineTwo;
    }

    public void setSellerCityName(String sellerCityName) {
        this.sellerCityName = sellerCityName;
    }

    public void setSellerCountryID(String sellerCountryID) {
        this.sellerCountryID = sellerCountryID;
    }

    public void addSellerTaxRegistration(String schemeID, String taxId) {
        sellerTaxRegistrationSchemeID.add(schemeID);
        sellerTaxRegistrationID.add(taxId);
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public void setBuyerPostcode(String buyerPostcode) {
        this.buyerPostcode = buyerPostcode;
    }

    public void setBuyerLineOne(String buyerLineOne) {
        this.buyerLineOne = buyerLineOne;
    }

    public void setBuyerLineTwo(String buyerLineTwo) {
        this.buyerLineTwo = buyerLineTwo;
    }

    public void setBuyerCityName(String buyerCityName) {
        this.buyerCityName = buyerCityName;
    }

    public void setBuyerCountryID(String buyerCountryID) {
        this.buyerCountryID = buyerCountryID;
    }

    public void addBuyerTaxRegistration(String schemeID, String taxId) {
        buyerTaxRegistrationSchemeID.add(schemeID);
        buyerTaxRegistrationID.add(taxId);
    }

    public void setDeliveryDate(Date deliveryDate, String deliveryDateFormat) {
        this.deliveryDate = deliveryDate;
        this.deliveryDateFormat = deliveryDateFormat;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public void setInvoiceCurrencyCode(String invoiceCurrencyCode) {
        this.invoiceCurrencyCode = invoiceCurrencyCode;
    }

    public void addPaymentMeans(String schemeAgencyID, String id, String iban, String accountname, String proprietaryID, String bic, String germanBankleitzahlID, String institutionname) {
        paymentMeansID.add(id);
        paymentMeansSchemeAgencyID.add(schemeAgencyID);
        paymentMeansPayeeAccountIBAN.add(iban);
        paymentMeansPayeeAccountName.add(accountname);
        paymentMeansPayeeAccountProprietaryID.add(proprietaryID);
        paymentMeansPayeeFinancialInstitutionBIC.add(bic);
        paymentMeansPayeeFinancialInstitutionGermanBankleitzahlID.add(germanBankleitzahlID);
        paymentMeansPayeeFinancialInstitutionName.add(institutionname);
    }

    public void addApplicableTradeTax(String calculatedAmount, String calculatedAmountCurrencyID, String typeCode,
            String basisAmount, String basisAmountCurrencyID, String applicablePercent) {
        taxCalculatedAmount.add(calculatedAmount);
        taxCalculatedAmountCurrencyID.add(calculatedAmountCurrencyID);
        taxTypeCode.add(typeCode);
        taxBasisAmount.add(basisAmount);
        taxBasisAmountCurrencyID.add(basisAmountCurrencyID);
        taxApplicablePercent.add(applicablePercent);
    }

    public void setMonetarySummation(String lineTotalAmount, String lineTotalAmountCurrencyID,
            String chargeTotalAmount, String chargeTotalAmountCurrencyID,
            String allowanceTotalAmount, String allowanceTotalAmountCurrencyID,
            String taxBasisTotalAmount, String taxBasisTotalAmountCurrencyID,
            String taxTotalAmount, String taxTotalAmountCurrencyID,
            String grandTotalAmount, String grandTotalAmountCurrencyID) {
        this.lineTotalAmount = lineTotalAmount;
        this.lineTotalAmountCurrencyID = lineTotalAmountCurrencyID;
        this.chargeTotalAmount = chargeTotalAmount;
        this.chargeTotalAmountCurrencyID = chargeTotalAmountCurrencyID;
        this.allowanceTotalAmount = allowanceTotalAmount;
        this.allowanceTotalAmountCurrencyID = allowanceTotalAmountCurrencyID;
        this.taxBasisTotalAmount = taxBasisTotalAmount;
        this.taxBasisTotalAmountCurrencyID = taxBasisTotalAmountCurrencyID;
        this.taxTotalAmount = taxTotalAmount;
        this.taxTotalAmountCurrencyID = taxTotalAmountCurrencyID;
        this.grandTotalAmount = grandTotalAmount;
        this.grandTotalAmountCurrencyID = grandTotalAmountCurrencyID;
    }

    public void addIncludedSupplyChainTradeLineItem(String billedQuantity, String billedQuantityUnitCode, String specifiedTradeProductName) {
        this.lineItemBilledQuantity.add(billedQuantity);
        this.lineItemBilledQuantityUnitCode.add(billedQuantityUnitCode);
        this.lineItemSpecifiedTradeProductName.add(specifiedTradeProductName);
    }
    
    // helper methods
    protected String[] to1DArray(List<String> list) {
        return (String[]) list.toArray(new String[list.size()]);
    }
    
    protected Boolean[] to1DArrayB(List<Boolean> list) {
        return (Boolean[]) list.toArray(new Boolean[list.size()]);
    }
    
    protected String[][] to2DArray(List<String[]> list) {
        int n = list.size();
        String[][] array = new String[n][];
        for (int i = 0; i < n; i++) {
            array[i] = list.get(i);
        }
        return array;
    }
    
    protected Boolean[][] to2DArrayB(List<Boolean[]> list) {
        int n = list.size();
        Boolean[][] array = new Boolean[n][];
        for (int i = 0; i < n; i++) {
            array[i] = list.get(i);
        }
        return array;
    }
    
    protected String[][][] to3DArray(List<String[][]> list) {
        int n = list.size();
        String[][][] array = new String[n][][];
        for (int i = 0; i < n; i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
