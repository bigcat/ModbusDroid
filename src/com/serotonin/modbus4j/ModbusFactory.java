package com.serotonin.modbus4j;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;
import com.serotonin.modbus4j.ip.tcp.TcpSlave;
import com.serotonin.modbus4j.ip.udp.UdpMaster;
import com.serotonin.modbus4j.ip.udp.UdpSlave;

public class ModbusFactory {
    //
    // /
    // / Modbus masters
    // /
    //
/*    public ModbusMaster createRtuMaster(SerialParameters params) {
        return new RtuMaster(params);
    }

    public ModbusMaster createAsciiMaster(SerialParameters params) {
        return new AsciiMaster(params);
    }
*/

    public ModbusMaster createTcpMaster(IpParameters params, boolean keepAlive) {
        return new TcpMaster(params, keepAlive);
    }

    public ModbusMaster createUdpMaster(IpParameters params) {
        return new UdpMaster(params);
    }

    //
    // /
    // / Modbus slaves
    // /
    //
/*    public ModbusSlaveSet createRtuSlave(SerialParameters params) {
        return new RtuSlave(params);
    }

    public ModbusSlaveSet createAsciiSlave(SerialParameters params) {
        return new AsciiSlave(params);
    }
    */

    public ModbusSlaveSet createTcpSlave() {
        return new TcpSlave();
    }

    public ModbusSlaveSet createUdpSlave() {
        return new UdpSlave();
    }
}
