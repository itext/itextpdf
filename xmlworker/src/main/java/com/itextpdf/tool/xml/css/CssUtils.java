/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.css;

import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.MarginMemory;
import com.itextpdf.tool.xml.exceptions.NoDataException;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author redlab_b
 *
 */
public class CssUtils {

	private static final String COLOR = "-color";
	private static final String STYLE = "-style";
	private static final String WIDTH = "-width";
	private static final String BORDER2 = "border-";
	private static final String _0_LEFT_1 = "{0}left{1}";
	private static final String _0_RIGHT_1 = "{0}right{1}";
	private static final String _0_BOTTOM_1 = "{0}bottom{1}";
	private static final String _0_TOP_1 = "{0}top{1}";
	private static CssUtils myself;

	/**
	 * Default font size if none is set.
	 */
	public static final int DEFAULT_FONT_SIZE_PT = 12;


	/**
	 * @return Singleton instance of CssUtils.
	 */
	public static synchronized CssUtils getInstance() {
		if(null == myself) {
			myself = new CssUtils();
		}
		return myself;
	}

	/**
	 *
	 */
	private CssUtils() {
	}
	/**
	 * Returns the top, bottom, left, right version for the given box. the keys
	 * will be the pre value concatenated with either top, bottom, right or left
	 * and the post value. <strong>Note:</strong> Does not work when double
	 * spaces are in the boxes value. (<strong>Tip:</strong> Use
	 * {@link CssUtils#stripDoubleSpacesAndTrim(String)})
	 *
	 * @param box
	 *            the value to parse
	 * @param pre
	 *            the pre key part
	 * @param post
	 *            the post key part
	 * @return a map with the parsed properties
	 */
    public Map<String, String> parseBoxValues(final java.lang.String box,
                                             final java.lang.String pre, final java.lang.String post) {

        return parseBoxValues(box, pre, post, null);
    }
    public Map<String, String> parseBoxValues(final String box,
                                              final String pre, final String post, String preKey) {
		String[] props = box.split(" ");
		int length = props.length;
		Map<String, String> map = new HashMap<String, String>(4);
		if (length == 1) {
			String value = props[0];
            if (preKey == null) {
                map.put(MessageFormat.format(_0_TOP_1, pre, post), value);
                map.put(MessageFormat.format(_0_BOTTOM_1, pre, post), value);
                map.put(MessageFormat.format(_0_RIGHT_1, pre, post), value);
                map.put(MessageFormat.format(_0_LEFT_1, pre, post), value);
            } else {
                map.put(MessageFormat.format(preKey + "{0}", post), value);
            }
		} else if (length == 2) {
            if (preKey == null) {
                map.put(MessageFormat.format(_0_TOP_1, pre, post), props[0]);
                map.put(MessageFormat.format(_0_BOTTOM_1, pre, post), props[0]);
                map.put(MessageFormat.format(_0_RIGHT_1, pre, post), props[1]);
                map.put(MessageFormat.format(_0_LEFT_1, pre, post), props[1]);
            } else {
                map.put(MessageFormat.format(preKey + "{0}", post), props[0]);
            }
		} else if (length == 3) {
            if (preKey == null) {
                map.put(MessageFormat.format(_0_TOP_1, pre, post), props[0]);
                map.put(MessageFormat.format(_0_BOTTOM_1, pre, post), props[2]);
                map.put(MessageFormat.format(_0_RIGHT_1, pre, post), props[1]);
                map.put(MessageFormat.format(_0_LEFT_1, pre, post), props[1]);
            } else {
                map.put(MessageFormat.format(preKey + "{0}", post), props[0]);
            }
		} else if (length == 4) {
            if (preKey == null) {
                map.put(MessageFormat.format(_0_TOP_1, pre, post), props[0]);
                map.put(MessageFormat.format(_0_BOTTOM_1, pre, post), props[2]);
                map.put(MessageFormat.format(_0_RIGHT_1, pre, post), props[1]);
                map.put(MessageFormat.format(_0_LEFT_1, pre, post), props[3]);
            } else {
                map.put(MessageFormat.format(preKey + "{0}", post), props[0]);
            }
		}
		return map;
	}

