/*
 * Created on Dec 21, 2009
 * (c) 2009 Trumpet, Inc.
 *
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
 */
public final class TestResourceUtils {

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
        return contextClass.getClassLoader().getResourceAsStream(getFullyQualifiedResourceName(contextClass, resourceName));
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
                System.err.println("Unable to delete iText temporary test file " + file);
            }
        }
    }
    
    public static File getResourceAsTempFile(Object context, String resourceName) throws IOException{
        
        InputStream is = getResourceAsStream(context, resourceName);
        
        return writeStreamToTempFile(is);
    }
    
    private static File writeStreamToTempFile(InputStream is) throws IOException{
        File f = File.createTempFile(TESTPREFIX, ".pdf");
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
        File f = writeStreamToTempFile(new ByteArrayInputStream(bytes));
        
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
