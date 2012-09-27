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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class SettingPreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener {
	
	private ListPreference mUpdateInterval;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_pref);
		
		mUpdateInterval = (ListPreference) findPreference("update_interval");
		mUpdateInterval.setOnPreferenceChangeListener(this);
    }

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {

		if (preference == mUpdateInterval) {
			mUpdateInterval.setSummary(Misc.getEntryFromEntryValue(
						mUpdateInterval.getEntries(), mUpdateInterval.getEntryValues(), (String)newValue));
			
			long timeoutMs = Integer.valueOf((String)newValue) * 60 * 1000;
			Intent serviceIntent = new Intent(getActivity(), UpdateService.class);
        	PendingIntent pendingIntent = PendingIntent.getService(
        			getActivity(), 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        	AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        	am.cancel(pendingIntent);
        	am.set(AlarmManager.RTC_WAKEUP, timeoutMs, pendingIntent);
			
			return true;
		}
		return false;
	}
}
