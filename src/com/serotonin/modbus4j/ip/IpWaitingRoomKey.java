package com.serotonin.modbus4j.ip;

import com.serotonin.messaging.WaitingRoomKey;

public class IpWaitingRoomKey implements WaitingRoomKey {
    private final int transactionId;
    private final int slaveId;
    private final byte functionCode;

    public IpWaitingRoomKey(int transactionId, int slaveId, byte functionCode) {
        this.transactionId = transactionId;
        this.slaveId = slaveId;
        this.functionCode = functionCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + functionCode;
        result = prime * result + slaveId;
        result = prime * result + transactionId;
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
        IpWaitingRoomKey other = (IpWaitingRoomKey) obj;
        if (functionCode != other.functionCode)
            return false;
        if (slaveId != other.slaveId)
            return false;
        if (transactionId != other.transactionId)
            return false;
        return true;
    }
}
