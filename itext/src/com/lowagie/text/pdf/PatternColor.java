package com.lowagie.text.pdf;

/** Represents a pattern. Can be used in high-level constructs (Paragraph, Cell, etc.).
 */
public class PatternColor extends ExtendedColor {
    /** The actual pattern.
     */    
    PdfPatternPainter painter;
    
    /** Creates a color representing a pattern.
     * @param painter the actual pattern
     */    
    public PatternColor(PdfPatternPainter painter) {
        super(TYPE_PATTERN, .5f, .5f, .5f);
        this.painter = painter;
    }
    
    /** Gets the pattern.
     * @return the pattern
     */    
    public PdfPatternPainter getPainter() {
        return this.painter;
    }
}
