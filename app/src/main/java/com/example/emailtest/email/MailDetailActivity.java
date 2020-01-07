package com.example.emailtest.email;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailtest.R;
import com.example.emailtest.adapter.AttachmentListAdapter;
import com.example.emailtest.db.MailService;
import com.example.emailtest.db.NotepadService;
import com.example.emailtest.entity.User;
import com.example.emailtest.mail.util.IOUtil;
import com.example.emailtest.mail.util.MailReceiver;
import com.example.emailtest.mail.util.Stack;
import com.example.emailtest.view.NoScrollListView;
import com.sun.mail.pop3.POP3Folder;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

public class MailDetailActivity extends Activity implements OnClickListener{
	private ProgressDialog dialog;
	private WebView wv_content;
	private TextView tv_content;
	private TextView mailSubject;
	private TextView mailFrom;
	private ImageView image_back,image_xingbiao,image_huifu,image_delet,image_more;
	private ListView lv_mailattachment;
	private AttachmentListAdapter adapter;
	private List<String> attachmentsList = new ArrayList<String>();
	List<InputStream> attachmentsIsList = new ArrayList<InputStream>();
	private Handler handler;
	 
	private MailService mailService ;
	private NotepadService note ;
	private User user;
	
	
	private NoScrollListView mail_add_more_lv, mail_chaosong_more_lv;
	private RelativeLayout mail_chaosong_rl, mail_from_simple, mail_add_more_rl;
	private LinearLayout mial_form_more_ll;
	private int settingMore = 1;
	private String[] tos;
	private TextView mail_from_more_name, mail_from_more_email, mail_time_more_time, setting_more;

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_detail);
		Stack.addActivity(this);
		
		setting_more = (TextView) findViewById(R.id.setting_more);
		mail_from_more_name = (TextView) findViewById(R.id.mail_from_more_name);
		mail_from_more_email = (TextView) findViewById(R.id.mail_from_more_email);
		mail_time_more_time = (TextView) findViewById(R.id.mail_time_more_time);
		mail_add_more_lv = (NoScrollListView) findViewById(R.id.mail_add_more_lv);
		mail_chaosong_more_lv = (NoScrollListView) findViewById(R.id.mail_chaosong_more_lv);
		mail_chaosong_rl = (RelativeLayout) findViewById(R.id.mail_chaosong_rl);
		mail_from_simple = (RelativeLayout) findViewById(R.id.mail_from_simple);
		mial_form_more_ll = (LinearLayout) findViewById(R.id.mial_form_more_ll);
		mail_add_more_rl = (RelativeLayout) findViewById(R.id.mail_add_more_rl);
		
		
		mailService = new MailService(this);
		note = new NotepadService(this);
		
		mailService.updateTypeById(getIntent().getStringExtra("id"), "1");
		
		
		handler = new MyHandler(this);
		image_back = (ImageView) findViewById(R.id.image_back);
		
		image_xingbiao = (ImageView) findViewById(R.id.image_xingbiao);
		image_huifu = (ImageView) findViewById(R.id.image_huifu);
		image_delet = (ImageView) findViewById(R.id.image_delet);
		image_more = (ImageView) findViewById(R.id.image_more);
		
		image_xingbiao.setOnClickListener(this);image_huifu.setOnClickListener(this);
		image_delet.setOnClickListener(this);image_more.setOnClickListener(this);
		image_back.setOnClickListener(this);
		
		lv_mailattachment = (ListView) findViewById(R.id.lv_mailattachment);
		adapter = new AttachmentListAdapter(this, attachmentsList);
		lv_mailattachment.setAdapter(adapter);
		lv_mailattachment.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				new Thread(new Runnable() {

                    @Override
                    public void run() {
                    	handler.obtainMessage(0, "开始下载\"" + attachmentsList.get(position) + "\"").sendToTarget();
                        InputStream is = attachmentsIsList.get(position);
                        String path = new IOUtil().stream2file(is, Environment.getExternalStorageDirectory().toString() + "/XingYe/" + attachmentsList.get(position));
                        if (path == null) {
                            handler.obtainMessage(0, "下载失败！").sendToTarget();
                        } else {
                            handler.obtainMessage(0, "文件保存在：" + path).sendToTarget();
                        }
                    }
                }).start();
				
			}
		});
		wv_content = (WebView) findViewById(R.id.wv_content);
		tv_content = (TextView) findViewById(R.id.tv_content);
		mailSubject = (TextView) findViewById(R.id.mail_subject);
		mailFrom = (TextView) findViewById(R.id.mail_from);
		Intent intent = getIntent();
		String  position = intent.getStringExtra("position");
		user = (User) intent.getSerializableExtra("user");
		String mailsubject = intent.getStringExtra("mailsubject");
		String mailTime = intent.getStringExtra("mailTime");
		String mailTo = intent.getStringExtra("mailTo");
		String mailCC = intent.getStringExtra("mailCC");
		String mailBCC = intent.getStringExtra("mailBCC");
		setting_more.setText("详情");

		setting_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (settingMore == 1) {
					setting_more.setText("隐藏");
					settingMore = 0;
					mail_from_simple.setVisibility(View.GONE);
					mial_form_more_ll.setVisibility(View.VISIBLE);
				} else {
					setting_more.setText("详情");
					settingMore = 1;
					mail_from_simple.setVisibility(View.VISIBLE);
					mial_form_more_ll.setVisibility(View.GONE);
				}
			}
		});
		mail_time_more_time.setText(mailTime);
		mailSubject.setText(mailsubject);
		String mailfrom = intent.getStringExtra("mailfrom");
		if (!"未获取到发件人".equals(mailfrom)) {

			String[] from = mailfrom.split("<");
			if (!"".equals(from[0])) {
				mailFrom.setText(from[0]);
				mail_from_more_name.setText(from[0]);
			} else {
				String[] fromName = from[1].split("@");
				mailFrom.setText(fromName[0]);
				mail_from_more_name.setText(fromName[0]);
			}
			String[] fromAddress = from[1].split(">");
			mail_from_more_email.setText(fromAddress[0]);
			tos = mailTo.split(",");
			if ("".equals(tos[0])) {
				mail_add_more_rl.setVisibility(View.GONE);
			} else {
				MyMailToAdatper adapter = new MyMailToAdatper();
				mail_add_more_lv.setAdapter(adapter);
			}
		} else {
			mail_from_more_name.setText("未获取到发件人");
			mail_from_more_email.setText("未获取到发件人");
		}
		new GetMailDetailTask().execute(user.getUsername(), user.getPwd(), position);
	}
	
	/**
	 * 多个收件人用的是noSrollListview的适配器
	 */
	class MyMailToAdatper extends BaseAdapter {

		@Override
		public int getCount() {
			return tos.length;
		}

		@Override
		public String getItem(int position) {
			return tos[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(MailDetailActivity.this, R.layout.item_mail_to_address, null);
			TextView mailToName = (TextView) convertView.findViewById(R.id.mail_to_name);
			TextView mailToAddress = (TextView) convertView.findViewById(R.id.mail_to_address);
			String itemString = getItem(position);

			String[] from = itemString.split("<");
			if (!"".equals(from[0])) {
				mailToName.setText(from[0]);
			} else {
				String[] fromName = from[1].split("@");
				mailToName.setText(fromName[0]);
			}
			String[] fromAddress = from[1].split(">");
			mailToAddress.setText(fromAddress[0]);

			return convertView;
		}

	}
	
	class GetMailDetailTask extends AsyncTask<String, Void, HashMap<String, Object>>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog=new ProgressDialog(MailDetailActivity.this);
			dialog.setMessage("加载中...");
			dialog.show();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... arg0) {
			String[] mailAddress = arg0[0].split("@");
			String host = "pop." + mailAddress[1];
			HashMap<String, Object> map = new HashMap<String, Object>();
			ArrayList<String> attachments = new ArrayList<String>();
		    ArrayList<InputStream> attachmentsInputStreams = new ArrayList<InputStream>();
			String mailContent = "";
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
				POP3Folder folder = (POP3Folder) store.getFolder("INBOX");
				folder.open(Folder.READ_WRITE);	
				Message[] message = folder.getMessages();
				
				MimeMessage mimeMessage = (MimeMessage) message[message.length - 1 - Integer.parseInt(arg0[2])]; 
				
				MailReceiver mr = new MailReceiver(mimeMessage);
				mailContent = mr.getMailContent();
				attachments = mr.getAttachments();
				attachmentsInputStreams = mr.getAttachmentsInputStreams();
				map.put("mailContent", mailContent);
				map.put("attachments", attachments);
				map.put("attachmentsInputStreams", attachmentsInputStreams);
				folder.close(true);
				store.close();
			} catch (Exception e) {
				Log.e("zcj", e.toString());
			}
			return map;
		}
		
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.cancel();
			String mailContent = (String) result.get("mailContent");
			List<String> attachments = (List<String>) result.get("attachments");
			List<InputStream> attachmentsInputStreams = (List<InputStream>) result.get("attachmentsInputStreams");
			if(attachments.size() > 0){
				
				//存储附件的地址
				StringBuffer stringBuffer  = new StringBuffer();
				for(int i=0;i<attachments.size();i++){
					stringBuffer.append(Environment.getExternalStorageDirectory() + "/XingYe/" +attachments.get(i)+",");
				}
				String str = stringBuffer.toString();
				String str2 = str.substring(0, str.length()-1);
				mailService.updateFileById(getIntent().getStringExtra("id"), str2);
				
				
				lv_mailattachment.setVisibility(View.VISIBLE);
				attachmentsList.addAll(attachments);
				attachmentsIsList.addAll(attachmentsInputStreams);
				adapter.notifyDataSetChanged();
			}
			wv_content.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			wv_content.loadDataWithBaseURL(null, mailContent, "text/html", "utf-8", null);
			//设置缩放
			wv_content.getSettings().setBuiltInZoomControls(true);
	        //网页适配
	        DisplayMetrics dm = getResources().getDisplayMetrics();
	        //不显示缩放按钮
	        wv_content.getSettings().setDisplayZoomControls(false);
	        int scale = dm.densityDpi;
	        if (scale == 240) {
	        	wv_content.getSettings().setDefaultZoom(ZoomDensity.FAR);
	        } else if (scale == 160) {
	        	wv_content.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
	        } else {
	        	wv_content.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
	        }
	        wv_content.setWebChromeClient(new WebChromeClient());
			
		}
	}

	private static class MyHandler extends Handler {
	
	    private WeakReference<MailDetailActivity> wrActivity;
	
	    public MyHandler(MailDetailActivity activity) {
	        this.wrActivity = new WeakReference<MailDetailActivity>(activity);
	    }
	
	    @Override
	    public void handleMessage(android.os.Message msg) {
	        final MailDetailActivity activity = wrActivity.get();
	        switch (msg.what) {
	            case 0:
	                Toast.makeText(activity.getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
	                break;
	            default:
	                break;
	        }
	    };
	}


@Override
public void onClick(View v) {
	
	switch (v.getId()) {
	case R.id.image_back:
		MailDetailActivity.this.finish();
	break;
	case R.id.image_xingbiao:
		new PopupWindows(MailDetailActivity.this,image_xingbiao);
	break;
	case R.id.image_huifu:
		new PopupWindows2(MailDetailActivity.this,image_xingbiao);
	break;
	case R.id.image_delet:
		mailService.updateTypeById(getIntent().getStringExtra("id"), "5");
		finish();
	break;
	case R.id.image_more:
		new PopupWindows3(MailDetailActivity.this,image_xingbiao);
	break;
	}
	
};


	public class PopupWindows extends PopupWindow {
	
		public PopupWindows(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.select_xingbiao, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
			LinearLayout ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
			ll_1.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));
	
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0,200);
			update();
			TextView tv_yidu = (TextView) view.findViewById(R.id.tv_yidu);
			TextView tv_xingbiao = (TextView) view.findViewById(R.id.tv_xingbiao);
			tv_yidu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mailService.updateTypeById(getIntent().getStringExtra("id"), "6");
					dismiss();
				}
			});
			tv_xingbiao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mailService.updateTypeById(getIntent().getStringExtra("id"), "2");
					dismiss();
				}
			});
		}
	}
	
	
	public class PopupWindows2 extends PopupWindow {

		public PopupWindows2(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.select_huifu, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
			LinearLayout ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
			ll_1.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0,200);
			update();
			TextView tv_huifu = (TextView) view.findViewById(R.id.tv_huifu);
			TextView tv_zhuanfa = (TextView) view.findViewById(R.id.tv_zhuanfa);
			tv_huifu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(MailDetailActivity.this, WriteActivity.class);
					intent.putExtra("user", user);
					intent.putExtra("id", getIntent().getStringExtra("id"));
					
					String str = getIntent().getStringExtra("mailfrom");
					String str2 = str.substring(0, (str.length()-1));
					int a = str2.indexOf("<");
					String str3 = str2.substring(a+1,str2.length());
					
					intent.putExtra("mailfrom",str3);
					startActivity(intent);
					dismiss();
				}
			});
			tv_zhuanfa.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(attachmentsList.size()>0){
						for(int i=0;i<attachmentsList.size();i++){
							final int index = i;
							new Thread(new Runnable() {
			                    @Override
			                    public void run() {
			                    	InputStream is = attachmentsIsList.get(index);
									String path = new IOUtil().stream2file(is, Environment.getExternalStorageDirectory().toString() + "/XingYe/" + attachmentsList.get(index));
									if (path == null) {
			                        } else {
			                        }
			                    }
			                }).start();
						}
					}
					
					Intent intent = new Intent(MailDetailActivity.this, WriteActivity.class);
					intent.putExtra("user", user);
					intent.putExtra("zhuanfa", "zhuanfa");
					intent.putExtra("id", getIntent().getStringExtra("id"));
					
					String str = getIntent().getStringExtra("mailfrom");
					String str2 = str.substring(0, (str.length()-1));
					int a = str2.indexOf("<");
					String str3 = str2.substring(a+1,str2.length());
					
					intent.putExtra("mailfrom",str3);
					startActivity(intent);
					dismiss();
					
				}
			});
		}
	}
	
	public class PopupWindows3 extends PopupWindow {

		public PopupWindows3(Context mContext, View parent) {
			View view = View.inflate(mContext, R.layout.select_more, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
			LinearLayout ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
			ll_1.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0,200);
			update();  
			TextView tv_rili = (TextView) view.findViewById(R.id.tv_rili);
			TextView tv_jishiben = (TextView) view.findViewById(R.id.tv_jishiben);
			tv_rili.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MailDetailActivity.this, BuildActivity.class);
					intent.putExtra("id", getIntent().getStringExtra("id"));
					startActivity(intent);
					dismiss();
				}
			});
			tv_jishiben.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String str = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date());
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("title", "");
					hashMap.put("com", "");
					hashMap.put("flieMath", "");
					hashMap.put("datatime", str);
					hashMap.put("jtype", "2");
					hashMap.put("mailId",getIntent().getStringExtra("id"));
					note.addMail(hashMap);
					Toast.makeText(MailDetailActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
		}
	}
	
}
