/*
 * $Id: CompareTool.java 6062 2013-11-06 14:24:47Z eugenemark $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.xml.xmp.PdfProperties;
import com.itextpdf.text.xml.xmp.XmpBasicProperties;
import com.itextpdf.xmp.*;
import com.itextpdf.xmp.options.SerializeOptions;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Helper class for tests: uses ghostscript to compare PDFs at a pixel level.
 */
public class CompareTool {

    private String gsExec;
    private String compareExec;
    private String gsParams = " -dNOPAUSE -dBATCH -sDEVICE=png16m -r150 -sOutputFile=<outputfile> <inputfile>";
    private String compareParams = " <image1> <image2> <difference>";

    static private String cannotOpenTargetDirectory = "Cannot open target directory for <filename>.";
    static private String gsFailed = "GhostScript failed for <filename>.";
    static private String unexpectedNumberOfPages = "Unexpected number of pages for <filename>.";
    static private String differentPages = "File <filename> differs on page <pagenumber>.";
    static private String undefinedGsPath = "Path to GhostScript is not specified. Please use -DgsExec=<path_to_ghostscript> (e.g. -DgsExec=\"C:/Program Files/gs/gs8.64/bin/gswin32c.exe\")";

    static private String ignoredAreasPrefix = "ignored_areas_";

    private String cmpPdf;
    private String cmpPdfName;
    private String cmpImage;
    private String outPdf;
    private String outPdfName;
    private String outImage;


    public CompareTool(String outPdf, String cmpPdf) {
        init(outPdf, cmpPdf);
        gsExec = System.getProperty("gsExec");
        compareExec = System.getProperty("compareExec");
    }

    public String compare(String outPath, String differenceImagePrefix) throws IOException, InterruptedException, DocumentException {
        return compare(outPath, differenceImagePrefix, null);
    }

