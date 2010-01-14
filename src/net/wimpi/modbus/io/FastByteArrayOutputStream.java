//License
/***
 * Java Modbus Library (jamod)
 * Copyright (c) 2002-2004, jamod development team
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the author nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS ``AS
 * IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ***/
/***
 * This class has been derived from FastOutputStream code provided
 * with the Berkeley DB under following license conditions.
 *
 * Modifications include the naming, coding style as well as various
 * changed to make the class an extendable drop-in replacement for
 * ByteArrayOutputStream.
 *
 * Copyright (c) 2000-2003
 *      Sleepycat Software.  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Redistributions in any form must be accompanied by information on
 *    how to obtain complete source code for the DB software and any
 *    accompanying software that uses the DB software.  The source code
 *    must either be included in the distribution or be available for no
 *    more than the cost of distribution plus a nominal fee, and must be
 *    freely redistributable under reasonable conditions.  For an
 *    executable file, complete source code means the source code for all
 *    modules it contains.  It does not include source code for modules or
 *    files that typically accompany the major components of the operating
 *    system on which the executable file runs.
 *
 * THIS SOFTWARE IS PROVIDED BY SLEEPYCAT SOFTWARE ``AS IS'' AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT, ARE DISCLAIMED.  IN NO EVENT SHALL SLEEPYCAT SOFTWARE
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 ***/
package net.wimpi.modbus.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * This class is a replacement implementation for ByteArrayOutputStream
 * that does not synchronize every
 * byte written.
 * <p/>
 * 
 * @author Mark Hayes
 * @author Dieter Wimberger
 * 
 * @version 1.2rc1 (09/11/2004)
 */
