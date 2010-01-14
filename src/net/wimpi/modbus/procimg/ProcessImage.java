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
 * Interface defining a process image
 * in an object oriented manner.
 * <p>
 * The process image is understood as a shared
 * memory area used form communication between
 * slave and master or device side.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public interface ProcessImage {

  /**
   * Returns a range of <tt>DigitalOut</tt> instances.
   * <p>
   *
   * @param offset the start offset.
   * @param count the amount of <tt>DigitalOut</tt> from the offset.
   *
   * @return an array of <tt>DigitalOut</tt> instances.
   *
   * @throws IllegalAddressException if the range from offset
   *         to offset+count is non existant.
   */
  public DigitalOut[] getDigitalOutRange(int offset, int count)
      throws IllegalAddressException;

  /**
   * Returns the <tt>DigitalOut</tt> instance at the given
   * reference.
   * <p>
   *
   * @param ref the reference.
   *
   * @return the <tt>DigitalOut</tt> instance at the given address.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public DigitalOut getDigitalOut(int ref)
      throws IllegalAddressException;

  /**
   * Returns the number of <tt>DigitalOut</tt> instances
   * in this <tt>ProcessImage</tt>.
   *
   * @return the number of digital outs as <tt>int</tt>.
   */
  public int getDigitalOutCount();

  /**
   * Returns a range of <tt>DigitalIn</tt> instances.
   * <p>
   *
   * @param offset the start offset.
   * @param count the amount of <tt>DigitalIn</tt> from the offset.
   *
   * @return an array of <tt>DigitalIn</tt> instances.
   *
   * @throws IllegalAddressException if the range from offset
   *         to offset+count is non existant.
   */
  public DigitalIn[] getDigitalInRange(int offset, int count)
      throws IllegalAddressException;

  /**
   * Returns the <tt>DigitalIn</tt> instance at the given
   * reference.
   * <p>
   *
   * @param ref the reference.
   *
   * @return the <tt>DigitalIn</tt> instance at the given address.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public DigitalIn getDigitalIn(int ref)
      throws IllegalAddressException;

  /**
   * Returns the number of <tt>DigitalIn</tt> instances
   * in this <tt>ProcessImage</tt>.
   *
   * @return the number of digital ins as <tt>int</tt>.
   */
  public int getDigitalInCount();

  /**
   * Returns a range of <tt>InputRegister</tt> instances.
   * <p>
   *
   * @param offset the start offset.
   * @param count the amount of <tt>InputRegister</tt>
   *        from the offset.
   *
   * @return an array of <tt>InputRegister</tt> instances.
   *
   * @throws IllegalAddressException if the range from offset
   *         to offset+count is non existant.
   */
  public InputRegister[] getInputRegisterRange(int offset, int count)
      throws IllegalAddressException;

  /**
   * Returns the <tt>InputRegister</tt> instance at the given
   * reference.
   * <p>
   *
   * @param ref the reference.
   *
   * @return the <tt>InputRegister</tt> instance at the given address.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public InputRegister getInputRegister(int ref)
      throws IllegalAddressException;


  /**
   * Returns the number of <tt>InputRegister</tt> instances
   * in this <tt>ProcessImage</tt>.
   *
   * @return the number of input registers as <tt>int</tt>.
   */
  public int getInputRegisterCount();

  /**
   * Returns a range of <tt>Register</tt> instances.
   * <p>
   *
   * @param offset the start offset.
   * @param count the amount of <tt>Register</tt> from the offset.
   *
   * @return an array of <tt>Register</tt> instances.
   *
   * @throws IllegalAddressException if the range from offset
   *         to offset+count is non existant.
   */
  public Register[] getRegisterRange(int offset, int count)
      throws IllegalAddressException;

  /**
   * Returns the <tt>Register</tt> instance at the given
   * reference.
   * <p>
   *
   * @param ref the reference.
   *
   * @return the <tt>Register</tt> instance at the given address.
   *
   * @throws IllegalAddressException if the reference is invalid.
   */
  public Register getRegister(int ref)
      throws IllegalAddressException;

  /**
   * Returns the number of <tt>Register</tt> instances
   * in this <tt>ProcessImage</tt>.
   *
   * @return the number of registers as <tt>int</tt>.
   */
  public int getRegisterCount();

}//interface ProcessImage
