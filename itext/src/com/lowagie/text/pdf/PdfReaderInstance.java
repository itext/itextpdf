/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Paulo Soares
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

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
    static final PdfLiteral IDENTITYMATRIX = new PdfLiteral("[1 0 0 1 0 0]");
    static final PdfNumber ONE = new PdfNumber(1);
    PdfObject xrefObj[];
    PdfDictionary pages[];
    int myXref[]; 
    PdfReader reader;
    RandomAccessFileOrArray file;
    HashMap importedPages = new HashMap();
    PdfWriter writer;
    HashMap visited = new HashMap();
    ArrayList nextRound = new ArrayList();

    PdfReaderInstance(PdfReader reader, PdfWriter writer, PdfObject xrefObj[], PdfDictionary pages[]) {
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
        return reader.getPdfObject(pages[pageNumber - 1].get(PdfName.RESOURCES));
    }
    
    PdfStream getFormXObject(int pageNumber) throws IOException {
        PdfDictionary page = pages[pageNumber - 1];
        PdfObject contents = reader.getPdfObject(page.get(PdfName.CONTENTS));
        int length = 0;
        int offset = 0;
        PdfDictionary dic = new PdfDictionary();
        ByteArrayOutputStream bout = null;
        ArrayList filters = null;
        if (contents != null) {
            if (contents.type() == PdfObject.STREAM) {
                PRStream stream = (PRStream)contents;
                length = stream.getLength();
                offset = stream.getOffset();
                dic.putAll(stream);
            }
            else {
                PdfArray array = (PdfArray)contents;
                ArrayList list = array.getArrayList();
                bout = new ByteArrayOutputStream();
                for (int k = 0; k < list.size(); ++k) {
                    PRStream stream = (PRStream)reader.getPdfObject((PdfObject)list.get(k));
                    PdfObject filter = stream.get(PdfName.FILTER);
                    byte b[] = new byte[stream.getLength()];
                    file.seek(stream.getOffset());
                    file.readFully(b);
                    filters = new ArrayList();
                    if (filter != null) {
                        if (filter.type() == PdfObject.NAME) {
                            filters.add(filter);
                        }
                        else if (filter.type() == PdfObject.ARRAY) {
                            filters = ((PdfArray)filter).getArrayList();
                        }
                    }
                    String name;
                    for (int j = 0; j < filters.size(); ++j) {
                        name = ((PdfName)filters.get(j)).toString();
                        if (name.equals("/FlateDecode") || name.equals("/Fl"))
                            b = PdfReader.FlateDecode(b);
                        else if (name.equals("/ASCIIHexDecode") || name.equals("/AHx"))
                            b = PdfReader.ASCIIHexDecode(b);
                        else if (name.equals("/ASCII85Decode") || name.equals("/A85"))
                            b = PdfReader.ASCII85Decode(b);
                        else if (name.equals("/LZWDecode"))
                            b = PdfReader.LZWDecode(b);
                        else
                            throw new IOException("The filter " + name + " is not supported.");
                    }
                    bout.write(b);
                    if (k != list.size() - 1)
                       bout.write('\n');
                }
            }
        }
        dic.put(PdfName.RESOURCES, reader.getPdfObject(page.get(PdfName.RESOURCES)));
        dic.put(PdfName.TYPE, PdfName.XOBJECT);
        dic.put(PdfName.SUBTYPE, PdfName.FORM);
        dic.put(PdfName.BBOX, new PdfRectangle(((PdfImportedPage)importedPages.get(new Integer(pageNumber))).getBoundingBox()));
        dic.put(PdfName.MATRIX, IDENTITYMATRIX);
        dic.put(PdfName.FORMTYPE, ONE);
        PRStream stream;
        if (bout == null) {
            stream = new PRStream(reader, offset);
            stream.putAll(dic);
            stream.setLength(length);
        }
        else {
            stream = new PRStream(reader, bout.toByteArray());
            stream.putAll(dic);
        }
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
        try {
            file.reOpen();
            for (Iterator it = importedPages.values().iterator(); it.hasNext();) {
                PdfImportedPage ip = (PdfImportedPage)it.next();
                writer.addToBody(ip.getFormXObject(), ip.getIndirectReference());
            }
            writeAllVisited();
        }
        finally {
            try {
                file.close();
            }
            catch (Exception e) {
                //Empty on purpose
            }
        }
    }
}
