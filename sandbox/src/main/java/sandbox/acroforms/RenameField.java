/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21344750/itextsharp-renamefield-bug
 * 
 * When renaming a field, you need to respect the existing hierarchy.
 */
package sandbox.acroforms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class RenameField {

    public static final String SRC = "resources/pdfs/subscribe.pdf";
    public static final String DEST = "results/acroforms/subscribe_renamed.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RenameField().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.renameField("personal.loginname", "personal.login");
        stamper.close();
        reader.close();
        reader = new PdfReader(dest);
        form = reader.getAcroFields();
        Map<String, AcroFields.Item> fields = form.getFields();
        for (String name : fields.keySet()) {
            System.out.println(name);
        }
    }
}