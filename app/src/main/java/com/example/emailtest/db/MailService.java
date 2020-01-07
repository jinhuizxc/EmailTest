package com.example.emailtest.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.emailtest.entity.MailBrief;

import java.util.ArrayList;
import java.util.List;

public class MailService {

	private SqliteHelper helper;

	public MailService(Context ctx) {
		helper = new SqliteHelper(ctx);
	}

	public void addMail(MailBrief mail) {
		SQLiteDatabase db = null;
		
		String sql = "insert into mail(fromPersonal,subject,mailContent,malieId,flieMath,sendTime,mailType,UserId,Mindex,TOO,CC,BCC) values(?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {mail.getFromPersonal(),mail.getSubject(),mail.getMailContent(),
				mail.getMalieId(),mail.getFlieMath(),mail.getSendTime(),
				mail.getMailType(),mail.getUserId(),mail.getMindex(),mail.getTOO(),
				mail.getCC(),mail.getBCC()};
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

	public List<MailBrief> findMailAll() {
		List<MailBrief> list = new ArrayList<MailBrief>();
		SQLiteDatabase db = null;
		String sql = "select * from mail where mailType!=5 ";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				MailBrief mail = new MailBrief();
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String fromPersonal = cursor.getString(cursor.getColumnIndex("fromPersonal"));
				String subject = cursor.getString(cursor.getColumnIndex("subject"));
				String mailContent = cursor.getString(cursor.getColumnIndex("mailContent"));
				String malieId = cursor.getString(cursor.getColumnIndex("malieId"));
				String flieMath = cursor.getString(cursor.getColumnIndex("flieMath"));
				String sendTime = cursor.getString(cursor.getColumnIndex("sendTime"));
				String mailType = cursor.getString(cursor.getColumnIndex("mailType"));
				String UserId = cursor.getString(cursor.getColumnIndex("UserId"));
				String Mindex = cursor.getString(cursor.getColumnIndex("Mindex"));
				String TO = cursor.getString(cursor.getColumnIndex("TOO"));
				String CC = cursor.getString(cursor.getColumnIndex("CC"));
				String BCC = cursor.getString(cursor.getColumnIndex("BCC"));
				mail.setId(id);mail.setFromPersonal(fromPersonal);mail.setSubject(subject);mail.setMailContent(mailContent);
				mail.setMalieId(malieId);mail.setFlieMath(flieMath);mail.setSendTime(sendTime);
				mail.setMailType(mailType);mail.setUserId(UserId);mail.setMindex(Mindex);mail.setTOO(TO);mail.setCC(CC);
				mail.setBCC(BCC);
				list.add(mail);
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
	
	public List<MailBrief> findMailByMailType(String mailtype,String UserId) {
		List<MailBrief> list = new ArrayList<MailBrief>();
		SQLiteDatabase db = null;
		String sql = "select * from mail where mailType = ? and UserId = ?";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] { mailtype ,UserId });
			while (cursor.moveToNext()) {
				MailBrief mail = new MailBrief();
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String fromPersonal = cursor.getString(cursor.getColumnIndex("fromPersonal"));
				String subject = cursor.getString(cursor.getColumnIndex("subject"));
				String mailContent = cursor.getString(cursor.getColumnIndex("mailContent"));
				String malieId = cursor.getString(cursor.getColumnIndex("malieId"));
				String flieMath = cursor.getString(cursor.getColumnIndex("flieMath"));
				String sendTime = cursor.getString(cursor.getColumnIndex("sendTime"));
				String mailType = cursor.getString(cursor.getColumnIndex("mailType"));
				String userid = cursor.getString(cursor.getColumnIndex("UserId"));
				String TO = cursor.getString(cursor.getColumnIndex("TOO"));
				String Mindex = cursor.getString(cursor.getColumnIndex("Mindex"));
				String CC = cursor.getString(cursor.getColumnIndex("CC"));
				String BCC = cursor.getString(cursor.getColumnIndex("BCC"));
				mail.setId(id);mail.setFromPersonal(fromPersonal);mail.setSubject(subject);mail.setMailContent(mailContent);
				mail.setMalieId(malieId);mail.setFlieMath(flieMath);mail.setSendTime(sendTime);
				mail.setMailType(mailType);mail.setUserId(userid);mail.setMindex(Mindex);mail.setTOO(TO);mail.setCC(CC);
				mail.setBCC(BCC);
				list.add(mail);
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
	
	
	public List<MailBrief> findMailByUserId(String UserId) {
		List<MailBrief> list = new ArrayList<MailBrief>();
		SQLiteDatabase db = null;
		String sql = "select * from mail where mailType!=5 and UserId = ?  order by id desc";
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery(sql, new String[] { UserId });
			while (cursor.moveToNext()) {
				MailBrief mail = new MailBrief();
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String fromPersonal = cursor.getString(cursor.getColumnIndex("fromPersonal"));
				String subject = cursor.getString(cursor.getColumnIndex("subject"));
				String mailContent = cursor.getString(cursor.getColumnIndex("mailContent"));
				String malieId = cursor.getString(cursor.getColumnIndex("malieId"));
				String flieMath = cursor.getString(cursor.getColumnIndex("flieMath"));
				String sendTime = cursor.getString(cursor.getColumnIndex("sendTime"));
				String mailType = cursor.getString(cursor.getColumnIndex("mailType"));
				String userid = cursor.getString(cursor.getColumnIndex("UserId"));
				String Mindex = cursor.getString(cursor.getColumnIndex("Mindex"));
				String TO = cursor.getString(cursor.getColumnIndex("TOO"));
				String CC = cursor.getString(cursor.getColumnIndex("CC"));
				String BCC = cursor.getString(cursor.getColumnIndex("BCC"));
				mail.setId(id);mail.setFromPersonal(fromPersonal);mail.setSubject(subject);mail.setMailContent(mailContent);
				mail.setMalieId(malieId);mail.setFlieMath(flieMath);mail.setSendTime(sendTime);
				mail.setMailType(mailType);mail.setUserId(userid);mail.setMindex(Mindex);mail.setTOO(TO);mail.setCC(CC);
				mail.setBCC(BCC);
				list.add(mail);
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
	
	
	public MailBrief findById(String id) {
		MailBrief mail = new MailBrief();
		SQLiteDatabase db = null;
		String sql = "select * from mail where id = ?";
		try {
			db = helper.getReadableDatabase();
			Cursor cursor = db.rawQuery(sql, new String[] { id });
			if(cursor.moveToNext()){
			mail.setId(cursor.getString(cursor.getColumnIndex("id")));
			mail.setFlieMath(cursor.getString(cursor.getColumnIndex("flieMath")));
			mail.setFromPersonal(cursor.getString(cursor.getColumnIndex("fromPersonal")));
			mail.setMailType(cursor.getString(cursor.getColumnIndex("mailType")));
			mail.setMalieId(cursor.getString(cursor.getColumnIndex("malieId")));
			mail.setSendTime(cursor.getString(cursor.getColumnIndex("sendTime")));
			mail.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
			mail.setMailContent(cursor.getString(cursor.getColumnIndex("mailContent")));
			mail.setUserId(cursor.getString(cursor.getColumnIndex("UserId")));
			mail.setMindex(cursor.getString(cursor.getColumnIndex("Mindex")));
			mail.setTOO(cursor.getString(cursor.getColumnIndex("TOO")));
			mail.setCC(cursor.getString(cursor.getColumnIndex("CC")));
			mail.setBCC(cursor.getString(cursor.getColumnIndex("BCC")));
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return mail;
	}
	
	
	public void updateTypeById(String id , String mailType){
		SQLiteDatabase db = null;
		String sql = "update mail set mailType = ? where id = ?";
		Object[] params = {mailType,id};
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
	
	public void updateFileById(String id , String flieMath){
		SQLiteDatabase db = null;
		String sql = "update mail set flieMath = ? where id = ?";
		Object[] params = {flieMath,id};
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
	
	public void deleteMail(String id) {
		SQLiteDatabase db = null;
		String sql = "delete from mail where id = ?";

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
	
	
	public void deleteMailByUserId(String UserId) {
		SQLiteDatabase db = null;
		String sql = "delete from mail where UserId = ?";

		try {
			db = helper.getWritableDatabase();
			db.execSQL(sql, new Object[] { UserId });
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
