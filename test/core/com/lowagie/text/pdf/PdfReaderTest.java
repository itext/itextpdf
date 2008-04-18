package com.lowagie.text.pdf;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PdfReaderTest {

    @Test
    public void testPartialReadOpenFile() throws Exception {
	/* commit 3265 incorrectly closed the input stream, make sure
	 * the constructor contract is kept, i.e. file is still open
	 */
        RandomAccessFileOrArray f = new RandomAccessFileOrArray("RomeoJuliet.pdf");
        PdfReader r = new PdfReader(f, null);

        assertTrue("kept open", f.isOpen());
    }

}
