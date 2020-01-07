package com.example.emailtest.mail.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

public class Stack {

	private static List<Activity> activityList = new LinkedList<Activity>();

	private Stack() {
	}

	// 添加Activity到容器中
	public static void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public static void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		// System.exit(0);
	}

}
