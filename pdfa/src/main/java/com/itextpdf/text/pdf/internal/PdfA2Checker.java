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

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Jpeg2000;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class PdfA2Checker extends PdfAChecker {

    static private HashSet<PdfName> allowedBlendModes = new HashSet<PdfName>(Arrays.asList(new PdfName[]{PdfGState.BM_NORMAL, PdfGState.BM_COMPATIBLE,
            PdfGState.BM_MULTIPLY, PdfGState.BM_SCREEN, PdfGState.BM_OVERLAY, PdfGState.BM_DARKEN, PdfGState.BM_LIGHTEN, PdfGState.BM_COLORDODGE,
            PdfGState.BM_COLORBURN, PdfGState.BM_HARDLIGHT, PdfGState.BM_SOFTLIGHT, PdfGState.BM_DIFFERENCE, PdfGState.BM_EXCLUSION}));

    static private final HashSet<PdfName> restrictedActions = new HashSet<PdfName>(Arrays.asList(PdfName.LAUNCH, PdfName.SOUND,
            PdfName.MOVIE, PdfName.RESETFORM, PdfName.IMPORTDATA, PdfName.HIDE, PdfName.SETOCGSTATE, PdfName.RENDITION, PdfName.TRANS, PdfName.GOTO3DVIEW, PdfName.JAVASCRIPT));

    static final int maxPageSize = 14400;
    static final int minPageSize = 3;

    protected int gsStackDepth = 0;

    PdfA2Checker(PdfAConformanceLevel conformanceLevel) {
        super(conformanceLevel);
    }

    @Override
    protected void checkFont(PdfWriter writer, int key, Object obj1) {
        BaseFont bf = (BaseFont) obj1;
        if (bf.getFontType() == BaseFont.FONT_TYPE_DOCUMENT) {
            PdfStream prs = null;
            PdfDictionary fontDictionary = ((DocumentFont) bf).getFontDictionary();
            PdfDictionary fontDescriptor = fontDictionary.getAsDict(PdfName.FONTDESCRIPTOR);
            if (fontDescriptor != null) {
                prs = fontDescriptor.getAsStream(PdfName.FONTFILE);
                if (prs == null) {
                    prs = fontDescriptor.getAsStream(PdfName.FONTFILE2);
                }
                if (prs == null) {
                    prs = fontDescriptor.getAsStream(PdfName.FONTFILE3);
                }
            }
            if (prs == null) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont) obj1).getPostscriptFontName()));
            }
        } else {
            if (!bf.isEmbedded())
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("all.the.fonts.must.be.embedded.this.one.isn.t.1", ((BaseFont) obj1).getPostscriptFontName()));
        }
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
        if (pdfImage.contains(PdfName.OPI)) {
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.image.dictionary.shall.not.contain.opi.key"));
        }
        PdfBoolean interpolate = pdfImage.getAsBoolean(PdfName.INTERPOLATE);
        if (interpolate != null && interpolate.booleanValue()) {
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.value.of.interpolate.key.shall.not.be.true"));
        }
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
    protected void checkInlineImage(PdfWriter writer, int key, Object obj1) {
        PdfImage pdfImage = (PdfImage) obj1;
        PdfBoolean interpolate = pdfImage.getAsBoolean(PdfName.INTERPOLATE);
        if (interpolate != null && interpolate.booleanValue()) {
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.value.of.interpolate.key.shall.not.be.true"));
        }

        PdfObject filter = pdfImage.getDirectObject(PdfName.FILTER);
        if (filter instanceof PdfName) {
            if (filter.equals(PdfName.LZWDECODE))
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("lzwdecode.filter.is.not.permitted"));
            if (filter.equals(PdfName.CRYPT)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("crypt.filter.is.not.permitted.inline.image"));
            }
        } else if (filter instanceof PdfArray) {
            for (int i = 0; i < ((PdfArray) filter).size(); i++) {
                PdfName f = ((PdfArray) filter).getAsName(i);
                if (f.equals(PdfName.LZWDECODE))
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("lzwdecode.filter.is.not.permitted"));
                if (f.equals(PdfName.CRYPT)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("crypt.filter.is.not.permitted.inline.image"));
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
        if (obj1 instanceof PdfWriter.PdfTrailer) {
            PdfWriter.PdfTrailer trailer = (PdfWriter.PdfTrailer) obj1;
            if (trailer.get(PdfName.ENCRYPT) != null) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("keyword.encrypt.shall.not.be.used.in.the.trailer.dictionary"));
            }
        }
    }

    @Override
    protected void checkStream(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfStream) {
            PdfStream stream = (PdfStream) obj1;
            if (stream.contains(PdfName.F) || stream.contains(PdfName.FFILTER) || stream.contains(PdfName.FDECODEPARMS)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("stream.object.dictionary.shall.not.contain.the.f.ffilter.or.fdecodeparams.keys"));
            }

            PdfObject filter = stream.getDirectObject(PdfName.FILTER);
            if (filter instanceof PdfName) {
                if (filter.equals(PdfName.LZWDECODE))
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("lzwdecode.filter.is.not.permitted"));
                if (filter.equals(PdfName.CRYPT)) {
                    PdfDictionary decodeParams = stream.getAsDict(PdfName.DECODEPARMS);
                    if (decodeParams != null) {
                        PdfString cryptFilterName = decodeParams.getAsString(PdfName.NAME);
                        if (cryptFilterName != null && !cryptFilterName.equals(PdfName.IDENTITY)) {
                            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("not.identity.crypt.filter.is.not.permitted"));
                        }
                    }
                }
            } else if (filter instanceof PdfArray) {
                for (int i = 0; i < ((PdfArray) filter).size(); i++) {
                    PdfName f = ((PdfArray) filter).getAsName(i);
                    if (f.equals(PdfName.LZWDECODE))
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("lzwdecode.filter.is.not.permitted"));
                    if (f.equals(PdfName.CRYPT)) {
                        PdfArray decodeParams = stream.getAsArray(PdfName.DECODEPARMS);
                        if (decodeParams != null && i < decodeParams.size()) {
                            PdfDictionary decodeParam = decodeParams.getAsDict(i);
                            PdfString cryptFilterName = decodeParam.getAsString(PdfName.NAME);
                            if (cryptFilterName != null && !cryptFilterName.equals(PdfName.IDENTITY)) {
                                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("not.identity.crypt.filter.is.not.permitted"));
                            }
                        }
                    }
                }
            }

            if (PdfName.FORM.equals(stream.getAsName(PdfName.SUBTYPE))) {
                if (stream.contains(PdfName.OPI)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("a.form.xobject.dictionary.shall.not.contain.opi.key"));
                }
                if (stream.contains(PdfName.PS)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("a.form.xobject..dictionary.shall.not.contain.ps.key"));
                }
            }

            if (PdfName.PS.equals(stream.getAsName(PdfName.SUBTYPE))) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("postscript.xobjects.are.not.allowed"));
            }
        }
    }

    @Override
    protected void checkFileSpec(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfFileSpecification) {
            PdfDictionary fileSpec = (PdfFileSpecification)obj1;
            if (fileSpec.contains(PdfName.EF) &&
                    (!fileSpec.contains(PdfName.UF) || !fileSpec.contains(PdfName.F))) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("file.specification.dictionary.shall.contain.f.uf.and.desc.entries"));
            }
        }
    }

    @Override
    protected void checkPdfObject(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfNumber) {
            PdfNumber number = (PdfNumber) obj1;
            if (Math.abs(number.doubleValue()) > PdfA1Checker.maxRealValue && number.toString().contains(".")) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("real.number.is.out.of.range"));
            }
        } else if (obj1 instanceof PdfString) {
            PdfString string = (PdfString) obj1;
            if (string.getBytes().length > PdfA1Checker.maxStringLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.string.is.too.long"));
            }
        } else if (obj1 instanceof PdfArray) {
            PdfArray array = (PdfArray) obj1;
            if (array.size() > PdfA1Checker.maxArrayLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.array.is.out.of.bounds"));
            }
        }  else if (obj1 instanceof PdfDictionary) {
            PdfDictionary dictionary = (PdfDictionary) obj1;
            PdfName type = dictionary.getAsName(PdfName.TYPE);
            if (PdfName.CATALOG.equals(type)) {
                if (dictionary.contains(PdfName.AA)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.an.aa.entry"));
                }

                if (dictionary.contains(PdfName.REQUIREMENTS)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.a.requirements.entry"));
                }

                if (dictionary.contains(PdfName.NEEDRENDERING)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.a.needrendering.entry"));
                }

                if (dictionary.contains(PdfName.ACROFORM)) {
                    PdfDictionary acroForm = dictionary.getAsDict(PdfName.ACROFORM);
                    if (acroForm != null && acroForm.contains(PdfName.XFA)) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.acroform.xfa.entry"));
                    }
                }

                if (dictionary.contains(PdfName.NAMES)) {
                    PdfDictionary names = dictionary.getAsDict(PdfName.NAMES);
                    if (names != null && names.contains(PdfName.ALTERNATEPRESENTATION)) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.alternatepresentation.names.entry"));
                    }
                }

                if (checkStructure(conformanceLevel)) {
                    PdfDictionary markInfo = dictionary.getAsDict(PdfName.MARKINFO);
                    if (markInfo == null || markInfo.getAsBoolean(PdfName.MARKED) == null || markInfo.getAsBoolean(PdfName.MARKED).booleanValue() == false) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("document.catalog.dictionary.shall.include.a.markinfo.dictionary.whose.entry.marked.shall.have.a.value.of.true"));
                    }
                    if (!dictionary.contains(PdfName.LANG)) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("document.catalog.dictionary.should.contain.lang.entry"));
                    }
                }

            }
            if (PdfName.PAGE.equals(type)) {
                PdfName[] boxNames = new PdfName[] {PdfName.MEDIABOX, PdfName.CROPBOX, PdfName.TRIMBOX, PdfName.ARTBOX, PdfName.BLEEDBOX};
                for (PdfName boxName: boxNames) {
                    PdfObject box = dictionary.getDirectObject(boxName);
                    if (box instanceof PdfRectangle) {
                        float width = ((PdfRectangle)box).width();
                        float height = ((PdfRectangle)box).height();
                        if (width < minPageSize || width > maxPageSize || height < minPageSize || height > maxPageSize)
                            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.page.less.3.units.nor.greater.14400.in.either.direction"));
                    }
                }
                if (dictionary.contains(PdfName.AA)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("page.dictionary.shall.not.include.aa.entry"));
                }

                if (dictionary.contains(PdfName.PRESSTEPS)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("page.dictionary.shall.not.include.pressteps.entry"));
                }
            }
        }
    }

    @Override
    protected void checkCanvas(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof String) {
            if ("q".equals(obj1)) {
                if (++gsStackDepth > PdfA1Checker.maxGsStackDepth)
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("graphics.state.stack.depth.is.greater.than.28"));
            } else if ("Q".equals(obj1)) {
                gsStackDepth--;
            }
        }
    }

    @Override
    protected void checkColor(PdfWriter writer, int key, Object obj1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void checkAnnotation(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfFormField) {
            PdfFormField field = (PdfFormField) obj1;
            if (!field.contains(PdfName.SUBTYPE))
                return;
            if (field.contains(PdfName.AA) || field.contains(PdfName.A)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("widget.annotation.dictionary.or.field.dictionary.shall.not.include.a.or.aa.entry"));
            }
        }
        if (obj1 instanceof PdfAnnotation) {
            PdfAnnotation annot = (PdfAnnotation) obj1;
            PdfObject subtype = annot.get(PdfName.SUBTYPE);
            if (PdfName.WIDGET.equals(annot.getAsName(PdfName.SUBTYPE)) && (annot.contains(PdfName.AA) || annot.contains(PdfName.A))) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("widget.annotation.dictionary.or.field.dictionary.shall.not.include.a.or.aa.entry"));
            }

            if (checkStructure(conformanceLevel)) {
                if (PdfA1Checker.contentAnnotations.contains(subtype) && !annot.contains(PdfName.CONTENTS)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("annotation.of.type.1.should.have.contents.key", subtype.toString()));
                }
            }
        }
    }

    @Override
    protected void checkAction(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfAction) {
            PdfAction action = (PdfAction) obj1;
            PdfName s = action.getAsName(PdfName.S);
            if (PdfA1Checker.setState.equals(s) || PdfA1Checker.noOp.equals(s)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("deprecated.setstate.and.noop.actions.are.not.allowed"));
            }
            if (restrictedActions.contains(s)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("launch.sound.movie.resetform.importdata.and.javascript.actions.are.not.allowed"));
            }
            if (PdfName.NAMED.equals(s)) {
                PdfName n = action.getAsName(PdfName.N);
                if (n != null && !PdfA1Checker.allowedNamedActions.contains(n)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("named.action.type.1.not.allowed", n.toString()));
                }
            }
        }
    }

    @Override
    protected void checkForm(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfAcroForm) {
            PdfAcroForm form = (PdfAcroForm) obj1;
            PdfBoolean needAppearances = form.getAsBoolean(PdfName.NEEDAPPEARANCES);
            if (needAppearances != null && needAppearances.booleanValue()) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("needappearances.flag.of.the.interactive.form.dictionary.shall.either.not.be.present.or.shall.be.false"));
            }
        }
    }

    @Override
    protected void checkStructElem(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfStructureElement) {
            PdfStructureElement structElem = (PdfStructureElement) obj1;
            PdfName role = structElem.getStructureType();
            if (PdfName.FIGURE.equals(role) || PdfName.FORMULA.equals(role) || PdfName.FORM.equals(role)) {
                PdfObject o = structElem.get(PdfName.ALT);
                if (o instanceof PdfString && o.toString().length() > 0) {

                } else {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("alt.entry.should.specify.alternate.description.for.1.element", role.toString()));
                }
            }
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
