/**
 *
 */
package com.itextpdf.tool.xml.pipeline.pipe;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author redlab_b
 *
 */
public interface FileMaker {

	/**
	 * @return the outputstream to write the pdf to
	 * @throws IOException
	 */
	OutputStream getStream() throws IOException;

}
