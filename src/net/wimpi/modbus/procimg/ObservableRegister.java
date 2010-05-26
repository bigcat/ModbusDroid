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

import net.wimpi.modbus.util.Observable;


/**
 * Class implementing an observable register.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class ObservableRegister
    extends Observable
    implements Register {

  /**
   * The word (<tt>byte[2]</tt> holding the content of this register.
   */
  protected byte[] m_Register = new byte[2];

  public int getValue() {
    return ((m_Register[0] & 0xff) << 8 | (m_Register[1] & 0xff));
  }//getValue

  public final int toUnsignedShort() {
    return ((m_Register[0] & 0xff) << 8 | (m_Register[1] & 0xff));
  }//toUnsignedShort

  public final synchronized void setValue(int v) {
    m_Register[0] = (byte) (0xff & (v >> 8));
    m_Register[1] = (byte) (0xff & v);
    notifyObservers("value");
  }//setValue

  public final short toShort() {
    return (short) ((m_Register[0] << 8) | (m_Register[1] & 0xff));
  }//toShort

  public final synchronized void setValue(short s) {
    m_Register[0] = (byte) (0xff & (s >> 8));
    m_Register[1] = (byte) (0xff & s);
    notifyObservers("value");
  }//setValue

  public final synchronized void setValue(byte[] bytes) {
    if (bytes.length < 2) {
      throw new IllegalArgumentException();
    } else {
      m_Register[0] = bytes[0];
      m_Register[1] = bytes[1];
      notifyObservers("value");
    }
  }//setValue

  public byte[] toBytes() {
    return m_Register;
  }//toBytes
  
}//class ObservableRegister
