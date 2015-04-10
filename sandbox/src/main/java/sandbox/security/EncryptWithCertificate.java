/*
 * This example was written by Bruno Lowagie.
 */
package sandbox.security;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * The file created using this example can not be opened, unless
 * you import the private key stored in test.p12 in your certificate store.
 * The password for the p12 file is kspass.
 */
public class EncryptWithCertificate {
    
    public static final String PUBLIC = "resources/misc/test.cer";
    public static final String DEST = "results/security/encrypted.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException, CertificateException, NoSuchAlgorithmException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new EncryptWithCertificate().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException, CertificateException, NoSuchAlgorithmException {     

        Security.addProvider(new BouncyCastleProvider());
        
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));

        Certificate cert = getPublicCertificate(PUBLIC);
        writer.setEncryption(
                new Certificate[]{cert},
                new int[]{PdfWriter.ALLOW_PRINTING},
                PdfWriter.ENCRYPTION_AES_256);

        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("My secret hello"));
        // step 5
        document.close();
    }
    
    public Certificate getPublicCertificate(String path)
        throws IOException, CertificateException {
        FileInputStream is = new FileInputStream(path);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
        return cert;
    }
}
