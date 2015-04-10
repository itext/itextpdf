/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20401125/overlapping-characters-in-text-field-itext-pdf
 * 
 * Sometimes you need to change the font of a field.
 */
package sandbox.acroforms;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.Assert;

import sandbox.GenericTest;

public class FillFormSpecialCharsTest extends GenericTest {

    @Override
	public void setup() {
		setKlass("sandbox.acroforms.FillFormSpecialChars");
	}

    @Override
    protected void assertPdf(String outPdf) throws Exception {
    	String value = sandbox.acroforms.FillFormSpecialChars.VALUE;
        PdfReader reader = new PdfReader(outPdf);
        AcroFields fields = reader.getAcroFields();
        Assert.assertTrue("Check test value", fields.getField("test").equals(value));
        Assert.assertTrue("Check test2 value", fields.getField("test2").equals(value));
        reader.close();
    }
}
