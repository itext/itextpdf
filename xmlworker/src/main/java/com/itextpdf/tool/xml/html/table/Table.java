/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.html.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.css.WidthCalculator;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.AbstractTagProcessor;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;

/**
 * @author Emiel Ackermann
 *
 */
public class Table extends AbstractTagProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(Table.class);
	private static final CssUtils utils = CssUtils.getInstance();
	private static final FontSizeTranslator fst = FontSizeTranslator.getInstance();

	private final class TableRowElementComparator implements Comparator<TableRowElement> {
		public int compare(final TableRowElement o1, final TableRowElement o2) {
			return o1.getPlace().getI().compareTo(o2.getPlace().getI());
		}
	}
	/**
	 *
	 */
	public Table() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag,
	 * java.util.List, com.itextpdf.text.Document)
	 */
	@Override
	public List<Element> end(final Tag tag, final List<Element> currentContent) {
		int numberOfColumns = 0;
		//
		List<TableRowElement> tableRows = new ArrayList<TableRowElement>(currentContent.size());
		List<Element> invalidRowElements = new ArrayList<Element>(1);
		for (Element e : currentContent) {
			int localNumCols = 0;
			if (e instanceof TableRowElement) {
				TableRowElement tableRowElement = (TableRowElement) e;
				for (HtmlCell cell : tableRowElement.getContent()) {
					localNumCols += cell.getColspan();
				}
				if (localNumCols > numberOfColumns) {
					numberOfColumns = localNumCols;
				}
				tableRows.add(tableRowElement);
			} else {
				invalidRowElements.add(e);
			}

		}
		Collections.sort(tableRows, new TableRowElementComparator());
		//
		PdfPTable table = new PdfPTable(numberOfColumns);
		TableStyleValues styleValues = setStyleValues(tag);
		table.setTableEvent(new TableBorderEvent(styleValues));
		setVerticalMargin(table, tag, styleValues);
		widenLastCell(tableRows, styleValues.getHorBorderSpacing());
		float[] columnWidths = new float[numberOfColumns];
		float[] widestWords = new float[numberOfColumns];
		float[] fixedWidths = new float[numberOfColumns];
		int[] rowspanValue = new int[numberOfColumns];
		float largestColumn = 0;
		int indexOfLargestColumn = 0;
		// Initial fill of the widths arrays
		for (TableRowElement row : tableRows) {
			int column = 0;
			for (HtmlCell cell : row.getContent()) {
				// check whether the current column should be skipped due to a
				// rowspan value of higher cell in this column.
				while (rowspanValue[column] > 1) {
					rowspanValue[column] = rowspanValue[column] - 1;
					++column;
				}
				// sets a rowspan counter for current column (counter not
				// needed for last column).
				if (cell.getRowspan() > 1 && column != numberOfColumns - 1) {
					rowspanValue[column] = cell.getRowspan() - 1;
				}
				if (cell.getFixedWidth() != 0) {
					float fixedWidth = cell.getFixedWidth() +
								getCellStartWidth(cell) +
								cell.getColspan() * styleValues.getHorBorderSpacing();
					if (fixedWidth > fixedWidths[column]) {
						fixedWidths[column] = fixedWidth;
						columnWidths[column] = fixedWidth;
					}
				}
				if (cell.getCompositeElements() != null) {
					float[] widthValues = setCellWidthAndWidestWord(cell);
					float cellWidth = widthValues[0];
					float widestWordOfCell = widthValues[1];
					if (fixedWidths[column] == 0 && cellWidth > columnWidths[column]) {
						columnWidths[column] = cellWidth;
						if (cellWidth > largestColumn) {
							largestColumn = cellWidth;
							indexOfLargestColumn = column;
						}
					}
					if (widestWordOfCell > widestWords[column]) {
						widestWords[column] = widestWordOfCell;
					}
				}
				column++;
				if (cell.getColspan() > 1) {
					if (LOG.isLogging(Level.TRACE)) {
						LOG.trace(String.format("Cell Colspan: %d", cell.getColspan()));
					}
					column += cell.getColspan() - 1;
				}
			}
		}
		float totalFixedWidth = getTableWidth(fixedWidths, tag, styleValues.getHorBorderSpacing());
		float targetWidth = calculateTargetWidth(tag, columnWidths, styleValues.getHorBorderSpacing());
		if (totalFixedWidth > targetWidth) {
			float targetPercentage = targetWidth / totalFixedWidth;
			for (int column = 0; column < columnWidths.length; column++) {
				columnWidths[column] *= targetPercentage;
			}
		} else {
			float initialTotalWidth = getTableWidth(columnWidths, tag, styleValues.getHorBorderSpacing());
			float targetPercentage = (targetWidth - totalFixedWidth) / (initialTotalWidth - totalFixedWidth);
			// Reduce width of columns if the columnWidth array + borders +
			// paddings
			// is too large for the given targetWidth.
			if (initialTotalWidth > targetWidth) {
				float leftToReduce = 0;
				for (int column = 0; column < columnWidths.length; column++) {
					if (fixedWidths[column] == 0) {
						// Reduce width of the column to its targetWidth, if
						// widestWord of column still fits in the targetWidth of
						// the
						// column.
						if (widestWords[column] <= columnWidths[column] * targetPercentage) {
							columnWidths[column] *= targetPercentage;
							// else take the widest word and calculate space
							// left to
							// reduce.
						} else {
							columnWidths[column] = widestWords[column];
							leftToReduce += widestWords[column] - columnWidths[column] * targetPercentage;
						}
						// if widestWord of a column does not fit in the
						// fixedWidth,
						// set the column width to the widestWord.
					} else if (fixedWidths[column] < widestWords[column]) {
						columnWidths[column] = widestWords[column];
						leftToReduce += widestWords[column] - fixedWidths[column];
					}
				}
				if (leftToReduce != 0) {
					// Reduce width of the column with the most text, if its
					// widestWord still fits in the reduced column.
					if (widestWords[indexOfLargestColumn] <= columnWidths[indexOfLargestColumn] - leftToReduce) {
						columnWidths[indexOfLargestColumn] -= leftToReduce;
					} else { // set all columnWidths to their minimum with the
								// widestWord array.
						for (int column = 0; leftToReduce != 0 && column < columnWidths.length; column++) {
							if (fixedWidths[column] == 0 && columnWidths[column] > widestWords[column]) {
								float difference = columnWidths[column] - widestWords[column];
								if (difference <= leftToReduce) {
									leftToReduce -= difference;
									columnWidths[column] = widestWords[column];
								} else {
									columnWidths[column] -= leftToReduce;
									leftToReduce = 0;
								}
							}
						}
						if (leftToReduce != 0) {
							// If the table has an insufficient fixed width by
							// an
							// attribute or style, try to enlarge the table to
							// its
							// minimum width (= widestWords array).
							float pageWidth = configuration.getPageSize().getWidth();
							if (getTableWidth(widestWords, tag, styleValues.getHorBorderSpacing()) < pageWidth) {
								targetWidth = getTableWidth(widestWords, tag, styleValues.getHorBorderSpacing());
								leftToReduce = 0;
							} else {
								// If all columnWidths are set to the
								// widestWordWidths and the table is still to
								// wide
								// content will fall off the edge of a page,
								// which
								// is similar to HTML.
								targetWidth = pageWidth - getTableOuterWidth(tag, styleValues.getHorBorderSpacing());
								leftToReduce = 0;
							}
						}
					}
				}
				// Enlarge width of columns to fit the targetWidth.
			} else if (initialTotalWidth < targetWidth) {
				for (int column = 0; column < columnWidths.length; column++) {
					if (fixedWidths[column] == 0) {
						columnWidths[column] *= targetPercentage;
					}
				}
			}
		}
		try {
			table.setTotalWidth(columnWidths);
			table.setLockedWidth(true);
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		} catch (DocumentException e) {
			throw new RuntimeWorkerException(e);
		}
		for (TableRowElement row : tableRows) {
			int columnNumber = -1;
			for (HtmlCell cell : row.getContent()) {
				columnNumber += cell.getColspan();
				List<Element> compositeElements = cell.getCompositeElements();
				if (compositeElements != null) {
					for (Element baseLevel : compositeElements) {
						if (baseLevel instanceof PdfPTable) {
							TableStyleValues cellValues = cell.getCellValues();
							float totalBordersWidth = cellValues.isLastInRow() ? styleValues.getHorBorderSpacing() * 2
									: styleValues.getHorBorderSpacing();
							totalBordersWidth += cellValues.getBorderWidthLeft() + cellValues.getBorderWidthRight();
							float columnWidth = columnWidths[columnNumber];
							PdfPTableEvent tableEvent = ((PdfPTable) baseLevel).getTableEvent();
							TableStyleValues innerStyleValues = ((TableBorderEvent) tableEvent).getTableStyleValues();
							totalBordersWidth += innerStyleValues.getBorderWidthLeft();
							totalBordersWidth += innerStyleValues.getBorderWidthRight();
							((PdfPTable) baseLevel).setTotalWidth(columnWidth - totalBordersWidth);
						}
					}
				}
				table.addCell(cell);
			}
			table.completeRow();
		}
		List<Element> elems = new ArrayList<Element>();
		if (invalidRowElements.size() > 0) {
			// all invalid row elements taken as caption
			int i = 0;
			Tag captionTag = tag.getChildren().get(i++);
			while (!captionTag.getTag().equalsIgnoreCase(HTML.Tag.CAPTION) && i < tag.getChildren().size()) {
				captionTag = tag.getChildren().get(i);
				i++;
			}
			String captionSideValue = captionTag.getCSS().get(CSS.Property.CAPTION_SIDE);
			if (captionSideValue != null && captionSideValue.equalsIgnoreCase(CSS.Value.BOTTOM)) {
				elems.add(table);
				elems.addAll(invalidRowElements);
			} else {
				elems.addAll(invalidRowElements);
				elems.add(table);
			}
		} else {
			elems.add(table);
		}
		return elems;
	}

	/**
	 * Calculates the target width. If one of the following is true:
	 * <ol>
	 * <li>the attribute or style "width" if found in the given tag.<br />
	 * targetWidth = width value </li>
	 * <li>table's parent is a root tag or table has no parent.<br />
	 * targetWidth = width of the page - {@link Table#getTableOuterWidth(Tag, float)}.</li>
	 * </ol>
	 * If none of the above is true, the width of the table is set to its default with the columnWidths array.
	 * @param columnWidths
	 * @return
	 */
	private float calculateTargetWidth(final Tag tag, final float[] columnWidths, final float horBorderSpacing) {
		float targetWidth = 0;
		float marginsBordersSpacing = getTableOuterWidth(tag, horBorderSpacing);
		if (tag.getAttributes().get(CSS.Property.WIDTH) != null || tag.getCSS().get(CSS.Property.WIDTH) != null) {
			targetWidth = new WidthCalculator().getWidth(tag, configuration) - marginsBordersSpacing;
		} else if (null == tag.getParent()
				|| (null != tag.getParent() && configuration.getRootTags().contains(tag.getParent().getTag()))) {
			targetWidth = configuration.getPageSize().getWidth() - marginsBordersSpacing;
		} else /* this table is an inner table and width adjustment is done in outer table */ {
			targetWidth = getTableWidth(columnWidths, tag, horBorderSpacing);
		}
		return targetWidth;
	}
	/**
	 * Adds horizontal border spacing to the right padding of the last cell of each row.
	 * @param tableRows List of {@link TableRowElement} objects of the table.
	 * @param horBorderSpacing float containing the horizontal border spacing of the table.
	 */
	private void widenLastCell(final List<TableRowElement> tableRows, final float horBorderSpacing) {
		for (TableRowElement row : tableRows) {
			List<HtmlCell> cells = row.getContent();
			HtmlCell last = cells.get(cells.size() - 1);
			last.getCellValues().setLastInRow(true);
			last.setPaddingRight(last.getPaddingRight() + horBorderSpacing);
		}
	}

	/** Set the table style values in a {@link TableStyleValues} object based on attributes and css of the given tag.
	 * @param tag containing attributes and css.
	 * @return a {@link TableStyleValues} object containing the table's style values.
	 */
	private TableStyleValues setStyleValues(final Tag tag) {
		TableStyleValues styleValues = new TableStyleValues();
		Map<String, String> css = tag.getCSS();
		Map<String, String> attributes = tag.getAttributes();
		if (attributes.containsKey(CSS.Property.BORDER)) {
			styleValues.setBorderColor(BaseColor.BLACK);
			styleValues.setBorderWidth(utils.parsePxInCmMmPcToPt(attributes.get(CSS.Property.BORDER)));
		} else {
			styleValues.setBorderColorBottom(HtmlUtilities.decodeColor(css.get(CSS.Property.BORDER_BOTTOM_COLOR)));
			styleValues.setBorderColorTop(HtmlUtilities.decodeColor(css.get(CSS.Property.BORDER_TOP_COLOR)));
			styleValues.setBorderColorLeft(HtmlUtilities.decodeColor(css.get(CSS.Property.BORDER_LEFT_COLOR)));
			styleValues.setBorderColorRight(HtmlUtilities.decodeColor(css.get(CSS.Property.BORDER_RIGHT_COLOR)));
			styleValues.setBorderWidthBottom(utils.checkMetricStyle(css, CSS.Property.BORDER_BOTTOM_WIDTH));
			styleValues.setBorderWidthTop(utils.checkMetricStyle(css, CSS.Property.BORDER_TOP_WIDTH));
			styleValues.setBorderWidthLeft(utils.checkMetricStyle(css, CSS.Property.BORDER_LEFT_WIDTH));
			styleValues.setBorderWidthRight(utils.checkMetricStyle(css, CSS.Property.BORDER_RIGHT_WIDTH));
		}
		styleValues.setBackground(HtmlUtilities.decodeColor(css.get(CSS.Property.BACKGROUND_COLOR)));
		styleValues.setHorBorderSpacing(getBorderOrCellSpacing(true, css, attributes));
		styleValues.setVerBorderSpacing(getBorderOrCellSpacing(false, css, attributes));
		return styleValues;
	}

	/**
	 * Extracts and parses the style border-spacing or the attribute cellspacing
	 * of a table tag, if present. Favors the style border-spacing over the
	 * attribute cellspacing. <br />
	 * If style="border-collapse:collapse" is found in the css, the spacing is
	 * always 0f. <br />
	 * If no spacing is set, the default of 1.5pt is returned.
	 *
	 * @param getHor true for horizontal spacing, false for vertical spacing.
	 * @param css of the table tag.
	 * @param attributes of the table tag.
	 * @return horizontal or vertical spacing between two cells or a cell and
	 *         the border of the table.
	 */
	public float getBorderOrCellSpacing(final boolean getHor, final Map<String, String> css, final Map<String, String> attributes) {
		float spacing = 1.5f;
		String collapse = css.get("border-collapse");
		if(collapse == null || collapse.equals("seperate")) {
			String borderSpacing = css.get("border-spacing");
			String cellSpacing = attributes.get("cellspacing");
			String borderAttr = attributes.get("border");
			if(borderSpacing != null) {
				if(borderSpacing.contains(" ")){
					if(getHor) {
						spacing = utils.parsePxInCmMmPcToPt(borderSpacing.split(" ")[0]);
					} else {
						spacing = utils.parsePxInCmMmPcToPt(borderSpacing.split(" ")[1]);
					}
				} else {
					spacing = utils.parsePxInCmMmPcToPt(borderSpacing);
				}
			} else if (cellSpacing != null){
				spacing = utils.parsePxInCmMmPcToPt(cellSpacing);
			} else if (borderAttr != null){
				spacing = 1.5f;
			}
		} else if(collapse.equals("collapse")){
			spacing = 0;
		}
		return spacing;
	}

	/**
	 * Sets the default cell width and widest word of a cell.
	 * <ul>
	 * <li>cell width = {@link Table#getCellStartWidth(HtmlCell)} + the width of the widest line of text.</li>
	 * <li>widest word = {@link Table#getCellStartWidth(HtmlCell)} + the widest word of the cell.</li>
	 * </ul>
	 * These 2 widths are used as the starting point when determining the width of the table in
	 * @param cell HtmlCell of which the widths are needed.
	 * @return float array containing the default cell width and the widest word.
	 * <ul>
	 * <li>float[0] = cell width.</li>
	 * <li>float[1] = widest word.</li>
	 * </ul>
	 */
	private float[] setCellWidthAndWidestWord(final HtmlCell cell) {
		List<Float> rulesWidth = new ArrayList<Float>();
		float widestWordOfCell = 0f;
		float startWidth = getCellStartWidth(cell);
		float cellWidth = startWidth;
		List<Element> compositeElements = cell.getCompositeElements();
		if (compositeElements != null) {
			for(Element baseLevel: compositeElements){
	        	if (baseLevel instanceof Phrase) {
	        		for(int i=0; i < ((Phrase)baseLevel).size() ; i++) {
	        			Element inner = ((Phrase)baseLevel).get(i);
	        			if (inner instanceof Chunk) {
		        			cellWidth += ((Chunk)inner).getWidthPoint();
		        			float widestWord = startWidth + new ChunkCssApplier().getWidestWord((Chunk) inner);
							if(widestWord > widestWordOfCell) {
		        				widestWordOfCell = widestWord;
		        			}
	        			}
	        		}
	        		rulesWidth.add(cellWidth);
	        		cellWidth = startWidth;
	        	} else if (baseLevel instanceof com.itextpdf.text.List) {
					for(Element li: ((com.itextpdf.text.List)baseLevel).getItems()) {
	    				rulesWidth.add(cellWidth);
	    				cellWidth = startWidth + ((ListItem)li).getIndentationLeft();
						for(Chunk c :((ListItem)li).getChunks()) {
							cellWidth += c.getWidthPoint();
		        			float widestWord = new ChunkCssApplier().getWidestWord(c);
							if(startWidth + widestWord > widestWordOfCell) {
		        				widestWordOfCell = startWidth + widestWord;
		        			}
						}
					}
					rulesWidth.add(cellWidth);
					cellWidth = startWidth;
	        	} else if (baseLevel instanceof PdfPTable) {
	        		rulesWidth.add(cellWidth);
					cellWidth = startWidth + ((PdfPTable)baseLevel).getTotalWidth();
					for(PdfPRow innerRow :((PdfPTable)baseLevel).getRows()) {
						float minRowWidth = 0;
						int size = innerRow.getCells().length;
						int celnr = 0;
						for(PdfPCell innerCell : innerRow.getCells()) {
							celnr++;
							if(innerCell != null) {
								float innerWidestWordOfCell = setCellWidthAndWidestWord(new HtmlCell(innerCell, celnr == size))[1];
								minRowWidth += innerWidestWordOfCell;
							}
						}
						if(minRowWidth > widestWordOfCell){
							widestWordOfCell = minRowWidth;
						}
					}
					rulesWidth.add(cellWidth);
					cellWidth = startWidth;
	        	}
	    	}
		}
		for(Float width: rulesWidth) {
			if(width > cellWidth) {
				cellWidth = width;
			}
		}
	return new float[]{cellWidth, widestWordOfCell};
	}

	private float getTableWidth(final float[] widths, final Tag tag, final float horBorderSpacing) {
		float width = 0;
		for(float f: widths) {
			width += f;
		}
		return width + getTableOuterWidth(tag, horBorderSpacing);
	}

	/**
	 * Adds horizontal values of a table and its parent if present. Following values are added up:
	 * <ul>
	 * <li>left and right margins of the table.</li>
	 * <li>left and right border widths of the table.</li>
	 * <li>left and right margins of the parent of the table is present.</li>
	 * <li>one horizontal border spacing.</li>
	 * </ul>
	 * @param tag
	 * @param horBorderSpacing
	 * @return
	 */
	private float getTableOuterWidth(final Tag tag, final float horBorderSpacing) {
		float total = utils.getLeftAndRightMargin(tag, configuration.getPageSize().getWidth())
			+ utils.checkMetricStyle(tag, CSS.Property.BORDER_LEFT_WIDTH)
			+ utils.checkMetricStyle(tag, CSS.Property.BORDER_RIGHT_WIDTH)
			+ horBorderSpacing;
		Tag parent = tag.getParent();
		if (parent != null) {
			total += utils.getLeftAndRightMargin(parent, configuration.getPageSize().getWidth());
		}
		return total;
	}

	/**
	 * Calculates the start width of a cell. Following values are added up:
	 * <ul>
	 * <li>padding left, this includes left border width and a horizontal border spacing.</li>
	 * <li>padding right, this includes right border width.</li>
	 * <li>the (colspan - 1) * horizontal border spacing.</li>
	 * </ul>
	 * @param cell HtmlCell of which the start width is needed.
	 * @return float containing the start width.
	 */
	private float getCellStartWidth(final HtmlCell cell) {
		TableStyleValues cellStyleValues = cell.getCellValues();
		// colspan - 1, because one horBorderSpacing has been added to paddingLeft for all cells.
		int spacingMultiplier = cell.getColspan() - 1;
		float spacing = spacingMultiplier*cellStyleValues.getHorBorderSpacing();
		return spacing + cell.getPaddingLeft()+cell.getPaddingRight()+1;
	}

	/**
	 * Sets the top and bottom margin of the given table.
	 *
	 * @param table PdfPTable on which the margins need to be set.
	 * @param t Tag containing the margin styles and font size if needed.
	 * @param values {@link TableStyleValues} containing border widths and border spacing values.
	 */
	private void setVerticalMargin(final PdfPTable table, final Tag t, final TableStyleValues values) {
		float spacingBefore = values.getBorderWidthTop();
		Map<String, Object> memory = configuration.getMemory();
		Object mb = memory.get(XMLWorkerConfig.LAST_MARGIN_BOTTOM);
		if(mb != null) {
			spacingBefore += (Float)mb;
		}
		float spacingAfter = values.getVerBorderSpacing()+values.getBorderWidthBottom();
		for (Entry<String, String> css : t.getCSS().entrySet()) {
        	String key = css.getKey();
			String value = css.getValue();
			if(CSS.Property.MARGIN_TOP.equalsIgnoreCase(key)) {
				spacingBefore += utils.parseValueToPt(value, fst.getFontSize(t));
			} else if (CSS.Property.MARGIN_BOTTOM.equalsIgnoreCase(key)) {
				float marginBottom = utils.parseValueToPt(value, fst.getFontSize(t));
				spacingAfter += marginBottom;
				configuration.getMemory().put(XMLWorkerConfig.LAST_MARGIN_BOTTOM, marginBottom);
			}
		}
		table.setSpacingBefore(spacingBefore);
		table.setSpacingAfter(spacingAfter);
	}
    /*
     * (non-Javadoc)
     *
     * @see com.itextpdf.tool.xml.TagProcessor#isStackOwner()
     */
    @Override
	public boolean isStackOwner() {
        return true;
    }
}
