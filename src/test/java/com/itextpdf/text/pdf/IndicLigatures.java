package com.itextpdf.text.pdf;

import junit.framework.Assert;

import org.junit.Test;

import com.itextpdf.text.pdf.indic.DevanagariLigaturizer;
import com.itextpdf.text.pdf.indic.GujaratiLigaturizer;
import com.itextpdf.text.pdf.indic.IndicLigaturizer;

public class IndicLigatures {

    @Test
    public void testDevanagari() throws Exception {
    	IndicLigaturizer d = new DevanagariLigaturizer();
    	String processed = d.process("\u0936\u093e\u0902\u0924\u094d\u093f");
    	Assert.assertEquals("\u0936\u093e\u0902\u093f\u0924", processed);
    }
    
    @Test
    public void testGujarati() throws Exception {
    	IndicLigaturizer g = new GujaratiLigaturizer();
    	String processed = g.process("\u0ab6\u0abe\u0a82\u0aa4\u0acd\u0abf");
    	Assert.assertEquals("\u0ab6\u0abe\u0a82\u0abf\u0aa4", processed);
    }
}
