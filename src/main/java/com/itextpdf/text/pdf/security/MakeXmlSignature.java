package com.itextpdf.text.pdf.security;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.XmlSignatureAppearance;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.jcp.xml.dsig.internal.dom.DOMReference;
import org.jcp.xml.dsig.internal.dom.DOMSignedInfo;
import org.jcp.xml.dsig.internal.dom.DOMXMLSignature;
import org.jcp.xml.dsig.internal.dom.DOMXMLSignatureFactory;
import org.jcp.xml.dsig.internal.dom.DOMUtils;
import org.jcp.xml.dsig.internal.dom.DOMKeyInfoFactory;
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
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Collections;


public class MakeXmlSignature {

    /** The Logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MakeXmlSignature.class);

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

    public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, KeyInfo keyInfo)
            throws GeneralSecurityException, IOException, DocumentException {

        XmlLocator xmlLocator = sap.getXmlLocator();
        if (xmlLocator == null)
            throw new DocumentException("Set XmlLocator to XMlSignatureAppearance");
        if (!externalSignature.getHashAlgorithm().equals("SHA1"))
            throw new UnsupportedOperationException("Support SHA1 hash algorithm");

        XMLSignatureFactory fac = DOMXMLSignatureFactory.getInstance();
        DigestMethod digestMethodSHA1 = fac.newDigestMethod(DigestMethod.SHA1, null);

        Document doc = xmlLocator.getDocument();

        // Create the Reference
        DOMReference reference = (DOMReference)fac.newReference("", digestMethodSHA1,
                Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                null, null);

        String signatureMethod;
        if (externalSignature.getEncryptionAlgorithm().equals("RSA"))
            signatureMethod = SignatureMethod.RSA_SHA1;
        else if (externalSignature.getEncryptionAlgorithm().equals("DSA"))
            signatureMethod = SignatureMethod.DSA_SHA1;
        else
            throw new UnsupportedOperationException("Support only RSA and DSA");

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


    private static Element findElement(NodeList nodes, String localName)
    {
        for(int i = nodes.getLength() - 1; i >= 0; --i) {
            Node currNode = nodes.item(i);
            if (currNode.getNodeType() == Node.ELEMENT_NODE && currNode.getLocalName().equals(localName))
                return (Element)currNode;
        }
        return null;
    }

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

    public static void signXmlDSig(XmlSignatureAppearance sap, ExternalSignature externalSignature, PublicKey publicKey)
            throws GeneralSecurityException, DocumentException, IOException {
        KeyInfoFactory kif = new DOMKeyInfoFactory();
        KeyValue kv = kif.newKeyValue(publicKey);
        signXmlDSig(sap, externalSignature, kif.newKeyInfo(Collections.singletonList(kv)));
    }
}
