/*
 * $Id: SimpleTextExtractingPdfContentRenderListener.java 4115 2009-12-01 14:08:23Z blowagie $
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
 * perpendicular distance, but different parallel distance is separated by tab characters.
 * <br>
 * If text is relatively close to each other on the same line (within 4 space widths), the text
 * is kept together (separated with a single space).
 * <br>
 * This renderer also uses a simple strategy based on the font metrics to determine if
 * a blank space should be inserted into the output.
 *
 * @since	5.0.2
 */
public class LocationTextExtractionStrategy implements TextExtractionStrategy {

    /** set to true for debugging */
    static boolean DUMP_STATE = false;
    
    /** the starting point of the current line of text */
    private Vector chunkStart;
    /** the most recent ending point of the current chunk of text */
    private Vector chunkEnd;
    /** contains the text accumulated so far for the current chunk */
    private StringBuffer chunkText;
    /** a summary of all found text */
    private List<LocationOnPage> locationalResult;
    /** whether the operation is the first render of the page */
    boolean firstRender;

    /**
     * Creates a new text extraction renderer.
     */
    public LocationTextExtractionStrategy() {
        reset();
    }

    /**
     * Resets the internal state
     * @see com.itextpdf.text.pdf.parser.RenderListener#reset()
     */
    public void reset() {
        beginTextBlock();
        locationalResult = new ArrayList<LocationOnPage>();
    }
    /**
     *
     * @see com.itextpdf.text.pdf.parser.TextRenderListener#beginTextBlock()
     */
    public void beginTextBlock(){
        firstRender = true;
        chunkText = new StringBuffer();
    }

    /**
     *
     * @see com.itextpdf.text.pdf.parser.TextRenderListener#endTextBlock()
     */
    public void endTextBlock(){
        if (!firstRender)
            captureChunk(chunkText.toString());
    }

    /**
     * Returns the result so far.
     * @return	a String with the resulting text.
     */
    public String getResultantText(){

        if (DUMP_STATE) dumpState();
        
        Collections.sort(locationalResult);

        StringBuffer sb = new StringBuffer();
        LocationOnPage lastLocation = null;
        for (LocationOnPage locationOnPage : locationalResult) {
            LocationOnPage location = locationOnPage;

            if (lastLocation == null){
                sb.append(location.text);
            } else {
                if (location.sameLine(lastLocation)){
                    sb.append('\t');
                    sb.append(location.text);
                } else {
                    sb.append('\n');
                    sb.append(location.text);
                }
            }
            lastLocation = location;
        }

        return sb.toString();

    }

    /** Used for debugging only */
    private void dumpState(){
        for (Iterator iterator = locationalResult.iterator(); iterator.hasNext(); ) {
            LocationOnPage location = (LocationOnPage) iterator.next();
            
            location.printDiagnostics();
            
            System.out.println();
        }
        
    }
    
    /**
     * Captures text using a relatively advanced algorithm for determining text chunks and spaces
     * @param	renderInfo	render info
     */
    public void renderText(TextRenderInfo renderInfo) {
        boolean newChunk = false;

        Vector start = renderInfo.getStartPoint();
        Vector end = renderInfo.getEndPoint();

        float singleSpaceWidth = renderInfo.getSingleSpaceWidth();
        
        Vector lastUnitVector = null;
        float distFromLastChunkEnd = 0;

        if (!firstRender){
            lastUnitVector = chunkEnd.subtract(chunkStart).normalize();
            distFromLastChunkEnd = start.subtract(chunkEnd).dot(lastUnitVector);

            
            Vector x0 = start;
            Vector x1 = chunkStart;
            Vector x2 = chunkEnd;

            // see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
            float dist = x2.subtract(x1).cross(x1.subtract(x0)).lengthSquared() / x2.subtract(x1).lengthSquared();

            float sameLineThreshold = 1f; // we should probably base this on the current font metrics, but 1 pt seems to be sufficient for the time being
            if (dist > sameLineThreshold){
                newChunk = true;
            } else {

                Vector cross = start.subtract(chunkEnd).cross(chunkEnd.subtract(chunkStart));

                if (cross.length() <= 0.0001){ // parallel
                    // now check for anti-parallel or big spacing
                    //float spacing = chunkEnd.subtract(start).length();
                    
                    if (distFromLastChunkEnd < -singleSpaceWidth){
                        newChunk = true;
                    } else if (distFromLastChunkEnd > singleSpaceWidth*4){
                        newChunk = true;
                    } else {
                        newChunk = false;
                    }

                } else { // not parallel
                    newChunk = true;
                }
            }

        }

        if (newChunk){
            //System.out.println("<< Hard Return >>");
            captureChunk(chunkText.toString());
            chunkText.setLength(0);
            chunkStart = start;
        } else if (!firstRender){
            if (chunkText.charAt(chunkText.length()-1) != ' ' && renderInfo.getText().charAt(0) != ' '){ // we only insert a blank space if the trailing character of the previous string wasn't a space, and the leading character of the current string isn't a space
                if (distFromLastChunkEnd > singleSpaceWidth/2f){
                    chunkText.append(' ');
                    //System.out.println("Inserting implied space before '" + renderInfo.getText() + "'");
                }
            }
        } else {
            //System.out.println("Displaying first string of content '" + text + "' :: x1 = " + x1);
        }

        //System.out.println("[" + renderInfo.getStartPoint() + "]->[" + renderInfo.getEndPoint() + "] " + renderInfo.getText());
        chunkText.append(renderInfo.getText());

        if (firstRender){
            chunkStart = start;
            firstRender = false;
        }
        chunkEnd = end;

    }

