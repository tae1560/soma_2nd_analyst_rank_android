package kr.stockrank;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import kr.stockrank.gcm.GCMManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class Splash extends Activity {

	Class<?> activityClass;
	Class[] paramTypes = { Integer.TYPE, Integer.TYPE };

	Method overrideAnimation = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Intent intent = getIntent();
		final int notification_id = intent.getIntExtra("notification_id", -1);
		final String webview_url = intent.getStringExtra("webview_url");
		
		CommonUtils.debug("notification_id : " + notification_id);
		
		GCMManager.notification_id = notification_id;
		GCMManager.register(this);
		
		
		try {
			activityClass = Class.forName("android.app.Activity");
			overrideAnimation = activityClass.getDeclaredMethod(
					"overridePendingTransition", paramTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			public void run() {
				Intent i = new Intent(Splash.this,
						Webview.class);
				
				i.putExtra("notification_id", notification_id);
				i.putExtra("webview_url", webview_url);
				startActivity(i);
				finish();
				if (overrideAnimation != null) {
					try {
						overrideAnimation.invoke(Splash.this, android.R.anim.fade_in,
								android.R.anim.fade_out);
					} catch (IllegalArgumentException e) {
						
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						
						e.printStackTrace();
					}
				}
			}
		}, 3000);
	}
}
