package com.example.emailtest.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class FileService {

	private SqliteHelper helper;

	public FileService(Context ctx) {
		helper = new SqliteHelper(ctx);
	}
	

	public void addFile(HashMap<String, Object> hashMap) {
		SQLiteDatabase db = null;
		String sql = "insert into fileMain(filepath,filesize,datatime) values(?,?,?)";
		Object[] params = {hashMap.get("filepath").toString(),hashMap.get("filesize").toString(),hashMap.get("datatime").toString()};
		try {
			db = helper.getWritableDatabase();
			db.execSQL(sql, params);
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public List<HashMap<String, Object>> findFileAll() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SQLiteDatabase db = null;
		String sql = "select * from fileMain";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id",cursor.getInt(cursor.getColumnIndex("id")));
				map.put("filepath",cursor.getString(cursor.getColumnIndex("filepath")));
				map.put("filesize",cursor.getString(cursor.getColumnIndex("filesize")));
				map.put("datatime",cursor.getString(cursor.getColumnIndex("datatime")));
				list.add(map);
			}
			cursor.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return list;
	}
	
	public HashMap<String, Object> findFileById(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SQLiteDatabase db = null;
		String sql = "select * from fileMain where id = ?";
		try {
			db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { id });
			if(cursor.moveToNext()){
				map.put("id",cursor.getInt(cursor.getColumnIndex("id")));
				map.put("filepath",cursor.getString(cursor.getColumnIndex("filepath")));
				map.put("filesize",cursor.getString(cursor.getColumnIndex("filesize")));
				map.put("datatime",cursor.getString(cursor.getColumnIndex("datatime")));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return map;
	}
	
	public void deleteFile(String id) {
		SQLiteDatabase db = null;
		String sql = "delete from fileMain where id = ?";

		try {
			db = helper.getWritableDatabase();
			db.execSQL(sql, new Object[] { id });
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}

	}
	
	
	
	
}
