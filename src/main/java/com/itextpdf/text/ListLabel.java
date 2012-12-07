package com.itextpdf.text;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.HashMap;
import java.util.UUID;

public class ListLabel extends ListBody {

    protected PdfName role = PdfName.LBL;
    protected float indentation = 0;

    protected ListLabel(final ListItem parentItem) {
        super(parentItem);
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

    public float getIndentation() {
        return indentation;
    }

    public void setIndentation(float indentation) {
        this.indentation = indentation;
    }

}
