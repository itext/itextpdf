/*
 * $Id$
 * $Name$
 *
 * Copyright 2000, 2001 by Paulo Soares.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;

import com.lowagie.text.DocumentException;
import java.io.*;

/**
 * Creates a CJK font compatible with the fonts in the Adobe Asian font Pack.
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */

class CJKFont extends BaseFont
{
    /** The encoding used in the PDF document for CJK fonts
     */    
    static final String CJK_ENCODING = "UnicodeBigUnmarked";
/** Metrics for the font STSong-Light with the encoding UniGB-UCS2-H */
    private static final int STSong_Light_UniGB_UCS2_H[] = {
        32,207,33,270,34,342,35,467,36,462,37,797,38,710,39,239,40,374,41,374,42,423,43,605,44,238,45,375,46,238,47,334,
        48,462,49,462,50,462,51,462,52,462,53,462,54,462,55,462,56,462,57,462,58,238,59,238,60,605,61,605,62,605,63,344,
        64,748,65,684,66,560,67,695,68,739,69,563,70,511,71,729,72,793,73,318,74,312,75,666,76,526,77,896,78,758,79,772,
        80,544,81,772,82,628,83,465,84,607,85,753,86,711,87,972,88,647,89,620,90,607,91,374,92,333,93,374,94,606,95,500,
        96,239,97,417,98,503,99,427,100,529,101,415,102,264,103,444,104,518,105,241,106,230,107,495,108,228,109,793,110,527,111,524,
        112,524,113,504,114,338,115,336,116,277,117,517,118,450,119,652,120,466,121,452,122,407,123,370,124,258,125,370,126,605
    };
    
/** Metrics for the font MHei-Medium with the encoding UniCNS-UCS2-H */
    private static final int MHei_Medium_UniCNS_UCS2_H[] = {
        32,278,33,278,34,355,35,556,36,556,37,889,38,667,39,222,40,333,41,333,42,389,43,584,44,278,45,333,46,278,47,278,
        48,556,49,556,50,556,51,556,52,556,53,556,54,556,55,556,56,556,57,556,58,278,59,278,60,584,61,584,62,584,63,556,
        64,1015,65,667,66,667,67,722,68,722,69,667,70,611,71,778,72,722,73,278,74,500,75,667,76,556,77,833,78,722,79,778,
        80,667,81,778,82,722,83,667,84,611,85,722,86,667,87,944,88,667,89,667,90,611,91,278,92,278,93,278,94,469,95,556,
        96,222,97,556,98,556,99,500,100,556,101,556,102,278,103,556,104,556,105,222,106,222,107,500,108,222,109,833,110,556,111,556,
        112,556,113,556,114,333,115,500,116,278,117,556,118,500,119,722,120,500,121,500,122,500,123,334,124,260,125,334,126,584
    };
    
/** Metrics for the font MSung_Light with the encoding UniCNS-UCS2-H */
    private static final int MSung_Light_UniCNS_UCS2_H[] = {
        32,250,33,250,34,408,35,668,36,490,37,875,38,698,39,250,40,240,41,240,42,417,43,667,44,250,45,313,46,250,47,520,
        48,500,49,500,50,500,51,500,52,500,53,500,54,500,55,500,56,500,57,500,58,250,59,250,60,667,61,667,62,667,63,396,
        64,921,65,677,66,615,67,719,68,760,69,625,70,552,71,771,72,802,73,354,74,354,75,781,76,604,77,927,78,750,79,823,
        80,563,81,823,82,729,83,542,84,698,85,771,86,729,87,948,88,771,89,677,90,635,91,344,92,520,93,344,94,469,95,500,
        96,250,97,469,98,521,99,427,100,521,101,438,102,271,103,469,104,531,105,250,106,250,107,458,108,240,109,802,110,531,111,500,
        112,521,113,521,114,365,115,333,116,292,117,521,118,458,119,677,120,479,121,458,122,427,123,480,124,496,125,480,126,667
    };
    
/** Metrics for the font HeiseiKakuGo-W5 with the encoding UniJIS-UCS2-H */
    private static final int HeiseiKakuGo_W5_UniJIS_UCS2_H[] = {
        32,278,33,278,34,355,35,556,36,556,37,889,38,667,39,191,40,333,41,333,42,389,43,584,44,278,45,333,46,278,47,278,
        48,556,49,556,50,556,51,556,52,556,53,556,54,556,55,556,56,556,57,556,58,278,59,278,60,584,61,584,62,584,63,556,
        64,1015,65,667,66,667,67,722,68,722,69,667,70,611,71,778,72,722,73,278,74,500,75,667,76,556,77,833,78,722,79,778,
        80,667,81,778,82,722,83,667,84,611,85,722,86,667,87,944,88,667,89,667,90,611,91,278,92,278,93,278,94,469,95,556,
        96,333,97,556,98,556,99,500,100,556,101,556,102,278,103,556,104,556,105,222,106,222,107,500,108,222,109,833,110,556,111,556,
        112,556,113,556,114,333,115,500,116,278,117,556,118,500,119,722,120,500,121,500,122,500,123,334,124,260,125,334,126,333,161,333,
        162,556,163,556,164,556,165,556,166,260,169,737,170,370,171,556,172,584,173,584,174,737,175,333,178,333,179,333,181,556,183,278,
        184,333,185,333,186,365,187,556,188,834,189,834,190,834,191,611,192,667,193,667,194,667,195,667,196,667,197,667,199,722,200,667,
        201,667,202,667,203,667,204,278,205,278,206,278,207,278,208,722,209,722,210,778,211,778,212,778,213,778,214,778,216,778,217,722,
        218,722,219,722,220,722,221,667,222,667,223,611,224,556,225,556,226,556,227,556,228,556,229,556,230,889,231,500,232,556,233,556,
        234,556,235,556,236,278,237,278,238,278,239,278,240,556,241,556,242,556,243,556,244,556,245,556,246,556,248,611,249,556,250,556,
        251,556,252,556,253,500,254,556,255,500,305,278,321,556,322,222,339,944,352,667,353,500,376,667,381,611,382,500,448,260,768,333,
        769,333,770,333,771,333,772,333,773,556,774,333,775,333,776,333,778,333,779,333,780,333,807,333,808,333,818,556,8194,500,8209,333,
        8210,556,8211,556,8218,222,8222,333,8226,350,8249,333,8250,333,8254,500,8260,167,64257,500,64258,500,65377,500,65378,500,65379,500,65380,500,65381,500,
        65382,500,65383,500,65384,500,65385,500,65386,500,65387,500,65388,500,65389,500,65390,500,65391,500,65392,500,65393,500,65394,500,65395,500,65396,500,65397,500,
        65398,500,65399,500,65400,500,65401,500,65402,500,65403,500,65404,500,65405,500,65406,500,65407,500,65408,500,65409,500,65410,500,65411,500,65412,500,65413,500,
        65414,500,65415,500,65416,500,65417,500,65418,500,65419,500,65420,500,65421,500,65422,500,65423,500,65424,500,65425,500,65426,500,65427,500,65428,500,65429,500,
        65430,500,65431,500,65432,500,65433,500,65434,500,65435,500,65436,500,65437,500,65438,500,65439,500,65512,500
    };
    
/** Metrics for the font HeiseiKakuGo-W5 with the encoding UniJIS-UCS2-HW-H */
    private static final int HeiseiKakuGo_W5_UniJIS_UCS2_HW_H[] = {
        32,500,33,500,34,500,35,500,36,500,37,500,38,500,39,500,40,500,41,500,42,500,43,500,44,500,45,500,46,500,47,500,
        48,500,49,500,50,500,51,500,52,500,53,500,54,500,55,500,56,500,57,500,58,500,59,500,60,500,61,500,62,500,63,500,
        64,500,65,500,66,500,67,500,68,500,69,500,70,500,71,500,72,500,73,500,74,500,75,500,76,500,77,500,78,500,79,500,
        80,500,81,500,82,500,83,500,84,500,85,500,86,500,87,500,88,500,89,500,90,500,91,500,92,500,93,500,94,500,95,500,
        96,500,97,500,98,500,99,500,100,500,101,500,102,500,103,500,104,500,105,500,106,500,107,500,108,500,109,500,110,500,111,500,
        112,500,113,500,114,500,115,500,116,500,117,500,118,500,119,500,120,500,121,500,122,500,123,500,124,500,125,500,126,500,165,500
    };
/** Metrics for the font HeiseiKakuGo-W5 with the encoding UniJIS-UCS2-HW-V */
    private static final int HeiseiKakuGo_W5_UniJIS_UCS2_HW_V[] = {
        32,500,33,500,34,500,35,500,36,500,37,500,38,500,39,500,40,500,41,500,42,500,43,500,44,500,45,500,46,500,47,500,
        48,500,49,500,50,500,51,500,52,500,53,500,54,500,55,500,56,500,57,500,58,500,59,500,60,500,61,500,62,500,63,500,
        64,500,65,500,66,500,67,500,68,500,69,500,70,500,71,500,72,500,73,500,74,500,75,500,76,500,77,500,78,500,79,500,
        80,500,81,500,82,500,83,500,84,500,85,500,86,500,87,500,88,500,89,500,90,500,91,500,92,500,93,500,94,500,95,500,
        96,500,97,500,98,500,99,500,100,500,101,500,102,500,103,500,104,500,105,500,106,500,107,500,108,500,109,500,110,500,111,500,
        112,500,113,500,114,500,115,500,116,500,117,500,118,500,119,500,120,500,121,500,122,500,123,500,124,500,125,500,126,500,165,500
    };
/** Metrics for the font HeiseiMin-W3 with the encoding UniJIS-UCS2-H */
    private static final int HeiseiMin_W3_UniJIS_UCS2_H[] = {
        32,250,33,333,34,408,35,500,36,500,37,833,38,778,39,180,40,333,41,333,42,500,43,564,44,250,45,333,46,250,47,278,
        48,500,49,500,50,500,51,500,52,500,53,500,54,500,55,500,56,500,57,500,58,278,59,278,60,564,61,564,62,564,63,444,
        64,921,65,722,66,667,67,667,68,722,69,611,70,556,71,722,72,722,73,333,74,389,75,722,76,611,77,889,78,722,79,722,
        80,556,81,722,82,667,83,556,84,611,85,722,86,722,87,944,88,722,89,722,90,611,91,333,92,278,93,333,94,469,95,500,
        96,333,97,444,98,500,99,444,100,500,101,444,102,333,103,500,104,500,105,278,106,278,107,500,108,278,109,778,110,500,111,500,
        112,500,113,500,114,333,115,389,116,278,117,500,118,500,119,722,120,500,121,500,122,444,123,480,124,200,125,480,126,333,161,333,
        162,500,163,500,164,500,165,500,166,200,169,760,170,276,171,500,172,564,173,564,174,760,175,333,178,300,179,300,181,500,183,250,
        184,333,185,300,186,310,187,500,188,750,189,750,190,750,191,444,192,722,193,722,194,722,195,722,196,722,197,722,198,889,199,667,
        200,611,201,611,202,611,203,611,204,333,205,333,206,333,207,333,208,722,209,722,210,722,211,722,212,722,213,722,214,722,216,722,
        217,722,218,722,219,722,220,722,221,722,222,556,223,500,224,444,225,444,226,444,227,444,228,444,229,444,230,667,231,444,232,444,
        233,444,234,444,235,444,236,278,237,278,238,278,239,278,240,500,241,500,242,500,243,500,244,500,245,500,246,500,248,500,249,500,
        250,500,251,500,252,500,253,500,254,500,255,500,305,278,321,611,322,278,338,889,339,722,352,556,353,389,376,722,381,611,382,444,
        448,200,768,333,769,333,770,333,771,333,772,333,773,500,774,333,775,333,776,333,778,333,779,333,780,333,807,333,808,333,818,500,
        8194,500,8209,333,8210,500,8211,500,8218,333,8222,444,8226,350,8249,333,8250,333,8254,500,8260,167,8482,980,64257,556,64258,556,65377,500,65378,500,
        65379,500,65380,500,65381,500,65382,500,65383,500,65384,500,65385,500,65386,500,65387,500,65388,500,65389,500,65390,500,65391,500,65392,500,65393,500,65394,500,
        65395,500,65396,500,65397,500,65398,500,65399,500,65400,500,65401,500,65402,500,65403,500,65404,500,65405,500,65406,500,65407,500,65408,500,65409,500,65410,500,
        65411,500,65412,500,65413,500,65414,500,65415,500,65416,500,65417,500,65418,500,65419,500,65420,500,65421,500,65422,500,65423,500,65424,500,65425,500,65426,500,
        65427,500,65428,500,65429,500,65430,500,65431,500,65432,500,65433,500,65434,500,65435,500,65436,500,65437,500,65438,500,65439,500,65512,500
    };
    
/** Metrics for the font HeiseiMin-W3 with the encoding UniJIS-UCS2-HW-H */
    private static final int HeiseiMin_W3_UniJIS_UCS2_HW_H[] = {
        32,500,33,500,34,500,35,500,36,500,37,500,38,500,39,500,40,500,41,500,42,500,43,500,44,500,45,500,46,500,47,500,
        48,500,49,500,50,500,51,500,52,500,53,500,54,500,55,500,56,500,57,500,58,500,59,500,60,500,61,500,62,500,63,500,
        64,500,65,500,66,500,67,500,68,500,69,500,70,500,71,500,72,500,73,500,74,500,75,500,76,500,77,500,78,500,79,500,
        80,500,81,500,82,500,83,500,84,500,85,500,86,500,87,500,88,500,89,500,90,500,91,500,92,500,93,500,94,500,95,500,
        96,500,97,500,98,500,99,500,100,500,101,500,102,500,103,500,104,500,105,500,106,500,107,500,108,500,109,500,110,500,111,500,
        112,500,113,500,114,500,115,500,116,500,117,500,118,500,119,500,120,500,121,500,122,500,123,500,124,500,125,500,126,500,165,500
    };
    
/** Metrics for the font HeiseiMin-W3 with the encoding UniJIS-UCS2-HW-V */
    private static final int HeiseiMin_W3_UniJIS_UCS2_HW_V[] = {
        32,500,33,500,34,500,35,500,36,500,37,500,38,500,39,500,40,500,41,500,42,500,43,500,44,500,45,500,46,500,47,500,
        48,500,49,500,50,500,51,500,52,500,53,500,54,500,55,500,56,500,57,500,58,500,59,500,60,500,61,500,62,500,63,500,
        64,500,65,500,66,500,67,500,68,500,69,500,70,500,71,500,72,500,73,500,74,500,75,500,76,500,77,500,78,500,79,500,
        80,500,81,500,82,500,83,500,84,500,85,500,86,500,87,500,88,500,89,500,90,500,91,500,92,500,93,500,94,500,95,500,
        96,500,97,500,98,500,99,500,100,500,101,500,102,500,103,500,104,500,105,500,106,500,107,500,108,500,109,500,110,500,111,500,
        112,500,113,500,114,500,115,500,116,500,117,500,118,500,119,500,120,500,121,500,122,500,123,500,124,500,125,500,126,500,165,500
    };
/** Metrics for the font HYGoThic-Medium with the encoding UniKS-UCS2-H */
    private static final int HYGoThic_Medium_UniKS_UCS2_H[] = {
        32,333,33,416,34,416,35,833,36,666,37,916,38,750,39,250,40,416,41,416,42,583,43,833,44,375,45,833,46,375,47,375,
        48,583,49,583,50,583,51,583,52,583,53,583,54,583,55,583,56,583,57,583,58,416,59,416,60,833,61,833,62,833,63,583,
        65,666,66,708,67,750,68,750,69,666,70,625,71,833,72,750,73,291,74,541,75,708,76,583,77,875,78,750,79,791,80,666,
        81,791,82,708,83,666,84,583,85,750,86,625,87,916,88,625,89,625,90,625,91,416,92,375,93,416,94,500,95,500,96,500,
        97,583,98,625,99,583,100,625,101,583,102,375,103,625,104,583,105,250,106,250,107,541,108,250,109,916,110,625,111,625,112,625,
        113,625,114,333,115,541,116,333,117,583,118,500,119,750,120,500,121,500,122,500,123,500,124,500,125,500,126,750
    };
    
/** Metrics for the font HYSMyeongJo-Medium with the encoding UniKS-UCS2-H */
    private static final int HYSMyeongJo_Medium_UniKS_UCS2_H[] = {
        32,333,33,416,34,416,35,833,36,625,37,916,38,833,39,250,40,500,41,500,42,500,43,833,44,291,45,833,46,291,47,375,
        48,625,49,625,50,625,51,625,52,625,53,625,54,625,55,625,56,625,57,625,58,333,59,333,60,833,61,833,62,916,63,500,
        65,791,66,708,67,708,68,750,69,708,70,666,71,750,72,791,73,375,74,500,75,791,76,666,77,916,78,791,79,750,80,666,
        81,750,82,708,83,666,84,791,85,791,86,750,88,708,89,708,90,666,91,500,92,375,93,500,94,500,95,500,96,333,97,541,
        98,583,99,541,100,583,101,583,102,375,103,583,104,583,105,291,106,333,107,583,108,291,109,875,110,583,111,583,112,583,113,583,
        114,458,115,541,116,375,117,583,118,583,119,833,120,625,121,625,122,500,123,583,124,583,125,583,126,750
    };
/** Array of font/encoding combinations allowed and information to build the font descriptors */
    private static final  Object cjk[] =
    {
        new Object[]{
            "STSong-Light", //font name
            new int[]{880,880,-120,6,-25,-254,1000,880,0,93,2}, //font descriptor (Ascent,CapHeight,Descent,Flags,FontBBox,ItalicAngle,StemV,Supplement
            //font descriptor registry, ordering, W
            new String[]{"Adobe", "GB1", "[1[207 270 342 467 462 797 710 239]9 10 374 11[423 605 238 375 238 334]17 26 462 27 28 238 29 31 605 32[344 748 684 560 695 739 563 511 729 793 318 312 666 526 896 758 772 544 772 628 465 607 753 711 972 647 620 607 374 333 374 606 500 239 417 503 427 529 415 264 444 518 241 230 495 228 793 527]80 81 524 82[504 338 336 277 517 450 652 466 452 407 370 258 370 605]814 939 500 7712[517 684 723 1000 500]]"},
            new byte[]{1,5,2,2,4,0,0,0,0,0,0,0}, //panose
            new String[]{"UniGB-UCS2-H","UniGB-UCS2-V"}, // allowed cmaps
            new int[][]{STSong_Light_UniGB_UCS2_H, null, STSong_Light_UniGB_UCS2_H, null} //metrics (two for each cmap, search first then second}
        },
        new Object[]{
            "MHei-Medium", //font name
            new int[]{880,880,-120,4,-45,-250,1015,887,0,93,0}, //font descriptor (Ascent,CapHeight,Descent,Flags,FontBBox,ItalicAngle,StemV,Supplement
            //font descriptor registry, ordering, W
            new String[]{"Adobe", "CNS1", "[1 2 278 3[355]4 5 556 6[889 667 222]9 10 333 11[389 584 278 333]15 16 278 17 26 556 27 28 278 29 31 584 32[556 1015]34 35 667 36 37 722 38[667 611 778 722 278 500 667 556 833 722 778 667 778 722 667 611 722 667 944]57 58 667 59[611]60 62 278 63[469 556 222]66 67 556 68[500]69 70 556 71[278]72 73 556 74 75 222 76[500 222 833]79 82 556 83[333 500 278 556 500 722]89 91 500 92[334 260 334 584 737]13648 13742 500]"},
            new byte[]{8,1,2,11,6,0,0,0,0,0,0,0}, //panose
            new String[]{"UniCNS-UCS2-H","UniCNS-UCS2-V"}, // allowed cmaps
            new int[][]{MHei_Medium_UniCNS_UCS2_H, null, MHei_Medium_UniCNS_UCS2_H, null} //metrics (two for each cmap, search first then second}
        },
        new Object[]{
            "MSung-Light", //font name
            new int[]{880,880,-120,6,-160,-249,1015,888,0,93,0}, //font descriptor (Ascent,CapHeight,Descent,Flags,FontBBox,ItalicAngle,StemV,Supplement
            //font descriptor registry, ordering, W
            new String[]{"Adobe", "CNS1", "[1 2 250 3[408 668 490 875 698 250]9 10 240 11[417 667 250 313 250 520]17 26 500 27 28 250 29 31 667 32[396 921 677 615 719 760 625 552 771 802]42 43 354 44[781 604 927 750 823 563 823 729 542 698 771 729 948 771 677 635 344 520 344 469 500 250 469 521 427 521 438 271 469 531]74 75 250 76[458 240 802 531 500]81 82 521 83[365 333 292 521 458 677 479 458 427 480 496 480 667 760 980]13648 13742 500]"},
            new byte[]{1,5,2,2,4,0,0,0,0,0,0,0}, //panose
            new String[]{"UniCNS-UCS2-H","UniCNS-UCS2-V"}, // allowed cmaps
            new int[][]{MSung_Light_UniCNS_UCS2_H, null, MSung_Light_UniCNS_UCS2_H, null} //metrics (two for each cmap, search first then second}
        },
        new Object[]{
            "HeiseiKakuGo-W5", //font name
            new int[]{875,718,-125,4,-92,-250,1010,922,0,93,2}, //font descriptor (Ascent,CapHeight,Descent,Flags,FontBBox,ItalicAngle,StemV,Supplement
            //font descriptor registry, ordering, W
            new String[]{"Adobe", "Japan1", "[1 2 278 3[355]4 5 556 6[889 667 191]9 10 333 11[389 584 278 333]15 16 278 17 26 556 27 28 278 29 31 584 32[556 1015]34 35 667 36 37 722 38[667 611 778 722 278 500 667 556 833 722 778 667 778 722 667 611 722 667 944]57 58 667 59[611 278 556 278 469 556 333]66 67 556 68[500]69 70 556 71[278]72 73 556 74 75 222 76[500 222 833]79 82 556 83[333 500 278 556 500 722]89 91 500 92[334 260 334 333 222 278 222 260 584 333]102 103 556 104[167]105 107 556 108[333 556]110 111 333 112 113 500 114 116 556 117[278 537 350 222]121 122 333 123[556]126[611]127 137 333 140[370 556 778 1000 365 889 278 222 611 944 611 584 737 584 737 400 584]157 158 333 159[556 333]161 163 834 164 169 667 170[722]171 174 667 175 178 278 179 180 722 181 185 778 186[584]187 190 722 191 192 667 193 198 556 199[500]200 203 556 204 207 278 208 214 556 215[584]216 219 556 220[500 556 500]223 224 667 225[611 556 500 1000 500 556]231 632 500 8718[500]]"},
            new byte[]{8,1,2,11,6,0,0,0,0,0,0,0}, //panose
            new String[]{"UniJIS-UCS2-H","UniJIS-UCS2-V","UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-V"}, // allowed cmaps
            new int[][]{HeiseiKakuGo_W5_UniJIS_UCS2_H, null, HeiseiKakuGo_W5_UniJIS_UCS2_H, null, HeiseiKakuGo_W5_UniJIS_UCS2_HW_H, HeiseiKakuGo_W5_UniJIS_UCS2_H,
            HeiseiKakuGo_W5_UniJIS_UCS2_HW_V, HeiseiKakuGo_W5_UniJIS_UCS2_H} //metrics (two for each cmap, search first then second}
        },
        new Object[]{
            "HeiseiMin-W3", //font name
            new int[]{857,718,-143,6,-123,-257,1001,910,0,93,2}, //font descriptor (Ascent,CapHeight,Descent,Flags,FontBBox,ItalicAngle,StemV,Supplement
            //font descriptor registry, ordering, W
            new String[]{"Adobe", "Japan1", "[1[250 333 408]4 5 500 6[833 778 180]9 10 333 11[500 564 250 333 250 278]17 26 500 27 28 278 29 31 564 32[444 921 722]35 36 667 37[722 611 556]40 41 722 42[333 389 722 611 889]47 48 722 49[556 722 667 556 611]54 55 722 56[944]57 58 722 59[611 333 500 333 469 500 333 444 500 444 500 444 333]72 73 500 74 75 278 76[500 278 778]79 82 500 83[333 389 278]86 87 500 88[722]89 90 500 91[444 480 200 480]95 96 333 97[278 333 200 541 333]102 103 500 104[167]105 107 500 108[444 500]110 111 333 112 113 556 114 116 500 117[250 453 350 333]121 122 444 123[500]126[444]127 137 333 138[1000 889 276 611 722 889 310 667]146 147 278 148[500 722 500 564 760 564 760 400 564]157 158 300 159[500 300]161 163 750 164 169 722 170[667]171 174 611 175 178 333 179 185 722 186[564]187 191 722 192[556]193 203 444 204 207 278 208 214 500 215[564]216 222 500 223[556 722 611 500 389 980 444]230 632 500 8718[500]]"},
            new byte[]{1,5,2,2,4,0,0,0,0,0,0,0}, //panose
            new String[]{"UniJIS-UCS2-H","UniJIS-UCS2-V","UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-V"}, // allowed cmaps
            new int[][]{HeiseiMin_W3_UniJIS_UCS2_H, null, HeiseiMin_W3_UniJIS_UCS2_H, null, HeiseiMin_W3_UniJIS_UCS2_HW_H, HeiseiMin_W3_UniJIS_UCS2_H,
            HeiseiMin_W3_UniJIS_UCS2_HW_V, HeiseiMin_W3_UniJIS_UCS2_H} //metrics (two for each cmap, search first then second}
        },
        new Object[]{
            "HYGoThic-Medium", //font name
            new int[]{880,880,-120,4,-6,-145,1003,880,0,93,1}, //font descriptor (Ascent,CapHeight,Descent,Flags,FontBBox,ItalicAngle,StemV,Supplement
            //font descriptor registry, ordering, W
            new String[]{"Adobe", "Korea1", "[1[333]2 3 416 4[833 666 916 750 250]9 10 416 11[583 833 375 833]15 16 375 17 26 583 27 28 416 29 31 833 32[583 1000 666 708]36 37 750 38[666 625 833 750 291 541 708 583 875 750 791 666 791 708 666 583 750 625 916]57 59 625 60[416 375 416]63 65 500 66[583 625 583 625 583 375 625 583]74 75 250 76[541 250 916]79 82 625 83[333 541 333 583 500 750]89 94 500 95[750 958 500 881 963]8094 8190 500]"},
            new byte[]{8,1,2,11,6,0,0,0,0,0,0,0}, //panose
            new String[]{"UniKS-UCS2-H","UniKS-UCS2-V"}, // allowed cmaps
            new int[][]{HYGoThic_Medium_UniKS_UCS2_H, null, HYGoThic_Medium_UniKS_UCS2_H, null} //metrics (two for each cmap, search first then second}
        },
        new Object[]{
            "HYSMyeongJo-Medium", //font name
            new int[]{880,880,-120,6,0,-148,1001,880,0,93,1}, //font descriptor (Ascent,CapHeight,Descent,Flags,FontBBox,ItalicAngle,StemV,Supplement
            //font descriptor registry, ordering, W
            new String[]{"Adobe", "Korea1", "[1[333]2 3 416 4[833 625 916 833 250]9 11 500 12[833 291 833 291 375]17 26 625 27 28 333 29 30 833 31[916 500 1000 791]35 36 708 37[750 708 666 750 791 375 500 791 666 916 791 750 666 750 708 666]53 54 791 55[750 1000]57 58 708 59[666 500 375]62 64 500 65[333 541 583 541]69 70 583 71[375]72 73 583 74[291 333 583 291 875]79 82 583 83[458 541 375]86 87 583 88[833]89 90 625 91[500]92 94 583 95[750 1000 500]8094 8190 500]"},
            new byte[]{1,5,2,2,4,0,0,0,0,0,0,0}, //panose
            new String[]{"UniKS-UCS2-H","UniKS-UCS2-V"}, // allowed cmaps
            new int[][]{HYSMyeongJo_Medium_UniKS_UCS2_H, null, HYSMyeongJo_Medium_UniKS_UCS2_H, null} //metrics (two for each cmap, search first then second}
        }
    };
    
/** The font name */
    private String fontName;
/** The style modifier */
    private String style = "";
/** The CMap associated with this font */
    private String CMap;
/** The descriptor information of type <CODE>int</Code> */
    private int fdescInt[];
/** The descriptor information of type <CODE>String</Code> */
    private String fdescStr[];
/** The panose information */
    private byte panose[];
/** The first metric array to search */
    private int metrics1[];
/** The second metric array to search if the search failled on the first */
    private int metrics2[];
    /** Creates a CJK font.
     * @param fontName the name of the font
     * @param enc the encoding of the font
     * @param emb always <CODE>false</CODE>. CJK font and not embedded
     * @throws DocumentException on error
     * @throws IOException on error
     */    
    CJKFont(String fontName, String enc, boolean emb) throws DocumentException, IOException
    {
        fontType = FONT_TYPE_CJK;
        String nameBase = getBaseName(fontName);
        if (!isCJKFont(nameBase, enc))
            throw new DocumentException("Font '" + fontName + "' with '" + enc + "' encoding is not a CJK font.");
        if (nameBase.length() < fontName.length()) {
            style = fontName.substring(nameBase.length());
            fontName = nameBase;
        }
        encoding = CJK_ENCODING;
        for (int k = 0; k < cjk.length; ++k) {
            Object obj[] = (Object[])cjk[k];
            String name = (String)obj[0];
            if (name.equals(fontName)) {
                String tenc[] = (String[])obj[4];
                for (int j = 0; j < tenc.length; ++j) {
                    if (enc.equals(tenc[j])) {
                        this.fontName = fontName;
                        CMap = enc;
                        fdescInt = (int[])obj[1];
                        fdescStr = (String[])obj[2];
                        int met[][] = (int[][])obj[5];
                        metrics1 = met[j << 1];
                        metrics2 = met[(j << 1) + 1];
                        panose = (byte[])obj[3];
                        return;
                    }
                }
            }
        }
    }
    
