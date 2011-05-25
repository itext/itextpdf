/**
 *
 */
package com.itextpdf.tool.xml.css;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;

/**
 * @author Emiel Ackermann
 *
 */
public class FontSizeTranslatorTest {

	private final FontSizeTranslator fst = FontSizeTranslator.getInstance();
	Tag p = new Tag("p", null);
	Tag span = new Tag("span", null);

	@Before
	public void before() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		p.addChild(span);
		span.setParent(p);
	}

	@Test
	public void resolveXXAndXSmall() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.XX_SMALL);
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(6.75f, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.X_SMALL);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(7.5f, c2.getFont().getSize(), 0);
	}

	@Test
	public void resolveSmallAndMedium() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.SMALL);
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(9.75f, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.MEDIUM);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(12f, c2.getFont().getSize(), 0);
	}

	@Test
	public void resolveLargeXLargeXXLarge() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.LARGE);
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(13.5f, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.X_LARGE);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(18f, c2.getFont().getSize(), 0);

		p.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.XX_LARGE);
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c3 = new ChunkCssApplier().apply(new Chunk("Text after span."), p);
		assertEquals(24f, c3.getFont().getSize(), 0);
	}

	@Test
	public void resolveDefaultToSmaller() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(12, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.SMALLER);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(9.75f, c2.getFont().getSize(), 0);
	}

	@Test
	public void resolveDefaultToLarger() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(12, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.LARGER);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(13.5f, c2.getFont().getSize(), 0);
	}

	@Test
	public void resolve15ToSmaller() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, "15pt");
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(15, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.SMALLER);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(15*0.85f, c2.getFont().getSize(), 0);
	}

	@Test
	public void resolve15ToLarger() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, "15pt");
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(15, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.LARGER);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(15*1.5f, c2.getFont().getSize(), 0);
	}

	@Test
	public void resolve34ToSmaller() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, "34pt");
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(34, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.SMALLER);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(34*2/3f, c2.getFont().getSize(), 0);
	}

	@Test
	public void resolve34ToLarger() throws IOException {
		p.getCSS().put(CSS.Property.FONT_SIZE, "34pt");
		p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p)+"pt");
		Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
		assertEquals(34, c1.getFont().getSize(), 0);

		span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.LARGER);
		span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span)+"pt");
		Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
		assertEquals(34*1.5f, c2.getFont().getSize(), 0);
	}
}
