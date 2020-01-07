package com.example.emailtest.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class NotepadService {

	private SqliteHelper helper;

	public NotepadService(Context ctx) {
		helper = new SqliteHelper(ctx);
	}

	public void addMail(HashMap<String, Object> hashMap) {
		SQLiteDatabase db = null;
		String sql = "insert into notepad(title,com,datatime,flieMath,jtype,mailId) values(?,?,?,?,?,?)";
		Object[] params = {
				hashMap.get("title").toString(),hashMap.get("com").toString(),
				hashMap.get("datatime").toString(),hashMap.get("flieMath").toString(),
				hashMap.get("jtype").toString(),hashMap.get("mailId").toString()};
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
	
	
	public List<HashMap<String, Object>> findNoteAll() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SQLiteDatabase db = null;
		String sql = "select * from notepad order by datatime desc";   //按照时间排序
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id",cursor.getInt(cursor.getColumnIndex("id")));
				map.put("title",cursor.getString(cursor.getColumnIndex("title")));
				map.put("com",cursor.getString(cursor.getColumnIndex("com")));
				map.put("datatime",cursor.getString(cursor.getColumnIndex("datatime")));
				map.put("flieMath",cursor.getString(cursor.getColumnIndex("flieMath")));
				map.put("jtype",cursor.getString(cursor.getColumnIndex("jtype")));
				map.put("mailId",cursor.getString(cursor.getColumnIndex("mailId")));
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
	
	public List<HashMap<String, Object>> findNoteByType(String jtype) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SQLiteDatabase db = null;
		String sql = "select * from notepad where jtype = ?";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] { jtype });
			while (cursor.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id",cursor.getInt(cursor.getColumnIndex("id")));
				map.put("title",cursor.getString(cursor.getColumnIndex("title")));
				map.put("com",cursor.getString(cursor.getColumnIndex("com")));
				map.put("datatime",cursor.getString(cursor.getColumnIndex("datatime")));
				map.put("flieMath",cursor.getString(cursor.getColumnIndex("flieMath")));
				map.put("jtype",cursor.getString(cursor.getColumnIndex("jtype")));
				map.put("mailId",cursor.getString(cursor.getColumnIndex("mailId")));
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
	
	
	public HashMap<String, Object> findNoteById(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SQLiteDatabase db = null;
		String sql = "select * from notepad where id = ?";
		try {
			db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { id });
			if(cursor.moveToNext()){
				map.put("id",cursor.getInt(cursor.getColumnIndex("id")));
				map.put("title",cursor.getString(cursor.getColumnIndex("title")));
				map.put("com",cursor.getString(cursor.getColumnIndex("com")));
				map.put("datatime",cursor.getString(cursor.getColumnIndex("datatime")));
				map.put("flieMath",cursor.getString(cursor.getColumnIndex("flieMath")));
				map.put("jtype",cursor.getString(cursor.getColumnIndex("jtype")));
				map.put("mailId",cursor.getString(cursor.getColumnIndex("mailId")));
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
	
	
	public void update(String id , String jtype){
		SQLiteDatabase db = null;
		String sql = "update notepad set jtype = ? where id = ?";
		Object[] params = {id,jtype};
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
	
//	title,com,datatime,flieMath,jtype,mailId
	public void updateDetile(String title ,String datetime ,String flieMath ,String id){
		SQLiteDatabase db = null;
		String sql = "update notepad set title = ? , datatime = ?,flieMath = ? where id = ?";
		Object[] params = {title,datetime,flieMath,id};
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
	
	public void deleteNote(String id) {
		SQLiteDatabase db = null;
		String sql = "delete from notepad where id = ?";

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
