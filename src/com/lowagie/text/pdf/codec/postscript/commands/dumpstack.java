package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.Enumeration;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class dumpstack
    implements PACommand,IPSLevel1 {
  public dumpstack() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    if (PAContext.DebugExecution) {
      Enumeration enumx = context.operands.elements();
      System.out.println("-------------Stack--------------");
      while (enumx.hasMoreElements()) {
        System.out.println(enumx.nextElement());
      }
      System.out.println("--------------------------------");
    }
  }
}
