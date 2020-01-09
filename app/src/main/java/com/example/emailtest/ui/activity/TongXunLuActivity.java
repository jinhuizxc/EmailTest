package com.example.emailtest.ui.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.emailtest.R;
import com.example.emailtest.db.CommunictionService;
import com.example.emailtest.entity.Communication;
import com.example.emailtest.utils.Stack;


public class TongXunLuActivity extends Activity implements OnClickListener{
	
	private ListView mailListView;
	private List<Communication> list = new ArrayList<Communication>();
	private MailListAdapter mailListAdapter;
	
	private CommunictionService comService ;
	
	ImageView image_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tongxunlu);
		Stack.addActivity(this);
		
		comService = new CommunictionService(this);
		
		image_back  = (ImageView) this.findViewById(R.id.image_back);
		image_back.setOnClickListener(this);
		
		list = comService.findComAll();
		mailListView = (ListView) this.findViewById(R.id.lstv);
		mailListAdapter = new MailListAdapter(this, list);
		mailListView.setAdapter(mailListAdapter);
		mailListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.image_back:
			finish();
		break;
		}
		
	}
	
	public class MailListAdapter extends BaseAdapter {
		private List<Communication> list;
		private LayoutInflater inflater;
		private Context context;

		public MailListAdapter(Context context, List<Communication> list) {
			inflater = LayoutInflater.from(context);
			this.list = list;
			this.context = context;
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
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_4, parent, false);
				vh = new ViewHolder();
				vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			Communication mb = list.get(position);
			vh.tv_name.setText(mb.getNumbers());
			return convertView;
		}
		class ViewHolder {
			TextView tv_name;
		}
	}
	
	
	
}