	private static final Set<String> borderwidth = new HashSet<String>(
			Arrays.asList(new String[] { CSS.Value.THIN, CSS.Value.MEDIUM, CSS.Value.THICK })); //  thin = 1px, medium = 3px, thick = 5px
	private static final Set<String> borderstyle = new HashSet<String>(
			Arrays.asList(new String[] { CSS.Value.NONE, CSS.Value.HIDDEN, CSS.Value.DOTTED, CSS.Value.DASHED, CSS.Value.SOLID, CSS.Value.DOUBLE, CSS.Value.GROOVE, CSS.Value.RIDGE, CSS.Value.INSET, CSS.Value.OUTSET}));
	/**
	 * @param border
	 *            the border property
	 * @return a map of the border property parsed to each property (width,
	 *         style, color).
	 */
    public Map<String, String> parseBorder(final String border) {
        return parseBorder(border, null);
    }
	public Map<String, String> parseBorder(final String border, final String borderKey) {
		HashMap<String, String> map = new HashMap<String, String>(0);
		String split[] = splitComplexCssStyle(border);
		int length = split.length;
		if (length == 1) {
			if (borderwidth.contains(split[0]) || isNumericValue(split[0]) || isMetricValue(split[0])) {
				map.putAll(parseBoxValues(split[0], BORDER2, WIDTH, borderKey));
			} else {
				map.putAll(parseBoxValues(split[0], BORDER2, STYLE, borderKey));
			}
		} else {
			for(int i = 0 ; i<length ; i++) {
				String value = split[i];
				if (borderwidth.contains(value) || isNumericValue(value) || isMetricValue(value)) {
					map.putAll(parseBoxValues(value, BORDER2, WIDTH, borderKey));
				} else if(borderstyle.contains(value)){
					map.putAll(parseBoxValues(value, BORDER2, STYLE, borderKey));
				} else if(value.contains("rgb(") || value.contains("#") || WebColors.NAMES.containsKey(value.toLowerCase())){
					map.putAll(parseBoxValues(value, BORDER2, COLOR, borderKey));
				}
			}
		}
		return map;
	}

