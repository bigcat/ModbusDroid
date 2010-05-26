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
 * Interface defining the factory methods for
 * the process image and it's elements.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public interface ProcessImageFactory {

  /**
   * Returns a new ProcessImageImplementation instance.
   *
   * @return a ProcessImageImplementation instance.
   */
  public ProcessImageImplementation createProcessImageImplementation();

  /**
   * Returns a new DigitalIn instance.
   *
   * @return a DigitalIn instance.
   */
  public DigitalIn createDigitalIn();

  /**
   * Returns a new DigitalIn instance with the given state.
   *
   * @param state true if set, false otherwise.
   * @return a DigitalIn instance.
   */
  public DigitalIn createDigitalIn(boolean state);

  /**
   * Returns a new DigitalOut instance.
   *
   * @return a DigitalOut instance.
   */
  public DigitalOut createDigitalOut();

  /**
   * Returns a new DigitalOut instance with the
   * given state.
   *
   * @param b true if set, false otherwise.
   * @return a DigitalOut instance.
   */
  public DigitalOut createDigitalOut(boolean b);

  /**
   * Returns a new InputRegister instance.
   *
   * @return an InputRegister instance.
   */
  public InputRegister createInputRegister();

  /**
   * Returns a new InputRegister instance with a
   * given value.
   *
   * @param b1 the first <tt>byte</tt>.
   * @param b2 the second <tt>byte</tt>.
   * @return an InputRegister instance.
   */
  public InputRegister createInputRegister(byte b1, byte b2);

  /**
   * Creates a new Register instance.
   *
   * @return a Register instance.
   */
  public Register createRegister();

  /**
   * Returns a new Register instance with a
   * given value.
   *
   * @param b1 the first <tt>byte</tt>.
   * @param b2 the second <tt>byte</tt>.
   * @return a Register instance.
   */
   public Register createRegister(byte b1, byte b2);

}//interface ProcessImageFactory
