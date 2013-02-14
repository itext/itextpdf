package com.itextpdf.text.pdf;

/**
 * Created by IntelliJ IDEA.
 * User: denis.koleda
 * Date: 12/21/12
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class PdfPHeaderCell extends PdfPCell{

    // static member variables for the different styles

    /** this is a possible style. */
    public static final int NONE = 0;

    /** this is a possible style. */
    public static final int ROW = 1;

    /** this is a possible style. */
    public static final int COLUMN = 2;

    /** this is a possible style. */
    public static final int BOTH = 3;

    
    protected int scope = NONE;

    public PdfPHeaderCell(){
        super();
        role = PdfName.TH;
    }

    public PdfPHeaderCell(PdfPHeaderCell headerCell){
        super(headerCell);
        role = headerCell.role;
        scope = headerCell.scope;
        name = headerCell.getName();
    }

    protected String name = null;
    
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
    
    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

    public void setScope(int scope){
        this.scope = scope;
    }

    public int getScope(){
        return scope;
    }
}
