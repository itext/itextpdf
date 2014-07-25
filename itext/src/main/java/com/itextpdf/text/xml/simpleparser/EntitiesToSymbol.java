/*
 * $Id: EntitiesToSymbol.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;

/**
 * This class contains entities that can be used in an entity tag.
 */

public class EntitiesToSymbol {

    /**
     * This is a map that contains all possible id values of the entity tag
     * that can be translated to a character in font Symbol.
     */
    private static final Map<String, Character> MAP;

    static {
        MAP = new HashMap<String, Character>();
        MAP.put("169", Character.valueOf((char)227));
        MAP.put("172", Character.valueOf((char)216));
        MAP.put("174", Character.valueOf((char)210));
        MAP.put("177", Character.valueOf((char)177));
        MAP.put("215", Character.valueOf((char)180));
        MAP.put("247", Character.valueOf((char)184));
        MAP.put("8230", Character.valueOf((char)188));
        MAP.put("8242", Character.valueOf((char)162));
        MAP.put("8243", Character.valueOf((char)178));
        MAP.put("8260", Character.valueOf((char)164));
        MAP.put("8364", Character.valueOf((char)240));
        MAP.put("8465", Character.valueOf((char)193));
        MAP.put("8472", Character.valueOf((char)195));
        MAP.put("8476", Character.valueOf((char)194));
        MAP.put("8482", Character.valueOf((char)212));
        MAP.put("8501", Character.valueOf((char)192));
        MAP.put("8592", Character.valueOf((char)172));
        MAP.put("8593", Character.valueOf((char)173));
        MAP.put("8594", Character.valueOf((char)174));
        MAP.put("8595", Character.valueOf((char)175));
        MAP.put("8596", Character.valueOf((char)171));
        MAP.put("8629", Character.valueOf((char)191));
        MAP.put("8656", Character.valueOf((char)220));
        MAP.put("8657", Character.valueOf((char)221));
        MAP.put("8658", Character.valueOf((char)222));
        MAP.put("8659", Character.valueOf((char)223));
        MAP.put("8660", Character.valueOf((char)219));
        MAP.put("8704", Character.valueOf((char)34));
        MAP.put("8706", Character.valueOf((char)182));
        MAP.put("8707", Character.valueOf((char)36));
        MAP.put("8709", Character.valueOf((char)198));
        MAP.put("8711", Character.valueOf((char)209));
        MAP.put("8712", Character.valueOf((char)206));
        MAP.put("8713", Character.valueOf((char)207));
        MAP.put("8717", Character.valueOf((char)39));
        MAP.put("8719", Character.valueOf((char)213));
        MAP.put("8721", Character.valueOf((char)229));
        MAP.put("8722", Character.valueOf((char)45));
        MAP.put("8727", Character.valueOf((char)42));
        MAP.put("8729", Character.valueOf((char)183));
        MAP.put("8730", Character.valueOf((char)214));
        MAP.put("8733", Character.valueOf((char)181));
        MAP.put("8734", Character.valueOf((char)165));
        MAP.put("8736", Character.valueOf((char)208));
        MAP.put("8743", Character.valueOf((char)217));
        MAP.put("8744", Character.valueOf((char)218));
        MAP.put("8745", Character.valueOf((char)199));
        MAP.put("8746", Character.valueOf((char)200));
        MAP.put("8747", Character.valueOf((char)242));
        MAP.put("8756", Character.valueOf((char)92));
        MAP.put("8764", Character.valueOf((char)126));
        MAP.put("8773", Character.valueOf((char)64));
        MAP.put("8776", Character.valueOf((char)187));
        MAP.put("8800", Character.valueOf((char)185));
        MAP.put("8801", Character.valueOf((char)186));
        MAP.put("8804", Character.valueOf((char)163));
        MAP.put("8805", Character.valueOf((char)179));
        MAP.put("8834", Character.valueOf((char)204));
        MAP.put("8835", Character.valueOf((char)201));
        MAP.put("8836", Character.valueOf((char)203));
        MAP.put("8838", Character.valueOf((char)205));
        MAP.put("8839", Character.valueOf((char)202));
        MAP.put("8853", Character.valueOf((char)197));
        MAP.put("8855", Character.valueOf((char)196));
        MAP.put("8869", Character.valueOf((char)94));
        MAP.put("8901", Character.valueOf((char)215));
        MAP.put("8992", Character.valueOf((char)243));
        MAP.put("8993", Character.valueOf((char)245));
        MAP.put("9001", Character.valueOf((char)225));
        MAP.put("9002", Character.valueOf((char)241));
        MAP.put("913", Character.valueOf((char)65));
        MAP.put("914", Character.valueOf((char)66));
        MAP.put("915", Character.valueOf((char)71));
        MAP.put("916", Character.valueOf((char)68));
        MAP.put("917", Character.valueOf((char)69));
        MAP.put("918", Character.valueOf((char)90));
        MAP.put("919", Character.valueOf((char)72));
        MAP.put("920", Character.valueOf((char)81));
        MAP.put("921", Character.valueOf((char)73));
        MAP.put("922", Character.valueOf((char)75));
        MAP.put("923", Character.valueOf((char)76));
        MAP.put("924", Character.valueOf((char)77));
        MAP.put("925", Character.valueOf((char)78));
        MAP.put("926", Character.valueOf((char)88));
        MAP.put("927", Character.valueOf((char)79));
        MAP.put("928", Character.valueOf((char)80));
        MAP.put("929", Character.valueOf((char)82));
        MAP.put("931", Character.valueOf((char)83));
        MAP.put("932", Character.valueOf((char)84));
        MAP.put("933", Character.valueOf((char)85));
        MAP.put("934", Character.valueOf((char)70));
        MAP.put("935", Character.valueOf((char)67));
        MAP.put("936", Character.valueOf((char)89));
        MAP.put("937", Character.valueOf((char)87));
        MAP.put("945", Character.valueOf((char)97));
        MAP.put("946", Character.valueOf((char)98));
        MAP.put("947", Character.valueOf((char)103));
        MAP.put("948", Character.valueOf((char)100));
        MAP.put("949", Character.valueOf((char)101));
        MAP.put("950", Character.valueOf((char)122));
        MAP.put("951", Character.valueOf((char)104));
        MAP.put("952", Character.valueOf((char)113));
        MAP.put("953", Character.valueOf((char)105));
        MAP.put("954", Character.valueOf((char)107));
        MAP.put("955", Character.valueOf((char)108));
        MAP.put("956", Character.valueOf((char)109));
        MAP.put("957", Character.valueOf((char)110));
        MAP.put("958", Character.valueOf((char)120));
        MAP.put("959", Character.valueOf((char)111));
        MAP.put("960", Character.valueOf((char)112));
        MAP.put("961", Character.valueOf((char)114));
        MAP.put("962", Character.valueOf((char)86));
        MAP.put("963", Character.valueOf((char)115));
        MAP.put("964", Character.valueOf((char)116));
        MAP.put("965", Character.valueOf((char)117));
        MAP.put("966", Character.valueOf((char)102));
        MAP.put("967", Character.valueOf((char)99));
        MAP.put("9674", Character.valueOf((char)224));
        MAP.put("968", Character.valueOf((char)121));
        MAP.put("969", Character.valueOf((char)119));
        MAP.put("977", Character.valueOf((char)74));
        MAP.put("978", Character.valueOf((char)161));
        MAP.put("981", Character.valueOf((char)106));
        MAP.put("982", Character.valueOf((char)118));
        MAP.put("9824", Character.valueOf((char)170));
        MAP.put("9827", Character.valueOf((char)167));
        MAP.put("9829", Character.valueOf((char)169));
        MAP.put("9830", Character.valueOf((char)168));
        MAP.put("Alpha", Character.valueOf((char)65));
        MAP.put("Beta", Character.valueOf((char)66));
        MAP.put("Chi", Character.valueOf((char)67));
        MAP.put("Delta", Character.valueOf((char)68));
        MAP.put("Epsilon", Character.valueOf((char)69));
        MAP.put("Eta", Character.valueOf((char)72));
        MAP.put("Gamma", Character.valueOf((char)71));
        MAP.put("Iota", Character.valueOf((char)73));
        MAP.put("Kappa", Character.valueOf((char)75));
        MAP.put("Lambda", Character.valueOf((char)76));
        MAP.put("Mu", Character.valueOf((char)77));
        MAP.put("Nu", Character.valueOf((char)78));
        MAP.put("Omega", Character.valueOf((char)87));
        MAP.put("Omicron", Character.valueOf((char)79));
        MAP.put("Phi", Character.valueOf((char)70));
        MAP.put("Pi", Character.valueOf((char)80));
        MAP.put("Prime", Character.valueOf((char)178));
        MAP.put("Psi", Character.valueOf((char)89));
        MAP.put("Rho", Character.valueOf((char)82));
        MAP.put("Sigma", Character.valueOf((char)83));
        MAP.put("Tau", Character.valueOf((char)84));
        MAP.put("Theta", Character.valueOf((char)81));
        MAP.put("Upsilon", Character.valueOf((char)85));
        MAP.put("Xi", Character.valueOf((char)88));
        MAP.put("Zeta", Character.valueOf((char)90));
        MAP.put("alefsym", Character.valueOf((char)192));
        MAP.put("alpha", Character.valueOf((char)97));
        MAP.put("and", Character.valueOf((char)217));
        MAP.put("ang", Character.valueOf((char)208));
        MAP.put("asymp", Character.valueOf((char)187));
        MAP.put("beta", Character.valueOf((char)98));
        MAP.put("cap", Character.valueOf((char)199));
        MAP.put("chi", Character.valueOf((char)99));
        MAP.put("clubs", Character.valueOf((char)167));
        MAP.put("cong", Character.valueOf((char)64));
        MAP.put("copy", Character.valueOf((char)211));
        MAP.put("crarr", Character.valueOf((char)191));
        MAP.put("cup", Character.valueOf((char)200));
        MAP.put("dArr", Character.valueOf((char)223));
        MAP.put("darr", Character.valueOf((char)175));
        MAP.put("delta", Character.valueOf((char)100));
        MAP.put("diams", Character.valueOf((char)168));
        MAP.put("divide", Character.valueOf((char)184));
        MAP.put("empty", Character.valueOf((char)198));
        MAP.put("epsilon", Character.valueOf((char)101));
        MAP.put("equiv", Character.valueOf((char)186));
        MAP.put("eta", Character.valueOf((char)104));
        MAP.put("euro", Character.valueOf((char)240));
        MAP.put("exist", Character.valueOf((char)36));
        MAP.put("forall", Character.valueOf((char)34));
        MAP.put("frasl", Character.valueOf((char)164));
        MAP.put("gamma", Character.valueOf((char)103));
        MAP.put("ge", Character.valueOf((char)179));
        MAP.put("hArr", Character.valueOf((char)219));
        MAP.put("harr", Character.valueOf((char)171));
        MAP.put("hearts", Character.valueOf((char)169));
        MAP.put("hellip", Character.valueOf((char)188));
        MAP.put("horizontal arrow extender", Character.valueOf((char)190));
        MAP.put("image", Character.valueOf((char)193));
        MAP.put("infin", Character.valueOf((char)165));
        MAP.put("int", Character.valueOf((char)242));
        MAP.put("iota", Character.valueOf((char)105));
        MAP.put("isin", Character.valueOf((char)206));
        MAP.put("kappa", Character.valueOf((char)107));
        MAP.put("lArr", Character.valueOf((char)220));
        MAP.put("lambda", Character.valueOf((char)108));
        MAP.put("lang", Character.valueOf((char)225));
        MAP.put("large brace extender", Character.valueOf((char)239));
        MAP.put("large integral extender", Character.valueOf((char)244));
        MAP.put("large left brace (bottom)", Character.valueOf((char)238));
        MAP.put("large left brace (middle)", Character.valueOf((char)237));
        MAP.put("large left brace (top)", Character.valueOf((char)236));
        MAP.put("large left bracket (bottom)", Character.valueOf((char)235));
        MAP.put("large left bracket (extender)", Character.valueOf((char)234));
        MAP.put("large left bracket (top)", Character.valueOf((char)233));
        MAP.put("large left parenthesis (bottom)", Character.valueOf((char)232));
        MAP.put("large left parenthesis (extender)", Character.valueOf((char)231));
        MAP.put("large left parenthesis (top)", Character.valueOf((char)230));
        MAP.put("large right brace (bottom)", Character.valueOf((char)254));
        MAP.put("large right brace (middle)", Character.valueOf((char)253));
        MAP.put("large right brace (top)", Character.valueOf((char)252));
        MAP.put("large right bracket (bottom)", Character.valueOf((char)251));
        MAP.put("large right bracket (extender)", Character.valueOf((char)250));
        MAP.put("large right bracket (top)", Character.valueOf((char)249));
        MAP.put("large right parenthesis (bottom)", Character.valueOf((char)248));
        MAP.put("large right parenthesis (extender)", Character.valueOf((char)247));
        MAP.put("large right parenthesis (top)", Character.valueOf((char)246));
        MAP.put("larr", Character.valueOf((char)172));
        MAP.put("le", Character.valueOf((char)163));
        MAP.put("lowast", Character.valueOf((char)42));
        MAP.put("loz", Character.valueOf((char)224));
        MAP.put("minus", Character.valueOf((char)45));
        MAP.put("mu", Character.valueOf((char)109));
        MAP.put("nabla", Character.valueOf((char)209));
        MAP.put("ne", Character.valueOf((char)185));
        MAP.put("not", Character.valueOf((char)216));
        MAP.put("notin", Character.valueOf((char)207));
        MAP.put("nsub", Character.valueOf((char)203));
        MAP.put("nu", Character.valueOf((char)110));
        MAP.put("omega", Character.valueOf((char)119));
        MAP.put("omicron", Character.valueOf((char)111));
        MAP.put("oplus", Character.valueOf((char)197));
        MAP.put("or", Character.valueOf((char)218));
        MAP.put("otimes", Character.valueOf((char)196));
        MAP.put("part", Character.valueOf((char)182));
        MAP.put("perp", Character.valueOf((char)94));
        MAP.put("phi", Character.valueOf((char)102));
        MAP.put("pi", Character.valueOf((char)112));
        MAP.put("piv", Character.valueOf((char)118));
        MAP.put("plusmn", Character.valueOf((char)177));
        MAP.put("prime", Character.valueOf((char)162));
        MAP.put("prod", Character.valueOf((char)213));
        MAP.put("prop", Character.valueOf((char)181));
        MAP.put("psi", Character.valueOf((char)121));
        MAP.put("rArr", Character.valueOf((char)222));
        MAP.put("radic", Character.valueOf((char)214));
        MAP.put("radical extender", Character.valueOf((char)96));
        MAP.put("rang", Character.valueOf((char)241));
        MAP.put("rarr", Character.valueOf((char)174));
        MAP.put("real", Character.valueOf((char)194));
        MAP.put("reg", Character.valueOf((char)210));
        MAP.put("rho", Character.valueOf((char)114));
        MAP.put("sdot", Character.valueOf((char)215));
        MAP.put("sigma", Character.valueOf((char)115));
        MAP.put("sigmaf", Character.valueOf((char)86));
        MAP.put("sim", Character.valueOf((char)126));
        MAP.put("spades", Character.valueOf((char)170));
        MAP.put("sub", Character.valueOf((char)204));
        MAP.put("sube", Character.valueOf((char)205));
        MAP.put("sum", Character.valueOf((char)229));
        MAP.put("sup", Character.valueOf((char)201));
        MAP.put("supe", Character.valueOf((char)202));
        MAP.put("tau", Character.valueOf((char)116));
        MAP.put("there4", Character.valueOf((char)92));
        MAP.put("theta", Character.valueOf((char)113));
        MAP.put("thetasym", Character.valueOf((char)74));
        MAP.put("times", Character.valueOf((char)180));
        MAP.put("trade", Character.valueOf((char)212));
        MAP.put("uArr", Character.valueOf((char)221));
        MAP.put("uarr", Character.valueOf((char)173));
        MAP.put("upsih", Character.valueOf((char)161));
        MAP.put("upsilon", Character.valueOf((char)117));
        MAP.put("vertical arrow extender", Character.valueOf((char)189));
        MAP.put("weierp", Character.valueOf((char)195));
        MAP.put("xi", Character.valueOf((char)120));
        MAP.put("zeta", Character.valueOf((char)122));
    }

    /**
     * Gets a chunk with a symbol character.
     * @param e a symbol value (see Entities class: alfa is greek alfa,...)
     * @param font the font if the symbol isn't found (otherwise Font.SYMBOL)
     * @return a Chunk
     */
    public static Chunk get(final String e, final Font font) {
        char s = getCorrespondingSymbol(e);
        if (s == (char)0) {
            try {
                return new Chunk(String.valueOf((char)Integer.parseInt(e)), font);
            }
            catch(Exception exception) {
                return new Chunk(e, font);
            }
        }
        Font symbol = new Font(FontFamily.SYMBOL, font.getSize(), font.getStyle(), font.getColor());
        return new Chunk(String.valueOf(s), symbol);
    }

    /**
     * Looks for the corresponding symbol in the font Symbol.
     *
     * @param	name	the name of the entity
     * @return	the corresponding character in font Symbol
     */
    public static char getCorrespondingSymbol(final String name) {
        Character symbol = MAP.get(name);
        if (symbol == null) {
            return (char)0;
        }
        return symbol.charValue();
    }
}
