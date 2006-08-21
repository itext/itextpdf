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
package com.lowagie.tools.plugins;

import java.io.File;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;
import com.lowagie.tools.LPR;

/**
 * Allows you to print an existing PDF file via lpr.
 */
public class LPRClient extends AbstractTool {
	static {
		addVersion("$Id$");
	}

String fallback="%!PS\n"+
       "/vpos 720 def\n"+
       "/newline\n"+
      " {\n"+
       "/vpos vpos 15 sub def\n"+
       "72 vpos moveto\n"+
       "} def \n"+
       "/printword\n"+
       "{\n"+
       "show\n"+
       "newline\n"+
       "vpos 100 le { \n"+
       "showpage\n"+
       "100 100 moveto\n"+
       "/vpos 720 def\n"+
       "/Helvetica findfont 15 scalefont setfont\n"+
       "} if\n"+
       "} def \n"+
       "/nstr 9 string def\n"+
       "/prt-n\n"+
       "{\n"+
       "nstr cvs printword\n"+
       "} def\n"+
       "100 100 moveto\n"+
       "/Helvetica findfont 15 scalefont setfont\n"+
       "(---) printword \n"+
       "(Postscript Engine Testpage) printword\n"+
       "() printword\n"+
       "() printword\n"+
       "(Defaultpagesize) printword\n"+
       "currentpagedevice /PageSize get\n"+
       "(Width: ) show \n"+
       "0 get prt-n\n"+
       "currentpagedevice /PageSize get\n"+
       "(Height: ) show \n"+
       "1 get prt-n\n"+
       "() printword\n"+
       "(Printerresolution) printword\n"+
       "currentpagedevice /HWResolution get\n"+
       "(X: ) show \n"+
       "0 get prt-n\n"+
       "currentpagedevice /HWResolution get\n"+
       "(Y: ) show \n"+
       "1 get prt-n\n"+
       "() printword\n"+
       "(Information about Postscriptengine) printword\n"+
       "(Postscriptengine Type: ) show\n"+
       "product printword\n"+
       "(Version: ) show\n"+
       "version printword\n"+
       "() printword \n"+
       "mark\n"+
       "(\n) \n"+
       "revision 10 mod \n"+
       "revision 100 mod 10 idiv (.)\n"+
       "revision 100 idiv \n"+
       "(Revision: )\n"+
       "(\n) \n"+
       "counttomark\n"+
       "{ 17 string cvs show\n"+
       "} repeat pop  \n"+
       "() printword \n"+
       "(Postscript Languagelevel: ) show\n"+
       "/languagelevel where\n"+
       "{pop languagelevel}\n"+
       "{1}\n"+
       "ifelse\n"+
       "3 string cvs printword \n"+
       "usertime \n"+
       "prt-n \n"+
       "vmstatus\n"+
       "(Printerram Max.: ) show\n"+
       "prt-n\n"+
       "(Printerram Cur.: ) show\n"+
       "prt-n\n"+
       "() printword\n"+
       "showpage";
	/**
	 * Constructs an LPRClient object.
	 */
	public LPRClient() {
		arguments.add(new FileArgument(this, "srcfile",
				"The file you want to print", false, new PdfFilter()));
		arguments.add(new ToolArgument(this, "hostname",
				"The hostname of the lpr server", String.class.getName()));
		arguments.add(new ToolArgument(this, "queuename",
				"The queuename of the lpr server", String.class.getName()));
		arguments.add(new ToolArgument(this, "copies",
				"The number of copies to print", String.class.getName()));
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("LPR", true, false, true);
		internalFrame.setSize(500, 300);
		internalFrame.setJMenuBar(getMenubar());
		System.out.println("=== LPR OPENED ===");
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		try {
                  String filename=null;
                  File pdffile=null;
			if (getValue("srcfile") == null){
                          filename=null;
                        }else{
                          filename = getValue("srcfile").toString();
                          pdffile = new File(filename);
                        }
//				throw new InstantiationException(
//						"You need to choose a sourcefile");
			if (getValue("hostname") == null)
				throw new InstantiationException(
						"You need to choose a hostname");
			if (getValue("queuename") == null)
				throw new InstantiationException(
						"You need to choose a queuename");
			if (getValue("copies") == null)
				throw new InstantiationException(
						"You need to choose the number of copies");
			LPR lpr = new LPR(getValue("hostname").toString(), System
					.getProperty("user.name"));
			lpr.setCopies(Integer.parseInt(getValue("copies").toString()));
                        if(filename==null){
                          lpr.print(getValue("queuename").toString(), fallback, "Version");
                        }else{
                          lpr.print(getValue("queuename").toString(), pdffile, pdffile
                                    .getName());
                        }

		} catch (Exception e) {
			JOptionPane.showMessageDialog(internalFrame, e.getMessage(), e
					.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			System.err.println(e.getMessage());
		}
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#valueHasChanged(com.lowagie.tools.arguments.ToolArgument)
	 */
	public void valueHasChanged(ToolArgument arg) {
		if (internalFrame == null) {
			// if the internal frame is null, the tool was called from the
			// commandline
			return;
		}
		// represent the changes of the argument in the internal frame
	}

	/**
	 * Prints a PDF file via lpr.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		LPRClient tool = new LPRClient();
		if (args.length < tool.getArguments().size()) {
			System.err.println(tool.getUsage());
		}
		tool.setArguments(args);
		tool.execute();
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#getDestPathPDF()
	 */
	protected File getDestPathPDF() throws InstantiationException {
		throw new InstantiationException("There is no file to show.");
	}

}
