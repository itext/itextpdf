/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.tool.xml.html.table;

import com.itextpdf.text.*;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.css.*;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.AbstractTagProcessor;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import com.itextpdf.tool.xml.html.table.TableRowElement.Place;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.util.*;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author Emiel Ackermann
 *
 */
public class Table extends AbstractTagProcessor {
    public static final float DEFAULT_CELL_BORDER_WIDTH = 0.75f;

	private static final Logger LOG = LoggerFactory.getLogger(Table.class);
	private static final CssUtils utils = CssUtils.getInstance();
	private static final FontSizeTranslator fst = FontSizeTranslator.getInstance();

	/**
	 * Reorganizes table rows based on the designated normal {@link Place} in a
	 * table.
	 *
	 * @author Emiel Ackermann
	 *
	 */
	private final class NormalRowComparator implements Comparator<TableRowElement> {
		public int compare(final TableRowElement o1, final TableRowElement o2) {
			return o1.getPlace().getNormal().compareTo(o2.getPlace().getNormal());
		}
	}

	/**
	 * Reorganizes table rows based on the designated repeated {@link Place} in
	 * a table.
	 *
	 * @author Emiel Ackermann
	 *
	 */
	private final class RepeatedRowComparator implements Comparator<TableRowElement> {
		public int compare(final TableRowElement o1, final TableRowElement o2) {
			return o1.getPlace().getRepeated().compareTo(o2.getPlace().getRepeated());
		}
	}

