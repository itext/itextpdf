/* in_action/chapter12/SunTutorialExample.java */

package in_action.chapter12;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SunTutorialExample {

	/* the width and the height of the PDF page and the Graphics2D object. */
	int w, h;

	/**
	 * Example code from SUN's Java tutorial translated to PDF.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: example from SUN's Java tutorial");
		System.out.println("-> Converts a shape created with Graphics2D to PDF");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: sun_tutorial.pdf");
		SunTutorialExample example = new SunTutorialExample();
		example.createPdf();
	}

	/** Creates the example object. */
	public SunTutorialExample() {
		w = 150;
		h = 150;
		init();
	}

	/** Creates the PDF. */
	public void createPdf() {
		// step 1: creation of a document-object
		Document document = new Document(new Rectangle(w, h));
		try {
			// step 2:
			// we create a writer
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter12/sun_tutorial.pdf"));
			// step 3: we open the document
			document.open();
			// step 4:
			// we create a template and a Graphics2D object that corresponds
			// with it
			PdfContentByte cb = writer.getDirectContent();
			Graphics2D g2 = cb.createGraphics(w, h);
			paint(g2);
			g2.dispose();
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}

	// starting here, the code was copied from SUN's tutorial:
	// http://java.sun.com/docs/books/tutorial/2d/display/example-1dot2/Pear.java
	Ellipse2D.Double circle, oval, leaf, stem;

	Area circ, ov, leaf1, leaf2, st1, st2;

	public void init() {
		circle = new Ellipse2D.Double();
		oval = new Ellipse2D.Double();
		leaf = new Ellipse2D.Double();
		stem = new Ellipse2D.Double();
		circ = new Area(circle);
		ov = new Area(oval);
		leaf1 = new Area(leaf);
		leaf2 = new Area(leaf);
		st1 = new Area(stem);
		st2 = new Area(stem);
		/*
		 * In the original example, the background of the applet has to be set
		 * to white. setBackground(Color.white);
		 */
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		/*
		 * In our example w and h are member variables. Dimension d = getSize();
		 * int w = d.width; int h = d.height;
		 */
		double ew = w / 2;
		double eh = h / 2;

		g2.setColor(Color.green);

		// Creates the first leaf by filling the intersection of two Area
		// objects created from an ellipse.
		leaf.setFrame(ew - 16, eh - 29, 15.0, 15.0);
		leaf1 = new Area(leaf);
		leaf.setFrame(ew - 14, eh - 47, 30.0, 30.0);
		leaf2 = new Area(leaf);
		leaf1.intersect(leaf2);
		g2.fill(leaf1);

		// Creates the second leaf.
		leaf.setFrame(ew + 1, eh - 29, 15.0, 15.0);
		leaf1 = new Area(leaf);
		leaf2.intersect(leaf1);
		g2.fill(leaf2);

		g2.setColor(Color.black);

		// Creates the stem by filling the Area resulting from the subtraction
		// of two Area objects created from an ellipse.
		stem.setFrame(ew, eh - 42, 40.0, 40.0);
		st1 = new Area(stem);
		stem.setFrame(ew + 3, eh - 47, 50.0, 50.0);
		st2 = new Area(stem);
		st1.subtract(st2);
		g2.fill(st1);

		g2.setColor(Color.yellow);

		// Creates the pear itself by filling the Area resulting from the union
		// of two Area objects created by two different ellipses.
		circle.setFrame(ew - 25, eh, 50.0, 50.0);
		oval.setFrame(ew - 19, eh - 20, 40.0, 70.0);
		circ = new Area(circle);
		ov = new Area(oval);
		circ.add(ov);
		g2.fill(circ);
	}
}