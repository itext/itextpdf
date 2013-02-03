package com.itextpdf.text.pdf.languages;

import java.util.List;

import com.itextpdf.text.pdf.Glyph;

/**
 *  
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
public interface GlyphRepositioner {
	
	void repositionGlyphs(List<Glyph> glyphList);
	
}
