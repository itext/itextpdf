/* in_action/chapter12/MyJTable.java */

package in_action.chapter12;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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

public class MyJTable extends JFrame {
	private static final long serialVersionUID = 3314147924092290633L;
	/** The JTable we will show in a Swing app and print to PDF. */
	private JTable table;

	/**
	 * Generates a PDF file with a table.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: example MyJTable");
		System.out.println("-> Opens a JFrame with a JTable;");
		System.out.println("   Click on Create PDF to create a Pdf file.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDFs: my_jtable_shapes.pdf");
		System.out.println("                   my_jtable_fonts.pdf");
		MyJTable frame = new MyJTable();
		frame.pack();
		frame.setVisible(true);
	}

	/** Constructs a frame with a JTable. */
	public MyJTable() {
		getContentPane().setLayout(new BorderLayout());
		setTitle("JTable test");
		createToolbar();
		createTable();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	/**
	 * Create a table with some dummy data
	 */
	private void createTable() {
		Object[][] data = {
				{ "Mary", "Campione", "Snowboarding", new Integer(5),
						new Boolean(false) },
				{ "Alison", "Huml", "Rowing", new Integer(3), new Boolean(true) },
				{ "Kathy", "Walrath", "Chasing toddlers", new Integer(2),
						new Boolean(false) },
				{ "Mark", "Andrews", "Speed reading", new Integer(20),
						new Boolean(true) },
				{ "Angela", "Lih", "Teaching high school", new Integer(4),
						new Boolean(false) } };

		String[] columnNames = { "First Name", "Last Name", "Sport",
				"# of Years", "Vegetarian" };

		table = new JTable(data, columnNames);

		// Use a panel to contains the table and add it the frame
		JPanel tPanel = new JPanel(new BorderLayout());
		tPanel.add(table.getTableHeader(), BorderLayout.NORTH);
		tPanel.add(table, BorderLayout.CENTER);

		getContentPane().add(tPanel, BorderLayout.CENTER);
	}

	/**
	 * Toolbar for print and exit
	 */
	private void createToolbar() {
		JToolBar tb = new JToolBar();

		JButton createBtn1 = new JButton("Create PDF (shapes)");
		createBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createPdf(true);
			}
		});
		JButton createBtn2 = new JButton("Create PDF (fonts)");
		createBtn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createPdf(false);
			}
		});

		JButton exitBtn = new JButton("Exit");
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});

		tb.add(createBtn1);
		tb.add(createBtn2);
		tb.add(exitBtn);

		getContentPane().add(tb, BorderLayout.NORTH);
	}

	/**
	 * Exit app
	 */
	private void exit() {
		System.exit(0);
	}

	/**
	 * Creates the actual PDF document
	 */
	public void createPdf(boolean shapes) {
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter writer;
			if (shapes)
				writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter12/my_jtable_shapes.pdf"));
			else
				writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter12/my_jtable_fonts.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a table to the document
			PdfContentByte cb = writer.getDirectContent();

			// Create the graphics as shapes
			PdfTemplate tp = cb.createTemplate(500, 500);
			Graphics2D g2;
			if (shapes)
				g2 = tp.createGraphicsShapes(500, 500);
			else
				g2 = tp.createGraphics(500, 500);
			table.print(g2);
			g2.dispose();
			cb.addTemplate(tp, 30, 300);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}