package com.example.emailtest.email;

import java.util.List;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Session;
import javax.mail.Store;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailtest.R;
import com.example.emailtest.db.UserService;
import com.example.emailtest.entity.User;
import com.example.emailtest.mail.util.Stack;

public class LoginActivity extends Activity implements OnClickListener{
	
	TextView tv_name;
	EditText ed_user,ed_pwd;
	Button image_quexiao,image_xiayibu;
	private ProgressDialog dialog;
	private String type;
	private UserService userService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Stack.addActivity(this);
		userService = new UserService(this);
		ed_user =  (EditText) this.findViewById(R.id.ed_user);
		ed_pwd =  (EditText) this.findViewById(R.id.ed_pwd);
		image_quexiao =  (Button) this.findViewById(R.id.image_quexiao);
		image_xiayibu =  (Button) this.findViewById(R.id.image_xiayibu);
		tv_name =  (TextView) this.findViewById(R.id.tv_name);
		image_xiayibu.setOnClickListener(this);image_quexiao.setOnClickListener(this);
		type = getIntent().getStringExtra("type");
		if("qq".equals(type)){
			tv_name.setText("qq邮箱");
			ed_user.setHint("example@qq.com");
		}else if("qtecent".equals(type)){
			tv_name.setText("腾讯企业邮箱");
			ed_user.setHint("example@company.com");
		}else if("Exchange".equals(type)){
			tv_name.setText("Exchange邮箱");
			ed_user.setHint("example@company.com");
		}else if("126mail".equals(type)){
			tv_name.setText("126邮箱");
			ed_user.setHint("example@126.com");
		}else if("163mail".equals(type)){
			tv_name.setText("163邮箱");
			ed_user.setHint("example@163.com");
		}else if("Gmail".equals(type)){
			tv_name.setText("Gmail邮箱");
			ed_user.setHint("example@gmail.com");
		}else if("Outlook".equals(type)){
			tv_name.setText("Outlook邮箱");
			ed_user.setHint("example@outlook.com");
		}else if("other".equals(type)){
			tv_name.setText("其他邮箱");
			ed_user.setHint("example@company.com");
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.image_xiayibu:
			loginEmail();
		break;
		case R.id.image_quexiao:
			finish();
		break;
		
		}
	}
	
	/**
	 * 登入邮箱
	 */
	private void loginEmail(){
		String address=ed_user.getText().toString().trim();
		String pwd=ed_pwd.getText().toString().trim();
		if(TextUtils.isEmpty(address)){
			Toast.makeText(LoginActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else{
			if(TextUtils.isEmpty(pwd)){
				Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		if(!userService.hasUser(address)){
			new LoginTask().execute(address,pwd);
		}else{
			Toast.makeText(this, "你已经登录此邮箱", Toast.LENGTH_SHORT).show();
		}
		
		
		
	}
	
	class LoginTask extends AsyncTask<String, Void, String>{
		
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog=new ProgressDialog(LoginActivity.this);
				dialog.setMessage("正在登入，请稍后");
				dialog.show();
			}
	
			@Override
			protected String doInBackground(String... arg0) {
				String[] s = arg0[0].split("@");
				String host = "pop." + s[1];
				try {
					Properties prop = new Properties();
					Store store;
					Session session;
					if(host.equals("pop.qq.com")){
						prop.setProperty("mail.store.protocol", "pop3s");
						prop.setProperty("mail.pop3s.host", host);
						session = Session.getInstance(prop); 
						store = session.getStore("pop3s");
					}else{
						prop.setProperty("mail.store.protocol", "pop3");
						prop.setProperty("mail.pop3.host", host);
						session = Session.getInstance(prop); 
						store = session.getStore("pop3");
					}
					store.connect(arg0[0], arg0[1]);
					
					
					List<User> list = userService.findUserAll();
					if(list.size()==0){
						User user = new User();
						user.setUsername(arg0[0]);
						user.setPwd(arg0[1]);
						user.setIsmain("1");
						userService.addUser(user);	
					}else{
						User user = new User();
						user.setUsername(arg0[0]);
						user.setPwd(arg0[1]);
						user.setIsmain("0");
						userService.addUser(user);	
					}
					
					store.close();
					return "0";
				}catch(AuthenticationFailedException e){
					return "1";
				}catch (Exception e) {
					return "2";
				}
				
			}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				dialog.cancel();
				if("0".equals(result)){
					Intent intent=new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
				}else if("1".equals(result)){
					Toast.makeText(LoginActivity.this, "请您先开通授权码，确保输入正确。", Toast.LENGTH_SHORT).show();
				}else if("2".equals(result)){
					Toast.makeText(LoginActivity.this, "未知异常，请联系客服。", Toast.LENGTH_SHORT).show();
				}
				
			}
			
			
			
		}
}
