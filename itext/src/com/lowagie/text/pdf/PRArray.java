package com.lowagie.text.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import com.lowagie.text.pdf.PdfEncryption;

/**
 * <CODE>PdfArray</CODE> is the PDF Array object.
 * <P>
 * An array is a sequence of PDF objects. An array may contain a mixture of object types.
 * An array is written as a left square bracket ([), followed by a sequence of objects,
 * followed by a right square bracket (]).<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.6 (page 40).
 *
 * @see		PRObject
 */

class PRArray extends PRObject {
    
    // membervariables
    
/** this is the actual array of PdfObjects */
    protected ArrayList arrayList;
    
    // constructors
    
/**
 * Constructs an empty <CODE>PdfArray</CODE>-object.
 */
    
    PRArray() {
        super(ARRAY);
        arrayList = new ArrayList();
    }
    
/**
 * Constructs an <CODE>PdfArray</CODE>-object, containing 1 <CODE>PRObject</CODE>.
 *
 * @param	object		a <CODE>PRObject</CODE> that has to be added to the array
 */
    
    PRArray(PdfObject object) {
        super(ARRAY);
        arrayList = new ArrayList();
        arrayList.add(object);
    }
    
/**
 * Constructs an <CODE>PdfArray</CODE>-object, containing all the <CODE>PRObject</CODE>s in a given <CODE>PdfArray</CODE>.
 *
 * @param	object		a <CODE>PdfArray</CODE> that has to be added to the array
 */
    
    PRArray(PRArray array) {
        super(ARRAY);
        arrayList = new ArrayList(array.getArrayList());
    }
    
    // methods overriding some methods in PRObject
    
/**
 * Returns the PDF representation of this <CODE>PdfArray</CODE>.
 *
 * @return		an array of <CODE>byte</CODE>s
 */
    
    public byte[] toPdf(PdfWriter writer) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write('[');
            
            Iterator i = arrayList.iterator();
            PdfObject object;
            if (i.hasNext()) {
                object = (PdfObject) i.next();
                stream.write(object.toPdf(writer));
            }
            while (i.hasNext()) {
                object = (PdfObject) i.next();
                stream.write(' ');
                stream.write(object.toPdf(writer));
            }
            stream.write(']');
            
            return stream.toByteArray();
        }
        catch(IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }
    
    // methods concerning the ArrayList-membervalue
    
/**
 * Returns an ArrayList containing <CODE>PRObject</CODE>s.
 *
 * @return		an ArrayList
 */
    
    final ArrayList getArrayList() {
        return arrayList;
    }
    
/**
 * Returns the number of entries in the array.
 *
 * @return		the size of the ArrayList
 */
    
    public final int size() {
        return arrayList.size();
    }
    
/**
 * Adds a <CODE>PRObject</CODE> to the <CODE>PdfArray</CODE>.
 *
 * @param		object			<CODE>PRObject</CODE> to add
 * @return		<CODE>true</CODE>
 */
    
    boolean add(PdfObject object) {
        return arrayList.add(object);
    }
    
/**
 * Adds a <CODE>PRObject</CODE> to the <CODE>PdfArray</CODE>.
 * <P>
 * The newly added object will be the first element in the <CODE>ArrayList</CODE>.
 *
 * @param		object			<CODE>PRObject</CODE> to add
 * @return		<CODE>true</CODE>
 */
    
    void addFirst(PRObject object) {
        arrayList.add(0, object);
    }
    
/**
 * Checks if the <CODE>PdfArray</CODE> allready contains a certain <CODE>PRObject</CODE>.
 *
 * @param		object			<CODE>PRObject</CODE> to check
 * @return		<CODE>true</CODE>
 */
    
    final boolean contains(PRObject object) {
        return arrayList.contains(object);
    }
    
    // deprecated methods
    
/**
 * Returns an array containing <CODE>PRObject</CODE>s.
 *
 * @return		an array
 * @deprecated	this method was never used
 */
    
    final Object[] toArray() {
        return arrayList.toArray();
    }
}