    /** Checks if its a valid CJK font.
     * @param fontName the font name
     * @param enc the encoding
     * @return <CODE>true</CODE> if it is CJK font
     */    
    public static boolean isCJKFont(String fontName, String enc)
    {
        for (int k = 0; k < cjk.length; ++k) {
            Object obj[] = (Object[])cjk[k];
            String name = (String)obj[0];
            if (name.equals(fontName)) {
                String tenc[] = (String[])obj[4];
                for (int j = 0; j < tenc.length; ++j) {
                    if (enc.equals(tenc[j]))
                        return true;
                }
                return false;
            }
        }
        return false;
    }
    
    public static boolean isCJKEncoding(String enc)
    {
        for (int k = 0; k < cjk.length; ++k) {
            Object obj[] = (Object[])cjk[k];
            String tenc[] = (String[])obj[4];
            for (int j = 0; j < tenc.length; ++j) {
                if (enc.equals(tenc[j]))
                    return true;
            }
        }
        return false;
    }
    
    public static Object[] getCharacterCollection(String enc) {
        for (int k = 0; k < cjk.length; ++k) {
            Object obj[] = (Object[])cjk[k];
            String tenc[] = (String[])obj[4];
            for (int j = 0; j < tenc.length; ++j) {
                if (enc.equals(tenc[j])) {
                    tenc = (String[])obj[2];
                    return new Object[]{tenc[0], tenc[1], new Integer(((int[])obj[1])[10])};
                }
            }
        }
        return null;
    }
    
