package com.example.emailtest.ui.activity;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.emailtest.R;
import com.example.emailtest.db.UserService;
import com.example.emailtest.entity.User;
import com.example.emailtest.utils.Stack;

public class OneActivity extends Activity implements OnClickListener{
	
	private User user = new User();
	private UserService userService;
	TextView tv_main,tv_dele,tv_name,tv_nicheng;
	Button btn_back;
	
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one);
		Stack.addActivity(this);
		userService = new UserService(this);
		username = getIntent().getStringExtra("username");
		tv_nicheng =  (TextView) this.findViewById(R.id.tv_nicheng);
		tv_main =  (TextView) this.findViewById(R.id.tv_main);
		tv_name =  (TextView) this.findViewById(R.id.tv_name);
		tv_dele =  (TextView) this.findViewById(R.id.tv_dele);
		btn_back  =  (Button) this.findViewById(R.id.btn_back);
		
		tv_main.setOnClickListener(this);tv_dele.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		
		int str = username.indexOf("@");
		String str2 = username.substring(0, str);
		tv_nicheng.setText(str2);
		tv_name.setText(username);
		
		
		user= userService.findByUsername(username);
		
		if("1".equals(user.getIsmain())){
			tv_main.setText("已是主账号");
			tv_main.setTextColor(Color.parseColor("#e2e2e2"));
		}else{
			tv_main.setTextColor(Color.parseColor("#166dae"));
			tv_main.setText("设为主账户");
		}
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_main:
			if("0".equals(user.getIsmain())){
				userService.update2();
				userService.update(username);
				Intent intent = OneActivity.this.getIntent();
				OneActivity.this.setResult(Activity.RESULT_OK, intent);
				finish();
			}
		break;
		case R.id.tv_dele:
			userService.deleteUser(username);
			Intent intent = OneActivity.this.getIntent();
			OneActivity.this.setResult(Activity.RESULT_OK, intent);
			finish();
		break;
		case R.id.btn_back:
			finish();
		break;
		}
		
	}
	
	
}
