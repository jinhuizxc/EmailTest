package com.example.emailtest.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.ToggleButton;

import com.example.emailtest.R;
import com.example.emailtest.db.ClanderService;
import com.example.emailtest.db.MailService;
import com.example.emailtest.entity.MailBrief;
import com.example.emailtest.utils.Stack;


@SuppressLint("SimpleDateFormat")
public class BuildActivity extends Activity implements OnClickListener,OnDateChangedListener,OnTimeChangedListener{
	
	RelativeLayout re_start,re_end,re_tixing,re_chongfu,re_rili;LinearLayout ll_main;
	TextView tv_riqi,tv_xingqi,tv_time,tv_riqi2,tv_xingqi2,tv_time2,tv_tixing,tv_chongfu,tv_rili,tv_data,tv_data2,tv_ok;
	Button image_quexiao,image_ok;
	EditText ed_name,ed_address,ed_beizhu;
	
	ToggleButton btn_quantian;
	
	private DatePicker datePicker;
	private TimePicker timePicker;
	private String dateTime;
	private String initDateTime;
	
	int number=1;
	
	private MailBrief mail;
	private MailService mailService ;
	private ClanderService clanderService;
	
	private String startime,endtime,alltime="0",tixing="1",chongfu="1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_build);
		Stack.addActivity(this);
		mailService = new MailService(this);
		clanderService = new ClanderService(this);
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		Date date = new Date(); 
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
		String strs = sdf1.format(date);
		
		initDateTime = year+"年"+(month+1)+"月"+day+"日"+" "+strs;
		
		
		startime = year+""+(month+1)+day;
		endtime = year+""+(month+1)+day;
		
		
		
		ll_main =  (LinearLayout) this.findViewById(R.id.ll_main);
		re_start =  (RelativeLayout) this.findViewById(R.id.re_start);
		re_end =  (RelativeLayout) this.findViewById(R.id.re_end);
		re_tixing =  (RelativeLayout) this.findViewById(R.id.re_tixing);
		re_chongfu =  (RelativeLayout) this.findViewById(R.id.re_chongfu);
		re_rili =  (RelativeLayout) this.findViewById(R.id.re_rili);
		
		tv_riqi =  (TextView) this.findViewById(R.id.tv_riqi);
		tv_xingqi =  (TextView) this.findViewById(R.id.tv_xingqi);
		tv_time =  (TextView) this.findViewById(R.id.tv_time);
		tv_riqi2 =  (TextView) this.findViewById(R.id.tv_riqi2);
		tv_xingqi2 =  (TextView) this.findViewById(R.id.tv_xingqi2);
		tv_time2 =  (TextView) this.findViewById(R.id.tv_time2);
		tv_tixing =  (TextView) this.findViewById(R.id.tv_tixing);
		tv_chongfu =  (TextView) this.findViewById(R.id.tv_chongfu);
		tv_rili =  (TextView) this.findViewById(R.id.tv_rili);
		
		image_quexiao  =  (Button) this.findViewById(R.id.image_quexiao);
		image_ok  =  (Button) this.findViewById(R.id.image_ok);
		
		ed_name  =  (EditText) this.findViewById(R.id.ed_name);
		ed_address  =  (EditText) this.findViewById(R.id.ed_address);
		ed_beizhu  =  (EditText) this.findViewById(R.id.ed_beizhu);
		
