package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;

public class AcroFieldsTest {
    
    @Test
    public void testSetFields() throws Exception {
        singleTest("register.xfdf");
    }

    @Test
    public void testListInSetFields() throws Exception {
        singleTest("list_register.xfdf");
    }
    
    private void singleTest(String xfdfResourceName) throws Exception {
        // merging the FDF file
        PdfReader pdfreader = TestResourceUtils.getResourceAsPdfReader(this, "SimpleRegistrationForm.pdf");
        PdfStamper stamp = new PdfStamper(pdfreader, new ByteArrayOutputStream());
        File xfdfFile = TestResourceUtils.getResourceAsTempFile(this, xfdfResourceName);
        XfdfReader fdfreader = new XfdfReader(xfdfFile.getAbsolutePath());
        AcroFields form = stamp.getAcroFields();
        form.setFields(fdfreader);
        stamp.close();
    }
}