    /**
     * Captures the specified text as a single, cohesive chunk of text
     * using the current line start and end information
     * @param text
     */
    private void captureChunk(String text){

        LocationOnPage location = new LocationOnPage(text, chunkStart, chunkEnd);
        locationalResult.add(location);

    }

    /**
     * Represents a chunk of text, it's orientation, and location relative to the orientation vector
     */
    private static class LocationOnPage implements Comparable<LocationOnPage>{
        /** the text of the chunk */
        final String text;
        /** the starting location of the chunk */
        final Vector startLocation;
        /** the ending location of the chunk */
        final Vector endLocation;
        /** unit vector in the orientation of the chunk */
        final Vector orientationVector;
        /** the magnitude of the orientation - this consists of just the Y component of the orientation vector
         *  this seems to work for now, but we may need to move to a different mechanism once we run into
         *  PDFs with different text orientation (This is just not an area that's been tested yet)
         */
        final int orientationMagnitude;
        /** perpendicular distance to the orientation unit vector (i.e. the Y position in an unrotated coordinate system) */
        final int distPerpendicular;
        /** parallel distance to the orientation unit vector (i.e. the X position in an unrotated coordinate system */
        final int distParallel;

        public LocationOnPage(String string, Vector startLocation, Vector endLocation) {
            this.text = string;
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            
            orientationVector = endLocation.subtract(startLocation).normalize();
            this.orientationMagnitude = (int)(orientationVector.get(Vector.I2)*1000);

            // see http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
            // the two vectors we are crossing are in the same plane, so the result will be purely
            // in the z-axis (out of plane) direction, so we just take the I3 component of the result
            Vector origin = new Vector(0,0,1);
            distPerpendicular = (int)(startLocation.subtract(origin)).cross(orientationVector).get(Vector.I3);

            distParallel = (int)orientationVector.dot(startLocation);
            
        }

        private void printDiagnostics(){
            System.out.println("Text (@" + startLocation + " -> " + endLocation + "): " + text);
            System.out.println("orientationMagnitude: " + orientationMagnitude);
            System.out.println("distPerpendicular: " + distPerpendicular);
            System.out.println("distParallel: " + distParallel);
        }
        
        /**
         * @param as the location to compare to
         * @return true is this location is on the the same line as the other
         */
        public boolean sameLine(LocationOnPage as){
            if (orientationMagnitude != as.orientationMagnitude) return false;
            if (distPerpendicular != as.distPerpendicular) return false;
            return true;
        }

        /**
         * Compares based on orientation, perpendicular distance, then parallel distance
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(LocationOnPage rhs) {
            int rslt;
            rslt = compareInts(orientationMagnitude, rhs.orientationMagnitude);
            if (rslt != 0) return rslt;

            rslt = compareInts(distPerpendicular, rhs.distPerpendicular);
            if (rslt != 0) return rslt;

            
            rslt = compareInts(distParallel, rhs.distParallel);
            return rslt;
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



}
