/**
 * Example written by Bruno Lowagie in answer to:
 * http://thread.gmane.org/gmane.comp.java.lib.itext.general/65892
 * 
 * Some text displayed using a Small Caps font.
 */
package sandbox.fonts;

import org.junit.Test;

import sandbox.GenericTest;



public class UnembedFontTest extends GenericTest {

    @Override
	public void setup() {
		setKlass("sandbox.fonts.UnembedFont");
	}
	
    @Override
    protected String getCmpPdf() {
        return "cmpfiles/fonts/cmp_withoutTimesNewRoman.pdf";
    }
    protected String getCmpPdf2() {
        return "cmpfiles/fonts/cmp_withTimesNewRoman.pdf";
    }


	/**
	 * Tests the example.
	 * If SRC and DEST are defined, the example manipulates a PDF;
	 * if only DEST is defined, the example creates a PDF.
	 */
    @Test(timeout = 60000)
    public void test() throws Exception {
        // Getting the destination PDF file (must be there!)
        String dest= getDest();
        // Getting the source PDF file
        String src = getSrc();
        createPdf(src);
        manipulatePdf(src, dest);
        // Do some further tests on the PDF
        assertPdf(dest);
        // Compare the destination PDF with a reference PDF
        comparePdf(src, getCmpPdf2());
        comparePdf(dest, getCmpPdf());
    }
}
