package com.lowagie.text.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import java.awt.Color;

/**
 * Implements the pattern.
 */

public class PdfPatternPainter extends PdfTemplate {
    
    protected float xstep, ystep;
    protected boolean stencil = false;
    protected Color defaultColor;
    
    /**
     *Creates a <CODE>PdfPattern</CODE>.
     */
    
    private PdfPatternPainter() {
        super(null);
        type = TYPE_PATTERN;
    }
    
    /**
     * Creates new PdfPattern
     *
     * @param wr the <CODE>PdfWriter</CODE>
     */
    
    PdfPatternPainter(PdfWriter wr) {
        super(wr);
        type = TYPE_PATTERN;
    }
    
    PdfPatternPainter(PdfWriter wr, Color defaultColor) {
        this(wr);
        stencil = true;
        if (defaultColor == null)
            this.defaultColor = Color.gray;
        else
            this.defaultColor = defaultColor;
    }
    
    /**
     * Sets the xstep of this pattern.
     *
     * @param xstep the xstep in horizontal painting
     */
    
    public void setXStep(float xstep) {
        this.xstep = xstep;
    }
    
    /**
     * Sets the ystep of this pattern.
     *
     * @param ystep in vertical painting
     */
    
    public void setYStep(float ystep) {
        this.ystep = ystep;
    }
    
    public float getXStep() {
        return this.xstep;
    }
    
    public float getYStep() {
        return this.ystep;
    }
    
    public boolean isStencil() {
        return stencil;
    }
    
    public void setPatternMatrix(float a, float b, float c, float d, float e, float f) {
        setMatrix(a, b, c, d, e, f);
    }
    /**
     * Gets the stream representing this pattern
     *
     * @return the stream representing this pattern
     */
    
    PdfPattern getPattern() {
        return new PdfPattern(this);
    }
    
    /**
     * Gets a duplicate of this <CODE>PdfPatternPainter</CODE>. All
     * the members are copied by reference but the buffer stays different.
     * @return a copy of this <CODE>PdfPatternPainter</CODE>
     */
    
    public PdfContentByte getDuplicate() {
        PdfPatternPainter tpl = new PdfPatternPainter();
        tpl.writer = writer;
        tpl.pdf = pdf;
        tpl.thisReference = thisReference;
        tpl.pageResources = pageResources;
        tpl.bBox = new Rectangle(bBox);
        tpl.xstep = xstep;
        tpl.ystep = ystep;
        tpl.matrix = matrix;
        tpl.stencil = stencil;
        tpl.defaultColor = defaultColor;
        return tpl;
    }
    
    public Color getDefaultColor() {
        return defaultColor;
    }
    
    public void setGrayFill(float gray) {
        checkNoColor();
        super.setGrayFill(gray);
    }
    
    public void resetGrayFill() {
        checkNoColor();
        super.resetGrayFill();
    }
    
    public void setGrayStroke(float gray) {
        checkNoColor();
        super.setGrayStroke(gray);
    }
    
    public void resetGrayStroke() {
        checkNoColor();
        super.resetGrayStroke();
    }
    
    public void setRGBColorFillF(float red, float green, float blue) {
        checkNoColor();
        super.setRGBColorFillF(red, green, blue);
    }
    
    public void resetRGBColorFill() {
        checkNoColor();
        super.resetRGBColorFill();
    }
    
    public void setRGBColorStrokeF(float red, float green, float blue) {
        checkNoColor();
        super.setRGBColorStrokeF(red, green, blue);
    }
    
    public void resetRGBColorStroke() {
        checkNoColor();
        super.resetRGBColorStroke();
    }
    
    public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
        checkNoColor();
        super.setCMYKColorFillF(cyan, magenta, yellow, black);
    }
    
    public void resetCMYKColorFill() {
        checkNoColor();
        super.resetCMYKColorFill();
    }
    
    public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
        checkNoColor();
        super.setCMYKColorStrokeF(cyan, magenta, yellow, black);
    }
    
    public void resetCMYKColorStroke() {
        checkNoColor();
        super.resetCMYKColorStroke();
    }
    
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        if (stencil && !image.isMask())
            checkNoColor();
        super.addImage(image, a, b, c, d, e, f);
    }
    
    public void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
        checkNoColor();
        super.setCMYKColorFill(cyan, magenta, yellow, black);
    }
    
    public void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
        checkNoColor();
        super.setCMYKColorStroke(cyan, magenta, yellow, black);
    }
    
    public void setRGBColorFill(int red, int green, int blue) {
        checkNoColor();
        super.setRGBColorFill(red, green, blue);
    }
    
    
    public void setRGBColorStroke(int red, int green, int blue) {
        checkNoColor();
        super.setRGBColorStroke(red, green, blue);
    }
    
    public void setColorStroke(Color color) {
        checkNoColor();
        super.setColorStroke(color);
    }
    
    public void setColorFill(Color color) {
        checkNoColor();
        super.setColorFill(color);
    }
    
    public void setColorFill(PdfSpotColor sp, float tint) {
        checkNoColor();
        super.setColorFill(sp, tint);
    }
    
    public void setColorStroke(PdfSpotColor sp, float tint) {
        checkNoColor();
        super.setColorStroke(sp, tint);
    }
    
    public void setPatternFill(PdfPatternPainter p) {
        checkNoColor();
        super.setPatternFill(p);
    }
    
    public void setPatternFill(PdfPatternPainter p, Color color, float tint) {
        checkNoColor();
        super.setPatternFill(p, color, tint);
    }
    
    public void setPatternStroke(PdfPatternPainter p, Color color, float tint) {
        checkNoColor();
        super.setPatternStroke(p, color, tint);
    }
    
    public void setPatternStroke(PdfPatternPainter p) {
        checkNoColor();
        super.setPatternStroke(p);
    }
    
    void checkNoColor() {
        if (stencil)
            throw new RuntimeException("Colors are not allowed in uncolored tile patterns.");
    }
}
