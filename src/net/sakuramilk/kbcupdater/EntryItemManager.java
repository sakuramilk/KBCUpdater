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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class EntryItemManager {
	
	private static final Uri CONTENT_URI = Uri.parse("content://net.sakuramilk.kbcupdater.entryitemprovider");

	public static List<EntryItem> load(Context context, String url, String src) {
		List<EntryItem> entryItemList = null;
		
		ContentResolver contentResolver = context.getContentResolver();
		String selection = "target_url='" + url + "'";
		Cursor cur = contentResolver.query(CONTENT_URI, null, selection, null, null);
		if (cur != null && cur.getCount() > 0) {
			entryItemList = new ArrayList<EntryItem>();
			while (cur.moveToNext()) {
				EntryItem entryItem = new EntryItem();
				entryItem.category = cur.getString(2);
				entryItem.subCategory = cur.getString(3);
				entryItem.title = cur.getString(4);
				entryItem.name = cur.getString(5);
				entryItem.comment = cur.getString(6);
				entryItem.url = cur.getString(7);
				entryItemList.add(entryItem);
			}
		}
		
		if (entryItemList == null || "update".equals(src)) {
			List<EntryItem> updateEntryItemList = update(context, url);
			save(context, url, updateEntryItemList);
			if (entryItemList == null) {
				entryItemList = updateEntryItemList;
			} else {
				for (int i = 0; i < updateEntryItemList.size(); i++) {
					EntryItem updateItem = updateEntryItemList.get(i); 
					for (int j = 0; j < entryItemList.size(); j++) {
						EntryItem savedItem = entryItemList.get(j);
						if (!updateItem.category.equals(savedItem.category)) {
							continue;
						}
						if (!updateItem.title.equals(savedItem.title)) {
							continue;
						}
						if (updateItem.subCategory != null && savedItem.subCategory != null) {
							if (!updateItem.subCategory.equals(savedItem.subCategory)) {
								continue;
							}
						}
						
						if (!updateItem.url.equals(savedItem.url)) {
							updateItem.update = true;
						}
						break;
					}
				}
				entryItemList = updateEntryItemList;
			}
		}

		return entryItemList;
	}
	
	public static void save(Context context, String url, List<EntryItem> entryItemList) {
		ContentResolver contentResolver = context.getContentResolver();
		contentResolver.delete(CONTENT_URI, "target_url='" + url + "'", null);
		for (EntryItem item : entryItemList) {
			ContentValues values = new ContentValues();
			values.put("target_url", url);
			values.put("category", item.category);
			values.put("sub_category", item.subCategory);
			values.put("title", item.title);
			values.put("name", item.name);
			values.put("comment", item.comment);
			values.put("url", item.url);
			contentResolver.insert(CONTENT_URI, values);
		}
	}
	
	private static List<EntryItem> update(Context context, String url) {
		WikiPageParser parser = new WikiPageParser();
		parser.parse(url);
		return parser.getEntryItemList();
	}
}
