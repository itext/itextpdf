/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.itextpdf.text.pdf;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
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
import java.security.cert.X509Certificate;

/**
 *
 * @author psoares
 */
public class CrlClientImp implements CrlClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrlClientImp.class);

    public byte[] getEncoded(X509Certificate checkCert, String url) {
        try {
            if (url == null) {
                if (checkCert == null)
                    return null;
                url = PdfPKCS7.getCrlUrl(checkCert);
            }
            if (url == null)
                return null;
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
            if (LOGGER.isLogging(Level.ERROR))
                LOGGER.error("CrlClientImp", ex);
        }
        return null;
    }
}
