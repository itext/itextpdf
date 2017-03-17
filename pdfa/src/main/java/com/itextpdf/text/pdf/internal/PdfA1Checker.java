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
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;

public class PdfA1Checker extends PdfAChecker {

    static public final PdfName setState = new PdfName("SetState");
    static public final PdfName noOp = new PdfName("NoOp");

    static private HashSet<PdfName> allowedAnnotTypes = new HashSet<PdfName>(Arrays.asList(PdfName.TEXT, PdfName.LINK,
            PdfName.FREETEXT, PdfName.LINE, PdfName.SQUARE, PdfName.CIRCLE, PdfName.HIGHLIGHT, PdfName.UNDERLINE,
            PdfName.SQUIGGLY, PdfName.STRIKEOUT, PdfName.STAMP, PdfName.INK, PdfName.POPUP, PdfName.WIDGET,
            PdfName.PRINTERMARK, PdfName.TRAPNET));

    static public final HashSet<PdfName> allowedNamedActions = new HashSet<PdfName>(Arrays.asList(PdfName.NEXTPAGE,
            PdfName.PREVPAGE, PdfName.FIRSTPAGE, PdfName.LASTPAGE));

    static private HashSet<PdfName> restrictedActions = new HashSet<PdfName>(Arrays.asList(PdfName.LAUNCH, PdfName.SOUND,
            PdfName.MOVIE, PdfName.RESETFORM, PdfName.IMPORTDATA, PdfName.JAVASCRIPT));

    static public final HashSet<PdfName> contentAnnotations = new HashSet<PdfName>(Arrays.asList(PdfName.TEXT,
            PdfName.FREETEXT, PdfName.LINE, PdfName.SQUARE, PdfName.CIRCLE, PdfName.STAMP, PdfName.INK, PdfName.POPUP));

    static private final HashSet<PdfName> keysForCheck = new HashSet<PdfName>(Arrays.asList(PdfName.FONTDESCRIPTOR,
            PdfName.FONTFILE, PdfName.FONTFILE2, PdfName.FONTFILE3, PdfName.FILTER, PdfName.GROUP, PdfName.S,
            PdfName.EMBEDDEDFILES, PdfName.MARKED, PdfName.DESTOUTPUTPROFILE));

    static public final double maxRealValue = 32767;
    static public final int maxStringLength = 65535;
    static public final int maxArrayLength = 8191;
    static public final int maxDictionaryLength = 4095;
    static public final int maxGsStackDepth = 28;
    protected int gsStackDepth = 0;
    protected boolean rgbUsed = false;
    protected boolean cmykUsed = false;
    protected boolean grayUsed = false;

    PdfA1Checker(PdfAConformanceLevel conformanceLevel) {
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
    protected void checkImage(PdfWriter writer, int key, Object obj1) {
        PdfImage image = (PdfImage) obj1;
        if (image.get(PdfName.SMASK) != null && !PdfName.NONE.equals(image.getAsName(PdfName.SMASK)))
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.images"));
        if (image.contains(PdfName.ALTERNATES)) {
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.image.dictionary.shall.not.contain.alternates.key"));
        }
        if (image.contains(PdfName.OPI)) {
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.image.dictionary.shall.not.contain.opi.key"));
        }
        PdfBoolean interpolate = image.getAsBoolean(PdfName.INTERPOLATE);
        if (interpolate != null && interpolate.booleanValue()) {
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.value.of.interpolate.key.shall.not.be.true"));
        }
        PdfName intent = image.getAsName(PdfName.INTENT);
        if (intent != null && !(PdfName.RELATIVECOLORIMETRIC.equals(intent) || PdfName.ABSOLUTECOLORIMETRIC.equals(intent) || PdfName.PERCEPTUAL.equals(intent) || PdfName.SATURATION.equals(intent))) {
            throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("1.value.of.intent.key.is.not.allowed", intent.toString()));
        }
    }

    @Override
    protected void checkFormXObj(PdfWriter writer, int key, Object obj1) {
        //For PDF/A-1 it is enough to check a form xObject at the addToBody... only.
    }

    @Override
    protected void checkInlineImage(PdfWriter writer, int key, Object obj1) {}

