/*
 * $Id:  $
 *
 * Copyright 2009 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999-2009 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2009 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.pdf.richmedia;

import java.io.IOException;
import java.util.HashMap;

import com.lowagie.text.Rectangle;
import com.lowagie.text.exceptions.IllegalPdfSyntaxException;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNameTree;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Object that is able to create Rich Media Annotations as described
 * in the document "Acrobat Supplement to the ISO 32000", referenced
 * in the code as "ExtensionLevel 3". This annotation is described in
 * section 9.6 entitled "Rich Media" of this document.
 * Extension level 3 introduces rich media PDF constructs that support
 * playing a SWF file and provide enhanced rich media. With rich media
 * annotation, Flash applications, video, audio, and other multimedia
 * can be attached to a PDF with expanded functionality. It improves upon
 * the existing 3D annotation structure to support multiple multimedia
 * file assets, including Flash video and compatible variations on the
 * H.264 format. The new constructs allow a two-way scripting bridge between
 * Flash and a conforming application. There is support for generalized
 * linking of a Flash application state to a comment or view, which enables
 * video commenting. Finally, actions can be linked to video chapter points.
 * @since	2.1.6
 */
public class RichMediaAnnotation {
	/** The PdfWriter to which the annotation will be added. */
	protected PdfWriter writer;
	/** The annotation object */
	protected PdfAnnotation annot;
	/** the rich media content (can be reused for different annotations) */
	protected PdfDictionary richMediaContent = new PdfDictionary(PdfName.RICHMEDIACONTENT);
	/** the rich media settings (specific for this annotation) */
	protected PdfDictionary richMediaSettings = new PdfDictionary(PdfName.RICHMEDIASETTINGS);
	/** a map with the assets (will be used to construct a name tree.) */
	protected HashMap assetsmap = new HashMap();
	/** an array with configurations (will be added to the RichMediaContent). */
	protected PdfArray configurations = new PdfArray();
	/** an array of views (will be added to the RichMediaContent) */
	protected PdfArray views = new PdfArray();
	
	/**
	 * Creates a RichMediaAnnotation.
	 * @param	writer	the PdfWriter to which the annotation will be added.
	 * @param	rect	the rectangle where the annotation will be added.
	 */
	public RichMediaAnnotation(PdfWriter writer, Rectangle rect) {
		this.writer = writer;
		annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.RICHMEDIA);
        richMediaContent.setIndRef(writer.getPdfIndirectReference());
        annot.put(PdfName.RICHMEDIACONTENT, richMediaContent.getIndRef());
        richMediaSettings.setIndRef(writer.getPdfIndirectReference());
        annot.put(PdfName.RICHMEDIASETTINGS, richMediaSettings.getIndRef());
	}

	/**
	 * Creates a RichMediaAnnotation using rich media content that has already
	 * been added to the writer. Note that assets, configurations, views added
	 * to a RichMediaAnnotation created like this will be ignored.
	 * @param	writer	the PdfWriter to which the annotation will be added.
	 * @param	rect	the rectangle where the annotation will be added.
	 * @param	richMediaContent	reused rich media content.
	 */
	public RichMediaAnnotation(PdfWriter writer, Rectangle rect, PdfDictionary richMediaContent) {
		if (richMediaContent.getIndRef() == null)
			throw new IllegalPdfSyntaxException(
					"You can't create a RichMediaAnnotation using content that isn't present in the writer yet.");
		this.writer = writer;
		annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.RICHMEDIA);
        assetsmap = null;
        this.richMediaContent = richMediaContent;
        annot.put(PdfName.RICHMEDIACONTENT, richMediaContent.getIndRef());
        richMediaSettings.setIndRef(writer.getPdfIndirectReference());
        annot.put(PdfName.RICHMEDIASETTINGS, richMediaSettings.getIndRef());
	}
	
	/**
	 * Getter for the RichMediaContent.
	 * @return	a PdfDictionary with RichMediaContent
	 */
	public PdfDictionary getRichMediaContent() {
		return richMediaContent;
	}

	/**
	 * Adds an embedded file.
	 * (Part of the RichMediaContent.)
	 * @param	name	a name for the name tree
	 * @param	fs		a file specification for an embedded file.
	 */
	public PdfIndirectReference addAsset(String name, PdfFileSpecification fs)
		throws IOException {
		if (assetsmap == null)
			throw new IllegalPdfSyntaxException(
				"You can't add assets to reused RichMediaContent.");
		PdfIndirectReference ref = writer.addPdfObject(fs);
		assetsmap.put(name, ref);
		return ref;
	}
	
	/**
	 * Adds a RichMediaConfiguration.
	 * (Part of the RichMediaContent.)
	 * @param	configuration	a configuration dictionary
	 */
	public PdfIndirectReference addConfiguration(RichMediaConfiguration configuration) throws IOException {
		if (assetsmap == null)
			throw new IllegalPdfSyntaxException(
				"You can't add configurations to reused RichMediaContent.");
		PdfIndirectReference ref = writer.addPdfObject(configuration);
		configurations.add(ref);
		return ref;
	}
	
	/**
	 * Adds a view dictionary.
	 * (Part of the RichMediaContent.)
	 * @param	view	a view dictionary
	 */
	public PdfIndirectReference addView(PdfDictionary view) throws IOException {
		if (assetsmap == null)
			throw new IllegalPdfSyntaxException(
				"You can't add views to reused RichMediaContent.");
		PdfIndirectReference ref = writer.addPdfObject(view);
		views.add(ref);
		return ref;
	}

	/**
	 * Sets the RichMediaActivation dictionary specifying the style of
	 * presentation, default script behavior, default view information,
	 * and animation style when the annotation is activated.
	 * (Part of the RichMediaSettings.)
	 * @param	richMediaActivation
	 */
	public void setActivation(RichMediaActivation richMediaActivation) {
		richMediaSettings.put(PdfName.ACTIVATION, richMediaActivation);
	}
	
	/**
	 * Sets the RichMediaDeactivation dictionary specifying the condition
	 * that causes deactivation of the annotation.
	 * (Part of the RichMediaSettings.)
	 * @param	richMediaDeactivation
	 */
	public void setDeactivation(RichMediaDeactivation richMediaDeactivation) {
		richMediaSettings.put(PdfName.DEACTIVATION, richMediaDeactivation);
	}
	
	/**
	 * Creates the actual annotation and adds different elements to the
	 * PdfWriter while doing so.
	 * @return	a PdfAnnotation
	 */
	public PdfAnnotation createAnnotation() throws IOException {
		if (assetsmap != null) {
			if (assetsmap.size() > 0) {
				PdfDictionary assets = PdfNameTree.writeTree(assetsmap, writer);
				richMediaContent.put(PdfName.ASSETS, writer.addToBody(assets).getIndirectReference());
				writer.addToBody(richMediaContent, richMediaContent.getIndRef());
			}
			if (configurations.size() > 0) {
				richMediaContent.put(PdfName.CONFIGURATION, writer.addToBody(configurations).getIndirectReference());
			}
			if (views.size() > 0) {
				richMediaContent.put(PdfName.VIEWS, writer.addToBody(views).getIndirectReference());
			}
		}
		writer.addToBody(richMediaSettings, richMediaSettings.getIndRef());
		return annot;
	}
}
