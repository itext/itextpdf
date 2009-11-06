/*
 * Created on Jul 9, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.lowagie.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;

public class PdfContentStreamProcessorTest
{
  private DebugRenderListener _renderListener;

  private File testFileRoot = new File(".");;
  
  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception{
      // this is necessary b/c the ant build doesn't keep the PDF files in the same folder as it resides in the source.  This makes it tough to do unit testing from an IDE during development
      String testFileRootPath = System.getenv("com.lowagie.testFileRoot");
      if (testFileRootPath != null)
          testFileRoot = new File(testFileRootPath);
      
      _renderListener = new DebugRenderListener();
  }
  
  // Replicates iText bug 2817030
  @Test
  public void testPositionAfterTstar()
    throws Exception
  {
    final byte[] pdfBytes = readDocument(new File(testFileRoot, "yaxiststar.pdf"));
    processBytes(pdfBytes, 1);
  }


  private byte[] readDocument(final File file) throws IOException {

    final InputStream inputStream = new FileInputStream(file);

    final ByteArrayOutputStream fileBytes = new ByteArrayOutputStream();
    final byte[] buffer = new byte[8192];
    while (true)
    {
      final int bytesRead = inputStream.read(buffer);
      if (bytesRead == -1)
      {
        break;
      }
      fileBytes.write(buffer, 0, bytesRead);
    }

    return fileBytes.toByteArray();
  }


  private void processBytes(
      final byte[] pdfBytes,
      final int pageNumber)
    throws IOException
  {
    final PdfReader pdfReader = new PdfReader(pdfBytes);

    final PdfDictionary pageDictionary = pdfReader.getPageN(pageNumber);

    final PdfDictionary resourceDictionary = pageDictionary.getAsDict(PdfName.RESOURCES);

    final PdfObject contentObject = pageDictionary.get(PdfName.CONTENTS);
    final byte[] contentBytes = readContentBytes(contentObject);
    PdfContentStreamProcessor processor = new PdfContentStreamProcessor(_renderListener);
    processor.processContent(contentBytes, resourceDictionary);
    
  }


  private byte[] readContentBytes(
      final PdfObject contentObject)
    throws IOException
  {
    final byte[] result;
    switch (contentObject.type())
    {
      case PdfObject.INDIRECT:
        final PRIndirectReference ref = (PRIndirectReference) contentObject;
        final PdfObject directObject = PdfReader.getPdfObject(ref);
        result = readContentBytes(directObject);
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
        final ListIterator iter = contentArray.listIterator();
        while (iter.hasNext())
        {
          final PdfObject element = (PdfObject) iter.next();
          allBytes.write(readContentBytes(element));
        }
        result = allBytes.toByteArray();
        break;
      default:
        final String msg = "Unable to handle Content of type " + contentObject.getClass();
        throw new IllegalStateException(msg);
    }
    return result;
  }


  private class DebugRenderListener
    implements RenderListener
  {
    private float _lastY = Float.MAX_VALUE;

    @Override
    public void reset() {
        _lastY = Float.MAX_VALUE;
    }
    
    @Override
    public void renderText(TextRenderInfo renderInfo) {
        Vector start = renderInfo.getStartPoint();
        final float x = start.get(Vector.I1);
        final float y = start.get(Vector.I2);
        System.out.println("Display text: '" + renderInfo.getText() + "' (" + x + "," + y + ")");
        if (y > _lastY){
          Assert.fail("Text has jumped back up the page");
        }
        _lastY = y;
        
    }

  }

}

