package com.lowagie.text.pdf;

import java.util.HashMap;
import java.io.UnsupportedEncodingException;
/** Each spotcolor in the document will have an instance of this class
 *
 * @author Phillip Pan (phillip@formstar.com)
 */
class ColorDetails {

    /** The indirect reference to this color
     */
    PdfIndirectReference indirectReference;
    /** The color name that appears in the document body stream
     */
    PdfName colorName;
    /** The color
     */
    PdfSpotColor spotcolor;

    /** Each spot color used in a document has an instance of this class.
     * @param colorName the color name
     * @param indirectReference the indirect reference to the font
     * @param spotColor the <CODE>PDfSpotColor</CODE>
     */
    ColorDetails(PdfName colorName, PdfIndirectReference indirectReference, PdfSpotColor scolor) {
        this.colorName = colorName;
        this.indirectReference = indirectReference;
        this.spotcolor = scolor;
    }

    /** Gets the indirect reference to this color.
     * @return the indirect reference to this color
     */
    PdfIndirectReference getIndirectReference() {
        return indirectReference;
    }

    /** Gets the color name as it appears in the document body.
     * @return the color name
     */
    PdfName getColorName() {
        return colorName;
    }

    /** Gets the <CODE>SpotColor</CODE> object.
     * @return the <CODE>PdfSpotColor</CODE>
     */
    PdfSpotColor getSpotColor() {
        return spotcolor;
    }
}
