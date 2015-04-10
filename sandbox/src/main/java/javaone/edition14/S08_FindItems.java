/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javaone.edition14.part4.helper.MyItem;
import javaone.edition14.part4.helper.MyRenderListener;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

/**
 * In this first example that parses a PDF to discover its structure,
 * we'll highlight all the different text items and images that are
 * encountered during the parsing process.
 */
public class S08_FindItems {
    /** The source file that is going to be parsed. */
    public static final String SRC = "resources/pdfs/page229.pdf";
    /** The resulting PDF after parsing for structure. */
    public static final String DEST = "results/javaone/edition2014/08_page229_items.pdf";
    
    /**
     * Reads the first page of a document of which the top margin is 48pt heigh
     * and highlights text items and images.
     * @param args No arguments needed
     * @throws IOException
     * @throws DocumentException 
     */
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        S08_FindItems app = new S08_FindItems();
        PdfReader reader = new PdfReader(SRC);
        List<MyItem> items = app.getContentItems(reader, 1, 48);
        app.highlight(items, reader, 1, DEST);
    }

    /**
     * Parses a page of a PDF file resulting in a list of
     * TextItem and ImageItem objects.
     * @param reader        a PdfReader
     * @param page          the page number of the page that needs to be parsed
     * @param header_height the height of the top margin
     * @return  a list of TextItem and ImageItem objects
     * @throws IOException 
     */
    public List<MyItem> getContentItems(PdfReader reader, int page, float header_height) throws IOException {
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        Rectangle pageSize = reader.getPageSize(page);
        MyRenderListener myRenderListener = new MyRenderListener(pageSize.getTop() - header_height);
        parser.processContent(page, myRenderListener);
        return myRenderListener.getItems();
    }

    /**
     * Accepts a list of MyItem objects and draws a colored rectangle for each
     * item in the list.
     * @param items         The list of items
     * @param reader        The reader instance that has access to the PDF file
     * @param pageNum       The page number of the page that needs to be parsed
     * @param destination   The path for the altered PDF file
     * @throws IOException
     * @throws DocumentException 
     */
    public void highlight(List items, PdfReader reader, int pageNum, String destination) throws IOException, DocumentException {
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destination));
        PdfContentByte over = stamper.getOverContent(pageNum);
        for (Object obj : items) {
            MyItem item = (MyItem)obj;
            if (item.getColor() == null)
                continue;
            over.saveState();
            over.setColorStroke(item.getColor());
            over.setLineWidth(2);
            Rectangle r = item.getRectangle();
            over.rectangle(r.getLeft(), r.getBottom(), r.getWidth(), r.getHeight());
            over.stroke();
            over.restoreState();
        }
        stamper.close();
    }
}
