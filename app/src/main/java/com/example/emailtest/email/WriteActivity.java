package com.example.emailtest.email;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailtest.R;
import com.example.emailtest.db.CommunictionService;
import com.example.emailtest.db.MailService;
import com.example.emailtest.db.UserService;
import com.example.emailtest.entity.Communication;
import com.example.emailtest.entity.MailBrief;
import com.example.emailtest.entity.User;
import com.example.emailtest.mail.util.Attachment;
import com.example.emailtest.mail.util.GridViewAdapter;
import com.example.emailtest.mail.util.MyGridViewBut;
import com.example.emailtest.mail.util.SendMail;
import com.example.emailtest.mail.util.Stack;


public class WriteActivity extends Activity implements OnClickListener{
	
	TextView tv_write,tv_state,tv_number,tv_zhuzhanghao;
	ImageView image_add,image_phone,image_pic,image_file,image_number;
	EditText ed_com,ed_cm,ed_shoujianren,ed_zhuti,ed_misong;
	LinearLayout ll_caozuo,ll_misong;
	
	Button image_quexiao,image_send;
	
	private MailBrief mail;
	private User user;
	private UserService userService;
	private MailService mailService ;
	private CommunictionService comService ;
	
	MyGridViewBut noScrollgridview;
	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	MyAdapter adapt;
	String path2="";int reqCode = 100;
	Boolean index = true;

	private MyGridViewBut gridView;
	private GridViewAdapter<Attachment> adapter = null;
	private ArrayList<String> lists = new ArrayList<String>(); 
	HashMap<String, Object> hashMap = new HashMap<String, Object>(); 
	private ProgressDialog dialog;
	private static final int notifyId = 100;
	private static NotificationCompat.Builder mBuilder;
	private static NotificationManager notificationManager;

	@SuppressLint("HandlerLeak")
	public static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {   
            case 1:
            	Toast.makeText(MyApp.sContext, "发送成功", Toast.LENGTH_SHORT).show();
            	mBuilder.setContentTitle("邮件发送成功")
				.setContentText("邮件发送成功")
//				.setNumber(number)//显示数量
				.setTicker("邮件发送成功");//通知首次出现在通知栏，带上升动画效果的
				notificationManager.notify(notifyId, mBuilder.build());
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						notificationManager.cancel(notifyId);
					}
				}, 5000);
            	break;   
            case 0:
            	Toast.makeText(MyApp.sContext, "邮件发送失败", Toast.LENGTH_SHORT).show();
				mBuilder.setContentTitle("邮件发送失败")
				.setContentText("邮件发送失败")
