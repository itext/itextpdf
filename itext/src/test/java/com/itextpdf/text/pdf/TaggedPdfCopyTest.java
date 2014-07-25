package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.parser.*;
import org.junit.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class TaggedPdfCopyTest {
    Document document;
    PdfCopy copy;
    String output;


    public static final String NO_PARENT_TREE = "The document does not contain ParentTree";
    public static final String NO_CLASS_MAP = "The document does not contain ClassMap";
    public static final String NO_ROLE_MAP = "The document does not contain RoleMap";
    public static final String NO_STRUCT_TREE_ROOT = "No StructTreeRoot found";

    public static final String SOURCE4 =  "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source4.pdf";
    public static final String SOURCE10 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source10.pdf";
    public static final String SOURCE11 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source11.pdf";
    public static final String SOURCE12 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source12.pdf";
    public static final String SOURCE16 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source16.pdf";
    public static final String SOURCE17 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source17.pdf";
    public static final String SOURCE18 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source18.pdf";
    public static final String SOURCE19 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source19.pdf";
    public static final String SOURCE22 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source22.pdf";
    public static final String SOURCE32 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source32.pdf";
    public static final String SOURCE42 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source42.pdf";
    public static final String SOURCE51 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source51.pdf";
    public static final String SOURCE52 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source52.pdf";
    public static final String SOURCE53 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source53.pdf";
    public static final String SOURCE61 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source61.pdf";
    public static final String SOURCE62 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source62.pdf";
    public static final String SOURCE63 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source63.pdf";
    public static final String SOURCE64 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source64.pdf";
    public static final String SOURCE72 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source72.pdf";
    public static final String SOURCE73 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/source73.pdf";
    public static final String DEV_805 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/pdf/dev-805.pdf";

    public static final String OUT = "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out";

    public static final PdfDictionary CM31 = new PdfDictionary();
    public static final PdfDictionary sElem = new PdfDictionary();
    //<</O/Layout/EndIndent 18.375/StartIndent 11.25/TextIndent -11.25/LineHeight 13>>
    //<</C/SC.7.147466/Pg 118 0 R/Type/StructElem/K 3/S/Span/Lang(en)/P 1 0 R>>

    static {
        CM31.put(PdfName.O, new PdfName("Layout"));
        CM31.put(new PdfName("EndIndent"), new PdfNumber(18.375));
        CM31.put(new PdfName("StartIndent"), new PdfNumber(11.25));
        CM31.put(new PdfName("TextIndent"), new PdfNumber(-11.25));
        CM31.put(new PdfName("LineHeight"), new PdfNumber(13));
        sElem.put(PdfName.C, new PdfName("SC.7.147466"));
        sElem.put(PdfName.K, new PdfNumber(5));
        sElem.put(PdfName.S, PdfName.SPAN);
        sElem.put(PdfName.LANG, new PdfString("en"));
    }

    @Before
    public void init() throws FileNotFoundException, DocumentException {
        new File("./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/").mkdirs();
        Document.compress = false;
    }

    private void initializeDocument(String name) throws DocumentException, FileNotFoundException {
        this.output = OUT + name + ".pdf";
        document = new Document();
        copy = new PdfCopy(document, new FileOutputStream(output));
        copy.setTagged();
        document.open();
    }

    @Test
    public void classMapConflict() throws IOException, DocumentException {
        initializeDocument("-cmc");
        PdfReader reader1 = new PdfReader(SOURCE11);
        try {
            copy.addPage(copy.getImportedPage(reader1, 76, true));
        } catch (BadPdfFormatException e) {}
        reader1.close();
        PdfReader reader2 = new PdfReader(SOURCE12);
        boolean exceptionThrown = false;
        try {
            copy.addPage(copy.getImportedPage(reader2, 76, true));
        } catch (BadPdfFormatException bpfe) {
            exceptionThrown = true;
        }
        reader2.close();
        if (!exceptionThrown)
            Assert.fail("BadPdfFormatException expected!");
    }

    @Test
    public void roleMapConflict() throws IOException, DocumentException {
        initializeDocument("-rolemap");

        PdfReader reader1 = new PdfReader(SOURCE11);
        //PdfDictionary trailer = reader1.trailer;
        try {
            copy.addPage(copy.getImportedPage(reader1, 76, true));
        } catch (BadPdfFormatException e) {}
        reader1.close();
        PdfReader reader2 = new PdfReader(SOURCE22);
        boolean exceptionThrown = false;
        try {
            copy.addPage(copy.getImportedPage(reader2, 76, true));
        } catch (BadPdfFormatException bpfe) {
            exceptionThrown = true;
        }
        reader2.close();
        if (!exceptionThrown)
            Assert.fail("BadPdfFormatException expected!");
    }

    @Test
    public void pdfMergeTest() throws IOException, DocumentException {
        initializeDocument("-merge");

        int n = 14;
        PdfReader reader1 = new PdfReader(SOURCE11);
        copy.addPage(copy.getImportedPage(reader1, 76, true));
        copy.addPage(copy.getImportedPage(reader1, 83, true));
        PdfReader reader2 = new PdfReader(SOURCE32);
        copy.addPage(copy.getImportedPage(reader2, 69, true));
        copy.addPage(copy.getImportedPage(reader2, 267, true));
        document.close();
        reader1.close();
        reader2.close();
        PdfReader reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 2, "Kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);

        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        int[] nums = new int[] {44, 0, 65, 42, 0, 0, 0, 0, 0, 0, 0, 0, 0, 81};
        for (int i = 0; i < n; ++i)
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1), true);

        PdfDictionary ClassMap = verifyIsDictionary(PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.CLASSMAP)), NO_CLASS_MAP);
        PdfDictionary CM31 = verifyIsDictionary(PdfStructTreeController.getDirectObject(ClassMap.get(new PdfName("CM31"))), "ClassMap does not contain.\"CM31\"");
        if (!PdfStructTreeController.compareObjects(TaggedPdfCopyTest.CM31, CM31))
            Assert.fail("ClassMap contains incorrect \"CM31\"");

        PdfDictionary RoleMap = verifyIsDictionary(PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.ROLEMAP)), NO_ROLE_MAP);
        if (!PdfName.SPAN.equals(RoleMap.get(new PdfName("ParagraphSpan"))))
            throw new BadPdfFormatException("RoleMap does not contain \"ParagraphSpan\"");

        reader.close();
    }

    @Test
    public void copyTaggedPdf0() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("0");
        PdfReader reader = new PdfReader(SOURCE11);
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; ++i)
            copy.addPage(copy.getImportedPage(reader, i, true));
        document.close();
        reader.close();

        Assert.assertEquals(getCommonNumsCount(SOURCE11), getCommonNumsCount(output));

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = (PdfDictionary)reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT);


        PdfDictionary ClassMap = verifyIsDictionary(PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.CLASSMAP)), NO_CLASS_MAP);
        if (ClassMap.size() != 109) Assert.fail("ClassMap incorrect");
        String[] CMs = new String[]{"CM84", "CM81", "CM80", "CM87", "CM88", "CM9", "CM94", "CM95", "CM96",
                "CM97", "CM90", "CM91", "CM92", "CM93", "CM98", "CM99", "CM16", "CM17", "CM14", "CM15",
                "CM12", "CM13", "CM10", "CM19", "CM20", "CM21", "CM22", "CM23", "CM24", "CM25", "CM26",
                "CM27", "CM28", "CM29", "CM100", "CM101", "CM102", "CM103", "CM105", "CM106", "CM30",
                "CM31", "CM34", "CM35", "CM32", "CM33", "CM38", "CM39", "CM36", "CM118", "CM117", "CM49",
                "CM48", "CM116", "CM115", "CM47", "CM114", "CM46", "CM113", "CM45", "CM112", "CM44", "CM43",
                "CM111", "CM42", "CM110", "CM41", "CM108", "CM109", "CM127", "CM126", "CM58", "CM129",
                "CM128", "CM55", "CM123", "CM54", "CM125", "CM57", "CM56", "CM51", "CM50", "CM53", "CM120",
                "CM52", "CM119", "CM68", "CM136", "CM135", "CM67", "CM133", "CM139", "CM60", "CM132", "CM64",
                "CM63", "CM62", "CM61", "CM145", "CM76", "CM78", "CM1", "CM2", "CM71", "CM70", "CM73",
                "CM141", "CM72", "CM74"};
        for (int i = 0; i < CMs.length; ++i)
            verifyIsDictionary(PdfStructTreeController.getDirectObject(ClassMap.get(new PdfName(CMs[i]))), "ClassMap does not contain \""+CMs[i]+"\"");

        PdfDictionary RoleMap = verifyIsDictionary(PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.ROLEMAP)), NO_ROLE_MAP);
        if (!PdfName.SPAN.equals(RoleMap.get(new PdfName("ParagraphSpan"))))
            throw new BadPdfFormatException("RoleMap does not contain \"ParagraphSpan\".");

        //if (reader.eofPos != 3378440L) Assert.fail("Invalid size of pdf.");
        reader.close();
        compareResults("0");
    }

    @Test
    public void copyTaggedPdf1() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("1");
        PdfReader reader = new PdfReader(SOURCE32);
        copy.addPage(copy.getImportedPage(reader, 5, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, 22, "Nums");
        verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(1)), 61, "Nums of page 1");
        reader.close();
        compareResults("1");
    }

    @Test
    public void copyTaggedPdf2() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("2");
        PdfReader reader = new PdfReader(SOURCE16);
        copy.addPage(copy.getImportedPage(reader, 2, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, 2, "Nums");
        verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(1)), 7, "Nums of page 1");
        reader.close();
        compareResults("2");
    }

    @Test
    public void copyTaggedPdf3() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("3");
        PdfReader reader = new PdfReader(SOURCE10);
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; ++i)
            copy.addPage(copy.getImportedPage(reader, i, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, n*2, "Nums");
        int[] nums = new int[] {16, 87, 128, 74, 74, 74, 26};
        for (int i = 0; i < n; ++i)
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

        reader.close();
        compareResults("3");
    }

    @Test
    public void copyTaggedPdf4() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("4");
        PdfReader reader = new PdfReader(SOURCE10);
        int n = reader.getNumberOfPages();
        for (int i = n; i > 0; --i)
            copy.addPage(copy.getImportedPage(reader, i, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 7, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, n*2, "Nums");
        int[] nums = new int[] {26, 74, 74, 74, 128, 87, 16};
        for (int i = 0; i < n; ++i)
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

        reader.close();
        compareResults("4");
    }

    @Test
    public void copyTaggedPdf5() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("5");
        PdfReader reader = new PdfReader(SOURCE10);
        int n = 3;
        copy.addPage(copy.getImportedPage(reader, 1, true));
        copy.addPage(copy.getImportedPage(reader, 3, true));
        copy.addPage(copy.getImportedPage(reader, 7, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, n*2, "Nums");
        int[] nums = new int[] {16, 128, 26};
        for (int i = 0; i < n; ++i)
            //nums[i] = ((PdfArray)PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1))).size();
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

        reader.close();
        compareResults("5");
    }

    @Test
    public void copyTaggedPdf6() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("6");
        PdfReader reader = new PdfReader(SOURCE11);
        int n = 12;
        copy.addPage(copy.getImportedPage(reader, 1, true));
        copy.addPage(copy.getImportedPage(reader, 25, true));
        copy.addPage(copy.getImportedPage(reader, 7, true));
        copy.addPage(copy.getImportedPage(reader, 48, true));
        copy.addPage(copy.getImportedPage(reader, 50, true));
        copy.addPage(copy.getImportedPage(reader, 2, true));
        copy.addPage(copy.getImportedPage(reader, 8, true));
        copy.addPage(copy.getImportedPage(reader, 90, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 6, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, n*2, "Nums");
        int[] nums = new int[] {5, 0, 33, 12, 0, 48, 35, 182, 0, 0, 17, 37};
        for (int i = 0; i < n; ++i)
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1), true);

        PdfDictionary ClassMap = verifyIsDictionary(PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.CLASSMAP)), NO_CLASS_MAP);
        if (ClassMap.size() != 27) Assert.fail("ClassMap incorrect");
        String[] CMs = new String[]{"CM118", "CM117", "CM133", "CM47", "CM46", "CM114", "CM43", "CM110", "CM21", "CM22", "CM26", "CM27", "CM145", "CM128", "CM29", "CM56", "CM1", "CM2", "CM72", "CM16", "CM34", "CM17", "CM14", "CM15", "CM119", "CM12", "CM13"};
        for (int i = 0; i < CMs.length; ++i)
            verifyIsDictionary(PdfStructTreeController.getDirectObject(ClassMap.get(new PdfName(CMs[i]))), "ClassMap.does.not.contain.\""+CMs[i]+"\"");

        PdfDictionary RoleMap = verifyIsDictionary(PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.ROLEMAP)), NO_ROLE_MAP);
        if (!PdfName.SPAN.equals(RoleMap.get(new PdfName("ParagraphSpan"))))
            throw new BadPdfFormatException("RoleMap does not contain \"ParagraphSpan\".");
        //if (reader.eofPos != 249068) Assert.fail("Invalid size of pdf.");

        reader.close();
        compareResults("6");
    }

    @Test
    public void copyTaggedPdf7() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("7");
        PdfReader reader = new PdfReader(SOURCE16);
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; ++i){
            copy.addPage(copy.getImportedPage(reader, i, true));
            copy.addPage(copy.getImportedPage(reader, i, true));
        }
        for (int i = 1; i <= n; ++i)
            copy.addPage(copy.getImportedPage(reader, i, true));
        for (int i = 1; i <= n; ++i)
            copy.addPage(copy.getImportedPage(reader, i, true));
        n *= 4;
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 5, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, n*2, "Nums");
        int[] nums = new int[] {48, 48, 7, 7, 48, 7, 48, 7};
        for (int i = 0; i < n; ++i)
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

        reader.close();
        compareResults("7");
    }

    @Test
    public void copyTaggedPdf8() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("8");
        PdfReader reader = new PdfReader(SOURCE42);
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; ++i){
            copy.addPage(copy.getImportedPage(reader, i, true));
            copy.addPage(copy.getImportedPage(reader, i, true));
        }
        for (int i = 1; i <= n; ++i)
            copy.addPage(copy.getImportedPage(reader, i, true));
        for (int i = 1; i <= n; ++i)
            copy.addPage(copy.getImportedPage(reader, i, true));
        n = 52;
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 6, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, n*2, "Nums");
//        int[] nums = new int[] {42, 42, 11, 11, 13, 13, 42, 11, 13, 42, 11, 13};
//        for (int i = 0; i < n; ++i)
//            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

        reader.close();
        compareResults("8");
    }

    @Test
    public void copyTaggedPdf9() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("9");
        PdfReader reader4 = new PdfReader(SOURCE4);
        PdfReader reader10 = new PdfReader(SOURCE10);
        PdfReader reader32 = new PdfReader(SOURCE32);
        int n = 40;
        copy.addPage(copy.getImportedPage(reader4, 1, true));
        copy.addPage(copy.getImportedPage(reader10, 2, true));
        copy.addPage(copy.getImportedPage(reader10, 3, true));
        copy.addPage(copy.getImportedPage(reader10, 7, true));
        copy.addPage(copy.getImportedPage(reader32, 50, true));
        copy.addPage(copy.getImportedPage(reader32, 55, true));
        copy.addPage(copy.getImportedPage(reader4, 1, true));
        copy.addPage(copy.getImportedPage(reader32, 50, true));
        copy.addPage(copy.getImportedPage(reader32, 55, true));
        copy.addPage(copy.getImportedPage(reader32, 56, true));
        copy.addPage(copy.getImportedPage(reader32, 60, true));
        copy.addPage(copy.getImportedPage(reader10, 3, true));
        copy.addPage(copy.getImportedPage(reader10, 4, true));
        copy.addPage(copy.getImportedPage(reader10, 1, true));
        copy.addPage(copy.getImportedPage(reader32, 1, true));
        copy.addPage(copy.getImportedPage(reader32, 15, true));
        copy.addPage(copy.getImportedPage(reader32, 20, true));
        copy.addPage(copy.getImportedPage(reader32, 5, true));
        copy.addPage(copy.getImportedPage(reader4, 1, true));
        copy.addPage(copy.getImportedPage(reader10, 7, true));

        document.close();
        reader4.close();
        reader10.close();
        reader32.close();

        PdfReader reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 11, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, n*2, "Nums");
        int[] nums = new int[] {7, 87, 128, 26, 135, 0, 0, 83, 7, 135, 0, 0, 0, 0, 0, 0, 83, 116, 26, 128, 74, 16, 12, 0, 0, 38, 54, 61, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 26};
        for (int i = 0; i < n; ++i)
