/*
 * $Id$
 *
 * Copyright 2005 by Paulo Soares.
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

import java.io.IOException;
import java.util.ArrayList;
/**
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */
public class PdfContentParser {
    
    public static final int COMMAND_TYPE = 200;
    /**
     * Holds value of property tokeniser.
     */
    private PRTokeniser tokeniser;    
    
    /** Creates a new instance of PdfContentParser */
    public PdfContentParser(PRTokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }
    
    public ArrayList parse(ArrayList ls) throws IOException {
        if (ls == null)
            ls = new ArrayList();
        else
            ls.clear();
        PdfObject ob = null;
        while ((ob = readPRObject()) != null) {
            ls.add(ob);
            if (ob.type() == COMMAND_TYPE)
                break;
        }
        return ls;
    }
    
    /**
     * Getter for property tokeniser.
     * @return Value of property tokeniser.
     */
    public PRTokeniser getTokeniser() {
        return this.tokeniser;
    }
    
    /**
     * Setter for property tokeniser.
     * @param tokeniser New value of property tokeniser.
     */
    public void setTokeniser(PRTokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }
    
    public PdfDictionary readDictionary() throws IOException {
        PdfDictionary dic = new PdfDictionary();
        while (true) {
            if (!nextValidToken())
                throw new IOException("Unexpected end of file.");;
                if (tokeniser.getTokenType() == PRTokeniser.TK_END_DIC)
                    break;
                if (tokeniser.getTokenType() != PRTokeniser.TK_NAME)
                    throw new IOException("Dictionary key is not a name.");
                PdfName name = new PdfName(tokeniser.getStringValue());
                PdfObject obj = readPRObject();
                int type = obj.type();
                if (-type == PRTokeniser.TK_END_DIC)
                    throw new IOException("Unexpected '>>'");
                if (-type == PRTokeniser.TK_END_ARRAY)
                    throw new IOException("Unexpected ']'");
                dic.put(name, obj);
        }
        return dic;
    }
    
    public PdfArray readArray() throws IOException {
        PdfArray array = new PdfArray();
        while (true) {
            PdfObject obj = readPRObject();
            int type = obj.type();
            if (-type == PRTokeniser.TK_END_ARRAY)
                break;
            if (-type == PRTokeniser.TK_END_DIC)
                throw new IOException("Unexpected '>>'");
            array.add(obj);
        }
        return array;
    }
    
    public PdfObject readPRObject() throws IOException {
        if (!nextValidToken())
            return null;
        int type = tokeniser.getTokenType();
        switch (type) {
            case PRTokeniser.TK_START_DIC: {
                PdfDictionary dic = readDictionary();
                return dic;
            }
            case PRTokeniser.TK_START_ARRAY:
                return readArray();
            case PRTokeniser.TK_STRING:
                PdfString str = new PdfString(tokeniser.getStringValue(), null).setHexWriting(tokeniser.isHexString());
                return str;
            case PRTokeniser.TK_NAME:
                return new PdfName(tokeniser.getStringValue());
            case PRTokeniser.TK_OTHER:
                return new PdfLiteral(COMMAND_TYPE, tokeniser.getStringValue());
            default:
                return new PdfLiteral(-type, tokeniser.getStringValue());
        }
    }
    
    public boolean nextValidToken() throws IOException {
        while (tokeniser.nextToken()) {
            if (tokeniser.getTokenType() == PRTokeniser.TK_COMMENT)
                continue;
            return true;
        }
        return false;
    }
}