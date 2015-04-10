/*
 * $Id: MessageLocalization.java 6134 2013-12-23 13:15:14Z blowagie $
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
package com.itextpdf.text.error_messages;

import com.itextpdf.text.io.StreamUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

/**
 * Localizes error messages. The messages are located in the package
 * com.itextpdf.text.error_messages in the form language_country.lng.
 * The internal file encoding is UTF-8 without any escape chars, it's not a
 * normal property file. See en.lng for more information on the internal format.
 * @author Paulo Soares (psoares@glintt.com)
 */
public final class MessageLocalization {
    private static HashMap<String, String> defaultLanguage = new HashMap<String, String>();
    private static HashMap<String, String> currentLanguage;
    private static final String BASE_PATH = "com/itextpdf/text/l10n/error/";

    private MessageLocalization() {
    }

    static {
        try {
            defaultLanguage = getLanguageMessages("en", null);
        } catch (Exception ex) {
            // do nothing
        }
        if (defaultLanguage == null)
            defaultLanguage = new HashMap<String, String>();
    }

    /**
     * Get a message without parameters.
     * @param key the key to the message
     * @return the message
     */
    public static String getMessage(String key) {
        return getMessage(key, true);
    }

    public static String getMessage(String key, boolean useDefaultLanguageIfMessageNotFound) {
        HashMap<String, String> cl = currentLanguage;
        String val;
        if (cl != null) {
            val = cl.get(key);
            if (val != null)
                return val;
        }

        if (useDefaultLanguageIfMessageNotFound) {
            cl = defaultLanguage;
        	val = cl.get(key);
        	if (val != null)
            	return val;
        }

        return "No message found for " + key;
    }

    /**
     * Get a message with one parameter as an primitive int. The parameter will replace the string
     * "{1}" found in the message.
     * @param key the key to the message
     * @param p1 the parameter
     * @return the message
     */
    public static String getComposedMessage(String key, int p1) {
        return getComposedMessage(key, String.valueOf(p1), null, null, null);
    }

	/**
	 * Get a message with param.length parameters or none if param is null. In
	 * the message the "{1}", "{2}" to "{lenght of param array}" are replaced
	 * with the object.toString of the param array. (with param[0] being "{1}")
	 *
	 * @since iText 5.0.6
	 * @param key
	 *            the key to the message
	 * @param param array of parameter objects, (toString is used to add it to the message)
	 * @return the message
	 */
	public static String getComposedMessage(final String key, final Object... param) {
		String msg = getMessage(key);
		if (null != param) {
			int i = 1;
			for (Object o : param) {
				if (null != o) {
					msg = msg.replace("{" + i + "}", o.toString());
				}
				i++;
			}
		}
		return msg;
	}

    /**
     * Sets the language to be used globally for the error messages. The language
     * is a two letter lowercase country designation like "en" or "pt". The country
     * is an optional two letter uppercase code like "US" or "PT".
     * @param language the language
     * @param country the country
     * @return true if the language was found, false otherwise
     * @throws IOException on error
     */
    public static boolean setLanguage(String language, String country) throws IOException {
        HashMap<String, String> lang = getLanguageMessages(language, country);
        if (lang == null)
            return false;
        currentLanguage = lang;
        return true;
    }

    /**
     * Sets the error messages directly from a Reader.
     * @param r the Reader
     * @throws IOException on error
     */
    public static void setMessages(Reader r) throws IOException {
        currentLanguage = readLanguageStream(r);
    }

    private static HashMap<String, String> getLanguageMessages(String language, String country) throws IOException {
        if (language == null)
            throw new IllegalArgumentException("The language cannot be null.");
        InputStream is = null;
        try {
            String file;
            if (country != null)
                file = language + "_" + country + ".lng";
            else
                file = language + ".lng";
            is = StreamUtil.getResourceStream(BASE_PATH + file, new MessageLocalization().getClass().getClassLoader());
            if (is != null)
                return readLanguageStream(is);
            if (country == null)
                return null;
            file = language + ".lng";
            is = StreamUtil.getResourceStream(BASE_PATH + file, new MessageLocalization().getClass().getClassLoader());
            if (is != null)
                return readLanguageStream(is);
            else
                return null;
        }
        finally {
            try {
                if (null != is){
                	is.close();
                }
            } catch (Exception exx) {
            }
            // do nothing
        }
    }

    private static HashMap<String, String> readLanguageStream(InputStream is) throws IOException {
        return readLanguageStream(new InputStreamReader(is, "UTF-8"));
    }

    private static HashMap<String, String> readLanguageStream(Reader r) throws IOException {
        HashMap<String, String> lang = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(r);
        String line;
        while ((line = br.readLine()) != null) {
            int idxeq = line.indexOf('=');
            if (idxeq < 0)
                continue;
            String key = line.substring(0, idxeq).trim();
            if (key.startsWith("#"))
                continue;
            lang.put(key, line.substring(idxeq + 1));
        }
        return lang;
    }
}
