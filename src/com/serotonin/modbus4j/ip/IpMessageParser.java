package com.serotonin.modbus4j.ip;

import android.util.Log;

import com.serotonin.io.messaging.MessageRequest;
import com.serotonin.io.messaging.MessageResponse;
import com.serotonin.modbus4j.base.BaseMessageParser;
import com.serotonin.util.queue.ByteQueue;

public class IpMessageParser extends BaseMessageParser {
    protected MessageResponse parseResponseImpl(ByteQueue queue) throws Exception {
    	Log.i(getClass().getSimpleName(), "Parsing Response");
        return IpMessageResponse.createIpMessageResponse(queue);
    }

    protected MessageRequest parseRequestImpl(ByteQueue queue) throws Exception {
    	Log.i(getClass().getSimpleName(), "Parsing Response");
        return IpMessageRequest.createIpMessageRequest(queue);
    }
}
