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
import java.awt.Color;

/**
 * A Helper Class for the <CODE>RtfWriter</CODE>
 * <P>
 * Do not use it directly
 */
public class RtfCell
{
  /** Constants for merging Cells */

  /** A possible value for merging */
  private static final int MERGE_HORIZ_FIRST = 1;
  /** A possible value for merging */
  private static final int MERGE_VERT_FIRST = 2;
  /** A possible value for merging */
  private static final int MERGE_BOTH_FIRST = 3;
  /** A possible value for merging */
  private static final int MERGE_HORIZ_PREV = 4;
  /** A possible value for merging */
  private static final int MERGE_VERT_PREV = 5;
  /** A possible value for merging */
  private static final int MERGE_BOTH_PREV = 6;

  /**
   * RTF Tags
   */

  /** First cell to merge with - Horizontal */
  private static final byte[] cellMergeFirst = "clmgf".getBytes();
  /** First cell to merge with - Vertical */
  private static final byte[] cellVMergeFirst = "clvmgf".getBytes();
  /** Merge cell with previous horizontal cell */
  private static final byte[] cellMergePrev = "clmrg".getBytes();
  /** Merge cell with previous vertical cell */
  private static final byte[] cellVMergePrev = "clvmrg".getBytes();
  /** Cell content vertical alignment bottom */
  private static final byte[] cellVerticalAlignBottom = "clvertalb".getBytes();
  /** Cell content vertical alignment center */
  private static final byte[] cellVerticalAlignCenter = "clvertalc".getBytes();
  /** Cell content vertical alignment top */
  private static final byte[] cellVerticalAlignTop = "clvertalt".getBytes();
  /** Cell border left */
  private static final byte[] cellBorderLeft = "clbrdrl".getBytes();
  /** Cell border right */
  private static final byte[] cellBorderRight = "clbrdrr".getBytes();
  /** Cell border top */
  private static final byte[] cellBorderTop = "clbrdrt".getBytes();
  /** Cell border bottom */
  private static final byte[] cellBorderBottom = "clbrdrb".getBytes();
  /** Cell background color */
  private static final byte[] cellBackgroundColor = "clcbpat".getBytes();
  /** Cell width format */
  private static final byte[] cellWidthStyle = "clftsWidth3".getBytes();
  /** Cell width */
  private static final byte[] cellWidthTag = "clwWidth".getBytes();
  /** Cell right border position */
  private static final byte[] cellRightBorder = "cellx".getBytes();
  /** Cell is part of table */
  private static final byte[] cellInTable= "intbl".getBytes();
  /** End of cell */
  private static final byte[] cellEnd = "cell".getBytes();

  /** The <code>RtfWriter</code> to which this <code>RtfCell</code> belongs. */
  private RtfWriter writer = null;
  /** The <code>RtfTable</code> to which this <code>RtfCell</code> belongs. */
  private RtfTable mainTable = null;

  /** Cell width */
  private int cellWidth = 0;
  /** Cell right border position */
  private int cellRight = 0;
  /** <code>Cell</code> containing the actual data */
  private Cell store = null;
  /** Is this an empty cell */
  private boolean emptyCell = true;
  /** Type of merging to do */
  private int mergeType = 0;

  /** 
   * Create a new <code>RtfCell</code>
   * @param writer The <code>RtfWriter</code> that this <code>RtfCell</code> belongs to
   * @param table The <code>RtfTable</code> that created the <code>RtfRow</code> that created the <code>RtfCell</code> :-)
   */
  public RtfCell(RtfWriter writer, RtfTable mainTable)
  {
    super();
    this.writer = writer;
    this.mainTable = mainTable;
  }

