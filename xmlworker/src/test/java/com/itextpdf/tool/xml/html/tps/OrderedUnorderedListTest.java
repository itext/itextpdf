/**
 *
 */
package com.itextpdf.tool.xml.html.tps;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.ParagraphCssApplier;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.OrderedUnorderedList;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

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
	private List<Element> listWithOne;
	private List<Element> listWithTwo;
	private final ListItem single = new ListItem("Single");
	private final ListItem start = new ListItem("Start");
	private final ListItem end = new ListItem("End");
	private OrderedUnorderedList orderedUnorderedList;
	private WorkerContextImpl workerContextImpl;

	@Before
	public void before() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		root = new Tag("body");
		p = new Tag("p");
		ul = new Tag("ul");
		first = new Tag("li");
		last = new Tag("li");

		listWithOne = new ArrayList<Element>();
		listWithTwo = new ArrayList<Element>();
		orderedUnorderedList = new OrderedUnorderedList();
		CssAppliersImpl cssAppliers = new CssAppliersImpl();
		orderedUnorderedList.setCssAppliers(cssAppliers);
		workerContextImpl = new WorkerContextImpl();
		HtmlPipelineContext context2 = new HtmlPipelineContext(cssAppliers);
		workerContextImpl.put(HtmlPipeline.class.getName(), context2);
		root.addChild(p);
		root.addChild(ul);
		ul.addChild(first);
		ul.addChild(last);
		p.getCSS().put("font-size", "12pt");
        p.getCSS().put("margin-top", "12pt");
        p.getCSS().put("margin-bottom", "12pt");
		new ParagraphCssApplier(cssAppliers).apply(new Paragraph("paragraph"), p, context2);
		first.getCSS().put("margin-top", "50pt");
		first.getCSS().put("padding-top", "25pt");
		first.getCSS().put("margin-bottom", "50pt");
		first.getCSS().put("padding-bottom", "25pt");
		last.getCSS().put("margin-bottom", "50pt");
		last.getCSS().put("padding-bottom", "25pt");
		listWithOne.add(single);
		listWithTwo.add(start);
		listWithTwo.add(end);
	}
	@Test
	public void listWithOneNoListMarginAndPadding() {
		com.itextpdf.text.List endList = (com.itextpdf.text.List)orderedUnorderedList.end(workerContextImpl, ul, listWithOne).get(0);
		assertEquals(50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
		assertEquals(50f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}

	@Test
	public void listWithOneNoListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithOne).get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void listWithOneNoListPaddingTop2() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithOne).get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	@Test
	public void listWithOneWithListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List)orderedUnorderedList.end(workerContextImpl, ul, listWithOne).get(0);
		assertEquals(100f + 25f + 50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}
	/**
	 * Verifies if the largest of the margin-bottom's of the ul and it's only li is selected and added to the padding-bottom of li, because padding-bottom of ul == null.
	 */
	@Test
	public void listWithOneNoListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithOne).get(0);
		assertEquals(50f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}
	/**
	 * Verifies if the largest of the margin-bottom's of the ul and it's only li is selected and added to the padding-bottom of li, because padding-bottom of ul == 0.
	 */
	@Test
	public void listWithOneNoListPaddingBottom2() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithOne).get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}
	/**
	 * Verifies if margin-bottom and padding-bottom of both the ul and it's only li are added up, because padding-bottom of ul != 0.
	 */
	@Test
	public void listWithOneWithListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithOne).get(0);
		assertEquals(100f + 25f + 50f + 25f, ((ListItem) endList.getItems().get(0)).getSpacingAfter(), 0f);
	}
	/**
	 * In this test the ul tag does not contain any margin or padding settings.
	 * Verifies if the margin- and padding-top of the first li and the margin- and padding-bottom of the last are set.
	 */
	@Test
	public void listWithTwoNoListMarginAndPadding() {
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithTwo).get(0);
		assertEquals(50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
		assertEquals(50f + 25f, ((ListItem) endList.getItems().get(1)).getSpacingAfter(), 0f);
	}
	/**
	 * Verifies if the largest of the margin-top's of the ul and it's first li is selected and added to the padding-top of li, because padding-top of ul == null.
	 */
	@Test
	public void listWithTwoNoListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List)orderedUnorderedList.end(workerContextImpl, ul, listWithTwo).get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}
	/**
	 * Verifies if the largest of the margin-top's of the ul and it's first li is selected and added to the padding-top of li, because padding-top of ul == 0.
	 */
	@Test
	public void listWithTwoNoListPaddingTop2() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithTwo).get(0);
		assertEquals(100f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}
	/**
	 * Verifies if margin-top and padding-top of both the ul and it's first li are added up, because padding-top of ul != 0.
	 */
	@Test
	public void listWithTwoWithListPaddingTop() {
		ul.getCSS().put("margin-top", "100pt");
		ul.getCSS().put("padding-top", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List)orderedUnorderedList.end(workerContextImpl, ul, listWithTwo).get(0);
		assertEquals(100f + 25f + 50f + 25f - 12f, ((ListItem) endList.getItems().get(0)).getSpacingBefore(), 0f);
	}

	/**
	 * Verifies if the largest of the margin-bottom's of the ul and it's last li is selected and added to the padding-bottom of li, because padding-bottom of ul == null.
	 */
	@Test
	public void listWithTwoNoListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithTwo).get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(1)).getSpacingAfter(), 0f);
	}
	/**
	 * Verifies if the largest of the margin-bottom's of the ul and it's last li is selected and added to the padding-bottom of li, because padding-bottom of ul == 0.
	 */
	@Test
	public void listWithTwoNoListPaddingBottom2() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "0pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithTwo).get(0);
		assertEquals(100f + 25f, ((ListItem) endList.getItems().get(1)).getSpacingAfter(), 0f);
	}

	/**
	 * Verifies if margin-bottom and padding-bottom of both the ul and it's last li are added up, because padding-bottom of ul != 0.
	 */
	@Test
	public void listWithTwoWithListPaddingBottom() {
		ul.getCSS().put("margin-bottom", "100pt");
		ul.getCSS().put("padding-bottom", "25pt");
		com.itextpdf.text.List endList = (com.itextpdf.text.List) orderedUnorderedList.end(workerContextImpl, ul, listWithTwo).get(0);
		assertEquals(100f + 25f + 50f + 25f, ((ListItem) endList.getItems().get(1)).getSpacingAfter(), 0f);
	}

	/**
	 * Verifies if {@link OrderedUnorderedList} is a stack owner. Should be true.
	 */
	@Test
	public void verifyIfStackOwner() {
		Assert.assertTrue(orderedUnorderedList.isStackOwner());
	}
}
