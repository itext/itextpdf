/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import java.util.HashMap;

/**
 * This class contains entities that can be used in an entity tag.
 */

public class Entities {
    
    /** This is a map that contains all possible id values of the entity tag. */
    public static final HashMap map;
    
    static {
        map = new HashMap();
        map.put("169", new Integer(227));
        map.put("172", new Integer(216));
        map.put("174", new Integer(210));
        map.put("177", new Integer(177));
        map.put("215", new Integer(180));
        map.put("247", new Integer(184));
        map.put("8230", new Integer(188));
        map.put("8242", new Integer(162));
        map.put("8243", new Integer(178));
        map.put("8260", new Integer(164));
        map.put("8364", new Integer(240));
        map.put("8465", new Integer(193));
        map.put("8472", new Integer(195));
        map.put("8476", new Integer(194));
        map.put("8482", new Integer(212));
        map.put("8501", new Integer(192));
        map.put("8592", new Integer(172));
        map.put("8593", new Integer(173));
        map.put("8594", new Integer(174));
        map.put("8595", new Integer(175));
        map.put("8596", new Integer(171));
        map.put("8629", new Integer(191));
        map.put("8656", new Integer(220));
        map.put("8657", new Integer(221));
        map.put("8658", new Integer(222));
        map.put("8659", new Integer(223));
        map.put("8660", new Integer(219));
        map.put("8704", new Integer(34));
        map.put("8706", new Integer(182));
        map.put("8707", new Integer(36));
        map.put("8709", new Integer(198));
        map.put("8711", new Integer(209));
        map.put("8712", new Integer(206));
        map.put("8713", new Integer(207));
        map.put("8717", new Integer(39));
        map.put("8719", new Integer(213));
        map.put("8721", new Integer(229));
        map.put("8722", new Integer(45));
        map.put("8727", new Integer(42));
        map.put("8729", new Integer(183));
        map.put("8730", new Integer(214));
        map.put("8733", new Integer(181));
        map.put("8734", new Integer(165));
        map.put("8736", new Integer(208));
        map.put("8743", new Integer(217));
        map.put("8744", new Integer(218));
        map.put("8745", new Integer(199));
        map.put("8746", new Integer(200));
        map.put("8747", new Integer(242));
        map.put("8756", new Integer(92));
        map.put("8764", new Integer(126));
        map.put("8773", new Integer(64));
        map.put("8776", new Integer(187));
        map.put("8800", new Integer(185));
        map.put("8801", new Integer(186));
        map.put("8804", new Integer(163));
        map.put("8805", new Integer(179));
        map.put("8834", new Integer(204));
        map.put("8835", new Integer(201));
        map.put("8836", new Integer(203));
        map.put("8838", new Integer(205));
        map.put("8839", new Integer(202));
        map.put("8853", new Integer(197));
        map.put("8855", new Integer(196));
        map.put("8869", new Integer(94));
        map.put("8901", new Integer(215));
        map.put("8992", new Integer(243));
        map.put("8993", new Integer(245));
        map.put("9001", new Integer(225));
        map.put("9002", new Integer(241));
        map.put("913", new Integer(65));
        map.put("914", new Integer(66));
        map.put("915", new Integer(71));
        map.put("916", new Integer(68));
        map.put("917", new Integer(69));
        map.put("918", new Integer(90));
        map.put("919", new Integer(72));
        map.put("920", new Integer(81));
        map.put("921", new Integer(73));
        map.put("922", new Integer(75));
        map.put("923", new Integer(76));
        map.put("924", new Integer(77));
        map.put("925", new Integer(78));
        map.put("926", new Integer(88));
        map.put("927", new Integer(79));
        map.put("928", new Integer(80));
        map.put("929", new Integer(82));
        map.put("931", new Integer(83));
        map.put("932", new Integer(84));
        map.put("933", new Integer(85));
        map.put("934", new Integer(70));
        map.put("935", new Integer(67));
        map.put("936", new Integer(89));
        map.put("937", new Integer(87));
        map.put("945", new Integer(97));
        map.put("946", new Integer(98));
        map.put("947", new Integer(103));
        map.put("948", new Integer(100));
        map.put("949", new Integer(101));
        map.put("950", new Integer(122));
        map.put("951", new Integer(104));
        map.put("952", new Integer(113));
        map.put("953", new Integer(105));
        map.put("954", new Integer(107));
        map.put("955", new Integer(108));
        map.put("956", new Integer(109));
        map.put("957", new Integer(110));
        map.put("958", new Integer(120));
        map.put("959", new Integer(111));
        map.put("960", new Integer(112));
        map.put("961", new Integer(114));
        map.put("962", new Integer(86));
        map.put("963", new Integer(115));
        map.put("964", new Integer(116));
        map.put("965", new Integer(117));
        map.put("966", new Integer(102));
        map.put("967", new Integer(99));
        map.put("9674", new Integer(224));
        map.put("968", new Integer(121));
        map.put("969", new Integer(119));
        map.put("977", new Integer(74));
        map.put("978", new Integer(161));
        map.put("981", new Integer(106));
        map.put("982", new Integer(118));
        map.put("9824", new Integer(170));
        map.put("9827", new Integer(167));
        map.put("9829", new Integer(169));
        map.put("9830", new Integer(168));
        map.put("Alpha", new Integer(65));
        map.put("Beta", new Integer(66));
        map.put("Chi", new Integer(67));
        map.put("Delta", new Integer(68));
        map.put("Epsilon", new Integer(69));
        map.put("Eta", new Integer(72));
        map.put("Gamma", new Integer(71));
        map.put("Iota", new Integer(73));
        map.put("Kappa", new Integer(75));
        map.put("Lambda", new Integer(76));
        map.put("Mu", new Integer(77));
        map.put("Nu", new Integer(78));
        map.put("Omega", new Integer(87));
        map.put("Omicron", new Integer(79));
        map.put("Phi", new Integer(70));
        map.put("Pi", new Integer(80));
        map.put("Prime", new Integer(178));
        map.put("Psi", new Integer(89));
        map.put("Rho", new Integer(82));
        map.put("Sigma", new Integer(83));
        map.put("Tau", new Integer(84));
        map.put("Theta", new Integer(81));
        map.put("Upsilon", new Integer(85));
        map.put("Xi", new Integer(88));
        map.put("Zeta", new Integer(90));
        map.put("alefsym", new Integer(192));
        map.put("alpha", new Integer(97));
        map.put("and", new Integer(217));
        map.put("ang", new Integer(208));
        map.put("asymp", new Integer(187));
        map.put("beta", new Integer(98));
        map.put("cap", new Integer(199));
        map.put("chi", new Integer(99));
        map.put("clubs", new Integer(167));
        map.put("cong", new Integer(64));
        map.put("copy", new Integer(211));
        map.put("crarr", new Integer(191));
        map.put("cup", new Integer(200));
        map.put("dArr", new Integer(223));
        map.put("darr", new Integer(175));
        map.put("delta", new Integer(100));
        map.put("diams", new Integer(168));
        map.put("divide", new Integer(184));
        map.put("empty", new Integer(198));
        map.put("epsilon", new Integer(101));
        map.put("equiv", new Integer(186));
        map.put("eta", new Integer(104));
        map.put("euro", new Integer(240));
        map.put("exist", new Integer(36));
        map.put("forall", new Integer(34));
        map.put("frasl", new Integer(164));
        map.put("gamma", new Integer(103));
        map.put("ge", new Integer(179));
        map.put("hArr", new Integer(219));
        map.put("harr", new Integer(171));
        map.put("hearts", new Integer(169));
        map.put("hellip", new Integer(188));
        map.put("horizontal arrow extender", new Integer(190));
        map.put("image", new Integer(193));
        map.put("infin", new Integer(165));
        map.put("int", new Integer(242));
        map.put("iota", new Integer(105));
        map.put("isin", new Integer(206));
        map.put("kappa", new Integer(107));
        map.put("lArr", new Integer(220));
        map.put("lambda", new Integer(108));
        map.put("lang", new Integer(225));
        map.put("large brace extender", new Integer(239));
        map.put("large integral extender", new Integer(244));
        map.put("large left brace (bottom)", new Integer(238));
        map.put("large left brace (middle)", new Integer(237));
        map.put("large left brace (top)", new Integer(236));
        map.put("large left bracket (bottom)", new Integer(235));
        map.put("large left bracket (extender)", new Integer(234));
        map.put("large left bracket (top)", new Integer(233));
        map.put("large left parenthesis (bottom)", new Integer(232));
        map.put("large left parenthesis (extender)", new Integer(231));
        map.put("large left parenthesis (top)", new Integer(230));
        map.put("large right brace (bottom)", new Integer(254));
        map.put("large right brace (middle)", new Integer(253));
        map.put("large right brace (top)", new Integer(252));
        map.put("large right bracket (bottom)", new Integer(251));
        map.put("large right bracket (extender)", new Integer(250));
        map.put("large right bracket (top)", new Integer(249));
        map.put("large right parenthesis (bottom)", new Integer(248));
        map.put("large right parenthesis (extender)", new Integer(247));
        map.put("large right parenthesis (top)", new Integer(246));
        map.put("larr", new Integer(172));
        map.put("le", new Integer(163));
        map.put("lowast", new Integer(42));
        map.put("loz", new Integer(224));
        map.put("minus", new Integer(45));
        map.put("mu", new Integer(109));
        map.put("nabla", new Integer(209));
        map.put("ne", new Integer(185));
        map.put("not", new Integer(216));
        map.put("notin", new Integer(207));
        map.put("nsub", new Integer(203));
        map.put("nu", new Integer(110));
        map.put("omega", new Integer(119));
        map.put("omicron", new Integer(111));
        map.put("oplus", new Integer(197));
        map.put("or", new Integer(218));
        map.put("otimes", new Integer(196));
        map.put("part", new Integer(182));
        map.put("perp", new Integer(94));
        map.put("phi", new Integer(102));
        map.put("pi", new Integer(112));
        map.put("piv", new Integer(118));
        map.put("plusmn", new Integer(177));
        map.put("prime", new Integer(162));
        map.put("prod", new Integer(213));
        map.put("prop", new Integer(181));
        map.put("psi", new Integer(121));
        map.put("rArr", new Integer(222));
        map.put("radic", new Integer(214));
        map.put("radical extender", new Integer(96));
        map.put("rang", new Integer(241));
        map.put("rarr", new Integer(174));
        map.put("real", new Integer(194));
        map.put("reg", new Integer(210));
        map.put("rho", new Integer(114));
        map.put("sdot", new Integer(215));
        map.put("sigma", new Integer(115));
        map.put("sigmaf", new Integer(86));
        map.put("sim", new Integer(126));
        map.put("spades", new Integer(170));
        map.put("sub", new Integer(204));
        map.put("sube", new Integer(205));
        map.put("sum", new Integer(229));
        map.put("sup", new Integer(201));
        map.put("supe", new Integer(202));
        map.put("tau", new Integer(116));
        map.put("there4", new Integer(92));
        map.put("theta", new Integer(113));
        map.put("thetasym", new Integer(74));
        map.put("times", new Integer(180));
        map.put("trade", new Integer(212));
        map.put("uArr", new Integer(221));
        map.put("uarr", new Integer(173));
        map.put("upsih", new Integer(161));
        map.put("upsilon", new Integer(117));
        map.put("vertical arrow extender", new Integer(189));
        map.put("weierp", new Integer(195));
        map.put("xi", new Integer(120));
        map.put("zeta", new Integer(122));
    }
    
 /**
  * Gets a chunk with a symbol character.
  */
    
    public static Chunk get(String e, Font font) {
        int s = getCorrespondingSymbol(e);
        if (s == -1) {
            try {
                return new Chunk(String.valueOf((char)Integer.parseInt(e)), font);
            }
            catch(Exception exception) {
                return new Chunk(e, font);
            }
        }
        Font symbol = new Font(Font.SYMBOL, font.size(), font.style(), font.color());
        return new Chunk(String.valueOf((char)s), symbol);
    }
    
/**
 * Looks for the corresponding symbol in the font Symbol.
 *
 * @param	c	the original ASCII-char
 * @return	the corresponding symbol in font Symbol
 */
    
    public static int getCorrespondingSymbol(String e) {
        Integer integer = (Integer) map.get(e);
        if (integer == null) {
            return -1;
        }
        return integer.intValue();
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.ENTITY.equals(tag);
    }
}
