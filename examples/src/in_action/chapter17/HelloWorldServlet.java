/* in_action/chapter17/HelloWorldServlet.java */

package in_action.chapter17;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldServlet extends HttpServlet {

	private static final long serialVersionUID = 1936378722615937262L;

	/**
	 * Returns a PDF, RTF or HTML document.
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// we retrieve the presentationtype
		String presentationtype = request.getParameter("presentationtype");

		// step 1
		Document document = new Document();
		try {
			// step 2: we set the ContentType and create an instance of the
			// corresponding Writer
			if ("pdf".equals(presentationtype)) {
				response.setContentType("application/pdf");
				PdfWriter.getInstance(document, response.getOutputStream());
			} else if ("html".equals(presentationtype)) {
				response.setContentType("text/html");
				HtmlWriter.getInstance(document, response.getOutputStream());
			} else if ("rtf".equals(presentationtype)) {
				response.setContentType("text/rtf");
				RtfWriter2.getInstance(document, response.getOutputStream());
			} else {
				response
						.sendRedirect("http://itextdocs.lowagie.com/tutorial/general/webapp/index.html#HelloWorld");
			}

			// step 3
			document.open();

			// step 4
			document.add(new Paragraph("Hello World"));
			document.add(new Paragraph(new Date().toString()));
		} catch (DocumentException de) {
			de.printStackTrace();
			System.err.println("document: " + de.getMessage());
		}

		// step 5: we close the document (the outputstream is also closed
		// internally)
		document.close();
	}

	/**
	 * Generates a file with a header and a footer.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 17: Hello World Example");
		System.out.println("-> THIS EXAMPLE SHOULD BE EXECUTED ON");
		System.out.println("   AN APPLICATION SERVER");
	}
}