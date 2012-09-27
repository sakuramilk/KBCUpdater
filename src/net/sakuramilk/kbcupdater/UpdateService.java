package net.sakuramilk.kbcupdater;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {

	private static final String TAG = "UpdateService";
	private NotificationManager mNotificationManager;

	private void updateList(String url) {
		int count = 0;
		List<EntryItem> list = EntryItemManager.load(this, url, "update");
		for (EntryItem item : list) {
			if (item.update) {
				count++;
			}
		}
		
		if (count > 0) {
			Intent notificationIntent = new Intent(this, MainFragmentActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this,
			        1000, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			
			Resources res = getResources();
			Notification.Builder builder = new Notification.Builder(this);

			builder.setContentIntent(contentIntent)
			            .setSmallIcon(R.drawable.ic_launcher)
			            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
			            .setTicker("TICKER")
			            .setWhen(System.currentTimeMillis())
			            .setAutoCancel(true)
			            .setContentTitle("TITLE")
			            .setContentText("TEXT");
			Notification notification = builder.getNotification();
			mNotificationManager.notify(1001, notification);
		}
	}
	
	@Override
    public void onStart(Intent intent, int StartId) {
		Log.i(TAG, "onStart");
		
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		updateList(Constant.LIST_URL_SC06D);
		updateList(Constant.LIST_URL_SC02C);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		return null;
	}
}
