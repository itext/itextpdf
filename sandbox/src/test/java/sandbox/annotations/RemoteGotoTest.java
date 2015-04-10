package sandbox.annotations;

import java.lang.reflect.Method;

import org.junit.Test;

import sandbox.GenericTest;



public class RemoteGotoTest extends GenericTest {

    @Override
	public void setup() {
		setKlass("sandbox.annotations.RemoteGoto");
	}
	
    @Override
    protected String getCmpPdf() {
        return "cmpfiles/annotations/subdir/cmp_abc.pdf";
    }
    protected String getCmpPdf2() {
        return "cmpfiles/annotations/cmp_xyz.pdf";
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

    	Method method = klass.getDeclaredMethod("createPdf2", String.class);
    	method.invoke(klass.getConstructor().newInstance(), src);
        createPdf(dest);
        
        // Do some further tests on the PDF
        assertPdf(dest);
        // Compare the destination PDF with a reference PDF
        comparePdf(src, getCmpPdf2());
        comparePdf(dest, getCmpPdf());
    }
}