//            nums[i] = ((PdfArray)PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1))).size();
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1), true);

        reader.close();
        compareResults("9");
    }

    @Test
    public void copyTaggedPdf10() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        //source17: StructTreeRoot has no kids - incorrect syntax of tags - try to fix in result pdf
        initializeDocument("10");
        PdfReader reader = new PdfReader(SOURCE17);
        copy.addPage(copy.getImportedPage(reader, 2, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, 2, "Nums");
        verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(1)), 7, "Nums of page 1");
        reader.close();
        compareResults("10");
    }

    @Test
    public void copyTaggedPdf11() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        //source51: invalid nums - references to PdfDictionary, all pages has the same "NumDictionary"
        // 58 0 obj
        // <</Nums[0 2 0 R 1 2 0 R 2 2 0 R 3 2 0 R 4 2 0 R 5 2 0 R 6 2 0 R]>>
        // endobj
        //where 2 0 R is StructElement of Document
        initializeDocument("11");
        PdfReader reader = new PdfReader(SOURCE51);
        boolean exceptionThrown = false;
        try {
            copy.addPage(copy.getImportedPage(reader, 2, true));
        } catch (BadPdfFormatException e) {
            exceptionThrown = true;
        }
        //document.close();
        reader.close();

        if (!exceptionThrown)
            Assert.fail("BadPdfFormatException expected!");

