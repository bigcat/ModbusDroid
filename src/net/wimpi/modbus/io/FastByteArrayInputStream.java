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
 * This class has been derived from FastInputStream code provided
 * with the Berkeley DB under following license conditions.
 *
 * Modifications include the naming, coding style as well as various
 * changed to make the class an extendable drop-in replacement for
 * ByteArrayInputStream.
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
import java.io.InputStream;

/**
 * This class is a replacement for ByteArrayInputStream that
 * does not synchronize every byte read.
 * <p/>
 *
 * @author Mark Hayes
 * @author Dieter Wimberger
 * 
 * @version 1.2rc1 (09/11/2004)
 */
public class FastByteArrayInputStream
    extends InputStream {

  /**
   * Number of bytes in the input buffer.
   */
  protected int count;
  /**
   * Actual position pointer into the input buffer.
   */
  protected int pos;

  /**
   * Marked position pointer into the input buffer.
   */
  protected int mark;
  /**
   * Input buffer <tt>byte[]</tt>.
   */
  protected byte[] buf;

  /**
   * Creates an input stream.
   *
   * @param buffer the data to read.
   */
  public FastByteArrayInputStream(byte[] buffer) {
    buf = buffer;
    count = buffer.length;
    pos = 0;
    mark = 0;
    //System.out.println("Buffer length="+buf.length );
    //System.out.println("count=" + count + " pos=" + pos);
  }//constructor

  /**
   * Creates an input stream.
   *
   * @param buffer the data to read.
   * @param offset the byte offset at which to begin reading.
   * @param length the number of bytes to read.
   */
  public FastByteArrayInputStream(byte[] buffer, int offset, int length) {
    buf = buffer;
    pos = offset;
    count = length;
  }//constructor

  // --- begin ByteArrayInputStream compatible methods ---

  public int available() {
    return count - pos;
  }//available

  public boolean markSupported() {
    return true;
  }//markSupported

  public void mark(int readlimit) {
    //System.out.println("mark()");
    mark = pos;
    //System.out.println("mark=" + mark + " pos=" + pos);
  }//mark

  public void reset() {
    //System.out.println("reset()");
    pos = mark;
    //System.out.println("mark=" + mark + " pos=" + pos);
  }//reset

  public long skip(long count) {
    int myCount = (int) count;
    if (myCount + pos > this.count) {
      myCount = this.count - pos;
    }
    pos += myCount;
    return myCount;
  }//skip

  public int read() throws IOException {
    //System.out.println("read()");
    //System.out.println("count=" + count + " pos=" + pos);
    return (pos < count) ? (buf[pos++] & 0xff) : (-1);
  }//read

  public int read(byte[] toBuf) throws IOException {
    //System.out.println("read(byte[])");
    return read(toBuf, 0, toBuf.length);
  }//read

  public int read(byte[] toBuf, int offset, int length)
      throws IOException {
    //System.out.println("read(byte[],int,int)");
    int avail = count - pos;
    if (avail <= 0) {
      return -1;
    }
    if (length > avail) {
      length = avail;
    }
    for (int i = 0; i < length; i++) {
      toBuf[offset++] = buf[pos++];
    }
    return length;
  }//read

  // --- end ByteArrayInputStream compatible methods ---

  /**
   * Returns the underlying data being read.
   *
   * @return the underlying data.
   */
  public byte[] getBufferBytes() {
    return buf;
  }//getBufferBytes

  /**
   * Returns the offset at which data is being read from the buffer.
   *
   * @return the offset at which data is being read.
   */
  public int getBufferOffset() {
    return pos;
  }//getBufferOffset

  /**
   * Returns the end of the buffer being read.
   *
   * @return the end of the buffer.
   */
  public int getBufferLength() {
    return count;
  }//getBufferLength

}//class FastByteArrayInputStream
