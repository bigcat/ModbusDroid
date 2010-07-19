package com.serotonin.modbus4j;

import com.serotonin.messaging.DefaultMessagingExceptionHandler;
import com.serotonin.messaging.MessagingExceptionHandler;

/**
 * Base level for masters and slaves/listeners
 * 
 * TODO: - handle echoing in RS485
 * 
 * @author mlohbihler
 */
public class Modbus {
    private MessagingExceptionHandler exceptionHandler = new DefaultMessagingExceptionHandler();

    public void setExceptionHandler(MessagingExceptionHandler exceptionHandler) {
        if (exceptionHandler == null)
            this.exceptionHandler = new DefaultMessagingExceptionHandler();
        else
            this.exceptionHandler = exceptionHandler;
    }

    public MessagingExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }
}
