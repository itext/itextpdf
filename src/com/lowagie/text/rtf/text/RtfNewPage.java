/*
 * Created on Aug 12, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.lowagie.text.rtf.text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.document.RtfDocument;


/**
 * The RtfNewPage creates a new page. INTERNAL CLASS
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfNewPage extends RtfElement {

    /**
     * Constant for a new page
     */
    private static final byte[] NEW_PAGE = "\\page".getBytes();
    
    /**
     * Constructs a RtfNewPage
     * 
     * @param doc The RtfDocument this RtfNewPage belongs to
     */
    public RtfNewPage(RtfDocument doc) {
        super(doc);
    }
    
    /**
     * Writes a new page
     * 
     * @return A byte array with the new page set
     */
    public byte[] write() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(NEW_PAGE);
            result.write(RtfParagraph.PARAGRAPH_DEFAULTS);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
}
