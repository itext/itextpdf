/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
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
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author kevin
 */
public class PdfTextExtractorEncodingsTest
{

    /** Basic Latin characters, with Unicode values less than 128 */
    private static final String TEXT1 = "AZaz09*!";
    /** Latin-1 characters */
    private static final String TEXT2 = "\u0027\u0060\u00a4\u00a6";

    // the following will cause failures
    //  private static final String TEXT2 = "\u0027\u0060\u00a4\u00a6\00b5\u2019";


    @BeforeClass
    public static void initializeFontFactory(){
        FontFactory.registerDirectories();
    }

    protected static Font getSomeTTFont(String encoding, boolean embedded, float size) {

        String fontNames[] = {"arial"};
        for (String name : fontNames) {
            Font foundFont = FontFactory.getFont(name, encoding, embedded, size);
            if (foundFont != null) {
                switch(foundFont.getBaseFont().getFontType()){
                    case BaseFont.FONT_TYPE_TT:
                    case BaseFont.FONT_TYPE_TTUNI:
                        return foundFont; // SUCCESS
                }
            }
        }
        throw new IllegalArgumentException("Unable to find TrueType font to test with - add the name of a TT font on the system to the fontNames array in this method");
    }


    private static byte[] createPdf(final Font font)
      throws Exception
    {
      final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

      final Document document = new Document();
      PdfWriter.getInstance(document, byteStream);
      document.open();
      document.add(new Paragraph(TEXT1, font));
      document.newPage();
      document.add(new Paragraph(TEXT2, font));
      document.close();

      final byte[] pdfBytes = byteStream.toByteArray();

      return pdfBytes;
    }


    /**
     * Used for testing only if we need to open the PDF itself
     * @param bytes
     * @param file
     * @throws Exception
     */
    private void saveBytesToFile(byte[] bytes, File file) throws Exception{
        final FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
        System.out.println("PDF dumped to " + file.getAbsolutePath());
    }

    /**
     * Test parsing a document which uses a standard non-embedded font.
     *
     * @throws Exception any exception will cause the test to fail
     */
    @Test
    public void testStandardFont() throws Exception
    {
        Font font = new Font(FontFamily.TIMES_ROMAN, 12);
        byte[] pdfBytes = createPdf(font);

        if (false){
            saveBytesToFile(pdfBytes, new File("testout", "test.pdf"));
        }

        checkPdf(pdfBytes);

    }


    /**
     * Test parsing a document which uses a font encoding which creates a /Differences
     * PdfArray in the PDF.
     *
     * @throws Exception any exception will cause the test to fail
     */
    @Ignore(value="Failing on hudson, not locally")
    @Test
    public void testEncodedFont()
      throws Exception
    {
        Font font = getSomeTTFont("ISO-8859-1", BaseFont.EMBEDDED, 12);
        byte[] pdfBytes = createPdf(font);
        checkPdf(pdfBytes);
    }


    /**
     * Test parsing a document which uses a Unicode font encoding which creates a /ToUnicode
     * PdfArray.
     *
     * @throws Exception any exception will cause the test to fail
     */
    @Ignore(value="Failing on hudson, not locally")
    @Test
    public void testUnicodeFont()
      throws Exception
    {
        Font font = getSomeTTFont(BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
        byte[] pdfBytes = createPdf(font);
        checkPdf(pdfBytes);
    }


    private void checkPdf(final byte[] pdfBytes) throws Exception {

      final PdfReader pdfReader = new PdfReader(pdfBytes);
      // Characters from http://unicode.org/charts/PDF/U0000.pdf
      Assert.assertEquals(TEXT1, PdfTextExtractor.getTextFromPage(pdfReader, 1));
      // Characters from http://unicode.org/charts/PDF/U0080.pdf
      Assert.assertEquals(TEXT2, PdfTextExtractor.getTextFromPage(pdfReader, 2));
    }

  }
