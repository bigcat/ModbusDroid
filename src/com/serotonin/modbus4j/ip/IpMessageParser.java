package com.serotonin.modbus4j.ip;

import com.serotonin.messaging.IncomingMessage;
import com.serotonin.modbus4j.base.BaseMessageParser;
import com.serotonin.util.queue.ByteQueue;

public class IpMessageParser extends BaseMessageParser {
    public IpMessageParser(boolean master) {
        super(master);
    }

    @Override
    protected IncomingMessage parseMessageImpl(ByteQueue queue) throws Exception {
        if (master)
            return IpMessageResponse.createIpMessageResponse(queue);
        return IpMessageRequest.createIpMessageRequest(queue);
    }
}
