/*
 * $Name$
 * $Id$
 *
 * Copyright 2002 by Marcelo Vanzin.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.servlets;


//Import General
import java.io.Writer;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *  <p>Wraps an HttpServletResponse instance, filtering any attempt to write
 *  or flush its output. The data written to the response is store in a
 *  memory buffer.</p>
 *
 *  @author     Marcelo Vanzin
 *  @created    18/Abr/2002
 */
public class BufferedResponse extends HttpServletResponseWrapper {

    private ByteArrayServletStream out;
    private PrintWriter wOut;

    private int declaredLength;
    private boolean isModified;
    private String declaredContentType;
    
    /**
     *  <p>Constructs a new BufferedResponse, wrapping the response passed.</p>
     *
     *  @param  response    The original response from the filter chain..
     */
    public BufferedResponse(HttpServletResponse response) throws IOException {
        super(response);
        out = new ByteArrayServletStream();
        wOut = new PrintWriter(out);
        declaredLength = 0;
        isModified = true;
    }
    
    /** <p>Returns a OutputStream to the memory buffer.</p> */
    public ServletOutputStream getOutputStream() throws IOException {
        return out;
    }
    
    /** <p>Returns a PrintWriter to the memory buffer.</p> */
    public PrintWriter getWriter() throws IOException {
        return wOut;
    }
    
    /**
     *  <p>Returns a InputStream with the data currently stored in the
     *  memory buffer..</p> 
     */
    public InputStream getData() throws IOException {
        return new ByteArrayInputStream(out.toByteArray());
    }
    
    /** 
     *  <p>Does not let the content length be set, but stores it in a
     *  variable in case it is needed later.</p> 
     */
    public void setContentLength(int length) {
        this.declaredLength = length;
    }

    /** <p>Returns the content length declared by the called resource.</p> */
    public int getDeclaredLength() {
        return declaredLength;
    }
    
    /** 
     *  <p>Does not let the content type be set, but stores it in a
     *  variable in case it is needed later.</p> 
     */
    public void setContentType(String type) {
        this.declaredContentType = type;
    }
    
    /** Returns the content type declared by the called resource. */
    public String getDeclaredContentType() {
        return declaredContentType;
    }
    
    /** 
     *  <p>If the status set if SC_NOT_MODIFIED, sets a flag so that the
     *  output filter knows that the buffer may be empty. This is to prevent
     *  errors when a static resource is wrapped by the filter and the
     *  containers returns an empty buffer if the resource has not been
     *  modified.</p>
     */
    public void setStatus(int sc) {
        if (sc == HttpServletResponse.SC_NOT_MODIFIED) {
            isModified = false;
        }
        super.setStatus(sc);
    }
    
    /** Says if the resource called has been modified or not. */
    public boolean isModified() {
        return isModified;
    }
    
    /**
     *  Filters calls to flushBuffer() so that no data is written to the
     *  response before executing the output filter.
     */
    public void flushBuffer() throws IOException { 
        // Nothing to do here.
    }
    
    /**
     *  Implements a ServletOutputStream that writes to a memory buffer.
     */
    private static class ByteArrayServletStream extends ServletOutputStream {
        
        private ByteArrayOutputStream data;
        
        public ByteArrayServletStream() {
            data = new ByteArrayOutputStream();
        }
        
        public void write( byte[] b, int o, int l ) throws IOException {
            this.data.write( b, o, l );
        }

        public void write( int i ) throws IOException {
            this.data.write( i );
        }

        public void flush() throws IOException {
            this.data.flush();
        }

        public void close() throws IOException {
            this.data.close();
        }
        
        public byte[] toByteArray() throws IOException {
            this.data.flush();
            return data.toByteArray();
        }
        
    }
}