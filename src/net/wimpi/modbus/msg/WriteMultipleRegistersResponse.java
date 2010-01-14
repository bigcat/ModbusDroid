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
package net.wimpi.modbus.msg;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>WriteMultipleRegistersResponse</tt>.
 * The implementation directly correlates with the class 0
 * function <i>read multiple registers (FC 16)</i>. It encapsulates
 * the corresponding response message.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public final class WriteMultipleRegistersResponse
    extends ModbusResponse {

  //instance attributes
  private int m_WordCount;
  private int m_Reference;

  /**
   * Constructs a new <tt>WriteMultipleRegistersResponse</tt>
   * instance.
   */
  public WriteMultipleRegistersResponse() {
    super();
  }//constructor

  /**
   * Constructs a new <tt>WriteMultipleRegistersResponse</tt>
   * instance.
   *
   * @param reference the offset to start reading from.
   * @param wordcount the number of words (registers) to be read.
   */
  public WriteMultipleRegistersResponse(int reference, int wordcount) {
    super();
    m_Reference = reference;
    m_WordCount = wordcount;
    setDataLength(4);
  }//constructor

  /**
   * Sets the reference of the register to start writing to
   * with this <tt>WriteMultipleRegistersResponse</tt>.
   * <p>
   * @param ref the reference of the register
   *        to start writing to as <tt>int</tt>.
   */
  private void setReference(int ref) {
    m_Reference = ref;
  }//setReference

  /**
   * Returns the reference of the register to start
   * writing to with this
   * <tt>WriteMultipleRegistersResponse</tt>.
   * <p>
   * @return the reference of the register
   *        to start writing to as <tt>int</tt>.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Returns the number of bytes that have been written.
   * <p>
   * @return the number of bytes that have been read
   *         as <tt>int</tt>.
   */
  public int getByteCount() {
    return m_WordCount * 2;
  }//getByteCount

  /**
   * Returns the number of words that have been read.
   * The returned value should be half of
   * the byte count of the response.
   * <p>
   * @return the number of words that have been read
   *         as <tt>int</tt>.
   */
  public int getWordCount() {
    return m_WordCount;
  }//getWordCount

  /**
   * Sets the number of words that have been returned.
   * <p>
   * @param count the number of words as <tt>int</tt>.
   */
  private void setWordCount(int count) {
    m_WordCount = count;
  }//setWordCount

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeShort(m_Reference);
    dout.writeShort(getWordCount());
  }//writeData

  public void readData(DataInput din)
      throws IOException {

    setReference(din.readUnsignedShort());
    setWordCount(din.readUnsignedShort());
    //NOTE: register values are not echoed

    //update data length
    setDataLength(4);
  }//readData

}//class WriteMultipleRegistersResponse
