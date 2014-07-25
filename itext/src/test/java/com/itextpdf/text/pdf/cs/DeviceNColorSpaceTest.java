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

        CompareTool compareTool = new CompareTool(dest_file, "./src/test/resources/com/itextpdf/text/pdf/cs/DeviceNColorSpaceTest/cmp_device_n_gradient_base.pdf");
        String error = compareTool.compare(DEST_FOLDER, "diff_");
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

        CompareTool compareTool = new CompareTool(dest_file, "./src/test/resources/com/itextpdf/text/pdf/cs/DeviceNColorSpaceTest/cmp_device_n_gradient_CmykRedRgbBlue.pdf");
        String error = compareTool.compare(DEST_FOLDER, "diff_");
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

