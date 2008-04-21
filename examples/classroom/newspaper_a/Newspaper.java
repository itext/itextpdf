package classroom.newspaper_a;

public abstract class Newspaper {

	public static final String RESULTPATH = "results/classroom/newspaper/";
	public static final String RESOURCESPATH = "resources/classroom/newspaper/";
	public static final String NEWSPAPER = RESOURCESPATH + "metro.pdf";
	
	public static final float LLX1 = 190;
	public static final float LLY1 = 41;
	public static final float URX1 = 320;
	public static final float URY1 = 328;
	public static final float W1 = URX1 - LLX1;
	public static final float H1 = URY1 - LLY1;

	public static final float LLX2 = 328;
	public static final float LLY2 = 41;
	public static final float URX2 = 734;
	public static final float URY2 = 611;
	public static final float W2 = URX2 - LLX2;
	public static final float H2 = URY2 -LLY2;
	
	public static final String MESSAGE = "Your ad could be here. Contact +32 555 12 34 for more information.";
}
