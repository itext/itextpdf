package com.lowagie.text.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.HashMap;
import com.lowagie.text.pdf.PdfEncryption;

/**
 * <CODE>PdfDictionary</CODE> is the Pdf dictionary object.
 * <P>
 * A dictionary is an associative table containing pairs of objects. The first element
 * of each pair is called the <I>key</I> and the second element is called the <I>value</I>.
 * Unlike dictionaries in the PostScript language, a key must be a <CODE>PdfName</CODE>.
 * A value can be any kind of <CODE>PdfObject</CODE>, including a dictionary. A dictionary is
 * generally used to collect and tie together the attributes of a complex object, with each
 * key-value pair specifying the name and value of an attribute.<BR>
 * A dictionary is represented by two left angle brackets (<<), followed by a sequence of
 * key-value pairs, followed by two right angle brackets (>>).<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.7 (page 40-41).
 * <P>
 *
 * @see		PdfObject
 * @see		PdfName
 * @see		BadPdfFormatException
 */

class PRDictionary extends PRObject {
    
/** This is the hashmap that contains all the values and keys of the dictionary */
    protected HashMap hashMap;
    
    // constructors
    
/**
 * Constructs an empty <CODE>PdfDictionary</CODE>-object.
 */
    
    PRDictionary() {
        super(DICTIONARY);
        hashMap = new HashMap();
    }
    
    // methods overriding some methods in PdfObject
    
/**
 * Returns the PDF representation of this <CODE>PdfDictionary</CODE>.
 *
 * @return		an array of <CODE>byte</CODE>
 */
    
    public byte[] toPdf(PdfWriter writer) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write('<');
            stream.write('<');
            
            // loop over all the object-pairs in the HashMap
            PRName key;
            PdfObject value;
            for (Iterator i = hashMap.keySet().iterator(); i.hasNext(); ) {
                key = (PRName) i.next();
                value = (PdfObject) hashMap.get(key);
                stream.write(key.toPdf(writer));
                stream.write(' ');
                stream.write(value.toPdf(writer));
                stream.write('\n');
            }
            stream.write('>');
            stream.write('>');
            
            return stream.toByteArray();
        }
        catch(IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }
    
    // methods concerning the HashMap member value
    
/**
 * Adds a <CODE>PdfObject</CODE> and its key to the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @param		value	value of the entry (a <CODE>PdfObject</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    
    final PdfObject put(PRName key, PdfObject value) {
        return (PdfObject) hashMap.put(key, value);
    }
    
/**
 * Removes a <CODE>PdfObject</CODE> and its key from the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    
    final PdfObject remove(PRName key) {
        return (PdfObject) hashMap.remove(key);
    }
    
/**
 * Gets a <CODE>PdfObject</CODE> with a certain key from the <CODE>PdfDictionary</CODE>.
 *
 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
 */
    
    final PdfObject get(PRName key) {
        return (PdfObject) hashMap.get(key);
    }
    
    final void putAll(PRDictionary dic) {
        hashMap.putAll(dic.hashMap);
    }
    
    final Iterator getIterator() {
        return hashMap.keySet().iterator();
    }
}