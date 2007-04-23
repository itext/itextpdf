/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Carsten Hammer.
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
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
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

package com.lowagie.tools.plugins.treeview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog {

	/** A serial version id */
	private static final long serialVersionUID = -2215681126685955584L;

	/** The total number of pages to read. */
	private int totalNumberOfPages;
	/** The current page being read. */
	private int currentPage;
	/** Visualization of the progress (current page / total number of pages). */
	JProgressBar progressbar = new JProgressBar();

	/** Constructs a dialog with a progress bar showing how many pages have been read. */
	public ProgressDialog(Frame frame, String title, boolean modal) {
		super(frame, title, modal);
		try {
			initialize();
			pack();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Initializes the dialog.
	 */
	private void initialize() throws Exception {
		getContentPane().setLayout(new BorderLayout());
		
		JPanel master_panel = new JPanel();
		master_panel.setLayout(new BorderLayout());
		getContentPane().add(master_panel, BorderLayout.CENTER);
		
		JLabel label = new JLabel();
		label.setText("PDF Analysis Progress");
		this.getContentPane().add(label, BorderLayout.NORTH);

		progressbar.setStringPainted(true);
		JPanel progress_panel = new JPanel();
		progress_panel.setLayout(new BorderLayout());
		master_panel.add(progress_panel, BorderLayout.CENTER);
		progress_panel.add(progressbar, BorderLayout.CENTER);
		
		// Center the dialog
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}

	/**
	 * Getter for the total number of pages.
	 */
	public int getTotalNumberOfPages() {
		return totalNumberOfPages;
	}

	/**
	 * Setter for the total number of pages.
	 * Also updates the progressbar.
	 */
	public void setTotalNumberOfPages(int total) {
		this.totalNumberOfPages = total;
		progressbar.setMaximum(total);
	}

	/**
	 * Getter for the current page.
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Setter for the current page.
	 * Also updates the progressbar.
	 */
	public void setCurrentPage(int current) {
		this.currentPage = current;
		progressbar.setValue(current);
	}
}
