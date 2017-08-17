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
package com.itextpdf.text.zugferd;

import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.zugferd.checkers.NumberChecker;
import com.itextpdf.text.zugferd.checkers.basic.CountryCode;
import com.itextpdf.text.zugferd.checkers.basic.CurrencyCode;
import com.itextpdf.text.zugferd.checkers.basic.DateFormatCode;
import com.itextpdf.text.zugferd.checkers.basic.DocumentTypeCode;
import com.itextpdf.text.zugferd.checkers.basic.MeasurementUnitCode;
import com.itextpdf.text.zugferd.checkers.basic.TaxIDTypeCode;
import com.itextpdf.text.zugferd.checkers.basic.TaxTypeCode;
import com.itextpdf.text.zugferd.checkers.comfort.FreeTextSubjectCode;
import com.itextpdf.text.zugferd.checkers.comfort.GlobalIdentifierCode;
import com.itextpdf.text.zugferd.checkers.comfort.PaymentMeansCode;
import com.itextpdf.text.zugferd.checkers.comfort.TaxCategoryCode;
import com.itextpdf.text.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.text.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.text.zugferd.profiles.BasicProfile;
import com.itextpdf.text.zugferd.profiles.ComfortProfile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author iText
 */
public class InvoiceDOM {
    
    // code checkers
    public static final CountryCode COUNTRY_CODE = new CountryCode();
    public static final CurrencyCode CURR_CODE = new CurrencyCode();
    public static final DateFormatCode DF_CODE = new DateFormatCode();
    public static final GlobalIdentifierCode GI_CODE = new GlobalIdentifierCode();
    public static final MeasurementUnitCode M_UNIT_CODE = new MeasurementUnitCode();
    public static final NumberChecker DEC2 = new NumberChecker(NumberChecker.TWO_DECIMALS);
    public static final NumberChecker DEC4 = new NumberChecker(NumberChecker.FOUR_DECIMALS);
    public static final PaymentMeansCode PM_CODE = new PaymentMeansCode();
    public static final TaxCategoryCode TC_CODE = new TaxCategoryCode();
    public static final TaxIDTypeCode TIDT_CODE = new TaxIDTypeCode();
    public static final TaxTypeCode TT_CODE = new TaxTypeCode();
    
    // The DOM document
    protected final Document doc;
    
    /**
     * Creates an object that will import data into an XML template.
     * @param data If this is an instance of BASICInvoice, the BASIC profile will be used;
     *             If this is an instance of COMFORTInvoice, the COMFORT profile will be used.
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    public InvoiceDOM(BasicProfile data)
            throws ParserConfigurationException, SAXException, IOException,
            DataIncompleteException, InvalidCodeException {
        // loading the XML template
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        docBuilder.setEntityResolver(new SafeEmptyEntityResolver());
        InputStream is = StreamUtil.getResourceStream("com/itextpdf/text/zugferd/zugferd-template.xml");
	    doc = docBuilder.parse(is);
        // importing the data
        importData(doc, data);
    }
    
    // top-level import methods
    
    /**
     * Imports the data into the XML template.
     * @param doc   the Document object we are going to populate
     * @param data  the interface that gives us access to the data
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    private void importData(Document doc, BasicProfile data)
            throws DataIncompleteException, InvalidCodeException {
        if (!data.getTestIndicator()) throw new InvalidCodeException("false",
            "the test indicator: the ZUGFeRD functionality is still in beta; contact sales@itextpdf.com for more info.");
        importSpecifiedExchangedDocumentContext(
                (Element) doc.getElementsByTagName("rsm:SpecifiedExchangedDocumentContext").item(0), data);
        importHeaderExchangedDocument(
                (Element) doc.getElementsByTagName("rsm:HeaderExchangedDocument").item(0), data);
        importSpecifiedSupplyChainTradeTransaction(
                (Element) doc.getElementsByTagName("rsm:SpecifiedSupplyChainTradeTransaction").item(0), data);
    }
    
    /**
     * Imports the data for the following tag: rsm:SpecifiedExchangedDocumentContext
     * @param   element the rsm:SpecifiedExchangedDocumentContext element
     * @param   data    the invoice data
     */
    protected void importSpecifiedExchangedDocumentContext(Element element, BasicProfile data) {
        // TestIndicator (optional)
        importContent(element, "udt:Indicator", data.getTestIndicator() ? "true" : "false");
    }
    
    /**
     * Imports the data for the following tag: rsm:HeaderExchangedDocument
     * @param   element the rsm:HeaderExchangedDocument element
     * @param   data    the invoice data
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importHeaderExchangedDocument(Element element, BasicProfile data)
            throws DataIncompleteException, InvalidCodeException {
        // ID (required)
        check(data.getId(), "HeaderExchangedDocument > ID");
        importContent(element, "ram:ID", data.getId());
        // Name (required)
        check(data.getName(), "HeaderExchangedDocument > Name");
        importContent(element, "ram:Name", data.getName());
        // TypeCode (required)
        DocumentTypeCode dtCode = new DocumentTypeCode(
                data instanceof ComfortProfile ? DocumentTypeCode.COMFORT : DocumentTypeCode.BASIC);
        importContent(element, "ram:TypeCode", dtCode.check(data.getTypeCode()));
        // IssueDateTime (required)
        check(data.getDateTimeFormat(), "HeaderExchangedDocument > DateTimeString");
        importDateTime(element, "udt:DateTimeString", data.getDateTimeFormat(), data.getDateTime());
        // IncludedNote (optional): header level
        String[][] notes = data.getNotes();
        String[] notesCodes = null;
        if (data instanceof ComfortProfile) {
            notesCodes = ((ComfortProfile)data).getNotesCodes();
        }
        importIncludedNotes(element, FreeTextSubjectCode.HEADER, notes, notesCodes);
    }
    
    // Sub-level import methods
    
    /**
     * Helper method to set the content of a tag.
     * @param parent    the parent element of the tag
     * @param tag       the tag for which we want to set the content
     * @param content   the new content for the tag
     * @param attributes    a sequence of attributes of which
     *                      the odd elements are keys, the even elements the
     *                      corresponding value.
     */
    protected void importContent(Element parent, String tag,
        String content, String... attributes) {
        Node node = parent.getElementsByTagName(tag).item(0);
        // content
        node.setTextContent(content);
        // attributes
        if (attributes == null || attributes.length == 0)
            return;
        int n = attributes.length;
        String attrName, attrValue;
        NamedNodeMap attrs = node.getAttributes();
        Node attr;
        for (int i = 0; i < n; i++) {
            attrName = attributes[i];
            if (++i == n) continue;
            attrValue = attributes[i];
            attr = attrs.getNamedItem(attrName);
            if (attr != null)
                attr.setTextContent(attrValue);
        }
    }
    
