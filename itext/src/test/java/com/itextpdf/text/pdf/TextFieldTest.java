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