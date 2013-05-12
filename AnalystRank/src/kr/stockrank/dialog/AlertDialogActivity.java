package kr.stockrank.dialog;

import kr.stockrank.CommonUtils;
import kr.stockrank.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;


public class AlertDialogActivity extends Activity {

	private String notiTitle;
 private String notiMessage;
 private int notification_id;

 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  requestWindowFeature(Window.FEATURE_NO_TITLE);
  Bundle bun = getIntent().getExtras();
  notiTitle = bun.getString("notiTitle");
  notiMessage = bun.getString("notiMessage");
  notification_id = bun.getInt("notification_id", -1);
  
  CommonUtils.debug("alert notification_id : " + notification_id);
  
//  GCMManager.notification_id = notification_id;
   
  setContentView(R.layout.alertdialog);
 
  AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
  
  alertDialog.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
//          PushWakeLock.releaseCpuLock(); 
    	  AlertDialogActivity.this.finish(); 
      }
  });
   
  alertDialog.setNegativeButton("보기", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
    	  Intent intent = new Intent();
    	  intent.setClassName(getPackageName(), getPackageName()+".Splash");
    	  intent.putExtra("notification_id", notification_id);
    	  startActivity(intent);
//          PushWakeLock.releaseCpuLock();
    	  AlertDialogActivity.this.finish();
      }
  });
   
  alertDialog.setTitle(notiTitle);
  alertDialog.setMessage(notiMessage);
  alertDialog.show();

 }
 private class SubmitOnClickListener implements OnClickListener {

  public void onClick(View v) {
   finish();

  }
 }
}