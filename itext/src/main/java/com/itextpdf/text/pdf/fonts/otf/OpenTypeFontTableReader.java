/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * 
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
public abstract class OpenTypeFontTableReader {

	protected static final Logger LOG = LoggerFactory
			.getLogger(OpenTypeFontTableReader.class);

	protected final RandomAccessFileOrArray rf;
	protected final int tableLocation;
	
	private List<String> supportedLanguages;

	public OpenTypeFontTableReader(RandomAccessFileOrArray rf, int tableLocation)
			throws IOException {
		this.rf = rf;
		this.tableLocation = tableLocation;
	}
	
	public Language getSupportedLanguage() throws FontReadingException { 
		
		Language[] allLangs = Language.values();
		
		for (String supportedLang : supportedLanguages) {
			for (Language lang : allLangs) {
				if (lang.isSupported(supportedLang)) {
					return lang;
				}
			}
		}
		
		throw new FontReadingException("Unsupported languages " + supportedLanguages); 
	}

	/**
	 * This is the starting point of the class. A sub-class must call this
	 * method to start getting call backs to the {@link #readSubTable(int, int)}
	 * method.
	 * @throws FontReadingException 
	 */
	protected final void startReadingTable() throws FontReadingException {
		try {
			TableHeader header = readHeader();
			// read the Script tables
			readScriptListTable(tableLocation + header.scriptListOffset);

			// read Feature table
			readFeatureListTable(tableLocation + header.featureListOffset);

			// read LookUpList table
			readLookupListTable(tableLocation + header.lookupListOffset);
		} catch (IOException e) {
			throw new FontReadingException("Error reading font file", e);
		}
	}

	protected abstract void readSubTable(int lookupType, int subTableLocation)
			throws IOException;

	private void readLookupListTable(int lookupListTableLocation)
			throws IOException {
		rf.seek(lookupListTableLocation);
		int lookupCount = rf.readShort();

		List<Integer> lookupTableOffsets = new ArrayList<Integer>();

		for (int i = 0; i < lookupCount; i++) {
			int lookupTableOffset = rf.readShort();
			lookupTableOffsets.add(lookupTableOffset);
		}
		
		// read LookUp tables
		for (int i = 0; i < lookupCount; i++) {
//			LOG.debug("#############lookupIndex=" + i);
			int lookupTableOffset = lookupTableOffsets.get(i);
			readLookupTable(lookupListTableLocation + lookupTableOffset);
		}
		
	}

	private void readLookupTable(int lookupTableLocation) throws IOException {
		rf.seek(lookupTableLocation);
		int lookupType = rf.readShort();
		// LOG.debug("lookupType=" + lookupType);

		// skip 2 bytes for the field `lookupFlag`
		rf.skipBytes(2);

		int subTableCount = rf.readShort();
		// LOG.debug("subTableCount=" + subTableCount);

		List<Integer> subTableOffsets = new ArrayList<Integer>();

		for (int i = 0; i < subTableCount; i++) {
			int subTableOffset = rf.readShort();
			subTableOffsets.add(subTableOffset);
		}

		for (int subTableOffset : subTableOffsets) {
			// LOG.debug("subTableOffset=" + subTableOffset);
			readSubTable(lookupType, lookupTableLocation + subTableOffset);
		}
	}

	protected final List<Integer> readCoverageFormat(int coverageLocation)
			throws IOException {
		rf.seek(coverageLocation);
		int coverageFormat = rf.readShort();

		List<Integer> glyphIds;

		if (coverageFormat == 1) {
			int glyphCount = rf.readShort();

			glyphIds = new ArrayList<Integer>(glyphCount);

			for (int i = 0; i < glyphCount; i++) {
				int coverageGlyphId = rf.readShort();
				glyphIds.add(coverageGlyphId);
			}

		} else if (coverageFormat == 2) {

			int rangeCount = rf.readShort();

			glyphIds = new ArrayList<Integer>();

			for (int i = 0; i < rangeCount; i++) {
				readRangeRecord(glyphIds);
			}

		} else {
			throw new UnsupportedOperationException("Invalid coverage format: "
					+ coverageFormat);
		}

		return Collections.unmodifiableList(glyphIds);
	}

	private void readRangeRecord(List<Integer> glyphIds) throws IOException {
		int startGlyphId = rf.readShort();
		int endGlyphId = rf.readShort();
		int startCoverageIndex = rf.readShort();
		
		for (int glyphId = startGlyphId; glyphId <= endGlyphId; glyphId++) {
			glyphIds.add(glyphId);
		}
		
//		LOG.debug("^^^^^^^^^Coverage Format 2.... " 
//				+ "startGlyphId=" + startGlyphId
//				+ ", endGlyphId=" + endGlyphId
//				+ ", startCoverageIndex=" + startCoverageIndex 
//				+ "\n, glyphIds" + glyphIds);

	}