//				.setNumber(number)//显示数量
				.setTicker("邮件发送失败");//通知首次出现在通知栏，带上升动画效果的
				notificationManager.notify(notifyId, mBuilder.build());
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						notificationManager.cancel(notifyId);
					}
				}, 5000);
            	break;
			}
       }   
	};
	
	
	private int btn1 = R.drawable.j5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		Stack.addActivity(this);
		comService = new CommunictionService(this);
		mailService = new MailService(this);
		userService = new UserService(this);
		
		ll_caozuo =  (LinearLayout) this.findViewById(R.id.ll_caozuo);
		ll_misong =  (LinearLayout) this.findViewById(R.id.ll_misong);
		
		ed_cm =  (EditText) this.findViewById(R.id.ed_cm);
		ed_com =  (EditText) this.findViewById(R.id.ed_com);
		ed_misong =  (EditText) this.findViewById(R.id.ed_misong);
		ed_shoujianren =  (EditText) this.findViewById(R.id.ed_shoujianren);
		ed_zhuti =  (EditText) this.findViewById(R.id.ed_zhuti);
		
		tv_zhuzhanghao =  (TextView) this.findViewById(R.id.tv_zhuzhanghao);
		tv_write =  (TextView) this.findViewById(R.id.tv_write);
		tv_state =  (TextView) this.findViewById(R.id.tv_state);
		tv_number =  (TextView) this.findViewById(R.id.tv_number);
		
		image_quexiao  =  (Button) this.findViewById(R.id.image_quexiao);
		image_send  =  (Button) this.findViewById(R.id.image_send);
		image_add  =  (ImageView) this.findViewById(R.id.image_add);
		image_number  =  (ImageView) this.findViewById(R.id.image_number);
		image_phone  =  (ImageView) this.findViewById(R.id.image_phone);
		image_pic  =  (ImageView) this.findViewById(R.id.image_pic);
		image_file  =  (ImageView) this.findViewById(R.id.image_file);
		
		tv_write.setOnClickListener(this);image_quexiao.setOnClickListener(this);
		image_send.setOnClickListener(this);image_add.setOnClickListener(this);
		image_phone.setOnClickListener(this);image_pic.setOnClickListener(this);
		image_file.setOnClickListener(this);image_number.setOnClickListener(this);
		ed_cm.setOnClickListener(this);ed_shoujianren.setOnClickListener(this);
		
		ed_cm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
 			@Override
 			public void onFocusChange(View v, boolean hasFocus) {
 				if (hasFocus) {
 					tv_state.setText("抄送： ");
 					ll_misong.setVisibility(View.VISIBLE);
 				}
 			}
 		});
		
		noScrollgridview  = (MyGridViewBut) this.findViewById(R.id.noScrollgridview);
		adapt = new MyAdapter(this);
		for(int i = 1; i < 4; i++){
        	HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name","16545"+i);
        	list.add(map);
        }
		
		adapt.setDatas(list);
		noScrollgridview.setAdapter(adapt);
		
		gridView=(MyGridViewBut) findViewById(R.id.pre_view);
		adapter = new GridViewAdapter<Attachment>(this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new MyOnItemClickListener());
		
		user = (User) getIntent().getSerializableExtra("user");
		if(user==null){
			user= userService.findByUsername2();
		}
		
		if(getIntent().getStringExtra("mailfrom")!=null){
			ed_shoujianren.setText(getIntent().getStringExtra("mailfrom"));
		}
		
		if(getIntent().getStringExtra("id")!=null){
			mail= mailService.findById(getIntent().getStringExtra("id"));
			if(getIntent().getStringExtra("zhuanfa")!=null){
				
				ll_misong.setVisibility(View.VISIBLE);
				ed_cm.setText(mail.getBCC());
				ed_misong.setText(mail.getCC());
				tv_state.setText("抄送： ");
				ed_zhuti.setText(mail.getSubject());
				if(mail.getFlieMath()!=null){
					ll_caozuo.setVisibility(View.VISIBLE);
					gridView.setVisibility(View.VISIBLE);
					String[] file = mail.getFlieMath().split(",");
					for(int i=0;i<file.length;i++){
						lists.add(file[i]);
						tv_number.setText(file.length+"");
						image_number.setBackgroundResource(btn1);
						Attachment affInfos = Attachment.GetFileInfo(file[i]);
						adapter.appendToList(affInfos);
						int a = adapter.getList().size();
						int count = (int) Math.ceil(a / 4.0);
						gridView.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								(int) (94 * 1.5 * count)));
					}
				}
				
			}else{
				ll_misong.setVisibility(View.VISIBLE);
				ed_cm.setText(mail.getBCC());
				ed_misong.setText(mail.getCC());
				tv_state.setText("抄送： ");
			}
		}
		 
		tv_zhuzhanghao.setText(user.getUsername());
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_write:
			
		break;
		case R.id.ed_cm:
			tv_state.setText("抄送： ");
			ll_misong.setVisibility(View.VISIBLE);
		break;
		case R.id.image_quexiao:
			
			String shoujianren = ed_shoujianren.getText().toString();
			String zhuti = ed_zhuti.getText().toString();
			String neirong = ed_com.getText().toString();
			
			if(shoujianren.length()>0 && zhuti.length()>0 && neirong.length()>0){
				
				LayoutInflater inflater = WriteActivity.this.getLayoutInflater();
				final View layout = inflater.inflate(R.layout.select_6,(ViewGroup)WriteActivity.this.findViewById(R.id.selectLayout));
				final Dialog dialog = new Dialog(WriteActivity.this, R.style.dialog);
				Window win = dialog.getWindow();
				win.getDecorView().setPadding(0, 0, 0, 0);
				WindowManager.LayoutParams lp = win.getAttributes();
				lp.width =getWindowsWidth(WriteActivity.this) - 100;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				win.setAttributes(lp);
				dialog.setContentView(layout);
				dialog.show();
				TextView tv_no = (TextView) layout.findViewById(R.id.tv_no);
				TextView tv_yes = (TextView) layout.findViewById(R.id.tv_yes);
				
				tv_no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});
				tv_yes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
						StringBuffer stringBuffer = new StringBuffer();
						for(int i=0;i<lists.size();i++){
							stringBuffer.append(lists.get(i)+",");
						}
						String str = stringBuffer.toString();
						String filePath = str.substring(0, str.length()-1);
						
						MailBrief mailBrief = new MailBrief();
						mailBrief.setFromPersonal(user.getUsername());
						mailBrief.setSubject(ed_zhuti.getText().toString());
						mailBrief.setMailContent(ed_com.getText().toString());
