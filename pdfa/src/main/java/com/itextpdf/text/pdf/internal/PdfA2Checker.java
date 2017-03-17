/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Alexander Chingarev, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Jpeg2000;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class PdfA2Checker extends PdfAChecker {

    static public final HashSet<PdfName> allowedBlendModes = new HashSet<PdfName>(Arrays.asList(PdfGState.BM_NORMAL,
            PdfGState.BM_COMPATIBLE, PdfGState.BM_MULTIPLY, PdfGState.BM_SCREEN, PdfGState.BM_OVERLAY,
            PdfGState.BM_DARKEN, PdfGState.BM_LIGHTEN, PdfGState.BM_COLORDODGE, PdfGState.BM_COLORBURN,
            PdfGState.BM_HARDLIGHT, PdfGState.BM_SOFTLIGHT, PdfGState.BM_DIFFERENCE, PdfGState.BM_EXCLUSION));

    static public final HashSet<PdfName> restrictedActions = new HashSet<PdfName>(Arrays.asList(PdfName.LAUNCH,
            PdfName.SOUND, PdfName.MOVIE, PdfName.RESETFORM, PdfName.IMPORTDATA, PdfName.HIDE, PdfName.SETOCGSTATE,
            PdfName.RENDITION, PdfName.TRANS, PdfName.GOTO3DVIEW, PdfName.JAVASCRIPT));

    static private HashSet<PdfName> allowedAnnotTypes = new HashSet<PdfName>(Arrays.asList(PdfName.TEXT, PdfName.LINK,
            PdfName.FREETEXT, PdfName.LINE, PdfName.SQUARE, PdfName.CIRCLE, PdfName.POLYGON, PdfName.POLYLINE,
            PdfName.HIGHLIGHT, PdfName.UNDERLINE, PdfName.SQUIGGLY, PdfName.STRIKEOUT, PdfName.STAMP, PdfName.CARET,
            PdfName.INK, PdfName.POPUP, PdfName.FILEATTACHMENT, PdfName.WIDGET, PdfName.PRINTERMARK, PdfName.TRAPNET,
            PdfName.WATERMARK, PdfName.REDACT));

    static public final HashSet<PdfName> contentAnnotations = new HashSet<PdfName>(Arrays.asList(PdfName.TEXT,
            PdfName.FREETEXT, PdfName.LINE, PdfName.SQUARE, PdfName.CIRCLE, PdfName.STAMP, PdfName.INK, PdfName.POPUP));

    static private final HashSet<PdfName> keysForCheck = new HashSet<PdfName>(Arrays.asList(PdfName.AP, PdfName.N,
            PdfName.R, PdfName.D, PdfName.FONTFILE, PdfName.FONTFILE2, PdfName.FONTFILE3, PdfName.NAME, PdfName.XFA,
            PdfName.ALTERNATEPRESENTATION, PdfName.DOCMDP, PdfName.REFERENCE, new PdfName("DigestLocation"),
            new PdfName("DigestMethod"), new PdfName("DigestValue"), PdfName.MARKED, PdfName.S, PdfName.SUBTYPE,
            PdfName.F));

    static public final PdfName DIGESTLOCATION = new PdfName("DigestLocation");
    static public final PdfName DIGESTMETHOD = new PdfName("DigestMethod");
    static public final PdfName DIGESTVALUE = new PdfName("DigestValue");

    static final int maxPageSize = 14400;
    static final int minPageSize = 3;
    protected int gsStackDepth = 0;
    protected boolean rgbUsed = false;
    protected boolean cmykUsed = false;
    protected boolean grayUsed = false;
    protected boolean transparencyWithoutPageGroupDetected = false;
    protected boolean transparencyDetectedOnThePage = false;
    static public final int maxStringLength = 32767;


    PdfA2Checker(PdfAConformanceLevel conformanceLevel) {
        super(conformanceLevel);
    }

    @Override
    protected HashSet<PdfName> initKeysForCheck() {
        return keysForCheck;
    }

    @Override
    protected void checkFont(PdfWriter writer, int key, Object obj1) {
        BaseFont bf = (BaseFont) obj1;
        if (bf.getFontType() == BaseFont.FONT_TYPE_DOCUMENT) {
            PdfStream prs = null;
            PdfDictionary fontDictionary = ((DocumentFont) bf).getFontDictionary();
            PdfDictionary fontDescriptor = getDirectDictionary(fontDictionary.get(PdfName.FONTDESCRIPTOR));
            if (fontDescriptor != null) {
                prs = getDirectStream(fontDescriptor.get(PdfName.FONTFILE));
                if (prs == null) {
                    prs = getDirectStream(fontDescriptor.get(PdfName.FONTFILE2));
                }
                if (prs == null) {
                    prs = getDirectStream(fontDescriptor.get(PdfName.FONTFILE3));
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
        if (obj1 instanceof PdfObject) {
            PdfDictionary gs = getDirectDictionary((PdfObject) obj1);
            PdfObject obj = gs.get(PdfName.BM);
            if (obj != null) {
                if (!allowedBlendModes.contains(obj))
                    throw new PdfAConformanceException(MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
                if (!PdfGState.BM_NORMAL.equals(obj))
                    transparencyDetectedOnThePage = true;
            }

            PdfNumber ca = gs.getAsNumber(PdfName.ca);
            if (ca != null && ca.floatValue() < 1f) {
                transparencyDetectedOnThePage = true;
            }

            ca = gs.getAsNumber(PdfName.CA);
            if (ca != null && ca.floatValue() < 1f) {
                transparencyDetectedOnThePage = true;
            }

            PdfDictionary smask = getDirectDictionary(gs.get(PdfName.SMASK));
            if (smask != null) {
                transparencyDetectedOnThePage = true;
            }

            if (gs.contains(PdfName.TR)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.extgstate.dictionary.shall.not.contain.the.tr.key"));
            }

            PdfName tr2 = gs.getAsName(PdfName.TR2);
            if (tr2 != null && !tr2.equals(PdfName.DEFAULT)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.extgstate.dictionary.shall.not.contain.the.TR2.key.with.a.value.other.than.default"));
            }

            if (gs.contains(PdfName.HTP)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.extgstate.dictionary.shall.not.contain.the.htp.key"));
            }

            PdfDictionary halfTone = getDirectDictionary(gs.get(PdfName.HT));
            if (halfTone != null) {
                if (halfTone.contains(PdfName.HALFTONENAME))
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("halftones.shall.not.contains.halftonename"));

                PdfNumber halftoneType = halfTone.getAsNumber(PdfName.HALFTONETYPE);
                if (halftoneType == null || (halftoneType.intValue() != 1 && halftoneType.intValue() != 5))
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("all.halftones.shall.have.halftonetype.1.or.5"));
            }
            PdfName ri = gs.getAsName(PdfName.RI);
            if (ri != null && !(PdfName.RELATIVECOLORIMETRIC.equals(ri) || PdfName.ABSOLUTECOLORIMETRIC.equals(ri) || PdfName.PERCEPTUAL.equals(ri) || PdfName.SATURATION.equals(ri))) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("1.value.of.ri.key.is.not.allowed", ri.toString()));
            }
        }
    }

    @Override
    protected void checkImage(PdfWriter writer, int key, Object obj1) {
        PdfImage pdfImage = (PdfImage) obj1;
        if (getDirectStream(pdfImage.get(PdfName.SMASK)) != null) {
            transparencyDetectedOnThePage = true;
        }
        PdfNumber smaskInData = pdfImage.getAsNumber(PdfName.SMASKINDATA);
        if (smaskInData != null && smaskInData.floatValue() > 0) {
            transparencyDetectedOnThePage = true;
        }
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
    protected void checkFormXObj(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfTemplate) {
            if (((PdfTemplate) obj1).getGroup() != null) {
                transparencyDetectedOnThePage = true;
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
            PdfDictionary d = getDirectDictionary(properties.get(PdfName.D));
            if (d != null)
                configsList.add(d);
            PdfArray configs = getDirectArray(properties.get(PdfName.CONFIGS));
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    PdfDictionary config = getDirectDictionary(configs.getPdfObject(i));
                    if (config != null)
                        configsList.add(config);
                }
            }
            HashSet<PdfObject> ocgs = new HashSet<PdfObject>();
            PdfArray ocgsArray = getDirectArray(properties.get(PdfName.OCGS));
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
                PdfArray orderArray = getDirectArray(config.get(PdfName.ORDER));
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
                    PdfDictionary decodeParams = getDirectDictionary(stream.get(PdfName.DECODEPARMS));
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
                        PdfArray decodeParams = getDirectArray(stream.get(PdfName.DECODEPARMS));
                        if (decodeParams != null && i < decodeParams.size()) {
                            PdfDictionary decodeParam = getDirectDictionary(decodeParams.getPdfObject(i));
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
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("a.form.xobject.dictionary.shall.not.contain.ps.key"));
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
            PdfDictionary fileSpec = (PdfFileSpecification) obj1;
            if (!fileSpec.contains(PdfName.UF) || !fileSpec.contains(PdfName.F) || !fileSpec.contains(PdfName.DESC)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("file.specification.dictionary.shall.contain.f.uf.and.desc.entries"));
            }

            if (fileSpec.contains(PdfName.EF)) {
                PdfDictionary dict = getDirectDictionary(fileSpec.get(PdfName.EF));
                if (dict == null || !dict.contains(PdfName.F)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("ef.key.of.file.specification.dictionary.shall.contain.dictionary.with.valid.f.key"));
                }

                PdfDictionary embeddedFile = getDirectDictionary(dict.get(PdfName.F));
                if (embeddedFile == null) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("ef.key.of.file.specification.dictionary.shall.contain.dictionary.with.valid.f.key"));
                }

                checkEmbeddedFile(embeddedFile);
            }
        }
    }

    private static PdfName MimeTypePdf = new PdfName(PdfAWriter.MimeTypePdf);

    protected void checkEmbeddedFile(PdfDictionary embeddedFile) {
        PdfName subtype = embeddedFile.getAsName(PdfName.SUBTYPE);
        if (subtype == null || !MimeTypePdf.equals(subtype)) {
            throw new PdfAConformanceException(embeddedFile, MessageLocalization.getComposedMessage("embedded.file.shall.contain.pdf.mime.type"));
        }
    }

    @Override
    protected void checkPdfObject(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfNumber) {
            PdfNumber number = (PdfNumber) obj1;
            if (Math.abs(number.doubleValue()) > Float.MAX_VALUE && number.toString().contains(".")) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("real.number.is.out.of.range"));
            }
        } else if (obj1 instanceof PdfString) {
            PdfString string = (PdfString) obj1;
            if (string.getBytes().length > maxStringLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.string.is.too.long"));
            }
        } else if (obj1 instanceof PdfDictionary) {
            PdfDictionary dictionary = (PdfDictionary) obj1;
            PdfName type = dictionary.getAsName(PdfName.TYPE);
            if (PdfName.CATALOG.equals(type)) {
                if (!dictionary.contains(PdfName.METADATA)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.contain.metadata"));
                }
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
                    PdfDictionary acroForm = getDirectDictionary(dictionary.get(PdfName.ACROFORM));
                    if (acroForm != null && acroForm.contains(PdfName.XFA)) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.acroform.xfa.entry"));
                    }
                }

                if (dictionary.contains(PdfName.NAMES)) {
                    PdfDictionary names = getDirectDictionary(dictionary.get(PdfName.NAMES));
                    if (names != null && names.contains(PdfName.ALTERNATEPRESENTATION)) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.alternatepresentation.names.entry"));
                    }
                }


                PdfDictionary permissions = getDirectDictionary(dictionary.get(PdfName.PERMS));
                if (permissions != null) {
                    for (PdfName dictKey : permissions.getKeys()) {
                        if (PdfName.DOCMDP.equals(dictKey)) {
                            PdfDictionary signatureDict = getDirectDictionary(permissions.get(PdfName.DOCMDP));
                            if (signatureDict != null) {
                                PdfArray references = getDirectArray(signatureDict.get(PdfName.REFERENCE));
                                if (references != null) {
                                    for (int i = 0; i < references.size(); i++) {
                                        PdfDictionary referenceDict = getDirectDictionary(references.getPdfObject(i));
                                        if (referenceDict.contains(DIGESTLOCATION)
                                                || referenceDict.contains(DIGESTMETHOD)
                                                || referenceDict.contains(DIGESTVALUE)) {
                                            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("signature.references.dictionary.shall.not.contain.digestlocation.digestmethod.digestvalue"));
                                        }
                                    }
                                }
                            }
                        } else if (PdfName.UR3.equals(dictKey)) {
                        } else {
                            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("no.keys.other.than.UR3.and.DocMDP.shall.be.present.in.a.permissions.dictionary"));
                        }
                    }
                }

                if (checkStructure(conformanceLevel)) {
                    PdfDictionary markInfo = getDirectDictionary(dictionary.get(PdfName.MARKINFO));
                    if (markInfo == null || markInfo.getAsBoolean(PdfName.MARKED) == null || markInfo.getAsBoolean(PdfName.MARKED).booleanValue() == false) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("document.catalog.dictionary.shall.include.a.markinfo.dictionary.whose.entry.marked.shall.have.a.value.of.true"));
                    }
                    if (!dictionary.contains(PdfName.LANG)) {
                        LOGGER.warning(MessageLocalization.getComposedMessage("document.catalog.dictionary.should.contain.lang.entry"));
                    }
                }
            } else if (PdfName.PAGE.equals(type)) {
                PdfName[] boxNames = new PdfName[]{PdfName.MEDIABOX, PdfName.CROPBOX, PdfName.TRIMBOX, PdfName.ARTBOX, PdfName.BLEEDBOX};
                for (PdfName boxName : boxNames) {
                    PdfObject box = dictionary.getDirectObject(boxName);
                    if (box instanceof PdfRectangle) {
                        float width = ((PdfRectangle) box).width();
                        float height = ((PdfRectangle) box).height();
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

                if (transparencyDetectedOnThePage) {
                    PdfDictionary group = getDirectDictionary(dictionary.get(PdfName.GROUP));
                    if (group == null || !PdfName.TRANSPARENCY.equals(group.getAsName(PdfName.S)) || !group.contains(PdfName.CS)) {
                        transparencyWithoutPageGroupDetected = true;
                    } else {
                        PdfName csName = group.getAsName(PdfName.CS);
                        if (PdfName.DEVICERGB.equals(csName))
                            rgbUsed = true;
                        if (PdfName.DEVICEGRAY.equals(csName))
                            grayUsed = true;
                        if (PdfName.DEVICECMYK.equals(csName))
                            cmykUsed = true;
                    }
                }
                transparencyDetectedOnThePage = false;

            } else if (PdfName.OUTPUTINTENT.equals(type)) {
                isCheckOutputIntent = true;
                PdfObject destOutputIntent = dictionary.get(PdfName.DESTOUTPUTPROFILE);
                if (destOutputIntent != null && pdfaDestOutputIntent != null) {
                    if (pdfaDestOutputIntent.getIndRef() != destOutputIntent.getIndRef())
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("if.outputintents.array.more.than.one.entry.the.same.indirect.object"));
                } else {
                    pdfaDestOutputIntent = destOutputIntent;
                }

                PdfName gts = dictionary.getAsName(PdfName.S);
                if (pdfaDestOutputIntent != null) {
                    if (PdfName.GTS_PDFA1.equals(gts)) {
                        if (pdfaOutputIntentColorSpace != null)
                            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("a.pdfa.file.may.have.only.one.pdfa.outputintent"));
                        pdfaOutputIntentColorSpace = "";
                    }

                    String deviceClass = "";
                    ICC_Profile icc_profile = writer.getColorProfile();
                    try {
                        if (PdfName.GTS_PDFA1.equals(gts))
                            pdfaOutputIntentColorSpace = new String(icc_profile.getData(), 16, 4, "US-ASCII");
                        deviceClass = new String(icc_profile.getData(), 12, 4, "US-ASCII");
                    } catch (UnsupportedEncodingException e) {
                        throw new ExceptionConverter(e);
                    }
                    if (!"prtr".equals(deviceClass) && !"mntr".equals(deviceClass))
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("outputintent.shall.be.prtr.or.mntr"));

                } else {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("outputintent.shall.have.gtspdfa1.and.destoutputintent"));
                }
            } else if (PdfName.EMBEDDEDFILE.equals(type)) {
                checkEmbeddedFile(dictionary);
            }
            PdfObject obj2 = dictionary.get(PdfName.HALFTONETYPE);
            if (obj2 != null && obj2.isNumber()) {
                PdfNumber number = (PdfNumber) obj2;
                if (number.intValue() != 1 || number.intValue() != 5) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.extgstate.dictionary.shall.contain.the.halftonetype.key.of.value.1.or.5"));
                }

                if (dictionary.contains(PdfName.HALFTONENAME)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.extgstate.dictionary.shall.not.contain.the.halftonename.key"));
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
        switch (key) {
            case PdfIsoKeys.PDFISOKEY_COLOR:
                if (obj1 instanceof ExtendedColor) {
                    ExtendedColor ec = (ExtendedColor) obj1;
                    switch (ec.getType()) {
                        case ExtendedColor.TYPE_CMYK:
                            checkColor(writer, PdfIsoKeys.PDFISOKEY_CMYK, obj1);
                            break;
                        case ExtendedColor.TYPE_GRAY:
                            checkColor(writer, PdfIsoKeys.PDFISOKEY_GRAY, obj1);
                            return;
                        case ExtendedColor.TYPE_RGB:
                            checkColor(writer, PdfIsoKeys.PDFISOKEY_RGB, obj1);
                            break;
                        case ExtendedColor.TYPE_SEPARATION:
                            SpotColor sc = (SpotColor) ec;
                            checkColor(writer, PdfIsoKeys.PDFISOKEY_COLOR, sc.getPdfSpotColor().getAlternativeCS());
                            break;
                        case ExtendedColor.TYPE_SHADING:
                            ShadingColor xc = (ShadingColor) ec;
                            checkColor(writer, PdfIsoKeys.PDFISOKEY_COLOR, xc.getPdfShadingPattern().getShading().getColorSpace());
                            break;
                        case ExtendedColor.TYPE_PATTERN:
                            PatternColor pc = (PatternColor) ec;
                            checkColor(writer, PdfIsoKeys.PDFISOKEY_COLOR, pc.getPainter().getDefaultColor());
                            break;
                    }
                } else if (obj1 instanceof BaseColor)
                    checkColor(writer, PdfIsoKeys.PDFISOKEY_RGB, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_CMYK:
                cmykUsed = true;
                break;
            case PdfIsoKeys.PDFISOKEY_RGB:
                rgbUsed = true;
                break;
            case PdfIsoKeys.PDFISOKEY_GRAY:
                grayUsed = true;
                break;
        }
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
            if (subtype == null) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("annotation.type.1.not.allowed", "null"));
            }
            if (subtype != null && !allowedAnnotTypes.contains(subtype)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("annotation.type.1.not.allowed", subtype.toString()));
            }

            if (!PdfName.POPUP.equals(annot.getAsName(PdfName.SUBTYPE))) {
                PdfNumber f = annot.getAsNumber(PdfName.F);
                if (f == null) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.annotation.dictionary.shall.contain.the.f.key"));
                }
                int flags = f.intValue();
                if (checkFlag(flags, PdfAnnotation.FLAGS_PRINT) == false
                        || checkFlag(flags, PdfAnnotation.FLAGS_HIDDEN) == true
                        || checkFlag(flags, PdfAnnotation.FLAGS_INVISIBLE) == true
                        || checkFlag(flags, PdfAnnotation.FLAGS_NOVIEW) == true
                        || checkFlag(flags, PdfAnnotation.FLAGS_TOGGLENOVIEW) == true) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.f.keys.print.flag.bit.shall.be.set.to.1.and.its.hidden.invisible.noview.and.togglenoview.flag.bits.shall.be.set.to.0"));
                }
                if (PdfName.TEXT.equals(annot.getAsName(PdfName.SUBTYPE))) {

                    if (checkFlag(flags, PdfAnnotation.FLAGS_NOZOOM) == false || checkFlag(flags, PdfAnnotation.FLAGS_NOROTATE) == false) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("text.annotations.should.set.the.nozoom.and.norotate.flag.bits.of.the.f.key.to.1"));
                    }
                }
            }

            if (PdfName.WIDGET.equals(annot.getAsName(PdfName.SUBTYPE)) && (annot.contains(PdfName.AA) || annot.contains(PdfName.A))) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("widget.annotation.dictionary.or.field.dictionary.shall.not.include.a.or.aa.entry"));
            }
            if (checkStructure(conformanceLevel)) {
                if (contentAnnotations.contains(subtype) && !annot.contains(PdfName.CONTENTS)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("annotation.of.type.1.should.have.contents.key", subtype.toString()));
                }
            }

            PdfDictionary ap = getDirectDictionary(annot.get(PdfName.AP));
            if (ap != null) {
                if (ap.contains(PdfName.R) || ap.contains(PdfName.D)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("appearance.dictionary.shall.contain.only.the.n.key.with.stream.value"));
                }
                PdfObject n = getDirectObject(ap.get(PdfName.N));
                if (PdfName.WIDGET.equals(annot.getAsName(PdfName.SUBTYPE)) && new PdfName("Btn").equals(annot.getAsName(PdfName.FT))) {
                    if (n == null || (!n.isDictionary() && n.type() != 0))
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("appearance.dictionary.of.widget.subtype.and.btn.field.type.shall.contain.only.the.n.key.with.dictionary.value"));
                } else {
                    if (n == null || (!n.isStream() && n.type() != 0))
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("appearance.dictionary.shall.contain.only.the.n.key.with.stream.value"));
                }
            } else {
                boolean isCorrectRect = false;
                PdfArray rect = getDirectArray(annot.get(PdfName.RECT));
                if (rect != null && rect.size() == 4) {
                    PdfNumber index0 = rect.getAsNumber(0);
                    PdfNumber index1 = rect.getAsNumber(1);
                    PdfNumber index2 = rect.getAsNumber(2);
                    PdfNumber index3 = rect.getAsNumber(3);
                    if (index0 != null && index1 != null && index2 != null && index3 != null &&
                            index0.doubleValue() == index2.doubleValue() && index1.doubleValue() == index3.doubleValue())
                        isCorrectRect = true;
                }
                if (!PdfName.POPUP.equals(annot.getAsName(PdfName.SUBTYPE)) &&
                        !PdfName.LINK.equals(annot.getAsName(PdfName.SUBTYPE)) &&
                        !isCorrectRect)
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("every.annotation.shall.have.at.least.one.appearance.dictionary"));
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

    @Override
    protected void checkOutputIntent(PdfWriter writer, int key, Object obj1) {
        if (writer instanceof PdfAStamperImp && writer.getColorProfile() != null)
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("outputintent.shall.not.be.updated"));
    }

    private void fillOrderRecursively(PdfArray orderArray, HashSet<PdfObject> order) {
        for (int i = 0; i < orderArray.size(); i++) {
            PdfArray orderChild = getDirectArray(orderArray.getPdfObject(i));
            if (orderChild == null) {
                order.add(orderArray.getPdfObject(i));
            } else {
                fillOrderRecursively(orderChild, order);
            }
        }
    }

    @Override
    public void close(PdfWriter writer) {
        checkOutputIntentsInStamperMode(writer);
        if (pdfaOutputIntentColorSpace != null) {
            if ("RGB ".equals(pdfaOutputIntentColorSpace)) {
                if (cmykUsed && writer.getDefaultColorspace().get(PdfName.DEFAULTCMYK) == null)
                    throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicecmyk.shall.only.be.used.if.defaultcmyk.pdfa.or.outputintent"));
            } else if ("CMYK".equals(pdfaOutputIntentColorSpace)) {
                if (rgbUsed && writer.getDefaultColorspace().get(PdfName.DEFAULTRGB) == null)
                    throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicergb.shall.only.be.used.if.defaultrgb.pdfa.or.outputintent"));
            } else if ("GRAY".equals(pdfaOutputIntentColorSpace)) {
                if (rgbUsed && writer.getDefaultColorspace().get(PdfName.DEFAULTRGB) == null)
                    throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicergb.shall.only.be.used.if.defaultrgb.pdfa.or.outputintent"));
                if (cmykUsed && writer.getDefaultColorspace().get(PdfName.DEFAULTCMYK) == null)
                    throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicecmyk.shall.only.be.used.if.defaultcmyk.pdfa.or.outputintent"));
            } else {
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("outputintent.shall.have.colourspace.gray.rgb.or.cmyk"));
            }
        } else {
            if (rgbUsed && writer.getDefaultColorspace().get(PdfName.DEFAULTRGB) == null) {
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicergb.shall.only.be.used.if.defaultrgb.pdfa.or.outputintent"));
            }
            if (cmykUsed && writer.getDefaultColorspace().get(PdfName.DEFAULTCMYK) == null) {
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicecmyk.shall.only.be.used.if.defaultcmyk.pdfa.or.outputintent"));
            }
            if (grayUsed && writer.getDefaultColorspace().get(PdfName.DEFAULTGRAY) == null) {
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicegray.shall.only.be.used.if.defaultgray.pdfa.or.outputintent"));
            }
            if (transparencyWithoutPageGroupDetected) {
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("if.the.document.not.contain.outputintent.transparencygroup.shall.comtain.cs.key"));
            }
        }
    }
}
