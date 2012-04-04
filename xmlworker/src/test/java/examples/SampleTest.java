package examples;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.CompareTool;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SampleTest {

    private String outPath;
    private String outPdf;
    private String outImage;
    private String inputHtml;
    private String cmpPdf;
    private String cmpImage;
    private String differenceImage;
    private CompareTool compareTool;

    public SampleTest() {
        compareTool = new CompareTool();
    }

    @Before
    public void setup() throws IOException {
        String testPath = this.getClass().getName();
        testPath = testPath.replace(".", File.separator);
        testPath = testPath.substring(0, testPath.lastIndexOf(File.separator) + 1);
        String testName = getTestName();
        if (testName.length() > 0) {
            if (testName.contains(File.separator)) {
                testName = testName.substring(testName.lastIndexOf(File.separator) + 1, testName.length());
            }
            outPath = "." + File.separator + "target" + File.separator + testPath + File.separator + testName + File.separator;
            String inputPath = "." + File.separator + "target" + File.separator + "test-classes" + File.separator + testPath + File.separator + testName + File.separator;
            differenceImage = outPath + "difference.png";
            outPdf = testName + ".pdf";
            outImage = "<testName>-%03d.png".replaceAll("<testName>", testName);
            inputHtml = inputPath + "<testName>.html".replaceAll("<testName>", testName);
            cmpPdf = inputPath + "<testName>.pdf".replaceAll("<testName>", testName);
            cmpImage = "cmp_<testName>-%03d.png".replaceAll("<testName>", testName);
            File dir = new File(outPath);
            if (dir.exists()) {
                deleteDirectory(dir);
            }
            dir.mkdirs();
        }
    }

    @Test(timeout = 60000)
    public void test() throws IOException, DocumentException, InterruptedException {
        String testName = getTestName();
        if (!this.getClass().getName().equals(SampleTest.class.getName()) && (testName.length() > 0)) {
            transformHtml2Pdf();
            if (detectCrashesAndHangUpsOnly() == false) {
                String errorMessage = compareTool.compare(outPdf, cmpPdf, outPath, outImage, cmpImage, differenceImage);
                if (errorMessage != null) {
                    Assert.fail(errorMessage);
                }
            }
        }
    }

    protected String getTestName() {
        return "";
    }

    protected boolean detectCrashesAndHangUpsOnly() {
        return false;
    }

    private void transformHtml2Pdf() throws IOException, DocumentException, InterruptedException {
        Document doc = new Document(PageSize.A4);
        PdfWriter
        pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outPath + outPdf));
        doc.open();
        doc.setMargins(0, 0, 0, 0);
        XMLWorkerHelper.getInstance()
                .parseXHtml(pdfWriter, doc, new FileInputStream(inputHtml), SampleTest.class.getResourceAsStream("sampleTest.css"),
                        null);
        doc.close();
    }

    private void deleteDirectory(File path) {
        if (path == null)
            return;
        if (path.exists()) {
            for (File f : path.listFiles()) {
                if (f.isDirectory()) {
                    deleteDirectory(f);
                    f.delete();
                } else {
                    f.delete();
                }
            }
            path.delete();
        }
    }
}
