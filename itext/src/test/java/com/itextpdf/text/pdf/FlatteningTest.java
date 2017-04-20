/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.events.FieldPositioningEvents;
import junit.framework.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

/**
 * @author Michael Demey
 */
public class FlatteningTest {

    private static final String RESOURCES_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/FlatteningTest/";
    private static final String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/FlatteningTest/";

    @Test
    public void testFlatteningNewAppearances() throws InterruptedException, DocumentException, IOException {
        new File(OUTPUT_FOLDER).mkdirs();

        final String OUT = "tpl3_flattened.pdf";

        PdfReader reader = new PdfReader(RESOURCES_FOLDER + "tpl3.pdf");
        AcroFields fields = reader.getAcroFields();
        if (fields != null && fields.getFields() != null && fields.getFields().size() > 0) {
            OutputStream out = null;
            out = new FileOutputStream(OUTPUT_FOLDER + OUT);
            PdfStamper stamp = new PdfStamper(reader, out);
            stamp.setFormFlattening(true);
            AcroFields form = stamp.getAcroFields();

            Set<Map.Entry<String, AcroFields.Item>> map = form.getFields().entrySet();
            for (Map.Entry<String, AcroFields.Item> e : map) {
                form.setField(e.getKey(), e.getKey());
            }

            stamp.close();
            out.close();
        }
        reader.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compare(OUTPUT_FOLDER + OUT, RESOURCES_FOLDER + "cmp_" + OUT, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testFlattening() throws IOException, DocumentException, InterruptedException {
        final String INPUT_FOLDER = RESOURCES_FOLDER + "input/";
        final String CMP_FOLDER = RESOURCES_FOLDER + "cmp/";
        File inputFolder = new File(INPUT_FOLDER);

        if (!inputFolder.exists())
            Assert.fail("Input folder can't be found (" + INPUT_FOLDER + ")");

        new File(OUTPUT_FOLDER).mkdirs();

        String[] files = inputFolder.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".pdf");
            }
        });

        for (String file : files) {
            // flatten fields
            PdfReader reader = new PdfReader(INPUT_FOLDER + file);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT_FOLDER + file));
            stamper.setFormFlattening(true);
            stamper.close();

            // compare
            CompareTool compareTool = new CompareTool();
            String errorMessage = compareTool.compare(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
            if (errorMessage != null) {
                Assert.fail(errorMessage);
            }
        }
    }

    @Test
    public void testFlatteningGenerateAppearances1() throws IOException, DocumentException, InterruptedException {

        new File(OUTPUT_FOLDER).mkdirs();

        final String OUT = "noappearances-needapp-false_override-false.pdf";
        testFlatteningGenerateAppearance(RESOURCES_FOLDER + "noappearances-needapp-false.pdf", OUTPUT_FOLDER + OUT, false);

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compare(OUTPUT_FOLDER + OUT, RESOURCES_FOLDER + "cmp_" + OUT, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testFlatteningGenerateAppearances2() throws IOException, DocumentException, InterruptedException {

        new File(OUTPUT_FOLDER).mkdirs();

        final String OUT = "noappearances-needapp-false_override-true.pdf";
        testFlatteningGenerateAppearance(RESOURCES_FOLDER + "noappearances-needapp-false.pdf", OUTPUT_FOLDER + OUT, true);

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compare(OUTPUT_FOLDER + OUT, RESOURCES_FOLDER + "cmp_" + OUT, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testFlatteningGenerateAppearances3() throws IOException, DocumentException, InterruptedException {

        new File(OUTPUT_FOLDER).mkdirs();

        final String OUT = "noappearances-needapp-false_override-none.pdf";
        testFlatteningGenerateAppearance(RESOURCES_FOLDER + "noappearances-needapp-false.pdf", OUTPUT_FOLDER + OUT, null);

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compare(OUTPUT_FOLDER + OUT, RESOURCES_FOLDER + "cmp_" + OUT, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testFlatteningGenerateAppearances4() throws IOException, DocumentException, InterruptedException {

        new File(OUTPUT_FOLDER).mkdirs();

        final String OUT = "noappearances-needapp-true_override-false.pdf";
        testFlatteningGenerateAppearance(RESOURCES_FOLDER + "noappearances-needapp-true.pdf", OUTPUT_FOLDER + OUT, false);

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compare(OUTPUT_FOLDER + OUT, RESOURCES_FOLDER + "cmp_" + OUT, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testFlatteningGenerateAppearances5() throws IOException, DocumentException, InterruptedException {

        new File(OUTPUT_FOLDER).mkdirs();

        final String OUT = "noappearances-needapp-true_override-true.pdf";
        testFlatteningGenerateAppearance(RESOURCES_FOLDER + "noappearances-needapp-true.pdf", OUTPUT_FOLDER + OUT, true);

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compare(OUTPUT_FOLDER + OUT, RESOURCES_FOLDER + "cmp_" + OUT, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testFlatteningGenerateAppearances6() throws IOException, DocumentException, InterruptedException {

        new File(OUTPUT_FOLDER).mkdirs();

        final String OUT = "noappearances-needapp-true_override-none.pdf";
        testFlatteningGenerateAppearance(RESOURCES_FOLDER + "noappearances-needapp-true.pdf", OUTPUT_FOLDER + OUT, null);

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compare(OUTPUT_FOLDER + OUT, RESOURCES_FOLDER + "cmp_" + OUT, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    public void testFlatteningGenerateAppearance(String in, String out, Boolean gen) throws FileNotFoundException, DocumentException, IOException {
        PdfReader reader = new PdfReader(in);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(out));
        if (gen != null)
            stamper.getAcroFields().setGenerateAppearances(gen);
        stamper.setFormFlattening(true);
        stamper.close();
    }

    @Test
    public void testRegeneratingFieldsFalse() throws IOException, DocumentException, InterruptedException {

        new File(OUTPUT_FOLDER).mkdirs();

        String file = "regenerateField_false.pdf";

        Document doc = new Document(PageSize.A4);
        ByteArrayOutputStream fs = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(doc, fs);

        doc.open();
        PdfPTable myTable = new PdfPTable(1);
        myTable.setTotalWidth(300f);
        myTable.setLockedWidth(true);
        myTable.setHorizontalAlignment(0);

        //Create the textfield that will sit on a cell in the table
        TextField tf = new TextField(writer, new Rectangle(0, 0, 70, 200), "cellTextBox");
        tf.setText("text field");
        //Create the table cell
        PdfPCell tbCell = new PdfPCell(new Phrase(" "));
        FieldPositioningEvents events = new FieldPositioningEvents(writer, tf.getTextField());
        tbCell.setCellEvent(events);
        myTable.addCell(tbCell);
        PdfContentByte cb = writer.getDirectContent();
        //Write out the table to the middle of the document
        myTable.writeSelectedRows(0, -1, 0, -1, 20, 700, cb);
        doc.close();

        PdfReader reader2 = new PdfReader(new ByteArrayInputStream(fs.toByteArray()));
        fs.reset();
        PdfStamper stamper2 = new PdfStamper(reader2, fs);
        stamper2.getAcroFields().setGenerateAppearances(false);
        stamper2.close();
        reader2.close();

        PdfReader reader = new PdfReader(new ByteArrayInputStream(fs.toByteArray()));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT_FOLDER + file));
        stamper.setFormFlattening(true);
        stamper.close();
        reader.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, RESOURCES_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testRegeneratingFieldsTrue() throws IOException, DocumentException, InterruptedException {

        new File(OUTPUT_FOLDER).mkdirs();

        String file = "regenerateField_true.pdf";

        Document doc = new Document(PageSize.A4);
        ByteArrayOutputStream fs = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(doc, fs);

        doc.open();
        PdfPTable myTable = new PdfPTable(1);
        myTable.setTotalWidth(300f);
        myTable.setLockedWidth(true);
        myTable.setHorizontalAlignment(0);

        //Create the textfield that will sit on a cell in the table
        TextField tf = new TextField(writer, new Rectangle(0, 0, 70, 200), "cellTextBox");
        tf.setText("text field");
        //Create the table cell
        PdfPCell tbCell = new PdfPCell(new Phrase(" "));
        FieldPositioningEvents events = new FieldPositioningEvents(writer, tf.getTextField());
        tbCell.setCellEvent(events);
        myTable.addCell(tbCell);
        PdfContentByte cb = writer.getDirectContent();
        //Write out the table to the middle of the document
        myTable.writeSelectedRows(0, -1, 0, -1, 20, 700, cb);
        doc.close();

        PdfReader reader2 = new PdfReader(new ByteArrayInputStream(fs.toByteArray()));
        fs.reset();
        PdfStamper stamper2 = new PdfStamper(reader2, fs);
        stamper2.getAcroFields().setGenerateAppearances(true);
        stamper2.close();
        reader2.close();

        PdfReader reader = new PdfReader(new ByteArrayInputStream(fs.toByteArray()));
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT_FOLDER + file));
        stamper.setFormFlattening(true);
        stamper.close();
        reader.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, RESOURCES_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testAnnotationFlatteningWithSkewAndRotation() throws IOException, DocumentException, InterruptedException {
        new File(OUTPUT_FOLDER).mkdirs();
        
        String file = "annotationWithTransformMatrix.pdf";
        PdfReader reader = new PdfReader(RESOURCES_FOLDER + file);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT_FOLDER + file));
        stamper.getWriter().setCompressionLevel(0);
        stamper.setAnnotationFlattening(true);
        stamper.close();
        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, RESOURCES_FOLDER + "cmp_" + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testRotatedFilledField() throws IOException, DocumentException, InterruptedException {
        new File(OUTPUT_FOLDER).mkdirs();

        String file = "rotatedField.pdf";
        PdfReader pdfReader = new PdfReader(RESOURCES_FOLDER + file);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(OUTPUT_FOLDER + file));

        AcroFields fields = pdfStamper.getAcroFields();
        fields.setField("Text1", "TEST");
        fields.setGenerateAppearances(true);

        pdfStamper.setFormFlattening(true);
        pdfStamper.close();
        pdfReader.close();
        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, RESOURCES_FOLDER + "cmp_" + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}
