package kr.swmaestro.stock_firm_rank.dialog;

import android.content.DialogInterface;

public class Aboutlistener implements DialogInterface.OnClickListener {

	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			dialog.cancel();
			break;
		}		
	}


}
