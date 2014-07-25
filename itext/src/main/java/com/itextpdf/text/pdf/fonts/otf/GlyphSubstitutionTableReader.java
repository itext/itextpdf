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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.pdf.Glyph;

/**
 * <p>
 * Parses an OpenTypeFont file and reads the Glyph Substitution Table. This table governs how two or more Glyphs should be merged
 * to a single Glyph. This is especially useful for Asian languages like Bangla, Hindi, etc.
 * </p>
 * <p>
 * This has been written according to the OPenTypeFont specifications. This may be found <a href="http://www.microsoft.com/typography/otspec/gsub.htm">here</a>.
 * </p>
 * 
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
public class GlyphSubstitutionTableReader extends OpenTypeFontTableReader {

    private final int[] glyphWidthsByIndex;
    private final Map<Integer, Character> glyphToCharacterMap;
    private Map<Integer, List<Integer>> rawLigatureSubstitutionMap;

    public GlyphSubstitutionTableReader(String fontFilePath, int gsubTableLocation, 
    		Map<Integer, Character> glyphToCharacterMap, int[] glyphWidthsByIndex) throws IOException {
        super(fontFilePath, gsubTableLocation);
        this.glyphWidthsByIndex = glyphWidthsByIndex;
        this.glyphToCharacterMap = glyphToCharacterMap;
    }
    
    public void read() throws FontReadingException { 
        rawLigatureSubstitutionMap = new LinkedHashMap<Integer, List<Integer>>();
        startReadingTable();
    }
    
    public Map<String, Glyph> getGlyphSubstitutionMap() throws FontReadingException {
        Map<String, Glyph> glyphSubstitutionMap = new LinkedHashMap<String, Glyph>();
        
        for (Integer glyphIdToReplace : rawLigatureSubstitutionMap.keySet()) {
            List<Integer> constituentGlyphs = rawLigatureSubstitutionMap.get(glyphIdToReplace);
            StringBuilder chars = new StringBuilder(constituentGlyphs.size());
            
            for (Integer constituentGlyphId : constituentGlyphs) {
                chars.append(getTextFromGlyph(constituentGlyphId, glyphToCharacterMap));
            }
            
            Glyph glyph = new Glyph(glyphIdToReplace, glyphWidthsByIndex[glyphIdToReplace], chars.toString());
            
            glyphSubstitutionMap.put(glyph.chars, glyph);
        }
        
        return Collections.unmodifiableMap(glyphSubstitutionMap);
    }
    
    private String getTextFromGlyph(int glyphId, Map<Integer, Character> glyphToCharacterMap) throws FontReadingException {
        
        StringBuilder chars = new StringBuilder(1);
        
        Character c = glyphToCharacterMap.get(glyphId);
        
        if (c == null) {
            // it means this represents a compound glyph
            List<Integer> constituentGlyphs = rawLigatureSubstitutionMap.get(glyphId);
            
            if (constituentGlyphs == null || constituentGlyphs.isEmpty()) {
                throw new FontReadingException("No corresponding character or simple glyphs found for GlyphID=" + glyphId);
            }
            
            for (int constituentGlyphId : constituentGlyphs) {
                chars.append(getTextFromGlyph(constituentGlyphId, glyphToCharacterMap));
            }
            
        } else {
            chars.append(c.charValue());
        }
        
        return chars.toString();
    }
    
    @Override
    protected void readSubTable(int lookupType, int subTableLocation) throws IOException {
        
        if (lookupType == 1) {
            readSingleSubstitutionSubtable(subTableLocation);
        } else if (lookupType == 4) {
            readLigatureSubstitutionSubtable(subTableLocation);
        } else {
            System.err.println("LookupType " + lookupType + " is not yet handled for " + GlyphSubstitutionTableReader.class.getSimpleName());
        }
        
    }

    /**
     * LookupType 1: Single Substitution Subtable
     */
    private void readSingleSubstitutionSubtable(int subTableLocation) throws IOException { 
        rf.seek(subTableLocation);
        
        int substFormat = rf.readShort();
        LOG.debug("substFormat=" + substFormat);
        
        if (substFormat == 1) {
            int coverage = rf.readShort();
            LOG.debug("coverage=" + coverage);
            
            int deltaGlyphID = rf.readShort();
            LOG.debug("deltaGlyphID=" + deltaGlyphID);
            
            List<Integer> coverageGlyphIds = readCoverageFormat(subTableLocation + coverage);
            
            for (int coverageGlyphId : coverageGlyphIds) {
                int substituteGlyphId = coverageGlyphId + deltaGlyphID;
                rawLigatureSubstitutionMap.put(substituteGlyphId, Arrays.asList(coverageGlyphId)); 
            }
        } else if (substFormat == 2) {
            System.err.println("LookupType 1 :: substFormat 2 is not yet handled by " + GlyphSubstitutionTableReader.class.getSimpleName());
        } else {
            throw new IllegalArgumentException("Bad substFormat: " + substFormat);
        }
    }

    /**
     * LookupType 4: Ligature Substitution Subtable
     */
    private void readLigatureSubstitutionSubtable(int ligatureSubstitutionSubtableLocation) throws IOException {
        rf.seek(ligatureSubstitutionSubtableLocation);
        int substFormat = rf.readShort();
        LOG.debug("substFormat=" + substFormat);

        if (substFormat != 1) {
            throw new IllegalArgumentException("The expected SubstFormat is 1");
        }

        int coverage = rf.readShort();
        LOG.debug("coverage=" + coverage);

        int ligSetCount = rf.readShort();

        List<Integer> ligatureOffsets = new ArrayList<Integer>(ligSetCount);

        for (int i = 0; i < ligSetCount; i++) {
            int ligatureOffset = rf.readShort();
            ligatureOffsets.add(ligatureOffset);
        }

        List<Integer> coverageGlyphIds = readCoverageFormat(ligatureSubstitutionSubtableLocation + coverage);

        if (ligSetCount != coverageGlyphIds.size()) {
            throw new IllegalArgumentException("According to the OpenTypeFont specifications, the coverage count should be equal to the no. of LigatureSetTables");
        }

        for (int i = 0; i < ligSetCount; i++) {

            int coverageGlyphId = coverageGlyphIds.get(i);
            int ligatureOffset = ligatureOffsets.get(i);
            LOG.debug("ligatureOffset=" + ligatureOffset);
            readLigatureSetTable(ligatureSubstitutionSubtableLocation + ligatureOffset, coverageGlyphId);
        }

    }

    private void readLigatureSetTable(int ligatureSetTableLocation, int coverageGlyphId) throws IOException {
        rf.seek(ligatureSetTableLocation);
        int ligatureCount = rf.readShort();
        LOG.debug("ligatureCount=" + ligatureCount);

        List<Integer> ligatureOffsets = new ArrayList<Integer>(ligatureCount);

        for (int i = 0; i < ligatureCount; i++) {
            int ligatureOffset = rf.readShort();
            ligatureOffsets.add(ligatureOffset);
        }

        for (int ligatureOffset : ligatureOffsets) {
            readLigatureTable(ligatureSetTableLocation + ligatureOffset, coverageGlyphId);
        }
    }

    private void readLigatureTable(int ligatureTableLocation, int coverageGlyphId) throws IOException {
        rf.seek(ligatureTableLocation);
        int ligGlyph = rf.readShort();
        LOG.debug("ligGlyph=" + ligGlyph);
        
        int compCount = rf.readShort();

        List<Integer> glyphIdList = new ArrayList<Integer>();

        glyphIdList.add(coverageGlyphId);

        for (int i = 0; i < compCount - 1; i++) {
            int glyphId = rf.readShort();
            glyphIdList.add(glyphId);
        }
        
        LOG.debug("glyphIdList=" + glyphIdList);

        List<Integer> previousValue = rawLigatureSubstitutionMap.put(ligGlyph, glyphIdList);
        
        if (previousValue != null) {
        	LOG.warn("!!!!!!!!!!glyphId=" + ligGlyph 
        			+ ",\npreviousValue=" + previousValue
        			+ ",\ncurrentVal=" + glyphIdList);
        }
    }

}
