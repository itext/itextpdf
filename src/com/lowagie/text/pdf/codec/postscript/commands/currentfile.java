package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.io.InputStream;
import java.io.IOException;
import com.lowagie.text.pdf.codec.postscript.JavaCharStream;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: currentfile</p>
 *
 * <p>Description: - currentfile file</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class currentfile
    implements PACommand, IPSLevel1 {
  public currentfile() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    final JavaCharStream jcs = context.getJj_input_stream();
    InputStream ins = new InputStream() {
      /**
       * Reads the next byte of data from the input stream.
       *
       * @return the next byte of data, or <code>-1</code> if the end of the stream is reached.
       * @throws IOException if an I/O error occurs.
       */
      public int read() throws IOException {
        return jcs.readChar();
      }
    };
    context.operands.push(ins);
  }
}
