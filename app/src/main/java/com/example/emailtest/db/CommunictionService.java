package com.example.emailtest.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.emailtest.entity.Communication;

import java.util.ArrayList;
import java.util.List;

public class CommunictionService {

	private SqliteHelper helper;

	public CommunictionService(Context ctx) {
		helper = new SqliteHelper(ctx);
	}

	public void addCommunication(Communication com) {
		SQLiteDatabase db = null;
		String sql = "insert into commu(names,numbers) values(?,?)";
		Object[] params = { com.getNames(),com.getNumbers()};
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

	public List<Communication> findComAll() {
		List<Communication> list = new ArrayList<Communication>();
		SQLiteDatabase db = null;
		String sql = "select * from commu";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				Communication com = new Communication();
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String names = cursor.getString(cursor.getColumnIndex("names"));
				String numbers = cursor.getString(cursor.getColumnIndex("numbers"));
				com.setId(id);com.setNames(names);com.setNumbers(numbers);
				list.add(com);
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
	
	public boolean hasCom(String numbers) {
		boolean has = false;
		SQLiteDatabase db = null;
		String sql = "select * from commu where numbers = ?";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[]{numbers});
			if(cursor.getCount() > 0){
				has = true;
			}else{
				has = false;
			}
			
			cursor.close();
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return has;
	}
	
	
	
	public void deleteCom(String id) {
		SQLiteDatabase db = null;
		String sql = "delete from commu where id = ?";

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
	
	
	public Communication findComById(String id) {
		Communication com = new Communication();
		SQLiteDatabase db = null;
		String sql = "select * from commu where id = ?";
		try {
			db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { id });
			if(cursor.moveToNext()){
				com.setId(cursor.getInt(cursor.getColumnIndex("id")));
				com.setNames(cursor.getString(cursor.getColumnIndex("names")));
				com.setNumbers(cursor.getString(cursor.getColumnIndex("pwd")));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return com;
	}
	
	
}
