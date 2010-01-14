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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.wimpi.modbus.Modbus;

/**
 * Abstract class implementing a <tt>ModbusResponse</tt>.
 * This class provides specialised implementations with
 * the functionality they have in common.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public abstract class ModbusResponse
    extends ModbusMessageImpl {


  /**
   * Utility method to set the raw data of the message.
   * Should not be used except under rare circumstances.
   * <p>
   * @param msg the <tt>byte[]</tt> resembling the raw modbus
   *        response message.
   */
  protected void setMessage(byte[] msg) {
    try {
      readData(
          new DataInputStream(
              new ByteArrayInputStream(msg)
          )
      );
    } catch (IOException ex) {

    }
  }//setMessage

  /**
   * Factory method creating the required specialized <tt>ModbusResponse</tt>
   * instance.
   *
   * @param functionCode the function code of the response as <tt>int</tt>.
   * @return a ModbusResponse instance specific for the given function code.
   */
  public static ModbusResponse createModbusResponse(int functionCode) {
    ModbusResponse response = null;

    switch (functionCode) {
      case Modbus.READ_MULTIPLE_REGISTERS:
        response = new ReadMultipleRegistersResponse();
        break;
      case Modbus.READ_INPUT_DISCRETES:
        response = new ReadInputDiscretesResponse();
        break;
      case Modbus.READ_INPUT_REGISTERS:
        response = new ReadInputRegistersResponse();
        break;
      case Modbus.READ_COILS:
        response = new ReadCoilsResponse();
        break;
      case Modbus.WRITE_MULTIPLE_REGISTERS:
        response = new WriteMultipleRegistersResponse();
        break;
      case Modbus.WRITE_SINGLE_REGISTER:
        response = new WriteSingleRegisterResponse();
        break;
      case Modbus.WRITE_COIL:
        response = new WriteCoilResponse();
        break;
      case Modbus.WRITE_MULTIPLE_COILS:
        response = new WriteMultipleCoilsResponse();
        break;
      default:
        response = new ExceptionResponse();
        break;
    }
    return response;
  }//createModbusResponse

}//class ModbusResponse
