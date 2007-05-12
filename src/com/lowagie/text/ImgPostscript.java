/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Paulo Soares.
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

package com.lowagie.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.codec.postscript.MetaDoPS;

/**
 * An <CODE>ImgPostscript</CODE> is the representation of an EPS
 * that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 */

public class ImgPostscript extends Image {

    // Constructors

    ImgPostscript(Image image) {
        super(image);
    }

    public ImgPostscript(byte[] content,float width,float height) throws IOException {
        super( (URL)null);
        rawData = content;
        originalData = content;
        processParameters();
        this.urx=width;
        this.ury=height;
    }
    /**
     * Constructs an <CODE>ImgPostscript</CODE>-object, using an <VAR>url</VAR>.
     *
     * @param url the <CODE>URL</CODE> where the image can be found
     * @throws BadElementException on error
     * @throws IOException on error
     */

    public ImgPostscript(URL url) throws IOException {
        super(url);
        processParameters();
    }

    /**
     * Constructs an <CODE>ImgPostscript</CODE>-object, using a <VAR>filename</VAR>.
     *
     * @param filename a <CODE>String</CODE>-representation of the file that contains the image.
     * @throws BadElementException on error
     * @throws MalformedURLException on error
     * @throws IOException on error
     */

    public ImgPostscript(String filename) throws
    MalformedURLException, IOException {
        this(Utilities.toURL(filename));
    }

    /**
     * Constructs an <CODE>ImgPostscript</CODE>-object from memory.
     *
     * @param img the memory image
     * @throws BadElementException on error
     * @throws IOException on error
     */

    public ImgPostscript(byte[] img) throws IOException {
        super( (URL)null);
        rawData = img;
        originalData = img;
        processParameters();
    }


    /**
     * This method checks if the image is a valid Postscript and processes some parameters.
     * @throws BadElementException
     * @throws IOException
     */

    private void processParameters() throws IOException {
        type = IMGTEMPLATE;
        originalType = ORIGINAL_PS;
        InputStream is = null;
        try {
            if (rawData == null) {
                is = url.openStream();
            }
            else {
                is = new java.io.ByteArrayInputStream(rawData);
            }
            String boundingbox=null;
            //String templatebox=null;
            Reader r = new BufferedReader(new InputStreamReader(is));
            //  StreamTokenizer st = new StreamTokenizer(r);
            while (r.ready()) {
                char c;
                StringBuffer sb = new StringBuffer();
                while ( (c = ( (char) r.read())) != '\n'&&c!='\r') {
                    sb.append(c);
                }
                //System.out.println("<<" + sb.toString() + ">>");
                String s = sb.toString();
                if (s.startsWith("%%BoundingBox:")) {
                    boundingbox = s;

                }
                //if (s.startsWith("%%TemplateBox:")) {
                //    templatebox = s;
                //}
                if (s.startsWith("%%EndComments")) {
                    break;
                }
                if ((!s.startsWith("%%"))&&(!s.startsWith("%!"))) {
                   break;
               }

            }
            if(boundingbox==null){
              scaledHeight=PageSize.A4.height();
            setTop(scaledHeight);
            scaledWidth=PageSize.A4.width();
            setRight(scaledWidth);
              return;
            }
            StringTokenizer st=new StringTokenizer(boundingbox,": \r\n");
            st.nextElement();
            String xx1=st.nextToken();
            String yy1=st.nextToken();
            String xx2=st.nextToken();
            String yy2=st.nextToken();

            int left = Integer.parseInt(xx1);
            int bottom = Integer.parseInt(yy1);
            int right = Integer.parseInt(xx2);
            int top = Integer.parseInt(yy2);
            int inch = 1;
            dpiX = 72;
            dpiY = 72;
            scaledHeight = (float) (top-bottom ) / inch *1f;
            setTop(top);
            scaledWidth = (float) (right - left) / inch * 1f;
            setRight(right);
        }
        finally {
            if (is != null) {
                is.close();
            }
            plainWidth = width();
            plainHeight = height();
        }
    }

    /** Reads the Postscript into a template.
     * @param template the template to read to
     * @throws IOException on error
     * @throws DocumentException on error
     */
    public void readPostscript(PdfTemplate template) throws IOException {
        setTemplateData(template);
        template.setWidth(width());
        template.setHeight(height());
        InputStream is = null;
        try {
            if (rawData == null) {
                is = url.openStream();
            }
            else {
                is = new java.io.ByteArrayInputStream(rawData);
            }
            MetaDoPS meta = new MetaDoPS(is, template);
            meta.readAll();
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
