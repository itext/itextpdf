
package com.lowagie.text.rtf;

import com.lowagie.text.Cell;
import com.lowagie.text.Element;
import com.lowagie.text.BadElementException;

import java.util.Properties;

/**
 * A <code>Cell</code> with extended style attributes
 */
public class RtfTableCell extends Cell
{
    /* Table border styles */
    
    /** Table border solid */
    public static final int BORDER_UNDEFINED = 0;
    
    /** Table border solid */
    public static final int BORDER_SINGLE = 1;
    
    /** Table border double thickness */
    public static final int BORDER_DOUBLE_THICK = 2;
    
    /** Table border shadowed */
    public static final int BORDER_SHADOWED = 3;
    
    /** Table border dotted */
    public static final int BORDER_DOTTED = 4;
    
    /** Table border dashed */
    public static final int BORDER_DASHED = 5;
    
    /** Table border hairline */
    public static final int BORDER_HAIRLINE = 6;
    
    /** Table border double line */
    public static final int BORDER_DOUBLE = 7;
    
    /** Table border dot dash line */
    public static final int BORDER_DOT_DASH = 8;
    
    /** Table border dot dot dash line */
    public static final int BORDER_DOT_DOT_DASH = 9;
    
    /** Table border triple line */
    public static final int BORDER_TRIPLE = 10;

    /** Table border line */
    public static final int BORDER_THICK_THIN = 11;
    
    /** Table border line */
    public static final int BORDER_THIN_THICK = 12;
    
    /** Table border line */
    public static final int BORDER_THIN_THICK_THIN = 13;
    
    /** Table border line */
    public static final int BORDER_THICK_THIN_MED = 14;
    
    /** Table border line */
    public static final int BORDER_THIN_THICK_MED = 15;
    
    /** Table border line */
    public static final int BORDER_THIN_THICK_THIN_MED = 16;
    
    /** Table border line */
    public static final int BORDER_THICK_THIN_LARGE = 17;
    
    /** Table border line */
    public static final int BORDER_THIN_THICK_LARGE = 18;
    
    /** Table border line */
    public static final int BORDER_THIN_THICK_THIN_LARGE = 19;
    
    /** Table border line */
    public static final int BORDER_WAVY = 20;
    
    /** Table border line */
    public static final int BORDER_DOUBLE_WAVY = 21;
    
    /** Table border line */
    public static final int BORDER_STRIPED = 22;
    
    /** Table border line */
    public static final int BORDER_EMBOSS = 23;
    
    /** Table border line */
    public static final int BORDER_ENGRAVE = 24;
    
    /* Instance variables */
    private float topBorderWidth;
    private float leftBorderWidth;
    private float rightBorderWidth;
    private float bottomBorderWidth;
    private int topBorderStyle = 1;
    private int leftBorderStyle = 1;
    private int rightBorderStyle = 1;
    private int bottomBorderStyle = 1;
    
/**
 * Constructs an empty <CODE>Cell</CODE> (for internal use only).
 *
 * @param   dummy   a dummy value
 */

    public RtfTableCell(boolean dummy) {
        super(dummy);
    }
    
/**
 * Constructs a <CODE>Cell</CODE> with a certain <CODE>Element</CODE>.
 * <P>
 * if the element is a <CODE>ListItem</CODE>, <CODE>Row</CODE> or
 * <CODE>Cell</CODE>, an exception will be thrown.
 *
 * @param	element		the element
 * @throws	BadElementException when the creator was called with a <CODE>ListItem</CODE>, <CODE>Row</CODE> or <CODE>Cell</CODE>
 */
    public RtfTableCell(Element element) throws BadElementException {
        super(element);
    }
    
/**
 * Constructs a <CODE>Cell</CODE> with a certain content.
 * <P>
 * The <CODE>String</CODE> will be converted into a <CODE>Paragraph</CODE>.
 *
 * @param	content		a <CODE>String</CODE>
 */
    public RtfTableCell(String content) {
        super(content);
    }
    
/**
 * Returns a <CODE>Cell</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 */

    public RtfTableCell(Properties attributes) {
        super(attributes);
    }
    
    /**
     * Set all four borders to <code>f</code> width
     *
     * @param f the desired width
     */
    public void setBorderWidth(float f) {
        super.setBorderWidth(f);
        topBorderWidth = f;
        leftBorderWidth = f;
        rightBorderWidth = f;
        bottomBorderWidth = f;
    }
    
    /**
     * Set the top border to <code>f</code> width
     *
     * @param f the desired width
     */
    public void setTopBorderWidth(float f) {
        topBorderWidth = f;
    }
    
    /**
     * Get the top border width
     */
    public float topBorderWidth() {
        return topBorderWidth;
    }
    
