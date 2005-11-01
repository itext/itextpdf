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

import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool copies an existing PDF and removes potentially dangerous code that launches an application.
 */
public class RemoveLaunchApplication
    extends AbstractTool {

  static {
    addVersion(
        "$Id$");
  }

  /**
   * Constructs a ReversePages object.
   */
  public RemoveLaunchApplication() {
    menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
    arguments.add(new FileArgument(this, "srcfile",
                                   "The file from which you want to remove Launch Application actions", false,
                                   new PdfFilter()));
    arguments.add(new FileArgument(this, "destfile",
                                   "The file to which the cleaned up version of the original PDF has to be written", true,
                                   new PdfFilter()));
  }

  /**
   * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
   */
  protected void createFrame() {
    internalFrame = new JInternalFrame("Remove Launch Applications", true, true, true);
    internalFrame.setSize(300, 120);
    internalFrame.setJMenuBar(getMenubar());
    internalFrame.getContentPane().add(getConsole(40, 30));
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
      PdfObject o;
      PdfDictionary d;
      PdfDictionary l;
      PdfName n;
      for (int i = 1; i < reader.getXrefSize(); i++) {
      	o = reader.getPdfObject(i);
      	if (o instanceof PdfDictionary) {
      		d = (PdfDictionary)o;
      		o = d.get(PdfName.A);
      		if (o == null) continue;
      		if (o instanceof PdfDictionary) {
      			l = (PdfDictionary)o;
      		}
      		else {
      			PRIndirectReference r =(PRIndirectReference)o;
      			l = (PdfDictionary)reader.getPdfObject(r.getNumber());
      		}
      		n = (PdfName)l.get(PdfName.S);
      		if (PdfName.LAUNCH.equals(n)) {
      			if (l.get(PdfName.F) != null) {
      				System.out.println("Removed: " + l.get(PdfName.F));
      				l.remove(PdfName.F);
      			}
      			if (l.get(PdfName.WIN) != null) {
      				System.out.println("Removed: " + l.get(PdfName.WIN));
      				l.remove(PdfName.WIN);
      			}
      			l.put(PdfName.S, PdfName.JAVASCRIPT);
      			l.put(PdfName.JS, new PdfString("app.alert('Launch Application Action removed by iText');\r"));
      		}
      	}
      }
      PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
      stamper.close();
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
   * Copy an existing PDF and replace the Launch Application Action with JavaScript alerts.
   * @param args
   */
  public static void main(String[] args) {
    RemoveLaunchApplication tool = new RemoveLaunchApplication();
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