    public int getWidth(String text)
    {
        int total = 0;
        for (int k = 0; k < text.length(); ++k) {
            int c = text.charAt(k);
            int v = getValueByKey(metrics1, c);
            if (v >= 0) {
                total += v;
                continue;
            }
            if (metrics2 == null)
                total += 1000;
            else {
                v = getValueByKey(metrics2, c);
                if (v >= 0)
                    total += v;
                else
                    total += 1000;
            }
        }
        return total;
    }
    
    protected int getRawWidth(int c, String name)
    {
        return 0;
    }
    public int getKerning(char char1, char char2)
    {
        return 0;
    }
    public static int getValueByKey(int a[], int key)
    {
        int low = 0;
        int high = a.length / 2 -1;
        while (low <= high) {
            int mid =(low + high)/2;
            int midVal = a[mid << 1];
            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return a[(mid << 1) + 1]; // key found
        }
        return -1;  // key not found.
    }
    private PdfDictionary getFontDescriptor() throws DocumentException
    {
        PdfDictionary dic = new PdfDictionary(new PdfName("FontDescriptor"));
        dic.put(new PdfName("Ascent"), new PdfNumber(fdescInt[0]));
        dic.put(new PdfName("CapHeight"), new PdfNumber(fdescInt[1]));
        dic.put(new PdfName("Descent"), new PdfNumber(fdescInt[2]));
        dic.put(new PdfName("Flags"), new PdfNumber(fdescInt[3]));
        dic.put(new PdfName("FontBBox"), new PdfRectangle(fdescInt[4], fdescInt[5], fdescInt[6], fdescInt[7]));
        dic.put(new PdfName("FontName"), new PdfName(fontName + style));
        dic.put(new PdfName("ItalicAngle"), new PdfNumber(fdescInt[8]));
        dic.put(new PdfName("StemV"), new PdfNumber(fdescInt[9]));
        PdfDictionary pdic = new PdfDictionary();
        pdic.put(PdfName.PANOSE, new PdfStringLiteral(panose));
        dic.put(new PdfName("Style"), pdic);
        return dic;
    }
    
