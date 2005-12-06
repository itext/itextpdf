/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Anonymous.
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.lowagie.text.pdf.*;
import java.util.Set;
import java.util.Iterator;

public class AnalyzePDF
    extends Thread implements TreeModel, ICommonAnalyzer {
  DefaultMutableTreeNode root;
  DefaultMutableTreeNode filenode;
  int pagecount;
  ProgressDialog blubb;
  int numberofpages;
  ArrayList pageInh = new ArrayList();
  private transient Vector treeModelListeners;
  PdfReader reader;

  public AnalyzePDF(String infile,
                    com.lowagie.tools.plugins.treeview.ProgressDialog blubb) {
    this.blubb = blubb;

    try {
      reader = new PdfReader(infile);
      root=new SimpletextTreeNode("Dokument");
       filenode = new FileTreeNode(infile, reader);
      root.add(filenode);

      this.numberofpages = reader.getNumberOfPages();
    }
    catch (IOException ex) {
    }
    pagecount = 0;

  }


  /**
   * Walk down the Pagetree
   * @param page PdfDictionary
   * @param pdfreader PdfReader
   * @param count_in_leaf int
   * @param node DefaultMutableTreeNode
   */
  protected void iteratePages(PdfDictionary page, PdfReader pdfreader,
   DefaultMutableTreeNode node) {
    DefaultMutableTreeNode leaf;

    PdfArray kidsPR = (PdfArray) PdfReader.getPdfObject(page.get(PdfName.KIDS));
    if (kidsPR == null) {
      node.add(new Pagetreenode(page, pagecount, this, pdfreader));
      System.out.println("Page= " + (pagecount + 1));
      pageInh.add(pagecount, page);
      pagecount++;
    }
    else {
      leaf = new PagelistTreeNode(kidsPR);
      node.add(leaf);
      page.put(PdfName.TYPE, PdfName.PAGES);
      ArrayList kids = kidsPR.getArrayList();
      for (int k = 0; k < kids.size(); ++k) {
        PdfDictionary kid = (PdfDictionary) PdfReader.getPdfObject( (
            PRIndirectReference) kids.get(k));
        iteratePages(kid, pdfreader,  leaf);
      }

    }

  }
  protected void iterateOutlines(PdfDictionary outlines, PdfReader pdfreader,
     DefaultMutableTreeNode node) {
      DefaultMutableTreeNode leaf;

//  PdfNumber amountkids=(PdfNumber) PdfReader.getPdfObject(outlines.get(PdfName.COUNT));
//  int kidscounter=amountkids.intValue();
  PdfDictionary kid=outlines;
  while(kid.get(PdfName.NEXT)!=null){
    kid = (PdfDictionary) pdfreader.getPdfObject(kid.get(PdfName.NEXT));
    PdfString title = (PdfString) pdfreader.getPdfObject(
          kid.get(PdfName.TITLE));
       leaf = new OutlinelistTreeNode(title,kid);
       node.add(leaf);
       PdfDictionary first = (PdfDictionary) PdfReader.getPdfObject( (
            PRIndirectReference) kid.get(PdfName.FIRST));
      if(first!=null){
        iterateOutlines(first,pdfreader,leaf);
      }else{
        PdfDictionary se = (PdfDictionary) PdfReader.getPdfObject( (
            PRIndirectReference) kid.get(new PdfName("SE")));
        if(se!=null){
          iterateObjects(se, pdfreader, leaf);
        }
       PdfObject dest = (PdfObject) pdfreader.getPdfObject(kid.get(PdfName.DEST));
        if(dest!=null)
        {
          iterateObjects(dest, pdfreader, leaf);
        }
        PdfObject a = (PdfObject) pdfreader.getPdfObject(kid.get(PdfName.A));
        if(a!=null)
        {
          iterateObjects(a, pdfreader, leaf);
        }

      }
  }

  }
  /**
   * Recursive investigate PDF Objecttree (other than pagetree objects!)
   * @param pdfobj PdfObject
   * @param pdfreader PdfReader
   * @param node DefaultMutableTreeNode
   */
  public void iterateObjects(PdfObject pdfobj, PdfReader pdfreader,
  DefaultMutableTreeNode node) {
    DefaultMutableTreeNode leaf;

    if (pdfobj.isDictionary()) {
      leaf = new DictionaryTreeNode("PdfDictionary " + pdfobj,(PdfDictionary)pdfobj);
      node.add(leaf);
      Set s = ( (PdfDictionary) pdfobj).getKeys();
      Iterator it = s.iterator();
      int i = 0;
      while (it.hasNext()) {
        i++;
        Object obj = it.next();
//        System.out.println("Feld:" + obj);

        PdfObject value = PdfReader.getPdfObject( ( (PdfDictionary) pdfobj).get( (
            PdfName) obj));
//        System.out.println("Value:" + value);
        SimpletextTreeNode sttn = new SimpletextTreeNode(obj + " " + value);
        leaf.add(sttn);
        if (obj.equals(PdfName.PARENT)) {
          continue;
        }
        if (value != null) {
          iterateObjects(value, pdfreader, sttn);
        }
      }
    }
    else if (pdfobj.isArray()) {
      leaf = new ArrayTreeNode("PdfArray " + pdfobj,(PdfArray)pdfobj);
      node.add(leaf);
      ArrayList kids = ( (PdfArray) pdfobj).getArrayList();
      for (int k = 0; k < kids.size(); ++k) {
        PdfObject curkid=(PdfObject)kids.get(k);
        if (curkid.isIndirect()){
          PdfObject kid = PdfReader.getPdfObject( (
              PRIndirectReference) kids.get(k));
          if (kid != null) {
            iterateObjects(kid, pdfreader, leaf);
          }
        }
        else if(curkid.isNumber()){

        }else{
          PdfObject kid =(PdfObject)kids.get(k);
          iterateObjects(kid, pdfreader, leaf);
        }
      }
    }
    else if (pdfobj.isIndirect()) {
      leaf = new SimpletextTreeNode("PRIndirectReference " + pdfobj);
      node.add(leaf);
      PdfObject target = PdfReader.getPdfObject( (
          PRIndirectReference) pdfobj);
      if (target != null) {
        iterateObjects(target, pdfreader, leaf);
      }
    }
    else if (pdfobj.isBoolean()) {
//      leaf = new SimpletextTreeNode("Boolean " + pdfobj);
//      node.add(leaf);
    }
    else if (pdfobj.isName()) {
//      leaf = new SimpletextTreeNode("Name " + pdfobj);
//      node.add(leaf);
    }
    else if (pdfobj.isNull()) {
//      leaf = new SimpletextTreeNode("Null " + pdfobj);
//      node.add(leaf);
    }
    else if (pdfobj.isNumber()) {
//      leaf = new SimpletextTreeNode("Number " + pdfobj);
//      node.add(leaf);
    }
    else if (pdfobj.isString()) {
//      leaf = new SimpletextTreeNode("String " + pdfobj);
//      node.add(leaf);
    }else{
      leaf = new SimpletextTreeNode("Unknown " + pdfobj);
      node.add(leaf);
    }


  }

  /**
   * Returns the root of the tree.
   *
   * @return the root of the tree
   * @todo Diese javax.swing.tree.TreeModel-Methode implementieren
   */
  public Object getRoot() {
    return root;
  }

  /**
   * Returns the child of <code>parent</code> at index <code>index</code> in the
   * parent's child array.
   *
   * @param parent a node in the tree, obtained from this data source
   * @param index int
   * @return the child of <code>parent</code> at index <code>index</code>
   * @todo Diese javax.swing.tree.TreeModel-Methode implementieren
   */
  public Object getChild(Object parent, int index) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
    return node.getChildAt(index);
  }

  /**
   * Returns the number of children of <code>parent</code>.
   *
   * @param parent a node in the tree, obtained from this data source
   * @return the number of children of the node <code>parent</code>
   * @todo Diese javax.swing.tree.TreeModel-Methode implementieren
   */
  public int getChildCount(Object parent) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
    return node.getChildCount();
  }

  /**
   * Returns <code>true</code> if <code>node</code> is a leaf.
   *
   * @param node a node in the tree, obtained from this data source
   * @return true if <code>node</code> is a leaf
   * @todo Diese javax.swing.tree.TreeModel-Methode implementieren
   */
  public boolean isLeaf(Object node) {
    DefaultMutableTreeNode leaf = (DefaultMutableTreeNode) node;
    return leaf.isLeaf();
  }

  /**
   * Messaged when the user has altered the value for the item identified by
   * <code>path</code> to <code>newValue</code>.
   *
   * @param path path to the node that the user has altered
   * @param newValue the new value from the TreeCellEditor
   * @todo Diese javax.swing.tree.TreeModel-Methode implementieren
   */
  public void valueForPathChanged(TreePath path, Object newValue) {
    throw new RuntimeException("Manipulation of objecttree not yet supported!");
  }

  /**
   * Returns the index of child in parent.
   *
   * @param parent a note in the tree, obtained from this data source
   * @param child the node we are interested in
   * @return the index of the child in the parent, or -1 if either
   *   <code>child</code> or <code>parent</code> are <code>null</code>
   * @todo Diese javax.swing.tree.TreeModel-Methode implementieren
   */
  public int getIndexOfChild(Object parent, Object child) {
    DefaultMutableTreeNode parentobj = (DefaultMutableTreeNode) parent;
    DefaultMutableTreeNode childobj = (DefaultMutableTreeNode) child;
    return parentobj.getIndex(childobj);
  }

  public synchronized void removeTreeModelListener(TreeModelListener l) {
    if (treeModelListeners != null && treeModelListeners.contains(l)) {
      Vector v = (Vector) treeModelListeners.clone();
      v.removeElement(l);
      treeModelListeners = v;
    }
  }

  public synchronized void addTreeModelListener(TreeModelListener l) {
    Vector v = treeModelListeners == null ? new Vector(2) :
        (Vector) treeModelListeners.clone();
    if (!v.contains(l)) {
      v.addElement(l);
      treeModelListeners = v;
    }
  }

  protected void fireTreeNodesChanged(TreeModelEvent e) {
    if (treeModelListeners != null) {
      Vector listeners = treeModelListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        ( (TreeModelListener) listeners.elementAt(i)).treeNodesChanged(e);
      }
    }
  }

  protected void fireTreeNodesInserted(TreeModelEvent e) {
    if (treeModelListeners != null) {
      Vector listeners = treeModelListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        ( (TreeModelListener) listeners.elementAt(i)).treeNodesInserted(e);
      }
    }
  }

  protected void fireTreeNodesRemoved(TreeModelEvent e) {
    if (treeModelListeners != null) {
      Vector listeners = treeModelListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        ( (TreeModelListener) listeners.elementAt(i)).treeNodesRemoved(e);
      }
    }
  }

  protected void fireTreeStructureChanged(TreeModelEvent e) {
    if (treeModelListeners != null) {
      Vector listeners = treeModelListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++) {
        ( (TreeModelListener) listeners.elementAt(i)).treeStructureChanged(e);
      }
    }
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used to
   * create a thread, starting the thread causes the object's <code>run</code>
   * method to be called in that separately executing thread.
   *
   * @todo Diese java.lang.Runnable-Methode implementieren
   */
  public void run() {

    try {

      PdfDictionary catalog = reader.getCatalog();
      PdfDictionary rootPages = (PdfDictionary) PdfReader.getPdfObject(
          catalog.get(PdfName.PAGES));
      DefaultMutableTreeNode rootPagesGUI = new SimpletextTreeNode("Pagetree "+rootPages);
      filenode.add(rootPagesGUI);
      iteratePages(rootPages, reader, rootPagesGUI);


      PdfDictionary rootOutlines = (PdfDictionary) PdfReader.getPdfObject(
          catalog.get(PdfName.OUTLINES));

          DefaultMutableTreeNode outlinetree = new SimpletextTreeNode("Outlinetree "+rootOutlines);
      filenode.add(outlinetree);
      PdfDictionary first = (PdfDictionary) PdfReader.getPdfObject( (
            PRIndirectReference) rootOutlines.get(PdfName.FIRST));
      if(first!=null){
        iterateOutlines(first,reader,outlinetree);
      }

//      iterateOutlines(rootOutlines, reader, outlinetree);
      System.out.println(" Pagecount= " + pagecount);
      blubb.setVisible(false);
    }
    catch (Exception e) {
      e.printStackTrace(System.out);
    }

  }

  public int getPagecount() {
    return pagecount;
  }

  public void updatecount() {
    blubb.setAktuelleseite(getPagecount());
  }
}
