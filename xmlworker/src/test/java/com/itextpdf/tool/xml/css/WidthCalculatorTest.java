/**
 *
 */
package com.itextpdf.tool.xml.css;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.XMLWorkerConfigurationImpl;
import com.itextpdf.tool.xml.html.HTML;

/**
 * @author Emiel Ackermann
 *
 */
public class WidthCalculatorTest {

	Tag body = new Tag("body", new HashMap<String,String>());
	Tag table = new Tag("table", new HashMap<String,String>());
	Tag row = new Tag("tr", new HashMap<String,String>());
	Tag cell = new Tag("td", new HashMap<String,String>());
	private final XMLWorkerConfig config = new XMLWorkerConfigurationImpl();
	private final WidthCalculator calc = new WidthCalculator();

	@Before
	public void before() {
		body.addChild(table);
		table.setParent(body);
		table.addChild(row);
		row.setParent(table);
		row.addChild(cell);
		cell.setParent(row);
	}

	@Test
	public void resolveTableWidth80() throws IOException {
		table.getAttributes().put(HTML.Attribute.WIDTH, "80%");
		assertEquals(0.8*config.getPageSize().getWidth(), calc.getWidth(table, config), 0);
	}
	@Test
	public void resolveCellWidth20of100() throws IOException {
		cell.getAttributes().put(HTML.Attribute.WIDTH, "20%");
		assertEquals(config.getPageSize().getWidth()*0.2f, calc.getWidth(cell, config),0.01f);
	}
	@Test
	public void resolveCellWidth20of80() throws IOException {
		table.getAttributes().put(HTML.Attribute.WIDTH, "80%");
		cell.getAttributes().put(HTML.Attribute.WIDTH, "20%");
		assertEquals(0.8f*config.getPageSize().getWidth()*0.2f, calc.getWidth(cell, config),0.01f);
	}
}
