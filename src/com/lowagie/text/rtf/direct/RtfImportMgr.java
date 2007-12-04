package com.lowagie.text.rtf.direct;


import java.awt.Color;
import java.util.HashMap;

import com.lowagie.text.Document;
import com.lowagie.text.List;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.list.RtfList;
import com.lowagie.text.rtf.style.RtfColor;
import com.lowagie.text.rtf.style.RtfFont;

/**
 * The RtfImportHeader stores the docment header information from
 * an RTF document that is being imported. Currently font and
 * color settings are stored. The RtfImportHeader maintains a mapping
 * from font and color numbers from the imported RTF document to
 * the RTF document that is the target of the import. This guarantees
 * that the merged document has the correct font and color settings.
 * 
 * @version $Revision: 2337 $
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfImportMgr {
    //TODO: Add list, stylesheet, info, etc. mappings
    /**
     * The HashMap storing the font number mappings.
     */
    private HashMap importFontMapping = null;
    /**
     * The HashMap storing the color number mapings.
     */
    private HashMap importColorMapping = null;
    /**
     * The HashMap storing the Stylesheet List number mapings.
     */
    private HashMap importStylesheetListMapping = null;
    /**
     * The HashMap storing the List number mapings.
     */
    private HashMap importListMapping = null;
    /**
     * The RtfDocument to get font and color numbers from.
     */
    private RtfDocument rtfDoc = null;
    /**
     * The Document.
     * Used for conversions, but not imports.
     */
    private Document doc = null;


    /**
     * Constructs a new RtfImportHeader.
     * 
     * @param rtfDoc The RtfDocument to get font and color numbers from.
     */
    public RtfImportMgr(RtfDocument rtfDoc, Document doc) {
        this.rtfDoc = rtfDoc;
        this.doc = doc;
        this.importFontMapping = new HashMap();
        this.importColorMapping = new HashMap();
        this.importStylesheetListMapping = new HashMap();
        this.importListMapping = new HashMap();
    }

    /**
     * Imports a font. The font name is looked up in the RtfDocumentHeader and
     * then the mapping from original font number to actual font number is added.
     * 
     * @param fontNr The original font number.
     * @param fontName The font name to look up.
     */
    public boolean importFont(String fontNr, String fontName) {
        RtfFont rtfFont = new RtfFont(fontName);
        if(rtfFont != null){
            rtfFont.setRtfDocument(this.rtfDoc);
            this.importFontMapping.put(fontNr, Integer.toString(this.rtfDoc.getDocumentHeader().getFontNumber(rtfFont)));
            return true;
        } else {
            return false;
        }
    }
    /**
     * Imports a font. The font name is looked up in the RtfDocumentHeader and
     * then the mapping from original font number to actual font number is added.
     * 
     * @param fontNr The original font number.
     * @param fontName The font name to look up.
     * @param charset The characterset to use for the font.
     */
    public boolean importFont(String fontNr, String fontName, int charset) {
        RtfFont rtfFont = new RtfFont(fontName);
        if(charset>= 0)
            rtfFont.setCharset(charset);
        if(rtfFont != null){
            rtfFont.setRtfDocument(this.rtfDoc);
            this.importFontMapping.put(fontNr, Integer.toString(this.rtfDoc.getDocumentHeader().getFontNumber(rtfFont)));
            return true;
        } else {
            return false;
        }
    }
    /**
     * Imports a font. The font name is looked up in the RtfDocumentHeader and
     * then the mapping from original font number to actual font number is added.
     * 
     * @param fontNr The original font number.
     * @param fontName The font name to look up.
     * @param charset The characterset to use for the font.
     */
    public boolean importFont(String fontNr, String fontName, String fontFamily, int charset) {
        RtfFont rtfFont = new RtfFont(fontName);

        if(charset>= 0)
            rtfFont.setCharset(charset);
        if(fontFamily != null && fontFamily.length() > 0)
            rtfFont.setFamily(fontFamily);
        if(rtfFont != null){
            rtfFont.setRtfDocument(this.rtfDoc);
            this.importFontMapping.put(fontNr, Integer.toString(this.rtfDoc.getDocumentHeader().getFontNumber(rtfFont)));
            return true;
        } else {
            return false;
        }
    }
    /**
     * Performs the mapping from the original font number to the actual
     * font number in the resulting RTF document. If the font number was not
     * seen during import (thus no mapping) then 0 is returned, guaranteeing
     * that the font number is always valid.
     * 
     * @param fontNr The font number to map.
     * @return The mapped font number.
     */
    public String mapFontNr(String fontNr) {
        if(this.importFontMapping.containsKey(fontNr)) {
            return (String) this.importFontMapping.get(fontNr);
        } else {
            return "0";
        }
    }

    /**
     * Imports a color value. The color number for the color defined
     * by its red, green and blue values is determined and then the
     * resulting mapping is added.
     * 
     * @param colorNr The original color number.
     * @param color The color to import.
     */
    public void importColor(String colorNr, Color color) {
        RtfColor rtfColor = new RtfColor(this.rtfDoc, color);
        this.importColorMapping.put(colorNr, Integer.toString(rtfColor.getColorNumber()));
    }

    /**
     * Performs the mapping from the original font number to the actual font
     * number used in the RTF document. If the color number was not
     * seen during import (thus no mapping) then 0 is returned, guaranteeing
     * that the color number is always valid.
     * 
     * @param colorNr The color number to map.
     * @return The mapped color number
     */
    public String mapColorNr(String colorNr) {
        if(this.importColorMapping.containsKey(colorNr)) {
            return (String) this.importColorMapping.get(colorNr);
        } else {
            return "0";
        }
    }

    /**
     * Imports a List.
     */
    public void importList(String listNr, List list) {
        RtfList rtfList = new RtfList(this.rtfDoc, list);

        //if(rtfList != null){
        //rtfList.setRtfDocument(this.rtfDoc);
        this.importStylesheetListMapping.put(listNr, Integer.toString(this.rtfDoc.getDocumentHeader().getListNumber(rtfList)));
//      return true;
//      } else {
//      return false;
//      }
    }

    /**
     * 
     */
    public String mapListNr(String listNr) {
        if(this.importListMapping.containsKey(listNr)) {
            return (String) this.importListMapping.get(listNr);
        } else {
            return "0";
        }
    }

    /**
     * Imports a Stylesheet List.
     */
    public boolean importStylesheetList(String listNr, List listIn) {
        RtfList rtfList = new RtfList(this.rtfDoc, listIn);

        if(rtfList != null){
            rtfList.setRtfDocument(this.rtfDoc);
            //this.importStylesheetListMapping.put(listNr, Integer.toString(this.rtfDoc.getDocumentHeader().getRtfParagraphStyle(styleName)(rtfList)));
            return true;
        } else {
            return false;
        }
    }
    /**
     * 
     */
    public String mapStylesheetListNr(String listNr) {
        if(this.importStylesheetListMapping.containsKey(listNr)) {
            return (String) this.importStylesheetListMapping.get(listNr);
        } else {
            return "0";
        }
    }

}
