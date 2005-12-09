/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Paulo Soares
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
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
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

package com.lowagie.text.pdf;

import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import java.awt.Color;

/**
 * A row in a PdfPTable.
 * 
 * @author Paulo Soares (psoares@consiste.pt)
 */

public class PdfPRow {

	/** the bottom limit (bottom right y) */
	public static final float BOTTOM_LIMIT = -(1 << 30);

	protected PdfPCell cells[];

	protected float widths[];

	protected float maxHeight = 0;

	protected boolean calculated = false;

	/**
	 * Constructs a new PdfPRow with the cells in the array that was passed as a parameter.
	 * @param cells
	 */
	public PdfPRow(PdfPCell cells[]) {
		this.cells = cells;
		widths = new float[cells.length];
	}

	/**
	 * Makes a copy of an existing row.
	 * @param row
	 */
	public PdfPRow(PdfPRow row) {
		maxHeight = row.maxHeight;
		calculated = row.calculated;
		cells = new PdfPCell[row.cells.length];
		for (int k = 0; k < cells.length; ++k) {
			if (row.cells[k] != null)
				cells[k] = new PdfPCell(row.cells[k]);
		}
		widths = new float[cells.length];
		System.arraycopy(row.widths, 0, widths, 0, cells.length);
	}

	/**
	 * Sets the widths of the columns in the row.
	 * @param widths
	 * @return true if everything went right
	 */
	public boolean setWidths(float widths[]) {
		if (widths.length != cells.length)
			return false;
		System.arraycopy(widths, 0, this.widths, 0, cells.length);
		float total = 0;
		calculated = false;
		for (int k = 0; k < widths.length; ++k) {
			PdfPCell cell = cells[k];
			cell.setLeft(total);
			int last = k + cell.getColspan();
			for (; k < last; ++k)
				total += widths[k];
			--k;
			cell.setRight(total);
			cell.setTop(0);
		}
		return true;
	}

	/**
	 * Calculates the heights of each cell in the row.
	 * @return the maximum height of the row.
	 */
	public float calculateHeights() {
		maxHeight = 0;
		for (int k = 0; k < cells.length; ++k) {
			PdfPCell cell = cells[k];
			if (cell == null)
				continue;
			Image img = cell.getImage();
			if (img != null) {
				img.scalePercent(100);
				float scale = (cell.right() - cell.getEffectivePaddingRight()
						- cell.getEffectivePaddingLeft() - cell.left())
						/ img.scaledWidth();
				img.scalePercent(scale * 100);
				cell
						.setBottom(cell.top() - cell.getEffectivePaddingTop()
								- cell.getEffectivePaddingBottom()
								- img.scaledHeight());
			} else {
				float rightLimit = cell.isNoWrap() ? 20000 : cell.right()
						- cell.getEffectivePaddingRight();
				float bry = (cell.getFixedHeight() > 0) ? cell.top()
						- cell.getEffectivePaddingTop()
						+ cell.getEffectivePaddingBottom()
						- cell.getFixedHeight() : BOTTOM_LIMIT;
				ColumnText ct = ColumnText.duplicate(cell.getColumn());
				ct.setSimpleColumn(
						cell.left() + cell.getEffectivePaddingLeft(), bry,
						rightLimit, cell.top() - cell.getEffectivePaddingTop());
				try {
					ct.go(true);
				} catch (DocumentException e) {
					throw new ExceptionConverter(e);
				}
				float yLine = ct.getYLine();
				if (cell.isUseDescender())
					yLine += ct.getDescender();
				cell.setBottom(yLine - cell.getEffectivePaddingBottom());
			}
			float height = cell.getFixedHeight();
			if (height <= 0)
				height = cell.height();
			if (height < cell.getFixedHeight())
				height = cell.getFixedHeight();
			else if (height < cell.getMinimumHeight())
				height = cell.getMinimumHeight();
			if (height > maxHeight)
				maxHeight = height;
		}
		calculated = true;
		return maxHeight;
	}

