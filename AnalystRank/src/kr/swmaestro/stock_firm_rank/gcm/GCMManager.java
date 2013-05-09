package kr.swmaestro.stock_firm_rank.gcm;

import java.util.HashMap;
import java.util.Map;

import kr.swmaestro.stock_firm_rank.CommonUtils;
import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class GCMManager {
	
	private static String SENDER_ID = "233707069103";
	public static String TAG = "TESTTAG";
	public static String regId = "";
	private static String deviceRegisterURL = "http://www.stockrank.kr/gcm_devices";
	public static int notification_id = -1;
	
	public static void register(Context context) {
		// setup gcm
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		regId = GCMRegistrar.getRegistrationId(context);
		
		TAG = context.getPackageName();
		
		Log.e(TAG, "register started : " + regId );
		
		if (regId.equals("")) {
		  GCMRegistrar.register(context, SENDER_ID);
		} else {
		  Log.e(TAG, "Already registered");
		  Log.e(TAG, "regId : " + regId);
		  registerToSite(regId);
		}
	}
	
	public static void registerToSite(final String id)
	{
		regId = id;

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, String> m_params = new HashMap<String, String>();
				m_params.put("regId", id);
				m_params.put("notification_id", String.valueOf(notification_id));
				String result = CommonUtils.requestWithPost(deviceRegisterURL, m_params);
				CommonUtils.debug(result);
			}
		}).start();
	}
}
