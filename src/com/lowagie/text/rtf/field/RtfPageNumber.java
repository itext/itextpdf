/*
 * Created on Aug 10, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.lowagie.text.rtf.field;

import java.io.IOException;

import com.lowagie.text.Font;
import com.lowagie.text.rtf.document.RtfDocument;


/**
 * The RtfPageNumber provides the page number field in rtf documents.
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author <a href="mailto:Steffen.Stundzig@smb-tec.com">Steffen.Stundzig@smb-tec.com</a>
 */
public class RtfPageNumber extends RtfField {

    /**
     * Constructs a RtfPageNumber. This can be added anywhere to add a page number field.
     */
    public RtfPageNumber() {
        super(null);
    }
    
    /**
     * Constructs a RtfPageNumber with a specified Font. This can be added anywhere to
     * add a page number field.
     * @param font
     */
    public RtfPageNumber(Font font) {
        super(null, font);
    }
    
    /**
     * Constructs a RtfPageNumber object.
     * 
     * @param doc The RtfDocument this RtfPageNumber belongs to
     */
    public RtfPageNumber(RtfDocument doc) {
        super(doc);
    }
    
    /**
     * Constructs a RtfPageNumber object with a specific font.
     * 
     * @param doc The RtfDocument this RtfPageNumber belongs to
     * @param font The Font to use
     */
    public RtfPageNumber(RtfDocument doc, Font font) {
        super(doc, font);
    }
    
    /**
     * Writes the field instruction content
     * 
     * @return A byte array containing "PAGE"
     * @throws IOException
     */
    protected byte[] writeFieldInstContent() throws IOException {
        return "PAGE".getBytes();
    }

    /**
     * Writes the field result content
     * 
     * @return An empty byte array
     * @throws IOException
     */
    protected byte[] writeFieldResultContent() throws IOException {
        return new byte[0];
    }
}
