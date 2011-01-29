package com.itextpdf.text.html.simpleparser;

import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.DocumentException;

public interface TagProcessor {
	
	public abstract void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException, IOException;
	
	public abstract void endElement(HTMLWorker worker, String tag) throws DocumentException;
}
