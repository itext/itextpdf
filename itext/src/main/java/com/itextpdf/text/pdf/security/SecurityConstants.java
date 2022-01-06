/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Pavel Alay, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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

public final class SecurityConstants {

    public static final String XMLNS = "xmlns";
    public static final String XMLNS_XADES = "xmlns:xades";

    public static final String OIDAsURN = "OIDAsURN";
    public static final String OID_DSA_SHA1 = "urn:oid:1.2.840.10040.4.3";
    public static final String OID_DSA_SHA1_DESC = "ANSI X9.57 DSA signature generated with SHA-1 hash (DSA x9.30)";

    public static final String OID_RSA_SHA1 = "urn:oid:1.2.840.113549.1.1.5";
    public static final String OID_RSA_SHA1_DESC = "RSA (PKCS #1 v1.5) with SHA-1 signature";

    public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    public static final String XMLDSIG_URI = "http://www.w3.org/2000/09/xmldsig#";
    public static final String XADES_132_URI = "http://uri.etsi.org/01903/v1.3.2#";

    public static final String SHA1_URI = "http://www.w3.org/2000/09/xmldsig#sha1";
    public static final String SignedProperties_Type = "http://uri.etsi.org/01903#SignedProperties";

    public static final String DSA = "DSA";
    public static final String RSA = "RSA";
    public static final String SHA1 = "SHA1";

    public static final String DigestMethod = "DigestMethod";
    public static final String DigestValue = "DigestValue";
    public static final String Signature = "Signature";
    public static final String SignatureValue = "SignatureValue";
    public static final String X509SerialNumber = "X509SerialNumber";
    public static final String X509IssuerName = "X509IssuerName";

    public static final String Algorithm = "Algorithm";
    public static final String Id = "Id";
    public static final String ObjectReference = "ObjectReference";
    public static final String Target = "Target";
    public static final String Qualifier = "Qualifier";

    public static final String XADES_Encoding = "xades:Encoding";
    public static final String XADES_MimeType = "xades:MimeType";
    public static final String XADES_Description = "xades:Description";
    public static final String XADES_DataObjectFormat = "xades:DataObjectFormat";
    public static final String XADES_SignedDataObjectProperties = "xades:SignedDataObjectProperties";
    public static final String XADES_IssuerSerial = "xades:IssuerSerial";
    public static final String XADES_CertDigest = "xades:CertDigest";
    public static final String XADES_Cert = "xades:Cert";
    public static final String XADES_SigningCertificate = "xades:SigningCertificate";
    public static final String XADES_SigningTime = "xades:SigningTime";
    public static final String XADES_SignedSignatureProperties = "xades:SignedSignatureProperties";
    public static final String XADES_SignedProperties = "xades:SignedProperties";
    public static final String XADES_QualifyingProperties = "xades:QualifyingProperties";
    public static final String XADES_SignaturePolicyIdentifier = "xades:SignaturePolicyIdentifier";
    public static final String XADES_SignaturePolicyId = "xades:SignaturePolicyId";
    public static final String XADES_SigPolicyId = "xades:SigPolicyId";
    public static final String XADES_Identifier = "xades:Identifier";
    public static final String XADES_SigPolicyHash = "xades:SigPolicyHash";

    public static final String Reference_ = "Reference-";
    public static final String SignedProperties_ = "SignedProperties-";
    public static final String Signature_ = "Signature-";

    public static final String SigningTimeFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
}
