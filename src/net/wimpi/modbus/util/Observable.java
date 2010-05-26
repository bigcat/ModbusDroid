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
package net.wimpi.modbus.util;

import java.util.Vector;

/**
 * A cleanroom implementation of the Observable pattern.
 * <p/>
 *
 * @author Dieter Wimberger (wimpi)
 * @version 1.2rc1 (09/11/2004)
 */
public class Observable {

  private Vector m_Observers;

  /**
   * Constructs a new Observable instance.
   */
  public Observable() {
    m_Observers = new Vector(10);
  }//constructor

  public int getObserverCount() {
    synchronized (m_Observers) {
      return m_Observers.size();
    }
  }//getObserverCount

  /**
   * Adds an observer instance if it is not already in the
   * set of observers for this <tt>Observable</tt>.
   *
   * @param o an observer instance to be added.
   */
  public void addObserver(Observer o) {
    synchronized (m_Observers) {
      if (!m_Observers.contains(o)) {
        m_Observers.addElement(o);

      }
    }
  }//addObserver

  /**
   * Removes an observer instance from the set of observers
   * of this <tt>Observable</tt>.
   *
   * @param o an observer instance to be removed.
   */
  public void removeObserver(Observer o) {
    synchronized (m_Observers) {
      m_Observers.removeElement(o);
    }
  }//removeObserver

  /**
   * Removes all observer instances from the set of observers
   * of this <tt>Observable</tt>.
   */
  public void removeObservers() {
    synchronized (m_Observers) {
      m_Observers.removeAllElements();
    }
  }//removeObservers

  /**
   * Notifies all observer instances in the set of observers
   * of this <tt>Observable</tt>.
   *
   * @param arg an arbitrary argument to be passed.
   */
  public void notifyObservers(Object arg) {
    synchronized (m_Observers) {
      for (int i = 0; i < m_Observers.size(); i++) {
        ((Observer) m_Observers.elementAt(i)).update(this, arg);
      }
    }
  }//notifyObservers

}//class Observable
