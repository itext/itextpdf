/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package book.pdfabc.chapter05;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import java.io.IOException;

/**
 *
 * @author Bruno Lowagie (iText Software)
 */
public class C0505_SupportedEncoding {

    public static final String TYPE1 = "resources/fonts/cmr10.afm";
    public static final String OT_T1 = "resources/fonts/Puritan2.otf";
    public static final String OT_TT = "resources/fonts/OpenSans-Regular.ttf";
    
    public static void main(String[] args) throws DocumentException, IOException {
        C0505_SupportedEncoding app = new C0505_SupportedEncoding();
        app.listEncodings(BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED));
        app.listEncodings(BaseFont.createFont(TYPE1, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED));
        app.listEncodings(BaseFont.createFont(OT_T1, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED));
        app.listEncodings(BaseFont.createFont(OT_TT, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED));
    }
    
    public void listEncodings(BaseFont bf) {
        System.out.println(bf.getPostscriptFontName());
        String[] encoding = bf.getCodePagesSupported();
        for (String enc : encoding) {
            System.out.print('\t');
            System.out.println(enc);
        }
    }
}
