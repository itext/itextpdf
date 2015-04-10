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

    public static void main(String[] args) throws IOException {
        PdfReader reader = new PdfReader("resources/pdfs/datasheet.pdf");
        AcroFields fields = reader.getAcroFields();
        // CP_1 is the name of a check box field
        String[] values = fields.getAppearanceStates("CP_1");
        for (String value : values) {
            System.out.println(value);
        }
    }
}
