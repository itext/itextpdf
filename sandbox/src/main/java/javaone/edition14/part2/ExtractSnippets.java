/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part2;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.RenderListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Extracts snippets of text from different Hello World examples
 */
public class ExtractSnippets {
    public static final String RESULT_HIGH = "results/javaone/edition2014/part2/hello-highlevel.txt";
    public static final String RESULT_LOW = "results/javaone/edition2014/part2/hello-lowlevel.txt";
    public static final String RESULT_CHUNKS = "results/javaone/edition2014/part2/hello-chunks.txt";
    public static final String RESULT_ABSOLUTE = "results/javaone/edition2014/part2/hello-absolute.txt";
    
    public static void main(String[] args) throws DocumentException, IOException {
        ContentStreams.main(args);
        ExtractSnippets app = new ExtractSnippets();
        app.extractSnippets(ContentStreams.RESULT_HIGH, RESULT_HIGH);
        app.extractSnippets(ContentStreams.RESULT_CHUNKS, RESULT_CHUNKS);
        app.extractSnippets(ContentStreams.RESULT_ABSOLUTE, RESULT_ABSOLUTE);
    }
    
    public void extractSnippets(String src, String dest) throws IOException {
        PrintWriter out = new PrintWriter(new FileOutputStream(dest));
        PdfReader reader = new PdfReader(src);
        RenderListener listener = new MyTextRenderListener(out);
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);
        PdfDictionary pageDic = reader.getPageN(1);
        PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, 1), resourcesDic);
        out.flush();
        out.close();
        reader.close();
    }
}
