/*
 * MetaPen.java
 *
 * Created on October 28, 2001, 11:20 AM
 */

package com.lowagie.text.pdf.wmf;
import java.io.IOException;
import java.awt.Color;

/**
 *
 * @author  Administrator
 * @version 
 */
public class MetaBrush extends MetaObject {

    public static final int BS_SOLID = 0;
    public static final int BS_NULL = 1;
    public static final int BS_HATCHED = 2;
    public static final int BS_PATTERN = 3;
    public static final int BS_DIBPATTERN = 5;
    public static final int HS_HORIZONTAL = 0;
    public static final int HS_VERTICAL = 1;
    public static final int HS_FDIAGONAL = 2;
    public static final int HS_BDIAGONAL = 3;
    public static final int HS_CROSS = 4;
    public static final int HS_DIAGCROSS = 5;

    int style = BS_SOLID;
    int hatch;
    Color color = Color.white;

    public MetaBrush() {
        type = META_BRUSH;
    }

    public void init(InputMeta in) throws IOException {
        style = in.readWord();
        color = in.readColor();
        hatch = in.readWord();
    }
    
    public int getStyle() {
        return style;
    }
    
    public int getHatch() {
        return hatch;
    }
    
    public Color getColor() {
        return color;
    }
}
