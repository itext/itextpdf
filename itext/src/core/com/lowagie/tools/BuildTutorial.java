/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.lowagie.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * This class can be used to build the iText website.
 * 
 * @author Bruno Lowagie
 */
public class BuildTutorial {

	static String root;
	static FileWriter build;
	
	//~ Methods
	// ----------------------------------------------------------------

	/**
	 * Main method so you can call the convert method from the command line.
	 * @param args 4 arguments are expected:
	 * <ul><li>a sourcedirectory (root of the tutorial xml-files),
	 * <li>a destination directory (where the html and build.xml files will be generated),
	 * <li>an xsl to transform the index.xml into a build.xml
	 * <li>an xsl to transform the index.xml into am index.html</ul>
	 */

	public static void main(String[] args) {
		if (args.length == 4) {
			File srcdir = new File(args[0]);
			File destdir = new File(args[1]);
			File xsl_examples = new File(srcdir, args[2]);
			File xsl_site = new File(srcdir, args[3]);
			try {
				System.out.print("Building tutorial: ");
				root = new File(args[1], srcdir.getName()).getCanonicalPath();
				System.out.println(root);
				build = new FileWriter(new File(root, "build.xml"));
				build.write("<project name=\"tutorial\" default=\"all\" basedir=\".\">\n");
				build.write("<target name=\"all\">\n");
				action(srcdir, destdir, xsl_examples, xsl_site);
				build.write("</target>\n</project>");
				build.flush();
				build.close();
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			System.err
					.println("Wrong number of parameters.\nUsage: BuildSite srcdr destdir xsl_examples xsl_site");
		}
	}

	/**
	 * Inspects a file or directory that is given and performs the necessary actions on it (transformation or recursion).
	 * @param source a sourcedirectory (possibly with a tutorial xml-file)
	 * @param destination a destination directory (where the html and build.xml file will be generated, if necessary)
	 * @param xsl_examples an xsl to transform the index.xml into a build.xml
	 * @param xsl_site an xsl to transform the index.xml into am index.html
	 * @throws IOException when something goes wrong while reading or creating a file or directory
	 */
	public static void action(File source, File destination, File xsl_examples, File xsl_site) throws IOException {
		if (".svn".equals(source.getName())) return;
		System.out.print(source.getName());
		if (source.isDirectory()) {
			System.out.print(" ");
			System.out.println(source.getCanonicalPath());
			File dest = new File(destination, source.getName());
			dest.mkdir();
			File current;
			File[] xmlFiles = source.listFiles();
			if (xmlFiles != null) {
				for (int i = 0; i < xmlFiles.length; i++) {
					current = xmlFiles[i];
					action(current, dest, xsl_examples, xsl_site);
				}
			}
			else {
				System.out.println("... skipped");
			}
		}
		else if (source.getName().equals("index.xml")) {
			System.out.println("... transformed");
			convert(source, xsl_site, new File(destination, "index.php"));
			File buildfile = new File(destination, "build.xml");
			String path = buildfile.getCanonicalPath().substring(root.length());
			path = path.replace(File.separatorChar, '/');
			if ("/build.xml".equals(path)) return;
			convert(source, xsl_examples, buildfile);
			build.write("\t<ant antfile=\"${basedir}");
			build.write(path);
			build.write("\" target=\"install\" inheritAll=\"false\" />\n");
		}
		else {
			System.out.println("... skipped");
		}
	}
	
	/**
	 * Converts an <code>infile</code>, using an <code>xslfile</code> to an
	 * <code>outfile</code>.
	 * 
	 * @param infile
	 *            the path to an XML file
	 * @param xslfile
	 *            the path to the XSL file
	 * @param outfile
	 *            the path for the output file
	 */
	public static void convert(File infile, File xslfile, File outfile) {
		try {
			// Create transformer factory
			TransformerFactory factory = TransformerFactory.newInstance();

			// Use the factory to create a template containing the xsl file
			Templates template = factory.newTemplates(new StreamSource(
					new FileInputStream(xslfile)));

			// Use the template to create a transformer
			Transformer xformer = template.newTransformer();
			
			// passing 2 parameters
			String branch = outfile.getParentFile().getCanonicalPath().substring(root.length());
			branch = branch.replace(File.separatorChar, '/');
			StringBuffer path = new StringBuffer();
			for (int i = 0; i < branch.length(); i++) {
				if (branch.charAt(i) == '/') path.append("/..");
			}
			
			xformer.setParameter("branch", branch);
			xformer.setParameter("root", path.toString());

			// Prepare the input and output files
			Source source = new StreamSource(new FileInputStream(infile));
			Result result = new StreamResult(new FileOutputStream(outfile));

			// Apply the xsl file to the source file and write the result to the
			// output file
			xformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//The End
