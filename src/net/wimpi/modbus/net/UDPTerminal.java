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

import java.net.InetAddress;

import net.wimpi.modbus.io.ModbusUDPTransport;

/**
 * Interface defining a <tt>UDPTerminal</tt>.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public interface UDPTerminal {

  /**
   * Returns the local address of this <tt>UDPTerminal</tt>.
   *
   * @return an <tt>InetAddress</tt> instance.
   */
  public InetAddress getLocalAddress();

  /**
   * Returns the local port of this <tt>UDPTerminal</tt>.
   *
   * @return the local port as <tt>int</tt>.
   */
  public int getLocalPort();

  /**
   * Tests if this <tt>UDPTerminal</tt> is active.
   *
   * @return <tt>true</tt> if active, <tt>false</tt> otherwise.
   */
  public boolean isActive();

  /**
   * Activate this <tt>UDPTerminal</tt>.
   *
   * @throws java.lang.Exception if there is a network failure.
   */
  public void activate() throws Exception;

  /**
   * Deactivates this <tt>UDPTerminal</tt>.
   */
  public void deactivate();

  /**
   * Returns the <tt>ModbusTransport</tt> associated with this
   * <tt>UDPTerminal</tt>.
   *
   * @return a <tt>ModbusTransport</tt> instance.
   */
  public ModbusUDPTransport getModbusTransport();

  /**
   * Sends the given message.
   *
   * @param msg the message as <tt>byte[]</tt>.
   * @throws Exception if sending the message fails.
   */
  public void sendMessage(byte[] msg) throws Exception;

  /**
   * Receives and returns a message.
   *
   * @return the message as a newly allocated <tt>byte[]</tt>.
   * @throws Exception if receiving a message fails.
   */
  public byte[] receiveMessage() throws Exception;

}//interface UDPTerminal
