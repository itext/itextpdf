/*
 * $Id: MakeXmlSignature.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Pavel Alay, Bruno Lowagie, et al.
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
package com.itextpdf.text.pdf.security;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.XmlSignatureAppearance;
import org.apache.jcp.xml.dsig.internal.dom.*;
import org.apache.xml.security.utils.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec;
import javax.xml.crypto.dsig.spec.XPathType;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class that signs your XML.
 */
public class MakeXmlSignature {

    /**
     * Empty class for key simulation
     */
    private static class EmptyKey implements Key {

        private EmptyKey(){}

        private static EmptyKey instance = new EmptyKey();

        public static EmptyKey getInstance() {
            return instance;
        }

        public String getAlgorithm() {
            return null;
        }

        public String getFormat() {
            return null;
        }

        public byte[] getEncoded() {
            return new byte[0];
        }
    }

    /**
     * Signs the xml with XmlDSig using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param keyInfo KeyInfo for verification
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, KeyInfo keyInfo)
            throws GeneralSecurityException, IOException, DocumentException {

        verifyArguments(sap, externalSignature);
        XMLSignatureFactory fac = createSignatureFactory();
        Reference reference = generateContentReference(fac, sap, null);
        String signatureMethod = null;
        if (externalSignature.getEncryptionAlgorithm().equals(SecurityConstants.RSA))
            signatureMethod = SignatureMethod.RSA_SHA1;
        else if (externalSignature.getEncryptionAlgorithm().equals(SecurityConstants.DSA))
            signatureMethod = SignatureMethod.DSA_SHA1;

        // Create the SignedInfo
        DOMSignedInfo signedInfo = (DOMSignedInfo)fac.newSignedInfo(
                fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                fac.newSignatureMethod(signatureMethod, null), Collections.singletonList(reference));
        //sign and update document with XmlLocator
        sign(fac, externalSignature, sap.getXmlLocator(), signedInfo, null, keyInfo, null);

        sap.close();
    }

    /**
     * Signs the xml with XmlDSig using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param chain the certificate chain
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain)
            throws DocumentException, GeneralSecurityException, IOException {
        signXmlDSig(sap, externalSignature, generateKeyInfo(chain, sap));
    }

    /**
     * Signs the xml with XmlDSig using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param publicKey PublicKey for verification
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, PublicKey publicKey)
            throws GeneralSecurityException, DocumentException, IOException {
        signXmlDSig(sap, externalSignature, generateKeyInfo(publicKey));
    }

    /**
     * Signs the xml with XAdES BES using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param chain the certificate chain
     * @param includeSignaturePolicy if true SignaturePolicyIdentifier will be included (XAdES-EPES)
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXades(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain,
                                 boolean includeSignaturePolicy)
            throws GeneralSecurityException, DocumentException, IOException {

        verifyArguments(sap, externalSignature);

        String signatureMethod = null;
        if (externalSignature.getEncryptionAlgorithm().equals(SecurityConstants.RSA))
            signatureMethod = SignatureMethod.RSA_SHA1;
        else if (externalSignature.getEncryptionAlgorithm().equals(SecurityConstants.DSA))
            signatureMethod = SignatureMethod.DSA_SHA1;

        String contentReferenceId = SecurityConstants.Reference_ + getRandomId();
        String signedPropertiesId = SecurityConstants.SignedProperties_ + getRandomId();
        String signatureId = SecurityConstants.Signature_ + getRandomId();

        XMLSignatureFactory fac = createSignatureFactory();

        KeyInfo keyInfo = generateKeyInfo(chain, sap);
        String[] signaturePolicy = null;
        if (includeSignaturePolicy) {
            signaturePolicy = new String[2];
            if (signatureMethod.equals(SignatureMethod.RSA_SHA1)) {
                signaturePolicy[0] = SecurityConstants.OID_RSA_SHA1;
                signaturePolicy[1] = SecurityConstants.OID_RSA_SHA1_DESC;
            } else {
                signaturePolicy[0] = SecurityConstants.OID_DSA_SHA1;
                signaturePolicy[1] = SecurityConstants.OID_DSA_SHA1_DESC;
            }
        }
        XMLObject xmlObject = generateXadesObject(fac, sap, signatureId, contentReferenceId, signedPropertiesId, signaturePolicy);
        Reference contentReference = generateContentReference(fac, sap, contentReferenceId);
        Reference signedPropertiesReference = generateCustomReference(fac, "#"+signedPropertiesId, SecurityConstants.SignedProperties_Type, null);

        List<Reference> references = Arrays.asList(signedPropertiesReference, contentReference);

        // Create the SignedInfo
        DOMSignedInfo signedInfo = (DOMSignedInfo)fac.newSignedInfo(
                fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                fac.newSignatureMethod(signatureMethod, null), references, null);
        //sign and update document with XmlLocator
        sign(fac, externalSignature, sap.getXmlLocator(), signedInfo, xmlObject, keyInfo, signatureId);

        sap.close();
    }

    /**
     * Signs the xml with XAdES BES using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param chain the certificate chain
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXadesBes(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain)
            throws GeneralSecurityException, DocumentException, IOException {
        signXades(sap, externalSignature, chain, false);
    }

    /**
     * Signs the xml with XAdES BES using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param chain the certificate chain
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXadesEpes(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain)
            throws GeneralSecurityException, DocumentException, IOException {
        signXades(sap, externalSignature, chain, true);
    }

    private static void verifyArguments(XmlSignatureAppearance sap, ExternalSignature externalSignature)
            throws DocumentException {
        if (sap.getXmlLocator() == null)
            throw new DocumentException(MessageLocalization.getComposedMessage("xmllocator.cannot.be.null"));
        if (!externalSignature.getHashAlgorithm().equals(SecurityConstants.SHA1))
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("support.only.sha1.hash.algorithm"));

        if (!externalSignature.getEncryptionAlgorithm().equals(SecurityConstants.RSA) && !externalSignature.getEncryptionAlgorithm().equals(SecurityConstants.DSA))
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("support.only.rsa.and.dsa.algorithms"));
    }

    /**
     * Find Signature and SignatureValue elements after marshalization.
     */
    private static Element findElement(NodeList nodes, String localName) {
        for(int i = nodes.getLength() - 1; i >= 0; --i) {
            Node currNode = nodes.item(i);
            if (currNode.getNodeType() == Node.ELEMENT_NODE && currNode.getLocalName().equals(localName))
                return (Element)currNode;
        }
        return null;
    }

