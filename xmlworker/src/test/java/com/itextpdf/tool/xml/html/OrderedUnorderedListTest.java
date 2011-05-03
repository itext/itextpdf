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
import com.itextpdf.tool.xml.css.apply.ParagraphCssApplier;

/**
 * @author Emiel Ackermann
 *
 */
public class OrderedUnorderedListTest {
	private final Tag root = new Tag("body");
	private final Tag p = new Tag("p");
	private final Tag ul = new Tag("ul");
	private final Tag li = new Tag("li");

	private final List<Element> currentContent = new ArrayList<Element>();
	private final ListItem listItem = new ListItem("ListItem");
	private final OrderedUnorderedList orderedUnorderedList = new OrderedUnorderedList();

	@Before
	public void before(){
		root.addChild(p);
		p.setParent(root);
		root.addChild(ul);
		ul.setParent(root);
		ul.addChild(li);
		li.setParent(ul);
		p.getCSS().put("font-size", "12pt");
		new ParagraphCssApplier(null).apply(new Paragraph("paragraph"), p);
		li.getCSS().put("margin-top", "50pt");
		li.getCSS().put("padding-top", "25pt");
		li.getCSS().put("margin-bottom", "50pt");
		li.getCSS().put("padding-bottom", "25pt");
		li.getCSS().put("font-size", "12pt");
		new ParagraphCssApplier(null).apply(listItem, li);
		currentContent.add(listItem);
	}

	@Test
	public void testNoListMarginAndPadding(){
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(50f+25f-12f,((ListItem)endList.getItems().get(0)).getSpacingBefore(),0f);
		assertEquals(50f+25f,((ListItem)endList.getItems().get(0)).getSpacingAfter(),0f);
	}

	@Test
	public void testNoListPaddingTop(){
		ul.getCSS().put("margin-top", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f+25f-12f,((ListItem)endList.getItems().get(0)).getSpacingBefore(),0f);
	}

	@Test
	public void testNoListPaddingTop2(){
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f+25f-12f,((ListItem)endList.getItems().get(0)).getSpacingBefore(),0f);
	}

	@Test
	public void testWithListPaddingTop(){
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f+25f+50f+25f-12f,((ListItem)endList.getItems().get(0)).getSpacingBefore(),0f);
	}

	@Test
	public void testNoListPaddingBottom(){
		ul.getCSS().put("margin-bottom", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f+25f,((ListItem)endList.getItems().get(0)).getSpacingAfter(),0f);
	}

	@Test
	public void testNoListPaddingBottom2(){
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f+25f,((ListItem)endList.getItems().get(0)).getSpacingAfter(),0f);
	}

	@Test
	public void testWithListPaddingBottom(){
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(ul, currentContent).get(0);
		assertEquals(100f+25f+50f+25f,((ListItem)endList.getItems().get(0)).getSpacingAfter(),0f);
	}
}
