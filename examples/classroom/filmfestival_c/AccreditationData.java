/*
 * This example was written by Bruno Lowagie, author of the book
 * 'iText in Action' by Manning Publications (ISBN: 1932394796).
 * You can use this example as inspiration for your own applications.
 * The following license applies:
 * http://www.1t3xt.com/about/copyright/index.php?page=MIT
 */

package classroom.filmfestival_c;

import java.awt.Color;

import com.lowagie.text.Image;

public class AccreditationData {

	/** The color indicating the type of accreditation. */
	private Color typeColor;
	/** The name of the type of accreditation. */
	private String typeName;
	/** The name of the person that is accredited. */
	private String name;
	/** The accreditation number. */
	private String number;
	/** A picture of the person that is accredited. */
	private Image photo;
	/** Indicates if the form has to be flattened. */
	private boolean flatten;
	

	/**
	 * Getter for the type color.
	 * @return	a Color
	 */
	public Color getTypeColor() {
		return typeColor;
	}
	/**
	 * Setter for the type color.
	 * @param type_color
	 */
	public void setTypeColor(Color type_color) {
		this.typeColor = type_color;
	}
	/**
	 * Getter for the type name
	 * @return	a String
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * Setter for the type name.
	 * @param type_name
	 */
	public void setTypeName(String type_name) {
		this.typeName = type_name;
	}
	/**
	 * Getter for the name of the accredited person.
	 * @return a String
	 */
	public String getName() {
		return name;
	}
	/**
	 * Setter for the name of the accredited person.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Getter for the number.
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * Setter for the accreditation number.
	 * @param number
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * Getter for the photo.
	 * @return an Image object
	 */
	public Image getPhoto() {
		return photo;
	}
	/**
	 * Setter for the photo.
	 * @param photo
	 */
	public void setPhoto(Image photo) {
		this.photo = photo;
	}
	/**
	 * Getter for the flatten value.
	 * @return	true if the form has to be flattened
	 */
	public boolean isFlatten() {
		return flatten;
	}
	/**
	 * Setter for the flatten value.
	 * @param flatten
	 */
	public void setFlatten(boolean flatten) {
		this.flatten = flatten;
	}
	
	// helper method
	
	/**
	 * Special getter for the number.
	 * Adds "N° " or leading zeros depending on the parameter
	 * @param barcode
	 * @return	the number as a String
	 */
	public String getNumber(boolean barcode) {
		StringBuffer buf = new StringBuffer();
		if (barcode) {
			int length = 13 - number.length();
			for (int i = 0; i < length; i++) {
				buf.append(0);
			}
		}
		else {
			buf.append("N° ");
		}
		buf.append(number);
		return buf.toString();
	}
}
