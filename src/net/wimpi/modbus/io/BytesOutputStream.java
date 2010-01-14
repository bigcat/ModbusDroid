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

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Class implementing a byte array output stream with
 * a DataInput interface.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class BytesOutputStream
    extends FastByteArrayOutputStream
    implements DataOutput {

  private DataOutputStream m_Dout;

 /**
  * Constructs a new <tt>BytesOutputStream</tt> instance with
   * a new output buffer of the given size.
  *
  * @param size the size of the output buffer as <tt>int</tt>.
  */
  public BytesOutputStream(int size) {
    super(size);
    m_Dout = new DataOutputStream(this);
  }//BytesOutputStream

  /**
   * Constructs a new <tt>BytesOutputStream</tt> instance with
   * a given output buffer.
   *
   * @param buffer the output buffer as <tt>byte[]</tt>.
   */
  public BytesOutputStream(byte[] buffer) {
    buf = buffer;
    count = 0;
    m_Dout = new DataOutputStream(this);
  }//BytesOutputStream

  /**
   * Returns the reference to the output buffer.
   *
   * @return the reference to the <tt>byte[]</tt> output buffer.
   */
  public byte[] getBuffer() {
    return buf;
  }//getBuffer

  public void reset() {
    count = 0;
  }//reset

  public void writeBoolean(boolean v)
      throws IOException {
    m_Dout.writeBoolean(v);
  }//writeBoolean

  public void writeByte(int v)
      throws IOException {
    m_Dout.writeByte(v);
  }//writeByte

  public void writeShort(int v)
      throws IOException {
    m_Dout.writeShort(v);
  }//writeShort

  public void writeChar(int v)
      throws IOException {
    m_Dout.writeChar(v);
  }//writeChar

  public void writeInt(int v)
      throws IOException {
    m_Dout.writeInt(v);
  }//writeInt

  public void writeLong(long v)
      throws IOException {
    m_Dout.writeLong(v);
  }//writeLong

  //

  public void writeFloat(float v)
      throws IOException {
    m_Dout.writeFloat(v);
  }//writeFloat

  public void writeDouble(double v)
      throws IOException {
    m_Dout.writeDouble(v);
  }//writeDouble
  //

  public void writeBytes(String s)
      throws IOException {
    int len = s.length();
    for (int i = 0; i < len; i++) {
      this.write((byte) s.charAt(i));
    }
  }//writeBytes

  public void writeChars(String s)
      throws IOException {
    m_Dout.writeChars(s);
  }//writeChars

  public void writeUTF(String str)
      throws IOException {
    m_Dout.writeUTF(str);
  }//writeUTF

}//class BytesOutputStream
