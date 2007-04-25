package com.lowagie.text.pdf.codec.postscript.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Jpeg;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class image
    implements PACommand, IPSLevel1 {
  public image() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
    PdfContentByte cb = pdfg2d.getContent();
    Object data[];
    data = context.popOperands(1);
    if (data[0] instanceof Number) {
      /**
       * Level1 image
       */
      int width = ( (Number) data[0]).intValue();
      data = context.popOperands(4);
      int height = ( (Number) data[0]).intValue();
      int bits = ( (Number) data[1]).intValue();
      if(PAContext.DebugCommandleveltrace)System.out.println("image number");
    }
    else if (data[0] instanceof PAToken) {
      PAToken proc = (PAToken) data[0];

      data = context.popOperands(4);
      int width = ( (Number) data[0]).intValue();
      int height = ( (Number) data[1]).intValue();
      int bitspercomponent = ( (Number) data[2]).intValue();
      ArrayList ar = (ArrayList) data[3];
      if (PAContext.DebugExecution) {
        System.out.println("I " + width + "*" + height + " " +
                           bitspercomponent + " " + ar);
      }

//                     context.engine.process(proc);
      if(PAContext.DebugCommandleveltrace)System.out.println("image patoken");
    }
    else if (data[0] instanceof HashMap) {
      HashMap hsm = (HashMap) data[0];
      Iterator it = hsm.entrySet().iterator();
      int width = 0, height = 0, bitspercomponent = 0;
      int imagetype = 0;
      InputStream datasrc = null;
      Object decode = null;
      Object imagematrix = null;
      while (it.hasNext()) {
        Map.Entry entry = (Map.Entry) it.next();
        PAToken token = (PAToken) entry.getKey();
        String tokenString = token.value.toString();
        Object value = entry.getValue();
        if (tokenString.equals("ImageType")) {
          imagetype = ( (Number) value).intValue();
        }
        else if (tokenString.equals("DataSource")) {
          datasrc = (InputStream) value;
        }
        else if (tokenString.equals("BitsPerComponent")) {
          bitspercomponent = ( (Number) value).intValue();
        }
        else if (tokenString.equals("Width")) {
          width = ( (Number) value).intValue();
        }
        else if (tokenString.equals("Height")) {
          height = ( (Number) value).intValue();
        }
        else if (tokenString.equals("Decode")) {
          decode = value;
        }
        else if (tokenString.equals("ImageMatrix")) {
          imagematrix = value;
        }
      }

      try {
        byte[] barr = {};
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int aByte;
        while ( (aByte = datasrc.read()) >= 0) {
          bout.write(aByte);
//              System.out.print((char)aByte);
        }
        System.out.println("I " + width + "*" + height + " " +
                           bitspercomponent + " " + imagetype + " " +
                           decode + " " + imagematrix + " " + datasrc);
        barr = bout.toByteArray();
//            com.lowagie.text.Image img = new ImgRaw(width, height, 1,
//                bitspercomponent, barr);
        com.lowagie.text.Image img = new Jpeg(barr);
        try {
          cb.addImage(img, width, 0, 0, height, 0, 0);
        }
        catch (DocumentException ex1) {
          ex1.printStackTrace();
        }
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
      catch (BadElementException ex) {
        ex.printStackTrace();
      }
      if(PAContext.DebugCommandleveltrace)System.out.println("image hashmap");
    }
  }
}
