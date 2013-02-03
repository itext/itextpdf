package com.itextpdf.text.pdf.languages;

import java.util.List;

import com.itextpdf.text.pdf.Glyph;

/**
 * 
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
abstract class IndicGlyphRepositioner implements GlyphRepositioner {

	public void repositionGlyphs(List<Glyph> glyphList) {

		for (int i = 0; i < glyphList.size(); i++) {
			Glyph glyph = glyphList.get(i);
			Glyph nextGlyph = getNextGlyph(glyphList, i);

			if ((nextGlyph != null)
					&& getCharactersToBeShiftedLeftByOnePosition().contains(
							nextGlyph.chars)) {
				glyphList.set(i, nextGlyph);
				glyphList.set(i + 1, glyph);
				i++;
				continue;
			}
		}

	}

	abstract List<String> getCharactersToBeShiftedLeftByOnePosition();

	private Glyph getNextGlyph(List<Glyph> glyphs, int currentIndex) {
		if (currentIndex + 1 < glyphs.size()) {
			return glyphs.get(currentIndex + 1);
		} else {
			return null;
		}
	}

}
