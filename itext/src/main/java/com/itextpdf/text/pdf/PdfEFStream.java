/*
 * $Id: PdfEFStream.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 * Extends PdfStream and should be used to create Streams for Embedded Files
 * (file attachments).
 * @since	2.1.3
 */

public class PdfEFStream extends PdfStream {

	/**
	 * Creates a Stream object using an InputStream and a PdfWriter object
	 * @param	in	the InputStream that will be read to get the Stream object
	 * @param	writer	the writer to which the stream will be added
	 */
	public PdfEFStream(InputStream in, PdfWriter writer) {
		super(in, writer);
	}

	/**
	 * Creates a Stream object using a byte array
	 * @param	fileStore	the bytes for the stream
	 */
	public PdfEFStream(byte[] fileStore) {
		super(fileStore);
	}

    /**
     * @see com.itextpdf.text.pdf.PdfDictionary#toPdf(com.itextpdf.text.pdf.PdfWriter, java.io.OutputStream)
     */
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        if (inputStream != null && compressed)
            put(PdfName.FILTER, PdfName.FLATEDECODE);
        PdfEncryption crypto = null;
        if (writer != null)
            crypto = writer.getEncryption();
        if (crypto != null) {
            PdfObject filter = get(PdfName.FILTER);
            if (filter != null) {
                if (PdfName.CRYPT.equals(filter))
                    crypto = null;
                else if (filter.isArray()) {
                    PdfArray a = (PdfArray)filter;
                    if (!a.isEmpty() && PdfName.CRYPT.equals(a.getPdfObject(0)))
                        crypto = null;
                }
            }
        }
    	if (crypto != null && crypto.isEmbeddedFilesOnly()) {
    		PdfArray filter = new PdfArray();
    		PdfArray decodeparms = new PdfArray();
    		PdfDictionary crypt = new PdfDictionary();
    		crypt.put(PdfName.NAME, PdfName.STDCF);
    		filter.add(PdfName.CRYPT);
    		decodeparms.add(crypt);
    		if (compressed) {
    			filter.add(PdfName.FLATEDECODE);
    			decodeparms.add(new PdfNull());
    		}
    		put(PdfName.FILTER, filter);
    		put(PdfName.DECODEPARMS, decodeparms);
    	}
        PdfObject nn = get(PdfName.LENGTH);
        if (crypto != null && nn != null && nn.isNumber()) {
            int sz = ((PdfNumber)nn).intValue();
            put(PdfName.LENGTH, new PdfNumber(crypto.calculateStreamSize(sz)));
            superToPdf(writer, os);
            put(PdfName.LENGTH, nn);
        }
        else
            superToPdf(writer, os);

        os.write(STARTSTREAM);
        if (inputStream != null) {
            rawLength = 0;
            DeflaterOutputStream def = null;
            OutputStreamCounter osc = new OutputStreamCounter(os);
            OutputStreamEncryption ose = null;
            OutputStream fout = osc;
            if (crypto != null)
                fout = ose = crypto.getEncryptionStream(fout);
            Deflater deflater = null;
            if (compressed) {
                deflater = new Deflater(compressionLevel);
                fout = def = new DeflaterOutputStream(fout, deflater, 0x8000);
            }
            
            byte buf[] = new byte[4192];
            while (true) {
                int n = inputStream.read(buf);
                if (n <= 0)
                    break;
                fout.write(buf, 0, n);
                rawLength += n;
            }
            if (def != null) {
                def.finish();
                deflater.end();
            }
            if (ose != null)
                ose.finish();
            inputStreamLength = (int)osc.getCounter();
        }
        else {
            if (crypto == null) {
                if (streamBytes != null)
                    streamBytes.writeTo(os);
                else
                    os.write(bytes);
            }
            else {
                byte b[];
                if (streamBytes != null) {
                    b = crypto.encryptByteArray(streamBytes.toByteArray());
                }
                else {
                    b = crypto.encryptByteArray(bytes);
                }
                os.write(b);
            }
        }
        os.write(ENDSTREAM);
    }
}
