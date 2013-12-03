/*
 * $Id: PdfStream.java 6076 2013-11-20 13:54:29Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.internal.PdfIsoKeys;

/**
 * <CODE>PdfStream</CODE> is the Pdf stream object.
 * <P>
 * A stream, like a string, is a sequence of characters. However, an application can
 * read a small portion of a stream at a time, while a string must be read in its entirety.
 * For this reason, objects with potentially large amounts of data, such as images and
 * page descriptions, are represented as streams.<BR>
 * A stream consists of a dictionary that describes a sequence of characters, followed by
 * the keyword <B>stream</B>, followed by zero or more lines of characters, followed by
 * the keyword <B>endstream</B>.<BR>
 * All streams must be <CODE>PdfIndirectObject</CODE>s. The stream dictionary must be a direct
 * object. The keyword <B>stream</B> that follows the stream dictionary should be followed by
 * a carriage return and linefeed or just a linefeed.<BR>
 * Remark: In this version only the FLATEDECODE-filter is supported.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.7'
 * section 3.2.7 (page 60-63).<BR>
 *
 * @see		PdfObject
 * @see		PdfDictionary
 */

public class PdfStream extends PdfDictionary {
    
    // membervariables

	/**
	 * A possible compression level.
	 * @since	2.1.3
	 */
	public static final int DEFAULT_COMPRESSION = -1;
	/**
	 * A possible compression level.
	 * @since	2.1.3
	 */
	public static final int NO_COMPRESSION = 0;
	/**
	 * A possible compression level.
	 * @since	2.1.3
	 */
	public static final int BEST_SPEED = 1;
	/**
	 * A possible compression level.
	 * @since	2.1.3
	 */
	public static final int BEST_COMPRESSION = 9;
	
	
/** is the stream compressed? */
    protected boolean compressed = false;
    /**
     * The level of compression.
     * @since	2.1.3
     */
    protected int compressionLevel = NO_COMPRESSION;
    
    protected ByteArrayOutputStream streamBytes = null;
    protected InputStream inputStream;
    protected PdfIndirectReference ref;
    protected int inputStreamLength = -1;
    protected PdfWriter writer;
    protected int rawLength;
        
    static final byte STARTSTREAM[] = DocWriter.getISOBytes("stream\n");
    static final byte ENDSTREAM[] = DocWriter.getISOBytes("\nendstream");
    static final int SIZESTREAM = STARTSTREAM.length + ENDSTREAM.length;

    // constructors
    
/**
 * Constructs a <CODE>PdfStream</CODE>-object.
 *
 * @param		bytes			content of the new <CODE>PdfObject</CODE> as an array of <CODE>byte</CODE>.
 */
 
    public PdfStream(byte[] bytes) {
        super();
        type = STREAM;
        this.bytes = bytes;
        rawLength = bytes.length;
        put(PdfName.LENGTH, new PdfNumber(bytes.length));
    }
  
    /**
     * Creates an efficient stream. No temporary array is ever created. The <CODE>InputStream</CODE>
     * is totally consumed but is not closed. The general usage is:
     * <p>
     * <pre>
     * InputStream in = ...;
     * PdfStream stream = new PdfStream(in, writer);
     * stream.flateCompress();
     * writer.addToBody(stream);
     * stream.writeLength();
     * in.close();
     * </pre>
     * @param inputStream the data to write to this stream
     * @param writer the <CODE>PdfWriter</CODE> for this stream
     */    
    public PdfStream(InputStream inputStream, PdfWriter writer) {
        super();
        type = STREAM;
        this.inputStream = inputStream;
        this.writer = writer;
        ref = writer.getPdfIndirectReference();
        put(PdfName.LENGTH, ref);
    }
  
/**
 * Constructs a <CODE>PdfStream</CODE>-object.
 */
    
    protected PdfStream() {
        super();
        type = STREAM;
    }
    
