package com.itextpdf.text.pdf;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * PdfASmartCopy has the same functionality as PdfACopy,
 * but when resources (such as fonts, images,...) are
 * encountered, a reference to these resources is saved
 * in a cache, so that they can be reused.
 * This requires more memory, but reduces the file size
 * of the resulting PDF document.
 */
public class PdfASmartCopy extends PdfACopy{

    /** the cache with the streams and references. */
    private HashMap<PdfSmartCopy.ByteStore, PdfIndirectReference> streamMap = null;
    private final HashMap<RefKey, Integer> serialized = new HashMap<RefKey, Integer>();

    /**
     * Constructor
     *
     * @param document         document
     * @param os               outputstream
     * @param conformanceLevel
     */
    public PdfASmartCopy(Document document, OutputStream os, PdfAConformanceLevel conformanceLevel) throws DocumentException {
        super(document, os, conformanceLevel);
        this.streamMap = new HashMap<PdfSmartCopy.ByteStore, PdfIndirectReference>();
    }

    @Override
    protected PdfIndirectReference copyIndirect(PRIndirectReference in) throws IOException, BadPdfFormatException {
        PdfObject srcObj = PdfReader.getPdfObjectRelease(in);
        PdfSmartCopy.ByteStore streamKey = null;
        boolean validStream = false;
        if (srcObj.isStream()) {
            streamKey = new PdfSmartCopy.ByteStore((PRStream)srcObj, serialized);
            validStream = true;
            PdfIndirectReference streamRef = streamMap.get(streamKey);
            if (streamRef != null) {
                return streamRef;
            }
        }
        else if (srcObj.isDictionary()) {
            streamKey = new PdfSmartCopy.ByteStore((PdfDictionary)srcObj, serialized);
            validStream = true;
            PdfIndirectReference streamRef = streamMap.get(streamKey);
            if (streamRef != null) {
                return streamRef;
            }
        }

        PdfIndirectReference theRef;
        RefKey key = new RefKey(in);
        IndirectReferences iRef = indirects.get(key);
        if (iRef != null) {
            theRef = iRef.getRef();
            if (iRef.getCopied()) {
                return theRef;
            }
        } else {
            theRef = body.getPdfIndirectReference();
            iRef = new IndirectReferences(theRef);
            indirects.put(key, iRef);
        }
        if (srcObj.isDictionary()) {
            PdfObject type = PdfReader.getPdfObjectRelease(((PdfDictionary)srcObj).get(PdfName.TYPE));
            if (type != null && PdfName.PAGE.equals(type)) {
                return theRef;
            }
        }
        iRef.setCopied();

        if (validStream) {
            streamMap.put(streamKey, theRef);
        }

        PdfObject obj = copyObject(srcObj);
        addToBody(obj, theRef);
        return theRef;
    }

    @Override
    public void freeReader(PdfReader reader) throws IOException {
        serialized.clear();
        super.freeReader(reader);
    }

    @Override
    public void addPage(PdfImportedPage iPage) throws IOException, BadPdfFormatException {
        if (currentPdfReaderInstance.getReader() != reader)
            serialized.clear();
        super.addPage(iPage);
    }
}
