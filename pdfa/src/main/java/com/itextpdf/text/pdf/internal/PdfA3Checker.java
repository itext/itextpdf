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

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.*;

import java.util.Arrays;
import java.util.HashSet;

public class PdfA3Checker extends PdfA2Checker {

    static private HashSet<PdfName> allowedAFRelationships = new HashSet<PdfName>(Arrays.asList(
            AFRelationshipValue.Source, AFRelationshipValue.Data, AFRelationshipValue.Alternative,
            AFRelationshipValue.Supplement, AFRelationshipValue.Unspecified));


    PdfA3Checker(PdfAConformanceLevel conformanceLevel) {
        super(conformanceLevel);
    }

    @Override
    protected HashSet<PdfName> initKeysForCheck() {
        HashSet<PdfName> keysForCheck = super.initKeysForCheck();
        keysForCheck.add(PdfName.PARAMS);
        keysForCheck.add(PdfName.MODDATE);
        keysForCheck.add(PdfName.F);
        return keysForCheck;
    }

    @Override
    protected void checkFileSpec(PdfWriter writer, int key, Object obj1) {
        if (obj1 instanceof PdfFileSpecification) {
            PdfDictionary fileSpec = (PdfFileSpecification)obj1;
            if(!fileSpec.contains(PdfName.UF) || !fileSpec.contains(PdfName.F)
                    || !fileSpec.contains(PdfName.DESC)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("file.specification.dictionary.shall.contain.f.uf.and.desc.entries"));
            }

            PdfObject obj = fileSpec.get(PdfName.AFRELATIONSHIP);

            if (obj == null || !obj.isName() || !allowedAFRelationships.contains(obj)) {
                throw new PdfAConformanceException(obj1, MessageLocalization.getComposedMessage("file.specification.dictionary.shall.contain.correct.afrelationship.key"));
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

    protected void checkEmbeddedFile(PdfDictionary embeddedFile) {
        PdfObject params = getDirectObject(embeddedFile.get(PdfName.PARAMS));
        if (params == null) {
            throw new PdfAConformanceException(embeddedFile, MessageLocalization.getComposedMessage("embedded.file.shall.contain.valid.params.key"));
        } else if (params.isDictionary()){
            PdfObject modDate = ((PdfDictionary)params).get(PdfName.MODDATE);
            if (modDate == null || !(modDate instanceof PdfString)) {
                throw new PdfAConformanceException(embeddedFile, MessageLocalization.getComposedMessage("embedded.file.shall.contain.params.key.with.valid.moddate.key"));
            }
        }
    }

    @Override
    protected void checkPdfObject(PdfWriter writer, int key, Object obj1) {
        super.checkPdfObject(writer, key, obj1);
        if (obj1 instanceof PdfDictionary) {
            PdfDictionary dictionary = (PdfDictionary) obj1;
            PdfName type = dictionary.getAsName(PdfName.TYPE);
            if (PdfName.EMBEDDEDFILE.equals(type)) {
                checkEmbeddedFile(dictionary);
            }
        }
    }
}
