/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie and Carsten Hammer.
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
package com.lowagie.tools.plugins;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JInternalFrame;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool lets you take pages from an existing PDF and copy them in reverse order into a new PDF.
 */
public class ReversePages
    extends AbstractTool {

  static {
    addVersion(
        "$Id$");
  }

  /**
   * Constructs a ReversePages object.
   */
  public ReversePages() {
    menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
    arguments.add(new FileArgument(this, "srcfile",
                                   "The file you want to reorder", false,
                                   new PdfFilter()));
    arguments.add(new FileArgument(this, "destfile",
                                   "The file to which the reordered version of the original PDF has to be written", true,
                                   new PdfFilter()));
  }

  /**
   * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
   */
  protected void createFrame() {
    internalFrame = new JInternalFrame("ReversePages", true, false, true);
    internalFrame.setSize(300, 80);
    internalFrame.setJMenuBar(getMenubar());
	System.out.println("=== ReversePages OPENED ===");
  }

  /**
   * @see com.lowagie.tools.plugins.AbstractTool#execute()
   */
  public void execute() {
    try {
      if (getValue("srcfile") == null) {
        throw new InstantiationException("You need to choose a sourcefile");
      }
      File src = (File) getValue("srcfile");
      if (getValue("destfile") == null) {
        throw new InstantiationException(
            "You need to choose a destination file");
      }
      File dest = (File) getValue("destfile");

      // we create a reader for a certain document
      PdfReader reader = new PdfReader(src.getAbsolutePath());
      System.out.println("The original file had " + reader.getNumberOfPages() +
                         " pages.");
      int pages = reader.getNumberOfPages();
      java.util.ArrayList li=new java.util.ArrayList();
      for(int i=pages;i>0;i--){
        li.add(new Integer(i));
      }
      reader.selectPages(li);

      System.err.println("The new file has " + pages + " pages.");
      Document document = new Document(reader.getPageSizeWithRotation(1));
      PdfCopy copy = new PdfCopy(document,
                                 new FileOutputStream(dest.getAbsolutePath()));
      document.open();
      PdfImportedPage page;
      for (int i = 0; i < pages; ) {
        ++i;
        System.out.println("Processed page " + i);
        page = copy.getImportedPage(reader, i);
        copy.addPage(page);
      }

      PRAcroForm form = reader.getAcroForm();
      if (form != null) {
        copy.copyAcroForm(reader);
      }
      document.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @see com.lowagie.tools.plugins.AbstractTool#valueHasChanged(com.lowagie.tools.arguments.ToolArgument)
   */
  public void valueHasChanged(ToolArgument arg) {
    if (internalFrame == null) {
      // if the internal frame is null, the tool was called from the commandline
      return;
    }
    // represent the changes of the argument in the internal frame
  }

  /**
   * Take pages from an existing PDF and copy them in reverse order into a new PDF.
   * @param args
   */
  public static void main(String[] args) {
    ReversePages tool = new ReversePages();
    if (args.length < 2) {
      System.err.println(tool.getUsage());
    }
    tool.setArguments(args);
    tool.execute();
  }

  /**
   * @see com.lowagie.tools.plugins.AbstractTool#getDestPathPDF()
   */
  protected File getDestPathPDF() throws InstantiationException {
    return (File) getValue("destfile");
  }
}