//        reader = new PdfReader(output);
//        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
//        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
//        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
//        verifyIsDictionary(obj, NO_PARENT_TREE);
//        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
//        verifyArraySize(array, 2, "Nums");
//        verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(1)), 7, "Nums of page 1");
//        reader.close();
//        compareResults("11");
    }

    @Test
    public void copyTaggedPdf12() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        //source52: Nums array is empty:
        // 58 0 obj
        // <</Nums[                                                       ]>>
        // endobj
        initializeDocument("12");
        PdfReader reader = new PdfReader(SOURCE52);
        boolean exceptionThrown = false;
        try {
            copy.addPage(copy.getImportedPage(reader, 2, true));
        } catch (BadPdfFormatException e) {
            exceptionThrown = true;
        }
        //document.close();
        reader.close();

        if (!exceptionThrown)
            Assert.fail("BadPdfFormatException expected!");

//        reader = new PdfReader(output);
//        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
//        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
//        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
//        verifyIsDictionary(obj, NO_PARENT_TREE);
//        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
//        verifyArraySize(array, 2, "Nums");
//        verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(1)), 7, "Nums of page 1");
//        reader.close();
//        compareResults("12");
    }

    @Test
    public void copyTaggedPdf13() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        //source53: StructTreeRoot doesn't have kids and Nums is empty
        initializeDocument("13");
        PdfReader reader = new PdfReader(SOURCE53);
        boolean exceptionThrown = false;
        try {
            copy.addPage(copy.getImportedPage(reader, 2, true));
        } catch (BadPdfFormatException e) {
            exceptionThrown = true;
        }
        //document.close();
        reader.close();

        if (!exceptionThrown)
            Assert.fail("BadPdfFormatException expected!");

