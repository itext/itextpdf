/** 
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Mark Hall
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either versioni 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 *
 */

package com.lowagie.text.rtf;

import com.lowagie.text.*;
import com.lowagie.text.rtf.*;

import java.util.*;
import java.io.*;

/**
 * A Helper Class for the <CODE>RtfWriter</CODE>
 * <P>
 * Do not use it directly, except if you want to write a <CODE>DocumentListener</CODE> for Rtf
 */
public class RtfTable
{
  /** Stores the different rows */
  private ArrayList rowsList = new ArrayList();
  /** Stores the RtfWriter, which created this RtfTable */
  private RtfWriter writer = null;

  /** 
   * Create a new <code>RtfTable</code>
   * @param writer The <code>RtfWriter</code> that created this Table
   */
  public RtfTable(RtfWriter writer)
  {
    super();
    this.writer = writer;
  }

  /** 
   * Import a <CODE>Table</CODE> into the <CODE>RtfTable</CODE>
   * <P>
   * @param table A <code>Table</code> specifying the <code>Table</code> to be imported
   * @param pageWidth An <code>int</code> specifying the page width
   */
  public boolean importTable(Table table, int pageWidth)
  {
    // All Cells are pregenerated first, so that cell and rowspanning work
    Iterator rows = table.iterator();
    Row row = null;

    int tableWidth = (int) table.widthPercentage();
    int cellpadding = (int) (table.cellpadding() * writer.twipsFactor);
    int cellspacing = (int) (table.cellspacing() * writer.twipsFactor);
    float[] propWidths = table.getProportionalWidths();
    int borders = table.border();
    java.awt.Color borderColor = table.borderColor();
    float borderWidth = table.borderWidth();

    for(int i = 0; i < table.size(); i++)
      {
	RtfRow rtfRow = new RtfRow(writer, this);
	rtfRow.pregenerateRows(table.columns());
	rowsList.add(rtfRow);
      }
    int i = 0;
    while(rows.hasNext())
      {
	row = (Row) rows.next();
	RtfRow rtfRow = (RtfRow) rowsList.get(i);
	rtfRow.importRow(row, tableWidth, pageWidth, cellpadding, cellspacing, borders, borderColor, borderWidth, i);
	i++;
      }
    return true;
  }

  /**
   * Output the content of the <CODE>RtfTable</CODE> to an OutputStream
   *
   * @param os The <code>OutputStream</code> that the content of the <code>RtfTable</code> is to be written to
   */
  public boolean writeTable(OutputStream os) throws DocumentException, IOException
  {
    Iterator rows = rowsList.iterator();
    RtfRow row = null;
    while(rows.hasNext())
      {
	row = (RtfRow) rows.next();
	row.writeRow(os);
      }
    return true;
  }

  /**
   * <code>RtfCell</code>s call this method to specify that a certain other cell is to be merged with it
   *
   * @param x The column position of the cell to be merged
   * @param y The row position of the cell to be merged
   * @param mergeType The merge type specifies the kind of merge to be applied (MERGE_HORIZ_PREV, MERGE_VERT_PREV, MERGE_BOTH_PREV)
   * @param mergeCell The <code>RtfCell</code> that the cell at x and y is to be merged with
   */
  public void setMerge(int x, int y, int mergeType, RtfCell mergeCell)
  {
    RtfRow row = (RtfRow) rowsList.get(y);
    row.setMerge(x, mergeType, mergeCell);
  }
}