    /**
     * Set the content of a date tag along with the attribute that defines the format.
     * @param parent    the parent element that holds the date tag
     * @param tag       the date tag we want to change
     * @param dateTimeFormat    the format that will be used as an attribute
     * @param dateTime  the actual date
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importDateTime(Element parent, String tag,
        String dateTimeFormat, Date dateTime)
            throws InvalidCodeException {
        if (dateTimeFormat == null) return;
        importContent(parent, tag, DF_CODE.convertToString(dateTime, DF_CODE.check(dateTimeFormat)), "format", dateTimeFormat);
    }
    
    /**
     * Includes notes and (in case of the COMFORT profile) the subject codes
     * for those notes.
     * @param parent    the parent element of the tag we want to change
     * @param level the level where the notices are added (header or line)
     * @param notes array of notes
     * @param notesCodes    array of codes for the notes.
     *          If not null, notes and notesCodes need to have an equal number of elements.
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importIncludedNotes(Element parent, int level,
        String notes[][], String[] notesCodes)
            throws DataIncompleteException, InvalidCodeException {
        if (notes == null) return;
        Node includedNoteNode = parent.getElementsByTagName("ram:IncludedNote").item(0);
        int n = notes.length;
        FreeTextSubjectCode ftsCode = new FreeTextSubjectCode(level);
        if (notesCodes != null && n != notesCodes.length)
            throw new DataIncompleteException("Number of included notes is not equal to number of codes for included notes.");
        for (int i = 0; i < n; i++) {
            Element noteNode = (Element)includedNoteNode.cloneNode(true);
            Node content = noteNode.getElementsByTagName("ram:Content").item(0);
            for (String note : notes[i]) {
                Node newNode = content.cloneNode(true);
                newNode.setTextContent(note);
                noteNode.insertBefore(newNode, content);
            }
            if (notesCodes != null) {
                Node code = noteNode.getElementsByTagName("ram:SubjectCode").item(0);
                code.setTextContent(ftsCode.check(notesCodes[i]));
            }
            parent.insertBefore(noteNode, includedNoteNode);
        }
    }
    /**
     * Imports the data for the following tag: rsm:SpecifiedSupplyChainTradeTransaction
     * @param element
     * @param   data    the invoice data
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importSpecifiedSupplyChainTradeTransaction(Element element, BasicProfile data)
            throws DataIncompleteException, InvalidCodeException {
        
        ComfortProfile comfortData = null;
        if (data instanceof ComfortProfile)
            comfortData = (ComfortProfile)data;
        
        /* ram:ApplicableSupplyChainTradeAgreement */

        // buyer reference (optional; comfort only)
        if (comfortData != null) {
            String buyerReference = comfortData.getBuyerReference();
            importContent(element, "ram:BuyerReference", buyerReference);
        }
        // SellerTradeParty (required)
        check(data.getSellerName(), "SpecifiedSupplyChainTradeTransaction > ApplicableSupplyChainTradeAgreement > SellerTradeParty > Name");
        importSellerTradeParty(element, data);
        // BuyerTradeParty (required)
        check(data.getBuyerName(), "SpecifiedSupplyChainTradeTransaction > ApplicableSupplyChainTradeAgreement > BuyerTradeParty > Name");
        importBuyerTradeParty(element, data);
        
        /* ram:ApplicableSupplyChainTradeDelivery */
        
        if (comfortData != null) {
            // BuyerOrderReferencedDocument (optional)
            Element document = (Element)element.getElementsByTagName("ram:BuyerOrderReferencedDocument").item(0);
            importDateTime(document, "ram:IssueDateTime", comfortData.getBuyerOrderReferencedDocumentIssueDateTimeFormat(), comfortData.getBuyerOrderReferencedDocumentIssueDateTime());
            importContent(document, "ram:ID", comfortData.getBuyerOrderReferencedDocumentID());
            // ContractReferencedDocument (optional)
            document = (Element)element.getElementsByTagName("ram:ContractReferencedDocument").item(0);
            importDateTime(document, "ram:IssueDateTime", comfortData.getContractReferencedDocumentIssueDateTimeFormat(), comfortData.getContractReferencedDocumentIssueDateTime());
            importContent(document, "ram:ID", comfortData.getContractReferencedDocumentID());
            // CustomerOrderReferencedDocument (optional)
            document = (Element)element.getElementsByTagName("ram:CustomerOrderReferencedDocument").item(0);
            importDateTime(document, "ram:IssueDateTime", comfortData.getCustomerOrderReferencedDocumentIssueDateTimeFormat(), comfortData.getCustomerOrderReferencedDocumentIssueDateTime());
            importContent(document, "ram:ID", comfortData.getCustomerOrderReferencedDocumentID());
        }
        
        /* ram:ApplicableSupplyChainTradeDelivery */
        
        // ActualDeliverySupplyChainEvent (optional)
        Element parent = (Element)element.getElementsByTagName("ram:ActualDeliverySupplyChainEvent").item(0);
        importDateTime(parent, "udt:DateTimeString", data.getDeliveryDateTimeFormat(), data.getDeliveryDateTime());
        // DeliveryNoteReferencedDocument (optional)
        if (comfortData != null) {
            Element document = (Element)element.getElementsByTagName("ram:DeliveryNoteReferencedDocument").item(0);
            importDateTime(document, "ram:IssueDateTime", comfortData.getDeliveryNoteReferencedDocumentIssueDateTimeFormat(), comfortData.getDeliveryNoteReferencedDocumentIssueDateTime());
            importContent(document, "ram:ID", comfortData.getDeliveryNoteReferencedDocumentID());
        }
        
        /* ram:ApplicableSupplyChainTradeSettlement */
        
        // ram:PaymentReference (optional)
        importContent(element, "ram:PaymentReference", data.getPaymentReference());
        // ram:InvoiceCurrencyCode (required)
        importContent(element, "ram:InvoiceCurrencyCode", CURR_CODE.check(data.getInvoiceCurrencyCode()));
        // ram:InvoiceeTradeParty (optional)
        if (comfortData != null) {
            importInvoiceeTradeParty(element, comfortData);
        }
        
        // ram:SpecifiedTradeSettlementPaymentMeans
        parent = (Element)element.getElementsByTagName("ram:ApplicableSupplyChainTradeSettlement").item(0);
        importPaymentMeans(parent, data);
        
        // ram:ApplicableTradeTax
        importTax(parent, data);
        
        if (comfortData != null) {
            
            // ram:BillingSpecifiedPeriod
            
            Element period = (Element)element.getElementsByTagName("ram:BillingSpecifiedPeriod").item(0);
            Element start = (Element)period.getElementsByTagName("ram:StartDateTime").item(0);
            importDateTime(start, "udt:DateTimeString", comfortData.getBillingStartDateTimeFormat(), comfortData.getBillingStartDateTime());
            // ContractReferencedDocument (optional)
            Element end = (Element)period.getElementsByTagName("ram:EndDateTime").item(0);
            importDateTime(end, "udt:DateTimeString", comfortData.getBillingEndDateTimeFormat(), comfortData.getBillingEndDateTime());
            
            // ram:SpecifiedTradeAllowanceCharge
            importSpecifiedTradeAllowanceCharge(parent, comfortData);
            
            // ram:SpecifiedLogisticsServiceCharge
            importSpecifiedLogisticsServiceCharge(parent, comfortData);
            
            // ram:SpecifiedTradePaymentTerms
            importSpecifiedTradePaymentTerms(parent, comfortData);
        }
        