    /**
     * Set the left border to <code>f</code> width
     *
     * @param f the desired width
     */
    public void setLeftBorderWidth(float f) {
        leftBorderWidth = f;
    }
    
    /**
     * Get the left border width
     */
    public float leftBorderWidth() {
        return leftBorderWidth;
    }
    
    /**
     * Set the right border to <code>f</code> width
     *
     * @param f the desired width
     */
    public void setRightBorderWidth(float f) {
        rightBorderWidth = f;
    }
    
    /**
     * Get the right border width
     */
    public float rightBorderWidth() {
        return rightBorderWidth;
    }
    
    /**
     * Set the bottom border to <code>f</code> width
     *
     * @param f the desired width
     */
    public void setBottomBorderWidth(float f) {
        bottomBorderWidth = f;
    }
    
    /**
     * Get the bottom border width
     */
    public float bottomBorderWidth() {
        return bottomBorderWidth;
    }
    
    /**
     * Set all four borders to style defined by <code>style</code>
     *
     * @param style the desired style
     */
    public void setBorderStyle(int style) {
        topBorderStyle = style;
        leftBorderStyle = style;
        rightBorderStyle = style;
        bottomBorderStyle = style;
    }
    
    /**
     * Set the top border to style defined by <code>style</code>
     *
     * @param style the desired style
     */
    public void setTopBorderStyle(int style) {
        topBorderStyle = style;
    }
    
    /**
     * Get the top border style
     */
    public int topBorderStyle() {
        return topBorderStyle;
    }
    
    /**
     * Set the left border to style defined by <code>style</code>
     *
     * @param style the desired style
     */
    public void setLeftBorderStyle(int style) {
        leftBorderStyle = style;
    }
    
    /**
     * Get the left border style
     */
    public int leftBorderStyle() {
        return leftBorderStyle;
    }
    
    /**
     * Set the right border to style defined by <code>style</code>
     *
     * @param style the desired style
     */
    public void setRightBorderStyle(int style) {
        rightBorderStyle = style;
    }
    
    /**
     * Get the right border style
     */
    public int rightBorderStyle() {
        return rightBorderStyle;
    }
    
    /**
     * Set the bottom border to style defined by <code>style</code>
     *
     * @param style the desired style
     */
    public void setBottomBorderStyle(int style) {
        bottomBorderStyle = style;
    }
    
    /**
     * Get the bottom border style
     */
    public int bottomBorderStyle() {
        return bottomBorderStyle;
    }
    
    /**
     * Get the RTF control word for <code>style</code>
     */
    protected static byte[] getStyleControlWord(int style) {
        switch(style)
        {
            case BORDER_UNDEFINED				: return "brdrs".getBytes();
            case BORDER_SINGLE 					: return "brdrs".getBytes();
            case BORDER_DOUBLE_THICK	 		: return "brdrth".getBytes();
            case BORDER_SHADOWED 				: return "brdrsh".getBytes();
            case BORDER_DOTTED   				: return "brdrdot".getBytes();
            case BORDER_DASHED   				: return "brdrdash".getBytes();
            case BORDER_HAIRLINE   				: return "brdrhair".getBytes();
            case BORDER_DOUBLE 		  			: return "brdrdb".getBytes();
            case BORDER_DOT_DASH   				: return "brdrdashd".getBytes();
            case BORDER_DOT_DOT_DASH			: return "brdrdashdd".getBytes();
            case BORDER_TRIPLE					: return "brdrtriple".getBytes();
            case BORDER_THICK_THIN				: return "brdrtnthsg".getBytes();
            case BORDER_THIN_THICK				: return "brdrthtnsg".getBytes();
            case BORDER_THIN_THICK_THIN			: return "brdrtnthtnsg".getBytes();
            case BORDER_THICK_THIN_MED			: return "brdrtnthmg".getBytes();
            case BORDER_THIN_THICK_MED			: return "brdrthtnmg".getBytes();
            case BORDER_THIN_THICK_THIN_MED		: return "brdrtnthtnmg".getBytes();
            case BORDER_THICK_THIN_LARGE		: return "brdrtnthlg".getBytes();
            case BORDER_THIN_THICK_LARGE		: return "brdrthtnlg".getBytes();
            case BORDER_THIN_THICK_THIN_LARGE	: return "brdrtnthtnlg".getBytes();
            case BORDER_WAVY					: return "brdrwavy".getBytes();
            case BORDER_DOUBLE_WAVY				: return "brdrwavydb".getBytes();
            case BORDER_STRIPED					: return "brdrdashdotstr".getBytes();
            case BORDER_EMBOSS					: return "brdremboss".getBytes();
            case BORDER_ENGRAVE					: return "brdrengrave".getBytes();
        }
        
        return "brdrs".getBytes();
    }
}