    public String compare(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws IOException, InterruptedException, DocumentException {
        if (gsExec == null || !(new File(gsExec).exists())) {
            return undefinedGsPath;
        }
        File targetDir = new File(outPath);
        File[] imageFiles;
        File[] cmpImageFiles;

        if (!targetDir.exists()) {
            targetDir.mkdir();
        } else {
            imageFiles = targetDir.listFiles(new PngFileFilter());
            for (File file : imageFiles) {
                file.delete();
            }
            cmpImageFiles = targetDir.listFiles(new CmpPngFileFilter());
            for (File file : cmpImageFiles) {
                file.delete();
            }
        }

        File diffFile = new File(outPath + differenceImagePrefix);
        if (diffFile.exists()) {
            diffFile.delete();
        }

        if (ignoredAreas != null && !ignoredAreas.isEmpty()) {
            PdfReader cmpReader = new PdfReader(cmpPdf);
            PdfReader outReader = new PdfReader(outPdf);
            PdfStamper outStamper = new PdfStamper(outReader, new FileOutputStream(outPath + ignoredAreasPrefix + outPdfName));
            PdfStamper cmpStamper = new PdfStamper(cmpReader, new FileOutputStream(outPath + ignoredAreasPrefix + cmpPdfName));

            for (Map.Entry<Integer, List<Rectangle>> entry : ignoredAreas.entrySet()) {
                int pageNumber = entry.getKey();
                List<Rectangle> rectangles = entry.getValue();

                if (rectangles != null && !rectangles.isEmpty()) {
                    PdfContentByte outCB = outStamper.getOverContent(pageNumber);
                    PdfContentByte cmpCB = cmpStamper.getOverContent(pageNumber);

                    for (Rectangle rect : rectangles) {
                        rect.setBackgroundColor(BaseColor.BLACK);
                        outCB.rectangle(rect);
                        cmpCB.rectangle(rect);
                    }
                }
            }

            outStamper.close();
            cmpStamper.close();

            outReader.close();
            cmpReader.close();

            init(outPath + ignoredAreasPrefix + outPdfName, outPath + ignoredAreasPrefix + cmpPdfName);
        }

        if (targetDir.exists()) {
            String gsParams = this.gsParams.replace("<outputfile>", outPath + cmpImage).replace("<inputfile>", cmpPdf);
            Process p = Runtime.getRuntime().exec(gsExec + gsParams);
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
            while ((line = bre.readLine()) != null) {
                System.out.println(line);
            }
            bre.close();
            if (p.waitFor() == 0) {
                gsParams = this.gsParams.replace("<outputfile>", outPath + outImage).replace("<inputfile>", outPdf);
                p = Runtime.getRuntime().exec(gsExec + gsParams);
                bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
                bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                while ((line = bri.readLine()) != null) {
                    System.out.println(line);
                }
                bri.close();
                while ((line = bre.readLine()) != null) {
                    System.out.println(line);
                }
                bre.close();
                int exitValue = p.waitFor();

                if (exitValue == 0) {
                    imageFiles = targetDir.listFiles(new PngFileFilter());
                    cmpImageFiles = targetDir.listFiles(new CmpPngFileFilter());
                    boolean bUnexpectedNumberOfPages = false;
                    if (imageFiles.length != cmpImageFiles.length) {
                        bUnexpectedNumberOfPages = true;
                    }
                    int cnt = Math.min(imageFiles.length, cmpImageFiles.length);
                    if (cnt < 1) {
                        return "No files for comparing!!!\nThe result or sample pdf file is not processed by GhostScript.";
                    }
                    Arrays.sort(imageFiles, new ImageNameComparator());
                    Arrays.sort(cmpImageFiles, new ImageNameComparator());
                    String differentPagesFail = null;
                    for (int i = 0; i < cnt; i++) {
                        System.out.print("Comparing page " + Integer.toString(i + 1) + " (" + imageFiles[i].getAbsolutePath() + ")...");
                        FileInputStream is1 = new FileInputStream(imageFiles[i]);
                        FileInputStream is2 = new FileInputStream(cmpImageFiles[i]);
                        boolean cmpResult = compareStreams(is1, is2);
                        is1.close();
                        is2.close();
                        if (!cmpResult) {
                            if (new File(compareExec).exists()) {
                                String compareParams = this.compareParams.replace("<image1>", imageFiles[i].getAbsolutePath()).replace("<image2>", cmpImageFiles[i].getAbsolutePath()).replace("<difference>", outPath + differenceImagePrefix + Integer.toString(i + 1) + ".png");
                                p = Runtime.getRuntime().exec(compareExec + compareParams);
                                bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                while ((line = bre.readLine()) != null) {
                                    System.out.println(line);
                                }
                                bre.close();
                                int cmpExitValue = p.waitFor();
                                if (cmpExitValue == 0) {
                                    if (differentPagesFail == null)  {
                                        differentPagesFail = differentPages.replace("<filename>", outPdf).replace("<pagenumber>", Integer.toString(i + 1));
                                        differentPagesFail += "\nPlease, examine " + outPath + differenceImagePrefix + Integer.toString(i + 1) + ".png for more details.";
                                    } else {
                                        differentPagesFail =
                                                "File " + outPdf + " differs.\nPlease, examine difference images for more details.";
                                    }
                                } else {
                                    differentPagesFail = differentPages.replace("<filename>", outPdf).replace("<pagenumber>", Integer.toString(i + 1));
                                }
                            } else {
                                differentPagesFail = differentPages.replace("<filename>", outPdf).replace("<pagenumber>", Integer.toString(i + 1));
                                differentPagesFail += "\nYou can optionally specify path to ImageMagick compare tool (e.g. -DcompareExec=\"C:/Program Files/ImageMagick-6.5.4-2/compare.exe\") to visualize differences.";
                                break;
                            }
                            System.out.println(differentPagesFail);
                        } else {
                            System.out.println("done.");
                        }
                    }
                    if (differentPagesFail != null) {
                        return differentPagesFail;
                    } else {
                        if (bUnexpectedNumberOfPages)
                            return unexpectedNumberOfPages.replace("<filename>", outPdf) + "\n" + differentPagesFail;
                    }
                } else {
                    return gsFailed.replace("<filename>", outPdf);
                }
            } else {
                return gsFailed.replace("<filename>", cmpPdf);
            }
        } else {
            return cannotOpenTargetDirectory.replace("<filename>", outPdf);
        }

        return null;
    }

    public String compare(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws IOException, InterruptedException, DocumentException {
        init(outPdf, cmpPdf);
        return compare(outPath, differenceImagePrefix, ignoredAreas);
    }

    public String compare(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix) throws IOException, InterruptedException, DocumentException {
        return compare(outPdf, cmpPdf, outPath, differenceImagePrefix, null);
    }

    public String compareXmp(){
        return compareXmp(false);
    }

    public String compareXmp(boolean ignoreDateAndProducerProperties){
        PdfReader cmpReader = null;
        PdfReader outReader = null;
        try {
            cmpReader = new PdfReader(cmpPdf);
            outReader = new PdfReader(outPdf);
            byte[] cmpBytes = cmpReader.getMetadata(), outBytes = outReader.getMetadata();
            if (ignoreDateAndProducerProperties) {
                XMPMeta xmpMeta = XMPMetaFactory.parseFromBuffer(cmpBytes);

                XMPUtils.removeProperties(xmpMeta, XMPConst.NS_XMP, XmpBasicProperties.CREATEDATE, true, true);
                XMPUtils.removeProperties(xmpMeta, XMPConst.NS_XMP, XmpBasicProperties.MODIFYDATE, true, true);
                XMPUtils.removeProperties(xmpMeta, XMPConst.NS_XMP, XmpBasicProperties.METADATADATE, true, true);
                XMPUtils.removeProperties(xmpMeta, XMPConst.NS_PDF, PdfProperties.PRODUCER, true, true);

                cmpBytes = XMPMetaFactory.serializeToBuffer(xmpMeta, new SerializeOptions(SerializeOptions.SORT));

                xmpMeta = XMPMetaFactory.parseFromBuffer(outBytes);
                XMPUtils.removeProperties(xmpMeta, XMPConst.NS_XMP, XmpBasicProperties.CREATEDATE, true, true);
                XMPUtils.removeProperties(xmpMeta, XMPConst.NS_XMP, XmpBasicProperties.MODIFYDATE, true, true);
                XMPUtils.removeProperties(xmpMeta, XMPConst.NS_XMP, XmpBasicProperties.METADATADATE, true, true);
                XMPUtils.removeProperties(xmpMeta, XMPConst.NS_PDF, PdfProperties.PRODUCER, true, true);

                outBytes = XMPMetaFactory.serializeToBuffer(xmpMeta, new SerializeOptions(SerializeOptions.SORT));
            }

            if (!compareXmls(cmpBytes, outBytes)) {
                return "The XMP packages different!!!";
            }
        } catch (XMPException xmpExc) {
            return "XMP parsing failure!!!";
        } catch (IOException ioExc) {
            return "XMP parsing failure!!!";
        } catch (ParserConfigurationException parseExc)  {
            return "XMP parsing failure!!!";
        } catch (SAXException parseExc)  {
            return "XMP parsing failure!!!";
        }
        finally {
            if (cmpReader != null)
                cmpReader.close();
            if (outReader != null)
                outReader.close();
        }
        return null;
    }

    public boolean compareXmls(byte[] xml1, byte[] xml2) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        org.w3c.dom.Document doc1 = db.parse(new ByteArrayInputStream(xml1));
        doc1.normalizeDocument();

        org.w3c.dom.Document doc2 = db.parse(new ByteArrayInputStream(xml2));
        doc2.normalizeDocument();

        return doc2.isEqualNode(doc1);
    }

    private void init(String outPdf, String cmpPdf) {
        this.outPdf = outPdf;
        this.cmpPdf = cmpPdf;
        outPdfName =  new File(outPdf).getName();
        cmpPdfName = new File(cmpPdf).getName();
        outImage = outPdfName + "-%03d.png";
        cmpImage = "cmp_" + cmpPdfName + "-%03d.png";
    }

    private boolean compareStreams(InputStream is1, InputStream is2) throws IOException {
        byte[] buffer1 = new byte[64 * 1024];
        byte[] buffer2 = new byte[64 * 1024];
        int len1 = 0;
        int len2 = 0;
        for (; ;) {
            len1 = is1.read(buffer1);
            len2 = is2.read(buffer2);
            if (len1 != len2)
                return false;
            if (!Arrays.equals(buffer1, buffer2))
                return false;
            if (len1 == -1 || len2 == -1)
                break;
        }
        return true;
    }

    class PngFileFilter implements FileFilter {

        public boolean accept(File pathname) {
            String ap = pathname.getAbsolutePath();
            boolean b1 = ap.endsWith(".png");
            boolean b2 = ap.contains("cmp_");
            return b1 && !b2 && ap.contains(outPdfName);
        }
    }

    class CmpPngFileFilter implements FileFilter {
        public boolean accept(File pathname) {
            String ap = pathname.getAbsolutePath();
            boolean b1 = ap.endsWith(".png");
            boolean b2 = ap.contains("cmp_");
            return b1 && b2 && ap.contains(cmpPdfName);
        }
    }

    class ImageNameComparator implements Comparator<File> {
        public int compare(File f1, File f2) {
            String f1Name = f1.getAbsolutePath();
            String f2Name = f2.getAbsolutePath();
            return f1Name.compareTo(f2Name);
        }
    }
}