//						mailBrief.setMalieId(malieId);
						mailBrief.setFlieMath(filePath);
						mailBrief.setSendTime(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
						mailBrief.setMailType("3");
						mailBrief.setUserId(user.getId()+"");
//						mailBrief.setMindex(mindex);
						mailBrief.setTOO(ed_shoujianren.getText().toString());
						mailBrief.setCC(ed_cm.getText().toString());
						mailBrief.setBCC(ed_misong.getText().toString());
						mailService.addMail(mailBrief);
						
						finish();
					}
				});
				
				
			}else{
				finish();
			}
			
		break;
		case R.id.image_send:
			
			String shoujianren2 = ed_shoujianren.getText().toString();
			String zhuti2 = ed_zhuti.getText().toString();
			String neirong2 = ed_com.getText().toString();
			if(shoujianren2.length()>0 && zhuti2.length()>0 && neirong2.length()>0){
				
				notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mBuilder = new NotificationCompat.Builder(this);
				mBuilder.setContentTitle("正在发送邮件")
				.setContentText("正在发送邮件")
				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
				.setTicker("正在发送邮件")//通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				.setSmallIcon(R.drawable.ic_launcher);
				notificationManager.notify(notifyId, mBuilder.build());
				   new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							SendMail send = SendMail.getInstance();
							
							String to[] = ed_shoujianren.getText().toString().split(",");
							String cs[] = ed_cm.getText().toString().split(",");
							String ms[] = ed_misong.getText().toString().split(",");
							
							String username = user.getUsername();
							String host="smtp."+username.substring(username.lastIndexOf("@")+1);
							
							String pwd = user.getPwd();
							String subject = ed_zhuti.getText().toString();
							String content = ed_com.getText().toString();
							String formEmail =username;
							String[] arrArchievList = null;
							if(lists.size()>0){
								arrArchievList = new String[lists.size()];
								for(int i=0;i<lists.size();i++){
									arrArchievList[i] = lists.get(i);
								}
							}
							send.send(to, cs, ms, subject, content, formEmail, arrArchievList,pwd,host);
						}
					}).start();
				   
				   
				   if(ed_shoujianren.getText().toString().length()>0){
					   String to[] = ed_shoujianren.getText().toString().split(",");
					   for(int i=0 ; i<to.length;i++ ){
						   if(!comService.hasCom(to[i])){
							   Communication com = new Communication();
							   com.setNumbers(to[i]);
							   comService.addCommunication(com);
						   }
					   }
				   }
				   if(ed_cm.getText().toString().length()>0){
					   String cs[] = ed_cm.getText().toString().split(",");
					   for(int i=0 ; i<cs.length;i++ ){
						   if(!comService.hasCom(cs[i])){
							   Communication com = new Communication();
							   com.setNumbers(cs[i]);
							   comService.addCommunication(com);
						   }
						   
					   }
				   }
				   if(ed_misong.getText().toString().length()>0){
					   String ms[] = ed_misong.getText().toString().split(",");
					   for(int i=0 ; i<ms.length;i++ ){
						   if(!comService.hasCom(ms[i])){
							   Communication com = new Communication();
							   com.setNumbers(ms[i]);
							   comService.addCommunication(com);
						   }
					   }
				   }
				   
				   StringBuffer stringBuffer = new StringBuffer();
					for(int i=0;i<lists.size();i++){
						stringBuffer.append(lists.get(i)+",");
					}
					String str = stringBuffer.toString();
					String filePath = str.substring(0, str.length()-1);
					MailBrief mailBrief = new MailBrief();
					mailBrief.setFromPersonal(user.getUsername());
					mailBrief.setSubject(ed_zhuti.getText().toString());
					mailBrief.setMailContent(ed_com.getText().toString());
