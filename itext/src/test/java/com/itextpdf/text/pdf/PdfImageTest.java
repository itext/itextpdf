package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfImageTest {
    private static final String target = "./target/com/itextpdf/test/pdf/PdfImageTest/";
    private static final String source = "./src/test/resources/com/itextpdf/text/pdf/PdfImageTest/";

    @BeforeClass
    public static void setUp() {
        new File(target).mkdirs();
    }

    @Test
    public void pngColorProfileTest() throws DocumentException, InterruptedException, IOException {
        simpleImageTest("pngColorProfileImage.pdf", "test_icc.png");
    }

    @Test
    public void pngColorProfilePalletTest() throws DocumentException, InterruptedException, IOException {
        simpleImageTest("pngColorProfilePalletImage.pdf", "test_icc_pallet.png");
    }

    @Test
    public void pngIncorrectColorProfileTest() throws DocumentException, InterruptedException, IOException {
        simpleImageTest("pngIncorrectProfileImage.pdf", "test_incorrect_icc.png");
    }

    private void simpleImageTest(String fileName, String imageName) throws IOException, DocumentException, InterruptedException {
        String outPath = target + fileName;
        String cmpPath = source + "cmp_" + fileName;
        String imgPath = source + imageName;
        String diff = "diff_" + fileName + "_";

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));

        document.open();
        Image image = Image.getInstance(imgPath);
        image.scaleToFit(new Rectangle(document.left(), document.bottom(), document.right(), document.top()));
        document.add(image);
        document.close();
        writer.close();

        Assert.assertNull(new CompareTool().compareByContent(outPath, cmpPath, target, diff));
    }
}
