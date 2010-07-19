package com.serotonin.modbus4j.base;

import com.serotonin.messaging.IncomingMessage;
import com.serotonin.messaging.MessageParser;
import com.serotonin.util.queue.ByteQueue;

abstract public class BaseMessageParser implements MessageParser {
    protected final boolean master;

    public BaseMessageParser(boolean master) {
        this.master = master;
    }

    //@Override
    public IncomingMessage parseMessage(ByteQueue queue) throws Exception {
        try {
            return parseMessageImpl(queue);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            // Means that we ran out of data trying to read the message. Just return null.
            return null;
        }
    }

    abstract protected IncomingMessage parseMessageImpl(ByteQueue queue) throws Exception;
}
