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

/**
 * This is a node in a document logical structure. It may contain a mark point or it may contain
 * other nodes.
 * @author Paulo Soares (psoares@consiste.pt)
 *
 * Something to keep in mind about MCIDs.  They're not really IDentifiers.  They're
 * InDexes into their container(a page or annotation at the moment)'s kid array.  The name has lead
 * to misconceptions that can be found across several popular PDF products (including
 * LifeCycle Designer 8).
 */
public class PdfStructureElement extends PdfStructureBase {
    // may be null if this element is directly attached to the TreeRoot
    private PdfStructureElement parent;
    private PdfStructureTreeRoot top;
    // either kids or MCID must be null
    private Integer mcid; // marked content ID.
    private boolean hasPageMark = false;

    /**
     * Creates a new instance of PdfStructureElement.
     * @param parent the parent of this node
     * @param structureType the type of structure. It may be a standard type or a user type mapped by the role map
     */
    public PdfStructureElement(PdfStructureElement parent, PdfName structureType) {
      top = parent.top;
      init( parent, structureType );
      this.parent = parent;
      put( PdfName.P, parent.getIndRef() );
    }

    /**
     * Constructs a new Element with a predetermined reference
     * @param parent
     * @param structType
     * @param ref
     * @since 2.1.5
     */
    public PdfStructureElement( PdfStructureElement parent, PdfName structType, PdfIndirectReference ref ) {
      top = parent.top;
      init( parent, structType, ref );
      this.parent = parent;
      put( PdfName.P, parent.getIndRef() );
    }

    /**
     * Creates a new instance of PdfStructureElement.
     * @param parent the parent of this node
     * @param structureType the type of structure. It may be a standard type or a user type mapped by the role map
     */
    public PdfStructureElement(PdfStructureTreeRoot parent, PdfName structureType) {
        top = parent;
        init(parent, structureType);
        put(PdfName.P, parent.getIndRef());
    }

    /**
     * Creates a new element and immediately assigns it the next MCID.
     * NOTE: MCIDs do NOT control reading order.  That's handled by parent/child
     * relationships of StructureElements alone.
     * @param parent Structure Thing to contain the new element
     * @param structureType durh.
     * @return the new element, with its freshly minted MCID already assigned.
     * NOTE: There is no version of this function that can use the TreeRoot as
     * its parent, because that would be silly.  Think about it.
     * @since 2.1.5
     */
    public static PdfStructureElement createNextElement( PdfStructureElement parent, PdfName structureType ) {
        PdfStructureElement elem = new PdfStructureElement( parent, structureType );
        elem.getMCID();
        return elem;
    }

    /**
     * Should never be null... TreeRoot would have already thrown in it's constructor.
     * @return  Gee, I wonder.
     * @since 2.1.5
     */
    public PdfWriter getWriter() {
        return top.getWriter();
    }

    private void init(PdfStructureBase parent, PdfName structureType) {
        init( parent, structureType, getWriter().getPdfIndirectReference() );
    }

    private void init( PdfStructureBase parent, PdfName structureType, PdfIndirectReference ref ) {
      parent.addKid( this );
      if (structureType != null) {
        put( PdfName.S, structureType );
      }
      setIndRef( ref );
    }

    /**
     * Gets the parent of this node.
     * @return the parent of this node
     */
    public PdfDictionary getParent() {
        return parent;
    }

    /**
     * Get/assign a marked content ID for this element.
     * WARNING: if this element has > 0 kids, calling this function will throw
     * @return the elements marked content ID.
     * @since 2.1.5
     */
    public int getMCID() {
        checkKids(); // throws if we're not a leaf node
        if (mcid == null) {
            mcid = new Integer( top.getNextMCID() );
        }

        return mcid.intValue();
    }

    /**
     * Will throw if this element has >0 kids
     * @param id MCID
     * @since 2.1.5
     */
    public void setMCID( int id ) {
        checkKids();

        if (mcid != null) {
            throw new RuntimeException( "this element already has an MCID" );
        }

        mcid = new Integer( id );
    }

    /**
     * sets this structure element to represent a particular section of marked content
     * within the page indicated by pageRef
     * @param pageRef the reference to a particular page
     * @since 2.1.5
     */
    public void setMarkedContent( PdfIndirectReference pageRef ) {
        checkKids();

        // if there's already a 'K', but checkKids didn't throw, then
        // this element is being used more than once.  Ergo, K is either a
        // PdfNumber, or an array.
        if (contains( PdfName.K) ) {
          PdfArray structIDs = null;
          PdfObject curKObj = get( PdfName.K );
          if (curKObj == null) {
              structIDs = new PdfArray();

              if (curKObj != null && curKObj.isNumber()) {
                  structIDs.add( curKObj );
              }
              put( PdfName.K, structIDs );
          } else if (curKObj.isArray()) {
              structIDs = (PdfArray) curKObj;
          } else {
              throw new IllegalArgumentException( "Unknown object at /K " + curKObj.getClass().toString() );
          }
          structIDs.add( new PdfStructureMC( getMCID(), pageRef ) );
        } else {
          // No existing 'k', so we can use a simple numeric value.
          put( PdfName.K,  new PdfNumber( getMCID() ) );
        }
    }

    /**
     * Sets this StructElem up as a marked object.
     * @param objRef required
     * @param pageRef may be null
     * @since 2.1.5
     */
    public void setMarkedObject( PdfIndirectReference objRef, PdfIndirectReference pageRef ) {
        checkKids();

        if (objRef == null) throw new NullPointerException( "object reference must be valid" );

        // for marked objects, the MCID is written to the object, not the structureObj
        PdfStructureObj structObj = new PdfStructureObj( objRef, pageRef );
        put( PdfName.K, structObj );

        setObjMark();
    }

    /**
     *
     * @param page
     * @since 2.1.5
     */
    void setPageMark( int page ) {
        checkKids();
        if (!hasPageMark) {
            mcid = new Integer( top.setPageMark( page, getIndRef() ) );
            hasPageMark = true;
        }
    }

    /**
     * @since 2.1.5
     */
    private void setObjMark() {
      top.setObjMark( getMCID(), getIndRef() );
    }

    /**
     * Throw if this structure element is being used as a parent
     * @since 2.1.5
     */
    private void checkKids() {
        if (kids != null) {
          throw new IllegalArgumentException( "This structure element already has children" );
        }
    }

    /**
     * A dictionary placed in the /K of a leaf structure element.
     * It represents an element with a particular MCID.  It can
     * be used to represent an element that appears on more than one page.
     * @since 2.1.5
     */
    class PdfStructureMC extends PdfDictionary {
        public PdfStructureMC( int id, PdfIndirectReference pageRef ) {
            super( PdfName.MCR );

            put( PdfName.MCID, new PdfNumber( id ) );
            if (pageRef != null) {
                put( PdfName.PG, pageRef );
            }
        }
    }

    /**
     * Another dictionary residing in the /K of a leaf element.
     * A PdfStructureObj indicates than an entire indirect object represents
     * the marked content in question.  Annotations and Form Fields are the
     * most common examples.  Again, a structObj can be used to represent
     * an object in the reading order that exists on more than one page.
     * @since 2.1.5
     */
    class PdfStructureObj extends PdfDictionary {
        public PdfStructureObj( PdfIndirectReference objRef, PdfIndirectReference pageRef ) {
            super( PdfName.OBJR );
            put( PdfName.OBJ, objRef );
            if (pageRef != null) {
                put( PdfName.PG, pageRef );
            }
        }
    }
}
