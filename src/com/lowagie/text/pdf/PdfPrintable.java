/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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

package com.lowagie.text.pdf;

/**
 * Classes that implement the <CODE>PdfPrintable</CODE>-interface can be printed as text on a page.
 *
 * @see		PdfNull
 * @see		PdfBoolean
 * @see		PdfNumber
 * @see		PdfString
 */

public interface PdfPrintable {

    // static membervariables

/** possible align value of a printable object */
    public static final int DEFAULT = 0;

/** possible align value of a printable object */
    public static final int RIGHT = 1;

/** possible align value of a printable object */
    public static final int LEFT = 2;

/** possible align value of a printable object */
    public static final int CENTER = 3;

    // basic methods

/**
 * Every printable object should have a <CODE>toString</CODE>-method.
 *
 * @return		a <CODE>String</CODE>
 */

    public String toString();

/**
 * Returns the length of the actual content of the <CODE>PdfObject</CODE>.
 *
 * @return		a length
 */

    public int length();

    // methods concerning the actual type of a PdfPrintable object

/**
 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfNull</CODE>.
 *
 * @return		<CODE>true</CODE> or <CODE>false</CODE>
 */

    public boolean isNull();

/**
 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfBoolean</CODE>.
 *
 * @return		<CODE>true</CODE> or <CODE>false</CODE>
 */

    public boolean isBoolean();

/**
 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfNumber</CODE>.
 *
 * @return		<CODE>true</CODE> or <CODE>false</CODE>
 */

    public boolean isNumber();

/**
 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfString</CODE>.
 *
 * @return		<CODE>true</CODE> or <CODE>false</CODE>
 */

    public boolean isString();

    public byte[] toPdf(PdfWriter writer);
}