package com.bencatlin.modbusdroid;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.serotonin.messaging.MessagingExceptionHandler;



public class MbDroidMsgExceptionHandler implements MessagingExceptionHandler {
	
	private Handler mainThreadHandler;
	private Message m;
	
	public MbDroidMsgExceptionHandler (Handler h) {
		this.mainThreadHandler = h;
	}
	
	
	public void receivedException(Exception genericException) {
		// TODO Auto-generated method stub
		Log.e(getClass().getSimpleName(), genericException.getMessage() );
		m.arg1 = -1;
		m.obj = genericException.getMessage();
		mainThreadHandler.sendMessage(m);
	}

	public void receivedMessageMismatchException(Exception mismatchException) {
		// TODO Auto-generated method stub
		Log.e(getClass().getSimpleName(), mismatchException.getMessage() );
		m.arg1 = -1;
		m.obj = mismatchException.getMessage();
		mainThreadHandler.sendMessage(m);
	}

	public void receivedResponseException(Exception responseException) {
		// TODO Auto-generated method stub
		Log.e(getClass().getSimpleName(), responseException.getMessage() );
		m.arg1 = -1;
		m.obj = responseException.getMessage();
		mainThreadHandler.sendMessage(m);
	}
	
}