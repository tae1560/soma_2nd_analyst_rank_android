package kr.stockrank;

import kr.stockrank.dialog.Exitlistener;
import kr.stockrank.gcm.GCMManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Webview extends Activity {


	WebView mWebView;

	String URL="http://www.stockrank.kr/";

	ProgressBar loadingProgressBar,loadingTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {   
		Ratethisapp.app_launched(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		Intent intent = getIntent();
		String webview_url = intent.getStringExtra("webview_url");

		if(webview_url!=null)URL = webview_url;

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setHorizontalScrollBarEnabled(true); 
		mWebView.getSettings().setLoadsImagesAutomatically(true);

		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		setProgressBarVisibility(true);
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.getSettings().setSupportZoom(false);
		mWebView.getSettings().setBuiltInZoomControls(false);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.setInitialScale(1);

		CommonUtils.debug("notification_id : " + GCMManager.notification_id);
		mWebView.loadUrl(URL + "?regId=" + GCMManager.regId + "&notification_id=" + GCMManager.notification_id);
		//Toast loadingmess = Toast.makeText(this, " Webview App ", Toast.LENGTH_LONG);
		//loadingmess.show();
		mWebView.setWebViewClient(new MyWebViewClient());


		loadingProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal); 
		final Activity activity = this;


		mWebView.setWebViewClient(new WebViewClient() { 


			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) { 
				Toast.makeText(activity, "Internet connection down? " + description, Toast.LENGTH_SHORT).show(); 
				view.loadData("<h1>Please check your internet settings. If you are already connected hit BACK Key on your mobile and re-launch the application.</h1>", "text/html", "UTF-8"); 
			} 

			@Override

			public boolean shouldOverrideUrlLoading(WebView view, String url) {


				if (url != null && url.startsWith("vnd.youtube")) {
					view.getContext().startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					return true;
				} 
				else if (url.contains("rtsp")) {
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
					return true;
				}

				else if (url.startsWith("mailto:")) {
					url = url.replaceFirst("mailto:", "");
					url = url.trim();
					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("plain/text").putExtra(Intent.EXTRA_EMAIL, new String[]{url});
					startActivity(i);
					return true;
				}
				else if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(url), "video/*");
					startActivity(intent);
					return true;

				}	           else if (url.endsWith(".m3u8") || url.startsWith("rtmp")) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(url), "video/*");
					startActivity(intent);
					return true;

				}	

				else if (url.endsWith(".mp3")) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.parse(url), "audio/*");
					view.getContext().startActivity(intent);   
					return true; 
				} 

				else if (url.startsWith("geo:")) {
					Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); 
					startActivity(searchAddress); 
					return true;
				}
				else if (url.startsWith("tel:")) { 
					startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url))); 
					return true; 
				}  else if (url != null && url.startsWith("https://")) {
					view.getContext().startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					return true;
				} else if (url != null && url.startsWith("market://")) {
					view.getContext().startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
					return true;
				} else {
					return false;
				}

			}


		});

		mWebView.setWebChromeClient(new WebChromeClient() {


			@Override

			public void onProgressChanged(WebView view, int newProgress) {


				super.onProgressChanged(view, newProgress);


				loadingProgressBar.setProgress(newProgress);
				if (newProgress == 100) {
					loadingProgressBar.setVisibility(View.GONE);

				} else{
					loadingProgressBar.setVisibility(View.VISIBLE);

				}
			}
		});

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		else
		{
			displayExitPopUP();
			return false;
		}
		//return super.onKeyDown(keyCode, event);
	}

	//Display on exit dialog
	public void  displayExitPopUP()
	{
		/*
		DialogInterface.OnClickListener dialogYesNoClickListener = new Exitlistener(this);

		AlertDialog.Builder builderYesNo = new AlertDialog.Builder(this);
		builderYesNo
				.setMessage(this.getString(R.string.are_you_sure))
				.setPositiveButton(this.getString(R.string.yes),
						dialogYesNoClickListener)
				.setNegativeButton(this.getString(R.string.no),
						dialogYesNoClickListener).show();
		*/
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder =new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.dialog_message)
		.setTitle(R.string.dialog_title);

		// Add the buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK button
				//closeNotification();
				finish();

			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();

		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return new Mainmenu(this)
		.handleMenuSelect(item, mWebView);
	}




	private class MyWebViewClient extends WebViewClient {


		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;


		}

	}


}