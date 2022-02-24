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
package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;

/**
 *
 * @author  Paulo Soares
 */
public abstract class ExtendedColor extends BaseColor{
    
	private static final long serialVersionUID = 2722660170712380080L;
	/** a type of extended color. */
    public static final int TYPE_RGB = 0;
    /** a type of extended color. */
    public static final int TYPE_GRAY = 1;
    /** a type of extended color. */
    public static final int TYPE_CMYK = 2;
    /** a type of extended color. */
    public static final int TYPE_SEPARATION = 3;
    /** a type of extended color. */
    public static final int TYPE_PATTERN = 4;
    /** a type of extended color. */
    public static final int TYPE_SHADING = 5;
    /** a type of extended color. */
    public static final int TYPE_DEVICEN = 6;
    /** a type of extended color. */
    public static final int TYPE_LAB = 7;

    protected int type;

    /**
     * Constructs an extended color of a certain type.
     * @param type
     */
    public ExtendedColor(int type) {
        super(0, 0, 0);
        this.type = type;
    }
    
    /**
     * Constructs an extended color of a certain type and a certain color.
     * @param type
     * @param red
     * @param green
     * @param blue
     */
    public ExtendedColor(int type, float red, float green, float blue) {
        super(normalize(red), normalize(green), normalize(blue));
        this.type = type;
    }
    /**
     * Constructs an extended color of a certain type and a certain color.
     * @param type
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public ExtendedColor(int type, int red, int green, int blue, int alpha) {
    	super(normalize((float)red / 0xFF), normalize((float)green / 0xFF), normalize((float)blue / 0xFF), normalize((float)alpha / 0xFF));
		this.type = type;
	}

	/**
     * Gets the type of this color.
     * @return one of the types (see constants)
     */
    public int getType() {
        return type;
    }
    
    /**
     * Gets the type of a given color.
     * @param color
     * @return one of the types (see constants)
     */
    public static int getType(BaseColor color) {
        if (color instanceof ExtendedColor)
            return ((ExtendedColor)color).getType();
        return TYPE_RGB;
    }

    static final float normalize(float value) {
        if (value < 0)
            return 0;
        if (value > 1)
            return 1;
        return value;
    }
}
