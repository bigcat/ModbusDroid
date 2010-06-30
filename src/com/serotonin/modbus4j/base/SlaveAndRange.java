package com.serotonin.modbus4j.base;

public class SlaveAndRange {
    private final int slaveId;
    private final int range;
    
    public SlaveAndRange(int slaveId, int range) {
        ModbusUtils.validateSlaveId(slaveId);
        
        this.slaveId = slaveId;
        this.range = range;
    }
    
    public int getRange() {
        return range;
    }
    public int getSlaveId() {
        return slaveId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + range;
        result = prime * result + slaveId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SlaveAndRange other = (SlaveAndRange) obj;
        if (range != other.range)
            return false;
        if (slaveId != other.slaveId)
            return false;
        return true;
    }
}
