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

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class UpdatePreference extends Preference {

	TextView mTextViewTitle;

	public UpdatePreference(Context context) {
		this(context, null);
	}
	
	public UpdatePreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public UpdatePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setLayoutResource(R.layout.update_prefernce);
	}
	
	private String mTitle;
	
	public void setTitle(CharSequence title) {
		mTitle = title.toString();
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	@Override
	protected void onBindView(View view) {
	    mTextViewTitle = (TextView)view.findViewById(R.id.textView_title);
        mTextViewTitle.setText(mTitle);

	    super.onBindView(view);
	}
	
}
