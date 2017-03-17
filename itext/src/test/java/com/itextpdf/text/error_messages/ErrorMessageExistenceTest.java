/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.error_messages;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorMessageExistenceTest {

    private static String[] LANGUAGES = {"nl", "en"};
    private static String SOURCE_FILES_EXTENSION = ".java";
    private static String SOURCE_CODE_ROOT_PATH = "./";
    private List<File> sourceFiles;
    private List<String> nonLozalizedMessageErrors;
    Pattern pattern;

    @Before
    public void setUp() throws IOException {
        pattern = Pattern.compile("MessageLocalization.getComposedMessage\\(\"([^\"]*)\"");
        sourceFiles = new ArrayList<File>();
        nonLozalizedMessageErrors = new ArrayList<String>();
        addSourceFilesRecursively(new File(SOURCE_CODE_ROOT_PATH));
    }

    @Test
    public void test() throws IOException {
        for (String language : LANGUAGES) {
            testSingleLanguageLocalization(language);
        }

        Assert.assertTrue("There exist messages without localization.", nonLozalizedMessageErrors.isEmpty());
    }

    private void addSourceFilesRecursively(File folder) throws IOException {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                addSourceFilesRecursively(file);
            }
            else if (file.getName().endsWith(SOURCE_FILES_EXTENSION)) {
                sourceFiles.add(file);
            }
        }
    }

    private void testSingleLanguageLocalization(String language) throws IOException {
        MessageLocalization.setLanguage(language, null);

        for (File file : sourceFiles) {
            testOneSourceFile(file, language);
        }
    }

    private void testOneSourceFile(File file, String language) throws IOException {
        String lineSeparator = System.getProperty("line.separator");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder fileContents = new StringBuilder((int)file.length());
        String line = null;
        while ((line = reader.readLine()) != null) {
            fileContents.append(line);
            fileContents.append(lineSeparator);
        }
        reader.close();

        Matcher matcher = pattern.matcher(fileContents);
        while (matcher.find()) {
            String key = matcher.group(1);
            String assertMessage = language + " localization for " + key + " message was not found. File " + file.getAbsolutePath();

            if (MessageLocalization.getMessage(key, false).startsWith("No message found for")) {
                System.out.println(assertMessage);
                nonLozalizedMessageErrors.add(assertMessage);
            }
        }
    }
}