        // ram:SpecifiedTradeSettlementMonetarySummation
        check(DEC2.check(data.getLineTotalAmount()), "SpecifiedTradeSettlementMonetarySummation > LineTotalAmount");
        check(CURR_CODE.check(data.getLineTotalAmountCurrencyID()), "SpecifiedTradeSettlementMonetarySummation > LineTotalAmount . currencyID");
        importContent(element, "ram:LineTotalAmount", data.getLineTotalAmount(), "currencyID", data.getLineTotalAmountCurrencyID());
        check(DEC2.check(data.getChargeTotalAmount()), "SpecifiedTradeSettlementMonetarySummation > ChargeTotalAmount");
        check(CURR_CODE.check(data.getChargeTotalAmountCurrencyID()), "SpecifiedTradeSettlementMonetarySummation > ChargeTotalAmount . currencyID");
        importContent(element, "ram:ChargeTotalAmount", data.getChargeTotalAmount(), "currencyID", data.getChargeTotalAmountCurrencyID());
        check(DEC2.check(data.getAllowanceTotalAmount()), "SpecifiedTradeSettlementMonetarySummation > AllowanceTotalAmount");
        check(CURR_CODE.check(data.getAllowanceTotalAmountCurrencyID()), "SpecifiedTradeSettlementMonetarySummation > AllowanceTotalAmount . currencyID");
        importContent(element, "ram:AllowanceTotalAmount", data.getAllowanceTotalAmount(), "currencyID", data.getAllowanceTotalAmountCurrencyID());
        check(DEC2.check(data.getTaxBasisTotalAmount()), "SpecifiedTradeSettlementMonetarySummation > TaxBasisTotalAmount");
        check(CURR_CODE.check(data.getTaxBasisTotalAmountCurrencyID()), "SpecifiedTradeSettlementMonetarySummation > TaxBasisTotalAmount . currencyID");
        importContent(element, "ram:TaxBasisTotalAmount", data.getTaxBasisTotalAmount(), "currencyID", data.getTaxBasisTotalAmountCurrencyID());
        check(DEC2.check(data.getTaxTotalAmount()), "SpecifiedTradeSettlementMonetarySummation > TaxTotalAmount");
        check(CURR_CODE.check(data.getTaxTotalAmountCurrencyID()), "SpecifiedTradeSettlementMonetarySummation > TaxTotalAmount . currencyID");
        importContent(element, "ram:TaxTotalAmount", data.getTaxTotalAmount(), "currencyID", data.getTaxTotalAmountCurrencyID());
        check(DEC2.check(data.getGrandTotalAmount()), "SpecifiedTradeSettlementMonetarySummation > GrandTotalAmount");
        check(CURR_CODE.check(data.getGrandTotalAmountCurrencyID()), "SpecifiedTradeSettlementMonetarySummation > GrandTotalAmount . currencyID");
        importContent(element, "ram:GrandTotalAmount", data.getGrandTotalAmount(), "currencyID", data.getGrandTotalAmountCurrencyID());
        if (comfortData != null) {
            importContent(element, "ram:TotalPrepaidAmount", comfortData.getTotalPrepaidAmount(), "currencyID", comfortData.getTotalPrepaidAmountCurrencyID());
            importContent(element, "ram:DuePayableAmount", comfortData.getDuePayableAmount(), "currencyID", comfortData.getDuePayableAmountCurrencyID());
        }
        
