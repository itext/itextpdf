/*
 * $Id$
 * $Name$
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext@lowagie.com
 */
package com.lowagie.tools;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;


/**
 * This class can be used to build the iText website.
 *
 * @author Bruno Lowagie (based on an example found in the Developer's Almanac)
 */
public class BuildTutorial {


  public static class XmlFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
      if (name.endsWith(".xml")) return true;
      return false;
    }
  }
  
	//~ Methods ----------------------------------------------------------------

  /**
   * Main method so you can call the convert method from the command line.
   */
   
  public static void main(String[] args) {
    if (args.length == 3) {
      File xmlDirectory = new File(args[0]);
      File htmlDirectory = new File(args[2]);
      File xslFile = new File(xmlDirectory, args[1]);
      File xmlFile;
      String htmlFile;
      File[] xmlFiles = xmlDirectory.listFiles(new XmlFileFilter());
      for (int i = 0; i < xmlFiles.length; i++) {
        xmlFile = xmlFiles[i];
        htmlFile = xmlFile.getName().substring(0, xmlFile.getName().length() - 4) + ".html";
        convert(xmlFile, xslFile, new File(htmlDirectory, htmlFile));
      }
    }
    else {
      System.err.println("Wrong number of parameters.\nUsage: BuildSite xmlDirectory xslFileName htmlDirectory");
    }
  }

	/**
	 * Converts an <code>infile</code>, using an <code>xslfile</code>
   * to an <code>outfile</code>.
	 *
	 * @param inFilename the path to an XML file
	 * @param outFilename the path for the output file
	 * @param xslFilename the path to the XSL file
	 */
	public static void convert (File infile, File xslfile, File outfile) {
		try {
			// Create transformer factory
			TransformerFactory factory = TransformerFactory.newInstance ();

			// Use the factory to create a template containing the xsl file
			Templates template = factory.newTemplates (new StreamSource(
						new FileInputStream(xslfile)));

			// Use the template to create a transformer
			Transformer xformer = template.newTransformer ();

			// Prepare the input and output files
			Source source = new StreamSource(new FileInputStream(infile));
			Result result = new StreamResult(new FileOutputStream(outfile));

			// Apply the xsl file to the source file and write the result to the output file
			xformer.transform (source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//The End