/*
 * $Id: LocationTextExtractionStrategy.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * <b>Development preview</b> - this class (and all of the parser classes) are still experiencing
 * heavy development, and are subject to change both behavior and interface.
 * <br>
 * A text extraction renderer that keeps track of relative position of text on page
 * The resultant text will be relatively consistent with the physical layout that most
 * PDF files have on screen.
 * <br>
 * This renderer keeps track of the orientation and distance (both perpendicular
 * and parallel) to the unit vector of the orientation.  Text is ordered by
 * orientation, then perpendicular, then parallel distance.  Text with the same
 * perpendicular distance, but different parallel distance is treated as being on
 * the same line.
 * <br>
 * This renderer also uses a simple strategy based on the font metrics to determine if
 * a blank space should be inserted into the output.
 *
 * @since   5.0.2
 */
public class LocationTextExtractionStrategy implements TextExtractionStrategy {

    /** set to true for debugging */
    static boolean DUMP_STATE = false;
    
    /** a summary of all found text */
    private final List<TextChunk> locationalResult = new ArrayList<TextChunk>();

    /**
     * Creates a new text extraction renderer.
     */
    public LocationTextExtractionStrategy() {
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
     */
    public void beginTextBlock(){
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
     */
    public void endTextBlock(){
    }

    /**
     * @param str
     * @return true if the string starts with a space character, false if the string is empty or starts with a non-space character
     */
    private boolean startsWithSpace(String str){
        if (str.length() == 0) return false;
        return str.charAt(0) == ' ';
    }
    
    /**
     * @param str
     * @return true if the string ends with a space character, false if the string is empty or ends with a non-space character
     */
    private boolean endsWithSpace(String str){
        if (str.length() == 0) return false;
        return str.charAt(str.length()-1) == ' ';
    }

    /**
     * Filters the provided list with the provided filter
     * @param textChunks a list of all TextChunks that this strategy found during processing
     * @param filter the filter to apply.  If null, filtering will be skipped.
     * @return the filtered list
     * @since 5.3.3
     */
    private List<TextChunk> filterTextChunks(List<TextChunk> textChunks, TextChunkFilter filter){
    	if (filter == null)
    		return textChunks;
    	
    	List<TextChunk> filtered = new ArrayList<TextChunk>();
    	for (TextChunk textChunk : textChunks) {
			if (filter.accept(textChunk))
				filtered.add(textChunk);
		}
    	return filtered;
    }
    
    /**
     * Determines if a space character should be inserted between a previous chunk and the current chunk.
     * This method is exposed as a callback so subclasses can fine time the algorithm for determining whether a space should be inserted or not.
     * By default, this method will insert a space if the there is a gap of more than half the font space character width between the end of the
     * previous chunk and the beginning of the current chunk.  It will also indicate that a space is needed if the starting point of the new chunk 
     * appears *before* the end of the previous chunk (i.e. overlapping text).
     * @param chunk the new chunk being evaluated
     * @param previousChunk the chunk that appeared immediately before the current chunk
     * @return true if the two chunks represent different words (i.e. should have a space between them).  False otherwise.
     */
    protected boolean isChunkAtWordBoundary(TextChunk chunk, TextChunk previousChunk){
        float dist = chunk.distanceFromEndOf(previousChunk);
        
        if (dist < -chunk.getCharSpaceWidth() || dist > chunk.getCharSpaceWidth()/2.0f)
            return true;
        
        return false;
    }
    
    /**
     * Gets text that meets the specified filter
     * If multiple text extractions will be performed for the same page (i.e. for different physical regions of the page), 
     * filtering at this level is more efficient than filtering using {@link FilteredRenderListener} - but not nearly as powerful
     * because most of the RenderInfo state is not captured in {@link TextChunk}
     * @param chunkFilter the filter to to apply
     * @return the text results so far, filtered using the specified filter
     */
    public String getResultantText(TextChunkFilter chunkFilter){
        if (DUMP_STATE) dumpState();
        
        List<TextChunk> filteredTextChunks = filterTextChunks(locationalResult, chunkFilter);
    	Collections.sort(filteredTextChunks);

        StringBuffer sb = new StringBuffer();
        TextChunk lastChunk = null;
        for (TextChunk chunk : filteredTextChunks) {

            if (lastChunk == null){
                sb.append(chunk.text);
            } else {
                if (chunk.sameLine(lastChunk)){
                    // we only insert a blank space if the trailing character of the previous string wasn't a space, and the leading character of the current string isn't a space
                    if (isChunkAtWordBoundary(chunk, lastChunk) && !startsWithSpace(chunk.text) && !endsWithSpace(lastChunk.text))
                        sb.append(' ');
                    
                    sb.append(chunk.text);
                } else {
                    sb.append('\n');
                    sb.append(chunk.text);
                }
            }
            lastChunk = chunk;
        }

        return sb.toString();    	
    }
    
    /**
     * Returns the result so far.
     * @return  a String with the resulting text.
     */
    public String getResultantText(){

    	return getResultantText(null);

    }
    
    /** Used for debugging only */
    private void dumpState(){
        for (Iterator<TextChunk> iterator = locationalResult.iterator(); iterator.hasNext(); ) {
            TextChunk location = (TextChunk) iterator.next();
            
            location.printDiagnostics();
            
            System.out.println();
        }
        
    }
    
    /**
     * 
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    public void renderText(TextRenderInfo renderInfo) {
    	LineSegment segment = renderInfo.getBaseline();
    	if (renderInfo.getRise() != 0){ // remove the rise from the baseline - we do this because the text from a super/subscript render operations should probably be considered as part of the baseline of the text the super/sub is relative to 
	    	Matrix riseOffsetTransform = new Matrix(0, -renderInfo.getRise());
	    	segment = segment.transformBy(riseOffsetTransform);
    	}
        TextChunk location = new TextChunk(renderInfo.getText(), segment.getStartPoint(), segment.getEndPoint(), renderInfo.getSingleSpaceWidth());
        locationalResult.add(location);        
    }
    


    /**
     * Represents a chunk of text, it's orientation, and location relative to the orientation vector
     */
    public static class TextChunk implements Comparable<TextChunk>{
        /** the text of the chunk */
        private final String text;
        /** the starting location of the chunk */
        private final Vector startLocation;
        /** the ending location of the chunk */
        private final Vector endLocation;
        /** unit vector in the orientation of the chunk */
        private final Vector orientationVector;
        /** the orientation as a scalar for quick sorting */
        private final int orientationMagnitude;
        /** perpendicular distance to the orientation unit vector (i.e. the Y position in an unrotated coordinate system)
         * we round to the nearest integer to handle the fuzziness of comparing floats */
        private final int distPerpendicular;
        /** distance of the start of the chunk parallel to the orientation unit vector (i.e. the X position in an unrotated coordinate system) */
        private final float distParallelStart;
        /** distance of the end of the chunk parallel to the orientation unit vector (i.e. the X position in an unrotated coordinate system) */
        private final float distParallelEnd;
        /** the width of a single space character in the font of the chunk */
        private final float charSpaceWidth;
        
        public TextChunk(String string, Vector startLocation, Vector endLocation, float charSpaceWidth) {
            this.text = string;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.charSpaceWidth = charSpaceWidth;
            
            Vector oVector = endLocation.subtract(startLocation);
            if (oVector.length() == 0) {
            	oVector = new Vector(1, 0, 0);
            }
            orientationVector = oVector.normalize();
            orientationMagnitude = (int)(Math.atan2(orientationVector.get(Vector.I2), orientationVector.get(Vector.I1))*1000);

            // see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
            // the two vectors we are crossing are in the same plane, so the result will be purely
            // in the z-axis (out of plane) direction, so we just take the I3 component of the result
            Vector origin = new Vector(0,0,1);
            distPerpendicular = (int)(startLocation.subtract(origin)).cross(orientationVector).get(Vector.I3);

            distParallelStart = orientationVector.dot(startLocation);
            distParallelEnd = orientationVector.dot(endLocation);
        }

        /**
         * @return the start location of the text
         */
        public Vector getStartLocation(){
        	return startLocation;
        }
        /**
         * @return the end location of the text
         */
        public Vector getEndLocation(){
        	return endLocation;
        }
        
        /**
         * @return the text captured by this chunk
         */
        public String getText(){
        	return text;
        }
        
        /**
         * @return the width of a single space character as rendered by this chunk
         */
        public float getCharSpaceWidth() {
			return charSpaceWidth;
		}
        
        private void printDiagnostics(){
            System.out.println("Text (@" + startLocation + " -> " + endLocation + "): " + text);
            System.out.println("orientationMagnitude: " + orientationMagnitude);
            System.out.println("distPerpendicular: " + distPerpendicular);
            System.out.println("distParallel: " + distParallelStart);
        }
        
        /**
         * @param as the location to compare to
         * @return true is this location is on the the same line as the other
         */
        public boolean sameLine(TextChunk as){
            if (orientationMagnitude != as.orientationMagnitude) return false;
            if (distPerpendicular != as.distPerpendicular) return false;
            return true;
        }

        /**
         * Computes the distance between the end of 'other' and the beginning of this chunk
         * in the direction of this chunk's orientation vector.  Note that it's a bad idea
         * to call this for chunks that aren't on the same line and orientation, but we don't
         * explicitly check for that condition for performance reasons.
         * @param other
         * @return the number of spaces between the end of 'other' and the beginning of this chunk
         */
        public float distanceFromEndOf(TextChunk other){
            float distance = distParallelStart - other.distParallelEnd;
            return distance;
        }
        
        /**
         * Compares based on orientation, perpendicular distance, then parallel distance
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(TextChunk rhs) {
            if (this == rhs) return 0; // not really needed, but just in case
            
            int rslt;
            rslt = compareInts(orientationMagnitude, rhs.orientationMagnitude);
            if (rslt != 0) return rslt;

            rslt = compareInts(distPerpendicular, rhs.distPerpendicular);
            if (rslt != 0) return rslt;

            return Float.compare(distParallelStart, rhs.distParallelStart);
        }

        /**
         *
         * @param int1
         * @param int2
         * @return comparison of the two integers
         */
        private static int compareInts(int int1, int int2){
            return int1 == int2 ? 0 : int1 < int2 ? -1 : 1;
        }

        
    }

    /**
     * no-op method - this renderer isn't interested in image events
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
     * @since 5.0.1
     */
    public void renderImage(ImageRenderInfo renderInfo) {
        // do nothing
    }

    /**
     * Specifies a filter for filtering {@link TextChunk} objects during text extraction 
     * @see LocationTextExtractionStrategy#getResultantText(TextChunkFilter)
     * @since 5.3.3
     */
    public static interface TextChunkFilter{
    	/**
    	 * @param textChunk the chunk to check
    	 * @return true if the chunk should be allowed
    	 */
    	public boolean accept(TextChunk textChunk);
    }
}
