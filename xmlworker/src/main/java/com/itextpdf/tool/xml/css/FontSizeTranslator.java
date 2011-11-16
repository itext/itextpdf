/**
 *
 */
package com.itextpdf.tool.xml.css;

import com.itextpdf.text.Font;
import com.itextpdf.tool.xml.Tag;

/**
 * @author Emiel Ackermann
 */
public class FontSizeTranslator {

    /**
     *
     */
    private static CssUtils utils = CssUtils.getInstance();
    private static FontSizeTranslator myself;

    /**
     * @return Singleton instance of FontSizeTranslater.
     */
    public static synchronized FontSizeTranslator getInstance() {
        if (null == myself) {
            myself = new FontSizeTranslator();
        }
        return myself;
    }

    /**
     * Returns the css value of the style <b>font-size</b> in a pt-value. Possible font-size values:
     * <ul>
     * <li>a constant in px, in, cm, mm, pc, em or ex,</li>
     * <li>xx-small,</li>
     * <li>x-small,</li>
     * <li>small,</li>
     * <li>medium,</li>
     * <li>large,</li>
     * <li>x-large,</li>
     * <li>xx-large,</li>
     * <li>smaller (than tag's parent size),</li>
     * <li>larger (than tag's parent size),</li>
     * <li>a percentage (e.g font-size:250%) of tag's parent size,</li>
     * </ul>
     *
     * @param tag to get the font size of.
     *
     * @return float font size of the content of the tag in pt.
     */
    public float translateFontSize(final Tag tag) {
        float size = Font.UNDEFINED;
        if (tag.getCSS().get(CSS.Property.FONT_SIZE) != null) {
            String value = tag.getCSS().get(CSS.Property.FONT_SIZE);
            if (value.equalsIgnoreCase(CSS.Value.XX_SMALL)) {
                size = 6.75f;
            } else if (value.equalsIgnoreCase(CSS.Value.X_SMALL)) {
                size = 7.5f;
            } else if (value.equalsIgnoreCase(CSS.Value.SMALL)) {
                size = 9.75f;
            } else if (value.equalsIgnoreCase(CSS.Value.MEDIUM)) {
                size = 12f;
            } else if (value.equalsIgnoreCase(CSS.Value.LARGE)) {
                size = 13.5f;
            } else if (value.equalsIgnoreCase(CSS.Value.X_LARGE)) {
                size = 18f;
            } else if (value.equalsIgnoreCase(CSS.Value.XX_LARGE)) {
                size = 24f;
            } else if (value.equalsIgnoreCase(CSS.Value.SMALLER)) {
                if (tag.getParent() != null) {
                    float parentSize =
                        getFontSize(tag.getParent()); // if the font-size of the parent can be set in some memory the translation part is not needed anymore.
                    if (parentSize <= 6.75f) {
                        size = parentSize - 1;
                    } else if (parentSize == 7.5f) {
                        size = 6.75f;
                    } else if (parentSize == 9.75f) {
                        size = 7.5f;
                    } else if (parentSize == 12f) {
                        size = 9.75f;
                    } else if (parentSize == 13.5f) {
                        size = 12f;
                    } else if (parentSize == 18f) {
                        size = 13.5f;
                    } else if (parentSize == 24f) {
                        size = 18f;
                    } else if (parentSize < 24f) {
                        size = parentSize * 0.85f;
                    } else if (parentSize >= 24) {
                        size = parentSize * 2 / 3;
                    }
                } else {
                    size = 9.75f;
                }
            } else if (value.equalsIgnoreCase(CSS.Value.LARGER)) {
                if (tag.getParent() != null) {
                    float parentSize = getFontSize(tag.getParent()); // if the font-size of the parent can be set in some memory the translation part is not needed anymore.
                    if (parentSize == 6.75f) {
                        size = 7.5f;
                    } else if (parentSize == 7.5f) {
                        size = 9.75f;
                    } else if (parentSize == 9.75f) {
                        size = 12f;
                    } else if (parentSize == 12f) {
                        size = 13.5f;
                    } else if (parentSize == 13.5f) {
                        size = 18f;
                    } else if (parentSize == 18f) {
                        size = 24f;
                    } else {
                        size = parentSize * 1.5f;
                    }
                } else {
                    size = 13.5f;
                }
            } else if (utils.isMetricValue(value) || utils.isNumericValue(value)) {
                size = utils.parsePxInCmMmPcToPt(value);
            } else if (utils.isRelativeValue(value)) {
                float baseValue = Font.UNDEFINED;
                if (tag.getParent() != null) {
                    baseValue = getFontSize(tag.getParent());
                }
                if (baseValue == Font.UNDEFINED) {
                    baseValue = 12;
                }
                size = utils.parseRelativeValue(value, baseValue);
            }
        }
        return size;
    }

    /**
     * Retrieves the pt font size from {@link Tag#getCSS()} with {@link CSS.Property#FONT_SIZE} or returns default 12pt
     *
     * @param tag the tag to get the font-size from.
     *
     * @return the font size
     */
    public float getFontSize(final Tag tag) {
        String str = tag.getCSS().get(CSS.Property.FONT_SIZE);
        if (null != str) {
            return Float.parseFloat(str.replace("pt", ""));
        }
        return Font.UNDEFINED;
    }
}
