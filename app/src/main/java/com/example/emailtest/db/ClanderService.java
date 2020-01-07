package com.example.emailtest.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class ClanderService {

	private SqliteHelper helper;

	public ClanderService(Context ctx) {
		helper = new SqliteHelper(ctx);
	}

	public void addClander(HashMap<String, Object> hashMap) {
		SQLiteDatabase db = null;
		String sql = "insert into clander(title,startime,endtime,alltime,tixing,chongfu,address,comment,mailId,notepadId) values(?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {hashMap.get("title").toString(),hashMap.get("startime").toString(),hashMap.get("endtime").toString(),hashMap.get("alltime").toString(),
				hashMap.get("tixing").toString(),hashMap.get("chongfu").toString(),hashMap.get("address").toString(),
				hashMap.get("comment").toString(),hashMap.get("mailId").toString(),hashMap.get("notepadId").toString()};
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

	
	public List<HashMap<String, Object>> findClanderAll() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SQLiteDatabase db = null;
		String sql = "select * from clander";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id",cursor.getInt(cursor.getColumnIndex("id")));
				map.put("title",cursor.getString(cursor.getColumnIndex("title")));
				map.put("startime",cursor.getString(cursor.getColumnIndex("startime")));
				map.put("endtime",cursor.getString(cursor.getColumnIndex("endtime")));
				map.put("alltime",cursor.getString(cursor.getColumnIndex("alltime")));
				map.put("tixing",cursor.getString(cursor.getColumnIndex("tixing")));
				map.put("chongfu",cursor.getString(cursor.getColumnIndex("chongfu")));
				map.put("address",cursor.getString(cursor.getColumnIndex("address")));
				map.put("comment",cursor.getString(cursor.getColumnIndex("comment")));
				map.put("mailId",cursor.getString(cursor.getColumnIndex("mailId")));
				map.put("notepadId",cursor.getString(cursor.getColumnIndex("notepadId")));
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
	
	
	public List<HashMap<String, Object>> findClanderByTime(String datatime) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		SQLiteDatabase db = null;
		String sql = "select * from clander where jtype = ?";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] {datatime});
			while (cursor.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id",cursor.getInt(cursor.getColumnIndex("id")));
				map.put("title",cursor.getString(cursor.getColumnIndex("title")));
				map.put("startime",cursor.getString(cursor.getColumnIndex("startime")));
				map.put("endtime",cursor.getString(cursor.getColumnIndex("endtime")));
				map.put("alltime",cursor.getString(cursor.getColumnIndex("alltime")));
				map.put("tixing",cursor.getString(cursor.getColumnIndex("tixing")));
				map.put("chongfu",cursor.getString(cursor.getColumnIndex("chongfu")));
				map.put("address",cursor.getString(cursor.getColumnIndex("address")));
				map.put("comment",cursor.getString(cursor.getColumnIndex("comment")));
				map.put("mailId",cursor.getString(cursor.getColumnIndex("mailId")));
				map.put("notepadId",cursor.getString(cursor.getColumnIndex("notepadId")));
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
	
	
	public HashMap<String, Object> findClanderById(String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SQLiteDatabase db = null;
		String sql = "select * from clander where id = ?";
		try {
			db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { id });
			if(cursor.moveToNext()){
				map.put("id",cursor.getInt(cursor.getColumnIndex("id")));
				map.put("title",cursor.getString(cursor.getColumnIndex("title")));
				map.put("startime",cursor.getString(cursor.getColumnIndex("startime")));
				map.put("endtime",cursor.getString(cursor.getColumnIndex("endtime")));
				map.put("alltime",cursor.getString(cursor.getColumnIndex("alltime")));
				map.put("tixing",cursor.getString(cursor.getColumnIndex("tixing")));
				map.put("chongfu",cursor.getString(cursor.getColumnIndex("chongfu")));
				map.put("address",cursor.getString(cursor.getColumnIndex("address")));
				map.put("comment",cursor.getString(cursor.getColumnIndex("comment")));
				map.put("mailId",cursor.getString(cursor.getColumnIndex("mailId")));
				map.put("notepadId",cursor.getString(cursor.getColumnIndex("notepadId")));
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
	
	public void update(String id,String title,String startime,String endtime,String alltime,String tixing,String chongfu,
			String address,String comment,String mailId,String notepadId){
		SQLiteDatabase db = null;
		String sql = "update clander set title = ? ,startime = ?,endtime = ?,alltime = ?,tixing = ?,chongfu = ?,address = ?,comment = ?,mailId = ?,notepadId = ? where id = ?";
		Object[] params = {id,title,startime,endtime,alltime,tixing,chongfu,address,comment,mailId,notepadId};
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
	
	public void deleteClander(String id) {
		SQLiteDatabase db = null;
		String sql = "delete from clander where id = ?";

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
