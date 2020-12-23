package com.itextpdf.text.pdf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.MessageFormat;
import org.junit.Assert;
import org.junit.Test;

public class DefaultSplitCharacterProfilingTest {

    private static final String INPUT_DIR = "./src/test/resources/com/itextpdf/text/pdf/DefaultSplitCharacterProfilingTest/";

    private static final String CHECK_DATE_PATTERN_FAIL_MESSAGE =
            "The test verifies the optimization of the checkDatePattern method. This failure indicates that the optimization was broken.";

    private static final String READ_FILE_FAIL_MESSAGE = "Failed to read test file {0}. The test could not be completed.";

    private static final int TIME_LIMIT = 20000;

    @Test(timeout = 30000)
    public void checkDatePatternProfilingTest() {
        String testFile = INPUT_DIR + "profilingText.txt";
        String str = readFile(testFile);
        if (str == null) {
            Assert.fail(MessageFormat.format(READ_FILE_FAIL_MESSAGE, testFile));
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 70000; i++) {
            isSplitCharacter(str);
        }
        long time = System.currentTimeMillis() - startTime;
        System.out.println("Test run time: " + time);
        Assert.assertTrue(CHECK_DATE_PATTERN_FAIL_MESSAGE, time < TIME_LIMIT);
    }

    private static void isSplitCharacter(String text) {
        new DefaultSplitCharacter().isSplitCharacter(0, 0, text.length() + 1, text.toCharArray(), null);
    }

    private static String readFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }
}