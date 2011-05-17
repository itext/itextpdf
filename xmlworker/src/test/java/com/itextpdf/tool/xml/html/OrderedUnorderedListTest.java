/**
 *
 */
package com.itextpdf.tool.xml.html;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.XMLWorkerConfigurationImpl;
import com.itextpdf.tool.xml.css.apply.ParagraphCssApplier;
import com.itextpdf.tool.xml.pipeline.Writable;
import com.itextpdf.tool.xml.pipeline.WritableElement;

/**
 * @author Emiel Ackermann
 *
 */
public class OrderedUnorderedListTest {

	private Tag root;
	private Tag p;
	private Tag ul;
	private Tag first;
	private Tag last;
	private List<Writable> listWithOne;
	private List<Writable> listWithTwo;
	private final ListItem single = new ListItem("Single");
	private final ListItem start = new ListItem("Start");
	private final ListItem end = new ListItem("End");
	private XMLWorkerConfig config;
	private OrderedUnorderedList orderedUnorderedList;

	@Before
	public void before() {
		root = new Tag("body");
		p = new Tag("p");
		ul = new Tag("ul");
		first = new Tag("li");
		last = new Tag("li");

		listWithOne = new ArrayList<Writable>();
		listWithTwo = new ArrayList<Writable>();
		config = new XMLWorkerConfigurationImpl();
		orderedUnorderedList = new OrderedUnorderedList();
		orderedUnorderedList.setConfiguration(config);
		root.addChild(p);
		root.addChild(ul);
		ul.addChild(first);
		ul.addChild(last);
		p.getCSS().put("font-size", "12pt");
		new ParagraphCssApplier(config).apply(new Paragraph("paragraph"), p);
		first.getCSS().put("margin-top", "50pt");
		first.getCSS().put("padding-top", "25pt");
		first.getCSS().put("margin-bottom", "50pt");
		first.getCSS().put("padding-bottom", "25pt");
		last.getCSS().put("margin-bottom", "50pt");
		last.getCSS().put("padding-bottom", "25pt");
		listWithOne.add(new WritableElement(single));
		listWithTwo.add(new WritableElement(start));
		listWithTwo.add(new WritableElement(end));
	}
	@Test
	public void listWithOneNoListMarginAndPadding() {
		com.itextpdf.text.List endList = (com.itextpdf.text.List)( (WritableElement) orderedUnorderedList.end(ul, listWithOne).get(0)).elements().get(0);
		assertEquals(50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
		assertEquals(50f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}

	@Test
	public void listWithOneNoListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithOne).get(0)).elements().get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void listWithOneNoListPaddingTop2() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithOne).get(0)).elements().get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void listWithOneWithListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List)( (WritableElement) orderedUnorderedList.end(ul, listWithOne).get(0)).elements().get(0);
		assertEquals(100f + 25f + 50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void listWithOneNoListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithOne).get(0)).elements().get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}

	@Test
	public void listWithOneNoListPaddingBottom2() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithOne).get(0)).elements().get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}

	@Test
	public void listWithOneWithListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithOne).get(0)).elements().get(0);
		assertEquals(100f + 25f + 50f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}

	@Test
	public void listWithTwoNoListMarginAndPadding() {
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithTwo).get(0)).elements().get(0);
		assertEquals(50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
		assertEquals(50f + 25f, ((ListItem) endList.getItems().get(1)).getSpacingAfter(), 0f);
	}

	@Test
	public void listWithTwoNoListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithTwo).get(0)).elements().get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void listWithTwoNoListPaddingTop2() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithTwo).get(0)).elements().get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void listWithTwoWithListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithTwo).get(0)).elements().get(0);
		assertEquals(100f + 25f + 50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void listWithTwoNoListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithTwo).get(0)).elements().get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(1)).getSpacingAfter(), 0f);
	}

	@Test
	public void listWithTwoNoListPaddingBottom2() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithTwo).get(0)).elements().get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(1)).getSpacingAfter(), 0f);
	}

	@Test
	public void listWithTwoWithListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) ( (WritableElement) orderedUnorderedList.end(ul, listWithTwo).get(0)).elements().get(0);
		assertEquals(100f + 25f + 50f + 25f, ((ListItem) endList.getItems().get(1)).getSpacingAfter(), 0f);
	}
}
