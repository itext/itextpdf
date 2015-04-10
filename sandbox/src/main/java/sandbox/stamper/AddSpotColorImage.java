package sandbox.stamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class AddSpotColorImage {
    public static final String SRC = "resources/pdfs/image.pdf";
    public static final String DEST = "results/stamper/spot_color_image.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddSpotColorImage().manipulatePdf(SRC, DEST);
    }

    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        // suppose that this is our image data
        byte circledata[] = { (byte) 0x3c, (byte) 0x7e, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x7e,
                (byte) 0x3c };
        // we have an image of 8 by 8 with 1 component and 1 bit per component
        // the 1 value is colored, the 0 value is transparent
        Image image = Image.getInstance(8, 8, 1, 1, circledata, new int[]{0, 0});
        // By default, the colorspace of such an image is DeviceGray
        // In other words: the image is in black and white
        // We want to use a separation colorspace:
        PdfArray colorspace = getSeparationColorspace(stamper.getWriter(), new CMYKColor(0.8f, 0.3f, 0.3f, 0.1f));
        // We get the image as a stream object
        PdfImage stream = new PdfImage(image, "", null);
        // and we change its color space:
        stream.put(PdfName.COLORSPACE, colorspace);
        // We add the stream to the writer
        PdfIndirectObject ref = stamper.getWriter().addToBody(stream);
        // We adapt the original image
        image.setDirectReference(ref.getIndirectReference());
        image.scaleAbsolute(100, 100);
        image.setAbsolutePosition(100, 200);
        // Now we add the image to the existing PDF document
        PdfContentByte canvas = stamper.getOverContent(1);
        canvas.addImage(image);
        stamper.close();
        reader.close();
    }

    public PdfArray getSeparationColorspace(PdfWriter writer, CMYKColor cmyk) {
        PdfArray array = new PdfArray(PdfName.SEPARATION);
        array.add(new PdfName("mySpotColor"));
        array.add(PdfName.DEVICECMYK);
        PdfDictionary func = new PdfDictionary();
        func.put(PdfName.FUNCTIONTYPE, new PdfNumber(2));
        func.put(PdfName.DOMAIN, new PdfArray(new float[]{0, 1}));
        func.put(PdfName.C0, new PdfArray(new float[]{0, 0, 0, 0}));
        func.put(PdfName.C1, new PdfArray(new float[]{cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack()}));
        func.put(PdfName.N, new PdfNumber(1));
        array.add(func);
        return array;
    }
}
