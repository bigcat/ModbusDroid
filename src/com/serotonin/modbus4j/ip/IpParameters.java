package com.serotonin.modbus4j.ip;

import com.serotonin.modbus4j.base.ModbusUtils;

public class IpParameters {
    private String host;
    private int port = ModbusUtils.TCP_PORT;
    
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
}
