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
package net.wimpi.modbus.procimg;

/**
 * Class implementing a simple <tt>DigitalIn</tt>.
 * <p>
 * The set method is synchronized, which ensures atomic
 * access, but no specific access order.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class SimpleDigitalIn implements DigitalIn {

  /**
   * Field for the digital in state.
   */
  protected boolean m_Set = false;

  /**
   * Constructs a new <tt>SimpleDigitalIn</tt> instance.
   */
  public SimpleDigitalIn() {
  }//constructor

  /**
   * Constructs a new <tt>SimpleDigitalIn</tt> instance
   * with a given valid state.
   *
   * @param b true if to be set, false otherwise.
   */
  public SimpleDigitalIn(boolean b) {
    set(b);
  }//constructor(boolean)

  public boolean isSet() {
    return m_Set;
  }//isSet

  /**
   * Sets the state of this <tt>SimpleDigitalIn</tt>.
   * This method should only be used from master/device
   * side.
   *
   * @param b true if to be set, false otherwise.
   */
  public synchronized void set(boolean b) {
    m_Set = b;
  }//set

}//SimpleDigitalIn
