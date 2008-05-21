/* in_action/chapter13/PhotoThumbs.java */

package in_action.chapter13;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class PhotoThumbs {

	/**
	 * Generates a file that can be used as a photo album.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 13: example PhotoThumbs");
		System.out.println("-> Generates a file that can be used as a photo album.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("->              iText-toolbox.jar");
		System.out.println("-> resources needed: some photos in the /resources directory");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   photo_album.pdf");
		String[] arg = { "resources/in_action/chapter13/", "results/in_action/chapter13/photo_album.pdf" };
		com.lowagie.toolbox.plugins.PhotoAlbum.main(arg);
	}
}