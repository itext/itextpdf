/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14;

import java.io.IOException;

import javaone.edition14.part2.ContentStreams;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

/**
 *
 * @author Bruno Lowagie (iText Software)
 */
public class S06_ExtractText {
        public static void main(String[] args) throws DocumentException, IOException {
        ContentStreams.main(args);
        S06_ExtractText app = new S06_ExtractText();
        app.extractSnippets(ContentStreams.RESULT_HIGH);
        app.extractSnippets(ContentStreams.RESULT_CHUNKS);
        app.extractSnippets(ContentStreams.RESULT_ABSOLUTE);
    }
    
    public void extractSnippets(String src) throws IOException {
        PdfReader reader = new PdfReader(src);
        System.out.println(PdfTextExtractor.getTextFromPage(reader, 1));
    }
}