    private PdfDictionary getCIDFont(PdfIndirectReference fontDescriptor) throws DocumentException
    {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, new PdfName("CIDFontType0"));
        dic.put(new PdfName("BaseFont"), new PdfName(fontName + style));
        dic.put(new PdfName("FontDescriptor"), fontDescriptor);
        dic.put(new PdfName("DW"), new PdfNumber(1000));
        dic.put(new PdfName("W"), new PdfLiteral(fdescStr[2]));
        PdfDictionary cdic = new PdfDictionary();
        cdic.put(PdfName.REGISTRY, new PdfString(fdescStr[0]));
        cdic.put(PdfName.ORDERING, new PdfString(fdescStr[1]));
        cdic.put(PdfName.SUPPLEMENT, new PdfNumber(fdescInt[10]));
        dic.put(new PdfName("CIDSystemInfo"), cdic);
        return dic;
    }
    
    private PdfDictionary getFontBaseType(PdfIndirectReference CIDFont) throws DocumentException
    {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, new PdfName("Type0"));
        String name = fontName;
        if (style.length() > 0)
            name += "-" + style.substring(1);
        name += "-" + CMap;
        dic.put(new PdfName("BaseFont"), new PdfName(name));
        dic.put(new PdfName("Encoding"), new PdfName(CMap));
        dic.put(new PdfName("DescendantFonts"), new PdfArray(CIDFont));
        return dic;
    }
    
