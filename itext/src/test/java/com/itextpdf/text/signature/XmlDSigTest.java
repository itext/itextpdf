package com.itextpdf.text.signature;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeXmlSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.SecurityConstants;
import org.apache.jcp.xml.dsig.internal.dom.DOMKeyInfoFactory;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;

/**
 * This class is abstract to prevent old Surefire versions from
 * running it as a test and failing because it contains no tests.
 */
public abstract class XmlDSigTest {


    protected BouncyCastleProvider provider;

    protected void initialize() throws Exception {
        provider = new BouncyCastleProvider();
        Security.addProvider(provider);
    }

    protected void signXadesWithCertificate(String src, String dest, PrivateKey pk, Certificate[] chain, String digestAlgorithm, String provider, boolean includeSignaturePolicy) throws GeneralSecurityException, IOException, DocumentException, TransformException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        appearance.setDescription("Simple xfa form");
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        //MakeXmlSignature.signXadesBes(appearance, pks, chain);
        MakeXmlSignature.signXades(appearance, pks, chain, includeSignaturePolicy);
    }

    protected void signDsWithCertificate(String src, String dest, PrivateKey pk, Certificate[] chain, String digestAlgorithm, String provider) throws GeneralSecurityException, IOException, DocumentException, TransformException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        //MakeXmlSignature.signXadesBes(appearance, pks, chain);
        MakeXmlSignature.signXmlDSig(appearance, pks, chain);
    }

    protected void signDsWithKeyInfo(String src, String dest, PrivateKey pk, PublicKey publicKey, String digestAlgorithm,
                                     String provider) throws GeneralSecurityException, IOException, DocumentException, TransformException, URIReferenceException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        //Set XfaXmlLocator to control getting and setting Document
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        KeyInfoFactory kif = new DOMKeyInfoFactory();
        KeyValue kv = kif.newKeyValue(publicKey);

        MakeXmlSignature.signXmlDSig(appearance, pks, kif.newKeyInfo(Collections.singletonList(kv)));
    }

    protected void signDsWithPublicKey(String src, String dest, PrivateKey pk, PublicKey publicKey, String digestAlgorithm,
                                       String provider) throws GeneralSecurityException, IOException, DocumentException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        //Set XfaXmlLocator to control getting and setting Document
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);

        MakeXmlSignature.signXmlDSig(appearance, pks, publicKey);
    }

    protected void signPackageXadesWithCertificate(String src, String dest, XfaXpathConstructor.XdpPackage xdpPackage, PrivateKey pk, Certificate[] chain, String digestAlgorithm, String provider, boolean includeSignaturePolicy) throws GeneralSecurityException, IOException, DocumentException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        appearance.setDescription("Simple xfa form");
        //Set XfaXmlLocator to control getting and setting Document
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Set XpathConstructor, to construct xpath expression for signing an xdp package
        appearance.setXpathConstructor(new XfaXpathConstructor(xdpPackage));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);

        MakeXmlSignature.signXades(appearance, pks, chain, includeSignaturePolicy);
    }

    protected void signPackageDsWithCertificate(String src, String dest, XfaXpathConstructor.XdpPackage xdpPackage, PrivateKey pk, Certificate[] chain, String digestAlgorithm, String provider) throws GeneralSecurityException, IOException, DocumentException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        //Set XfaXmlLocator to control getting and setting Document
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Set XpathConstructor, to construct xpath expression for signing an xdp package
        appearance.setXpathConstructor(new XfaXpathConstructor(xdpPackage));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);

        MakeXmlSignature.signXmlDSig(appearance, pks, chain);
    }

    protected void signPackageDsWithKeyInfo(String src, String dest, XfaXpathConstructor.XdpPackage xdpPackage, PrivateKey pk, PublicKey publicKey, String digestAlgorithm, String provider) throws GeneralSecurityException, IOException, DocumentException, TransformException, URIReferenceException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        //Set XfaXmlLocator to control getting and setting Document
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Set XpathConstructor, to construct xpath expression for signing an xdp package
        appearance.setXpathConstructor(new XfaXpathConstructor(xdpPackage));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        KeyInfoFactory kif = new DOMKeyInfoFactory();
        KeyValue kv = kif.newKeyValue(publicKey);

        MakeXmlSignature.signXmlDSig(appearance, pks, kif.newKeyInfo(Collections.singletonList(kv)));
    }

    protected void signPackageDsWithPublicKey(String src, String dest, XfaXpathConstructor.XdpPackage xdpPackage, PrivateKey pk, PublicKey publicKey, String digestAlgorithm, String provider) throws GeneralSecurityException, IOException, DocumentException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        //Set XfaXmlLocator to control getting and setting Document
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Set XpathConstructor, to construct xpath expression for signing an xdp package
        appearance.setXpathConstructor(new XfaXpathConstructor(xdpPackage));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);

        MakeXmlSignature.signXmlDSig(appearance, pks, publicKey);
    }

    protected String saveXmlFromResult(String input)
            throws IOException, ParserConfigurationException, SAXException, TransformerException {
        PdfReader reader = new PdfReader(input);
        XfaForm form = new XfaForm(reader);

        String output = input.replace(".pdf", ".xml");

        FileOutputStream file = new FileOutputStream(output);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(form.getDomDocument()), new StreamResult(file));
        file.close();

        reader.close();

        return output;
    }

    protected boolean compareXmls(String xml1, String xml2) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        org.w3c.dom.Document doc1 = db.parse(new File(xml1));
        doc1.normalizeDocument();

        org.w3c.dom.Document doc2 = db.parse(new File(xml2));
        doc2.normalizeDocument();

        return doc2.isEqualNode(doc1);
    }

    boolean verifyXmlDSig(String filename) throws Exception {
        DocumentBuilderFactory dbf =
                DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(new FileInputStream(filename));

        NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if (nl.getLength() == 0)
            throw new Exception("Cannot find Signature element");

        NodeList sps = doc.getElementsByTagNameNS(SecurityConstants.XADES_132_URI, "SignedProperties");
        if (sps.getLength() > 0)
            ((Element)sps.item(0)).setIdAttribute("Id", true);

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI());
        DOMValidateContext valContext = new DOMValidateContext
                (new KeyValueKeySelector(), nl.item(0));

        XMLSignature signature =
                fac.unmarshalXMLSignature(valContext);
        return signature.validate(valContext);
    }

    /**
     * KeySelector which retrieves the public key out of the
     * KeyValue element and returns it.
     * NOTE: If the key algorithm doesn't match signature algorithm,
     * then the public key will be ignored.
     */
    private static class KeyValueKeySelector extends KeySelector {
        public KeySelectorResult select(KeyInfo keyInfo,
                                        KeySelector.Purpose purpose,
                                        AlgorithmMethod method,
                                        XMLCryptoContext context)
                throws KeySelectorException {
            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            if (keyInfo.getContent().isEmpty())
                throw new KeySelectorException("No KeyValue element found!");

            XMLStructure xmlStructure = (XMLStructure) keyInfo.getContent().get(0);
            PublicKey pk = null;
            if (xmlStructure instanceof KeyValue) {

                try {
                    pk = ((KeyValue)xmlStructure).getPublicKey();
                } catch (KeyException ke) {
                    throw new KeySelectorException(ke);
                }
            }
            else if (xmlStructure instanceof X509Data) {
                X509Data xd = (X509Data) keyInfo.getContent().get(0);
                X509Certificate cert = (X509Certificate)xd.getContent().get(0);
                pk = cert.getPublicKey();
            }

            // make sure algorithm is compatible with method
            if (pk != null && algEquals(sm.getAlgorithm(), pk.getAlgorithm()))
                return new SimpleKeySelectorResult(pk);

            throw new KeySelectorException("No KeyValue element found!");
        }

        static boolean algEquals(String algURI, String algName) {
            if (algName.equalsIgnoreCase("DSA") &&
                    algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
                return true;
            } else if (algName.equalsIgnoreCase("RSA") &&
                    algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private static class SimpleKeySelectorResult implements KeySelectorResult {
        private PublicKey pk;
        SimpleKeySelectorResult(PublicKey pk) {
            this.pk = pk;
        }

        public Key getKey() { return pk; }
    }
}