  /**
   * Import a <code>Cell</code>
   * <BR>
   * @param cell The <code>Cell</code> containing the data for this <code>RtfCell</code>
   * @param cellLeft The position of the left border
   * @param cellWidth The default width of a cell
   * @param x The column index of this <code>RtfCell</code>
   * @param y The row index of this <code>RtfCell</code>
   */
  public int importCell(Cell cell, int cellLeft, int cellWidth, int x, int y)
  {
    if(cell == null) 
      {
	cellRight = cellLeft + cellWidth;
	return cellRight; 
      }
    if(cell.cellWidth() != null && !cell.cellWidth().equals(""))
      {
	this.cellWidth = (int) (Integer.parseInt(cell.cellWidth()) * writer.twipsFactor); 
      }
    else
      {
	this.cellWidth = cellWidth;
      }
    cellRight = cellLeft + this.cellWidth;
    store = cell;
    emptyCell = false;
    if(cell.colspan() > 1)
      {
	if(cell.rowspan() > 1)
	  {
	    mergeType = MERGE_BOTH_FIRST;
	    for(int i = y; i < y + cell.rowspan(); i++)
	      {
		if(i > y) mainTable.setMerge(x, i, MERGE_BOTH_PREV, this);
		for(int j = x + 1; j < x + cell.colspan(); j++)
		  {
		    mainTable.setMerge(j, i, MERGE_HORIZ_PREV, this);
		  }
	      }
	  }
	else
	  {
	    mergeType = MERGE_HORIZ_FIRST;
	    for(int i = x + 1; i < x + cell.colspan(); i++)
	      {
		mainTable.setMerge(i, y, MERGE_HORIZ_PREV, this);
	      }
	  }
      }
    else if(cell.rowspan() > 1)
      {
	mergeType = MERGE_VERT_FIRST;
	for(int i = y + 1; i < y + cell.rowspan(); i++)
	  {
	    mainTable.setMerge(x, i, MERGE_VERT_PREV, this);
	  }
      }
    return cellRight;
  }