	private void readScriptListTable(int scriptListTableLocationOffset)
			throws IOException {
		rf.seek(scriptListTableLocationOffset);
		// Number of ScriptRecords
		int scriptCount = rf.readShort();

		Map<String, Integer> scriptRecords = new HashMap<String, Integer>(
				scriptCount);

		for (int i = 0; i < scriptCount; i++) {
			readScriptRecord(scriptListTableLocationOffset, scriptRecords);
		}
		
		List<String> supportedLanguages = new ArrayList<String>(scriptCount);

		for (String scriptName : scriptRecords.keySet()) {
			readScriptTable(scriptRecords.get(scriptName));
			supportedLanguages.add(scriptName);
		}
		
		this.supportedLanguages = Collections.unmodifiableList(supportedLanguages);
	}

	private void readScriptRecord(final int scriptListTableLocationOffset,
			Map<String, Integer> scriptRecords) throws IOException {
		String scriptTag = rf.readString(4, "utf-8");

		int scriptOffset = rf.readShort();

		scriptRecords.put(scriptTag, scriptListTableLocationOffset
				+ scriptOffset);
	}

	private void readScriptTable(final int scriptTableLocationOffset)
			throws IOException {
		rf.seek(scriptTableLocationOffset);
		int defaultLangSys = rf.readShort();
		int langSysCount = rf.readShort();

		if (langSysCount > 0) {
			Map<String, Integer> langSysRecords = new LinkedHashMap<String, Integer>(
					langSysCount);

			for (int i = 0; i < langSysCount; i++) {
				readLangSysRecord(langSysRecords);
			}

			// read LangSys tables
			for (String langSysTag : langSysRecords.keySet()) {
				readLangSysTable(scriptTableLocationOffset
						+ langSysRecords.get(langSysTag));
			}
		}

		// read default LangSys table
		readLangSysTable(scriptTableLocationOffset + defaultLangSys);
	}

	private void readLangSysRecord(Map<String, Integer> langSysRecords)
			throws IOException {
		String langSysTag = rf.readString(4, "utf-8");
		int langSys = rf.readShort();
		langSysRecords.put(langSysTag, langSys);
	}

	private void readLangSysTable(final int langSysTableLocationOffset)
			throws IOException {
		rf.seek(langSysTableLocationOffset);
		int lookupOrderOffset = rf.readShort();
		LOG.debug("lookupOrderOffset=" + lookupOrderOffset);
		int reqFeatureIndex = rf.readShort();
		LOG.debug("reqFeatureIndex=" + reqFeatureIndex);
		int featureCount = rf.readShort();

		List<Short> featureListIndices = new ArrayList<Short>(featureCount);
		for (int i = 0; i < featureCount; i++) {
			featureListIndices.add(rf.readShort());
		}

		LOG.debug("featureListIndices=" + featureListIndices);

	}

	private void readFeatureListTable(final int featureListTableLocationOffset)
			throws IOException {
		rf.seek(featureListTableLocationOffset);
		int featureCount = rf.readShort();
		LOG.debug("featureCount=" + featureCount);

		Map<String, Short> featureRecords = new LinkedHashMap<String, Short>(
				featureCount);
		for (int i = 0; i < featureCount; i++) {
			featureRecords.put(rf.readString(4, "utf-8"), rf.readShort());
		}

		for (String featureName : featureRecords.keySet()) {
			LOG.debug("*************featureName=" + featureName);
			readFeatureTable(featureListTableLocationOffset
					+ featureRecords.get(featureName));
		}

	}

	private void readFeatureTable(final int featureTableLocationOffset)
			throws IOException {
		rf.seek(featureTableLocationOffset);
		int featureParamsOffset = rf.readShort();
		LOG.debug("featureParamsOffset=" + featureParamsOffset);

		int lookupCount = rf.readShort();
		LOG.debug("lookupCount=" + lookupCount);

		List<Short> lookupListIndices = new ArrayList<Short>(lookupCount);
		for (int i = 0; i < lookupCount; i++) {
			lookupListIndices.add(rf.readShort());
		}

//		LOG.debug("lookupListIndices=" + lookupListIndices);

	}

	private TableHeader readHeader() throws IOException {
		rf.seek(tableLocation);
		// 32 bit signed
		int version = rf.readInt();
		// 16 bit unsigned
		int scriptListOffset = rf.readUnsignedShort();
		int featureListOffset = rf.readUnsignedShort();
		int lookupListOffset = rf.readUnsignedShort();

		// LOG.debug("version=" + version);
		// LOG.debug("scriptListOffset=" + scriptListOffset);
		// LOG.debug("featureListOffset=" + featureListOffset);
		// LOG.debug("lookupListOffset=" + lookupListOffset);

		TableHeader header = new TableHeader(version, scriptListOffset,
				featureListOffset, lookupListOffset);

		return header;
	}

}
