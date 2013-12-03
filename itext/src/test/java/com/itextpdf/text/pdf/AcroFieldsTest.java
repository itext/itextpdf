/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.testutils.TestResourceUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class AcroFieldsTest {
    
    @Before
    public void setUp() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @After
    public void tearDown() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

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

    private final String PDF_COMBO = "./src/test/resources/com/itextpdf/text/pdf/AcroFieldsTest/choice_field_order.pdf";
    private final String PDF_COMBO_EXPORT = "./src/test/resources/com/itextpdf/text/pdf/AcroFieldsTest/choice_field_order_export.pdf";
    
    private final String PDF_COMBO_FIELD_NAME = "choice_field";
    
    private final String[] PDF_COMBO_VALUES = {
            "Option 1",
            "Option 2",
            "Option 3"
    };
    
    private final String[] PDF_COMBO_EXPORT_VALUES = {
            "Export 1",
            "Export 2",
            "Export 3"
    };

    @Test
     public void testComboboxAppearanceStateOrder() {
        try {
            checkOrderOfAppearanceStates(PDF_COMBO, PDF_COMBO_FIELD_NAME, PDF_COMBO_VALUES);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testComboboxDisplayValues() {
        try {
            PdfReader reader = new PdfReader(PDF_COMBO);
            AcroFields acroFields = reader.getAcroFields();
            String[] actual = acroFields.getListOptionDisplay(PDF_COMBO_FIELD_NAME);

            Assert.assertEquals(PDF_COMBO_VALUES.length, actual.length);

            for ( int i = 0; i < PDF_COMBO_VALUES.length; i++ ) {
                Assert.assertEquals(PDF_COMBO_VALUES[i], actual[i]);
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testComboboxExportValues() {
        try {
            PdfReader reader = new PdfReader(PDF_COMBO_EXPORT);
            AcroFields acroFields = reader.getAcroFields();
            String[] actual = acroFields.getListOptionExport(PDF_COMBO_FIELD_NAME);

            Assert.assertEquals(PDF_COMBO_EXPORT_VALUES.length, actual.length);

            for ( int i = 0; i < PDF_COMBO_EXPORT_VALUES.length; i++ ) {
                Assert.assertEquals(PDF_COMBO_EXPORT_VALUES[i], actual[i]);
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    private void checkOrderOfAppearanceStates(String pdf, String fieldName, String[] expected) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        AcroFields acroFields = reader.getAcroFields();
        String[] actual = acroFields.getAppearanceStates(fieldName);

        Assert.assertEquals(expected.length, actual.length);

        for ( int i = 0; i < expected.length; i++ ) {
            Assert.assertEquals(expected[i], actual[i]);
        }
    }
}
