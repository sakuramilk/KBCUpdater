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
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class EntryItemPreference extends Preference {

	TextView mTextViewTitle;
	TextView mTextViewName;
	TextView mTextViewComment;

	public EntryItemPreference(Context context) {
		this(context, null);
	}

	public EntryItemPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EntryItemPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		setLayoutResource(R.layout.entry_item_prefernce);
	}

	private EntryItem mEntryItem;
	
	public void setEntryItem(EntryItem entryItem) {
		mEntryItem = entryItem;
	}

	public EntryItem getEnteryItem() {
		return mEntryItem;
	}

	@Override
    protected void onBindView(View view) {
        mTextViewTitle = (TextView)view.findViewById(R.id.textView_title);
        String title;
        if (Misc.isNullOfEmpty(mEntryItem.subCategory)) {
        	title = mEntryItem.title;
        } else {
        	title = mEntryItem.subCategory + " " + mEntryItem.title;
        }
        mTextViewTitle.setText(title);
        
        if (mEntryItem.update) {
        	mTextViewTitle.setTextColor(Color.rgb(255, 200, 200));
        	mTextViewTitle.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
        	mTextViewTitle.setTextColor(Color.WHITE);
        	mTextViewTitle.setTypeface(Typeface.DEFAULT);
        }
        
        
        mTextViewName = (TextView)view.findViewById(R.id.textView_name);
        mTextViewName.setText(mEntryItem.name);

        mTextViewComment = (TextView)view.findViewById(R.id.textView_comment);
        mTextViewComment.setText(mEntryItem.comment);
        
        super.onBindView(view);
    }
}
