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

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.util.ThreadPool;

/**
 * Class that implements a ModbusTCPListener.<br>
 * If listening, it accepts incoming requests
 * passing them on to be handled.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class ModbusTCPListener
    implements Runnable {

  private static int c_RequestCounter = 0;

  private ServerSocket m_ServerSocket = null;
  private ThreadPool m_ThreadPool;
  private Thread m_Listener;
  private int m_Port = Modbus.DEFAULT_PORT;
  private int m_FloodProtection = 5;
  private boolean m_Listening;
  private InetAddress m_Address;

  /**
   * Constructs a ModbusTCPListener instance.<br>
   *
   * @param poolsize the size of the <tt>ThreadPool</tt> used to handle
   *        incoming requests.
   */
  public ModbusTCPListener(int poolsize) {
    m_ThreadPool = new ThreadPool(poolsize);
    try {
      m_Address = InetAddress.getLocalHost();
    } catch (UnknownHostException ex) {

    }
  }//constructor

  /**
   * Constructs a ModbusTCPListener instance.<br>
   *
   * @param poolsize the size of the <tt>ThreadPool</tt> used to handle
   *        incoming requests.
   * @param addr the interface to use for listening.
   */
  public ModbusTCPListener(int poolsize, InetAddress addr) {
    m_ThreadPool = new ThreadPool(poolsize);
    m_Address = addr;
  }//constructor


  /**
   * Sets the port to be listened to.
   *
   * @param port the number of the IP port as <tt>int</tt>.
   */
  public void setPort(int port) {
    m_Port = port;
  }//setPort

  /**
   * Sets the address of the interface to be listened to.
   *
   * @param addr an <tt>InetAddress</tt> instance.
   */
  public void setAddress(InetAddress addr) {
    m_Address = addr;
  }//setAddress

  /**
   * Starts this <tt>ModbusTCPListener</tt>.
   */
  public void start() {
    m_Listener = new Thread(this);
    m_Listener.start();
    m_Listening = true;
  }//start

  /**
   * Stops this <tt>ModbusTCPListener</tt>.
   */
  public void stop() {
    m_Listening = false;
    try {
      m_ServerSocket.close();
      m_Listener.join();
    } catch (Exception ex) {
      //?
    }
  }//stop

  /**
   * Accepts incoming connections and handles then with
   * <tt>TCPConnectionHandler</tt> instances.
   */
  public void run() {
    try {
      /*
          A server socket is opened with a connectivity queue of a size specified
          in int floodProtection.  Concurrent login handling under normal circumstances
          should be allright, denial of service attacks via massive parallel
          program logins can probably be prevented.
      */
      m_ServerSocket = new ServerSocket(m_Port, m_FloodProtection, m_Address);
      if(Modbus.debug) System.out.println("Listenening to " + m_ServerSocket.toString() + "(Port " + m_Port + ")");

      //Infinite loop, taking care of resources in case of a lot of parallel logins
      do {
        Socket incoming = m_ServerSocket.accept();
        if (Modbus.debug) System.out.println("Making new connection " + incoming.toString());
        if (m_Listening) {
          //FIXME: Replace with object pool due to resource issues
          m_ThreadPool.execute(
              new TCPConnectionHandler(
                  new TCPSlaveConnection(incoming)
              )
          );
          count();
        } else {
          //just close the socket
          incoming.close();
        }
      } while (m_Listening);
    } catch (SocketException iex) {
      if (!m_Listening) {
        return;
      } else {
        iex.printStackTrace();
      }
    } catch (IOException e) {
      //FIXME: this is a major failure, how do we handle this
    }
  }//run

  /**
   * Tests if this <tt>ModbusTCPListener</tt> is listening
   * and accepting incoming connections.
   *
   * @return true if listening (and accepting incoming connections),
   *          false otherwise.
   */
  public boolean isListening() {
    return m_Listening;
  }//isListening

  private void count() {
    c_RequestCounter++;
    if (c_RequestCounter == REQUESTS_TOGC) {
      System.gc();
      c_RequestCounter = 0;
    }
  }//count

  private static final int REQUESTS_TOGC = 10;

}//class ModbusTCPListener
