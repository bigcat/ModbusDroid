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

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.procimg.DigitalIn;
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.ProcessImage;

/**
 * Class implementing a <tt>ReadInputDiscretesRequest</tt>.
 * The implementation directly correlates with the class 1
 * function <i>read input discretes (FC 2)</i>. It encapsulates
 * the corresponding request message.
 * <p>
 * Input Discretes are understood as bits that cannot be
 * manipulated (i.e. set or unset).
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public final class ReadInputDiscretesRequest
    extends ModbusRequest {

  //instance attributes
  private int m_Reference;
  private int m_BitCount;

  /**
   * Constructs a new <tt>ReadInputDiscretesRequest</tt>
   * instance.
   */
  public ReadInputDiscretesRequest() {
    super();
    setFunctionCode(Modbus.READ_INPUT_DISCRETES);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
  }//constructor

  /**
   * Constructs a new <tt>ReadInputDiscretesRequest</tt>
   * instance with a given reference and count of input
   * discretes (i.e. bits) to be read.
   * <p>
   * @param ref the reference number of the register
   *        to read from.
   * @param count the number of bits to be read.
   */
  public ReadInputDiscretesRequest(int ref, int count) {
    super();
    setFunctionCode(Modbus.READ_INPUT_DISCRETES);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
    setReference(ref);
    setBitCount(count);
  }//constructor

  /*
  public ModbusResponse getResponse() {
    ReadInputDiscretesResponse response =
        new ReadInputDiscretesResponse(getBitCount());
    response.setHeadless(isHeadless());
    return response;
  }//getResponse
  */

  public ModbusResponse createResponse() {
    ReadInputDiscretesResponse response = null;
    DigitalIn[] dins = null;

    //1. get process image
    ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
    //2. get inputdiscretes range
    try {
      dins = procimg.getDigitalInRange(this.getReference(), this.getBitCount());
    } catch (IllegalAddressException iaex) {
      return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
    }
    response = new ReadInputDiscretesResponse(dins.length);
    //transfer header data
    if (!isHeadless()) {
      response.setTransactionID(this.getTransactionID());
      response.setProtocolID(this.getProtocolID());
    } else {
      response.setHeadless();
    }
    response.setUnitID(this.getUnitID());
    response.setFunctionCode(this.getFunctionCode());

    for (int i = 0; i < dins.length; i++) {
      response.setDiscreteStatus(i, dins[i].isSet());
    }
    return response;
  }//createResponse


  /**
   * Sets the reference of the register to start reading
   * from with this <tt>ReadInputDiscretesRequest</tt>.
   * <p>
   * @param ref the reference of the register
   *        to start reading from.
   */
  public void setReference(int ref) {
    m_Reference = ref;
    //setChanged(true);
  }//setReference

  /**
   * Returns the reference of the register to to start
   * reading from with this
   * <tt>ReadInputDiscretesRequest</tt>.
   * <p>
   * @return the reference of the register
   *        to start reading from as <tt>int</tt>.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Sets the number of bits (i.e. input discretes) to be
   * read with this <tt>ReadInputDiscretesRequest</tt>.
   * <p>
   * @param count the number of bits to be read.
   */
  public void setBitCount(int count) {
    //assert <=2000
    m_BitCount = count;
    //setChanged(true);
  }//setBitCount

  /**
   * Returns the number of bits (i.e. input discretes)
   * to be read with this
   * <tt>ReadInputDiscretesRequest</tt>.
   * <p>
   * @return the number of bits to be read.
   */
  public int getBitCount() {
    return m_BitCount;
  }//getBitCount

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeShort(m_Reference);
    dout.writeShort(m_BitCount);
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    m_Reference = din.readUnsignedShort();
    m_BitCount = din.readUnsignedShort();
  }//readData

/*
  protected void assembleData() throws IOException {
    m_DataOut.writeShort(m_Reference);
    m_DataOut.writeShort(m_BitCount);
  }//assembleData

  public void readData(DataInputStream in) throws IOException, EOFException {
    m_Reference = in.readUnsignedShort();
    m_BitCount = in.readUnsignedShort();
  }//readData
*/
}//class ReadInputDiscretesRequest
