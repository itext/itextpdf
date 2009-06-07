package com.lowagie.text.pdf;

import java.io.*;
import java.math.*;
import java.net.*;


import org.bouncycastle.asn1.cmp.*;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.tsp.*;

/**
 * Time Stamp Authority Client interface implementation using Bouncy Castle
 * org.bouncycastle.tsp package.
 * <p>
 * Created by Aiken Sam, 2006-11-15, refactored by Martin Brunecky, 07/15/2007
 * for ease of subclassing.
 * </p>
 */
public class TSAClientBouncyCastle implements TSAClient {
    protected String tsaURL;
    protected String tsaUsername;
    protected String tsaPassword;
    protected int tokSzEstimate;
    
    public TSAClientBouncyCastle(String url) {
        this(url, null, null, 4096);
    }
    
    public TSAClientBouncyCastle(String url, String username, String password) {
        this(url, username, password, 4096);
    }
    
    /**
     * Constructor.
     * Note the token size estimate is updated by each call, as the token
     * size is not likely to change (as long as we call the same TSA using
     * the same imprint length).
     * @param url String - Time Stamp Authority URL (i.e. "http://tsatest1.digistamp.com/TSA")
     * @param username String - user(account) name
     * @param password String - password
     * @param tokSzEstimate int - estimated size of received time stamp token (DER encoded)
     */
    public TSAClientBouncyCastle(String url, String username, String password, int tokSzEstimate) {
        this.tsaURL       = url;
        this.tsaUsername  = username;
        this.tsaPassword  = password;
        this.tokSzEstimate = tokSzEstimate;
    }
    
    /**
     * Get the token size estimate.
     * Returned value reflects the result of the last succesfull call, padded
     * @return int
     */
    public int getTokenSizeEstimate() {
        return tokSzEstimate;
    }
    
    public byte[] getTimeStampToken(PdfPKCS7 caller, byte[] imprint) throws Exception {
        return getTimeStampToken(imprint);
    }
    
    /**
     * Get timestamp token - Bouncy Castle request encoding / decoding layer
     */
    protected byte[] getTimeStampToken(byte[] imprint) throws Exception {
        byte[] respBytes = null;
        try {
            // Setup the time stamp request
            TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
            tsqGenerator.setCertReq(true);
            // tsqGenerator.setReqPolicy("1.3.6.1.4.1.601.10.3.1");
            BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
            TimeStampRequest request = tsqGenerator.generate(X509ObjectIdentifiers.id_SHA1.getId() , imprint, nonce);
            byte[] requestBytes = request.getEncoded();
            
            // Call the communications layer
            respBytes = getTSAResponse(requestBytes);
            
            // Handle the TSA response
            TimeStampResponse response = new TimeStampResponse(respBytes);
            
            // validate communication level attributes (RFC 3161 PKIStatus)
            response.validate(request);
            PKIFailureInfo failure = response.getFailInfo();
            int value = (failure == null) ? 0 : failure.intValue();
            if (value != 0) {
                // @todo: Translate value of 15 error codes defined by PKIFailureInfo to string
                throw new Exception("Invalid TSA '" + tsaURL + "' response, code " + value);
            }
            // @todo: validate the time stap certificate chain (if we want
            //        assure we do not sign using an invalid timestamp).
            
            // extract just the time stamp token (removes communication status info)
            TimeStampToken  tsToken = response.getTimeStampToken();
            if (tsToken == null) {
                throw new Exception("TSA '" + tsaURL + "' failed to return time stamp token: " + response.getStatusString());
            }
            TimeStampTokenInfo info = tsToken.getTimeStampInfo(); // to view details
            byte[] encoded = tsToken.getEncoded();
            long stop = System.currentTimeMillis();
            
            // Update our token size estimate for the next call (padded to be safe)
            this.tokSzEstimate = encoded.length + 32;
            return encoded;
        } catch (Exception e) {
            throw e;
        } catch (Throwable t) {
            throw new Exception("Failed to get TSA response from '" + tsaURL +"'", t);
        }
    }
    
    /**
     * Get timestamp token - communications layer
     * @return - byte[] - TSA response, raw bytes (RFC 3161 encoded)
     */
    protected byte[] getTSAResponse(byte[] requestBytes) throws Exception {
        // Setup the TSA connection
        URL url = new URL(tsaURL);
        URLConnection tsaConnection;
        tsaConnection = (URLConnection) url.openConnection();
        
        tsaConnection.setDoInput(true);
        tsaConnection.setDoOutput(true);
        tsaConnection.setUseCaches(false);
        tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
        //tsaConnection.setRequestProperty("Content-Transfer-Encoding", "base64");
        tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");
        
        if ((tsaUsername != null) && !tsaUsername.equals("") ) {
            String userPassword = tsaUsername + ":" + tsaPassword;
            tsaConnection.setRequestProperty("Authorization", "Basic " +
                new String(new sun.misc.BASE64Encoder().encode(userPassword.getBytes())));
        };
        OutputStream out = tsaConnection.getOutputStream();
        out.write(requestBytes);
        out.close();
        
        // Get TSA response as a byte array
        InputStream inp = tsaConnection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
            baos.write(buffer, 0, bytesRead);
        }
        byte[] respBytes = baos.toByteArray();
        
        String encoding = tsaConnection.getContentEncoding();
        if (encoding != null && encoding.equalsIgnoreCase("base64")) {
            sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
            respBytes = dec.decodeBuffer(new String(respBytes));
        }
        return respBytes;
    }    
}