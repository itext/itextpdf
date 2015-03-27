package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;

public class DeviceNColor extends ExtendedColor {
    PdfDeviceNColor pdfDeviceNColor;
    float[] tints;
    public DeviceNColor(PdfDeviceNColor pdfDeviceNColor, float[] tints) {
        super(TYPE_DEVICEN);
        if (pdfDeviceNColor.getSpotColors().length != tints.length)
            throw new RuntimeException(MessageLocalization.getComposedMessage("devicen.color.shall.have.the.same.number.of.colorants.as.the.destination.DeviceN.color.space"));
        this.pdfDeviceNColor = pdfDeviceNColor;
        this.tints = tints;
    }

    public PdfDeviceNColor getPdfDeviceNColor() {
        return pdfDeviceNColor;
    }

    public float[] getTints() {
        return tints;
    }
    public boolean equals(Object obj) {
        if (obj instanceof DeviceNColor && ((DeviceNColor) obj).tints.length == tints.length) {
            int i = 0;
            for (float tint : this.tints) {
                if (tint != ((DeviceNColor) obj).tints[i])
                    return false;
                i++;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = pdfDeviceNColor.hashCode();
        for (Float tint : this.tints) {
            hashCode ^= tint.hashCode();
        }
        return hashCode;
    }
}
