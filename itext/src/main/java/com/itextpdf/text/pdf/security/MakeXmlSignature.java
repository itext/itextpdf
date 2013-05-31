/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Pavel Alay, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
import org.apache.xml.security.utils.Base64;
import org.apache.jcp.xml.dsig.internal.dom.DOMReference;
import org.apache.jcp.xml.dsig.internal.dom.DOMSignedInfo;
import org.apache.jcp.xml.dsig.internal.dom.DOMXMLSignature;
import org.apache.jcp.xml.dsig.internal.dom.DOMUtils;
import org.apache.jcp.xml.dsig.internal.dom.DOMKeyInfoFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * Signs the xml using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param keyInfo KeyInfo for verification
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, KeyInfo keyInfo)
            throws GeneralSecurityException, IOException, DocumentException {

        XmlLocator xmlLocator = sap.getXmlLocator();
        if (xmlLocator == null)
            throw new DocumentException(MessageLocalization.getComposedMessage("xmllocator.cannot.be.null"));
        if (!externalSignature.getHashAlgorithm().equals("SHA1"))
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("support.only.sha1.hash.algorithm"));

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance
                        ("DOM", new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());
        DigestMethod digestMethodSHA1 = fac.newDigestMethod(DigestMethod.SHA1, null);

        Document doc = xmlLocator.getDocument();

        List<Transform> transforms = new ArrayList<Transform>();
        transforms.add(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));

        // Create the Reference
        XpathConstructor xpathConstructor = sap.getXpathConstructor();
        if (xpathConstructor != null && xpathConstructor.getXpathExpression().length() > 0) {
            XPathFilter2ParameterSpec xpath2Spec = new XPathFilter2ParameterSpec(Collections.singletonList(new XPathType(xpathConstructor.getXpathExpression(), XPathType.Filter.INTERSECT)));
            transforms.add(fac.newTransform(Transform.XPATH2, xpath2Spec));
        }
        DOMReference reference = (DOMReference)fac.newReference("", digestMethodSHA1, transforms, null, null);

        String signatureMethod;
        if (externalSignature.getEncryptionAlgorithm().equals("RSA"))
            signatureMethod = SignatureMethod.RSA_SHA1;
        else if (externalSignature.getEncryptionAlgorithm().equals("DSA"))
            signatureMethod = SignatureMethod.DSA_SHA1;
        else
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("support.only.rsa.and.dsa.algorithms"));

        // Create the SignedInfo
        DOMSignedInfo signedInfo = (DOMSignedInfo)fac.newSignedInfo(
                fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                fac.newSignatureMethod(signatureMethod, null), Collections.singletonList(reference));

        DOMSignContext domSignContext = new DOMSignContext(EmptyKey.getInstance(), doc.getDocumentElement());

        DOMXMLSignature signature = (DOMXMLSignature)fac.newXMLSignature(signedInfo, keyInfo);
        ByteArrayOutputStream byteRange = new ByteArrayOutputStream();
        try {
            signature.marshal(domSignContext.getParent(), domSignContext.getNextSibling(),
                    DOMUtils.getSignaturePrefix(domSignContext), domSignContext);
            reference.digest(domSignContext);
            signedInfo.canonicalize(domSignContext, byteRange);

            Element signElement = findElement(doc.getDocumentElement().getChildNodes(), "Signature");
            Element signValue = findElement(signElement.getChildNodes(), "SignatureValue");

            //Sign with ExternalSignature
            String valueBase64 = Base64.encode(externalSignature.sign(byteRange.toByteArray()));
            //Set calculated SignatureValue
            signValue.appendChild(doc.createTextNode(valueBase64));
            xmlLocator.setDocument(doc);

        } catch (Exception e) {
            throw new DocumentException(e);
        }

        sap.close();
    }

    /**
     * Signs the xml using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param chain the certificate chain
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, Certificate[] chain)
            throws DocumentException, GeneralSecurityException, IOException {

        Certificate certificate = chain[0];
        sap.setCertificate(certificate);
        KeyInfoFactory kif = new DOMKeyInfoFactory();
        // Create an X509Data containing the X.509 certificate
        X509Data x509d = kif.newX509Data(Collections.singletonList(certificate));
        // Create a KeyInfo and add the KeyValue to it
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));

        signXmlDSig(sap, externalSignature, ki);
    }

    /**
     * Signs the xml using the enveloped mode, with optional xpath transform (see XmlSignatureAppearance).
     * @param sap the XmlSignatureAppearance
     * @param externalSignature  the interface providing the actual signing
     * @param publicKey PublicKey for verification
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws DocumentException
     */
    public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, PublicKey publicKey)
            throws GeneralSecurityException, DocumentException, IOException {
        KeyInfoFactory kif = new DOMKeyInfoFactory();
        KeyValue kv = kif.newKeyValue(publicKey);
        signXmlDSig(sap, externalSignature, kif.newKeyInfo(Collections.singletonList(kv)));
    }

    /**
     * Find Signature and SignatureValue elements after marshalization.
     */
    private static Element findElement(NodeList nodes, String localName)
    {
        for(int i = nodes.getLength() - 1; i >= 0; --i) {
            Node currNode = nodes.item(i);
            if (currNode.getNodeType() == Node.ELEMENT_NODE && currNode.getLocalName().equals(localName))
                return (Element)currNode;
        }
        return null;
    }
}
