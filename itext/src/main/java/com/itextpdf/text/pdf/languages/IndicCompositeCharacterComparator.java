/*
 * $Id: $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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

import java.util.Comparator;

/**
 * <p>
 * This works on CompositeCharcaters or Juktakshar-s of Indian languages like Bangla, Hindi, etc. CompositeCharcters
 * are single glyphs consisting of more than one characters.
 * <p>
 * <p>
 * This class works on these CompositeCharacters and places the Strings having higher number
 * of Characters before the one with lower no. This is necessay to properly display the CompositeCharacters
 * when they occur side by side.
 * </p>
 * <p>
 * <h3>Examples of CompositeCharactes from Bangla</h3>
 * <ul>
 * <li><b>à¦?à§?à¦?</b></li>
 * <li><b>à¦?à§?</b></li>
 * <li><b>à¦?à§?à¦·à§?à¦®</b></li>
 * <li><b>à¦?à§?à¦·</b></li>
 * </ul>
 * </p>
 *  
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
public class IndicCompositeCharacterComparator implements Comparator<String> {
    
    public int compare(String o1, String o2) {
        if (o2.length() > o1.length()) { 
            return 1;
        } else if (o1.length() > o2.length()) { 
            return -1;
        } else {
            return o1.compareTo(o2);
        }
    }

}
