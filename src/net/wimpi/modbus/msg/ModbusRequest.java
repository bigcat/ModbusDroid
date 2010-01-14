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

/**
 * Abstract class implementing a <tt>ModbusRequest</tt>.
 * This class provides specialised implementations with
 * the functionality they have in common.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public abstract class ModbusRequest
    extends ModbusMessageImpl {

  /**
   * Returns the <tt>ModbusResponse</tt> that
   * correlates with this <tt>ModbusRequest</tt>.
   * <p>
   * @return the corresponding <tt>ModbusResponse</tt>.
   *
  public abstract ModbusResponse getResponse();
  */

  /**
   * Returns the <tt>ModbusResponse</tt> that
   * represents the answer to this <tt>ModbusRequest</tt>.
   * <p>
   * The implementation should take care about assembling
   * the reply to this <tt>ModbusRequest</tt>.
   * <p>
   * @return the corresponding <tt>ModbusResponse</tt>.
   */
  public abstract ModbusResponse createResponse();

  /**
   * Factory method for creating exception responses with the
   * given exception code.
   *
   * @param EXCEPTION_CODE the code of the exception.
   * @return a ModbusResponse instance representing the exception
   *         response.
   */
  public ModbusResponse createExceptionResponse(int EXCEPTION_CODE) {
    ExceptionResponse response =
        new ExceptionResponse(this.getFunctionCode(), EXCEPTION_CODE);
    if (!isHeadless()) {
      response.setTransactionID(this.getTransactionID());
      response.setProtocolID(this.getProtocolID());
    } else {
      response.setHeadless();
    }
    return response;
  }//createExceptionResponse

  /**
   * Factory method creating the required specialized <tt>ModbusRequest</tt>
   * instance.
   *
   * @param functionCode the function code of the request as <tt>int</tt>.
   * @return a ModbusRequest instance specific for the given function type.
   */
  public static ModbusRequest createModbusRequest(int functionCode) {
    ModbusRequest request = null;

    switch (functionCode) {
      case Modbus.READ_MULTIPLE_REGISTERS:
        request = new ReadMultipleRegistersRequest();
        break;
      case Modbus.READ_INPUT_DISCRETES:
        request = new ReadInputDiscretesRequest();
        break;
      case Modbus.READ_INPUT_REGISTERS:
        request = new ReadInputRegistersRequest();
        break;
      case Modbus.READ_COILS:
        request = new ReadCoilsRequest();
        break;
      case Modbus.WRITE_MULTIPLE_REGISTERS:
        request = new WriteMultipleRegistersRequest();
        break;
      case Modbus.WRITE_SINGLE_REGISTER:
        request = new WriteSingleRegisterRequest();
        break;
      case Modbus.WRITE_COIL:
        request = new WriteCoilRequest();
        break;
      case Modbus.WRITE_MULTIPLE_COILS:
        request = new WriteMultipleCoilsRequest();
        break;
      default:
        request = new IllegalFunctionRequest(functionCode);
        break;
    }
    return request;
  }//createModbusRequest

}//class ModbusRequest
