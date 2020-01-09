package com.example.emailtest.ui.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.emailtest.R;
import com.example.emailtest.db.MailService;
import com.example.emailtest.db.NotepadService;
import com.example.emailtest.db.UserService;
import com.example.emailtest.entity.MailBrief;
import com.example.emailtest.entity.User;
import com.example.emailtest.utils.Stack;


public class JiShiBenActivity extends Activity implements OnClickListener{
	
	Activity thisActivity = this;
	List<HashMap<String, Object>> result = null;
	ListView nolist;
	NewsAdapter grabAdapter=null;
	
	TextView tv_jishiben;
	ImageView image_quexiao,image_add;
	RelativeLayout ll_zhoubian;
	
	private NotepadService note;
	private MailService mail;
	private User user;
	private UserService userService;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jishiben);
		Stack.addActivity(this);
		note = new NotepadService(this);
		mail = new MailService(this);
		userService = new UserService(this);
		user = new User();
		
		ll_zhoubian =  (RelativeLayout) this.findViewById(R.id.ll_zhoubian);
		nolist =  (ListView) this.findViewById(R.id.nolist);
		image_quexiao  =  (ImageView) this.findViewById(R.id.image_quexiao);
		image_add  =  (ImageView) this.findViewById(R.id.image_add);
		tv_jishiben  =  (TextView) this.findViewById(R.id.tv_jishiben);
		image_quexiao.setOnClickListener(this);image_add.setOnClickListener(this);
		tv_jishiben.setOnClickListener(this);
		
		result = note.findNoteAll();
		grabAdapter = new NewsAdapter(thisActivity,	result);
		nolist.setAdapter(grabAdapter);
		
		nolist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				Intent intent = new Intent(thisActivity, NoteDetailActivity.class);
				HashMap<String, Object> hashMap =result.get(position);
				if(hashMap.get("mailId").toString().length()>0){
					MailBrief mb = mail.findById(hashMap.get("mailId").toString());
					user = userService.findByUserId(mb.getUserId());
					intent.putExtra("user", user);
					intent.putExtra("position", mb.getMindex());
					intent.putExtra("mailsubject", mb.getSubject());
					intent.putExtra("mailfrom", mb.getFromPersonal());
					intent.putExtra("mailTime", mb.getSendTime());
					intent.putExtra("mailTo", mb.getTOO());
					intent.putExtra("mailCC", mb.getCC());
					intent.putExtra("mailBCC", mb.getBCC());
					intent.putExtra("id", mb.getId());
					
					intent.putExtra("NoteId", hashMap.get("id").toString());
					startActivity(intent);
				}else{
					intent.putExtra("user", user);
					intent.putExtra("mailsubject", hashMap.get("title").toString());
					intent.putExtra("NoteId", hashMap.get("id").toString());
					startActivity(intent);
				}
				
				
				
