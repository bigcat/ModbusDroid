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

/**
 * Class implementing a <tt>WriteCoilResponse</tt>.
 * The implementation directly correlates with the class 0
 * function <i>write coil (FC 5)</i>. It
 * encapsulates the corresponding response message.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public final class WriteCoilResponse
    extends ModbusResponse {

  //instance attributes
  private boolean m_Coil = false;
  private int m_Reference;

  /**
   * Constructs a new <tt>WriteCoilResponse</tt>
   * instance.
   */
  public WriteCoilResponse() {
    super();
    setDataLength(4);
  }//constructor

  /**
   * Constructs a new <tt>WriteCoilResponse</tt>
   * instance.
   *
   * @param reference the offset were writing was started from.
   * @param b the state of the coil; true set, false reset.
   */
  public WriteCoilResponse(int reference, boolean b) {
    super();
    setReference(reference);
    setCoil(b);
    setDataLength(4);
  }//constructor

  /**
   * Sets the state that has been returned
   * in the raw response.
   * <p>
   * @param b true if the coil should be set of
   *        false if it should be unset.
   */
  private void setCoil(boolean b) {
    m_Coil = b;
  }//setCoil

  /**
   * Gets the state that has been returned
   * in this <tt>WriteCoilRequest</tt>.
   * <p>
   * @return true if the coil is set, false if unset.
   */
  public boolean getCoil() {
    return m_Coil;
  }//getCoil

  /**
   * Returns the reference of the register of the coil
   * that has been written to with the request.
   * <p>
   * @return the reference of the coil's register.
   */
  public int getReference() {
    return m_Reference;
  }//getReference

  /**
   * Sets the reference of the register of the coil
   * that has been written to with the request.
   * <p>
   * @param ref the reference of the coil's register.
   */
  private void setReference(int ref) {
    m_Reference = ref;
    //setChanged(true);
  }//setReference

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeShort(getReference());
    if (getCoil()) {
      dout.write(Modbus.COIL_ON_BYTES, 0, 2);
    } else {
      dout.write(Modbus.COIL_OFF_BYTES, 0, 2);
    }
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    setReference(din.readUnsignedShort());

    byte[] data = new byte[2];
    for (int k = 0; k < 2; k++) {
      data[k] = din.readByte();
    }
    //set toggle
    if (data[0] == Modbus.COIL_ON) {
      setCoil(true);
    } else {
      setCoil(false);
    }

    //update data length
    setDataLength(4);
  }//readData

}//class WriteCoilResponse