    @Override
    protected void checkGState(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfDictionary) {
            PdfDictionary gs = (PdfDictionary) obj1;
            PdfObject obj = gs.get(PdfName.BM);
            if (obj != null && !PdfGState.BM_NORMAL.equals(obj) && !PdfGState.BM_COMPATIBLE.equals(obj))
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("blend.mode.1.not.allowed", obj.toString()));
            obj = gs.get(PdfName.CA);
            double v = 0.0;
            if (obj != null && (v = ((PdfNumber) obj).doubleValue()) != 1.0)
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));
            obj = gs.get(PdfName.ca);
            v = 0.0;
            if (obj != null && (v = ((PdfNumber) obj).doubleValue()) != 1.0)
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("transparency.is.not.allowed.ca.eq.1", String.valueOf(v)));

            if (gs.contains(PdfName.TR)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.extgstate.dictionary.shall.not.contain.the.tr.key"));
            }
            PdfName tr2 = gs.getAsName(PdfName.TR2);
            if (tr2 != null && !tr2.equals(PdfName.DEFAULT)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.extgstate.dictionary.shall.not.contain.the.TR2.key.with.a.value.other.than.default"));
            }
            PdfName ri = gs.getAsName(PdfName.RI);
            if (ri != null && !(PdfName.RELATIVECOLORIMETRIC.equals(ri) || PdfName.ABSOLUTECOLORIMETRIC.equals(ri) || PdfName.PERCEPTUAL.equals(ri) || PdfName.SATURATION.equals(ri))) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("1.value.of.ri.key.is.not.allowed", ri.toString()));
            }
            if (gs.get(PdfName.SMASK) != null && !PdfName.NONE.equals(gs.getAsName(PdfName.SMASK)))
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.smask.key.is.not.allowed.in.extgstate"));
        }
    }

    @Override
    protected void checkLayer(PdfWriter writer, int key, Object obj1) {
        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("layers.are.not.allowed"));
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

            PdfObject filter = getDirectObject(stream.get(PdfName.FILTER));
            if (filter instanceof PdfName) {
                if (filter.equals(PdfName.LZWDECODE))
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("lzwdecode.filter.is.not.permitted"));
            } else if (filter instanceof PdfArray) {
                for (PdfObject f : ((PdfArray) filter)) {
                    if (f.equals(PdfName.LZWDECODE))
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("lzwdecode.filter.is.not.permitted"));
                }
            }

            if (PdfName.FORM.equals(stream.getAsName(PdfName.SUBTYPE))) {
                if (stream.contains(PdfName.OPI)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("a.form.xobject.dictionary.shall.not.contain.opi.key"));
                }
                if (stream.contains(PdfName.PS)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("a.form.xobject.dictionary.shall.not.contain.ps.key"));
                }
                PdfDictionary group = getDirectDictionary(stream.get(PdfName.GROUP));
                if (group != null) {
                    PdfName s = group.getAsName(PdfName.S);
                    if (PdfName.TRANSPARENCY.equals(s)) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("a.group.object.with.an.s.key.with.a.value.of.transparency.shall.not.be.included.in.a.form.xobject"));
                    }
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
            PdfFileSpecification fileSpec = (PdfFileSpecification) obj1;
            if (fileSpec.contains(PdfName.EF)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("embedded.files.are.not.permitted"));
            }
        }
    }

    @Override
    protected void checkPdfObject(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfNumber) {
            PdfNumber number = (PdfNumber) obj1;
            if (Math.abs(number.doubleValue()) > maxRealValue && number.toString().contains(".")) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("real.number.is.out.of.range"));
            }
        } else if (obj1 instanceof PdfString) {
            PdfString string = (PdfString) obj1;
            if (string.getBytes().length > maxStringLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.string.is.too.long"));
            }
        } else if (obj1 instanceof PdfArray) {
            PdfArray array = (PdfArray) obj1;
            if (array.size() > maxArrayLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.array.exceeds.length.set.by.PDFA1.standard", Integer.toString(array.size())));
            }
        } else if (obj1 instanceof PdfDictionary) {
            PdfDictionary dictionary = (PdfDictionary) obj1;
            if (dictionary.size() > maxDictionaryLength) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("pdf.dictionary.is.out.of.bounds"));
            }
            PdfName type = dictionary.getAsName(PdfName.TYPE);
            if (PdfName.CATALOG.equals(type)) {
                if (!dictionary.contains(PdfName.METADATA)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.contain.metadata"));
                }

                if (dictionary.contains(PdfName.AA)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.an.aa.entry"));
                }

                if (dictionary.contains(PdfName.NAMES)) {
                    PdfDictionary names = getDirectDictionary(dictionary.get(PdfName.NAMES));
                    if (names != null && names.contains(PdfName.EMBEDDEDFILES)) {
                        throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.document.catalog.dictionary.shall.not.include.embeddedfiles.names.entry"));
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
                if (dictionary.contains(PdfName.AA)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("page.dictionary.shall.not.include.aa.entry"));
                }
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
                        ICC_Profile icc_profile = writer.getColorProfile();
                        try {
                            pdfaOutputIntentColorSpace = new String(icc_profile.getData(), 16, 4, "US-ASCII");
                        } catch (UnsupportedEncodingException e) {
                            throw new ExceptionConverter(e);
                        }
                    }
                } else {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("outputintent.shall.have.gtspdfa1.and.destoutputintent"));
                }
            }
        }
    }

    @Override
    protected void checkCanvas(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof String) {
            if ("q".equals(obj1)) {
                if (++gsStackDepth > maxGsStackDepth)
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
                if (rgbUsed) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("devicergb.and.devicecmyk.colorspaces.cannot.be.used.both.in.one.file"));
                }
                cmykUsed = true;
                break;
            case PdfIsoKeys.PDFISOKEY_RGB:
                if (cmykUsed) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("devicergb.and.devicecmyk.colorspaces.cannot.be.used.both.in.one.file"));
                }
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
            PdfNumber ca = annot.getAsNumber(PdfName.CA);
            if (ca != null && ca.floatValue() != 1.0) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.annotation.dictionary.shall.not.contain.the.ca.key.with.a.value.other.than.1"));
            }
            PdfNumber f = annot.getAsNumber(PdfName.F);
            if (f == null) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("an.annotation.dictionary.shall.contain.the.f.key"));
            }
            int flags = f.intValue();
            if (checkFlag(flags, PdfAnnotation.FLAGS_PRINT) == false || checkFlag(flags, PdfAnnotation.FLAGS_HIDDEN) == true ||
                    checkFlag(flags, PdfAnnotation.FLAGS_INVISIBLE) == true || checkFlag(flags, PdfAnnotation.FLAGS_NOVIEW) == true) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("the.f.keys.print.flag.bit.shall.be.set.to.1.and.its.hidden.invisible.and.noview.flag.bits.shall.be.set.to.0"));
            }
            if (PdfName.TEXT.equals(annot.getAsName(PdfName.SUBTYPE))) {
                if (checkFlag(flags, PdfAnnotation.FLAGS_NOZOOM) == false || checkFlag(flags, PdfAnnotation.FLAGS_NOROTATE) == false) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("text.annotations.should.set.the.nozoom.and.norotate.flag.bits.of.the.f.key.to.1"));
                }
            }
            if (annot.contains(PdfName.C) || annot.contains(PdfName.IC)) {
                ICC_Profile colorProfile = ((PdfAWriter) writer).getColorProfile();
                String cs = "";
                try {
                    cs = new String(colorProfile.getData(), 16, 4, "US-ASCII");
                } catch (UnsupportedEncodingException e) {
                    throw new ExceptionConverter(e);
                }
                if (!"RGB ".equalsIgnoreCase(cs)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("destoutputprofile.in.the.pdfa1.outputintent.dictionary.shall.be.rgb"));
                }
            }
            PdfDictionary ap = getDirectDictionary(annot.get(PdfName.AP));
            if (ap != null) {
                if (ap.contains(PdfName.R) || ap.contains(PdfName.D)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("appearance.dictionary.shall.contain.only.the.n.key.with.stream.value"));
                }
                PdfObject n = ap.get(PdfName.N);
                if (!(n instanceof PdfIndirectReference)) {
                    throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("appearance.dictionary.shall.contain.only.the.n.key.with.stream.value"));
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
        }
    }

    @Override
    protected void checkAction(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfAction) {
            PdfAction action = (PdfAction) obj1;
            PdfName s = action.getAsName(PdfName.S);
            if (setState.equals(s) || noOp.equals(s)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("deprecated.setstate.and.noop.actions.are.not.allowed"));
            }
            if (restrictedActions.contains(s)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("launch.sound.movie.resetform.importdata.and.javascript.actions.are.not.allowed"));
            }
            if (PdfName.NAMED.equals(s)) {
                PdfName n = action.getAsName(PdfName.N);
                if (n != null && !allowedNamedActions.contains(n)) {
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

    @Override
    public void close(PdfWriter writer) {
        checkOutputIntentsInStamperMode(writer);
        if ((rgbUsed || cmykUsed || grayUsed) && pdfaOutputIntentColorSpace == null) {
            throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("if.device.rgb.cmyk.gray.used.in.file.that.file.shall.contain.pdfa.outputintent"));
        }

        if ("RGB ".equals(pdfaOutputIntentColorSpace)) {
            if (cmykUsed)
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicecmyk.may.be.used.only.if.the.file.has.a.cmyk.pdfa.outputIntent"));
        } else if ("CMYK".equals(pdfaOutputIntentColorSpace)) {
            if (rgbUsed)
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicergb.may.be.used.only.if.the.file.has.a.rgb.pdfa.outputIntent"));
        } else {
            if (cmykUsed)
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicecmyk.may.be.used.only.if.the.file.has.a.cmyk.pdfa.outputIntent"));
            if (rgbUsed)
                throw new PdfAConformanceException(null, MessageLocalization.getComposedMessage("devicergb.may.be.used.only.if.the.file.has.a.rgb.pdfa.outputIntent"));
        }
    }
}
