/*
 * $Id: GenericTest.java 6208 2014-02-05 14:43:21Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, et al.
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
package sandbox;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.management.OperationsException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

public /*abstract*/ class GenericTest {
	
	/** The logger class */
    private final static Logger LOGGER = LoggerFactory.getLogger(GenericTest.class.getName());

    /** The class file for the example we're going to test. */
	protected Class<?> klass;
    protected String className;
	/** An error message */
    private String errorMessage;
    /** A prefix that is part of the error message. */
    private String differenceImagePrefix = "difference";

    /**
     * Gets triggered before the test is performed.
     * When writing tests, you need to override this method to set
     * the klass variable (using the setKlass() method)
     */
    @Before
    public void setup() {

    }

    /**
     * Creates a Class object for the example you want to test.
     * @param	className	the class you want to test
     */
	protected void setKlass(String className) {
		this.className = className;
        try {
			klass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(className + " not found");
		}
	}

    /**
         * Tests the example.
         * If SRC and DEST are defined, the example manipulates a PDF;
         * if only DEST is defined, the example creates a PDF.
         */
    @Test(timeout = 120000)
    public void test() throws Exception {
        if (this.getClass().getName().equals(GenericTest.class.getName()))
            return;
        LOGGER.info("Starting test " + className + ".");
        // Getting the destination PDF file (must be there!)
        String dest= getDest();
        if (dest == null || dest.length() == 0)
            throw new OperationsException("DEST cannot be empty!");
        // Getting the source PDF file
        String src = getSrc();
        // if there is none, just create a PDF
        if (src == null || src.length() == 0) {
        	createPdf(dest);
        }
        // if there is one, manipulate the PDF
        else {
        	manipulatePdf(src, dest);
        }
        // Do some further tests on the PDF
        assertPdf(dest);
        // Compare the destination PDF with a reference PDF
        System.out.println(dest + "\n" + getCmpPdf());
        comparePdf(dest, getCmpPdf());
        LOGGER.info("Test complete.");
    }
    
    /**
     * Creates a PDF by invoking the createPdf() method in the
     * original sample class.
     * @param	dest	the resulting PDF
     */
	protected void createPdf(String dest) throws Exception {
        LOGGER.info("Creating PDF.");
    	Method method = klass.getDeclaredMethod("createPdf", String.class);
    	method.invoke(klass.getConstructor().newInstance(), dest);
	}
    
    /**
     * Manupulates a PDF by invoking the manipulatePdf() method in the
     * original sample class.
     * @param	src		the source PDF
     * @param	dest	the resulting PDF
     */
	protected void manipulatePdf(String src, String dest) throws Exception {
        LOGGER.info("Manipulating PDF.");
    	Method method = klass.getDeclaredMethod("manipulatePdf", String.class, String.class);
    	method.invoke(klass.getConstructor().newInstance(), src, dest);
	}
	
	/**
	 * Gets the path to the source PDF from the sample class.
	 * @return	a path to a source PDF
	 */
	protected String getSrc() {
		return getStringField("SRC");
	}
	
	/**
	 * Gets the path to the resulting PDF from the sample class;
	 * this method also creates directories if necessary.
	 * @return	a path to a resulting PDF
	 */
	protected String getDest() {
		String dest = getStringField("DEST");
		if (dest != null) {
			File file = new File(dest);
			file.getParentFile().mkdirs();
		}
		return dest;
	}
	
	/**
	 * Returns a string value that is stored as a static variable
	 * inside an example class.
	 * @param name	the name of the variable
	 * @return	the value of the variable
	 */
	protected String getStringField(String name) {
		try {
			Field field = klass.getField(name);
			if (field == null)
				return null;
			Object obj = field.get(null);
			if (obj == null || ! (obj instanceof String))
				return null;
			return (String)obj;
		}
		catch(Exception e) {
			return null;
		}
	}

	/**
	 * Compares two PDF files using iText's CompareTool.
	 * @param	dest	the PDF that resulted from the test
	 * @param	cmp		the reference PDF
	 */
    protected void comparePdf(String dest, String cmp) throws Exception {
    	if (cmp == null || cmp.length() == 0) return;
        CompareTool compareTool = new CompareTool(dest, cmp);
        String outPath = "./target/" + new File(dest).getParent();
        new File(outPath).mkdirs();
        addError(compareTool.compare(dest, cmp, outPath, differenceImagePrefix));
        addError(compareTool.compareDocumentInfo(dest, cmp));
        addError(compareTool.compareLinks(dest, cmp));

        if (errorMessage != null) Assert.fail(errorMessage);
    }
	
    /**
     * Perform other tests on the resulting PDF.
     * @param	dest	the resulting PDF
     */
    protected void assertPdf(String dest) throws Exception {};

    /**
     * Every test needs to know where to find its reference file.
     */
    protected String getCmpPdf() {
    	String tmp = getDest();
    	if (tmp == null)
    		return null;
    	int i = tmp.lastIndexOf("/");
    	return "cmpfiles/" + tmp.substring(8, i + 1) + "cmp_" + tmp.substring(i + 1);
    }

    /**
     * Helper method to construct error messages.
     * @param	error	part of an error message.
     */
    private void addError(String error) {
        if (error != null && error.length() > 0) {
            if (errorMessage == null)
                errorMessage = "";
            else
                errorMessage += "\n";

            errorMessage += error;
        }
    }
}
