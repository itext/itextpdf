/* in_action/chapter12/FoobarCharts.java */
package in_action.chapter12;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoobarCharts {

	/**
	 * Shows some charts with the number of students.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 7: FoobarCharts");
		System.out.println("-> Creates a barchart and a pie chart.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("                jfreechart.jar");
		System.out.println("                jcommon.jar");
		System.out.println("-> resulting PDFs: barchart.pdf and piechart.pdf");
		org.jfree.text.TextUtilities.setUseDrawRotatedStringWorkaround(false);
		convertToPdf(getBarChart(), 400, 600, "results/in_action/chapter12/barchart.pdf");
		convertToPdf(getPieChart(), 400, 600, "results/in_action/chapter12/piechart.pdf");
	}

	/**
	 * Converts a JFreeChart to PDF syntax.
	 * 
	 * @param filename
	 *            the name of the PDF file
	 * @param chart
	 *            the JFreeChart
	 * @param width
	 *            the width of the resulting PDF
	 * @param height
	 *            the height of the resulting PDF
	 */
	public static void convertToPdf(JFreeChart chart, int width, int height,
			String filename) {
		// step 1
		Document document = new Document(new Rectangle(width, height));
		try {
			// step 2
			PdfWriter writer;
			writer = PdfWriter.getInstance(document, new FileOutputStream(
					filename));
			// step 3
			document.open();
			// step 4
			PdfContentByte cb = writer.getDirectContent();
			PdfTemplate tp = cb.createTemplate(width, height);
			Graphics2D g2d = tp.createGraphics(width, height,
					new DefaultFontMapper());
			Rectangle2D r2d = new Rectangle2D.Double(0, 0, width, height);
			chart.draw(g2d, r2d);
			g2d.dispose();
			cb.addTemplate(tp, 0, 0);
		} catch (DocumentException de) {
			de.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// step 5
		document.close();
	}

	/**
	 * Gets an example barchart.
	 * 
	 * @return a barchart
	 */
	public static JFreeChart getBarChart() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.setValue(57, "students", "Asia");
		dataset.setValue(36, "students", "Africa");
		dataset.setValue(29, "students", "S-America");
		dataset.setValue(17, "students", "N-America");
		dataset.setValue(12, "students", "Australia");
		return ChartFactory.createBarChart("T.U.F. Students", "continent",
				"number of students", dataset, PlotOrientation.VERTICAL, false,
				true, false);
	}

	/**
	 * Gets an example piechart.
	 * 
	 * @return a piechart
	 */
	public static JFreeChart getPieChart() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Europe", 302);
		dataset.setValue("Asia", 57);
		dataset.setValue("Africa", 17);
		dataset.setValue("S-America", 29);
		dataset.setValue("N-America", 17);
		dataset.setValue("Australia", 12);
		return ChartFactory.createPieChart("Students per continent", dataset,
				true, true, false);
	}
}