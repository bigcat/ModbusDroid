package com.serotonin.modbus4j.base;

import com.serotonin.io.messaging.MessageParser;
import com.serotonin.io.messaging.MessageRequest;
import com.serotonin.io.messaging.MessageResponse;
import com.serotonin.util.queue.ByteQueue;

abstract public class BaseMessageParser implements MessageParser {
    public final MessageRequest parseRequest(ByteQueue queue) throws Exception {
        try {
            return parseRequestImpl(queue);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            // Means that we ran out of data trying to read the message. Just return null.
            return null;
        }
    }
    abstract protected MessageRequest parseRequestImpl(ByteQueue queue) throws Exception;

    public final MessageResponse parseResponse(ByteQueue queue) throws Exception {
        try {
            return parseResponseImpl(queue);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            // Means that we ran out of data trying to read the message. Just return null.
            return null;
        }
    }
    abstract protected MessageResponse parseResponseImpl(ByteQueue queue) throws Exception;

}
