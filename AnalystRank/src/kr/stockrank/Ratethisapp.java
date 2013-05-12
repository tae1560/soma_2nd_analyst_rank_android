package kr.stockrank;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Ratethisapp {
    private final static String APP_TITLE = "증권사 RANK";// Your App Name Or Anything
    private final static String APP_PACKAGENAME = "kr.swmaestro.stock_firm_rank";// Package Name ( must match play store package name )

    private final static int DAYS_UNTIL_PROMPT = 1;//Minimum number of days to prompt the rate dialog 
    // Recommended 1 or 2 days after installation OR
    //private final static int LAUNCHES_UNTIL_PROMPT = 1;//Minimum number of launches to prompt the dialog
    // Recommended 3-4 launches 
    private final static int LAUNCHES_UNTIL_PROMPT = 4;
    

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }   

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        // --> you can change the text here <--
        tv.setText("" + APP_TITLE + " 가 맘에 드신다면, 마켓에 평가를 부탁드립니다. 감사합니다.");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
           
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGENAME)));
                dialog.dismiss();
            }
        });        
        ll.addView(b1);
// change 2nd button text ( let me test this app first or something )
        Button b2 = new Button(mContext);
        b2.setText("더 사용해 보기");
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);
// Change the 3rd button never show again or something like that
        Button b3 = new Button(mContext);
        b3.setText("Dont Show Again");
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);        
        dialog.show();        
    }
}