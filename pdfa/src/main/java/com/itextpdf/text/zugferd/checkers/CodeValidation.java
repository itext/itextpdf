/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, et al.
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
package com.itextpdf.text.zugferd.checkers;

import com.itextpdf.text.zugferd.exceptions.InvalidCodeException;

/**
 * Abstract superclass of a series of code validation classes.
 */
public abstract class CodeValidation {
    
    /**
     * Checks if a specific code is valid.
     * @param code the value you want to check
     * @return true if the code is valid
     */
    public abstract boolean isValid(String code);
    
    /**
     * Checks if a specific code is valid.
     * @param code the value you want to check
     * @return the code that has been checked
     * @throws com.itextpdf.text.zugferd.exceptions.InvalidCodeException
     *      reveals the reason why the code isn't valid
     */
    public String check(String code) throws InvalidCodeException {
        if (code == null || !isValid(code))
            throw new InvalidCodeException(code, this.getClass().getName());
        return code;
    }
    
    /**
     * Checks the length of a code and if a code consists of numbers only.
     * @param code  the code that needs to be checked
     * @param digits    the expected length of the code
     * @return  true if the code is numeric and has the expected length
     */
    public boolean isNumeric(String code, int digits) {
        if (code.length() != digits) return false;
        for (char c : code.toCharArray())
            if (c < 48 || c > 57) return false;
        return true;
    }
    
    /**
     * Checks the length of a code and if a code consists of uppercase letters
     * from A to Z.
     * @param code  the code that needs to be checked
     * @param chars    the expected length of the code
     * @return  true if the code consists of letters from A to Z and has the expected length
     */
    public boolean isUppercase(String code, int chars) {
        if (code.length() != chars) return false;
        for (char c : code.toCharArray())
            if (c < 65 || c > 90) return false;
        return true;
    }
        
    /**
     * Checks the length of a code and if a code consists of lowercase letters
     * from a to z.
     * @param code  the code that needs to be checked
     * @param chars    the expected length of the code
     * @return  true if the code consists of letters from a to z and has the expected length
     */
    public boolean isLowercase(String code, int chars) {
        if (code.length() != chars) return false;
        for (char c : code.toCharArray())
            if (c < 97 || c > 122) return false;
        return true;
    }
    
}
