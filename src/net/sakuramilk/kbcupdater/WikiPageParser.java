package net.sakuramilk.kbcupdater;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sakuramilk.kbcupdater.HttpContentHandler.OnEntryItemListener;

import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class WikiPageParser implements OnEntryItemListener {
	
	private static final String TAG = WikiPageParser.class.getSimpleName();
	private List<EntryItem> mItemList = new ArrayList<EntryItem>();  

	public void parse(String uri) {
		InputStream in = null;

		try {
			URL url = new URL(uri);
			in = url.openStream();
	        try {
	        	Parser parser = new Parser();
	        	HttpContentHandler handler = new HttpContentHandler();
	        	handler.setOnEntryItemListener(this);
	        	parser.setContentHandler(handler);
	        	parser.parse(new InputSource(new InputStreamReader(in,"UTF-8")));
	        } catch (SAXException e) {
	        	throw new RuntimeException(e);
	        }
	    } catch (MalformedURLException e) {
	    	throw new RuntimeException(e);
	    } catch (IOException e) {
	    	throw new RuntimeException(e);
	    } finally {
	    	if (in != null) {
	    		try {
	    			in.close();
	    		} catch (IOException e) {
	    			throw new RuntimeException(e);
	    		}
	    	}
	    }
		
		Log.i(TAG, "parse end");
	}

	@Override
	public void onEntryItem(EntryItem entryItem) {
		Log.i(TAG, "entryItem.title=" + entryItem.title);
		Log.i(TAG, "entryItem.name=" + entryItem.name);
		Log.i(TAG, "entryItem.comment=" + entryItem.comment);
		Log.i(TAG, "entryItem.url=" + entryItem.url);
		mItemList.add(entryItem);
	}
	
	public List<EntryItem> getEntryItemList() {
		return mItemList;
	}
}