  /**
   * Write the properties of the <code>RtfCell</code>
   *
   * @param os The <code>OutputStream</code> to which to write the properties of the <code>RtfCell</code> to.
   */
  public boolean writeCellSettings(OutputStream os) throws DocumentException
  {
    try
      {
	switch(mergeType)
	  {
	  case MERGE_HORIZ_FIRST : os.write(RtfWriter.escape); os.write(cellMergeFirst); break;
	  case MERGE_VERT_FIRST  : os.write(RtfWriter.escape); os.write(cellVMergeFirst); break;
	  case MERGE_BOTH_FIRST  : os.write(RtfWriter.escape); os.write(cellMergeFirst); os.write(RtfWriter.escape); os.write(cellVMergeFirst); break;
	  case MERGE_HORIZ_PREV  : os.write(RtfWriter.escape); os.write(cellMergePrev); break;
	  case MERGE_VERT_PREV   : os.write(RtfWriter.escape); os.write(cellVMergePrev); break;
	  case MERGE_BOTH_PREV   : os.write(RtfWriter.escape); os.write(cellMergeFirst); os.write(RtfWriter.escape); os.write(cellVMergePrev); break;
	  }
	switch(store.verticalAlignment())
	  {
	  case Element.ALIGN_BOTTOM : os.write(RtfWriter.escape); os.write(cellVerticalAlignBottom); break;
	  case Element.ALIGN_CENTER : os.write(RtfWriter.escape); os.write(cellVerticalAlignCenter); break;
	  case Element.ALIGN_TOP    : os.write(RtfWriter.escape); os.write(cellVerticalAlignTop); break;
	  }
	if(((store.border() & Rectangle.LEFT) == Rectangle.LEFT) && (store.borderWidth() > 0))
	  {
	    os.write(RtfWriter.escape);
	    os.write(cellBorderLeft);
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorder);
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorderWidth);
	    writeInt(os, (int) (store.borderWidth() * writer.twipsFactor));
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorderColor);
	    if(store.borderColor() == null) writeInt(os, writer.addColor(new Color(0,0,0))); else writeInt(os, writer.addColor(store.borderColor()));
	    os.write((byte) '\n');
	  }
	if(((store.border() & Rectangle.TOP) == Rectangle.TOP) && (store.borderWidth() > 0))
	  {
	    os.write(RtfWriter.escape);
	    os.write(cellBorderTop);
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorder);
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorderWidth);
	    writeInt(os, (int) (store.borderWidth() * writer.twipsFactor));
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorderColor);
	    if(store.borderColor() == null) writeInt(os, writer.addColor(new Color(0,0,0))); else writeInt(os, writer.addColor(store.borderColor()));
	    os.write((byte) '\n');
	  }
	if(((store.border() & Rectangle.BOTTOM) == Rectangle.BOTTOM) && (store.borderWidth() > 0))
	  {
	    os.write(RtfWriter.escape);
	    os.write(cellBorderBottom);
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorder);
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorderWidth);
	    writeInt(os, (int) (store.borderWidth() * writer.twipsFactor));
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorderColor);
	    if(store.borderColor() == null) writeInt(os, writer.addColor(new Color(0,0,0))); else writeInt(os, writer.addColor(store.borderColor()));
	    os.write((byte) '\n');
	  }
	if(((store.border() & Rectangle.RIGHT) == Rectangle.RIGHT) && (store.borderWidth() > 0))
	  {
	    os.write(RtfWriter.escape);
	    os.write(cellBorderRight);
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorder);
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorderWidth);
	    writeInt(os, (int) (store.borderWidth() * writer.twipsFactor));
	    os.write(RtfWriter.escape);
	    os.write(RtfRow.tableBorderColor);
	    if(store.borderColor() == null) writeInt(os, writer.addColor(new Color(0,0,0))); else writeInt(os, writer.addColor(store.borderColor()));
	    os.write((byte) '\n');
	  }
	os.write(RtfWriter.escape);
	os.write(cellBackgroundColor);
	if(store.backgroundColor() == null) writeInt(os, writer.addColor(new Color(255,255,255))); else writeInt(os, writer.addColor(store.backgroundColor()));
	os.write((byte) '\n');
	os.write(RtfWriter.escape);
	os.write(cellWidthStyle);
	os.write((byte) '\n');
	os.write(RtfWriter.escape);
	os.write(cellWidthTag);
	writeInt(os, cellWidth);
	os.write((byte) '\n');
	os.write(RtfWriter.escape);
	os.write(cellInTable);
	os.write(RtfWriter.escape);
	os.write(cellRightBorder);
	writeInt(os, cellRight);
	os.write((byte) '\n');
      }
    catch(IOException e)
      {
	return false;
      }
    return true;
  }

  /**
   * Write the content of the <code>RtfCell</code>
   *
   * @param os The <code>OutputStream</code> to which to write the content of the <code>RtfCell</code> to.
   */
  public boolean writeCellContent(OutputStream os) throws DocumentException
  {
    try
      {
	if(emptyCell)
	  {
	    os.write(RtfWriter.openGroup);
	    os.write(RtfWriter.closeGroup);
	  }
	else
	  {
	    os.write(RtfWriter.openGroup);
	    Iterator cellIterator = store.getChunks().iterator();
	    while(cellIterator.hasNext())
	      {
		Element element = (Element) cellIterator.next();
		element.process(writer);
	      }
	    os.write(RtfWriter.closeGroup);
	  }
	os.write(RtfWriter.openGroup);
	os.write(RtfWriter.escape);
	os.write(cellEnd);
	os.write(RtfWriter.closeGroup);
      }
    catch(IOException e)
      {
	return false;
      }
    return true;
  }

  /**
   * Sets the merge type and the <code>RtfCell</code> with which this <code>RtfCell</code> is to be merged.
   *
   * @param mergeType The merge type specifies the kind of merge to be applied (MERGE_HORIZ_PREV, MERGE_VERT_PREV, MERGE_BOTH_PREV)
   * @param mergeCell The <code>RtfCell</code> that the cell at x and y is to be merged with
   */
  public void setMerge(int mergeType, RtfCell mergeCell)
  {
    this.mergeType = mergeType;
    store = mergeCell.getStore();
    cellWidth = mergeCell.getCellWidth();
  }

  /**
   * Get the <code>Cell</code> with the actual content
   *
   * @return <code>Cell</code> which is contained in the <code>RtfCell</code>
   */
  public Cell getStore()
  {
    return store;
  }

  /**
   * Get the with of this <code>RtfCell</code>
   *
   * @return Width of the current <code>RtfCell</code>
   */
  public int getCellWidth()
  {
    return cellWidth;
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
 
