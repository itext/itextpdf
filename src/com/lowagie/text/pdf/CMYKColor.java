package com.lowagie.text.pdf;

/**
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */
public class CMYKColor extends ExtendedColor {

    float cyan;
    float magenta;
    float yellow;
    float black;

    public CMYKColor(int intCyan, int intMagenta, int intYellow, int intBlack) {
        this((float)intCyan / 255f, (float)intMagenta / 255f, (float)intYellow / 255f, (float)intBlack / 255f);
    }

    public CMYKColor(float floatCyan, float floatMagenta, float floatYellow, float floatBlack) {
        super(TYPE_CMYK, 1f - floatCyan - floatBlack, 1f - floatMagenta - floatBlack, 1f - floatYellow - floatBlack);
        cyan = normalize(floatCyan);
        magenta = normalize(floatMagenta);
        yellow = normalize(floatYellow);
        black = normalize(floatBlack);
    }
    
    public float getCyan() {
        return cyan;
    }

    public float getMagenta() {
        return magenta;
    }

    public float getYellow() {
        return yellow;
    }

    public float getBlack() {
        return black;
    }

}
