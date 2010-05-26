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
import net.wimpi.modbus.io.ModbusTransport;

import java.net.InetAddress;

/**
 * Class that implements a UDPMasterConnection.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class UDPMasterConnection {

  //instance attributes
  private UDPMasterTerminal m_Terminal;
  private int m_Timeout = Modbus.DEFAULT_TIMEOUT;
  private boolean m_Connected;

  private InetAddress m_Address;
  private int m_Port = Modbus.DEFAULT_PORT;

  /**
   * Constructs a <tt>UDPMasterConnection</tt> instance
   * with a given destination address.
   *
   * @param adr the destination <tt>InetAddress</tt>.
   */
  public UDPMasterConnection(InetAddress adr) {
    m_Address = adr;
  }//constructor

  /**
   * Opens this <tt>UDPMasterConnection</tt>.
   *
   * @throws Exception if there is a network failure.
   */
  public synchronized void connect()
      throws Exception {
    if (!m_Connected) {
      m_Terminal = new UDPMasterTerminal();
      m_Terminal.setLocalAddress(InetAddress.getLocalHost());
      m_Terminal.setLocalPort(5000);
      m_Terminal.setRemoteAddress(m_Address);
      m_Terminal.setRemotePort(m_Port);
      m_Terminal.setTimeout(m_Timeout);
      m_Terminal.activate();
      m_Connected = true;
    }
  }//connect

  /**
   * Closes this <tt>UDPMasterConnection</tt>.
   */
  public void close() {
    if (m_Connected) {
      try {
        m_Terminal.deactivate();
      } catch (Exception ex) {
        if (Modbus.debug) ex.printStackTrace();
      }
      m_Connected = false;
    }
  }//close

  /**
   * Returns the <tt>ModbusTransport</tt> associated with this
   * <tt>UDPMasterConnection</tt>.
   *
   * @return the connection's <tt>ModbusTransport</tt>.
   */
  public ModbusTransport getModbusTransport() {
    return m_Terminal.getModbusTransport();
  }//getModbusTransport

  /**
   * Returns the terminal used for handling the package traffic.
   *
   * @return a <tt>UDPTerminal</tt> instance.
   */
  public UDPTerminal getTerminal() {
    return m_Terminal;
  }//getTerminal

  /**
   * Returns the timeout for this <tt>UDPMasterConnection</tt>.
   *
   * @return the timeout as <tt>int</tt>.
   */
  public int getTimeout() {
    return m_Timeout;
  }//getTimeout

  /**
   * Sets the timeout for this <tt>UDPMasterConnection</tt>.
   *
   * @param timeout the timeout as <tt>int</tt>.
   */
  public void setTimeout(int timeout) {
    m_Timeout = timeout;
    m_Terminal.setTimeout(timeout);
  }//setTimeout

  /**
   * Returns the destination port of this
   * <tt>UDPMasterConnection</tt>.
   *
   * @return the port number as <tt>int</tt>.
   */
  public int getPort() {
    return m_Port;
  }//getPort

  /**
   * Sets the destination port of this
   * <tt>UDPMasterConnection</tt>.
   * The default is defined as <tt>Modbus.DEFAULT_PORT</tt>.
   *
   * @param port the port number as <tt>int</tt>.
   */
  public void setPort(int port) {
    m_Port = port;
  }//setPort

  /**
   * Returns the destination <tt>InetAddress</tt> of this
   * <tt>UDPMasterConnection</tt>.
   *
   * @return the destination address as <tt>InetAddress</tt>.
   */
  public InetAddress getAddress() {
    return m_Address;
  }//getAddress

  /**
   * Sets the destination <tt>InetAddress</tt> of this
   * <tt>UDPMasterConnection</tt>.
   *
   * @param adr the destination address as <tt>InetAddress</tt>.
   */
  public void setAddress(InetAddress adr) {
    m_Address = adr;
  }//setAddress

  /**
   * Tests if this <tt>UDPMasterConnection</tt> is connected.
   *
   * @return <tt>true</tt> if connected, <tt>false</tt> otherwise.
   */
  public boolean isConnected() {
    return m_Connected;
  }//isConnected

}//class UDPMasterConnection
