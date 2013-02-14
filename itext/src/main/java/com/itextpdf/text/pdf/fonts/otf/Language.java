package com.itextpdf.text.pdf.fonts.otf;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
public enum Language {
	
	BENGALI("beng", "bng2");
	
	private final List<String> codes;
	
	private Language(String... codes) {
		this.codes = Arrays.asList(codes);
	}
	
	public boolean isSupported(String languageCode) {
		return codes.contains(languageCode);
	}
}