/** Generates the dictionary or stream required to represent the font.
 *  <CODE>index</CODE> will cycle from 0 to 2 with the next cycle beeing fed
 *  with the indirect reference from the previous cycle.
 * @param iobj an indirect reference to a Pdf object. May be null
 * @param index the type of object to generate. It may be 0, 1 or 2
 * @return the object requested
 * @throws DocumentException error in generating the object
 */
    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object params[]) throws DocumentException, IOException {
        PdfIndirectReference ind_font = null;
        PdfObject pobj = null;
        PdfIndirectObject obj = null;
        pobj = getFontDescriptor();
        if (pobj != null){
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        pobj = getCIDFont(ind_font);
        if (pobj != null){
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        pobj = getFontBaseType(ind_font);
        writer.addToBody(pobj, ref);
    }
    
    /** Gets the font parameter identified by <CODE>key</CODE>. Valid values
     * for <CODE>key</CODE> are <CODE>ASCENT</CODE>, <CODE>CAPHEIGHT</CODE>, <CODE>DESCENT</CODE>
     * and <CODE>ITALICANGLE</CODE>.
     * @param key the parameter to be extracted
     * @param fontSize the font size in points
     * @return the parameter in points
     */    
    public float getFontDescriptor(int key, float fontSize) {
        switch (key) {
            case ASCENT:
                return fdescInt[0] * fontSize / 1000;
            case CAPHEIGHT:
                return fdescInt[1] * fontSize / 1000;
            case DESCENT:
                return fdescInt[2] * fontSize / 1000;
            case ITALICANGLE:
                return fdescInt[8];
            case BBOXLLX:
                return fontSize * fdescInt[4] / 1000;
            case BBOXLLY:
                return fontSize * fdescInt[5] / 1000;
            case BBOXURX:
                return fontSize * fdescInt[6] / 1000;
            case BBOXURY:
                return fontSize * fdescInt[7] / 1000;
        }
        return 0;
    }
    
    public String getPostscriptFontName() {
        return fontName;
    }
    
    /** Gets the full name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the full name of the font
     */
    public String[][] getFullFontName() {
        return new String[][]{{"", "", "", fontName}};
    }
    
}