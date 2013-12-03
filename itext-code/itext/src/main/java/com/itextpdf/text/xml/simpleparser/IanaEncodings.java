/*
 * $Id: IanaEncodings.java 5914 2013-07-28 14:18:11Z blowagie $
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

/* The values used in this class are based on class org.apache.xercis.util.EncodingMap
 * http://svn.apache.org/viewvc/xerces/java/trunk/src/org/apache/xerces/util/EncodingMap.java?view=markup
 * This class was originally published under the following license:
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itextpdf.text.xml.simpleparser;

import java.util.HashMap;
import java.util.Map;

/**
 * Translates a IANA encoding name to a Java encoding.
 */

public class IanaEncodings {

	/** The object that maps IANA to Java encodings. */
    private static final Map<String, String> MAP = new HashMap<String, String>();

    static {
        // add IANA to Java encoding mappings.
        MAP.put("BIG5", "Big5");
        MAP.put("CSBIG5", "Big5");
        MAP.put("CP037", "CP037");
        MAP.put("IBM037", "CP037");
        MAP.put("CSIBM037", "CP037");
        MAP.put("EBCDIC-CP-US", "CP037");
        MAP.put("EBCDIC-CP-CA", "CP037");
        MAP.put("EBCDIC-CP-NL", "CP037");
        MAP.put("EBCDIC-CP-WT", "CP037");
        MAP.put("IBM277", "CP277");
        MAP.put("CP277", "CP277");
        MAP.put("CSIBM277", "CP277");
        MAP.put("EBCDIC-CP-DK", "CP277");
        MAP.put("EBCDIC-CP-NO", "CP277");
        MAP.put("IBM278", "CP278");
        MAP.put("CP278", "CP278");
        MAP.put("CSIBM278", "CP278");
        MAP.put("EBCDIC-CP-FI", "CP278");
        MAP.put("EBCDIC-CP-SE", "CP278");
        MAP.put("IBM280", "CP280");
        MAP.put("CP280", "CP280");
        MAP.put("CSIBM280", "CP280");
        MAP.put("EBCDIC-CP-IT", "CP280");
        MAP.put("IBM284", "CP284");
        MAP.put("CP284", "CP284");
        MAP.put("CSIBM284", "CP284");
        MAP.put("EBCDIC-CP-ES", "CP284");
        MAP.put("EBCDIC-CP-GB", "CP285");
        MAP.put("IBM285", "CP285");
        MAP.put("CP285", "CP285");
        MAP.put("CSIBM285", "CP285");
        MAP.put("EBCDIC-CP-FR", "CP297");
        MAP.put("IBM297", "CP297");
        MAP.put("CP297", "CP297");
        MAP.put("CSIBM297", "CP297");
        MAP.put("EBCDIC-CP-AR1", "CP420");
        MAP.put("IBM420", "CP420");
        MAP.put("CP420", "CP420");
        MAP.put("CSIBM420", "CP420");
        MAP.put("EBCDIC-CP-HE", "CP424");
        MAP.put("IBM424", "CP424");
        MAP.put("CP424", "CP424");
        MAP.put("CSIBM424", "CP424");
        MAP.put("EBCDIC-CP-CH", "CP500");
        MAP.put("IBM500", "CP500");
        MAP.put("CP500", "CP500");
        MAP.put("CSIBM500", "CP500");
        MAP.put("EBCDIC-CP-CH", "CP500");
        MAP.put("EBCDIC-CP-BE", "CP500");
        MAP.put("IBM868", "CP868");
        MAP.put("CP868", "CP868");
        MAP.put("CSIBM868", "CP868");
        MAP.put("CP-AR", "CP868");
        MAP.put("IBM869", "CP869");
        MAP.put("CP869", "CP869");
        MAP.put("CSIBM869", "CP869");
        MAP.put("CP-GR", "CP869");
        MAP.put("IBM870", "CP870");
        MAP.put("CP870", "CP870");
        MAP.put("CSIBM870", "CP870");
        MAP.put("EBCDIC-CP-ROECE", "CP870");
        MAP.put("EBCDIC-CP-YU", "CP870");
        MAP.put("IBM871", "CP871");
        MAP.put("CP871", "CP871");
        MAP.put("CSIBM871", "CP871");
        MAP.put("EBCDIC-CP-IS", "CP871");
        MAP.put("IBM918", "CP918");
        MAP.put("CP918", "CP918");
        MAP.put("CSIBM918", "CP918");
        MAP.put("EBCDIC-CP-AR2", "CP918");
        MAP.put("EUC-JP", "EUCJIS");
        MAP.put("CSEUCPkdFmtJapanese", "EUCJIS");
        MAP.put("EUC-KR", "KSC5601");
        MAP.put("GB2312", "GB2312");
        MAP.put("CSGB2312", "GB2312");
        MAP.put("ISO-2022-JP", "JIS");
        MAP.put("CSISO2022JP", "JIS");
        MAP.put("ISO-2022-KR", "ISO2022KR");
        MAP.put("CSISO2022KR", "ISO2022KR");
        MAP.put("ISO-2022-CN", "ISO2022CN");

        MAP.put("X0201", "JIS0201");
        MAP.put("CSISO13JISC6220JP", "JIS0201");
        MAP.put("X0208", "JIS0208");
        MAP.put("ISO-IR-87", "JIS0208");
        MAP.put("X0208dbiJIS_X0208-1983", "JIS0208");
        MAP.put("CSISO87JISX0208", "JIS0208");
        MAP.put("X0212", "JIS0212");
        MAP.put("ISO-IR-159", "JIS0212");
        MAP.put("CSISO159JISX02121990", "JIS0212");
        MAP.put("SHIFT_JIS", "SJIS");
        MAP.put("CSSHIFT_JIS", "SJIS");
        MAP.put("MS_Kanji", "SJIS");

        // Add support for Cp1252 and its friends
        MAP.put("WINDOWS-1250", "Cp1250");
        MAP.put("WINDOWS-1251", "Cp1251");
        MAP.put("WINDOWS-1252", "Cp1252");
        MAP.put("WINDOWS-1253", "Cp1253");
        MAP.put("WINDOWS-1254", "Cp1254");
        MAP.put("WINDOWS-1255", "Cp1255");
        MAP.put("WINDOWS-1256", "Cp1256");
        MAP.put("WINDOWS-1257", "Cp1257");
        MAP.put("WINDOWS-1258", "Cp1258");
        MAP.put("TIS-620", "TIS620");

        MAP.put("ISO-8859-1", "ISO8859_1");
        MAP.put("ISO-IR-100", "ISO8859_1");
        MAP.put("ISO_8859-1", "ISO8859_1");
        MAP.put("LATIN1", "ISO8859_1");
        MAP.put("CSISOLATIN1", "ISO8859_1");
        MAP.put("L1", "ISO8859_1");
        MAP.put("IBM819", "ISO8859_1");
        MAP.put("CP819", "ISO8859_1");

        MAP.put("ISO-8859-2", "ISO8859_2");
        MAP.put("ISO-IR-101", "ISO8859_2");
        MAP.put("ISO_8859-2", "ISO8859_2");
        MAP.put("LATIN2", "ISO8859_2");
        MAP.put("CSISOLATIN2", "ISO8859_2");
        MAP.put("L2", "ISO8859_2");

        MAP.put("ISO-8859-3", "ISO8859_3");
        MAP.put("ISO-IR-109", "ISO8859_3");
        MAP.put("ISO_8859-3", "ISO8859_3");
        MAP.put("LATIN3", "ISO8859_3");
        MAP.put("CSISOLATIN3", "ISO8859_3");
        MAP.put("L3", "ISO8859_3");

        MAP.put("ISO-8859-4", "ISO8859_4");
        MAP.put("ISO-IR-110", "ISO8859_4");
        MAP.put("ISO_8859-4", "ISO8859_4");
        MAP.put("LATIN4", "ISO8859_4");
        MAP.put("CSISOLATIN4", "ISO8859_4");
        MAP.put("L4", "ISO8859_4");

        MAP.put("ISO-8859-5", "ISO8859_5");
        MAP.put("ISO-IR-144", "ISO8859_5");
        MAP.put("ISO_8859-5", "ISO8859_5");
        MAP.put("CYRILLIC", "ISO8859_5");
        MAP.put("CSISOLATINCYRILLIC", "ISO8859_5");

        MAP.put("ISO-8859-6", "ISO8859_6");
        MAP.put("ISO-IR-127", "ISO8859_6");
        MAP.put("ISO_8859-6", "ISO8859_6");
        MAP.put("ECMA-114", "ISO8859_6");
        MAP.put("ASMO-708", "ISO8859_6");
        MAP.put("ARABIC", "ISO8859_6");
        MAP.put("CSISOLATINARABIC", "ISO8859_6");

        MAP.put("ISO-8859-7", "ISO8859_7");
        MAP.put("ISO-IR-126", "ISO8859_7");
        MAP.put("ISO_8859-7", "ISO8859_7");
        MAP.put("ELOT_928", "ISO8859_7");
        MAP.put("ECMA-118", "ISO8859_7");
        MAP.put("GREEK", "ISO8859_7");
        MAP.put("CSISOLATINGREEK", "ISO8859_7");
        MAP.put("GREEK8", "ISO8859_7");

        MAP.put("ISO-8859-8", "ISO8859_8");
        MAP.put("ISO-8859-8-I", "ISO8859_8"); // added since this encoding only differs w.r.t. presentation
        MAP.put("ISO-IR-138", "ISO8859_8");
        MAP.put("ISO_8859-8", "ISO8859_8");
        MAP.put("HEBREW", "ISO8859_8");
        MAP.put("CSISOLATINHEBREW", "ISO8859_8");

        MAP.put("ISO-8859-9", "ISO8859_9");
        MAP.put("ISO-IR-148", "ISO8859_9");
        MAP.put("ISO_8859-9", "ISO8859_9");
        MAP.put("LATIN5", "ISO8859_9");
        MAP.put("CSISOLATIN5", "ISO8859_9");
        MAP.put("L5", "ISO8859_9");

        MAP.put("KOI8-R", "KOI8_R");
        MAP.put("CSKOI8-R", "KOI8_R");
        MAP.put("US-ASCII", "ASCII");
        MAP.put("ISO-IR-6", "ASCII");
        MAP.put("ANSI_X3.4-1986", "ASCII");
        MAP.put("ISO_646.IRV:1991", "ASCII");
        MAP.put("ASCII", "ASCII");
        MAP.put("CSASCII", "ASCII");
        MAP.put("ISO646-US", "ASCII");
        MAP.put("US", "ASCII");
        MAP.put("IBM367", "ASCII");
        MAP.put("CP367", "ASCII");
        MAP.put("UTF-8", "UTF8");
        MAP.put("UTF-16", "Unicode");
        MAP.put("UTF-16BE", "UnicodeBig");
        MAP.put("UTF-16LE", "UnicodeLittle");
    }

    /**
     * Gets the java encoding from the IANA encoding. If the encoding cannot be found
     * it returns the input.
     * @param iana the IANA encoding
     * @return the java encoding
     */
    public static String getJavaEncoding(final String iana) {
        String IANA = iana.toUpperCase();
        String jdec = MAP.get(IANA);
        if (jdec == null)
            jdec = iana;
        return jdec;
    }
}
