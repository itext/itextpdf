/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, Alexander Chingarev, et al.
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

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Extension of PdfStamper that will attempt to keep a file
 * in conformance with the PDF/A standard.
 * @see PdfStamper
 */
public class PdfAStamper extends PdfStamper {

    /**
     * Starts the process of adding extra content to an existing PDF document keeping the document PDF/A conformant.
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @throws DocumentException on error
     * @throws IOException or error
     */
    public PdfAStamper(final PdfReader reader, final OutputStream os, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        stamper = new PdfAStamperImp(reader, os, '\0', false, conformanceLevel);
    }

    /**
     * Starts the process of adding extra content to an existing PDF document keeping the document PDF/A conformant.
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original document
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @throws DocumentException on error
     * @throws IOException or error
     */
    public PdfAStamper(final PdfReader reader, final OutputStream os, final char pdfVersion, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        stamper = new PdfAStamperImp(reader, os, pdfVersion, false, conformanceLevel);
    }

    /**
     * Starts the process of adding extra content to an existing PDF document keeping the document PDF/A conformant.
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original document
     * @param append if <CODE>true</CODE> appends the document changes as a new revision. This is only useful for multiple signatures as nothing is gained in speed or memory
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @throws DocumentException on error
     * @throws IOException or error
     */
    public PdfAStamper(final PdfReader reader, final OutputStream os, final char pdfVersion, final boolean append, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        stamper = new PdfAStamperImp(reader, os, pdfVersion, append, conformanceLevel);
    }

    /**
     * Applies a digital signature to a document, possibly as a new revision, making
     * possible multiple signatures. The returned PdfStamper
     * can be used normally as the signature is only applied when closing.
     * <p>
     * A possible use for adding a signature without invalidating an existing one is:
     * <p>
     * <pre>
     * KeyStore ks = KeyStore.getInstance("pkcs12");
     * ks.load(new FileInputStream("my_private_key.pfx"), "my_password".toCharArray());
     * String alias = (String)ks.aliases().nextElement();
     * PrivateKey key = (PrivateKey)ks.getKey(alias, "my_password".toCharArray());
     * Certificate[] chain = ks.getCertificateChain(alias);
     * PdfReader reader = new PdfReader("original.pdf");
     * FileOutputStream fout = new FileOutputStream("signed.pdf");
     * PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0', new
     * File("/temp"), true);
     * PdfSignatureAppearance sap = stp.getSignatureAppearance();
     * sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
     * sap.setReason("I'm the author");
     * sap.setLocation("Lisbon");
     * // comment next line to have an invisible signature
     * sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
     * stp.close();
     * </pre>
     * @param reader the original document
     * @param os the output stream or <CODE>null</CODE> to keep the document in the temporary file
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @param tempFile location of the temporary file. If it's a directory a temporary file will be created there.
     *     If it's a file it will be used directly. The file will be deleted on exit unless <CODE>os</CODE> is null.
     *     In that case the document can be retrieved directly from the temporary file. If it's <CODE>null</CODE>
     *     no temporary file will be created and memory will be used
     * @param append if <CODE>true</CODE> the signature and all the other content will be added as a
     * new revision thus not invalidating existing signatures
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @return a <CODE>PdfAStamper</CODE>
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public static PdfAStamper createSignature(final PdfReader reader, final OutputStream os, final char pdfVersion, File tempFile, final boolean append, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        PdfAStamper stp;
        if (tempFile == null) {
            ByteBuffer bout = new ByteBuffer();
            stp = new PdfAStamper(reader, bout, pdfVersion, append, conformanceLevel);
            stp.sigApp = new PdfSignatureAppearance(stp.stamper);
            stp.sigApp.setSigout(bout);
        }
        else {
            if (tempFile.isDirectory())
                tempFile = File.createTempFile("pdf", null, tempFile);
            FileOutputStream fout = new FileOutputStream(tempFile);
            stp = new PdfAStamper(reader, fout, pdfVersion, append, conformanceLevel);
            stp.sigApp = new PdfSignatureAppearance(stp.stamper);
            stp.sigApp.setTempFile(tempFile);
        }
        stp.sigApp.setOriginalout(os);
        stp.sigApp.setStamper(stp);
        stp.hasSignature = true;
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
        if (acroForm != null) {
            acroForm.remove(PdfName.NEEDAPPEARANCES);
            stp.stamper.markUsed(acroForm);
        }
        return stp;
    }

    /**
     * Applies a digital signature to a document. The returned PdfStamper
     * can be used normally as the signature is only applied when closing.
     * <p>
     * Note that the pdf is created in memory.
     * <p>
     * A possible use is:
     * <p>
     * <pre>
     * KeyStore ks = KeyStore.getInstance("pkcs12");
     * ks.load(new FileInputStream("my_private_key.pfx"), "my_password".toCharArray());
     * String alias = (String)ks.aliases().nextElement();
     * PrivateKey key = (PrivateKey)ks.getKey(alias, "my_password".toCharArray());
     * Certificate[] chain = ks.getCertificateChain(alias);
     * PdfReader reader = new PdfReader("original.pdf");
     * FileOutputStream fout = new FileOutputStream("signed.pdf");
     * PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
     * PdfSignatureAppearance sap = stp.getSignatureAppearance();
     * sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
     * sap.setReason("I'm the author");
     * sap.setLocation("Lisbon");
     * // comment next line to have an invisible signature
     * sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
     * stp.close();
     * </pre>
     * @param reader the original document
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @throws DocumentException on error
     * @throws IOException on error
     * @return a <CODE>PdfAStamper</CODE>
     */
    public static PdfAStamper createSignature(final PdfReader reader, final OutputStream os, final char pdfVersion, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException {
        return createSignature(reader, os, pdfVersion, null, false, conformanceLevel);
    }

    /**
     * Applies a digital signature to a document. The returned PdfStamper
     * can be used normally as the signature is only applied when closing.
     * <p>
     * A possible use is:
     * <p>
     * <pre>
     * KeyStore ks = KeyStore.getInstance("pkcs12");
     * ks.load(new FileInputStream("my_private_key.pfx"), "my_password".toCharArray());
     * String alias = (String)ks.aliases().nextElement();
     * PrivateKey key = (PrivateKey)ks.getKey(alias, "my_password".toCharArray());
     * Certificate[] chain = ks.getCertificateChain(alias);
     * PdfReader reader = new PdfReader("original.pdf");
     * FileOutputStream fout = new FileOutputStream("signed.pdf");
     * PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0', new File("/temp"));
     * PdfSignatureAppearance sap = stp.getSignatureAppearance();
     * sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
     * sap.setReason("I'm the author");
     * sap.setLocation("Lisbon");
     * // comment next line to have an invisible signature
     * sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
     * stp.close();
     * </pre>
     * @param reader the original document
     * @param os the output stream or <CODE>null</CODE> to keep the document in the temporary file
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @param tempFile location of the temporary file. If it's a directory a temporary file will be created there.
     *     If it's a file it will be used directly. The file will be deleted on exit unless <CODE>os</CODE> is null.
     *     In that case the document can be retrieved directly from the temporary file. If it's <CODE>null</CODE>
     *     no temporary file will be created and memory will be used
     * @param conformanceLevel PDF/A conformance level of a new PDF document
     * @return a <CODE>PdfAStamper</CODE>
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public static PdfAStamper createSignature(final PdfReader reader, final OutputStream os, final char pdfVersion, final File tempFile, final PdfAConformanceLevel conformanceLevel) throws DocumentException, IOException
    {
        return createSignature(reader, os, pdfVersion, tempFile, false, conformanceLevel);
    }
}
