package com.example.emailtest.test;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.emailtest.R;

public class TestActivity extends AppCompatActivity {

    private RelativeLayout mRootView;
    private LinearLayout bottomView;
    private EditText editText;

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        View decorView = getWindow().getDecorView();
//        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
//        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));

        editText = findViewById(R.id.etContent);
        mRootView = findViewById(R.id.mRootView);
        bottomView = findViewById(R.id.ll_bottomView);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputManager(editText);
            }
        });

    }

    private void showInputManager(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        /** 目前测试来看，还是挺准的
         * 原理：OnGlobalLayoutListener 每次布局变化时都会调用
         * 界面view 显示消失都会调用，软键盘显示与消失时都调用
         * */
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
        InputMethodManager inputManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

    }

    ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Log.e(TAG, "onGlobalLayout: ");
            //判断窗口可见区域大小
            Rect rect = new Rect();
            // getWindowVisibleDisplayFrame()会返回窗口的可见区域高度
//            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            mRootView.getWindowVisibleDisplayFrame(rect);//获取布局的可视区域
            //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
            int keyboardHeight = mRootView.getHeight() - (rect.bottom - rect.top);
            Log.e(TAG, "onGlobalLayout ScreenHeight: " + mRootView.getHeight());
            Log.e(TAG, "onGlobalLayout: heightDifference: " + keyboardHeight + " , bottom: " + rect.bottom + " , top: " + rect.top);
            // onGlobalLayout ScreenHeight: 2070
            // onGlobalLayout: heightDifference: 892 , bottom: 1238 , top: 60
            boolean isKeyboardShowing = keyboardHeight > getScreenHeight() / 3;
            if (isKeyboardShowing) {
//                D.i("slack","show..."+ r.bottom + " - " + r.top + " = " + (r.bottom - r.top) +","+ heightDifference);
                // bottomView 需要跟随软键盘移动的布局
                // setDuration(0) 默认300, 设置 0 ，表示动画执行时间为0，没有过程，只有动画结果了
                bottomView.animate().translationY(-keyboardHeight).setDuration(0).start();
            } else {
//                D.i("slack","hide...");
                bottomView.animate().translationY(0).start();
            }
        }
    };

    private int getScreenHeight() {
        return getWindowManager().getDefaultDisplay().getHeight();
    }


    /*private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //判断窗口可见区域大小
                Rect r = new Rect();
                // getWindowVisibleDisplayFrame()会返回窗口的可见区域高度
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }*/


}
