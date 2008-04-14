/* in_action/chapter17/FoobarCourses.java */

package in_action.chapter17;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.SimpleBookmark;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoobarCourses extends HttpServlet {

	private static final long serialVersionUID = -3820172470814287447L;
	
	String resource = "/home/itext/main/examples/results/in_action/chapter14/course_catalog_events.pdf";

	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		makeHtml(response.getOutputStream(), "");
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		makePdf(request, response);
	}

	private void makeHtml(ServletOutputStream stream, String msg)
			throws IOException {
		PdfReader reader = new PdfReader(resource);
		List list = SimpleBookmark.getBookmark(reader);
		Map bookmark;
		stream
				.print("<html>\n\t<head>\n\t\t<title>Print your own Course Catalog</title>\n\t</head>\n\t<body>");
		stream.print(msg);
		stream.print("<form method=\"POST\"><table>");
		int p = 0;
		for (Iterator i = list.iterator(); i.hasNext();) {
			bookmark = (Map) i.next();
			stream.print("<tr><td>");
			stream.print((String) bookmark.get("Title"));
			stream
					.print("</td><td><input type=\"Checkbox\" name=\"page\" value=\""
							+ (++p));
			stream.print("\"></td>");
		}
		stream
				.print("</table><input type=\"Submit\" value=\"Get PDF\"></form>\n\t</body>\n</html>");
	}

	/**
	 * Performs the action: generate a PDF from a GET or POST.
	 * 
	 * @param request
	 *            the Servlets request object
	 * @param response
	 *            the Servlets request object
	 * @param methodGetPost
	 *            the method that was used in the form
	 * @throws ServletException
	 * @throws IOException
	 */
	public void makePdf(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String[] pages = request.getParameterValues("page");
			// take the message from the URL or create default message
			StringBuffer selection = new StringBuffer();
			if (pages.length == 0) {
				response.setContentType("text/html");
				makeHtml(response.getOutputStream(),
						"You must at least choose one!");
				return;
			}
			selection.append(pages[0]);
			for (int i = 1; i < pages.length; i++) {
				selection.append(",");
				selection.append(pages[i]);
			}
			PdfReader reader = new PdfReader(resource);
			reader.selectPages(selection.toString());
			int p = reader.getNumberOfPages();
			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfCopy copy = new PdfCopy(document, baos);
			document.open();
			for (int i = 0; i < p;) {
				i++;
				copy.addPage(copy.getImportedPage(reader, i));
			}
			copy.setViewerPreferences(PdfWriter.PageModeUseThumbs);
			copy.setOutlines(SimpleBookmark.getBookmark(reader));
			document.close();

			// setting some response headers
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			// setting the content type
			response.setContentType("application/pdf");
			// the contentlength is needed for MSIE!!!
			response.setContentLength(baos.size());
			// write ByteArrayOutputStream to the ServletOutputStream
			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();
		} catch (Exception e) {
			System.out.println("Error in " + getClass().getName() + "\n" + e);
			throw (new ServletException(e));
		}
	}

	/**
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
	}

	/**
	 * Generates a file with a header and a footer.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 17: Foobar Courses");
		System.out.println("-> THIS EXAMPLE SHOULD BE EXECUTED ON");
		System.out.println("   AN APPLICATION SERVER");
	}
}