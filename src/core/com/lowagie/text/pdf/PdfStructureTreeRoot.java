/*
 * $Id$
 *
 * Copyright 2005 by Paulo Soares.
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
package com.lowagie.text.pdf;

import java.io.IOException;
import java.util.*;

/**
 * The structure tree root corresponds to the highest hierarchy level in a tagged PDF.
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfStructureTreeRoot extends PdfStructureBase {

    //private HashMap<Integer, PdfObject> parentTree = new HashMap<Integer, PdfObject>();
    private HashMap parentTree = new HashMap();
    //private Map pageMCIDs<Integer, Integer> = new HashMap<Integer, Integer>();
    private Map pageMCIDs = new HashMap();
    private int nextMark = 0;
    private PdfWriter writer;

    /**
     *
     * @param writer a VALID PdfWriter.  Or else.
     */
    PdfStructureTreeRoot(PdfWriter writer) {
        super(PdfName.STRUCTTREEROOT);
        if (writer == null) {
            throw new NullPointerException( "PdfWriter param must not be null" );
        }
        this.writer = writer;
        setIndRef( writer.getPdfIndirectReference() );
    }

    /**
     * Maps the user tags to the standard tags. The mapping will allow a standard application to make some sense of the tagged
     * document whatever the user tags may be.
     * @param used the user tag
     * @param standard the standard tag
     */
    public void mapRole(PdfName used, PdfName standard) {
        PdfDictionary rm = (PdfDictionary)get(PdfName.ROLEMAP);
        if (rm == null) {
            rm = new PdfDictionary();
            put(PdfName.ROLEMAP, rm);
        }
        rm.put(used, standard);
    }

    /**
     * Gets the writer.
     * @return You'll never guess.
     * @since 2.1.5
     */
    public PdfWriter getWriter() {
        return writer;
    }

    /**
     * returns the next Marked Content ID.
     * @since 2.1.5
     * @return the next top-level MCID
     */
    public int getNextMCID() {
        return nextMark++;
    }


    /**
     * Sets the page's MCID.  "Pages" in the name is possessive, not plural.
     * @param pageIdx That page thing we were just talking about.
     * @param pageMCID  The MCID for the page...  Do try and keep up.
     * @since 2.1.5
     */
    public void setPagesMCID( int pageIdx, int pageMCID ) {
      Integer idxObj = new Integer( pageIdx );
      if (!pageMCIDs.containsKey( idxObj )) {
        pageMCIDs.put( idxObj, new Integer( pageMCID ) );
        parentTree.put( idxObj, new PdfArray() );
      }
    }

    /**
     * will NPE if pageIdx is unknown
     * @param pageIdx
     * @return the MCID for the given page
     * @since 2.1.5
     */
    public int getMCIDForPage( int pageIdx ) {
      return ((Integer)pageMCIDs.get( new Integer( pageIdx ) )).intValue();
      //return pageMCIDs.get( pageIdx );
    }

    /**
     * retrieves the existing MCID for a given page.
     * @param pageIdx
     * @return the MCID for the given page
     * @since 2.1.5
     */
    public Integer getMCIDForPage( Integer pageIdx ) {
        return (Integer)pageMCIDs.get( pageIdx );
    }

    /**
     * Adds a marked content item to the given page.  It returns the index
     * into that page's marked content array... the MCID (which isn't a unique
     * ID, and has no bearing on reading order)
     * @param pageIdx durh
     * @param struc An indirect reference to the *structure element*
     * @return MCID (aka index) for the referenced StructureElement
     * @since 2.1.5
     */
    public int setPageMark(int pageIdx, PdfIndirectReference struc) {
        Integer i = (Integer)pageMCIDs.get( new Integer( pageIdx ) );
        //Integer i = new Integer( pageMCIDs.get( pageIdx ) );
        PdfArray ar = (PdfArray)parentTree.get( i );
        ar.add(struc);
        return ar.size() - 1;
    }

    /**
     * Adds a structure element that is not part of a content item (annots & such)
     * @param objID
     * @param strucRef
     * @since 2.1.5
     */
    public void setObjMark( int objID, PdfIndirectReference strucRef) {
      Integer i = new Integer( objID );
      parentTree.put( i, strucRef );
    }

    private void nodeProcess(PdfDictionary struc, PdfIndirectReference reference) throws IOException {
        PdfObject obj = struc.get(PdfName.K);
        if (obj != null && obj.isArray() && !((PdfObject)((PdfArray)obj).getArrayList().get(0)).isNumber()) {
            PdfArray ar = (PdfArray)obj;
            ArrayList a = ar.getArrayList();
            for (int k = 0; k < a.size(); ++k) {
                PdfStructureElement e = (PdfStructureElement)a.get(k);
                a.set(k, e.getIndRef());
                nodeProcess(e, e.getIndRef());
            }
        }
        if (reference != null)
            writer.addToBody(struc, reference);
    }

    void buildTree() throws IOException {
        HashMap numTree = new HashMap();
        for (Iterator it = parentTree.keySet().iterator(); it.hasNext();) {
            Integer i = (Integer)it.next();
            PdfObject obj = (PdfObject)parentTree.get( i );
            if (obj.isIndirect()) {
                numTree.put( i, obj );
            } else {
                numTree.put(i, writer.addToBody(obj).getIndirectReference());
            }
        }
        PdfDictionary dicTree = PdfNumberTree.writeTree(numTree, writer);
        if (dicTree != null)
            put(PdfName.PARENTTREE, writer.addToBody(dicTree).getIndirectReference());

        nodeProcess(this, getIndRef() );

        put( PdfName.PARENTTREENEXTKEY, new PdfNumber( nextMark ) );
    }
}
