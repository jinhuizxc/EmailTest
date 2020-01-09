package com.example.emailtest.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.emailtest.R;
import com.example.emailtest.ui.activity.write.WriteEmailActivity;
import com.example.emailtest.entity.User;
import com.example.emailtest.utils.Stack;


public class MeActivity extends Activity implements OnClickListener{
	
	
	RelativeLayout re_shoujian,re_caogaoxiang,re_yifasong,re_yishanchu,re_xingbiao;
	TextView tv_name;ImageView image_back,image_edit;

	private User user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me);
		Stack.addActivity(this);
		user = (User) getIntent().getSerializableExtra("user");
		
		re_shoujian =  (RelativeLayout) this.findViewById(R.id.re_shoujian);
		re_caogaoxiang =  (RelativeLayout) this.findViewById(R.id.re_caogaoxiang);
		re_yifasong =  (RelativeLayout) this.findViewById(R.id.re_yifasong);
		re_yishanchu =  (RelativeLayout) this.findViewById(R.id.re_yishanchu);
		re_xingbiao =  (RelativeLayout) this.findViewById(R.id.re_xingbiao);
		
		tv_name =  (TextView) this.findViewById(R.id.tv_name);
		
		image_back  =  (ImageView) this.findViewById(R.id.image_back);
		image_edit  =  (ImageView) this.findViewById(R.id.image_edit);
		
		re_shoujian.setOnClickListener(this);re_xingbiao.setOnClickListener(this);
		re_caogaoxiang.setOnClickListener(this);re_yifasong.setOnClickListener(this);
		re_yishanchu.setOnClickListener(this);
		image_back.setOnClickListener(this);image_edit.setOnClickListener(this);
		
		tv_name.setText(user.getUsername());
		
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(MeActivity.this, MyEmailActivity.class);
		intent.putExtra("userid", user.getId()+"");
		switch (v.getId()) {
		case R.id.re_shoujian:
			intent.putExtra("title", "收件箱");
			startActivity(intent);
		break;
		case R.id.re_xingbiao:
			intent.putExtra("title", "星标邮件");
			startActivity(intent);
		break;
		case R.id.re_caogaoxiang:
			intent.putExtra("title", "草稿箱");
			startActivity(intent);
		break;
		case R.id.re_yifasong:
			intent.putExtra("title", "已发送");
			startActivity(intent);
		break;
		case R.id.re_yishanchu:
			intent.putExtra("title", "已删除");
			startActivity(intent);
		break;
		case R.id.image_back:
			finish();
		break;
		case R.id.image_edit:
			Intent intent2 = new Intent(MeActivity.this, WriteEmailActivity.class);
			intent2.putExtra("user", user);
			startActivity(intent2);
		break;
		}
		
	}
	
}
