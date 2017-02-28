/**
 * Package containing the option classes.
 *
 * These are used to configure diverse function calls of xmpcore:
 *
 * <ul>
 *     <li>PropertyOptions - these are used to create properties and also to retrieve information about simple, array or struct properties, as well as qualifiers</li>
 *     <li>ParseOptions - used to configure the parsing of xmp metadata packets</li>
 *     <li>SerializationOptions - used to control the serialization of xmp metadata packets</li>
 *     <li>AliasOptions - used by XMPSchemaRegistry#registerAlias()</li>
 *     <li>IteratorOptions - used to set up an XMPIterator</li>
 *     <li>Options - the base class of all option classes</li>
 * </ul>
 *
 * For internal use only. If you want to use iText, please use a dependency on iText 7.
 */
@Deprecated
package com.itextpdf.xmp.options;