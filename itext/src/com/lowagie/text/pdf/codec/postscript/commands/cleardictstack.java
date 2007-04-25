package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.util.HashMap;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: cleardictstack</p>
 *
 * <p>Description: - cleardictstack -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class cleardictstack
    implements PACommand, IPSLevel1 {
  public cleardictstack() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    context.dictionaries.clear();
    HashMap newSystemDict = context.constructSystemDict();
    context.dictionaries.push(newSystemDict);
    HashMap globalDict = context.constructGlobalDict();
    context.dictionaries.push(globalDict);
    HashMap userDict = context.constructUserDict();
    newSystemDict.put("userdict", userDict);
    newSystemDict.put("globaldict", globalDict);
    context.dictionaries.push(userDict);
  }
}
