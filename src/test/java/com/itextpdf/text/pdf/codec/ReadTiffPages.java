package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Load a TIFF file and read every page. This should not throw an exception.
 *
 * @author Michael Demey
 */
public class ReadTiffPages {

    @Test
    public void loadPages() throws IOException {
        final String folderPath = "./src/test/resources/com/itextpdf/text/pdf/codec/tiffReadPages/";
        final File folder = new File(folderPath);

        final RandomAccessSourceFactory rasFactory = new RandomAccessSourceFactory();

        for ( String file : folder.list() ) {
            System.out.println(String.format("Reading TIFF %s", file));
            RandomAccessFileOrArray raf = new RandomAccessFileOrArray(rasFactory.createSource(new FileInputStream(folderPath + file)));

            int numberOfPages = TiffImage.getNumberOfPages(raf);

            for ( int currentPage = 1; currentPage <= numberOfPages; currentPage++ ) {
                TiffImage.getTiffImage(raf, currentPage);
            }
        }
    }
}