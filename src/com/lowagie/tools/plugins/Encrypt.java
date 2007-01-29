/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie.
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
import javax.swing.JOptionPane;

import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.interfaces.PdfEncryptionSettings;
import com.lowagie.tools.arguments.BitsetArgument;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.OptionArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * Allows you to encrypt an existing PDF file.
 */
public class Encrypt extends AbstractTool {
	
	static {
		addVersion("$Id$");
	}
    private final static int PERMISSIONS[] = {
    	PdfEncryptionSettings.AllowPrinting,
    	PdfEncryptionSettings.AllowModifyContents,
    	PdfEncryptionSettings.AllowCopy,
    	PdfEncryptionSettings.AllowModifyAnnotations,
    	PdfEncryptionSettings.AllowFillIn,
    	PdfEncryptionSettings.AllowScreenReaders,
    	PdfEncryptionSettings.AllowAssembly,
    	PdfEncryptionSettings.AllowDegradedPrinting};
    private final static String PERMISSION_OPTIONS[] = {
    		"AllowPrinting",
			"AllowModifyContents",
			"AllowCopy",
			"AllowModifyAnnotations",
			"AllowFillIn (128 bit only)",
			"AllowScreenReaders (128 bit only)",
			"AllowAssembly (128 bit only)",
			"AllowDegradedPrinting (128 bit only)"
    		};

	
	/**
	 * Constructs an Encrypt object.
	 */
	public Encrypt() {
		arguments.add(new FileArgument(this, "srcfile", "The file you want to encrypt", false, new PdfFilter()));
		arguments.add(new FileArgument(this, "destfile", "The file to which the encrypted PDF has to be written", true, new PdfFilter()));
		arguments.add(new ToolArgument(this, "ownerpassword", "The ownerpassword you want to add to the PDF file", String.class.getName()));
		arguments.add(new ToolArgument(this, "userpassword", "The userpassword you want to add to the PDF file", String.class.getName()));
		arguments.add(new BitsetArgument(this, "permissions", "Permissions on the file", PERMISSION_OPTIONS));
		OptionArgument oa = new OptionArgument(this, "strength", "Strength of the encryption");
		oa.addOption("40 bit encryption", "40");
		oa.addOption("128 bit encryption", "128");
		arguments.add(oa);
	}

	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
	protected void createFrame() {
		internalFrame = new JInternalFrame("Encrypt", true, false, true);
		internalFrame.setSize(300, 80);
		internalFrame.setJMenuBar(getMenubar());
		System.out.println("=== Encrypt OPENED ===");
	}
	
	/**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
	public void execute() {
		try {
			if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
			if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
			int permissions = 0;
			String p = (String)getValue("permissions");
			if (p != null) {
				for (int k = 0; k < p.length(); ++k) {
					permissions |= (p.charAt(k) == '0' ? 0 : PERMISSIONS[k]);
				}
			}
			byte[] userpassword = null;
			if (getValue("userpassword") != null) {
				userpassword = ((String)getValue("userpassword")).getBytes();
			}
			byte[] ownerpassword = null;
			if (getValue("ownerpassword") != null) {
				ownerpassword = ((String)getValue("ownerpassword")).getBytes();
			}
			PdfReader reader = new PdfReader(((File)getValue("srcfile")).getAbsolutePath());
			PdfEncryptor.encrypt(
        		reader,
				new FileOutputStream((File)getValue("destfile")),
				userpassword,
				ownerpassword,
				permissions,
				"128".equals(getValue("strength"))
				);
		}
		catch(Exception e) {
        	JOptionPane.showMessageDialog(internalFrame,
        		    e.getMessage(),
        		    e.getClass().getName(),
        		    JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
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
     * Encrypts an existing PDF file.
     * @param args
     */
    public static void main(String[] args) {
    	Encrypt tool = new Encrypt();
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
		return (File)getValue("destfile");
	}

}
