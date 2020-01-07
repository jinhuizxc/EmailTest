package com.example.emailtest.mail.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ��ҳ���ܲ˵�GridView
 * @author xinjun
 *
 */
public class MyGridViewBut extends GridView { 
	public MyGridViewBut(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public MyGridViewBut(Context context) { 
        super(context); 
    } 

    public MyGridViewBut(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 

    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
    
}
