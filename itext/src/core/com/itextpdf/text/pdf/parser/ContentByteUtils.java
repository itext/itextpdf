/*
 * Created on Dec 21, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ListIterator;

import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author kevin
 * @since 5.0.1
 */
public class ContentByteUtils {
    private ContentByteUtils() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Gets the content bytes from a content object, which may be a reference
     * a stream or an array.
     * @param contentObject the object to read bytes from
     * @return the content bytes
     * @throws IOException
     */
    public static byte[] getContentBytesFromContentObject(final PdfObject contentObject) throws IOException {
          final byte[] result;
          switch (contentObject.type())
          {
            case PdfObject.INDIRECT:
              final PRIndirectReference ref = (PRIndirectReference) contentObject;
              final PdfObject directObject = PdfReader.getPdfObject(ref);
              result = getContentBytesFromContentObject(directObject);
              break;
            case PdfObject.STREAM:
              final PRStream stream = (PRStream) PdfReader.getPdfObject(contentObject);
              result = PdfReader.getStreamBytes(stream);
              break;
            case PdfObject.ARRAY:
              // Stitch together all content before calling processContent(), because
              // processContent() resets state.
              final ByteArrayOutputStream allBytes = new ByteArrayOutputStream();
              final PdfArray contentArray = (PdfArray) contentObject;
              final ListIterator<PdfObject> iter = contentArray.listIterator();
              while (iter.hasNext())
              {
                final PdfObject element = iter.next();
                allBytes.write(getContentBytesFromContentObject(element));
              }
              result = allBytes.toByteArray();
              break;
            default:
              final String msg = "Unable to handle Content of type " + contentObject.getClass();
              throw new IllegalStateException(msg);
          }
          return result;
        }
}
