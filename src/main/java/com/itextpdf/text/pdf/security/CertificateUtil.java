/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * This class contains a series of static methods that
 * allow you to retrieve information from a Certificate.
 */
public class CertificateUtil {

	// inner classes
	
    /**
     * a class that holds an X509 name
     */
    public static class X500Name {
        /** country code - StringType(SIZE(2)) */
        public static final ASN1ObjectIdentifier C = new ASN1ObjectIdentifier("2.5.4.6");

        /** organization - StringType(SIZE(1..64)) */
        public static final ASN1ObjectIdentifier O = new ASN1ObjectIdentifier("2.5.4.10");

        /** organizational unit name - StringType(SIZE(1..64)) */
        public static final ASN1ObjectIdentifier OU = new ASN1ObjectIdentifier("2.5.4.11");

        /** Title */
        public static final ASN1ObjectIdentifier T = new ASN1ObjectIdentifier("2.5.4.12");

        /** common name - StringType(SIZE(1..64)) */
        public static final ASN1ObjectIdentifier CN = new ASN1ObjectIdentifier("2.5.4.3");

        /** device serial number name - StringType(SIZE(1..64)) */
        public static final ASN1ObjectIdentifier SN = new ASN1ObjectIdentifier("2.5.4.5");

        /** locality name - StringType(SIZE(1..64)) */
        public static final ASN1ObjectIdentifier L = new ASN1ObjectIdentifier("2.5.4.7");

        /** state, or province name - StringType(SIZE(1..64)) */
        public static final ASN1ObjectIdentifier ST = new ASN1ObjectIdentifier("2.5.4.8");

        /** Naming attribute of type X520name */
        public static final ASN1ObjectIdentifier SURNAME = new ASN1ObjectIdentifier("2.5.4.4");
        
        /** Naming attribute of type X520name */
        public static final ASN1ObjectIdentifier GIVENNAME = new ASN1ObjectIdentifier("2.5.4.42");
        
        /** Naming attribute of type X520name */
        public static final ASN1ObjectIdentifier INITIALS = new ASN1ObjectIdentifier("2.5.4.43");
        
        /** Naming attribute of type X520name */
        public static final ASN1ObjectIdentifier GENERATION = new ASN1ObjectIdentifier("2.5.4.44");
        
        /** Naming attribute of type X520name */
        public static final ASN1ObjectIdentifier UNIQUE_IDENTIFIER = new ASN1ObjectIdentifier("2.5.4.45");

        /**
         * Email address (RSA PKCS#9 extension) - IA5String.
         * <p>Note: if you're trying to be ultra orthodox, don't use this! It shouldn't be in here.</p>
         */
        public static final ASN1ObjectIdentifier EmailAddress = new ASN1ObjectIdentifier("1.2.840.113549.1.9.1");

        /**
         * email address in Verisign certificates
         */
        public static final ASN1ObjectIdentifier E = EmailAddress;

        /** object identifier */
        public static final ASN1ObjectIdentifier DC = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.25");

        /** LDAP User id. */
        public static final ASN1ObjectIdentifier UID = new ASN1ObjectIdentifier("0.9.2342.19200300.100.1.1");

        /** A Map with default symbols */
        public static final Map<ASN1ObjectIdentifier, String> DefaultSymbols = new HashMap<ASN1ObjectIdentifier, String>();

        static {
            DefaultSymbols.put(C, "C");
            DefaultSymbols.put(O, "O");
            DefaultSymbols.put(T, "T");
            DefaultSymbols.put(OU, "OU");
            DefaultSymbols.put(CN, "CN");
            DefaultSymbols.put(L, "L");
            DefaultSymbols.put(ST, "ST");
            DefaultSymbols.put(SN, "SN");
            DefaultSymbols.put(EmailAddress, "E");
            DefaultSymbols.put(DC, "DC");
            DefaultSymbols.put(UID, "UID");
            DefaultSymbols.put(SURNAME, "SURNAME");
            DefaultSymbols.put(GIVENNAME, "GIVENNAME");
            DefaultSymbols.put(INITIALS, "INITIALS");
            DefaultSymbols.put(GENERATION, "GENERATION");
        }
        
        /** A HashMap with values */
        public Map<String, ArrayList<String>> values = new HashMap<String, ArrayList<String>>();

        /**
         * Constructs an X509 name
         * @param seq an ASN1 Sequence
         */
        public X500Name(ASN1Sequence seq) {
            @SuppressWarnings("unchecked")
			Enumeration<ASN1Set> e = seq.getObjects();

            while (e.hasMoreElements()) {
                ASN1Set set = e.nextElement();

                for (int i = 0; i < set.size(); i++) {
                    ASN1Sequence s = (ASN1Sequence)set.getObjectAt(i);
                    String id = DefaultSymbols.get(s.getObjectAt(0));
                    if (id == null)
                        continue;
                    ArrayList<String> vs = values.get(id);
                    if (vs == null) {
                        vs = new ArrayList<String>();
                        values.put(id, vs);
                    }
                    vs.add(((ASN1String)s.getObjectAt(1)).getString());
                }
            }
        }
        
