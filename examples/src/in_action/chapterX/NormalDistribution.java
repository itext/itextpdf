package in_action.chapterX;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class NormalDistribution {

	/** the largest value stored in the list of entered values. */
	protected int maximumValue = Integer.MIN_VALUE;
	/** the largest value stored in the list of entered values. */
	protected int maximumKey = Integer.MIN_VALUE;
	/** the largest value stored in the list of entered values. */
	protected int minimumKey = Integer.MAX_VALUE;

	/** the number of entered values. */
	protected int n = 0;

	/** the average of all the entered values. */
	protected float mu = 0f;

	/** the standard deviation. */
	protected float sigma = 0f;

	/** the skewness. */
	protected float skew = 0f;
	
	/** the discrete values */
	TreeMap values = new TreeMap();
	
	/**
	 * Reads the values from a CSV file and construct a NormalDistribution object
	 * @param filename
	 * @throws IOException
	 */
	public NormalDistribution(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("resources/in_action/chapterX/test.csv"));
		String line;
		Integer key, value, old;
		StringTokenizer st;
		float total = 0;
		while ((line = reader.readLine()) != null) {
			st = new StringTokenizer(line, ";");
			key = new Integer(st.nextToken());
			value = new Integer(st.nextToken());
			if (maximumValue < value.intValue()) {
				maximumValue = value.intValue();
			}
			if (value.intValue() > 0 && minimumKey > key.intValue()) {
				minimumKey = key.intValue();
			}
			if (value.intValue() > 0 && maximumKey < key.intValue()) {
				maximumKey = key.intValue();
			}
			n += value.intValue();
			total += value.floatValue() * key.floatValue();
			if (values.containsKey(key)) {
				old = (Integer) values.get(key);
				values.put(key, new Integer(old.intValue() + value.intValue()));
			}
			else {
				values.put(key, value);
			}
		}
		mu = total / n;
		for (Iterator i = values.keySet().iterator(); i.hasNext(); ) {
			key = (Integer) i.next();
			value = (Integer) values.get(key);
			sigma += Math.pow(key.doubleValue() - mu, 2.0) * value.intValue() / n;
			skew += Math.pow(key.doubleValue() - mu, 3.0) * value.intValue() / n;
		}
		sigma = (float) Math.sqrt(sigma);
		skew = skew / (float)Math.pow(sigma, 3f);
	}
	
	/**
	 * Reads a CSV file with values and their frequency.
	 * Draws the frequency table and the Gauss Curve.
	 * @param args no arguments needed here
	 */
	public static void main(String[] args) {
		try {
			NormalDistribution nd = new NormalDistribution("results/in_action/chapterX/test.csv");
			nd.createPDF("results/in_action/chapterX/normal_distribution.pdf");
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Creates the PDF. 
	 * @param filename
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 */
	public void createPDF(String filename) throws FileNotFoundException, DocumentException {
		int max = ((maximumValue / 10) + 1) * 10;
		Document document = new Document(PageSize.A4.rotate());
		float step = (document.right() - document.left()) / (values.size() * 2);
		float start = PageSize.A4.getHeight() / 4;
		float top = document.top();
		float bottom = document.bottom() + 36;
		float height = top - bottom;
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		Integer key;
		int  value;
		for (Iterator i = values.keySet().iterator(); i.hasNext(); ) {
			key = (Integer)i.next();
			value = ((Integer)values.get(key)).intValue();
			cb.rectangle(start - step / 4, bottom, step / 2, ((float)value / (float)max) * height);
			start += step;
		}
		cb.stroke();
		cb.setRGBColorStroke(0xFF, 0, 0);
		start = (PageSize.A4.getHeight() / 4) - ((float)step * 3f);
		float mn = ((Integer)values.firstKey()).intValue() - 3;
		float mx = ((Integer)values.lastKey()).intValue() + 3.1f;
		for (float i = mn; i < mx; ) {
			if (i == mn) {
				cb.moveTo(start, (frequency(i) / (float)max) * height + bottom);
			}
			else {
				cb.lineTo(start, (frequency(i) / (float)max) * height + bottom);
			}
			start += step / 10f;
			i += 0.1f;
		}
		cb.stroke();
		cb.setLineWidth(3);
		start = (PageSize.A4.getHeight() / 4);
		float p;
		p = start + step * (value(10f) - 10f);
		cb.moveTo(p, bottom);
		cb.lineTo(p, bottom + ((frequency(value(10f)) / (float)max) * height));
		p = start + step * (value(35f) - 10f);
		cb.moveTo(p, bottom);
		cb.lineTo(p, bottom + ((frequency(value(35f)) / (float)max) * height));
		p = start + step * (value(65f) - 10f);
		cb.moveTo(p, bottom);
		cb.lineTo(p, bottom + ((frequency(value(65f)) / (float)max) * height));
		p = start + step * (value(90f) - 10f);
		cb.moveTo(p, bottom);
		cb.lineTo(p, bottom + ((frequency(value(90f)) / (float)max) * height));
		cb.stroke();
		cb.setRGBColorStroke(0xFF, 0xFF, 0x00);
		p = start + step * (discreteValue(10f) - 10f);
		cb.moveTo(p, bottom);
		cb.lineTo(p, bottom + max / 2);
		p = start + step * (discreteValue(35f) - 10f);
		cb.moveTo(p, bottom);
		cb.lineTo(p, bottom + max / 2);
		p = start + step * (discreteValue(65f) - 10f);
		cb.moveTo(p, bottom);
		cb.lineTo(p, bottom + max / 2);
		p = start + step * (discreteValue(90f) - 10f);
		cb.moveTo(p, bottom);
		cb.lineTo(p, bottom + max / 2);
		cb.stroke();
		document.close();
		System.out.println("A - B: " + discreteValue(90f) + " Gauss: " + value(90f));
		System.out.println("B - C: " + discreteValue(65f) + " Gauss: " + value(65f));
		System.out.println("C - D: " + discreteValue(35f) + " Gauss: " + value(35f));
		System.out.println("D - E: " + discreteValue(10f) + " Gauss: " + value(10f));
	}	

	/**
	 * Returns the frequency of a value in the list.
	 * @param key
	 *
	 * @return	a frequency
	 */

	public float frequency(float key) {
		return n * percentage(key);
	}

	/**
	 * Returns the frequency as a percentage.
	 * @param key
	 *
	 * @return	a percentage
	 */

	public float percentage(float key) {
		return (float)Math.exp(-0.5f * (float)Math.pow(normalize(key), 2.0f)) / (float)Math.sqrt(2f * Math.PI * Math.pow(sigma, 2.0f));
	}
	/**
	 * Normalizes a value.
	 *
	 * @param	value	a value
	 * @return	the normalized value
	 */

	public float normalize(float value) {
		return (value - mu) / sigma;
	}

	/**
	 * Returns the value corresponding with a given percentile.
	 * @param percentile
	 * @return	a value
	 */

	public float value(float percentile) {
		return invert(normalValue(percentile));
	}
	/**
	 * Turns a normalized value back into a concrete value.
	 *
	 * @param	normalValue		a normalized value
	 * @return	a concrete value
	 */

	public float invert(float normalValue) {
		return mu + sigma * normalValue;
	}
	/**
	 * Returns the normalized value corresponding with a given percentile.
	 * <P> 
	 * algorithm: Beasley Springer
	 * @param percentile
	 *
	 * @return	a normalized value
	 */

	public static float normalValue(float percentile) {
		float p50 = 0.01f * Math.abs(50f - percentile);
		float z1;
		float z2;
		if (p50 > 0.42) {
			float p = (float) Math.sqrt(-Math.log(0.5f - p50));
			z1 = ((2.3212128f * p + 4.8501413f) * p - 2.2979648f) * p - 2.7871893f;
			z2 = (1.6370678f * p + 3.5438892f) * p + 1f;
		}
		else {
			float p = (float)Math.pow(p50, 2.0f);
			z1 = p50 * (((-25.4410605f * p + 41.3911977f) * p - 18.6150006f) * p + 2.5066282f);
			z2 = (((3.1308291f * p - 21.0622410f) * p + 23.0833674f) * p - 8.4735109f) * p + 1f;
		}
		if (percentile < 50f) {
			return -z1 / z2;
		}
		return z1 / z2;
	}

	/**
	 * Returns an estimation the value corresponding with a given percentile.
	 *
	 * @param        a percentile
	 * @return        a value
	 */
	public float discreteValue(float percentile) {
		int valueBelow = minimumKey;
		float percentileBelow = percentile(minimumKey);
		if (percentile <= percentileBelow) {
			return minimumKey;
		}

		int valueAbove = maximumKey;
		float percentileAbove = percentile(valueAbove);
		if (percentile > percentileAbove) { 
			return maximumKey;
		}

		for (Iterator i = values.keySet().iterator (); i.hasNext ();) {
			valueAbove = ((Integer) i.next ()).intValue ();
			percentileAbove = percentile(valueAbove);

			if ((percentileBelow <= percentile) &&
					(percentile < percentileAbove)) {
				float fraction = (percentile - percentileBelow) / (percentileAbove -
					percentileBelow);
				return valueBelow + (fraction * (valueAbove - valueBelow));
			}
			valueBelow = valueAbove;
			percentileBelow = percentileAbove;
		}
		return valueAbove;
	}

	/**
	 * Calculates the percentile corresponding with a certain value.
	 *
	 * @param        a value
	 * @return        the corresponding percentile
	 */
	public float percentile(int value) {
		return percentageBelow (value) + (discretePercentage(value) / 2);
	}

	/**
	 * Returns the total percentage of values below a given value.
	 *
	 * @return        a percentage
	 */
	private float percentageBelow (int key) {
		return (100.0f * frequencyBelow (key)) / n;
	}

	/**
	 * Returns the total number of values below a given value.
	 *
	 * @return        a frequency
	 */
	private int frequencyBelow (int key) {
		int f = 0;
		Integer keyBelow;
		Integer value;

		for (Iterator i = values.keySet().iterator (); i.hasNext ();) {
			keyBelow = (Integer) i.next();

			if (keyBelow.doubleValue () < key) {
				value = (Integer) values.get(keyBelow);
				f += value.intValue ();
			} else {
				return f;
			}
		}
		return f;
	}

	/**
	 * Returns the frequency as a percentage.
	 *
	 * @return        a percentage
	 */
	public float discretePercentage (int key) {
		return (100.0f * frequency (key)) / n;
	}
}
