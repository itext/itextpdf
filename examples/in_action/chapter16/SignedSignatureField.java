/* in_action/chapter16/SignedSignatureField.java */

package in_action.chapter16;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SignedSignatureField {

	/**
	 * Creates a signed PDF file.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {

		UnsignedSignatureField.main(new String[0]);
		
		System.out.println("Chapter 16: example Signing a Pdf file");
		System.out.println("-> Creates a signed PDF;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   signed_signature_field.pdf");

		PdfReader reader;
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(new FileInputStream("resources/in_action/chapter16/.keystore"), "f00b4r"
					.toCharArray());
			PrivateKey key = (PrivateKey) ks.getKey("foobar", "r4b00f"
					.toCharArray());
			Certificate[] chain = ks.getCertificateChain("foobar");
			reader = new PdfReader("results/in_action/chapter16/unsigned_signature_field.pdf");
			FileOutputStream os = new FileOutputStream(
					"results/in_action/chapter16/signed_signature_field.pdf");
			PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
			PdfSignatureAppearance appearance = stamper
					.getSignatureAppearance();
			appearance.setCrypto(key, chain, null,
					PdfSignatureAppearance.SELF_SIGNED);
			appearance.setReason("It's personal.");
			appearance.setLocation("Foobar");
			appearance.setVisibleSignature("foobarsig");
			stamper.close();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}