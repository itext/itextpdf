package sandbox.xmlworker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;

/**
 * Converts an HTML file into an XTHML file.
 */
public class D00_XHTML {

    /** The name of a HTML file */
    public static final String WALDEN = "resources/html/walden.html";
    /** The name of a HTML file */
    public static final String THOREAU = "resources/html/thoreau.html";
    
    /** The main method. */
    public static void main(String[] args) throws IOException {
        tidyUp(WALDEN);
        tidyUp(THOREAU);
    }
    
    public static void tidyUp(String path) throws IOException {
        File html = new File(path);
        byte[] xhtml = Jsoup.parse(html, "US-ASCII").html().getBytes();
        File dir = new File("results/xml");
        dir.mkdirs();
        FileOutputStream fos = new FileOutputStream(new File(dir, html.getName()));
        fos.write(xhtml);
        fos.close();
    }
}
