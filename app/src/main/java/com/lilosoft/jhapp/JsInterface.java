package com.lilosoft.jhapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JsInterface {
	/* interface for javascript to invokes */
	private Activity activity;
	
	public JsInterface(Activity activity){
		this.activity = activity;
	}
	
	@JavascriptInterface
	public String getInterface(){
		return "android_js_interface";
	}
	
	@JavascriptInterface
	public void closeDialog() {
		activity.finish();
	}
}
