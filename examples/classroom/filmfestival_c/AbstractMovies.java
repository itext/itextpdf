package classroom.filmfestival_c;

import java.awt.Color;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

/**
 * This abstract class has some constants we are going to use frequently.
 * We'll extend this class to make actual PDFs with method makePdf.
 */

public abstract class AbstractMovies {
	public static final int YEAR = 2007;
	public static final long TIME930 = 30600000l;
	
	public static final Color SILVER = new Color(0xC0, 0xC0, 0xC0);
	public static final Color BLACK = new Color(0x00, 0x00, 0x00);
	public static final Color WHITE = new Color(0xFF, 0xFF, 0xFF);

	public static final Color[] COLOR = new Color[7];
	static {
		COLOR[0] = new Color(0x00, 0x00, 0x00); // unknown
		COLOR[1] = new Color(0xFF, 0x8C, 0x00); // comp
		COLOR[2] = new Color(0x00, 0xCE, 0xD1); // prev
		COLOR[3] = new Color(0x7C, 0xFC, 0x00); // world
		COLOR[4] = new Color(0xAD, 0xD8, 0xE6); // hist
		COLOR[5] = new Color(0xFF, 0x00, 0x00); // apart
		COLOR[6] = new Color(0xFF, 0xFF, 0x00); // focus
	}
	public static final float[] WIDTHS = {1, 10, 10};
	public static final float[] INNER = {6, 1, 1};
	
	public static final Font BOLD = new Font(Font.HELVETICA, 12, Font.BOLD);
	public static final Font BOLDSMALL = new Font(Font.HELVETICA, 10, Font.BOLD);
	public static final Font SMALL = new Font(Font.HELVETICA, 10);
	public static final Font SMALLEST = new Font(Font.HELVETICA, 8);
	public static final Font NORMALWHITE = new Font(Font.HELVETICA, 12, Font.NORMAL, WHITE);
	public static final BaseFont FONT = FontFactory.getFont(
			BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED).getBaseFont();
	
	public static final int COLUMNS = 33;
	public static final String TIME[] = new String[COLUMNS];
	static {
		TIME[0] = "09:30";	TIME[1] = "10:00";	TIME[2] = "10:30";	TIME[3] = "11:00";
		TIME[4] = "11:30";	TIME[5] = "12:00";	TIME[6] = "12:30";	TIME[7] = "13:00";
		TIME[8] = "13:30";	TIME[9] = "14:00";	TIME[10] = "14:30";	TIME[11] = "15:00";
		TIME[12] = "15:30";	TIME[13] = "16:00";	TIME[14] = "16:30";	TIME[15] = "17:00";
		TIME[16] = "17:30";	TIME[17] = "18:00";	TIME[18] = "18:30";	TIME[19] = "19:00";
		TIME[20] = "19:30";	TIME[21] = "20:00";	TIME[22] = "20:30";	TIME[23] = "21:00";
		TIME[24] = "21:30";	TIME[25] = "22:00";	TIME[26] = "22:30";	TIME[27] = "23:00";
		TIME[28] = "23:30";	TIME[29] = "00:00";	TIME[30] = "00:30";	TIME[31] = "01:00";
		TIME[32] = "01:30";
	}
	
}