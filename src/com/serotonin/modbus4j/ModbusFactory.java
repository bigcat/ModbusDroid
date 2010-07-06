package com.serotonin.modbus4j;

import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpSlave;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;
import com.serotonin.modbus4j.ip.udp.UdpMaster;
import com.serotonin.modbus4j.ip.udp.UdpSlave;
//import com.serotonin.modbus4j.serial.ascii.AsciiSlave;
//import com.serotonin.modbus4j.serial.ascii.AsciiMaster;
//import com.serotonin.modbus4j.serial.rtu.RtuSlave;
//import com.serotonin.modbus4j.serial.rtu.RtuMaster;

public class ModbusFactory {
    //
    ///
    /// Modbus masters
    ///
    //

/* Need to blow away the Serial parts to get this to work on android 
 * 

  	public ModbusMaster createRtuMaster(SerialParameters params, boolean reverseCRC) {

        return new RtuMaster(params, reverseCRC);
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
    ///
    /// Modbus slaves
    ///
    //
  
/* Need to blow away the Serial parts to get this to work on android     
 * 
    
    public ModbusSlave createRtuSlave(ProcessImage processImage, int slaveId, SerialParameters params,
            boolean reverseCRC) {
        return new RtuSlave(processImage, slaveId, params, reverseCRC);
    }
    
    public ModbusSlave createAsciiSlave(ProcessImage processImage, int slaveId, SerialParameters params) {
        return new AsciiSlave(processImage, slaveId, params);
    }
*/    
    
    public ModbusSlave createTcpSlave(ProcessImage processImage, int slaveId) {
        return new TcpSlave(processImage, slaveId);
    }
    
    public ModbusSlave createUdpSlave(ProcessImage processImage, int slaveId) {
        return new UdpSlave(processImage, slaveId);
    }
}
