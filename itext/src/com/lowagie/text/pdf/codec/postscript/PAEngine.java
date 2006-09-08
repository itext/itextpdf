/*
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
 *
 * This class is based on a sample class by SUN.
 * The original copyright notice is as follows:
 * 
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 * 
 * The license agreement can be found at this URL:
 * http://java.sun.com/products/java-media/2D/samples/samples-license.html
 * See also the file sun.txt in directory com.lowagie.text.pdf
 */

package com.lowagie.text.pdf.codec.postscript;

import java.util.*;


public class PAEngine extends Object {

    static public final int MODE_STACK = 0;
    static public final int MODE_PROCEDURE = 1;
    static public final int MODE_ARRAY = 2;

    protected PAContext context;
    protected int mode;
    protected Stack procedure;
    protected int innerProcedures;

    public PAEngine(PAContext context){
        super();
        this.context = context;
        this.mode = PAEngine.MODE_STACK;
    }

    public void startProcedure() throws PainterException {
        this.procedure = new Stack();
        this.mode = PAEngine.MODE_PROCEDURE;
        this.innerProcedures = 0;
    }

    public void endProcedure() throws PainterException {
        this.context.operands.push(new PAToken(this.procedure, PAToken.PROCEDURE));
        this.mode = PAEngine.MODE_STACK;
    }

    public void bindProcedure(PAToken patoken){
        Stack oldStack = (Stack) patoken.value;
        Stack newStack = new Stack();
        int i,n;
        n = oldStack.size();
        for(i = 0; i < n; i++){
            Object token = oldStack.elementAt(i);
            if((token instanceof PAToken) && ((PAToken) token).type == PAToken.IDENTIFIER){
                Object foundToken = this.context.findIdentifier(((PAToken) token).value);
                if(foundToken == null){
                    newStack.push(token);
                } else {
                    newStack.push(foundToken);
                }
            } else {
                newStack.push(token);
            }
        }
        patoken.value = newStack;
    }

    public void process(Object token) throws PainterException {
        if(token == null){
            throw new IllegalStateException("Null token encountered; last unknown identifier was " + this.context.getLastUnknownIdentifier()+" at line "+this.context.poorscript.token.beginLine);
        }
        if(PAContext.DebugExecution){
          System.out.print("==>" + token.toString());
//          System.out.flush();
        }
        if(token instanceof PAToken && ((PAToken) token).type == PAToken.IMMEDIATE){
            Object foundValue = this.context.findIdentifier(((PAToken) token).value);
            this.process(foundValue);
            return;
        }
        if(this.mode == MODE_STACK){
            if(token instanceof PACommand){
                ((PACommand) token).execute(this.context);
            } else if(token instanceof PAToken){
                PAToken patoken = (PAToken) token;

                switch(patoken.type){
                case PAToken.IDENTIFIER:
                    this.process(this.context.findIdentifier(patoken.value));
                    break;
                case PAToken.KEY:
                case PAToken.MARK:
                case PAToken.START_ARRAY:
                    this.context.operands.push(token);
                    break;
                case PAToken.PROCEDURE:
                    Enumeration enumx = ((Vector) patoken.value).elements();
                    while(enumx.hasMoreElements()){
                        this.process(enumx.nextElement());
                    }
                    break;
                case PAToken.START_PROCEDURE:
                    this.startProcedure();
                    break;
                case PAToken.END_ARRAY:
                    this.context.collectArray();
                    break;
                case PAToken.START_DICT:
                  this.context.operands.push(token);
                  break;
                case PAToken.END_DICT:
                  this.context.collectDict();
                  break;
                default:
                    throw new IllegalStateException("Unknown token encountered" + token);
                }
            } else {
                this.context.operands.push(token);
            }
        } else if(this.mode == MODE_PROCEDURE){
            if(token instanceof PAToken){
                PAToken patoken = (PAToken) token;

                switch(patoken.type){
                case PAToken.START_PROCEDURE:
                    this.innerProcedures++;
                    this.procedure.push(token);
                    break;
                case PAToken.END_PROCEDURE:
                    if(this.innerProcedures > 0){
                        this.innerProcedures--;
                        this.procedure.push(token);
                    } else {
                        this.endProcedure();
                    }
                    break;
                default:
                    this.procedure.push(token);
                }
            } else {
                this.procedure.push(token);
            }
        }
    }

    public String litMode(){
      switch(this.mode){
        case MODE_ARRAY: return "Array";
          case MODE_PROCEDURE: return "Proc"+innerProcedures;
            case MODE_STACK: return "Stack";
              default: return "Unknown";
      }
    }

}

