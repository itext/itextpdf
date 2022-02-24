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
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import com.itextpdf.text.log.Counter;
import com.itextpdf.text.log.CounterFactory;
/** Reads an FDF form and makes the fields available
 * @author Paulo Soares
 */
public class FdfReader extends PdfReader {

    HashMap<String, PdfDictionary> fields;
    String fileSpec;
    PdfName encoding;

    /** Reads an FDF form.
     * @param filename the file name of the form
     * @throws IOException on error
     */
    public FdfReader(String filename) throws IOException {
        super(filename);
    }

    /** Reads an FDF form.
     * @param pdfIn the byte array with the form
     * @throws IOException on error
     */
    public FdfReader(byte pdfIn[]) throws IOException {
        super(pdfIn);
    }

    /** Reads an FDF form.
     * @param url the URL of the document
     * @throws IOException on error
     */
    public FdfReader(URL url) throws IOException {
        super(url);
    }

    /** Reads an FDF form.
     * @param is the <CODE>InputStream</CODE> containing the document. The stream is read to the
     * end but is not closed
     * @throws IOException on error
     */
    public FdfReader(InputStream is) throws IOException {
        super(is);
    }

	protected static Counter COUNTER = CounterFactory.getCounter(FdfReader.class);
	protected Counter getCounter() {
		return COUNTER;
	}
	
    @Override
    protected void readPdf() throws IOException {
        fields = new HashMap<String, PdfDictionary>();
        tokens.checkFdfHeader();
        rebuildXref();
        readDocObj();
        readFields();
    }

    protected void kidNode(PdfDictionary merged, String name) {
        PdfArray kids = merged.getAsArray(PdfName.KIDS);
        if (kids == null || kids.isEmpty()) {
            if (name.length() > 0)
                name = name.substring(1);
            fields.put(name, merged);
        }
        else {
            merged.remove(PdfName.KIDS);
            for (int k = 0; k < kids.size(); ++k) {
                PdfDictionary dic = new PdfDictionary();
                dic.merge(merged);
                PdfDictionary newDic = kids.getAsDict(k);
                PdfString t = newDic.getAsString(PdfName.T);
                String newName = name;
                if (t != null)
                    newName += "." + t.toUnicodeString();
                dic.merge(newDic);
                dic.remove(PdfName.T);
                kidNode(dic, newName);
            }
        }
    }

    protected void readFields() {
        catalog = trailer.getAsDict(PdfName.ROOT);
        PdfDictionary fdf = catalog.getAsDict(PdfName.FDF);
        if (fdf == null)
            return;
        PdfString fs = fdf.getAsString(PdfName.F);
        if (fs != null)
            fileSpec = fs.toUnicodeString();
        PdfArray fld = fdf.getAsArray(PdfName.FIELDS);
        if (fld == null)
            return;
        encoding = fdf.getAsName(PdfName.ENCODING);
        PdfDictionary merged = new PdfDictionary();
        merged.put(PdfName.KIDS, fld);
        kidNode(merged, "");
    }

    /** Gets all the fields. The map is keyed by the fully qualified
     * field name and the value is a merged <CODE>PdfDictionary</CODE>
     * with the field content.
     * @return all the fields
     */
    public HashMap<String, PdfDictionary> getFields() {
        return fields;
    }

    /** Gets the field dictionary.
     * @param name the fully qualified field name
     * @return the field dictionary
     */
    public PdfDictionary getField(String name) {
        return fields.get(name);
    }

    /**
     * Gets a byte[] containing a file that is embedded in the FDF.
     * @param name the fully qualified field name
     * @return the bytes of the file
     * @throws IOException
     * @since 5.0.1
     */
    public byte[] getAttachedFile(String name) throws IOException {
    	PdfDictionary field = fields.get(name);
    	if (field != null) {
    		PdfIndirectReference ir = (PRIndirectReference)field.get(PdfName.V);
    		PdfDictionary filespec = (PdfDictionary)getPdfObject(ir.getNumber());
    		PdfDictionary ef = filespec.getAsDict(PdfName.EF);
    		ir = (PRIndirectReference)ef.get(PdfName.F);
    		PRStream stream = (PRStream)getPdfObject(ir.getNumber());
    		return getStreamBytes(stream);
    	}
		return new byte[0];
    }

    /**
     * Gets the field value or <CODE>null</CODE> if the field does not
     * exist or has no value defined.
     * @param name the fully qualified field name
     * @return the field value or <CODE>null</CODE>
     */
    public String getFieldValue(String name) {
        PdfDictionary field = fields.get(name);
        if (field == null)
            return null;
        PdfObject v = getPdfObject(field.get(PdfName.V));
        if (v == null)
            return null;
        if (v.isName())
            return PdfName.decodeName(((PdfName)v).toString());
        else if (v.isString()) {
            PdfString vs = (PdfString)v;
            if (encoding == null || vs.getEncoding() != null)
                return vs.toUnicodeString();
            byte b[] = vs.getBytes();
            if (b.length >= 2 && b[0] == (byte)254 && b[1] == (byte)255)
                return vs.toUnicodeString();
            try {
                if (encoding.equals(PdfName.SHIFT_JIS))
                    return new String(b, "SJIS");
                else if (encoding.equals(PdfName.UHC))
                    return new String(b, "MS949");
                else if (encoding.equals(PdfName.GBK))
                    return new String(b, "GBK");
                else if (encoding.equals(PdfName.BIGFIVE))
                    return new String(b, "Big5");
                else if (encoding.equals(PdfName.UTF_8))
                    return new String(b, "UTF8");
            }
            catch (Exception e) {
            }
            return vs.toUnicodeString();
        }
        return null;
    }

    /** Gets the PDF file specification contained in the FDF.
     * @return the PDF file specification contained in the FDF
     */
    public String getFileSpec() {
        return fileSpec;
    }
}