		btn_quantian  =  (ToggleButton) this.findViewById(R.id.btn_quantian);
		btn_quantian.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					alltime="1";
				}else{
					alltime="0";
				}
				
			}
		});
		
		ed_name.setText(getIntent().getStringExtra("title"));
		
		re_start.setOnClickListener(this);re_end.setOnClickListener(this);
		re_tixing.setOnClickListener(this);re_chongfu.setOnClickListener(this);
		re_rili.setOnClickListener(this);image_quexiao.setOnClickListener(this);
		image_ok.setOnClickListener(this);
		
		if(getIntent().getStringExtra("id")!=null){
			mail = mailService.findById(getIntent().getStringExtra("id"));
			ed_name.setText(mail.getSubject());
		}
		
		
		String str = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date());
		String str2 = str.substring(5, 10);
		String str3 = str.substring(11,str.length());
		int a = str3.indexOf(":");
		String str4 = str3.substring(0,a);
		String str5 = str3.substring(a+1,str3.length());
		int b = Integer.parseInt(str4.trim())+1;
		String str6 = b+":"+str5;
		
		Date dates = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		
		tv_riqi.setText(str2);tv_time.setText(str3);tv_xingqi.setText(dateFm.format(date));
		tv_riqi2.setText(str2);tv_time2.setText(str6);tv_xingqi2.setText(dateFm.format(date));
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.re_start:
			new PopupWindows(BuildActivity.this, ll_main);
		break;
		case R.id.re_end:
			new PopupWindows2(BuildActivity.this, ll_main);
		break;
		case R.id.re_tixing:
			if(number==1){
				LayoutInflater inflater = BuildActivity.this.getLayoutInflater();
				final View layout = inflater.inflate(R.layout.select_3,(ViewGroup)BuildActivity.this.findViewById(R.id.selectLayout));
				final Dialog dialog = new Dialog(BuildActivity.this, R.style.dialog);
				Window win = dialog.getWindow();
				win.getDecorView().setPadding(0, 0, 0, 0);
				WindowManager.LayoutParams lp = win.getAttributes();
				lp.width =getWindowsWidth(BuildActivity.this) - 100;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				win.setAttributes(lp);
				dialog.setContentView(layout);
				dialog.show();
				RelativeLayout re_no = (RelativeLayout) layout.findViewById(R.id.re_no);
				RelativeLayout re_fa = (RelativeLayout) layout.findViewById(R.id.re_fa);
				RelativeLayout re_five = (RelativeLayout) layout.findViewById(R.id.re_five);
				RelativeLayout re_shifive = (RelativeLayout) layout.findViewById(R.id.re_shifive);
				RelativeLayout re_one = (RelativeLayout) layout.findViewById(R.id.re_one);
				RelativeLayout re_day = (RelativeLayout) layout.findViewById(R.id.re_day);
				final ImageView image_no = (ImageView) layout.findViewById(R.id.image_no);
				final ImageView image_fa = (ImageView) layout.findViewById(R.id.image_fa);
				final ImageView image_five = (ImageView) layout.findViewById(R.id.image_five);
				final ImageView image_shifive = (ImageView) layout.findViewById(R.id.image_shifive);
				final ImageView image_one = (ImageView) layout.findViewById(R.id.image_one);
				final ImageView image_day = (ImageView) layout.findViewById(R.id.image_day);
				String str = tv_tixing.getText().toString();
				if("无提醒".equals(str)){
					image_no.setVisibility(View.VISIBLE);
				}else if("事件发生时".equals(str)){
					image_fa.setVisibility(View.VISIBLE);
				}else if("5分钟前".equals(str)){
					image_five.setVisibility(View.VISIBLE);
				}else if("15分钟前".equals(str)){
					image_shifive.setVisibility(View.VISIBLE);
				}else if("1小时前".equals(str)){
					image_one.setVisibility(View.VISIBLE);
				}else if("1天前".equals(str)){
					image_day.setVisibility(View.VISIBLE);
				}
				re_no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.VISIBLE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.GONE);
						tv_tixing.setText("无提醒");
						tixing="1";
						dialog.dismiss();
					}
				});
				re_fa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.VISIBLE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.GONE);
						tv_tixing.setText("事件发生时");
						tixing="2";
						dialog.dismiss();
					}
				});
				re_five.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.VISIBLE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.GONE);
						tv_tixing.setText("5分钟前");
						tixing="3";
						dialog.dismiss();
					}
				});
				re_shifive.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.VISIBLE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.GONE);
						tv_tixing.setText("15分钟前");
						tixing="4";
						dialog.dismiss();
					}
				});
				re_one.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.VISIBLE);
						image_day.setVisibility(View.GONE);
						tv_tixing.setText("1小时前");
						tixing="5";
						dialog.dismiss();
					}
				});
				re_day.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.VISIBLE);
						tv_tixing.setText("1天前");
						tixing="6";
						dialog.dismiss();
					}
				});
			}
		break;
		case R.id.re_chongfu:
			if(number==1){
				LayoutInflater inflater = BuildActivity.this.getLayoutInflater();
				final View layout = inflater.inflate(R.layout.select_4,(ViewGroup)BuildActivity.this.findViewById(R.id.selectLayout));
				final Dialog dialog = new Dialog(BuildActivity.this, R.style.dialog);
				Window win = dialog.getWindow();
				win.getDecorView().setPadding(0, 0, 0, 0);
				WindowManager.LayoutParams lp = win.getAttributes();
				lp.width =getWindowsWidth(BuildActivity.this) - 100;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				win.setAttributes(lp);
				dialog.setContentView(layout);
				dialog.show();
				RelativeLayout re_no = (RelativeLayout) layout.findViewById(R.id.re_no);
				RelativeLayout re_fa = (RelativeLayout) layout.findViewById(R.id.re_fa);
				RelativeLayout re_five = (RelativeLayout) layout.findViewById(R.id.re_five);
				RelativeLayout re_shifive = (RelativeLayout) layout.findViewById(R.id.re_shifive);
				RelativeLayout re_one = (RelativeLayout) layout.findViewById(R.id.re_one);
				RelativeLayout re_day = (RelativeLayout) layout.findViewById(R.id.re_day);
				final ImageView image_no = (ImageView) layout.findViewById(R.id.image_no);
				final ImageView image_fa = (ImageView) layout.findViewById(R.id.image_fa);
				final ImageView image_five = (ImageView) layout.findViewById(R.id.image_five);
				final ImageView image_shifive = (ImageView) layout.findViewById(R.id.image_shifive);
				final ImageView image_one = (ImageView) layout.findViewById(R.id.image_one);
				final ImageView image_day = (ImageView) layout.findViewById(R.id.image_day);
				String str = tv_chongfu.getText().toString();
				if("永不".equals(str)){
					image_no.setVisibility(View.VISIBLE);
				}else if("每天".equals(str)){
					image_fa.setVisibility(View.VISIBLE);
				}else if("工作日".equals(str)){
					image_five.setVisibility(View.VISIBLE);
				}else if("每周".equals(str)){
					image_shifive.setVisibility(View.VISIBLE);
				}else if("每月".equals(str)){
					image_one.setVisibility(View.VISIBLE);
				}else if("每年".equals(str)){
					image_day.setVisibility(View.VISIBLE);
				}
				re_no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.VISIBLE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.GONE);
						tv_chongfu.setText("永不");
						chongfu="1";
						dialog.dismiss();
					}
				});
				re_fa.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.VISIBLE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.GONE);
						tv_chongfu.setText("每天");
						chongfu="2";
						dialog.dismiss();
					}
				});
				re_five.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.VISIBLE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.GONE);
						tv_chongfu.setText("工作日");
						chongfu="3";
						dialog.dismiss();
					}
				});
				re_shifive.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.VISIBLE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.GONE);
						tv_chongfu.setText("每周");
						chongfu="4";
						dialog.dismiss();
					}
				});
				re_one.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.VISIBLE);
						image_day.setVisibility(View.GONE);
						tv_chongfu.setText("每月");
						chongfu="5";
						dialog.dismiss();
					}
				});
				re_day.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						image_no.setVisibility(View.GONE);
						image_fa.setVisibility(View.GONE);
						image_five.setVisibility(View.GONE);
						image_shifive.setVisibility(View.GONE);
						image_one.setVisibility(View.GONE);
						image_day.setVisibility(View.VISIBLE);
						tv_chongfu.setText("每年");
						chongfu="6";
						dialog.dismiss();
					}
				});
			}
		break;
		case R.id.re_rili:
			LayoutInflater inflater = BuildActivity.this.getLayoutInflater();
			final View layout = inflater.inflate(R.layout.select_5,(ViewGroup)BuildActivity.this.findViewById(R.id.selectLayout));
			final Dialog dialog = new Dialog(BuildActivity.this, R.style.dialog);
			Window win = dialog.getWindow();
			win.getDecorView().setPadding(0, 0, 0, 0);
			WindowManager.LayoutParams lp = win.getAttributes();
			lp.width =getWindowsWidth(BuildActivity.this) - 100;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			win.setAttributes(lp);
			dialog.setContentView(layout);
			dialog.show();
			TextView tv_name = (TextView) layout.findViewById(R.id.tv_name);
			tv_name.setText("测试数据");
		break;
		case R.id.image_quexiao:
			finish();
		break;
		case R.id.image_ok:
//			title 标题   ， startime 开始时间   ， endtime 结束时间  ， 
//			alltime 是否全天（  0  否   ，  1 是）  ，
//			tixing （1 无提醒，  2  事件发生时  ，  3  五分钟前   ，  4 15分钟前   ，  5 一小时前  ，  6  一天前）  , 
//			chongfu（1 永不，  2  事件发生时  ，  3  每天   ，  4 工作日   ，  5 每周  ，  6  每月  ，  7 每年）， 
//			address  地址   ，comment  备注  ，  mailId  邮件的id
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("title",ed_name.getText().toString());
			hashMap.put("startime",startime);
			hashMap.put("endtime", endtime);
			hashMap.put("alltime", alltime);
			hashMap.put("tixing", tixing);
			hashMap.put("chongfu", chongfu);
			hashMap.put("address", ed_address.getText().toString());
			hashMap.put("comment", ed_beizhu.getText().toString());
			if(getIntent().getStringExtra("id")!=null){
				hashMap.put("mailId", getIntent().getStringExtra("id"));
			}else{
				hashMap.put("mailId", "0");
			}
			
			if(getIntent().getStringExtra("notepadId")!=null){
				hashMap.put("notepadId", getIntent().getStringExtra("notepadId"));
			}else{
				hashMap.put("notepadId", "0");
			}
			clanderService.addClander(hashMap);
			finish();
		break;
		
		}
		
	}
	
	public class PopupWindows extends PopupWindow {
		public PopupWindows(Context mContext, View parent) {
			View view = View
					.inflate(mContext, R.layout.common_datetime, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			datePicker = (DatePicker) view.findViewById(R.id.datepicker);
			timePicker = (TimePicker) view.findViewById(R.id.timepicker);
			init(datePicker, timePicker);
			timePicker.setIs24HourView(true);
			timePicker.setOnTimeChangedListener(BuildActivity.this);
			tv_data = (TextView) view.findViewById(R.id.tv_data);
			tv_data2 = (TextView) view.findViewById(R.id.tv_time);
			int a = initDateTime.indexOf("日");
			String data = initDateTime.substring(0, a+1);
			String data2 = initDateTime.substring(a+2);
			tv_data.setText(data);tv_data2.setText(data2);
			tv_ok = (TextView) view.findViewById(R.id.tv_ok);
			tv_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int a = dateTime.indexOf("日");
					int a2 = dateTime.indexOf("年");
					String data = dateTime.substring(a2+1, a+1);
					String data2 = dateTime.substring(a+2);
					String data3 = dateTime.substring(0, a+1);
					SimpleDateFormat si = new SimpleDateFormat("yyyy年MM月dd日");
					Date dt;
					try {
					dt = si.parse(data3);
					 String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
				     Calendar cal = Calendar.getInstance();
				     cal.setTime(dt);
				     int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
				     if (w < 0)
				         w = 0;
				     tv_riqi.setText(data);tv_time.setText(data2);tv_xingqi.setText(weekDays[w]+"");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String years=dateTime.substring(0, 4);
					String months=dateTime.substring(6, 7);
					String dates=dateTime.substring(8, 10);
					
					startime = years+months+dates;
					Toast.makeText(BuildActivity.this,dateTime , Toast.LENGTH_SHORT).show();
					
					dismiss();
				}
			});
			tv_data.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					datePicker.setVisibility(View.VISIBLE);
					timePicker.setVisibility(View.GONE);
					tv_data.setTextColor(Color.parseColor("#1d91e5"));
					tv_data2.setTextColor(Color.parseColor("#000000"));
				}
			});
			tv_data2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					datePicker.setVisibility(View.GONE);
					timePicker.setVisibility(View.VISIBLE);
					tv_data.setTextColor(Color.parseColor("#000000"));
					tv_data2.setTextColor(Color.parseColor("#1d91e5"));
				}
			});
			
			onDateChanged(null, 0, 0, 0);
		}
	}
	
	public class PopupWindows2 extends PopupWindow {
		public PopupWindows2(Context mContext, View parent) {
			View view = View
					.inflate(mContext, R.layout.common_datetime, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			
			datePicker = (DatePicker) view.findViewById(R.id.datepicker);
			timePicker = (TimePicker) view.findViewById(R.id.timepicker);
			init(datePicker, timePicker);
			timePicker.setIs24HourView(true);
			timePicker.setOnTimeChangedListener(BuildActivity.this);
			
			tv_data = (TextView) view.findViewById(R.id.tv_data);
			tv_data2 = (TextView) view.findViewById(R.id.tv_time);
			int a = initDateTime.indexOf("日");
			String data = initDateTime.substring(0, a+1);
			String data2 = initDateTime.substring(a+2);
			tv_data.setText(data);tv_data2.setText(data2);
			tv_ok = (TextView) view.findViewById(R.id.tv_ok);
			tv_ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int a = dateTime.indexOf("日");
					int a2 = dateTime.indexOf("年");
					String data = dateTime.substring(a2+1, a+1);
					String data2 = dateTime.substring(a+2);
					String data3 = dateTime.substring(0, a+1);
					SimpleDateFormat si = new SimpleDateFormat("yyyy年MM月dd日");
					Date dt;
					try {
					 dt = si.parse(data3);
					 String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
				     Calendar cal = Calendar.getInstance();
				     cal.setTime(dt);
				     int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
				     if (w < 0)
				         w = 0;
				     tv_riqi2.setText(data);tv_time2.setText(data2);tv_xingqi2.setText(weekDays[w]+"");
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					String years=dateTime.substring(0, 4);
					String months=dateTime.substring(6, 7);
					String dates=dateTime.substring(8, 10);
					
					endtime = years+months+dates;
					Toast.makeText(BuildActivity.this,dateTime , Toast.LENGTH_SHORT).show();
					dismiss();
				}
			});
			tv_data.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					datePicker.setVisibility(View.VISIBLE);
					timePicker.setVisibility(View.GONE);
					tv_data.setTextColor(Color.parseColor("#1d91e5"));
					tv_data2.setTextColor(Color.parseColor("#000000"));
				}
			});
			tv_data2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					datePicker.setVisibility(View.GONE);
					timePicker.setVisibility(View.VISIBLE);
					
					tv_data.setTextColor(Color.parseColor("#000000"));
					tv_data2.setTextColor(Color.parseColor("#1d91e5"));
				}
			});
			
			onDateChanged(null, 0, 0, 0);
			
		}
	}
	
	public void init(DatePicker datePicker, TimePicker timePicker) {
		Calendar calendar = Calendar.getInstance();
		if (!(null == initDateTime || "".equals(initDateTime))) {
			calendar = this.getCalendarByInintData(initDateTime);
		} else {
			initDateTime = calendar.get(Calendar.YEAR) + "年"
					+ calendar.get(Calendar.MONTH) + "月"
					+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "
					+ calendar.get(Calendar.HOUR_OF_DAY) + ":"
					+ calendar.get(Calendar.MINUTE);
		}

		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), this);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
	}
	
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		onDateChanged(null, 0, 0, 0);
	}

	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// 获得日历实例
		Calendar calendar = Calendar.getInstance();

		calendar.set(datePicker.getYear(), datePicker.getMonth(),
				datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		dateTime = sdf.format(calendar.getTime());
		int a = dateTime.indexOf("日");
		String data = dateTime.substring(0, a+1);
		String data2 = dateTime.substring(a+2);
		tv_data.setText(data);tv_data2.setText(data2);
	}

	/**
	 * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
	 * 
	 * @param initDateTime
	 *            初始日期时间值 字符串型
	 * @return Calendar
	 */
	private Calendar getCalendarByInintData(String initDateTime) {
		Calendar calendar = Calendar.getInstance();

		// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
		String date = spliteString(initDateTime, "日", "index", "front"); // 日期
		String time = spliteString(initDateTime, "日", "index", "back"); // 时间
		
		String yearStr = spliteString(date, "年", "index", "front"); // 年份
		String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

		String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
		String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

		String hourStr = spliteString(time, ":", "index", "front"); // 时
		String minuteStr = spliteString(time, ":", "index", "back"); // 分

		int currentYear = Integer.valueOf(yearStr.trim()).intValue();
		int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
		int currentDay = Integer.valueOf(dayStr.trim()).intValue();
		int currentHour = Integer.valueOf(hourStr.trim()).intValue();
		int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();

		calendar.set(currentYear, currentMonth, currentDay, currentHour,
				currentMinute);
		return calendar;
	}

	/**
	 * 截取子串
	 * 
	 * @param srcStr
	 *            源串
	 * @param pattern
	 *            匹配模式
	 * @param indexOrLast
	 * @param frontOrBack
	 * @return
	 */
	public static String spliteString(String srcStr, String pattern,
			String indexOrLast, String frontOrBack) {
		String result = "";
		int loc = -1;
		if (indexOrLast.equalsIgnoreCase("index")) {
			loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
		} else {
			loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
		}
		if (frontOrBack.equalsIgnoreCase("front")) {
			if (loc != -1)
				result = srcStr.substring(0, loc); // 截取子串
		} else {
			if (loc != -1)
				result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
		}
		return result;
	}
	
	/** 获取屏幕的宽度 */
	public int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	
	
	
}
