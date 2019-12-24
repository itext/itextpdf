/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @deprecated For internal use only. If you want to use iText, please use a dependency on iText 7.
 */
@Deprecated
public class PdfReaderTest {

    @Before
    public void setUp() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @After
    public void tearDown() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @Ignore("validity of test needs to be resolved")
    @Test
    public void testGetLink() throws Exception {
        File testFile = TestResourceUtils.getResourceAsTempFile(this, "getLinkTest1.pdf");
        PdfReader currentReader = new PdfReader(testFile.getAbsolutePath());
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        PdfWriter writer = PdfWriter.getInstance(document, new
                ByteArrayOutputStream());
        document.open();
        document.newPage();
        List<PdfAnnotation.PdfImportedLink> links = currentReader.getLinks(1);
        PdfAnnotation.PdfImportedLink link = links.get(0);
        writer.addAnnotation(link.createAnnotation(writer));
        document.close();
        
        currentReader.close();
    }

    @Test
    public void testPRTokenizer() throws IOException {
        String obj = "13 0 obj\n" +
                "<< /Type /StructElem /Pg 111117220777773888836 0 R>>\n" +
                "endobj";
        PRTokeniser tokens= new PRTokeniser(new RandomAccessFileOrArray(obj.getBytes()));
        for (int i = 0; i < 11; i++) {
            tokens.nextValidToken();
            if (tokens.getTokenType() == PRTokeniser.TokenType.REF)
                assertTrue(tokens.getReference() < 0);
            if (tokens.getTokenType() == PRTokeniser.TokenType.ENDOFFILE)
                break;
        }
    }

    @Test
    public void testGetLink2() throws Exception {
        File testFile = TestResourceUtils.getResourceAsTempFile(this, "getLinkTest2.pdf");
        String filename = testFile.getAbsolutePath();
        PdfReader rdr = new PdfReader(new RandomAccessFileOrArray(filename), new byte[0]);
        // this one works: PdfReader rdr = new PdfReader(filename);
        rdr.consolidateNamedDestinations(); // does not help
        rdr.getLinks(1);
        
        rdr.close();
    }

    @Test
    public void testPageResources() throws Exception {
    	File testFile = TestResourceUtils.getResourceAsTempFile(this, "getLinkTest2.pdf");
        String filename = testFile.getAbsolutePath();
        PdfReader rdr = new PdfReader(new RandomAccessFileOrArray(filename), new byte[0]);

        PdfDictionary pageResFromNum = rdr.getPageResources(1);
        PdfDictionary pageResFromDict = rdr.getPageResources(rdr.getPageN(1));
        // same size & keys
        assertTrue(pageResFromNum.getKeys().equals(pageResFromDict.getKeys()));
        
        rdr.close();
    }

    //Check for crash
    @Test
    public void readCompressedPdfTest1() throws IOException {
        File testFile = TestResourceUtils.getResourceAsTempFile(this, "readCompressedPdfTest1.pdf");
        String filename = testFile.getAbsolutePath();
        PdfReader rdr = new PdfReader(filename);
        for (int i = 1; i <= rdr.getNumberOfPages(); i++) {
            PdfDictionary p = rdr.getPageNRelease(i);
            assertEquals(PdfName.PAGE.toString(), p.getAsName(PdfName.TYPE).toString());
        }
        rdr.close();
    }

    @Test
    public void partialReadFromByteArrayTest() throws IOException {
        byte[] pdfFile = TestResourceUtils.getResourceAsByteArray(this, "iphone_user_guide.pdf");
        PdfReader reader = new PdfReader(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(pdfFile)), null, true);

        int pagesNum = 0;
        PdfDictionary pagesObj = reader.getCatalog().getAsDict(PdfName.PAGES);
        Stack<PdfDictionary> pages = new Stack<PdfDictionary>();
        pages.push(pagesObj);
        while(pages.size() > 0) {
            PdfDictionary page = pages.pop();
            PdfArray kids = page.getAsArray(PdfName.KIDS);
            if (kids != null) {
                for (int i = 0; i < kids.size(); ++i) {
                    pages.push(kids.getAsDict(i));
                }
            } else {
                ++pagesNum;
            }
        }

        assertTrue(String.format("There is 130 pages in document, but iText counted %d", pagesNum), pagesNum == 130);
    }

    @Test(expected = ExceptionConverter.class)
    public void circularReferencesInResources() throws IOException {
        File testFile = TestResourceUtils.getResourceAsTempFile(this, "circularReferencesInResources.pdf");
        String filename = testFile.getAbsolutePath();
        PdfReader rdr = new PdfReader(filename);
        rdr.close();
        BaseFont.getDocumentFonts(rdr);
    }

    @Test(expected = InvalidPdfException.class)
    public void circularReferencesInPageTree() throws IOException {
        File testFile = TestResourceUtils.getResourceAsTempFile(this, "circularReferencesInPageTree.pdf");
        String filename = testFile.getAbsolutePath();
        PdfReader rdr = new PdfReader(filename);
    }
}
