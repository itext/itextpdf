/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import org.junit.Assert;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class TextRenderInfoTest {

    public static final int FIRST_PAGE = 1;
    public static final int FIRST_ELEMENT_INDEX = 0;

	@Test
	public void testCharacterRenderInfos() throws Exception {
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate().rotate(), "ABCD");
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));

        PdfReader r = new PdfReader(bytes);

        PdfReaderContentParser parser = new PdfReaderContentParser(r);
        parser.processContent(FIRST_PAGE, new CharacterPositionRenderListener());

	}

        /**
         * Test introduced to exclude a bug related to a Unicode quirk for
         * Japanese. TextRenderInfo threw an AIOOBE for some characters.
         * @throws java.lang.Exception
         * @since 5.5.5-SNAPSHOT
         */
        @Test
        public void testUnicodeEmptyString() throws Exception {
            StringBuilder sb = new StringBuilder();
            String inFile = "japanese_text.pdf";


            PdfReader p = TestResourceUtils.getResourceAsPdfReader(this, inFile);
            TextExtractionStrategy strat = new SimpleTextExtractionStrategy();

            sb.append(PdfTextExtractor.getTextFromPage(p, FIRST_PAGE, strat));

            String result = sb.substring(0, sb.indexOf("\n"));
            String origText =
                    "\u76f4\u8fd1\u306e\u0053\uff06\u0050\u0035\u0030\u0030"
                    + "\u914d\u5f53\u8cb4\u65cf\u6307\u6570\u306e\u30d1\u30d5"
                    + "\u30a9\u30fc\u30de\u30f3\u30b9\u306f\u0053\uff06\u0050"
                    + "\u0035\u0030\u0030\u6307\u6570\u3092\u4e0a\u56de\u308b";
            Assert.assertEquals(result, origText);
        }

    @Test
    public void testType3FontWidth() throws Exception {
        String inFile = "type3font_text.pdf";
        LineSegment origLineSegment = new LineSegment(new Vector(20.3246f, 769.4974f, 1.0f), new Vector(151.22923f, 769.4974f, 1.0f));

        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, inFile);
        TextPositionRenderListener renderListener = new TextPositionRenderListener();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(renderListener);

        PdfDictionary pageDic = reader.getPageN(FIRST_PAGE);
        PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, FIRST_PAGE), resourcesDic);


        Assert.assertEquals(renderListener.getLineSegments().get(FIRST_ELEMENT_INDEX).getStartPoint().get(FIRST_ELEMENT_INDEX),
                origLineSegment.getStartPoint().get(FIRST_ELEMENT_INDEX), 1 / 2f);

        Assert.assertEquals(renderListener.getLineSegments().get(FIRST_ELEMENT_INDEX).getEndPoint().get(FIRST_ELEMENT_INDEX),
                origLineSegment.getEndPoint().get(FIRST_ELEMENT_INDEX), 1 / 2f);

    }


    private static class TextPositionRenderListener implements RenderListener {

        List<LineSegment> lineSegments = new ArrayList<LineSegment>();

        public List<LineSegment> getLineSegments() {
            return lineSegments;
        }

        public void renderText(TextRenderInfo renderInfo) {
            lineSegments.add(renderInfo.getBaseline());
        }

        public void beginTextBlock() {
        }

        public void endTextBlock() {

        }

        public void renderImage(ImageRenderInfo renderInfo) {

        }


    }

	private static class CharacterPositionRenderListener implements TextExtractionStrategy{

		public void beginTextBlock() {
		}

		public void renderText(TextRenderInfo renderInfo) {
			List<TextRenderInfo> subs = renderInfo.getCharacterRenderInfos();
			TextRenderInfo previousCharInfo = subs.get(0);

			for(int i = 1; i < subs.size(); i++){
				TextRenderInfo charInfo = subs.get(i);
				Vector previousEndPoint = previousCharInfo.getBaseline().getEndPoint();
				Vector currentStartPoint = charInfo.getBaseline().getStartPoint();
				assertVectorsEqual(charInfo.getText(), previousEndPoint, currentStartPoint);
				previousCharInfo = charInfo;
			}

		}

		private void assertVectorsEqual(String message, Vector v1, Vector v2){
			Assert.assertEquals(message, v1.get(0), v2.get(0), 1/72f);
			Assert.assertEquals(message, v1.get(1), v2.get(1), 1/72f);
		}
		public void endTextBlock() {
		}

		public void renderImage(ImageRenderInfo renderInfo) {
		}

		public String getResultantText() {
			return null;
		}

	}

	private byte[] createSimplePdf(Rectangle pageSize, final String... text) throws Exception{
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document(pageSize);
        PdfWriter.getInstance(document, byteStream);
        document.open();
        for (String string : text) {
            document.add(new Paragraph(string));
            document.newPage();
        }

        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;
}
}
