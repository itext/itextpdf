package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class PdfCleanUpProcessorTest {

    private static final String INPUT_PATH = "./src/test/resources/com/itextpdf/text/pdf/pdfcleanup/";
    private static final String OUTPUT_PATH = "./target/test/com/itextpdf/text/pdf/pdfcleanup/";

    private String input;
    private String output;
    private String cmp;
    private List<PdfCleanUpLocation> cleanUpLocations;

    public PdfCleanUpProcessorTest(Object inputFileName, Object outputFileName, Object cmpFileName, Object cleanUpLocations) {
        this.input = INPUT_PATH + inputFileName;
        this.output = OUTPUT_PATH + outputFileName;
        this.cmp = INPUT_PATH + cmpFileName;
        this.cleanUpLocations = (List<PdfCleanUpLocation>) cleanUpLocations;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<PdfCleanUpLocation> cleanUpLocations1 = Arrays.asList(new PdfCleanUpLocation(1, new Rectangle(240.0f, 602.3f, 275.7f, 614.8f), BaseColor.GRAY),
                                                                   new PdfCleanUpLocation(1, new Rectangle(171.3f, 550.3f, 208.4f, 562.8f), BaseColor.GRAY),
                                                                   new PdfCleanUpLocation(1, new Rectangle(270.7f, 459.2f, 313.1f, 471.7f), BaseColor.GRAY),
                                                                   new PdfCleanUpLocation(1, new Rectangle(249.9f, 329.3f, 279.6f, 341.8f), BaseColor.GRAY),
                                                                   new PdfCleanUpLocation(1, new Rectangle(216.2f, 303.3f, 273.0f, 315.8f), BaseColor.GRAY));

        List<PdfCleanUpLocation> cleanUpLocations2 = Arrays.asList(new PdfCleanUpLocation(1, new Rectangle(97f, 405f, 480f, 445f), BaseColor.GRAY));
        List<PdfCleanUpLocation> cleanUpLocations3 = Arrays.asList(new PdfCleanUpLocation(1, new Rectangle(97f, 605f, 480f, 645f), BaseColor.GRAY));

        return Arrays.asList(new Object[][] {{"page229.pdf", "page229_01.pdf", "cmp_page229_01.pdf", cleanUpLocations1},
                                             {"page229-modified-Tc-Tw.pdf", "page229-modified-Tc-Tw.pdf", "cmp_page229-modified-Tc-Tw.pdf", cleanUpLocations1},
                                             {"page166_03.pdf", "page166_03.pdf", "cmp_page166_03.pdf", null},
                                             {"page166_04.pdf", "page166_04.pdf", "cmp_page166_04.pdf", null},
                                             {"hello_05.pdf", "hello_05.pdf", "cmp_hello_05.pdf", null},
                                             {"BigImage-jpg.pdf", "BigImage-jpg.pdf", "cmp_BigImage-jpg.pdf", null},
                                             {"BigImage-png.pdf", "BigImage-png.pdf", "cmp_BigImage-png.pdf", null},
                                             {"BigImage-tif.pdf", "BigImage-tif.pdf", "cmp_BigImage-tif.pdf", null},
                                             {"BigImage-tif-lzw.pdf", "BigImage-tif-lzw.pdf", "cmp_BigImage-tif-lzw.pdf", null},
                                             {"simpleImmediate.pdf", "simpleImmediate.pdf", "cmp_simpleImmediate.pdf", cleanUpLocations2},
                                             {"simpleImmediate-tm.pdf", "simpleImmediate-tm.pdf", "cmp_simpleImmediate-tm.pdf", cleanUpLocations2},
                                             {"multiUseIndirect.pdf", "multiUseIndirect.pdf", "cmp_multiUseIndirect.pdf", cleanUpLocations3},
                                             {"multiUseImage.pdf", "multiUseImage.pdf", "cmp_multiUseImage.pdf", cleanUpLocations2},
                                             {"smaskImage.pdf", "smaskImage.pdf", "cmp_smaskImage.pdf", cleanUpLocations2}});
    }

    @Test
    public void cleanUp() throws IOException, DocumentException, InterruptedException {
        cleanUp(input, output, cleanUpLocations);
        compareByContent(cmp, output, OUTPUT_PATH, "diff");

    }

    private void cleanUp(String input, String output, List<PdfCleanUpLocation> cleanUpLocations) throws IOException, DocumentException {
        File outDir = new File(OUTPUT_PATH);

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        PdfReader reader = new PdfReader(input);
        FileOutputStream fos = new FileOutputStream(output);
        PdfStamper stamper = new PdfStamper(reader, fos);

        PdfCleanUpProcessor cleaner = (cleanUpLocations == null)? new PdfCleanUpProcessor(stamper) : new PdfCleanUpProcessor(cleanUpLocations, stamper);
        cleaner.cleanUp();

        stamper.close();
        fos.close();
        reader.close();
    }

    private void compareByContent(String cmp, String output, String targetDir, String operation) throws DocumentException, InterruptedException, IOException {
        CompareTool cmpTool = new CompareTool();
        String errorMessage = cmpTool.compareByContent(output, cmp, targetDir, operation);

        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}
