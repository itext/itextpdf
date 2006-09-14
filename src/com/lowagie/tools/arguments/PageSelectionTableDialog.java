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
package com.lowagie.tools.arguments;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/**
 * A dialog that allows you to select pages from an existing PDF file.
 */
public class PageSelectionTableDialog extends JDialog {

	private static final long serialVersionUID = -4513164788016907219L;

	JPanel panel1 = new JPanel();

	BorderLayout borderLayout1 = new BorderLayout();

	ListSelectionModel listSelectionModel1;

	JTable jTable1 = new JTable();

	JScrollPane jScrollPane1 = new JScrollPane();

	String selectionstring = "";

	JLabel jLabel1 = new JLabel();

	BorderLayout borderLayout2 = new BorderLayout();

	JPanel jPanel1 = new JPanel();

	JButton alljButton1 = new JButton();

	JButton oddjButton2 = new JButton();

	JButton evenjButton3 = new JButton();

	JToggleButton jToggleButton1 = new JToggleButton();

	JButton none = new JButton();

	public PageSelectionTableDialog(JInternalFrame owner, String title,
			boolean modal) {
		super(new Frame(), title, modal);
		// super( title);
		try {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			jbInit();
			pack();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public PageSelectionTableDialog(JInternalFrame jinternalframe) {
		this(jinternalframe, "", false);
	}

	private void jbInit() throws Exception {
		panel1.setLayout(borderLayout1);
		this.getContentPane().setLayout(borderLayout2);
		alljButton1.setText("all");
		alljButton1
				.addActionListener(new PageSelectionTableDialog_jButton1_actionAdapter(
						this));
		oddjButton2.setText("odd");
		oddjButton2
				.addActionListener(new PageSelectionTableDialog_jButton2_actionAdapter(
						this));
		evenjButton3.setText("even");
		evenjButton3
				.addActionListener(new PageSelectionTableDialog_jButton3_actionAdapter(
						this));
		jToggleButton1.setText("swap");
		jToggleButton1
				.addActionListener(new PageSelectionTableDialog_jToggleButton1_actionAdapter(
						this));
		none.setText("none");
		none.addActionListener(new PageSelectionTableDialog_none_actionAdapter(
				this));

		panel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
		panel1.add(jLabel1, java.awt.BorderLayout.SOUTH);
		this.getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
		jPanel1.add(none);
		jPanel1.add(jToggleButton1);
		jPanel1.add(evenjButton3);
		jPanel1.add(oddjButton2);
		jPanel1.add(alljButton1);
		this.getContentPane().add(panel1, java.awt.BorderLayout.CENTER);
		jScrollPane1.setViewportView(jTable1);
		listSelectionModel1 = jTable1.getSelectionModel();
		listSelectionModel1
				.addListSelectionListener(new PageSelectionTableDialog_listSelectionModel1_listSelectionAdapter(
						this));
	}

	public void setDataModel(TableModel dataModel) {
		TableSorter sorter = new TableSorter(dataModel);
		jTable1.setModel(sorter);
		sorter.addMouseListenerToHeaderInTable(jTable1);
		this.repaint();
	}

	public void listSelectionModel1_valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			pulllistselectionmodel();
		}
	}

	private void pulllistselectionmodel() {
		TableSorter mysorter = (TableSorter) jTable1.getModel();
		int[] values = jTable1.getSelectedRows();
		int max = jTable1.getSelectedRowCount();
		int[] swappedvalues = new int[max];

		if (jToggleButton1.getModel().isSelected()) {
			for (int i = 0; i < max; i += 2) {
				int second = (i + 1) < max ? i + 1 : i;
				swappedvalues[i] = mysorter.getModelrow(values[second]) + 1;
				swappedvalues[second] = mysorter.getModelrow(values[i]) + 1;
			}
		} else {
			for (int i = 0; i < max; i++) {
				swappedvalues[i] = mysorter.getModelrow(values[i]) + 1;
			}

		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < max; i++) {
			sb.append(swappedvalues[i]);
			if ((i + 1) < max) {
				sb.append(",");
			}
		}

		jLabel1.setText(sb.toString());
		this.firePropertyChange(
				PageSelectorToolArgument.PROPERTYPAGESELECTIONSTRING,
				selectionstring, sb.toString());
		selectionstring = sb.toString();
	}

	public void jButton1_actionPerformed(ActionEvent e) {
		listSelectionModel1.addSelectionInterval(0, jTable1.getRowCount() - 1);
		jTable1.repaint();
	}

	public void jButton3_actionPerformed(ActionEvent e) {
		for (int i = 0; i < jTable1.getRowCount(); i += 2) {
			listSelectionModel1.addSelectionInterval(i, i);
		}
		jTable1.repaint();
	}

	public void jButton2_actionPerformed(ActionEvent e) {
		for (int i = 1; i < jTable1.getRowCount(); i += 2) {
			listSelectionModel1.addSelectionInterval(i, i);
		}
		jTable1.repaint();
	}

	public void jToggleButton1_actionPerformed(ActionEvent e) {
		pulllistselectionmodel();
	}

	public void none_actionPerformed(ActionEvent e) {
		listSelectionModel1.clearSelection();
		jTable1.repaint();
	}
}

class PageSelectionTableDialog_none_actionAdapter implements ActionListener {
	private PageSelectionTableDialog adaptee;

	PageSelectionTableDialog_none_actionAdapter(PageSelectionTableDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.none_actionPerformed(e);
	}
}

class PageSelectionTableDialog_jToggleButton1_actionAdapter implements
		ActionListener {
	private PageSelectionTableDialog adaptee;

	PageSelectionTableDialog_jToggleButton1_actionAdapter(
			PageSelectionTableDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jToggleButton1_actionPerformed(e);
	}
}

class PageSelectionTableDialog_jButton2_actionAdapter implements ActionListener {
	private PageSelectionTableDialog adaptee;

	PageSelectionTableDialog_jButton2_actionAdapter(
			PageSelectionTableDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButton2_actionPerformed(e);
	}
}

class PageSelectionTableDialog_jButton3_actionAdapter implements ActionListener {
	private PageSelectionTableDialog adaptee;

	PageSelectionTableDialog_jButton3_actionAdapter(
			PageSelectionTableDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {

		adaptee.jButton3_actionPerformed(e);
	}
}

class PageSelectionTableDialog_jButton1_actionAdapter implements ActionListener {
	private PageSelectionTableDialog adaptee;

	PageSelectionTableDialog_jButton1_actionAdapter(
			PageSelectionTableDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButton1_actionPerformed(e);
	}
}

class PageSelectionTableDialog_listSelectionModel1_listSelectionAdapter
		implements ListSelectionListener {
	private PageSelectionTableDialog adaptee;

	PageSelectionTableDialog_listSelectionModel1_listSelectionAdapter(
			PageSelectionTableDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void valueChanged(ListSelectionEvent e) {
		adaptee.listSelectionModel1_valueChanged(e);
	}
}
