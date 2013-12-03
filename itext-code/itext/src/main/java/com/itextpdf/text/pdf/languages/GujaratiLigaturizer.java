/*
 * $Id: GujaratiLigaturizer.java 5561 2012-11-22 16:22:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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
package com.itextpdf.text.pdf.languages;

/**
 * Implementation of the IndicLigaturizer for Gujarati.
 */
public class GujaratiLigaturizer extends IndicLigaturizer {
    
    // Gujrati constants
	public static final char GUJR_MATRA_AA = '\u0ABE';
	public static final char GUJR_MATRA_I = '\u0ABF';
	public static final char GUJR_MATRA_E = '\u0AC7';
	public static final char GUJR_MATRA_AI = '\u0AC8';
	public static final char GUJR_MATRA_HLR = '\u0AE2';
	public static final char GUJR_MATRA_HLRR = '\u0AE3';
	public static final char GUJR_LETTER_A = '\u0A85';
	public static final char GUJR_LETTER_AU = '\u0A94';
	public static final char GUJR_LETTER_KA = '\u0A95';
	public static final char GUJR_LETTER_HA = '\u0AB9';
	public static final char GUJR_HALANTA = '\u0ACD';
    
    /**
     * Constructor for the IndicLigaturizer for Gujarati.
     */
	public GujaratiLigaturizer() {
    	langTable = new char[11];
        langTable[MATRA_AA] = GUJR_MATRA_AA;
        langTable[MATRA_I] = GUJR_MATRA_I;
        langTable[MATRA_E] = GUJR_MATRA_E;
        langTable[MATRA_AI] = GUJR_MATRA_AI;
        langTable[MATRA_HLR] = GUJR_MATRA_HLR;
        langTable[MATRA_HLRR] = GUJR_MATRA_HLRR;
        langTable[LETTER_A] = GUJR_LETTER_A;
        langTable[LETTER_AU] = GUJR_LETTER_AU;
        langTable[LETTER_KA] = GUJR_LETTER_KA;
        langTable[LETTER_HA] = GUJR_LETTER_HA;
        langTable[HALANTA] = GUJR_HALANTA;
	}
}
