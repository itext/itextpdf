package com.lowagie.text.rtf;

import com.lowagie.text.*;
import com.lowagie.text.rtf.*;

import java.util.*;
import java.io.*;
import java.awt.Color;

public class RtfRow
{
  public static final byte[] tableBorder = "brdrs".getBytes();
  public static final byte[] tableBorderWidth = "brdrw".getBytes();
  public static final byte[] tableBorderColor = "brdrcf".getBytes();

  private static final byte[] rowBegin = "trowd".getBytes();
  private static final byte[] rowEnd = "row".getBytes();
  private static final byte[] rowAutofit = "trautofit1".getBytes();
  private static final byte[] graphLeft = "trgraph".getBytes();
  private static final byte[] rowBorderLeft = "trbrdrl".getBytes();
  private static final byte[] rowBorderRight = "trbrdrr".getBytes();
  private static final byte[] rowBorderTop = "trbrdrt".getBytes();
  private static final byte[] rowBorderBottom = "trbrdrb".getBytes();
  private static final byte[] rowSpacingLeft = "trspdl".getBytes();
  private static final byte[] rowSpacingRight = "trspdr".getBytes();
  private static final byte[] rowSpacingTop = "trspdt".getBytes();
  private static final byte[] rowSpacingBottom = "trspdb".getBytes();
  private static final byte[] rowSpacingLeftStyle = "trspdfl3".getBytes();
  private static final byte[] rowSpacingRightStyle = "trspdfr3".getBytes();
  private static final byte[] rowSpacingTopStyle = "trspdft3".getBytes();
  private static final byte[] rowSpacingBottomStyle = "trspdfb3".getBytes();
  private static final byte[] rowPaddingLeft = "trpaddl".getBytes();
  private static final byte[] rowPaddingRight = "trpaddr".getBytes();
  private static final byte[] rowPaddingLeftStyle = "trpaddfl3".getBytes();
  private static final byte[] rowPaddingRightStyle = "trpaddfr3".getBytes();

  private ArrayList cells = new ArrayList();
  private RtfWriter writer = null;
  private RtfTable mainTable = null;

  private int width = 100;
  private int cellpadding = 115;
  private int cellspacing = 14;
  private int borders = 0;
  private java.awt.Color borderColor = null;
  private float borderWidth = 0;

  public RtfRow(RtfWriter writer, RtfTable mainTable)
  {
    super();
    this.writer = writer;
    this.mainTable = mainTable;
  }

  public void pregenerateRows(int columns)
  {
    for(int i = 0; i < columns; i++)
      {
	RtfCell rtfCell = new RtfCell(writer, mainTable);
	cells.add(rtfCell);
      }
  }

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

  public boolean writeRow(OutputStream os) throws DocumentException, IOException
  {
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

  public void setMerge(int x, int mergeType, RtfCell mergeCell)
  {
    RtfCell cell = (RtfCell) cells.get(x);
    cell.setMerge(mergeType, mergeCell);
  }

  private void writeInt(OutputStream out, int i) throws IOException
  {
    out.write(Integer.toString(i).getBytes());
  }
}
