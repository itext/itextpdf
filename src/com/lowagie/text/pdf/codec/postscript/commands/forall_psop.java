package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.ArrayList;
import java.util.Iterator;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: forall</p>
 *
 * <p>Description: array proc forall
 * packedarray proc forall
 * dict proc forall
 * string proc forall</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class forall_psop
    implements PACommand, IBinaryExecute, IPSLevel1 {
  public forall_psop() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    if ( (operand1 instanceof ArrayList) && (operand2 instanceof PAToken)) {
      this.execute(context, (ArrayList) operand1, (PAToken) operand2);
    }
    else
    if ( (operand1 instanceof StringBuffer) && (operand2 instanceof PAToken)) {
      this.execute(context, (StringBuffer) operand1, (PAToken) operand2);
    }
    else {
      throw new PainterException("forall: wrong arguments");
    }
  }

  public void execute(PAContext context, ArrayList list, PAToken patoken) throws
      PainterException {
    if (! (patoken.type == PAToken.PROCEDURE)) {
      throw new PainterException("forall: wrong arguments");
    }
    Iterator iterator = list.iterator();
    while (iterator.hasNext()) {
      context.operands.push(iterator.next());
      context.engine.process(patoken);
    }
  }

  public void execute(PAContext context, StringBuffer list, PAToken patoken) throws
      PainterException {
    if (! (patoken.type == PAToken.PROCEDURE)) {
      throw new PainterException("forall: wrong arguments");
    }
    char[] ch = list.toString().toCharArray();
    for (int i = 0; i < ch.length; i++) {
      context.operands.push(new Integer(ch[i]));
      context.engine.process(patoken);
    }
  }

}
