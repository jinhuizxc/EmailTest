package com.example.emailtest.adapter;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.emailtest.R;
import com.example.emailtest.entity.User;

public class UserListTwoAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<User> list;
	
	public UserListTwoAdapter(Context context, List<User> list) {
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
			convertView = inflater.inflate(R.layout.user_list_item2, parent, false);
			vh = new ViewHolder();
			vh.mailName = (TextView) convertView.findViewById(R.id.mail_name);
			vh.tv_ismain = (TextView) convertView.findViewById(R.id.tv_ismain);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.mailName.setText(list.get(position).getUsername());
		
		if("1".equals(list.get(position).getIsmain())){
			vh.tv_ismain.setText("主账号");
		}else{
			vh.tv_ismain.setText("");
		}
		
		return convertView;
	}

	class ViewHolder{
		public TextView mailName,tv_ismain;
	}
}
