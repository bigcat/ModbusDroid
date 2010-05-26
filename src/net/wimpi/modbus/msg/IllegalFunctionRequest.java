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
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;

import net.wimpi.modbus.Modbus;

/**
 * Class implementing a <tt>ModbusRequest</tt>
 * which is created for illegal or non implemented
 * function codes.<p>
 * This is just a helper class to keep the implementation
 * patterns the same for all cases.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class IllegalFunctionRequest
    extends ModbusRequest {

  /**
   * Constructs a new <tt>IllegalFunctionRequest</tt> instance for
   * a given function code.
   *
   * @param fc the function code as <tt>int</tt>.
   */
  public IllegalFunctionRequest(int fc) {
    setFunctionCode(fc);
  }//constructor

  public ModbusResponse createResponse() {
    return this.createExceptionResponse(Modbus.ILLEGAL_FUNCTION_EXCEPTION);
  }//createResponse

  public void writeData(DataOutput dout)
      throws IOException {
    throw new RuntimeException();
  }//writeData

  public void readData(DataInput din)
      throws IOException {
    //skip all following bytes
    int length = getDataLength();
    for (int i = 0; i < length; i++) {
      din.readByte();
    }
  }//readData

}//IllegalFunctionRequest
