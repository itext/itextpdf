/**
 * 
 */
package com.itextpdf.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

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
    public static final String OUTFOLDER = "./target/com/itextpdf/test/Chunk";
    public static final String OUT = OUTFOLDER + "/out.pdf";

    @Before
    public void Init() throws IOException{
        new File(OUTFOLDER).mkdirs();
        File f = new File(OUT.substring(0,32));
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
    public void documentTest() throws Exception
    {
            Font f = FontFactory.getFont(FontFactory.COURIER, 11);
            FileOutputStream fs = new FileOutputStream(OUT);
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, fs);
            Paragraph p;
            writer.setCompressionLevel(0);
            doc.open();

            p = new Paragraph(new Chunk("Hello world", f));
            addTabs(p, f, 0);
            doc.add(p);

            p = new Paragraph(new Chunk("Hello World!!!"));
            addTabs(p, f, 0);
            doc.add(p);

            f.setSize(16);
            p = new Paragraph(new Chunk("Hello world", f));
            addTabs(p, f, 0);
            doc.add(p);

            f = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
            p = new Paragraph(new Chunk("Hello world", f));
            addTabs(p, f, 0);
            doc.add(p);

            f.setSize(20);
            p = new Paragraph(new Chunk("Hello world", f));
            addTabs(p, f, 0);
            doc.add(p);

            doc.close();
            fs.close();
            Assert.assertTrue(compareInnerText(SOURCE11, OUT));
        
    }

    @Test
    public void columntTextTest() throws Exception
    {
            Font f = FontFactory.getFont(FontFactory.COURIER, 11);
            Document doc = new Document();
            Paragraph p;
            FileOutputStream fs = new FileOutputStream(OUT);
            PdfWriter writer = PdfWriter.getInstance(doc, fs);
            writer.setCompressionLevel(0);
            doc.open();
            ColumnText ct = new ColumnText(writer.getDirectContent());
            ct.setSimpleColumn(36, 36, 436, 800);
            p = new Paragraph(new Chunk("Hello world", f));
            addTabs(p, f, 0);
            ct.addElement(p);

            p = new Paragraph(new Chunk("Hello World!!!"));
            addTabs(p, f, 0);
            ct.addElement(p);

            f.setSize(16);
            p = new Paragraph(new Chunk("Hello world", f));
            addTabs(p, f, 0);
            ct.addElement(p);

            f = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);
            p = new Paragraph(new Chunk("Hello world", f));
            addTabs(p, f, 0);
            ct.addElement(p);

            f.setSize(20);
            p = new Paragraph(new Chunk("Hello world", f));
            addTabs(p, f, 0);
            ct.addElement(p);
            ct.go();
            doc.close();
            fs.close();
            Assert.assertTrue(compareInnerText(SOURCE12, OUT));
        
    }

    public void addTabs(Paragraph p, Font f, int count)
    {
        p.add(Chunk.createTabspace());
        p.add(new Chunk("|", f));
        if (count == 16)
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
