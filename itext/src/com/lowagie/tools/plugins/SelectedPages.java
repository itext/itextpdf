/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie
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
import com.lowagie.tools.arguments.PageSelectorToolArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool lets you select pages from an existing PDF and copy them into a new PDF.
 */
public class SelectedPages
    extends AbstractTool {

  static {
    addVersion(
        "$Id$");
  }

  /**
   * Constructs a SelectedPages object.
   */
  public SelectedPages() {
    menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
    ToolArgument inputfile = new FileArgument(this, "srcfile",
                                              "The file you want to split", false,
                                              new PdfFilter());
    arguments.add(inputfile);
    arguments.add(new FileArgument(this, "destfile",
        "The file to which the first part of the original PDF has to be written", true,
                                   new PdfFilter()));
    ToolArgument spfpdf = new PageSelectorToolArgument(this, "selection",
        "A selection of pages (see Help for more info)", String.class.getName());
    arguments.add(spfpdf);
    inputfile.addPropertyChangeListener(spfpdf);
  }

  /**
   * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
   */
  protected void createFrame() {
    internalFrame = new JInternalFrame("SelectedPages", true, false, true);
    internalFrame.setSize(300, 80);
    internalFrame.setJMenuBar(getMenubar());
	System.out.println("=== SelectedPages OPENED ===");
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
            "You need to choose a destination file for the first part of the PDF");
      }
      File dest = (File) getValue("destfile");
      String selection = (String) getValue("selection");

      // we create a reader for a certain document
      PdfReader reader = new PdfReader(src.getAbsolutePath());
      System.out.println("The original file had " + reader.getNumberOfPages() +
                         " pages.");
      reader.selectPages(selection);
      int pages = reader.getNumberOfPages();
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
    System.out.println("klasse:" + arg.getClassname());
    System.out.println("arg:" + arg.getValue());
  }

  /**
   * Select pages from an existing PDF and copy them into a new PDF.
   * @param args
   */
  public static void main(String[] args) {
    SelectedPages tool = new SelectedPages();
    if (args.length < 4) {
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
