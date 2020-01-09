package com.example.emailtest.ui.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emailtest.R;
import com.example.emailtest.db.FileService;
import com.example.emailtest.utils.Stack;
import com.example.emailtest.view.NoScrollListView;


public class WenJianActivity extends Activity implements OnClickListener{
	
	
	List<HashMap<String, Object>> List = new ArrayList<HashMap<String, Object>>();
	NoScrollListView nolist;
	ImageAdapter grabAdapter=null;
	
	ImageView image_quexiao,image_updata;
	
	private FileService fileService ;
	private String path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wenjian);
		Stack.addActivity(this);
		fileService = new FileService(this);
		
		nolist =  (NoScrollListView) this.findViewById(R.id.nolist);
		image_quexiao  =  (ImageView) this.findViewById(R.id.image_quexiao);
		image_updata  =  (ImageView) this.findViewById(R.id.image_updata);
		
		image_quexiao.setOnClickListener(this);image_updata.setOnClickListener(this);
		
		List = fileService.findFileAll();
		grabAdapter = new ImageAdapter(getApplicationContext(), List,this);
		nolist.setAdapter(grabAdapter);
			
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.image_updata:
			new PopupWindows(WenJianActivity.this,image_updata);
		break;
		case R.id.image_quexiao:
			finish();
		break;
		}
		
	}
	
	
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.select_1, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
			LinearLayout ll_1 = (LinearLayout) view.findViewById(R.id.ll_1);
			ll_1.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.push_bottom_in_2));

			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAsDropDown(parent);
			update();
			TextView tv_paizhao = (TextView) view.findViewById(R.id.tv_paizhao);
			TextView tv_pic = (TextView) view.findViewById(R.id.tv_pic);
			TextView tv_upload = (TextView) view.findViewById(R.id.tv_upload);
			
			tv_paizhao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					
					String state = Environment.getExternalStorageState();
					if (state.equals(Environment.MEDIA_MOUNTED)) {
						Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
						path = getFileName();
						getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));  
						startActivityForResult(getImageByCamera, 0);
					} else {
						Toast.makeText(WenJianActivity.this, "请插入SD卡",Toast.LENGTH_LONG).show();
					}
					
				}
			});
			tv_pic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(getImage, 1);
					dismiss();
				}
			});
			tv_upload.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("file/");
					startActivityForResult(intent, 2);
					dismiss();
				}
			});
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data); 
		if (requestCode ==0) {//拍照直接上传
			
			String datetime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date());
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("filepath",path);
			File fil=new File(path);
	        long a=fil.length();
			hashMap.put("filesize", String.valueOf(a));
			hashMap.put("datatime",datetime);
			fileService.addFile(hashMap);
			List.clear();
			List = fileService.findFileAll();
			grabAdapter = new ImageAdapter(getApplicationContext(), List,this);
			nolist.setAdapter(grabAdapter);
			
			
		}else if(requestCode ==1){
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				path = cursor.getString(columnIndex);
				
				String datetime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date());
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("filepath",path);
				File fil=new File(path);
		        long a=fil.length();
				hashMap.put("filesize", String.valueOf(a));
				hashMap.put("datatime",datetime);
				fileService.addFile(hashMap);
				List.clear();
				List = fileService.findFileAll();
				grabAdapter = new ImageAdapter(getApplicationContext(), List,this);
				nolist.setAdapter(grabAdapter);
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	public class ImageAdapter extends BaseAdapter{
		List<HashMap<String, Object>> map = null;
		Context context;
		Activity pinglun;
		public ImageAdapter(Context context,List<HashMap<String, Object>> map,Activity pinglun) {
			this.map = map; 
			this.context=context;
			this.pinglun=pinglun;
		}
		@Override
		public int getCount() {
			return map.size();
		}
		@Override
		public HashMap<String, Object> getItem(int position) {
			return map.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_2, null);
				holder = new ViewHolder();
	 			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name); 
	 			holder.tv_guige = (TextView) convertView.findViewById(R.id.tv_guige); 
	 			holder.image_tou = (ImageView) convertView.findViewById(R.id.image_tou); 
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			} 
			HashMap<String, Object> mapItem = map.get(position);
			
			if(mapItem.get("filepath").toString().length()>0){
				int index = mapItem.get("filepath").toString().lastIndexOf("/");
				String name = mapItem.get("filepath").toString().substring(index+1);
				holder.tv_name.setText(name);
			}
			
			
			holder.tv_guige.setText(mapItem.get("filesize").toString()+"k     "+mapItem.get("datatime").toString());
			
			BitmapFactory.Options options = new BitmapFactory.Options();  
            Bitmap myBitmap = BitmapFactory.decodeFile(mapItem.get("filepath").toString(), options);
            holder.image_tou.setImageBitmap(myBitmap);
			
	 		return convertView;
		}
		public void addItem(HashMap<String, Object> newsitem) {
			map.add(newsitem);
		} 
		public void addItemAll(List newsitem) {
			map.addAll(newsitem);
		}
		public void removeAll() {
			map.clear();
		}
		class ViewHolder { 
			TextView tv_name,tv_guige;ImageView image_tou;
		}
		
	}
	
	
	private String getFileName() {  
        String saveDir = Environment.getExternalStorageDirectory() + "/email";  
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


}
