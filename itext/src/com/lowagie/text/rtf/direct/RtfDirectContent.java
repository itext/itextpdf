package com.lowagie.text.rtf.direct;

import com.lowagie.text.rtf.RtfAddableElement;

/**
 * The RtfDirectContent makes it possible to directly add RTF code into
 * an RTF document. This can be used to directly add RTF fragments that
 * have been created with other RTF editors. One important aspect is that
 * font and color numbers will not be modified. This means that the
 * fonts and colors visible in the final document might not be equivalent
 * with those set on the direct content.<br /><br />
 * 
 * For convenience the RtfDirectContent provides a DIRECT_SOFT_LINEBREAK
 * constant that makes it possible to easily add soft line-breaks anywhere in
 * the RTF document.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfDirectContent extends RtfAddableElement {
	/**
	 * Add the DIRECT_SOFT_LINEBREAK to the Document to insert
	 * a soft line-break at that position.
	 */
	public static final RtfDirectContent DIRECT_SOFT_LINEBREAK = new RtfDirectContent("\\line");
	
	/**
	 * The direct content to add.
	 */
	private String directContent = "";
	
	/**
	 * Constructs a new RtfDirectContent with the content to add.
	 * 
	 * @param directContent The content to add.
	 */
	public RtfDirectContent(String directContent) {
		this.directContent = directContent;
	}
	
	/**
	 * Writes the direct content.
	 */
	public byte[] write() {
		return this.directContent.getBytes();
	}
}
