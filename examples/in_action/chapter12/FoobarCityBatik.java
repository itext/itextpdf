/* in_action/chapter12/FoobarCityBatik.java */
package in_action.chapter12;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfLayer;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoobarCityBatik {

	/** The document to which the SVG is written. */
	protected Document document = new Document();

	/** The writer that generates the PDF. */
	protected PdfWriter writer;

	/**
	 * Generates a PDF showing the Map of a City.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 12: City of Foobar");
		System.out.println("-> Creates a Map based on an SVG image.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("                batik-awt-util.jar");
		System.out.println("                batik-bridge.jar");
		System.out.println("                batik-css.jar");
		System.out.println("                batik-util.jar");
		System.out.println("                batik-ext.jar");
		System.out.println("                batik-gvt.jar");
		System.out.println("                batik-dom.jar");
		System.out.println("                batik-parser.jar");
		System.out.println("                batik-script.jar");
		System.out.println("                batik-svg-dom.jar");
		System.out.println("                batik-xml.jar");
		System.out.println("-> external resources needed: foobarcity.svg");
		System.out.println("       streets.svg, rues.svg and straten.svg");
		System.out.println("       webdings.ttf and map.jpg");
		System.out.println("-> resulting PDF: interactive_city.pdf");
		try {
			Document document = new Document(new Rectangle(6000, 6000));
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter12/interactive_city.pdf"));
			writer.setViewerPreferences(PdfWriter.PageModeUseOC
					| PdfWriter.FitWindow);
			writer.setPdfVersion(PdfWriter.VERSION_1_5);
			document.open();

			PdfLayer imageLayer = new PdfLayer("Map of Foobar", writer);
			imageLayer.setZoom(-1, 0.2f);
			imageLayer.setOnPanel(false);
			PdfLayer vectorLayer = new PdfLayer("Vector", writer);
			vectorLayer.setZoom(0.2f, -1);
			vectorLayer.setOnPanel(false);
			PdfLayer gridLayer = new PdfLayer("Grid", writer);
			gridLayer.setZoom(0.2f, 1);
			gridLayer.setOnPanel(false);
			PdfLayer streetlayer = PdfLayer.createTitle(
					"Streets / Rues / Straten", writer);
			PdfLayer streetlayer_en = new PdfLayer("English", writer);
			streetlayer_en.setOn(true);
			streetlayer_en.setLanguage("en", true);
			PdfLayer streetlayer_fr = new PdfLayer("Français", writer);
			streetlayer_fr.setOn(false);
			streetlayer_fr.setLanguage("fr", false);
			PdfLayer streetlayer_nl = new PdfLayer("Nederlands", writer);
			streetlayer_nl.setOn(false);
			streetlayer_nl.setLanguage("nl", false);
			streetlayer.addChild(streetlayer_en);
			streetlayer.addChild(streetlayer_fr);
			streetlayer.addChild(streetlayer_nl);
			ArrayList radio = new ArrayList();
			radio.add(streetlayer_en);
			radio.add(streetlayer_fr);
			radio.add(streetlayer_nl);
			writer.addOCGRadioGroup(radio);

			String parser = XMLResourceDescriptor.getXMLParserClassName();
			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
			SVGDocument city = factory.createSVGDocument(new File(
					"resources/in_action/chapter10/foobarcity.svg").toURL()
					.toString());
			SVGDocument streets = factory
					.createSVGDocument(new File(
							"resources/in_action/chapter11/streets.svg").toURL()
							.toString());
			SVGDocument rues = factory.createSVGDocument(new File(
					"resources/in_action/chapter12/rues.svg").toURL().toString());
			SVGDocument straten = factory
					.createSVGDocument(new File(
							"resources/in_action/chapter12/straten.svg").toURL()
							.toString());
			UserAgent userAgent = new UserAgentAdapter();
			DocumentLoader loader = new DocumentLoader(userAgent);
			BridgeContext ctx = new BridgeContext(userAgent, loader);
			GVTBuilder builder = new GVTBuilder();
			ctx.setDynamicState(BridgeContext.DYNAMIC);

			PdfContentByte cb = writer.getDirectContent();
			Graphics2D g2d;
			PdfTemplate map = cb.createTemplate(6000, 6000);
			g2d = map.createGraphics(6000, 6000, new DefaultFontMapper());
			GraphicsNode mapGraphics = builder.build(ctx, city);
			mapGraphics.paint(g2d);
			g2d.dispose();
			cb.beginLayer(vectorLayer);
			cb.addTemplate(map, 0, 0);
			cb.endLayer();

			PdfTemplate streets_en = cb.createTemplate(6000, 6000);
			g2d = streets_en
					.createGraphics(6000, 6000, new DefaultFontMapper());
			GraphicsNode streetGraphicsEn = builder.build(ctx, streets);
			streetGraphicsEn.paint(g2d);
			g2d.dispose();
			streets_en.setLayer(streetlayer_en);
			cb.addTemplate(streets_en, 0, 0);

			PdfTemplate streets_fr = cb.createTemplate(6000, 6000);
			g2d = streets_fr
					.createGraphics(6000, 6000, new DefaultFontMapper());
			GraphicsNode streetGraphicsFr = builder.build(ctx, rues);
			streetGraphicsFr.paint(g2d);
			g2d.dispose();
			streets_fr.setLayer(streetlayer_fr);
			cb.addTemplate(streets_fr, 0, 0);

			PdfTemplate streets_nl = cb.createTemplate(6000, 6000);
			g2d = streets_nl
					.createGraphics(6000, 6000, new DefaultFontMapper());
			GraphicsNode streetGraphicsNl = builder.build(ctx, straten);
			streetGraphicsNl.paint(g2d);
			g2d.dispose();
			streets_nl.setLayer(streetlayer_nl);
			cb.addTemplate(streets_nl, 0, 0);

			Image image = Image.getInstance("resources/in_action/chapter12/map.jpg");
			image.scalePercent(240);
			image.setAbsolutePosition(450, 1400);
			image.setLayer(imageLayer);
			cb.addImage(image);

			cb.saveState();
			cb.beginLayer(gridLayer);
			cb.setGrayStroke(0.7f);
			cb.setLineWidth(2);
			for (int i = 0; i < 8; i++) {
				cb.moveTo(1250, 1500 + i * 500);
				cb.lineTo(4750, 1500 + i * 500);
			}
			for (int i = 0; i < 8; i++) {
				cb.moveTo(1250 + i * 500, 1500);
				cb.lineTo(1250 + i * 500, 5000);
			}
			cb.stroke();
			cb.endLayer();
			cb.restoreState();

			PdfLayer cityInfoLayer = new PdfLayer("Foobar Info", writer);
			cityInfoLayer.setOn(false);
			PdfLayer hotelLayer = new PdfLayer("Hotel", writer);
			hotelLayer.setOn(false);
			cityInfoLayer.addChild(hotelLayer);
			PdfLayer parkingLayer = new PdfLayer("Parking", writer);
			parkingLayer.setOn(false);
			cityInfoLayer.addChild(parkingLayer);
			PdfLayer businessLayer = new PdfLayer("Industry", writer);
			businessLayer.setOn(false);
			cityInfoLayer.addChild(businessLayer);

			PdfLayer cultureLayer = PdfLayer.createTitle("Leisure and Culture",
					writer);
			PdfLayer goingoutLayer = new PdfLayer("Going out", writer);
			goingoutLayer.setOn(false);
			cultureLayer.addChild(goingoutLayer);
			PdfLayer restoLayer = new PdfLayer("Restaurants", writer);
			restoLayer.setOn(false);
			goingoutLayer.addChild(restoLayer);
			PdfLayer theatreLayer = new PdfLayer("(Movie) Theatres", writer);
			theatreLayer.setOn(false);
			goingoutLayer.addChild(theatreLayer);
			PdfLayer monumentLayer = new PdfLayer("Museums and Monuments",
					writer);
			monumentLayer.setOn(false);
			cultureLayer.addChild(monumentLayer);
			PdfLayer sportsLayer = new PdfLayer("Sports", writer);
			sportsLayer.setOn(false);
			cultureLayer.addChild(sportsLayer);

			BaseFont font = BaseFont.createFont(
					"c:/windows/fonts/webdings.ttf", BaseFont.WINANSI,
					BaseFont.EMBEDDED);
			cb.saveState();
			cb.beginText();
			cb.setRGBColorFill(0x00, 0x00, 0xFF);
			cb.setFontAndSize(font, 36);
			cb.beginLayer(cityInfoLayer);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x69), 2700, 3100, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x69), 3000, 2050, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x69), 3100, 2550, 0);
			cb.beginLayer(hotelLayer);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2000, 1900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2100, 1950, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2200, 2200, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2700, 3000, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2750, 3050, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2500, 3500, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2300, 2000, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 3250, 2200, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 3300, 2300, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 3400, 3050, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 3250, 3200, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2750, 3800, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2900, 3800, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 3000, 2400, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2000, 2800, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe3), 2600, 3200, 0);
			cb.endLayer(); // hotelLayer
			cb.beginLayer(parkingLayer);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe8), 2400, 2000, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe8), 2100, 2600, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe8), 3300, 2250, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe8), 3000, 3900, 0);
			cb.endLayer(); // parkingLayer
			cb.beginLayer(businessLayer);
			cb.setRGBColorFill(0xC0, 0xC0, 0xC0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3050, 3600, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3200, 3900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3150, 3700, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3260, 3610, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3350, 3750, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3500, 4000, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3500, 3800, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3450, 3700, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x46), 3450, 3600, 0);
			cb.endLayer(); // businessLayer
			cb.endLayer(); // cityInfoLayer
			cb.beginLayer(goingoutLayer);
			cb.beginLayer(restoLayer);
			cb.setRGBColorFill(0xFF, 0x14, 0x93);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 2650, 3500, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 2400, 1900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 2750, 3850, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 2700, 3200, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 2900, 3100, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 2850, 3000, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 2800, 2900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 2300, 2900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 1950, 2650, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 1800, 2750, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 3350, 3150, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 3400, 3100, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xe4), 3250, 3450, 0);
			cb.endLayer(); // restoLayer
			cb.beginLayer(theatreLayer);
			cb.setRGBColorFill(0xDC, 0x14, 0x3C);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xae), 2850, 3300, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xae), 3050, 2900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xae), 2650, 2900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xae), 2750, 2600, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xB8), 2800, 3350, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xB8), 2550, 2850, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xB8), 2850, 3300, 0);
			cb.endLayer(); // theatreLayer
			cb.endLayer(); // goingoutLayer
			cb.beginLayer(monumentLayer);
			cb.setRGBColorFill(0x00, 0x00, 0x00);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x47), 3250, 2750, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x47), 2750, 2900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x47), 2850, 3500, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xad), 2150, 3550, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xad), 3300, 2730, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xad), 2200, 2000, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xad), 2900, 3300, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0xad), 2080, 3000, 0);
			cb.endLayer(); // monumentLayer
			cb.beginLayer(sportsLayer);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x53), 2700, 4050, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x53), 2700, 3900, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x53), 2800, 3980, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x53), 1950, 2800, 0);
			cb.showTextAligned(PdfContentByte.ALIGN_CENTER, String
					.valueOf((char) 0x53), 3700, 2450, 0);
			cb.endLayer(); // sportsLayer
			cb.endText();
			cb.restoreState();

			document.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (DocumentException de) {
			de.printStackTrace();
		}
	}
}