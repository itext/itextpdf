package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;

import java.util.Arrays;

public class PdfLabColor implements ICachedColorSpace {
    float[] whitePoint = new float[]{0.9505f, 1.0f, 1.0890f};
    float[] blackPoint = null;
    float[] range = null;

    public PdfLabColor() {}

    public PdfLabColor(float[] whitePoint) {
        if (whitePoint == null
                || whitePoint.length != 3
                || whitePoint[0] < 0.000001f || whitePoint[2] < 0.000001f
                || whitePoint[1] < 0.999999f || whitePoint[1] > 1.000001f)
            throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.white.point"));
        this.whitePoint = whitePoint;
    }

    public PdfLabColor(float[] whitePoint, float[] blackPoint) {
        this(whitePoint);
        this.blackPoint = blackPoint;
    }

    public PdfLabColor(float[] whitePoint, float[] blackPoint, float[] range) {
        this(whitePoint, blackPoint);
        this.range = range;
    }

    public PdfObject getPdfObject(PdfWriter writer) {
        PdfArray array = new PdfArray(PdfName.LAB);
        PdfDictionary dictionary = new PdfDictionary();
        if (whitePoint == null
                || whitePoint.length != 3
                || whitePoint[0] < 0.000001f || whitePoint[2] < 0.000001f
                || whitePoint[1] < 0.999999f || whitePoint[1] > 1.000001f)
            throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.white.point"));
        dictionary.put(PdfName.WHITEPOINT, new PdfArray(whitePoint));
        if (blackPoint != null) {
            if (blackPoint.length != 3
                    || blackPoint[0] < -0.000001f || blackPoint[1] < -0.000001f || blackPoint[2] < -0.000001f)
                throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.black.point"));
            dictionary.put(PdfName.BLACKPOINT, new PdfArray(blackPoint));
        }
        if (range != null) {
            if (range.length != 4 || range[0] > range[1] || range[2] > range[3])
                throw new RuntimeException(MessageLocalization.getComposedMessage("lab.cs.range"));
            dictionary.put(PdfName.RANGE, new PdfArray(range));
        }
        array.add(dictionary);
        return array;
    }

    public BaseColor lab2Rgb(float l, float a, float b) {
        double[] clinear = lab2RgbLinear(l, a, b);
        return new BaseColor((float)clinear[0], (float)clinear[1], (float)clinear[2]);
    }

    CMYKColor lab2Cmyk(float l, float a, float b) {
        double[] clinear = lab2RgbLinear(l, a, b);

        double r = clinear[0];
        double g = clinear[1];
        double bee = clinear[2];
        double computedC = 0, computedM = 0, computedY = 0, computedK = 0;

        // BLACK
        if (r==0 && g==0 && b==0) {
            computedK = 1;
        } else {
            computedC = 1 - r;
            computedM = 1 - g;
            computedY = 1 - bee;

            double minCMY = Math.min(computedC,
                    Math.min(computedM,computedY));
            computedC = (computedC - minCMY) / (1 - minCMY) ;
            computedM = (computedM - minCMY) / (1 - minCMY) ;
            computedY = (computedY - minCMY) / (1 - minCMY) ;
            computedK = minCMY;
        }

        return new CMYKColor((float)computedC, (float)computedM, (float)computedY, (float)computedK);
    }

    protected double[] lab2RgbLinear(float l, float a, float b) {
        if (range != null && range.length == 4) {
            if (a < range[0])
                a = range[0];
            if (a > range[1])
                a = range[1];
            if (b < range[2])
                b = range[2];
            if (b > range[3])
                b = range[3];
        }
        double theta = 6.0 / 29.0;

        double fy = (l + 16) / 116.0;
        double fx = fy + (a / 500.0);
        double fz = fy - (b / 200.0);

        double x = fx > theta ? whitePoint[0] * (fx * fx * fx) : (fx - 16.0/116.0) * 3 * (theta * theta) * whitePoint[0];
        double y = fy > theta ? whitePoint[1] * (fy * fy * fy) : (fy - 16.0/116.0) * 3 * (theta * theta) * whitePoint[1];
        double z = fz > theta ? whitePoint[2] * (fz * fz * fz) : (fz - 16.0/116.0) * 3 * (theta * theta) * whitePoint[2];

        double[] clinear = new double[3];
        clinear[0] = x * 3.2410 - y * 1.5374 - z * 0.4986; // red
        clinear[1] = -x * 0.9692 + y * 1.8760 - z * 0.0416; // green
        clinear[2] = x * 0.0556 - y * 0.2040 + z * 1.0570; // blue

        for (int i = 0; i < 3; i++) {
            clinear[i] = (clinear[i] <= 0.0031308)
                    ? 12.92 * clinear[i]
                    : (1 + 0.055) * Math.pow(clinear[i], (1.0/2.4)) - 0.055;
            if (clinear[i] < 0)
                clinear[i] = 0;
            else if (clinear[i] > 1f)
                clinear[i] = 1.0;
        }

        return clinear;
    }

    public LabColor rgb2lab(BaseColor baseColor) {
        double rLinear = baseColor.getRed() / 255f;
        double gLinear = baseColor.getGreen() / 255f;
        double bLinear = baseColor.getBlue() / 255f;

        // convert to a sRGB form
        double r = (rLinear > 0.04045) ? Math.pow((rLinear + 0.055) / (1 + 0.055), 2.2) : (rLinear/12.92);
        double g = (gLinear > 0.04045) ? Math.pow((gLinear + 0.055) / (1 + 0.055), 2.2) : (gLinear/12.92);
        double b = (bLinear > 0.04045) ? Math.pow((bLinear + 0.055) / (1 + 0.055), 2.2) : (bLinear/12.92);

        // converts
        double x = r * 0.4124 + g * 0.3576 + b * 0.1805;
        double y = r * 0.2126 + g * 0.7152 + b * 0.0722;
        double z = r * 0.0193 + g * 0.1192 + b * 0.9505;

        float l = Math.round((116.0 * fXyz(y/whitePoint[1]) - 16) * 1000) / 1000f;
        float a = Math.round((500.0*(fXyz(x/whitePoint[0]) - fXyz(y/whitePoint[1]))) * 1000) / 1000f;
        float bee = Math.round((200.0*(fXyz(y/whitePoint[1]) - fXyz(z/whitePoint[2]))) * 1000) / 1000f;

        return new LabColor(this, l, a, bee);
    }

    private static double fXyz(double t) {
        return ((t > 0.008856) ? Math.pow(t, (1.0/3.0)) : (7.787*t + 16.0/116.0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdfLabColor)) return false;

        PdfLabColor that = (PdfLabColor) o;

        if (!Arrays.equals(blackPoint, that.blackPoint)) return false;
        if (!Arrays.equals(range, that.range)) return false;
        if (!Arrays.equals(whitePoint, that.whitePoint)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(whitePoint);
        result = 31 * result + (blackPoint != null ? Arrays.hashCode(blackPoint) : 0);
        result = 31 * result + (range != null ? Arrays.hashCode(range) : 0);
        return result;
    }
}
