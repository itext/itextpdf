package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class sub extends add implements PACommand, IBinaryExecute,IPSLevel1 {
  public sub() {
    super();
  }

 public void execute(PAContext context, Number operand1, Number operand2) throws
     PainterException {
   context.operands.push(new Double(operand1.doubleValue() -
                                    operand2.doubleValue()));
   if(PAContext.DebugCommandleveltrace)System.out.println("sub("+operand1.doubleValue()+"-"+operand2.doubleValue()+")");
 }
}
