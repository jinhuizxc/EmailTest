package com.example.emailtest.email;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.TextView;

import com.example.emailtest.R;

public class NotePadDetailActivity extends Activity {
	
	private TextView mNotepadTime; //时间
	private TextView mNotepadName; //主题
	
	private WebView mNotepadContent; //内容
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notepad_detail);
		initViews();
/*		intent.putExtra("com", hashMap.get("com").toString());
		intent.putExtra("datatime", hashMap.get("datatime").toString());
		intent.putExtra("fileMath", hashMap.get("flieMath").toString());
		intent.putExtra("id", hashMap.get("id").toString());
		intent.putExtra("jtype", hashMap.get("jtype").toString());
		intent.putExtra("mailId", hashMap.get("mailId").toString());
		intent.putExtra("title", hashMap.get("title").toString());*/
		
		Intent intent = getIntent();
		String dataTime = intent.getExtras().getString("datatime");
		String fileMath = intent.getExtras().getString("fileMath");
		String title = intent.getExtras().getString("title");
		mNotepadTime.setText(dataTime);
		mNotepadName.setText(title);
		
		
		setWebContent(fileMath);
		
	}


	private void setWebContent(String mailContent) {
		mNotepadContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mNotepadContent.loadUrl("file://"+mailContent);
		//mNotepadContent.loadDataWithBaseURL(null, "file:///"+mailContent, "text/html", "utf-8", null);
		//设置缩放
		mNotepadContent.getSettings().setBuiltInZoomControls(true);
        //网页适配
        DisplayMetrics dm = getResources().getDisplayMetrics();
        //不显示缩放按钮
        mNotepadContent.getSettings().setDisplayZoomControls(false);
        int scale = dm.densityDpi;
        if (scale == 240) {
        	mNotepadContent.getSettings().setDefaultZoom(ZoomDensity.FAR);
        } else if (scale == 160) {
        	mNotepadContent.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
        } else {
        	mNotepadContent.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
        }
        mNotepadContent.setWebChromeClient(new WebChromeClient());
	}


	private void initViews() {
		mNotepadTime = (TextView) findViewById(R.id.notepad_time);
		mNotepadName = (TextView) findViewById(R.id.notepad_name);
		mNotepadContent = (WebView) findViewById(R.id.notepad_content);
	}
}
