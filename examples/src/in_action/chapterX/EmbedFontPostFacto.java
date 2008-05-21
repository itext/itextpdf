package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfStream;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class EmbedFontPostFacto {

	public static void main(String[] args) {
		try {
			String font_path = "resources/in_action/chapter09/abserif4_5.ttf";
			BaseFont bf = BaseFont.createFont(
					font_path, BaseFont.WINANSI,
					BaseFont.NOT_EMBEDDED);
			createPdf("results/in_action/chapterX/not_embedded.pdf", bf);
			PdfReader reader = new PdfReader("results/in_action/chapterX/not_embedded.pdf");
			EmbedFontPostFacto embed_font = new EmbedFontPostFacto();
			int n = reader.getNumberOfPages() + 1;
			PdfDictionary page_dict;
			for (int page = 1; page < n; page++) {
				page_dict = reader.getPageN(page);
				embed_font.processResource((PdfDictionary)page_dict.get(PdfName.RESOURCES), "/" + bf.getPostscriptFontName());
			}
			
			RandomAccessFile raf = new RandomAccessFile(font_path, "r");
			byte font[] = new byte[(int)raf.length()];
			raf.readFully(font);
			raf.close();
			
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("results/in_action/chapterX/embedded.pdf"));
			PdfStream stream = new PdfStream(font);
			stream.flateCompress();
			stream.put(PdfName.LENGTH1, new PdfNumber(font.length));
			PdfDictionary fdesc = (PdfDictionary)PdfReader.getPdfObject(embed_font.font_ref);
			PdfIndirectReference refFont = stamper.getWriter().addToBody(stream).getIndirectReference();
			fdesc.put(PdfName.FONTFILE2, refFont);
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private PdfIndirectReference font_ref = null;
	
	public void processResource(PdfDictionary resource, String fontname) {
		if (resource == null)
			return;
		PdfDictionary x = (PdfDictionary)PdfReader.getPdfObject(resource.get(PdfName.XOBJECT));
		if (x != null) {
			PdfName name;
			for (Iterator i = x.getKeys().iterator(); i.hasNext(); ) {
				name = (PdfName)i.next();
				PdfDictionary xobject = (PdfDictionary)PdfReader.getPdfObject(x.get(name));
				processResource((PdfDictionary)PdfReader.getPdfObject(xobject.get(PdfName.RESOURCES)), fontname);
			}
		}
		PdfDictionary fonts = (PdfDictionary)PdfReader.getPdfObject(resource.get(PdfName.FONT));
		if (fonts == null)
			return;
		PdfDictionary font;
		PdfName name;
		for (Iterator i = fonts.getKeys().iterator(); i.hasNext(); ) {
			name = (PdfName)i.next();
			font = (PdfDictionary)PdfReader.getPdfObject(fonts.get(name));
			name = (PdfName)PdfReader.getPdfObject(font.get(PdfName.BASEFONT));
			if (fontname.equals(name.toString())) {
				font_ref = (PdfIndirectReference)font.get(PdfName.FONTDESCRIPTOR);
			}
		}
	}
	
	public static void createPdf(String filename, BaseFont bf) {
		// step 1
		Document document = new Document();
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(filename));

			// step 3: we open the document
			document.open();
			// step 4:
			Font font = new Font(bf, 12);
			document.add(new Paragraph("abcdefghijklmnopqrstuvwxyz", font));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
