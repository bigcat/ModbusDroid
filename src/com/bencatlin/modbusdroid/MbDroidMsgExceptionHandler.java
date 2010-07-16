package com.bencatlin.modbusdroid;

import android.util.Log;

import com.serotonin.io.messaging.MessagingConnectionListener;


public class MbDroidMsgExceptionHandler implements MessagingConnectionListener {

	public void receivedException(Exception genericException) {
		// TODO Auto-generated method stub
		Log.e(getClass().getSimpleName(), genericException.getMessage() );
	}

	public void receivedMessageMismatchException(Exception mismatchException) {
		// TODO Auto-generated method stub
		Log.e(getClass().getSimpleName(), mismatchException.getMessage() );
	}

	public void receivedResponseException(Exception responseException) {
		// TODO Auto-generated method stub
		Log.e(getClass().getSimpleName(), responseException.getMessage() );
	}
	
}