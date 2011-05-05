/**
 *
 */
package com.itextpdf.tool.xml.html;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.XMLWorkerConfigurationImpl;
import com.itextpdf.tool.xml.css.apply.ParagraphCssApplier;

/**
 * @author Emiel Ackermann
 * 
 */
public class OrderedUnorderedListTest {

	private Tag root;
	private Tag p;
	private Tag ul;
	private Tag li;
	private List<Element> currentContent;
	private ListItem listItem;
	private XMLWorkerConfig config;
	private OrderedUnorderedList orderedUnorderedList;

	@Before
	public void before() {
		root = new Tag("body");
		p = new Tag("p");
		ul = new Tag("ul");
		li = new Tag("li");

		currentContent = new ArrayList<Element>();
		listItem = new ListItem("ListItem");
		config = new XMLWorkerConfigurationImpl();
		orderedUnorderedList = new OrderedUnorderedList();
		orderedUnorderedList.setConfiguration(config);
		root.addChild(p);
		root.addChild(ul);
		ul.addChild(li);
		p.getCSS().put("font-size", "12pt");
		new ParagraphCssApplier(config).apply(new Paragraph("paragraph"), p);
		li.getCSS().put("margin-top", "50pt");
		li.getCSS().put("padding-top", "25pt");
		li.getCSS().put("margin-bottom", "50pt");
		li.getCSS().put("padding-bottom", "25pt");
		li.getCSS().put("font-size", "12pt");
		new ParagraphCssApplier(config).apply(listItem, li);
		currentContent.add(listItem);
	}

	@Test
	public void testNoListMarginAndPadding() {
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
		assertEquals(50f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}

	@Test
	public void testNoListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void testNoListPaddingTop2() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void testWithListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f + 25f + 50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void testNoListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}

	@Test
	public void testNoListPaddingBottom2() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}

	@Test
	public void testWithListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f + 25f + 50f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}
}
