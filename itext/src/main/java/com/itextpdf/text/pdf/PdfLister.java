/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
 package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
/**
 * List a PDF file in human-readable form (for debugging reasons mostly)
 * @author Mark Thompson
 */

public class PdfLister {

	/** the printStream you want to write the output to. */
    PrintStream out;

    /**
     * Create a new lister object.
     * @param out
     */
    public PdfLister(PrintStream out) {
        this.out = out;
    }

    /**
     * Visualizes a PDF object.
     * @param object	a com.itextpdf.text.pdf object
     */
    public void listAnyObject(PdfObject object)
    {
        switch (object.type()) {
        case PdfObject.ARRAY:
            listArray((PdfArray)object);
            break;
        case PdfObject.DICTIONARY:
            listDict((PdfDictionary) object);
            break;
        case PdfObject.STRING:
            out.println("(" + object.toString() + ")");
            break;
        default:
            out.println(object.toString());
            break;
        }
    }
    /**
     * Visualizes a PdfDictionary object.
     * @param dictionary	a com.itextpdf.text.pdf.PdfDictionary object
     */
    public void listDict(PdfDictionary dictionary)
    {
        out.println("<<");
        PdfObject value;
        for (PdfName key: dictionary.getKeys()) {
            value = dictionary.get(key);
            out.print(key.toString());
            out.print(' ');
            listAnyObject(value);
        }
        out.println(">>");
    }

    /**
     * Visualizes a PdfArray object.
     * @param array	a com.itextpdf.text.pdf.PdfArray object
     */
    public void listArray(PdfArray array)
    {
        out.println('[');
        for (Iterator<PdfObject> i = array.listIterator(); i.hasNext(); ) {
            PdfObject item = i.next();
            listAnyObject(item);
        }
        out.println(']');
    }
    /**
     * Visualizes a Stream.
     * @param stream
     * @param reader
     */
    public void listStream(PRStream stream, PdfReaderInstance reader)
    {
        try {
            listDict(stream);
            out.println("startstream");
            byte[] b = PdfReader.getStreamBytes(stream);
//                  byte buf[] = new byte[Math.min(stream.getLength(), 4096)];
//                  int r = 0;
//                  stream.openStream(reader);
//                  for (;;) {
//                      r = stream.readStream(buf, 0, buf.length);
//                      if (r == 0) break;
//                      out.write(buf, 0, r);
//                  }
//                  stream.closeStream();
            int len = b.length - 1;
            for (int k = 0; k < len; ++k) {
                if (b[k] == '\r' && b[k + 1] != '\n')
                    b[k] = (byte)'\n';
            }
            out.println(new String(b));
            out.println("endstream");
        } catch (IOException e) {
            System.err.println("I/O exception: " + e);
//          } catch (java.util.zip.DataFormatException e) {
//              System.err.println("Data Format Exception: " + e);
        }
    }
    /**
     * Visualizes an imported page
     * @param iPage
     */
    public void listPage(PdfImportedPage iPage)
    {
        int pageNum = iPage.getPageNumber();
        PdfReaderInstance readerInst = iPage.getPdfReaderInstance();
        PdfReader reader = readerInst.getReader();

        PdfDictionary page = reader.getPageN(pageNum);
        listDict(page);
        PdfObject obj = PdfReader.getPdfObject(page.get(PdfName.CONTENTS));
        if (obj == null)
            return;
        switch (obj.type) {
        case PdfObject.STREAM:
            listStream((PRStream)obj, readerInst);
            break;
        case PdfObject.ARRAY:
            for (Iterator<PdfObject> i = ((PdfArray)obj).listIterator(); i.hasNext();) {
                PdfObject o = PdfReader.getPdfObject(i.next());
                listStream((PRStream)o, readerInst);
                out.println("-----------");
            }
            break;
        }
    }
}
