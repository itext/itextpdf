package com.lowagie.text.pdf;

/**
 * <CODE>PdfName</CODE> is an object that can be used as a name in a PDF-file.
 * <P>
 * A name, like a string, is a sequence of characters. It must begin with a slash
 * followed by a sequence of ASCII characters in the range 32 through 136 except
 * %, (, ), [, ], &lt;, &gt;, {, }, / and #. Any character except 0x00 may be included
 * in a name by writing its twocharacter hex code, preceded by #. The maximum number
 * of characters in a name is 127.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.5 (page 39-40).
 * <P>
 *
 * @see		PdfObject
 * @see		PdfDictionary
 * @see		BadPdfFormatException
 */

public class PRName extends PRObject implements Comparable{
    
    private int hash = 0;
    // constructors
    
    /**
     * Constructs a <CODE>PdfName</CODE>-object.
     *
     * @param		name		the new Name.
     * @exception	BadPdfFormatException	gets thrown	when the name is too long or an illegal character is used.
     */
    
    PRName(String name) {
        super(NAME);
        // every special character has to be substituted
        StringBuffer pdfName = new StringBuffer("/");
        int length = name.length();
        char character;
        // loop over all the characters
        for (int index = 0; index < length; index++) {
            character = name.charAt(index);
            // special characters are escaped (reference manual p.39)
            switch (character) {
                case ' ':
                case '%':
                case '(':
                case ')':
                case '<':
                case '>':
                case '[':
                case ']':
                case '{':
                case '}':
                case '/':
                case '#':
                    pdfName.append('#');
                    pdfName.append(Integer.toString((int) character, 16));
                    break;
                default:
                    pdfName.append(character);
            }
        }
        setContent(pdfName.toString());
    }
    
    // methods
    
    /**
     * Compares the names alfabetically.
     *
     * @param		object	an object of the type PdfName
     * @return		the value 0 if the object is a name equal to the name of this object,
     *				a value less than 0 if the argument's name is greater than the name of this object,
     *				a value greater than 0 if the argument's name is less than the name of this object
     * @throws		a <CODE>ClassCastException</CODE> if the argument is not a PdfName
     *
     */
    
    public final int compareTo(Object object) {
        PRName name = (PRName) object;
        
        byte myBytes[] = bytes;
        byte objBytes[] = name.bytes;
        int len = Math.min(myBytes.length, objBytes.length);
        for(int i=0; i<len; i++) {
            if(myBytes[i] > objBytes[i])
                return 1;
            
            if(myBytes[i] < objBytes[i])
                return -1;
        }
        if (myBytes.length < objBytes.length)
            return -1;
        if (myBytes.length > objBytes.length)
            return 1;
        return 0;
    }
    
    public final boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }
    
    public final int hashCode() {
        int h = hash;
        if (h == 0) {
            int ptr = 0;
            int len = bytes.length;
            
            for (int i = 0; i < len; i++)
                h = 31*h + (bytes[ptr++] & 0xff);
            hash = h;
        }
        return h;
    }
}