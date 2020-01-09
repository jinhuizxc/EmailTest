package com.example.emailtest.ui.activity.welcome;


import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.emailtest.R;
import com.example.emailtest.db.UserService;
import com.example.emailtest.ui.activity.main.MainActivity;
import com.example.emailtest.ui.activity.login.AddAccountActivity;
import com.example.emailtest.entity.User;
import com.example.emailtest.utils.Stack;

public class WelcomeActivity extends Activity {

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Stack.addActivity(this);

        // 初始化数据库
        userService = new UserService(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<User> userList = userService.findUserAll();
                if (userList.size() > 0) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(WelcomeActivity.this, AddAccountActivity.class));
                    finish();
                }

            }
        }, 3000);
    }
}
