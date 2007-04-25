package com.lowagie.text.pdf.codec.postscript.commands;

import java.io.IOException;
import java.io.InputStream;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: filter</p>
 *
 * <p>Description: datasrc dict param1 ... paramn filtername filter file
 * datatgt dict param1 ... paramn filtername filter file</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class filter
    implements PACommand, IPSLevel1 {
  public filter() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    String filtername;
    filtername = (String) ( (PAToken) context.popOperands(1)[0]).value;
    while (! ( (context.peekOperand()) instanceof InputStream)) {
      Object param = context.popOperands(1);
    }

    InputStream datasrc;
    datasrc = (InputStream) (context.popOperands(1)[0]);

    InputStream dis;
    if (filtername.equals("ASCIIHexDecode")) {
      //          dis = new ASCIIHexInputStream(datasrc);
      final InputStream is = datasrc;
      dis = new InputStream() {

        /**
         * Reads the next byte of data from the input stream.
         *
         * @return the next byte of data, or <code>-1</code> if the end of the stream is reached.
         * @throws IOException if an I/O error occurs.
         */
        public int read() throws IOException {
          //todo: implement this java.io.InputStream method
          int firstchar, secondchar;
          for (; ; ) {
            firstchar = is.read();
            if (firstchar == -1) {
              return -1;
            }
            if (firstchar == '>') {
              return -1;
            }
            if (firstchar == '\n') {
              continue;
            }
            if (firstchar == '\r') {
              continue;
            }
            break;
          }
          for (; ; ) {
            secondchar = is.read();
            if (secondchar == '>') {
              return -1;
            }
            if (secondchar == -1) {
              return -1;
            }
            if (secondchar == '\n') {
              continue;
            }
            if (secondchar == '\r') {
              continue;
            }
            break;
          }
          int highbyte = 0;
          if (firstchar >= 48 && firstchar <= 57) {
            highbyte = firstchar - 48;
          }
          if (firstchar >= 65 && firstchar <= 70) {
            highbyte = firstchar - 55;
          }
          int lowbyte = 0;
          if (secondchar >= 48 && secondchar <= 57) {
            lowbyte = secondchar - 48;
          }
          if (secondchar >= 65 && secondchar <= 70) {
            lowbyte = secondchar - 55;
          }

          return (highbyte * 16 + lowbyte);
        }
      };
    }
//        else
//        if (filtername.equals("DCTDecode")) {
//          dis = new DCTInputStream(datasrc);
//        }
    else {
      dis = datasrc;
    }
    context.operands.push(dis);
  }
}