	/**
	 * Writes the border and background of one cell in the row.
	 * @param xPos
	 * @param yPos
	 * @param cell
	 * @param canvases
	 */
	public void writeBorderAndBackground(float xPos, float yPos, PdfPCell cell,
			PdfContentByte[] canvases) {
		PdfContentByte lines = canvases[PdfPTable.LINECANVAS];
		PdfContentByte backgr = canvases[PdfPTable.BACKGROUNDCANVAS];
		// the coordinates of the border are retrieved
		float x1 = cell.left() + xPos;
		float y2 = cell.top() + yPos;
		float x2 = cell.right() + xPos;
		float y1 = y2 - maxHeight;

		// the backgroundcolor is set
		Color background = cell.backgroundColor();
		if (background != null) {
			backgr.setColorFill(background);
			backgr.rectangle(x1, y1, x2 - x1, y2 - y1);
			backgr.fill();
        }
		// if the element hasn't got any borders, nothing is added
		if (cell.hasBorders()) {
			if (cell.isUseVariableBorders()) {
				Rectangle borderRect = new Rectangle(cell.left() + xPos, cell
						.top()
						- maxHeight + yPos, cell.right() + xPos, cell.top()
						+ yPos);
				borderRect.cloneNonPositionParameters(cell);
                borderRect.setBackgroundColor(null);
				lines.rectangle(borderRect);
			} else {
				// the width is set to the width of the element
				if (cell.borderWidth() != Rectangle.UNDEFINED) {
					lines.setLineWidth(cell.borderWidth());
				}
				// the color is set to the color of the element
				Color color = cell.borderColor();
				if (color != null) {
					lines.setColorStroke(color);
				}

				// if the box is a rectangle, it is added as a rectangle
				if (cell.hasBorder(Rectangle.BOX)) {
					lines.rectangle(x1, y1, x2 - x1, y2 - y1);
				}
				// if the border isn't a rectangle, the different sides are
				// added apart
				else {
					if (cell.hasBorder(Rectangle.RIGHT)) {
						lines.moveTo(x2, y1);
						lines.lineTo(x2, y2);
					}
					if (cell.hasBorder(Rectangle.LEFT)) {
						lines.moveTo(x1, y1);
						lines.lineTo(x1, y2);
					}
					if (cell.hasBorder(Rectangle.BOTTOM)) {
						lines.moveTo(x1, y1);
						lines.lineTo(x2, y1);
					}
					if (cell.hasBorder(Rectangle.TOP)) {
						lines.moveTo(x1, y2);
						lines.lineTo(x2, y2);
					}
				}
				lines.stroke();
				if (color != null) {
					lines.resetRGBColorStroke();
				}
			}
		}
	}

	/**
	 * Writes a number of cells (not necessarily all cells).
	 * @param colStart
	 * @param colEnd
	 * @param xPos
	 * @param yPos
	 * @param canvases
	 */
	public void writeCells(int colStart, int colEnd, float xPos, float yPos,
			PdfContentByte[] canvases) {
		if (!calculated)
			calculateHeights();
		if (colEnd < 0)
			colEnd = cells.length;
		colEnd = Math.min(colEnd, cells.length);
		if (colStart < 0)
			colStart = 0;
		if (colStart >= colEnd)
			return;
		int newStart;
		for (newStart = colStart; newStart >= 0; --newStart) {
			if (cells[newStart] != null)
				break;
			xPos -= widths[newStart - 1];
		}
		xPos -= cells[newStart].left();
		for (int k = newStart; k < colEnd; ++k) {
			PdfPCell cell = cells[k];
			if (cell == null)
				continue;
			writeBorderAndBackground(xPos, yPos, cell, canvases);
			Image img = cell.getImage();
			float tly = 0;
			switch (cell.getVerticalAlignment()) {
			case Element.ALIGN_BOTTOM:
				tly = cell.top() + yPos - maxHeight + cell.height()
						- cell.getEffectivePaddingTop();
				break;
			case Element.ALIGN_MIDDLE:
				tly = cell.top() + yPos + (cell.height() - maxHeight) / 2
						- cell.getEffectivePaddingTop();
				break;
			default:
				tly = cell.top() + yPos - cell.getEffectivePaddingTop();
				break;
			}
			if (img != null) {
				boolean vf = false;
				if (cell.height() > maxHeight) {
					img.scalePercent(100);
					float scale = (maxHeight - cell.getEffectivePaddingTop() - cell
							.getEffectivePaddingBottom())
							/ img.scaledHeight();
					img.scalePercent(scale * 100);
					vf = true;
				}
				float left = cell.left() + xPos
						+ cell.getEffectivePaddingLeft();
				if (vf) {
					switch (cell.getHorizontalAlignment()) {
					case Element.ALIGN_CENTER:
						left = xPos
								+ (cell.left() + cell.getEffectivePaddingLeft()
										+ cell.right()
										- cell.getEffectivePaddingRight() - img
										.scaledWidth()) / 2;
						break;
					case Element.ALIGN_RIGHT:
						left = xPos + cell.right()
								- cell.getEffectivePaddingRight()
								- img.scaledWidth();
						break;
					default:
						break;
					}
					tly = cell.top() + yPos - cell.getEffectivePaddingTop();
				}
				img.setAbsolutePosition(left, tly - img.scaledHeight());
				try {
					canvases[PdfPTable.TEXTCANVAS].addImage(img);
				} catch (DocumentException e) {
					throw new ExceptionConverter(e);
				}
			} else {
				float fixedHeight = cell.getFixedHeight();
				float rightLimit = cell.right() + xPos
						- cell.getEffectivePaddingRight();
				float leftLimit = cell.left() + xPos
						+ cell.getEffectivePaddingLeft();
				if (cell.isNoWrap()) {
					switch (cell.getHorizontalAlignment()) {
					case Element.ALIGN_CENTER:
						rightLimit += 10000;
						leftLimit -= 10000;
						break;
					case Element.ALIGN_RIGHT:
						leftLimit -= 20000;
						break;
					default:
						rightLimit += 20000;
						break;
					}
				}
				ColumnText ct = ColumnText.duplicate(cell.getColumn());
				ct.setCanvases(canvases);
				float bry = tly
						- (maxHeight /* cell.height() */
								- cell.getEffectivePaddingTop() - cell
								.getEffectivePaddingBottom());
				if (fixedHeight > 0) {
					if (cell.height() > maxHeight) {
						tly = cell.top() + yPos - cell.getEffectivePaddingTop();
						bry = cell.top() + yPos - maxHeight
								+ cell.getEffectivePaddingBottom();
					}
				}
				if ((tly > bry || ct.zeroHeightElement()) && leftLimit < rightLimit) {
					ct
							.setSimpleColumn(leftLimit, bry - 0.001f,
									rightLimit, tly);
					try {
						ct.go();
					} catch (DocumentException e) {
						throw new ExceptionConverter(e);
					}
				}
			}
			PdfPCellEvent evt = cell.getCellEvent();
			if (evt != null) {
				Rectangle rect = new Rectangle(cell.left() + xPos, cell.top()
						+ yPos - maxHeight, cell.right() + xPos, cell.top()
						+ yPos);
				evt.cellLayout(cell, rect, canvases);
			}
		}
	}

