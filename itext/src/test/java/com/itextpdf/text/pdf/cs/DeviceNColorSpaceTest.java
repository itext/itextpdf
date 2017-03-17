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
package com.itextpdf.text.pdf.cs;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeviceNColorSpaceTest {
    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/cs/DeviceNColorSpaceTest";
    @Before
    public void Init() throws IOException{
        File dir = new File(DEST_FOLDER);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        } else
            dir.mkdirs();
    }

    @Test
    public void DeviceNSpotBasedGradient() throws IOException, DocumentException, InterruptedException {
        // step 1
        Document document = new Document(PageSize.A3);
        // step 2
        String dest_file = DEST_FOLDER + "/device_n_gradient_base.pdf";
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest_file));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        PdfSpotColor psc_gray = new PdfSpotColor("iTextGray", new GrayColor(0f));
        PdfSpotColor psc_cmyk_yell = new PdfSpotColor("iTextYellow", new CMYKColor(0f, 0f, 1f, 0f));
        PdfSpotColor psc_cmyk_magen = new PdfSpotColor("iTextMagenta", new CMYKColor(0f, 1f, 0f, 0f));
        PdfSpotColor psc_rgb_blue = new PdfSpotColor("iTextBlue", new BaseColor(0, 0, 255));

        PdfDeviceNColor pdfDeviceNNChannelColor = new PdfDeviceNColor(new PdfSpotColor[]{psc_cmyk_yell, psc_cmyk_magen, psc_rgb_blue});
        PdfDeviceNColor pdfDeviceNNChannelColor2 = new PdfDeviceNColor(new PdfSpotColor[]{psc_cmyk_magen, psc_cmyk_yell, psc_rgb_blue});

        colorRectangle(canvas, new SpotColor(new PdfSpotColor("iTextGray", new GrayColor(0f)), 0.8f), 36, 824, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("iTextYellow", new CMYKColor(0f, 0f, 1f, 0f)), 0.8f), 90, 824, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("iTextMagenta", new CMYKColor(0f, 1f, 0f, 0f)), 0.4f), 144, 824, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("iTextBlue", new BaseColor(0, 0, 255)), 0.7f), 198, 824, 36, 36);

        colorRectangle(canvas, new DeviceNColor(new PdfDeviceNColor(new PdfSpotColor[]{psc_cmyk_yell, psc_cmyk_magen, psc_rgb_blue}), new float[]{0, 0.0f, 1}), 36, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.1f, 0.1f, 1}), 90, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.2f, 0.2f, 1}), 144, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.3f, 0.3f, 1}), 198, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.4f, 0.4f, 1}), 252, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(new PdfDeviceNColor(new PdfSpotColor[]{psc_cmyk_yell, psc_cmyk_magen, psc_rgb_blue, psc_gray}), new float[]{0.5f, 0.5f, 1, 0.5f}), 306, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor2, new float[]{0.6f, 0.1f, 1}), 360, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.7f, 0.7f, 1}), 416, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.8f, 0.8f, 1}), 470, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(new PdfDeviceNColor(new PdfSpotColor[]{psc_cmyk_yell, psc_cmyk_magen, psc_rgb_blue}), new float[]{0.9f, 0.9f, 1}), 524, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{1, 1, 1}), 578, 770, 36, 36);

        PdfDeviceNColor pdfDeviceNColor = new PdfDeviceNColor(new PdfSpotColor[]{psc_cmyk_yell, psc_cmyk_magen, psc_rgb_blue});
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0, 0, 1}));
        canvas.rectangle(36, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.1f, 0.1f, 1}));
        canvas.rectangle(90, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.2f, 0.2f, 1}));
        canvas.rectangle(144, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.3f, 0.3f, 1}));
        canvas.rectangle(198, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.4f, 0.4f, 1}));
        canvas.rectangle(252, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.5f, 0.5f, 1}));
        canvas.rectangle(306, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.6f, 0.1f, 1}));
        canvas.rectangle(360, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.7f, 0.7f, 1}));
        canvas.rectangle(416, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.8f, 0.8f, 1}));
        canvas.rectangle(470, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{0.9f, 0.9f, 1}));
        canvas.rectangle(524, 716, 36, 36);
        canvas.fillStroke();
        canvas.setColorFill(new DeviceNColor(pdfDeviceNColor, new float[]{1, 1, 1}));
        canvas.rectangle(578, 716, 36, 36);
        canvas.fillStroke();

        canvas.saveState();
        canvas.rectangle(418, 412, -329, 189);
        canvas.clip();
        canvas.newPath();
        canvas.saveState();
        canvas.concatCTM(329f, 0f, 0f, -329f, 89f, 506.5f);
        canvas.paintShading(PdfShading.simpleAxial(writer, 0, 0, 1, 0, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{1, 1, 0}), new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0, 0, 1})));
        canvas.restoreState();
        canvas.restoreState();
        canvas.setColorStroke(new DeviceNColor(pdfDeviceNNChannelColor, new float[]{1, 1, 1}));
        canvas.rectangle(418, 412, -329, 189);
        canvas.stroke();

        // step 5
        document.close();

        CompareTool compareTool = new CompareTool();
        String error = compareTool.compareByContent(dest_file, "./src/test/resources/com/itextpdf/text/pdf/cs/DeviceNColorSpaceTest/cmp_device_n_gradient_base.pdf", DEST_FOLDER, "diff_");
        if (error != null) {
            Assert.fail(error);
        }
    }

    @Test
    public void DeviceNCmykRedRgbBlueGradient() throws IOException, DocumentException, InterruptedException {
        Document document = new Document();
        // step 2
        String dest_file = DEST_FOLDER + "/device_n_gradient_CmykRedRgbBlue.pdf";
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest_file));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        PdfSpotColor psc_red = new PdfSpotColor("Spot Red", new CMYKColor(0f, 1f, 1f, 0f));
        PdfSpotColor psc_blue = new PdfSpotColor("Spot Blue", new BaseColor(0, 0, 255));
        PdfDeviceNColor deviceNColor = new PdfDeviceNColor(new PdfSpotColor[] {psc_red, psc_blue});

        canvas.saveState();
        canvas.rectangle(418, 412, -329, 189);
        canvas.clip();
        canvas.newPath();
        canvas.saveState();
        canvas.concatCTM(329f, 0f, 0f, -329f, 89f, 506.5f);
        canvas.paintShading(PdfShading.simpleAxial(writer, 0, 0, 1, 0, new DeviceNColor(deviceNColor, new float[]{1, 0}), new DeviceNColor(deviceNColor, new float[]{0, 1})));
        canvas.restoreState();
        canvas.restoreState();
        canvas.setCMYKColorStroke(0, 0, 0, 0xFF);
        canvas.rectangle(418, 412, -329, 189);
        canvas.stroke();
        document.close();

        CompareTool compareTool = new CompareTool();
        String error = compareTool.compareByContent(dest_file, "./src/test/resources/com/itextpdf/text/pdf/cs/DeviceNColorSpaceTest/cmp_device_n_gradient_CmykRedRgbBlue.pdf", DEST_FOLDER, "diff_");
        if (error != null) {
            Assert.fail(error);
        }
    }

    public void colorRectangle(PdfContentByte canvas,
                               BaseColor color, float x, float y, float width, float height) {
        canvas.saveState();
        canvas.setColorFill(color);
        canvas.rectangle(x, y, width, height);
        canvas.fillStroke();
        canvas.restoreState();
    }
}

