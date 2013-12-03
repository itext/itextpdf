/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Eugene Markovskyi, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
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