    /**
     * Writes the stream length to the <CODE>PdfWriter</CODE>.
     * <p>
     * This method must be called and can only be called if the constructor {@link #PdfStream(InputStream,PdfWriter)}
     * is used to create the stream.
     * @throws IOException on error
     * @see #PdfStream(InputStream,PdfWriter)
     */
    public void writeLength() throws IOException {
        if (inputStream == null)
            throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("writelength.can.only.be.called.in.a.contructed.pdfstream.inputstream.pdfwriter"));
        if (inputStreamLength == -1)
            throw new IOException(MessageLocalization.getComposedMessage("writelength.can.only.be.called.after.output.of.the.stream.body"));
        writer.addToBody(new PdfNumber(inputStreamLength), ref, false);
    }
    
    /**
     * Gets the raw length of the stream.
     * @return the raw length of the stream
     */
    public int getRawLength() {
        return rawLength;
    }
    
    /**
     * Compresses the stream.
     */
    public void flateCompress() {
    	flateCompress(DEFAULT_COMPRESSION);
    }
    
    /**
     * Compresses the stream.
	 * @param compressionLevel the compression level (0 = best speed, 9 = best compression, -1 is default)
	 * @since	2.1.3
     */
    public void flateCompress(int compressionLevel) {
        if (!Document.compress)
            return;
        // check if the flateCompress-method has already been
        if (compressed) {
            return;
        }
    	this.compressionLevel = compressionLevel;
        if (inputStream != null) {
            compressed = true;
            return;
        }
        // check if a filter already exists
        PdfObject filter = PdfReader.getPdfObject(get(PdfName.FILTER));
        if (filter != null) {
            if (filter.isName()) {
                if (PdfName.FLATEDECODE.equals(filter))
                    return;
            }
            else if (filter.isArray()) {
                if (((PdfArray) filter).contains(PdfName.FLATEDECODE))
                    return;
            }
            else {
                throw new RuntimeException(MessageLocalization.getComposedMessage("stream.could.not.be.compressed.filter.is.not.a.name.or.array"));
            }
        }
        try {
            // compress
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Deflater deflater = new Deflater(compressionLevel);
            DeflaterOutputStream zip = new DeflaterOutputStream(stream, deflater);
            if (streamBytes != null)
                streamBytes.writeTo(zip);
            else
                zip.write(bytes);
            zip.close();
            deflater.end();
            // update the object
            streamBytes = stream;
            bytes = null;
            put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
            if (filter == null) {
                put(PdfName.FILTER, PdfName.FLATEDECODE);
            }
            else {
                PdfArray filters = new PdfArray(filter);
                filters.add(PdfName.FLATEDECODE);
                put(PdfName.FILTER, filters);
            }
            compressed = true;
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

//    public int getStreamLength(PdfWriter writer) {
//        if (dicBytes == null)
//            toPdf(writer);
//        if (streamBytes != null)
//            return streamBytes.size() + dicBytes.length + SIZESTREAM;
//        else
//            return bytes.length + dicBytes.length + SIZESTREAM;
//    }
    
    protected void superToPdf(PdfWriter writer, OutputStream os) throws IOException {
        super.toPdf(writer, os);
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
        PdfObject nn = get(PdfName.LENGTH);
        if (crypto != null && nn != null && nn.isNumber()) {
            int sz = ((PdfNumber)nn).intValue();
            put(PdfName.LENGTH, new PdfNumber(crypto.calculateStreamSize(sz)));
            superToPdf(writer, os);
            put(PdfName.LENGTH, nn);
        }
        else
            superToPdf(writer, os);
        PdfWriter.checkPdfIsoConformance(writer, PdfIsoKeys.PDFISOKEY_STREAM, this);
        os.write(STARTSTREAM);
        if (inputStream != null) {
            rawLength = 0;
            DeflaterOutputStream def = null;
            OutputStreamCounter osc = new OutputStreamCounter(os);
            OutputStreamEncryption ose = null;
            OutputStream fout = osc;
            if (crypto != null && !crypto.isEmbeddedFilesOnly())
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
            if (crypto != null && !crypto.isEmbeddedFilesOnly()) {
                byte b[];
                if (streamBytes != null) {
                    b = crypto.encryptByteArray(streamBytes.toByteArray());
                }
                else {
                    b = crypto.encryptByteArray(bytes);
                }
                os.write(b);
            }
            else {
                if (streamBytes != null)
                    streamBytes.writeTo(os);
                else
                    os.write(bytes);
            }
        }
        os.write(ENDSTREAM);
    }
    
    /**
     * Writes the data content to an <CODE>OutputStream</CODE>.
     * @param os the destination to write to
     * @throws IOException on error
     */    
    public void writeContent(OutputStream os) throws IOException {
        if (streamBytes != null)
            streamBytes.writeTo(os);
        else if (bytes != null)
            os.write(bytes);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfObject#toString()
     */
    public String toString() {
    	if (get(PdfName.TYPE) == null) return "Stream";
    	return "Stream of type: " + get(PdfName.TYPE);
    }
}
