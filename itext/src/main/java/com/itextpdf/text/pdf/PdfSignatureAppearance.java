/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Version;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.RASInputStream;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.interfaces.PdfVersion;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.CertificateInfo.X500Name;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that takes care of the cryptographic options
 * and appearances that form a signature.
 */
public class PdfSignatureAppearance {

	/**
	 * Constructs a PdfSignatureAppearance object.
     * @param writer	the writer to which the signature will be written.
     */
    PdfSignatureAppearance(PdfStamperImp writer) {
        this.writer = writer;
        signDate = new GregorianCalendar();
        fieldName = getNewSigName();
        signatureCreator = Version.getInstance().getVersion();
    }

	/*
	 * SIGNATURE
	 */

    // signature types

    /** Approval signature */
    public static final int NOT_CERTIFIED = 0;

    /** Author signature, no changes allowed */
    public static final int CERTIFIED_NO_CHANGES_ALLOWED = 1;

    /** Author signature, form filling allowed */
    public static final int CERTIFIED_FORM_FILLING = 2;

    /** Author signature, form filling and annotations allowed */
    public static final int CERTIFIED_FORM_FILLING_AND_ANNOTATIONS = 3;

    /** The certification level */
    private int certificationLevel = NOT_CERTIFIED;

    /**
     * Sets the document type to certified instead of simply signed.
     * @param certificationLevel the values can be: <code>NOT_CERTIFIED</code>, <code>CERTIFIED_NO_CHANGES_ALLOWED</code>,
     * <code>CERTIFIED_FORM_FILLING</code> and <code>CERTIFIED_FORM_FILLING_AND_ANNOTATIONS</code>
     */
    public void setCertificationLevel(int certificationLevel) {
        this.certificationLevel = certificationLevel;
    }

    /**
     * Gets the certified status of this document.
     * @return the certified status
     */
    public int getCertificationLevel() {
        return this.certificationLevel;
    }

    // signature info

    /** The caption for the reason for signing. */
    private String reasonCaption = "Reason: ";

    /** The caption for the location of signing. */
    private String locationCaption = "Location: ";

    /** The reason for signing. */
    private String reason;

    /** Holds value of property location. */
    private String location;

    /** Holds value of property signDate. */
    private Calendar signDate;

    /**
     * Gets the signing reason.
     * @return the signing reason
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Sets the signing reason.
     * @param reason the signing reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Sets the caption for signing reason.
     * @param reasonCaption the signing reason caption
     */
    public void setReasonCaption(String reasonCaption) {
        this.reasonCaption = reasonCaption;
    }

    /**
     * Gets the signing location.
     * @return the signing location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * Sets the signing location.
     * @param location the signing location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the caption for the signing location.
     * @param locationCaption the signing location caption
     */
    public void setLocationCaption(String locationCaption) {
        this.locationCaption = locationCaption;
    }

    /** Holds value of the application that creates the signature */
    private String signatureCreator;

    /**
     * Gets the signature creator.
     * @return the signature creator
     */
    public String getSignatureCreator(){
    	return signatureCreator;
    }

    /**
     * Sets the name of the application used to create the signature.
     * @param signatureCreator the name of the signature creating application
     */
    public void setSignatureCreator(String signatureCreator){
    	this.signatureCreator = signatureCreator;
    }

    /** The contact name of the signer. */
    private String contact;

    /**
     * Gets the signing contact.
     * @return the signing contact
     */
    public String getContact() {
        return this.contact;
    }

    /**
     * Sets the signing contact.
     * @param contact the signing contact
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * Gets the signature date.
     * @return the signature date
     */
    public java.util.Calendar getSignDate() {
        return signDate;
    }

    /**
     * Sets the signature date.
     * @param signDate the signature date
     */
    public void setSignDate(java.util.Calendar signDate) {
        this.signDate = signDate;
    }

    // the PDF file

    /** The file right before the signature is added (can be null). */
    private RandomAccessFile raf;
    /** The bytes of the file right before the signature is added (if raf is null) */
    private byte[] bout;
    /** Array containing the byte positions of the bytes that need to be hashed. */
    private long[] range;

    /**
     * Gets the document bytes that are hashable when using external signatures. The general sequence is:
     * preClose(), getRangeStream() and close().
     * <p>
     * @return the document bytes that are hashable
     */
    public InputStream getRangeStream() throws IOException {
    	RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
        return new RASInputStream(fac.createRanged(getUnderlyingSource(), range));
    }

    /**
     * @return the underlying source
     * @throws IOException
     */
    private RandomAccessSource getUnderlyingSource() throws IOException {
    	//TODO: get rid of separate byte[] and RandomAccessFile objects and just store a RandomAccessSource
    	RandomAccessSourceFactory fac = new RandomAccessSourceFactory();
    	return raf == null ? fac.createSource(bout) : fac.createSource(raf);
    }

    /** The signing certificate */
    private Certificate signCertificate;

    // Developer extenstion

    /**
     * Adds the appropriate developer extension.
     */
	public void addDeveloperExtension(final PdfDeveloperExtension de) {
		writer.addDeveloperExtension(de);
	}

    // Crypto dictionary

    /** The crypto dictionary */
    private PdfDictionary cryptoDictionary;
    /**
     * Gets the user made signature dictionary. This is the dictionary at the /V key.
     * @return the user made signature dictionary
     */
    public com.itextpdf.text.pdf.PdfDictionary getCryptoDictionary() {
        return cryptoDictionary;
    }

    /**
     * Sets a user made signature dictionary. This is the dictionary at the /V key.
     * @param cryptoDictionary a user made signature dictionary
     */
    public void setCryptoDictionary(com.itextpdf.text.pdf.PdfDictionary cryptoDictionary) {
        this.cryptoDictionary = cryptoDictionary;
    }

    /**
     * Sets the certificate used to provide the text in the appearance.
     * This certificate doesn't take part in the actual signing process.
     * @param signCertificate the certificate
     */
    public void setCertificate(Certificate signCertificate) {
        this.signCertificate = signCertificate;
    }

