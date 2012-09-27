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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

public class EntryItemProvider extends ContentProvider {

	static final String DB_FILE_NAME = "entry_item.db";
	static final String DB_TABLE_NAME = "entry_item_tbl";
	private DatabaseHelper mDatabaseHelper;

	private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DB_FILE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DB_TABLE_NAME + " (" +
            		BaseColumns._ID + " INTEGER PRIMARY KEY," +
            		"target_url TEXT," +
            		"category TEXT," +
            		"sub_category TEXT," +
            		"title TEXT," +
            		"name TEXT," +
            		"comment TEXT," +
            		"url TEXT" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
            onCreate(db);
        }
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        db.delete(DB_TABLE_NAME, selection, selectionArgs);
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.insert(DB_TABLE_NAME, null, values);
		return null;
	}

	@Override
	public boolean onCreate() {
		mDatabaseHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DB_TABLE_NAME);
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}
}
