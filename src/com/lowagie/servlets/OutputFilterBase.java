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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletResponse;

/**
 *  <p>Base filter to enable filtering on the output of a resource, instead
 *  of the input. This filter calls the filter chain before calling the
 *  <p>perform()</p> method, making the output of the filter chain
 *  available to said method.</p>
 *
 *  @author     Marcelo Vanzin
 *  @created    18/Abr/2002
 */
public abstract class OutputFilterBase implements Filter {

    private final static String FILTER_APPLIED = "OUTPUTFILTER_APPLIED";
    
    /**
     *  Key where to store an object in the request if you do not want
     *  the output filter applied on this request.
     */
    public final static String PREVENT_FILTER = "OUTPUTFILTER_PREVENT";
    
    /**
     *  Where the exception caught when executing <i>perform()</i> is stored
     *  in the request (so that it can be used on customized error pages).
     */
    public final static String EXCEPTION_KEY = "OUTPUTFILTER_PREVENT";

    protected FilterConfig fConfig;

    /**
     *  <p>Prepares the filter before calling the filter chain. This enables
     *  the implementing class to provide customized <i>ServletRequest</i>
     *  and <i>ServletResponse</i> objects to the chain.</p>
     *
     *  <p>After calling the chain, if a object is found under the 
     *  "PREVENT_FILTER" key in the ServletRequest objeto, the output filter
     *  is not executed (that is, the <i>perform()</i> method is not called).
     *  If this situation is detected, the buffer received from the chain
     *  is copied to the original ServletResponse object.</p>
     *
     *  <p>Also, if the response has been committed (e.g., by calling sendError()
     *  on the response), the filter is not applied.</p>
     *
     *  <p>If an exception occurs during the execution of the <i>perform()</i>
     *  method, a <i>Internal Server Error</i> is sent to the response with
     *  the exception message. The exception is stored under the "EXCEPTION_KEY"
     *  key of the request.</p>
     *
     *  @param  request     The request from the filter chain (or from the browser,
     *                      if this is the first filter).
     *  @param  response    The response from the filter chain (or browser).
     *  @param  chain       The filter chain.
     */
    public final void doFilter(ServletRequest request,
                               ServletResponse response,
                               FilterChain chain)
        throws IOException, ServletException {
                  
        if (request.getAttribute(FILTER_APPLIED) == null) {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            
            ServletRequest req = prepareRequest(request);
            BufferedResponse res = prepareResponse(request, response);
            
            chain.doFilter(req, res);
            
            if (!res.isCommitted() && res.isModified()) {
                if (req.getAttribute(PREVENT_FILTER) == null) {
                    try {
                        perform(request, response, res.getData());
                    } catch (Exception e) {
                        sendError(request, (HttpServletResponse)response, e);
                    }
                } else {
                    dump(res, response);
                }
            }
        }

    }
    
    /**
     *  <p>This method is called before executing the filter chain. It should
     *  return a ServletRequest object to be provided to the chain. The default
     *  implementation just returns the request object passed.</p>
     *
     *  @param  request     The request received from the filter chain.
     *  @return A ServletRequest to be sent to the filter chain.
     */
    protected ServletRequest prepareRequest(ServletRequest request) {
        return request;
    }
    
    /**
     *  <p>This method is called before executing the filter chain, and should
     *  return a BufferedResponse objeto to be sent to the chain. 
     *  This implementation uses the default BufferedResponse implementation,
     *  that stores data received from the resources called from the chain
     *  in a memory buffer.</p>
     *
     *  @param  request     The request from the filter chain.
     *  @param  response    The response from the filter chain.
     *  @return An instance of BufferedResponse that wraps the original 
     *          response object.
     */
    protected BufferedResponse prepareResponse(ServletRequest request,
                                               ServletResponse response)
        throws IOException {
        return new BufferedResponse((HttpServletResponse)response);
    }
    
    /**
     *  <p>This method should contain the logic to manipulate the data coming
     *  from the resources called by the filter chain and send the data to
     *  the response object received.</p>
     *
     *  @param  request     The original request received from the chain.
     *  @param  response    The original response received from the chain.
     *  @param  data        An InputStream containing the buffer that stores
     *                      the data from the called resources.
     */
    protected abstract void perform(ServletRequest request, 
                                    ServletResponse response,
                                    InputStream data) 
        throws Exception;

        
    /**
     *  <p>Dumps the contents of the passed BufferedResponse to the 
     *  ServletResponse. Before dumping the data itself, sets the original
     *  content type and the original content length.</p>
     *
     *  @param  data    A BufferedResponse object.
     *  @param  res     A ServletResponse where to dump the data to.
     */
    protected void dump(BufferedResponse data, ServletResponse res) 
        throws IOException {
        
        res.setContentType(data.getDeclaredContentType());
        
        if (data.getDeclaredLength() > 0) {
            res.setContentLength(data.getDeclaredLength());
        }
        
        dump(data.getData(), res);
    }
        
    /**
     *  <p>Dumps the data from the InputStream passed to the ServletResponse's
     *  OutputStream.</p>
     *
     *  @param  data    An input stream.
     *  @param  res     A ServletResponse where to dump the data to.
     */
    protected void dump(InputStream in, ServletResponse res) 
        throws IOException {
        
        OutputStream out = res.getOutputStream();
        int i;
        
        while ((i = in.read()) != -1) {
            out.write(i);
        }
        
    }
    
    /**
     *  <p>Stores the throwable in the EXCEPTION_KEY attribute of the request
     *  and sends an "Internal Server Error" to the response with the
     *  message from the error.</p>
     */
    protected void sendError(ServletRequest req, 
                             HttpServletResponse res,
                             Throwable t) throws IOException {
        req.setAttribute(EXCEPTION_KEY, t);
        res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t.getMessage());
    }

    // Method needed to implement the "Filter" interface
    
    public void init(FilterConfig filterConfig) throws ServletException {
        fConfig = filterConfig;
    }
    
    public void destroy() {
        fConfig = null;
    }
    
    public FilterConfig getFilterConfig() {
        return fConfig;
    }
    
    public void setFilterConfig(FilterConfig filterConfig) {
        fConfig = filterConfig;
    }

}

