/*
 * BoxedText.java
 *
 * Created on April 15, 2001, 10:16 AM
 */

package com.lowagie.text.pdf;

import com.lowagie.text.Phrase;
import java.util.ArrayList;

/**
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class BoxedText
{
    protected PdfContentByte cb;
    
    protected Phrase phrase;
    
    protected float nextX;

    protected float nextY;

    public BoxedText(PdfContentByte cb, Phrase phrase, ArrayList boxList, float firstLineIdent)
    {
    }
    
    public boolean isAllTextPlaced()
    {
        return false;
    }
    
    public ArrayList getBoxList()
    {
        return null;
    }
    
    public int getBlockMarker()
    {
        return 0;
    }
    
    public float getNextLineY()
    {
        return 0f;
    }

    public float getNextLineX()
    {
        return 0f;
    }

}
