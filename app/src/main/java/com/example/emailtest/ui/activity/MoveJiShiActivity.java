package com.example.emailtest.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.emailtest.R;
import com.example.emailtest.db.NotepadService;
import com.example.emailtest.utils.Stack;


public class MoveJiShiActivity extends Activity implements OnClickListener{
	
	
	Button image_quexiao,image_send;
	
	RelativeLayout re_no;
	RelativeLayout re_fa ;
	RelativeLayout re_five ;
	
	ImageView image_no;
	ImageView image_fa;
	ImageView image_five;
	
	private NotepadService note;
	private String tag = "2";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jishiben_move);
		Stack.addActivity(this);
		
		note = new NotepadService(this);
		
		image_quexiao  =  (Button) this.findViewById(R.id.image_quexiao);
		image_send  =  (Button) this.findViewById(R.id.image_send);
		
		re_no = (RelativeLayout) this.findViewById(R.id.re_no);
		re_fa = (RelativeLayout) this.findViewById(R.id.re_fa);
		re_five = (RelativeLayout) this.findViewById(R.id.re_five);
		
		image_no = (ImageView) this.findViewById(R.id.image_no);
		image_fa = (ImageView) this.findViewById(R.id.image_fa);
		image_five = (ImageView) this.findViewById(R.id.image_five);
		
		image_quexiao.setOnClickListener(this);image_send.setOnClickListener(this);
		
		re_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				image_no.setVisibility(View.VISIBLE);
				image_fa.setVisibility(View.GONE);
				image_five.setVisibility(View.GONE);
				re_no.setBackgroundColor(Color.parseColor("#ecf6ff"));
				re_fa.setBackgroundColor(Color.parseColor("#ffffff"));
				re_five.setBackgroundColor(Color.parseColor("#ffffff"));
				tag="2";
			}
		});
		re_fa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				image_no.setVisibility(View.GONE);
				image_fa.setVisibility(View.VISIBLE);
				image_five.setVisibility(View.GONE);
				re_no.setBackgroundColor(Color.parseColor("#ffffff"));
				re_fa.setBackgroundColor(Color.parseColor("#ecf6ff"));
				re_five.setBackgroundColor(Color.parseColor("#ffffff"));
				tag="3";
			}
		});
		re_five.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				image_no.setVisibility(View.GONE);
				image_fa.setVisibility(View.GONE);
				image_five.setVisibility(View.VISIBLE);
				re_no.setBackgroundColor(Color.parseColor("#ffffff"));
				re_fa.setBackgroundColor(Color.parseColor("#ffffff"));
				re_five.setBackgroundColor(Color.parseColor("#ecf6ff"));
				tag="4";
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_quexiao:
			finish();
		break;
		case R.id.image_send:
			note.update(getIntent().getStringExtra("id"), tag);
			finish();
		break;
		}
		
	}
	
	
}
