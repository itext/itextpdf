package com.itextpdf.text.pdf.security;

import java.security.cert.X509Certificate;

public class VerificationOK {

	protected X509Certificate certificate;
	protected Class<? extends CertificateVerifier> verifierClass;
	protected String message;
	public VerificationOK(X509Certificate certificate,
			Class<? extends CertificateVerifier> verifierClass, String message) {
		this.certificate = certificate;
		this.verifierClass = verifierClass;
		this.message = message;
	}
	/**
	 * @param certificate the certificate to set
	 */
	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}
	/**
	 * @param verifierClass the verifierClass to set
	 */
	public void setVerifierClass(Class<CertificateVerifier> verifierClass) {
		this.verifierClass = verifierClass;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(certificate.getSubjectDN().getName());
		sb.append(" verified with ");
		sb.append(verifierClass.getName());
		sb.append(": ");
		sb.append(message);
		return sb.toString();
	}
}