	/**
	 * Default constructor.
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
	public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
		try {
			boolean percentage = false;
			String widthValue = tag.getCSS().get(HTML.Attribute.WIDTH);
			if(widthValue == null) {
				widthValue = tag.getAttributes().get(HTML.Attribute.WIDTH);
			}
			if(widthValue != null && widthValue.trim().endsWith("%")) {
				percentage = true;
			}

			int numberOfColumns = 0;
			List<TableRowElement> tableRows = new ArrayList<TableRowElement>(currentContent.size());
			List<Element> invalidRowElements = new ArrayList<Element>(1);
			String repeatHeader = tag.getCSS().get(CSS.Property.REPEAT_HEADER);
			String repeatFooter = tag.getCSS().get(CSS.Property.REPEAT_FOOTER);
			int headerRows = 0;
			int footerRows = 0;
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
					if (repeatHeader != null && repeatHeader.equalsIgnoreCase("yes")
							&& tableRowElement.getPlace().equals(TableRowElement.Place.HEADER)) {
						headerRows++;
					}
					if (repeatFooter != null && repeatFooter.equalsIgnoreCase("yes")
							&& tableRowElement.getPlace().equals(TableRowElement.Place.FOOTER)) {
						footerRows++;
					}
				} else {
					invalidRowElements.add(e);
				}
			}
			if (repeatFooter == null || !repeatFooter.equalsIgnoreCase("yes")) {
				Collections.sort(tableRows, new NormalRowComparator());
			} else {
				Collections.sort(tableRows, new RepeatedRowComparator());
			}
            PdfPTable table = intPdfPTable(numberOfColumns);
            table.setHeaderRows(headerRows + footerRows);
            table.setFooterRows(footerRows);
            
            int direction = getRunDirection(tag);
            if (direction != PdfWriter.RUN_DIRECTION_DEFAULT) {
                table.setRunDirection(direction);
            }
            TableStyleValues styleValues = setStyleValues(tag);
            table.setTableEvent(new TableBorderEvent(styleValues));
            setVerticalMargin(table, tag, styleValues, ctx);
            widenLastCell(tableRows, styleValues.getHorBorderSpacing());
			float[] columnWidths = new float[numberOfColumns];
			float[] widestWords = new float[numberOfColumns];
			float[] fixedWidths = new float[numberOfColumns];
            float[] colspanWidestWords = new float[numberOfColumns];
			int[] rowspanValue = new int[numberOfColumns];
			float largestColumn = 0;
            float largestColspanColumn = 0;
			int indexOfLargestColumn = -1;
            int indexOfLargestColspanColumn = -1;
			// Initial fill of the widths arrays
			for (TableRowElement row : tableRows) {
				int column = 0;
				for (HtmlCell cell : row.getContent()) {
					// check whether the current column should be skipped due to a
					// rowspan value of higher cell in this column.
                    // Contribution made by Arnost Havelka (Asseco): added while condition
                    while ((column < numberOfColumns) && (rowspanValue[column] > 0)) {
                        rowspanValue[column] = rowspanValue[column] - 1;
                        ++column;
                    }
					// sets a rowspan counter for current column (counter not
					// needed for last column).
					if (cell.getRowspan() > 1 && column != numberOfColumns - 1 && column < rowspanValue.length) {
						rowspanValue[column] = cell.getRowspan() - 1;
					}
					int colspan = cell.getColspan();
					if (cell.getFixedWidth() != 0) {
						float fixedWidth = cell.getFixedWidth() + getCellStartWidth(cell);
						float colSpanWidthSum = 0;
						int nonZeroColspanCols = 0;
						// Contribution made by Arnost Havelka (Asseco) (modified)
						for (int c = column; c < column + colspan && c < numberOfColumns; c++) {
							colSpanWidthSum += fixedWidths[c];
							if (fixedWidths[c] != 0)
								nonZeroColspanCols++;
						}
						for (int c = column; c < column + colspan && c < numberOfColumns; c++) {
							if (fixedWidths[c] == 0) {
								fixedWidths[c] = (fixedWidth - colSpanWidthSum) / (colspan - nonZeroColspanCols);
								columnWidths[c] = (fixedWidth - colSpanWidthSum) / (colspan - nonZeroColspanCols);
							}
						}
					}
					if (cell.getCompositeElements() != null) {
						float[] widthValues = setCellWidthAndWidestWord(cell);
						float cellWidth = widthValues[0] / colspan;
						float widestWordOfCell = widthValues[1] / colspan;
						for (int i = 0; i < colspan; i++) {
							int c = column + i;
                            // Contribution made by Arnost Havelka (Asseco)
                            if (c >= numberOfColumns) {
                                continue;
                            }
							if (fixedWidths[c] == 0 && cellWidth > columnWidths[c]) {
								columnWidths[c] = cellWidth;
                                if (colspan == 1) {
                                    if (cellWidth > largestColumn) {
                                        largestColumn = cellWidth;
                                        indexOfLargestColumn = c;
                                    }
                                } else {
                                    if (cellWidth > largestColspanColumn) {
                                        largestColspanColumn = cellWidth;
                                        indexOfLargestColspanColumn = c;
                                    }
                                }
							}
                            if (colspan == 1) {
                                if (widestWordOfCell > widestWords[c]) {
                                    widestWords[c] = widestWordOfCell;
                                }
                            } else {
                                if (widestWordOfCell > colspanWidestWords[c]) {
                                    colspanWidestWords[c] = widestWordOfCell;
                                }
                            }
						}
					}
					if (colspan > 1) {
						if (LOG.isLogging(Level.TRACE)) {
							LOG.trace(String.format(LocaleMessages.getInstance().getMessage(LocaleMessages.COLSPAN),
									colspan));
						}
						column += colspan - 1;
					}
					column++;
				}
			}
            if (indexOfLargestColumn == -1) {
                indexOfLargestColumn = indexOfLargestColspanColumn;
                if (indexOfLargestColumn == -1) {
                    indexOfLargestColumn = 0;
                }

                for (int column = 0; column < numberOfColumns; column++) {
                    widestWords[column] = colspanWidestWords[column];
                }
            }
			float outerWidth = getTableOuterWidth(tag, styleValues.getHorBorderSpacing(), ctx);
			float initialTotalWidth = getTableWidth(columnWidths, 0);
//			float targetWidth = calculateTargetWidth(tag, columnWidths, outerWidth, ctx);
			float targetWidth = 0;
			HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
			float max = htmlPipelineContext.getPageSize().getWidth() - outerWidth;
			boolean tableWidthFixed = false;
			if (tag.getAttributes().get(CSS.Property.WIDTH) != null || tag.getCSS().get(CSS.Property.WIDTH) != null) {
				targetWidth = new WidthCalculator().getWidth(tag, htmlPipelineContext.getRootTags(), htmlPipelineContext.getPageSize().getWidth(), initialTotalWidth);
				if (targetWidth > max) {
					targetWidth = max;
				}
				tableWidthFixed = true;
			} else if (initialTotalWidth <= max) {
				targetWidth = initialTotalWidth;
			} else if (null == tag.getParent() || (null != tag.getParent() && htmlPipelineContext.getRootTags().contains(tag.getParent().getName()))) {
				targetWidth = max;
			} else /* this table is an inner table and width adjustment is done in outer table */{
				targetWidth = getTableWidth(columnWidths, outerWidth);
			}
			float totalFixedColumnWidth = getTableWidth(fixedWidths, 0);
			float targetPercentage = 0;
			if (totalFixedColumnWidth == initialTotalWidth) { // all column widths are fixed
				targetPercentage = targetWidth / initialTotalWidth;
				if (initialTotalWidth > targetWidth) {
					for (int column = 0; column < columnWidths.length; column++) {
						columnWidths[column] *= targetPercentage;
					}
				} else if(tableWidthFixed && targetPercentage != 1){
					for (int column = 0; column < columnWidths.length; column++) {
						columnWidths[column] *= targetPercentage;
					}
				}
			} else {
				targetPercentage = (targetWidth - totalFixedColumnWidth) / (initialTotalWidth - totalFixedColumnWidth);
				// Reduce width of columns if the columnWidth array + borders +
				// paddings
				// is too large for the given targetWidth.
				if (initialTotalWidth > targetWidth) {
					float leftToReduce = 0;
					for (int column = 0; column < columnWidths.length; column++) {
						if (fixedWidths[column] == 0) {
							// Reduce width of the column to its targetWidth, if
							// widestWord of column still fits in the
							// targetWidth of
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
						} else { // set all none fixed columns to their minimum, with the
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
								// If the table has an insufficient fixed width
								// by
								// an
								// attribute or style, try to enlarge the table
								// to
								// its
								// minimum width (= widestWords array).
								float pageWidth = getHtmlPipelineContext(ctx).getPageSize().getWidth();
								if (getTableWidth(widestWords, outerWidth) < pageWidth) {
									targetWidth = getTableWidth(widestWords, outerWidth);
									leftToReduce = 0;
								} else {
									// If all columnWidths are set to the
									// widestWordWidths and the table is still
									// to
									// wide
									// content will fall off the edge of a page,
									// which
									// is similar to HTML.
									targetWidth = pageWidth - outerWidth;
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
				throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(
						LocaleMessages.NO_CUSTOM_CONTEXT), e);
			}

            Float tableHeight = new HeightCalculator().getHeight(tag, getHtmlPipelineContext(ctx).getPageSize().getHeight());
            Float tableRowHeight = null;
            if (tableHeight != null && tableHeight > 0) tableRowHeight = tableHeight/tableRows.size();
            int rowNumber = 0;
			for (TableRowElement row : tableRows) {
				int columnNumber = -1;
                Float computedRowHeight = null;
                /*if (tableHeight != null &&  tableRows.indexOf(row) == tableRows.size() - 1) {
                    float computedTableHeigt = table.calculateHeights();
                    computedRowHeight = tableHeight - computedTableHeigt;
                }*/
                List<HtmlCell> rowContent = row.getContent();
                if (rowContent.size() < 1)
                    continue;
                for (HtmlCell cell : rowContent) {
					List<Element> compositeElements = cell.getCompositeElements();
					if (compositeElements != null) {
						for (Element baseLevel : compositeElements) {
							if (baseLevel instanceof PdfPTable) {
								TableStyleValues cellValues = cell.getCellValues();
								float totalBordersWidth = cellValues.isLastInRow() ? styleValues
										.getHorBorderSpacing() * 2
										: styleValues.getHorBorderSpacing();
								totalBordersWidth += cellValues.getBorderWidthLeft()
										+ cellValues.getBorderWidthRight();
								float columnWidth = 0;
                                for (int currentColumnNumber = columnNumber + 1 ;currentColumnNumber <= columnNumber + cell.getColspan(); currentColumnNumber++ ) {
                                    columnWidth += columnWidths[currentColumnNumber];
                                }
                                PdfPTableEvent tableEvent = ((PdfPTable) baseLevel).getTableEvent();
								TableStyleValues innerStyleValues = ((TableBorderEvent) tableEvent)
										.getTableStyleValues();
								totalBordersWidth += innerStyleValues.getBorderWidthLeft();
								totalBordersWidth += innerStyleValues.getBorderWidthRight();
								((PdfPTable) baseLevel).setTotalWidth(columnWidth - totalBordersWidth);
							}
						}
					}
                    columnNumber += cell.getColspan();

					table.addCell(cell);
				}
				table.completeRow();
                if ((computedRowHeight == null || computedRowHeight <= 0) && tableRowHeight != null)
                    computedRowHeight = tableRowHeight;
                if (computedRowHeight != null && computedRowHeight > 0) {
                    float rowHeight = table.getRow(rowNumber).getMaxHeights();
                    if (rowHeight < computedRowHeight) {
                        table.getRow(rowNumber).setMaxHeights(computedRowHeight);
                    } else if (tableRowHeight != null && tableRowHeight < rowHeight){
                        tableRowHeight = (tableHeight - rowHeight - rowNumber*tableRowHeight)
                                /(tableRows.size() - rowNumber - 1);
                    }
                }
                rowNumber++;
			}
			if (percentage) {
				table.setWidthPercentage(utils.parsePxInCmMmPcToPt(widthValue));
				table.setLockedWidth(false);
			}
			List<Element> elems = new ArrayList<Element>();
			if (invalidRowElements.size() > 0) {
				// all invalid row elements taken as caption
				int i = 0;
				Tag captionTag = tag.getChildren().get(i++);
				while (!captionTag.getName().equalsIgnoreCase(HTML.Tag.CAPTION) && i < tag.getChildren().size()) {
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
		} catch (NoCustomContextException e) {
			throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e);
		}
	}

    protected PdfPTable intPdfPTable(int numberOfColumn) {
        PdfPTable table = new PdfPTable(numberOfColumn);

        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setSplitLate(false);

        return table;
    }

	/**
	 * Calculates the target width. First checks:
	 * <ol>
	 * <li>if the attribute or style "width" is found in the given tag and it is not wider than pageWidth - outerWidth, then the
	 * targetWidth = width value</li>
	 * <li>if the columnWidths array in total is not wider than pageWidth - outerWidth, then the
	 * targetWidth = the total of the columnWidths array</li>
	 * <li>if table's parent is a root tag or table has no parent, then the
	 * targetWidth = width of the page - outerWidth
	 * {@link Table#getTableOuterWidth(Tag, float, WorkerContext)}.</li>
	 * </ol>
	 * If none of the above is true, the width of the table is set to its
	 * default with the columnWidths array.
	 *
	 * @param tag containing attributes and css.
	 * @param columnWidths float[] containing the widest lines of text found in
	 *            the columns.
	 * @param outerWidth width needed for margins and borders.
	 * @param ctx
	 * @return float the target width of a table.
	 * @throws NoCustomContextException
	 */
	@SuppressWarnings("unused")
	private float calculateTargetWidth(final Tag tag, final float[] columnWidths, final float outerWidth,
			final WorkerContext ctx) throws NoCustomContextException {
		float targetWidth = 0;
		HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
		float max = htmlPipelineContext.getPageSize().getWidth() - outerWidth;
		float start = getTableWidth(columnWidths, 0);
		if (tag.getAttributes().get(CSS.Property.WIDTH) != null || tag.getCSS().get(CSS.Property.WIDTH) != null) {
			targetWidth = new WidthCalculator().getWidth(tag, htmlPipelineContext.getRootTags(), htmlPipelineContext
					.getPageSize().getWidth());
			if (targetWidth > max) {
				targetWidth = max;
			}
		} else if (start <= max) {
			targetWidth = start;
		} else if (null == tag.getParent()
				|| (null != tag.getParent() && htmlPipelineContext.getRootTags().contains(tag.getParent().getName()))) {
			targetWidth = max;
		} else /*
				 * this table is an inner table and width adjustment is done in
				 * outer table
				 */{
			targetWidth = getTableWidth(columnWidths, outerWidth);
		}
		return targetWidth;
	}

	/**
	 * Adds horizontal border spacing to the right padding of the last cell of
	 * each row.
	 *
	 * @param tableRows List of {@link TableRowElement} objects of the table.
	 * @param horBorderSpacing float containing the horizontal border spacing of
	 *            the table.
	 */
	private void widenLastCell(final List<TableRowElement> tableRows, final float horBorderSpacing) {
		for (TableRowElement row : tableRows) {
			List<HtmlCell> cells = row.getContent();
            if (cells.size() < 1)
                continue;
			HtmlCell last = cells.get(cells.size() - 1);
			last.getCellValues().setLastInRow(true);
			last.setPaddingRight(last.getPaddingRight() + horBorderSpacing);
		}
	}

	/**
	 * Set the table style values in a {@link TableStyleValues} object based on
	 * attributes and css of the given tag.
	 *
	 * @param tag containing attributes and css.
	 * @return a {@link TableStyleValues} object containing the table's style
	 *         values.
	 */
	public static TableStyleValues setStyleValues(final Tag tag) {
		TableStyleValues styleValues = new TableStyleValues();
		Map<String, String> css = tag.getCSS();
		Map<String, String> attributes = tag.getAttributes();
		if (attributes.containsKey(CSS.Property.BORDER)) {
			styleValues.setBorderColor(BaseColor.BLACK);
            String borderValue = attributes.get(CSS.Property.BORDER);
            if ("".equals(borderValue))
                styleValues.setBorderWidth(DEFAULT_CELL_BORDER_WIDTH);
            else
			    styleValues.setBorderWidth(utils.parsePxInCmMmPcToPt(borderValue));
		} else {
            for (Entry<String, String> entry : css.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key.equalsIgnoreCase(CSS.Property.BORDER_LEFT_STYLE) && CSS.Value.SOLID.equalsIgnoreCase(value)) {
                    styleValues.setBorderColorLeft(BaseColor.BLACK);
                    styleValues.setBorderWidthLeft(DEFAULT_CELL_BORDER_WIDTH);
                } else if (key.equalsIgnoreCase(CSS.Property.BORDER_RIGHT_STYLE) && CSS.Value.SOLID.equalsIgnoreCase(value)) {
                    styleValues.setBorderColorRight(BaseColor.BLACK);
                    styleValues.setBorderWidthRight(DEFAULT_CELL_BORDER_WIDTH);
                } else if (key.equalsIgnoreCase(CSS.Property.BORDER_TOP_STYLE) && CSS.Value.SOLID.equalsIgnoreCase(value)) {
                    styleValues.setBorderColorTop(BaseColor.BLACK);
                    styleValues.setBorderWidthTop(DEFAULT_CELL_BORDER_WIDTH);
                }  else if (key.equalsIgnoreCase(CSS.Property.BORDER_BOTTOM_STYLE) && CSS.Value.SOLID.equalsIgnoreCase(value)) {
                    styleValues.setBorderColorBottom(BaseColor.BLACK);
                    styleValues.setBorderWidthBottom(DEFAULT_CELL_BORDER_WIDTH);
                }
            }
            
            String color = css.get(CSS.Property.BORDER_BOTTOM_COLOR);
            if (color != null) {
			    styleValues.setBorderColorBottom(HtmlUtilities.decodeColor(color));
            }
            color = css.get(CSS.Property.BORDER_TOP_COLOR);
            if (color != null) {
                styleValues.setBorderColorTop(HtmlUtilities.decodeColor(color));
            }
            color = css.get(CSS.Property.BORDER_LEFT_COLOR);
            if (color != null) {
                styleValues.setBorderColorLeft(HtmlUtilities.decodeColor(color));
            }
            color = css.get(CSS.Property.BORDER_RIGHT_COLOR);
            if (color != null) {
                styleValues.setBorderColorRight(HtmlUtilities.decodeColor(color));
            }
            Float width = utils.checkMetricStyle(css, CSS.Property.BORDER_BOTTOM_WIDTH);
            if (width != null) {
			    styleValues.setBorderWidthBottom(width);
            }
            width =  utils.checkMetricStyle(css, CSS.Property.BORDER_TOP_WIDTH);
            if (width != null) {
                styleValues.setBorderWidthTop(width);
            }
            width =  utils.checkMetricStyle(css, CSS.Property.BORDER_RIGHT_WIDTH);
            if (width != null) {
                styleValues.setBorderWidthRight(width);
            }
            width = utils.checkMetricStyle(css, CSS.Property.BORDER_LEFT_WIDTH);
            if (width != null) {
                styleValues.setBorderWidthLeft(width);
            }
		}
		styleValues.setBackground(HtmlUtilities.decodeColor(css.get(CSS.Property.BACKGROUND_COLOR)));
		styleValues.setHorBorderSpacing(getBorderOrCellSpacing(true, css, attributes));
		styleValues.setVerBorderSpacing(getBorderOrCellSpacing(false, css, attributes));
		return styleValues;
	}

    public static TableStyleValues setBorderAttributeForCell(final Tag tag) {
        TableStyleValues styleValues = new TableStyleValues();
        if (tag == null) return styleValues;
        Map<String, String> attributes = tag.getAttributes();
        Map<String, String> css = tag.getCSS();
        String border = attributes.get(CSS.Property.BORDER);
        if (border != null  && (border.trim().length() == 0 || utils.parsePxInCmMmPcToPt(attributes.get(CSS.Property.BORDER)) > 0)) {
            styleValues.setBorderColor(BaseColor.BLACK);
            styleValues.setBorderWidth(Table.DEFAULT_CELL_BORDER_WIDTH);
        }

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
	static public float getBorderOrCellSpacing(final boolean getHor, final Map<String, String> css,
			final Map<String, String> attributes) {
		float spacing = 0f;
		String collapse = css.get(CSS.Property.BORDER_COLLAPSE);
		if (collapse == null || collapse.equals(CSS.Value.SEPARATE)) {
			String borderSpacing = css.get(CSS.Property.BORDER_SPACING);
			String cellSpacing = attributes.get(HTML.Attribute.CELLSPACING);
			if (borderSpacing != null) {
				if (borderSpacing.contains(" ")) {
					if (getHor) {
						spacing = utils.parsePxInCmMmPcToPt(borderSpacing.split(" ")[0]);
					} else {
						spacing = utils.parsePxInCmMmPcToPt(borderSpacing.split(" ")[1]);
					}
				} else {
					spacing = utils.parsePxInCmMmPcToPt(borderSpacing);
				}
			} else if (cellSpacing != null) {
			    spacing = utils.parsePxInCmMmPcToPt(cellSpacing);
			} else {
                spacing = 2f * DEFAULT_CELL_BORDER_WIDTH;
            }
		} else if (collapse.equals(CSS.Value.COLLAPSE)) {
			spacing = 0;
		}
		return spacing;
	}

	/**
	 * Sets the default cell width and widest word of a cell.
	 * <ul>
	 * <li>cell width = {@link Table#getCellStartWidth(HtmlCell)} + the width of
	 * the widest line of text.</li>
	 * <li>widest word = {@link Table#getCellStartWidth(HtmlCell)} + the widest
	 * word of the cell.</li>
	 * </ul>
	 * These 2 widths are used as the starting point when determining the width
	 * of the table in
	 *
	 * @param cell HtmlCell of which the widths are needed.
	 * @return float array containing the default cell width and the widest
	 *         word.
	 *         <ul>
	 *         <li>float[0] = cell width.</li>
	 *         <li>float[1] = widest word.</li>
	 *         </ul>
	 */
	private float[] setCellWidthAndWidestWord(final HtmlCell cell) {
		List<Float> rulesWidth = new ArrayList<Float>();
		float widestWordOfCell = 0f;
		float startWidth = getCellStartWidth(cell);
		float cellWidth;
        float widthDeviation = 0.001f;
		List<Element> compositeElements = cell.getCompositeElements();
		if (compositeElements != null) {
			for (Element baseLevel : compositeElements) {
                cellWidth = Float.NaN;
				if (baseLevel instanceof Phrase) {
					for (int i = 0; i < ((Phrase) baseLevel).size(); i++) {
						Element inner = ((Phrase) baseLevel).get(i);
						if (inner instanceof Chunk) {
                            if (Float.isNaN(cellWidth))
                                cellWidth = startWidth + widthDeviation;
							cellWidth += ((Chunk) inner).getWidthPoint();
							float widestWord = startWidth + widthDeviation + getCssAppliers().getChunkCssAplier().getWidestWord((Chunk) inner);
							if (widestWord > widestWordOfCell) {
								widestWordOfCell = widestWord;
							}
						}
					}
                    if (!Float.isNaN(cellWidth))
					    rulesWidth.add(cellWidth);
				} else if (baseLevel instanceof com.itextpdf.text.List) {
					for (Element li : ((com.itextpdf.text.List) baseLevel).getItems()) {
						cellWidth = startWidth + widthDeviation + ((ListItem) li).getIndentationLeft();
						for (Chunk c : li.getChunks()) {
							cellWidth += c.getWidthPoint();
							float widestWord = getCssAppliers().getChunkCssAplier().getWidestWord(c);
							if (startWidth + widthDeviation + widestWord > widestWordOfCell) {
								widestWordOfCell = startWidth + widthDeviation + widestWord;
							}
						}
                        rulesWidth.add(cellWidth);
					}
				} else if (baseLevel instanceof PdfPTable) {
					cellWidth = startWidth + widthDeviation + ((PdfPTable) baseLevel).getTotalWidth();
					for (PdfPRow innerRow : ((PdfPTable) baseLevel).getRows()) {
						int size = innerRow.getCells().length;
						TableBorderEvent event = (TableBorderEvent) ((PdfPTable) baseLevel).getTableEvent();
						TableStyleValues values = event.getTableStyleValues();
						float minRowWidth = values.getBorderWidthLeft() + (size + 1) * values.getHorBorderSpacing()
								+ values.getBorderWidthRight();
						int celnr = 0;
						for (PdfPCell innerCell : innerRow.getCells()) {
							celnr++;
							if (innerCell != null) {
								float innerWidestWordOfCell = setCellWidthAndWidestWord(new HtmlCell(innerCell,
										celnr == size))[1];
								minRowWidth += innerWidestWordOfCell;
							}
						}
						if (minRowWidth > widestWordOfCell) {
							widestWordOfCell = minRowWidth;
						}
					}
					rulesWidth.add(cellWidth);
				} else if (baseLevel instanceof PdfDiv) {
                    cellWidth = startWidth + widthDeviation + ((PdfDiv) baseLevel).getActualWidth();
                    rulesWidth.add(cellWidth);
                }
			}
		}
        cellWidth = startWidth;
		for (Float width : rulesWidth) {
			if (width > cellWidth) {
				cellWidth = width;
			}
		}
		return new float[] { cellWidth, widestWordOfCell };
	}

	/**
	 * Calculates the total width based on the given widths array and the given
	 * outer width.
	 *
	 * @param widths array of floats containing column width values.
	 * @param outerWidth equals the required space outside of the table for
	 *            margins and borders.
	 * @return a table's width.
	 * @throws NoCustomContextException
	 */
	private float getTableWidth(final float[] widths, final float outerWidth) throws NoCustomContextException {
		float width = 0;
		for (float f : widths) {
			width += f;
		}
		return width + outerWidth;
	}

	/**
	 * Adds horizontal values of a table and its parent if present. Following
	 * values are added up:
	 * <ul>
	 * <li>left and right margins of the table.</li>
	 * <li>left and right border widths of the table.</li>
	 * <li>left and right margins of the parent of the table is present.</li>
	 * <li>one horizontal border spacing.</li>
	 * </ul>
	 *
	 * @param tag
	 * @param horBorderSpacing
	 * @param ctx
	 * @return float containing the needed space for margins of table and
	 *         parent(s) and the borders of the table.
	 * @throws NoCustomContextException
	 */
	private float getTableOuterWidth(final Tag tag, final float horBorderSpacing, final WorkerContext ctx)
			throws NoCustomContextException {
		float total = utils.getLeftAndRightMargin(tag, getHtmlPipelineContext(ctx).getPageSize().getWidth())
				+ utils.checkMetricStyle(tag, CSS.Property.BORDER_LEFT_WIDTH)
				+ utils.checkMetricStyle(tag, CSS.Property.BORDER_RIGHT_WIDTH)
				+ horBorderSpacing;
		Tag parent = tag.getParent();
		if (parent != null) {
			total += utils.getLeftAndRightMargin(parent, getHtmlPipelineContext(ctx).getPageSize().getWidth());
		}
		return total;
	}

	/**
	 * Calculates the start width of a cell. Following values are added up:
	 * <ul>
	 * <li>padding left, this includes left border width and a horizontal border
	 * spacing.</li>
	 * <li>padding right, this includes right border width.</li>
	 * <li>the (colspan - 1) * horizontal border spacing.</li>
	 * </ul>
	 *
	 * @param cell HtmlCell of which the start width is needed.
	 * @return float containing the start width.
	 */
	private float getCellStartWidth(final HtmlCell cell) {
		TableStyleValues cellStyleValues = cell.getCellValues();
		// colspan - 1, because one horBorderSpacing has been added to
		// paddingLeft for all cells.
		int spacingMultiplier = cell.getColspan() - 1;
		float spacing = spacingMultiplier * cellStyleValues.getHorBorderSpacing();
		return spacing + cell.getPaddingLeft() + cell.getPaddingRight();
	}

	/**
	 * Sets the top and bottom margin of the given table.
	 *
	 * @param table PdfPTable on which the margins need to be set.
	 * @param t Tag containing the margin styles and font size if needed.
	 * @param values {@link TableStyleValues} containing border widths and
	 *            border spacing values.
	 * @param ctx
	 * @throws NoCustomContextException
	 */
	private void setVerticalMargin(final PdfPTable table, final Tag t, final TableStyleValues values,
			final WorkerContext ctx) throws NoCustomContextException {
		float spacingBefore = values.getBorderWidthTop();
		float spacingAfter = values.getVerBorderSpacing() + values.getBorderWidthBottom();
		for (Entry<String, String> css : t.getCSS().entrySet()) {
			String key = css.getKey();
			String value = css.getValue();
			if (CSS.Property.MARGIN_TOP.equalsIgnoreCase(key)) {
                final CssUtils utils = CssUtils.getInstance();
				spacingBefore += utils.calculateMarginTop(value, fst.getFontSize(t), getHtmlPipelineContext(ctx));
			} else if (CSS.Property.MARGIN_BOTTOM.equalsIgnoreCase(key)) {
				float marginBottom = utils.parseValueToPt(value, fst.getFontSize(t));
				spacingAfter += marginBottom;
				getHtmlPipelineContext(ctx).getMemory().put(HtmlPipelineContext.LAST_MARGIN_BOTTOM, marginBottom);
			} else if (CSS.Property.PADDING_TOP.equalsIgnoreCase(key)){
				table.setPaddingTop(utils.parseValueToPt(value, fst.getFontSize(t)));
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
