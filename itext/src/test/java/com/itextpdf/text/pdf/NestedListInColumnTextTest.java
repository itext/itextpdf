package com.itextpdf.text.pdf;


import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.RomanList;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NestedListInColumnTextTest {
    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/NestedListInColumnTextTest/";
    public static final String SOURCE_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/NestedListInColumnTextTest/";

    @Before
    public void Init() throws IOException {
        File dir = new File(DEST_FOLDER);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        } else
            dir.mkdirs();
    }

    //SUP-879 Nested List items not displaying properly
    @Test
    public void nestedListAtTheEndOfAnotherNestedList() throws DocumentException, IOException, InterruptedException {
        String pdfFile = "nestedListAtTheEndOfAnotherNestedList.pdf";
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(DEST_FOLDER + pdfFile));
        // step 3
        document.open();
        // step 4
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.ORANGE);

        RomanList romanlist = new RomanList(true, 20);
        romanlist.setIndentationLeft(10f);
        romanlist.add("One");
        romanlist.add("Two");
        romanlist.add("Three");

        RomanList romanlist2 = new RomanList(true, 20);
        romanlist2.setIndentationLeft(10f);
        romanlist2.add("One");
        romanlist2.add("Two");
        romanlist2.add("Three");

        romanlist.add(romanlist2);
        //romanlist.add("Four");

        com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.ORDERED, 20f);
        list.setListSymbol("\u2022");
        list.setIndentationLeft(20f);
        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        list.add("Roman List");
        list.add(romanlist);

        list.add("Five");
        list.add("Six");

        cell.addElement(list);
        table.addCell(cell);
        document.add(table);
        // step 5
        document.close();

        CompareTool compareTool = new CompareTool();
        String error = compareTool.compareByContent(DEST_FOLDER + pdfFile, SOURCE_FOLDER + pdfFile, DEST_FOLDER, "diff_");
        if (error != null) {
            Assert.fail(error);
        }
    }

}
