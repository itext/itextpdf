/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * A {@link java.nio.MappedByteBuffer} wrapped as a {@link java.io.RandomAccessFile}
 *
 * @author Joakim Sandstroem
 * Created on 6.9.2006
 */
public class MappedRandomAccessFile {

    private static final int BUFSIZE = 1 << 30;
    
    private FileChannel channel = null;
    private MappedByteBuffer[] mappedBuffers;
    private long size;
    private long pos;

    /**
     * Constructs a new MappedRandomAccessFile instance
     * @param filename String
     * @param mode String r, w or rw
     * @throws FileNotFoundException
     * @throws IOException
     */
    public MappedRandomAccessFile(String filename, String mode)
    throws FileNotFoundException, IOException {

        if (mode.equals("rw"))
            init(
                    new java.io.RandomAccessFile(filename, mode).getChannel(),
                    FileChannel.MapMode.READ_WRITE);
        else
            init(
                    new FileInputStream(filename).getChannel(),
                    FileChannel.MapMode.READ_ONLY);

    }

    /**
     * initializes the channel and mapped bytebuffer
     * @param channel FileChannel
     * @param mapMode FileChannel.MapMode
     * @throws IOException
     */
    private void init(FileChannel channel, FileChannel.MapMode mapMode)
    throws IOException {

        this.channel = channel;
        
        
        size = channel.size();
        pos = 0;
        int requiredBuffers = (int)(size/BUFSIZE) + (size % BUFSIZE == 0 ? 0 : 1);
        //System.out.println("This will require " + requiredBuffers + " buffers");
        
        mappedBuffers = new MappedByteBuffer[requiredBuffers];
        try{
            int index = 0;
            for(long offset = 0; offset < size; offset += BUFSIZE){
                long size2 = Math.min(size - offset, BUFSIZE);
                mappedBuffers[index] = channel.map(mapMode, offset, size2);
                mappedBuffers[index].load();
                index++;
            }
            if (index != requiredBuffers){
                throw new Error("Should never happen - " + index + " != " + requiredBuffers);
            }
        } catch (IOException e){
            close();
            throw e;
        } catch (RuntimeException e){
            close();
            throw e;
        }
        
    }

    /**
     * @since 2.0.8
     */
    public FileChannel getChannel() {
    	return channel;
    }

    /**
     * @see java.io.RandomAccessFile#read()
     * @return int next integer or -1 on EOF
     */
    public int read() {
        try {
            int mapN = (int) (pos / BUFSIZE);
            int offN = (int) (pos % BUFSIZE);
            
            if (mapN >= mappedBuffers.length) // we have run out of data to read from
                return -1;
            
            if (offN >= mappedBuffers[mapN].limit())
                return -1;
            
            byte b = mappedBuffers[mapN].get(offN);
            pos++;
            int n = b & 0xff;

            return n;
        } catch (BufferUnderflowException e) {
            return -1; // EOF
        }
    }

    /**
     * @see java.io.RandomAccessFile#read(byte[], int, int)
     * @param bytes byte[]
     * @param off int offset
     * @param len int length
     * @return int bytes read or -1 on EOF
     */
    public int read(byte bytes[], int off, int len) {
        int mapN = (int) (pos / BUFSIZE);
        int offN = (int) (pos % BUFSIZE);
        int totalRead = 0;
        
        while(totalRead < len){
            if (mapN >= mappedBuffers.length) // we have run out of data to read from
                break;
            MappedByteBuffer currentBuffer = mappedBuffers[mapN];
            if (offN > currentBuffer.limit())
                break;
            currentBuffer.position(offN);
            int bytesFromThisBuffer = Math.min(len - totalRead, currentBuffer.remaining());
            currentBuffer.get(bytes, off, bytesFromThisBuffer);
            off += bytesFromThisBuffer;
            pos += bytesFromThisBuffer;
            totalRead += bytesFromThisBuffer;

            mapN++;
            offN = 0;
            
        }
        return totalRead == 0 ? -1 : totalRead;
    }

    /**
     * @see java.io.RandomAccessFile#getFilePointer()
     * @return long
     */
    public long getFilePointer() {
        return pos;
    }

    /**
     * @see java.io.RandomAccessFile#seek(long)
     * @param pos long position
     */
    public void seek(long pos) {
        this.pos = pos;
    }

    /**
     * @see java.io.RandomAccessFile#length()
     * @return long length
     */
    public long length() {
        return size;
    }

    /**
     * @see java.io.RandomAccessFile#close()
     * Cleans the mapped bytebuffer and closes the channel
     */
    public void close() throws IOException {
        for(int i = 0; i < mappedBuffers.length; i++){
            if (mappedBuffers[i] != null){
                clean(mappedBuffers[i]);
                mappedBuffers[i] = null;
            }
        }

        if (channel != null)
            channel.close();
        channel = null;
    }

    /**
     * invokes the close method
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    /**
     * invokes the clean method on the ByteBuffer's cleaner
     * @param buffer ByteBuffer
     * @return boolean true on success
     */
    public static boolean clean(final java.nio.ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect())
            return false;

        Boolean b = (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                Boolean success = Boolean.FALSE;
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", (Class<?>[])null);
                    getCleanerMethod.setAccessible(true);
                    Object cleaner = getCleanerMethod.invoke(buffer, (Object[])null);
                    Method clean = cleaner.getClass().getMethod("clean", (Class<?>[])null);
                    clean.invoke(cleaner, (Object[])null);
                    success = Boolean.TRUE;
                } catch (Exception e) {
                    // This really is a show stopper on windows
                    //e.printStackTrace();
                }
                return success;
            }
        });

        return b.booleanValue();
    }

}
