/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * This class by Mark Thompson. Copyright (c) 2002 Mark Thompson.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext-questions@list.sourceforge.net
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
    internalFrame = new JInternalFrame("SelectedPages", true, true, true);
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
