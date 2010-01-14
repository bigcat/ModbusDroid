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
package net.wimpi.modbus.net;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.io.ModbusTransport;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;

/**
 * Class implementing a handler for incoming Modbus/TCP requests.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class TCPConnectionHandler implements Runnable {

  private TCPSlaveConnection m_Connection;
  private ModbusTransport m_Transport;

  /**
   * Constructs a new <tt>TCPConnectionHandler</tt> instance.
   *
   * @param con an incoming connection.
   */
  public TCPConnectionHandler(TCPSlaveConnection con) {
    setConnection(con);
  }//constructor

  /**
   * Sets a connection to be handled by this <tt>
   * TCPConnectionHandler</tt>.
   *
   * @param con a <tt>TCPSlaveConnection</tt>.
   */
  public void setConnection(TCPSlaveConnection con) {
    m_Connection = con;
    m_Transport = m_Connection.getModbusTransport();
  }//setConnection

  public void run() {
    try {
      do {
        //1. read the request
        ModbusRequest request = m_Transport.readRequest();
        //System.out.println("Request:" + request.getHexMessage());
        ModbusResponse response = null;

        //test if Process image exists
        if (ModbusCoupler.getReference().getProcessImage() == null) {
          response =
              request.createExceptionResponse(Modbus.ILLEGAL_FUNCTION_EXCEPTION);
        } else {
          response = request.createResponse();
        }
        /*DEBUG*/
        if (Modbus.debug) System.out.println("Request:" + request.getHexMessage());
        if (Modbus.debug) System.out.println("Response:" + response.getHexMessage());

        //System.out.println("Response:" + response.getHexMessage());
        m_Transport.writeMessage(response);
      } while (true);
    } catch (ModbusIOException ex) {
      if (!ex.isEOF()) {
        //other troubles, output for debug
        ex.printStackTrace();
      }
    } finally {
      try {
        m_Connection.close();
      } catch (Exception ex) {
        //ignore
      }

    }
  }//run

}//TCPConnectionHandler

