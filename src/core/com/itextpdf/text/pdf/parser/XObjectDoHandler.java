/*
 * Created on Jan 21, 2010
 * (c) 2010 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfStream;

/**
 * @author kevin
 */
public interface XObjectDoHandler {
    public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream);
}
