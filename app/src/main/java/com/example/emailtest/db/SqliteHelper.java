package com.example.emailtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "Mail.db";
	private static final int DB_VERSION = 22;
	
	// ismain是否为主邮箱  , host 服务器的IP ,port 服务器端口 ,session
	private static String user = "create table mailUser(id integer primary key autoincrement, username varchar(255), pwd varchar(255),ismain varchar(255))";
	
	// fromPersonal 发件人   ， subject 邮件主题   ，mailContent 邮件内容 ， sendTime 发件时间  ， malieId 邮件ID  ，flieMath 附件的路径  , mailType邮件类型（  1 已读  ， 2 星标邮件     3 草稿    ， 4 已发送  ，5 已删除   ，  6 未读   7 收件箱    ） , UserId 用户id,"TO"--收件人地址， "CC"--抄送人地址 ，"BCC"--密送人地址
	private static String mail = "create table mail(id integer primary key autoincrement, fromPersonal varchar(255), subject varchar(255),mailContent varchar(255),sendTime varchar(255),malieId varchar(255),flieMath text,mailType varchar(255),UserId varchar(255),Mindex varchar(255),TOO varchar(255),CC varchar(255),BCC varchar(255))";
	
	// title 标题   ， startime 开始时间   ， endtime 结束时间  ， alltime 是否全天（  0  否   ，  1 是）  ，tixing （1 无提醒，  2  事件发生时  ，  3  五分钟前   ，  4 15分钟前   ，  5 一小时前  ，  6  一天前）  , chongfu（1 永不，    2  每天   ，  3 工作日   ，  4 每周  ， 5 每月  ，  6 每年），  address  地址   ，comment  备注  ，  mailId  邮件的id
	private static String clander = "create table clander(id integer primary key autoincrement, title varchar(255), startime varchar(255),endtime varchar(255),alltime varchar(255),tixing varchar(255),chongfu varchar(255),address varchar(255),comment varchar(255),mailId varchar(255),notepadId varchar(255))";
	
	// title 标题   ， datatime 创建时间   ， flieMath 附件地址  ， jtype 类型（  1  星标   ，  2 未分类   3  日记   ， 4书签）  ，mailId 邮件的id
	private static String notepad = "create table notepad(id integer primary key autoincrement, title varchar(255), com varchar(255), datatime varchar(255) ,flieMath text , jtype varchar(255) ,mailId varchar(255))";
	
	// filepath 文件地址  , filesize 文件大小 ,datatime 文件上传时间
	private static String filePath = "create table fileMain(id integer primary key autoincrement, filepath varchar(255), filesize varchar(255),datatime varchar(255))";
	
	// names 姓名  , numbers 邮件账号
	private static String commu = "create table commu(id integer primary key autoincrement, names varchar(255), numbers varchar(255))";
	
	public SqliteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(user);
		db.execSQL(mail);
		db.execSQL(clander);
		db.execSQL(notepad);
		db.execSQL(filePath);
		db.execSQL(commu);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(user);
		db.execSQL(mail);
		db.execSQL(clander);
		db.execSQL(notepad);
		db.execSQL(filePath);
		db.execSQL(commu);
	}

	public void close(SQLiteDatabase db){
		if(db != null && db.isOpen()){
			db.close();
		}
	}
	
}
