/*
 * Created on Sep 22, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.lowagie.text.rtf.style;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.RtfExtendedElement;
import com.lowagie.text.rtf.document.RtfDocument;

/**
 * The RtfStylesheetList stores the RtfParagraphStyles that are used in the document.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfStylesheetList extends RtfElement implements RtfExtendedElement {

    /**
     * The HashMap containing the RtfParagraphStyles.
     */
    private HashMap styleMap = null;
    /**
     * Whether the default settings have been loaded.
     */
    private boolean defaultsLoaded = false;
    
    /**
     * Constructs a new RtfStylesheetList for the RtfDocument.
     * 
     * @param doc The RtfDocument this RtfStylesheetList belongs to.
     */
    public RtfStylesheetList(RtfDocument doc) {
        super(doc);
        this.styleMap = new HashMap();
    }

    /**
     * Register a RtfParagraphStyle with this RtfStylesheetList.
     * 
     * @param rtfParagraphStyle The RtfParagraphStyle to add.
     */
    public void registerParagraphStyle(RtfParagraphStyle rtfParagraphStyle) {
        RtfParagraphStyle tempStyle = new RtfParagraphStyle(this.document, rtfParagraphStyle);
        tempStyle.setStyleNumber(this.styleMap.size());
        tempStyle.handleInheritance();
        this.styleMap.put(tempStyle.getStyleName(), tempStyle);
    }

    /**
     * Registers all default styles. If styles with the given name have already been registered,
     * then they are NOT overwritten.
     */
    private void registerDefaultStyles() {
        defaultsLoaded = true;
        if(!this.styleMap.containsKey(RtfParagraphStyle.STYLE_NORMAL.getStyleName())) {
            registerParagraphStyle(RtfParagraphStyle.STYLE_NORMAL);
        }
        if(!this.styleMap.containsKey(RtfParagraphStyle.STYLE_HEADING_1.getStyleName())) {
            registerParagraphStyle(RtfParagraphStyle.STYLE_HEADING_1);
        }
        if(!this.styleMap.containsKey(RtfParagraphStyle.STYLE_HEADING_2.getStyleName())) {
            registerParagraphStyle(RtfParagraphStyle.STYLE_HEADING_2);
        }
        if(!this.styleMap.containsKey(RtfParagraphStyle.STYLE_HEADING_3.getStyleName())) {
            registerParagraphStyle(RtfParagraphStyle.STYLE_HEADING_3);
        }
    }

    /**
     * Gets the RtfParagraphStyle with the given name. Makes sure that the defaults
     * have been loaded.
     * 
     * @param styleName The name of the RtfParagraphStyle to get.
     * @return The RtfParagraphStyle with the given name or null.
     */
    public RtfParagraphStyle getRtfParagraphStyle(String styleName) {
        if(!defaultsLoaded) {
            registerDefaultStyles();
        }
        if(this.styleMap.containsKey(styleName)) {
            return (RtfParagraphStyle) this.styleMap.get(styleName);
        } else {
            return null;
        }
    }
    
    /**
     * Writes the definition of the stylesheet list.
     */
    public byte[] writeDefinition() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write("{".getBytes());
            result.write("\\stylesheet".getBytes());
            result.write(RtfBasicElement.DELIMITER);
            if(this.document.getDocumentSettings().isOutputDebugLineBreaks()) {
                result.write("\n".getBytes());
            }
            Iterator it = this.styleMap.values().iterator();
            while(it.hasNext()) {
                result.write(((RtfParagraphStyle) it.next()).writeDefinition());
            }
            result.write("}".getBytes());
            if(this.document.getDocumentSettings().isOutputDebugLineBreaks()) {
                result.write('\n');
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
}
