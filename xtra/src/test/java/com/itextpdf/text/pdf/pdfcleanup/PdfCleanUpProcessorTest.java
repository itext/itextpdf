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
        List<PdfCleanUpLocation> cleanUpLocations4 = Arrays.asList(new PdfCleanUpLocation(1, new Rectangle(212, 394, 212 + 186, 394 + 170), null));

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
                                             {"smaskImage.pdf", "smaskImage.pdf", "cmp_smaskImage.pdf", cleanUpLocations2},
                                             {"rotatedImg.pdf", "rotatedImg.pdf", "cmp_rotatedImg.pdf", cleanUpLocations2},
                                             {"lineArtsCompletely.pdf", "lineArtsCompletely.pdf", "cmp_LineArtsCompletely.pdf", null},
                                             {"lineArtsPartially.pdf", "lineArtsPartially.pdf", "cmp_lineArtsPartially.pdf", null},
                                             {"dashedStyledClosedBezier.pdf", "dashedStyledClosedBezier.pdf", "cmp_dashedStyledClosedBezier.pdf", null},
                                             {"styledLineArts.pdf", "styledLineArts.pdf", "cmp_styledLineArts.pdf", null},
                                             {"dashedBezier.pdf", "dashedBezier.pdf", "cmp_dashedBezier.pdf", null},
                                             {"closedBezier.pdf", "closedBezier.pdf", "cmp_closedBezier.pdf", null},
                                             {"clippingNWRule.pdf", "clippingNWRule.pdf", "cmp_clippingNWRule.pdf", null},
                                             {"dashedClosedRotatedTriangles.pdf", "dashedClosedRotatedTriangles.pdf", "cmp_dashedClosedRotatedTriangles.pdf", null},
                                             {"miterTest.pdf", "miterTest.pdf", "cmp_miterTest.pdf", null},
                                             {"degenerateCases.pdf", "degenerateCases.pdf", "cmp_degenerateCases.pdf", null},
                                             {"absentICentry.pdf", "absentICentry.pdf", "cmp_absentICentry.pdf", null},
                                             {"lotOfDashes.pdf", "lotOfDashes.pdf", "cmp_lotOfDashes.pdf", null},
                                             {"clipPathReduction.pdf", "clipPathReduction.pdf", "cmp_clipPathReduction.pdf", cleanUpLocations4},
        });
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
