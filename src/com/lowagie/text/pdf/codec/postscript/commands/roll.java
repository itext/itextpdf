package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class roll implements PACommand,IPSLevel1{
  public roll() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
   Object data[];
   Object rollData[];

   data = context.popOperands(2);
   if (! (data[0] instanceof Number)) {
     throw new PainterException("roll: wrong arguments");
   }
   if (! (data[1] instanceof Number)) {
     throw new PainterException("roll: wrong arguments");
   }
   int numberOfElements, numberOfPositions, i;

   numberOfElements = ( (Number) data[0]).intValue();
   numberOfPositions = ( (Number) data[1]).intValue();

   if (numberOfPositions == 0 || numberOfElements <= 0) {
     return;
   }

   rollData = context.popOperands(numberOfElements);

   if (numberOfPositions < 0) {
     numberOfPositions = -numberOfPositions;
     numberOfPositions = numberOfPositions % numberOfElements;

     // downward roll
     for (i = numberOfPositions; i < numberOfElements; i++) {
       context.operands.push(rollData[i]);
     }
     for (i = 0; i < numberOfPositions; i++) {
       context.operands.push(rollData[i]);
     }
   }
   else {
     numberOfPositions = numberOfPositions % numberOfElements;

     // upward roll
     for (i = numberOfElements - numberOfPositions; i < numberOfElements;
          i++) {
       context.operands.push(rollData[i]);
     }
     for (i = 0; i < numberOfElements - numberOfPositions; i++) {
       context.operands.push(rollData[i]);
     }
   }
 }

}
