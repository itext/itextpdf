/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Bruno Lowagie.
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
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
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

package com.lowagie.text.markup;

import java.awt.Color;
import java.util.HashMap;

/**
 * This class is a HashMap that contains the names of colors as a key
 * and the corresponding Color as value.
 * (Source: Wikipedia http://en.wikipedia.org/wiki/Web_colors )
 * 
 * @author blowagie
 */
public class RGBColors extends HashMap {

	public static final RGBColors NAMES = new RGBColors();
	static {
		NAMES.put("aliceblue", new Color(0xf0, 0xf8, 0xff));
		NAMES.put("antiquewhite", new Color(0xfa, 0xeb, 0xd7));
		NAMES.put("aqua", new Color(0x00, 0xff, 0xff));
		NAMES.put("aquamarine", new Color(0x7f, 0xff, 0xd4));
		NAMES.put("aquamarine", new Color(0x7f, 0xff, 0xd4));
		NAMES.put("azure", new Color(0xf0, 0xff, 0xff));
		NAMES.put("beige", new Color(0xf5, 0xf5, 0xdc));
		NAMES.put("bisque", new Color(0xff, 0xe4, 0xc4));
		NAMES.put("black", new Color(0x00, 0x00, 0x00));
		NAMES.put("blanchedalmond", new Color(0xff, 0xeb, 0xcd));
		NAMES.put("blue", new Color(0x00, 0x00, 0xff));
		NAMES.put("blueviolet", new Color(0x8a, 0x2b, 0xe2));
		NAMES.put("brown", new Color(0xa5, 0x2a, 0x2a));
		NAMES.put("burlywood", new Color(0xde, 0xb8, 0x87));
		NAMES.put("cadetblue", new Color(0x5f, 0x9e, 0xa0));
		NAMES.put("chartreuse", new Color(0x7f, 0xff, 0x00));
		NAMES.put("chocolate", new Color(0xd2, 0x69, 0x1e));
		NAMES.put("coral", new Color(0xff, 0x7f, 0x50));
		NAMES.put("cornflowerblue", new Color(0x64, 0x95, 0xed));
		NAMES.put("cornsilk", new Color(0xff, 0xf8, 0xdc));
		NAMES.put("crimson", new Color(0xdc, 0x14, 0x3c));
		NAMES.put("cyan", new Color(0x00, 0xff, 0xff));
		NAMES.put("darkblue", new Color(0x00, 0x00, 0x8b));
		NAMES.put("darkcyan", new Color(0x00, 0x8b, 0x8b));
		NAMES.put("darkgoldenrod", new Color(0xb8, 0x86, 0x0b));
		NAMES.put("darkgray", new Color(0xa9, 0xa9, 0xa9));
		NAMES.put("darkgreen", new Color(0x00, 0x64, 0x00));
		NAMES.put("darkkhaki", new Color(0xbd, 0xb7, 0x6b));
		NAMES.put("darkmagenta", new Color(0x8b, 0x00, 0x8b));
		NAMES.put("darkolivegreen", new Color(0x55, 0x6b, 0x2f));
		NAMES.put("darkorange", new Color(0xff, 0x8c, 0x00));
		NAMES.put("darkorchid", new Color(0x99, 0x32, 0xcc));
		NAMES.put("darkred", new Color(0x8b, 0x00, 0x00));
		NAMES.put("darkred", new Color(0x8b, 0x00, 0x00));
		NAMES.put("darksalmon", new Color(0xe9, 0x96, 0x7a));
		NAMES.put("darkseagreen", new Color(0x8f, 0xbc, 0x8f));
		NAMES.put("darkslateblue", new Color(0x48, 0x3d, 0x8b));
		NAMES.put("darkslategray", new Color(0x2f, 0x4f, 0x4f));
		NAMES.put("darkturquoise", new Color(0x00, 0xce, 0xd1));
		NAMES.put("darkviolet", new Color(0x94, 0x00, 0xd3));
		NAMES.put("deeppink", new Color(0xff, 0x14, 0x93));
		NAMES.put("deepskyblue", new Color(0x00, 0xbf, 0xff));
		NAMES.put("dimgray", new Color(0x69, 0x69, 0x69));
		NAMES.put("dodgerblue", new Color(0x1e, 0x90, 0xff));
		NAMES.put("firebrick", new Color(0xb2, 0x22, 0x22));
		NAMES.put("floralwhite", new Color(0xff, 0xfa, 0xf0));
		NAMES.put("forestgreen", new Color(0x22, 0x8b, 0x22));
		NAMES.put("fuchsia", new Color(0xff, 0x00, 0xff));
		NAMES.put("gainsboro", new Color(0xdc, 0xdc, 0xdc));
		NAMES.put("ghostwhite", new Color(0xf8, 0xf8, 0xff));
		NAMES.put("gold", new Color(0xff, 0xd7, 0x00));
		NAMES.put("goldenrod", new Color(0xda, 0xa5, 0x20));
		NAMES.put("gray", new Color(0x80, 0x80, 0x80));
		NAMES.put("green", new Color(0x00, 0x80, 0x00));
		NAMES.put("greenyellow", new Color(0xad, 0xff, 0x2f));
		NAMES.put("honeydew", new Color(0xf0, 0xff, 0xf0));
		NAMES.put("hotpink", new Color(0xff, 0x69, 0xb4));
		NAMES.put("indianred", new Color(0xcd, 0x5c, 0x5c));
		NAMES.put("indigo", new Color(0x4b, 0x00, 0x82));
		NAMES.put("ivory", new Color(0xff, 0xff, 0xf0));
		NAMES.put("khaki", new Color(0xf0, 0xe6, 0x8c));
		NAMES.put("lavender", new Color(0xe6, 0xe6, 0xfa));
		NAMES.put("lavenderblush", new Color(0xff, 0xf0, 0xf5));
		NAMES.put("lawngreen", new Color(0x7c, 0xfc, 0x00));
		NAMES.put("lemonchiffon", new Color(0xff, 0xfa, 0xcd));
		NAMES.put("lightblue", new Color(0xad, 0xd8, 0xe6));
		NAMES.put("lightcoral", new Color(0xf0, 0x80, 0x80));
		NAMES.put("lightcyan", new Color(0xe0, 0xff, 0xff));
		NAMES.put("lightgoldenrodyellow", new Color(0xfa, 0xfa, 0xd2));
		NAMES.put("lightgreen", new Color(0x90, 0xee, 0x90));
		NAMES.put("lightgrey", new Color(0xd3, 0xd3, 0xd3));
		NAMES.put("lightpink", new Color(0xffb6c1));
		NAMES.put("lightsalmon", new Color(0xff, 0xa0, 0x7a));
		NAMES.put("lightseagreen", new Color(0x20, 0xb2, 0xaa));
		NAMES.put("lightskyblue", new Color(0x87, 0xce, 0xfa));
		NAMES.put("lightslategray", new Color(0x77, 0x88, 0x99));
		NAMES.put("lightsteelblue", new Color(0xb0, 0xc4, 0xde));
		NAMES.put("lightyellow", new Color(0xff, 0xff, 0xe0));
		NAMES.put("lime", new Color(0x00, 0xff, 0x00));
		NAMES.put("limegreen", new Color(0x32, 0xcd, 0x32));
		NAMES.put("linen", new Color(0xfa, 0xf0, 0xe6));
		NAMES.put("magenta", new Color(0xff, 0x00, 0xff));
		NAMES.put("maroon", new Color(0x80, 0x00, 0x00));
		NAMES.put("mediumaquamarine", new Color(0x66, 0xcd, 0xaa));
		NAMES.put("mediumblue", new Color(0x00, 0x00, 0xcd));
		NAMES.put("mediumorchid", new Color(0xba, 0x55, 0xd3));
		NAMES.put("mediumpurple", new Color(0x93, 0x70, 0xdb));
		NAMES.put("mediumseagreen", new Color(0x3c, 0xb3, 0x71));
		NAMES.put("mediumslateblue", new Color(0x7b, 0x68, 0xee));
		NAMES.put("mediumspringgreen", new Color(0x00, 0xfa, 0x9a));
		NAMES.put("mediumturquoise", new Color(0x48, 0xd1, 0xcc));
		NAMES.put("mediumvioletred", new Color(0xc7, 0x15, 0x85));
		NAMES.put("midnightblue", new Color(0x19, 0x19, 0x70));
		NAMES.put("mintcream", new Color(0xf5, 0xff, 0xfa));
		NAMES.put("mistyrose", new Color(0xff, 0xe4, 0xe1));
		NAMES.put("moccasin", new Color(0xff, 0xe4, 0xb5));
		NAMES.put("navajowhite", new Color(0xff, 0xde, 0xad));
		NAMES.put("navy", new Color(0x00, 0x00, 0x80));
		NAMES.put("oldlace", new Color(0xfd, 0xf5, 0xe6));
		NAMES.put("olive", new Color(0x80, 0x80, 0x00));
		NAMES.put("olivedrab", new Color(0x6b, 0x8e, 0x23));
		NAMES.put("orange", new Color(0xff, 0xa5, 0x00));
		NAMES.put("orangered", new Color(0xff, 0x45, 0x00));
		NAMES.put("orangered", new Color(0xff, 0x45, 0x00));
		NAMES.put("orchid", new Color(0xda, 0x70, 0xd6));
		NAMES.put("palegoldenrod", new Color(0xee, 0xe8, 0xaa));
		NAMES.put("palegreen", new Color(0x98, 0xfb, 0x98));
		NAMES.put("paleturquoise", new Color(0xaf, 0xee, 0xee));
		NAMES.put("palevioletred", new Color(0xdb, 0x70, 0x93));
		NAMES.put("papayawhip", new Color(0xff, 0xef, 0xd5));
		NAMES.put("peachpuff", new Color(0xff, 0xda, 0xb9));
		NAMES.put("peru", new Color(0xcd, 0x85, 0x3f));
		NAMES.put("pink", new Color(0xff, 0xc0, 0xcb));
		NAMES.put("plum", new Color(0xdd, 0xa0, 0xdd));
		NAMES.put("powderblue", new Color(0xb0, 0xe0, 0xe6));
		NAMES.put("purple", new Color(0x80, 0x00, 0x80));
		NAMES.put("red", new Color(0xff, 0x00, 0x00));
		NAMES.put("rosybrown", new Color(0xbc, 0x8f, 0x8f));
		NAMES.put("royalblue", new Color(0x41, 0x69, 0xe1));
		NAMES.put("saddlebrown", new Color(0x8b, 0x45, 0x13));
		NAMES.put("salmon", new Color(0xfa, 0x80, 0x72));
		NAMES.put("sandybrown", new Color(0xf4, 0xa4, 0x60));
		NAMES.put("seagreen", new Color(0x2e, 0x8b, 0x57));
		NAMES.put("seashell", new Color(0xff, 0xf5, 0xee));
		NAMES.put("sienna", new Color(0xa0, 0x52, 0x2d));
		NAMES.put("silver", new Color(0xc0, 0xc0, 0xc0));
		NAMES.put("skyblue", new Color(0x87, 0xce, 0xeb));
		NAMES.put("slateblue", new Color(0x6a, 0x5a, 0xcd));
		NAMES.put("slategray", new Color(0x70, 0x80, 0x90));
		NAMES.put("slategray", new Color(0x70, 0x80, 0x90));
		NAMES.put("snow", new Color(0xff, 0xfa, 0xfa));
		NAMES.put("springgreen", new Color(0x00, 0xff, 0x7f));
		NAMES.put("steelblue", new Color(0x46, 0x82, 0xb4));
		NAMES.put("tan", new Color(0xd2, 0xb4, 0x8c));
		NAMES.put("teal", new Color(0x00, 0x80, 0x80));
		NAMES.put("thistle", new Color(0xd8, 0xbf, 0xd8));
		NAMES.put("tomato", new Color(0xff, 0x63, 0x47));
		NAMES.put("turquoise", new Color(0x40, 0xe0, 0xd0));
		NAMES.put("turquoise", new Color(0x40, 0xe0, 0xd0));
		NAMES.put("violet", new Color(0xee, 0x82, 0xee));
		NAMES.put("wheat", new Color(0xf5, 0xde, 0xb3));
		NAMES.put("white", new Color(0xff, 0xff, 0xff));
		NAMES.put("whitesmoke", new Color(0xf5, 0xf5, 0xf5));
		NAMES.put("yellow", new Color(0xff, 0xff, 0x00));
		NAMES.put("yellowgreen", new Color(0x9, 0xacd, 0x32));
	}
	
	/**
	 * Gives you a Color based on a name.
	 * @param name a name such as black, violet, cornflowerblue
	 * @return the corresponding Color object
	 */
	public static Color getByName(String name) {
		Object o = NAMES.get(name.toLowerCase());
		if (o != null) return (Color)o;
		return null;
	}
}
