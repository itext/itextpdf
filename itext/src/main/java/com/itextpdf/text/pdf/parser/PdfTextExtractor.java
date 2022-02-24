/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.parser;

import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Extracts text from a PDF file.
 * @since	2.1.4
 */
public final class PdfTextExtractor {

	/**
	 * This class only contains static methods.
	 */
	private PdfTextExtractor()  {
	}
        
    /**
     * Extract text from a specified page using an extraction strategy.
     * Also allows registration of custom ContentOperators
     * @param reader the reader to extract text from
     * @param pageNumber the page to extract text from
     * @param strategy the strategy to use for extracting text
     * @param additionalContentOperators an optional map of custom ContentOperators for rendering instructions
     * @return the extracted text
     * @throws IOException if any operation fails while reading from the provided PdfReader
     */
    public static String getTextFromPage(PdfReader reader, int pageNumber, TextExtractionStrategy strategy, Map<String, ContentOperator> additionalContentOperators) throws IOException{
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        return parser.processContent(pageNumber, strategy, additionalContentOperators).getResultantText();
    }
	
    /**
     * Extract text from a specified page using an extraction strategy.
     * @param reader the reader to extract text from
     * @param pageNumber the page to extract text from
     * @param strategy the strategy to use for extracting text
     * @return the extracted text
     * @throws IOException if any operation fails while reading from the provided PdfReader
     * @since 5.0.2
     */
    public static String getTextFromPage(PdfReader reader, int pageNumber, TextExtractionStrategy strategy) throws IOException{
        return getTextFromPage(reader, pageNumber, strategy, new HashMap<String, ContentOperator>());
    }
    
    /**
     * Extract text from a specified page using the default strategy.
     * <p><strong>Note:</strong> the default strategy is subject to change.  If using a specific strategy
     * is important, use {@link PdfTextExtractor#getTextFromPage(PdfReader, int, TextExtractionStrategy)}
     * @param reader the reader to extract text from
     * @param pageNumber the page to extract text from
     * @return the extracted text
     * @throws IOException if any operation fails while reading from the provided PdfReader
     * @since 5.0.2
     */
    public static String getTextFromPage(PdfReader reader, int pageNumber) throws IOException{
        return getTextFromPage(reader, pageNumber, new LocationTextExtractionStrategy());
    }

}
