package com.example.emailtest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.emailtest.R;
import com.example.emailtest.utils.Stack;
import com.example.emailtest.email.clander.CalendarActivity;


public class MoreActivity extends Activity implements OnClickListener {

	RelativeLayout re_rili, re_wenjian;
	ImageView image_quexiao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		Stack.addActivity(this);
		image_quexiao = (ImageView) this.findViewById(R.id.image_quexiao);
		re_rili = (RelativeLayout) this.findViewById(R.id.re_rili);
		re_wenjian = (RelativeLayout) this.findViewById(R.id.re_wenjian);
		image_quexiao.setOnClickListener(this);
		re_rili.setOnClickListener(this);
		re_wenjian.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.re_rili:
			startActivity(new Intent(MoreActivity.this, CalendarActivity.class));
			break;
		case R.id.re_wenjian:
			startActivity(new Intent(MoreActivity.this, WenJianActivity.class));
			break;
		case R.id.image_quexiao:
			finish();
			break;

		}
	}

}
