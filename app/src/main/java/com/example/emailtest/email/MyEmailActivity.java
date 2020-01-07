package com.example.emailtest.email;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.emailtest.R;
import com.example.emailtest.adapter.MailListAdapter;
import com.example.emailtest.db.MailService;
import com.example.emailtest.db.UserService;
import com.example.emailtest.entity.MailBrief;
import com.example.emailtest.entity.User;
import com.example.emailtest.mail.util.Stack;


public class MyEmailActivity extends Activity implements OnClickListener{
	
	private ListView mailListView;
	private List<MailBrief> list = new ArrayList<MailBrief>();
	private MailListAdapter mailListAdapter;
	
	private String userId ,title;
	private User user;
	private MailService mailService ;
	private UserService userService ;
	
	ImageView image_back,image_edit;TextView tv_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me2);
		Stack.addActivity(this);
		
		userId =  getIntent().getStringExtra("userid");
		title =  getIntent().getStringExtra("title");
		
		image_back  = (ImageView) this.findViewById(R.id.image_back);
		image_edit  = (ImageView) this.findViewById(R.id.image_edit);
		tv_title  = (TextView) this.findViewById(R.id.tv_title);
		
		tv_title.setText(getIntent().getStringExtra("title"));
		
		image_edit.setOnClickListener(this);image_back.setOnClickListener(this);
		
		userService = new UserService(this);
		mailService = new MailService(this);
		
		if("收件箱".equals(title)){
			list = mailService.findMailByMailType("7",userId);
		}else if("星标邮件".equals(title)){
			list = mailService.findMailByMailType("2", userId);
		}else if("草稿箱".equals(title)){
			list = mailService.findMailByMailType("3", userId);
		}else if("已发送".equals(title)){
			list = mailService.findMailByMailType("4", userId);
		}else if("已删除".equals(title)){
			list = mailService.findMailByMailType("5",userId);
		}
		
		user = userService.findByUserId(userId);
		mailListView = (ListView) this.findViewById(R.id.lstv);
		mailListAdapter = new MailListAdapter(this, list, user);
		mailListView.setAdapter(mailListAdapter);
		mailListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.image_edit:
			
		break;
		case R.id.image_back:
			finish();
		break;
		}
		
	}
	
}
