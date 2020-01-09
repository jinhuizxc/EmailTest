package com.example.emailtest.ui.activity;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.emailtest.R;
import com.example.emailtest.adapter.UserListTwoAdapter;
import com.example.emailtest.db.UserService;
import com.example.emailtest.ui.activity.login.AddAccountActivity;
import com.example.emailtest.entity.User;
import com.example.emailtest.utils.Stack;
import com.example.emailtest.view.NoScrollListView;

public class SettingActivity extends Activity implements OnClickListener{
	
	private UserService userService;
	private List<User> list = new ArrayList<User>();
	NoScrollListView mListView;
	UserListTwoAdapter twoAdapter=null;
	
	RelativeLayout re_tianjia,re_rili,re_tixing,re_qingchu,re_guanyu,re_guanbi;
	TextView tv_huancun;
	Button image_quexiao;
	ToggleButton btn_touxiang,btn_yinxiao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		Stack.addActivity(this);
		re_tianjia =  (RelativeLayout) this.findViewById(R.id.re_tianjia);
		re_rili =  (RelativeLayout) this.findViewById(R.id.re_rili);
		re_tixing =  (RelativeLayout) this.findViewById(R.id.re_tixing);
		re_qingchu =  (RelativeLayout) this.findViewById(R.id.re_qingchu);
		re_guanyu =  (RelativeLayout) this.findViewById(R.id.re_guanyu);
		re_guanbi =  (RelativeLayout) this.findViewById(R.id.re_guanbi);
		tv_huancun =  (TextView) this.findViewById(R.id.tv_huancun);
		image_quexiao  =  (Button) this.findViewById(R.id.image_quexiao);
		btn_touxiang  =  (ToggleButton) this.findViewById(R.id.btn_touxiang);
		btn_yinxiao  =  (ToggleButton) this.findViewById(R.id.btn_yinxiao);
		
		re_tianjia.setOnClickListener(this);re_rili.setOnClickListener(this);
		re_tixing.setOnClickListener(this);re_qingchu.setOnClickListener(this);
		re_guanyu.setOnClickListener(this);image_quexiao.setOnClickListener(this);
		re_guanbi.setOnClickListener(this);
		
		//mListView  = (NoScrollListView) this.findViewById(R.id.user_list_two);
		
		userService = new UserService(this);
		mListView = (NoScrollListView) findViewById(R.id.user_list_two);
		list = userService.findUserAll();
		twoAdapter = new UserListTwoAdapter(this, list);
		mListView.setAdapter(twoAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this, OneActivity.class);
				intent.putExtra("username", list.get(position).getUsername());
				startActivityForResult(intent, 0);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_tianjia:
			startActivity(new Intent(SettingActivity.this, AddAccountActivity.class));
		break;
		case R.id.re_rili:
			
		break;
		case R.id.re_tixing:
			
		break;
		case R.id.re_qingchu:
			
		break;
		case R.id.re_guanyu:
		break;
		case R.id.image_quexiao:
			SettingActivity.this.finish();
		break;
		case R.id.re_guanbi:
			finish();
		break;
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0 && resultCode == Activity.RESULT_OK){
			list.clear();
			list = userService.findUserAll();
			twoAdapter = new UserListTwoAdapter(this, list);
			mListView.setAdapter(twoAdapter);
			twoAdapter.notifyDataSetChanged();
		}
	}
	
	
}
