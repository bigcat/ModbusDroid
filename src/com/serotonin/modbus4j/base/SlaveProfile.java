package com.serotonin.modbus4j.base;

/**
 * Class for maintaining the profile of a slave device on the master side. Initially, we assume that the device is 
 * fully featured, and then we note function failures so that we know how requests should subsequently be sent.
 * 
 * @author mlohbihler
 */
public class SlaveProfile {
    private boolean writeMaskRegister = true;
    
    public void setWriteMaskRegister(boolean writeMaskRegister) {
        this.writeMaskRegister = writeMaskRegister;
    }
    
    public boolean getWriteMaskRegister() {
        return writeMaskRegister;
    }
}
