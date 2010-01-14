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
package net.wimpi.modbus.cmd;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.io.ModbusTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleRegister;

import java.net.InetAddress;

/**
 * Class that implements a simple commandline
 * tool which demonstrates how a analog input
 * can be bound with a analog output.
 * <p/>
 * Note that if you write to a remote I/O with
 * a Modbus protocol stack, it will most likely
 * expect that the communication is <i>kept alive</i>
 * after the first write message.<br>
 * This can be achieved either by sending any kind of
 * message, or by repeating the write message within a
 * given period of time.<br>
 * If the time period is exceeded, then the device might
 * react by turning pos all signals of the I/O modules.
 * After this timeout, the device might require a
 * reset message.
 *
 * @author Dieter Wimberger
 * @version 1.2rc1 (09/11/2004)
 */
public class AIAOTest {

  public static void main(String[] args) {

    InetAddress addr = null;
    TCPMasterConnection con = null;
    ModbusRequest ai_req = null;
    WriteSingleRegisterRequest ao_req = null;

    ModbusTransaction ai_trans = null;
    ModbusTransaction ao_trans = null;

    int ai_ref = 0;
    int ao_ref = 0;
    int port = Modbus.DEFAULT_PORT;

    try {

      //1. Setup the parameters
      if (args.length < 3) {
        printUsage();
        System.exit(1);
      } else {
        try {
          String astr = args[0];
          int idx = astr.indexOf(':');
          if (idx > 0) {
            port = Integer.parseInt(astr.substring(idx + 1));
            astr = astr.substring(0, idx);
          }
          addr = InetAddress.getByName(astr);
          ai_ref = Integer.parseInt(args[1]);
          ao_ref = Integer.parseInt(args[2]);

        } catch (Exception ex) {
          ex.printStackTrace();
          printUsage();
          System.exit(1);
        }
      }

      //2. Open the connection
      con = new TCPMasterConnection(addr);
      con.setPort(port);
      con.connect();
      if (Modbus.debug) System.out.println("Connected to " + addr.toString() + ":" + con.getPort());

      //3. Prepare the requests
      ai_req = new ReadInputRegistersRequest(ai_ref, 1);
      ao_req = new WriteSingleRegisterRequest();
      ao_req.setReference(ao_ref);

      ai_req.setUnitID(0);
      ao_req.setUnitID(0);


      //4. Prepare the transactions
      ai_trans = new ModbusTCPTransaction(con);
      ai_trans.setRequest(ai_req);
      ao_trans = new ModbusTCPTransaction(con);
      ao_trans.setRequest(ao_req);

      //5. Prepare holders to update only on change
      SimpleRegister new_out = new SimpleRegister(0);
      ao_req.setRegister(new_out);
      int last_out = Integer.MIN_VALUE;

      //5. Execute the transaction repeatedly
      do {
        ai_trans.execute();
        int new_in =
            ((ReadInputRegistersResponse) ai_trans.getResponse()).getRegister(0).getValue();

        //write only if differ
        if (new_in != last_out) {
          new_out.setValue(new_in); //update register
          ao_trans.execute();
          last_out = new_in;
          if(Modbus.debug) System.out.println("Updated Register with value from Input Register.");
        }
      } while (true);

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      //6. Close the connection
      con.close();
    }
  }//main

  private static void printUsage() {
    System.out.println("java net.wimpi.modbus.cmd.AIAOTest <address{:<port>} [String]> <register a_in [int16]> <register a_out [int16]>");
  }//printUsage

}//class AIAOTest
