/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002, 2003, 2004 by Mark Hall
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
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
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

package com.lowagie.text.rtf.list;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.RtfExtendedElement;
import com.lowagie.text.rtf.document.RtfDocument;


/**
 * The RtfListTable manages all RtfLists in one RtfDocument. It also generates
 * the list and list override tables in the document header.
 * 
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfListTable extends RtfElement implements RtfExtendedElement {

    /**
     * Constant for the list number
     */
    protected static final byte[] LIST_NUMBER = "\\ls".getBytes();
    /**
     * Constant for the list table
     */
    private static final byte[] LIST_TABLE = "\\*\\listtable".getBytes();
    /**
     * Constant for the list
     */
    private static final byte[] LIST = "\\list".getBytes();
    /**
     * Constant for the list template id
     */
    private static final byte[] LIST_TEMPLATE_ID = "\\listtemplateid".getBytes();
    /**
     * Constant for the hybrid list
     */
    private static final byte[] LIST_HYBRID = "\\hybrid".getBytes();
    /**
     * Constant for the list id
     */
    private static final byte[] LIST_ID = "\\listid".getBytes();
    /**
     * Constant for the list override table
     */
    private static final byte[] LIST_OVERRIDE_TABLE = "\\*\\listoverridetable".getBytes();
    /**
     * Constant for the list override
     */
    private static final byte[] LIST_OVERRIDE = "\\listoverride".getBytes();
    /**
     * Constant for the list override count
     */
    private static final byte[] LIST_OVERRIDE_COUNT = "\\listoverridecount".getBytes();
    
    /**
     * The RtfLists managed by this RtfListTable
     */
    private ArrayList lists;
    
    /**
     * Constructs a RtfListTable for a RtfDocument
     * 
     * @param doc The RtfDocument this RtfListTable belongs to
     */
    public RtfListTable(RtfDocument doc) {
        super(doc);
        
        this.lists = new ArrayList();
    }

    /**
     * Writes the list and list override tables.
     * 
     * @return A byte array with the list and list override tables.
     */
    public byte[] writeDefinition() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        int[] listIds = new int[lists.size()];
        try {
            result.write(OPEN_GROUP);
            result.write(LIST_TABLE);
            result.write("\n".getBytes());
            for(int i = 0; i < lists.size(); i++) {
                result.write(OPEN_GROUP);
                result.write(LIST);
                result.write(LIST_TEMPLATE_ID);
                result.write(intToByteArray(document.getRandomInt()));
                result.write(LIST_HYBRID);
                result.write("\n".getBytes());
                result.write(((RtfList) lists.get(i)).writeDefinition());
                result.write(LIST_ID);
                listIds[i] = document.getRandomInt();
                result.write(intToByteArray(listIds[i]));
                result.write(CLOSE_GROUP);
                result.write("\n".getBytes());
            }
            result.write(CLOSE_GROUP);
            result.write("\n".getBytes());
            result.write(OPEN_GROUP);
            result.write(LIST_OVERRIDE_TABLE);
            result.write("\n".getBytes());
            for(int i = 0; i < lists.size(); i++) {
                result.write(OPEN_GROUP);
                result.write(LIST_OVERRIDE);
                result.write(LIST_ID);
                result.write(intToByteArray(listIds[i]));
                result.write(LIST_OVERRIDE_COUNT);
                result.write(intToByteArray(0));
                result.write(LIST_NUMBER);
                result.write(intToByteArray(((RtfList) lists.get(i)).getListNumber()));
                result.write(CLOSE_GROUP);
                result.write("\n".getBytes());
            }
            result.write(CLOSE_GROUP);
            result.write("\n".getBytes());
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }

    /**
     * Gets the id of the specified RtfList. If the RtfList is not yet in the
     * list of RtfLists, then it is added.
     * 
     * @param list The RtfList for which to get the id.
     * @return The id of the RtfList.
     */
    public int getListNumber(RtfList list) {
        if(lists.contains(list)) {
            return lists.indexOf(list);
        } else {
            lists.add(list);
            return lists.size();
        }
    }
    
    /**
     * Remove a RtfList from the list of RtfLists
     * 
     * @param list The RtfList to remove.
     */
    public void freeListNumber(RtfList list) {
        int i = lists.indexOf(list);
        if(i >= 0) {
            lists.remove(i);
        }
    }
}
