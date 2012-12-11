package com.itextpdf.text;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.HashMap;
import java.util.UUID;

public class ListLabel extends ListBody {

    protected PdfName role = PdfName.LBL;
    protected float indentation = 0;
    protected boolean tagLabelContent = true;

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

    /**
     * Gets the value of <code>tagLabelContent</code> property.
     * If the property is <code>true</code> it means that content of the list item lable will be tagged.
     * For example:
     * <code>
     * &lt;LI&gt;
     *     &lt;Lbl&gt;
     *         &lt;Span&gt;1.&lt;/Span&gt;
     *     &lt;/Lbl&gt;
     * &lt;/LI&gt;
     * </code>
     * If the property set to <code>false</code> it will look as follows:
     * <code>
     * &lt;LI&gt;
     *     &lt;Lbl&gt;1.&lt;/Lbl&gt;
     * &lt;/LI&gt;
     * @return
     */
    public boolean getTagLabelContent() {
        return tagLabelContent;
    }

    public void setTagLabelContent(boolean tagLabelContent) {
        this.tagLabelContent = tagLabelContent;
    }

}
