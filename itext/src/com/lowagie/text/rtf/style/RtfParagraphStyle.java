package com.lowagie.text.rtf.style;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.text.RtfParagraph;

/**
 * The RtfParagraphStyle stores all style/formatting attributes of a RtfParagraph.
 * Additionally it also supports the style name system available in RTF. The RtfParagraphStyle
 * is a Font and can thus be used as such. To use the stylesheet functionality
 * it needs to be set as the font of a Paragraph. Otherwise it will work like a
 * RtfFont. It also supports inheritance of styles.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfParagraphStyle extends RtfFont {

    /**
     * Constant for left alignment
     */
    public static final byte[] ALIGN_LEFT = "\\ql".getBytes();
    /**
     * Constant for right alignment
     */
    public static final byte[] ALIGN_RIGHT = "\\qr".getBytes();
    /**
     * Constant for center alignment
     */
    public static final byte[] ALIGN_CENTER = "\\qc".getBytes();
    /**
     * Constant for justified alignment
     */
    public static final byte[] ALIGN_JUSTIFY = "\\qj".getBytes();
    /**
     * Constant for left indentation
     */
    public static final byte[] INDENT_LEFT = "\\li".getBytes();
    /**
     * Constant for right indentation
     */
    public static final byte[] INDENT_RIGHT = "\\ri".getBytes();
    /**
     * Constant for keeping the paragraph together on one page
     */
    public static final byte[] KEEP_TOGETHER = "\\keep".getBytes();
    /**
     * Constant for keeping the paragraph toghether with the next one on one page
     */
    public static final byte[] KEEP_TOGETHER_WITH_NEXT = "\\keepn".getBytes();
    /**
     * Constant for the space after the paragraph.
     */
    public static final byte[] SPACING_AFTER = "\\sa".getBytes();
    /**
     * Constant for the space before the paragraph.
     */
    public static final byte[] SPACING_BEFORE = "\\sb".getBytes();

    /**
     * The NORMAL/STANDARD style.
     */
    public static final RtfParagraphStyle STYLE_NORMAL = new RtfParagraphStyle("Normal", "Arial", 12, Font.NORMAL, Color.BLACK);
    /**
     * The style for level 1 headings.
     */
    public static final RtfParagraphStyle STYLE_HEADING_1 = new RtfParagraphStyle("heading 1", "Normal");
    /**
     * The style for level 2 headings.
     */
    public static final RtfParagraphStyle STYLE_HEADING_2 = new RtfParagraphStyle("heading 2", "Normal");
    /**
     * The style for level 3 headings.
     */
    public static final RtfParagraphStyle STYLE_HEADING_3 = new RtfParagraphStyle("heading 3", "Normal");

    /**
     * Initialises the properties of the styles.
     */
    static {
        STYLE_HEADING_1.setSize(16);
        STYLE_HEADING_1.setStyle(Font.BOLD);
        STYLE_HEADING_2.setSize(14);
        STYLE_HEADING_2.setStyle(Font.BOLDITALIC);
        STYLE_HEADING_3.setSize(13);
        STYLE_HEADING_3.setStyle(Font.BOLD);
    }
    
    /**
     * No modification has taken place when compared to the RtfParagraphStyle this RtfParagraphStyle
     * is based on. These modification markers are used to determine what needs to be
     * inherited and what not from the parent RtfParagraphStyle.
     */
    private static final int MODIFIED_NONE = 0;
    /**
     * The alignment has been modified.
     */
    private static final int MODIFIED_ALIGNMENT = 1;
    /**
     * The left indentation has been modified.
     */
    private static final int MODIFIED_INDENT_LEFT = 2;
    /**
     * The right indentation has been modified.
     */
    private static final int MODIFIED_INDENT_RIGHT = 4;
    /**
     * The spacing before a paragraph has been modified.
     */
    private static final int MODIFIED_SPACING_BEFORE = 8;
    /**
     * The spacing after a paragraph has been modified.
     */
    private static final int MODIFIED_SPACING_AFTER = 16;
    /**
     * The font name has been modified.
     */
    private static final int MODIFIED_FONT_NAME = 32;
    /**
     * The font style has been modified.
     */
    private static final int MODIFIED_FONT_SIZE = 64;
    /**
     * The font size has been modified.
     */
    private static final int MODIFIED_FONT_STYLE = 128;
    /**
     * The font colour has been modified.
     */
    private static final int MODIFIED_FONT_COLOR = 256;
    /**
     * The line leading has been modified. 
     */
    private static final int MODIFIED_LINE_LEADING = 512;
    /**
     * The paragraph keep together setting has been modified.
     */
    private static final int MODIFIED_KEEP_TOGETHER = 1024;
    /**
     * The paragraph keep together with next setting has been modified.
     */
    private static final int MODIFIED_KEEP_TOGETHER_WITH_NEXT = 2048;
    
    /**
     * The alignment of the paragraph.
     */
    private int alignment = Element.ALIGN_LEFT;
    /**
     * The left indentation of the paragraph.
     */
    private int indentLeft = 0;
    /**
     * The right indentation of the paragraph.
     */
    private int indentRight = 0;
    /**
     * The spacing before a paragraph.
     */
    private int spacingBefore = 0;
    /**
     * The spacing after a paragraph.
     */
    private int spacingAfter = 0;
    /**
     * The line leading of the paragraph.
     */
    private int lineLeading = 0;
    /**
     * Whether this RtfParagraph must stay on one page.
     */
    private boolean keepTogether = false;
    /**
     * Whether this RtfParagraph must stay on the same page as the next paragraph.
     */
    private boolean keepTogetherWithNext = false;
    /**
     * The name of this RtfParagraphStyle.
     */
    private String styleName = "";
    /**
     * The name of the RtfParagraphStyle this RtfParagraphStyle is based on.
     */
    private String basedOnName = null;
    /**
     * The RtfParagraphStyle this RtfParagraphStyle is based on.
     */
    private RtfParagraphStyle baseStyle = null;
    /**
     * Which properties have been modified when compared to the base style.
     */
    private int modified = MODIFIED_NONE;
    /**
     * The number of this RtfParagraphStyle in the stylesheet list.
     */
    private int styleNumber = -1;
    
    /**
     * Constructs a new RtfParagraphStyle with the given attributes.
     * 
     * @param styleName The name of this RtfParagraphStyle.
     * @param fontName The name of the font to use for this RtfParagraphStyle.
     * @param fontSize The size of the font to use for this RtfParagraphStyle.
     * @param fontStyle The style of the font to use for this RtfParagraphStyle.
     * @param fontColor The colour of the font to use for this RtfParagraphStyle.
     */
    public RtfParagraphStyle(String styleName, String fontName, int fontSize, int fontStyle, Color fontColor) {
        super(null, new RtfFont(fontName, fontSize, fontStyle, fontColor));
        this.styleName = styleName;
    }
    
    /**
     * Constructs a new RtfParagraphStyle that is based on an existing RtfParagraphStyle.
     * 
     * @param styleName The name of this RtfParagraphStyle.
     * @param basedOnName The name of the RtfParagraphStyle this RtfParagraphStyle is based on.
     */
    public RtfParagraphStyle(String styleName, String basedOnName) {
        super(null, new Font());
        this.styleName = styleName;
        this.basedOnName = basedOnName;
    }
    
    /**
     * Constructs a RtfParagraphStyle from another RtfParagraphStyle.
     * 
     * INTERNAL USE ONLY
     * 
     * @param doc The RtfDocument this RtfParagraphStyle belongs to.
     * @param style The RtfParagraphStyle to copy settings from.
     */
    public RtfParagraphStyle(RtfDocument doc, RtfParagraphStyle style) {
        super(doc, style);
        this.document = doc;
        this.styleName = style.getStyleName();
        this.alignment = style.getAlignment();
        this.indentLeft = (int) (style.getIndentLeft() * RtfBasicElement.TWIPS_FACTOR);
        this.indentRight = (int) (style.getIndentRight() * RtfBasicElement.TWIPS_FACTOR);
        this.spacingBefore = (int) (style.getSpacingBefore() * RtfBasicElement.TWIPS_FACTOR);
        this.spacingAfter = (int) (style.getSpacingAfter() * RtfBasicElement.TWIPS_FACTOR);
        this.lineLeading = (int) (style.getLineLeading() * RtfBasicElement.TWIPS_FACTOR);
        this.keepTogether = style.getKeepTogether();
        this.keepTogetherWithNext = style.getKeepTogetherWithNext();
        this.basedOnName = style.basedOnName;
        this.modified = style.modified;
        this.styleNumber = style.getStyleNumber();

        if(this.document != null) {
            setRtfDocument(this.document);
        }
    }

    /**
     * Gets the name of this RtfParagraphStyle.
     * 
     * @return The name of this RtfParagraphStyle.
     */
    public String getStyleName() {
        return this.styleName;
    }
    
    /**
     * Gets the name of the RtfParagraphStyle this RtfParagraphStyle is based on.
     * 
     * @return The name of the base RtfParagraphStyle.
     */
    public String getBasedOnName() {
        return this.basedOnName;
    }
    
    /**
     * Gets the alignment of this RtfParagraphStyle.
     * 
     * @return The alignment of this RtfParagraphStyle.
     */
    public int getAlignment() {
        return this.alignment;
    }

    /**
     * Sets the alignment of this RtfParagraphStyle.
     * 
     * @param alignment The alignment to use.
     */
    public void setAlignment(int alignment) {
        this.modified = this.modified | MODIFIED_ALIGNMENT;
        this.alignment = alignment;
    }
    
    /**
     * Gets the left indentation of this RtfParagraphStyle.
     * 
     * @return The left indentation of this RtfParagraphStyle.
     */
    public int getIndentLeft() {
        return this.indentLeft;
    }

    /**
     * Sets the left indentation of this RtfParagraphStyle.
     * 
     * @param indentLeft The left indentation to use.
     */
    public void setIndentLeft(int indentLeft) {
        this.modified = this.modified | MODIFIED_INDENT_LEFT;
        this.indentLeft = indentLeft;
    }
    
    /**
     * Gets the right indentation of this RtfParagraphStyle.
     * 
     * @return The right indentation of this RtfParagraphStyle.
     */
    public int getIndentRight() {
        return this.indentRight;
    }

    /**
     * Sets the right indentation of this RtfParagraphStyle.
     * 
     * @param indentRight The right indentation to use.
     */
    public void setIndentRight(int indentRight) {
        this.modified = this.modified | MODIFIED_INDENT_RIGHT;
        this.indentRight = indentRight;
    }
    
    /**
     * Gets the space before the paragraph of this RtfParagraphStyle..
     * 
     * @return The space before the paragraph.
     */
    public int getSpacingBefore() {
        return this.spacingBefore;
    }

    /**
     * Sets the space before the paragraph of this RtfParagraphStyle.
     * 
     * @param spacingBefore The space before to use.
     */
    public void setSpacingBefore(int spacingBefore) {
        this.modified = this.modified | MODIFIED_SPACING_BEFORE;
        this.spacingBefore = spacingBefore;
    }
    
    /**
     * Gets the space after the paragraph of this RtfParagraphStyle.
     * 
     * @return The space after the paragraph.
     */
    public int getSpacingAfter() {
        return this.spacingAfter;
    }
    
    /**
     * Sets the space after the paragraph of this RtfParagraphStyle.
     * 
     * @param spacingAfter The space after to use.
     */
    public void setSpacingAfter(int spacingAfter) {
        this.modified = this.modified | MODIFIED_SPACING_AFTER;
        this.spacingAfter = spacingAfter;
    }
    
    /**
     * Sets the font name of this RtfParagraphStyle.
     * 
     * @param fontName The font name to use 
     */
    public void setFontName(String fontName) {
        this.modified = this.modified | MODIFIED_FONT_NAME;
        super.setFontName(fontName);
    }
    
    /**
     * Sets the font size of this RtfParagraphStyle.
     * 
     * @param fontSize The font size to use.
     */
    public void setSize(float fontSize) {
        this.modified = this.modified | MODIFIED_FONT_SIZE;
        super.setSize(fontSize);
    }
    
    /**
     * Sets the font style of this RtfParagraphStyle.
     * 
     * @param fontStyle The font style to use.
     */
    public void setStyle(int fontStyle) {
        this.modified = this.modified | MODIFIED_FONT_STYLE;
        super.setStyle(fontStyle);
    }
    
    /**
     * Sets the colour of this RtfParagraphStyle.
     * 
     * @param color The Color to use.
     */
    public void setColor(Color color) {
        this.modified = this.modified | MODIFIED_FONT_COLOR;
        super.setColor(color);
    }
    
    /**
     * Gets the line leading of this RtfParagraphStyle.
     * 
     * @return The line leading of this RtfParagraphStyle.
     */
    public int getLineLeading() {
        return this.lineLeading;
    }
    
    /**
     * Sets the line leading of this RtfParagraphStyle.
     * 
     * @param lineLeading The line leading to use.
     */
    public void setLineLeading(int lineLeading) {
        this.lineLeading = lineLeading;
        this.modified = this.modified | MODIFIED_LINE_LEADING;
    }
    
    /**
     * Gets whether the lines in the paragraph should be kept together in
     * this RtfParagraphStyle.
     * 
     * @return Whether the lines in the paragraph should be kept together.
     */
    public boolean getKeepTogether() {
        return this.keepTogether;
    }
    
    /**
     * Sets whether the lines in the paragraph should be kept together in
     * this RtfParagraphStyle.
     * 
     * @param keepTogether Whether the lines in the paragraph should be kept together.
     */
    public void setKeepTogether(boolean keepTogether) {
        this.keepTogether = keepTogether;
        this.modified = this.modified | MODIFIED_KEEP_TOGETHER;
    }
    
    /**
     * Gets whether the paragraph should be kept toggether with the next in
     * this RtfParagraphStyle.
     * 
     * @return Whether the paragraph should be kept together with the next.
     */
    public boolean getKeepTogetherWithNext() {
        return this.keepTogetherWithNext;
    }
    
    /**
     * Sets whether the paragraph should be kept together with the next in
     * this RtfParagraphStyle.
     * 
     * @param keepTogetherWithNext Whether the paragraph should be kept together with the next.
     */
    public void setKeepTogetherWithNext(boolean keepTogetherWithNext) {
        this.keepTogetherWithNext = keepTogetherWithNext;
        this.modified = this.modified | MODIFIED_KEEP_TOGETHER_WITH_NEXT;
    }
    
    /**
     * Handles the inheritance of paragraph style settings. All settings that
     * have not been modified will be inherited from the base RtfParagraphStyle.
     * If this RtfParagraphStyle is not based on another one, then nothing happens.
     */
    public void handleInheritance() {
        if(this.basedOnName != null && this.document.getDocumentHeader().getRtfParagraphStyle(this.basedOnName) != null) {
            this.baseStyle = this.document.getDocumentHeader().getRtfParagraphStyle(this.basedOnName);
            this.baseStyle.handleInheritance();
            if(!((this.modified & MODIFIED_ALIGNMENT) == MODIFIED_ALIGNMENT)) {
                this.alignment = this.baseStyle.getAlignment();
            }
            if(!((this.modified & MODIFIED_INDENT_LEFT) == MODIFIED_INDENT_LEFT)) {
                this.indentLeft = this.baseStyle.getIndentLeft();
            }
            if(!((this.modified & MODIFIED_INDENT_RIGHT) == MODIFIED_INDENT_RIGHT)) {
                this.indentRight = this.baseStyle.getIndentRight();
            }
            if(!((this.modified & MODIFIED_SPACING_BEFORE) == MODIFIED_SPACING_BEFORE)) {
                this.spacingBefore = this.baseStyle.getSpacingBefore();
            }
            if(!((this.modified & MODIFIED_SPACING_AFTER) == MODIFIED_SPACING_AFTER)) {
                this.spacingAfter = this.baseStyle.getSpacingAfter();
            }
            if(!((this.modified & MODIFIED_FONT_NAME) == MODIFIED_FONT_NAME)) {
                setFontName(this.baseStyle.getFontName());
            }
            if(!((this.modified & MODIFIED_FONT_SIZE) == MODIFIED_FONT_SIZE)) {
                setSize(this.baseStyle.getFontSize());
            }
            if(!((this.modified & MODIFIED_FONT_STYLE) == MODIFIED_FONT_STYLE)) {
                setStyle(this.baseStyle.getFontStyle());
            }
            if(!((this.modified & MODIFIED_FONT_COLOR) == MODIFIED_FONT_COLOR)) {
                setColor(this.baseStyle.color());
            }
            if(!((this.modified & MODIFIED_LINE_LEADING) == MODIFIED_LINE_LEADING)) {
                setLineLeading(this.baseStyle.getLineLeading());
            }
            if(!((this.modified & MODIFIED_KEEP_TOGETHER) == MODIFIED_KEEP_TOGETHER)) {
                setKeepTogether(this.baseStyle.getKeepTogether());
            }
            if(!((this.modified & MODIFIED_KEEP_TOGETHER_WITH_NEXT) == MODIFIED_KEEP_TOGETHER_WITH_NEXT)) {
                setKeepTogetherWithNext(this.baseStyle.getKeepTogetherWithNext());
            }
        }
    }
    
    /**
     * Writes the settings of this RtfParagraphStyle.
     * 
     * @return A byte array with the settings of this RtfParagraphStyle.
     */
    private byte[] writeParagraphSettings() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            if(this.keepTogether) {
                result.write(RtfParagraphStyle.KEEP_TOGETHER);
            }
            if(this.keepTogetherWithNext) {
                result.write(RtfParagraphStyle.KEEP_TOGETHER_WITH_NEXT);
            }
            switch (alignment) {
                case Element.ALIGN_LEFT:
                    result.write(RtfParagraphStyle.ALIGN_LEFT);
                    break;
                case Element.ALIGN_RIGHT:
                    result.write(RtfParagraphStyle.ALIGN_RIGHT);
                    break;
                case Element.ALIGN_CENTER:
                    result.write(RtfParagraphStyle.ALIGN_CENTER);
                    break;
                case Element.ALIGN_JUSTIFIED:
                case Element.ALIGN_JUSTIFIED_ALL:
                    result.write(RtfParagraphStyle.ALIGN_JUSTIFY);
                    break;
            }
            result.write(RtfParagraphStyle.INDENT_LEFT);
            result.write(intToByteArray(indentLeft));
            result.write(RtfParagraphStyle.INDENT_RIGHT);
            result.write(intToByteArray(indentRight));
            if(this.spacingBefore > 0) {
                result.write(RtfParagraphStyle.SPACING_BEFORE);
                result.write(intToByteArray(this.spacingBefore));
            }
            if(this.spacingAfter > 0) {
                result.write(RtfParagraphStyle.SPACING_AFTER);
                result.write(intToByteArray(this.spacingAfter));
            }
            if(this.lineLeading > 0) {
                result.write(RtfParagraph.LINE_SPACING);
                result.write(intToByteArray(this.lineLeading));
            }            
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    
    /**
     * Writes the definition of this RtfParagraphStyle for the stylesheet list.
     */
    public byte[] writeDefinition() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write("{".getBytes());
            result.write("\\style".getBytes());
            result.write("\\s".getBytes());
            result.write(intToByteArray(this.styleNumber));
            result.write(RtfBasicElement.DELIMITER);
            result.write(writeParagraphSettings());
            result.write(super.writeBegin());
            result.write(RtfBasicElement.DELIMITER);
            result.write(this.styleName.getBytes());
            result.write(";".getBytes());
            result.write("}".getBytes());
            if(this.document.getDocumentSettings().isOutputDebugLineBreaks()) {
                result.write('\n');
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    
    /**
     * Writes the start information of this RtfParagraphStyle.
     */
    public byte[] writeBegin() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write("\\s".getBytes());
            result.write(intToByteArray(this.styleNumber));
            result.write(writeParagraphSettings());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    
    /**
     * Unused
     * @return An empty byte array.
     */
    public byte[] writeEnd() {
        return new byte[0];
    }
    
    /**
     * Unused
     * @return An empty byte array.
     */
    public byte[] write() {
        return new byte[0];
    }
    
    /**
     * Tests whether two RtfParagraphStyles are equal. Equality
     * is determined via the name.
     */
    public boolean equals(Object o) {
        if(o == null || !(o instanceof RtfParagraphStyle)) {
            return false;
        }
        RtfParagraphStyle paragraphStyle = (RtfParagraphStyle) o;
        boolean result = this.getStyleName().equals(paragraphStyle.getStyleName());
        return result;
    }
    
    /**
     * Gets the hash code of this RtfParagraphStyle.
     */
    public int hashCode() {
        return this.styleName.hashCode();
    }
    
    /**
     * Gets the number of this RtfParagraphStyle in the stylesheet list.
     * 
     * @return The number of this RtfParagraphStyle in the stylesheet list.
     */
    private int getStyleNumber() {
        return this.styleNumber;
    }
    
    /**
     * Sets the number of this RtfParagraphStyle in the stylesheet list.
     * 
     * @param styleNumber The number to use.
     */
    protected void setStyleNumber(int styleNumber) {
        this.styleNumber = styleNumber;
    }
}
