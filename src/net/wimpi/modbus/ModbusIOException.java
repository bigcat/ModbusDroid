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
package net.wimpi.modbus;

/**
 * Class that implements a <tt>ModbusIOException</tt>.
 * Instances of this exception are thrown when
 * errors in the I/O occur.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class ModbusIOException
    extends ModbusException {

  private boolean m_EOF = false;

  /**
   * Constructs a new <tt>ModbusIOException</tt>
   * instance.
   */
  public ModbusIOException() {
  }//constructor

  /**
   * Constructs a new <tt>ModbusIOException</tt>
   * instance with the given message.
   * <p>
   * @param message the message describing this
   *        <tt>ModbusIOException</tt>.
   */
  public ModbusIOException(String message) {
    super(message);
  }//constructor(String)

  /**
   * Constructs a new <tt>ModbusIOException</tt>
   * instance.
   *
   * @param b true if caused by end of stream, false otherwise.
   */
  public ModbusIOException(boolean b) {
    m_EOF = b;
  }//constructor

  /**
   * Constructs a new <tt>ModbusIOException</tt>
   * instance with the given message.
   * <p>
   * @param message the message describing this
   *        <tt>ModbusIOException</tt>.
   * @param b true if caused by end of stream, false otherwise.
   */
  public ModbusIOException(String message, boolean b) {
    super(message);
    m_EOF = b;
  }//constructor(String)


  /**
   * Tests if this <tt>ModbusIOException</tt>
   * is caused by an end of the stream.
   * <p>
   * @return true if stream ended, false otherwise.
   */
  public boolean isEOF() {
    return m_EOF;
  }//isEOF

  /**
   * Sets the flag that determines whether this
   * <tt>ModbusIOException</tt> was caused by
   * an end of the stream.
   * <p>
   * @param b true if stream ended, false otherwise.
   */
  public void setEOF(boolean b) {
    m_EOF = b;
  }//setEOF

}//ModbusIOException
