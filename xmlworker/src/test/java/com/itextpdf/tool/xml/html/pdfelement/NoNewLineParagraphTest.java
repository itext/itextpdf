package com.itextpdf.tool.xml.html.pdfelement;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;

/**
 * @author Amedee Van Gasse
 *
 */
public class NoNewLineParagraphTest {

    private static final String IMAGE = "src/test/resources/images.jpg";
    private NoNewLineParagraph paragraph;
    private Element jpegImage;

    @Before
    public void setup()
            throws BadElementException, MalformedURLException, IOException {
        jpegImage = Image.getInstance(IMAGE);
        paragraph = new NoNewLineParagraph();
    }

    /**
     * Test method for
     * {@link com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph#add(com.itextpdf.text.Element)}
     * .
     */
    @Test
    public void testAddImageToParagraph() {
        final String message = "Could not add " + jpegImage.getClass().getName()
                + " to " + paragraph.getClass().getName();
        assertTrue(message, paragraph.add(jpegImage));
    }

}
