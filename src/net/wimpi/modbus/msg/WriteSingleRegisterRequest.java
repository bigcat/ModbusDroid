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

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.ProcessImage;
import net.wimpi.modbus.procimg.Register;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Class implementing a <tt>WriteSingleRegisterRequest</tt>.
 * The implementation directly correlates with the class 0
 * function <i>write single register (FC 6)</i>. It
 * encapsulates the corresponding request message.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public final class WriteSingleRegisterRequest
    extends ModbusRequest {

  //instance attributes
  private int m_Reference;
  private Register m_Register;

  /**
   * Constructs a new <tt>WriteSingleRegisterRequest</tt>
   * instance.
   */
  public WriteSingleRegisterRequest() {
    super();
    setFunctionCode(Modbus.WRITE_SINGLE_REGISTER);
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
  }//constructor

  /**
   * Constructs a new <tt>WriteSingleRegisterRequest</tt>
   * instance with a given reference and value to be written.
   * <p/>
   *
   * @param ref the reference number of the register
   *            to read from.
   * @param reg the register containing the data to be written.
   */
  public WriteSingleRegisterRequest(int ref, Register reg) {
    super();
    setFunctionCode(Modbus.WRITE_SINGLE_REGISTER);
    m_Reference = ref;
    m_Register = reg;
    //4 bytes (unit id and function code is excluded)
    setDataLength(4);
  }//constructor

  public ModbusResponse createResponse() {
    WriteSingleRegisterResponse response = null;
    Register reg = null;

    //1. get process image
    ProcessImage procimg = ModbusCoupler.getReference().getProcessImage();
    //2. get register
    try {
      reg = procimg.getRegister(m_Reference);
      //3. set Register
      reg.setValue(m_Register.toBytes());
    } catch (IllegalAddressException iaex) {
      return createExceptionResponse(Modbus.ILLEGAL_ADDRESS_EXCEPTION);
    }
    response = new WriteSingleRegisterResponse(this.getReference(), reg.getValue());
    //transfer header data
    if (!isHeadless()) {
      response.setTransactionID(this.getTransactionID());
      response.setProtocolID(this.getProtocolID());
    } else {
      response.setHeadless();
    }
    response.setUnitID(this.getUnitID());
    response.setFunctionCode(this.getFunctionCode());
    return response;
  }//createResponse

  /**
   * Sets the reference of the register to be written
   * to with this <tt>WriteSingleRegisterRequest</tt>.
   * <p/>
   *
   * @param ref the reference of the register
   *            to be written to.
   */
  public void setReference(int ref) {
    m_Reference = ref;
    //setChanged(true);
  }//setReference

  /**
   * Returns the reference of the register to be
   * written to with this
   * <tt>WriteSingleRegisterRequest</tt>.
   * <p/>
   *
   * @return the reference of the register
   *         to be written to.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Sets the value that should be written to the
   * register with this <tt>WriteSingleRegisterRequest</tt>.
   * <p/>
   *
   * @param reg the register to be written.
   */
  public void setRegister(Register reg) {
    m_Register = reg;
  }//setRegister

  /**
   * Returns the value that should be written to the
   * register with this <tt>WriteSingleRegisterRequest</tt>.
   * <p/>
   *
   * @return the value to be written to the register.
   */
  public Register getRegister() {
    return m_Register;
  }//getRegister


  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeShort(m_Reference);
    dout.write(m_Register.toBytes(), 0, 2);
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    m_Reference = din.readUnsignedShort();
    m_Register = ModbusCoupler.getReference().getProcessImageFactory().createRegister(din.readByte(), din.readByte());
  }//readData

}//class WriteSingleRegisterRequest
