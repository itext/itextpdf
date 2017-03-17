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
package com.itextpdf.testutils;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import junit.framework.Assert;

public class CompareToolTest {

    private static final String OUT_PATH = "./target/com/itextpdf/testutils/CompareToolTest/";
    private static final String RESOURCE_PATH = "./src/test/resources/com/itextpdf/testutils/CompareToolTest/";

    @Before
    public void setUp() {
        File dest = new File(OUT_PATH);
        dest.mkdirs();
        File[] files = dest.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    @Test
    public void compareToolErrorReportTest01() throws DocumentException, InterruptedException, IOException, ParserConfigurationException, SAXException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        compareTool.setXmlReportName("report01");
        String outPdf = RESOURCE_PATH + "simple_pdf.pdf";
        String cmpPdf = RESOURCE_PATH + "cmp_simple_pdf.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull("CompareTool must return differences found between the files", result);
        // Comparing the report to the reference one.
        Assert.assertTrue("CompareTool report differs from the reference one", compareTool.compareXmls(RESOURCE_PATH + "cmp_report01.xml", OUT_PATH + "report01.xml"));
    }

    @Test
    public void compareToolErrorReportTest02() throws IOException, InterruptedException, DocumentException, ParserConfigurationException, SAXException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        compareTool.setXmlReportName("report02");
        String outPdf = RESOURCE_PATH + "tagged_pdf.pdf";
        String cmpPdf = RESOURCE_PATH + "cmp_tagged_pdf.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull("CompareTool must return differences found between the files", result);
        // Comparing the report to the reference one.
        Assert.assertTrue("CompareTool report differs from the reference one", compareTool.compareXmls(RESOURCE_PATH + "cmp_report02.xml", OUT_PATH + "report02.xml"));
    }

    @Test
    public void compareToolErrorReportTest03() throws DocumentException, InterruptedException, IOException, ParserConfigurationException, SAXException {
        CompareTool compareTool = new CompareTool();
        compareTool.setCompareByContentErrorsLimit(10);
        compareTool.setGenerateCompareByContentXmlReport(true);
        compareTool.setXmlReportName("report03");
        String outPdf = RESOURCE_PATH + "screenAnnotation.pdf";
        String cmpPdf = RESOURCE_PATH + "cmp_screenAnnotation.pdf";
        String result = compareTool.compareByContent(outPdf, cmpPdf, OUT_PATH, "difference");
        System.out.println(result);
        Assert.assertNotNull("CompareTool must return differences found between the files", result);
        // Comparing the report to the reference one.
        Assert.assertTrue("CompareTool report differs from the reference one", compareTool.compareXmls(RESOURCE_PATH + "cmp_report03.xml", OUT_PATH + "report03.xml"));
    }

}
