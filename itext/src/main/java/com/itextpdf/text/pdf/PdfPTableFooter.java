package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PdfPTableFooter extends PdfPTableBody {

    protected PdfName role = PdfName.TFOOT;

    public PdfPTableFooter() {
        super();
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

}
