/*
 * MetaObject.java
 *
 * Created on October 28, 2001, 10:01 AM
 */

package com.lowagie.text.pdf.wmf;

/**
 *
 * @author  Administrator
 * @version 
 */
public class MetaObject {
    public final static int META_NOT_SUPPORTED = 0;
    public final static int META_PEN = 1;
    public final static int META_BRUSH = 2;
    public final static int META_FONT = 3;
    public int type = META_NOT_SUPPORTED;

    public MetaObject() {
    }

    public MetaObject(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }

}
