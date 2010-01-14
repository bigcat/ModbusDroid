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

import net.wimpi.modbus.util.BitVector;

/**
 * Class implementing a <tt>ReadInputDiscretesResponse</tt>.
 * The implementation directly correlates with the class 1
 * function <i>read input discretes (FC 2)</i>. It encapsulates
 * the corresponding response message.
 * <p>
 * Input Discretes are understood as bits that cannot be
 * manipulated (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public final class ReadInputDiscretesResponse
    extends ModbusResponse {

  //instance attributes
  private int m_BitCount;
  private BitVector m_Discretes;

  /**
   * Constructs a new <tt>ReadInputDiscretesResponse</tt>
   * instance.
   */
  public ReadInputDiscretesResponse() {
    super();
  }//constructor


  /**
   * Constructs a new <tt>ReadInputDiscretesResponse</tt>
   * instance with a given count of input discretes
   * (i.e. bits).
   * <b>
   * @param count the number of bits to be read.
   */
  public ReadInputDiscretesResponse(int count) {
    super();
    setBitCount(count);
  }//constructor

  /**
   * Returns the number of bits (i.e. input discretes)
   * read with the request.
   * <p>
   * @return the number of bits that have been read.
   */
  public int getBitCount() {
    return m_BitCount;
  }//getBitCount

  /**
   * Sets the number of bits in this response.
   *
   * @param count the number of response bits as int.
   */
  public void setBitCount(int count) {
    m_BitCount = count;
    m_Discretes = new BitVector(count);
    //set correct length, without counting unitid and fc
    setDataLength(m_Discretes.byteSize() + 1);
  }//setBitCount


  /**
   * Returns the <tt>BitVector</tt> that stores
   * the collection of bits that have been read.
   * <p>
   * @return the <tt>BitVector</tt> holding the
   *         bits that have been read.
   */
  public BitVector getDiscretes() {
    return m_Discretes;
  }//getDiscretes

  /**
   * Convenience method that returns the state
   * of the bit at the given index.
   * <p>
   * @param index the index of the input discrete
   *        for which the status should be returned.
   *
   * @return true if set, false otherwise.
   *
   * @throws IndexOutOfBoundsException if the
   *         index is out of bounds
   */
  public boolean getDiscreteStatus(int index)
      throws IndexOutOfBoundsException {

    return m_Discretes.getBit(index);
  }//getDiscreteStatus

  /**
   * Sets the status of the given input discrete.
   *
   * @param index the index of the input discrete to be set.
   * @param b true if to be set, false if to be reset.
   * @throws IndexOutOfBoundsException if the given index exceeds bounds.
   */
  public void setDiscreteStatus(int index, boolean b)
      throws IndexOutOfBoundsException {
    m_Discretes.setBit(index, b);
  }//setDiscreteStatus

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeByte(m_Discretes.byteSize());
    dout.write(m_Discretes.getBytes(), 0, m_Discretes.byteSize());
  }//writeData

  public void readData(DataInput din)
      throws IOException {

    int count = din.readUnsignedByte();
    byte[] data = new byte[count];
    for (int k = 0; k < count; k++) {
      data[k] = din.readByte();
    }

    //decode bytes into bitvector
    m_Discretes = BitVector.createBitVector(data);

    //update data length
    setDataLength(count + 1);
  }//readData

}//class ReadInputDiscretesResponse