//        reader = new PdfReader(output);
//        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
//        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
//        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
//        verifyIsDictionary(obj, NO_PARENT_TREE);
//        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
//        verifyArraySize(array, 2, "Nums");
//        verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(1)), 7, "Nums of page 1");
//        reader.close();
//        compareResults("13");
    }

    @Test
    public void copyTaggedPdf14() throws IOException, DocumentException {
        initializeDocument("14");
        PdfReader reader = new PdfReader(SOURCE11);

        copy.addPage(copy.getImportedPage(reader, 5, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 1, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, 8, "Nums");
        verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(1)), 20, "Nums of page 1");
        reader.close();
    }

    @Test
    public void copyTaggedPdf15() throws IOException, DocumentException {
        initializeDocument("15");
        copy.setMergeFields();

        PdfReader reader1 = new PdfReader(SOURCE61);
        PdfReader reader2 = new PdfReader(SOURCE62);
        copy.addDocument(reader1);
        copy.addDocument(reader2);
        document.close();
        reader1.close();
        reader2.close();

        PdfReader reader = new PdfReader(output);
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary structTreeRoot = catalog.getAsDict(PdfName.STRUCTTREEROOT);
        PdfDictionary structParent = structTreeRoot.getAsDict(PdfName.PARENTTREE);
        PdfArray nums = structParent.getAsArray(PdfName.NUMS);
        PdfDictionary acroForm = catalog.getAsDict(PdfName.ACROFORM);
        PdfDictionary fonts = acroForm.getAsDict(PdfName.DR).getAsDict(PdfName.FONT);

        Assert.assertEquals(new PdfName("Helvetica"), fonts.getAsDict(new PdfName("Helv")).getAsName(PdfName.BASEFONT));
        Assert.assertEquals(new PdfName("ZapfDingbats"), fonts.getAsDict(new PdfName("ZaDb")).getAsName(PdfName.BASEFONT));
        Assert.assertEquals(new PdfName("ArialMT"), fonts.getAsDict(new PdfName("ArialMT")).getAsName(PdfName.BASEFONT));
        Assert.assertEquals(new PdfName("CourierStd"), fonts.getAsDict(new PdfName("CourierStd")).getAsName(PdfName.BASEFONT));

        Assert.assertEquals(1, nums.getAsNumber(2).intValue());
        Assert.assertEquals(4, nums.getAsNumber(8).intValue());

        Assert.assertEquals(acroForm.getAsArray(PdfName.FIELDS).getAsIndirectObject(0).getNumber(), nums.getAsDict(3).getAsDict(PdfName.K).getAsIndirectObject(PdfName.OBJ).getNumber());
        Assert.assertEquals(acroForm.getAsArray(PdfName.FIELDS).getAsIndirectObject(2).getNumber(), nums.getAsDict(9).getAsDict(PdfName.K).getAsIndirectObject(PdfName.OBJ).getNumber());
        Assert.assertEquals(acroForm.getAsArray(PdfName.FIELDS).getAsDict(1).getAsArray(PdfName.KIDS).getAsIndirectObject(0).getNumber(), nums.getAsDict(5).getAsDict(PdfName.K).getAsIndirectObject(PdfName.OBJ).getNumber());
        Assert.assertEquals(acroForm.getAsArray(PdfName.FIELDS).getAsDict(1).getAsArray(PdfName.KIDS).getAsIndirectObject(1).getNumber(), nums.getAsDict(11).getAsDict(PdfName.K).getAsIndirectObject(PdfName.OBJ).getNumber());

        Assert.assertEquals(1, acroForm.getAsArray(PdfName.FIELDS).getAsDict(0).getAsNumber(PdfName.STRUCTPARENT).intValue());
        Assert.assertEquals(2, acroForm.getAsArray(PdfName.FIELDS).getAsDict(1).getAsArray(PdfName.KIDS).getAsDict(0).getAsNumber(PdfName.STRUCTPARENT).intValue());
        Assert.assertEquals(4, acroForm.getAsArray(PdfName.FIELDS).getAsDict(2).getAsNumber(PdfName.STRUCTPARENT).intValue());
        Assert.assertEquals(5, acroForm.getAsArray(PdfName.FIELDS).getAsDict(1).getAsArray(PdfName.KIDS).getAsDict(1).getAsNumber(PdfName.STRUCTPARENT).intValue());

        reader.close();
    }

    @Test
    @Ignore
    public void copyTaggedPdf16() throws IOException, DocumentException {
        initializeDocument("16");
        copy.setMergeFields();

        PdfReader reader1 = new PdfReader(SOURCE63);
        PdfReader reader2 = new PdfReader(SOURCE64);
        copy.addDocument(reader1);
        copy.addDocument(reader2);
        document.close();
        reader1.close();
        reader2.close();

        PdfReader reader = new PdfReader(output);
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary acroForm = catalog.getAsDict(PdfName.ACROFORM);
        PdfDictionary fonts = acroForm.getAsDict(PdfName.DR).getAsDict(PdfName.FONT);

        Assert.assertEquals(new PdfName("Helvetica"), fonts.getAsDict(new PdfName("Helv")).getAsName(PdfName.BASEFONT));
        Assert.assertEquals(new PdfName("Courier"), fonts.getAsDict(new PdfName("Cour")).getAsName(PdfName.BASEFONT));
        Assert.assertEquals(new PdfName("Times-Bold"), fonts.getAsDict(new PdfName("TiBo")).getAsName(PdfName.BASEFONT));
        Assert.assertEquals(new PdfName("ZapfDingbats"), fonts.getAsDict(new PdfName("ZaDb")).getAsName(PdfName.BASEFONT));

        reader.close();
    }

    @Test
    public void copyTaggedPdf17() throws IOException, DocumentException {
        initializeDocument("17");

        PdfReader reader1 = new PdfReader(SOURCE10);
        PdfReader reader2 = new PdfReader(SOURCE19);
        copy.addPage(copy.getImportedPage(reader1, 1, true));
        copy.addPage(copy.getImportedPage(reader2, 1, false));
        document.close();
        reader1.close();
        reader2.close();

        PdfReader reader = new PdfReader(output);
        Assert.assertEquals(2, reader.getNumberOfPages());
        Assert.assertNotNull(reader.getPageN(1));
        Assert.assertNotNull(reader.getPageN(2));
        reader.close();
    }

    @Test
    public void copyTaggedPdf18() throws IOException, DocumentException {
        initializeDocument("18");

        copy.setMergeFields();

        PdfReader reader1 = new PdfReader(SOURCE10);
        PdfReader reader2 = new PdfReader(SOURCE19);
        copy.addDocument(reader1);
        copy.addDocument(reader2);
        document.close();

        PdfReader reader = new PdfReader(output);
        Assert.assertEquals(reader1.getNumberOfPages() + reader2.getNumberOfPages(), reader.getNumberOfPages());
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            Assert.assertNotNull(reader.getPageN(i));
        }
        reader.close();

        reader1.close();
        reader2.close();
    }

    @Test
    public void copyTaggedPdf19() throws IOException, DocumentException {
        initializeDocument("19");

        PdfReader reader = new PdfReader(SOURCE18);
        copy.addPage(copy.getImportedPage(reader, 1, true));

        document.close();
        reader.close();

        reader = new PdfReader(output);

        PdfDictionary page1 = reader.getPageN(1);
        PdfDictionary t1_0 = page1.getAsDict(PdfName.RESOURCES).getAsDict(PdfName.XOBJECT).getAsStream(new PdfName("Fm0")).getAsDict(PdfName.RESOURCES).getAsDict(PdfName.FONT).getAsDict(new PdfName("T1_0"));
        Assert.assertNotNull(t1_0);

        reader.close();
    }

    @Test
    public void copyTaggedPdf20() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument("20");
        copy.setMergeFields();

        PdfReader reader2 = new PdfReader(SOURCE72);
        copy.addDocument(reader2, java.util.Arrays.asList(1,3,5));
        document.close();
        reader2.close();

        PdfReader reader = new PdfReader(output);
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary acroForm = catalog.getAsDict(PdfName.ACROFORM);
        PdfArray acroFields = acroForm.getAsArray(PdfName.FIELDS);
        junit.framework.Assert.assertTrue(acroFields.size() == 4);

        reader.close();

        compareResults("20");
    }

    @Test
    public void copyTaggedPdf21() throws IOException, DocumentException {
        initializeDocument("21");
        copy.setMergeFields();

        PdfReader reader1 = new PdfReader(SOURCE73);
        copy.addDocument(reader1);
        document.close();
        reader1.close();

        PdfReader reader = new PdfReader(output);
        PdfDictionary page = reader.getPageN(1);
        PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
        PdfDictionary xObject = resources.getAsDict(PdfName.XOBJECT);
        PdfStream img = xObject.getAsStream(new PdfName("Im0"));
        PdfArray decodeParms = img.getAsArray(PdfName.DECODEPARMS);
        junit.framework.Assert.assertEquals(2, decodeParms.size());
        PdfObject iref = decodeParms.getPdfObject(0);
        junit.framework.Assert.assertTrue(iref instanceof PdfIndirectReference);
        junit.framework.Assert.assertTrue(reader.getPdfObjectRelease(((PdfIndirectReference)iref).getNumber()) instanceof PdfNull);

        reader.close();
    }

    //Check for crash in case of structure element contains no "Pg" keys.
    @Test
    public void copyTaggedPdf22() throws IOException, DocumentException {
        initializeDocument("22");

        PdfReader reader = new PdfReader(DEV_805);

        int n = reader.getNumberOfPages();
        for (int page = 0; page < n; ) {
            copy.addPage(copy.getImportedPage(reader, ++page,true));
        }

        copy.freeReader(reader);
        document.close();
        reader.close();
    }

    @After
    public void finalize() {
        Document.compress = true;
    }

    private PdfArray verifyArraySize(PdfObject obj, Integer size, String message) {
        return verifyArraySize(obj, size, message, false);
    }

    private PdfArray verifyArraySize(PdfObject obj, Integer size, String message, boolean ignoreIfNotArray) {
        if (!(obj instanceof PdfArray)) {
            if (ignoreIfNotArray)
                return null;
            else
                Assert.fail(message + " is not array");
        }
        if (((PdfArray)obj).size() != size)
            Assert.fail(message+" has wrong size");
        return (PdfArray)obj;
    }

    private PdfDictionary verifyIsDictionary(PdfObject obj, String message) {
        if (obj == null || !obj.isDictionary())
            Assert.fail(message);
        return (PdfDictionary)obj;
    }

    private void compareResults(String name) throws IOException, ParserConfigurationException, SAXException {
        PdfReader reader = new PdfReader("./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out"+ name +".pdf");
        FileOutputStream xmlOut = new FileOutputStream("./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/test"+ name +".xml");
        new MyTaggedPdfReaderTool().convertToXml(reader, xmlOut);
        xmlOut.close();
        Assert.assertTrue(compareXmls("./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/xml/test"+ name +".xml", "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/test"+ name +".xml"));
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

    private int getCommonNumsCount(String filename) throws IOException {
        PdfReader reader = new PdfReader(filename);
        PdfDictionary structTreeRoot = reader.getCatalog().getAsDict(PdfName.STRUCTTREEROOT);
        PdfArray kids = ((PdfDictionary)PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE))).getAsArray(PdfName.KIDS);
        int cnt = 0;
        for (int i = 0; i < kids.size(); i++) {
            PdfArray nums = kids.getAsDict(i).getAsArray(PdfName.NUMS);
            cnt += nums.size();
        }
        reader.close();
        return cnt;
    }


    static class MyTaggedPdfReaderTool extends TaggedPdfReaderTool {

        @Override
        public void convertToXml(PdfReader reader, OutputStream os, String charset)
                throws IOException {
            this.reader = reader;
            OutputStreamWriter outs = new OutputStreamWriter(os, charset);
            out = new PrintWriter(outs);
            out.write("<root>");
            // get the StructTreeRoot from the root object
            PdfDictionary catalog = reader.getCatalog();
            PdfDictionary struct = catalog.getAsDict(PdfName.STRUCTTREEROOT);
            if (struct == null)
                throw new IOException(MessageLocalization.getComposedMessage("no.structtreeroot.found"));
            // Inspect the child or children of the StructTreeRoot
            inspectChild(struct.getDirectObject(PdfName.K));
            out.write("</root>");
            out.flush();
            out.close();
        }
    }
}
