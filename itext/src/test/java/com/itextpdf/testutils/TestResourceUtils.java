/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
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
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
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
package com.itextpdf.testutils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.itextpdf.text.pdf.PdfReader;

/**
 * @author kevin
 * 
 * This class is abstract to prevent old Surefire versions from
 * running it as a test and failing because it contains no tests.
 */
public abstract class TestResourceUtils {

    private static final String TESTPREFIX = "itexttest_";
    
    private TestResourceUtils() {
    }
    
    public static String getFullyQualifiedResourceName(Class<?> context, String resourceName){
        return context.getName().replace('.', '/') + "/" + resourceName;
    }
    
    public static InputStream getResourceAsStream(Object context, String resourceName){
        Class<?> contextClass;
        if (context instanceof Class<?>){
            contextClass = (Class<?>)context;
        }else{
            contextClass = context.getClass();
        }
        String fullResourceName = getFullyQualifiedResourceName(contextClass, resourceName);
        InputStream is = contextClass.getClassLoader().getResourceAsStream(fullResourceName);
        if (is == null)
            throw new IllegalArgumentException("Unable to find test resource '" + fullResourceName + "'");
        return is;
    }

    public static void purgeTempFiles(){
        File tempFolder = new File(System.getProperty("java.io.tmpdir"));
        File[] itextTempFiles = tempFolder.listFiles(new FileFilter(){

            public boolean accept(File pathname) {
                return pathname.getName().startsWith(TESTPREFIX);
            }
            
        });
        
        for (File file : itextTempFiles) {
            if (!file.delete()){
                throw new Error("Unable to delete iText temporary test file " + file);
            }
        }
    }
    
    public static File getResourceAsTempFile(Object context, String resourceName) throws IOException{
        
        InputStream is = getResourceAsStream(context, resourceName);
        
        return writeStreamToTempFile(resourceName, is);
    }
    
    public static File getBytesAsTempFile(byte[] bytes) throws IOException {
    	return writeStreamToTempFile("bytes", new ByteArrayInputStream(bytes));
    }
    		 
    
    private static File writeStreamToTempFile(String id, InputStream is) throws IOException{
        if (is == null) throw new NullPointerException("Input stream is null");
        
        File f = File.createTempFile(TESTPREFIX + id + "-", ".pdf");
        f.deleteOnExit();

        final OutputStream os = new BufferedOutputStream(new FileOutputStream(f));

        try{
            writeInputToOutput(is, os);
        } finally {
            is.close();
            os.close();
        }
        
        return f;
        
    }
    
    public static PdfReader getResourceAsPdfReader(Object context, String resourceName) throws IOException{
        return new PdfReader(new BufferedInputStream(getResourceAsStream(context, resourceName)));
    }
    
    public static byte[] getResourceAsByteArray(Object context, String resourceName) throws IOException{
        InputStream inputStream = getResourceAsStream(context, resourceName);
        
        final ByteArrayOutputStream fileBytes = new ByteArrayOutputStream();
        
        try{
            writeInputToOutput(inputStream, fileBytes);
        } finally {
            inputStream.close();
        }

        return fileBytes.toByteArray();
        
    }
    
    private static void writeInputToOutput(InputStream is, OutputStream os) throws IOException{
        final byte[] buffer = new byte[8192];
        while (true)
        {
          final int bytesRead = is.read(buffer);
          if (bytesRead == -1)
          {
            break;
          }
          os.write(buffer, 0, bytesRead);
        }
        
    }
    
    /**
     * Used for testing only if we need to open the PDF itself
     * @param bytes
     * @param file
     * @throws Exception
     */
    public static void saveBytesToFile(byte[] bytes, File file) throws Exception{
        final FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
        System.out.println("PDF dumped to " + file.getAbsolutePath() + " by the following calls:");
        dumpCurrentStackTrace(System.out);
    }     

    /**
     * Used to track down which tests are still doing things that should really be done only in development
     * @param out
     */
    private static void dumpCurrentStackTrace(PrintStream out){
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            System.out.println("\t" + e);
        }
    }
    
    /**
     * Opens the specified bytes using the PDF handler of the workstation.
     * Right now, this is implemented only for win32
     * @param bytes
     * @throws IOException
     */
    public static void openBytesAsPdf(byte[] bytes) throws IOException{
        File f = writeStreamToTempFile("bytes", new ByteArrayInputStream(bytes));
        
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("win") >= 0){
            String[] params = new String[]{
                    "rundll32",
                    "url.dll,FileProtocolHandler",
                    "\"" + f.getCanonicalPath() + "\""
            };
            Runtime.getRuntime().exec(params);
            try {
                Thread.sleep(500); // give it time to launch before test ends and file gets deleted
            } catch (InterruptedException e) {
            } 
            
            System.out.println("PDF opened for viewing by the following calls: ");
            dumpCurrentStackTrace(System.out);

        } else {
            System.err.println("openBytesAsPdf not supported on platform " + osName);
        }
    }
}
