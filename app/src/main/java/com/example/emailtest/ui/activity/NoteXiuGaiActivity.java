package com.example.emailtest.ui.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailtest.R;
import com.example.emailtest.db.MailService;
import com.example.emailtest.db.NotepadService;
import com.example.emailtest.db.UserService;
import com.example.emailtest.entity.MailBrief;
import com.example.emailtest.utils.Attachment;
import com.example.emailtest.adapter.GridViewAdapter;
import com.example.emailtest.utils.MyGridView;
import com.example.emailtest.utils.Stack;


public class NoteXiuGaiActivity extends Activity implements OnClickListener{
	
	ImageView image_phone,image_pic,image_number;
	EditText ed_com;
	Button image_quexiao,image_send;
	LinearLayout ll_caozuo;
	TextView tv_datetime,tv_fajianren,tv_time,tv_shoujianren,tv_zhuti;
	private MyGridView gridView;
	private GridViewAdapter<Attachment> adapter = null;
	private ArrayList<String> lists = new ArrayList<String>(); 
	
	private int btn1 = R.drawable.il;
	String path2="";int reqCode = 100;
	
	private NotepadService note ;
	private MailService mailService ;
	private UserService userService ;
	private MailBrief mail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jishiben_xiugai);
		Stack.addActivity(this);
		note = new NotepadService(this);
		mailService = new MailService(this);
		userService = new UserService(this);
		
		if(getIntent().getStringExtra("NoteId")!=null){
			mail = mailService.findById(note.findNoteById(getIntent().getStringExtra("NoteId")).get("mailId").toString());
		}
		
		
		
		ed_com =  (EditText) this.findViewById(R.id.ed_com);
		
		tv_datetime =  (TextView) this.findViewById(R.id.tv_datetime);
		tv_fajianren =  (TextView) this.findViewById(R.id.tv_fajianren);
		tv_time =  (TextView) this.findViewById(R.id.tv_time);
		tv_shoujianren =  (TextView) this.findViewById(R.id.tv_shoujianren);
		tv_zhuti =  (TextView) this.findViewById(R.id.tv_zhuti);
		
		tv_fajianren.setText(mail.getFromPersonal());
		tv_time.setText(mail.getSendTime());
		if(mail.getUserId()!=null){
			tv_shoujianren.setText(userService.findByUserId(mail.getUserId()).getUsername());
		}
		tv_zhuti.setText(note.findNoteById(getIntent().getStringExtra("NoteId")).get("title").toString());
		ed_com.setText(note.findNoteById(getIntent().getStringExtra("NoteId")).get("title").toString());
		
		image_quexiao  =  (Button) this.findViewById(R.id.image_quexiao);
		image_send  =  (Button) this.findViewById(R.id.image_send);
		
		image_number  =  (ImageView) this.findViewById(R.id.image_number);
		image_phone  =  (ImageView) this.findViewById(R.id.image_phone);
		image_pic  =  (ImageView) this.findViewById(R.id.image_pic);
		
		ll_caozuo =  (LinearLayout) this.findViewById(R.id.ll_caozuo);
		
		gridView=(MyGridView) findViewById(R.id.pre_view);
		adapter = new GridViewAdapter<Attachment>(this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new MyOnItemClickListener());
		
		image_quexiao.setOnClickListener(this);image_send.setOnClickListener(this);
		image_phone.setOnClickListener(this);image_pic.setOnClickListener(this);
		image_number.setOnClickListener(this);
		
		String str = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date());
		tv_datetime.setText(str);   
		
		String  filePath = note.findNoteById(getIntent().getStringExtra("NoteId")).get("flieMath").toString();
		if(filePath.length()>0){
			ll_caozuo.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.VISIBLE);
			String[] filePathArry = filePath.split(",");
			for(int i=0;i<filePathArry.length;i++){
				lists.add(filePathArry[i]);
				Attachment affInfos = Attachment.GetFileInfo(filePathArry[i]);
				adapter.appendToList(affInfos);
				int a = adapter.getList().size();
				int count = (int) Math.ceil(a / 4.0);
				gridView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						(int) (94 * 1.5 * count)));
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_quexiao:
			finish();
		break;
		case R.id.image_send:
			StringBuffer stringBuffer  = new StringBuffer();
			for(int i=0;i<lists.size();i++){
				stringBuffer.append(lists.get(i)+",");
			}
			String str = stringBuffer.toString();
			String str2 = str.substring(0, str.length()-1);
			String datetime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date());
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("title",ed_com.getText().toString());
			hashMap.put("flieMath", str2);
			hashMap.put("datetime", datetime);
			hashMap.put("id",getIntent().getStringExtra("NoteId"));
			note.updateDetile(ed_com.getText().toString(),datetime,str2,getIntent().getStringExtra("NoteId"));
			finish();
		break;
		case R.id.image_phone:
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
				path2 = getFileName();
				getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path2)));  
				startActivityForResult(getImageByCamera, 0);
			} else {
				Toast.makeText(NoteXiuGaiActivity.this, "请插入SD卡",Toast.LENGTH_LONG).show();
			}
		break;
		case R.id.image_pic:
			Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(getImage, 1);
		break;
		case R.id.image_number:
			ll_caozuo.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.VISIBLE);
		break;
		
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data); 
		if (requestCode ==0) {//拍照直接上传
			
			lists.add(path2);
			image_number.setBackgroundResource(btn1);
			
			Attachment affInfos = Attachment.GetFileInfo(path2);
			adapter.appendToList(affInfos);
			int a = adapter.getList().size();
			int count = (int) Math.ceil(a / 4.0);
			gridView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT,
					(int) (94 * 1.5 * count)));
		}else if(requestCode ==1){
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				path2 = cursor.getString(columnIndex);
				
				lists.add(path2);
				image_number.setBackgroundResource(btn1);
				
				cursor.close();
				Attachment affInfos = Attachment.GetFileInfo(path2);
				adapter.appendToList(affInfos);
				int a = adapter.getList().size();
				int count = (int) Math.ceil(a / 4.0);
				gridView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT,
						(int) (94 * 1.5 * count)));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

	private String getFileName() {  
        String saveDir = Environment.getExternalStorageDirectory() + "/bank";  
        File dir = new File(saveDir);  
        if (!dir.exists()) {  
            dir.mkdir(); // 创建文件夹  
        }  
        //用日期作为文件名，确保唯一性  
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");  
        String fileName = saveDir + "/" + formatter.format(date) + ".PNG";  
        return fileName;  
    }  
	
	/**
	 * 点击事件
	 * @author Administrator
	 *
	 */
	private class MyOnItemClickListener implements OnItemClickListener{
		@SuppressLint("ResourceType")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				final int arg2, long arg3) {
			Attachment infos = (Attachment) adapter.getItem(arg2);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					NoteXiuGaiActivity.this);
			builder.setTitle(infos.getFileName());
			builder.setIcon(getResources().getColor(
					android.R.color.transparent));
			builder.setMessage("是否删除当前附件");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							
							lists.remove(arg2);
							
							adapter.clearPositionList(arg2);
							int a = adapter.getList().size();
							int count = (int) Math.ceil(a / 4.0);
							gridView.setLayoutParams(new LayoutParams(
									LayoutParams.MATCH_PARENT,
									(int) (94 * 1.5 * count)));
						}
					});
			builder.setPositiveButton("取消",null);
			builder.create().show();
		}
	}
	
	
	
	
}
