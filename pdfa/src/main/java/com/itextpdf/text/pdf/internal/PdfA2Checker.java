package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.Jpeg2000;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class PdfA2Checker extends PdfA1Checker {

    static private HashSet<PdfName> allowedBlendModes = new HashSet<PdfName>(Arrays.asList(new PdfName[]{PdfGState.BM_NORMAL, PdfGState.BM_COMPATIBLE,
            PdfGState.BM_MULTIPLY, PdfGState.BM_SCREEN, PdfGState.BM_OVERLAY, PdfGState.BM_DARKEN, PdfGState.BM_LIGHTEN, PdfGState.BM_COLORDODGE,
            PdfGState.BM_COLORBURN, PdfGState.BM_HARDLIGHT, PdfGState.BM_SOFTLIGHT, PdfGState.BM_DIFFERENCE, PdfGState.BM_EXCLUSION}));

    @Override
    protected void checkGState(PdfWriter writer, int key, Object obj1) {
        PdfDictionary gs = (PdfDictionary) obj1;
        PdfObject obj = gs.get(PdfName.BM);
        if (obj != null && !allowedBlendModes.contains(obj)) {
            throw new PdfAConformanceException(MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
        }
    }

    @Override
    protected void checkImage(PdfWriter writer, int key, Object obj1) {
        PdfImage pdfImage = (PdfImage) obj1;
        if (pdfImage != null && (pdfImage.getImage() instanceof Jpeg2000)) {
            Jpeg2000 jpeg2000 = (Jpeg2000) pdfImage.getImage();
            if (!jpeg2000.isJp2()) {
                throw new PdfAConformanceException(MessageLocalization.getComposedMessage("only.jpx.baseline.set.of.features.shall.be.used"));
            }
            if (jpeg2000.getNumOfComps() != 1 && jpeg2000.getNumOfComps() != 3 && jpeg2000.getNumOfComps() != 4) {
                throw new PdfAConformanceException(MessageLocalization.getComposedMessage("the.number.of.colour.channels.in.the.jpeg2000.data.shall.be.123"));
            }
            if (jpeg2000.getBpc() < 1 || jpeg2000.getBpc() > 38) {
                throw new PdfAConformanceException(MessageLocalization.getComposedMessage("the.bit-depth.of.the.jpeg2000.data.shall.have.a.value.in.the.range.1to38"));
            }
            if (jpeg2000.getBpcBoxData() != null) {
                throw new PdfAConformanceException(MessageLocalization.getComposedMessage("all.colour.channels.in.the.jpeg2000.data.shall.have.the.same.bit-depth"));
            }
            ArrayList<Jpeg2000.ColorSpecBox> colorSpecBoxes = jpeg2000.getColorSpecBoxes();
            if (colorSpecBoxes != null) {
                if (colorSpecBoxes.size() > 1) {
                    int approx0x01 = 0;
                    for (Jpeg2000.ColorSpecBox colorSpecBox : colorSpecBoxes) {
                        if (colorSpecBox.getApprox() == 1)
                            approx0x01++;
                    }
                    if (approx0x01 != 1) {
                        throw new PdfAConformanceException(MessageLocalization.getComposedMessage("exactly.one.colour.space.specification.shall.have.the.value.0x01.in.the.approx.field"));
                    }
                }
                for (Jpeg2000.ColorSpecBox colorSpecBox : colorSpecBoxes) {
                    if (colorSpecBox.getMeth() != 1 && colorSpecBox.getMeth() != 2 && colorSpecBox.getMeth() != 3) {
                        throw new PdfAConformanceException(MessageLocalization.getComposedMessage("the.value.of.the.meth.entry.in.colr.box.shall.be.123"));
                    }
                    if (colorSpecBox.getEnumCs() == 19) {
                        throw new PdfAConformanceException(MessageLocalization.getComposedMessage("jpeg2000.enumerated.colour.space.19.(CIEJab).shall.not.be.used"));
                    }
                    byte[] colorProfileBytes = colorSpecBox.getColorProfile();
                    if (colorProfileBytes != null) {
                        //ICC profile verification should follow here.
                    }
                }

            }
        }
    }

    @Override
    protected void checkLayer(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfOCG) {

        } else if (obj1 instanceof PdfOCProperties) {
            PdfOCProperties properties = (PdfOCProperties)obj1;
            ArrayList<PdfDictionary> configsList = new ArrayList<PdfDictionary>();
            PdfDictionary d = properties.getAsDict(PdfName.D);
            if (d != null)
                configsList.add(d);
            PdfArray configs = properties.getAsArray(PdfName.CONFIGS);
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    PdfDictionary config = configs.getAsDict(i);
                    if (config != null)
                        configsList.add(config);
                }
            }
            HashSet<PdfObject> ocgs = new HashSet<PdfObject>();
            PdfArray ocgsArray = properties.getAsArray(PdfName.OCGS);
            if (ocgsArray != null)
                for (int i = 0; i < ocgsArray.size(); i++)
                    ocgs.add(ocgsArray.getPdfObject(i));
            HashSet<String> names = new HashSet<String>();
            HashSet<PdfObject> order = new HashSet<PdfObject>();
            for (PdfDictionary config : configsList) {
                PdfString name = config.getAsString(PdfName.NAME);
                if (name == null) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("optional.content.configuration.dictionary.shall.contain.name.entry"));
                }
                String name1 = name.toUnicodeString();
                if (names.contains(name1)) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("value.of.name.entry.shall.be.unique.amongst.all.optional.content.configuration.dictionaries"));
                }
                names.add(name1);
                if (config.contains(PdfName.AS)) {
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("the.as.key.shall.not.appear.in.any.optional.content.configuration.dictionary"));
                }
                PdfArray orderArray = config.getAsArray(PdfName.ORDER);
                if (orderArray != null)
                    fillOrderRecursively(orderArray, order);
            }
            if (order.size() != ocgs.size()) {
                throw new PdfAConformanceException(MessageLocalization.getComposedMessage("order.array.shall.contain.references.to.all.ocgs"));
            }
            ocgs.retainAll(order);
            if (order.size() != ocgs.size()) {
                throw new PdfAConformanceException(MessageLocalization.getComposedMessage("order.array.shall.contain.references.to.all.ocgs"));
            }
        } else {

        }
    }

    private void fillOrderRecursively(PdfArray orderArray, HashSet<PdfObject> order) {
        for (int i = 0; i < orderArray.size(); i++) {
            PdfArray orderChild = orderArray.getAsArray(i);
            if (orderChild == null) {
                order.add(orderArray.getPdfObject(i));
            } else {
                fillOrderRecursively(orderChild, order);
            }
        }
    }

}