    private static KeyInfo generateKeyInfo(Certificate[] chain, XmlSignatureAppearance sap) {
        Certificate certificate = chain[0];
        sap.setCertificate(certificate);
        KeyInfoFactory kif = new DOMKeyInfoFactory();
        // Create an X509Data containing the X.509 certificate
        X509Data x509d = kif.newX509Data(Collections.singletonList(certificate));
        // Create a KeyInfo and add the KeyValue to it
        return kif.newKeyInfo(Collections.singletonList(x509d));
    }

    private static KeyInfo generateKeyInfo(PublicKey publicKey) throws GeneralSecurityException {
        KeyInfoFactory kif = new DOMKeyInfoFactory();
        KeyValue kv = kif.newKeyValue(publicKey);
        return kif.newKeyInfo(Collections.singletonList(kv));
    }

    private static String getRandomId() {
        return UUID.randomUUID().toString().substring(24);
    }

    private static XMLSignatureFactory createSignatureFactory(){
        return XMLSignatureFactory.getInstance("DOM", new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());
    }

    private static XMLObject generateXadesObject(XMLSignatureFactory fac, XmlSignatureAppearance sap,
            String signatureId, String contentReferenceId, String signedPropertiesId, String[] signaturePolicy)
            throws GeneralSecurityException {

        MessageDigest md = MessageDigest.getInstance(SecurityConstants.SHA1);
        Certificate cert = sap.getCertificate();

        org.w3c.dom.Document doc = sap.getXmlLocator().getDocument();

        Element QualifyingProperties = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_QualifyingProperties);
        QualifyingProperties.setAttribute("Target", "#"+signatureId);
            Element SignedProperties = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_SignedProperties);
            SignedProperties.setAttribute("Id", signedPropertiesId);
            SignedProperties.setIdAttribute("Id", true);
                Element SignedSignatureProperties = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_SignedSignatureProperties);
                    Element SigningTime = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_SigningTime);
                        SimpleDateFormat sdf = new SimpleDateFormat(SecurityConstants.SigningTimeFormat);
                        String result = sdf.format(sap.getSignDate().getTime());
                        result = result.substring(0, result.length()-2).concat(":").concat(result.substring(result.length()-2));
                    SigningTime.appendChild(doc.createTextNode(result));
                SignedSignatureProperties.appendChild(SigningTime);
                    Element SigningCertificate = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_SigningCertificate);
                        Element Cert = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_Cert);
                            Element CertDigest = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_CertDigest);
                                Element DigestMethod = doc.createElementNS(SecurityConstants.XMLDSIG_URI, SecurityConstants.DigestMethod);
                                DigestMethod.setAttribute(SecurityConstants.Algorithm, SecurityConstants.SHA1_URI);
                            CertDigest.appendChild(DigestMethod);
                                Element DigestValue = doc.createElementNS(SecurityConstants.XMLDSIG_URI, SecurityConstants.DigestValue);
                                DigestValue.appendChild(doc.createTextNode(Base64.encode(md.digest(cert.getEncoded()))));
                            CertDigest.appendChild(DigestValue);
                        Cert.appendChild(CertDigest);
                        if (cert instanceof X509Certificate) {
                            Element IssueSerial = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_IssuerSerial);
                                Element X509IssuerName = doc.createElementNS(SecurityConstants.XMLDSIG_URI, SecurityConstants.X509IssuerName);
                                X509IssuerName.appendChild(doc.createTextNode(getX509IssuerName((X509Certificate)cert)));
                            IssueSerial.appendChild(X509IssuerName);
                                Element X509SerialNumber = doc.createElementNS(SecurityConstants.XMLDSIG_URI, SecurityConstants.X509SerialNumber);
                                X509SerialNumber.appendChild(doc.createTextNode(getX509SerialNumber((X509Certificate) cert)));
                            IssueSerial.appendChild(X509SerialNumber);
                        Cert.appendChild(IssueSerial);
                        }
                    SigningCertificate.appendChild(Cert);
                SignedSignatureProperties.appendChild(SigningCertificate);
                if (signaturePolicy != null) {
                    Element SignaturePolicyIdentifier = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_SignaturePolicyIdentifier);
                        Element SignaturePolicyId = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_SignaturePolicyId);
                            Element SigPolicyId = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_SigPolicyId);
                                Element Identifier = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_Identifier);
                                Identifier.appendChild(doc.createTextNode(signaturePolicy[0]));
                                Identifier.setAttribute(SecurityConstants.Qualifier, SecurityConstants.OIDAsURN);
                            SigPolicyId.appendChild(Identifier);
                    //ANSI X9.57 DSA signature generated with SHA-1 hash (DSA x9.30)
                                Element Description = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_Description);
                                Description.appendChild(doc.createTextNode(signaturePolicy[1]));
                            SigPolicyId.appendChild(Description);
                        SignaturePolicyId.appendChild(SigPolicyId);
                            Element SigPolicyHash = doc.createElementNS(SecurityConstants.XADES_132_URI, SecurityConstants.XADES_SigPolicyHash);
                                DigestMethod = doc.createElementNS(SecurityConstants.XMLDSIG_URI, SecurityConstants.DigestMethod);
                                DigestMethod.setAttribute(SecurityConstants.Algorithm, SecurityConstants.SHA1_URI);
                            SigPolicyHash.appendChild(DigestMethod);
                                DigestValue = doc.createElementNS(SecurityConstants.XMLDSIG_URI, SecurityConstants.DigestValue);
                                byte[] policyIdContent = getByteArrayOfNode(SigPolicyId);
                                DigestValue.appendChild(doc.createTextNode(Base64.encode(md.digest(policyIdContent))));
                            SigPolicyHash.appendChild(DigestValue);
                        SignaturePolicyId.appendChild(SigPolicyHash);
                    SignaturePolicyIdentifier.appendChild(SignaturePolicyId);
                SignedSignatureProperties.appendChild(SignaturePolicyIdentifier);
                }
                SignedProperties.appendChild(SignedSignatureProperties);
                Element SignedDataObjectProperties = doc.createElement(SecurityConstants.XADES_SignedDataObjectProperties);
                    Element DataObjectFormat = doc.createElement(SecurityConstants.XADES_DataObjectFormat);
                    DataObjectFormat.setAttribute(SecurityConstants.ObjectReference, "#" + contentReferenceId);
                        String descr = sap.getDescription();
                    if (descr != null) {
                        Element Description = doc.createElement(SecurityConstants.XADES_Description);
                        Description.appendChild(doc.createTextNode(descr));
                    DataObjectFormat.appendChild(Description);
                    }
                        Element MimeType = doc.createElement(SecurityConstants.XADES_MimeType);
                        MimeType.appendChild(doc.createTextNode(sap.getMimeType()));
                    DataObjectFormat.appendChild(MimeType);
                        String enc = sap.getXmlLocator().getEncoding();
                    if (enc != null) {
                        Element Encoding = doc.createElement(SecurityConstants.XADES_Encoding);
                        Encoding.appendChild(doc.createTextNode(enc));
                    DataObjectFormat.appendChild(Encoding);
                    }
                SignedDataObjectProperties.appendChild(DataObjectFormat);
            SignedProperties.appendChild(SignedDataObjectProperties);
        QualifyingProperties.appendChild(SignedProperties);

        XMLStructure content = new DOMStructure(QualifyingProperties);
        return fac.newXMLObject(Collections.singletonList(content), null, null, null);
    }

    private static String getX509IssuerName(X509Certificate cert) {
        return cert.getIssuerX500Principal().toString();
    }

    private static String getX509SerialNumber(X509Certificate cert) {
        return cert.getSerialNumber().toString();
    }

    private static Reference generateContentReference(XMLSignatureFactory fac, XmlSignatureAppearance sap, String referenceId)
            throws GeneralSecurityException {
        DigestMethod digestMethodSHA1 = fac.newDigestMethod(DigestMethod.SHA1, null);

        List<Transform> transforms = new ArrayList<Transform>();
        transforms.add(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));

        // Create the Reference
        XpathConstructor xpathConstructor = sap.getXpathConstructor();
        if (xpathConstructor != null && xpathConstructor.getXpathExpression().length() > 0) {
            XPathFilter2ParameterSpec xpath2Spec = new XPathFilter2ParameterSpec(Collections.singletonList(new XPathType(xpathConstructor.getXpathExpression(), XPathType.Filter.INTERSECT)));
            transforms.add(fac.newTransform(Transform.XPATH2, xpath2Spec));
        }
        return  fac.newReference("", digestMethodSHA1, transforms, null, referenceId);
    }

    private static Reference generateCustomReference(XMLSignatureFactory fac, String uri, String type, String id) throws GeneralSecurityException {
        DigestMethod dsDigestMethod = fac.newDigestMethod(DigestMethod.SHA1, null);
        return  fac.newReference(uri, dsDigestMethod, null, type, id);
    }

    private static void sign(XMLSignatureFactory fac, ExternalSignature externalSignature, XmlLocator locator,
                             DOMSignedInfo si, XMLObject xo, KeyInfo ki, String signatureId) throws DocumentException {

        Document doc = locator.getDocument();

        DOMSignContext domSignContext = new DOMSignContext(EmptyKey.getInstance(), doc.getDocumentElement());

        List objects = null;
        if (xo != null)
            objects = Collections.singletonList(xo);
        DOMXMLSignature signature = (DOMXMLSignature)fac.newXMLSignature(si, ki, objects, signatureId, null);

        ByteArrayOutputStream byteRange = new ByteArrayOutputStream();
        try {
            signature.marshal(domSignContext.getParent(), domSignContext.getNextSibling(),
                    DOMUtils.getSignaturePrefix(domSignContext), domSignContext);
            Element signElement = findElement(doc.getDocumentElement().getChildNodes(), SecurityConstants.Signature);
            if (signatureId != null)
                signElement.setAttributeNS(SecurityConstants.XMLNS_URI, SecurityConstants.XMLNS_XADES, SecurityConstants.XADES_132_URI);

            List references = si.getReferences();
            for (int i = 0; i < references.size(); i++)
                    ((DOMReference)references.get(i)).digest(domSignContext);
            si.canonicalize(domSignContext, byteRange);

            Element signValue = findElement(signElement.getChildNodes(), SecurityConstants.SignatureValue);

            //Sign with ExternalSignature
            String valueBase64 = Base64.encode(externalSignature.sign(byteRange.toByteArray()));
            //Set calculated SignatureValue
            signValue.appendChild(doc.createTextNode(valueBase64));
            locator.setDocument(doc);
        } catch (Exception e) {
            throw new DocumentException(e);
        }
    }

    private static byte[] getByteArrayOfNode(Node node) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(node), xmlOutput);
            return xmlOutput.getWriter().toString().getBytes();
        } catch (Exception e) {
        }
        return stream.toByteArray();
    }
}
