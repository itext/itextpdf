/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextFieldTest {

    private static String CMP_FOLDER ="./src/test/resources/com/itextpdf/text/pdf/TextFieldTest/";
    private static String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/TextFieldTest/";


    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void testVisibleTopChoice() throws IOException, DocumentException, InterruptedException {
        int[] testValues        = new int[] {-3, 0, 2,  3};
        int[] expectedValues    = new int[] {-1, 0, 2, -1};

        for ( int i = 0; i < testValues.length; i++ ) {
            visibleTopChoiceHelper(testValues[i], expectedValues[i], "textfield-top-visible-"+i+".pdf");
        }
    }


    private void visibleTopChoiceHelper(int topVisible, int expected, String file) throws DocumentException, IOException, InterruptedException {
        Document document = new Document();
        FileOutputStream fos = new FileOutputStream(OUTPUT_FOLDER + file);
        PdfWriter writer = PdfWriter.getInstance(document, fos);
        document.open();

        TextField textField = new TextField(writer, new Rectangle(380,560,500,610), "testListBox");

        textField.setVisibility(BaseField.VISIBLE);
        textField.setRotation(0);

        textField.setFontSize(14f);
        textField.setTextColor(BaseColor.MAGENTA);

        textField.setBorderColor(BaseColor.BLACK);
        textField.setBorderStyle(PdfBorderDictionary.STYLE_SOLID);

        textField.setFont(BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.EMBEDDED));
        textField.setBorderWidth(BaseField.BORDER_WIDTH_THIN);

        writer.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);

        textField.setChoices( new String[] { "one", "two", "three" } );
        textField.setChoiceExports(new String[] { "1", "2", "3" });

        //choose the second item
        textField.setChoiceSelection(1);
        //set the first item as the visible one
        textField.setVisibleTopChoice(topVisible);

        Assert.assertEquals(expected, textField.getVisibleTopChoice());

        PdfFormField field = textField.getListField();

        writer.addAnnotation(field);

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}