package com.lowagie.text.pdf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.*;
/**
 * Instance of PdfReader in each output document.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
class PdfReaderInstance {
    static final PRLiteral IDENTITYMATRIX = new PRLiteral(0, "[1 0 0 1 0 0]");
    static final PRNumber ONE = new PRNumber(1);
    PdfObject xrefObj[];
    PRDictionary pages[];
    int myXref[]; 
    PdfReader reader;
    RandomAccessFileOrArray file;
    HashMap importedPages = new HashMap();;
    PdfWriter writer;
    HashMap visited = new HashMap();
    ArrayList nextRound = new ArrayList();

    PdfReaderInstance(PdfReader reader, PdfWriter writer, PdfObject xrefObj[], PRDictionary pages[]) {
        this.reader = reader;
        this.xrefObj = xrefObj;
        this.pages = pages;
        this.writer = writer;
        file = reader.getSafeFile();
        myXref = new int[xrefObj.length];
    }

    PdfReader getReader() {
        return reader;
    }
    
    PdfImportedPage getImportedPage(int pageNumber) {
        if (pageNumber < 1 || pageNumber > pages.length)
            throw new IllegalArgumentException("Invalid page number");
        Integer i = new Integer(pageNumber);
        PdfImportedPage pageT = (PdfImportedPage)importedPages.get(i);
        if (pageT == null) {
            pageT = new PdfImportedPage(this, writer, pageNumber);
            importedPages.put(i, pageT);
        }
        return pageT;
    }
    
    int getNewObjectNumber(int number, int generation) {
        if (myXref[number] == 0) {
            myXref[number] = writer.getIndirectReferenceNumber();
            nextRound.add(new Integer(number));
        }
        return myXref[number];
    }
    
    RandomAccessFileOrArray getReaderFile() {
        return file;
    }
    
    PdfObject getResources(int pageNumber) {
        return reader.getPdfObject(pages[pageNumber - 1].get(PdfReader.NAME_RESOURCES));
    }
    
    PdfStream getFormXObject(int pageNumber) throws IOException {
        PRDictionary page = pages[pageNumber - 1];
        PdfObject contents = reader.getPdfObject(page.get(PdfReader.NAME_CONTENTS));
        int length = 0;
        int offset = 0;
        PRDictionary dic = new PRDictionary();
        ByteArrayOutputStream bout = null;
        ArrayList filters = null;
        if (contents != null) {
            if (contents.type() == PdfObject.STREAM) {
                PRStream stream = (PRStream)contents;
                length = stream.getLength();
                offset = stream.getOffset();
                dic.putAll(stream.getDictionary());
            }
            else {
                PRArray array = (PRArray)contents;
                ArrayList list = array.getArrayList();
                bout = new ByteArrayOutputStream();
                for (int k = 0; k < list.size(); ++k) {
                    PRStream stream = (PRStream)reader.getPdfObject((PdfObject)list.get(k));
                    PdfObject filter = stream.getDictionary().get(new PRName("Filter"));
                    byte b[] = new byte[stream.getLength()];
                    file.seek(stream.getOffset());
                    file.readFully(b);
                    filters = new ArrayList();
                    if (filter.type() == PdfObject.NAME) {
                        filters.add(filter);
                    }
                    else if (filter.type() == PdfObject.ARRAY) {
                        filters = ((PRArray)filter).getArrayList();
                    }
                    String name;
                    for (int j = 0; j < filters.size(); ++j) {
                        name = ((PRName)filters.get(j)).toString();
                        if (name.equals("/FlateDecode") || name.equals("/Fl"))
                            b = PdfReader.FlateDecode(b);
                        else if (name.equals("/ASCIIHexDecode") || name.equals("/AHx"))
                            b = PdfReader.ASCIIHexDecode(b);
                        else if (name.equals("/ASCII85Decode") || name.equals("/A85"))
                            b = PdfReader.ASCII85Decode(b);
                        else
                            throw new IOException("The filter " + name + " is not supported.");
                    }
                    bout.write(b);
                }
            }
        }
        dic.put(PdfReader.NAME_RESOURCES, reader.getPdfObject(page.get(PdfReader.NAME_RESOURCES)));
        dic.put(PdfReader.NAME_TYPE, PdfName.XOBJECT);
        dic.put(PdfReader.NAME_SUBTYPE, PdfName.FORM);
        dic.put(PdfReader.NAME_BBOX, new PdfRectangle(((PdfImportedPage)importedPages.get(new Integer(pageNumber))).getBoundingBox()));
        dic.put(PdfReader.NAME_MATRIX, IDENTITYMATRIX);
        dic.put(PdfReader.NAME_FORMTYPE, ONE);
        PRStream stream;
        if (bout == null) {
            stream = new PRStream(dic, reader, offset);
            stream.setLength(length);
        }
        else
            stream = new PRStream(dic, reader, bout.toByteArray());
        return stream;
    }
    
    void writeAllVisited() throws IOException {
        while (nextRound.size() > 0) {
            ArrayList vec = nextRound;
            nextRound = new ArrayList();
            for (int k = 0; k < vec.size(); ++k) {
                Integer i = (Integer)vec.get(k);
                if (!visited.containsKey(i)) {
                    visited.put(i, null);
                    int n = i.intValue();
                    writer.addToBody(xrefObj[n], myXref[n]);
                }
            }
        }
    }
    
    void writeAllPages() throws IOException {
        file.reOpen();
        for (Iterator it = importedPages.values().iterator(); it.hasNext();) {
            PdfImportedPage ip = (PdfImportedPage)it.next();
            writer.addToBody(ip.getFormXObject(), ip.getIndirectReference());
        }
        writeAllVisited();
        file.close();
    }
}
