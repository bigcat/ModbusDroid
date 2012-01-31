package com.bencatlin.modbusdroid.utils;
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

import android.os.StrictMode;
import com.bencatlin.modbusdroid.utils.base.IStrictMode;

/**
 * Implementation that supports the Strict Mode functionality
 * available Honeycomb. 
 */
public class HoneycombStrictMode implements IStrictMode {
  protected static String TAG = "HoneycombStrictMode";
  
  /**
   * Enable {@link StrictMode}
   * TODO Set your preferred Strict Mode features.
   */
   public void enableStrictMode() {
     StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
     .detectDiskReads()
     .detectDiskWrites()
     .detectNetwork()
     .penaltyLog()
     .penaltyFlashScreen()
     .build());
   }
}
