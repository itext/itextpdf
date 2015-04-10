package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;

import java.util.Arrays;
import java.util.Locale;

public class PdfDeviceNColor implements ICachedColorSpace, IPdfSpecialColorSpace {
    PdfSpotColor[] spotColors;
    ColorDetails[] colorantsDetails;

    public PdfDeviceNColor(PdfSpotColor[] spotColors) {
        this.spotColors = spotColors;
    }

    public int getNumberOfColorants() {
        return spotColors.length;
    }

    public PdfSpotColor[] getSpotColors() {
        return spotColors;
    }

    public ColorDetails[] getColorantDetails(PdfWriter writer) {
        if (colorantsDetails == null) {
            colorantsDetails = new ColorDetails[spotColors.length];
            int i = 0;
            for (PdfSpotColor spotColorant : spotColors) {
                colorantsDetails[i] = writer.addSimple(spotColorant);
                i++;
            }
        }
        return colorantsDetails;
    }

    public PdfObject getPdfObject(PdfWriter writer) {
        PdfArray array = new PdfArray(PdfName.DEVICEN);

        PdfArray colorants = new PdfArray();
        float[] colorantsRanges = new float[spotColors.length * 2];
        PdfDictionary colorantsDict = new PdfDictionary();
        String psFunFooter = "";

        int numberOfColorants = spotColors.length;
        float[][] CMYK = new float[4][numberOfColorants];
        int i = 0;
        for (; i < numberOfColorants; i++) {
            PdfSpotColor spotColorant = spotColors[i];
            colorantsRanges[2 * i] = 0;
            colorantsRanges[2 * i + 1] = 1;
            colorants.add(spotColorant.getName());
            if (colorantsDict.get(spotColorant.getName()) != null)
                throw new RuntimeException(MessageLocalization.getComposedMessage("devicen.component.names.shall.be.different"));
            if (colorantsDetails != null)
                colorantsDict.put(spotColorant.getName(), colorantsDetails[i].getIndirectReference());
            else
                colorantsDict.put(spotColorant.getName(), spotColorant.getPdfObject(writer));
            BaseColor color = spotColorant.getAlternativeCS();
            if (color instanceof ExtendedColor) {
                int type = ((ExtendedColor)color).type;
                switch (type) {
                    case ExtendedColor.TYPE_GRAY:
                        CMYK[0][i] = 0;
                        CMYK[1][i] = 0;
                        CMYK[2][i] = 0;
                        CMYK[3][i] = 1 - ((GrayColor)color).getGray();
                        break;
                    case ExtendedColor.TYPE_CMYK:
                        CMYK[0][i] = ((CMYKColor)color).getCyan();
                        CMYK[1][i] = ((CMYKColor)color).getMagenta();
                        CMYK[2][i] = ((CMYKColor)color).getYellow();
                        CMYK[3][i] = ((CMYKColor)color).getBlack();
                        break;
                    case ExtendedColor.TYPE_LAB:
                        CMYKColor cmyk = ((LabColor)color).toCmyk();
                        CMYK[0][i] = cmyk.getCyan();
                        CMYK[1][i] = cmyk.getMagenta();
                        CMYK[2][i] = cmyk.getYellow();
                        CMYK[3][i] = cmyk.getBlack();
                        break;
                    default:
                        throw new RuntimeException(MessageLocalization.getComposedMessage("only.rgb.gray.and.cmyk.are.supported.as.alternative.color.spaces"));
                }
            } else {
                float r = color.getRed();
                float g = color.getGreen();
                float b = color.getBlue();
                float computedC = 0, computedM = 0, computedY = 0, computedK = 0;

                // BLACK
                if (r==0 && g==0 && b==0) {
                    computedK = 1;
                } else {
                    computedC = 1 - (r/255);
                    computedM = 1 - (g/255);
                    computedY = 1 - (b/255);

                    float minCMY = Math.min(computedC,
                            Math.min(computedM,computedY));
                    computedC = (computedC - minCMY) / (1 - minCMY) ;
                    computedM = (computedM - minCMY) / (1 - minCMY) ;
                    computedY = (computedY - minCMY) / (1 - minCMY) ;
                    computedK = minCMY;
                }
                CMYK[0][i] = computedC;
                CMYK[1][i] = computedM;
                CMYK[2][i] = computedY;
                CMYK[3][i] = computedK;
            }
            psFunFooter += "pop ";
        }
        array.add(colorants);

        String psFunHeader = String.format(Locale.US, "1.000000 %d 1 roll ", numberOfColorants + 1);
        array.add(PdfName.DEVICECMYK);
        psFunHeader = psFunHeader + psFunHeader + psFunHeader + psFunHeader;
        String psFun = "";
        i = numberOfColorants + 4;
        for (; i > numberOfColorants; i--) {
            psFun += String.format(Locale.US, "%d -1 roll ", i);
            for (int j = numberOfColorants; j > 0; j--) {
                psFun += String.format(Locale.US, "%d index %f mul 1.000000 cvr exch sub mul ", j, CMYK[numberOfColorants + 4 - i][numberOfColorants - j]);
            }
            psFun += String.format(Locale.US, "1.000000 cvr exch sub %d 1 roll ", i);
        }

        PdfFunction func = PdfFunction.type4(writer, colorantsRanges, new float[]{0, 1, 0, 1, 0, 1, 0, 1}, "{ " + psFunHeader + psFun + psFunFooter + "}");
        array.add(func.getReference());

        PdfDictionary attr = new PdfDictionary();
        attr.put(PdfName.SUBTYPE, PdfName.NCHANNEL);
        attr.put(PdfName.COLORANTS, colorantsDict);
        array.add(attr);

        return array;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdfDeviceNColor)) return false;

        PdfDeviceNColor that = (PdfDeviceNColor) o;

        if (!Arrays.equals(spotColors, that.spotColors)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(spotColors);
    }
}
