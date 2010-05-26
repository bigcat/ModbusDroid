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
package net.wimpi.modbus.io;

import java.io.DataOutput;
import java.io.IOException;
import java.io.InterruptedIOException;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.msg.ModbusMessage;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.net.UDPTerminal;

/**
 * Class that implements the Modbus UDP transport
 * flavor.
 *
 * @author Dieter Wimberger
 * @version 1.0 (29/04/2002)
 */
public class ModbusUDPTransport
    implements ModbusTransport {

  //instance attributes
  private UDPTerminal m_Terminal;
  private BytesOutputStream m_ByteOut;
  private BytesInputStream m_ByteIn;

  /**
   * Constructs a new <tt>ModbusTransport</tt> instance,
   * for a given <tt>UDPTerminal</tt>.
   * <p>
   * @param terminal the <tt>UDPTerminal</tt> used for message transport.
   */
  public ModbusUDPTransport(UDPTerminal terminal) {
    m_Terminal = terminal;
    m_ByteOut = new BytesOutputStream(Modbus.MAX_MESSAGE_LENGTH);
    m_ByteIn = new BytesInputStream(Modbus.MAX_MESSAGE_LENGTH);
  }//constructor


  public void close()
      throws IOException {
    //?
  }//close

  public void writeMessage(ModbusMessage msg)
      throws ModbusIOException {
    try {
      synchronized (m_ByteOut) {
        m_ByteOut.reset();
        msg.writeTo((DataOutput) m_ByteOut);
        m_Terminal.sendMessage(m_ByteOut.getBuffer());
      }
    } catch (Exception ex) {
      throw new ModbusIOException("I/O exception - failed to write.");
    }
  }//write

  public ModbusRequest readRequest()
      throws ModbusIOException {
    try {
      ModbusRequest req = null;
      synchronized (m_ByteIn) {
        m_ByteIn.reset(m_Terminal.receiveMessage());
        m_ByteIn.skip(7);
        int functionCode = m_ByteIn.readUnsignedByte();
        m_ByteIn.reset();
        req = ModbusRequest.createModbusRequest(functionCode);
        req.readFrom(m_ByteIn);
      }
      return req;

      /*
      new BytesInputStream(m_Terminal.receiveMessage());
      DataInputStream in = new DataInputStream(
          new ByteArrayInputStream(m_Terminal.receiveMessage())
      );
      //Timeout? Infinite could be problematic
      int transactionID = in.readShort();
      int protocolID = in.readShort();
      int dataLength = in.readShort();
      if (protocolID != Modbus.DEFAULT_PROTOCOL_ID || dataLength > 256) {
        throw new ModbusIOException();
      }
      int unitID = in.readUnsignedByte();
      int functionCode = in.readUnsignedByte();
      ModbusRequest request =
          ModbusRequest.createModbusRequest(functionCode, in, false);

      //set read parameters
      request.setTransactionID(transactionID);
      request.setProtocolID(protocolID);
      request.setUnitID(unitID);
      request.setDataLength(dataLength - 2);
      return request;
      */
    } catch (Exception ex) {
      throw new ModbusIOException("I/O exception - failed to read.");
    }
  }//readRequest

  public ModbusResponse readResponse()
      throws ModbusIOException {

    try {
      ModbusResponse res = null;
      synchronized (m_ByteIn) {
        m_ByteIn.reset(m_Terminal.receiveMessage());
        m_ByteIn.skip(7);
        int functionCode = m_ByteIn.readUnsignedByte();
        m_ByteIn.reset();
        res = ModbusResponse.createModbusResponse(functionCode);
        res.readFrom(m_ByteIn);
      }
      return res;


      /*
      DataInputStream in = new DataInputStream(
          new ByteArrayInputStream(m_Terminal.receiveMessage())
      );
      int transactionID = in.readShort();
      int protocolID = in.readShort();
      int dataLength = in.readShort();
      int unitID = in.readUnsignedByte();
      int functionCode = in.readUnsignedByte();
      ModbusResponse response =
          ModbusResponse.createModbusResponse(functionCode, in, false);

      //set read parameters
      response.setTransactionID(transactionID);
      response.setProtocolID(protocolID);
      response.setUnitID(unitID);
      return response;
      */
    } catch (InterruptedIOException ioex) {
      throw new ModbusIOException("Socket timed out.");
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new ModbusIOException("I/O exception - failed to read.");
    }
  }//readResponse

}//class ModbusUDPTransport