    public Certificate getCertificate() {
        return signCertificate;
    }

    // Signature event

    /**
     * An interface to retrieve the signature dictionary for modification.
     */
    public interface SignatureEvent {
        /**
         * Allows modification of the signature dictionary.
         * @param sig the signature dictionary
         */
        public void getSignatureDictionary(PdfDictionary sig);
    }

    /**
     * Holds value of property signatureEvent.
     */
    private SignatureEvent signatureEvent;

    /**
     * Getter for property signatureEvent.
     * @return Value of property signatureEvent.
     */
    public SignatureEvent getSignatureEvent() {
        return this.signatureEvent;
    }

    /**
     * Sets the signature event to allow modification of the signature dictionary.
     * @param signatureEvent the signature event
     */
    public void setSignatureEvent(SignatureEvent signatureEvent) {
        this.signatureEvent = signatureEvent;
    }

	/*
	 * SIGNATURE FIELD
	 */

    /** The name of the field */
    private String fieldName;

    /**
     * Gets the field name.
     * @return the field name
     */
    public java.lang.String getFieldName() {
        return fieldName;
    }

    /**
     * Gets a new signature field name that
     * doesn't clash with any existing name.
     * @return a new signature field name
     */
    public String getNewSigName() {
        AcroFields af = writer.getAcroFields();
        String name = "Signature";
        int step = 0;
        boolean found = false;
        while (!found) {
            ++step;
            String n1 = name + step;
            if (af.getFieldItem(n1) != null)
                continue;
            n1 += ".";
            found = true;
            for (Object element : af.getFields().keySet()) {
                String fn = (String)element;
                if (fn.startsWith(n1)) {
                    found = false;
                    break;
                }
            }
        }
        name += step;
        return name;
    }

    /**
     * The page where the signature will appear.
     */
    private int page = 1;

    /**
     * Gets the page number of the field.
     * @return the page number of the field
     */
    public int getPage() {
        return page;
    }

    /**
     * The coordinates of the rectangle for a visible signature,
     * or a zero-width, zero-height rectangle for an invisible signature.
     */
    private Rectangle rect;

    /**
     * Gets the rectangle representing the signature dimensions.
     * @return the rectangle representing the signature dimensions. It may be <CODE>null</CODE>
     * or have zero width or height for invisible signatures
     */
    public Rectangle getRect() {
        return rect;
    }

    /** rectangle that represent the position and dimension of the signature in the page. */
    private Rectangle pageRect;

    /**
     * Gets the rectangle that represent the position and dimension of the signature in the page.
     * @return the rectangle that represent the position and dimension of the signature in the page
     */
    public Rectangle getPageRect() {
        return pageRect;
    }

    /**
     * Gets the visibility status of the signature.
     * @return the visibility status of the signature
     */
    public boolean isInvisible() {
        return rect == null || rect.getWidth() == 0 || rect.getHeight() == 0;
    }

