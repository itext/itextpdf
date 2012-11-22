package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TaggedPdfCopyTest {

    Document document;
    PdfCopy copy;
    public static final String SOURCE11 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/1/source11.pdf";
    public static final String SOURCE12 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/1/source12.pdf";
    public static final String SOURCE22 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/2/source22.pdf";
    public static final String SOURCE32 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/3/source32.pdf";
    public static final String SOURCE41 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/4/source41.pdf";
    public static final String SOURCE42 = "./src/test/resources/com/itextpdf/text/pdf/TaggedPdfCopyTest/4/source42.pdf";
    public static final String OUT = "./target/com/itextpdf/test/pdf/TaggedPdfCopyTest/out.pdf";
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
        document = new Document();
        copy = new PdfCopy(document, new FileOutputStream(OUT));
        copy.setTagged();
        document.open();
    }

    @Test
    public void classMapConflict() throws IOException {
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
    public void roleMapConflict() throws IOException{
        PdfReader reader1 = new PdfReader(SOURCE11);
        PdfDictionary trailer = reader1.trailer;
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
    public void pdfMergeTest() throws IOException, BadPdfFormatException {
        PdfReader reader1 = new PdfReader(SOURCE11);
        copy.addPage(copy.getImportedPage(reader1, 76, true));
        copy.addPage(copy.getImportedPage(reader1, 83, true));
        reader1.close();
        PdfReader reader2 = new PdfReader(SOURCE32);
        copy.addPage(copy.getImportedPage(reader2, 69, true));
        copy.addPage(copy.getImportedPage(reader2, 267, true));
        document.close();
        reader2.close();
        PdfReader reader = new PdfReader(OUT);
        PdfObject obj = reader.getCatalog().get(PdfName.STRUCTTREEROOT);
        obj = PdfStructTreeController.getDirectObject(obj);
        if (!obj.isDictionary())
            Assert.fail(MessageLocalization.getComposedMessage("no.structtreeroot.found"));
        PdfDictionary structTreeRoot = (PdfDictionary)obj;
        verifyParentTree(structTreeRoot);
        verifyClassMap(structTreeRoot);
        verifyRoleMap(structTreeRoot);
        reader.close();
    }

    @Test
    public void copyOnePage()throws IOException, BadPdfFormatException {
        PdfReader reader = new PdfReader(SOURCE42);
        copy.addPage(copy.getImportedPage(reader, 2, true));
        document.close();
        reader.close();

        reader = new PdfReader(OUT);
        PdfObject obj = reader.getCatalog().getDirectObject(PdfName.STRUCTTREEROOT);
        PdfDictionary structTreeRoot = (PdfDictionary)obj;
        obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        if (!obj.isDictionary())
            Assert.fail(MessageLocalization.getComposedMessage("the.document.does.not.contain.parenttree"));
        PdfDictionary parentTree = (PdfDictionary)obj;
        PdfArray array = parentTree.getAsArray(PdfName.NUMS);
        if (array.size() != 4)
            Assert.fail("ParentTree has wrong size");

        reader.close();
    }

    private void verifyParentTree(PdfDictionary structTreeRoot) throws BadPdfFormatException {
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.PARENTTREE));
        if (!obj.isDictionary())
            Assert.fail(MessageLocalization.getComposedMessage("the.document.does.not.contain.parenttree"));
        PdfDictionary parentTree = (PdfDictionary)obj;
        PdfArray array = parentTree.getAsArray(PdfName.NUMS);
        if (array.size() != 28)
            Assert.fail("ParentTree has wrong size");
        array = (PdfArray)(array.getDirectObject(9));
        PdfDictionary tmp = (PdfDictionary)array.getDirectObject(0);
        tmp = tmp.getAsArray(PdfName.K).getAsDict(1);
        tmp.remove(PdfName.P);
        tmp.remove(PdfName.PG);
        if (!PdfStructTreeController.compareObjects(sElem, tmp))
            Assert.fail("StructElement is not correct");
    }

    private void verifyClassMap(PdfDictionary structTreeRoot) throws BadPdfFormatException {
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.CLASSMAP));
        if (!obj.isDictionary())
            Assert.fail("The document does not contain ClassMap");
        PdfDictionary ClassMap = (PdfDictionary)obj;
        obj = PdfStructTreeController.getDirectObject(ClassMap.get(new PdfName("CM31")));
        if (obj == null || !obj.isDictionary())
            Assert.fail("ClassMap does not contain \"CM31\"");
        PdfDictionary CM31 = (PdfDictionary)obj;
        if (!PdfStructTreeController.compareObjects(this.CM31, CM31))
            Assert.fail("ClassMap contains incorrect \"CM31\"");
    }

    private void verifyRoleMap(PdfDictionary structTreeRoot) throws BadPdfFormatException {
        PdfObject obj = PdfStructTreeController.getDirectObject(structTreeRoot.get(PdfName.ROLEMAP));
        if (!obj.isDictionary())
            throw new BadPdfFormatException("The document does not contain RoleMap");
        PdfDictionary RoleMap = (PdfDictionary)obj;
        if (!PdfName.SPAN.equals(RoleMap.get(new PdfName("ParagraphSpan"))))
            throw new BadPdfFormatException("RoleMap does not contain \"ParagraphSpan\"");
    }

    @After
    public void finalize() {
    	Document.compress = true;
    }
}
