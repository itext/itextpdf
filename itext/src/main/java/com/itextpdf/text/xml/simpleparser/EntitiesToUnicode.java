/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
package com.itextpdf.text.xml.simpleparser;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains entities that can be used in an entity tag.
 */

public class EntitiesToUnicode {

    /**
     * This is a map that contains the names of entities and their unicode value.
     */
    private static final Map<String, Character> MAP = new HashMap<String, Character>();
    static {
        MAP.put("nbsp",Character.valueOf('\u00a0')); // no-break space = non-breaking space, U+00A0 ISOnum
        MAP.put("iexcl",Character.valueOf('\u00a1')); // inverted exclamation mark, U+00A1 ISOnum
        MAP.put("cent",Character.valueOf('\u00a2')); // cent sign, U+00A2 ISOnum
        MAP.put("pound",Character.valueOf('\u00a3')); // pound sign, U+00A3 ISOnum
        MAP.put("curren",Character.valueOf('\u00a4')); // currency sign, U+00A4 ISOnum
        MAP.put("yen",Character.valueOf('\u00a5')); // yen sign = yuan sign, U+00A5 ISOnum
        MAP.put("brvbar",Character.valueOf('\u00a6')); // broken bar = broken vertical bar, U+00A6 ISOnum
        MAP.put("sect",Character.valueOf('\u00a7')); // section sign, U+00A7 ISOnum
        MAP.put("uml",Character.valueOf('\u00a8')); // diaeresis = spacing diaeresis, U+00A8 ISOdia
        MAP.put("copy",Character.valueOf('\u00a9')); // copyright sign, U+00A9 ISOnum
        MAP.put("ordf",Character.valueOf('\u00aa')); // feminine ordinal indicator, U+00AA ISOnum
        MAP.put("laquo",Character.valueOf('\u00ab')); // left-pointing double angle quotation mark = left pointing guillemet, U+00AB ISOnum
        MAP.put("not",Character.valueOf('\u00ac')); // not sign, U+00AC ISOnum
        MAP.put("shy",Character.valueOf('\u00ad')); // soft hyphen = discretionary hyphen, U+00AD ISOnum
        MAP.put("reg",Character.valueOf('\u00ae')); // registered sign = registered trade mark sign, U+00AE ISOnum
        MAP.put("macr",Character.valueOf('\u00af')); // macron = spacing macron = overline = APL overbar, U+00AF ISOdia
        MAP.put("deg",Character.valueOf('\u00b0')); // degree sign, U+00B0 ISOnum
        MAP.put("plusmn",Character.valueOf('\u00b1')); // plus-minus sign = plus-or-minus sign, U+00B1 ISOnum
        MAP.put("sup2",Character.valueOf('\u00b2')); // superscript two = superscript digit two = squared, U+00B2 ISOnum
        MAP.put("sup3",Character.valueOf('\u00b3')); // superscript three = superscript digit three = cubed, U+00B3 ISOnum
        MAP.put("acute",Character.valueOf('\u00b4')); // acute accent = spacing acute, U+00B4 ISOdia
        MAP.put("micro",Character.valueOf('\u00b5')); // micro sign, U+00B5 ISOnum
        MAP.put("para",Character.valueOf('\u00b6')); // pilcrow sign = paragraph sign, U+00B6 ISOnum
        MAP.put("middot",Character.valueOf('\u00b7')); // middle dot = Georgian comma = Greek middle dot, U+00B7 ISOnum
        MAP.put("cedil",Character.valueOf('\u00b8')); // cedilla = spacing cedilla, U+00B8 ISOdia
        MAP.put("sup1",Character.valueOf('\u00b9')); // superscript one = superscript digit one, U+00B9 ISOnum
        MAP.put("ordm",Character.valueOf('\u00ba')); // masculine ordinal indicator, U+00BA ISOnum
        MAP.put("raquo",Character.valueOf('\u00bb')); // right-pointing double angle quotation mark = right pointing guillemet, U+00BB ISOnum
        MAP.put("frac14",Character.valueOf('\u00bc')); // vulgar fraction one quarter = fraction one quarter, U+00BC ISOnum
        MAP.put("frac12",Character.valueOf('\u00bd')); // vulgar fraction one half = fraction one half, U+00BD ISOnum
        MAP.put("frac34",Character.valueOf('\u00be')); // vulgar fraction three quarters = fraction three quarters, U+00BE ISOnum
        MAP.put("iquest",Character.valueOf('\u00bf')); // inverted question mark = turned question mark, U+00BF ISOnum
        MAP.put("Agrave",Character.valueOf('\u00c0')); // latin capital letter A with grave = latin capital letter A grave, U+00C0 ISOlat1
        MAP.put("Aacute",Character.valueOf('\u00c1')); // latin capital letter A with acute, U+00C1 ISOlat1
        MAP.put("Acirc",Character.valueOf('\u00c2')); // latin capital letter A with circumflex, U+00C2 ISOlat1
        MAP.put("Atilde",Character.valueOf('\u00c3')); // latin capital letter A with tilde, U+00C3 ISOlat1
        MAP.put("Auml",Character.valueOf('\u00c4')); // latin capital letter A with diaeresis, U+00C4 ISOlat1
        MAP.put("Aring",Character.valueOf('\u00c5')); // latin capital letter A with ring above = latin capital letter A ring, U+00C5 ISOlat1
        MAP.put("AElig",Character.valueOf('\u00c6')); // latin capital letter AE = latin capital ligature AE, U+00C6 ISOlat1
        MAP.put("Ccedil",Character.valueOf('\u00c7')); // latin capital letter C with cedilla, U+00C7 ISOlat1
        MAP.put("Egrave",Character.valueOf('\u00c8')); // latin capital letter E with grave, U+00C8 ISOlat1
        MAP.put("Eacute",Character.valueOf('\u00c9')); // latin capital letter E with acute, U+00C9 ISOlat1
        MAP.put("Ecirc",Character.valueOf('\u00ca')); // latin capital letter E with circumflex, U+00CA ISOlat1
        MAP.put("Euml",Character.valueOf('\u00cb')); // latin capital letter E with diaeresis, U+00CB ISOlat1
        MAP.put("Igrave",Character.valueOf('\u00cc')); // latin capital letter I with grave, U+00CC ISOlat1
        MAP.put("Iacute",Character.valueOf('\u00cd')); // latin capital letter I with acute, U+00CD ISOlat1
        MAP.put("Icirc",Character.valueOf('\u00ce')); // latin capital letter I with circumflex, U+00CE ISOlat1
        MAP.put("Iuml",Character.valueOf('\u00cf')); // latin capital letter I with diaeresis, U+00CF ISOlat1
        MAP.put("ETH",Character.valueOf('\u00d0')); // latin capital letter ETH, U+00D0 ISOlat1
        MAP.put("Ntilde",Character.valueOf('\u00d1')); // latin capital letter N with tilde, U+00D1 ISOlat1
        MAP.put("Ograve",Character.valueOf('\u00d2')); // latin capital letter O with grave, U+00D2 ISOlat1
        MAP.put("Oacute",Character.valueOf('\u00d3')); // latin capital letter O with acute, U+00D3 ISOlat1
        MAP.put("Ocirc",Character.valueOf('\u00d4')); // latin capital letter O with circumflex, U+00D4 ISOlat1
        MAP.put("Otilde",Character.valueOf('\u00d5')); // latin capital letter O with tilde, U+00D5 ISOlat1
        MAP.put("Ouml",Character.valueOf('\u00d6')); // latin capital letter O with diaeresis, U+00D6 ISOlat1
        MAP.put("times",Character.valueOf('\u00d7')); // multiplication sign, U+00D7 ISOnum
        MAP.put("Oslash",Character.valueOf('\u00d8')); // latin capital letter O with stroke = latin capital letter O slash, U+00D8 ISOlat1
        MAP.put("Ugrave",Character.valueOf('\u00d9')); // latin capital letter U with grave, U+00D9 ISOlat1
        MAP.put("Uacute",Character.valueOf('\u00da')); // latin capital letter U with acute, U+00DA ISOlat1
        MAP.put("Ucirc",Character.valueOf('\u00db')); // latin capital letter U with circumflex, U+00DB ISOlat1
        MAP.put("Uuml",Character.valueOf('\u00dc')); // latin capital letter U with diaeresis, U+00DC ISOlat1
        MAP.put("Yacute",Character.valueOf('\u00dd')); // latin capital letter Y with acute, U+00DD ISOlat1
        MAP.put("THORN",Character.valueOf('\u00de')); // latin capital letter THORN, U+00DE ISOlat1
        MAP.put("szlig",Character.valueOf('\u00df')); // latin small letter sharp s = ess-zed, U+00DF ISOlat1
        MAP.put("agrave",Character.valueOf('\u00e0')); // latin small letter a with grave = latin small letter a grave, U+00E0 ISOlat1
        MAP.put("aacute",Character.valueOf('\u00e1')); // latin small letter a with acute, U+00E1 ISOlat1
        MAP.put("acirc",Character.valueOf('\u00e2')); // latin small letter a with circumflex, U+00E2 ISOlat1
        MAP.put("atilde",Character.valueOf('\u00e3')); // latin small letter a with tilde, U+00E3 ISOlat1
        MAP.put("auml",Character.valueOf('\u00e4')); // latin small letter a with diaeresis, U+00E4 ISOlat1
        MAP.put("aring",Character.valueOf('\u00e5')); // latin small letter a with ring above = latin small letter a ring, U+00E5 ISOlat1
        MAP.put("aelig",Character.valueOf('\u00e6')); // latin small letter ae = latin small ligature ae, U+00E6 ISOlat1
        MAP.put("ccedil",Character.valueOf('\u00e7')); // latin small letter c with cedilla, U+00E7 ISOlat1
        MAP.put("egrave",Character.valueOf('\u00e8')); // latin small letter e with grave, U+00E8 ISOlat1
        MAP.put("eacute",Character.valueOf('\u00e9')); // latin small letter e with acute, U+00E9 ISOlat1
        MAP.put("ecirc",Character.valueOf('\u00ea')); // latin small letter e with circumflex, U+00EA ISOlat1
        MAP.put("euml",Character.valueOf('\u00eb')); // latin small letter e with diaeresis, U+00EB ISOlat1
        MAP.put("igrave",Character.valueOf('\u00ec')); // latin small letter i with grave, U+00EC ISOlat1
        MAP.put("iacute",Character.valueOf('\u00ed')); // latin small letter i with acute, U+00ED ISOlat1
        MAP.put("icirc",Character.valueOf('\u00ee')); // latin small letter i with circumflex, U+00EE ISOlat1
        MAP.put("iuml",Character.valueOf('\u00ef')); // latin small letter i with diaeresis, U+00EF ISOlat1
        MAP.put("eth",Character.valueOf('\u00f0')); // latin small letter eth, U+00F0 ISOlat1
        MAP.put("ntilde",Character.valueOf('\u00f1')); // latin small letter n with tilde, U+00F1 ISOlat1
        MAP.put("ograve",Character.valueOf('\u00f2')); // latin small letter o with grave, U+00F2 ISOlat1
        MAP.put("oacute",Character.valueOf('\u00f3')); // latin small letter o with acute, U+00F3 ISOlat1
        MAP.put("ocirc",Character.valueOf('\u00f4')); // latin small letter o with circumflex, U+00F4 ISOlat1
        MAP.put("otilde",Character.valueOf('\u00f5')); // latin small letter o with tilde, U+00F5 ISOlat1
        MAP.put("ouml",Character.valueOf('\u00f6')); // latin small letter o with diaeresis, U+00F6 ISOlat1
        MAP.put("divide",Character.valueOf('\u00f7')); // division sign, U+00F7 ISOnum
        MAP.put("oslash",Character.valueOf('\u00f8')); // latin small letter o with stroke, = latin small letter o slash, U+00F8 ISOlat1
        MAP.put("ugrave",Character.valueOf('\u00f9')); // latin small letter u with grave, U+00F9 ISOlat1
        MAP.put("uacute",Character.valueOf('\u00fa')); // latin small letter u with acute, U+00FA ISOlat1
        MAP.put("ucirc",Character.valueOf('\u00fb')); // latin small letter u with circumflex, U+00FB ISOlat1
        MAP.put("uuml",Character.valueOf('\u00fc')); // latin small letter u with diaeresis, U+00FC ISOlat1
        MAP.put("yacute",Character.valueOf('\u00fd')); // latin small letter y with acute, U+00FD ISOlat1
        MAP.put("thorn",Character.valueOf('\u00fe')); // latin small letter thorn, U+00FE ISOlat1
        MAP.put("yuml",Character.valueOf('\u00ff')); // latin small letter y with diaeresis, U+00FF ISOlat1
        // Latin Extended-B
        MAP.put("fnof",Character.valueOf('\u0192')); // latin small f with hook = function = florin, U+0192 ISOtech
        // Greek
        MAP.put("Alpha",Character.valueOf('\u0391')); // greek capital letter alpha, U+0391
        MAP.put("Beta",Character.valueOf('\u0392')); // greek capital letter beta, U+0392
        MAP.put("Gamma",Character.valueOf('\u0393')); // greek capital letter gamma, U+0393 ISOgrk3
        MAP.put("Delta",Character.valueOf('\u0394')); // greek capital letter delta, U+0394 ISOgrk3
        MAP.put("Epsilon",Character.valueOf('\u0395')); // greek capital letter epsilon, U+0395
        MAP.put("Zeta",Character.valueOf('\u0396')); // greek capital letter zeta, U+0396
        MAP.put("Eta",Character.valueOf('\u0397')); // greek capital letter eta, U+0397
        MAP.put("Theta",Character.valueOf('\u0398')); // greek capital letter theta, U+0398 ISOgrk3
        MAP.put("Iota",Character.valueOf('\u0399')); // greek capital letter iota, U+0399
        MAP.put("Kappa",Character.valueOf('\u039a')); // greek capital letter kappa, U+039A
        MAP.put("Lambda",Character.valueOf('\u039b')); // greek capital letter lambda, U+039B ISOgrk3
        MAP.put("Mu",Character.valueOf('\u039c')); // greek capital letter mu, U+039C
        MAP.put("Nu",Character.valueOf('\u039d')); // greek capital letter nu, U+039D
        MAP.put("Xi",Character.valueOf('\u039e')); // greek capital letter xi, U+039E ISOgrk3
        MAP.put("Omicron",Character.valueOf('\u039f')); // greek capital letter omicron, U+039F
        MAP.put("Pi",Character.valueOf('\u03a0')); // greek capital letter pi, U+03A0 ISOgrk3
        MAP.put("Rho",Character.valueOf('\u03a1')); // greek capital letter rho, U+03A1
        // there is no Sigmaf, and no U+03A2 character either
        MAP.put("Sigma",Character.valueOf('\u03a3')); // greek capital letter sigma, U+03A3 ISOgrk3
        MAP.put("Tau",Character.valueOf('\u03a4')); // greek capital letter tau, U+03A4
        MAP.put("Upsilon",Character.valueOf('\u03a5')); // greek capital letter upsilon, U+03A5 ISOgrk3
        MAP.put("Phi",Character.valueOf('\u03a6')); // greek capital letter phi, U+03A6 ISOgrk3
        MAP.put("Chi",Character.valueOf('\u03a7')); // greek capital letter chi, U+03A7
        MAP.put("Psi",Character.valueOf('\u03a8')); // greek capital letter psi, U+03A8 ISOgrk3
        MAP.put("Omega",Character.valueOf('\u03a9')); // greek capital letter omega, U+03A9 ISOgrk3
        MAP.put("alpha",Character.valueOf('\u03b1')); // greek small letter alpha, U+03B1 ISOgrk3
        MAP.put("beta",Character.valueOf('\u03b2')); // greek small letter beta, U+03B2 ISOgrk3
        MAP.put("gamma",Character.valueOf('\u03b3')); // greek small letter gamma, U+03B3 ISOgrk3
        MAP.put("delta",Character.valueOf('\u03b4')); // greek small letter delta, U+03B4 ISOgrk3
        MAP.put("epsilon",Character.valueOf('\u03b5')); // greek small letter epsilon, U+03B5 ISOgrk3
        MAP.put("zeta",Character.valueOf('\u03b6')); // greek small letter zeta, U+03B6 ISOgrk3
        MAP.put("eta",Character.valueOf('\u03b7')); // greek small letter eta, U+03B7 ISOgrk3
        MAP.put("theta",Character.valueOf('\u03b8')); // greek small letter theta, U+03B8 ISOgrk3
        MAP.put("iota",Character.valueOf('\u03b9')); // greek small letter iota, U+03B9 ISOgrk3
        MAP.put("kappa",Character.valueOf('\u03ba')); // greek small letter kappa, U+03BA ISOgrk3
        MAP.put("lambda",Character.valueOf('\u03bb')); // greek small letter lambda, U+03BB ISOgrk3
        MAP.put("mu",Character.valueOf('\u03bc')); // greek small letter mu, U+03BC ISOgrk3
        MAP.put("nu",Character.valueOf('\u03bd')); // greek small letter nu, U+03BD ISOgrk3
        MAP.put("xi",Character.valueOf('\u03be')); // greek small letter xi, U+03BE ISOgrk3
        MAP.put("omicron",Character.valueOf('\u03bf')); // greek small letter omicron, U+03BF NEW
        MAP.put("pi",Character.valueOf('\u03c0')); // greek small letter pi, U+03C0 ISOgrk3
        MAP.put("rho",Character.valueOf('\u03c1')); // greek small letter rho, U+03C1 ISOgrk3
        MAP.put("sigmaf",Character.valueOf('\u03c2')); // greek small letter final sigma, U+03C2 ISOgrk3
        MAP.put("sigma",Character.valueOf('\u03c3')); // greek small letter sigma, U+03C3 ISOgrk3
        MAP.put("tau",Character.valueOf('\u03c4')); // greek small letter tau, U+03C4 ISOgrk3
        MAP.put("upsilon",Character.valueOf('\u03c5')); // greek small letter upsilon, U+03C5 ISOgrk3
        MAP.put("phi",Character.valueOf('\u03c6')); // greek small letter phi, U+03C6 ISOgrk3
        MAP.put("chi",Character.valueOf('\u03c7')); // greek small letter chi, U+03C7 ISOgrk3
        MAP.put("psi",Character.valueOf('\u03c8')); // greek small letter psi, U+03C8 ISOgrk3
        MAP.put("omega",Character.valueOf('\u03c9')); // greek small letter omega, U+03C9 ISOgrk3
        MAP.put("thetasym",Character.valueOf('\u03d1')); // greek small letter theta symbol, U+03D1 NEW
        MAP.put("upsih",Character.valueOf('\u03d2')); // greek upsilon with hook symbol, U+03D2 NEW
        MAP.put("piv",Character.valueOf('\u03d6')); // greek pi symbol, U+03D6 ISOgrk3
        // General Punctuation
        MAP.put("bull",Character.valueOf('\u2022')); // bullet = black small circle, U+2022 ISOpub
        // bullet is NOT the same as bullet operator, U+2219
        MAP.put("hellip",Character.valueOf('\u2026')); // horizontal ellipsis = three dot leader, U+2026 ISOpub
        MAP.put("prime",Character.valueOf('\u2032')); // prime = minutes = feet, U+2032 ISOtech
        MAP.put("Prime",Character.valueOf('\u2033')); // double prime = seconds = inches, U+2033 ISOtech
        MAP.put("oline",Character.valueOf('\u203e')); // overline = spacing overscore, U+203E NEW
        MAP.put("frasl",Character.valueOf('\u2044')); // fraction slash, U+2044 NEW
        // Letterlike Symbols
        MAP.put("weierp",Character.valueOf('\u2118')); // script capital P = power set = Weierstrass p, U+2118 ISOamso
        MAP.put("image",Character.valueOf('\u2111')); // blackletter capital I = imaginary part, U+2111 ISOamso
        MAP.put("real",Character.valueOf('\u211c')); // blackletter capital R = real part symbol, U+211C ISOamso
        MAP.put("trade",Character.valueOf('\u2122')); // trade mark sign, U+2122 ISOnum
        MAP.put("alefsym",Character.valueOf('\u2135')); // alef symbol = first transfinite cardinal, U+2135 NEW
        // alef symbol is NOT the same as hebrew letter alef,
        // U+05D0 although the same glyph could be used to depict both characters
        // Arrows
        MAP.put("larr",Character.valueOf('\u2190')); // leftwards arrow, U+2190 ISOnum
        MAP.put("uarr",Character.valueOf('\u2191')); // upwards arrow, U+2191 ISOnum
        MAP.put("rarr",Character.valueOf('\u2192')); // rightwards arrow, U+2192 ISOnum
        MAP.put("darr",Character.valueOf('\u2193')); // downwards arrow, U+2193 ISOnum
        MAP.put("harr",Character.valueOf('\u2194')); // left right arrow, U+2194 ISOamsa
        MAP.put("crarr",Character.valueOf('\u21b5')); // downwards arrow with corner leftwards = carriage return, U+21B5 NEW
        MAP.put("lArr",Character.valueOf('\u21d0')); // leftwards double arrow, U+21D0 ISOtech
        // ISO 10646 does not say that lArr is the same as the 'is implied by' arrow
        // but also does not have any other character for that function. So ? lArr can
        // be used for 'is implied by' as ISOtech suggests
        MAP.put("uArr",Character.valueOf('\u21d1')); // upwards double arrow, U+21D1 ISOamsa
        MAP.put("rArr",Character.valueOf('\u21d2')); // rightwards double arrow, U+21D2 ISOtech
        // ISO 10646 does not say this is the 'implies' character but does not have
        // another character with this function so ?
        // rArr can be used for 'implies' as ISOtech suggests
        MAP.put("dArr",Character.valueOf('\u21d3')); // downwards double arrow, U+21D3 ISOamsa
        MAP.put("hArr",Character.valueOf('\u21d4')); // left right double arrow, U+21D4 ISOamsa
        // Mathematical Operators
        MAP.put("forall",Character.valueOf('\u2200')); // for all, U+2200 ISOtech
        MAP.put("part",Character.valueOf('\u2202')); // partial differential, U+2202 ISOtech
        MAP.put("exist",Character.valueOf('\u2203')); // there exists, U+2203 ISOtech
        MAP.put("empty",Character.valueOf('\u2205')); // empty set = null set = diameter, U+2205 ISOamso
        MAP.put("nabla",Character.valueOf('\u2207')); // nabla = backward difference, U+2207 ISOtech
        MAP.put("isin",Character.valueOf('\u2208')); // element of, U+2208 ISOtech
        MAP.put("notin",Character.valueOf('\u2209')); // not an element of, U+2209 ISOtech
        MAP.put("ni",Character.valueOf('\u220b')); // contains as member, U+220B ISOtech
        // should there be a more memorable name than 'ni'?
        MAP.put("prod",Character.valueOf('\u220f')); // n-ary product = product sign, U+220F ISOamsb
        // prod is NOT the same character as U+03A0 'greek capital letter pi' though
        // the same glyph might be used for both
        MAP.put("sum",Character.valueOf('\u2211')); // n-ary sumation, U+2211 ISOamsb
        // sum is NOT the same character as U+03A3 'greek capital letter sigma'
        // though the same glyph might be used for both
        MAP.put("minus",Character.valueOf('\u2212')); // minus sign, U+2212 ISOtech
        MAP.put("lowast",Character.valueOf('\u2217')); // asterisk operator, U+2217 ISOtech
        MAP.put("radic",Character.valueOf('\u221a')); // square root = radical sign, U+221A ISOtech
        MAP.put("prop",Character.valueOf('\u221d')); // proportional to, U+221D ISOtech
        MAP.put("infin",Character.valueOf('\u221e')); // infinity, U+221E ISOtech
        MAP.put("ang",Character.valueOf('\u2220')); // angle, U+2220 ISOamso
        MAP.put("and",Character.valueOf('\u2227')); // logical and = wedge, U+2227 ISOtech
        MAP.put("or",Character.valueOf('\u2228')); // logical or = vee, U+2228 ISOtech
        MAP.put("cap",Character.valueOf('\u2229')); // intersection = cap, U+2229 ISOtech
        MAP.put("cup",Character.valueOf('\u222a')); // union = cup, U+222A ISOtech
        MAP.put("int",Character.valueOf('\u222b')); // integral, U+222B ISOtech
        MAP.put("there4",Character.valueOf('\u2234')); // therefore, U+2234 ISOtech
        MAP.put("sim",Character.valueOf('\u223c')); // tilde operator = varies with = similar to, U+223C ISOtech
        // tilde operator is NOT the same character as the tilde, U+007E,
        // although the same glyph might be used to represent both
        MAP.put("cong",Character.valueOf('\u2245')); // approximately equal to, U+2245 ISOtech
        MAP.put("asymp",Character.valueOf('\u2248')); // almost equal to = asymptotic to, U+2248 ISOamsr
        MAP.put("ne",Character.valueOf('\u2260')); // not equal to, U+2260 ISOtech
        MAP.put("equiv",Character.valueOf('\u2261')); // identical to, U+2261 ISOtech
        MAP.put("le",Character.valueOf('\u2264')); // less-than or equal to, U+2264 ISOtech
        MAP.put("ge",Character.valueOf('\u2265')); // greater-than or equal to, U+2265 ISOtech
        MAP.put("sub",Character.valueOf('\u2282')); // subset of, U+2282 ISOtech
        MAP.put("sup",Character.valueOf('\u2283')); // superset of, U+2283 ISOtech
        // note that nsup, 'not a superset of, U+2283' is not covered by the Symbol
        // font encoding and is not included. Should it be, for symmetry?
        // It is in ISOamsn
        MAP.put("nsub",Character.valueOf('\u2284')); // not a subset of, U+2284 ISOamsn
        MAP.put("sube",Character.valueOf('\u2286')); // subset of or equal to, U+2286 ISOtech
        MAP.put("supe",Character.valueOf('\u2287')); // superset of or equal to, U+2287 ISOtech
        MAP.put("oplus",Character.valueOf('\u2295')); // circled plus = direct sum, U+2295 ISOamsb
        MAP.put("otimes",Character.valueOf('\u2297')); // circled times = vector product, U+2297 ISOamsb
        MAP.put("perp",Character.valueOf('\u22a5')); // up tack = orthogonal to = perpendicular, U+22A5 ISOtech
        MAP.put("sdot",Character.valueOf('\u22c5')); // dot operator, U+22C5 ISOamsb
        // dot operator is NOT the same character as U+00B7 middle dot
        // Miscellaneous Technical
        MAP.put("lceil",Character.valueOf('\u2308')); // left ceiling = apl upstile, U+2308 ISOamsc
        MAP.put("rceil",Character.valueOf('\u2309')); // right ceiling, U+2309 ISOamsc
        MAP.put("lfloor",Character.valueOf('\u230a')); // left floor = apl downstile, U+230A ISOamsc
        MAP.put("rfloor",Character.valueOf('\u230b')); // right floor, U+230B ISOamsc
        MAP.put("lang",Character.valueOf('\u2329')); // left-pointing angle bracket = bra, U+2329 ISOtech
        // lang is NOT the same character as U+003C 'less than'
        // or U+2039 'single left-pointing angle quotation mark'
        MAP.put("rang",Character.valueOf('\u232a')); // right-pointing angle bracket = ket, U+232A ISOtech
        // rang is NOT the same character as U+003E 'greater than'
        // or U+203A 'single right-pointing angle quotation mark'
        // Geometric Shapes
        MAP.put("loz",Character.valueOf('\u25ca')); // lozenge, U+25CA ISOpub
        // Miscellaneous Symbols
        MAP.put("spades",Character.valueOf('\u2660')); // black spade suit, U+2660 ISOpub
        // black here seems to mean filled as opposed to hollow
        MAP.put("clubs",Character.valueOf('\u2663')); // black club suit = shamrock, U+2663 ISOpub
        MAP.put("hearts",Character.valueOf('\u2665')); // black heart suit = valentine, U+2665 ISOpub
        MAP.put("diams",Character.valueOf('\u2666')); // black diamond suit, U+2666 ISOpub
        // C0 Controls and Basic Latin
        MAP.put("quot",Character.valueOf('\u0022')); // quotation mark = APL quote, U+0022 ISOnum
        MAP.put("amp",Character.valueOf('\u0026')); // ampersand, U+0026 ISOnum
        MAP.put("apos",Character.valueOf('\''));
        MAP.put("lt",Character.valueOf('\u003c')); // less-than sign, U+003C ISOnum
        MAP.put("gt",Character.valueOf('\u003e')); // greater-than sign, U+003E ISOnum
        // Latin Extended-A
        MAP.put("OElig",Character.valueOf('\u0152')); // latin capital ligature OE, U+0152 ISOlat2
        MAP.put("oelig",Character.valueOf('\u0153')); // latin small ligature oe, U+0153 ISOlat2
        // ligature is a misnomer, this is a separate character in some languages
        MAP.put("Scaron",Character.valueOf('\u0160')); // latin capital letter S with caron, U+0160 ISOlat2
        MAP.put("scaron",Character.valueOf('\u0161')); // latin small letter s with caron, U+0161 ISOlat2
        MAP.put("Yuml",Character.valueOf('\u0178')); // latin capital letter Y with diaeresis, U+0178 ISOlat2
        // Spacing Modifier Letters
        MAP.put("circ",Character.valueOf('\u02c6')); // modifier letter circumflex accent, U+02C6 ISOpub
        MAP.put("tilde",Character.valueOf('\u02dc')); // small tilde, U+02DC ISOdia
        // General Punctuation
        MAP.put("ensp",Character.valueOf('\u2002')); // en space, U+2002 ISOpub
        MAP.put("emsp",Character.valueOf('\u2003')); // em space, U+2003 ISOpub
        MAP.put("thinsp",Character.valueOf('\u2009')); // thin space, U+2009 ISOpub
        MAP.put("zwnj",Character.valueOf('\u200c')); // zero width non-joiner, U+200C NEW RFC 2070
        MAP.put("zwj",Character.valueOf('\u200d')); // zero width joiner, U+200D NEW RFC 2070
        MAP.put("lrm",Character.valueOf('\u200e')); // left-to-right mark, U+200E NEW RFC 2070
        MAP.put("rlm",Character.valueOf('\u200f')); // right-to-left mark, U+200F NEW RFC 2070
        MAP.put("ndash",Character.valueOf('\u2013')); // en dash, U+2013 ISOpub
        MAP.put("mdash",Character.valueOf('\u2014')); // em dash, U+2014 ISOpub
        MAP.put("lsquo",Character.valueOf('\u2018')); // left single quotation mark, U+2018 ISOnum
        MAP.put("rsquo",Character.valueOf('\u2019')); // right single quotation mark, U+2019 ISOnum
        MAP.put("sbquo",Character.valueOf('\u201a')); // single low-9 quotation mark, U+201A NEW
        MAP.put("ldquo",Character.valueOf('\u201c')); // left double quotation mark, U+201C ISOnum
        MAP.put("rdquo",Character.valueOf('\u201d')); // right double quotation mark, U+201D ISOnum
        MAP.put("bdquo",Character.valueOf('\u201e')); // double low-9 quotation mark, U+201E NEW
        MAP.put("dagger",Character.valueOf('\u2020')); // dagger, U+2020 ISOpub
        MAP.put("Dagger",Character.valueOf('\u2021')); // double dagger, U+2021 ISOpub
        MAP.put("permil",Character.valueOf('\u2030')); // per mille sign, U+2030 ISOtech
        MAP.put("lsaquo",Character.valueOf('\u2039')); // single left-pointing angle quotation mark, U+2039 ISO proposed
        // lsaquo is proposed but not yet ISO standardized
        MAP.put("rsaquo",Character.valueOf('\u203a')); // single right-pointing angle quotation mark, U+203A ISO proposed
        // rsaquo is proposed but not yet ISO standardized
        MAP.put("euro",Character.valueOf('\u20ac')); // euro sign, U+20AC NEW
    }


