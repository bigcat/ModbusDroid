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
 * Interface defining implementation specific
 * details of the <tt>ProcessImage</tt>, adding
 * mechanisms for creating and modifying the actual
 * "process image".
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public interface ProcessImageImplementation extends ProcessImage {

  /**
   * Sets a new <tt>DigitalOut</tt> instance at the
   * given reference.
   *
   * @param ref the reference as <tt>int</tt>.
   * @param _do the new <tt>DigitalOut</tt> instance to be
   *        set.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public void setDigitalOut(int ref, DigitalOut _do)
      throws IllegalAddressException;

  /**
   * Adds a new <tt>DigitalOut</tt> instance.
   *
   * @param _do the <tt>DigitalOut</tt> instance to be
   *        added.
   */
  public void addDigitalOut(DigitalOut _do);

  /**
   * Removes a given <tt>DigitalOut</tt> instance.
   *
   * @param _do the <tt>DigitalOut</tt> instance to be
   *        removed.
   */
  public void removeDigitalOut(DigitalOut _do);

  /**
   * Sets a new <tt>DigitalIn</tt> instance at the
   * given reference.
   *
   * @param ref the reference as <tt>int</tt>.
   * @param di the new <tt>DigitalIn</tt> instance to be
   *        set.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public void setDigitalIn(int ref, DigitalIn di)
      throws IllegalAddressException;

  /**
   * Adds a new <tt>DigitalIn</tt> instance.
   *
   * @param di the <tt>DigitalIn</tt> instance to be
   *        added.
   */
  public void addDigitalIn(DigitalIn di);

  /**
   * Removes a given <tt>DigitalIn</tt> instance.
   *
   * @param di the <tt>DigitalIn</tt> instance to be
   *        removed.
   */
  public void removeDigitalIn(DigitalIn di);

  /**
   * Sets a new <tt>InputRegister</tt> instance at the
   * given reference.
   *
   * @param ref the reference as <tt>int</tt>.
   * @param reg the new <tt>InputRegister</tt> instance to be
   *        set.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public void setInputRegister(int ref, InputRegister reg)
      throws IllegalAddressException;

  /**
   * Adds a new <tt>InputRegister</tt> instance.
   *
   * @param reg the <tt>InputRegister</tt> instance to be
   *        added.
   */
  public void addInputRegister(InputRegister reg);

  /**
   * Removes a given <tt>InputRegister</tt> instance.
   *
   * @param reg the <tt>InputRegister</tt> instance to be
   *        removed.
   */
  public void removeInputRegister(InputRegister reg);

  /**
   * Sets a new <tt>Register</tt> instance at the
   * given reference.
   *
   * @param ref the reference as <tt>int</tt>.
   * @param reg the new <tt>Register</tt> instance to be
   *        set.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public void setRegister(int ref, Register reg)
      throws IllegalAddressException;

  /**
   * Adds a new <tt>Register</tt> instance.
   *
   * @param reg the <tt>Register</tt> instance to be
   *        added.
   */
  public void addRegister(Register reg);

  /**
   * Removes a given <tt>Register</tt> instance.
   *
   * @param reg the <tt>Register</tt> instance to be
   *        removed.
   */
  public void removeRegister(Register reg);

  /**
   * Defines the set state (i.e. <b>true</b>) of
   * a digital input or output.
   */
  public static final byte DIG_TRUE = 1;

  /**
   * Defines the unset state (i.e. <b>false</b>) of
   * a digital input or output.
   */
  public static final byte DIG_FALSE = 0;

  /**
   * Defines the invalid state of
   * a digital input or output.
   */
  public static final byte DIG_INVALID = -1;

}//ProcessImageImplementation
