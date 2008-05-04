package com.lowagie.text;

import com.lowagie.text.pdf.PdfContentByte;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author psoares
 */
public class LineSeparator implements Element, ZeroHeight {
    
    private float lineWidth = 1;
    private float percentage = 70;
    private Color color;
    private int align = Element.ALIGN_CENTER;
    private ZeroHeight drawInterface;
    private float offset = 0;
    
    /** Creates a new instance of LineSeparator */
    public LineSeparator(float lineWidth, float percentage, Color color, int align, float offset) {
        this.lineWidth = lineWidth;
        this.percentage = percentage;
        this.color = color;
        this.align = align;
        this.offset = offset;
    }

    public LineSeparator(ZeroHeight drawInterface, float offset) {
        this.drawInterface = drawInterface;
        this.offset = offset;
    }

    public LineSeparator() {
    }

    public boolean process(ElementListener listener) {
		try {
			return listener.add(this);
		} catch (DocumentException e) {
			return false;
		}
    }

    public int type() {
        return Element.ZEROHEIGHT;
    }

    public boolean isContent() {
        return true;
    }

    public boolean isNestable() {
        return false;
    }

    public ArrayList getChunks() {
        return new ArrayList();
    }

    public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
        if (drawInterface != null) {
            drawInterface.draw(canvas, llx, lly, urx, ury, y + offset);
            return;
        }
        float w;
        if (getPercentage() < 0)
            w = -getPercentage();
        else
            w = (urx - llx) * getPercentage() / 100.0f;
        float s;
        switch (getAlign()) {
            case Element.ALIGN_LEFT:
                s = 0;
                break;
            case Element.ALIGN_RIGHT:
                s = urx - llx - w;
                break;
            default:
                s = (urx - llx - w) / 2;
                break;
        }
        canvas.saveState();
        canvas.setLineWidth(getLineWidth());
        if (getColor() != null)
            canvas.setColorStroke(getColor());
        canvas.moveTo(s + llx, y + offset);
        canvas.lineTo(s + w + llx, y + offset);
        canvas.stroke();
        canvas.restoreState();
    }    

    public ZeroHeight getDrawInterface() {
        return drawInterface;
    }

    public void setDrawInterface(ZeroHeight drawInterface) {
        this.drawInterface = drawInterface;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }
}
