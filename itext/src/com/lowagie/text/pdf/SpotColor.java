
package com.lowagie.text.pdf;
import java.awt.Color;
/**
 *
 * @author  psoares
 * @version 
 */
public class SpotColor extends ExtendedColor {

    PdfSpotColor spot;
    float tint;

    public SpotColor(PdfSpotColor spot, float tint) {
        super(TYPE_SEPARATION,
            (float)spot.getAlternativeCS().getRed() / 255f * tint,
            (float)spot.getAlternativeCS().getGreen() / 255f * tint,
            (float)spot.getAlternativeCS().getBlue() / 255f * tint);
        this.spot = spot;
        this.tint = tint;
    }
    
    public SpotColor(PdfSpotColor spot) {
        this(spot, spot.getTint());
    }
    
    public PdfSpotColor getPdfSpotColor() {
        return spot;
    }
    
    public float getTint() {
        return tint;
    }

}
