package com.lowagie.text.pdf;
import com.lowagie.text.pdf.PdfEncryption;

/**
 * <CODE>PdfObject</CODE> is the abstract superclass of all PDF objects.
 * <P>
 * PDF supports seven basic types of objects: Booleans, numbers, strings, names,
 * arrays, dictionaries and streams. In addition, PDF provides a null object.
 * Objects may be labeled so that they can be referred to by other objects.<BR>
 * All these basic PDF objects are described in the 'Portable Document Format
 * Reference Manual version 1.3' Chapter 4 (pages 37-54).
 *
 * @see		PdfNull
 * @see		PdfBoolean
 * @see		PdfNumber
 * @see		PdfString
 * @see		PdfName
 * @see		PdfArray
 * @see		PdfDictionary
 * @see		PdfStream
 * @see		PdfIndirectReference
 */

abstract class PRObject extends PdfObject {
    
    static final int INDIRECT = 10;    
    // constructors
    
/**
 * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> without any <VAR>content</VAR>.
 *
 * @param		type			type of the new <CODE>PdfObject</CODE>
 */
    
    protected PRObject(int type) {
        super(type);
    }
    
/**
 * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> with a certain <VAR>content</VAR>.
 *
 * @param		type			type of the new <CODE>PdfObject</CODE>
 * @param		content			content of the new <CODE>PdfObject</CODE> as a <CODE>String</CODE>.
 */
    
    protected PRObject(int type, String content) {
        super(type);
        setContent(content);
    }
    
/**
 * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> with a certain <VAR>content</VAR>.
 *
 * @param		type			type of the new <CODE>PdfObject</CODE>
 * @param		bytes			content of the new <CODE>PdfObject</CODE> as an array of <CODE>byte</CODE>.
 */
    
    protected PRObject(int type, byte[] bytes) {
        super(type, bytes);
    }
    
    // methods dealing with the content of this object
    
    public String toString() {
        char c[] = new char[bytes.length];
        for (int k = 0; k < bytes.length; ++k)
            c[k] = (char)(bytes[k] & 0xff);
        return new String(c);
    }
    
/**
 * Changes the content of this <CODE>PdfObject</CODE>.
 *
 * @param		content			the new content of this <CODE>PdfObject</CODE>
 * @return		<CODE>void</CODE>
 */
    
    protected void setContent(String content) {
        bytes = stringToByte(content);
    }
    
    static byte[] stringToByte(String content) {
        int len = content.length();
        byte bytes[] = new byte[len];
        for (int k = 0; k < len; ++k)
            bytes[k] = (byte)content.charAt(k);
        return bytes;
    }
}
