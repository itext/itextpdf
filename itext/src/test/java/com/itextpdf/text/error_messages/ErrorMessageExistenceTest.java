package com.itextpdf.text.error_messages;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
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

            //Assert.assertFalse(assertMessage, MessageLocalization.getMessage(key, false).startsWith("No message found for"));
        }
    }
}
