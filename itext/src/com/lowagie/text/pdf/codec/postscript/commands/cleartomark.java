package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.util.EmptyStackException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: cleartomark</p>
 *
 * <p>Description: mark obj1 .. objn cleartomark</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class cleartomark
    implements PACommand,IPSLevel1 {
  public cleartomark() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data;
    boolean finished = false;

    while (!finished) {
      try {
        data = context.operands.pop();
        if (data instanceof PAToken) {
          if ( ( (PAToken) data).type == PAToken.MARK) {
            finished = true;
          }
        }
      }
      catch (EmptyStackException e) {
        throw new PainterException(e.toString());
      }
    }
  }
}
