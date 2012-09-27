/*
 * Copyright (C) 2012 sakuramilk <c.sakuramilk@gmail.com>
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

package net.sakuramilk.kbcupdater;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingPreferenceActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingPreferenceFragment.class.getName());
        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT_ARGUMENTS, intent.getExtras());
        
        super.onCreate(savedInstanceState);
    }
}
