package com.lowagie.text.pdf;

import com.lowagie.text.SplitCharacter;

public class DefaultSplitCharacter implements SplitCharacter {
	
	/**
	 * An instance of the default SplitCharacter.
	 */
	public static final SplitCharacter DEFAULT = new DefaultSplitCharacter();
	
	/**
	 * Checks if a character can be used to split a <CODE>PdfString</CODE>.
	 * <P>
	 * for the moment every character less than or equal to SPACE, the character '-'
	 * and some specific unicode ranges are 'splitCharacters'.
	 * 
	 * @param start start position in the array
	 * @param current current position in the array
	 * @param end end position in the array
	 * @param	cc		the character array that has to be checked
	 * @param ck chunk array
	 * @return	<CODE>true</CODE> if the character can be used to split a string, <CODE>false</CODE> otherwise
	 */
    public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
        char c;
        if (ck == null)
            c = (char)cc[current];
        else
            c = (char)ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
        if (c <= ' ' || c == '-' || c == '\u2010') {
            return true;
        }
        if (c < 0x2002)
            return false;
        return ((c >= 0x2002 && c <= 0x200b)
        || (c >= 0x2e80 && c < 0xd7a0)
        || (c >= 0xf900 && c < 0xfb00)
        || (c >= 0xfe30 && c < 0xfe50)
        || (c >= 0xff61 && c < 0xffa0));
    }

}
