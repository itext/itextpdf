package com.itextpdf.text.pdf.security;

import com.itextpdf.text.DocumentException;

import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;

/**
 * Help to locate xml stream inside Pdf document
 */
public interface XmlLocator {

    org.w3c.dom.Document getDocument();

    void setDocument(org.w3c.dom.Document document) throws TransformerConfigurationException, IOException, DocumentException;
}
