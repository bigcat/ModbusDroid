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
package net.wimpi.modbus.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Class implementing a byte array input stream with
 * a DataInput interface.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class BytesInputStream
    extends FastByteArrayInputStream
    implements DataInput {

  DataInputStream m_Din;

  /**
   * Constructs a new <tt>BytesInputStream</tt> instance,
   * with an empty buffer of a given size.
   *
   * @param size the size of the input buffer.
   */
  public BytesInputStream(int size) {
    super(new byte[size]);
    m_Din = new DataInputStream(this);
  }//BytesInputStream

  /**
   * Constructs a new <tt>BytesInputStream</tt> instance,
   * that will read from the given data.
   *
   * @param data a byte array containing data to be read.
   */
  public BytesInputStream(byte[] data) {
    super(data);
    m_Din = new DataInputStream(this);
  }//BytesInputStream

  /**
   * Resets this <tt>BytesInputStream</tt> using the given
   * byte[] as new input buffer.
   *
   * @param data a byte array with data to be read.
   */
  public void reset(byte[] data) {
   // System.out.println("reset(byte[])::count=" + count + " pos=" + pos);
    pos = 0;
    mark = 0;
    buf = data;
    count = data.length;
  }//reset

  /**
   * Resets this <tt>BytesInputStream</tt> using the given
   * byte[] as new input buffer and a given length.
   *
   * @param data a byte array with data to be read.
   * @param length the length of the buffer to be considered.
   */
  public void reset(byte[] data, int length) {
    pos = 0;
    mark = 0;
    count = length;
    buf = data;
    //System.out.println("reset(byte[],int)::count=" + count + " pos=" + pos);
  }//reset

  /**
   * Resets this <tt>BytesInputStream</tt>  assigning the input buffer
   * a new length.
   *
   * @param length the length of the buffer to be considered.
   */
  public void reset(int length) {
    //System.out.println("reset(int)::count=" + count + " pos=" + pos);
    pos = 0;
    count = length;
  }//reset

  /**
   * Skips the given number of bytes or all bytes till the end
   * of the assigned input buffer length.
   *
   * @param n the number of bytes to be skipped as <tt>int</tt>.
   * @return the number of bytes skipped.
   */
  public int skip(int n) {
    mark(pos);
    pos += n;
    return n;
  }//skip

  /**
   * Returns the reference to the input buffer.
   *
   * @return the reference to the <tt>byte[]</tt> input buffer.
   */
  public byte[] getBuffer() {
    return buf;
  }//getBuffer

  public int getBufferLength() {
    return buf.length;
  }//getBufferLength

  public void readFully(byte b[])
      throws IOException {
    m_Din.readFully(b);
  }//readFully

  public void readFully(byte b[], int off, int len)
      throws IOException {
    m_Din.readFully(b, off, len);
  }//readFully

  public int skipBytes(int n)
      throws IOException {
    return m_Din.skipBytes(n);
  }//skipBytes

  public boolean readBoolean()
      throws IOException {
    return m_Din.readBoolean();
  }//readBoolean

  public byte readByte()
      throws IOException {
    return m_Din.readByte();
  }

  public int readUnsignedByte()
      throws IOException {
    return m_Din.readUnsignedByte();
  }//readUnsignedByte

  public short readShort()
      throws IOException {
    return m_Din.readShort();
  }//readShort

  public int readUnsignedShort()
      throws IOException {
    return m_Din.readUnsignedShort();
  }//readUnsignedShort

  public char readChar()
      throws IOException {
    return m_Din.readChar();
  }//readChar

  public int readInt()
      throws IOException {
    return m_Din.readInt();
  }//readInt

  public long readLong()
      throws IOException {
    return m_Din.readLong();
  }//readLong

  //

  public float readFloat()
      throws IOException {
    return m_Din.readFloat();
  }//readFloat

  public double readDouble()
      throws IOException {
    return m_Din.readDouble();
  }//readDouble
  //

  public String readLine()
      throws IOException {
    throw new IOException("Not supported.");
  }//readLine

  public String readUTF()
      throws IOException {
    return m_Din.readUTF();
  }//readUTF

}//class BytesInputStream

