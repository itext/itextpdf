package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.parser.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

    public static final String SOURCE4 =  "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/1/source4.pdf";
    public static final String SOURCE10 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/1/source10.pdf";
    public static final String SOURCE11 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/1/source11.pdf";
    public static final String SOURCE12 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/1/source12.pdf";
    public static final String SOURCE16 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/1/source16.pdf";
    public static final String SOURCE17 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/1/source17.pdf";
    public static final String SOURCE22 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/2/source22.pdf";
    public static final String SOURCE32 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/3/source32.pdf";
    public static final String SOURCE42 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/4/source42.pdf";

    public static final String CLASSMAP = "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/classmap.pdf";
    public static final String ROLEMAP =  "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/rolemap.pdf";
    public static final String MERGE =    "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/merge.pdf";
    public static final String OUT0 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out0.pdf";
    public static final String OUT1 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out1.pdf";
    public static final String OUT2 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out2.pdf";
    public static final String OUT3 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out3.pdf";
    public static final String OUT4 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out4.pdf";
    public static final String OUT5 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out5.pdf";
    public static final String OUT6 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out6.pdf";
    public static final String OUT7 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out7.pdf";
    public static final String OUT8 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out8.pdf";
    public static final String OUT9 =     "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out9.pdf";
    public static final String OUT10 =    "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out10.pdf";

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

    private void initializeDocument(String output) throws DocumentException, FileNotFoundException {
        this.output = output;
        document = new Document();
        copy = new PdfCopy(document, new FileOutputStream(output));
        copy.setTagged();
        document.open();
    }

    @Test
    public void classMapConflict() throws IOException, DocumentException {
        initializeDocument(CLASSMAP);
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
        initializeDocument(ROLEMAP);

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
        initializeDocument(MERGE);

        int n = 4;
        PdfReader reader1 = new PdfReader(SOURCE11);
        copy.addPage(copy.getImportedPage(reader1, 76, true));
        copy.addPage(copy.getImportedPage(reader1, 83, true));
        reader1.close();
        PdfReader reader2 = new PdfReader(SOURCE32);
        copy.addPage(copy.getImportedPage(reader2, 69, true));
        copy.addPage(copy.getImportedPage(reader2, 267, true));
        document.close();
        reader2.close();
        PdfReader reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 2, "Kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);

        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        int[] nums = new int[] {30, 32, 39, 80};
        for (int i = 0; i < n; ++i)
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

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
        initializeDocument(OUT0);
        PdfReader reader = new PdfReader(SOURCE11);
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; ++i)
            copy.addPage(copy.getImportedPage(reader, i, true));
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 5, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        obj = ((PdfDictionary)obj).get(PdfName.KIDS);
        verifyArraySize(obj, 3, "Invalid nums.");
        PdfObject numsDict = PdfStructTreeController.getDirectObject(((PdfArray) obj).getPdfObject(0));
        verifyIsDictionary(numsDict, "nums1");
        PdfArray arrays[] = new PdfArray[3];
        arrays[0] = verifyArraySize(((PdfDictionary)numsDict).get(PdfName.NUMS), 128, "nums 1");
        numsDict = PdfStructTreeController.getDirectObject(((PdfArray) obj).getPdfObject(1));
        verifyIsDictionary(numsDict, "nums2");
        arrays[1] = verifyArraySize(((PdfDictionary)numsDict).get(PdfName.NUMS), 128, "nums 2");
        numsDict = PdfStructTreeController.getDirectObject(((PdfArray) obj).getPdfObject(2));
        verifyIsDictionary(numsDict, "nums3");
        arrays[2] = verifyArraySize(((PdfDictionary)numsDict).get(PdfName.NUMS), 4, "nums 3");
        int[] nums = new int[] {3, 91, 42, 19, 15, 15, 9, 13, 15, 17, 18, 5, 17, 37, 24, 19, 15, 23, 8, 11,
                17, 11, 13, 29, 18, 12, 11, 9, 14, 26, 17, 22, 30, 15, 21, 28, 22, 24, 22, 20, 21, 17, 24,
                25, 20, 14, 32, 25, 14, 15, 24, 20, 22, 24, 21, 22, 18, 12, 23, 29, 19, 22, 21, 27, 25, 19,
                6, 14, 18, 21, 25, 11, 26, 15, 15, 30, 23, 32, 17, 22, 18, 18, 32, 18, 16, 21, 28, 28, 10,
                18, 13, 23, 17, 19, 24, 29, 25, 34, 26, 24, 28, 27, 21, 23, 23, 23, 10, 10, 10, 9, 16, 20,
                16, 16, 22, 27, 14, 3, 11, 30, 11, 29, 6, 99, 117, 128, 92, 67, 132, 108};

        //OutputStreamWriter writer = new FileWriter(OUT0+".txt");
        int k = 0;
        for (int i = 0; i < arrays.length; ++i)
            for (int j = 0; j < arrays[i].size() / 2; ++j)
                verifyArraySize(PdfStructTreeController.getDirectObject(arrays[i].getPdfObject(j*2+1)), nums[k++], "Nums of page "+(i+1));
        //writer.write(((PdfArray)(PdfStructTreeController.getDirectObject(arrays[i].getPdfObject(j*2+1)))).size()+", ");
        //writer.close();

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

        if (reader.eofPos != 3378440L) Assert.fail("Invalid size of pdf.");
        reader.close();
        compareResults("0");
    }

    @Test
    public void copyTaggedPdf1() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument(OUT1);
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
        verifyArraySize(array, 2, "Nums");
        verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(1)), 61, "Nums of page 1");
        reader.close();
        compareResults("1");
    }

    @Test
    public void copyTaggedPdf2() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument(OUT2);
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
        initializeDocument(OUT3);
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
        initializeDocument(OUT4);
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
        initializeDocument(OUT5);
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
        initializeDocument(OUT6);
        PdfReader reader = new PdfReader(SOURCE11);
        int n = 8;
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
        int[] nums = new int[] {3, 18, 9, 25, 15, 91, 13, 18};
        for (int i = 0; i < n; ++i)
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

        PdfDictionary ClassMap = verifyIsDictionary(PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.CLASSMAP)), NO_CLASS_MAP);
        if (ClassMap.size() != 27) Assert.fail("ClassMap incorrect");
        String[] CMs = new String[]{"CM118", "CM117", "CM133", "CM47", "CM46", "CM114", "CM43", "CM110", "CM21", "CM22", "CM26", "CM27", "CM145", "CM128", "CM29", "CM56", "CM1", "CM2", "CM72", "CM16", "CM34", "CM17", "CM14", "CM15", "CM119", "CM12", "CM13"};
        for (int i = 0; i < CMs.length; ++i)
            verifyIsDictionary(PdfStructTreeController.getDirectObject(ClassMap.get(new PdfName(CMs[i]))), "ClassMap.does.not.contain.\""+CMs[i]+"\"");

        PdfDictionary RoleMap = verifyIsDictionary(PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.ROLEMAP)), NO_ROLE_MAP);
        if (!PdfName.SPAN.equals(RoleMap.get(new PdfName("ParagraphSpan"))))
            throw new BadPdfFormatException("RoleMap does not contain \"ParagraphSpan\".");
        if (reader.eofPos != 249068) Assert.fail("Invalid size of pdf.");

        reader.close();
        compareResults("6");
    }

    @Test
    public void copyTaggedPdf7() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument(OUT7);
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
        initializeDocument(OUT8);
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
        n *= 4;
        document.close();
        reader.close();

        reader = new PdfReader(output);
        PdfDictionary structTreeRoot = verifyIsDictionary(reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT), NO_STRUCT_TREE_ROOT);
        verifyArraySize(structTreeRoot.get(PdfName.K), 6, "Invalid count of kids in StructTreeRoot");
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        verifyIsDictionary(obj, NO_PARENT_TREE);
        PdfArray array = ((PdfDictionary)obj).getAsArray(PdfName.NUMS);
        verifyArraySize(array, n*2, "Nums");
        int[] nums = new int[] {42, 42, 11, 11, 13, 13, 42, 11, 13, 42, 11, 13};
        for (int i = 0; i < n; ++i)
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

        reader.close();
        compareResults("8");
    }

    @Test
    public void copyTaggedPdf9() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        initializeDocument(OUT9);
        PdfReader reader4 = new PdfReader(SOURCE4);
        PdfReader reader10 = new PdfReader(SOURCE10);
        PdfReader reader32 = new PdfReader(SOURCE32);
        int n = 20;
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
        int[] nums = new int[] {7, 87, 128, 26, 135, 83, 7, 135, 83, 116, 26, 128, 74, 16, 11, 38, 51, 61, 7, 26};
        for (int i = 0; i < n; ++i)
//            nums[i] = ((PdfArray)PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1))).size();
            verifyArraySize(PdfStructTreeController.getDirectObject(array.getPdfObject(i*2+1)), nums[i], "Nums of page "+(i+1));

        reader.close();
        compareResults("9");
    }

    @Test
    public void copyTaggedPdf10() throws IOException, DocumentException, ParserConfigurationException, SAXException {
        //source17: StructTreeRoot has no kids - incorrect syntax of tags - try to fix in result pdf
        initializeDocument(OUT10);
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
        compareResults("2");
    }

    @After
    public void finalize() {
        Document.compress = true;
    }

    private PdfArray verifyArraySize(PdfObject obj, Integer size, String message) {
        if (obj == null || !obj.isArray()) Assert.fail(message + " is not array");
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