    /**
     * Sets the signature to be visible. It creates a new visible signature field.
     * @param pageRect the position and dimension of the field in the page
     * @param page the page to place the field. The fist page is 1
     * @param fieldName the field name or <CODE>null</CODE> to generate automatically a new field name
     */
    public void setVisibleSignature(Rectangle pageRect, int page, String fieldName) {
        if (fieldName != null) {
            if (fieldName.indexOf('.') >= 0)
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("field.names.cannot.contain.a.dot"));
            AcroFields af = writer.getAcroFields();
            AcroFields.Item item = af.getFieldItem(fieldName);
            if (item != null)
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.already.exists", fieldName));
            this.fieldName = fieldName;
        }
        if (page < 1 || page > writer.reader.getNumberOfPages())
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.page.number.1", page));
        this.pageRect = new Rectangle(pageRect);
        this.pageRect.normalize();
        rect = new Rectangle(this.pageRect.getWidth(), this.pageRect.getHeight());
        this.page = page;
    }

    /**
     * Sets the signature to be visible. An empty signature field with the same name must already exist.
     * @param fieldName the existing empty signature field name
     */
    public void setVisibleSignature(String fieldName) {
        AcroFields af = writer.getAcroFields();
        AcroFields.Item item = af.getFieldItem(fieldName);
        if (item == null)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.does.not.exist", fieldName));
        PdfDictionary merged = item.getMerged(0);
        if (!PdfName.SIG.equals(PdfReader.getPdfObject(merged.get(PdfName.FT))))
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.field.1.is.not.a.signature.field", fieldName));
        this.fieldName = fieldName;
        PdfArray r = merged.getAsArray(PdfName.RECT);
        float llx = r.getAsNumber(0).floatValue();
        float lly = r.getAsNumber(1).floatValue();
        float urx = r.getAsNumber(2).floatValue();
        float ury = r.getAsNumber(3).floatValue();
        pageRect = new Rectangle(llx, lly, urx, ury);
        pageRect.normalize();
        page = item.getPage(0).intValue();
        int rotation = writer.reader.getPageRotation(page);
        Rectangle pageSize = writer.reader.getPageSizeWithRotation(page);
        switch (rotation) {
            case 90:
                pageRect = new Rectangle(
                pageRect.getBottom(),
                pageSize.getTop() - pageRect.getLeft(),
                pageRect.getTop(),
                pageSize.getTop() - pageRect.getRight());
                break;
            case 180:
                pageRect = new Rectangle(
                pageSize.getRight() - pageRect.getLeft(),
                pageSize.getTop() - pageRect.getBottom(),
                pageSize.getRight() - pageRect.getRight(),
                pageSize.getTop() - pageRect.getTop());
                break;
            case 270:
                pageRect = new Rectangle(
                pageSize.getRight() - pageRect.getBottom(),
                pageRect.getLeft(),
                pageSize.getRight() - pageRect.getTop(),
                pageRect.getRight());
                break;
        }
        if (rotation != 0)
            pageRect.normalize();
        rect = new Rectangle(this.pageRect.getWidth(), this.pageRect.getHeight());
    }

	/*
	 * SIGNATURE APPEARANCE
	 */

    /**
     * Signature rendering modes
     * @since 5.0.1
     */
    public enum RenderingMode {
        /**
         * The rendering mode is just the description.
         */
        DESCRIPTION,
        /**
         * The rendering mode is the name of the signer and the description.
         */
        NAME_AND_DESCRIPTION,
        /**
         * The rendering mode is an image and the description.
         */
        GRAPHIC_AND_DESCRIPTION,
        /**
         * The rendering mode is just an image.
         */
        GRAPHIC
    }

    /** The rendering mode chosen for visible signatures */
    private RenderingMode renderingMode = RenderingMode.DESCRIPTION;

    /**
    * Gets the rendering mode for this signature.
    * @return the rendering mode for this signature
    * @since 5.0.1
    */
    public RenderingMode getRenderingMode() {
        return renderingMode;
    }

    /**
     * Sets the rendering mode for this signature.
     * @param renderingMode the rendering mode
     * @since 5.0.1
     */
    public void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = renderingMode;
    }

    /** The image that needs to be used for a visible signature */
    private Image signatureGraphic = null;

    /**
    * Gets the Image object to render.
    * @return the image
    */
    public Image getSignatureGraphic() {
        return signatureGraphic;
    }

    /**
     * Sets the Image object to render when Render is set to <CODE>RenderingMode.GRAPHIC</CODE>
     * or <CODE>RenderingMode.GRAPHIC_AND_DESCRIPTION</CODE>.
     * @param signatureGraphic image rendered. If <CODE>null</CODE> the mode is defaulted
     * to <CODE>RenderingMode.DESCRIPTION</CODE>
     */
    public void setSignatureGraphic(Image signatureGraphic) {
        this.signatureGraphic = signatureGraphic;
    }

    /** Appearance compliant with the recommendations introduced in Acrobat 6? */
    private boolean acro6Layers = true;

    /**
     * Gets the Acrobat 6.0 layer mode.
     * @return the Acrobat 6.0 layer mode
     */
    public boolean isAcro6Layers() {
        return this.acro6Layers;
    }

    /**
     * Acrobat 6.0 and higher recommends that only layer n0 and n2 be present.
     * Use this method with value <code>false</code> if you want to ignore this recommendation.
     * @param acro6Layers if <code>true</code> only the layers n0 and n2 will be present
     * @deprecated Adobe no longer supports Adobe Acrobat / Reader versions older than 9
     */
    public void setAcro6Layers(boolean acro6Layers) {
        this.acro6Layers = acro6Layers;
    }

    /** Layers for a visible signature. */
    private PdfTemplate app[] = new PdfTemplate[5];

    /**
     * Gets a template layer to create a signature appearance. The layers can go from 0 to 4,
     * but only layer 0 and 2 will be used if acro6Layers is true.
     * <p>
     * Consult <A HREF="http://partners.adobe.com/asn/developer/pdfs/tn/PPKAppearances.pdf">PPKAppearances.pdf</A>
     * for further details.
     * @param layer the layer
     * @return a template
     */
    public PdfTemplate getLayer(int layer) {
        if (layer < 0 || layer >= app.length)
            return null;
        PdfTemplate t = app[layer];
        if (t == null) {
            t = app[layer] = new PdfTemplate(writer);
            t.setBoundingBox(rect);
            writer.addDirectTemplateSimple(t, new PdfName("n" + layer));
        }
        return t;
    }

    /** Indicates if we need to reuse the existing appearance as layer 0. */
    private boolean reuseAppearance = false;

    /**
     * Indicates that the existing appearances needs to be reused as layer 0.
     */
    public void setReuseAppearance(boolean reuseAppearance) {
    	this.reuseAppearance = reuseAppearance;
    }

    // layer 1

    /** An appearance that can be used for layer 1 (if acro6Layers is false). */
    public static final String questionMark =
        "% DSUnknown\n" +
        "q\n" +
        "1 G\n" +
        "1 g\n" +
        "0.1 0 0 0.1 9 0 cm\n" +
        "0 J 0 j 4 M []0 d\n" +
        "1 i \n" +
        "0 g\n" +
        "313 292 m\n" +
        "313 404 325 453 432 529 c\n" +
        "478 561 504 597 504 645 c\n" +
        "504 736 440 760 391 760 c\n" +
        "286 760 271 681 265 626 c\n" +
        "265 625 l\n" +
        "100 625 l\n" +
        "100 828 253 898 381 898 c\n" +
        "451 898 679 878 679 650 c\n" +
        "679 555 628 499 538 435 c\n" +
        "488 399 467 376 467 292 c\n" +
        "313 292 l\n" +
        "h\n" +
        "308 214 170 -164 re\n" +
        "f\n" +
        "0.44 G\n" +
        "1.2 w\n" +
        "1 1 0.4 rg\n" +
        "287 318 m\n" +
        "287 430 299 479 406 555 c\n" +
        "451 587 478 623 478 671 c\n" +
        "478 762 414 786 365 786 c\n" +
        "260 786 245 707 239 652 c\n" +
        "239 651 l\n" +
        "74 651 l\n" +
        "74 854 227 924 355 924 c\n" +
        "425 924 653 904 653 676 c\n" +
        "653 581 602 525 512 461 c\n" +
        "462 425 441 402 441 318 c\n" +
        "287 318 l\n" +
        "h\n" +
        "282 240 170 -164 re\n" +
        "B\n" +
        "Q\n";

    // layer 2

    /** A background image for the text in layer 2. */
    private Image image;

    /**
     * Gets the background image for the layer 2.
     * @return the background image for the layer 2
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Sets the background image for the layer 2.
     * @param image the background image for the layer 2
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /** the scaling to be applied to the background image.t  */
    private float imageScale;

    /**
     * Gets the scaling to be applied to the background image.
     * @return the scaling to be applied to the background image
     */
    public float getImageScale() {
        return this.imageScale;
    }

    /**
     * Sets the scaling to be applied to the background image. If it's zero the image
     * will fully fill the rectangle. If it's less than zero the image will fill the rectangle but
     * will keep the proportions. If it's greater than zero that scaling will be applied.
     * In any of the cases the image will always be centered. It's zero by default.
     * @param imageScale the scaling to be applied to the background image
     */
    public void setImageScale(float imageScale) {
        this.imageScale = imageScale;
    }

    /** The text that goes in Layer 2 of the signature appearance. */
    private String layer2Text;

    /**
     * Sets the signature text identifying the signer.
     * @param text the signature text identifying the signer. If <CODE>null</CODE> or not set
     * a standard description will be used
     */
    public void setLayer2Text(String text) {
        layer2Text = text;
    }

    /**
     * Gets the signature text identifying the signer if set by setLayer2Text().
     * @return the signature text identifying the signer
     */
    public String getLayer2Text() {
        return layer2Text;
    }

    /** Font for the text in Layer 2. */
    private Font layer2Font;

    /**
     * Gets the n2 and n4 layer font.
     * @return the n2 and n4 layer font
     */
    public Font getLayer2Font() {
        return this.layer2Font;
    }

    /**
     * Sets the n2 and n4 layer font. If the font size is zero, auto-fit will be used.
     * @param layer2Font the n2 and n4 font
     */
    public void setLayer2Font(Font layer2Font) {
        this.layer2Font = layer2Font;
    }

    /** Run direction for the text in layers 2 and 4. */
    private int runDirection = PdfWriter.RUN_DIRECTION_NO_BIDI;

    /** Sets the run direction in the n2 and n4 layer.
     * @param runDirection the run direction
     */
    public void setRunDirection(int runDirection) {
        if (runDirection < PdfWriter.RUN_DIRECTION_DEFAULT || runDirection > PdfWriter.RUN_DIRECTION_RTL)
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
        this.runDirection = runDirection;
    }

    /** Gets the run direction.
     * @return the run direction
     */
    public int getRunDirection() {
        return runDirection;
    }

    // layer 4

    /** The text that goes in Layer 4 of the appearance. */
    private String layer4Text;

    /**
     * Sets the text identifying the signature status. Will be ignored if acro6Layers is true.
     * @param text the text identifying the signature status. If <CODE>null</CODE> or not set
     * the description "Signature Not Verified" will be used
     */
    public void setLayer4Text(String text) {
        layer4Text = text;
    }

    /**
     * Gets the text identifying the signature status if set by setLayer4Text().
     * @return the text identifying the signature status
     */
    public String getLayer4Text() {
        return layer4Text;
    }

    // all layers

    /** Template containing all layers drawn on top of each other. */
    private PdfTemplate frm;

    /**
     * Gets the template that aggregates all appearance layers. This corresponds to the /FRM resource.
     * <p>
     * Consult <A HREF="http://partners.adobe.com/asn/developer/pdfs/tn/PPKAppearances.pdf">PPKAppearances.pdf</A>
     * for further details.
     * @return the template that aggregates all appearance layers
     */
    public PdfTemplate getTopLayer() {
        if (frm == null) {
            frm = new PdfTemplate(writer);
            frm.setBoundingBox(rect);
            writer.addDirectTemplateSimple(frm, new PdfName("FRM"));
        }
        return frm;
    }

    // creating the appearance

    /** extra space at the top. */
    private static final float TOP_SECTION = 0.3f;

    /** margin for the content inside the signature rectangle. */
    private static final float MARGIN = 2;

    /**
     * Gets the main appearance layer.
     * <p>
     * Consult <A HREF="http://partners.adobe.com/asn/developer/pdfs/tn/PPKAppearances.pdf">PPKAppearances.pdf</A>
     * for further details.
     * @return the main appearance layer
     * @throws DocumentException on error
     */
    public PdfTemplate getAppearance() throws DocumentException {
        if (isInvisible()) {
            PdfTemplate t = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(0, 0));
            writer.addDirectTemplateSimple(t, null);
            return t;
        }

        if (app[0] == null && !reuseAppearance) {
            createBlankN0();
        }
        if (app[1] == null && !acro6Layers) {
            PdfTemplate t = app[1] = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(100, 100));
            writer.addDirectTemplateSimple(t, new PdfName("n1"));
            t.setLiteral(questionMark);
        }
        if (app[2] == null) {
            String text;
            if (layer2Text == null) {
                StringBuilder buf = new StringBuilder();
                buf.append("Digitally signed by ");
                String name = null;
                X500Name x500name = CertificateInfo.getSubjectFields((X509Certificate)signCertificate);
                if (x500name != null) {
                	name = x500name.getField("CN");
                	if (name == null)
                		name = x500name.getField("E");
                }
                if (name == null)
                    name = "";
                buf.append(name).append('\n');
                SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
                buf.append("Date: ").append(sd.format(signDate.getTime()));
                if (reason != null)
                    buf.append('\n').append(reasonCaption).append(reason);
                if (location != null)
                    buf.append('\n').append(locationCaption).append(location);
                text = buf.toString();
            }
            else
                text = layer2Text;
            PdfTemplate t = app[2] = new PdfTemplate(writer);
            t.setBoundingBox(rect);
            writer.addDirectTemplateSimple(t, new PdfName("n2"));
            if (image != null) {
                if (imageScale == 0) {
                    t.addImage(image, rect.getWidth(), 0, 0, rect.getHeight(), 0, 0);
                }
                else {
                    float usableScale = imageScale;
                    if (imageScale < 0)
                        usableScale = Math.min(rect.getWidth() / image.getWidth(), rect.getHeight() / image.getHeight());
                    float w = image.getWidth() * usableScale;
                    float h = image.getHeight() * usableScale;
                    float x = (rect.getWidth() - w) / 2;
                    float y = (rect.getHeight() - h) / 2;
                    t.addImage(image, w, 0, 0, h, x, y);
                }
            }
            Font font;
            if (layer2Font == null)
                font = new Font();
            else
                font = new Font(layer2Font);
            float size = font.getSize();

            Rectangle dataRect = null;
            Rectangle signatureRect = null;

            if (renderingMode == RenderingMode.NAME_AND_DESCRIPTION ||
                renderingMode == RenderingMode.GRAPHIC_AND_DESCRIPTION && this.signatureGraphic != null) {
                // origin is the bottom-left
                signatureRect = new Rectangle(
                    MARGIN,
                    MARGIN,
                    rect.getWidth() / 2 - MARGIN,
                    rect.getHeight() - MARGIN);
                dataRect = new Rectangle(
                    rect.getWidth() / 2 +  MARGIN / 2,
                    MARGIN,
                    rect.getWidth() - MARGIN / 2,
                    rect.getHeight() - MARGIN);

                if (rect.getHeight() > rect.getWidth()) {
                    signatureRect = new Rectangle(
                        MARGIN,
                        rect.getHeight() / 2,
                        rect.getWidth() - MARGIN,
                        rect.getHeight());
                    dataRect = new Rectangle(
                        MARGIN,
                        MARGIN,
                        rect.getWidth() - MARGIN,
                        rect.getHeight() / 2 - MARGIN);
                }
            }
            else if (renderingMode == RenderingMode.GRAPHIC) {
                if (signatureGraphic == null) {
                    throw new IllegalStateException(MessageLocalization.getComposedMessage("a.signature.image.should.be.present.when.rendering.mode.is.graphic.only"));
                }
                signatureRect = new Rectangle(
                        MARGIN,
                        MARGIN,
                        rect.getWidth() - MARGIN, // take all space available
                        rect.getHeight() - MARGIN);
            }
            else {
                dataRect = new Rectangle(
                        MARGIN,
                        MARGIN,
                        rect.getWidth() - MARGIN,
                        rect.getHeight() * (1 - TOP_SECTION) - MARGIN);
            }

            switch (renderingMode) {
            case NAME_AND_DESCRIPTION:
                String signedBy = CertificateInfo.getSubjectFields((X509Certificate)signCertificate).getField("CN");
                if (signedBy == null)
                    signedBy = CertificateInfo.getSubjectFields((X509Certificate)signCertificate).getField("E");
                if (signedBy == null)
                    signedBy = "";
                Rectangle sr2 = new Rectangle(signatureRect.getWidth() - MARGIN, signatureRect.getHeight() - MARGIN );
                float signedSize = ColumnText.fitText(font, signedBy, sr2, -1, runDirection);

                ColumnText ct2 = new ColumnText(t);
                ct2.setRunDirection(runDirection);
                ct2.setSimpleColumn(new Phrase(signedBy, font), signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), signedSize, Element.ALIGN_LEFT);

                ct2.go();
                break;
            case GRAPHIC_AND_DESCRIPTION:
                if (signatureGraphic == null) {
                    throw new IllegalStateException(MessageLocalization.getComposedMessage("a.signature.image.should.be.present.when.rendering.mode.is.graphic.and.description"));
                }
                ct2 = new ColumnText(t);
                ct2.setRunDirection(runDirection);
                ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), 0, Element.ALIGN_RIGHT);

                Image im = Image.getInstance(signatureGraphic);
                im.scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());

                Paragraph p = new Paragraph();
                // must calculate the point to draw from to make image appear in middle of column
                float x = 0;
                // experimentation found this magic number to counteract Adobe's signature graphic, which
                // offsets the y co-ordinate by 15 units
                float y = -im.getScaledHeight() + 15;

                x = x + (signatureRect.getWidth() - im.getScaledWidth()) / 2;
                y = y - (signatureRect.getHeight() - im.getScaledHeight()) / 2;
                p.add(new Chunk(im, x + (signatureRect.getWidth() - im.getScaledWidth()) / 2, y, false));
                ct2.addElement(p);
                ct2.go();
                break;
            case GRAPHIC:
                ct2 = new ColumnText(t);
                ct2.setRunDirection(runDirection);
                ct2.setSimpleColumn(signatureRect.getLeft(), signatureRect.getBottom(), signatureRect.getRight(), signatureRect.getTop(), 0, Element.ALIGN_RIGHT);

                im = Image.getInstance(signatureGraphic);
                im.scaleToFit(signatureRect.getWidth(), signatureRect.getHeight());

                p = new Paragraph(signatureRect.getHeight());
                // must calculate the point to draw from to make image appear in middle of column
                x = (signatureRect.getWidth() - im.getScaledWidth()) / 2;
                y = (signatureRect.getHeight() - im.getScaledHeight()) / 2;
                p.add(new Chunk(im, x, y, false));
                ct2.addElement(p);
                ct2.go();
                break;
            default:
            }

            if(renderingMode != RenderingMode.GRAPHIC) {
            	if (size <= 0) {
                    Rectangle sr = new Rectangle(dataRect.getWidth(), dataRect.getHeight());
                    size = ColumnText.fitText(font, text, sr, 12, runDirection);
                }
                ColumnText ct = new ColumnText(t);
                ct.setRunDirection(runDirection);
                ct.setSimpleColumn(new Phrase(text, font), dataRect.getLeft(), dataRect.getBottom(), dataRect.getRight(), dataRect.getTop(), size, Element.ALIGN_LEFT);
                ct.go();
            }
        }
        if (app[3] == null && !acro6Layers) {
            PdfTemplate t = app[3] = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(100, 100));
            writer.addDirectTemplateSimple(t, new PdfName("n3"));
            t.setLiteral("% DSBlank\n");
        }
        if (app[4] == null && !acro6Layers) {
            PdfTemplate t = app[4] = new PdfTemplate(writer);
            t.setBoundingBox(new Rectangle(0, rect.getHeight() * (1 - TOP_SECTION), rect.getRight(), rect.getTop()));
            writer.addDirectTemplateSimple(t, new PdfName("n4"));
            Font font;
            if (layer2Font == null)
                font = new Font();
            else
                font = new Font(layer2Font);
            //float size = font.getSize();
            String text = "Signature Not Verified";
            if (layer4Text != null)
                text = layer4Text;
            Rectangle sr = new Rectangle(rect.getWidth() - 2 * MARGIN, rect.getHeight() * TOP_SECTION - 2 * MARGIN);
            float size = ColumnText.fitText(font, text, sr, 15, runDirection);
            ColumnText ct = new ColumnText(t);
            ct.setRunDirection(runDirection);
            ct.setSimpleColumn(new Phrase(text, font), MARGIN, 0, rect.getWidth() - MARGIN, rect.getHeight() - MARGIN, size, Element.ALIGN_LEFT);
            ct.go();
        }
        int rotation = writer.reader.getPageRotation(page);
        Rectangle rotated = new Rectangle(rect);
        int n = rotation;
        while (n > 0) {
            rotated = rotated.rotate();
            n -= 90;
        }
        if (frm == null) {
            frm = new PdfTemplate(writer);
            frm.setBoundingBox(rotated);
            writer.addDirectTemplateSimple(frm, new PdfName("FRM"));
            float scale = Math.min(rect.getWidth(), rect.getHeight()) * 0.9f;
            float x = (rect.getWidth() - scale) / 2;
            float y = (rect.getHeight() - scale) / 2;
            scale /= 100;
            if (rotation == 90)
                frm.concatCTM(0, 1, -1, 0, rect.getHeight(), 0);
            else if (rotation == 180)
                frm.concatCTM(-1, 0, 0, -1, rect.getWidth(), rect.getHeight());
            else if (rotation == 270)
                frm.concatCTM(0, -1, 1, 0, 0, rect.getWidth());
            if (reuseAppearance) {
                AcroFields af = writer.getAcroFields();
                PdfIndirectReference ref = af.getNormalAppearance(getFieldName());
                if (ref != null) {
                	frm.addTemplateReference(ref, new PdfName("n0"), 1, 0, 0, 1, 0, 0);
                }
                else {
                	reuseAppearance = false;
                    if (app[0] == null) {
                        createBlankN0();
                    }
                }
            }
            if (!reuseAppearance) {
            	frm.addTemplate(app[0], 0, 0);
            }
            if (!acro6Layers)
                frm.addTemplate(app[1], scale, 0, 0, scale, x, y);
            frm.addTemplate(app[2], 0, 0);
            if (!acro6Layers) {
                frm.addTemplate(app[3], scale, 0, 0, scale, x, y);
                frm.addTemplate(app[4], 0, 0);
            }
        }
        PdfTemplate napp = new PdfTemplate(writer);
        napp.setBoundingBox(rotated);
        writer.addDirectTemplateSimple(napp, null);
        napp.addTemplate(frm, 0, 0);
        return napp;
    }

    private void createBlankN0() {
        PdfTemplate t = app[0] = new PdfTemplate(writer);
        t.setBoundingBox(new Rectangle(100, 100));
        writer.addDirectTemplateSimple(t, new PdfName("n0"));
        t.setLiteral("% DSBlank\n");
    }

    /*
     * Creating the signed file.
     */

    /** The PdfStamper that creates the signed PDF. */
    private PdfStamper stamper;

    /**
     * Gets the <CODE>PdfStamper</CODE> associated with this instance.
     * @return the <CODE>PdfStamper</CODE> associated with this instance
     */
    public PdfStamper getStamper() {
        return stamper;
    }

    /**
     * Sets the PdfStamper
     * @param stamper PdfStamper
     */
    void setStamper(PdfStamper stamper) {
        this.stamper = stamper;
    }

    /** The PdfStamperImp object corresponding with the stamper. */
    private PdfStamperImp writer;

    /** A byte buffer containing the bytes of the Stamper. */
    private ByteBuffer sigout;

    /**
     * Getter for the byte buffer.
     */
    ByteBuffer getSigout() {
        return sigout;
    }

    /**
     * Setter for the byte buffer.
     */
    void setSigout(ByteBuffer sigout) {
        this.sigout = sigout;
    }

    /** OutputStream for the bytes of the stamper. */
    private OutputStream originalout;

    /**
     * Getter for the OutputStream.
     */
    OutputStream getOriginalout() {
        return originalout;
    }

    /**
     * Setter for the OutputStream.
     */
    void setOriginalout(OutputStream originalout) {
        this.originalout = originalout;
    }

    /** Temporary file in case you don't want to sign in memory. */
    private File tempFile;

    /**
     * Gets the temporary file.
     * @return the temporary file or <CODE>null</CODE> is the document is created in memory
     */
    public File getTempFile() {
        return tempFile;
    }

    /**
     * Setter for the temporary file.
     * @param tempFile
     */
    void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }

    /** Name and content of keys that can only be added in the close() method. */
    private HashMap<PdfName, PdfLiteral> exclusionLocations;

    /** Length of the output. */
    private int boutLen;

    /** Indicates if the stamper has already been pre-closed. */
    private boolean preClosed = false;

    /** Signature field lock dictionary */
    private PdfSigLockDictionary fieldLock;

    /**
     * Getter for the field lock dictionary.
     * @return Field lock dictionary.
     */
    public PdfSigLockDictionary getFieldLockDict() {
        return fieldLock;
    }

    /**
     * Setter for the field lock dictionary.
     * <p><strong>Be aware:</strong> if a signature is created on an existing signature field,
     * then its /Lock dictionary takes the precedence (if it exists).</p>
     *
     * @param fieldLock Field lock dictionary.
     */
    public void setFieldLockDict(PdfSigLockDictionary fieldLock) {
        this.fieldLock = fieldLock;
    }

    /**
     * Checks if the document is in the process of closing.
     * @return <CODE>true</CODE> if the document is in the process of closing,
     * <CODE>false</CODE> otherwise
     */
    public boolean isPreClosed() {
        return preClosed;
    }

    /**
     * This is the first method to be called when using external signatures. The general sequence is:
     * preClose(), getDocumentBytes() and close().
     * <p>
     * If calling preClose() <B>dont't</B> call PdfStamper.close().
     * <p>
     * <CODE>exclusionSizes</CODE> must contain at least
     * the <CODE>PdfName.CONTENTS</CODE> key with the size that it will take in the
     * document. Note that due to the hex string coding this size should be
     * byte_size*2+2.
     * @param exclusionSizes a <CODE>HashMap</CODE> with names and sizes to be excluded in the signature
     * calculation. The key is a <CODE>PdfName</CODE> and the value an
     * <CODE>Integer</CODE>. At least the <CODE>PdfName.CONTENTS</CODE> must be present
     * @throws IOException on error
     * @throws DocumentException on error
     */
	public void preClose(HashMap<PdfName, Integer> exclusionSizes) throws IOException, DocumentException {
        if (preClosed)
            throw new DocumentException(MessageLocalization.getComposedMessage("document.already.pre.closed"));
        stamper.mergeVerification();
        preClosed = true;
        AcroFields af = writer.getAcroFields();
        String name = getFieldName();
        boolean fieldExists = af.doesSignatureFieldExist(name);
        PdfIndirectReference refSig = writer.getPdfIndirectReference();
        writer.setSigFlags(3);
        PdfDictionary fieldLock = null;
        if (fieldExists) {
            PdfDictionary widget = af.getFieldItem(name).getWidget(0);
            writer.markUsed(widget);
            fieldLock = widget.getAsDict(PdfName.LOCK);

            if (fieldLock == null && this.fieldLock != null) {
                widget.put(PdfName.LOCK, writer.addToBody(this.fieldLock).getIndirectReference());
                fieldLock = this.fieldLock;
            }

            widget.put(PdfName.P, writer.getPageReference(getPage()));
            widget.put(PdfName.V, refSig);
            PdfObject obj = PdfReader.getPdfObjectRelease(widget.get(PdfName.F));
            int flags = 0;
            if (obj != null && obj.isNumber())
                flags = ((PdfNumber)obj).intValue();
            flags |= PdfAnnotation.FLAGS_LOCKED;
            widget.put(PdfName.F, new PdfNumber(flags));
            PdfDictionary ap = new PdfDictionary();
            ap.put(PdfName.N, getAppearance().getIndirectReference());
            widget.put(PdfName.AP, ap);
        }
        else {
            PdfFormField sigField = PdfFormField.createSignature(writer);
            sigField.setFieldName(name);
            sigField.put(PdfName.V, refSig);
            sigField.setFlags(PdfAnnotation.FLAGS_PRINT | PdfAnnotation.FLAGS_LOCKED);

            if (this.fieldLock != null) {
                sigField.put(PdfName.LOCK, writer.addToBody(this.fieldLock).getIndirectReference());
                fieldLock = this.fieldLock;
            }

            int pagen = getPage();
            if (!isInvisible())
                sigField.setWidget(getPageRect(), null);
            else
                sigField.setWidget(new Rectangle(0, 0), null);
            sigField.setAppearance(PdfAnnotation.APPEARANCE_NORMAL, getAppearance());
            sigField.setPage(pagen);
            writer.addAnnotation(sigField, pagen);
        }

        exclusionLocations = new HashMap<PdfName, PdfLiteral>();
        if (cryptoDictionary == null) {
            throw new DocumentException("No crypto dictionary defined.");
        }
        else {
            PdfLiteral lit = new PdfLiteral(80);
            exclusionLocations.put(PdfName.BYTERANGE, lit);
            cryptoDictionary.put(PdfName.BYTERANGE, lit);
            for (Map.Entry<PdfName, Integer> entry: exclusionSizes.entrySet()) {
                PdfName key = entry.getKey();
                Integer v = entry.getValue();
                lit = new PdfLiteral(v.intValue());
                exclusionLocations.put(key, lit);
                cryptoDictionary.put(key, lit);
            }
            if (certificationLevel > 0)
                addDocMDP(cryptoDictionary);
            if (fieldLock != null)
            	addFieldMDP(cryptoDictionary, fieldLock);
            if (signatureEvent != null)
                signatureEvent.getSignatureDictionary(cryptoDictionary);
            writer.addToBody(cryptoDictionary, refSig, false);
        }
        if (certificationLevel > 0) {
          // add DocMDP entry to root
             PdfDictionary docmdp = new PdfDictionary();
             docmdp.put(new PdfName("DocMDP"), refSig);
             writer.reader.getCatalog().put(new PdfName("Perms"), docmdp);
        }
        writer.close(stamper.getMoreInfo());

        range = new long[exclusionLocations.size() * 2];
        long byteRangePosition = exclusionLocations.get(PdfName.BYTERANGE).getPosition();
        exclusionLocations.remove(PdfName.BYTERANGE);
        int idx = 1;
        for (PdfLiteral lit: exclusionLocations.values()) {
            long n = lit.getPosition();
            range[idx++] = n;
            range[idx++] = lit.getPosLength() + n;
        }
        Arrays.sort(range, 1, range.length - 1);
        for (int k = 3; k < range.length - 2; k += 2)
            range[k] -= range[k - 1];

        if (tempFile == null) {
            bout = sigout.getBuffer();
            boutLen = sigout.size();
            range[range.length - 1] = boutLen - range[range.length - 2];
            ByteBuffer bf = new ByteBuffer();
            bf.append('[');
            for (int k = 0; k < range.length; ++k)
                bf.append(range[k]).append(' ');
            bf.append(']');
            System.arraycopy(bf.getBuffer(), 0, bout, (int)byteRangePosition, bf.size());
        }
        else {
            try {
                raf = new RandomAccessFile(tempFile, "rw");
                long len = raf.length();
                range[range.length - 1] = len - range[range.length - 2];
                ByteBuffer bf = new ByteBuffer();
                bf.append('[');
                for (int k = 0; k < range.length; ++k)
                    bf.append(range[k]).append(' ');
                bf.append(']');
                raf.seek(byteRangePosition);
                raf.write(bf.getBuffer(), 0, bf.size());
            }
            catch (IOException e) {
                try{raf.close();}catch(Exception ee){}
                try{tempFile.delete();}catch(Exception ee){}
                throw e;
            }
        }
    }

    /**
     * Adds keys to the signature dictionary that define
     * the certification level and the permissions.
     * This method is only used for Certifying signatures.
     * @param crypto the signature dictionary
     */
    private void addDocMDP(PdfDictionary crypto) {
        PdfDictionary reference = new PdfDictionary();
        PdfDictionary transformParams = new PdfDictionary();
        transformParams.put(PdfName.P, new PdfNumber(certificationLevel));
        transformParams.put(PdfName.V, new PdfName("1.2"));
        transformParams.put(PdfName.TYPE, PdfName.TRANSFORMPARAMS);
        reference.put(PdfName.TRANSFORMMETHOD, PdfName.DOCMDP);
        reference.put(PdfName.TYPE, PdfName.SIGREF);
        reference.put(PdfName.TRANSFORMPARAMS, transformParams);
        if (writer.getPdfVersion().getVersion() < PdfWriter.VERSION_1_6) {
            reference.put(new PdfName("DigestValue"), new PdfString("aa"));
            PdfArray loc = new PdfArray();
            loc.add(new PdfNumber(0));
            loc.add(new PdfNumber(0));
            reference.put(new PdfName("DigestLocation"), loc);
            reference.put(new PdfName("DigestMethod"), new PdfName("MD5"));
        }
        reference.put(PdfName.DATA, writer.reader.getTrailer().get(PdfName.ROOT));
        PdfArray types = new PdfArray();
        types.add(reference);
        crypto.put(PdfName.REFERENCE, types);
    }

    /**
     * Adds keys to the signature dictionary that define
     * the field permissions.
     * This method is only used for signatures that lock fields.
     * @param crypto the signature dictionary
     */
    private void addFieldMDP(PdfDictionary crypto, PdfDictionary fieldLock) {
        PdfDictionary reference = new PdfDictionary();
        PdfDictionary transformParams = new PdfDictionary();
        transformParams.putAll(fieldLock);
        transformParams.put(PdfName.TYPE, PdfName.TRANSFORMPARAMS);
        transformParams.put(PdfName.V, new PdfName("1.2"));
        reference.put(PdfName.TRANSFORMMETHOD, PdfName.FIELDMDP);
        reference.put(PdfName.TYPE, PdfName.SIGREF);
        reference.put(PdfName.TRANSFORMPARAMS, transformParams);
        reference.put(new PdfName("DigestValue"), new PdfString("aa"));
        PdfArray loc = new PdfArray();
        loc.add(new PdfNumber(0));
        loc.add(new PdfNumber(0));
        reference.put(new PdfName("DigestLocation"), loc);
        reference.put(new PdfName("DigestMethod"), new PdfName("MD5"));
        reference.put(PdfName.DATA, writer.reader.getTrailer().get(PdfName.ROOT));
        PdfArray types = crypto.getAsArray(PdfName.REFERENCE);
        if (types == null)
        	types = new PdfArray();
        types.add(reference);
        crypto.put(PdfName.REFERENCE, types);
    }

    /**
     * This is the last method to be called when using external signatures. The general sequence is:
     * preClose(), getDocumentBytes() and close().
     * <p>
     * <CODE>update</CODE> is a <CODE>PdfDictionary</CODE> that must have exactly the
     * same keys as the ones provided in {@link #preClose(HashMap)}.
     * @param update a <CODE>PdfDictionary</CODE> with the key/value that will fill the holes defined
     * in {@link #preClose(HashMap)}
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public void close(PdfDictionary update) throws IOException, DocumentException {
        try {
            if (!preClosed)
                throw new DocumentException(MessageLocalization.getComposedMessage("preclose.must.be.called.first"));
            ByteBuffer bf = new ByteBuffer();
            for (PdfName key: update.getKeys()) {
                PdfObject obj = update.get(key);
                PdfLiteral lit = exclusionLocations.get(key);
                if (lit == null)
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.key.1.didn.t.reserve.space.in.preclose", key.toString()));
                bf.reset();
                obj.toPdf(null, bf);
                if (bf.size() > lit.getPosLength())
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.key.1.is.too.big.is.2.reserved.3", key.toString(), String.valueOf(bf.size()), String.valueOf(lit.getPosLength())));
                if (tempFile == null)
                    System.arraycopy(bf.getBuffer(), 0, bout, (int)lit.getPosition(), bf.size());
                else {
                    raf.seek(lit.getPosition());
                    raf.write(bf.getBuffer(), 0, bf.size());
                }
            }
            if (update.size() != exclusionLocations.size())
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.update.dictionary.has.less.keys.than.required"));
            if (tempFile == null) {
                originalout.write(bout, 0, boutLen);
            }
            else {
                if (originalout != null) {
                    raf.seek(0);
                    long length = raf.length();
                    byte buf[] = new byte[8192];
                    while (length > 0) {
                        int r = raf.read(buf, 0, (int)Math.min((long)buf.length, length));
                        if (r < 0)
                            throw new EOFException(MessageLocalization.getComposedMessage("unexpected.eof"));
                        originalout.write(buf, 0, r);
                        length -= r;
                    }
                }
            }
        }
        finally {
        	writer.reader.close();
            if (tempFile != null) {
                try{raf.close();}catch(Exception ee){}
                if (originalout != null)
                    try{tempFile.delete();}catch(Exception ee){}
            }
            if (originalout != null)
                try{originalout.close();}catch(Exception e){}
        }
    }
}
