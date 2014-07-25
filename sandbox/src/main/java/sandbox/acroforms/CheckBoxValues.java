/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19698771/checking-off-pdf-checkbox-with-itextsharp
 * 
 * Given a check box in a form, how do we know which values to use in setField?
 */
package sandbox.acroforms;

import java.io.IOException;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;

public class CheckBoxValues {

    public static final String SRC = "../sandbox/resources/pdfs/datasheet.pdf";
    public static final String FIELD = "CP_1";
    
    public static void main(String[] args) throws IOException {
        CheckBoxValues app = new CheckBoxValues();
        System.out.println(app.getCheckboxValue(SRC, FIELD));
    }
    
    public String getCheckboxValue(String src, String name) throws IOException {
        PdfReader reader = new PdfReader(SRC);
        AcroFields fields = reader.getAcroFields();
        // CP_1 is the name of a check box field
        String[] values = fields.getAppearanceStates("CP_1");
        StringBuffer sb = new StringBuffer();
        for (String value : values) {
            sb.append(value);
            sb.append('\n');
        }
        return sb.toString();
    }
}
