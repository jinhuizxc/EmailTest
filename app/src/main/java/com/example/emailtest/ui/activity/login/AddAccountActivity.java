package com.example.emailtest.ui.activity.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.emailtest.R;
import com.example.emailtest.utils.Stack;

public class AddAccountActivity extends Activity implements OnClickListener {

	LinearLayout re_exchange, re_163, re_126, re_gemail, re_out, re_qita;
	RelativeLayout re_qq, re_tenxun;
	Button image_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_all);

		Stack.addActivity(this);

		initViews();


	}

	private void initViews() {
		re_tenxun = (RelativeLayout) this.findViewById(R.id.re_tenxun);
		re_qq = (RelativeLayout) this.findViewById(R.id.re_qq);
		re_exchange = (LinearLayout) this.findViewById(R.id.re_exchange);
		re_163 = (LinearLayout) this.findViewById(R.id.re_163);
		re_126 = (LinearLayout) this.findViewById(R.id.re_126);
		re_gemail = (LinearLayout) this.findViewById(R.id.re_gemail);
		re_out = (LinearLayout) this.findViewById(R.id.re_out);
		re_qita = (LinearLayout) this.findViewById(R.id.re_qita);

		image_back = (Button) this.findViewById(R.id.image_back);

		re_tenxun.setOnClickListener(this);
		re_qq.setOnClickListener(this);
		re_exchange.setOnClickListener(this);
		re_163.setOnClickListener(this);
		re_126.setOnClickListener(this);
		re_gemail.setOnClickListener(this);
		re_out.setOnClickListener(this);
		re_qita.setOnClickListener(this);
		image_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(AddAccountActivity.this, EmailLoginActivity.class);
		switch (v.getId()) {
		case R.id.re_qq:
			intent.putExtra("type", "qq");
			startActivity(intent);
			break;
		case R.id.re_tenxun:
			intent.putExtra("type", "qtecent");
			startActivity(intent);
			break;
		case R.id.re_exchange:
			intent.putExtra("type", "Exchange");
			startActivity(intent);
			break;
		case R.id.re_163:
			intent.putExtra("type", "163mail");
			startActivity(intent);
			break;
		case R.id.re_126:
			intent.putExtra("type", "126mail");
			startActivity(intent);
			break;
		case R.id.re_gemail:
			intent.putExtra("type", "Gmail");
			startActivity(intent);
			break;
		case R.id.re_out:
			intent.putExtra("type", "Outlook");
			startActivity(intent);
			break;
		case R.id.re_qita:
			intent.putExtra("type", "other");
			startActivity(intent);
			break;
		case R.id.image_back:
			finish();
			break;
		}
		this.finish();
	}

}
