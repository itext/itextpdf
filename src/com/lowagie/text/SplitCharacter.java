package com.lowagie.text;

/** Interface for customizing the split character.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public interface SplitCharacter {
    /** Returns <CODE>true</CODE> if the character can split a line.
     * @param c the character
     * @return <CODE>true</CODE> if the character can split a line
     */    
    public boolean isSplitCharacter(char c);
}