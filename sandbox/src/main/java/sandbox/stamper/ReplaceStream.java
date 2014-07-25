/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21617218/itext-or-itextsharp-rudimentary-text-edit
 * 
 * This is only a partial answer. It's a quick and dirty method showing how to
 * change a stream inside a PDF. Obviously, you'll have to detect words that are
 * stored in Form XObjects too, and you can seriously screw up the layout when
 * you manipulate the content stream as is done in this example.
 */
package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import sandbox.WrapToTest;

@WrapToTest
public class ReplaceStream {
    public static final String SRC = "resources/pdfs/hello.pdf";
    public static final String DEST = "results/stamper/hello_changed.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ReplaceStream().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfDictionary dict = reader.getPageN(1);
        PdfObject object = dict.getDirectObject(PdfName.CONTENTS);
        if (object instanceof PRStream) {
            PRStream stream = (PRStream)object;
            byte[] data = PdfReader.getStreamBytes(stream);
            stream.setData(new String(data).replace("Hello World", "HELLO WORLD").getBytes());
        }
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
        reader.close();
    }
}
