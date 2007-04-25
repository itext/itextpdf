package com.lowagie.text.pdf.codec.postscript.interfaces;

import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 *  2 binary 3 ternary 4 quaternary 5 quinary 6 senary 7 septenary 8
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public interface ISenaryExecute {
  public void execute(PAContext context, Object operand1, Object operand2,
                      Object operand3, Object operand4, Object operand5,
                      Object operand6) throws PainterException;
}
