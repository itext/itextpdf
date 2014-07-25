/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24301578/align-acrofields-in-java
 */
package sandbox.acroforms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class AlignField {

    public static final String SRC = "resources/pdfs/subscribe.pdf";
    public static final String DEST = "results/acroforms/subscribe_realigned.pdf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AlignField().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        AcroFields form = stamper.getAcroFields();
        AcroFields.Item item;
        item = form.getFieldItem("personal.name");
        item.getMerged(0).put(PdfName.Q, new PdfNumber(PdfFormField.Q_LEFT));
        item = form.getFieldItem("personal.loginname");
        item.getMerged(0).put(PdfName.Q, new PdfNumber(PdfFormField.Q_CENTER));
        item = form.getFieldItem("personal.password");
        item.getMerged(0).put(PdfName.Q, new PdfNumber(PdfFormField.Q_RIGHT));
        form.setField("personal.name", "Test");
        form.setField("personal.loginname", "Test");
        form.setField("personal.password", "Test");
        form.setField("personal.reason", "Test");
        stamper.close();
        reader.close();
    }
}