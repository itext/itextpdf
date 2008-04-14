/* in_action/chapter12/JTextPaneToPdf.java */

package in_action.chapter12;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.FileOutputStream;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bill Ensley and adapter by Bruno Lowagie.
 * It is part of the book 'iText in Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class JTextPaneToPdf {

	int inch = Toolkit.getDefaultToolkit().getScreenResolution();

	float pixelToPoint = (float) 72 / (float) inch;

	JTextPane textPane;

	/**
	 * Constructs a JTextPaneToPdf object. This opens a frame that can be used
	 * as text editor.
	 */
	public JTextPaneToPdf() {
		JFrame frame = new JFrame();
		textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);

		JPanel north = new JPanel();
		JButton print = new JButton("Print");
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				paintToPDF(textPane);
			}
		});
		JMenuBar menu = new JMenuBar();
		JMenu styleMenu = new JMenu();
		styleMenu.setText("Style");

		Action boldAction = new BoldAction();
		boldAction.putValue(Action.NAME, "Bold");
		styleMenu.add(boldAction);

		Action italicAction = new ItalicAction();
		italicAction.putValue(Action.NAME, "Italic");
		styleMenu.add(italicAction);

		Action foregroundAction = new ForegroundAction();
		foregroundAction.putValue(Action.NAME, "Color");
		styleMenu.add(foregroundAction);

		Action formatTextAction = new FontAndSizeAction();
		formatTextAction.putValue(Action.NAME, "Font and Size");
		styleMenu.add(formatTextAction);

		menu.add(styleMenu);

		north.add(menu);
		north.add(print);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(north, BorderLayout.NORTH);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.setSize(800, 500);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}

	/**
	 * Renders a JTextPane to PDF.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: JTextPaneToPdf");
		System.out.println("-> Renders a JTextPane to a PDF file");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> file generated: editor.pdf");
		new JTextPaneToPdf();
	}

	public void paintToPDF(JTextPane ta) {
		try {
			ta.setBounds(0, 0, (int) convertToPixels(612 - 58),
					(int) convertToPixels(792 - 60));

			Document document = new Document();
			FileOutputStream fos = new FileOutputStream("results/in_action/chapter12/editor.pdf");
			PdfWriter writer = PdfWriter.getInstance(document, fos);

			document.setPageSize(new com.lowagie.text.Rectangle(612, 792));
			document.open();
			PdfContentByte cb = writer.getDirectContent();

			cb.saveState();
			cb.concatCTM(1, 0, 0, 1, 0, 0);

			DefaultFontMapper mapper = new DefaultFontMapper();
			mapper.insertDirectory("c:/windows/fonts");

			Graphics2D g2 = cb.createGraphics(612, 792, mapper, true, .95f);

			AffineTransform at = new AffineTransform();
			at.translate(convertToPixels(20), convertToPixels(20));
			at.scale(pixelToPoint, pixelToPoint);

			g2.transform(at);

			g2.setColor(Color.WHITE);
			g2.fill(ta.getBounds());

			Rectangle alloc = getVisibleEditorRect(ta);
			ta.getUI().getRootView(ta).paint(g2, alloc);

			g2.setColor(Color.BLACK);
			g2.draw(ta.getBounds());

			g2.dispose();
			cb.restoreState();
			document.close();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public float convertToPoints(int pixels) {
		return (float) (pixels * pixelToPoint);
	}

	public float convertToPixels(int points) {
		return (float) (points / pixelToPoint);
	}

	protected Rectangle getVisibleEditorRect(JTextPane ta) {
		Rectangle alloc = ta.getBounds();
		if ((alloc.width > 0) && (alloc.height > 0)) {
			alloc.x = alloc.y = 0;
			Insets insets = ta.getInsets();
			alloc.x += insets.left;
			alloc.y += insets.top;
			alloc.width -= insets.left + insets.right;
			alloc.height -= insets.top + insets.bottom;
			return alloc;
		}
		return null;
	}

	public class BoldAction extends StyledEditorKit.StyledTextAction {
		private static final long serialVersionUID = 9174670038684056758L;

		/**
		 * Constructs a new BoldAction.
		 */
		public BoldAction() {
			super("font-bold");
		}

		public String toString() {
			return "Bold";
		}

		/**
		 * Toggles the bold attribute.
		 * 
		 * @param e
		 *            the action event
		 */
		public void actionPerformed(ActionEvent e) {
			JEditorPane editor = getEditor(e);
			if (editor != null) {
				StyledEditorKit kit = getStyledEditorKit(editor);
				MutableAttributeSet attr = kit.getInputAttributes();
				boolean bold = (StyleConstants.isBold(attr)) ? false : true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setBold(sas, bold);
				setCharacterAttributes(editor, sas, false);

			}
		}
	}

	class ItalicAction extends StyledEditorKit.StyledTextAction {

		private static final long serialVersionUID = -1428340091100055456L;

		/**
		 * Constructs a new ItalicAction.
		 */
		public ItalicAction() {
			super("font-italic");
		}

		public String toString() {
			return "Italic";
		}

		/**
		 * Toggles the italic attribute.
		 * 
		 * @param e
		 *            the action event
		 */
		public void actionPerformed(ActionEvent e) {
			JEditorPane editor = getEditor(e);
			if (editor != null) {
				StyledEditorKit kit = getStyledEditorKit(editor);
				MutableAttributeSet attr = kit.getInputAttributes();
				boolean italic = (StyleConstants.isItalic(attr)) ? false : true;
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setItalic(sas, italic);
				setCharacterAttributes(editor, sas, false);
			}
		}
	}

	public class ForegroundAction extends StyledEditorKit.StyledTextAction {

		private static final long serialVersionUID = 6384632651737400352L;

		JColorChooser colorChooser = new JColorChooser();

		JDialog dialog = new JDialog();

		boolean noChange = false;

		boolean cancelled = false;

		/**
		 * Creates a new ForegroundAction.
		 * 
		 * @param nm
		 *            the action name
		 * @param fg
		 *            the foreground color
		 */
		public ForegroundAction() {
			super("foreground");
			// this.fg = fg;

		}

		/**
		 * Sets the foreground color.
		 * 
		 * @param e
		 *            the action event
		 */
		public void actionPerformed(ActionEvent e) {
			JTextPane editor = (JTextPane) getEditor(e);

			if (editor == null) {
				JOptionPane
						.showMessageDialog(
								null,
								"You need to select the editor pane before you can change the color.",
								"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int p0 = editor.getSelectionStart();
			StyledDocument doc = getStyledDocument(editor);
			Element paragraph = doc.getCharacterElement(p0);
			AttributeSet as = paragraph.getAttributes();
			fg = StyleConstants.getForeground(as);
			if (fg == null) {
				fg = Color.BLACK;
			}
			colorChooser.setColor(fg);

			JButton accept = new JButton("OK");
			accept.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					fg = colorChooser.getColor();
					dialog.dispose();
				}
			});

			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					cancelled = true;
					dialog.dispose();
				}
			});

			JButton none = new JButton("None");
			none.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					noChange = true;
					dialog.dispose();
				}
			});

			JPanel buttons = new JPanel();
			buttons.add(accept);
			buttons.add(none);
			buttons.add(cancel);

			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(colorChooser, BorderLayout.CENTER);
			dialog.getContentPane().add(buttons, BorderLayout.SOUTH);
			dialog.setModal(true);
			dialog.pack();
			dialog.setVisible(true);

			if (!cancelled) {

				MutableAttributeSet attr = null;
				if (editor != null) {
					if (fg != null && !noChange) {
						attr = new SimpleAttributeSet();
						StyleConstants.setForeground(attr, fg);
						setCharacterAttributes(editor, attr, false);
					}
				}
			}// end if color != null
			noChange = false;
			cancelled = false;
		}

		private Color fg;
	}

	public class FontAndSizeAction extends StyledEditorKit.StyledTextAction {

		private static final long serialVersionUID = 584531387732416339L;

		private String family;

		private float fontSize;

		JDialog formatText;

		private boolean accept = false;

		JComboBox fontFamilyChooser;

		JComboBox fontSizeChooser;

		/**
		 * Creates a new FontAndSizeAction.
		 * 
		 * @param nm
		 *            the action name
		 * @param family
		 *            the font family
		 */
		public FontAndSizeAction() {
			super("Font and Size");
		}

		public String toString() {
			return "Font and Size";
		}

		/**
		 * Sets the font family.
		 * 
		 * @param e
		 *            the event
		 */
		public void actionPerformed(ActionEvent e) {
			JTextPane editor = (JTextPane) getEditor(e);
			if (editor == null) {
				JOptionPane
						.showMessageDialog(
								null,
								"You need to select the editor pane before you can change the font and size.",
								"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int p0 = editor.getSelectionStart();
			StyledDocument doc = getStyledDocument(editor);
			Element paragraph = doc.getCharacterElement(p0);
			AttributeSet as = paragraph.getAttributes();

			// init item selections
			family = StyleConstants.getFontFamily(as);
			fontSize = StyleConstants.getFontSize(as);

			formatText = new JDialog(new JFrame(), "Font and Size", true);
			formatText.getContentPane().setLayout(new BorderLayout());
			// formatText.setSize(250,100);

			JPanel choosers = new JPanel();
			choosers.setLayout(new GridLayout(2, 1));

			JPanel fontFamilyPanel = new JPanel();
			fontFamilyPanel.add(new JLabel("Font"));

			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			String[] fontNames = ge.getAvailableFontFamilyNames();

			fontFamilyChooser = new JComboBox();
			for (int i = 0; i < fontNames.length; i++) {
				fontFamilyChooser.addItem(fontNames[i]);
			}
			fontFamilyChooser.setSelectedItem(family);
			fontFamilyPanel.add(fontFamilyChooser);
			choosers.add(fontFamilyPanel);

			JPanel fontSizePanel = new JPanel();
			fontSizePanel.add(new JLabel("Size"));
			fontSizeChooser = new JComboBox();
			fontSizeChooser.setEditable(true);
			fontSizeChooser.addItem(new Float(4));
			fontSizeChooser.addItem(new Float(8));
			fontSizeChooser.addItem(new Float(12));
			fontSizeChooser.addItem(new Float(16));
			fontSizeChooser.addItem(new Float(20));
			fontSizeChooser.addItem(new Float(24));
			fontSizeChooser.setSelectedItem(new Float(fontSize));
			fontSizePanel.add(fontSizeChooser);
			choosers.add(fontSizePanel);

			JButton ok = new JButton("OK");
			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					accept = true;
					formatText.dispose();
					family = (String) fontFamilyChooser.getSelectedItem();
					fontSize = Float.parseFloat(fontSizeChooser
							.getSelectedItem().toString());
				}
			});

			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					formatText.dispose();
				}
			});

			JPanel buttons = new JPanel();
			buttons.add(ok);
			buttons.add(cancel);
			formatText.getContentPane().add(choosers, BorderLayout.CENTER);
			formatText.getContentPane().add(buttons, BorderLayout.SOUTH);
			formatText.pack();
			formatText.setVisible(true);

			MutableAttributeSet attr = null;
			if (editor != null && accept) {
				attr = new SimpleAttributeSet();
				StyleConstants.setFontFamily(attr, family);
				StyleConstants.setFontSize(attr, (int) fontSize);
				setCharacterAttributes(editor, attr, false);
			}

		}

	}

}