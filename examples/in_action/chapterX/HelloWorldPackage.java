package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class HelloWorldPackage {
	public static void main(String[] args) {
		in_action.chapter02.HelloWorldCopy.createPdfs();
		try {
			PdfReader reader = new PdfReader("results/in_action/chapter02/Hello1.pdf");
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("results/in_action/chapterX/HelloWorldPackage.pdf"), '7');
			stamper.addFileAttachment("An attached PDF file (Hello2.pdf)", null, "results/in_action/chapter02/Hello2.pdf", "Hello2.pdf");
			stamper.addFileAttachment("An attached PDF file (Hello3.pdf)", null, "results/in_action/chapter02/Hello3.pdf", "Hello3.pdf");
			stamper.makePackage( PdfName.T );
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