	/**
	 * Trims and Strips double spaces from the given string.
	 *
	 * @param str
	 *            the string to strip
	 * @return the string without double spaces
	 */
	public String stripDoubleSpacesAndTrim(final String str) {
		char[] charArray = str.toCharArray();
		if (str.contains("  ")) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < charArray.length; i++) {
				char c = charArray[i];
				if (c != ' ') {
					builder.append(c);
				} else {
					if (i + 1 < charArray.length && charArray[i + 1] != ' ') {
						builder.append(' ');
					}
				}
			}
			return builder.toString().trim();
		} else {
			return String.valueOf(charArray).trim();
		}
	}

    public String stripDoubleSpacesTrimAndToLowerCase(final String str) {
        return stripDoubleSpacesAndTrim(str).toLowerCase();
    }

	private static final Set<String> backgroundPositions = new HashSet<String>(
			Arrays.asList(new String[] { CSS.Value.LEFT, CSS.Value.CENTER, CSS.Value.BOTTOM, CSS.Value.TOP, CSS.Value.RIGHT }));

	/**
	 * Preparation before implementing the background style in iText. Splits the
	 * given background style and its attributes into background-color,
	 * background-image, background-repeat, background-attachment,
	 * background-position and css styles.
	 *
	 * @param background
	 *            the string containing the font style value.
	 * @return a map with the values of font parsed into each css property.
	 */
	public Map<String, String> processBackground(final String background) {
		Map<String, String> rules = new HashMap<String, String>();
		String[] styles = splitComplexCssStyle(background);
		for(String style : styles) {
			if (style.contains("url(")) {
				rules.put(CSS.Property.BACKGROUND_IMAGE, style);
			} else if (style.equalsIgnoreCase(CSS.Value.REPEAT)
					|| style.equalsIgnoreCase(CSS.Value.NO_REPEAT)
					|| style.equalsIgnoreCase(CSS.Value.REPEAT_X)
					|| style.equalsIgnoreCase(CSS.Value.REPEAT_Y)) {
				rules.put(CSS.Property.BACKGROUND_REPEAT, style);
			} else if (style.equalsIgnoreCase(CSS.Value.FIXED) || style.equalsIgnoreCase(CSS.Value.SCROLL)) {
				rules.put(CSS.Property.BACKGROUND_ATTACHMENT, style);
			} else if (backgroundPositions.contains(style)) {
				if(rules.get(CSS.Property.BACKGROUND_POSITION) == null) {
					rules.put(CSS.Property.BACKGROUND_POSITION, style);
				} else {
					style = style.concat(" "+rules.get(CSS.Property.BACKGROUND_POSITION));
					rules.put(CSS.Property.BACKGROUND_POSITION, style);
				}
			} else if (isNumericValue(style) || isMetricValue(style) || isRelativeValue(style)) {
				if(rules.get(CSS.Property.BACKGROUND_POSITION) == null) {
					rules.put(CSS.Property.BACKGROUND_POSITION, style);
				} else {
					style = style.concat(" "+rules.get(CSS.Property.BACKGROUND_POSITION));
					rules.put(CSS.Property.BACKGROUND_POSITION, style);
				}
			} else if(style.contains("rgb(") || style.contains("#") || WebColors.NAMES.containsKey(style.toLowerCase())) {
				rules.put(CSS.Property.BACKGROUND_COLOR, style);
			}
		}
		return rules;
	}
	/**
	 * Preparation before implementing the list style in iText. Splits the given
	 * list style and its attributes into list-style-type, list-style-position and list-style-image.
	 *
	 * @param listStyle the string containing the list style value.
	 * @return a map with the values of the parsed list style into each css property.
	 */
	public Map<String, String> processListStyle(final String listStyle) {
		Map<String, String> rules = new HashMap<String, String>();
		String[] styles = splitComplexCssStyle(listStyle);
		for(String style : styles) {
			if (style.equalsIgnoreCase(CSS.Value.DISC)
					|| style.equalsIgnoreCase(CSS.Value.SQUARE)
					|| style.equalsIgnoreCase(CSS.Value.CIRCLE)
					|| style.equalsIgnoreCase(CSS.Value.LOWER_ROMAN)
					|| style.equalsIgnoreCase(CSS.Value.UPPER_ROMAN)
					|| style.equalsIgnoreCase(CSS.Value.LOWER_GREEK)
					|| style.equalsIgnoreCase(CSS.Value.UPPER_GREEK)
					|| style.equalsIgnoreCase(CSS.Value.LOWER_ALPHA)
					|| style.equalsIgnoreCase(CSS.Value.UPPER_ALPHA)
					|| style.equalsIgnoreCase(CSS.Value.LOWER_LATIN)
					|| style.equalsIgnoreCase(CSS.Value.UPPER_LATIN)) {
				rules.put(CSS.Property.LIST_STYLE_TYPE, style);
			} else if (style.equalsIgnoreCase(CSS.Value.INSIDE) || style.equalsIgnoreCase(CSS.Value.OUTSIDE)) {
				rules.put(CSS.Property.LIST_STYLE_POSITION, style);
			} else if (style.contains("url(")) {
				rules.put(CSS.Property.LIST_STYLE_IMAGE, style);
			}
		}
		return rules;
	}
	/**
	 * Preparation before implementing the font style in iText. Splits the given
	 * font style and its attributes into font-size, line-height,
	 * font-weight, font-style, font-variant and font-family css styles.
	 *
	 * @param font the string containing the font style value.
	 * @return a map with the values of the parsed font into each css property.
	 */
	public Map<String, String> processFont(final String font) {
		Map<String, String> rules = new HashMap<String, String>();
		String[] styleAndRest = font.split("\\s");

		for (int i = 0 ; i < styleAndRest.length ; i++){
			String style = styleAndRest[i];
			if (style.equalsIgnoreCase(HtmlTags.ITALIC) || style.equalsIgnoreCase(HtmlTags.OBLIQUE)) {
				rules.put(HtmlTags.FONTSTYLE, style);
			} else if (style.equalsIgnoreCase("small-caps")){
				rules.put("font-variant", style);
			} else if (style.equalsIgnoreCase(HtmlTags.BOLD)){
				rules.put(HtmlTags.FONTWEIGHT, style);
			} else if (isMetricValue(style) || isNumericValue(style)){
				if (style.contains("/")) {
					String[] sizeAndLineHeight = style.split("/");
					style = sizeAndLineHeight[0]; // assuming font-size always is the first parameter
					rules.put(HtmlTags.LINEHEIGHT, sizeAndLineHeight[1]);
				}
				rules.put(HtmlTags.FONTSIZE, style);
				if (i != styleAndRest.length-1){
					String rest = styleAndRest[i+1];
					rest = rest.replaceAll("\"", "");
					rest = rest.replaceAll("'", "");
					rules.put(HtmlTags.FONTFAMILY, rest);
				}
			}
		}

		return rules;
	}

	/**
	 * Use only if value of style is a metric value ({@link CssUtils#isMetricValue(String)}) or a numeric value in pixels ({@link CssUtils#isNumericValue(String)}).<br />
	 * Checks if the style is present in the css of the tag, then parses it to pt. and returns the parsed value.
	 * @param t the tag which needs to be checked.
	 * @param style the style which needs to be checked.
	 * @return float the parsed value of the style or 0f if the value was invalid.
	 */
	public float checkMetricStyle(final Tag t, final String style) {
		Float metricValue = checkMetricStyle(t.getCSS(), style);
        if (metricValue != null) {
            return metricValue;
        }
        return 0f;
	}
	/**
	 * Use only if value of style is a metric value ({@link CssUtils#isMetricValue(String)}) or a numeric value in pixels ({@link CssUtils#isNumericValue(String)}).<br />
	 * Checks if the style is present in the css of the tag, then parses it to pt. and returns the parsed value.
	 * @param css the map of css styles which needs to be checked.
	 * @param style the style which needs to be checked.
	 * @return float the parsed value of the style or 0f if the value was invalid.
	 */
	public Float checkMetricStyle(final Map<String,String> css, final String style) {
		String value = css.get(style);
		if (value != null && (isMetricValue(value) || isNumericValue(value))) {
			return parsePxInCmMmPcToPt(value);
		}
		return null;
	}

	/**
	 * Checks whether a string contains an allowed metric unit in HTML/CSS; px, in, cm, mm, pc or pt.
	 * @param value the string that needs to be checked.
	 * @return boolean true if value contains an allowed metric value.
	 */
	public boolean isMetricValue(final String value) {
		return value.contains(CSS.Value.PX) || value.contains(CSS.Value.IN) || value.contains(CSS.Value.CM)
			|| value.contains(CSS.Value.MM) || value.contains(CSS.Value.PC) || value.contains(CSS.Value.PT);

	}
	/**
	 * Checks whether a string contains an allowed value relative to previously set value.
	 * @param value the string that needs to be checked.
	 * @return boolean true if value contains an allowed metric value.
	 */
	public boolean isRelativeValue(final String value) {
		return value.contains(CSS.Value.PERCENTAGE) || value.contains(CSS.Value.EM) || value.contains(CSS.Value.EX);

	}
	/**
	 * Checks whether a string matches a numeric value (e.g. 123, 1.23, .123). All these metric values are allowed in HTML/CSS.
	 * @param value the string that needs to be checked.
	 * @return boolean true if value contains an allowed metric value.
	 */
	public boolean isNumericValue(final String value) {
		return value.matches("^-?\\d\\d*\\.\\d*$") || value.matches("^-?\\d\\d*$") || value.matches("^-?\\.\\d\\d*$");

	}
	/**
	 * Convenience method for parsing a value to pt if a value can contain: <br />
	 * <ul>
	 * 	<li>a numeric value in pixels (e.g. 123, 1.23, .123),</li>
	 * 	<li>a value with a metric unit (px, in, cm, mm, pc or pt) attached to it,</li>
	 * 	<li>or a value with a relative value (%, em, ex).</li>
	 * </ul>
	 * <b>Note:</b> baseValue must be in pt.<br /><br />
	 * @param value the string containing the value to be parsed.
	 * @param baseValue float needed for the calculation of the relative value.
	 * @return parsedValue float containing the parsed value in pt.
	 */
	public float parseValueToPt(final String value, final float baseValue) {
		float parsedValue = 0;
		if(isMetricValue(value) || isNumericValue(value)) {
			parsedValue = parsePxInCmMmPcToPt(value);
		} else if (isRelativeValue(value)) {
			parsedValue = parseRelativeValue(value, baseValue);
		}
		return parsedValue;
	}
	/**
	 * Parses an relative value based on the base value that was given, in the metric unit of the base value. <br />
	 * (e.g. margin=10% should be based on the page width, so if an A4 is used, the margin = 0.10*595.0 = 59.5f)
	 * @param relativeValue in %, em or ex.
	 * @param baseValue the value the returned float is based on.
	 * @return the parsed float in the metric unit of the base value.
	 */
	public float parseRelativeValue(final String relativeValue, final float baseValue) {
		int pos = determinePositionBetweenValueAndUnit(relativeValue);
		if (pos == 0)
			return 0f;
		float f = Float.parseFloat(relativeValue.substring(0, pos) + "f");
		String unit = relativeValue.substring(pos);
		if (unit.startsWith("%")) {
			f = baseValue * f / 100;
		} else if (unit.startsWith("em")) {
			f = baseValue * f;
		} else if (unit.contains("ex")) {
			f = baseValue * f / 2;
		}
		return f;
	}

	/**
	 * Parses a length with an allowed metric unit (px, pt, in, cm, mm, pc, em or ex) or numeric value (e.g. 123, 1.23,
	 * .123) to pt.<br />
	 * A numeric value (without px, pt, etc in the given length string) is considered to be in the default metric that
	 * was given.
	 *
	 * @param length the string containing the length.
	 * @param defaultMetric the string containing the metric if it is possible that the length string does not contain
	 *            one. If null the length is considered to be in px as is default in HTML/CSS.
	 * @return parsed value
	 */
	public float parsePxInCmMmPcToPt(final String length, final String defaultMetric) {
		int pos = determinePositionBetweenValueAndUnit(length);
		if (pos == 0)
			return 0f;
		float f = Float.parseFloat(length.substring(0, pos) + "f");
		String unit = length.substring(pos);
		// inches
		if (unit.startsWith(CSS.Value.IN) || (unit.equals("") && defaultMetric.equals(CSS.Value.IN))) {
			f *= 72f;
		}
		// centimeters
		else if (unit.startsWith(CSS.Value.CM) || (unit.equals("") && defaultMetric.equals(CSS.Value.CM))) {
			f = (f / 2.54f) * 72f;
		}
		// millimeters
		else if (unit.startsWith(CSS.Value.MM) || (unit.equals("") && defaultMetric.equals(CSS.Value.MM))) {
			f = (f / 25.4f) * 72f;
		}
		// picas
		else if (unit.startsWith(CSS.Value.PC) || (unit.equals("") && defaultMetric.equals(CSS.Value.PC))) {
			f *= 12f;
		}
		// pixels (1px = 0.75pt).
		else if (unit.startsWith(CSS.Value.PX) || (unit.equals("") && defaultMetric.equals(CSS.Value.PX))) {
			f *= 0.75f;
		}
		return f;
	}

	/**
	 * Parses a length with an allowed metric unit (px, pt, in, cm, mm, pc, em or ex) or numeric value (e.g. 123, 1.23, .123) to pt.<br />
	 * A numeric value is considered to be in px as is default in HTML/CSS.
	 * @param length the string containing the length.
	 * @return float the parsed length in pt.
	 */
	public float parsePxInCmMmPcToPt(final String length) {
		return parsePxInCmMmPcToPt(length, CSS.Value.PX);
	}

	/**
	 * Method used in preparation of splitting a string containing a numeric value with a metric unit (e.g. 18px, 9pt, 6cm, etc).<br /><br />
	 * Determines the position between digits and affiliated characters ('+','-','0-9' and '.') and all other characters.<br />
	 * e.g. string "16px" will return 2, string "0.5em" will return 3 and string '-8.5mm' will return 4.
	 *
	 * @param string containing a numeric value with a metric unit
	 * @return int position between the numeric value and unit or 0 if string is null or string started with a non-numeric value.
	 */
	public int determinePositionBetweenValueAndUnit(final String string) {
		if (string == null)
			return 0;
		int pos = 0;
		boolean ok = true;
		while (ok && pos < string.length()) {
			switch (string.charAt(pos)) {
			case '+':
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				pos++;
				break;
			default:
				ok = false;
			}
		}
		return pos;
	}
	/**
	 * Returns the sum of the left and right margin of a tag.
	 * @param t the tag of which the total horizontal margin is needed.
	 * @param pageWidth the page width
	 * @return float the total horizontal margin.
	 */
	public float getLeftAndRightMargin(final Tag t, final float pageWidth) {
		float horizontalMargin = 0;
		String value = t.getCSS().get(CSS.Property.MARGIN_LEFT);
		if (value != null) {
			horizontalMargin += parseValueToPt(value, pageWidth);
		}
		value = t.getCSS().get(CSS.Property.MARGIN_RIGHT);
		if (value != null) {
			horizontalMargin += parseValueToPt(value, pageWidth);
		}
		return horizontalMargin;
	}

	/**
	 * Parses <code>url("file.jpg")</code> to <code>file.jpg</code>.
	 * @param url the url attribute to parse
	 * @return the parsed url. Or original url if not wrappend in url()
	 */
	public String extractUrl(final String url) {
		String str = null;
		if (url.startsWith("url")) {
			String urlString = url.substring(3).trim().replace("(", "").replace(")", "").trim();
			if (urlString.startsWith("'") && urlString.endsWith("'")) {
				str = urlString.substring(urlString.indexOf("'")+1, urlString.lastIndexOf("'"));
			} else if ( urlString.startsWith("\"") && urlString.endsWith("\"") ) {
				str = urlString.substring(urlString.indexOf('"')+1, urlString.lastIndexOf('"'));
			} else {
			    str = urlString;
			}
		} else {
			// assume it's an url without url
			str = url;
		}
		return str;
	}

	/**
	 * Validates a given textHeight based on the content of a tag against the css styles "min-height" and "max-height" of the tag if present.
	 *
	 * @param css the styles of a tag
	 * @param textHeight the current textHeight based on the content of a tag
	 * @return the final text height of an element.
	 */
	public float validateTextHeight(final Map<String, String> css,
			float textHeight) {
		if(null != css.get("min-height") && textHeight < new CssUtils().parsePxInCmMmPcToPt(css.get("min-height"))) {
			textHeight = new CssUtils().parsePxInCmMmPcToPt(css.get("min-height"));
		} else if(null != css.get("max-height") && textHeight > new CssUtils().parsePxInCmMmPcToPt(css.get("max-height"))) {
			textHeight = new CssUtils().parsePxInCmMmPcToPt(css.get("max-height"));
		}
		return textHeight;
	}

	/**
	 * Calculates the margin top or spacingBefore based on the given value and the last margin bottom.
	 * <br /><br />
	 * In HTML the margin-bottom of a tag overlaps with the margin-top of a following tag.
	 * This method simulates this behavior by subtracting the margin-top value of the given tag from the margin-bottom of the previous tag. The remaining value is returned or if the margin-bottom value is the largest, 0 is returned
	 * @param value the margin-top value of the given tag.
	 * @param largestFont used if a relative value was given to calculate margin.
	 * @param configuration XmlWorkerConfig containing the last margin bottom.
	 * @return an offset
	 */
	public float calculateMarginTop(final String value, final float largestFont, final MarginMemory configuration) {
		return calculateMarginTop(parseValueToPt(value, largestFont), configuration);
	}

	/**
	 * Calculates the margin top or spacingBefore based on the given value and the last margin bottom.
	 * <br /><br />
	 * In HTML the margin-bottom of a tag overlaps with the margin-top of a following tag.
	 * This method simulates this behavior by subtracting the margin-top value of the given tag from the margin-bottom of the previous tag. The remaining value is returned or if the margin-bottom value is the largest, 0 is returned
	 * @param value float containing the margin-top value.
	 * @param configuration XmlWorkerConfig containing the last margin bottom.
	 * @return an offset
	 */
	public float calculateMarginTop(final float value, final MarginMemory configuration) {
		float marginTop = value;
		try {
			float marginBottom = configuration.getLastMarginBottom();
			marginTop = (marginTop>marginBottom)?marginTop-marginBottom:0;
		} catch (NoDataException e) {
		}
		return marginTop;
	}

	/**
	 * Trims a string and removes surrounding " or '.
	 *
	 * @param s the string
	 * @return trimmed and unquoted string
	 */
	public String trimAndRemoveQuoutes(String s) {
		s = s.trim();
		if ((s.startsWith("\"") || s.startsWith("'")) && s.endsWith("\"") || s.endsWith("'")) {
			s = s.substring(1, s.length() - 1);
		}
		return s;
	}

    public String[] splitComplexCssStyle(String s) {
        s = s.replaceAll("\\s*,\\s*", ",") ;
        return s.split("\\s");
    }
}
