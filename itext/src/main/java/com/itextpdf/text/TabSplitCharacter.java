package com.itextpdf.text;

import com.itextpdf.text.pdf.DefaultSplitCharacter;
import com.itextpdf.text.pdf.PdfChunk;

public class TabSplitCharacter implements SplitCharacter {

    public static final SplitCharacter TAB = new TabSplitCharacter();

    public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
        return true;
    }
}
