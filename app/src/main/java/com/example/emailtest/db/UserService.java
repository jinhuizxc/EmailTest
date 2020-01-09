package com.example.emailtest.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.emailtest.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

	private SqliteHelper helper;

	public UserService(Context context) {
		helper = new SqliteHelper(context);
	}

	/**
	 * 添加用户
	 * @param user
	 */
	public void addUser(User user) {
		SQLiteDatabase db = null;
		String sql = "insert into mailUser(username,pwd,ismain) values(?,?,?)";
		Object[] params = { user.getUsername(),user.getPwd(),user.getIsmain()};
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

	/**
	 * 查询用户
	 * @return
	 */
	public List<User> findUserAll() {
		List<User> list = new ArrayList<User>();
		SQLiteDatabase db = null;
		String sql = "select * from mailUser";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				User user = new User();
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String username = cursor.getString(cursor.getColumnIndex("username"));
				String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
				String ismain = cursor.getString(cursor.getColumnIndex("ismain"));
				user.setId(id);
				user.setUsername(username);
				user.setPwd(pwd);
				user.setIsmain(ismain);
				list.add(user);
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

	/**
	 * 是否存在该用户
	 * @param username
	 * @return
	 */
	public boolean hasUser(String username) {
		boolean has = false;
		SQLiteDatabase db = null;
		String sql = "select * from mailUser where username = ?";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[]{username});
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

	/**
	 * 更新用户名
	 * @param username
	 */
	public void update(String username){
		SQLiteDatabase db = null;
		String sql = "update mailUser set ismain = 1 where username = ?";
		Object[] params = {username};
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
	
	public void update2(){
		SQLiteDatabase db = null;
		String sql = "update mailUser set ismain = 0 ";
		Object[] params = {};
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

	/**
	 * 删除用户
	 * @param username
	 */
	public void deleteUser(String username) {
		SQLiteDatabase db = null;
		String sql = "delete from mailUser where username = ?";

		try {
			db = helper.getWritableDatabase();
			db.execSQL(sql, new Object[] { username });
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}

	}
	
	
	public User findByUsername(String username) {
		User user = new User();
		SQLiteDatabase db = null;
		String sql = "select * from mailUser where username = ?";
		try {
			db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { username });
			if(cursor.moveToNext()){
				user.setId(cursor.getInt(cursor.getColumnIndex("id")));
				user.setIsmain(cursor.getString(cursor.getColumnIndex("ismain")));
				user.setPwd(cursor.getString(cursor.getColumnIndex("pwd")));
				user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return user;
	}
	
	
	public User findByUsername2() {
		User user = new User();
		SQLiteDatabase db = null;
		String sql = "select * from mailUser where  ismain=1";
		try {
			db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { });
			if(cursor.moveToNext()){
				user.setId(cursor.getInt(cursor.getColumnIndex("id")));
				user.setIsmain(cursor.getString(cursor.getColumnIndex("ismain")));
				user.setPwd(cursor.getString(cursor.getColumnIndex("pwd")));
				user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return user;
	}
	
	public User findByUserId(String id) {
		User user = new User();
		SQLiteDatabase db = null;
		String sql = "select * from mailUser where id = ?";
		try {
			db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { id });
			if(cursor.moveToNext()){
				user.setId(cursor.getInt(cursor.getColumnIndex("id")));
				user.setIsmain(cursor.getString(cursor.getColumnIndex("ismain")));
				user.setPwd(cursor.getString(cursor.getColumnIndex("pwd")));
				user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return user;
	}
	
	
}
