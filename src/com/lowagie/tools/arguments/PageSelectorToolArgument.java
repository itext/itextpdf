package com.lowagie.tools.arguments;

import java.beans.*;

import java.awt.event.*;

import com.lowagie.tools.plugins.*;
import javax.swing.JDialog;

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
public class PageSelectorToolArgument
    extends ToolArgument {
  public final static String PROPERTYFILENAME = "inputfilename";
  public final static String PROPERTYPAGESELECTIONSTRING =
      "pageselectionstring";

  public PageSelectorToolArgument(AbstractTool tool, String name,
                                  String description,
                                  String classname) {
    super(tool, name, description, classname);

  }

  public void actionPerformed(ActionEvent e) {
    if (jDialog1 != null) {
      jDialog1.show();
    }
  }

  public Object getArgument() throws InstantiationException {
    if (value == null) {
      return null;
    }
    return value;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    String propertyname = evt.getPropertyName();
    if (jDialog1 == null) {
      actionPerformed(null);
    }
    if (propertyname == null) {
      return;
    }
    else if (propertyname.equals(PROPERTYFILENAME)) {
      String filename = (String) evt.getNewValue();
      if(jDialog1!=null)jDialog1.hide();
      jDialog1 = new PageSelectionTableDialog(tool.getInternalFrame());
      jDialog1.show();
      jDialog1.addPropertyChangeListener(this);
      jDialog1.setDataModel(new PageTableModel(filename));
      jDialog1.setTitle(filename);
    }
    else if (propertyname.equals(PROPERTYPAGESELECTIONSTRING)) {
      String pageselectionstring = (String) evt.getNewValue();
      System.out.print(" Oldvalue:" + evt.getOldValue());
      System.out.println(" Newvalue:" + pageselectionstring);
      setValue(pageselectionstring, PROPERTYPAGESELECTIONSTRING);
    }
  }

  PageSelectionTableDialog jDialog1 = null;
}
