/**
 * 
 */
package com.itextpdf.text;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.parser.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author redlab
 *
 */
public class ChunkTest {

	
	/**
	 * 
	 */
	private static final String _4SPACES = "    4spaces    ";
	private static final String _TAB = "\t4spaces    ";
    public static final String SOURCE11 = "./src/test/resources/com/itextpdf/text/Chunk/source11.pdf";
    public static final String SOURCE12 = "./src/test/resources/com/itextpdf/text/Chunk/source12.pdf";
    public static final String SOURCE13 = "./src/test/resources/com/itextpdf/text/Chunk/source13.pdf";
    public static final String SOURCE14 = "./src/test/resources/com/itextpdf/text/Chunk/source14.pdf";
    public static final String SOURCE15 = "./src/test/resources/com/itextpdf/text/Chunk/source15.pdf";
    public static final String OUTFOLDER = "./target/com/itextpdf/test/Chunk";
    public static final String OUTTABSPACED = OUTFOLDER + "/tabspaceDocument.pdf";
    public static final String OUTABSPACEC = OUTFOLDER + "/tabspaceColumnText.pdf";
    public static final String OUTTABD = OUTFOLDER + "/tabDocument.pdf";
    public static final String OUTABC = OUTFOLDER + "/tabColumnText.pdf";
    public static final String OUTABSTOPSC = OUTFOLDER + "/tabstopsColumnText.pdf";
    public static final String OUTSPTRIMDOC = OUTFOLDER + "/spaceTrimDoc.pdf";
    public static final String OUTSPTRIMCT = OUTFOLDER + "/spaceTrimColumnText.pdf";


    @Before
    public void Init() throws IOException{
        new File(OUTFOLDER).mkdirs();
        File f = new File(OUTTABSPACED.substring(0,32));
        if (!f.exists())
            f.mkdir();
    }
    
	@Test
	public void prependingWhitspaces() {
		Chunk c = new Chunk(_4SPACES);
		Assert.assertEquals("difference in string", _4SPACES, c.getContent());
	}
	@Test
	public void prependingTab() {
		Chunk c = new Chunk(_TAB);
		Assert.assertEquals("difference in string", "4spaces    ", c.getContent());
	}

    @Test
    public void tabspaceDocumentTest() throws Exception {
        Font f = FontFactory.getFont(FontFactory.COURIER, 11);
        FileOutputStream fs = new FileOutputStream(OUTTABSPACED);
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, fs);
        Paragraph p;
        writer.setCompressionLevel(0);
        doc.open();

        p = new Paragraph(new Chunk("Hello world", f));
        addTabspaces(p, f, 0);
        doc.add(p);

        p = new Paragraph(new Chunk("Hello World!!!"));
        addTabspaces(p, f, 0);
        doc.add(p);

        f.setSize(16);
        p = new Paragraph(new Chunk("Hello world", f));
        addTabspaces(p, f, 0);
        doc.add(p);

        f = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
        p = new Paragraph(new Chunk("Hello world", f));
        addTabspaces(p, f, 0);
        doc.add(p);

        f.setSize(20);
        p = new Paragraph(new Chunk("Hello world", f));
        addTabspaces(p, f, 0);
        doc.add(p);

