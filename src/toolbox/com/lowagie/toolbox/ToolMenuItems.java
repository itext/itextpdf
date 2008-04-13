/*
 * $Id: ToolMenuItems.java 105 2007-09-24 20:09:31Z chammer $
 * Copyright (c) 2005-2007 Bruno Lowagie
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * This code was originally published under the MPL by Bruno Lowagie.
 * It was a part of iText, a Java-PDF library. You can now use it under
 * the MIT License; for backward compatibility you can also use it under
 * the MPL version 1.1: http://www.mozilla.org/MPL/
 * A copy of the MPL license is bundled with the source code FYI.
 */

package com.lowagie.toolbox;

/**
 * Keeps all the possible menuitems.
 * @since 2.1.1 (imported from itexttoolbox project)
 */
 public interface ToolMenuItems {

	/** An item in the menubar. */
	String FILE = "File";
	/** An item in the menubar. */
	String CLOSE = "Close";
	/** An item in the menubar. */
	String TOOLS = "Tools";
        String FILELIST = "Filelist";
        String VIEW = "View";
        String RESET = "Reset";
	/** An item in the menubar. */
	String HELP = "Help";
	/** An item in the menubar. */
	String VERSION = "Version";
	/** An item in the menubar. */
	String ABOUT = "About";
	/** An item in the menubar. */
	String TOOL = "Tool";
	/** An item in the menubar. */
	String USAGE = "Usage";
	/** An item in the menubar. */
	String ARGUMENTS = "Arguments";
	/** An item in the menubar. */
	String EXECUTE = "Execute";
	/** An item in the menubar. */
	String EXECUTESHOW = "Execute+Open";
	/** An item in the menubar. */
	String EXECUTEPRINT = "Execute+Printdialog";
	/** An item in the menubar. */
	String EXECUTEPRINTSILENT = "Execute+Print";

}
