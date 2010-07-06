package com.bencatlin.modbusdroid;


import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.tcp.TcpMaster;



public class ModbusTCPFactory extends ModbusFactory {
	
    public ModbusTCPMaster createModbusTCPMaster(IpParameters params, boolean keepAlive) {
        return new ModbusTCPMaster(params, keepAlive);
    }
	
}