//					mailBrief.setMalieId(malieId);
					mailBrief.setFlieMath(filePath);
					mailBrief.setSendTime(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
					mailBrief.setMailType("4");
					mailBrief.setUserId(user.getId()+"");
//					mailBrief.setMindex(mindex);
					mailBrief.setTOO(ed_shoujianren.getText().toString());
					mailBrief.setCC(ed_cm.getText().toString());
					mailBrief.setBCC(ed_misong.getText().toString());
					mailService.addMail(mailBrief);
				   
					
					
					
					finish();
					
			}else{
				Toast.makeText(WriteActivity.this, "请填写完整的内容", Toast.LENGTH_SHORT).show();
			}
			
			
			
		break;
		case R.id.image_add:
			
		break;
		case R.id.image_phone:
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
				path2 = getFileName();
				getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path2)));  
				startActivityForResult(getImageByCamera, 0);
			} else {
				Toast.makeText(WriteActivity.this, "请插入SD卡",Toast.LENGTH_LONG).show();
			}
		break;
		case R.id.image_pic:
			Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(getImage, 1);
		break;
		case R.id.image_file:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("file/");
			startActivityForResult(intent, 2);
			
		break;
		case R.id.image_number:
			if(index){
				ll_caozuo.setVisibility(View.VISIBLE);
				gridView.setVisibility(View.VISIBLE);
				
				index = false;
			}else{
				ll_caozuo.setVisibility(View.GONE);
				gridView.setVisibility(View.GONE);
				index = true;
			}
		break;
		
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data); 
		if (requestCode ==0) {//拍照直接上传
			
			lists.add(path2);
			tv_number.setText(lists.size()+"");
			image_number.setBackgroundResource(btn1);
			
			Attachment affInfos = Attachment.GetFileInfo(path2);
			adapter.appendToList(affInfos);
			int a = adapter.getList().size();
			int count = (int) Math.ceil(a / 4.0);
			gridView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					(int) (94 * 1.5 * count)));
		}else if(requestCode ==1){
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				path2 = cursor.getString(columnIndex);
				
				lists.add(path2);
				tv_number.setText(lists.size()+"");
				image_number.setBackgroundResource(btn1);
				
				cursor.close();
				Attachment affInfos = Attachment.GetFileInfo(path2);
				adapter.appendToList(affInfos);
				int a = adapter.getList().size();
				int count = (int) Math.ceil(a / 4.0);
				gridView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						(int) (94 * 1.5 * count)));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}else if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 2:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
				}
				String path = uri.getPath();
				
				lists.add(path);
				tv_number.setText(lists.size()+"");
				image_number.setBackgroundResource(btn1);
				Attachment affInfos = Attachment.GetFileInfo(path);
				adapter.appendToList(affInfos);
				int a = adapter.getList().size();
				int count = (int) Math.ceil(a / 4.0);
				gridView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						(int) (94 * 1.5 * count)));
				break;
			}
		}
	}

	private String getFileName() {  
        String saveDir = Environment.getExternalStorageDirectory() + "/bank";  
        File dir = new File(saveDir);  
        if (!dir.exists()) {  
            dir.mkdir(); // 创建文件夹  
        }  
        //用日期作为文件名，确保唯一性  
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");  
        String fileName = saveDir + "/" + formatter.format(date) + ".PNG";  
        return fileName;  
    }  
	
	private class MyAdapter extends BaseAdapter{
		private ArrayList<HashMap<String, Object>> list;
		private Context ctx;
		private LayoutInflater inflater;
		public MyAdapter(Context ctx){
			this.ctx = ctx;
			inflater = LayoutInflater.from(ctx);
		}
		public void setDatas(ArrayList<HashMap<String, Object>> list2){
			this.list = list2;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.item_1, null);
				vh = new ViewHolder();
				vh.tv_mail = (TextView)convertView.findViewById(R.id.tv_mail);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder)convertView.getTag();
			}
			HashMap<String, Object> map = list.get(position);
			vh.tv_mail.setText(map.get("name").toString());
			return convertView;
		}
		private class ViewHolder{
			TextView tv_mail;
		}
	}
	
	/**
	 * 点击事件
	 * @author Administrator
	 *
	 */
	private class MyOnItemClickListener implements OnItemClickListener{
		@SuppressLint("ResourceType")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				final int arg2, long arg3) {
			Attachment infos = (Attachment) adapter.getItem(arg2);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					WriteActivity.this);
			builder.setTitle(infos.getFileName());
			builder.setIcon(getResources().getColor(
					android.R.color.transparent));
			builder.setMessage("是否删除当前附件");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							adapter.clearPositionList(arg2);
							int a = adapter.getList().size();
							int count = (int) Math.ceil(a / 4.0);
							gridView.setLayoutParams(new LayoutParams(
									LayoutParams.MATCH_PARENT,
									(int) (94 * 1.5 * count)));
						}
					});
			builder.setPositiveButton("取消",null);
			builder.create().show();
		}
	}
	/**
	 * @获取默认的pendingIntent,为了防止2.3及以下版本报错
	 * @flags属性:  
	 * 在顶部常驻:Notification.FLAG_ONGOING_EVENT  
	 * 点击去除： Notification.FLAG_AUTO_CANCEL 
	 */
	public PendingIntent getDefalutIntent(int flags){
		PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
		return pendingIntent;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
			String shoujianren = ed_shoujianren.getText().toString();
			String zhuti = ed_zhuti.getText().toString();
			String neirong = ed_com.getText().toString();
			
			if(shoujianren.length()>0 && zhuti.length()>0 && neirong.length()>0){
				
				LayoutInflater inflater = WriteActivity.this.getLayoutInflater();
				final View layout = inflater.inflate(R.layout.select_6,(ViewGroup)WriteActivity.this.findViewById(R.id.selectLayout));
				final Dialog dialog = new Dialog(WriteActivity.this, R.style.dialog);
				Window win = dialog.getWindow();
				win.getDecorView().setPadding(0, 0, 0, 0);
				WindowManager.LayoutParams lp = win.getAttributes();
				lp.width =getWindowsWidth(WriteActivity.this) - 100;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				win.setAttributes(lp);
				dialog.setContentView(layout);
				dialog.show();
				TextView tv_no = (TextView) layout.findViewById(R.id.tv_no);
				TextView tv_yes = (TextView) layout.findViewById(R.id.tv_yes);
				
				tv_no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});
				tv_yes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
						StringBuffer stringBuffer = new StringBuffer();
						for(int i=0;i<lists.size();i++){
							stringBuffer.append(lists.get(i)+",");
						}
						String str = stringBuffer.toString();
						String filePath = str.substring(0, str.length()-1);
						
						MailBrief mailBrief = new MailBrief();
						mailBrief.setFromPersonal(user.getUsername());
						mailBrief.setSubject(ed_zhuti.getText().toString());
						mailBrief.setMailContent(ed_com.getText().toString());
//						mailBrief.setMalieId(malieId);
						mailBrief.setFlieMath(filePath);
						mailBrief.setSendTime(new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
						mailBrief.setMailType("3");
						mailBrief.setUserId(user.getId()+"");
//						mailBrief.setMindex(mindex);
						mailBrief.setTOO(ed_shoujianren.getText().toString());
						mailBrief.setCC(ed_cm.getText().toString());
						mailBrief.setBCC(ed_misong.getText().toString());
						mailService.addMail(mailBrief);
						
						finish();
					}
				});
				
				
			}else{
				finish();
			}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	/** 获取屏幕的宽度 */
	public int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	
}
