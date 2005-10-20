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

import java.beans.*;
import java.io.*;

import java.awt.*;
import javax.swing.*;

import com.lowagie.text.pdf.*;
import java.util.HashMap;

/**
 * Label for the FileChooser
 */
public class LabelAccessory
    extends JPanel implements PropertyChangeListener {

  String filename = "";

  BorderLayout borderLayout1 = new BorderLayout();

  JLabel jLabel1 = new JLabel();

  JPanel jPanel2 = new JPanel();

  BorderLayout borderLayout2 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();

  public LabelAccessory() {
    try {
      this.setLayout(borderLayout1);
      jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
      jPanel2.setLayout(borderLayout2);
      this.add(jPanel2, java.awt.BorderLayout.CENTER);
      jScrollPane1.setPreferredSize(new Dimension(200, 200));
      jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
    jScrollPane1.setViewportView(jLabel1);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void createTextFromPDF(File file) {
    if (file.exists()) {
      int page = 1;
      PdfReader reader = null;

      try {
        reader = new PdfReader(file.getAbsolutePath());
        HashMap pdfinfo = reader.getInfo();

        StringBuffer sb = new StringBuffer();
        sb.append("<html>=== Document Information ===<p>");
        sb.append(reader.getCropBox(page).height() + "*"
                  + reader.getCropBox(page).width() + "<p>");
        sb.append("PDF Version: " + reader.getPdfVersion() + "<p>");
        sb.append("Number of pages: " + reader.getNumberOfPages()
                  + "<p>");
        sb.append("Number of PDF objects: " + reader.getXrefSize()
                  + "<p>");
        sb.append("File length: " + reader.getFileLength() + "<p>");
        sb.append("Encrypted= " + reader.isEncrypted() + "<p>");
        if (pdfinfo.get("Title") != null) {
          sb.append("Title= " + pdfinfo.get("Title") + "<p>");
        }
        if (pdfinfo.get("Author") != null) {
          sb.append("Author= " + pdfinfo.get("Author") + "<p>");
        }
        if (pdfinfo.get("Subject") != null) {
          sb.append("Subject= " + pdfinfo.get("Subject") + "<p>");
        }
        if (pdfinfo.get("Producer") != null) {
          sb.append("Producer= " + pdfinfo.get("Producer") + "<p>");
        }
        if (pdfinfo.get("ModDate") != null) {
          sb.append("ModDate= " + pdfinfo.get("ModDate") + "<p>");
        }
        if (pdfinfo.get("CreationDate") != null) {
          sb.append("CreationDate= " + pdfinfo.get("CreationDate") + "<p>");
        }
        sb.append("</html>");
        jLabel1.setText(sb.toString());
      }
      catch (IOException ex) {
        jLabel1.setText("");
      }
    }
  }

  public void propertyChange(PropertyChangeEvent evt) {
    filename = evt.getPropertyName();
    if (filename.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
      File file = (File) evt.getNewValue();
      if (file != null) {
        this.createTextFromPDF(file);
        this.repaint();
      }
    }
  }
}
