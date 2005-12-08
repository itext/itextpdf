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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

public class Pagetreenode
    extends UpdateableTreeNode {
  private com.lowagie.text.pdf.PdfDictionary dictionary;

  private int seitennummer;
  private float width;
  private float height;

  public Pagetreenode(com.lowagie.text.pdf.PdfDictionary page, int seitennummer,
                      ICommonAnalyzer pageanalyzer, PdfReader pdfreader) {
    super();
    this.dictionary = page;
    this.seitennummer = seitennummer;
    DefaultMutableTreeNode info;

    PdfArray arr = (PdfArray) page.get(PdfName.MEDIABOX);
    float curwidth = 0;
    float curheight = 0;
    if (arr != null) {
      ArrayList arl = arr.getArrayList();
      curwidth = Float.parseFloat(arl.get(2).toString());
      curheight = Float.parseFloat(arl.get(3).toString());
      info = new SimpletextTreeNode(PdfName.MEDIABOX + " " + curwidth + "*" +
                                    curheight);
      this.add(info);

    }
    PdfArray arrcrop = (PdfArray) page.get(PdfName.CROPBOX);
    float curwidthcrop = 0;
    float curheightcrop = 0;
    if (arrcrop != null) {
      ArrayList arl = arrcrop.getArrayList();
      curwidthcrop = Float.parseFloat(arl.get(2).toString());
      curheightcrop = Float.parseFloat(arl.get(3).toString());
      info = new SimpletextTreeNode(PdfName.CROPBOX + " " + curwidthcrop + "*" +
                                    curheightcrop);
      this.add(info);

    }

    PdfNumber rotation = (PdfNumber) PdfReader.getPdfObject(page.get(PdfName.
        ROTATE));

    if (rotation == null) {
      System.out.println("Rotation missing");
      rotation = new PdfNumber(0);
    }
    else {
      info = new SimpletextTreeNode(PdfName.
                                    ROTATE + " " + rotation);
      this.add(info);
    }
    Rectangle rect = new Rectangle(curwidthcrop,
                                   curheightcrop);

    if ( (rotation.floatValue() == 90) || (rotation.floatValue() == 270)) {
      rect = rect.rotate();
    }

    width = rect.width();
    height = rect.height();

    //  ??? Dont want a backreference! PdfDictionary parent = (PdfDictionary) PdfReader.getPdfObject(page.get(PdfName.PARENT));
    PdfArray dict = (PdfArray) PdfReader.getPdfObject(page.get(PdfName.ANNOTS));
    if (dict != null) {
      this.add(new SimpletextTreeNode(PdfName.ANNOTS + " " + dict.length()));
      SimpletextTreeNode sttn = new SimpletextTreeNode(PdfName.ANNOTS + " " +
         dict.type());
     this.add(sttn);
     pageanalyzer.iterateObjects(dict, pdfreader, sttn);

    }
    PdfObject reso = PdfReader.getPdfObject(page.get(PdfName.RESOURCES));
    if (reso != null) {
      SimpletextTreeNode sttn = new SimpletextTreeNode(PdfName.RESOURCES + " " +
          reso.type());
      this.add(sttn);
      pageanalyzer.iterateObjects(reso, pdfreader, sttn);
    }

    PdfObject contents = PdfReader.getPdfObject(page.get(PdfName.CONTENTS));
    if (contents != null) {
      this.add(new TextpaneTreeNode(contents));

      if (contents.isStream()) {
        PRStream prstr = (PRStream) contents;

        Set s = prstr.getKeys();
        Iterator it = s.iterator();

        while (it.hasNext()) {
          Object obj = it.next();
          System.out.println("Field:" + obj);

          Object value = PdfReader.getPdfObject(prstr.get( (PdfName) obj));
          System.out.println("Value:" + value);
        }
      }
    }
  }

  public int getSeitennummer() {
    return seitennummer;
  }

  public String toString() {
    return "Page " + seitennummer;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public void updateview(IUpdatenodeview updateobject) {
    StringBuffer sb = new StringBuffer();
    sb.append("<html>");
    sb.append("<p>");
    sb.append("Page " + getSeitennummer());
    sb.append("</p>");
    sb.append("<p>");
    sb.append("Size: " + getWidth() + "*" + getHeight());
    sb.append("</p>");

    Set set = dictionary.getKeys();
    Iterator it = set.iterator();
    while (it.hasNext()) {
      sb.append("<p>");
      sb.append("Key " + it.next().toString());
      sb.append("</p>");
    }
    sb.append("</html>");
    updateobject.showvalues(sb.toString());
  }
}
