package com.itextpdf.text.signature;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.jcp.xml.dsig.internal.dom.DOMKeyInfoFactory;
import org.xml.sax.SAXException;

import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Collections;

public class XmlDSigTest {


    protected BouncyCastleProvider provider;

    protected void initialize() throws Exception {
        provider = new BouncyCastleProvider();
        Security.addProvider(provider);
    }

    protected void signWithCertificate(String src, String dest, PrivateKey pk, Certificate[] chain, String digestAlgorithm, String provider) throws GeneralSecurityException, IOException, DocumentException {

        // Creating the reader and the stamper
        PdfReader reader = new PdfReader(src);
        FileOutputStream os = new FileOutputStream(dest);
        PdfStamper stamper = PdfStamper.createXmlSignature(reader, os);
        // Creating the appearance
        XmlSignatureAppearance appearance = stamper.getXmlSignatureAppearance();
        appearance.setXmlLocator(new XfaXmlLocator(stamper));
        // Creating the signature
        ExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        MakeXmlSignature.signXmlDSig(appearance, pks, chain);
    }

    protected void signWithKeyInfo(String src, String dest, PrivateKey pk, PublicKey publicKey, String digestAlgorithm,
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

    protected void signWithPublicKey(String src, String dest, PrivateKey pk, PublicKey publicKey, String digestAlgorithm,
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

    protected void signPackageWithCertificate(String src, String dest, XfaXpathConstructor.XdpPackage xdpPackage, PrivateKey pk, Certificate[] chain, String digestAlgorithm, String provider) throws GeneralSecurityException, IOException, DocumentException {

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

    protected void signPackageWithKeyInfo(String src, String dest, XfaXpathConstructor.XdpPackage xdpPackage, PrivateKey pk, PublicKey publicKey, String digestAlgorithm, String provider) throws GeneralSecurityException, IOException, DocumentException, TransformException, URIReferenceException {

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

    protected void signPackageWithPublicKey(String src, String dest, XfaXpathConstructor.XdpPackage xdpPackage, PrivateKey pk, PublicKey publicKey, String digestAlgorithm, String provider) throws GeneralSecurityException, IOException, DocumentException {

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
}
