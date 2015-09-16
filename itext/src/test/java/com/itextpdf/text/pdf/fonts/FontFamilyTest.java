package com.itextpdf.text.pdf.fonts;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Daniel Lichtenberger, CHEMDOX
 */
public class FontFamilyTest {


    private static String srcFolder ="./src/test/resources/com/itextpdf/text/pdf/FontFamilyTest/";

    @Test
    public void testNotoFontFamily() throws IOException, DocumentException {
        String[] fonts = { "NotoSansCJKjp-Bold.otf", "NotoSansCJKjp-Regular.otf"};

        String fontFamily = "Noto Sans CJK JP";

        for (String file : fonts) {
            BaseFont font = BaseFont.createFont(srcFolder + file, BaseFont.CP1252, false);
            String[][] familyFontName = font.getFamilyFontName();
            for (String[] values : familyFontName) {
                Assert.assertEquals(fontFamily, values[3]);
            }
        }
    }
}
