/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Alexander Chingarev, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.Jpeg2000;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class PdfA2Checker extends PdfAChecker {

    static private HashSet<PdfName> allowedBlendModes = new HashSet<PdfName>(Arrays.asList(new PdfName[]{PdfGState.BM_NORMAL, PdfGState.BM_COMPATIBLE,
            PdfGState.BM_MULTIPLY, PdfGState.BM_SCREEN, PdfGState.BM_OVERLAY, PdfGState.BM_DARKEN, PdfGState.BM_LIGHTEN, PdfGState.BM_COLORDODGE,
            PdfGState.BM_COLORBURN, PdfGState.BM_HARDLIGHT, PdfGState.BM_SOFTLIGHT, PdfGState.BM_DIFFERENCE, PdfGState.BM_EXCLUSION}));

    PdfA2Checker(PdfAConformanceLevel conformanceLevel) {
        super(conformanceLevel);
    }

    @Override
    protected void checkFont(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

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
            PdfOCProperties properties = (PdfOCProperties) obj1;
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

    @Override
    protected void checkTrailer(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkStream(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkFileSpec(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkPdfObject(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkCanvas(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkColor(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkAnnotation(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkAction(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkForm(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkStructElem(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
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
