package com.lowagie.text.pdf.codec.postscript.commands;

import java.awt.geom.Rectangle2D;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class stringwidth implements PACommand,IPSLevel1{
  public stringwidth() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
       Object data[];
       java.awt.Font font;

       data = context.popOperands(1);
       if ((data[0] instanceof StringBuffer)) {
         data[0]=((StringBuffer)data[0]).toString();
       }
       if (! (data[0] instanceof String)) {
         throw new PainterException("stringwidth: wrong arguments");
       }

       font = context.pencil.graphics.getFont();
       Rectangle2D rect = font.getStringBounds( (String) data[0],
                                               context.pencil.graphics.
                                               getFontRenderContext());
       context.operands.push(new Float(rect.getWidth()));
       context.operands.push(new Float(rect.getHeight()));
       if(PAContext.DebugCommandleveltrace)System.out.println("stringwidth(\""+data[0]+"\")="+rect.getWidth()+","+rect.getHeight());
     }

}
