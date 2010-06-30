package com.serotonin.modbus4j.ip;

import com.serotonin.io.messaging.Message;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.msg.ModbusMessage;
import com.serotonin.util.queue.ByteQueue;

public class IpMessage implements Message {
    protected int transactionId;
    protected ModbusMessage modbusMessage;
    
    public IpMessage(ModbusMessage modbusMessage, int transactionId) {
        this.modbusMessage = modbusMessage;
        this.transactionId = transactionId;
    }
    
    public byte[] getMessageData() {
        ByteQueue msgQueue = new ByteQueue();
        
        // Write the particular message.
        modbusMessage.write(msgQueue);
        
        // Create the ip message
        ByteQueue ipQueue = new ByteQueue();
        ModbusUtils.pushShort(ipQueue, transactionId);
        ModbusUtils.pushShort(ipQueue, ModbusUtils.IP_PROTOCOL_ID);
        ModbusUtils.pushShort(ipQueue, msgQueue.size());
        ipQueue.push(msgQueue);
        
        // Return the data.
        return ipQueue.popAll();
    }
}
