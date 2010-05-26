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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusUDPTransport;

/**
 * Class implementing a <tt>UDPMasterTerminal</tt>.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
class UDPMasterTerminal
    implements UDPTerminal {

  private DatagramSocket m_Socket;
  private int m_Timeout = Modbus.DEFAULT_TIMEOUT;
  private boolean m_Active;
  protected InetAddress m_LocalAddress;
  protected InetAddress m_RemoteAddress;
  private int m_RemotePort = Modbus.DEFAULT_PORT;
  private int m_LocalPort = Modbus.DEFAULT_PORT;
  protected ModbusUDPTransport m_ModbusTransport;

  public UDPMasterTerminal() {
  }//constructor

  protected UDPMasterTerminal(InetAddress addr) {
    m_RemoteAddress = addr;
  }//constructor

  public InetAddress getLocalAddress() {
    return m_LocalAddress;
  }//getLocalAddress

  public void setLocalAddress(InetAddress addr) {
    m_LocalAddress = addr;
  }//setLocalAddress

  public int getLocalPort() {
    return m_LocalPort;
  }//getLocalPort

  protected void setLocalPort(int port) {
    m_LocalPort = port;
  }//setLocalPort

  /**
   * Returns the destination port of this
   * <tt>UDPSlaveTerminal</tt>.
   *
   * @return the port number as <tt>int</tt>.
   */
  public int getRemotePort() {
    return m_RemotePort;
  }//getDestinationPort

  /**
   * Sets the destination port of this
   * <tt>UDPSlaveTerminal</tt>.
   * The default is defined as <tt>Modbus.DEFAULT_PORT</tt>.
   *
   * @param port the port number as <tt>int</tt>.
   */
  public void setRemotePort(int port) {
    m_RemotePort = port;
  }//setPort

  /**
   * Returns the destination <tt>InetAddress</tt> of this
   * <tt>UDPSlaveTerminal</tt>.
   *
   * @return the destination address as <tt>InetAddress</tt>.
   */
  public InetAddress getRemoteAddress() {
    return m_RemoteAddress;
  }//getAddress

  /**
   * Sets the destination <tt>InetAddress</tt> of this
   * <tt>UDPSlaveTerminal</tt>.
   *
   * @param adr the destination address as <tt>InetAddress</tt>.
   */
  public void setRemoteAddress(InetAddress adr) {
    m_RemoteAddress = adr;
  }//setAddress

  /**
   * Tests if this <tt>UDPSlaveTerminal</tt> is active.
   *
   * @return <tt>true</tt> if active, <tt>false</tt> otherwise.
   */
  public boolean isActive() {
    return m_Active;
  }//isActive

  /**
   * Activate this <tt>UDPTerminal</tt>.
   *
   * @throws Exception if there is a network failure.
   */
  public synchronized void activate()
      throws Exception {
    if (!isActive()) {
      if (Modbus.debug) System.out.println("UDPMasterTerminal::activate()::laddr=:" + m_LocalAddress.toString() + ":lport=" + m_LocalPort);
      if (m_Socket == null) {
        if (m_LocalAddress != null && m_LocalPort != -1) {
          m_Socket = new DatagramSocket(m_LocalPort, m_LocalAddress);
        } else {
          m_Socket = new DatagramSocket();
          m_LocalPort = m_Socket.getLocalPort();
          m_LocalAddress = m_Socket.getLocalAddress();
        }
      }
      if (Modbus.debug) System.out.println("UDPMasterTerminal::haveSocket():" + m_Socket.toString());
      if (Modbus.debug) System.out.println("UDPMasterTerminal::laddr=:" + m_LocalAddress.toString() + ":lport=" + m_LocalPort);
      if (Modbus.debug) System.out.println("UDPMasterTerminal::raddr=:" + m_RemoteAddress.toString() + ":rport=" + m_RemotePort);

      m_Socket.setReceiveBufferSize(1024);
      m_Socket.setSendBufferSize(1024);

      m_ModbusTransport = new ModbusUDPTransport(this);
      m_Active = true;
    }
    if (Modbus.debug) System.out.println("UDPMasterTerminal::activated");
  }//activate

  /**
   * Deactivates this <tt>UDPSlaveTerminal</tt>.
   */
  public void deactivate() {
    try {
      if (Modbus.debug) System.out.println("UDPMasterTerminal::deactivate()");
      //close socket
      m_Socket.close();
      m_ModbusTransport = null;
      m_Active = false;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }//deactivate

  /**
   * Returns the <tt>ModbusTransport</tt> associated with this
   * <tt>TCPMasterConnection</tt>.
   *
   * @return the connection's <tt>ModbusTransport</tt>.
   */
  public ModbusUDPTransport getModbusTransport() {
    return m_ModbusTransport;
  }//getModbusTransport

  /**
   * Returns the timeout for this <tt>UDPMasterTerminal</tt>.
   *
   * @return the timeout as <tt>int</tt>.
   */
  public int getTimeout() {
    return m_Timeout;
  }//getTimeout

  /**
   * Sets the timeout for this <tt>UDPMasterTerminal</tt>.
   *
   * @param timeout the timeout as <tt>int</tt>.
   */
  public void setTimeout(int timeout) {
    m_Timeout = timeout;
  }//setTimeout

  public void sendMessage(byte[] msg)
      throws Exception {

    DatagramPacket req = new DatagramPacket(
        msg,
        msg.length,
        m_RemoteAddress,
        m_RemotePort
    );
    synchronized (m_Socket) {
      m_Socket.send(req);
    }
  }//sendPackage

  public byte[] receiveMessage()
      throws Exception {

    //1. Prepare buffer and receive package
    byte[] buffer = new byte[256];//max size
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    synchronized (m_Socket) {
      m_Socket.setSoTimeout(m_Timeout);
      m_Socket.receive(packet);
    }
    return buffer;
  }//receiveMessage

  public void receiveMessage(byte[] buffer)
      throws Exception {
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    m_Socket.setSoTimeout(m_Timeout);
    m_Socket.receive(packet);
  }//receiveMessage

}//class UDPMasterTerminal
