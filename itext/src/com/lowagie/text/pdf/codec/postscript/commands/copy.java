package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: copy</p>
 *
 * <p>Description: any1 ... anyn n copy any1 ... anyn any1 ... anyn
 * array1 array2 copy subarray2
 * dict1 dict2 copy dict2
 * string1 string2 copy subarray2
 * gstate1 gstate2 copy gstate2</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class copy
    implements PACommand, IPSLevel1 {
  public copy() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);

    // decide if it's a simple copy or a composite object copy
    if ( (data[0] instanceof PAToken) && (data[1] instanceof PAToken)) {
      // composite object copy
      if ( ( (PAToken) data[0]).type == ( (PAToken) data[1]).type) {
        // our tokens are immutable so a copy is easy
        context.operands.push(data[0]);
        context.operands.push(data[0]);
      }
      else {
        throw new PainterException(
            "copy operation failed because composite objects on stack are not of same type");
      }
    }
    else {
      // restore first arg, we're not interested in it in this simple case
      context.operands.push(data[0]);

      if (data[1] instanceof Number) {
        int index = ( (Number) data[1]).intValue();
        int i, n;
        n = context.operands.size();
        Object[] copyData = new Object[index];
        for (i = n - index; i < n; i++) {
          try {
            copyData[i - n + index] = context.operands.elementAt(i);
          }
          catch (ArrayIndexOutOfBoundsException e) {
            throw new PainterException(e.toString());
          }
        }
        for (i = 0; i < index; i++) {
          context.operands.push(copyData[i]);
        }
      }
      else {
        throw new PainterException("I expect a number on stack, dude");
      }
    }
  }
}
