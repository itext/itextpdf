/** 
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Mark Hall
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.rtf;

import com.lowagie.text.*;
import com.lowagie.text.rtf.*;

import java.util.*;
import java.io.*;
import java.awt.Color;

/**
 * A Helper Class for the <CODE>RtfWriter</CODE>
 * <P>
 * Do not use it directly
 */
public class RtfRow
{
  /* Table border solid */
  public static final byte[] tableBorder = "brdrs".getBytes();
  /* Table border width */
  public static final byte[] tableBorderWidth = "brdrw".getBytes();
  /* Table border color */
  public static final byte[] tableBorderColor = "brdrcf".getBytes();

  /* Table row defaults */
  private static final byte[] rowBegin = "trowd".getBytes();
  /* End of table row */
  private static final byte[] rowEnd = "row".getBytes();
  /* Table row autofit */
  private static final byte[] rowAutofit = "trautofit1".getBytes();
  private static final byte[] graphLeft = "trgraph".getBytes();
  /* Row border left */
  private static final byte[] rowBorderLeft = "trbrdrl".getBytes();
  /* Row border right */
  private static final byte[] rowBorderRight = "trbrdrr".getBytes();
  /* Row border top */
  private static final byte[] rowBorderTop = "trbrdrt".getBytes();
  /* Row border bottom */
  private static final byte[] rowBorderBottom = "trbrdrb".getBytes();
  /* Default cell spacing left */
  private static final byte[] rowSpacingLeft = "trspdl".getBytes();
  /* Default cell spacing right */
  private static final byte[] rowSpacingRight = "trspdr".getBytes();
  /* Default cell spacing top */
  private static final byte[] rowSpacingTop = "trspdt".getBytes();
  /* Default cell spacing bottom */
  private static final byte[] rowSpacingBottom = "trspdb".getBytes();
  /* Default cell spacing format left */
  private static final byte[] rowSpacingLeftStyle = "trspdfl3".getBytes();
  /* Default cell spacing format right */
  private static final byte[] rowSpacingRightStyle = "trspdfr3".getBytes();
  /* Default cell spacing format top */
  private static final byte[] rowSpacingTopStyle = "trspdft3".getBytes();
  /* Default cell spacing format bottom */
  private static final byte[] rowSpacingBottomStyle = "trspdfb3".getBytes();
  /* Default cell padding left */
  private static final byte[] rowPaddingLeft = "trpaddl".getBytes();
  /* Default cell padding right */
  private static final byte[] rowPaddingRight = "trpaddr".getBytes();
  /* Default cell padding format left */
  private static final byte[] rowPaddingLeftStyle = "trpaddfl3".getBytes();
  /* Default cell padding format right */
  private static final byte[] rowPaddingRightStyle = "trpaddfr3".getBytes();

  /** List of <code>RtfCell</code>s in this <code>RtfRow</code> */
  private ArrayList cells = new ArrayList();
  /** The <code>RtfWriter</code> to which this <code>RtfRow</code> belongs */
  private RtfWriter writer = null;
  /** The <coce>RtfTable</code> to which this <code>RtfRow</code> belongs */
  private RtfTable mainTable = null;

  /** The width of this <code>RtfRow</code> (in percent) */
  private int width = 100;
  /** The default cellpadding of <code>RtfCells</code> in this <code>RtfRow</code> */
  private int cellpadding = 115;
  /** The default cellspacing of <code>RtfCells</code> in this <code>RtfRow</code> */
  private int cellspacing = 14;
  /** The borders of this <code>RtfRow</code> */
  private int borders = 0;
  /** The border color of this <code>RtfRow</code> */
  private java.awt.Color borderColor = null;
  /** The border width of this <code>RtfRow</code> */
  private float borderWidth = 0;

  /** 
   * Create a new <code>RtfRow</code>
   * @param writer The <code>RtfWriter</code> that this <code>RtfRow</code> belongs to
   * @param table The <code>RtfTable</code> that created this <code>RtfRow</code>
   */
  public RtfRow(RtfWriter writer, RtfTable mainTable)
  {
    super();
    this.writer = writer;
    this.mainTable = mainTable;
  }

  /**
   * Pregenerate the <code>RtfCell</code>s in this <code>RtfRow</code>
   *
   * @param columns The number of <code>RtfCell</code>s to be generated.
  */
  public void pregenerateRows(int columns)
  {
    for(int i = 0; i < columns; i++)
      {
	RtfCell rtfCell = new RtfCell(writer, mainTable);
	cells.add(rtfCell);
      }
  }

  /**
   * Import a <code>Row</code>
   * <BR>
   * All the parameters are taken from the <code>RtfTable</code> which contains this <code>RtfRow</code> and they do exactely what they say
   */
  public boolean importRow(Row row, int width, int pageWidth, int cellpadding, int cellspacing, int borders, java.awt.Color borderColor, float borderWidth, int y)
  {
    this.width = width;
    this.cellpadding = cellpadding;
    this.cellspacing = cellspacing;
    this.borders = borders;
    this.borderColor = borderColor;
    this.borderWidth = borderWidth;

    if(this.borderWidth > 2) this.borderWidth = 2;

    int cellLeft = 0;
    int cellWidth = (int) (((((float) pageWidth) / 100) * width) / row.columns());
    for(int i = 0; i < row.columns(); i++)
      {
	Element cell = (Element) row.getCell(i);
	if(cell != null)
	  {
	    if(cell.type() == Element.CELL)
	      {
		RtfCell rtfCell = (RtfCell) cells.get(i);
		cellLeft = rtfCell.importCell((Cell) cell, cellLeft, cellWidth, i, y);
	      }
	  }
	else
	  {
		RtfCell rtfCell = (RtfCell) cells.get(i);
		cellLeft = rtfCell.importCell(null, cellLeft, cellWidth, i, y);
	  }
      }
    return true;
  }