//				/**
//				 * 上面注释的代码 会出现崩溃
//				 */
//				HashMap<String, Object> hashMap = result.get(position);
//				Intent intent = new Intent(thisActivity, NotePadDetailActivity.class);
//				intent.putExtra("com", hashMap.get("com").toString());
//				intent.putExtra("datatime", hashMap.get("datatime").toString());
//				intent.putExtra("fileMath", hashMap.get("flieMath").toString());
//				intent.putExtra("id", hashMap.get("id").toString());
//				intent.putExtra("jtype", hashMap.get("jtype").toString());
//				intent.putExtra("mailId", hashMap.get("mailId").toString());
//				intent.putExtra("title", hashMap.get("title").toString());
//
//				startActivity(intent);
			}
		});
			
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.image_add:
			startActivity(new Intent(JiShiBenActivity.this, WriteJiShiActivity.class));
		break;
		case R.id.image_quexiao:
			finish();
		break;
		case R.id.tv_jishiben:
			new PopupWindows(JiShiBenActivity.this,ll_zhoubian);
		break;
		}
		
	}
	
	
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.select_2, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
			LinearLayout ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
			ll_1.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAsDropDown(parent);
			showAtLocation(parent, Gravity.TOP, 0, 0);
			update();
			TextView tv_all = (TextView) view.findViewById(R.id.tv_all);
			TextView tv_xingbiao = (TextView) view.findViewById(R.id.tv_xingbiao);
			TextView tv_fenlei = (TextView) view.findViewById(R.id.tv_fenlei);
			TextView tv_riji = (TextView) view.findViewById(R.id.tv_riji);
			TextView tv_shuqian = (TextView) view.findViewById(R.id.tv_shuqian);
			RelativeLayout jishi_rl_xingbiao = (RelativeLayout) view.findViewById(R.id.jishi_rl_xingbiao);
			
			jishi_rl_xingbiao.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					result.clear();
					result = note.findNoteByType("1");
					grabAdapter = new NewsAdapter(thisActivity,	result);
					nolist.setAdapter(grabAdapter);
					grabAdapter.notifyDataSetChanged();
					dismiss();
				}
			});
			
			tv_all.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					result.clear();
					result = note.findNoteAll();
					grabAdapter = new NewsAdapter(thisActivity,	result);
					nolist.setAdapter(grabAdapter);
					grabAdapter.notifyDataSetChanged();
					dismiss();
				}
			});
			tv_xingbiao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					result.clear();
					result = note.findNoteByType("1");
					grabAdapter = new NewsAdapter(thisActivity,	result);
					nolist.setAdapter(grabAdapter);
					grabAdapter.notifyDataSetChanged();
					dismiss();
				}
			});
			tv_fenlei.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					result.clear();
					result = note.findNoteByType("2");
					grabAdapter = new NewsAdapter(thisActivity,	result);
					nolist.setAdapter(grabAdapter);
					grabAdapter.notifyDataSetChanged();
					dismiss();
				}
			});
			tv_riji.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					result.clear();
					result = note.findNoteByType("3");
					grabAdapter = new NewsAdapter(thisActivity,	result);
					nolist.setAdapter(grabAdapter);
					grabAdapter.notifyDataSetChanged();
					dismiss();
				}
			});
			
			tv_shuqian.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					result.clear();
					result = note.findNoteByType("4");
					grabAdapter = new NewsAdapter(thisActivity,	result);
					nolist.setAdapter(grabAdapter);
					grabAdapter.notifyDataSetChanged();
					dismiss();
				}
			});
			
		}
	}
	
	private String getFileName() {  
        String saveDir = Environment.getExternalStorageDirectory() + "/email";  
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
	
	
	public class NewsAdapter extends BaseAdapter{
		List<HashMap<String, Object>> map = null; 
		Context context;
		View mMenuView;
		public NewsAdapter(Activity context,List<HashMap<String, Object>> map) {
			this.map = map; 
			this.context=context;
		}

		@Override
		public int getCount() {
			return map.size();
		}

		@Override
		public HashMap<String, Object> getItem(int position) {
			return map.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
		
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_2, null);
				holder = new ViewHolder(); 
				
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name); 
	 			holder.tv_guige = (TextView) convertView.findViewById(R.id.tv_guige); 
	 			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time); 
	 			holder.image_tou = (ImageView) convertView.findViewById(R.id.image_tou); 
	 			holder.image_biaoji = (ImageView) convertView.findViewById(R.id.image_biaoji); 
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HashMap<String, Object> mapItem = map.get(position); 
			   
			String id=mapItem.get("id").toString();
	  
	 		if(mapItem.get("mailId").toString().length()>0){
				MailBrief mails = mail.findById(mapItem.get("mailId").toString());
				int a = mails.getFromPersonal().indexOf("<");
				String str = mails.getFromPersonal().substring(0, a);
				holder.tv_name.setText(str);
				holder.tv_guige.setText(mails.getSubject());
			}else{
				holder.tv_name.setText(mapItem.get("title").toString());
				holder.tv_guige.setText("(无摘要)");
			}
			
			if(mapItem.get("flieMath").toString().length()>0){
				String[] str = mapItem.get("flieMath").toString().split(",");
				BitmapFactory.Options options = new BitmapFactory.Options();  
	            Bitmap myBitmap = BitmapFactory.decodeFile(str[0], options);
	            holder.image_tou.setImageBitmap(myBitmap);
			}else{
				holder.image_tou.setBackgroundResource(R.drawable.ic_launcher);
			}
			
			holder.tv_time.setText(mapItem.get("datatime").toString().substring(5));
	 	  
			if("1".equals(mapItem.get("jtype").toString())){
				holder.image_biaoji.setVisibility(View.VISIBLE);
			}else{
				holder.image_biaoji.setVisibility(View.GONE);
			}
			
			return convertView;
		}
	 
		 
		public void addItem(HashMap<String, Object> newsitem) {
			map.add(newsitem);
		}
		public void addItemAll(List newsitem) {
			map.addAll(newsitem);
		} 
		public void removeAll() {
			map.clear();
		}
		
		class ViewHolder {   
			TextView tv_name,tv_guige,tv_time;ImageView image_tou,image_biaoji;
		}
	} 
	
	@Override
	protected void onResume() {
		super.onResume();
		result.clear();
		result = note.findNoteAll();
		grabAdapter = new NewsAdapter(thisActivity,	result);
		nolist.setAdapter(grabAdapter);
		grabAdapter.notifyDataSetChanged();
	}

}



