package com.itextpdf.text.pdf;

import com.itextpdf.testutils.TestResourceUtils;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;

public class PdfPageLabelsTest {
    @Test
    public void testGetPageLabels() throws Exception {
        File testFile = TestResourceUtils.getResourceAsTempFile(this, "test-prefix-reset.pdf");
        String[] expectedPageLabels = new String[] {"i", "ii", "iii", "iv", "v", "1", "2", "3", "4", "5", "G1", "G2", "G3", "G4", "G5", "6", "7", "8", "9", "10"};

        PdfReader reader = new PdfReader(testFile.getAbsolutePath());

        String[] pageLabels = PdfPageLabels.getPageLabels(reader);

        Assert.assertNotNull(pageLabels);
        Assert.assertEquals(expectedPageLabels.length, pageLabels.length);

        for (int page = 0; page < pageLabels.length; page++) {
            Assert.assertEquals(expectedPageLabels[page], pageLabels[page]);
        }

        reader.close();
    }
}
