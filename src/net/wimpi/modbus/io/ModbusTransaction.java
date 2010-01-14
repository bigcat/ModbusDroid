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

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;

/**
 * Interface defining a ModbusTransaction.
 * <p>
 * A transaction is defined by the sequence of
 * sending a request message and receiving a
 * related response message.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public interface ModbusTransaction {

  /**
   * Sets the <tt>ModbusRequest</tt> for this
   * <tt>ModbusTransaction</tt>.<p>
   * The related <tt>ModbusResponse</tt> is acquired
   * from the passed in <tt>ModbusRequest</tt> instance.<br>
   * <p>
   * @param req a <tt>ModbusRequest</tt>.
   */
  public void setRequest(ModbusRequest req);

  /**
   * Returns the <tt>ModbusRequest</tt> instance
   * associated with this <tt>ModbusTransaction</tt>.
   * <p>
   * @return the associated <tt>ModbusRequest</tt> instance.
   */
  public ModbusRequest getRequest();

  /**
   * Returns the <tt>ModbusResponse</tt> instance
   * associated with this <tt>ModbusTransaction</tt>.
   * <p>
   * @return the associated <tt>ModbusRequest</tt> instance.
   */
  public ModbusResponse getResponse();

  /**
   * Returns the actual transaction identifier of
   * this <tt>ModbusTransaction</tt>.
   * The identifier is a 2-byte (short) non negative
   * integer value valid in the range of 0-65535.<br>
   * <p>
   * @return the actual transaction identifier as
   *         <tt>int</tt>.
   */
  public int getTransactionID();

  /**
   * Set the amount of retries for opening
   * the connection for executing the transaction.
   * <p>
   * @param retries the amount of retries as <tt>int</tt>.
   */
  public void setRetries(int retries);

  /**
   * Returns the amount of retries for opening
   * the connection for executing the transaction.
   * <p>
   * @return the amount of retries as <tt>int</tt>.
   */
  public int getRetries();


  /**
   * Sets the flag that controls whether the
   * validity of a transaction will be checked.
   * <p>
   * @param b true if checking validity, false otherwise.
   */
  public void setCheckingValidity(boolean b);

  /**
   * Tests whether the validity of a transaction
   * will be checked.
   * <p>
   * @return true if checking validity, false otherwise.
   */
  public boolean isCheckingValidity();

  /**
   * Executes this <tt>ModbusTransaction</tt>.
   * Locks the <tt>ModbusTransport</tt> for sending
   * the <tt>ModbusRequest</tt> and reading the
   * related <tt>ModbusResponse</tt>.
   * If reconnecting is activated the connection will
   * be opened for the transaction and closed afterwards.
   * <p>
   * @throws ModbusException if an I/O error occurs,
   *         or the response is a modbus protocol exception.
   */
  public void execute() throws ModbusException;

}//interface ModbusTransaction