        /* ram:IncludedSupplyChainTradeLineItem */
        if (comfortData != null)
            importLineItemsComfort(element, comfortData);
        else
            importLineItemsBasic(element, data);
    }
    
    /**
     * Gets the seller trade party data to import this data.
     * @param parent    the parent element
     * @param data      the data
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importSellerTradeParty(Element parent, BasicProfile data)
            throws DataIncompleteException, InvalidCodeException {
        String id = null;
        String[] globalID = null;
        String[] globalIDScheme = null;
        if (data instanceof ComfortProfile) {
            id = ((ComfortProfile)data).getSellerID();
            globalID = ((ComfortProfile)data).getSellerGlobalID();
            globalIDScheme = ((ComfortProfile)data).getSellerGlobalSchemeID();
        }
        String name = data.getSellerName();
        String postcode = data.getSellerPostcode();
        String lineOne = data.getSellerLineOne();
        String lineTwo = data.getSellerLineTwo();
        String cityName = data.getSellerCityName();
        String countryID = data.getSellerCountryID();
        String[] taxRegistrationID = data.getSellerTaxRegistrationID();
        String[] taxRegistrationSchemeID = data.getSellerTaxRegistrationSchemeID();
        importTradeParty(
                (Element) parent.getElementsByTagName("ram:SellerTradeParty").item(0),
                id, globalID, globalIDScheme,
                name, postcode, lineOne, lineTwo, cityName, countryID,
                taxRegistrationID, taxRegistrationSchemeID);
    }
    
    /**
     * Gets the buyer trade party data to import this data.
     * @param parent    the parent element
     * @param data      the data
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importBuyerTradeParty(Element parent, BasicProfile data)
            throws DataIncompleteException, InvalidCodeException {
        String id = null;
        String[] globalID = null;
        String[] globalIDScheme = null;
        if (data instanceof ComfortProfile) {
            id = ((ComfortProfile)data).getBuyerID();
            globalID = ((ComfortProfile)data).getBuyerGlobalID();
            globalIDScheme = ((ComfortProfile)data).getBuyerGlobalSchemeID();
        }
        String name = data.getBuyerName();
        String postcode = data.getBuyerPostcode();
        String lineOne = data.getBuyerLineOne();
        String lineTwo = data.getBuyerLineTwo();
        String cityName = data.getBuyerCityName();
        String countryID = data.getBuyerCountryID();
        String[] taxRegistrationID = data.getBuyerTaxRegistrationID();
        String[] taxRegistrationSchemeID = data.getBuyerTaxRegistrationSchemeID();
        importTradeParty(
                (Element) parent.getElementsByTagName("ram:BuyerTradeParty").item(0),
                id, globalID, globalIDScheme,
                name, postcode, lineOne, lineTwo, cityName, countryID,
                taxRegistrationID, taxRegistrationSchemeID);
    }
    
    /**
     * Gets the invoicee party data to import this data.
     * @param parent    the parent element
     * @param data      the data
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importInvoiceeTradeParty(Element parent, ComfortProfile data)
            throws DataIncompleteException, InvalidCodeException {
        String name = data.getInvoiceeName();
        if (name == null) return;
        String id = data.getInvoiceeID();
        String[] globalID = data.getInvoiceeGlobalID();
        String[] globalIDScheme = data.getInvoiceeGlobalSchemeID();
        String postcode = data.getInvoiceePostcode();
        String lineOne = data.getInvoiceeLineOne();
        String lineTwo = data.getInvoiceeLineTwo();
        String cityName = data.getInvoiceeCityName();
        String countryID = data.getInvoiceeCountryID();
        String[] taxRegistrationID = data.getInvoiceeTaxRegistrationID();
        String[] taxRegistrationSchemeID = data.getInvoiceeTaxRegistrationSchemeID();
        importTradeParty(
                (Element) parent.getElementsByTagName("ram:InvoiceeTradeParty").item(0),
                id, globalID, globalIDScheme,
                name, postcode, lineOne, lineTwo, cityName, countryID,
                taxRegistrationID, taxRegistrationSchemeID);
    }
    
    /**
     * Imports trade party information (could be seller, buyer or invoicee).
     * @param parent    the parent element
     * @param id
     * @param globalID
     * @param globalIDScheme
     * @param name
     * @param postcode
     * @param lineOne
     * @param lineTwo
     * @param countryID
     * @param cityName
     * @param taxRegistrationID
     * @param taxRegistrationSchemeID
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importTradeParty(Element parent,
        String id, String[] globalID, String[] globalIDScheme,
        String name, String postcode, String lineOne, String lineTwo,
        String cityName, String countryID,
        String[] taxRegistrationID, String[] taxRegistrationSchemeID)
            throws DataIncompleteException, InvalidCodeException {
        Node node;
        if (id != null) {
            node = parent.getElementsByTagName("ram:ID").item(0);
            node.setTextContent(id);
        }
        if (globalID != null) {
            int n = globalID.length;
            if (globalIDScheme == null || globalIDScheme.length != n)
                throw new DataIncompleteException("Number of global ID schemes is not equal to number of global IDs.");
            node = parent.getElementsByTagName("ram:GlobalID").item(0);
            for (int i = 0; i < n; i++) {
                Element idNode = (Element)node.cloneNode(true);
                NamedNodeMap attrs = idNode.getAttributes();
                idNode.setTextContent(globalID[i]);
                Node schemeID = attrs.getNamedItem("schemeID");
                schemeID.setTextContent(GI_CODE.check(globalIDScheme[i]));
                parent.insertBefore(idNode, node);
            }
        }
        importContent(parent, "ram:Name", name);
        importContent(parent, "ram:PostcodeCode", postcode);
        importContent(parent, "ram:LineOne", lineOne);
        importContent(parent, "ram:LineTwo", lineTwo);
        importContent(parent, "ram:CityName", cityName);
        if (countryID != null) {
            importContent(parent, "ram:CountryID", COUNTRY_CODE.check(countryID));
        }
        int n = taxRegistrationID.length;
        if (taxRegistrationSchemeID != null && taxRegistrationSchemeID.length != n)
            throw new DataIncompleteException("Number of tax ID schemes is not equal to number of tax IDs.");
        Element tax = (Element) parent.getElementsByTagName("ram:SpecifiedTaxRegistration").item(0);
        node = tax.getElementsByTagName("ram:ID").item(0);
        for (int i = 0; i < n; i++) {
            Element idNode = (Element)node.cloneNode(true);
            idNode.setTextContent(taxRegistrationID[i]);
            NamedNodeMap attrs = idNode.getAttributes();
            Node schemeID = attrs.getNamedItem("schemeID");
            schemeID.setTextContent(TIDT_CODE.check(taxRegistrationSchemeID[i]));
            tax.insertBefore(idNode, node);
        }           
    }
    
    /**
     * Gets the payment means data to imports this data.
     * @param parent    the parent element
     * @param data      the data
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importPaymentMeans(Element parent, BasicProfile data)
            throws InvalidCodeException {
        String[] pmID = data.getPaymentMeansID();
        int n = pmID.length;
        String[] pmTypeCode = new String[n];
        String[][] pmInformation = new String[n][];
        String[] pmSchemeAgencyID = data.getPaymentMeansSchemeAgencyID();
        String[] pmPayerIBAN = new String[n];
        String[] pmPayerProprietaryID = new String[n];
        String[] pmIBAN = data.getPaymentMeansPayeeAccountIBAN();
        String[] pmAccountName = data.getPaymentMeansPayeeAccountAccountName();
        String[] pmAccountID = data.getPaymentMeansPayeeAccountProprietaryID();
        String[] pmPayerBIC = new String[n];
        String[] pmPayerGermanBankleitzahlID = new String[n];
        String[] pmPayerFinancialInst = new String[n];
        String[] pmBIC = data.getPaymentMeansPayeeFinancialInstitutionBIC();
        String[] pmGermanBankleitzahlID = data.getPaymentMeansPayeeFinancialInstitutionGermanBankleitzahlID();
        String[] pmFinancialInst = data.getPaymentMeansPayeeFinancialInstitutionName();
        if (data instanceof ComfortProfile) {
            ComfortProfile comfortData = (ComfortProfile)data;
            pmTypeCode = comfortData.getPaymentMeansTypeCode();
            pmInformation = comfortData.getPaymentMeansInformation();
            pmPayerIBAN = comfortData.getPaymentMeansPayerAccountIBAN();
            pmPayerProprietaryID = comfortData.getPaymentMeansPayerAccountProprietaryID();
            pmPayerBIC = comfortData.getPaymentMeansPayerFinancialInstitutionBIC();
            pmPayerGermanBankleitzahlID = comfortData.getPaymentMeansPayerFinancialInstitutionGermanBankleitzahlID();
            pmPayerFinancialInst = comfortData.getPaymentMeansPayerFinancialInstitutionName();
        }
        Node node = parent.getElementsByTagName("ram:SpecifiedTradeSettlementPaymentMeans").item(0);
        for (int i = 0; i < pmID.length; i++) {
            Node newNode = node.cloneNode(true);
            InvoiceDOM.this.importPaymentMeans((Element)newNode,
                    pmTypeCode[i],
                    pmInformation[i],
                    pmID[i],
                    pmSchemeAgencyID[i],
                    pmPayerIBAN[i],
                    pmPayerProprietaryID[i],
                    pmIBAN[i],
                    pmAccountName[i],
                    pmAccountID[i],
                    pmPayerBIC[i],
                    pmPayerGermanBankleitzahlID[i],
                    pmPayerFinancialInst[i],
                    pmBIC[i],
                    pmGermanBankleitzahlID[i],
                    pmFinancialInst[i]
            );
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Imports payment means data.
     * @param parent    the parent element
     * @param typeCode
     * @param information
     * @param id
     * @param scheme
     * @param payerIban
     * @param payerProprietaryID
     * @param iban
     * @param accID
     * @param accName
     * @param payerBic
     * @param payerBank
     * @param inst
     * @param bic
     * @param bank
     * @param payerInst
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importPaymentMeans(Element parent,
        String typeCode, String[] information, String id, String scheme,
        String payerIban, String payerProprietaryID,
        String iban, String accName, String accID,
        String payerBic, String payerBank, String payerInst,
        String bic, String bank, String inst)
            throws InvalidCodeException {
        if (typeCode != null) {
            importContent(parent, "ram:TypeCode", PM_CODE.check(typeCode));
        }
        if (information != null) {
            Node node = parent.getElementsByTagName("ram:Information").item(0);
            for (String info : information) {
                Node newNode = node.cloneNode(true);
                newNode.setTextContent(info);
                parent.insertBefore(newNode, node);
            }
        }
        importContent(parent, "ram:ID", id, "schemeAgencyID", scheme);
        Element payer = (Element)parent.getElementsByTagName("ram:PayerPartyDebtorFinancialAccount").item(0);
        importContent(payer, "ram:IBANID", payerIban);
        importContent(payer, "ram:ProprietaryID", payerProprietaryID);
        Element payee = (Element)parent.getElementsByTagName("ram:PayeePartyCreditorFinancialAccount").item(0);
        importContent(payee, "ram:IBANID", iban);
        importContent(payee, "ram:AccountName", accName);
        importContent(payee, "ram:ProprietaryID", accID);
        payer = (Element)parent.getElementsByTagName("ram:PayerSpecifiedDebtorFinancialInstitution").item(0);
        importContent(payer, "ram:BICID", payerBic);
        importContent(payer, "ram:GermanBankleitzahlID", payerBank);
        importContent(payer, "ram:Name", payerInst);
        payee = (Element)parent.getElementsByTagName("ram:PayeeSpecifiedCreditorFinancialInstitution").item(0);
        importContent(payee, "ram:BICID", bic);
        importContent(payee, "ram:GermanBankleitzahlID", bank);
        importContent(payee, "ram:Name", inst);
    }

    /**
     * Gets tax data to import the this data.
     * @param parent    the parent element
     * @param data      the data
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     */
    protected void importTax(Element parent, BasicProfile data)
            throws InvalidCodeException, DataIncompleteException {
        String[] calculated = data.getTaxCalculatedAmount();
        int n = calculated.length;
        String[] calculatedCurr = data.getTaxCalculatedAmountCurrencyID();
        String[] typeCode = data.getTaxTypeCode();
        String[] exemptionReason = new String[n];
        String[] basisAmount = data.getTaxBasisAmount();
        String[] basisAmountCurr = data.getTaxBasisAmountCurrencyID();
        String[] category = new String[n];
        String[] percent = data.getTaxApplicablePercent();
        if (data instanceof ComfortProfile) {
            ComfortProfile comfortData = (ComfortProfile)data;
            exemptionReason = comfortData.getTaxExemptionReason();
            category = comfortData.getTaxCategoryCode();
        }
        Node node = parent.getElementsByTagName("ram:ApplicableTradeTax").item(0);
        for (int i = 0; i < n; i++) {
            Node newNode = node.cloneNode(true);
            InvoiceDOM.this.importTax((Element)newNode, calculated[i], calculatedCurr[i], typeCode[i],
                exemptionReason[i], basisAmount[i], basisAmountCurr[i],
                category[i], percent[i]);
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Imports tax data.
     * @param parent
     * @param calculatedAmount
     * @param currencyID
     * @param typeCode
     * @param exemptionReason
     * @param basisAmount
     * @param basisAmountCurr
     * @param category
     * @param percent
     * @throws InvalidCodeException
     * @throws DataIncompleteException
     */
    protected void importTax(Element parent,
        String calculatedAmount, String currencyID, String typeCode,
        String exemptionReason, String basisAmount, String basisAmountCurr,
        String category, String percent)
            throws InvalidCodeException, DataIncompleteException {
        // Calculated amount (required; 2 decimals)
        check(CURR_CODE.check(currencyID), "ApplicableTradeTax > CalculatedAmount > CurrencyID");
        importContent(parent, "ram:CalculatedAmount", DEC2.check(calculatedAmount), "currencyID", currencyID);
        // TypeCode (required)
        check(typeCode, "ApplicableTradeTax > TypeCode");
        importContent(parent, "ram:TypeCode", TT_CODE.check(typeCode));
        // exemption reason (optional)
        importContent(parent, "ram:ExemptionReason", exemptionReason);
        // basis amount (required, 2 decimals)
        check(CURR_CODE.check(basisAmountCurr), "ApplicableTradeTax > BasisAmount > CurrencyID");
        importContent(parent, "ram:BasisAmount", DEC2.check(basisAmount), "currencyID", basisAmountCurr);
        // Category code (optional)
        if (category != null) {
            importContent(parent, "ram:CategoryCode", TC_CODE.check(category));
        }
        // Applicable percent (required; 2 decimals)
        importContent(parent, "ram:ApplicablePercent", DEC2.check(percent));
    }
    
    /**
     * Gets specified trade allowance charge data to import the this data.
     * @param parent    the parent element
     * @param data      the data
     * @throws InvalidCodeException
     */
    protected void importSpecifiedTradeAllowanceCharge(Element parent, ComfortProfile data)
            throws InvalidCodeException {
        Boolean[] indicator = data.getSpecifiedTradeAllowanceChargeIndicator();
        String[] actualAmount = data.getSpecifiedTradeAllowanceChargeActualAmount();
        String[] actualAmountCurr = data.getSpecifiedTradeAllowanceChargeActualAmountCurrency();
        String[] reason = data.getSpecifiedTradeAllowanceChargeReason();
        String[][] typeCode = data.getSpecifiedTradeAllowanceChargeTaxTypeCode();
        String[][] categoryCode = data.getSpecifiedTradeAllowanceChargeTaxCategoryCode();
        String[][] percent = data.getSpecifiedTradeAllowanceChargeTaxApplicablePercent();
        Node node = (Element)parent.getElementsByTagName("ram:SpecifiedTradeAllowanceCharge").item(0);
        for (int i = 0; i < indicator.length; i++) {
            Node newNode = node.cloneNode(true);
            InvoiceDOM.this.importSpecifiedTradeAllowanceCharge((Element)newNode, indicator[i],
                actualAmount[i], actualAmountCurr[i], reason[i],
                typeCode[i], categoryCode[i], percent[i]);
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Imports specified trade allowance charge.
     * @param parent
     * @param indicator
     * @param actualAmount
     * @param actualAmountCurrency
     * @param reason
     * @param typeCode
     * @param categoryCode
     * @param percent
     * @throws InvalidCodeException
     */
    protected void importSpecifiedTradeAllowanceCharge(Element parent,
        boolean indicator, String actualAmount, String actualAmountCurrency,
        String reason, String[] typeCode, String[] categoryCode, String[] percent)
            throws InvalidCodeException {
        importContent(parent, "udt:Indicator", indicator ? "true" : "false");
        importContent(parent, "ram:ActualAmount", DEC4.check(actualAmount), "currencyID", CURR_CODE.check(actualAmountCurrency));
        importContent(parent, "ram:Reason", reason);
        Node node = parent.getElementsByTagName("ram:CategoryTradeTax").item(0);
        for (int i = 0; i < typeCode.length; i++) {
            Element newNode = (Element) node.cloneNode(true);
            importContent(newNode, "ram:TypeCode", TT_CODE.check(typeCode[i]));
            importContent(newNode, "ram:CategoryCode", TC_CODE.check(categoryCode[i]));
            importContent(newNode, "ram:ApplicablePercent", DEC2.check(percent[i]));
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Gets specified logistics service charge data to import the this data.
     * @param parent    the parent element
     * @param data      the data
     * @throws InvalidCodeException
     */
    protected void importSpecifiedLogisticsServiceCharge(Element parent, ComfortProfile data)
            throws InvalidCodeException {
        String[][] description = data.getSpecifiedLogisticsServiceChargeDescription();
        String[] appliedAmount = data.getSpecifiedLogisticsServiceChargeAmount();
        String[] appliedAmountCurr = data.getSpecifiedLogisticsServiceChargeAmountCurrency();
        String[][] typeCode = data.getSpecifiedLogisticsServiceChargeTaxTypeCode();
        String[][] categoryCode = data.getSpecifiedLogisticsServiceChargeTaxCategoryCode();
        String[][] percent = data.getSpecifiedLogisticsServiceChargeTaxApplicablePercent();
        Node node = parent.getElementsByTagName("ram:SpecifiedLogisticsServiceCharge").item(0);
        for (int i = 0; i < appliedAmount.length; i++) {
            Node newNode = node.cloneNode(true);
            InvoiceDOM.this.importSpecifiedLogisticsServiceCharge((Element)newNode,
                description[i], appliedAmount[i], appliedAmountCurr[i],
                typeCode[i], categoryCode[i], percent[i]);
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Imports specified logistics service charge data.
     * @param parent    the parent element
     * @param description
     * @param appliedAmount
     * @param currencyID
     * @param typeCode
     * @param categoryCode
     * @param percent
     * @throws InvalidCodeException
     */
    protected void importSpecifiedLogisticsServiceCharge(Element parent,
        String[] description, String appliedAmount, String currencyID,
        String[] typeCode, String[] categoryCode, String[] percent)
            throws InvalidCodeException {
        Node node = parent.getElementsByTagName("ram:Description").item(0);
        for (String d : description) {
            Node newNode = node.cloneNode(true);
            newNode.setTextContent(d);
            parent.insertBefore(newNode, node);
        }
        importContent(parent, "ram:AppliedAmount", DEC4.check(appliedAmount), "currencyID", CURR_CODE.check(currencyID));
        node = parent.getElementsByTagName("ram:AppliedTradeTax").item(0);
        for (int i = 0; i < typeCode.length; i++) {
            Element newNode = (Element) node.cloneNode(true);
            importContent(newNode, "ram:TypeCode", TT_CODE.check(typeCode[i]));
            importContent(newNode, "ram:CategoryCode", TC_CODE.check(categoryCode[i]));
            importContent(newNode, "ram:ApplicablePercent", DEC2.check(percent[i]));
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Gets specified trade payment terms data to import the this data.
     * @param parent    the parent element
     * @param data      the data
     * @throws InvalidCodeException
     */
    protected void importSpecifiedTradePaymentTerms(Element parent, ComfortProfile data)
            throws InvalidCodeException {
        String[][] description = data.getSpecifiedTradePaymentTermsDescription();
        Date[] dateTime = data.getSpecifiedTradePaymentTermsDueDateTime();
        String[] dateTimeFormat = data.getSpecifiedTradePaymentTermsDueDateTimeFormat();
        Node node = parent.getElementsByTagName("ram:SpecifiedTradePaymentTerms").item(0);
        for (int i = 0; i < description.length; i++) {
            Node newNode = node.cloneNode(true);
            InvoiceDOM.this.importSpecifiedTradePaymentTerms((Element)newNode,
                    description[i], dateTime[i], dateTimeFormat[i]);
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Imports specified trade payment terms.
     * @param parent    the parent element
     * @param description
     * @param dateTime
     * @param dateTimeFormat
     * @throws InvalidCodeException
     */
    protected void importSpecifiedTradePaymentTerms(Element parent,
        String[] description, Date dateTime, String dateTimeFormat)
            throws InvalidCodeException {
        Node node = parent.getElementsByTagName("ram:Description").item(0);
        for (String d : description) {
            Node newNode = node.cloneNode(true);
            newNode.setTextContent(d);
            parent.insertBefore(newNode, node);
        }
        if (dateTimeFormat != null)
            importDateTime(parent, "udt:DateTimeString", dateTimeFormat, dateTime);
    }
    
    /**
     * Gets line item data to import the this data (comfort profile).
     * @param parent    the parent element
     * @param data      the data
     * @throws DataIncompleteException
     * @throws InvalidCodeException
     */
    protected void importLineItemsComfort(Element parent, ComfortProfile data)
            throws DataIncompleteException, InvalidCodeException {
        String[] lineIDs = data.getLineItemLineID();
        if (lineIDs.length == 0)
            throw new DataIncompleteException("You can create an invoice without any line items");
        String[][][] includedNote = data.getLineItemIncludedNote();
        String[] grossPriceChargeAmount = data.getLineItemGrossPriceChargeAmount();
        String[] grossPriceChargeAmountCurrencyID = data.getLineItemGrossPriceChargeAmountCurrencyID();
        String[] grossPriceBasisQuantity = data.getLineItemGrossPriceBasisQuantity();
        String[] grossPriceBasisQuantityCode = data.getLineItemGrossPriceBasisQuantityCode();
        Boolean[][] grossPriceTradeAllowanceChargeIndicator = data.getLineItemGrossPriceTradeAllowanceChargeIndicator();
        String[][] grossPriceTradeAllowanceChargeActualAmount = data.getLineItemGrossPriceTradeAllowanceChargeActualAmount();
        String[][] grossPriceTradeAllowanceChargeActualAmountCurrencyID = data.getLineItemGrossPriceTradeAllowanceChargeActualAmountCurrencyID();
        String[][] grossPriceTradeAllowanceChargeReason = data.getLineItemGrossPriceTradeAllowanceChargeReason();
        String[] netPriceChargeAmount = data.getLineItemNetPriceChargeAmount();
        String[] netPriceChargeAmountCurrencyID = data.getLineItemNetPriceChargeAmountCurrencyID();
        String[] netPriceBasisQuantity = data.getLineItemNetPriceBasisQuantity();
        String[] netPriceBasisQuantityCode = data.getLineItemNetPriceBasisQuantityCode();
        String[] billedQuantity = data.getLineItemBilledQuantity(); // BASIC
        String[] billedQuantityUnitCode = data.getLineItemBilledQuantityUnitCode();
        String[][] settlementTaxTypeCode = data.getLineItemSettlementTaxTypeCode();
        String[][] settlementTaxExemptionReason = data.getLineItemSettlementTaxExemptionReason();
        String[][] settlementTaxCategoryCode = data.getLineItemSettlementTaxCategoryCode();
        String[][] settlementTaxApplicablePercent = data.getLineItemSettlementTaxApplicablePercent();
        String[] totalAmount = data.getLineItemLineTotalAmount();
        String[] totalAmountCurrencyID = data.getLineItemLineTotalAmountCurrencyID();
        String[] specifiedTradeProductGlobalID = data.getLineItemSpecifiedTradeProductGlobalID();
        String[] specifiedTradeProductSchemeID = data.getLineItemSpecifiedTradeProductSchemeID();
        String[] specifiedTradeProductSellerAssignedID = data.getLineItemSpecifiedTradeProductSellerAssignedID();
        String[] specifiedTradeProductBuyerAssignedID = data.getLineItemSpecifiedTradeProductBuyerAssignedID();
        String[] specifiedTradeProductName = data.getLineItemSpecifiedTradeProductName(); // BASIC
        String[] specifiedTradeProductDescription = data.getLineItemSpecifiedTradeProductDescription();
        Node node = parent.getElementsByTagName("ram:IncludedSupplyChainTradeLineItem").item(0);
        for (int i = 0; i < lineIDs.length; i++) {
            Node newNode = node.cloneNode(true);
            importLineItemComfort((Element)newNode, lineIDs[i], includedNote[i],
                    grossPriceChargeAmount[i], grossPriceChargeAmountCurrencyID[i],
                    grossPriceBasisQuantity[i], grossPriceBasisQuantityCode[i],
                    grossPriceTradeAllowanceChargeIndicator[i],
                    grossPriceTradeAllowanceChargeActualAmount[i],
                    grossPriceTradeAllowanceChargeActualAmountCurrencyID[i],
                    grossPriceTradeAllowanceChargeReason[i],
                    netPriceChargeAmount[i], netPriceChargeAmountCurrencyID[i],
                    netPriceBasisQuantity[i], netPriceBasisQuantityCode[i],
                    billedQuantity[i], billedQuantityUnitCode[i],
                    settlementTaxTypeCode[i], settlementTaxExemptionReason[i],
                    settlementTaxCategoryCode[i], settlementTaxApplicablePercent[i],
                    totalAmount[i], totalAmountCurrencyID[i],
                    specifiedTradeProductGlobalID[i], specifiedTradeProductSchemeID[i],
                    specifiedTradeProductSellerAssignedID[i], specifiedTradeProductBuyerAssignedID[i],
                    specifiedTradeProductName[i], specifiedTradeProductDescription[i]
            );
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Imports line item data (comfort profile).
     * @param parent    the parent element
     * @param lineID
     * @param note
     * @param grossPriceChargeAmount
     * @param grossPriceChargeAmountCurrencyID
     * @param grossPriceBasisQuantity
     * @param grossPriceBasisQuantityCode
     * @param grossPriceTradeAllowanceChargeIndicator
     * @param grossPriceTradeAllowanceChargeActualAmount
     * @param grossPriceTradeAllowanceChargeActualAmountCurrencyID
     * @param grossPriceTradeAllowanceChargeReason
     * @param netPriceChargeAmount
     * @param netPriceChargeAmountCurrencyID
     * @param netPriceBasisQuantity
     * @param netPriceBasisQuantityCode
     * @param billedQuantity
     * @param billedQuantityCode
     * @param settlementTaxTypeCode
     * @param settlementTaxExemptionReason
     * @param settlementTaxCategoryCode
     * @param settlementTaxApplicablePercent
     * @param totalAmount
     * @param totalAmountCurrencyID
     * @param specifiedTradeProductGlobalID
     * @param specifiedTradeProductSchemeID
     * @param specifiedTradeProductSellerAssignedID
     * @param specifiedTradeProductBuyerAssignedID
     * @param specifiedTradeProductName
     * @param specifiedTradeProductDescription
     * @throws DataIncompleteException
     * @throws InvalidCodeException
     */
    protected void importLineItemComfort(Element parent,
        String lineID, String[][] note,
        String grossPriceChargeAmount, String grossPriceChargeAmountCurrencyID,
        String grossPriceBasisQuantity, String grossPriceBasisQuantityCode,
        Boolean[] grossPriceTradeAllowanceChargeIndicator,
        String[] grossPriceTradeAllowanceChargeActualAmount,
        String[] grossPriceTradeAllowanceChargeActualAmountCurrencyID,
        String[] grossPriceTradeAllowanceChargeReason,
        String netPriceChargeAmount, String netPriceChargeAmountCurrencyID,
        String netPriceBasisQuantity, String netPriceBasisQuantityCode,
        String billedQuantity, String billedQuantityCode,
        String[] settlementTaxTypeCode, String[] settlementTaxExemptionReason,
        String[] settlementTaxCategoryCode, String[] settlementTaxApplicablePercent,
        String totalAmount, String totalAmountCurrencyID,
        String specifiedTradeProductGlobalID, String specifiedTradeProductSchemeID,
        String specifiedTradeProductSellerAssignedID, String specifiedTradeProductBuyerAssignedID,
        String specifiedTradeProductName, String specifiedTradeProductDescription)
            throws DataIncompleteException, InvalidCodeException {

        /* ram:AssociatedDocumentLineDocument */
        Element sub = (Element)parent.getElementsByTagName("ram:AssociatedDocumentLineDocument").item(0);
        importContent(sub, "ram:LineID", lineID);
        importIncludedNotes(sub, FreeTextSubjectCode.LINE, note, null);

        /* ram:SpecifiedSupplyChainTradeAgreement */
        // ram:GrossPriceProductTradePrice
        if (grossPriceChargeAmount != null) {
            sub = (Element)parent.getElementsByTagName("ram:GrossPriceProductTradePrice").item(0);
            importContent(sub, "ram:ChargeAmount", DEC4.check(grossPriceChargeAmount), "currencyID", CURR_CODE.check(grossPriceChargeAmountCurrencyID));
            if (grossPriceBasisQuantity != null)
                importContent(sub, "ram:BasisQuantity", DEC4.check(grossPriceBasisQuantity), "unitCode", M_UNIT_CODE.check(grossPriceBasisQuantityCode));
            Node node = sub.getElementsByTagName("ram:AppliedTradeAllowanceCharge").item(0);
            if (grossPriceTradeAllowanceChargeIndicator != null) {
                for (int i = 0; i < grossPriceTradeAllowanceChargeIndicator.length; i++) {
                    Node newNode = node.cloneNode(true);
                    importAppliedTradeAllowanceCharge((Element)newNode,
                        grossPriceTradeAllowanceChargeIndicator[i],
                        grossPriceTradeAllowanceChargeActualAmount[i],
                        grossPriceTradeAllowanceChargeActualAmountCurrencyID[i],
                        grossPriceTradeAllowanceChargeReason[i]);
                    sub.insertBefore(newNode, node);
                }
            }
        }
        // ram:NetPriceProductTradePrice
        if (netPriceChargeAmount != null) {
            sub = (Element)parent.getElementsByTagName("ram:NetPriceProductTradePrice").item(0);
            importContent(sub, "ram:ChargeAmount", DEC4.check(netPriceChargeAmount), "currencyID", CURR_CODE.check(netPriceChargeAmountCurrencyID));
            if (netPriceBasisQuantity != null)
                importContent(sub, "ram:BasisQuantity", DEC4.check(netPriceBasisQuantity), "unitCode", M_UNIT_CODE.check(netPriceBasisQuantityCode));
        }
        
        /* ram:SpecifiedSupplyChainTradeDelivery */
        sub = (Element)parent.getElementsByTagName("ram:SpecifiedSupplyChainTradeDelivery").item(0);
        importContent(sub, "ram:BilledQuantity", DEC4.check(billedQuantity), "unitCode", M_UNIT_CODE.check(billedQuantityCode));
        
        /* ram:SpecifiedSupplyChainTradeSettlement */
        sub = (Element)parent.getElementsByTagName("ram:SpecifiedSupplyChainTradeSettlement").item(0);
        Node node = sub.getElementsByTagName("ram:ApplicableTradeTax").item(0);
        for (int i = 0; i < settlementTaxApplicablePercent.length; i++) {
            Node newNode = node.cloneNode(true);
            InvoiceDOM.this.importTax((Element) newNode, settlementTaxTypeCode[i], settlementTaxExemptionReason[i],
                settlementTaxCategoryCode[i], settlementTaxApplicablePercent[i]);
            sub.insertBefore(newNode, node);
        }
        importContent(sub, "ram:LineTotalAmount", totalAmount, "currencyID", totalAmountCurrencyID);
        
        /* ram:SpecifiedTradeProduct */
        sub = (Element)parent.getElementsByTagName("ram:SpecifiedTradeProduct").item(0);
        if (specifiedTradeProductGlobalID != null)
            importContent(sub, "ram:GlobalID", specifiedTradeProductGlobalID, "schemeID", GI_CODE.check(specifiedTradeProductSchemeID));
        importContent(sub, "ram:SellerAssignedID", specifiedTradeProductSellerAssignedID);
        importContent(sub, "ram:BuyerAssignedID", specifiedTradeProductBuyerAssignedID);
        importContent(sub, "ram:Name", specifiedTradeProductName);
        importContent(sub, "ram:Description", specifiedTradeProductDescription);
    }
    
    /**
     * Imports applied trade allowance charge data (line items).
     * @param parent    the parent element
     * @param indicator
     * @param actualAmount
     * @param currencyID
     * @param reason
     * @throws DataIncompleteException
     * @throws InvalidCodeException
     */
    protected void importAppliedTradeAllowanceCharge(Element parent,
        boolean indicator, String actualAmount,  String currencyID, String reason)
            throws DataIncompleteException, InvalidCodeException {
        importContent(parent, "udt:Indicator", indicator ? "true" : "false");
        check(DEC4.check(actualAmount), "AppliedTradeAllowanceCharge > ActualAmount");
        importContent(parent, "ram:ActualAmount", actualAmount, "currencyID", CURR_CODE.check(currencyID));
        importContent(parent, "ram:Reason", reason);
    }
    
    /**
     * Imports tax data.
     * @param parent    the parent element
     * @param typeCode
     * @param exemptionReason
     * @param category
     * @param percent
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException
     */
    protected void importTax(Element parent,
        String typeCode, String exemptionReason,
        String category, String percent)
            throws InvalidCodeException, DataIncompleteException {
        // Calculated amount (required; 2 decimals)
        // TypeCode (required)
        check(typeCode, "ApplicableTradeTax > TypeCode");
        importContent(parent, "ram:TypeCode", TT_CODE.check(typeCode));
        // exemption reason (optional)
        importContent(parent, "ram:ExemptionReason", exemptionReason);
        // Category code (optional)
        if (category != null) {
            importContent(parent, "ram:CategoryCode", TC_CODE.check(category));
        }
        // Applicable percent (required; 2 decimals)
        importContent(parent, "ram:ApplicablePercent", DEC2.check(percent));
    }
    
    /**
     * Gets line data to import the this data (basic profile).
     * @param parent
     * @param data
     * @throws DataIncompleteException
     * @throws InvalidCodeException
     */
    protected void importLineItemsBasic(Element parent, BasicProfile data)
            throws DataIncompleteException, InvalidCodeException {
        String[] quantity = data.getLineItemBilledQuantity();
        if (quantity.length == 0)
            throw new DataIncompleteException("You can create an invoice without any line items");
        String[] quantityCode = data.getLineItemBilledQuantityUnitCode();
        String[] name = data.getLineItemSpecifiedTradeProductName();
        Node node = parent.getElementsByTagName("ram:IncludedSupplyChainTradeLineItem").item(0);
        for (int i = 0; i < quantity.length; i++) {
            Node newNode = node.cloneNode(true);
            importLineItemBasic((Element)newNode, quantity[i], quantityCode[i], name[i]);
            parent.insertBefore(newNode, node);
        }
    }
    
    /**
     * Imports the data for a line item (basic profile)
     * @param parent    the parent element
     * @param quantity
     * @param code
     * @param name
     * @throws InvalidCodeException
     */
    protected void importLineItemBasic(Element parent,
        String quantity, String code, String name)
            throws InvalidCodeException {
        Element sub = (Element)parent.getElementsByTagName("ram:SpecifiedSupplyChainTradeDelivery").item(0);
        importContent(sub, "ram:BilledQuantity", DEC4.check(quantity), "unitCode", M_UNIT_CODE.check(code));
        sub = (Element)parent.getElementsByTagName("ram:SpecifiedTradeProduct").item(0);
        importContent(sub, "ram:Name", name);
    }
    
    // XML methods
    
    /**
     * Exports the Document as an XML file.
     * @return a byte[] with the data in XML format
     * @throws javax.xml.transform.TransformerException
     */
    public byte[] toXML() throws TransformerException {
        removeEmptyNodes(doc);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (Exception exc) {}
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	    DOMSource source = new DOMSource(doc);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Result result = new StreamResult(out);
        transformer.transform(source, result);
        return out.toByteArray();
    }
    
    /**
     * It is forbidden for a ZUGFeRD XML to contain empty tags, hence
     * we use this method recursively to remove empty nodes.
     * @param node  the node from which we want to remove the empty nodes
     */
    protected static void removeEmptyNodes(Node node) {
        NodeList list = node.getChildNodes();
        for (int i = list.getLength() - 1; i >= 0; i--) {
            removeEmptyNodes(list.item(i));
        }
        boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE
            && node.getChildNodes().getLength() == 0;
        boolean emptyText = node.getNodeType() == Node.TEXT_NODE
            && node.getNodeValue().trim().length() == 0;
        if (emptyElement || emptyText) {
            node.getParentNode().removeChild(node);
        }
    }
    
    // helper methods
    
    /**
     * Checks if a string is empty and throws a DataIncompleteException if so.
     * @param   s   the String to check
     * @param   message the message if an exception is thrown   
     * @throws com.itextpdf.text.zugferd.exceptions.DataIncompleteException   
     */
    protected void check(String s, String message) throws DataIncompleteException {
        if (s == null || s.trim().length() == 0)
            throw new DataIncompleteException(message);
    }

    private static class SafeEmptyEntityResolver implements EntityResolver {
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(new StringReader(""));
        }
    }
}
