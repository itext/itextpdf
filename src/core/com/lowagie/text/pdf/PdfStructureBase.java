/***********************************************************************
 * This file contains proprietary information of Cardiff.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2000-2008 Cardiff. All rights reserved.
 ***********************************************************************/

package com.lowagie.text.pdf;

/**********************************************************************
 *
 * The <code>PdfStructureBase</code> class, which handles basic common
 * functionality between PdfStructureElement and PdfStructureTreeRoot.
 *
 * Not for public consumption.
 *
 * @author mstorer
 * @since 2.1.5
 **********************************************************************/

abstract class PdfStructureBase extends PdfDictionary {
  protected PdfArray kids; // null till first call to addKid

  public PdfStructureBase( PdfName dicType ) {
    super( dicType );
  }

  /**
   *  child class responsible for 'reference'
   */
  public PdfStructureBase() {
  }

  abstract protected PdfWriter getWriter();


  /**
   * Gets the reference this object will be written to.
   *
   * @return the reference this object will be written to
   */
  public PdfIndirectReference getIndRef() {
    if (super.getIndRef() == null) {
        setIndRef( getWriter().getPdfIndirectReference() );
    }
    return super.getIndRef();
  }

    /**
     * Keep in mind that reading order is determined solely by the order in which
     * kids are added... not by MCID.
     * @param kid a structure element that will be a child of 'this'.
     * @throws IllegalArgumentException if 'this' has a /k entry already that isn't for holding children
     */
  public void addKid( PdfStructureElement kid ) throws IllegalArgumentException {
    if (kids == null) {
      if (contains( PdfName.K )) {
        throw new IllegalArgumentException( "this structure object already has a 'K' value" );
      }
      kids = new PdfArray();
      put( PdfName.K, kids );
    }

    kids.add( kid );
  }
}

