package com.itextpdf.text.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class with commonly used stream operations
 * @since 5.3.5
 *
 */
public final class StreamUtil {

	private StreamUtil() {
	}

	/**
	 * Reads the full content of a stream and returns them in a byte array
	 * @param is the stream to read
	 * @return a byte array containing all of the bytes from the stream
	 * @throws IOException if there is a problem reading from the input stream
	 */
    public static byte[] inputStreamToArray(InputStream is) throws IOException {
        byte b[] = new byte[8192];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            int read = is.read(b);
            if (read < 1)
                break;
            out.write(b, 0, read);
        }
        out.close();
        return out.toByteArray();
    }
}
