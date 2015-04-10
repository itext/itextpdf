/*
 * $Id: $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.fonts.otf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Not used at present, keeping for sometime future.
 * 
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
public class GlyphPositioningTableReader extends OpenTypeFontTableReader {
    
    public GlyphPositioningTableReader(String fontFilePath, int gposTableLocation) throws IOException {
        super(fontFilePath, gposTableLocation);
    }
    
    public void read() throws FontReadingException  {  
        startReadingTable();
    }
    
    @Override
    protected void readSubTable(int lookupType, int subTableLocation) throws IOException {
        
        if (lookupType == 1) {
            readLookUpType_1(subTableLocation);
        } else if (lookupType == 4) {
            readLookUpType_4(subTableLocation);
        } else if (lookupType == 8) {
        	readLookUpType_8(subTableLocation);
        } else {
        	System.err.println("The lookupType " + lookupType + " is not yet supported by " + GlyphPositioningTableReader.class.getSimpleName());   
        }
        
    }
    
    private void readLookUpType_1(int lookupTableLocation) throws IOException {
    	rf.seek(lookupTableLocation);
    	int posFormat = rf.readShort();
    	
    	if (posFormat == 1) {
    		LOG.debug("Reading `Look Up Type 1, Format 1` ....");
            int coverageOffset = rf.readShort();
            int valueFormat = rf.readShort();
//            LOG.debug("valueFormat=" + valueFormat); 
            
            //check if XPlacement should be read
            if  ((valueFormat & 1) == 1) {
            	int xPlacement = rf.readShort();
            	LOG.debug("xPlacement=" + xPlacement); 
            }
            
           //check if YPlacement should be read
            if  ((valueFormat & 2) ==2) {
            	int yPlacement = rf.readShort();
            	LOG.debug("yPlacement=" + yPlacement); 
            }
            
            List<Integer> glyphCodes = readCoverageFormat(lookupTableLocation + coverageOffset);
            
            LOG.debug("glyphCodes=" + glyphCodes); 
    	} else {
    		System.err.println("The PosFormat " + posFormat + " for `LookupType 1` is not yet supported by " + GlyphPositioningTableReader.class.getSimpleName());
    	}
        
    }
    
    private void readLookUpType_4(int lookupTableLocation) throws IOException {
        rf.seek(lookupTableLocation);
        
        int posFormat = rf.readShort();
        
        if (posFormat == 1) {
        	
        	LOG.debug("Reading `Look Up Type 4, Format 1` ....");
        	
            int markCoverageOffset = rf.readShort();
            int baseCoverageOffset = rf.readShort();
            int classCount = rf.readShort();
            int markArrayOffset = rf.readShort();
            int baseArrayOffset = rf.readShort();
            
            List<Integer> markCoverages = readCoverageFormat(lookupTableLocation + markCoverageOffset);
            LOG.debug("markCoverages=" + markCoverages);
            
            List<Integer> baseCoverages = readCoverageFormat(lookupTableLocation + baseCoverageOffset);
            LOG.debug("baseCoverages=" + baseCoverages);
            
            readMarkArrayTable(lookupTableLocation + markArrayOffset);
            
            readBaseArrayTable(lookupTableLocation + baseArrayOffset, classCount);
        } else {
        	System.err.println("The posFormat " + posFormat + " is not supported by " + GlyphPositioningTableReader.class.getSimpleName());
        }
    }
    
    private void readLookUpType_8(int lookupTableLocation) throws IOException {
        rf.seek(lookupTableLocation);
        
        int posFormat = rf.readShort();
        
        if (posFormat == 3) {
        	LOG.debug("Reading `Look Up Type 8, Format 3` ....");
        	readChainingContextPositioningFormat_3(lookupTableLocation);
        } else {
        	System.err.println("The posFormat " + posFormat + " for `Look Up Type 8` is not supported by " + GlyphPositioningTableReader.class.getSimpleName());
        }
    }
    
    private void readChainingContextPositioningFormat_3(int lookupTableLocation) throws IOException {
        int backtrackGlyphCount = rf.readShort();
        LOG.debug("backtrackGlyphCount=" + backtrackGlyphCount); 
        List<Integer> backtrackGlyphOffsets =  new ArrayList<Integer>(backtrackGlyphCount);
        
        for (int i = 0; i < backtrackGlyphCount; i++) {
        	int backtrackGlyphOffset = rf.readShort();
        	backtrackGlyphOffsets.add(backtrackGlyphOffset);
        }
        
        int inputGlyphCount = rf.readShort();
        LOG.debug("inputGlyphCount=" + inputGlyphCount); 
        List<Integer>inputGlyphOffsets =  new ArrayList<Integer>(inputGlyphCount);
        
        for (int i = 0; i < inputGlyphCount; i++) {
        	int inputGlyphOffset = rf.readShort();
        	inputGlyphOffsets.add(inputGlyphOffset);
        }
        
        int lookaheadGlyphCount = rf.readShort();
        LOG.debug("lookaheadGlyphCount=" + lookaheadGlyphCount); 
        List<Integer>lookaheadGlyphOffsets =  new ArrayList<Integer>(lookaheadGlyphCount);
        
        for (int i = 0; i < lookaheadGlyphCount; i++) {
        	int lookaheadGlyphOffset = rf.readShort();
        	lookaheadGlyphOffsets.add(lookaheadGlyphOffset);
        }
        
        int posCount = rf.readShort();
        LOG.debug("posCount=" + posCount);
        
        List<PosLookupRecord> posLookupRecords = new ArrayList<PosLookupRecord>(posCount);
        
        for (int i = 0; i < posCount; i++) {
        	int sequenceIndex  = rf.readShort();
        	int lookupListIndex  = rf.readShort();
        	LOG.debug("sequenceIndex=" + sequenceIndex + ", lookupListIndex=" + lookupListIndex); 
        	posLookupRecords.add(new PosLookupRecord(sequenceIndex, lookupListIndex));
        }
        
        for (int backtrackGlyphOffset : backtrackGlyphOffsets) {
        	List<Integer> backtrackGlyphs = readCoverageFormat(lookupTableLocation + backtrackGlyphOffset);
        	LOG.debug("backtrackGlyphs=" + backtrackGlyphs);
        }
        
        for (int inputGlyphOffset : inputGlyphOffsets) {
        	List<Integer> inputGlyphs = readCoverageFormat(lookupTableLocation + inputGlyphOffset);
        	LOG.debug("inputGlyphs=" + inputGlyphs);
        }
        
        for (int lookaheadGlyphOffset : lookaheadGlyphOffsets) {
        	List<Integer> lookaheadGlyphs = readCoverageFormat(lookupTableLocation + lookaheadGlyphOffset);
        	LOG.debug("lookaheadGlyphs=" + lookaheadGlyphs);
        }

    }
    
    private void readMarkArrayTable(int markArrayLocation) throws IOException {
    	rf.seek(markArrayLocation);
    	int markCount = rf.readShort();
    	List<MarkRecord> markRecords = new ArrayList<GlyphPositioningTableReader.MarkRecord>();
    	
    	for (int i = 0; i < markCount; i++) {
    		markRecords.add(readMarkRecord());
    	}
    	
    	for (MarkRecord markRecord : markRecords) {
    		readAnchorTable(markArrayLocation + markRecord.markAnchorOffset);
    	}
    }
    
    private MarkRecord readMarkRecord() throws IOException {
    	int markClass = rf.readShort();
    	int markAnchorOffset = rf.readShort();
    	return new MarkRecord(markClass, markAnchorOffset);
    }
    
    private void readAnchorTable(int anchorTableLocation) throws IOException {
    	rf.seek(anchorTableLocation);
    	int anchorFormat = rf.readShort();
    	
    	if  (anchorFormat != 1) {
    		System.err.println("The extra features of the AnchorFormat " + anchorFormat + " will not be used");
    	}
    	
    	int x = rf.readShort();
    	int y = rf.readShort();
    	
    }
    
    private void readBaseArrayTable(int baseArrayTableLocation, int classCount) throws IOException {
    	rf.seek(baseArrayTableLocation);
    	int baseCount = rf.readShort();
    	Set<Integer> baseAnchors = new HashSet<Integer>();
    	
    	for (int i = 0; i < baseCount; i++) {
    		//read BaseRecord
    		for (int k = 0; k < classCount; k++) {
    			int baseAnchor = rf.readShort();
    			baseAnchors.add(baseAnchor);
    		}
    	}
    	
//    	LOG.debug(baseAnchors.size()); 
    	
    	for (int baseAnchor : baseAnchors) {
    		readAnchorTable(baseArrayTableLocation + baseAnchor);
    	}
    	
    }
    
    static class MarkRecord {
    	final int markClass;
    	final int markAnchorOffset;
    	
		public MarkRecord(int markClass, int markAnchorOffset) {
			this.markClass = markClass;
			this.markAnchorOffset = markAnchorOffset;
		}
		
    }
    
    static class PosLookupRecord {
    	final int sequenceIndex;
    	final int lookupListIndex;
    	
		public PosLookupRecord(int sequenceIndex, int lookupListIndex) {
			this.sequenceIndex = sequenceIndex;
			this.lookupListIndex = lookupListIndex;
		}
		
    }

}