    /**
     * Translates an entity to a unicode character.
     *
     * @param	name	the name of the entity
     * @return	the corresponding unicode character
     */
    public static char decodeEntity(final String name) {
    	if (name.startsWith("#x")) {
    		try {
    			return (char)Integer.parseInt(name.substring(2),16);
    		}
    		catch(NumberFormatException nfe) {
    			return '\0';
    		}
    	}
    	if (name.startsWith("#")) {
    		try {
    			return (char)Integer.parseInt(name.substring(1));
    		}
    		catch(NumberFormatException nfe) {
    			return '\0';
    		}
    	}
    	Character c = MAP.get(name);
        if (c == null)
            return '\0';
        else
            return c.charValue();
    }

    /**
     * Translates a String with entities (&...;) to a String without entities,
     * replacing the entity with the right (unicode) character.
     */
    public static String decodeString(final String s) {
    	int pos_amp = s.indexOf('&');
    	if (pos_amp == -1) return s;

    	int pos_sc;
    	int pos_a;
    	StringBuffer buf = new StringBuffer(s.substring(0, pos_amp));
    	char replace;
    	while (true) {
    		pos_sc = s.indexOf(';', pos_amp);
    		if (pos_sc == -1) {
    			buf.append(s.substring(pos_amp));
    			return buf.toString();
    		}
    		pos_a = s.indexOf('&', pos_amp + 1);
    		while (pos_a != -1 && pos_a < pos_sc) {
    			buf.append(s.substring(pos_amp, pos_a));
    			pos_amp = pos_a;
    			pos_a = s.indexOf('&', pos_amp + 1);
    		}
    		replace = decodeEntity(s.substring(pos_amp + 1, pos_sc));
    		if (s.length() < pos_sc + 1) {
    			return buf.toString();
    		}
    		if (replace == '\0') {
    			buf.append(s.substring(pos_amp, pos_sc + 1));
    		}
    		else {
    			buf.append(replace);
    		}
    		pos_amp = s.indexOf('&', pos_sc);
    		if (pos_amp == -1) {
    			buf.append(s.substring(pos_sc + 1));
    			return buf.toString();
    		}
    		else {
    			buf.append(s.substring(pos_sc + 1, pos_amp));
    		}
    	}
    }
}
