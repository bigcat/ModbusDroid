package com.serotonin.modbus4j;

import com.serotonin.io.messaging.DefaultExceptionListener;
import com.serotonin.io.messaging.MessagingConnectionListener;

/**
 * Base level for masters and slaves/listeners
 * 
 * TODO:
 * - handle echoing in RS485
 * 
 * @author mlohbihler
 */
public class Modbus {
    private MessagingConnectionListener exceptionListener = new DefaultExceptionListener();

    public void setExceptionListener(MessagingConnectionListener exceptionListener) {
        if (exceptionListener == null)
            this.exceptionListener = new DefaultExceptionListener();
        else
            this.exceptionListener = exceptionListener;
    }
    
    public MessagingConnectionListener getExceptionListener() {
        return exceptionListener;
    }
}
