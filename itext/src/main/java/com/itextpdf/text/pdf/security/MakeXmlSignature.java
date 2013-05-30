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
