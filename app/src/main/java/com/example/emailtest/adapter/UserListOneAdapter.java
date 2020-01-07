package com.example.emailtest.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emailtest.R;
import com.example.emailtest.entity.User;

public class UserListOneAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<User> list;
	
	public UserListOneAdapter(Context context, List<User> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.user_list_item, parent, false);
			vh = new ViewHolder();
			vh.mailName = (TextView) convertView.findViewById(R.id.mail_name);
			vh.icon_mail = (ImageView) convertView.findViewById(R.id.icon_mail);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.mailName.setText(list.get(position).getUsername() + "的收件箱");
		String username = list.get(position).getUsername();
		String[] split = username.split("@");
		if ("qq.com".equals(split[1])) {
			vh.icon_mail.setBackgroundResource(R.drawable.mail_qq);
		}else if ("163.com".equals(split[1])) {
			vh.icon_mail.setBackgroundResource(R.drawable.mail_163);
		}else if ("126.com".equals(split[1])) {
			vh.icon_mail.setBackgroundResource(R.drawable.mail_126);
		}else if ("excange.com".equals(split[1])) {
			vh.icon_mail.setBackgroundResource(R.drawable.mail_excange);
		}else if ("gmial.com".equals(split[1])) {
			vh.icon_mail.setBackgroundResource(R.drawable.gmail_icon);
		}else if ("outlook.com".equals(split[1])) {
			vh.icon_mail.setBackgroundResource(R.drawable.mail_outlook);
		}else{
			vh.icon_mail.setBackgroundResource(R.drawable.or);
		}
		
		return convertView;
	}

	class ViewHolder{
		private TextView mailName;
		private ImageView icon_mail;
	}
}
