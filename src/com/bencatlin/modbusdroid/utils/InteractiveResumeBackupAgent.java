package com.bencatlin.modbusdroid.utils;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

public class InteractiveResumeBackupAgent extends BackupAgentHelper {
	@Override
	  public void onCreate() {
	    SharedPreferencesBackupHelper helper = new
	      SharedPreferencesBackupHelper(this, "SHARED_PREFERENCE_FILE");
	    //addHelper(PlacesConstants.SP_KEY_FOLLOW_LOCATION_CHANGES, helper);
	  }
}
