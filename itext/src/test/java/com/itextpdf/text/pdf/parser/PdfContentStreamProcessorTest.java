/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ListIterator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

public class PdfContentStreamProcessorTest
{
  private DebugRenderListener _renderListener;

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception{
      _renderListener = new DebugRenderListener();
  }

  // Replicates iText bug 2817030
  @Test
  public void testPositionAfterTstar()
    throws Exception
  {
    processBytes("yaxiststar.pdf", 1);
  }



  private void processBytes(
      final String resourceName,
      final int pageNumber)
    throws IOException
  {
    final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, resourceName);

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
        final ListIterator<PdfObject> iter = contentArray.listIterator();
        while (iter.hasNext())
        {
          final PdfObject element = iter.next();
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


  private class DebugRenderListener implements RenderListener
  {
    private float _lastY = Float.MAX_VALUE;

    public void renderText(TextRenderInfo renderInfo) {
        Vector start = renderInfo.getBaseline().getStartPoint();
        final float x = start.get(Vector.I1);
        final float y = start.get(Vector.I2);
//        System.out.println("Display text: '" + renderInfo.getText() + "' (" + x + "," + y + ")");
        if (y > _lastY){
          Assert.fail("Text has jumped back up the page");
        }
        _lastY = y;

    }

    public void beginTextBlock() {
        _lastY = Float.MAX_VALUE;
    }

    public void endTextBlock() {
    }

    public void renderImage(ImageRenderInfo renderInfo) {
    }

  }

}

