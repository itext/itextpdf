package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.ArrayList;

public class PdfTagMarker extends PdfChunk {

    private ArrayList<IAccessibleElement> openElements = new ArrayList<IAccessibleElement>();
    private ArrayList<IAccessibleElement> closeElements = new ArrayList<IAccessibleElement>();

    protected PdfTagMarker(String string, PdfChunk other) {
        super(string, other);
        if (other instanceof PdfTagMarker) {
            openElements = new ArrayList<IAccessibleElement>(((PdfTagMarker)other).openElements);
            closeElements = new ArrayList<IAccessibleElement>(((PdfTagMarker)other).closeElements);
        }
    }

    protected PdfTagMarker(Chunk chunk, PdfAction action) {
        super(chunk, action);
        openElements.add(chunk);
        closeElements.add(chunk);
    }


    protected PdfChunk getNewChunk(String string) {
        return new PdfTagMarker(string, this);
    }

    static public PdfTagMarker getPdfTagMarker(PdfChunk chunk, IAccessibleElement accessibleElement) {
        PdfTagMarker m = new PdfTagMarker(chunk.value, chunk);
        m.getOpenElements().add(accessibleElement);
        m.getCloseElements().add(accessibleElement);
        return m;
    }

    static public ArrayList<PdfChunk> splitChunk(PdfChunk chunk, IAccessibleElement accessibleElement) {
        ArrayList<PdfChunk> splitted = new ArrayList<PdfChunk>();
        int len = chunk.length();
        int startIdx = 0;
        for (; startIdx < len; startIdx++) {
            char c = (char)chunk.getUnicodeEquivalent(chunk.value.charAt(startIdx));
            if (!BidiLine.isWS(c) && !PdfChunk.noPrint(c))
                break;
        }
        int endIdx = len - 1;
        for (; endIdx > startIdx; endIdx--) {
            char c = (char)chunk.getUnicodeEquivalent(chunk.value.charAt(endIdx));
            if (!BidiLine.isWS(c) && !PdfChunk.noPrint(c))
                break;
        }
        String s1 = null;
        String s2 = null;
        String s3 = null;
        try {
            s1 = chunk.value.substring(0, startIdx + 1);
            s2 = chunk.value.substring(startIdx + 1, endIdx);
            s3 = chunk.value.substring(endIdx, chunk.value.length());
        } catch (StringIndexOutOfBoundsException e) {
        }

        if (s1 == null) {
            splitted.add(chunk);
        } else if (s1 != null && s2 == null) {
            PdfTagMarker marker = new PdfTagMarker(s1, chunk);
            marker.openElements.add(accessibleElement);
            marker.closeElements.add(accessibleElement);
            splitted.add(marker);
        } else if (s1 != null && s2 != null && s3 == null) {
            PdfTagMarker marker = new PdfTagMarker(s1, chunk);
            marker.openElements.add(accessibleElement);
            splitted.add(marker);
            marker = new PdfTagMarker(s2, chunk);
            marker.closeElements.add(accessibleElement);
            splitted.add(marker);
        } else {
            PdfTagMarker marker = new PdfTagMarker(s1, chunk);
            marker.openElements.add(accessibleElement);
            splitted.add(marker);
            PdfChunk c = new PdfChunk(s2, chunk);
            splitted.add(c);
            marker = new PdfTagMarker(s3, chunk);
            marker.closeElements.add(accessibleElement);
            splitted.add(marker);
        }
        return splitted;
    }

    public boolean isOpen() {
        return openElements.size() > 0;
    }

    public boolean isClose() {
        return closeElements.size() > 0;
    }

    public ArrayList<IAccessibleElement> getOpenElements() {
        return openElements;
    }

    public ArrayList<IAccessibleElement> getCloseElements() {
        return closeElements;
    }

    public void setOpenElements(ArrayList<IAccessibleElement> accessibleElements) {
        openElements = accessibleElements;
    }

    public void setCloseElements(ArrayList<IAccessibleElement> accessibleElements) {
        closeElements = accessibleElements;
    }

    public void clearOpenElements() {
        openElements.clear();
    }

    public void clearCloseElements() {
        closeElements.clear();
    }

}
