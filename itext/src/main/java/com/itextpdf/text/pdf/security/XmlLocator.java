package com.itextpdf.text.pdf.security;

import com.itextpdf.text.DocumentException;

import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;

/**
 * Helps to locate xml stream
 */
public interface XmlLocator {

    org.w3c.dom.Document getDocument();

    void setDocument(org.w3c.dom.Document document) throws TransformerConfigurationException, IOException, DocumentException;
}
