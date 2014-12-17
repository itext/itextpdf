/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27057187/setting-acrofield-text-color-in-itextsharp
 */
package sandbox.acroforms;

import com.itextpdf.text.BaseColor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class RemoveXFA {

    public static final String SRC = "resources/pdfs/reportcardinitial.pdf";
    public static final String DEST = "results/acroforms/reportcard.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RemoveXFA().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        form.removeXfa();
        Map<String, AcroFields.Item> fields = form.getFields();
        for (String name : fields.keySet()) {
            if (name.indexOf("Total") > 0)
                form.setFieldProperty(name, "textcolor", BaseColor.RED, null);
            form.setField(name, "X");
        }
        stamper.close();
        reader.close();
    }
}