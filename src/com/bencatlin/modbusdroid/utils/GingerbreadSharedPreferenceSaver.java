/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bencatlin.modbusdroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Save {@link SharedPreferences} using the asynchronous apply method available
 * in Gingerbread, and provide the option to notify the BackupManager to 
 * initiate a backup.
 */
public class GingerbreadSharedPreferenceSaver extends FroyoSharedPreferenceSaver {
    
  public GingerbreadSharedPreferenceSaver(Context context) {
    super(context);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void savePreferences(SharedPreferences.Editor editor, boolean backup) {
    editor.apply();
    backupManager.dataChanged();
  }
}
