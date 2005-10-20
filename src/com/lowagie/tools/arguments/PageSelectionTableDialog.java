package com.lowagie.tools.arguments;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.BorderLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PageSelectionTableDialog
    extends JDialog {
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

  public PageSelectionTableDialog(JInternalFrame owner, String title, boolean modal) {
    super(new Frame(), title, modal);
//    super( title);
    try {
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      jbInit();
      pack();
    }
    catch (Exception exception) {
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
    alljButton1.addActionListener(new
                               PageSelectionTableDialog_jButton1_actionAdapter(this));
    oddjButton2.setText("odd");
    oddjButton2.addActionListener(new
                               PageSelectionTableDialog_jButton2_actionAdapter(this));
    evenjButton3.setText("even");
    evenjButton3.addActionListener(new
                                   PageSelectionTableDialog_jButton3_actionAdapter(this));
    jToggleButton1.setText("swap");
    jToggleButton1.addActionListener(new
                                     PageSelectionTableDialog_jToggleButton1_actionAdapter(this));
    none.setText("none");
    none.addActionListener(new PageSelectionTableDialog_none_actionAdapter(this));

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
    listSelectionModel1.addListSelectionListener(new
                                                 PageSelectionTableDialog_listSelectionModel1_listSelectionAdapter(this));
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
      for (int i = 0; i < max; i+=2) {
        int second=(i+1)<max?i+1:i;
        swappedvalues[i] = mysorter.getModelrow(values[second]) + 1;
        swappedvalues[second] = mysorter.getModelrow(values[i]) + 1;
      }
    }
    else {
      for (int i = 0; i < max; i++) {
        swappedvalues[i] = mysorter.getModelrow(values[i]) + 1;
      }

    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < max; i++) {
      sb.append(swappedvalues[i]);
      if ( (i + 1) < max) {
        sb.append(",");
      }
    }

    jLabel1.setText(sb.toString());
    this.firePropertyChange(PageSelectorToolArgument.
                            PROPERTYPAGESELECTIONSTRING, selectionstring,
                            sb.toString());
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

class PageSelectionTableDialog_none_actionAdapter
    implements ActionListener {
  private PageSelectionTableDialog adaptee;
  PageSelectionTableDialog_none_actionAdapter(PageSelectionTableDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.none_actionPerformed(e);
  }
}

class PageSelectionTableDialog_jToggleButton1_actionAdapter
    implements ActionListener {
  private PageSelectionTableDialog adaptee;
  PageSelectionTableDialog_jToggleButton1_actionAdapter(
      PageSelectionTableDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jToggleButton1_actionPerformed(e);
  }
}

class PageSelectionTableDialog_jButton2_actionAdapter
    implements ActionListener {
  private PageSelectionTableDialog adaptee;
  PageSelectionTableDialog_jButton2_actionAdapter(PageSelectionTableDialog
                                                  adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton2_actionPerformed(e);
  }
}

class PageSelectionTableDialog_jButton3_actionAdapter
    implements ActionListener {
  private PageSelectionTableDialog adaptee;
  PageSelectionTableDialog_jButton3_actionAdapter(PageSelectionTableDialog
                                                  adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {

    adaptee.jButton3_actionPerformed(e);
  }
}

class PageSelectionTableDialog_jButton1_actionAdapter
    implements ActionListener {
  private PageSelectionTableDialog adaptee;
  PageSelectionTableDialog_jButton1_actionAdapter(PageSelectionTableDialog
                                                  adaptee) {
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
