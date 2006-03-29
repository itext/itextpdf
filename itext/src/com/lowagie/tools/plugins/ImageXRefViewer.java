/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Carsten Hammer and Bruno Lowagie.
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
 * the Initial Developer are Copyright (C) 1999-2006 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2006 by Paulo Soares. All Rights Reserved.
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
package com.lowagie.tools.plugins;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStream;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * Allows you to inspect the Image XObjects inside a PDF file.
 */
public class ImageXRefViewer
  extends AbstractTool {
	static {
		addVersion("$Id$");
	}
	
	class ViewXRefImages_jSpinner1_propertyChangeAdapter
    	implements javax.swing.event.ChangeListener {
		private ImageXRefViewer adaptee;
		ViewXRefImages_jSpinner1_propertyChangeAdapter(ImageXRefViewer adaptee) {
			this.adaptee = adaptee;
		}
		/**
		 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
		 */
		public void stateChanged(ChangeEvent e) {
			adaptee.jSpinner_propertyChange(e);
		}
	}
	
	JPanel jPanel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JLabel jLabel1 = new JLabel();
	int picturenumber = 0;
	JPanel jPanel2 = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	CardLayout cardLayout1 = new CardLayout();
	JPanel jPanel3 = new JPanel();
	JSpinner jSpinner1 = new JSpinner();
	BorderLayout borderLayout3 = new BorderLayout();
	SpinnerModel spinnerModel1 = jSpinner1.getModel();
	

	/**
	 * Creates a ViewImageXObjects object.
	 */
	public ImageXRefViewer() {
		arguments.add(new FileArgument(this, "srcfile", "The file you want to inspect", false, new PdfFilter()));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#getDestPathPDF()
	 */
	protected File getDestPathPDF() throws InstantiationException {
		throw new InstantiationException("There is no file to show.");
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("View Image XObjects", true, false, true);
		internalFrame.setSize(500, 300);
		internalFrame.setJMenuBar(getMenubar());
	    internalFrame.getContentPane().setLayout(borderLayout1);
	    jPanel1.setLayout(borderLayout2);
	    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
	    jLabel1.setText("images");
	    jPanel2.setLayout(cardLayout1);
	    jPanel3.setLayout(borderLayout3);
	    jSpinner1.addChangeListener(new
	    ViewXRefImages_jSpinner1_propertyChangeAdapter(this));
	    jPanel2.setBorder(BorderFactory.createEtchedBorder());
	    internalFrame.getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
	    jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);
	    jPanel3.add(jSpinner1, java.awt.BorderLayout.CENTER);
	    jPanel3.add(jLabel1, java.awt.BorderLayout.NORTH);
	    jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);
		System.out.println("=== Image XObject Viewer OPENED ===");
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#valueHasChanged(com.lowagie.tools.arguments.ToolArgument)
	 */
	public void valueHasChanged(ToolArgument arg) {
		// do nothing
	}
	
	/**
	 * Reflects the change event in the JSpinner object.
	 * @param evt
	 */
	public void jSpinner_propertyChange(ChangeEvent evt) {
	    int blatt = Integer.parseInt(jSpinner1.getValue().toString());
	    if (blatt < 0) blatt = 0;
	    if (blatt >= picturenumber) blatt = picturenumber - 1;
	    this.cardLayout1.show(jPanel2, String.valueOf(blatt));
	    jPanel2.repaint();
	}
	/**
	 * Shows the images that are added to the PDF as Image XObjects.
	 * @param args
	 */
	public static void main(String[] args) {
    	InspectPDF tool = new InspectPDF();
    	if (args.length < 1) {
    		System.err.println(tool.getUsage());
    	}
    	tool.setArguments(args);
        tool.execute();
	}
	
	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		picturenumber = 0;
		try {
			if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
			PdfReader reader = new PdfReader(((File)getValue("srcfile")).getAbsolutePath());
		      for (int i = 0; i < reader.getXrefSize(); i++) {
		        PdfObject pdfobj = reader.getPdfObject(i);
		        if (pdfobj != null) {
		          if (pdfobj.isStream()) {
		            PdfStream pdfdict = (PdfStream) pdfobj;
		            PdfObject pdfsubtype = pdfdict.get(PdfName.SUBTYPE);
		            if (pdfsubtype == null) {
		              continue;
		            }
		            if (!pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
		              continue;
		            }
		            System.out.println("picturenumber: " + picturenumber);
		            System.out.println("height:" +
		                               pdfdict.get(PdfName.HEIGHT));
		            System.out.println("width:" +
		                               pdfdict.get(PdfName.WIDTH));
		            System.out.println("bitspercomponent:" +
		                               pdfdict.get(PdfName.BITSPERCOMPONENT));
		            byte[] barr = PdfReader.getStreamBytesRaw( (PRStream) pdfdict);
		            try {
		              java.awt.Image im = Toolkit.getDefaultToolkit().createImage(barr);
		              javax.swing.ImageIcon ii = new javax.swing.ImageIcon(im);

		              JLabel jLabel1 = new JLabel();
		              jLabel1.setIcon(ii);
		              jPanel2.add(jLabel1, "" + picturenumber++);
		            }
		            catch (Exception ex1) {
		            }
		          }
		        }
		      }
		}
		catch(Exception e) {
        	JOptionPane.showMessageDialog(internalFrame,
        		    e.getMessage(),
        		    e.getClass().getName(),
        		    JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
		}
	}
}
