package com.lowagie.text.pdf;

/**
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */
public class GrayColor extends ExtendedColor {

    float gray;

    public GrayColor(int intGray) {
        this((float)intGray / 255f);
    }

    public GrayColor(float floatGray) {
        super(TYPE_GRAY, floatGray, floatGray, floatGray);
        gray = normalize(floatGray);
    }
    
    public float getGray() {
        return gray;
    }

}
