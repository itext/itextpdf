/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20401125/overlapping-characters-in-text-field-itext-pdf
 * 
 * Sometimes you need to change the font of a field.
 */
package sandbox.acroforms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class FillFormSpecialChars {
    
    public static final String SRC = "resources/pdfs/test.pdf";
    public static final String DEST = "results/acroforms/test.pdf";
    public static final String VALUE = "\u011b\u0161\u010d\u0159\u017e\u00fd\u00e1\u00ed\u00e9";
    public static final String FONT = "resources/fonts/FreeSans.ttf";

    public static void main(String[] args) throws DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillFormSpecialChars().manipulatePdf(SRC, DEST);
    }
    
    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream(dest));
        AcroFields fields = stamper.getAcroFields();
        fields.setGenerateAppearances(true);
        /**This method is used instead 'BaseFont createFont(String name, String encoding, boolean embedded)'
           in order to avoid the font cashing. The cashed font could be mistakenly used in another tests.
           This could cause a test failure on some platforms.
        **/
        BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, false, null, null, false);
        fields.setFieldProperty("test", "textfont", bf, null);
        fields.setField("test", VALUE);
        fields.setFieldProperty("test2", "textfont", bf, null);
        fields.setField("test2", VALUE);
        stamper.close();
    }
}
