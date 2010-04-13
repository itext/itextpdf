/*
 * Created on Mar 29, 2010
 * (c) 2010 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

/**
 * A utility class that makes it cleaner to process content from pages of a PdfReader
 * through a specified RenderListener.
 * @since 5.0.2
 */
public class PdfReaderContentParser {
    /** the reader this parser will process */
    private final PdfReader reader;
    
    public PdfReaderContentParser(PdfReader reader) {
        this.reader = reader;
    }

    /**
     * Processes content from the specified page number using the specified listener
     * @param <E> the type of the renderListener - this makes it easy to chain calls
     * @param pageNumber the page number to process
     * @param renderListener the listener that will receive render callbacks
     * @return the provided renderListener
     * @throws IOException if operations on the reader fail
     */
    
    public <E extends RenderListener> E processContent(int pageNumber, E renderListener) throws IOException{
        PdfDictionary pageDic = reader.getPageN(pageNumber);
        PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
        
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(renderListener);
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, pageNumber), resourcesDic);        
        return renderListener;

    }
}
