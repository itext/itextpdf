package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;

public class LabColor extends ExtendedColor {
    PdfLabColor labColorSpace;
    private float l;
    private float a;
    private float b;

    public LabColor(PdfLabColor labColorSpace, float l, float a, float b) {
        super(ExtendedColor.TYPE_LAB);
        this.labColorSpace = labColorSpace;
        this.l = l;
        this.a = a;
        this.b = b;
        BaseColor altRgbColor = labColorSpace.lab2Rgb(l, a, b);
        setValue(altRgbColor.getRed(), altRgbColor.getGreen(), altRgbColor.getBlue(), 255);
    }

    public PdfLabColor getLabColorSpace() {
        return labColorSpace;
    }

    public float getL() {
        return l;
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public BaseColor toRgb() {
        return labColorSpace.lab2Rgb(l, a, b);
    }

    CMYKColor toCmyk() {
        return labColorSpace.lab2Cmyk(l, a, b);
    }
}
