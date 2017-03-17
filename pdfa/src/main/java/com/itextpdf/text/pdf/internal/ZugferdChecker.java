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


import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.AFRelationshipValue;
import com.itextpdf.text.pdf.PdfAConformanceException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAStamperImp;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;


import java.util.ArrayList;
import java.util.List;

public class ZugferdChecker extends PdfA3Checker {

    private List<PdfFileSpecification> attachments = new ArrayList<PdfFileSpecification>();

    ZugferdChecker(PdfAConformanceLevel conformanceLevel) {
        super(conformanceLevel);
    }

    @Override
    protected void checkFileSpec(PdfWriter writer, int key, Object obj1) {
        super.checkFileSpec(writer, key, obj1);
        attachments.add((PdfFileSpecification) obj1);
    }

    @Override
    public void close(PdfWriter writer) {
        super.close(writer);

        boolean ok = false;
        XMPMeta xmpMeta = null;
        if (writer.getXmpWriter() == null) {
            if (writer instanceof PdfAStamperImp) {
                xmpMeta = ((PdfAStamperImp) writer).getXmpMeta();
                PdfReader pdfReader = ((PdfAStamperImp) writer).getPdfReader();
                PdfArray pdfArray = pdfReader.getCatalog().getAsArray(PdfName.AF);
                if (pdfArray != null) {
                    for (int i = 0; i < pdfArray.size(); i++) {
                        PdfFileSpecification pdfFileSpecification = new PdfFileSpecification();
                        pdfFileSpecification.putAll((PdfDictionary) pdfArray.getDirectObject(i));
                        attachments.add(pdfFileSpecification);
                    }
                }
            }
        } else {
            xmpMeta = writer.getXmpWriter().getXmpMeta();
        }

        if (xmpMeta == null) {
            writer.createXmpMetadata();
        }

        try {
            String docFileName = xmpMeta.getPropertyString(PdfAXmpWriter.zugferdSchemaNS, PdfAXmpWriter.zugferdDocumentFileName);
            for (PdfFileSpecification attachment : attachments) {
                if ((attachment.getAsString(PdfName.UF) != null && docFileName.equals(attachment.getAsString(PdfName.UF).toString()))
                        || (attachment.getAsString(PdfName.F) != null && docFileName.equals(attachment.getAsString(PdfName.F).toString()))) {
                    PdfName relationship = attachment.getAsName(PdfName.AFRELATIONSHIP);
                    if (!AFRelationshipValue.Alternative.equals(relationship)) {
                        attachments.clear();
                        throw new PdfAConformanceException(attachment, MessageLocalization.getComposedMessage("afrelationship.value.shall.be.alternative"));
                    }
                    ok = true;
                    break;
                }
            }
        } catch (XMPException e) {
            attachments.clear();
            throw new ExceptionConverter(e);
        }
        attachments.clear();
        if (!ok) {
            throw new PdfAConformanceException(xmpMeta, MessageLocalization.getComposedMessage("zugferd.xmp.schema.shall.contain.attachment.name"));
        }
    }


}
