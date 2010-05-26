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
import net.wimpi.modbus.io.ModbusUDPTransport;
import net.wimpi.modbus.util.LinkedQueue;
import net.wimpi.modbus.util.ModbusUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Hashtable;

/**
 * Class implementing a <tt>UDPSlaveTerminal</tt>.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
class UDPSlaveTerminal
    implements UDPTerminal {

  //instance attributes
  private DatagramSocket m_Socket;
  private int m_Timeout = Modbus.DEFAULT_TIMEOUT;
  private boolean m_Active;
  protected InetAddress m_LocalAddress;
  private int m_LocalPort = Modbus.DEFAULT_PORT;
  protected ModbusUDPTransport m_ModbusTransport;
  private int m_Retries = Modbus.DEFAULT_RETRIES;

  private LinkedQueue m_SendQueue;
  private LinkedQueue m_ReceiveQueue;
  private PacketSender m_PacketSender;
  private PacketReceiver m_PacketReceiver;
  private Thread m_Receiver;
  private Thread m_Sender;

  protected Hashtable m_Requests;

  protected UDPSlaveTerminal() {
    m_SendQueue = new LinkedQueue();
    m_ReceiveQueue = new LinkedQueue();
    //m_Requests = new Hashtable(342,0.75F);
    m_Requests = new Hashtable(342);
  }//constructor

  protected UDPSlaveTerminal(InetAddress localaddress) {
    m_LocalAddress = localaddress;
    m_SendQueue = new LinkedQueue();
    m_ReceiveQueue = new LinkedQueue();
    //m_Requests = new Hashtable(342, 0.75F);
    m_Requests = new Hashtable(342);
  }//constructor

  public InetAddress getLocalAddress() {
    return m_LocalAddress;
  }//getLocalAddress

  public int getLocalPort() {
    return m_LocalPort;
  }//getLocalPort

  protected void setLocalPort(int port) {
    m_LocalPort = port;
  }//setLocalPort

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
      if (Modbus.debug) System.out.println("UDPSlaveTerminal::activate()");
      if (m_Socket == null) {
        if (m_LocalAddress != null && m_LocalPort != -1) {
          m_Socket = new DatagramSocket(m_LocalPort, m_LocalAddress);
        } else {
          m_Socket = new DatagramSocket();
          m_LocalPort = m_Socket.getLocalPort();
          m_LocalAddress = m_Socket.getLocalAddress();
        }
      }
      if (Modbus.debug) System.out.println("UDPSlaveTerminal::haveSocket():" + m_Socket.toString());
      if (Modbus.debug) System.out.println("UDPSlaveTerminal::addr=:" + m_LocalAddress.toString() + ":port=" + m_LocalPort);


      m_Socket.setReceiveBufferSize(1024);
      m_Socket.setSendBufferSize(1024);
      m_PacketReceiver = new PacketReceiver();
      m_Receiver = new Thread(m_PacketReceiver);
      m_Receiver.start();
      if (Modbus.debug) System.out.println("UDPSlaveTerminal::receiver started()");
      m_PacketSender = new PacketSender();
      m_Sender = new Thread(m_PacketSender);
      m_Sender.start();
      if (Modbus.debug) System.out.println("UDPSlaveTerminal::sender started()");
      m_ModbusTransport = new ModbusUDPTransport(this);
      if (Modbus.debug) System.out.println("UDPSlaveTerminal::transport created");
      m_Active = true;
    }
    if (Modbus.debug) System.out.println("UDPSlaveTerminal::activated");
  }//activate

  /**
   * Deactivates this <tt>UDPSlaveTerminal</tt>.
   */
  public void deactivate() {
    try {
      if (m_Active) {
        //1. stop receiver
        m_PacketReceiver.stop();
        m_Receiver.join();
        //2. stop sender gracefully
        m_PacketSender.stop();
        m_Sender.join();
        //3. close socket
        m_Socket.close();
        m_ModbusTransport = null;
        m_Active = false;
      }
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


  protected boolean hasResponse() {
    return !m_ReceiveQueue.isEmpty();
  }//hasResponse

  /**
   * Returns the timeout for this <tt>UDPSlaveTerminal</tt>.
   *
   * @return the timeout as <tt>int</tt>.
   *
   public int getTimeout() {
   return m_Timeout;
   }//getTimeout

   /**
   * Sets the timeout for this <tt>UDPSlaveTerminal</tt>.
   *
   * @param timeout the timeout as <tt>int</tt>.
   *
   public void setTimeout(int timeout) {
   m_Timeout = timeout;
   try {
   m_Socket.setSoTimeout(m_Timeout);
   } catch (IOException ex) {
   ex.printStackTrace();
   //handle?
   }
   }//setTimeout
   */
  /**
   * Returns the socket of this <tt>UDPSlaveTerminal</tt>.
   *
   * @return the socket as <tt>DatagramSocket</tt>.
   */
  public DatagramSocket getSocket() {
    return m_Socket;
  }//getSocket

  /**
   * Sets the socket of this <tt>UDPTerminal</tt>.
   *
   * @param sock the <tt>DatagramSocket</tt> for this terminal.
   */
  protected void setSocket(DatagramSocket sock) {
    m_Socket = sock;
  }//setSocket

  public void sendMessage(byte[] msg)
      throws Exception {
    m_SendQueue.put(msg);
  }//sendPackage

  public byte[] receiveMessage()
      throws Exception {
    return (byte[]) m_ReceiveQueue.take();
  }//receiveMessage

  class PacketSender
      implements Runnable {

    private boolean m_Continue;

    public PacketSender() {
      m_Continue = true;
    }//constructor

    public void run() {
      do {
        try {
          //1. pickup the message and corresponding request
          byte[] message = (byte[]) m_SendQueue.take();
          DatagramPacket req = (DatagramPacket)
              m_Requests.remove(new Integer(ModbusUtil.registersToInt(message)));
          //2. create new Package with corresponding address and port
          DatagramPacket res = new DatagramPacket(message,
              message.length,
              req.getAddress(),
              req.getPort());
          m_Socket.send(res);
          if (Modbus.debug) System.out.println("Sent package from queue.");
        } catch (Exception ex) {
          DEBUG:ex.printStackTrace();
        }
      } while (m_Continue || !m_SendQueue.isEmpty());
    }//run

    public void stop() {
      m_Continue = false;
    }//stop

  }//PacketSender

  class PacketReceiver
      implements Runnable {

    private boolean m_Continue;

    public PacketReceiver() {
      m_Continue = true;
    }//constructor

    public void run() {
      do {
        try {
          //1. Prepare buffer and receive package
          byte[] buffer = new byte[256];//max size
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          m_Socket.receive(packet);
          //2. Extract TID and remember request
          Integer tid = new Integer(ModbusUtil.registersToInt(buffer));
          m_Requests.put(tid, packet);
          //3. place the data buffer in the queue
          m_ReceiveQueue.put(buffer);
          if (Modbus.debug) System.out.println("Received package to queue.");
        } catch (Exception ex) {
          DEBUG:ex.printStackTrace();
        }
      } while (m_Continue);
    }//run

    public void stop() {
      m_Continue = false;
    }//stop

  }//PacketReceiver

}//class UDPTerminal
