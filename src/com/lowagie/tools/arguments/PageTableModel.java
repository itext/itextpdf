package com.lowagie.tools.arguments;

import javax.swing.table.*;
import com.lowagie.text.pdf.PdfReader;
import java.io.*;
import com.lowagie.text.Rectangle;
import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PageTableModel
    extends AbstractTableModel {
  int seitenzahl;
  PdfReader reader;
  ArrayList auftragarray = new ArrayList();
  DecimalFormat myFormatter = new DecimalFormat("00000");
  public PageTableModel(String filename) {
    super();
    try {
      reader = new PdfReader(filename);
      seitenzahl = reader.getNumberOfPages();
    }
    catch (IOException ex) {
      throw new RuntimeException("Datei " + filename +
                                 " lässt sich nicht lesen!");
    }
  }

  /**
   * Returns the number of columns in the model.
   *
   * @return the number of columns in the model
   * @todo Implement this javax.swing.table.TableModel method
   */
  public int getColumnCount() {
    return 4;
  }

  /**
   * Returns the number of rows in the model.
   *
   * @return the number of rows in the model
   * @todo Implement this javax.swing.table.TableModel method
   */
  public int getRowCount() {
    return seitenzahl;
  }

  /**
   * Returns the value for the cell at <code>columnIndex</code> and <code>rowIndex</code>.
   *
   * @param rowIndex the row whose value is to be queried
   * @param columnIndex the column whose value is to be queried
   * @return the value Object at the specified cell
   * @todo Implement this javax.swing.table.TableModel method
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    Rectangle rec = reader.getPageSizeWithRotation(rowIndex + 1);
    switch (columnIndex) {
      case 0:
        return myFormatter.format(rowIndex + 1);
      case 1:
        return new Float(rec.width());
      case 2:
        return new Float(rec.height());
      case 3:
        return new Float(rec.getRotation());
    }
    return null;
  }

  public String getColumnName(int column) {
    String name = new Integer(column + 1).toString();
    switch (column) {
      case 0:
        name = "<html>Pagenr<p>" + name + "</html>";
        break;
      case 1:
        name = "<html>Weidth<p>" + name + "</html>";
        break;
      case 2:
        name = "<html>Height<p>" + name + "</html>";
        break;
      case 3:
        name = "<html>Rotation<p>" + name + "</html>";
        break;

      default:
        name = "<html>-<p>" + name + "</html>";
        break;
    }
    return name;
  }

}
