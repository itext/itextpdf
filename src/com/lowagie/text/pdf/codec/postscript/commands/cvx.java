package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.util.Stack;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import java.util.ArrayList;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: cvx (convert to executable)</p>
 *
 * <p>Description: any cvx any</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class cvx
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public cvx() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data;
    data = context.operands.pop();
    this.execute(context, data);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    this.execute(context, (ArrayList) operand1);
  }

  public void execute(PAContext context, ArrayList operand1) throws
      PainterException {
    Stack stack = new Stack();
    for (int i = operand1.size() - 1; i >= 0; i--) {
      stack.add(operand1.get(i));
    }
    PAToken patoken = new PAToken(stack, PAToken.PROCEDURE);
//           patoken.type=PAToken.PROCEDURE;
    context.operands.push(patoken);
  }
}