  /**
   * Write the <code>RtfRow</code> to the specified <code>OutputStream</code>
   *
   * @param os The <code>OutputStream</code> to which this <code>RtfRow</code> should be written to.
   */
  public boolean writeRow(OutputStream os) throws DocumentException, IOException
  {
    os.write(RtfWriter.escape);
    os.write(RtfWriter.paragraphDefaults);
    os.write(RtfWriter.escape);
    os.write(rowBegin);
    os.write((byte) '\n');
    os.write(RtfWriter.escape);
    os.write(rowAutofit);
    os.write(RtfWriter.escape);
    os.write(graphLeft);
    writeInt(os, 10);
    if(((borders & Rectangle.LEFT) == Rectangle.LEFT) && (borderWidth > 0))
      {
	os.write(RtfWriter.escape);
	os.write(rowBorderLeft);
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorder);
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorderWidth);
	writeInt(os, (int) (borderWidth * writer.twipsFactor));
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorderColor);
	if(borderColor == null) writeInt(os, writer.addColor(new Color(0,0,0))); else writeInt(os, writer.addColor(borderColor));
	os.write((byte) '\n');
      }
    if(((borders & Rectangle.TOP) == Rectangle.TOP) && (borderWidth > 0))
      {
	os.write(RtfWriter.escape);
	os.write(rowBorderTop);
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorder);
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorderWidth);
	writeInt(os, (int) (borderWidth * writer.twipsFactor));
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorderColor);
	if(borderColor == null) writeInt(os, writer.addColor(new Color(0,0,0))); else writeInt(os, writer.addColor(borderColor));
	os.write((byte) '\n');
      }
    if(((borders & Rectangle.BOTTOM) == Rectangle.BOTTOM) && (borderWidth > 0))
      {
	os.write(RtfWriter.escape);
	os.write(rowBorderBottom);
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorder);
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorderWidth);
	writeInt(os, (int) (borderWidth * writer.twipsFactor));
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorderColor);
	if(borderColor == null) writeInt(os, writer.addColor(new Color(0,0,0))); else writeInt(os, writer.addColor(borderColor));
	os.write((byte) '\n');
      }
    if(((borders & Rectangle.RIGHT) == Rectangle.RIGHT) && (borderWidth > 0))
      {
	os.write(RtfWriter.escape);
	os.write(rowBorderRight);
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorder);
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorderWidth);
	writeInt(os, (int) (borderWidth * writer.twipsFactor));
	os.write(RtfWriter.escape);
	os.write(RtfRow.tableBorderColor);
	if(borderColor == null) writeInt(os, writer.addColor(new Color(0,0,0))); else writeInt(os, writer.addColor(borderColor));
	os.write((byte) '\n');
      }
    os.write(RtfWriter.escape);
    os.write(rowSpacingLeft);
    writeInt(os, cellspacing / 2);
    os.write(RtfWriter.escape);
    os.write(rowSpacingLeftStyle);
    os.write(RtfWriter.escape);
    os.write(rowSpacingTop);
    writeInt(os, cellspacing / 2);
    os.write(RtfWriter.escape);
    os.write(rowSpacingTopStyle);
    os.write(RtfWriter.escape);
    os.write(rowSpacingBottom);
    writeInt(os, cellspacing / 2);
    os.write(RtfWriter.escape);
    os.write(rowSpacingBottomStyle);
    os.write(RtfWriter.escape);
    os.write(rowSpacingRight);
    writeInt(os, cellspacing / 2);
    os.write(RtfWriter.escape);
    os.write(rowSpacingRightStyle);
    os.write(RtfWriter.escape);
    os.write(rowPaddingLeft);
    writeInt(os, cellpadding / 2);
    os.write(RtfWriter.escape);
    os.write(rowPaddingRight);
    writeInt(os, cellpadding / 2);
    os.write(RtfWriter.escape);
    os.write(rowPaddingLeftStyle);
    os.write(RtfWriter.escape);
    os.write(rowPaddingRightStyle);
    os.write((byte) '\n');

    Iterator cellIterator = cells.iterator();
    while(cellIterator.hasNext())
      {
	RtfCell cell = (RtfCell) cellIterator.next();
	cell.writeCellSettings(os);
      }
    cellIterator = cells.iterator();
    while(cellIterator.hasNext())
      {
	RtfCell cell = (RtfCell) cellIterator.next();
	cell.writeCellContent(os);
      }
    os.write(RtfWriter.escape);
    os.write(rowEnd);
    return true;
  }

  /**
   * <code>RtfTable</code>s call this method from their own setMerge() to specify that a certain other cell is to be merged with it
   *
   * @param x The column position of the cell to be merged
   * @param mergeType The merge type specifies the kind of merge to be applied (MERGE_HORIZ_PREV, MERGE_VERT_PREV, MERGE_BOTH_PREV)
   * @param mergeCell The <code>RtfCell</code> that the cell at x and y is to be merged with
   */
  public void setMerge(int x, int mergeType, RtfCell mergeCell)
  {
    RtfCell cell = (RtfCell) cells.get(x);
    cell.setMerge(mergeType, mergeCell);
  }

  /*
   * Write an Integer to the Outputstream
   *
   * @param out The <code>OutputStream</code> to be written to.
   * @param i The int to be written.
   */
  private void writeInt(OutputStream out, int i) throws IOException
  {
    out.write(Integer.toString(i).getBytes());
  }
}
