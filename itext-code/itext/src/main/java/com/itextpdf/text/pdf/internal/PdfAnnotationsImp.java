/*
 * $Id: PdfAnnotationsImp.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAcroForm;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfAnnotationsImp {

    /**
     * This is the AcroForm object for the complete document.
     */
    protected PdfAcroForm acroForm;

    /**
     * This is the array containing the references to annotations
     * that were added to the document.
     */
    protected ArrayList<PdfAnnotation> annotations;

    /**
     * This is an array containing references to some delayed annotations
     * (that were added for a page that doesn't exist yet).
     */
    protected ArrayList<PdfAnnotation> delayedAnnotations = new ArrayList<PdfAnnotation>();


    public PdfAnnotationsImp(PdfWriter writer) {
    	acroForm = new PdfAcroForm(writer);
    }

    /**
     * Checks if the AcroForm is valid.
     */
    public boolean hasValidAcroForm() {
    	return acroForm.isValid();
    }

    /**
     * Gets the AcroForm object.
     * @return the PdfAcroform object of the PdfDocument
     */
    public PdfAcroForm getAcroForm() {
        return acroForm;
    }

    public void setSigFlags(int f) {
        acroForm.setSigFlags(f);
    }

    public void addCalculationOrder(PdfFormField formField) {
        acroForm.addCalculationOrder(formField);
    }

    public void addAnnotation(PdfAnnotation annot) {
        if (annot.isForm()) {
            PdfFormField field = (PdfFormField)annot;
            if (field.getParent() == null)
                addFormFieldRaw(field);
        }
        else
            annotations.add(annot);
    }

    public void addPlainAnnotation(PdfAnnotation annot) {
    	annotations.add(annot);
    }

    void addFormFieldRaw(PdfFormField field) {
        annotations.add(field);
        ArrayList<PdfFormField> kids = field.getKids();
        if (kids != null) {
            for (int k = 0; k < kids.size(); ++k)
                addFormFieldRaw(kids.get(k));
        }
    }

    public boolean hasUnusedAnnotations() {
    	return !annotations.isEmpty();
    }

    public void resetAnnotations() {
        annotations = delayedAnnotations;
        delayedAnnotations = new ArrayList<PdfAnnotation>();
    }

    public PdfArray rotateAnnotations(PdfWriter writer, Rectangle pageSize) {
        PdfArray array = new PdfArray();
        int rotation = pageSize.getRotation() % 360;
        int currentPage = writer.getCurrentPageNumber();
        for (int k = 0; k < annotations.size(); ++k) {
            PdfAnnotation dic = annotations.get(k);
            int page = dic.getPlaceInPage();
            if (page > currentPage) {
                delayedAnnotations.add(dic);
                continue;
            }
            if (dic.isForm()) {
                if (!dic.isUsed()) {
                    HashSet<PdfTemplate> templates = dic.getTemplates();
                    if (templates != null)
                        acroForm.addFieldTemplates(templates);
                }
                PdfFormField field = (PdfFormField)dic;
                if (field.getParent() == null)
                    acroForm.addDocumentField(field.getIndirectReference());
            }
            if (dic.isAnnotation()) {
                array.add(dic.getIndirectReference());
                if (!dic.isUsed()) {
                	PdfArray tmp = dic.getAsArray(PdfName.RECT);
                    PdfRectangle rect;
                    if (tmp.size() == 4) {
                    	rect = new PdfRectangle(tmp.getAsNumber(0).floatValue(), tmp.getAsNumber(1).floatValue(), tmp.getAsNumber(2).floatValue(), tmp.getAsNumber(3).floatValue());
                    }
                    else {
                    	rect = new PdfRectangle(tmp.getAsNumber(0).floatValue(), tmp.getAsNumber(1).floatValue());
                    }
                    if (rect != null) {
                    	switch (rotation) {
                        	case 90:
                        		dic.put(PdfName.RECT, new PdfRectangle(
                        				pageSize.getTop() - rect.bottom(),
										rect.left(),
										pageSize.getTop() - rect.top(),
										rect.right()));
                        		break;
                        	case 180:
                        		dic.put(PdfName.RECT, new PdfRectangle(
                        				pageSize.getRight() - rect.left(),
										pageSize.getTop() - rect.bottom(),
										pageSize.getRight() - rect.right(),
										pageSize.getTop() - rect.top()));
                        		break;
                        	case 270:
                        		dic.put(PdfName.RECT, new PdfRectangle(
                        				rect.bottom(),
										pageSize.getRight() - rect.left(),
										rect.top(),
										pageSize.getRight() - rect.right()));
                        		break;
                    	}
                    }
                }
            }
            if (!dic.isUsed()) {
                dic.setUsed();
                try {
                    writer.addToBody(dic, dic.getIndirectReference());
                }
                catch (IOException e) {
                    throw new ExceptionConverter(e);
                }
            }
        }
        return array;
    }

    public static PdfAnnotation convertAnnotation(PdfWriter writer, Annotation annot, Rectangle defaultRect) throws IOException {
        switch(annot.annotationType()) {
           case Annotation.URL_NET:
               return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((URL) annot.attributes().get(Annotation.URL)));
           case Annotation.URL_AS_STRING:
               return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE)));
           case Annotation.FILE_DEST:
               return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE), (String) annot.attributes().get(Annotation.DESTINATION)));
           case Annotation.SCREEN:
               boolean sparams[] = (boolean[])annot.attributes().get(Annotation.PARAMETERS);
               String fname = (String) annot.attributes().get(Annotation.FILE);
               String mimetype = (String) annot.attributes().get(Annotation.MIMETYPE);
               PdfFileSpecification fs;
               if (sparams[0])
                   fs = PdfFileSpecification.fileEmbedded(writer, fname, fname, null);
               else
                   fs = PdfFileSpecification.fileExtern(writer, fname);
               PdfAnnotation ann = PdfAnnotation.createScreen(writer, new Rectangle(annot.llx(), annot.lly(), annot.urx(), annot.ury()),
                       fname, fs, mimetype, sparams[1]);
               return ann;
           case Annotation.FILE_PAGE:
               return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.FILE), ((Integer) annot.attributes().get(Annotation.PAGE)).intValue()));
           case Annotation.NAMED_DEST:
               return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction(((Integer) annot.attributes().get(Annotation.NAMED)).intValue()));
           case Annotation.LAUNCH:
               return new PdfAnnotation(writer, annot.llx(), annot.lly(), annot.urx(), annot.ury(), new PdfAction((String) annot.attributes().get(Annotation.APPLICATION),(String) annot.attributes().get(Annotation.PARAMETERS),(String) annot.attributes().get(Annotation.OPERATION),(String) annot.attributes().get(Annotation.DEFAULTDIR)));
           default:
        	   return new PdfAnnotation(writer, defaultRect.getLeft(), defaultRect.getBottom(), defaultRect.getRight(), defaultRect.getTop(), new PdfString(annot.title(), PdfObject.TEXT_UNICODE), new PdfString(annot.content(), PdfObject.TEXT_UNICODE));
       }
   }
}
