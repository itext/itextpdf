package com.lowagie.text;

import java.util.ArrayList;
import java.util.Properties;

public class MarkedObject implements Element {

	/** The element that is wrapped in a MarkedObject. */
	Element element;

	/** Contains extra markupAttributes */
	protected Properties markupAttributes = new Properties();
	    
	/**
	 * Creates a MarkedObject.
	 */
	public MarkedObject(Element element) {
		this.element = element;
	}
	
    /**
     * Gets all the chunks in this element.
     *
     * @return  an <CODE>ArrayList</CODE>
     */
	public ArrayList getChunks() {
		return element.getChunks();
	}

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param       listener        an <CODE>ElementListener</CODE>
     * @return <CODE>true</CODE> if the element was processed successfully
     */
	public boolean process(ElementListener listener) {
        try {
            return listener.add(element);
        }
        catch(DocumentException de) {
            return false;
        }
	}
	
    /**
     * Gets the type of the text element.
     *
     * @return  a type
     */
	public int type() {
		return MARKED;
	}

	/**
	 * @return the markupAttributes
	 */
	public Properties getMarkupAttributes() {
		return markupAttributes;
	}
	
	public void setMarkupAttribute(String key, String value) {
		markupAttributes.setProperty(key, value);
	}

}