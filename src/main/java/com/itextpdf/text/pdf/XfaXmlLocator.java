package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.security.XmlLocator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class XfaXmlLocator implements XmlLocator {

    public XfaXmlLocator(PdfStamper stamper) throws DocumentException, IOException {
        this.stamper = stamper;
        try {
            createXfaForm();
        } catch (ParserConfigurationException e) {
            throw new DocumentException(e);
        } catch (SAXException e) {
            throw new DocumentException(e);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private PdfStamper stamper;
    private XfaForm xfaForm;

    protected void createXfaForm() throws ParserConfigurationException, SAXException, IOException {
        xfaForm = new XfaForm(stamper.getReader());
    }

    public Document getDocument() {
        return xfaForm.getDomDocument();
    }

    public void setDocument(Document document) throws IOException, DocumentException {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TransformerFactory tf = TransformerFactory.newInstance();

            Transformer trans = tf.newTransformer();

            //Convert Document to byte[] to save to PDF
            trans.transform(new DOMSource(document), new StreamResult(outputStream));
            //Create PdfStream
            PdfIndirectReference iref = stamper.getWriter().
                    addToBody(new PdfStream(outputStream.toByteArray())).getIndirectReference();
            stamper.getReader().getAcroForm().put(PdfName.XFA, iref);
        } catch (TransformerConfigurationException e) {
            throw new DocumentException(e);
        } catch (TransformerException e) {
            throw new DocumentException(e);
        }
    }
}
