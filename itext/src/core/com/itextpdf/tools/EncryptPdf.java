/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.tools;

import java.io.FileOutputStream;
import java.util.HashMap;

import com.itextpdf.text.pdf.PdfEncryptor;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Encrypts a PDF document. It needs iText (http://www.lowagie.com/iText).
 * @author Paulo Soares (psoares@consiste.pt)
 * @since 2.1.1 (renamed to follow Java naming conventions)
 */
public class EncryptPdf {
    
    private final static int INPUT_FILE = 0;
    private final static int OUTPUT_FILE = 1;
    private final static int USER_PASSWORD = 2;
    private final static int OWNER_PASSWORD = 3;
    private final static int PERMISSIONS = 4;
    private final static int STRENGTH = 5;
    private final static int MOREINFO = 6;
    private final static int permit[] = {
    	PdfWriter.ALLOW_PRINTING,
    	PdfWriter.ALLOW_MODIFY_CONTENTS,
    	PdfWriter.ALLOW_COPY,
    	PdfWriter.ALLOW_MODIFY_ANNOTATIONS,
    	PdfWriter.ALLOW_FILL_IN,
    	PdfWriter.ALLOW_SCREENREADERS,
    	PdfWriter.ALLOW_ASSEMBLY,
        PdfWriter.ALLOW_DEGRADED_PRINTING};

    private static void usage() {
        System.out.println("usage: input_file output_file user_password owner_password permissions 128|40 [new info string pairs]");
        System.out.println("permissions is 8 digit long 0 or 1. Each digit has a particular security function:");
        System.out.println();
        System.out.println("AllowPrinting");
        System.out.println("AllowModifyContents");
        System.out.println("AllowCopy");
        System.out.println("AllowModifyAnnotations");
        System.out.println("AllowFillIn (128 bit only)");
        System.out.println("AllowScreenReaders (128 bit only)");
        System.out.println("AllowAssembly (128 bit only)");
        System.out.println("AllowDegradedPrinting (128 bit only)");
        System.out.println("Example permissions to copy and print would be: 10100000");
    }
    
    /**
     * Encrypts a PDF document.
     * 
     * @param args input_file output_file user_password owner_password permissions 128|40 [new info string pairs]
     */
    public static void main (String args[]) {
        System.out.println("PDF document encryptor");
        if (args.length <= STRENGTH || args[PERMISSIONS].length() != 8) {
            usage();
            return;
        }
        try {
            int permissions = 0;
            String p = args[PERMISSIONS];
            for (int k = 0; k < p.length(); ++k) {
                permissions |= (p.charAt(k) == '0' ? 0 : permit[k]);
            }
            System.out.println("Reading " + args[INPUT_FILE]);
            PdfReader reader = new PdfReader(args[INPUT_FILE]);
            System.out.println("Writing " + args[OUTPUT_FILE]);
            HashMap moreInfo = new HashMap();
            for (int k = MOREINFO; k < args.length - 1; k += 2)
                moreInfo.put(args[k], args[k + 1]);
            PdfEncryptor.encrypt(reader, new FileOutputStream(args[OUTPUT_FILE]),
                args[USER_PASSWORD].getBytes(), args[OWNER_PASSWORD].getBytes(), permissions, args[STRENGTH].equals("128"), moreInfo);
            System.out.println("Done.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}