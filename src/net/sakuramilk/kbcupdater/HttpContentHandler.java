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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import android.util.Log;

public class HttpContentHandler implements ContentHandler {

	private static final String TAG = HttpContentHandler.class.getSimpleName();
	private boolean mBodyStart = false;
	private boolean mInATag = false;
	private boolean mInH1Tag = false;
	private boolean mInH2Tag = false;
	private boolean mInH3Tag = false;
	private boolean mInPTag = false;
	private final Pattern pattern_div = Pattern.compile("div", Pattern.CASE_INSENSITIVE);
	private final Pattern pattern_a = Pattern.compile("a", Pattern.CASE_INSENSITIVE);
	private final Pattern pattern_h1 = Pattern.compile("h1", Pattern.CASE_INSENSITIVE);
	private final Pattern pattern_h2 = Pattern.compile("h2", Pattern.CASE_INSENSITIVE);
	private final Pattern pattern_h3 = Pattern.compile("h3", Pattern.CASE_INSENSITIVE);
	private final Pattern pattern_p = Pattern.compile("p", Pattern.CASE_INSENSITIVE);
	private final Pattern pattern_category = Pattern.compile("\\[.*?\\]");

	private EntryItem mCurrentEntryItem;
	private OnEntryItemListener mListener;

	public interface OnEntryItemListener {
        public void onEntryItem(EntryItem entryItem);
    }

	public void setOnEntryItemListener(OnEntryItemListener listener) {
		mListener = listener;
	}

	@Override
	public void startDocument() throws SAXException {
		Log.i(TAG, "startDocument");
	}

	@Override
	public void endDocument() throws SAXException {
		Log.i(TAG, "endDocument");
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

		if (mBodyStart) {
			if (pattern_h1.matcher(localName).find()) {
				Log.i(TAG, "[h1] start element");
				mInH1Tag = true;

			} else if (pattern_h2.matcher(localName).find()) {
				Log.i(TAG, "[h2] start element");
				mInH2Tag = true;
				mCurrentEntryItem = new EntryItem();

			} else if (pattern_h3.matcher(localName).find()) {
				Log.i(TAG, "[h3] start element");
				mInH3Tag = true;

			} else if (pattern_a.matcher(localName).find()) {
				Log.i(TAG, "[a] start element");
				mInATag = true;
				for (int i = 0; i < atts.getLength(); i++) {
					if ("href".equals(atts.getLocalName(i))) {
						mCurrentEntryItem.url = atts.getValue(i);
					}
				}

			} else if (pattern_p.matcher(localName).find()) {
				Log.i(TAG, "[p] start element");
				mInPTag = true;

			}
		} else {
			for (int i = 0; i < atts.getLength(); i++) {
				if ("div".equals(localName) && "class".equals(atts.getLocalName(i))) {
					if ("markdown-body".equals(atts.getValue(i))) {
						mBodyStart = true;
						Log.i(TAG, "!!! set mBodyStart to true !!!");
					}
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (mBodyStart) {
			if (pattern_h1.matcher(localName).find()) {
				Log.i(TAG, "[h1] end element");
				mInH1Tag = false;

			} else if (pattern_h2.matcher(localName).find()) {
				Log.i(TAG, "[h2] end element");
				mInH2Tag = false;

			} else if (pattern_h3.matcher(localName).find()) {
				Log.i(TAG, "[h3] end element");
				mInH3Tag = false;

			} else if (pattern_a.matcher(localName).find()) {
				Log.i(TAG, "[a] end element");
				mInATag = false;

			} else if (pattern_p.matcher(localName).find()) {
				Log.i(TAG, "[p] end element");
				mInPTag = false;
				if (mCurrentEntryItem != null && mListener != null) {
					mListener.onEntryItem(mCurrentEntryItem);
				}

			} else if (pattern_div.matcher(localName).find()) {
				mBodyStart = false;
				Log.i(TAG, "!!! set mBodyStart to false !!!");

			}
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		Log.i(TAG, "startPrefixMapping prefix=" + prefix);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		Log.i(TAG, "endPrefixMapping prefix=" + prefix);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (mBodyStart) {
			if (mInH1Tag) {
				String str = new String(ch, start, length);
				Log.i(TAG, "[h1] characters str=" + str);

			} else if (mInH2Tag) {
				String str = new String(ch, start, length);
				Log.i(TAG, "[h2] characters str=" + str);
				Matcher matcher = pattern_category.matcher(str);
				int pos = 0;
				while (matcher.find()) {
					if (Misc.isNullOfEmpty(mCurrentEntryItem.category)) {
						mCurrentEntryItem.category = matcher.group();
					} else {
						mCurrentEntryItem.category += " " + matcher.group();
					}
					pos = matcher.end();
				}
				mCurrentEntryItem.title = str.substring(pos).trim();
				
			} else if (mInH3Tag) {
				String str = new String(ch, start, length);
				Log.i(TAG, "[h3] characters str=" + str);

			} else if (mInATag) {
				mCurrentEntryItem.name = new String(ch, start, length);
				Log.i(TAG, "[a] characters str=" + mCurrentEntryItem.name);

			} else if (mInPTag) {
				String str = new String(ch, start, length);
				Log.i(TAG, "[p] characters str=" + str);
				
				Matcher matcher = pattern_category.matcher(str);
				if (matcher.find()) {
					if (!Misc.isNullOfEmpty(mCurrentEntryItem.url)) {
						if (mCurrentEntryItem != null && mListener != null) {
							mListener.onEntryItem(mCurrentEntryItem);
						}
						EntryItem prevItem = mCurrentEntryItem;
						mCurrentEntryItem = new EntryItem();
						mCurrentEntryItem.title = prevItem.title;
						mCurrentEntryItem.category = prevItem.category;
					}
					mCurrentEntryItem.subCategory = matcher.group();
				} else {
					if (Misc.isNullOfEmpty(mCurrentEntryItem.comment)) {
						mCurrentEntryItem.comment = "";
					}
					mCurrentEntryItem.comment += str;
				}
			}
		}
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
		Log.i(TAG, "processingInstruction target=" + target + " data=" + data);
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		Log.i(TAG, "setDocumentLocator");
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		Log.i(TAG, "skippedEntity name=" + name);
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		Log.i(TAG, "ignorableWhitespace");
	}
}
