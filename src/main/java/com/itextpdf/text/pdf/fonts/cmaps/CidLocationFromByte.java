/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.pdf.PRTokeniser;
import java.io.IOException;

/**
 *
 * @author psoares
 */
public class CidLocationFromByte implements CidLocation {
    private byte[] data;

    public CidLocationFromByte(byte[] data) {
        this.data = data;
    }
    
    public PRTokeniser getLocation(String location) throws IOException {
        return new PRTokeniser(data);
    }
}
