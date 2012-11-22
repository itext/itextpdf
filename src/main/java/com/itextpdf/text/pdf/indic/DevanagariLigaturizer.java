/*
 * $Id: FontsResourceAnchor.java 4784 2011-03-15 08:33:00Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Ram Narayan, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.text.pdf.indic;

import java.util.HashMap;

public class DevanagariLigaturizer extends IndicLigaturizer {

    // Devanagari characters
    public static final char DEVA_MATRA_AA = '\u093E';
    public static final char DEVA_MATRA_I = '\u093F';
    public static final char DEVA_MATRA_AI = '\u094C';
    public static final char DEVA_MATRA_HLR = '\u0962';
    public static final char DEVA_MATRA_HLRR = '\u0963';
    public static final char DEVA_LETTER_A = '\u0905';
    public static final char DEVA_LETTER_AU = '\u0914';
    public static final char DEVA_LETTER_KA = '\u0915';
    public static final char DEVA_LETTER_HA = '\u0939';
    public static final char DEVA_HALANTA = '\u094D';
    
    public DevanagariLigaturizer() {
    	langTable = new HashMap<Integer, Character>();
        langTable.put(MATRA_AA, DEVA_MATRA_AA);
        langTable.put(MATRA_I, DEVA_MATRA_I);
        langTable.put(MATRA_AI, DEVA_MATRA_AI);
        langTable.put(MATRA_HLR, DEVA_MATRA_HLR);
        langTable.put(MATRA_HLRR, DEVA_MATRA_HLRR);
        langTable.put(LETTER_A, DEVA_LETTER_A);
        langTable.put(LETTER_AU, DEVA_LETTER_AU);
        langTable.put(LETTER_KA, DEVA_LETTER_KA);
        langTable.put(LETTER_HA, DEVA_LETTER_HA);
        langTable.put(HALANTA, DEVA_HALANTA);
    }
}
