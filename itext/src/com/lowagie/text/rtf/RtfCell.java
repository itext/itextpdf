package com.lowagie.text.rtf;

import com.lowagie.text.*;
import com.lowagie.text.rtf.*;

import java.util.*;
import java.io.*;
import java.awt.Color;

public class RtfCell
{
  /** Constants for merging Cells **/

  /** A possible value for merging **/
  private static final int MERGE_HORIZ_FIRST = 1;
  /** A possible value for merging **/
  private static final int MERGE_VERT_FIRST = 2;
  /** A possible value for merging **/
  private static final int MERGE_BOTH_FIRST = 3;
  /** A possible value for merging **/
  private static final int MERGE_HORIZ_PREV = 4;
  /** A possible value for merging **/
  private static final int MERGE_VERT_PREV = 5;
  /** A possible value for merging **/
  private static final int MERGE_BOTH_PREV = 6;

  /** RTF Tags **/

  private static final byte[] cellMergeFirst = "clmgf".getBytes();
  private static final byte[] cellVMergeFirst = "clvmgf".getBytes();
  private static final byte[] cellMergePrev = "clmrg".getBytes();
  private static final byte[] cellVMergePrev = "clvmrg".getBytes();
  private static final byte[] cellVerticalAlignBottom = "clvertalb".getBytes();
  private static final byte[] cellVerticalAlignCenter = "clvertalc".getBytes();
  private static final byte[] cellVerticalAlignTop = "clvertalt".getBytes();
  private static final byte[] cellBorderLeft = "clbrdrl".getBytes();
  private static final byte[] cellBorderRight = "clbrdrr".getBytes();
  private static final byte[] cellBorderTop = "clbrdrt".getBytes();
  private static final byte[] cellBorderBottom = "clbrdrb".getBytes();
  private static final byte[] cellBackgroundColor = "clcbpat".getBytes();
  private static final byte[] cellWidthStyle = "clftsWidth3".getBytes();
  private static final byte[] cellWidthTag = "clwWidth".getBytes();
  private static final byte[] cellRightBorder = "cellx".getBytes();
  private static final byte[] cellInTable= "intbl".getBytes();
  private static final byte[] cellEnd = "cell".getBytes();

  private RtfWriter writer = null;
  private RtfTable mainTable = null;

  private int cellWidth = 0;
  private int cellRight = 0;
  private Cell store = null;
  private boolean emptyCell = true;
  private int mergeType = 0;

  public RtfCell(RtfWriter writer, RtfTable mainTable)
  {
    super();
    this.writer = writer;
    this.mainTable = mainTable;
  }

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

  public void setMerge(int mergeType, RtfCell mergeCell)
  {
    this.mergeType = mergeType;
    store = mergeCell.getStore();
    cellWidth = mergeCell.getCellWidth();
  }

  public Cell getStore()
  {
    return store;
  }

  public int getCellWidth()
  {
    return cellWidth;
  }

  private void writeInt(OutputStream out, int i) throws IOException
  {
    out.write(Integer.toString(i).getBytes());
  }
}
 
