package com.lowagie.text.rtf;

import com.lowagie.text.*;
import com.lowagie.text.rtf.*;

import java.util.*;
import java.io.*;

public class RtfTable
{
  private ArrayList rowsList = new ArrayList();
  private RtfWriter writer = null;

  public RtfTable(RtfWriter writer)
  {
    super();
    this.writer = writer;
  }

  public boolean importTable(Table table, int pageWidth)
  {
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

  public void setMerge(int x, int y, int mergeType, RtfCell mergeCell)
  {
    RtfRow row = (RtfRow) rowsList.get(y);
    row.setMerge(x, mergeType, mergeCell);
  }
}