	/**
	 * Checks if the dimensions of the columns were calculated.
	 * @return true if the dimensions of the columns were calculated
	 */
	public boolean isCalculated() {
		return calculated;
	}

	/**
	 * Gets the maximum height of the row (i.e. of the 'highest' cell).
	 * @return the maximum height of the row
	 */
	public float getMaxHeights() {
		if (calculated)
			return maxHeight;
		else
			return calculateHeights();
	}

	/**
	 * Changes the maximum height of the row (to make it higher).
	 * (added by Jin-Hsia Yang)
	 * @param maxHeight the new maximum height
	 */
	public void setMaxHeights(float maxHeight) {
		this.maxHeight = maxHeight;
	}

	//end add

	float[] getEventWidth(float xPos) {
		int n = 0;
		for (int k = 0; k < cells.length; ++k) {
			if (cells[k] != null)
				++n;
		}
		float width[] = new float[n + 1];
		n = 0;
		width[n++] = xPos;
		for (int k = 0; k < cells.length; ++k) {
			if (cells[k] != null) {
				width[n] = width[n - 1] + cells[k].width();
				++n;
			}
		}
		return width;
	}

	/**
	 * Splits a row to newHeight. The returned row is the remainder. It will
	 * return null if the newHeight was so small that only an empty row would
	 * result.
	 * 
	 * @param newHeight
	 *            the new height
	 * @return the remainder row or null if the newHeight was so small that only
	 *         an empty row would result
	 */
	public PdfPRow splitRow(float newHeight) {
		PdfPCell newCells[] = new PdfPCell[cells.length];
		float fh[] = new float[cells.length * 2];
		boolean allEmpty = true;
		for (int k = 0; k < cells.length; ++k) {
			PdfPCell cell = cells[k];
			if (cell == null)
				continue;
			fh[k * 2] = cell.getFixedHeight();
			fh[k * 2 + 1] = cell.getMinimumHeight();
			Image img = cell.getImage();
			PdfPCell c2 = new PdfPCell(cell);
			if (img != null) {
				if (newHeight > cell.getEffectivePaddingBottom()
						+ cell.getEffectivePaddingTop() + 2) {
					c2.setPhrase(null);
					allEmpty = false;
				}
			} else {
				float rightLimit = cell.isNoWrap() ? 20000 : cell.right()
						- cell.getEffectivePaddingRight();
				ColumnText ct = ColumnText.duplicate(cell.getColumn());
				float y1 = cell.top() - newHeight
						+ cell.getEffectivePaddingBottom();
				float y2 = cell.top() - cell.getEffectivePaddingTop();
				float y = Math.max(y1, y2);
				int status;
				ct.setSimpleColumn(
						cell.left() + cell.getEffectivePaddingLeft(), y1,
						rightLimit, y2);
				try {
					status = ct.go(true);
				} catch (DocumentException e) {
					throw new ExceptionConverter(e);
				}
				boolean thisEmpty = (ct.getYLine() == y);
				if (thisEmpty)
					ct = ColumnText.duplicate(cell.getColumn());
				allEmpty = (allEmpty && thisEmpty);
				if ((status & ColumnText.NO_MORE_TEXT) == 0 || thisEmpty) {
					c2.setColumn(ct);
				} else {
					c2.setPhrase(null);
				}
			}
			newCells[k] = c2;
			cell.setFixedHeight(newHeight);
		}
		if (allEmpty) {
			for (int k = 0; k < cells.length; ++k) {
				PdfPCell cell = cells[k];
				if (cell == null)
					continue;
				float f = fh[k * 2];
				float m = fh[k * 2 + 1];
				if (f <= 0)
					cell.setMinimumHeight(m);
				else
					cell.setFixedHeight(f);
			}
			return null;
		}
		calculateHeights();
		PdfPRow split = new PdfPRow(newCells);
		split.widths = (float[]) widths.clone();
		split.calculateHeights();
		return split;
	}
}