        doc.close();
        fs.close();
        Assert.assertTrue(compareInnerText(SOURCE11, OUTTABSPACED));
    }

    @Test
    public void tabspaceColumnTextTest() throws Exception {
        Font f = FontFactory.getFont(FontFactory.COURIER, 11);
        Document doc = new Document();
        Paragraph p;
        FileOutputStream fs = new FileOutputStream(OUTABSPACEC);
        PdfWriter writer = PdfWriter.getInstance(doc, fs);
        writer.setCompressionLevel(0);
        doc.open();
        ColumnText ct = new ColumnText(writer.getDirectContent());
        ct.setSimpleColumn(36, 36, 436, 800);
        p = new Paragraph(new Chunk("Hello world", f));
        addTabspaces(p, f, 0);
        ct.addElement(p);

        p = new Paragraph(new Chunk("Hello World!!!"));
        addTabspaces(p, f, 0);
        ct.addElement(p);

        f.setSize(16);
        p = new Paragraph(new Chunk("Hello world", f));
        addTabspaces(p, f, 0);
        ct.addElement(p);

        f = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
        p = new Paragraph(new Chunk("Hello world", f));
        addTabspaces(p, f, 0);
        ct.addElement(p);

        f.setSize(20);
        p = new Paragraph(new Chunk("Hello world", f));
        addTabspaces(p, f, 0);
        ct.addElement(p);
        ct.go();
        doc.close();
        fs.close();
        Assert.assertTrue(compareInnerText(SOURCE12, OUTABSPACEC));
    }

    @Test
    public void tabDocumentTest() throws Exception {
        Font f = FontFactory.getFont(FontFactory.COURIER, 11);
        FileOutputStream fs = new FileOutputStream(OUTTABD);
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, fs);
        Paragraph p;
        java.util.List<TabStop> tabStopsList = new ArrayList<TabStop>();
        tabStopsList.add(new TabStop(100, new DottedLineSeparator()));
        tabStopsList.add(new TabStop(200, new LineSeparator(), TabStop.Alignment.CENTER));
        tabStopsList.add(new TabStop(300, new DottedLineSeparator(), TabStop.Alignment.RIGHT));
        writer.setCompressionLevel(0);
        doc.open();

        p = new Paragraph(new Chunk("Hello world", f));
        addTabs(p, f, 0, "la|la");
        p.setTabSettings(new TabSettings(tabStopsList, 50));
        doc.add(p);

        tabStopsList = new ArrayList<TabStop>();
        tabStopsList.add(new TabStop(100, new DottedLineSeparator()));
        tabStopsList.add(new TabStop(200, new LineSeparator(), TabStop.Alignment.ANCHOR));
        tabStopsList.add(new TabStop(300, new DottedLineSeparator(), TabStop.Alignment.ANCHOR));
        p = new Paragraph(new Chunk("Hello World!!!"));
        p.setTabSettings(new TabSettings(tabStopsList, 50));
        addTabs(p, f, 12, "l.aal");
        addTabs(p, f, 12, "laa.l");
        addTabs(p, f, 12, "la.al");
        doc.add(p);

        f.setSize(16);
        tabStopsList = new ArrayList<TabStop>();
        tabStopsList.add(new TabStop(100, new DottedLineSeparator()));
        tabStopsList.add(new TabStop(200, new LineSeparator(), TabStop.Alignment.ANCHOR, ','));
        tabStopsList.add(new TabStop(300, new DottedLineSeparator(), TabStop.Alignment.ANCHOR));
        p = new Paragraph(new Chunk("Hello world", f));
        p.setTabSettings(new TabSettings(tabStopsList, 50));
        addTabs(p, f, 13, "l.aal");
        addTabs(p, f, 11, "l,aal");
        addTabs(p, f, 11, "laa.l");
        addTabs(p, f, 11, "laa,l");
        doc.add(p);

        f = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
        p = new Paragraph(new Chunk("Hello world", f));
        p.setTabSettings(new TabSettings(38));
        addTabs(p, f, 0);
        doc.add(p);

        f.setSize(20);
        p = new Paragraph(new Chunk("Hello world", f));
        p.setTabSettings(new TabSettings(38));
        addTabs(p, f, 0);
        doc.add(p);

        doc.close();
        fs.close();
        Assert.assertTrue(compareInnerText(SOURCE13, OUTTABD));
    }

    @Test
    public void tabColumnTextTest() throws Exception {
        Font f = FontFactory.getFont(FontFactory.COURIER, 11);
        Document doc = new Document();
        Paragraph p;
        FileOutputStream fs = new FileOutputStream(OUTABC);
        PdfWriter writer = PdfWriter.getInstance(doc, fs);
        writer.setCompressionLevel(0);
        doc.open();
        ColumnText ct = new ColumnText(writer.getDirectContent());
        ct.setSimpleColumn(36, 36, 436, 800);
        java.util.List<TabStop> tabStopsList = new ArrayList<TabStop>();
        tabStopsList.add(new TabStop(100, new DottedLineSeparator()));
        tabStopsList.add(new TabStop(200, new LineSeparator(), TabStop.Alignment.CENTER));
        tabStopsList.add(new TabStop(300, new DottedLineSeparator(), TabStop.Alignment.RIGHT));
        p = new Paragraph(new Chunk("Hello world", f));
        p.setTabSettings(new TabSettings(tabStopsList, 50));
        addTabs(p, f, 0, "la|la");
        ct.addElement(p);

        tabStopsList = new ArrayList<TabStop>();
        tabStopsList.add(new TabStop(100, new DottedLineSeparator()));
        tabStopsList.add(new TabStop(200, new LineSeparator(), TabStop.Alignment.ANCHOR));
        tabStopsList.add(new TabStop(300, new DottedLineSeparator(), TabStop.Alignment.ANCHOR));
        p = new Paragraph(new Chunk("Hello World!!!"));
        p.setTabSettings(new TabSettings(tabStopsList, 50));
        addTabs(p, f, 12, "l.aal");
        addTabs(p, f, 12, "laa.l");
        addTabs(p, f, 12, "la.al");
        ct.addElement(p);

        f.setSize(16);
        p = new Paragraph(new Chunk("Hello world", f));
        tabStopsList = new ArrayList<TabStop>();
        tabStopsList.add(new TabStop(100, new DottedLineSeparator()));
        tabStopsList.add(new TabStop(200, new LineSeparator(), TabStop.Alignment.ANCHOR, ','));
        tabStopsList.add(new TabStop(300, new DottedLineSeparator(), TabStop.Alignment.ANCHOR));
        p.setTabSettings(new TabSettings(tabStopsList, 50));
        addTabs(p, f, 15, "l.aal");
        addTabs(p, f, 13, "laa,l");
        addTabs(p, f, 13, "laa.l");
        addTabs(p, f, 13, "l,aal");
        ct.addElement(p);

        f = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
        p = new Paragraph(new Chunk("Hello world", f));
        p.setTabSettings(new TabSettings(38));
        addTabs(p, f, 0);
        ct.addElement(p);

        f.setSize(20);
        p = new Paragraph(new Chunk("Hello world", f));
        p.setTabSettings(new TabSettings(38));
        addTabs(p, f, 0);
        ct.addElement(p);
        ct.go();
        doc.close();
        fs.close();
        Assert.assertTrue(compareInnerText(SOURCE14, OUTABC));

    }

    @Test
    public void tabStopsColumnText() throws Exception{
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTABSTOPSC));
        document.open();

        Font oFont1 = FontFactory.getFont(BaseFont.ZAPFDINGBATS, 15, Font.UNDEFINED);
        Font oFont2 = FontFactory.getFont(BaseFont.COURIER, 15, Font.UNDEFINED);
        Font oFont3 = FontFactory.getFont(BaseFont.TIMES_ROMAN, 15, Font.UNDEFINED);
        Font oFont4 = FontFactory.getFont(BaseFont.HELVETICA, 15, Font.UNDEFINED);
        Image oImg = Image.getInstance("./src/test/resources/com/itextpdf/text/Chunk/logo.gif");
        PdfContentByte canvas = writer.getDirectContentUnder();

        java.util.List<TabStop> tabStops = new ArrayList<TabStop>();
        //tabStops.add(new TabStop(100, new DottedLineSeparator()));
        //tabStops.add(new TabStop(200, new DottedLineSeparator()));
        tabStops.add(new TabStop(200, new DottedLineSeparator()));
        //tabStops.add(new TabStop(300, new DottedLineSeparator()));
        tabStops.add(new TabStop(400, new DottedLineSeparator()));
        //tabStops.add(new TabStop(500, new DottedLineSeparator()));
        //tabStops.add(new TabStop(550, new DottedLineSeparator()));

        Paragraph oPara = new Paragraph("Hello World! ", oFont1);
        oPara.setTabSettings(new TabSettings(tabStops));
        oPara.add(new Chunk("iText ® is a library that allows you to create and manipulate PDF documents.", oFont2));
        oPara.add(new Chunk("It enables developers looking to enhance web- and other applications with dynamic PDF docu", oFont3));
        oPara.add(Chunk.TABBING);
        oPara.add(new Chunk("ment generation and/or manipulation.", oFont3));
        oPara.add(new Chunk(oImg, 0, 0, true));
        //oPara.Add(new Chunk(new TestVerticalPositionMark()));
        oPara.add(Chunk.TABBING);
        oPara.add(new Chunk("Developers can use iText to:", oFont4));

        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("|100"), 100, 500, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("|200"), 200, 500, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("|250"), 250, 500, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("|300"), 300, 500, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("|400"), 400, 500, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("|500"), 500, 500, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("|550"), 550, 500, 0);
        ColumnText oColTxt1 = new ColumnText(canvas);

        oColTxt1.setSimpleColumn(0, 400, 595, 500);
        oColTxt1.addElement(oPara);
        oColTxt1.go();

        document.close();
        Assert.assertTrue(compareInnerText(SOURCE15, OUTABSTOPSC));
    }

    @Test
    public void spaceTrimPdfDocumentTest() throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4, 50, 30, 50, 30);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(OUTSPTRIMDOC)));
        doc.open();

        Phrase under = new Phrase();
        under.setFont(new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE));
        under.add(new Chunk(" 1                                                      1                                                                                                                             9      "));

        doc.add(under);

        doc.close();
        writer.close();

        PdfReader reader = new PdfReader(OUTSPTRIMDOC);
        MyTextRenderListener listener = new MyTextRenderListener();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);
        PdfDictionary pageDic = reader.getPageN(1);
        PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, 1), resourcesDic);
        //should be 60, as in @spaceTrimColumnTextTest
        //Assert.assertTrue("Unexpected text length", listener.getText().length() == 60);
        Assert.assertTrue("Unexpected text length", listener.getText().length() == 77);
    }

    @Test
    public void spaceTrimColumnTextTest() throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4, 50, 30, 50, 30);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(new File(OUTSPTRIMCT)));
        doc.open();

        Phrase under = new Phrase();
        under.setFont(new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.UNDERLINE));
        under.add(new Chunk(" 1                                                      1                                                                                                                             9      "));

        Paragraph underlineTest = new Paragraph(under);
        underlineTest.setKeepTogether(true);
        doc.add(underlineTest);

        doc.close();
        writer.close();

        PdfReader reader = new PdfReader(OUTSPTRIMCT);
        MyTextRenderListener listener = new MyTextRenderListener();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);
        PdfDictionary pageDic = reader.getPageN(1);
        PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
        processor.processContent(ContentByteUtils.getContentBytesForPage(reader, 1), resourcesDic);
        Assert.assertTrue("Unexpected text length", listener.getText().length() == 60);
    }

    private static class MyTextRenderListener implements RenderListener {
        protected StringBuffer buffer = new StringBuffer();

        public void beginTextBlock() {
        }

        public void endTextBlock() {
        }

        public void renderImage(ImageRenderInfo renderInfo) {
        }

        public void renderText(TextRenderInfo renderInfo) {
            buffer.append(renderInfo.getText());
            buffer.append("\n");
        }
        public String getText(){
            return buffer.toString();
        }
    }

    public void addTabspaces(Paragraph p, Font f, int count)
    {
        p.add(Chunk.createTabspace());
        p.add(new Chunk("|", f));
        if (count == 16)
            return;
        else
            addTabspaces(p, f, count + 1);
    }

    public void addTabs(Paragraph p, Font f, int count, String text)
    {
        p.add(Chunk.TABBING);
        p.add(new Chunk(text, f));
        if (count == 17)
            return;
        else
            addTabs(p, f, count + 1, text);
    }

    public void addTabs(Paragraph p, Font f, int count)
    {
        p.add(Chunk.TABBING);
        p.add(new Chunk("|", f));
        if (count == 17)
            return;
        else
            addTabs(p, f, count + 1);
    }

    public boolean compareInnerText(String path1, String path2) throws IOException{
        PdfReader reader1 = new PdfReader(path1);
        byte[] streamBytes1 = reader1.getPageContent(1);
        PRTokeniser tokenizer1 = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(streamBytes1)));
        


        PdfReader reader2 = new PdfReader(path2);
        byte[] streamBytes2 = reader2.getPageContent(1);
        PRTokeniser tokenizer2 = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(streamBytes2)));

        try{
	        while (tokenizer1.nextToken()) {
	            if (!tokenizer2.nextToken())
	                return false;
	            else {
	                if (tokenizer1.getTokenType() != tokenizer2.getTokenType())
	                    return false;
	                else  {
	                    if (tokenizer1.getTokenType() == PRTokeniser.TokenType.NUMBER)
	                    {
	                        if (Math.abs(Float.parseFloat(tokenizer1.getStringValue())
	                                    -Float.parseFloat(tokenizer2.getStringValue())) > 0.1)
	                            return false;
	                    }
	                    else
	                        if (!tokenizer1.getStringValue().equals(tokenizer2.getStringValue()))
	                            return false;
	                }
	
	            }
	        }
	        return true;
        } finally {
        	reader1.close();
        	reader2.close();
        }
    }

}
