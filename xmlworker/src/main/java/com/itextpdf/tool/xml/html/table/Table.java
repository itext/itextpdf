/*
 * $Id: $
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.tool.xml.AbstractTagProcessor;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.css.WidthCalculator;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.pdfelement.FixedWidthCell;
import com.itextpdf.tool.xml.html.table.TableRowElement.Place;

/**
 * @author Emiel Ackermann
 *
 */
public class Table extends AbstractTagProcessor {

	/**
	 * @author Emiel Ackermann
	 *
	 */
	private static final CssUtils utils = CssUtils.getInstance();
	private static final FontSizeTranslator fst = FontSizeTranslator.getInstance();
	private final TableStyleValues styleValues = new TableStyleValues();

	private final class TableRowElementComparator implements Comparator<Element> {
		public int compare(final Element o1, final Element o2) {
			return ((TableRowElement)o1).getPlace().getI().compareTo(((TableRowElement)o2).getPlace().getI());
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
	public List<Element> endElement(final Tag tag, final List<Element> currentContent) {
    	int numberOfColumns = 0;
    	TableRowElement caption = null;
		int rowNumber = 0;
		int total = currentContent.size();
		boolean found = false;
		while (!found && rowNumber < total) {
			TableRowElement row =  ((TableRowElement)currentContent.get(rowNumber));
			if(row.getPlace().equals(Place.CAPTION_BOTTOM) || row.getPlace().equals(Place.CAPTION_TOP)) {
				caption = (row);
				found = true;
			}
			rowNumber++;
		}
    	currentContent.remove(caption);
    	//Determine number of columns by taking the first row and counting its cells (colspan included).
    	for (Element cell : ((TableRowElement) currentContent.get(0)).getContent()) {
			numberOfColumns += ((PdfPCell)cell).getColspan();
		}
		Collections.sort(currentContent, new TableRowElementComparator());
		PdfPTable inner = new PdfPTable(numberOfColumns);
		Map<String, String> css = tag.getCSS();
		Map<String, String> attributes = tag.getAttributes();
		if(css.containsKey("border-top-width")||css.containsKey("border-left-width")||css.containsKey("border-right-width")||css.containsKey("border-bottom-width")) {
			styleValues.setBorderSpacing(getBorderOrCellSpacing(css, attributes));
			inner.setTableEvent(new TableBorderEvent(styleValues, css));
		} else if(attributes.containsKey(CSS.Property.BORDER)) {
			styleValues.setBorderSpacing(1.5f);
			styleValues.setTableBorderWidth(utils.parsePxInCmMmPcToPt(attributes.get(CSS.Property.BORDER)));
			styleValues.setTableBorderColor(BaseColor.BLACK);
			inner.setTableEvent(new SimpleTableBorderEvent(styleValues));
		}

		float[] columnWidths = new float[numberOfColumns];
		float[] widestWords = new float[numberOfColumns];
		float[] fixedWidths = new float[numberOfColumns];
		int[] rowspanValue = new int[numberOfColumns];
		float largestColumn = 0;
		int indexOfLargestColumn = 0;
		// Initial fill of the widths arrays
		for (Element row : currentContent) {
			int column = 0;
	        for (Element cell : ((TableRowElement) row).getContent()) {
	        	if(rowspanValue[column] != 0) {
	        		rowspanValue[column] = rowspanValue[column]-1;
	        		++column;
	        	}
	        	if(((PdfPCell)cell).getColspan() > 1) {
	        		column += ((PdfPCell)cell).getColspan()-1;
	        	}
	        	// sets a rowspan counter for current column (counter not needed for last column).
	        	if(((PdfPCell)cell).getRowspan() > 1 && column != numberOfColumns-1) {
	        		rowspanValue[column] = ((PdfPCell)cell).getRowspan()-1;
	        	}
	        	if(cell instanceof FixedWidthCell && fixedWidths[column] < ((FixedWidthCell) cell).getFixedWidth()) {
	        		fixedWidths[column] = ((FixedWidthCell) cell).getFixedWidth();
	        		columnWidths[column] = ((FixedWidthCell) cell).getFixedWidth();
	        	}
	        	if(cell instanceof PdfPCell && ((PdfPCell)cell).getCompositeElements() != null) {
		        	float[] settedValues = setCellWidthAndWidestWord((PdfPCell) cell);
		        	float cellWidth = settedValues[0];
		        	float widestWordOfCell = settedValues[1];
		        	if(fixedWidths[column] == 0 && cellWidth > columnWidths[column]) {
		        		columnWidths[column] = cellWidth;
		        		if(cellWidth > largestColumn) {
			        		largestColumn = cellWidth;
			        		indexOfLargestColumn = column;
			        	}
		        	}
		        	if(widestWordOfCell > widestWords[column]) {
		        		 widestWords[column] = widestWordOfCell;
		        	}
		        }
		        column++;
	        }
	    }
//	    PdfPCell outerCell = new PdfPCell(inner);
//		new PdfPCellCssApplier(configuration).apply(outerCell, tag);
		float totalFixedWidth = getTotalFixedWidth(fixedWidths);
		float targetWidth = 0;
		if(attributes.get("width") != null || css.get("width") != null) {
			targetWidth = new WidthCalculator().getWidth(tag, configuration);
		} else if(CssUtils.ROOT_TAGS.contains(tag.getParent().getTag())) {
			targetWidth = configuration.getPageSize().getWidth() - utils.getLeftAndRightMargin(tag);
		} else {
			targetWidth = getTotalWidth(columnWidths, tag);
		}
		float initialTotalWidth = getTotalWidth(columnWidths, tag);
		float targetPercentage = (targetWidth-totalFixedWidth)/(initialTotalWidth-totalFixedWidth);
		//Reduce width of columns if the columnWidth array + borders + paddings is too large for the given targetWidth.
		if(initialTotalWidth > targetWidth) {
			float leftToReduce = 0;
			for(int column = 0; column<columnWidths.length; column++) {
				if(fixedWidths[column] == 0) {
					//Reduce width of the column to its targetWidth, if widestWord of column still fits in the targetWidth of the column.
					if(widestWords[column] <= columnWidths[column]*targetPercentage) {
						columnWidths[column] *= targetPercentage;
					//else take the widest word and calculate space left to reduce.
					} else {
						columnWidths[column] = widestWords[column];
						leftToReduce += widestWords[column] - columnWidths[column]*targetPercentage;
					}
				// if widestWord of a column does not fit in the fixedWidth, set the column width to the widestWord.
				} else if(fixedWidths[column]<widestWords[column]){
					columnWidths[column] = widestWords[column];
					leftToReduce += widestWords[column] - fixedWidths[column];
				}
			}
			if(leftToReduce != 0) {
				//Reduce width of the column with the most text, if its widestWord still fits in the reduced column.
				if(widestWords[indexOfLargestColumn] <= columnWidths[indexOfLargestColumn] - leftToReduce) {
					columnWidths[indexOfLargestColumn] -= leftToReduce;
				} else while (leftToReduce != 0) {
					for(int column = 0; column<columnWidths.length; column++) {
						if(fixedWidths[column] == 0 && columnWidths[column] > widestWords[column]) {
							float difference = columnWidths[column] - widestWords[column];
							if(difference <= leftToReduce) {
								leftToReduce -= columnWidths[column] - widestWords[column];
								columnWidths[column] = widestWords[column];
							} else {
								columnWidths[column] -= leftToReduce;
							}
						}
					}
					// If all columnWidths are set to the widestWordWidths and the table is still to wide, try to enlarge table width to fit all content.
					if(getTotalWidth(widestWords, tag) < configuration.getPageSize().getWidth()) {
						targetWidth = getTotalWidth(widestWords, tag);
						leftToReduce = 0;
					} else {
						//If all columnWidths are set to the widestWordWidths and the table is still to wide content will fall off the edge of a page, which is similar to HTML.
						targetWidth = configuration.getPageSize().getWidth();
						leftToReduce = 0;
					}
				}
			}
		// Enlarge width of columns to fit the targetWidth.
		} else if(initialTotalWidth < targetWidth) {
			for(int column = 0; column<columnWidths.length; column++) {
				if(fixedWidths[column] == 0) {
					columnWidths[column] *= targetPercentage;
				}
			}
		}
		try {
			inner.setTotalWidth(columnWidths);
			inner.setLockedWidth(true);
		} catch (DocumentException e) {
			throw new RuntimeWorkerException(e);
		}
		setVerticalMargin(inner, tag);
//		PdfPTable outer = new PdfPTable(1);
//		outer.setTotalWidth(targetWidth);
//		outer.setLockedWidth(true);
//		outer.setSplitLate(false);
		for (Element row : currentContent) {
			int columnNumber = -1;
			for (Element cell : ((TableRowElement) row).getContent()) {
				columnNumber += ((PdfPCell)cell).getColspan();
				List<Element> compositeElements = ((PdfPCell) cell).getCompositeElements();
				if(compositeElements != null){
					for(Element baseLevel: ((PdfPCell) cell).getCompositeElements()){
						if(baseLevel instanceof PdfPTable) {
//	The height of the inner tables is not automatically adjusted to the new width.
//							float originalWidth = ((PdfPTable) baseLevel).getTotalWidth();
//							float originalHeight = ((PdfPTable) baseLevel).getTotalHeight();
							float columnWidth = columnWidths[columnNumber];
							((PdfPTable) baseLevel).setTotalWidth(columnWidth);
							PdfPCell innerCell = ((PdfPTable) baseLevel).getRow(0).getCells()[0];
							((PdfPTable)innerCell .getCompositeElements().get(0)).setTotalWidth(columnWidth-getCellStartWidth(innerCell));
						}
					}
				}
				inner.addCell((PdfPCell) cell);
			}
		}
//		if (caption != null) {
//			if (caption.getPlace().equals(Place.CAPTION_TOP)) {
//				outer.addCell((PdfPCell) caption.getContent().get(0));
//				outer.addCell(outerCell);
//			} else if (caption.getPlace().equals(Place.CAPTION_BOTTOM)) {
//				outer.addCell(outerCell);
//				outer.addCell((PdfPCell) caption.getContent().get(0));
//			}
//		} else {
//			outer.addCell(outerCell);
//		}
		List<Element> elems = new ArrayList<Element>();
		elems.add(inner);
		return elems;
	}
	/**
	 * Extracts and parses the style border-spacing or the attribute cellspacing of a table tag. <br />
	 * Favors the style border-spacing over the attribute cellspacing. If the style border-collapse is found in the css, the spacing is always 0f.
	 * @param css of the table tag.
	 * @param attributes of the table tag.
	 * @return spacing between two cells or a cell and the border of the table.
	 */
	public float getBorderOrCellSpacing(final Map<String, String> css, final Map<String, String> attributes) {
		float spacing = 0;
		String collapse = css.get("border-collapse");
		if(collapse == null || collapse.equals("seperate")) {
			spacing = utils.checkMetricStyle(css, "border-spacing");
			if(spacing == 0){
				if(attributes.containsKey("cellspacing")) {
					spacing = utils.parsePxInCmMmPcToPt(attributes.get("cellspacing"));
				} else {
					spacing = 1.5f;
				}
			}
		} else if(collapse.equals("collapse")){
			spacing = 0;
		}
		return spacing;
	}

	/**
	 * @param column
	 * @param fixedWidths
	 * @param widestWords
	 * @param columnWidths
	 * @return
	 */
	private float[] setCellWidthAndWidestWord(final PdfPCell cell) {
		List<Float> rulesWidth = new ArrayList<Float>();
		float widestWordOfCell = 0f;
		float startWidth = getCellStartWidth(cell);
		float cellWidth = startWidth;
		for(Element baseLevel: cell.getCompositeElements()){
        	if (baseLevel instanceof Phrase) {
        		for(int i=0; i < ((Phrase)baseLevel).size() ; i++) {
        			Element inner = ((Phrase)baseLevel).get(i);
        			if (inner instanceof Chunk) {
	        			cellWidth += ((Chunk)inner).getWidthPoint();
	        			if(startWidth + new ChunkCssApplier().getWidestWord((Chunk) inner) > widestWordOfCell) {
	        				widestWordOfCell = startWidth + new ChunkCssApplier().getWidestWord((Chunk) inner);
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
	        			if(startWidth + new ChunkCssApplier().getWidestWord(c) > widestWordOfCell) {
	        				widestWordOfCell = startWidth + new ChunkCssApplier().getWidestWord(c);
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
					for(PdfPCell innerCell : innerRow.getCells()) {
						if(innerCell != null) {
							float innerWidestWordOfCell = setCellWidthAndWidestWord(innerCell)[1];
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
        	for(Float width: rulesWidth) {
        		if(width > cellWidth) {
        			cellWidth = width;
        		}
        	}
    	}
	return new float[]{cellWidth, widestWordOfCell};
	}

	/**
	 * @param fixedWidths
	 * @return
	 */
	private float getTotalFixedWidth(final float[] fixedWidths) {
		int fixedWidthTotal = 0;
		for(float f: fixedWidths) {
			if(f != 0) {
				fixedWidthTotal += f;
			}
		}
		return fixedWidthTotal;
	}

	private float getTotalWidth(final float[] columnWidths, final Tag tag) {
		float width = 0;
		for(float f: columnWidths) {
			width += f;
		}
		Map<String, String> css = tag.getCSS();
		width += utils.checkMetricStyle(css, "border-left-width")+styleValues.getBorderSpacing();
		width += utils.checkMetricStyle(css, "border-right-width")+styleValues.getBorderSpacing();
		return width;
	}

	private float getCellStartWidth(final PdfPCell cell) {
		float spacing = cell.getColspan()*styleValues.getBorderSpacing();
		float left =  cell.getEffectivePaddingLeft();
		left = (left>1)?left:2;
		float right = cell.getEffectivePaddingRight();
		right = (right>1)?right:2;
		return spacing + left+right+1; // Default 2pt left and right padding + 1 for a border(?).
	}

	private void setVerticalMargin(final PdfPTable table, final Tag t) {
		for (Entry<String, String> css : t.getCSS().entrySet()) {
        	String key = css.getKey();
			String value = css.getValue();
			if(CSS.Property.MARGIN_TOP.equalsIgnoreCase(key)) {
				table.setSpacingBefore(utils.calculateMarginTop(t, value, fst.getFontSize(t)));
			} else if (CSS.Property.MARGIN_BOTTOM.equalsIgnoreCase(key)) {
                table.setSpacingAfter(utils.parseValueToPt(value, fst.getFontSize(t)));
			}
		}
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
