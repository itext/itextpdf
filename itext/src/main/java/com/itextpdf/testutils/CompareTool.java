/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Meta;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.text.xml.xmp.PdfProperties;
import com.itextpdf.text.xml.xmp.XmpBasicProperties;
import com.itextpdf.xmp.*;
import com.itextpdf.xmp.options.SerializeOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * Helper class for tests: uses ghostscript to compare PDFs at a pixel level.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class CompareTool {

    private class ObjectPath {
        protected RefKey baseCmpObject;
        protected RefKey baseOutObject;
        protected Stack<PathItem> path = new Stack<PathItem>();
        protected Stack<Pair<RefKey>> indirects = new Stack<Pair<RefKey>>();

        public ObjectPath() {
        }

        protected ObjectPath(RefKey baseCmpObject, RefKey baseOutObject) {
            this.baseCmpObject = baseCmpObject;
            this.baseOutObject = baseOutObject;
        }

        private ObjectPath(RefKey baseCmpObject, RefKey baseOutObject, Stack<PathItem> path) {
            this.baseCmpObject = baseCmpObject;
            this.baseOutObject = baseOutObject;
            this.path = path;
        }

        private class Pair<T> {
            private T first;
            private T second;

            public Pair(T first, T second) {
                this.first = first;
                this.second = second;
            }

            @Override
            public int hashCode() {
                return first.hashCode() * 31 + second.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return (obj instanceof Pair && first.equals(((Pair) obj).first) && second.equals(((Pair) obj).second));
            }
        }

        private abstract class PathItem {
            protected abstract Node toXmlNode(Document document);
        }

        private class DictPathItem extends PathItem {
            String key;
            public DictPathItem(String key) {
                this.key = key;
            }

            @Override
            public String toString() {
                return "Dict key: " + key;
            }

            @Override
            public int hashCode() {
                return key.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof DictPathItem && key.equals(((DictPathItem) obj).key);
            }

            @Override
            protected Node toXmlNode(Document document) {
                Node element = document.createElement("dictKey");
                element.appendChild(document.createTextNode(key));
                return element;
            }
        }

        private class ArrayPathItem extends PathItem {
            int index;
            public ArrayPathItem(int index) {
                this.index = index;
            }

            @Override
            public String toString() {
                return "Array index: " + String.valueOf(index);
            }

            @Override
            public int hashCode() {
                return index;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof ArrayPathItem && index == ((ArrayPathItem) obj).index;
            }

            @Override
            protected Node toXmlNode(Document document) {
                Node element = document.createElement("arrayIndex");
                element.appendChild(document.createTextNode(String.valueOf(index)));
                return element;
            }
        }

        private class OffsetPathItem extends PathItem {
            int offset;
            public OffsetPathItem(int offset) {
                this.offset = offset;
            }

            @Override
            public String toString() {
                return "Offset: " + String.valueOf(offset);
            }

            @Override
            public int hashCode() {
                return offset;
            }

            @Override
            public boolean equals(Object obj) {
                return obj instanceof OffsetPathItem && offset == ((OffsetPathItem) obj).offset;
            }

            @Override
            protected Node toXmlNode(Document document) {
                Node element = document.createElement("offset");
                element.appendChild(document.createTextNode(String.valueOf(offset)));
                return element;
            }
        }

        public ObjectPath resetDirectPath(RefKey baseCmpObject, RefKey baseOutObject) {
            ObjectPath newPath = new ObjectPath(baseCmpObject, baseOutObject);
            newPath.indirects = (Stack<Pair<RefKey>>) indirects.clone();
            newPath.indirects.add(new Pair<RefKey>(baseCmpObject, baseOutObject));
            return newPath;
        }

        public boolean isComparing(RefKey baseCmpObject, RefKey baseOutObject) {
            return indirects.contains(new Pair<RefKey>(baseCmpObject, baseOutObject));
        }

        public void pushArrayItemToPath(int index) {
            path.add(new ArrayPathItem(index));
        }

        public void pushDictItemToPath(String key) {
            path.add(new DictPathItem(key));
        }

        public void pushOffsetToPath(int offset) {
            path.add(new OffsetPathItem(offset));
        }

        public void pop() {
            path.pop();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Base cmp object: %s obj. Base out object: %s obj", baseCmpObject, baseOutObject));
            for (PathItem pathItem : path) {
                sb.append("\n");
                sb.append(pathItem.toString());
            }
            return sb.toString();
        }

        @Override
        public int hashCode() {
            int hashCode = baseCmpObject.hashCode() * 31 + baseOutObject.hashCode();
            for (PathItem pathItem : path) {
                hashCode *= 31;
                hashCode += pathItem.hashCode();
            }
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ObjectPath && baseCmpObject.equals(((ObjectPath) obj).baseCmpObject) && baseOutObject.equals(((ObjectPath) obj).baseOutObject) &&
                    path.equals(((ObjectPath) obj).path);
        }

        @Override
        protected Object clone() {
            return new ObjectPath(baseCmpObject, baseOutObject, (Stack<PathItem>) path.clone());
        }

        public Node toXmlNode(Document document) {
            Element element = document.createElement("path");
            Element baseNode = document.createElement("base");
            baseNode.setAttribute("cmp", baseCmpObject.toString() + " obj");
            baseNode.setAttribute("out", baseOutObject.toString() + " obj");
            element.appendChild(baseNode);
            for (PathItem pathItem : path) {
                element.appendChild(pathItem.toXmlNode(document));
            }
            return element;
        }
    }

    protected class CompareResult {
        protected HashMap<ObjectPath, String> differences = new HashMap<ObjectPath, String>();
        protected int messageLimit = 1;

        public CompareResult(int messageLimit) {
            this.messageLimit = messageLimit;
        }

        public boolean isOk() {
            return differences.size() == 0;
        }

        public int getErrorCount() {
            return differences.size();
        }

        protected boolean isMessageLimitReached() {
            return differences.size() >= messageLimit;
        }

        public String getReport() {
            StringBuilder sb = new StringBuilder();
            boolean firstEntry = true;
            for (Map.Entry<ObjectPath, String> entry : differences.entrySet()) {
                if (!firstEntry)
                    sb.append("-----------------------------").append("\n");
                ObjectPath diffPath = entry.getKey();
                sb.append(entry.getValue()).append("\n").append(diffPath.toString()).append("\n");
                firstEntry = false;
            }
            return sb.toString();
        }

        protected void addError(ObjectPath path, String message) {
            if (differences.size() < messageLimit) {
                differences.put(((ObjectPath) path.clone()), message);
            }
        }

        public void writeReportToXml(OutputStream stream) throws ParserConfigurationException, TransformerException {
            Document xmlReport = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = xmlReport.createElement("report");
            Element errors = xmlReport.createElement("errors");
            errors.setAttribute("count", String.valueOf(differences.size()));
            root.appendChild(errors);
            for (Map.Entry<ObjectPath, String> entry : differences.entrySet()) {
                Node errorNode = xmlReport.createElement("error");
                Node message = xmlReport.createElement("message");
                message.appendChild(xmlReport.createTextNode(entry.getValue()));
                Node path = entry.getKey().toXmlNode(xmlReport);
                errorNode.appendChild(message);
                errorNode.appendChild(path);
                errors.appendChild(errorNode);
            }
            xmlReport.appendChild(root);

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(xmlReport);
            StreamResult result = new StreamResult(stream);
            transformer.transform(source, result);
        }
    }

    private String gsExec;
    private String compareExec;
    private final String gsParams = " -dNOPAUSE -dBATCH -sDEVICE=png16m -r150 -sOutputFile=<outputfile> <inputfile>";
    private final String compareParams = " \"<image1>\" \"<image2>\" \"<difference>\"";

    static private final String cannotOpenTargetDirectory = "Cannot open target directory for <filename>.";
    static private final String gsFailed = "GhostScript failed for <filename>.";
    static private final String unexpectedNumberOfPages = "Unexpected number of pages for <filename>.";
    static private final String differentPages = "File <filename> differs on page <pagenumber>.";
    static private final String undefinedGsPath = "Path to GhostScript is not specified. Please use -DgsExec=<path_to_ghostscript> (e.g. -DgsExec=\"C:/Program Files/gs/gs9.14/bin/gswin32c.exe\")";

    static private final String ignoredAreasPrefix = "ignored_areas_";

    private String cmpPdf;
    private String cmpPdfName;
    private String cmpImage;
    private String outPdf;
    private String outPdfName;
    private String outImage;

    List<PdfDictionary> outPages;
    List<RefKey> outPagesRef;
    List<PdfDictionary> cmpPages;
    List<RefKey> cmpPagesRef;

    private int compareByContentErrorsLimit = 1;
    private boolean generateCompareByContentXmlReport = false;

    public CompareTool() {
        gsExec = System.getProperty("gsExec");
        compareExec = System.getProperty("compareExec");
    }

    private String compare(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws IOException, InterruptedException, DocumentException {
        return compare(outPath, differenceImagePrefix, ignoredAreas, null);
    }

    private String compare(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas,
                             List<Integer> equalPages) throws IOException, InterruptedException, DocumentException {
        if (gsExec == null)
        	return undefinedGsPath;
        if (!(new File(gsExec).exists())) {
            return new File(gsExec).getAbsolutePath() + " does not exist";
        }
        if (!outPath.endsWith("/"))
            outPath = outPath + "/";
        File targetDir = new File(outPath);
        File[] imageFiles;
        File[] cmpImageFiles;

        if (!targetDir.exists()) {
            targetDir.mkdirs();
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
                        if (equalPages != null && equalPages.contains(i))
                            continue;
                        System.out.print("Comparing page " + Integer.toString(i + 1) + " (" + imageFiles[i].getAbsolutePath() + ")...");
                        FileInputStream is1 = new FileInputStream(imageFiles[i]);
                        FileInputStream is2 = new FileInputStream(cmpImageFiles[i]);
                        boolean cmpResult = compareStreams(is1, is2);
                        is1.close();
                        is2.close();
                        if (!cmpResult) {
                            if (compareExec != null && new File(compareExec).exists()) {
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
                            return unexpectedNumberOfPages.replace("<filename>", outPdf);
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

    /**
     * Sets the maximum errors count which will be returned as the result of the comparison.
     * @param compareByContentMaxErrorCount the errors count.
     * @return Returns this.
     */
    public CompareTool setCompareByContentErrorsLimit(int compareByContentMaxErrorCount) {
        this.compareByContentErrorsLimit = compareByContentMaxErrorCount;
        return this;
    }

    public void setGenerateCompareByContentXmlReport(boolean generateCompareByContentXmlReport) {
        this.generateCompareByContentXmlReport = generateCompareByContentXmlReport;
    }

    private String compareByContent(String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws DocumentException, InterruptedException, IOException {
        System.out.print("[itext] INFO  Comparing by content..........");
        PdfReader outReader = new PdfReader(outPdf);
        outPages = new ArrayList<PdfDictionary>();
        outPagesRef = new ArrayList<RefKey>();
        loadPagesFromReader(outReader, outPages, outPagesRef);

        PdfReader cmpReader = new PdfReader(cmpPdf);
        cmpPages = new ArrayList<PdfDictionary>();
        cmpPagesRef = new ArrayList<RefKey>();
        loadPagesFromReader(cmpReader, cmpPages, cmpPagesRef);

        if (outPages.size() != cmpPages.size())
            return compare(outPath, differenceImagePrefix, ignoredAreas);

        CompareResult compareResult = new CompareResult(compareByContentErrorsLimit);
        List<Integer> equalPages = new ArrayList<Integer>(cmpPages.size());
        for (int i = 0; i < cmpPages.size(); i++) {
            ObjectPath currentPath = new ObjectPath(cmpPagesRef.get(i), outPagesRef.get(i));
            if (compareDictionariesExtended(outPages.get(i), cmpPages.get(i), currentPath, compareResult))
                equalPages.add(i);
        }

        PdfObject outStructTree = outReader.getCatalog().get(PdfName.STRUCTTREEROOT);
        PdfObject cmpStructTree = cmpReader.getCatalog().get(PdfName.STRUCTTREEROOT);
        RefKey outStructTreeRef = outStructTree == null ? null : new RefKey((PdfIndirectReference)outStructTree);
        RefKey cmpStructTreeRef = cmpStructTree == null ? null : new RefKey((PdfIndirectReference)cmpStructTree);
        compareObjects(outStructTree, cmpStructTree, new ObjectPath(outStructTreeRef, cmpStructTreeRef), compareResult);

        PdfObject outOcProperties = outReader.getCatalog().get(PdfName.OCPROPERTIES);
        PdfObject cmpOcProperties = cmpReader.getCatalog().get(PdfName.OCPROPERTIES);
        RefKey outOcPropertiesRef = outOcProperties == null ? null : new RefKey((PdfIndirectReference)outOcProperties);
        RefKey cmpOcPropertiesRef = cmpOcProperties == null ? null : new RefKey((PdfIndirectReference)cmpOcProperties);
        compareObjects(outOcProperties, cmpOcProperties, new ObjectPath(outOcPropertiesRef, cmpOcPropertiesRef), compareResult);

        outReader.close();
        cmpReader.close();

        if (generateCompareByContentXmlReport) {
            try {
                compareResult.writeReportToXml(new FileOutputStream(outPath + "/report.xml"));
            } catch (Exception exc) {}
        }

        if (equalPages.size() == cmpPages.size() && compareResult.isOk()) {
            System.out.println("OK");
            System.out.flush();
            return null;
        } else {
            System.out.println("Fail");
            System.out.flush();
            String compareByContentReport = "Compare by content report:\n" + compareResult.getReport();
            System.out.println(compareByContentReport);
            System.out.flush();
            String message = compare(outPath, differenceImagePrefix, ignoredAreas, equalPages);
            if (message == null || message.length() == 0)
                return "Compare by content fails. No visual differences";
            return message;
        }
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix, Map<Integer, List<Rectangle>> ignoredAreas) throws DocumentException, InterruptedException, IOException {
        init(outPdf, cmpPdf);
        return compareByContent(outPath, differenceImagePrefix, ignoredAreas);
    }

    public String compareByContent(String outPdf, String cmpPdf, String outPath, String differenceImagePrefix) throws DocumentException, InterruptedException, IOException {
        return compareByContent(outPdf, cmpPdf, outPath, differenceImagePrefix, null);
    }

    private void loadPagesFromReader(PdfReader reader, List<PdfDictionary> pages, List<RefKey> pagesRef) {
        PdfObject pagesDict = reader.getCatalog().get(PdfName.PAGES);
        addPagesFromDict(pagesDict, pages, pagesRef);
    }

    private void addPagesFromDict(PdfObject dictRef, List<PdfDictionary> pages, List<RefKey> pagesRef) {
        PdfDictionary dict = (PdfDictionary)PdfReader.getPdfObject(dictRef);
        if (dict.isPages()) {
            PdfArray kids = dict.getAsArray(PdfName.KIDS);
            if (kids == null) return;
            for (PdfObject kid : kids) {
                addPagesFromDict(kid, pages, pagesRef);
            }
        } else if(dict.isPage()) {
            pages.add(dict);
            pagesRef.add(new RefKey((PRIndirectReference)dictRef));
        }
    }

    private boolean compareObjects(PdfObject outObj, PdfObject cmpObj, ObjectPath currentPath, CompareResult compareResult) throws IOException {
        PdfObject outDirectObj = PdfReader.getPdfObject(outObj);
        PdfObject cmpDirectObj = PdfReader.getPdfObject(cmpObj);

        if (cmpDirectObj == null && outDirectObj == null)
            return true;

        if (outDirectObj == null) {
            compareResult.addError(currentPath, "Expected object was not found.");
            return false;
        } else if (cmpDirectObj == null) {
            compareResult.addError(currentPath, "Found object which was not expected to be found.");
            return false;
        } else if (cmpDirectObj.type() != outDirectObj.type()) {
            compareResult.addError(currentPath, String.format("Types do not match. Expected: %s. Found: %s.", cmpDirectObj.getClass().getSimpleName(), outDirectObj.getClass().getSimpleName()));
            return false;
        }

        if (cmpObj.isIndirect() && outObj.isIndirect()) {
            if (currentPath.isComparing(new RefKey((PdfIndirectReference) cmpObj), new RefKey((PdfIndirectReference) outObj)))
                return true;
            currentPath = currentPath.resetDirectPath(new RefKey((PdfIndirectReference) cmpObj), new RefKey((PdfIndirectReference) outObj));
        }

        if (cmpDirectObj.isDictionary() && ((PdfDictionary)cmpDirectObj).isPage()) {
            if (!outDirectObj.isDictionary() || !((PdfDictionary)outDirectObj).isPage()) {
                if (compareResult != null && currentPath != null)
                    compareResult.addError(currentPath, String.format("Expected a page. Found not a page."));
                return false;
            }
            RefKey cmpRefKey = new RefKey((PRIndirectReference)cmpObj);
            RefKey outRefKey = new RefKey((PRIndirectReference)outObj);
            // References to the same page
            if (cmpPagesRef.contains(cmpRefKey) && cmpPagesRef.indexOf(cmpRefKey) == outPagesRef.indexOf(outRefKey))
                return true;
            if (compareResult != null && currentPath != null)
                compareResult.addError(currentPath, String.format("The dictionaries refer to different pages. Expected page number: %s. Found: %s",
                        cmpPagesRef.indexOf(cmpRefKey), outPagesRef.indexOf(outRefKey)));
            return false;
        }

        if (cmpDirectObj.isDictionary()) {
            if (!compareDictionariesExtended((PdfDictionary)outDirectObj, (PdfDictionary)cmpDirectObj, currentPath, compareResult))
                return false;
        } else if (cmpDirectObj.isStream()) {
            if (!compareStreamsExtended((PRStream) outDirectObj, (PRStream) cmpDirectObj, currentPath, compareResult))
                return false;
        } else if (cmpDirectObj.isArray()) {
            if (!compareArraysExtended((PdfArray) outDirectObj, (PdfArray) cmpDirectObj, currentPath, compareResult))
                return false;
        } else if (cmpDirectObj.isName()) {
            if (!compareNamesExtended((PdfName) outDirectObj, (PdfName) cmpDirectObj, currentPath, compareResult))
                return false;
        } else if (cmpDirectObj.isNumber()) {
            if (!compareNumbersExtended((PdfNumber) outDirectObj, (PdfNumber) cmpDirectObj, currentPath, compareResult))
                return false;
        } else if (cmpDirectObj.isString()) {
            if (!compareStringsExtended((PdfString) outDirectObj, (PdfString) cmpDirectObj, currentPath, compareResult))
                return false;
        } else if (cmpDirectObj.isBoolean()) {
            if (!compareBooleansExtended((PdfBoolean) outDirectObj, (PdfBoolean) cmpDirectObj, currentPath, compareResult))
                return false;
        } else if (outDirectObj.isNull() && cmpDirectObj.isNull()) {
        } else {
            throw new UnsupportedOperationException();
        }
        return true;
    }

    public boolean compareDictionaries(PdfDictionary outDict, PdfDictionary cmpDict) throws IOException {
        return compareDictionariesExtended(outDict, cmpDict, null, null);
    }

    private boolean compareDictionariesExtended(PdfDictionary outDict, PdfDictionary cmpDict, ObjectPath currentPath, CompareResult compareResult) throws IOException {
        if (cmpDict != null && outDict == null || outDict != null && cmpDict == null) {
            compareResult.addError(currentPath, "One of the dictionaries is null, the other is not.");
            return false;
        }
        boolean dictsAreSame = true;
        // Iterate through the union of the keys of the cmp and out dictionaries!
        Set<PdfName> mergedKeys = new HashSet<PdfName>(cmpDict.getKeys());
        mergedKeys.addAll(outDict.getKeys());
        for (PdfName key : mergedKeys) {
            if (key.compareTo(PdfName.PARENT) == 0 || key.compareTo(PdfName.P) == 0) continue;
            if (outDict.isStream() && cmpDict.isStream() && (key.equals(PdfName.FILTER) || key.equals(PdfName.LENGTH))) continue;
            if (key.compareTo(PdfName.BASEFONT) == 0 || key.compareTo(PdfName.FONTNAME) == 0) {
                PdfObject cmpObj = cmpDict.getDirectObject(key);
                if (cmpObj.isName() && cmpObj.toString().indexOf('+') > 0) {
                    PdfObject outObj = outDict.getDirectObject(key);
                    if (!outObj.isName() || outObj.toString().indexOf('+') == -1) {
                        if (compareResult != null && currentPath != null)
                            compareResult.addError(currentPath, String.format("PdfDictionary %s entry: Expected: %s. Found: %s", key.toString(), cmpObj.toString(), outObj.toString()));
                        dictsAreSame = false;
                    }
                    String cmpName = cmpObj.toString().substring(cmpObj.toString().indexOf('+'));
                    String outName = outObj.toString().substring(outObj.toString().indexOf('+'));
                    if (!cmpName.equals(outName)) {
                        if (compareResult != null && currentPath != null)
                            compareResult.addError(currentPath, String.format("PdfDictionary %s entry: Expected: %s. Found: %s", key.toString(), cmpObj.toString(), outObj.toString()));
                        dictsAreSame = false;
                    }
                    continue;
                }
            }
            if (currentPath != null)
                currentPath.pushDictItemToPath(key.toString());
            dictsAreSame = compareObjects(outDict.get(key), cmpDict.get(key), currentPath, compareResult) && dictsAreSame;
            if (currentPath != null)
                currentPath.pop();
            if (!dictsAreSame && (currentPath == null || compareResult == null || compareResult.isMessageLimitReached()))
                return false;
        }
        return dictsAreSame;
    }

    public boolean compareStreams(PRStream outStream, PRStream cmpStream) throws IOException {
        return compareStreamsExtended(outStream, cmpStream, null, null);
    }

    private boolean compareStreamsExtended(PRStream outStream, PRStream cmpStream, ObjectPath currentPath, CompareResult compareResult) throws IOException {
        boolean decodeStreams = PdfName.FLATEDECODE.equals(outStream.get(PdfName.FILTER));
        byte[] outStreamBytes = PdfReader.getStreamBytesRaw(outStream);
        byte[] cmpStreamBytes = PdfReader.getStreamBytesRaw(cmpStream);
        if (decodeStreams) {
            outStreamBytes = PdfReader.decodeBytes(outStreamBytes, outStream);
            cmpStreamBytes = PdfReader.decodeBytes(cmpStreamBytes, cmpStream);
        }
        if (Arrays.equals(outStreamBytes, cmpStreamBytes)) {
            return compareDictionariesExtended(outStream, cmpStream, currentPath, compareResult);
        } else {
            if (cmpStreamBytes.length != outStreamBytes.length) {
                if (compareResult != null && currentPath != null) {
                    compareResult.addError(currentPath, String.format("PRStream. Lengths are different. Expected: %s. Found: %s", cmpStreamBytes.length, outStreamBytes.length));
                }
            } else {
                for (int i = 0; i < cmpStreamBytes.length; i++) {
                    if (cmpStreamBytes[i] != outStreamBytes[i]) {
                        int l = Math.max(0, i - 10);
                        int r = Math.min(cmpStreamBytes.length, i + 10);
                        if (compareResult != null && currentPath != null) {
                            currentPath.pushOffsetToPath(i);
                            compareResult.addError(currentPath, String.format("PRStream. The bytes differ at index %s. Expected: %s (%s). Found: %s (%s)",
                                    i, new String(new byte[]{cmpStreamBytes[i]}), new String(cmpStreamBytes, l, r - l).replaceAll("\\r|\\n", ""),
                                    new String(new byte[]{outStreamBytes[i]}), new String(outStreamBytes, l, r - l).replaceAll("\\r|\\n", "")));
                            currentPath.pop();
                        }
                    }
                }
            }
            return false;
        }
    }

    public boolean compareArrays(PdfArray outArray, PdfArray cmpArray) throws IOException {
        return compareArraysExtended(outArray, cmpArray, null, null);
    }

    private boolean compareArraysExtended(PdfArray outArray, PdfArray cmpArray, ObjectPath currentPath, CompareResult compareResult) throws IOException {
        if (outArray == null) {
            if (compareResult != null && currentPath != null)
                compareResult.addError(currentPath, "Found null. Expected PdfArray.");
            return false;
        } else if (outArray.size() != cmpArray.size()) {
            if (compareResult != null && currentPath != null)
                compareResult.addError(currentPath, String.format("PdfArrays. Lengths are different. Expected: %s. Found: %s.", cmpArray.size(), outArray.size()));
            return false;
        }
        boolean arraysAreEqual = true;
        for (int i = 0; i < cmpArray.size(); i++) {
            if (currentPath != null)
                currentPath.pushArrayItemToPath(i);
            arraysAreEqual = compareObjects(outArray.getPdfObject(i), cmpArray.getPdfObject(i), currentPath, compareResult) && arraysAreEqual;
            if (currentPath != null)
                currentPath.pop();
            if (!arraysAreEqual && (currentPath == null || compareResult == null || compareResult.isMessageLimitReached()))
                return false;
        }

        return arraysAreEqual;
    }

    public boolean compareNames(PdfName outName, PdfName cmpName) {
        return cmpName.compareTo(outName) == 0;
    }

    private boolean compareNamesExtended(PdfName outName, PdfName cmpName, ObjectPath currentPath, CompareResult compareResult) {
        if (cmpName.compareTo(outName) == 0) {
            return true;
        } else {
            if (compareResult != null && currentPath != null)
                compareResult.addError(currentPath, String.format("PdfName. Expected: %s. Found: %s", cmpName.toString(), outName.toString()));
            return false;
        }
    }

    public boolean compareNumbers(PdfNumber outNumber, PdfNumber cmpNumber) {
        return cmpNumber.doubleValue() == outNumber.doubleValue();
    }

    private boolean compareNumbersExtended(PdfNumber outNumber, PdfNumber cmpNumber, ObjectPath currentPath, CompareResult compareResult) {
        if (cmpNumber.doubleValue() == outNumber.doubleValue()) {
            return true;
        } else {
            if (compareResult != null && currentPath != null)
                compareResult.addError(currentPath, String.format("PdfNumber. Expected: %s. Found: %s", cmpNumber, outNumber));
            return false;
        }
    }

    public boolean compareStrings(PdfString outString, PdfString cmpString) {
        return Arrays.equals(cmpString.getBytes(), outString.getBytes());
    }

    private boolean compareStringsExtended(PdfString outString, PdfString cmpString, ObjectPath currentPath, CompareResult compareResult) {
        if (Arrays.equals(cmpString.getBytes(), outString.getBytes())) {
            return true;
        } else {
            String cmpStr = cmpString.toUnicodeString();
            String outStr = outString.toUnicodeString();
            if (cmpStr.length() != outStr.length()) {
                if (compareResult != null && currentPath != null)
                    compareResult.addError(currentPath, String.format("PdfString. Lengths are different. Expected: %s. Found: %s", cmpStr.length(), outStr.length()));
            } else {
                for (int i = 0; i < cmpStr.length(); i++) {
                    if (cmpStr.charAt(i) != outStr.charAt(i)) {
                        int l = Math.max(0, i - 10);
                        int r = Math.min(cmpStr.length(), i + 10);
                        if (compareResult != null && currentPath != null) {
                            currentPath.pushOffsetToPath(i);
                            compareResult.addError(currentPath, String.format("PdfString. Characters differ at position %s. Expected: %s (%s). Found: %s (%s).",
                                    i, Character.toString(cmpStr.charAt(i)), cmpStr.substring(l, r).replace("\n", "\\n"),
                                    Character.toString(outStr.charAt(i)), outStr.substring(l, r).replace("\n", "\\n")));
                            currentPath.pop();
                        }
                        break;
                    }
                }
            }
            return false;
        }
    }

    public boolean compareBooleans(PdfBoolean outBoolean, PdfBoolean cmpBoolean) {
        return Arrays.equals(cmpBoolean.getBytes(), outBoolean.getBytes());
    }

    private boolean compareBooleansExtended(PdfBoolean outBoolean, PdfBoolean cmpBoolean, ObjectPath currentPath, CompareResult compareResult) {
        if (cmpBoolean.booleanValue() == outBoolean.booleanValue()) {
            return true;
        } else {
            if (compareResult != null && currentPath != null)
                compareResult.addError(currentPath, String.format("PdfBoolean. Expected: %s. Found: %s.", cmpBoolean.booleanValue(), outBoolean.booleanValue()));
            return false;
        }
    }

    public String compareXmp(String outPdf, String cmpPdf){
        return compareXmp(outPdf, cmpPdf, false);
    }

    public String compareXmp(String outPdf, String cmpPdf, boolean ignoreDateAndProducerProperties){
        init(outPdf, cmpPdf);
        PdfReader cmpReader = null;
        PdfReader outReader = null;
        try {
            cmpReader = new PdfReader(this.cmpPdf);
            outReader = new PdfReader(this.outPdf);
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
                return "The XMP packages different!";
            }
        } catch (XMPException xmpExc) {
            return "XMP parsing failure!";
        } catch (IOException ioExc) {
            return "XMP parsing failure!";
        } catch (ParserConfigurationException parseExc)  {
            return "XMP parsing failure!";
        } catch (SAXException parseExc)  {
            return "XMP parsing failure!";
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

    public String compareDocumentInfo(String outPdf, String cmpPdf) throws IOException {
        System.out.print("[itext] INFO  Comparing document info.......");
        String message = null;
        PdfReader outReader = new PdfReader(outPdf);
        PdfReader cmpReader = new PdfReader(cmpPdf);
        String[] cmpInfo = convertInfo(cmpReader.getInfo());
        String[] outInfo = convertInfo(outReader.getInfo());
        for (int i = 0; i < cmpInfo.length; ++i) {
            if (!cmpInfo[i].equals(outInfo[i])){
                message = "Document info fail";
                break;
            }
        }
        outReader.close();
        cmpReader.close();

        if (message == null)
            System.out.println("OK");
        else
            System.out.println("Fail");
        System.out.flush();
        return message;
    }

    private boolean linksAreSame(PdfAnnotation.PdfImportedLink cmpLink, PdfAnnotation.PdfImportedLink outLink) {
        // Compare link boxes, page numbers the links refer to, and simple parameters (non-indirect, non-arrays, non-dictionaries)

        if (cmpLink.getDestinationPage() != outLink.getDestinationPage())
            return false;
        if (!cmpLink.getRect().toString().equals(outLink.getRect().toString()))
            return false;

        Map<PdfName, PdfObject> cmpParams = cmpLink.getParameters();
        Map<PdfName, PdfObject> outParams = outLink.getParameters();
        if (cmpParams.size() != outParams.size())
            return false;

        for (Map.Entry<PdfName, PdfObject> cmpEntry : cmpParams.entrySet()) {
            PdfObject cmpObj = cmpEntry.getValue();
            if (!outParams.containsKey(cmpEntry.getKey()))
                return false;
            PdfObject outObj = outParams.get(cmpEntry.getKey());
            if (cmpObj.type() != outObj.type())
                return false;

            switch (cmpObj.type()) {
                case PdfObject.NULL:
                case PdfObject.BOOLEAN:
                case PdfObject.NUMBER:
                case PdfObject.STRING:
                case PdfObject.NAME:
                    if (!cmpObj.toString().equals(outObj.toString()))
                        return false;
                    break;
            }
        }

        return true;
    }

    public String compareLinks(String outPdf, String cmpPdf) throws IOException {
        System.out.print("[itext] INFO  Comparing link annotations....");
        String message = null;
        PdfReader outReader = new PdfReader(outPdf);
        PdfReader cmpReader = new PdfReader(cmpPdf);
        for (int i = 0; i < outReader.getNumberOfPages() && i < cmpReader.getNumberOfPages(); i++) {
            List<PdfAnnotation.PdfImportedLink> outLinks = outReader.getLinks(i + 1);
            List<PdfAnnotation.PdfImportedLink> cmpLinks = cmpReader.getLinks(i + 1);
            if (cmpLinks.size() != outLinks.size()) {
                message =  String.format("Different number of links on page %d.", i + 1);
                break;
            }
            for (int j = 0; j < cmpLinks.size(); j++) {
                if (!linksAreSame(cmpLinks.get(j), outLinks.get(j))) {
                    message = String.format("Different links on page %d.\n%s\n%s", i + 1, cmpLinks.get(j).toString(), outLinks.get(j).toString());
                    break;
                }
            }
        }
        outReader.close();
        cmpReader.close();
        if (message == null)
            System.out.println("OK");
        else
            System.out.println("Fail");
        System.out.flush();
        return message;
    }

    public String compareTagStructures(String outPdf, String cmpPdf) throws IOException, ParserConfigurationException, SAXException {
        System.out.print("[itext] INFO  Comparing tag structures......");

        String outXml = outPdf.replace(".pdf", ".xml");
        String cmpXml = outPdf.replace(".pdf", ".cmp.xml");

        String message = null;
        PdfReader reader = new PdfReader(outPdf);
        FileOutputStream xmlOut1 = new FileOutputStream(outXml);
        new CmpTaggedPdfReaderTool().convertToXml(reader, xmlOut1);
        reader.close();
        reader = new PdfReader(cmpPdf);
        FileOutputStream xmlOut2 = new FileOutputStream(cmpXml);
        new CmpTaggedPdfReaderTool().convertToXml(reader, xmlOut2);
        reader.close();
        if (!compareXmls(outXml, cmpXml)) {
            message = "The tag structures are different.";
        }
        xmlOut1.close();
        xmlOut2.close();
        if (message == null)
            System.out.println("OK");
        else
            System.out.println("Fail");
        System.out.flush();
        return message;
    }

    private String[] convertInfo(HashMap<String, String> info){
        String[] convertedInfo = new String[]{"","","",""} ;
        for(Map.Entry<String,String> entry : info.entrySet()){
            if (Meta.TITLE.equalsIgnoreCase(entry.getKey())){
                convertedInfo[0] = entry.getValue();
            } else if (Meta.AUTHOR.equalsIgnoreCase(entry.getKey())){
                convertedInfo[1] = entry.getValue();
            } else if (Meta.SUBJECT.equalsIgnoreCase(entry.getKey())){
                convertedInfo[2] = entry.getValue();
            } else if (Meta.KEYWORDS.equalsIgnoreCase(entry.getKey())){
                convertedInfo[3] = entry.getValue();
            }
        }
        return convertedInfo;
    }

    public boolean compareXmls(String xml1, String xml2) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        org.w3c.dom.Document doc1 = db.parse(new File(xml1));
        doc1.normalizeDocument();

        org.w3c.dom.Document doc2 = db.parse(new File(xml2));
        doc2.normalizeDocument();

        return doc2.isEqualNode(doc1);
    }

    private void init(String outPdf, String cmpPdf) {
        this.outPdf = outPdf;
        this.cmpPdf = cmpPdf;
        outPdfName =  new File(outPdf).getName();
        cmpPdfName = new File(cmpPdf).getName();
        outImage = outPdfName + "-%03d.png";
        if (cmpPdfName.startsWith("cmp_")) cmpImage = cmpPdfName + "-%03d.png";
        else cmpImage = "cmp_" + cmpPdfName + "-%03d.png";
    }

    private boolean compareStreams(InputStream is1, InputStream is2) throws IOException {
        byte[] buffer1 = new byte[64 * 1024];
        byte[] buffer2 = new byte[64 * 1024];
        int len1;
        int len2;
        for (; ;) {
            len1 = is1.read(buffer1);
            len2 = is2.read(buffer2);
            if (len1 != len2)
                return false;
            if (!Arrays.equals(buffer1, buffer2))
                return false;
            if (len1 == -1)
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

    class CmpTaggedPdfReaderTool extends TaggedPdfReaderTool {

        Map<PdfDictionary, Map<Integer, String> > parsedTags = new HashMap<PdfDictionary, Map<Integer, String>>();

        @Override
        public void parseTag(String tag, PdfObject object, PdfDictionary page)
                throws IOException {
            if (object instanceof PdfNumber) {

                if (!parsedTags.containsKey(page)) {
                    CmpMarkedContentRenderFilter listener = new CmpMarkedContentRenderFilter();

                    PdfContentStreamProcessor processor = new PdfContentStreamProcessor(
                            listener);
                    processor.processContent(PdfReader.getPageContent(page), page
                            .getAsDict(PdfName.RESOURCES));

                    parsedTags.put(page, listener.getParsedTagContent());
                }

                String tagContent = "";
                if (parsedTags.get(page).containsKey(((PdfNumber) object).intValue()))
                    tagContent = parsedTags.get(page).get(((PdfNumber) object).intValue());

                out.print(XMLUtil.escapeXML(tagContent, true));

            } else {
                super.parseTag(tag, object, page);
            }
        }

        @Override
        public void inspectChildDictionary(PdfDictionary k) throws IOException {
            inspectChildDictionary(k, true);
        }
    }

    class CmpMarkedContentRenderFilter implements RenderListener {

        Map<Integer, TextExtractionStrategy> tagsByMcid = new HashMap<Integer, TextExtractionStrategy>();

        public Map<Integer, String> getParsedTagContent() {
            Map<Integer, String> content = new HashMap<Integer, String>();
            for (int id : tagsByMcid.keySet()) {
                content.put(id, tagsByMcid.get(id).getResultantText());
            }
            return content;
        }

        public void beginTextBlock() {
            for (int id : tagsByMcid.keySet()) {
                tagsByMcid.get(id).beginTextBlock();
            }
        }

        public void renderText(TextRenderInfo renderInfo) {
            Integer mcid = renderInfo.getMcid();
            if (mcid != null && tagsByMcid.containsKey(mcid)) {
                tagsByMcid.get(mcid).renderText(renderInfo);
            }
            else if (mcid != null) {
                tagsByMcid.put(mcid, new SimpleTextExtractionStrategy());
                tagsByMcid.get(mcid).renderText(renderInfo);
            }
        }

        public void endTextBlock() {
            for (int id : tagsByMcid.keySet()) {
                tagsByMcid.get(id).endTextBlock();
            }
        }

        public void renderImage(ImageRenderInfo renderInfo) {
        }
    }
}
