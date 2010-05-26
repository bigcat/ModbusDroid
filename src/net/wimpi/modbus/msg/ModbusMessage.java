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

//import java.io.EOFException;
//import java.io.IOException;

import net.wimpi.modbus.io.Transportable;

/**
 * Interface defining a ModbusMessage.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public interface ModbusMessage
    extends Transportable {

  /**
   * Sets the flag that marks this <tt>ModbusMessage</tt> as headless
   * (for serial transport).
   */
  public void setHeadless();

  /**
   * Returns the transaction identifier of this
   * <tt>ModbusMessage</tt> as <tt>int</tt>.<br>
   * The identifier is a 2-byte (short) non negative
   * integer value valid in the range of 0-65535.
   * <p>
   * @return the transaction identifier as <tt>int</tt>.
   */
  public int getTransactionID();

  /**
   * Returns the protocol identifier of this
   * <tt>ModbusMessage</tt> as <tt>int</tt>.<br>
   * The identifier is a 2-byte (short) non negative
   * integer value valid in the range of 0-65535.
   * <p>
   * @return the protocol identifier as <tt>int</tt>.
   */
  public int getProtocolID();

  /**
   * Returns the length of the data appended
   * after the protocol header.
   * <p>
   * @return the data length as <tt>int</tt>.
   */
  public int getDataLength();

  /**
   * Returns the unit identifier of  this
   * <tt>ModbusMessage</tt> as <tt>int</tt>.<br>
   * The identifier is a 1-byte non negative
   * integer value valid in the range of 0-255.
   * <p>
   * @return the unit identifier as <tt>int</tt>.
   */
  public int getUnitID();

  /**
   * Returns the function code of this
   * <tt>ModbusMessage</tt> as <tt>int</tt>.<br>
   * The function code is a 1-byte non negative
   * integer value valid in the range of 0-127.<br>
   * Function codes are ordered in conformance
   * classes their values are specified in
   * <tt>net.wimpi.modbus.Modbus</tt>.
   * <p>
   * @return the function code as <tt>int</tt>.
   *
   * @see net.wimpi.modbus.Modbus
   */
  public int getFunctionCode();

  /**
   * Writes this <tt>ModbusMessage</tt> to the
   * given raw <tt>OutputStream</tt>.
   * <p>
   * @param out the <tt>OutputStream</tt> to write to.
   *
   * @throws Exception if it fails to write to
   *         the given <tt>OutputStream</tt>.
   * @throws Exception if the stream ended
   *         before all data has been written to it.
   *
   public void writeTo(OutputStream out) throws EOFException, IOException;
   */

  /**
   * Returns the <i>raw</i> message as an array of
   * bytes.
   * <p>
   * @return the <i>raw</i> message as <tt>byte[]</tt>.
   *
   public byte[] getMessage();
   */

  /**
   * Returns the <i>raw</i> message as <tt>String</tt>
   * containing a hexadecimal series of bytes.
   * <br>
   * This method is specially for debugging purposes,
   * allowing to log the communication in a manner used
   * in the specification document.
   * <p>
   * @return the <i>raw</i> message as <tt>String</tt>
   *         containing a hexadecimal series of bytes.
   *
   */
  public String getHexMessage();

  /**
   * Returns the total length this <tt>ModbusMessage</tt>.
   * <p>
   * @return the length as <tt>int</tt>.
   *
   public int length();
   */
}//interface ModbusMessage
