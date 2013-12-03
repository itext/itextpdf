/*
 * Created on Nov 2, 2011
 * (c) 2011 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.parser.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Kevin
 */
public class CMapAwareDocumentFontTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWidths() throws Exception{
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "fontwithwidthissue.pdf");

        try {
            PdfDictionary fontsDic = pdfReader.getPageN(1).getAsDict(PdfName.RESOURCES).getAsDict(PdfName.FONT);
            PRIndirectReference fontDicIndirect = (PRIndirectReference)fontsDic.get(new PdfName("F1"));
            
            CMapAwareDocumentFont f = new CMapAwareDocumentFont(fontDicIndirect);
            Assert.assertTrue("Width should not be 0", f.getWidth('h') != 0);
        } finally {
            pdfReader.close();
        }
    }

    @Ignore
    @Test
    public void weirdHyphensTest() throws IOException {
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, "WeirdHyphens.pdf");
        ArrayList<String> textChunks = new ArrayList<String>();
        RenderListener listener
                = new MyTextRenderListener(textChunks);
        PdfContentStreamProcessor processor
                = new PdfContentStreamProcessor(listener);
        PdfDictionary pageDic = reader.getPageN(1);
        PdfDictionary resourcesDic
                = pageDic.getAsDict(PdfName.RESOURCES);
        processor.processContent(ContentByteUtils
                .getContentBytesForPage(reader, 1), resourcesDic);
        /**
         * This assertion makes sure that encoding has been read properly from FontDescriptor.
         * If not the vallue will be "\u0000 14".
         */
        Assert.assertEquals("\u0096 14", textChunks.get(18));
        reader.close();
    }

    static class MyTextRenderListener implements RenderListener {

        ArrayList<String> textChunks;

        MyTextRenderListener(ArrayList<String> textChunks) {
            this.textChunks = textChunks;
        }

        public void beginTextBlock() {
        }

        public void endTextBlock() {
        }

        public void renderImage(ImageRenderInfo renderInfo) {
        }

        public void renderText(TextRenderInfo renderInfo) {
            textChunks.add(renderInfo.getText());
        }
    }


}
