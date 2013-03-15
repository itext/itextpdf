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

    public TabStop(float position) {
        this(position, null, Alignment.LEFT);
    }

    public TabStop(float position, DrawInterface leader) {
        this(position, leader, Alignment.LEFT);
    }

    public TabStop(float position, Alignment alignment) {
        this(position, null, alignment);
    }

    public TabStop(float position, DrawInterface leader, Alignment alignment) {
        this.position = position;
        this.leader = leader;
        this.alignment = alignment;
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

    public float getPosition(float tabPosition, float textWidth) {
        float newPosition = position;
        switch (alignment) {
            case RIGHT:
                if (tabPosition + textWidth < position ) {
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
        }
        return newPosition;
    }
}
