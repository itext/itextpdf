/* in_action/chapter16/SignedPdf.java */

package in_action.chapter16;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class SignedPdf {

	/**
	 * Creates a signed PDF file.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 16: example SignedPdf");
		System.out.println("-> Creates a signed PDF;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   Resource needed: .keystore");
		System.out.println("-> Resulting PDFs: unsigned_message.pdf, signed_message.pdf,");
		System.out.println("   corrupted_message.pdf, signed_message_invisible.pdf,");
		System.out.println("   double_signed_message.pdf, revision_1.pdf and revision_2.pdf");

		createPdf();

		PdfReader reader;
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(new FileInputStream("resources/in_action/chapter16/.keystore"), "f00b4r"
					.toCharArray());
			PrivateKey key = (PrivateKey) ks.getKey("foobar", "r4b00f"
					.toCharArray());
			Certificate[] chain = ks.getCertificateChain("foobar");
			reader = new PdfReader("results/in_action/chapter16/unsigned_message.pdf");
			FileOutputStream os = new FileOutputStream("results/in_action/chapter16/signed_message.pdf");
			PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
			PdfSignatureAppearance appearance = stamper
					.getSignatureAppearance();
			appearance.setCrypto(key, chain, null,
					PdfSignatureAppearance.SELF_SIGNED);
			appearance.setReason("It's personal.");
			appearance.setLocation("Foobar");
			appearance.setVisibleSignature(new Rectangle(30, 750, 500, 565), 1,
					null);
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
		
		try {
			reader = new PdfReader("results/in_action/chapter16/signed_message.pdf");
			Document document = new Document(reader.getPageSizeWithRotation(1));
			// step 2
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(
					"results/in_action/chapter16/corrupted_message.pdf"));
			// step 3
			document.open();
			// step 4
			copy.addPage(copy.getImportedPage(reader, 1));
			// step 5
			document.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (DocumentException de) {
			de.printStackTrace();
		}

		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(new FileInputStream("resources/in_action/chapter16/.keystore"), "f00b4r"
					.toCharArray());
			PrivateKey key = (PrivateKey) ks.getKey("foobar", "r4b00f"
					.toCharArray());
			Certificate[] chain = ks.getCertificateChain("foobar");
			reader = new PdfReader("results/in_action/chapter16/unsigned_message.pdf");
			FileOutputStream os = new FileOutputStream("results/in_action/chapter16/signed_message_invisible.pdf");
			PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
			PdfSignatureAppearance appearance = stamper
					.getSignatureAppearance();
			appearance.setCrypto(key, chain, null,
					PdfSignatureAppearance.SELF_SIGNED);
			appearance.setReason("It's personal.");
			appearance.setLocation("Foobar");
			stamper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			reader = new PdfReader("results/in_action/chapter16/signed_message.pdf");
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(new FileInputStream("resources/in_action/chapter16/.keystore"), "f00b4r"
					.toCharArray());
			PrivateKey key = (PrivateKey) ks.getKey("foobar", "r4b00f"
					.toCharArray());
			Certificate[] chain = ks.getCertificateChain("foobar");
			FileOutputStream os = new FileOutputStream(
					"results/in_action/chapter16/double_signed_message.pdf");
			PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0',
					null, true);
			PdfSignatureAppearance appearance = stamper
					.getSignatureAppearance();
			appearance.setCrypto(key, chain, null,
					PdfSignatureAppearance.SELF_SIGNED);
			appearance.setReason("Double signed.");
			appearance.setLocation("Foobar");
			appearance.setVisibleSignature(new Rectangle(300, 750, 500, 800),
					1, "secondsig");
			stamper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			Collection col = cf.generateCertificates(new FileInputStream(
					"resources/in_action/chapter16/foobar.cer"));
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, null);
			for (Iterator it = col.iterator(); it.hasNext();) {
				X509Certificate cert = (X509Certificate) it.next();
				System.out.println(cert.getIssuerDN().getName());
				ks.setCertificateEntry(cert.getSerialNumber().toString(
						Character.MAX_RADIX), cert);
			}
			reader = new PdfReader("results/in_action/chapter16/double_signed_message.pdf");
			AcroFields af = reader.getAcroFields();
			ArrayList names = af.getSignatureNames();
			String name;
			for (Iterator it = names.iterator(); it.hasNext();) {
				name = (String) it.next();
				System.out.println("Signature name: " + name);
				System.out.println("Signature covers whole document: "
						+ af.signatureCoversWholeDocument(name));
				System.out.println("Document revision: " + af.getRevision(name)
						+ " of " + af.getTotalRevisions());
				FileOutputStream os = new FileOutputStream("results/in_action/chapter16/revision_"
						+ af.getRevision(name) + ".pdf");
				byte bb[] = new byte[8192];
				InputStream ip = af.extractRevision(name);
				int n = 0;
				while ((n = ip.read(bb)) > 0)
					os.write(bb, 0, n);
				os.close();
				ip.close();
				PdfPKCS7 pk = af.verifySignature(name);
				Calendar cal = pk.getSignDate();
				Certificate pkc[] = pk.getCertificates();
				System.out
						.println("Subject: "
								+ PdfPKCS7.getSubjectFields(pk
										.getSigningCertificate()));
				System.out.println("Document modified: " + !pk.verify());
				Object fails[] = PdfPKCS7
						.verifyCertificates(pkc, ks, null, cal);
				if (fails == null)
					System.out
							.println("Certificates verified against the KeyStore");
				else
					System.out.println("Certificate failed: " + fails[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createPdf() {
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter16/unsigned_message.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			document
					.add(new Paragraph("This is a personal message from Laura."));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}