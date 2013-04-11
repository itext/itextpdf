package com.itextpdf.text;

import com.itextpdf.text.pdf.draw.DrawInterface;

public class TabStop {

    public static TabStop newInstance(float currentPosition, float tabInterval) {
        currentPosition = (float)Math.round(currentPosition * 1000) / 1000;
        tabInterval = (float)Math.round(tabInterval * 1000) / 1000;

        TabStop tabStop = new TabStop(currentPosition + tabInterval - currentPosition % tabInterval);
        return tabStop;
    }

    public enum Alignment {
        LEFT,
        RIGHT,
        CENTER,
        ANCHOR
    }

    protected float position;
    protected Alignment alignment = Alignment.LEFT;
    protected DrawInterface leader;
    protected char anchorChar = '.';

    public TabStop(float position) {
        this(position, Alignment.LEFT);
    }

    public TabStop(float position, DrawInterface leader) {
        this(position, leader, Alignment.LEFT);
    }

    public TabStop(float position, Alignment alignment) {
        this(position, null, alignment);
    }

    public TabStop(float position, Alignment alignment, char anchorChar) {
        this(position, null, alignment, anchorChar);
    }

    public TabStop(float position, DrawInterface leader, Alignment alignment) {
        this(position, leader, alignment, '.');
    }

    public TabStop(float position, DrawInterface leader, Alignment alignment, char anchorChar) {
        this.position = position;
        this.leader = leader;
        this.alignment = alignment;
        this.anchorChar = anchorChar;
    }

    public TabStop(TabStop tabStop) {
        this(tabStop.getPosition(), tabStop.getLeader(), tabStop.getAlignment(), tabStop.getAnchorChar());
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public DrawInterface getLeader() {
        return leader;
    }

    public void setLeader(DrawInterface leader) {
        this.leader = leader;
    }

    public char getAnchorChar() {
        return anchorChar;
    }

    public void setAnchorChar(char anchorChar) {
        this.anchorChar = anchorChar;
    }

    public float getPosition(float tabPosition, float currentPosition, float anchorPosition) {
        float newPosition = position;
        float textWidth = currentPosition - tabPosition;
        switch (alignment) {
            case RIGHT:
                if (tabPosition + textWidth < position) {
                    newPosition = position - textWidth;
                } else {
                    newPosition = tabPosition;
                }
                break;
            case CENTER:
                if (tabPosition + textWidth / 2f < position) {
                    newPosition = position - textWidth / 2f;
                } else {
                    newPosition = tabPosition;
                }
                break;
            case ANCHOR:
                if (!Float.isNaN(anchorPosition)) {
                    if (anchorPosition < position) {
                        newPosition = position - (anchorPosition - tabPosition);
                    } else {
                        newPosition = tabPosition;
                    }
                } else {
                    if (tabPosition + textWidth < position) {
                        newPosition = position - textWidth;
                    } else {
                        newPosition = tabPosition;
                    }
                }
                break;
        }
        return newPosition;
    }
}
