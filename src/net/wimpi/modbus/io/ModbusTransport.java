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

import java.io.IOException;

import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.msg.ModbusMessage;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;

/**
 * Interface defining the I/O mechanisms for
 * <tt>ModbusMessage</tt> instances.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public interface ModbusTransport {

  /**
   * Closes the raw input and output streams of
   * this <tt>ModbusTransport</tt>.
   * <p>
   * @throws IOException if a stream
   *         cannot be closed properly.
   */
  public void close() throws IOException;

  /**
   * Writes a <tt<ModbusMessage</tt> to the
   * output stream of this <tt>ModbusTransport</tt>.
   * <p>
   * @param msg a <tt>ModbusMessage</tt>.
   * @throws ModbusIOException data cannot be
   *         written properly to the raw output stream of
   *         this <tt>ModbusTransport</tt>.
   */
  public void writeMessage(ModbusMessage msg) throws ModbusIOException;

  /**
   * Reads a <tt>ModbusRequest</tt> from the
   * input stream of this <tt>ModbusTransport<tt>.
   * <p>
   * @return req the <tt>ModbusRequest</tt> read from the underlying stream.
   * @throws ModbusIOException data cannot be
   *         read properly from the raw input stream of
   *         this <tt>ModbusTransport</tt>.
   */
  public ModbusRequest readRequest() throws ModbusIOException;

  /**
   * Reads a <tt>ModbusResponse</tt> from the
   * input stream of this <tt>ModbusTransport<tt>.
   * <p>
   * @return res the <tt>ModbusResponse</tt> read from the underlying stream.
   * @throws ModbusIOException data cannot be
   *         read properly from the raw input stream of
   *         this <tt>ModbusTransport</tt>.
   */
  public ModbusResponse readResponse() throws ModbusIOException;

}//class ModbusTransport
