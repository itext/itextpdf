/*
 * Copyright 2008 by Kevin Day.
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
 * the Initial Developer are Copyright (C) 1999-2008 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2008 by Paulo Soares. All Rights Reserved.
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
package com.lowagie.text.pdf.parser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;

/**
 * @since	2.1.4
 */
public class PdfContentReaderTool {

    public PdfContentReaderTool() {
        super();
        // TODO Auto-generated constructor stub
    }

    static public String getDictionaryDetail(PdfDictionary dic){
        return getDictionaryDetail(dic, 0);
    }
    static public String getDictionaryDetail(PdfDictionary dic, int depth){
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        List<PdfName> subDictionaries = new ArrayList<PdfName>();
        for (Object keyo : dic.getKeys()) {
            PdfName key = (PdfName)keyo;
            PdfObject val = dic.getDirectObject(key);
            if (val.isDictionary())
                subDictionaries.add(key);
            builder.append(key);
            builder.append('=');
            builder.append(val);
            builder.append(", ");
        }
        builder.setLength(builder.length()-2);
        builder.append(')');
        for (PdfName pdfSubDictionaryName : subDictionaries) {
            builder.append('\n');
            for(int i = 0; i < depth+1; i++){
                builder.append('\t');
            }
            builder.append("Subdictionary ");
            builder.append(pdfSubDictionaryName);
            builder.append(" = ");
            builder.append(getDictionaryDetail(dic.getAsDict(pdfSubDictionaryName), depth+1));
        }
        return builder.toString();
    }
    
    
    
    static public void listContentStream(File pdfFile) throws IOException {
        int maxPage = -1;    
        
        PdfReader reader = new PdfReader(pdfFile.getCanonicalPath());

            
            // look through the page content of each page and ensure that it does
            // not have a BT command.
            int maxPageNum = reader.getNumberOfPages();
            if (maxPage != -1)
                maxPageNum = maxPage;
            
            for (int pageNum = 1; pageNum <= maxPageNum; pageNum++){
                System.out.println("==============Page " + pageNum + "====================");
                System.out.println("- - - - - Dictionary - - - - - -");
                PdfDictionary pageDictionary = reader.getPageN(pageNum);
                System.out.println(getDictionaryDetail(pageDictionary));
                System.out.println("- - - - - Content Stream - - - - - -");
                RandomAccessFileOrArray f = reader.getSafeFile();
                
                byte[] contentBytes = reader.getPageContent(pageNum, f);
                f.close();

                
                
                InputStream is = new ByteArrayInputStream(contentBytes);
                int ch;
                while ((ch = is.read()) != -1){
                    System.out.print((char)ch);
                }

                System.out.println("- - - - - Text Extraction - - - - - -");
                PdfTextExtractor extractor = new PdfTextExtractor(reader);
                System.out.println(extractor.getTextFromPage(pageNum));
                
                System.out.println();
            }       
            
            
    }

    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1){
            System.out.println("Usage:  PdfContentReaderTool <pdf file path>");
            return;
        }
        listContentStream(new File(args[0]));
        
    }

}
