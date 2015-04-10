/*
 * $Id: PdfContentParser.java 6134 2013-12-23 13:15:14Z blowagie $
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

import java.io.IOException;
import java.util.ArrayList;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PRTokeniser.TokenType;
/**
 * Parses the page or template content.
 * @author Paulo Soares
 */
public class PdfContentParser {

    /**
     * Commands have this type.
     */
    public static final int COMMAND_TYPE = 200;
    /**
     * Holds value of property tokeniser.
     */
    private PRTokeniser tokeniser;

    /**
     * Creates a new instance of PdfContentParser
     * @param tokeniser the tokeniser with the content
     */
    public PdfContentParser(PRTokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    /**
     * Parses a single command from the content. Each command is output as an array of arguments
     * having the command itself as the last element. The returned array will be empty if the
     * end of content was reached.
     * @param ls an <CODE>ArrayList</CODE> to use. It will be cleared before using. If it's
     * <CODE>null</CODE> will create a new <CODE>ArrayList</CODE>
     * @return the same <CODE>ArrayList</CODE> given as argument or a new one
     * @throws IOException on error
     */
    public ArrayList<PdfObject> parse(ArrayList<PdfObject> ls) throws IOException {
        if (ls == null)
            ls = new ArrayList<PdfObject>();
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
     * Gets the tokeniser.
     * @return the tokeniser.
     */
    public PRTokeniser getTokeniser() {
        return this.tokeniser;
    }

    /**
     * Sets the tokeniser.
     * @param tokeniser the tokeniser
     */
    public void setTokeniser(PRTokeniser tokeniser) {
        this.tokeniser = tokeniser;
    }

    /**
     * Reads a dictionary. The tokeniser must be positioned past the "&lt;&lt;" token.
     * @return the dictionary
     * @throws IOException on error
     */
    public PdfDictionary readDictionary() throws IOException {
        PdfDictionary dic = new PdfDictionary();
        while (true) {
            if (!nextValidToken())
                throw new IOException(MessageLocalization.getComposedMessage("unexpected.end.of.file"));
                if (tokeniser.getTokenType() == TokenType.END_DIC)
                    break;
                if (tokeniser.getTokenType() == TokenType.OTHER && "def".equals(tokeniser.getStringValue()))
                    continue;
                if (tokeniser.getTokenType() != TokenType.NAME)
                    throw new IOException(MessageLocalization.getComposedMessage("dictionary.key.1.is.not.a.name", tokeniser.getStringValue()));
                PdfName name = new PdfName(tokeniser.getStringValue(), false);
                PdfObject obj = readPRObject();
                int type = obj.type();
                if (-type == TokenType.END_DIC.ordinal())
                    throw new IOException(MessageLocalization.getComposedMessage("unexpected.gt.gt"));
                if (-type == TokenType.END_ARRAY.ordinal())
                    throw new IOException(MessageLocalization.getComposedMessage("unexpected.close.bracket"));
                dic.put(name, obj);
        }
        return dic;
    }

    /**
     * Reads an array. The tokeniser must be positioned past the "[" token.
     * @return an array
     * @throws IOException on error
     */
    public PdfArray readArray() throws IOException {
        PdfArray array = new PdfArray();
        while (true) {
            PdfObject obj = readPRObject();
            int type = obj.type();
            if (-type == TokenType.END_ARRAY.ordinal())
                break;
            if (-type == TokenType.END_DIC.ordinal())
                throw new IOException(MessageLocalization.getComposedMessage("unexpected.gt.gt"));
            array.add(obj);
        }
        return array;
    }

    /**
     * Reads a pdf object.
     * @return the pdf object
     * @throws IOException on error
     */
    public PdfObject readPRObject() throws IOException {
        if (!nextValidToken())
            return null;
        TokenType type = tokeniser.getTokenType();
        switch (type) {
            case START_DIC: {
                PdfDictionary dic = readDictionary();
                return dic;
            }
            case START_ARRAY:
                return readArray();
            case STRING:
                PdfString str = new PdfString(tokeniser.getStringValue(), null).setHexWriting(tokeniser.isHexString());
                return str;
            case NAME:
                return new PdfName(tokeniser.getStringValue(), false);
            case NUMBER:
                return new PdfNumber(tokeniser.getStringValue());
            case OTHER:
                return new PdfLiteral(COMMAND_TYPE, tokeniser.getStringValue());
            default:
                return new PdfLiteral(-type.ordinal(), tokeniser.getStringValue());
        }
    }

    /**
     * Reads the next token skipping over the comments.
     * @return <CODE>true</CODE> if a token was read, <CODE>false</CODE> if the end of content was reached
     * @throws IOException on error
     */
    public boolean nextValidToken() throws IOException {
        while (tokeniser.nextToken()) {
            if (tokeniser.getTokenType() == TokenType.COMMENT)
                continue;
            return true;
        }
        return false;
    }
}
