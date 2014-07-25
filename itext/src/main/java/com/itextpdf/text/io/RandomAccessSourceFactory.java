/*
 * $Id: RandomAccessSourceFactory.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General License for more
 * details. You should have received a copy of the GNU Affero General License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General License.
 *
 * In accordance with Section 7(b) of the GNU Affero General License, a covered
 * work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.text.io;

import com.itextpdf.text.error_messages.MessageLocalization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;

/**
 * Factory to create {@link RandomAccessSource} objects based on various types of sources
 * @since 5.3.5
 *
 */

public final class RandomAccessSourceFactory {

	/**
	 * whether the full content of the source should be read into memory at construction
	 */
	private boolean forceRead = false;
	
	/**
	 * Whether {@link RandomAccessFile} should be used instead of a {@link FileChannel}, where applicable
	 */
	private boolean usePlainRandomAccess = false;
	
	/**
	 * Whether the underlying file should have a RW lock on it or just an R lock
	 */
	private boolean exclusivelyLockFile = false;
	
	/**
	 * Creates a factory that will give preference to accessing the underling data source using memory mapped files
	 */
	public RandomAccessSourceFactory() {
	}
	
	/**
	 * Determines whether the full content of the source will be read into memory
	 * @param forceRead true if the full content will be read, false otherwise
	 * @return this object (this allows chaining of method calls)
	 */
	public RandomAccessSourceFactory setForceRead(boolean forceRead){
		this.forceRead = forceRead;
		return this;
	}
	
	/**
	 * Determines whether {@link RandomAccessFile} should be used as the primary data access mechanism
	 * @param usePlainRandomAccess whether {@link RandomAccessFile} should be used as the primary data access mechanism
	 * @return this object (this allows chaining of method calls)
	 */
	public RandomAccessSourceFactory setUsePlainRandomAccess(boolean usePlainRandomAccess){
		this.usePlainRandomAccess = usePlainRandomAccess;
		return this;
	}
	
	public RandomAccessSourceFactory setExclusivelyLockFile(boolean exclusivelyLockFile){
		this.exclusivelyLockFile = exclusivelyLockFile;
		return this;
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on a byte array
	 * @param data the byte array
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createSource(byte[] data){
		return new ArrayRandomAccessSource(data); 
	}
	
	public RandomAccessSource createSource(RandomAccessFile raf) throws IOException {
		return new RAFRandomAccessSource(raf); 
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on a URL.  The data available at the URL is read into memory and used
	 * as the source for the {@link RandomAccessSource}
	 * @param url the url to read from
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createSource(URL url) throws IOException{
        InputStream is = url.openStream();
        try {
        	return createSource(is);
        }
        finally {
            try {is.close();}catch(IOException ioe){}
        }
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on an {@link InputStream}.  The full content of the InputStream is read into memory and used
	 * as the source for the {@link RandomAccessSource}
	 * @param is the stream to read from
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createSource(InputStream is) throws IOException{
       try {
        	return createSource(StreamUtil.inputStreamToArray(is));
        }
        finally {
            try {is.close();}catch(IOException ioe){}
        }		
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on a filename string.
	 * If the filename describes a URL, a URL based source is created
	 * If the filename describes a file on disk, the contents may be read into memory (if forceRead is true), opened using memory mapped file channel (if usePlainRandomAccess is false), or opened using {@link RandomAccessFile} access (if usePlainRandomAccess is true)
	 * This call will automatically failover to using {@link RandomAccessFile} if the memory map operation fails
	 * @param filename the name of the file or resource to create the {@link RandomAccessSource} for
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createBestSource(String filename) throws IOException{
		File file = new File(filename);
		if (!file.canRead()){
	        if (filename.startsWith("file:/")
	        		|| filename.startsWith("http://") 
	                || filename.startsWith("https://")
	                || filename.startsWith("jar:")
	                || filename.startsWith("wsjar:")
	                || filename.startsWith("wsjar:")
	                || filename.startsWith("vfszip:")) {
	        	return createSource(new URL(filename));
	        } else {
	        	return createByReadingToMemory(filename);
	        }
		}
	        
        if (forceRead){
        	return createByReadingToMemory(new FileInputStream(filename));
        }
		
        String openMode = exclusivelyLockFile ? "rw" : "r";
        
		RandomAccessFile raf = new RandomAccessFile(file, openMode);
    	if (exclusivelyLockFile){
    		raf.getChannel().lock();
    	}
    	
        if (usePlainRandomAccess){
        	return new RAFRandomAccessSource(raf);
        }
        
        try{
        	if (raf.length() <= 0) // files with zero length can't be mapped and will throw an IllegalArgumentException.  Just open using a simple RAF source.
        		return new RAFRandomAccessSource(raf); 
        	
			try{
				// ownership of the RAF passes to whatever source is created by createBestSource. 
				return createBestSource(raf.getChannel());
			} catch (MapFailedException e){
				return new RAFRandomAccessSource(raf);
			}
        } catch (IOException e){ // If RAFRandomAccessSource constructor or createBestSource throws, then we must close the RAF we created.
        	try{
        		raf.close();
        	} catch (IOException ignore){}
        	throw e;
        } catch (RuntimeException e){ // if we get a runtime exception during opening, we must close the RAF we created
        	try{
        		raf.close();
        	} catch (IOException ignore){}
        	throw e;
        }
	}
	
	/**
	 * Creates a {@link RandomAccessSource} based on memory mapping a file channel.
	 * Unless you are explicitly working with a FileChannel already, it is better to use
	 * {@link RandomAccessSourceFactory#createBestSource(String)}.
	 * If the file is large, it will be opened using a paging strategy.
	 * @param filename the name of the file or resource to create the {@link RandomAccessSource} for
	 * @return the newly created {@link RandomAccessSource}
	 */
	public RandomAccessSource createBestSource(FileChannel channel) throws IOException{
		if (channel.size() <= PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE){ // if less than the fully mapped usage of PagedFileChannelRandomAccessSource, just map the whole thing and be done with it
			return new GetBufferedRandomAccessSource(new FileChannelRandomAccessSource(channel));
		} else {
			return new GetBufferedRandomAccessSource(new PagedChannelRandomAccessSource(channel));
		}
	}
	
	public RandomAccessSource createRanged(RandomAccessSource source, long[] ranges) throws IOException{
		RandomAccessSource[] sources = new RandomAccessSource[ranges.length/2];
		for(int i = 0; i < ranges.length; i+=2){
			sources[i/2] = new WindowRandomAccessSource(source, ranges[i], ranges[i+1]);
		}
		return new GroupedRandomAccessSource(sources);
	}
	
	/**
	 * Creates a new {@link RandomAccessSource} by reading the specified file/resource into memory
	 * @param filename the name of the resource to read
	 * @return the newly created {@link RandomAccessSource}
	 * @throws IOException if reading the underling file or stream fails
	 */
	private RandomAccessSource createByReadingToMemory(String filename) throws IOException {
        InputStream is = StreamUtil.getResourceStream(filename);
        if (is == null)
            throw new IOException(MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", filename));
        return createByReadingToMemory(is);
	}
	
	/**
	 * Creates a new {@link RandomAccessSource} by reading the specified file/resource into memory
	 * @param is the name of the resource to read
	 * @return the newly created {@link RandomAccessSource}
	 * @throws IOException if reading the underling file or stream fails
	 */
	private RandomAccessSource createByReadingToMemory(InputStream is) throws IOException {
        try {
        	return new ArrayRandomAccessSource(StreamUtil.inputStreamToArray(is));
        }
        finally {
            try {is.close();}catch(IOException ioe){}
        }
	}
	

}
