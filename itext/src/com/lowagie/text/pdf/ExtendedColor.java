package com.lowagie.text.pdf;

import java.awt.Color;
/**
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */
public class ExtendedColor extends Color{
    
    static final int TYPE_GRAY = 1;
    static final int TYPE_CMYK = 2;
    static final int TYPE_SEPARATION = 3;
    static final int TYPE_PATTERN = 4;
    
    int type;

    public ExtendedColor(int type) {
        super(0, 0, 0);
        this.type = type;
    }
    
    public ExtendedColor(int type, float red, float green, float blue) {
        super(normalize(red), normalize(green), normalize(blue));
        this.type = type;
    }
    
    public int getType() {
        return type;
    }

    final static float normalize(float value) {
        if (value < 0)
            return 0;
        if (value > 1)
            return 1;
        return value;
    }
}
