/*
 * MakeCJK.java
 *
 * Created on March 15, 2001, 3:01 PM
 */

package com.lowagie.text.pdf;

/**
 *
 * @author  psoares
 * @version 
 */
public class MakeCJK
{
    static Object mapas[] =
    {
        new String[]{"STSong-Light","UniGB-UCS2-H","UniGB-UCS2-V"},
        new String[]{"MHei-Medium","UniCNS-UCS2-H","UniCNS-UCS2-V"},
        new String[]{"MSung-Light","UniCNS-UCS2-H","UniCNS-UCS2-V"},
        new String[]{"HeiseiKakuGo-W5","UniJIS-UCS2-H","UniJIS-UCS2-V","UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-V"},
        new String[]{"HeiseiMin-W3","UniJIS-UCS2-H","UniJIS-UCS2-V","UniJIS-UCS2-HW-H","UniJIS-UCS2-HW-V"},
        new String[]{"HYGoThic-Medium","UniKS-UCS2-H","UniKS-UCS2-V"},
        new String[]{"HYSMyeongJo-Medium","UniKS-UCS2-H","UniKS-UCS2-V"}
    };

    /** Creates new MakeCJK */
    public MakeCJK() {
    }

    public static void main(String[] args)
    {
        boolean cc;
        cc = CJKFont.isCJKFont("MHei-Medium", "UniCNS-UCS2-V");
        String fontPath = "C:\\Program Files\\Adobe\\Acrobat 4.0\\Resource\\CIDFont\\";
        String cmapPath = "C:\\Program Files\\Adobe\\Acrobat 4.0\\Resource\\CMap\\";
        String outPath = "c:\\genmapas\\";
        try {
            InputCMap mapa = new InputCMap();
            for (int k = 0; k < mapas.length; ++k) {
                String ff[] = (String[])mapas[k];
                for (int j = 1; j < ff.length; ++j) {
                    System.out.println(ff[0] + " " + ff[j]);
                    mapa.process(
                        cmapPath + ff[j],
                        fontPath + ff[0] + "-Acro",
                        outPath + ff[0] + "-" + ff[j] + ".txt");
                }
                    
                
            }
/*            mapa.process("C:\\Program Files\\Adobe\\Acrobat 4.0\\Resource\\CMap\\UniGB-UCS2-H",
                "C:\\Program Files\\Adobe\\Acrobat 4.0\\Resource\\CIDFont\\STSong-Light-Acro", "c:\\mapstsong.txt");
            mapa.process("C:\\Program Files\\Adobe\\Acrobat 4.0\\Resource\\CMap\\UniJIS-UCS2-HW-H",
                "C:\\Program Files\\Adobe\\Acrobat 4.0\\Resource\\CIDFont\\HeiseiKakuGo-W5-Acro", "c:\\mapjapn.txt");*/
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
