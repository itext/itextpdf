package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import java.util.ArrayList;

public class CMapByteCid extends AbstractCMap {
    private ArrayList<char[]> planes = new ArrayList<char[]>();

    public CMapByteCid() {
        planes.add(new char[256]);
    }
    
    @Override
    void addChar(PdfString mark, PdfObject code) {
        if (!(code instanceof PdfNumber))
            return;
        encodeSequence(decodeStringToByte(mark), (char)((PdfNumber)code).intValue());
    }
    
    private void encodeSequence(byte seqs[], char cid) {
        int size = seqs.length - 1;
        int nextPlane = 0;
        for (int idx = 0; idx < size; ++idx) {
            char plane[] = planes.get(nextPlane);
            int one = seqs[idx] & 0xff;
            char c = plane[one];
            if (c != 0 && (c & 0x8000) == 0)
                throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping"));
            if (c == 0) {
                planes.add(new char[256]);
                c = (char)(planes.size() - 1 | 0x8000);
                plane[one] = c;
            }
            nextPlane = c & 0x7fff;
        }
        char plane[] = planes.get(nextPlane);
        int one = seqs[size] & 0xff;
        char c = plane[one];
        if ((c & 0x8000) != 0)
            throw new RuntimeException(MessageLocalization.getComposedMessage("inconsistent.mapping"));
        plane[one] = cid;
    }
    
    /**
     * 
     * @param seq
     * @return the cid code or -1 for end
     */
    public int decodeSingle(CMapSequence seq) {
        int end = seq.off + seq.len;
        int currentPlane = 0;
        while (seq.off < end) {
            int one = seq.seq[seq.off++] & 0xff;
            --seq.len;
            char plane[] = planes.get(currentPlane);
            int cid = plane[one];
            if ((cid & 0x8000) == 0) {
                return cid;
            }
            else
                currentPlane = cid & 0x7fff;
        }
        return -1;
    }

    public String decodeSequence(CMapSequence seq) {
        StringBuilder sb = new StringBuilder();
        int cid = 0;
        while ((cid = decodeSingle(seq)) >= 0) {
            sb.append((char)cid);
        }
        return sb.toString();
    }
}
