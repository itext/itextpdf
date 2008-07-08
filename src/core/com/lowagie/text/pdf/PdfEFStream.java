package com.lowagie.text.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PdfEFStream extends PdfStream {

	public PdfEFStream(InputStream in, PdfWriter writer) {
		super(in, writer);
	}

	public PdfEFStream(byte[] fileStore) {
		super(fileStore);
	}

    /**
     * @see com.lowagie.text.pdf.PdfDictionary#toPdf(com.lowagie.text.pdf.PdfWriter, java.io.OutputStream)
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
                    ArrayList af = ((PdfArray)filter).getArrayList();
                    if (!af.isEmpty() && PdfName.CRYPT.equals(af.get(0)))
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
            if (compressed)    
                fout = def = new DeflaterOutputStream(fout, new Deflater(compressionLevel), 0x8000);
            
            byte buf[] = new byte[4192];
            while (true) {
                int n = inputStream.read(buf);
                if (n <= 0)
                    break;
                fout.write(buf, 0, n);
                rawLength += n;
            }
            if (def != null)
                def.finish();
            if (ose != null)
                ose.finish();
            inputStreamLength = osc.getCounter();
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
