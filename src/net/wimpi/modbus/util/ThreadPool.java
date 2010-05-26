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

/**
 * Class implementing a simple thread pool.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class ThreadPool {

  //instance attributes and associations
  private LinkedQueue m_TaskPool;
  private int m_Size = 1;

  /**
   * Constructs a new <tt>ThreadPool</tt> instance.
   *
   * @param size the size of the thread pool.
   */
  public ThreadPool(int size) {
    m_Size = size;
    m_TaskPool = new LinkedQueue();
    initPool();
  }//constructor

  /**
   * Execute the <tt>Runnable</tt> instance
   * through a thread in this <tt>ThreadPool</tt>.
   *
   * @param task the <tt>Runnable</tt> to be executed.
   */
  public synchronized void execute(Runnable task) {
    try {
      m_TaskPool.put(task);
    } catch (InterruptedException ex) {
      //FIXME: Handle!?
    }
  }//execute

  /**
   * Initializes the pool, populating it with
   * n started threads.
   */
  protected void initPool() {
    for (int i = m_Size; --i >= 0;) {
      new PoolThread().start();
    }
  }//initPool

  /**
   * Inner class implementing a thread that can be
   * run in a <tt>ThreadPool</tt>.
   *
   * @author Dieter Wimberger
   * @version 1.2rc1 (09/11/2004)
   */
  private class PoolThread extends Thread {

    /**
     * Runs the <tt>PoolThread</tt>.
     * <p>
     * This method will infinitely loop, picking
     * up available tasks from the <tt>LinkedQueue</tt>.
     */
    public void run() {
      //System.out.println("Running PoolThread");
      do {
        try {
          //System.out.println(this.toString());
          ((Runnable) m_TaskPool.take()).run();
        } catch (Exception ex) {
          //FIXME: Handle somehow!?
          ex.printStackTrace();
        }
      } while (true);
    }
  }//PoolThread

}//ThreadPool
