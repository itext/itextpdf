/**
 *
 */
package com.itextpdf.tool.xml.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Balder Van Camp
 *
 */
public interface FileRetrieve {

	/**
	 * Process content from a given URL. using {@link URL#openStream()}
	 * @param url the URL to process
	 * @param processor the ReadingProcessor
	 * @throws IOException if something went wrong.
	 */
	void processFromHref(final String href, final ReadingProcessor processor) throws IOException;

	/**
	 * Process content from a given stream.
	 * @param in the stream to process
	 * @param processor the ReadingProcessor
	 * @throws IOException if something went wrong.
	 */
	void processFromStream(final InputStream in, final ReadingProcessor processor) throws IOException;



}
