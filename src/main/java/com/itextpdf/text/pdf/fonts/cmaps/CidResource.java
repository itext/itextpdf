/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author psoares
 */
public class CidResource implements CidLocation{

    public PRTokeniser getLocation(String location) throws IOException {
        String fullName = BaseFont.RESOURCE_PATH + "cmaps/" + location;
        InputStream inp = BaseFont.getResourceStream(fullName);
        if (inp == null)
            throw new IOException(MessageLocalization.getComposedMessage("the.cmap.1.was.not.found", fullName));
        return new PRTokeniser(new RandomAccessFileOrArray(inp));
    }
}