        /**
         * Constructs an X509 name
         * @param dirName a directory name
         */
        public X500Name(String dirName) {
            X509NameTokenizer   nTok = new X509NameTokenizer(dirName);

            while (nTok.hasMoreTokens()) {
                String  token = nTok.nextToken();
                int index = token.indexOf('=');

                if (index == -1) {
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("badly.formated.directory.string"));
                }

                String id = token.substring(0, index).toUpperCase();
                String value = token.substring(index + 1);
                ArrayList<String> vs = values.get(id);
                if (vs == null) {
                    vs = new ArrayList<String>();
                    values.put(id, vs);
                }
                vs.add(value);
            }

        }

        /**
         * Gets the first entry from the field array retrieved from the values Map.
         * @param	name	the field name
         * @return	the (first) field value
         */
        public String getField(String name) {
            List<String> vs = values.get(name);
            return vs == null ? null : (String)vs.get(0);
        }

        /**
         * Gets a field array from the values Map
         * @param name
         * @return an ArrayList
         */
        public List<String> getFieldArray(String name) {
            return values.get(name);
        }

        /**
         * Getter for values
         * @return a Map with the fields of the X509 name
         */
        public Map<String, ArrayList<String>> getFields() {
            return values;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return values.toString();
        }
    }

    /**
     * class for breaking up an X500 Name into it's component tokens,
     * similar to java.util.StringTokenizer. We need this class as some
     * of the lightweight Java environments don't support classes such
     * as StringTokenizer.
     */
    public static class X509NameTokenizer {
        private String          oid;
        private int             index;
        private StringBuffer    buf = new StringBuffer();

        public X509NameTokenizer(String oid) {
            this.oid = oid;
            this.index = -1;
        }

        public boolean hasMoreTokens() {
            return index != oid.length();
        }

        public String nextToken() {
            if (index == oid.length()) {
                return null;
            }

            int     end = index + 1;
            boolean quoted = false;
            boolean escaped = false;

            buf.setLength(0);

            while (end != oid.length()) {
                char    c = oid.charAt(end);

                if (c == '"') {
                    if (!escaped) {
                        quoted = !quoted;
                    }
                    else {
                        buf.append(c);
                    }
                    escaped = false;
                }
                else {
                    if (escaped || quoted) {
                        buf.append(c);
                        escaped = false;
                    }
                    else if (c == '\\') {
                        escaped = true;
                    }
                    else if (c == ',') {
                        break;
                    }
                    else {
                        buf.append(c);
                    }
                }
                end++;
            }

            index = end;
            return buf.toString().trim();
        }
    }

    // Certificate issuer and subject
    
    /**
     * Get the issuer fields from an X509 Certificate
     * @param cert an X509Certificate
     * @return an X500Name
     */
    public static X500Name getIssuerFields(X509Certificate cert) {
        try {
            return new X500Name((ASN1Sequence)getIssuer(cert.getTBSCertificate()));
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Get the "issuer" from the TBSCertificate bytes that are passed in
     * @param enc a TBSCertificate in a byte array
     * @return a ASN1Primitive
     */
    public static ASN1Primitive getIssuer(byte[] enc) {
        try {
            ASN1InputStream in = new ASN1InputStream(new ByteArrayInputStream(enc));
            ASN1Sequence seq = (ASN1Sequence)in.readObject();
            return (ASN1Primitive)seq.getObjectAt(seq.getObjectAt(0) instanceof ASN1TaggedObject ? 3 : 2);
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Get the subject fields from an X509 Certificate
     * @param cert an X509Certificate
     * @return an X500Name
     */
    public static X500Name getSubjectFields(X509Certificate cert) {
        try {
            return new X500Name((ASN1Sequence)getSubject(cert.getTBSCertificate()));
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Get the "subject" from the TBSCertificate bytes that are passed in
     * @param enc A TBSCertificate in a byte array
     * @return a ASN1Primitive
     */
    public static ASN1Primitive getSubject(byte[] enc) {
        try {
            ASN1InputStream in = new ASN1InputStream(new ByteArrayInputStream(enc));
            ASN1Sequence seq = (ASN1Sequence)in.readObject();
            return (ASN1Primitive)seq.getObjectAt(seq.getObjectAt(0) instanceof ASN1TaggedObject ? 5 : 4);
        }
        catch (IOException e) {
            throw new ExceptionConverter(e);
        }
    }
	
	// Certificate Revocation Lists
	
	/**
	 * Gets a CRL from a certificate
	 * @param certificate
	 * @return	the CRL or null if there's no CRL available
	 * @throws CertificateException
	 * @throws CRLException
	 * @throws IOException
	 */
	public static CRL getCRL(X509Certificate certificate) throws CertificateException, CRLException, IOException {
		return CertificateUtil.getCRL(CertificateUtil.getCRLURL(certificate));
	}

	/**
	 * Gets the URL of the Certificate Revocation List for a Certificate
	 * @param certificate	the Certificate
	 * @return	the String where you can check if the certificate was revoked
	 * @throws CertificateParsingException
	 * @throws IOException 
	 */
	public static String getCRLURL(X509Certificate certificate) throws CertificateParsingException {
	    ASN1Primitive obj;
		try {
			obj = getExtensionValue(certificate, Extension.cRLDistributionPoints.getId());
		} catch (IOException e) {
			obj = null;
		}
	    if (obj == null) {
	        return null;
	    }
	    CRLDistPoint dist = CRLDistPoint.getInstance(obj);
	    DistributionPoint[] dists = dist.getDistributionPoints();
	    for (DistributionPoint p : dists) {
	        DistributionPointName distributionPointName = p.getDistributionPoint();
	        if (DistributionPointName.FULL_NAME != distributionPointName.getType()) {
	            continue;
	        }
	        GeneralNames generalNames = (GeneralNames)distributionPointName.getName();
	        GeneralName[] names = generalNames.getNames();
	        for (GeneralName name : names) {
	            if (name.getTagNo() != GeneralName.uniformResourceIdentifier) {
	                continue;
	            }
	            DERIA5String derStr = DERIA5String.getInstance((ASN1TaggedObject)name.toASN1Primitive(), false);
	            return derStr.getString();
	        }
	    }
	    return null;
	}

	/**
	 * Gets the CRL object using a CRL URL.
	 * @param url	the URL where to get the CRL
	 * @return	a CRL object
	 * @throws IOException
	 * @throws CertificateException
	 * @throws CRLException
	 */
	public static CRL getCRL(String url) throws IOException, CertificateException, CRLException {
		if (url == null)
			return null;
		InputStream is = new URL(url).openStream();
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (CRL)cf.generateCRL(is); 
	}

	// Online Certificate Status Protocol

	/**
	 * Retrieves the OCSP URL from the given certificate.
	 * @param certificate the certificate
	 * @return the URL or null
	 * @throws IOException
	 */
	public static String getOCSPURL(X509Certificate certificate) {
		ASN1Primitive obj;
		try {
			obj = getExtensionValue(certificate, Extension.authorityInfoAccess.getId());
	        if (obj == null) {
	            return null;
	        }
	        ASN1Sequence AccessDescriptions = (ASN1Sequence) obj;
	        for (int i = 0; i < AccessDescriptions.size(); i++) {
	        	ASN1Sequence AccessDescription = (ASN1Sequence) AccessDescriptions.getObjectAt(i);
	        	if ( AccessDescription.size() != 2 ) {
	        		continue;
	        	}
	        	else if (AccessDescription.getObjectAt(0) instanceof ASN1ObjectIdentifier) {
	        		ASN1ObjectIdentifier id = (ASN1ObjectIdentifier)AccessDescription.getObjectAt(0);
	        		if (SecurityIDs.ID_OCSP.equals(id.getId())) {
	            		ASN1Primitive description = (ASN1Primitive)AccessDescription.getObjectAt(1);
	                    String AccessLocation =  getStringFromGeneralName(description);
	                    if (AccessLocation == null) {
	                        return "" ;
	                    }
	                    else {
	                        return AccessLocation ;
	                    }
	                }
	            }
	        }
		} catch (IOException e) {
			return null;
		}
	    return null;
	}

    // Time Stamp Authority

	/**
	 * Gets the URL of the TSA if it's available on the certificate
	 * @param certificate	a certificate
	 * @return	a TSA URL
	 * @throws IOException
	 */
	public static String getTSAURL(X509Certificate certificate) {
	    byte der[] = certificate.getExtensionValue(SecurityIDs.ID_TSA);
	    if(der == null)
	        return null;
	    ASN1Primitive asn1obj;
		try {
			asn1obj = ASN1Primitive.fromByteArray(der);
	        DEROctetString octets = (DEROctetString)asn1obj;
	        asn1obj = ASN1Primitive.fromByteArray(octets.getOctets());
	        ASN1Sequence asn1seq = ASN1Sequence.getInstance(asn1obj);
	        return getStringFromGeneralName(asn1seq.getObjectAt(1).toASN1Primitive());
		} catch (IOException e) {
			return null;
		}
	}
	
	// helper methods

    /**
     * @param certificate	the certificate from which we need the ExtensionValue
     * @param oid the Object Identifier value for the extension.
     * @return	the extension value as an ASN1Primitive object
     * @throws IOException
     */
    private static ASN1Primitive getExtensionValue(X509Certificate certificate, String oid) throws IOException {
        byte[] bytes = certificate.getExtensionValue(oid);
        if (bytes == null) {
            return null;
        }
        ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(bytes));
        ASN1OctetString octs = (ASN1OctetString) aIn.readObject();
        aIn = new ASN1InputStream(new ByteArrayInputStream(octs.getOctets()));
        return aIn.readObject();
    }

    /**
     * Gets a String from an ASN1Primitive
     * @param names	the ASN1Primitive
     * @return	a human-readable String
     * @throws IOException
     */
    private static String getStringFromGeneralName(ASN1Primitive names) throws IOException {
        ASN1TaggedObject taggedObject = (ASN1TaggedObject) names ;
        return new String(ASN1OctetString.getInstance(taggedObject, false).getOctets(), "ISO-8859-1");
    }

}
