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
 * The default ProcessImageFactory.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class DefaultProcessImageFactory
    implements ProcessImageFactory {

  /**
   * Returns a new SimpleProcessImage instance.
   *
   * @return a SimpleProcessImage instance.
   */
  public ProcessImageImplementation createProcessImageImplementation() {
    return new SimpleProcessImage();
  }//createProcessImageImplementation

  /**
   * Returns a new SimpleDigitalIn instance.
   *
   * @return a SimpleDigitalIn instance.
   */
  public DigitalIn createDigitalIn() {
    return new SimpleDigitalIn();
  }//createDigitalIn

  /**
   * Returns a new DigitalIn instance with the given state.
   *
   * @param state true if set, false otherwise.
   * @return a SimpleDigitalIn instance.
   */
  public DigitalIn createDigitalIn(boolean state) {
    return new SimpleDigitalIn(state);
  }//createDigitalIn

  /**
   * Returns a new SimpleDigitalOut instance.
   *
   * @return a SimpleDigitalOut instance.
   */
  public DigitalOut createDigitalOut() {
    return new SimpleDigitalOut();
  }//createDigitalOut

  /**
   * Returns a new DigitalOut instance with the
   * given state.
   *
   * @param b true if set, false otherwise.
   * @return a SimpleDigitalOut instance.
   */
  public DigitalOut createDigitalOut(boolean b) {
    return new SimpleDigitalOut(b);
  }//createDigitalOut

  /**
   * Returns a new SimpleInputRegister instance.
   *
   * @return a SimpleInputRegister instance.
   */
  public InputRegister createInputRegister() {
    return new SimpleInputRegister();
  }//createSimpleInputRegister

  /**
   * Returns a new InputRegister instance with a
   * given value.
   *
   * @param b1 the first <tt>byte</tt>.
   * @param b2 the second <tt>byte</tt>.
   * @return an InputRegister instance.
   */
  public InputRegister createInputRegister(byte b1, byte b2) {
    return new SimpleInputRegister(b1, b2);
  }//createInputRegister

  /**
   * Creates a new SimpleRegister instance.
   *
   * @return a SimpleRegister instance.
   */
  public Register createRegister() {
    return new SimpleRegister();
  }//createRegister

  /**
   * Returns a new Register instance with a
   * given value.
   *
   * @param b1 the first <tt>byte</tt>.
   * @param b2 the second <tt>byte</tt>.
   * @return a Register instance.
   */
  public Register createRegister(byte b1, byte b2) {
    return new SimpleRegister(b1, b2);
  }//createRegister

}//class DefaultProcessImageFactory
