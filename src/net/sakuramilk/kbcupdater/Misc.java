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

public class Misc {
	
	/**
	 * 文字列がnullまたは長さ0かどうか調べる
	 * @param value		対象の文字列
	 * @return			文字列がnullまたは長さ0の場合true
	 */
    public static boolean isNullOfEmpty(String value) {
        return (value == null || "".equals(value));
    }
    
    /**
     * 渡されたvalueに一致するentryValueを返却する
     * @param entries      値配列
     * @param entryValues  エントリー値配列
     * @param value        対象の値
     * @return             一致するエントリー値、一致する値が無い場合は対象の値が返却される
     */
    public static String getEntryFromEntryValue(String[] entries, String[] entryValues, String value) {
        if (entries == null || entryValues == null) {
            return value;
        }
        for (int i = 0; i < entries.length; i++) {
            if (entryValues[i].equals(value)) {
                return entries[i];
            }
        }
        return value; // if not found value, return input value.
    }

    /**
     * 渡されたvalueに一致するentryValueを返却する
     * @param entries      値配列
     * @param entryValues  エントリー値配列
     * @param value        対象の値
     * @return             一致するエントリー値、一致する値が無い場合は対象の値が返却される
     */
    public static String getEntryFromEntryValue(CharSequence[] entries, CharSequence[] entryValues, String value) {
        if (entries == null || entryValues == null) {
            return value;
        }
        for (int i = 0; i < entries.length; i++) {
            if (entryValues[i].toString().equals(value)) {
                return entries[i].toString();
            }
        }
        return value; // if not found value, return input value.
    }

    /**
     * 渡されたvalueに一致するentryValueを返却する
     * @param entries      値配列
     * @param entryValues  エントリー値配列
     * @param value        対象の値
     * @return             一致するエントリー値、一致する値が無い場合は対象の値が返却される
     */
    public static String getEntryFromEntryValue(String[] entries, String[] entryValues, int value) {
        return getEntryFromEntryValue(entries, entryValues, String.valueOf(value));
    }

    /**
     * 渡されたvalueに一致するentryValueを返却する
     * @param entries      値配列
     * @param entryValues  エントリー値配列
     * @param value        対象の値
     * @return             一致するエントリー値、一致する値が無い場合は対象の値が返却される
     */
    public static String getEntryFromEntryValue(CharSequence[] entries, CharSequence[] entryValues, int value) {
        return getEntryFromEntryValue(entries, entryValues, String.valueOf(value));
    }
}
