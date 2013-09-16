package com.itextpdf.text.pdf.mc;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import com.itextpdf.text.xml.XMLUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AcroFieldsFlattenTest {
    public static final String OUT_FOLDER = "./target/com/itextpdf/text/pdf/mc/";
    public static final String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/mc/";

    @Before
    public void init() {
        File dir = new File(OUT_FOLDER);
        if (dir.exists()) {
            deleteDirectory(dir);
        }
        dir.mkdirs();
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

    @Test
    public void fieldFieldsAndFlattenTest() throws IOException, DocumentException, ParserConfigurationException, SAXException, InterruptedException {
        String acroFormFileName = "SF2809.pdf";
        String filledAcroFormFileName = "SF2809_filled.pdf";
        String flattenAcroFormFileName = "SF2809_alt.pdf";
        PdfReader reader = new PdfReader(CMP_FOLDER + acroFormFileName);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUT_FOLDER + filledAcroFormFileName));
        AcroFields form = stamper.getAcroFields();
        for (String key : form.getFields().keySet()) {
            form.setField(key, key);
        }

        stamper.close();

        LoggerFactory.getInstance().setLogger(new SysoLogger());
        reader = new PdfReader(OUT_FOLDER + filledAcroFormFileName);
        MCFieldFlattener flattener = new MCFieldFlattener();
        flattener.process(reader, new FileOutputStream(OUT_FOLDER + flattenAcroFormFileName));
        //compare(OUT_FOLDER + flattenAcroFormFileName, CMP_FOLDER + flattenAcroFormFileName);
    }

    private void compare(String outPdf, String cmpPdf) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        CompareTool ct = new CompareTool(outPdf, cmpPdf);
        Assert.assertNull(ct.compare(OUT_FOLDER, OUT_FOLDER + "difference"));

        String outXml = new File(outPdf).getName();
        String cmpXml = new File(cmpPdf).getName();

        outXml = OUT_FOLDER + outXml.replaceAll(".pdf", "") + ".xml";
        cmpXml = OUT_FOLDER + "cmp_" + cmpXml.replaceAll("cmp_", "").replaceAll(".pdf", "") + ".xml";

        PdfReader reader = new PdfReader(outPdf);
        FileOutputStream xmlOut = new FileOutputStream(outXml);
        new MyTaggedPdfReaderTool().convertToXml(reader, xmlOut);

        reader = new PdfReader(cmpPdf);
        xmlOut = new FileOutputStream(cmpXml);
        new MyTaggedPdfReaderTool().convertToXml(reader, xmlOut);

        Assert.assertTrue(compareXmls(outXml, cmpXml));
    }

    private boolean compareXmls(String xml1, String xml2) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        org.w3c.dom.Document doc1 = db.parse(new File(xml1));
        doc1.normalizeDocument();

        org.w3c.dom.Document doc2 = db.parse(new File(xml2));
        doc2.normalizeDocument();

        return doc2.isEqualNode(doc1);
    }

    static class MyTaggedPdfReaderTool extends TaggedPdfReaderTool {

        @Override
        public void parseTag(String tag, PdfObject object, PdfDictionary page)
                throws IOException {
            if (object instanceof PdfNumber) {
                PdfNumber mcid = (PdfNumber) object;
                RenderFilter filter = new MyMarkedContentRenderFilter(mcid.intValue());
                TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
                FilteredTextRenderListener listener = new FilteredTextRenderListener(
                        strategy, filter);
                PdfContentStreamProcessor processor = new PdfContentStreamProcessor(
                        listener);
                processor.processContent(PdfReader.getPageContent(page), page
                        .getAsDict(PdfName.RESOURCES));
                out.print(XMLUtil.escapeXML(listener.getResultantText(), true));
            } else {
                super.parseTag(tag, object, page);
            }
        }

        @Override
        public void inspectChildDictionary(PdfDictionary k) throws IOException {
            inspectChildDictionary(k, true);
        }


    }

    static class MyMarkedContentRenderFilter extends MarkedContentRenderFilter {

        int mcid;

        public MyMarkedContentRenderFilter(int mcid) {
            super(mcid);
            this.mcid = mcid;
        }

        @Override
        public boolean allowText(TextRenderInfo renderInfo){
            return renderInfo.hasMcid(mcid, true);
        }

    }
}

