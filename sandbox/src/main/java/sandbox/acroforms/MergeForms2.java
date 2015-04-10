package sandbox.acroforms;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;

import sandbox.WrapToTest;

@WrapToTest
public class MergeForms2 {

    public static final String SRC = "resources/pdfs/state.pdf";
    public static final String DEST = "results/acroforms/merged_form2.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeForms2().manipulatePdf(SRC, DEST);
    }
    
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfCopy copy = new PdfSmartCopy(document, new FileOutputStream(dest));
        copy.setMergeFields();
        document.open();
        List<PdfReader> readers = new ArrayList<PdfReader>();
        for (int i = 0; i < 3; ) {
            PdfReader reader = new PdfReader(renameFields(src, ++i));
            readers.add(reader);
            copy.addDocument(reader);
        }
        document.close();
        for (PdfReader reader : readers) {
            reader.close();
        }
    }
    
    public byte[] renameFields(String src, int i) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, baos);
        AcroFields form = stamper.getAcroFields();
        Set<String> keys = new HashSet<String>(form.getFields().keySet());
        for (String key : keys) {
            form.renameField(key, String.format("%s_%d", key, i));
        }
        stamper.close();
        reader.close();
        return baos.toByteArray();
    }
}
