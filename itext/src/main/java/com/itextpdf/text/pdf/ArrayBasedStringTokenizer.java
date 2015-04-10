/*
 * $Id: $
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
package com.itextpdf.text.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizes the given <i>text</i> based on a given array of Strings. On assembling the output array, 
 * you should be able to get back the original text.
 * 
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 * 
 */
public class ArrayBasedStringTokenizer {
    
    private final Pattern regex;
    
    public ArrayBasedStringTokenizer(String[] tokens) {
        this.regex = Pattern.compile(getRegexFromTokens(tokens));
    }
    
    public String[] tokenize(String text) {
        
        List<String> tokens = new ArrayList<String>();
        
        Matcher matcher = regex.matcher(text);
        
        int endIndexOfpreviousMatch = 0;
        
        while (matcher.find()) {
            
            int startIndexOfMatch = matcher.start();
            
            String previousToken = text.substring(endIndexOfpreviousMatch, startIndexOfMatch);
            
            if (previousToken.length() > 0) {
                tokens.add(previousToken);
            }
            
            String currentMatch = matcher.group();
            
//            System.out.println("currentMatch=" + currentMatch); 
            
            tokens.add(currentMatch);
            
            endIndexOfpreviousMatch = matcher.end();
            
        }
        
        String tail = text.substring(endIndexOfpreviousMatch, text.length());
        
        if (tail.length() > 0) {
            tokens.add(tail);
        }
        
        return tokens.toArray(new String[0]);
        
    }
    
    private String getRegexFromTokens(String[] tokens) {
        StringBuilder regexBuilder = new StringBuilder(100);
        
        for (String token : tokens) {
            regexBuilder.append("(").append(token).append(")|");
        }
        
        regexBuilder.setLength(regexBuilder.length() - 1); 
        
        String regex = regexBuilder.toString();
        
        return regex;
    }

}
