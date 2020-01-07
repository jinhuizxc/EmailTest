package com.example.emailtest.adapter;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emailtest.R;
import com.example.emailtest.entity.MailBrief;
import com.example.emailtest.entity.User;
import com.example.emailtest.mail.util.SharedPreferencesUtils;

public class MailListAdapter extends BaseAdapter {

	private List<MailBrief> list;
	private LayoutInflater inflater;
	private Context context;
	private User user;

	public MailListAdapter(Context context, List<MailBrief> list, User user) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
		this.user = user;
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
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mail_list_item, parent, false);
			vh = new ViewHolder();
			vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			vh.tvLaiYuan = (TextView) convertView.findViewById(R.id.tv_laiyuan);
			vh.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			vh.image_biaoji2 = (ImageView) convertView.findViewById(R.id.image_biaoji2);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		MailBrief mb = list.get(position);
		// 做的本地假已读未读
		boolean isBoolean = SharedPreferencesUtils.getValue(context, user.getUsername(), mb.getSubject()+mb.getSendTime(), false);
		if (mb.getMailType().equals("2")) {
			vh.image_biaoji2.setVisibility(View.VISIBLE);
		} else {
			vh.image_biaoji2.setVisibility(View.GONE);
		}

		// 圆圈中的文字显示，中文时显示一个，英文或数字时显示两个
		String split = mb.getFromPersonal();

		String[] from = split.split("<");
		if ("".equals(from[0])) { // 没有显示发件人备注，就用邮箱名截取@前面的字符串
			String[] split1 = from[1].split("@");
			if (isBoolean) {
				vh.tvName.setText(split1[0]);
				vh.tvName.setCompoundDrawables(null, null, null, null);
			} else {
				Drawable nav_left = context.getResources().getDrawable(R.drawable.wl);
				nav_left.setBounds(0, 0, nav_left.getMinimumWidth(), nav_left.getMinimumHeight());// 必须设置图片大小，否则不显示
				vh.tvName.setCompoundDrawables(nav_left, null, null, null);
				vh.tvName.setCompoundDrawablePadding(5);
				vh.tvName.setText(split1[0]);
			}

			String subString = getString(split1[0].substring(0, 1));
			if ("1".equals(subString)) {
				if (split1[0].length() >= 2) {
					vh.tv_title.setText(split1[0].substring(0, 2));
				} else if (split1[0].length() == 1) {
					vh.tv_title.setText(split1[0].substring(0, 1));
				}
				// vh.tv_title.setText(split1[0].substring(0, 2));
			} else if ("e".equals(subString)) {
				if (split1[0].length() >= 2) {
					String upperCase = split1[0].substring(0, 1).toUpperCase();
					vh.tv_title.setText(upperCase + split1[0].substring(1, 2));
				} else if (split1[0].length() == 1) {
					vh.tv_title.setText(split1[0].substring(0, 1).toUpperCase());
				}
			} else if ("c".equals(subString)) {
				vh.tv_title.setText(split1[0].substring(0, 1));
			} else {
				vh.tv_title.setText("邮");
			}
		} else { // 如果有备注的姓名,就从备注的姓名中截取显示

			if (isBoolean) {
				vh.tvName.setText(from[0]);
				vh.tvName.setCompoundDrawables(null, null, null, null);
			} else {
				vh.tvName.setText(from[0]);
				Drawable nav_left = context.getResources().getDrawable(R.drawable.wl);
				nav_left.setBounds(0, 0, nav_left.getMinimumWidth(), nav_left.getMinimumHeight());// 必须设置图片大小，否则不显示
				vh.tvName.setCompoundDrawables(nav_left, null, null, null);
				vh.tvName.setCompoundDrawablePadding(5);
			}

			String substring = getString(from[0].substring(0, 1));
			if ("1".equals(substring)) {
				if (from[0].length() >= 2) {
					vh.tv_title.setText(from[0].substring(0, 2));
				} else if (from[0].length() == 1) {
					vh.tv_title.setText(from[0].substring(0, 1));
				}
			} else if ("e".equals(substring)) {
				if (from[0].length() >= 2) {
					String upperCase = from[0].substring(0, 1).toUpperCase();
					vh.tv_title.setText(upperCase + from[0].substring(1, 2));
				} else if (from[0].length() == 1) {
					vh.tv_title.setText(from[0].substring(0, 1).toUpperCase());
				}
			} else if ("c".equals(substring)) {
				vh.tv_title.setText(from[0].substring(0, 1));
			} else {
				vh.tv_title.setText("邮");
			}
		}
		vh.tvLaiYuan.setText(mb.getSubject());

		String today = getTodayData();

		// 切分日期，只显示月、日、时间
		String sendTime = mb.getSendTime();
		if ("未知".equals(sendTime)) {
			vh.tvTime.setText("未知");
		} else {
			String[] date = sendTime.split("年");
			String[] maildate = date[1].split(" ");
			if (today.equals(maildate[0])) {
				vh.tvTime.setText(maildate[1]);
			} else {
				int day = getTodayData_1();
				int mailDay = Integer.parseInt(maildate[0].substring(3, 5));
				if (day - mailDay == 1) {
					vh.tvTime.setText("昨天 " + maildate[1]);
				} else if (day - mailDay == 2) {
					vh.tvTime.setText("前天 " + maildate[1]);
				} else {
					vh.tvTime.setText(maildate[0]);
				}
			}
		}

		if (!"".equals(mb.getMailContent())) {
			vh.tv_content.setText(mb.getMailContent());
		} else {
			vh.tv_content.setText("(无摘要)");
		}

		return convertView;
	}

	class ViewHolder {
		TextView tvName;
		TextView tvLaiYuan;
		TextView tvTime;
		TextView tv_content, tv_title;
		ImageView image_biaoji2;

	}

	/**
	 * 判断是字母还是数字还是英文
	 */
	public String getString(String str) {
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(str);
		if (m.matches()) {// 输入的是数字 1-9
			return "1";
		}
		p = Pattern.compile("[a-zA-Z]");
		m = p.matcher(str);
		if (m.matches()) {// 输入的是字母 english
			return "e";
		}
		p = Pattern.compile("[\u4e00-\u9fa5]");
		m = p.matcher(str);
		if (m.matches()) {// 输入的是汉字 chinese
			return "c";
		}
		return "error";
	}

	// 获得今天日期
	private String getTodayData() {
		Calendar calendar = Calendar.getInstance();
		// String year = calendar.get(Calendar.YEAR) + "";
		String month = calendar.get(Calendar.MONTH) + 1 + "";
		String day = calendar.get(Calendar.DATE) + "";
		if (Integer.parseInt(month) < 10) {
			month = "0" + month;
		}
		if (Integer.parseInt(day) < 10) {
			day = "0" + day;
		}
		String data = month + "月" + day + "日";
		return data;
	}

	// 获得今天日期,只要day
	private int getTodayData_1() {
		Calendar calendar = Calendar.getInstance();
		String day = calendar.get(Calendar.DATE) + "";
		if (Integer.parseInt(day) < 10) {
			day = "0" + day;
		}
		return Integer.parseInt(day);
	}
}
