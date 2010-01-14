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
 * Class implementing a<tt>ModbusResponse</tt>
 * that represents an exception.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class ExceptionResponse
    extends ModbusResponse {

  //instance attributes
  private int m_ExceptionCode = -1;

  /**
   * Constructs a new <tt>ExceptionResponse</tt> instance.
   */
  public ExceptionResponse() {
    //exception code, unitid and function code not counted.
    setDataLength(1);
  }//constructor

  /**
   * Constructs a new <tt>ExceptionResponse</tt> instance with
   * a given function code. Adds the exception offset automatically.
   *
   * @param fc the function code as <tt>int</tt>.
   */
  public ExceptionResponse(int fc) {
    //unitid and function code not counted.
    setDataLength(1);
    setFunctionCode(fc + Modbus.EXCEPTION_OFFSET);
  }//constructor

  /**
   * Constructs a new <tt>ExceptionResponse</tt> instance with
   * a given function code and an exception code. The function
   * code will be automatically increased with the exception offset.
   *
   *
   * @param fc the function code as <tt>int</tt>.
   * @param exc the exception code as <tt>int</tt>.
   */
  public ExceptionResponse(int fc, int exc) {
    //exception code, unitid and function code not counted.
    setDataLength(1);
    setFunctionCode(fc + Modbus.EXCEPTION_OFFSET);
    m_ExceptionCode = exc;
  }//constructor

  /**
   * Returns the Modbus exception code of this
   * <tt>ExceptionResponse</tt>.
   * <p>
   * @return the exception code as <tt>int</tt>.
   */
  public int getExceptionCode() {
    return m_ExceptionCode;
  }//getExceptionCode

  public void writeData(DataOutput dout)
      throws IOException {
    dout.writeByte(getExceptionCode());
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    m_ExceptionCode = din.readUnsignedByte();
  }//readData

}//ExceptionResponse
