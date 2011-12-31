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
public interface CidLocation {
    public PRTokeniser getLocation(String location) throws IOException ;
}
