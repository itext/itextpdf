package com.lowagie.text.pdf;

class PRIndirectReference extends PRObject {
    
    protected PdfReader reader;
    // membervariables
    
/** the object number */
    protected int number;
    
/** the generation number */
    protected int generation;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfIndirectReference</CODE>.
 *
 * @param		type			the type of the <CODE>PdfObject</CODE> that is referenced to
 * @param		number			the object number.
 * @param		generation		the generation number.
 */
    
    PRIndirectReference(PdfReader reader, int number, int generation) {
        super(INDIRECT, new StringBuffer().append(number).append(" ").append(generation).append(" R").toString());
        this.number = number;
        this.generation = generation;
        this.reader = reader;
    }
    
/**
 * Constructs a <CODE>PdfIndirectReference</CODE>.
 *
 * @param		type			the type of the <CODE>PdfObject</CODE> that is referenced to
 * @param		number			the object number.
 */
    
    PRIndirectReference(PdfReader reader, int number) {
        this(reader, number, 0);
    }
    
    // methods
    
/**
 * Returns the number of the object.
 *
 * @return		a number.
 */
    
    final int getNumber() {
        return number;
    }
    
/**
 * Returns the generation of the object.
 *
 * @return		a number.
 */
    
    final int getGeneration() {
        return generation;
    }
    
    public byte[] toPdf(PdfWriter writer) {
        int n = writer.getNewObjectNumber(reader, number, generation);
        return stringToByte(new StringBuffer().append(n).append(" 0 R").toString());
    }

}