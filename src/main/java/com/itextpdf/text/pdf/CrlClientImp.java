/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author psoares
 */
public class CrlClientImp implements CrlClient {
    private String url;

    public CrlClientImp(String url) {
        this.url = url;
    }

    public byte[] getEncoded() {
        try {
            URL urlt = new URL(url);
            HttpURLConnection con = (HttpURLConnection)urlt.openConnection();
            if (con.getResponseCode() / 100 != 2) {
                throw new IOException(MessageLocalization.getComposedMessage("invalid.http.response.1", con.getResponseCode()));
            }
            //Get Response
            InputStream inp = (InputStream) con.getContent();
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while (true) {
                int n = inp.read(buf, 0, buf.length);
                if (n <= 0)
                    break;
                bout.write(buf, 0, n);
            }
            inp.close();
            return bout.toByteArray();
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

}