public class FastByteArrayOutputStream
    extends OutputStream {

  /**
   * Defines the default oputput buffer size (100 bytes).
   */
  public static final int DEFAULT_INIT_SIZE = 100;
  /**
   * Defines the default increment of the output buffer size
   * (100 bytes).
   */
  public static final int DEFAULT_BUMP_SIZE = 100;

  /**
   * Number of bytes in the output buffer.
   */
  protected int count;

  /**
   * Increment of the output buffer size on overflow.
   */
  protected int bumpLen;

  /**
   * Output buffer <tt>byte[]</tt>.
   */
  protected byte[] buf;

  /**
   * Creates an output stream with default sizes.
   */
  public FastByteArrayOutputStream() {
    buf = new byte[DEFAULT_INIT_SIZE];
    bumpLen = DEFAULT_BUMP_SIZE;
  }//constructor

  /**
   * Creates an output stream with a default bump size and a given initial
   * size.
   *
   * @param initialSize the initial size of the buffer.
   */
  public FastByteArrayOutputStream(int initialSize) {
    buf = new byte[initialSize];
    bumpLen = DEFAULT_BUMP_SIZE;
  }//constructor

  /**
   * Creates an output stream with a given bump size and initial size.
   *
   * @param initialSize the initial size of the buffer.
   * @param bumpSize    the amount to increment the buffer.
   */
  public FastByteArrayOutputStream(int initialSize, int bumpSize) {
    buf = new byte[initialSize];
    bumpLen = bumpSize;
  }//constructor

  /**
   * Creates an output stream with a given initial buffer and a default
   * bump size.
   *
   * @param buffer the initial buffer; will be owned by this object.
   */
  public FastByteArrayOutputStream(byte[] buffer) {
    buf = buffer;
    bumpLen = DEFAULT_BUMP_SIZE;
  }//constructor

  /**
   * Creates an output stream with a given initial buffer and a given
   * bump size.
   *
   * @param buffer   the initial buffer; will be owned by this object.
   * @param bumpSize the amount to increment the buffer.
   */
  public FastByteArrayOutputStream(byte[] buffer, int bumpSize) {
    buf = buffer;
    bumpLen = bumpSize;
  }//constructor

  // --- begin ByteArrayOutputStream compatible methods ---

  /**
   * Returns the number of bytes written to this
   * <tt>FastByteArrayOutputStream</tt>.
   *
   * @return the number of bytes written as <tt>int</tt>.
   */
  public int size() {
    return count;
  }//size

  /**
   * Resets this <tt>FastByteArrayOutputStream</tt>.
   */
  public void reset() {
    count = 0;
  }//reset

  public void write(int b) throws IOException {
    if (count + 1 > buf.length) {
      bump(1);
    }
    buf[count++] = (byte) b;
  }//write

  public void write(byte[] fromBuf) throws IOException {
    int needed = count + fromBuf.length - buf.length;
    if (needed > 0) {
      bump(needed);
    }
    for (int i = 0; i < fromBuf.length; i++) {
      buf[count++] = fromBuf[i];
    }
  }//write

  public void write(byte[] fromBuf, int offset, int length)
      throws IOException {

    int needed = count + length - buf.length;
    if (needed > 0) {
      bump(needed);
    }
    int fromLen = offset + length;

    for (int i = offset; i < fromLen; i++) {
      buf[count++] = fromBuf[i];
    }
  }//write

  /**
   * Writes the content of this <tt>FastByteArrayOutputStream</tt>
   * to the given output stream.
   *
   * @param out the output stream to be written to.
   *
   * @throws IOException if an I/O error occurs.
   */
  public synchronized void writeTo(OutputStream out)
      throws IOException {
    out.write(buf, 0, count);
  }//writeTo

  public String toString() {
    return new String(buf, 0, count);
  }//toString

  /**
   * Returns the content of this <tt>FastByteArrayOutputStream</tt>
   * as String.
   *
   * @param encoding the encoding to be used for conversion.
   * @return a newly allocated String.
   *
   * @throws UnsupportedEncodingException if the given encoding is not supported.
   */
  public String toString(String encoding)
      throws UnsupportedEncodingException {
    return new String(buf, 0, count, encoding);
  }//toString

  /**
   * Returns the written bytes in a newly allocated byte[]
   * of length getSize().
   *
   * @return a newly allocated byte[] with the content of the
   *         output buffer.
   */
  public byte[] toByteArray() {
    byte[] toBuf = new byte[count];
    System.arraycopy(buf,0,toBuf,0,count);
    //for (int i = 0; i < count; i++) {
    //  toBuf[i] = buf[i];
    //}
    return toBuf;
  }//toByteArray

  // --- end ByteArrayOutputStream compatible methods ---

  /**
   * Copy the buffered data to the given array.
   *
   * @param toBuf  the buffer to hold a copy of the data.
   * @param offset the offset at which to start copying.
   */
  public void toByteArray(byte[] toBuf, int offset) {
    int toLen = (toBuf.length > count) ? count : toBuf.length;
    for (int i = offset; i < toLen; i++) {
      toBuf[i] = buf[i];
    }
  }//toByteArray

  /**
   * Returns the buffer owned by this object.
   *
   * @return the buffer.
   */
  public byte[] getBufferBytes() {
    return buf;
  }//getBufferBytes

  /**
   * Returns the offset of the internal buffer.
   *
   * @return always zero currently.
   */
  public int getBufferOffset() {
    return 0;
  }//getBufferOffset

  /**
   * Returns the length used in the internal buffer, that is, the offset at
   * which data will be written next.
   *
   * @return the buffer length.
   */
  public int getBufferLength() {
    return count;
  }//getBufferLength

  /**
   * Ensure that at least the given number of bytes are available in the
   * internal buffer.
   *
   * @param sizeNeeded the number of bytes desired.
   */
  public void makeSpace(int sizeNeeded) {

    int needed = count + sizeNeeded - buf.length;
    if (needed > 0) {
      bump(needed);
    }
  }//makeSpace

  /**
   * Skip the given number of bytes in the buffer.
   *
   * @param sizeAdded number of bytes to skip.
   */
  public void addSize(int sizeAdded) {
    count += sizeAdded;
  }//addSize

  private void bump(int needed) {

    byte[] toBuf = new byte[buf.length + needed + bumpLen];

    for (int i = 0; i < count; i++) {
      toBuf[i] = buf[i];
    }
    buf = toBuf;
  }//bump


}//class FastByteArrayOutputStream
