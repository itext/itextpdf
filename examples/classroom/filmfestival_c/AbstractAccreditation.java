/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.filmfestival_c;

import java.awt.Color;

import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

public class AbstractAccreditation {

	public static final String FORM = "results/classroom/filmfestival/movies22.pdf";
	public static final float WIDTH = mm2pt(53.98f);
	public static final float HEIGHT = mm2pt(85.60f);
	public static final String KUBRICK = "resources/classroom/filmfestival/kubrick.jpg";
	public static final String LOGO = "resources/classroom/filmfestival/fflogo.jpg";
	public static final BaseFont FONT = FontFactory.getFont(BaseFont.HELVETICA,
			BaseFont.WINANSI, BaseFont.NOT_EMBEDDED).getBaseFont();
	public static final Color RED = new Color(255, 0, 80);
	public static final Color GREEN = new Color(192, 222, 30);
	public static final Color BLUE = new Color(0, 173, 205);
	public static final Color ORANGE = new Color(241, 165, 1);
	public static final Color GRAY = new Color(192, 192, 192);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color[] COLOR = { GRAY, GREEN, RED, BLUE, ORANGE };
	public static final String[] TYPE = { "Guest", "Student", "Press", "Jury",
			"VIP Pass", "Crew" };
	public static final String PHOTO = "resources/classroom/filmfestival/ingeborg.jpg";
	
	public static float mm2pt(float mm) {
		return (mm * 7.2f) / 2.54f;
	}
}
