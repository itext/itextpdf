/*
 * ColumnText.java
 *
 * Created on April 22, 2001, 12:15 PM
 */

package com.lowagie.text.pdf;

import java.util.ArrayList;
import java.util.Iterator;
import com.lowagie.text.Phrase;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.DocumentException;

/** Formats text in a columnwise form. The text is bound
 * on the left and on the right by a sequence of lines. This allows the column
 * to have any shape, not only rectangular.
 * <P>
 * Several parameters can be set like the first paragraph line indent and
 * extra space between paragraphs.
 * <P>
 * A call to the method <CODE>go</CODE> will return one of the following
 * situations: the column ended or the text ended.
 * <P>
 * I the column ended, a new column definition can be loaded with the method
 * <CODE>setColumns</CODE> and the method <CODE>go</CODE> can be called again.
 * <P>
 * If the text ended, more text can be loaded with <CODE>addText</CODE>
 * and the method <CODE>go</CODE> can be called again.<BR>
 * The only limitation is that one or more complete paragraphs must be loaded
 * each time.
 * @author Paulo Soares (psoares@ip.pt)
 */
public class ColumnText
{
/** Signals that there are no more text available.
 */    
    public static final int NO_MORE_TEXT = 1;
/** Signals that there is no more column.
 */    
    public static final int NO_MORE_COLUMN = 2;
    
/** The column is valid.
 */    
    protected static final int LINE_STATUS_OK = 0;
/** The line is out outhe column limits.
 */    
    protected static final int LINE_STATUS_OFFLIMITS = 1;
/** The line cannot fit this column position.
 */    
    protected static final int LINE_STATUS_NOLINE = 2;
    
/** Upper bound of the column.
 */    
    protected float maxY;
/** Lower bound of the column.
 */    
    protected float minY;
/** The column alignment. Default is left alignment.
 */    
    protected int alignment = Element.ALIGN_LEFT;
/** The left column bound.
 */    
    protected ArrayList leftWall;
/** The right column bound.
 */    
    protected ArrayList rightWall;
/** The chunks that form the text.
 */    
    protected ArrayList chunks = new ArrayList();
/** The current y line location. Text will be written at this line minus
 * the leading.
 */    
    protected float yLine;
/** The text leading.
 */    
    protected float leading = 16;
/** The <CODE>PdfContent</CODE> where the text will be written to.
 */    
    protected PdfContentByte text;
/** The <CODE>PdfContent</CODE> where the graphics will be written to.
 */    
    protected PdfContentByte graphics;
/** The line status when trying to fit a line to a column.
 */    
    protected int lineStatus;
/** The first paragraph line indent.
 */    
    protected float indent = 0;
/** The extra space between paragraphs.
 */    
    protected float extraParagraphSpace = 0;
    
/** Creates a <CODE>ColumnText</CODE>.
 * @param text the place where the text will be written to. Can
 * be a template.
 */    
    public ColumnText(PdfContentByte text)
    {
        this.text = text;
        graphics = new PdfContentByte(null);
    }
    
/** Adds a <CODE>Phrase</CODE> to the current text array.
 * @param phrase the text
 */    
    public void addText(Phrase phrase)
    {
        for (Iterator j = phrase.getChunks().iterator(); j.hasNext();)
        {
            chunks.add(new PdfChunk((Chunk)j.next(), null));
        }
    }
    
/** Adds a <CODE>Chunk</CODE> to the current text array.
 * @param chunk the text
 */    
    public void addText(Chunk chunk)
    {
        chunks.add(new PdfChunk(chunk, null));
    }
    
/** Converts a sequence of lines representing one of the column bounds into
 * an internal format.
 * <p>
 * Each array element will contain a <CODE>float[4]</CODE> representing
 * the line x = ax + b.
 * @param cLine the column array
 * @return the converted array
 */    
    protected ArrayList convertColumn(float cLine[])
    {
        if (cLine.length < 4)
            throw new RuntimeException("No valid column line found.");
        ArrayList cc = new ArrayList();
        for (int k = 0; k < cLine.length - 2; k += 2) {
            float x1 = cLine[k];
            float y1 = cLine[k + 1];
            float x2 = cLine[k + 2];
            float y2 = cLine[k + 3];
            if (y1 == y2)
                continue;
            // x = ay + b
            float a = (x1 - x2) / (y1 - y2);
            float b = x1 - a * y1;
            float r[] = new float[4];
            r[0] = Math.min(y1, y2);
            r[1] = Math.max(y1, y2);
            r[2] = a;
            r[3] = b;
            cc.add(r);
            maxY = Math.max(maxY, r[1]);
            minY = Math.min(minY, r[0]);
        }
        if (cc.size() == 0)
            throw new RuntimeException("No valid column line found.");
        return cc;
    }
    
/** Finds the intersection between the <CODE>yLine</CODE> and the column. It will
 * set the <CODE>lineStatus</CODE> apropriatly.
 * @param wall the column to intersect
 * @return the x coordinate of the intersection
 */    
    protected float findLimitsPoint(ArrayList wall)
    {
        lineStatus = LINE_STATUS_OK;
        if (yLine < minY || yLine > maxY) {
            lineStatus = LINE_STATUS_OFFLIMITS;
            return 0;
        }
        for (int k = 0; k < wall.size(); ++k) {
            float r[] = (float[])wall.get(k);
            if (yLine < r[0] || yLine > r[1])
                continue;
            return r[2] * yLine + r[3];
        }
        lineStatus = LINE_STATUS_NOLINE;
        return 0;
    }
    
/** Finds the intersection between the <CODE>yLine</CODE> and the two
 * column bounds. It will set the <CODE>lineStatus</CODE> apropriatly.
 * @return a <CODE>float[2]</CODE>with the x coordinates of the intersection
 */    
    protected float[] findLimitsOneLine()
    {
        for (;;) {
            float x1 = findLimitsPoint(leftWall);
            if (lineStatus == LINE_STATUS_OFFLIMITS || lineStatus == LINE_STATUS_NOLINE)
                return null;
            float x2 = findLimitsPoint(rightWall);
            if (lineStatus == LINE_STATUS_NOLINE)
                return null;
            return new float[]{x1, x2};
        }
    }
    
/** Finds the intersection between the <CODE>yLine</CODE>,
 * the <CODE>yLine-leading</CODE>and the two
 * column bounds. It will set the <CODE>lineStatus</CODE> apropriatly.
 * @return a <CODE>float[4]</CODE>with the x coordinates of the intersection
 */    
    protected float[] findLimitsTwoLines()
    {
        for (;;) {
            float x1[] = findLimitsOneLine();
            if (lineStatus == LINE_STATUS_OFFLIMITS)
                return null;
            yLine -= leading;
            if (lineStatus == LINE_STATUS_NOLINE) {
                continue;
            }
            float x2[] = findLimitsOneLine();
            if (lineStatus == LINE_STATUS_OFFLIMITS)
                return null;
            if (lineStatus == LINE_STATUS_NOLINE) {
                yLine -= leading;
                continue;
            }
            if (x1[0] >= x2[1] || x2[0] >= x1[1])
                continue;
            return new float[]{x1[0], x1[1], x2[0], x2[1]};
        }
    }
    
/** Sets the columns bounds. Each column bound is described by a
 * <CODE>float[]</CODE> with the line points [x1,y1,x2,y2,...].
 * The array must have at least 4 elements.
 * @param leftLine the left column bound
 * @param rightLine the right column bound
 */    
    public void setColumns(float leftLine[], float rightLine[])
    {
        rightWall = convertColumn(rightLine);
        leftWall = convertColumn(leftLine);
    }
    
/** Simplified method for rectangular columns.
 * @param phrase a <CODE>Phrase</CODE>
 * @param llx the lower left x corner
 * @param lly the lower left y corner
 * @param urx the upper right x corner
 * @param ury the upper right y corner
 * @param leading the leading
 * @param alignment the column alignment
 */    
    public void setSimpleColumn(Phrase phrase, float llx, float lly, float urx, float ury, float leading, int alignment)
    {
        addText(phrase);
        setSimpleColumn(llx, lly, urx, ury, leading, alignment);
    }
    
/** Simplified method for rectangular columns.
 * @param llx the lower left x corner
 * @param lly the lower left y corner
 * @param urx the upper right x corner
 * @param ury the upper right y corner
 * @param leading the leading
 * @param alignment the column alignment
 */    
    public void setSimpleColumn(float llx, float lly, float urx, float ury, float leading, int alignment)
    {
        float leftLine[] = new float[4];
        float rightLine[] = new float[4];
        leftLine[0] = Math.min(llx, urx);
        leftLine[1] = Math.max(lly, ury);
        leftLine[2] = leftLine[0];
        leftLine[3] = Math.min(lly, ury);
        rightLine[0] = Math.max(llx, urx);
        rightLine[1] = leftLine[1];
        rightLine[2] = rightLine[0];
        rightLine[3] = leftLine[3];
        setColumns(leftLine, rightLine);
        this.leading = leading;
        this.alignment = alignment;
        yLine = leftLine[1];
    }
    
/** Sets the leading
 * @param leading the leading
 */    
    public void setLeading(float leading)
    {
        this.leading = leading;
    }
    
/** Gets the leading
 * @return the leading
 */    
    public float getLeading()
    {
        return leading;
    }
    
/** Sets the yLine. The line will be written to yLine-leading.
 * @param yLine the yLine
 */    
    public void setYLine(float yLine)
    {
        this.yLine = yLine;
    }

/** Gets the yLine.
 * @return the yLine
 */    
    public float getYLine()
    {
        return yLine;
    }
    
/** Sets the alignment.
 * @param alignment the alignment
 */    
    public void setAlignment(int alignment)
    {
        this.alignment = alignment;
    }
    
/** Gets the alignment.
 * @return the alignment
 */    
    public int getAlignment()
    {
        return alignment;
    }
    
/** Sets the first paragraph line indent.
 * @param indent the indent
 */    
    public void setIndent(float indent)
    {
        this.indent = indent;
    }
    
/** Gets the first paragraph line indent.
 * @return the indent
 */    
    public float getIndent()
    {
        return indent;
    }
    
/** Creates a line from the chunk array.
 * @param width the width of the line
 * @return the line or null if no more chunks
 */    
    protected PdfLine createLine(float width)
    {
        if (chunks.size() == 0)
            return null;
        PdfLine line = new PdfLine(0, width, alignment, 0);
        for (int k = 0; k < chunks.size(); ++k) {
            PdfChunk chunk = line.add((PdfChunk)(chunks.get(k)));
            if (chunk != null) {
                chunks.set(k, chunk);
                for (int j = k - 1; j >= 0; --j)
                    chunks.remove(j);
                return line;
            }
        }
        chunks.clear();
        return line;
    }

/** Outputs the lines to the document. It is equivalent to <CODE>go(false)</CODE>.
 * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
 * and/or <CODE>NO_MORE_COLUMN</CODE>
 * @throws DocumentException on error
 */    
    public int go() throws DocumentException
    {
        return go(false);
    }
    
/** Outputs the lines to the document. The output can be simulated.
 * @param simulate <CODE>true</CODE> to simulate the writting to the document
 * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
 * and/or <CODE>NO_MORE_COLUMN</CODE>
 * @throws DocumentException on error
 */    
    public int go(boolean simulate) throws DocumentException
    {
        boolean dirty = false;
        Object currentValues[] = new Object[2];
        PdfFont currentFont = null;
        Float lastBaseFactor = new Float(0);
        currentValues[1] = lastBaseFactor;
        PdfDocument pdf = text.getPdfDocument();
        float firstIndent = indent;
        graphics.reset();
        
        int status = 0;
        for (;;) {
            float yTemp = yLine;
            float xx[] = findLimitsTwoLines();
            if (xx == null) {
                status = NO_MORE_COLUMN;
                if (chunks.size() == 0)
                    status |= NO_MORE_TEXT;
                break;
            }
            if (chunks.size() == 0) {
                status = NO_MORE_TEXT;
                yLine = yTemp;
                break;
            }
            float x1 = Math.max(xx[0], xx[2]);
            float x2 = Math.min(xx[1], xx[3]);
            if (x2 - x1 <= firstIndent)
                continue;
            if (!simulate && !dirty) {
                text.beginText();
                dirty = true;
            }
            PdfLine line = createLine(x2 - x1 - firstIndent);
            if (!simulate) {
                currentValues[0] = currentFont;
                text.setTextMatrix(x1 + firstIndent + line.indentLeft(), yLine);
                pdf.writeLineToContent(line, text, graphics, currentValues);            
                currentFont = (PdfFont)currentValues[0];
            }
            firstIndent = line.isNewlineSplit() ? indent : 0;
            yLine -= line.isNewlineSplit() ? extraParagraphSpace : 0;
        }
        if (dirty) {
            text.endText();
            text.add(graphics);
        }
        return status;
    }
    
/** Sets the extra space between paragraphs.
 * @return the extra space between paragraphs
 */    
    public float getExtraParagraphSpace() {
        return extraParagraphSpace;
    }
    
/** Sets the extra space between paragraphs.
 * @param extraParagraphSpace the extra space between paragraphs
 */    
    public void setExtraParagraphSpace(float extraParagraphSpace) {
        this.extraParagraphSpace = extraParagraphSpace;
    }
    
/** Clears the chunk array. A call to <CODE>go()</CODE> will always return
 * NO_MORE_TEXT.
 */    
    public void clearChunks()
    {
        chunks.clear();
    }
    
}
