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

public class LabColorSpaceTest {
    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/cs/LabColorSpaceTest";
    @Before
    public void Init() throws IOException {
        File dir = new File(DEST_FOLDER);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        } else
            dir.mkdirs();
    }

    @Test
    public void labSpotBasedGradient() throws IOException, DocumentException, InterruptedException {
        // step 1
        Document document = new Document(PageSize.A3);
        // step 2
        String dest_file = DEST_FOLDER + "/lab_spot_based_gradient.pdf";
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest_file));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        PdfLabColor pdfLabColor = new PdfLabColor(new float[]{0.9505f, 1.0f, 1.0890f}, new float[]{0f, 0.5f, 1.5f}, new float[]{-10, 100, -10, 100});
        PdfDeviceNColor pdfDeviceNNChannelColor = new PdfDeviceNColor(new PdfSpotColor[]{new PdfSpotColor("lab3", pdfLabColor.rgb2lab(new BaseColor(0, 217, 83))), new PdfSpotColor("labBlue", pdfLabColor.rgb2lab(new BaseColor(0, 0, 255)))});
        PdfSpotColor psc_lab3 = new PdfSpotColor("lab3", pdfLabColor.rgb2lab(new BaseColor(0, 217, 83)));
        PdfSpotColor psc_lab2 = new PdfSpotColor("lab2", pdfLabColor.rgb2lab(new BaseColor(70, 138, 96)));
        PdfSpotColor psc_lab1 = new PdfSpotColor("lab1", pdfLabColor.rgb2lab(new BaseColor(255, 0, 0)));
        PdfSpotColor psc_lab_blue = new PdfSpotColor("labBlue", new BaseColor(0, 0, 100));

        PdfDeviceNColor pdfDeviceNNChannelColor2 = new PdfDeviceNColor(new PdfSpotColor[]{psc_lab_blue, psc_lab2, psc_lab1});

        colorRectangle(canvas, pdfLabColor.rgb2lab(new BaseColor(0, 0, 255)), 36, 878, 36, 36);
        colorRectangle(canvas, pdfLabColor.rgb2lab(new BaseColor(70, 138, 96)), 90, 878, 36, 36);
        colorRectangle(canvas, pdfLabColor.rgb2lab(new BaseColor(0, 217, 83)), 144, 878, 36, 36);
        colorRectangle(canvas, pdfLabColor.rgb2lab(new BaseColor(0, 50, 50)), 198, 878, 36, 36);

        colorRectangle(canvas, pdfLabColor.rgb2lab(new BaseColor(0, 0, 255)).toRgb(), 36, 824, 36, 36);
        colorRectangle(canvas, pdfLabColor.rgb2lab(new BaseColor(70, 138, 96)).toRgb(), 90, 824, 36, 36);
        colorRectangle(canvas, pdfLabColor.rgb2lab(new BaseColor(0, 217, 83)).toRgb(), 144, 824, 36, 36);
        colorRectangle(canvas, pdfLabColor.rgb2lab(new BaseColor(0, 50, 50)).toRgb(), 198, 824, 36, 36);


        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab1", pdfLabColor.rgb2lab(new BaseColor(255, 0, 0))), 1f), 36, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab1", pdfLabColor.rgb2lab(new BaseColor(255, 0, 0))), 0.8f), 90, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab1", pdfLabColor.rgb2lab(new BaseColor(255, 0, 0))), 0.6f), 144, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab1", pdfLabColor.rgb2lab(new BaseColor(255, 0, 0))), 0.2f), 198, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab2", pdfLabColor.rgb2lab(new BaseColor(70, 138, 96))), 1f), 252, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab2", pdfLabColor.rgb2lab(new BaseColor(70, 138, 96))), 0.8f), 306, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab2", pdfLabColor.rgb2lab(new BaseColor(70, 138, 96))), 0.6f), 360, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab2", pdfLabColor.rgb2lab(new BaseColor(70, 138, 96))), 0.2f), 416, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab3", pdfLabColor.rgb2lab(new BaseColor(0, 217, 83))), 1.0f), 470, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab3", pdfLabColor.rgb2lab(new BaseColor(0, 217, 83))), 0.8f), 524, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab3", pdfLabColor.rgb2lab(new BaseColor(0, 217, 83))), 0.6f), 578, 986, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("lab3", pdfLabColor.rgb2lab(new BaseColor(0, 217, 83))), 0.2f), 634, 986, 36, 36);

        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb1", new BaseColor(255, 0, 0)), 1f), 36, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb1", new BaseColor(255, 0, 0)), 0.8f), 90, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb1", new BaseColor(255, 0, 0)), 0.6f), 144, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb1", new BaseColor(255, 0, 0)), 0.2f), 198, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb2", new BaseColor(70, 138, 96)), 1f), 252, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb2", new BaseColor(70, 138, 96)), 0.8f), 306, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb2", new BaseColor(70, 138, 96)), 0.6f), 360, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb2", new BaseColor(70, 138, 96)), 0.2f), 416, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb3", new BaseColor(0, 217, 83)), 1.0f), 470, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb3", new BaseColor(0, 217, 83)), 0.8f), 524, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb3", new BaseColor(0, 217, 83)), 0.6f), 578, 932, 36, 36);
        colorRectangle(canvas, new SpotColor(new PdfSpotColor("rgb3", new BaseColor(0, 217, 83)), 0.2f), 634, 932, 36, 36);

        colorRectangle(canvas, new DeviceNColor(new PdfDeviceNColor(new PdfSpotColor[]{psc_lab2, psc_lab_blue, psc_lab1}), new float[]{0, 0.0f, 1}), 36, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.1f, 0.1f}), 90, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.2f, 0.2f}), 144, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.3f, 0.3f}), 198, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.4f, 0.4f}), 252, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(new PdfDeviceNColor(new PdfSpotColor[]{psc_lab2, psc_lab_blue, psc_lab1, psc_lab3}), new float[]{0.5f, 0.5f, 1, 0.5f}), 306, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor2, new float[]{0.6f, 0.1f, 0.5f}), 360, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.7f, 0.7f}), 416, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{0.8f, 0.8f}), 470, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(new PdfDeviceNColor(new PdfSpotColor[]{psc_lab2, psc_lab_blue, psc_lab1}), new float[]{0.9f, 0.9f, 1}), 524, 770, 36, 36);
        colorRectangle(canvas, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{1, 1}), 578, 770, 36, 36);

        canvas.saveState();
        canvas.rectangle(418, 412, -329, 189);
        canvas.clip();
        canvas.newPath();
        canvas.saveState();
        canvas.concatCTM(329f, 0f, 0f, -329f, 89f, 506.5f);
        canvas.paintShading(PdfShading.simpleAxial(writer, 0, 0, 1, 0, new DeviceNColor(pdfDeviceNNChannelColor, new float[]{1, 0}), new DeviceNColor(pdfDeviceNNChannelColor, new float[]{ 0, 1})));
        canvas.restoreState();
        canvas.restoreState();
        canvas.setColorStroke(new DeviceNColor(pdfDeviceNNChannelColor, new float[]{1, 1}));
        canvas.rectangle(418, 412, -329, 189);
        canvas.stroke();

        // step 5
        document.close();

        CompareTool compareTool = new CompareTool(dest_file, "./src/test/resources/com/itextpdf/text/pdf/cs/LabColorSpaceTest/cmp_lab_spot_based_gradient.pdf");
        String error = compareTool.compare(DEST_FOLDER, "diff");
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
