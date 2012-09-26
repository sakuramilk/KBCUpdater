package net.sakuramilk.kbcupdater;

import android.content.Context;
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
        mTextViewTitle.setText(mEntryItem.title);
        
        mTextViewName = (TextView)view.findViewById(R.id.textView_name);
        mTextViewName.setText(mEntryItem.name);

        mTextViewComment = (TextView)view.findViewById(R.id.textView_comment);
        mTextViewComment.setText(mEntryItem.comment);
        
        super.onBindView(view);
    }
}
