/* in_action/chapter07/MultiColumnPoemCustom.java */

package in_action.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Steve Appling and adapted
 * by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class MultiColumnPoemCustom {

	/**
	 * Generates a PDF file with absurd poems in columns.
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 7: example MultiColumnPoemCustom");
		System.out.println("-> Creates a PDF file with absurd poems in columns.");
		System.out.println("-> jars needed: iText.jar");	
		System.out.println("-> file generated: poemcolumns.pdf");	
		// step 1: creation of a document-object
		Document document = new Document(PageSize.A4);
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
					// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter07/poemcolumns.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			MultiColumnText mct = new MultiColumnText();
            mct.addSimpleColumn(100, 280);
            mct.addSimpleColumn(300, 480);
            // Write some iText poems
            for (int i = 0; i < 30; i++) {
            	mct.addElement(new Paragraph(String.valueOf(i + 1)));
                mct.addElement(newParagraph(randomWord(noun), Element.ALIGN_CENTER, Font.BOLDITALIC));
                for (int j = 0; j < 4; j++) {
                    mct.addElement(newParagraph(poemLine(), Element.ALIGN_LEFT, Font.NORMAL));
                }
                mct.addElement(newParagraph(randomWord(adverb), Element.ALIGN_LEFT, Font.NORMAL));
                mct.addElement(newParagraph("\n\n", Element.ALIGN_LEFT, Font.NORMAL));
            }
            document.add(mct);
            document.close();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
	
	/** Generates a new Paragraph. */
    private static Element newParagraph(String text, int alignment, int type) {
        Font font = FontFactory.getFont(BaseFont.HELVETICA, 10, type);
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(alignment);
        p.setLeading(font.getSize() * 1.2f);
        return p;
     }

     static Random rand = new Random();
     static String[] verb = {"flows", "draws", "renders", "throws exception", "runs",
         "crashes", "downloads", "usurps", "vexes", "whispers", "boils",
         "capitulates", "crashes", "craves", "looks", "defies", "defers",
         "defines", "envelops", "entombs", "falls", "fails", "halts",
         "appears", "nags", "overflows", "burns", "dies", "writes",
         "flushes"};
     static String[] noun = {"ColumnText", "paragraph", "phrase", "chunk", "PdfContentByte",
         "PdfPTable", "iText", "color", "vertical alignment", "horizontal alignment", "PdfWriter",
         "ListItem", "PdfStamper", "PDF", "HTML", "XML", "column", "font",
         "table", "FDF", "field", "NullPointerException", "CJK font"};
     static String[] adjective = {"foul", "broken", "gray", "slow", "beautiful",
        "throbbing", "sharp", "stout", "soundless", "neat",
        "swift", "uniform", "upright", "vibrant", "dingy",
        "vestigal", "messy", "sloppy", "baleful", "boastful",
        "dark", "capricious", "concrete", "deliberate", "sharp",
         "drunken", "undisciplined", "perfect", "bloated"};
     static String[] adverb = {"randomly", "quickly", "triumphantly", "suggestively",
        "slowly", "angrily", "uncomfortably", "finally", "unexpectedly",
        "hysterically", "thinly", "dryly", "blazingly",
        "terribly", "bleakly", "irritably", "dazzlingly", "expectantly",
        "impersonally", "abruptly", "awfully", "caressingly", "completely",
        "undesirably", "drolly", "hypocritically", "blankly",
        "dimly"};

     /** Returns a random word from an array. */
     private static String randomWord(String[] type)
     {
        return type[rand.nextInt(type.length)];
     }

     /**
      * Generates a random poem line.
      * @return a poem that is generated with some keywords.
      */
     public static String poemLine()
     {
        StringBuffer results = new StringBuffer(150);
        results.append(randomWord(adjective));
        results.append(" ");
        results.append(randomWord(noun));
        results.append(" ");
        results.append(randomWord(verb));
        results.append(" ");
        results.append(randomWord(adverb));
        results.append(", ");
        return results.toString();
  }
}