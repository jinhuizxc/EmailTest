package com.example.emailtest.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.emailtest.R;

public class AttachmentListAdapter extends BaseAdapter {

    private List<String> list;
    private LayoutInflater inflater;

    public AttachmentListAdapter(Context context, List<String> list) {
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.attachment_list_item, parent, false);
            vh = new ViewHolder();
            vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvName.setText(list.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
    }
}
