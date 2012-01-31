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

package com.bencatlin.modbusdroid.utils.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Abstract base class that can be extended to provide classes that save
 * {@link SharedPreferences} in the most efficient way possible.
 * Decendent classes can optionally choose to backup some {@link SharedPreferences}
 * to the Google {@link BackupService} on platforms where this is available.
 * 
 * BPC - I ganked this from Retro Mier's app for google IO 2011 - 
 * I believe it is apache licensed, and the source is available on google code.
 * 
 */
public abstract class SharedPreferenceSaver {
 
  protected Context context;
 
  protected SharedPreferenceSaver(Context context) {
    this.context = context;
  }
 
  /**
   * Save the Shared Preferences modified through the Editor object.
   * @param editor Shared Preferences Editor to commit.
   * @param backup Backup to the cloud if possible.
   */
  public void savePreferences(SharedPreferences.Editor editor, boolean backup) {}
}

