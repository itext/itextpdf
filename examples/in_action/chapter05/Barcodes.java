/* in_action/chapter05/Barcodes.java */

package in_action.chapter05;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.Barcode39;
import com.lowagie.text.pdf.BarcodeCodabar;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.BarcodeEANSUPP;
import com.lowagie.text.pdf.BarcodeInter25;
import com.lowagie.text.pdf.BarcodePDF417;
import com.lowagie.text.pdf.BarcodePostnet;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class Barcodes {

	/**
	 * Generates a PDF file with different types of barcodes.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 5: example Barcodes");
		System.out.println("-> Creates a PDF file with barcodes.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: barcodes.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter05/barcodes.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			PdfContentByte cb = writer.getDirectContent();

			// EAN 13
			document.add(new Paragraph("Barcode EAN.UCC-13"));
			BarcodeEAN codeEAN = new BarcodeEAN();
			codeEAN.setCode("4512345678906");
			Paragraph p = new Paragraph("default: ");
			p.add(new Chunk(codeEAN.createImageWithBarcode(cb, null, null), 0,
					-5));
			codeEAN.setGuardBars(false);
			p.add(" without guard bars: ");
			p.add(new Chunk(codeEAN.createImageWithBarcode(cb, null, null), 0,
					-5));
			codeEAN.setBaseline(-1f);
			codeEAN.setGuardBars(true);
			p.add(" text above: ");
			p.add(new Chunk(codeEAN.createImageWithBarcode(cb, null, null), 0,
					-5));
			p.setLeading(codeEAN.getBarcodeSize().getHeight());
			document.add(p);
			codeEAN.setBaseline(codeEAN.getSize());

			// UPC A
			document.add(new Paragraph("Barcode UCC-12 (UPC-A)"));
			codeEAN.setCodeType(Barcode.UPCA);
			codeEAN.setCode("785342304749");
			document.add(codeEAN.createImageWithBarcode(cb, null, null));

			// EAN 8
			document.add(new Paragraph("Barcode EAN.UCC-8"));
			codeEAN.setCodeType(Barcode.EAN8);
			codeEAN.setBarHeight(codeEAN.getSize() * 1.5f);
			codeEAN.setCode("34569870");
			document.add(codeEAN.createImageWithBarcode(cb, null, null));

			// UPC E
			document.add(new Paragraph("Barcode UPC-E"));
			codeEAN.setCodeType(Barcode.UPCE);
			codeEAN.setCode("03456781");
			document.add(codeEAN.createImageWithBarcode(cb, null, null));
			codeEAN.setBarHeight(codeEAN.getSize() * 3f);

			// EANSUPP
			document.add(new Paragraph("Bookland"));
			document.add(new Paragraph("ISBN 0-321-30474-8"));
			codeEAN.setCodeType(Barcode.EAN13);
			codeEAN.setCode("9780321304742");
			BarcodeEAN codeSUPP = new BarcodeEAN();
			codeSUPP.setCodeType(Barcode.SUPP5);
			codeSUPP.setCode("55499");
			codeSUPP.setBaseline(-2);
			BarcodeEANSUPP eanSupp = new BarcodeEANSUPP(codeEAN, codeSUPP);
			document.add(eanSupp.createImageWithBarcode(cb, null, Color.blue));

			// CODE 128
			document.add(new Paragraph("Barcode 128"));
			Barcode128 code128 = new Barcode128();
			code128.setCode("0123456789 hello");
			document.add(code128.createImageWithBarcode(cb, null, null));
			code128.setCode("0123456789\uffffMy Raw Barcode (0 - 9)");
			code128.setCodeType(Barcode.CODE128_RAW);
			document.add(code128.createImageWithBarcode(cb, null, null));

			// Data for the barcode :
			String code402 = "24132399420058289";
			String code90 = "3700000050";
			String code421 = "422356";
			StringBuffer data = new StringBuffer(code402);
			data.append(Barcode128.FNC1);
			data.append(code90);
			data.append(Barcode128.FNC1);
			data.append(code421);
			Barcode128 shipBarCode = new Barcode128();
			shipBarCode.setX(0.75f);
			shipBarCode.setN(1.5f);
			shipBarCode.setSize(10f);
			shipBarCode.setTextAlignment(Element.ALIGN_CENTER);
			shipBarCode.setBaseline(10f);
			shipBarCode.setBarHeight(50f);
			shipBarCode.setCode(data.toString());
			document.add(shipBarCode.createImageWithBarcode(cb, Color.black,
					Color.blue));

			// it is composed of 3 blocks whith AI 01, 3101 and 10
			Barcode128 uccEan128 = new Barcode128();
			uccEan128.setCodeType(Barcode.CODE128_UCC);
			uccEan128.setCode("(01)00000090311314(10)ABC123(15)060916");
			document.add(uccEan128.createImageWithBarcode(cb, Color.blue,
					Color.black));
			uccEan128.setCode("0191234567890121310100035510ABC123");
			document.add(uccEan128.createImageWithBarcode(cb, Color.blue,
					Color.red));
			uccEan128.setCode("(01)28880123456788");
			document.add(uccEan128.createImageWithBarcode(cb, Color.blue,
					Color.black));

			// INTER25
			document.add(new Paragraph("Barcode Interleaved 2 of 5"));
			BarcodeInter25 code25 = new BarcodeInter25();
			code25.setGenerateChecksum(true);
			code25.setCode("41-1200076041-001");
			document.add(code25.createImageWithBarcode(cb, null, null));
			code25.setCode("411200076041001");
			document.add(code25.createImageWithBarcode(cb, null, null));
			code25.setCode("0611012345678");
			code25.setChecksumText(true);
			document.add(code25.createImageWithBarcode(cb, null, null));
			document.newPage();

			// POSTNET
			document.add(new Paragraph("Barcode Postnet"));
			BarcodePostnet codePost = new BarcodePostnet();
			document.add(new Paragraph("ZIP"));
			codePost.setCode("01234");
			document.add(codePost.createImageWithBarcode(cb, null, null));
			document.add(new Paragraph("ZIP+4"));
			codePost.setCode("012345678");
			document.add(codePost.createImageWithBarcode(cb, null, null));
			document.add(new Paragraph("ZIP+4 and dp"));
			codePost.setCode("01234567890");
			document.add(codePost.createImageWithBarcode(cb, null, null));

			document.add(new Paragraph("Barcode Planet"));
			BarcodePostnet codePlanet = new BarcodePostnet();
			codePlanet.setCode("01234567890");
			codePlanet.setCodeType(Barcode.PLANET);
			document.add(codePlanet.createImageWithBarcode(cb, null, null));

			// CODE 39
			document.add(new Paragraph("Barcode 3 of 9"));
			Barcode39 code39 = new Barcode39();
			code39.setCode("ITEXT IN ACTION");
			document.add(code39.createImageWithBarcode(cb, null, null));

			document.add(new Paragraph("Barcode 3 of 9 extended"));
			Barcode39 code39ext = new Barcode39();
			code39ext.setCode("iText in Action");
			code39ext.setStartStopText(false);
			code39ext.setExtended(true);
			document.add(code39ext.createImageWithBarcode(cb, null, null));

			// CODABAR
			document.add(new Paragraph("Codabar"));
			BarcodeCodabar codabar = new BarcodeCodabar();
			codabar.setCode("A123A");
			codabar.setStartStopText(true);
			document.add(codabar.createImageWithBarcode(cb, null, null));

			// PDF417
			document.add(new Paragraph("Barcode PDF417"));
			BarcodePDF417 pdf417 = new BarcodePDF417();
			String text = "It was the best of times, it was the worst of times, "
					+ "it was the age of wisdom, it was the age of foolishness, "
					+ "it was the epoch of belief, it was the epoch of incredulity, "
					+ "it was the season of Light, it was the season of Darkness, "
					+ "it was the spring of hope, it was the winter of despair, "
					+ "we had everything before us, we had nothing before us, "
					+ "we were all going direct to Heaven, we were all going direct "
					+ "the other way - in short, the period was so far like the present "
					+ "period, that some of its noisiest authorities insisted on its "
					+ "being received, for good or for evil, in the superlative degree "
					+ "of comparison only.";
			pdf417.setText(text);
			Image img = pdf417.getImage();
			img.scalePercent(50, 50 * pdf417.getYHeight());
			document.add(img);

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
