/*
 * $Id: PdfTransition.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
package com.itextpdf.text.pdf;

public class PdfTransition {
    /**
     *  Out Vertical Split
     */
    public static final int SPLITVOUT      = 1;
    /**
     *  Out Horizontal Split
     */
    public static final int SPLITHOUT      = 2;
    /**
     *  In Vertical Split
     */
    public static final int SPLITVIN      = 3;
    /**
     *  IN Horizontal Split
     */
    public static final int SPLITHIN      = 4;
    /**
     *  Vertical Blinds
     */
    public static final int BLINDV      = 5;
    /**
     *  Vertical Blinds
     */
    public static final int BLINDH      = 6;
    /**
     *  Inward Box
     */
    public static final int INBOX       = 7;
    /**
     *  Outward Box
     */
    public static final int OUTBOX      = 8;
    /**
     *  Left-Right Wipe
     */
    public static final int LRWIPE      = 9;
    /**
     *  Right-Left Wipe
     */
    public static final int RLWIPE     = 10;
    /**
     *  Bottom-Top Wipe
     */
    public static final int BTWIPE     = 11;
    /**
     *  Top-Bottom Wipe
     */
    public static final int TBWIPE     = 12;
    /**
     *  Dissolve
     */
    public static final int DISSOLVE    = 13;
    /**
     *  Left-Right Glitter
     */
    public static final int LRGLITTER   = 14;
    /**
     *  Top-Bottom Glitter
     */
    public static final int TBGLITTER  = 15;
    /**
     *  Diagonal Glitter
     */
    public static final int DGLITTER  = 16;
    
    /**
     *  duration of the transition effect
     */
    protected int duration;
    /**
     *  type of the transition effect
     */
    protected int type;
    
    /**
     *  Constructs a <CODE>Transition</CODE>.
     *
     */
    public PdfTransition() {
        this(BLINDH);
    }
    
    /**
     *  Constructs a <CODE>Transition</CODE>.
     *
     *@param  type      type of the transition effect
     */
    public PdfTransition(int type) {
        this(type,1);
    }
    
    /**
     *  Constructs a <CODE>Transition</CODE>.
     *
     *@param  type      type of the transition effect
     *@param  duration  duration of the transition effect
     */
    public PdfTransition(int type, int duration) {
        this.duration = duration;
        this.type = type;
    }
    
    
    public int getDuration() {
        return duration;
    }
    
    
    public int getType() {
        return type;
    }
    
    public PdfDictionary getTransitionDictionary() {
        PdfDictionary trans = new PdfDictionary(PdfName.TRANS);
        switch (type) {
            case SPLITVOUT:
                trans.put(PdfName.S,PdfName.SPLIT);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.V);
                trans.put(PdfName.M,PdfName.O);
                break;
            case SPLITHOUT:
                trans.put(PdfName.S,PdfName.SPLIT);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.H);
                trans.put(PdfName.M,PdfName.O);
                break;
            case SPLITVIN:
                trans.put(PdfName.S,PdfName.SPLIT);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.V);
                trans.put(PdfName.M,PdfName.I);
                break;
            case SPLITHIN:
                trans.put(PdfName.S,PdfName.SPLIT);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.H);
                trans.put(PdfName.M,PdfName.I);
                break;
            case BLINDV:
                trans.put(PdfName.S,PdfName.BLINDS);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.V);
                break;
            case BLINDH:
                trans.put(PdfName.S,PdfName.BLINDS);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.H);
                break;
            case INBOX:
                trans.put(PdfName.S,PdfName.BOX);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.M,PdfName.I);
                break;
            case OUTBOX:
                trans.put(PdfName.S,PdfName.BOX);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.M,PdfName.O);
                break;
            case LRWIPE:
                trans.put(PdfName.S,PdfName.WIPE);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(0));
                break;
            case RLWIPE:
                trans.put(PdfName.S,PdfName.WIPE);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(180));
                break;
            case BTWIPE:
                trans.put(PdfName.S,PdfName.WIPE);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(90));
                break;
            case TBWIPE:
                trans.put(PdfName.S,PdfName.WIPE);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(270));
                break;
            case DISSOLVE:
                trans.put(PdfName.S,PdfName.DISSOLVE);
                trans.put(PdfName.D,new PdfNumber(duration));
                break;
            case LRGLITTER:
                trans.put(PdfName.S,PdfName.GLITTER);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(0));
                break;
            case TBGLITTER:
                trans.put(PdfName.S,PdfName.GLITTER);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(270));
                break;
            case DGLITTER:
                trans.put(PdfName.S,PdfName.GLITTER);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(315));
                break;
        }
        return trans;
    }
}

