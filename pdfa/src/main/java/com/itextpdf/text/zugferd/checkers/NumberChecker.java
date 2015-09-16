/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.text.zugferd.checkers;

/**
 * Provide a means to check if a String consist of characters from 0 to 9
 * and a decimal point in case a decimal is expected.
 */
public class NumberChecker extends CodeValidation {

    public final static int INTEGER = 0;
    public final static int ANY_DECIMALS = 1;
    public final static int TWO_DECIMALS = 2;
    public final static int FOUR_DECIMALS = 4;
    
    /** The type of checker: INTEGER, ANY_DECIMALS, TWO_DECIMALS, FOUR_DECIMALS. */
    protected int type;
    
    /**
     * Creates a specific number checker.
     * @param type  the type of checker that needs to be created:
     * INTEGER, ANY_DECIMALS, TWO_DECIMALS, or FOUR_DECIMALS
     */
    public NumberChecker(int type) {
        this.type = type;
    }
    
    @Override
    public boolean isValid(String code) {
        if (type == INTEGER)
            return isNumeric(code, code.length());
        if (code.endsWith("."))
            return false;
        int pos = code.indexOf(".");
        if (pos < 1)
            return false;
        String part1 = code.substring(0, pos);
        if (!isNumeric(part1, part1.length()))
            return false;
        String part2 = code.substring(pos + 1);
        switch(type) {
            case TWO_DECIMALS:
                return isNumeric(part2, 2);
            case FOUR_DECIMALS:
                return isNumeric(part2, 4);
            default:
                return isNumeric(part2, part2.length());
        }
    }
    
}
