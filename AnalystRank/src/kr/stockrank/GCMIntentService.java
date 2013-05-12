package kr.stockrank;

import kr.stockrank.dialog.AlertDialogActivity;
import kr.stockrank.gcm.GCMManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.e(GCMManager.TAG,"error : " + arg1); 	
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Log.d("test","message:"+message);
		Log.e(GCMManager.TAG,"message:"+intent.toString());
		
		String message = intent.getStringExtra("message_text");
		String title = intent.getStringExtra("title_text");
		String notification_id_string = intent.getStringExtra("notification_id");
		int notification_id = -1;
		if (notification_id_string != null)
			notification_id = Integer.parseInt(notification_id_string);
		
		//int notification_id = intent.getIntExtra("notification_id", -1);
		
		Bundle bun = new Bundle();
		bun.putString("notiMessage", message); 
		bun.putString("notiTitle", title);
		bun.putInt("notification_id", notification_id);

		Intent popupIntent = new Intent(getApplicationContext(), AlertDialogActivity.class);

		popupIntent.putExtras(bun);
		PendingIntent pie= PendingIntent.getActivity(getApplicationContext(), 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
		try {
		 pie.send();
		} catch (CanceledException e) {
//		 Log  Util.degug(e.getMessage());
		}
	        
//		    context.finish(); 
//		
//		try {
//			//String title = intent.getStringExtra(PUSH_DATA_TITLE);
//			String message = intent.getStringExtra("message_text");
//			Vibrator vibrator = 
//			 (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//			vibrator.vibrate(1000);
//			setNotification(context, "증권사 RANK", message);
//		} catch (Exception e) {
//			Log.e(GCMManager.TAG, "[onMessage] Exception : " + e.getMessage());
//		}
	}
	
	private void setNotification(Context context, String title, String message) {
		NotificationManager notificationManager = null;
		Notification notification = null;
		try {
			notificationManager = (NotificationManager) context
	 				.getSystemService(Context.NOTIFICATION_SERVICE);
			notification = new Notification(R.drawable.ic_launcher,
					message, System.currentTimeMillis());
			Intent intent = new Intent(context, Splash.class);
			PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
			notification.setLatestEventInfo(context, title, message, pi);
			notificationManager.notify(0, notification);
		} catch (Exception e) {
			Log.e(GCMManager.TAG, "[setNotification] Exception : " + e.getMessage());
		}
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) { 
		// TODO Auto-generated method stub
		Log.e(GCMManager.TAG,"등록ID:"+arg1);
		GCMManager.registerToSite(arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		Log.e(GCMManager.TAG,"해지ID:"+arg1);	
	}
	

}
