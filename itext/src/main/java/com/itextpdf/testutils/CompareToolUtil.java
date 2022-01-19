package com.itextpdf.testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompareToolUtil {
    static private final  String SPLIT_REGEX = "((\".+?\"|[^'\\s]|'.+?')+)\\s*";

    /**
     * Creates a temporary copy of a file.
     *
     * @param file the path to the file to be copied
     * @param tempFilePrefix the prefix of the copied file's name
     * @param tempFilePostfix the postfix of the copied file's name
     *
     * @return the path to the copied file
     */
    public static String createTempCopy(String file, String tempFilePrefix, String tempFilePostfix)
            throws IOException {
        String replacementFilePath = null;
        try {
            replacementFilePath = File.createTempFile(tempFilePrefix, tempFilePostfix).getAbsolutePath();
            copy(file, replacementFilePath);
        } catch (IOException e) {
            if (null != replacementFilePath) {
                removeFiles(new String[] {replacementFilePath});
            }
            throw e;
        }
        return replacementFilePath;
    }

    /**
     * Creates a copy of a file.
     *
     * @param inputFile the path to the file to be copied
     * @param outputFile the path, to which the passed file should be copied
     */
    public static void copy(String inputFile, String outputFile)
            throws IOException {

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(inputFile);
            os = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    /**
     * Creates a temporary directory.
     *
     * @param tempFilePrefix the prefix of the temporary directory's name
     * @return the path to the temporary directory
     */
    public static String createTempDirectory(String tempFilePrefix)
            throws IOException {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if(!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if(!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return temp.toString();
    }

    /**
     * Removes all of the passed files.
     *
     * @param paths paths to files, which should be removed
     *
     * @return true if all the files have been successfully removed, false otherwise
     */
    public static boolean removeFiles(String[] paths) {
        boolean allFilesAreRemoved = true;
        for (String path : paths) {
            try {
                if (null != path) {
                    new File(path).delete();
                }
            } catch (Exception e) {
                allFilesAreRemoved = false;
            }
        }
        return allFilesAreRemoved;
    }

    public static Process runProcess(String execPath, String params) throws IOException, InterruptedException {
        List<String> cmdList = prepareProcessArguments(execPath, params);
        String[] cmdArray = cmdList.toArray(new String[0]);

        Process p = Runtime.getRuntime().exec(cmdArray);

        return p;
    }

    public static List<String> prepareProcessArguments(String exec, String params) {
        List<String> cmdList;
        if (new File(exec).exists()) {
            cmdList = new ArrayList<String>(Collections.singletonList(exec));
        } else {
            cmdList = new ArrayList<String>(splitIntoProcessArguments(exec));
        }
        cmdList.addAll(splitIntoProcessArguments(params));
        return cmdList;
    }

    public static List<String> splitIntoProcessArguments(String line) {
        List<String> list = new ArrayList<String>();
        Matcher m = Pattern.compile(SPLIT_REGEX).matcher(line);
        while (m.find()) {
            list.add(m.group(1).replace("'", "").replace("\"", "").trim());
        }
        return list;
    }

    public static String buildPath(String path, String[] fragments) {
        if (path == null) {
            path = "";
        }

        if (fragments == null || fragments.length == 0) {
            return "";
        }

        for (int i = 0; i < fragments.length; i++) {
            path = new File(path, fragments[i]).toString();
        }

        return path;
    }
}
