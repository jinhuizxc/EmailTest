package com.example.emailtest.email.clander;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailtest.R;
import com.example.emailtest.db.ClanderService;
import com.example.emailtest.mail.util.Stack;
import com.example.emailtest.view.NoScrollListView;
import com.example.emailtest.email.BuildActivity;


/**
 * 日历显示activity
 */
public class CalendarActivity extends Activity implements OnGestureListener {

    ClanderService clanderService;

    private GestureDetector gestureDetector = null;
    private CalendarAdapter calV = null;
    private GridView gridView = null;
    private TextView topText = null, left_img, right_img;
    private static int jumpMonth = 0;      //每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0;       //滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";

    ArrayList<HashMap<String, Object>> List = new ArrayList<HashMap<String, Object>>();
    NoScrollListView mListView;
    ImageAdapter grabAdapter = null;

    LinearLayout ll_no;

    public CalendarActivity() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);  //当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Stack.addActivity(this);
        clanderService = new ClanderService(this);

        ll_no = (LinearLayout) this.findViewById(R.id.ll_no);
        mListView = (NoScrollListView) this.findViewById(R.id.pinglunlist);

        //-------------------------//
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String strTime = year + "" + (month + 1) + day;
        int strTime2 = Integer.parseInt(strTime);

        ArrayList<HashMap<String, Object>> List2 = new ArrayList<HashMap<String, Object>>();
        List = (ArrayList<HashMap<String, Object>>) clanderService.findClanderAll();

        for (int i = 0; i < List.size(); i++) {
            int startime = Integer.parseInt(List.get(i).get("startime").toString());
            int endtime = Integer.parseInt(List.get(i).get("endtime").toString());
            if (strTime2 >= startime && strTime2 <= endtime) {
                HashMap<String, Object> map = List.get(i);
                List2.add(map);
                mListView.setVisibility(View.VISIBLE);
                ll_no.setVisibility(View.GONE);
            } else {
                mListView.setVisibility(View.GONE);
                ll_no.setVisibility(View.VISIBLE);
            }
        }
        grabAdapter = new ImageAdapter(getApplicationContext(), List2, CalendarActivity.this);
        mListView.setAdapter(grabAdapter);
        grabAdapter.notifyDataSetChanged();
        //-------------------------//

        gestureDetector = new GestureDetector(this);
        calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
        addGridView();
        gridView.setAdapter(calV);

        topText = (TextView) findViewById(R.id.tv_month);
        addTextToTopTextView(topText);

        left_img = (TextView) findViewById(R.id.left_img);
        right_img = (TextView) findViewById(R.id.right_img);
        right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalendarActivity.this, BuildActivity.class));
            }
        });
        left_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int gvFlag = 0;         //每次添加gridview到viewflipper中时给的标记
        if (e1.getX() - e2.getX() > 120) {
            //像左滑动
            addGridView();   //添加一个gridView
            jumpMonth++;     //下一个月

            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
            gridView.setAdapter(calV);
            addTextToTopTextView(topText);
            gvFlag++;

            return true;
        } else if (e1.getX() - e2.getX() < -120) {
            //向右滑动
            addGridView();   //添加一个gridView
            jumpMonth--;     //上一个月

            calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
            gridView.setAdapter(calV);
            gvFlag++;
            addTextToTopTextView(topText);

            return true;
        }
        return false;
    }


    /**
     * 创建菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, menu.FIRST, menu.FIRST, "今天");
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 选择菜单
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                //跳转到今天
                int xMonth = jumpMonth;
                int xYear = jumpYear;
                int gvFlag = 0;
                jumpMonth = 0;
                jumpYear = 0;
                addGridView();   //添加一个gridView
                year_c = Integer.parseInt(currentDate.split("-")[0]);
                month_c = Integer.parseInt(currentDate.split("-")[1]);
                day_c = Integer.parseInt(currentDate.split("-")[2]);
                calV = new CalendarAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c);
                gridView.setAdapter(calV);
                addTextToTopTextView(topText);
                gvFlag++;

                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return this.gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    //添加头部的年份 闰哪月等信息
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        textDate.append(calV.getShowYear()).append("年").append(
                calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
        view.setTypeface(Typeface.DEFAULT_BOLD);
    }

    //添加gridview
    private void addGridView() {

        gridView = (GridView) findViewById(R.id.gridview);

        gridView.setOnTouchListener(new OnTouchListener() {
            //将gridview中的触摸事件回传给gestureDetector
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return CalendarActivity.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {
            //gridView中的每一个item的点击事件

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                String str2 = "";
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0];  //这一天的阳历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    str2 = scheduleYear + "-" + scheduleMonth + "-" + scheduleDay;

                    List.clear();
                    ArrayList<HashMap<String, Object>> List2 = new ArrayList<HashMap<String, Object>>();
                    String strTime = scheduleYear + "" + scheduleMonth + scheduleDay;
                    int strTime2 = Integer.parseInt(strTime);
                    List = (ArrayList<HashMap<String, Object>>) clanderService.findClanderAll();

                    for (int i = 0; i < List.size(); i++) {
                        int startime = Integer.parseInt(List.get(i).get("startime").toString());
                        int endtime = Integer.parseInt(List.get(i).get("endtime").toString());
                        if (strTime2 >= startime && strTime2 <= endtime) {
                            HashMap<String, Object> map = List.get(i);
                            List2.add(map);
                            mListView.setVisibility(View.VISIBLE);
                            ll_no.setVisibility(View.GONE);
                        } else {
                            mListView.setVisibility(View.GONE);
                            ll_no.setVisibility(View.VISIBLE);
                        }
                    }

                    grabAdapter = new ImageAdapter(getApplicationContext(), List2, CalendarActivity.this);
                    mListView.setAdapter(grabAdapter);
                    grabAdapter.notifyDataSetChanged();


                    Toast.makeText(CalendarActivity.this, str2, Toast.LENGTH_SHORT).show();
                }


                int count = arg0.getCount();
                for (int i = 0; i < count; i++) {
                    if (i == position) {
                        RelativeLayout ll_main = (RelativeLayout) arg0.getChildAt(i).findViewById(R.id.ll_main);
                        ll_main.setBackgroundColor(Color.parseColor("#138cdf"));

                        TextView textView = (TextView) arg0.getChildAt(i).findViewById(R.id.tvtext);
                        textView.setTextColor(Color.WHITE);

                    } else {
                        RelativeLayout ll_main = (RelativeLayout) arg0.getChildAt(i).findViewById(R.id.ll_main);
                        ll_main.setBackgroundColor(Color.parseColor("#ffffff"));

                        TextView textView = (TextView) arg0.getChildAt(i).findViewById(R.id.tvtext);
                        textView.setTextColor(Color.parseColor("#000000"));

                    }
                }

            }

        });
    }

    public class ImageAdapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> map = null;
        Context context;
        Activity pinglun;

        public ImageAdapter(Context context, ArrayList<HashMap<String, Object>> map, Activity pinglun) {
            this.map = map;
            this.context = context;
            this.pinglun = pinglun;
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
                        R.layout.item_3, null);
                holder = new ViewHolder();
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_com = (TextView) convertView.findViewById(R.id.tv_com);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, Object> mapItem = map.get(position);
//	 		holder.tv_time.setText(mapItem.get("time").toString());
            holder.tv_time.setVisibility(View.GONE);
            holder.tv_title.setText(mapItem.get("title").toString());
            holder.tv_com.setText(mapItem.get("comment").toString());
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
            TextView tv_time, tv_title, tv_com;
        }
    }

}