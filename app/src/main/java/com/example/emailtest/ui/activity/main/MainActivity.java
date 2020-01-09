package com.example.emailtest.ui.activity.main;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailtest.R;
import com.example.emailtest.adapter.UserListOneAdapter;
import com.example.emailtest.adapter.UserListTwoAdapter;
import com.example.emailtest.db.UserService;
import com.example.emailtest.ui.activity.AllMailListActivity;
import com.example.emailtest.ui.activity.JiShiBenActivity;
import com.example.emailtest.ui.activity.MailListActivity;
import com.example.emailtest.ui.activity.MeActivity;
import com.example.emailtest.ui.activity.MoreActivity;
import com.example.emailtest.ui.activity.SettingActivity;
import com.example.emailtest.ui.activity.TongXunLuActivity;
import com.example.emailtest.ui.activity.WenJianActivity;
import com.example.emailtest.ui.activity.login.AddAccountActivity;
import com.example.emailtest.ui.activity.write.WriteEmailActivity;
import com.example.emailtest.entity.User;
import com.example.emailtest.utils.Stack;
import com.example.emailtest.view.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class MainActivity extends Activity implements OnClickListener {

    private NoScrollListView userListOne;
    private NoScrollListView userListTwo;
    private UserListOneAdapter oneAdapter;
    private UserListTwoAdapter twoAdapter;

    private List<User> list = new ArrayList<User>();
    private UserService userService;
    private PopupWindow popupmenu;

    private TextView tv_write;
    private TextView tv_write2;
    private TextView tv_upload;
    private TextView tv_set;
    private LinearLayout popMenu;

    private RelativeLayout edit_book, more_app, main_rl_adduser, all_mail, msg_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stack.addActivity(this);

        initPopMenu();

        userService = new UserService(this);

        initViews();


    }

    private void initViews() {
        popMenu = (LinearLayout) findViewById(R.id.pop_menu);
        popMenu.setOnClickListener(this);

        // 用户列表1
        userListOne = (NoScrollListView) findViewById(R.id.user_list_one);
        oneAdapter = new UserListOneAdapter(this, list);
        userListOne.setAdapter(oneAdapter);

        // 用户列表2
        userListTwo = (NoScrollListView) findViewById(R.id.user_list_two);
        twoAdapter = new UserListTwoAdapter(this, list);
        userListTwo.setAdapter(twoAdapter);

        msg_book = (RelativeLayout) findViewById(R.id.msg_book);
        msg_book.setOnClickListener(this);
        all_mail = (RelativeLayout) findViewById(R.id.all_mail);
        all_mail.setOnClickListener(this);
        edit_book = (RelativeLayout) findViewById(R.id.edit_book);
        more_app = (RelativeLayout) findViewById(R.id.more_app);
        edit_book.setOnClickListener(this);
        more_app.setOnClickListener(this);

        main_rl_adduser = (RelativeLayout) findViewById(R.id.main_rl_adduser);
        main_rl_adduser.setOnClickListener(this);

        userListOne.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = list.get(position);
                Intent intent = new Intent(MainActivity.this, MailListActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        userListTwo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = list.get(position);
                Intent intent = new Intent(MainActivity.this, MeActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(userService.findUserAll());
        oneAdapter.notifyDataSetChanged();
        twoAdapter.notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPopMenu() {
        //  获取自定义布局文件pop.xml的视图
        View view = getLayoutInflater().inflate(R.layout.pop_menu,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        if (popupmenu == null) {
            popupmenu = new PopupWindow(view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //设置点击pop外部时不响应外部按钮的点击事件
        popupmenu.setOutsideTouchable(true);
        popupmenu.setTouchable(true);
        popupmenu.setFocusable(true);
        //点击pop外部 pop会消失，如果不加这句，只加setFocusable时，有些机器不会消失，还被拦截onBackPress事件
        popupmenu.setBackgroundDrawable(new BitmapDrawable());
        //当PopupWindow消失后，恢复背景色
        popupmenu.setOnDismissListener(new PopUpWindowDismissListener());

        LinearLayout pop = (LinearLayout) view.findViewById(R.id.ll_menu);
        pop.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupmenu != null && popupmenu.isShowing()) {
                    popupmenu.dismiss();
                }
                return false;
            }
        });

        tv_write = (TextView) view.findViewById(R.id.tv_write);
        tv_write.setOnClickListener(this);
        tv_write2 = (TextView) view.findViewById(R.id.tv_write2);
        tv_write2.setOnClickListener(this);
        tv_upload = (TextView) view.findViewById(R.id.tv_upload);
        tv_upload.setOnClickListener(this);
        tv_set = (TextView) view.findViewById(R.id.tv_set);
        tv_set.setOnClickListener(this);

    }

    /**
     * PopUpWindow 消失监听器
     */
    private class PopUpWindowDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1.0f);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_menu: {
                if (popupmenu.isShowing()) {
                    popupmenu.dismiss();
                    backgroundAlpha(1.0f);
                } else {
                    popupmenu.showAsDropDown(v);
                    backgroundAlpha(0.7f);
                }
            }
            break;
            case R.id.tv_write: {
                backgroundAlpha(1.0f);
                startActivity(new Intent(MainActivity.this, WriteEmailActivity.class));
                popupmenu.dismiss();
            }
            break;

            case R.id.msg_book: {
                backgroundAlpha(1.0f);
                startActivity(new Intent(MainActivity.this, TongXunLuActivity.class));
                popupmenu.dismiss();
            }
            break;

            case R.id.all_mail: {
                backgroundAlpha(1.0f);
                startActivity(new Intent(MainActivity.this, AllMailListActivity.class));
                popupmenu.dismiss();
            }
            break;

            case R.id.tv_write2: {
                startActivity(new Intent(MainActivity.this, JiShiBenActivity.class));
                popupmenu.dismiss();
                backgroundAlpha(1.0f);
            }
            break;
            case R.id.tv_upload: {
                startActivity(new Intent(MainActivity.this, WenJianActivity.class));
                popupmenu.dismiss();
                backgroundAlpha(1.0f);
            }
            break;
            case R.id.tv_set: {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                backgroundAlpha(1.0f);
                popupmenu.dismiss();
            }
            break;

            case R.id.edit_book: {
                startActivity(new Intent(MainActivity.this, JiShiBenActivity.class));
            }
            break;

            case R.id.more_app: {
                startActivity(new Intent(MainActivity.this, MoreActivity.class));
            }
            break;
            case R.id.main_rl_adduser:
                startActivity(new Intent(MainActivity.this, AddAccountActivity.class));
                break;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }


    private long prev_ms;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curr_ms = System.currentTimeMillis();
            if (curr_ms - prev_ms > 3000) {
                Toast.makeText(this, "再按一次回退键退出程序", Toast.LENGTH_SHORT).show();
                prev_ms = curr_ms;
            } else {
                Stack.exit();
                MainActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
