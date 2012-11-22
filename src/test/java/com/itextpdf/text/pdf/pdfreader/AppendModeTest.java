/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.text.pdf.pdfreader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

/**
 * @author Bruno
 */
public class AppendModeTest {
	public static final String EXISTING = "./target/com/itextpdf/test/pdf/pdfreader/existing.pdf";
	public static final String REMOVEFIELD = "./target/com/itextpdf/test/pdf/pdfreader/removefield.pdf";
	public static final String ADDANNOTATION = "./target/com/itextpdf/test/pdf/pdfreader/addannotation.pdf";
	public static final String REMOVEFIELD2 = "./target/com/itextpdf/test/pdf/pdfreader/removefield2.pdf";
	public static final String ADDANNOTATION2 = "./target/com/itextpdf/test/pdf/pdfreader/addannotation2.pdf";
	private byte[] existing;
	
    @Before
    public void setUp() throws Exception {
        new File("./target/com/itextpdf/test/pdf/pdfreader/").mkdirs();
    	existing = createPdf();
    	FileOutputStream fos = new FileOutputStream(EXISTING);
    	fos.write(existing);
    	fos.close();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRemoveField() throws Exception {
    	File f = new File(REMOVEFIELD);
    	FileOutputStream fos = new FileOutputStream(f);
		PdfReader reader = new PdfReader(existing);
		PdfStamper stamper = new PdfStamper(reader, fos, '\0', true);
		AcroFields form = stamper.getAcroFields();
		form.removeField("test");
		stamper.close();
		Assert.assertEquals(1928, f.length());
    }


    @Test
    public void testAddAnnotation() throws Exception {
    	File f = new File(ADDANNOTATION);
    	FileOutputStream fos = new FileOutputStream(f);
		PdfReader reader = new PdfReader(existing);
		PdfStamper stamper = new PdfStamper(reader, fos, '\0', true);
		PdfAnnotation annot = PdfAnnotation.createText(stamper.getWriter(),
				new Rectangle(36, 36, 559, 806), "Test", "Add annotation test", true, "Pin");
		stamper.addAnnotation(annot, 1);
		stamper.close();
		Assert.assertEquals(2019, f.length());
    }

    @Test
    public void testRemoveField2() throws Exception {
    	File f = new File(REMOVEFIELD2);
    	FileOutputStream fos = new FileOutputStream(f);
		PdfReader reader = new PdfReader(EXISTING);
		PdfStamper stamper = new PdfStamper(reader, fos, '\0', true);
		AcroFields form = stamper.getAcroFields();
		form.removeField("test");
		stamper.close();
		Assert.assertEquals(1928, f.length());
    }


    @Test
    public void testAddAnnotation2() throws Exception {
    	File f = new File(ADDANNOTATION2);
    	FileOutputStream fos = new FileOutputStream(f);
		PdfReader reader = new PdfReader(EXISTING);
		PdfStamper stamper = new PdfStamper(reader, fos, '\0', true);
		PdfAnnotation annot = PdfAnnotation.createText(stamper.getWriter(),
				new Rectangle(36, 36, 559, 806), "Test", "Add annotation test", true, "Pin");
		stamper.addAnnotation(annot, 1);
		stamper.close();
		Assert.assertEquals(2019, f.length());
    }
	
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws    DocumentException 
     * @throws    IOException 
     */
    public static byte[] createPdf()
	throws DocumentException, IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setFullCompression();
        // step 3
        document.open();
        // step 4
		TextField tf = new TextField(writer, new Rectangle(36, 806, 120, 780), "test");
		writer.addAnnotation(tf.getTextField());
        // step 5
        document.close();
        return baos.toByteArray();
    }
}
