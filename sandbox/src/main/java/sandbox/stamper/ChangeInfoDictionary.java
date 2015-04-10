/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21607286/unicode-characters-in-document-info-dictionary-keys
 * 
 * A user wants to update a Document Info Dictionary (DID)
 * introducing a custom key with a Unicode character.
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import sandbox.WrapToTest;

@WrapToTest
public class ChangeInfoDictionary {

    public static final String SRC = "resources/pdfs/hello.pdf";
    public static final String DEST = "results/stamper/unicode_in_did.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeInfoDictionary().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        Map<String, String> info = reader.getInfo();
        info.put("Special Character: \u00e4", "\u00e4");
        StringBuffer buf = new StringBuffer();
        buf.append((char) 0xc3);
        buf.append((char) 0xa4);
        info.put(buf.toString(), "\u00e4");
        stamper.setMoreInfo(info);
        stamper.close();
        reader.close();
    }
}
