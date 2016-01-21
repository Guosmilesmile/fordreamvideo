package cn.edu.xmu.ForDream.activities;

import info.hoang8f.widget.FButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.application.LocationApplication;
import cn.edu.xmu.ForDream.popWindows.SelectPicPopupWindow;
import cn.edu.xmu.ForDream.service.UploadLogService;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.HttpUtils;
import cn.edu.xmu.ForDream.util.StreamTool;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class TalkGroupAddActivity extends Activity implements View.OnClickListener{

	FButton okFBtn;
	EditText groupname, groupintroduction, videodescription;
	Spinner grouptype;
	ImageView addgroupReturn;
	ImageView addgroupVideo;
	Bitmap imageBitmap = null;
	String videoPath = "";
	String imagePath = "";
	String imagename = "";
	String videoname = "";
	String resultGroupId="";
	boolean isLocalVideo = true;
	private int selectedGroupType = FinalName.SQUARE_GAME;
	private static final String[] grouptypes={"游戏", "宿舍趣事", "旅游", "学霸"};
//    FinalName.SQUARE_GAME, FinalName.SQUARE_DORM_ANECDOTE, FinalName.SQUARE_TRAVEL, FinalName.SQUARE_STUDY
	private static final int VIDEO_ACTIVITY_REQUEST_CODE = 300;
	private UploadLogService service;
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";
	//定位图标和文字
    private ImageView getPositionImageView;
    private TextView positionTextView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.talkgroup_add_page);
        
        service =  new UploadLogService(this);
        
        mLocationClient = ((LocationApplication)getApplication()).mLocationClient;
        positionTextView = (TextView) findViewById(R.id.add_group_positionTextView);
        getPositionImageView = (ImageView) findViewById(R.id.add_group_getPositionImage);
        getPositionImageView.setOnClickListener(this);
      //定位信息初始化
        ((LocationApplication)getApplication()).mLocationResult = positionTextView;        
        ((LocationApplication)getApplication()).getPositionImageView = getPositionImageView; 
        
        okFBtn = (FButton) findViewById(R.id.add_group_ok_button);
        okFBtn.setOnClickListener(this);
        
        groupname = (EditText) findViewById(R.id.add_groupname_editText);
        groupintroduction = (EditText) findViewById(R.id.add_groupintroduction_editText);
        videodescription = (EditText) findViewById(R.id.add_group_videodes_editText);
        
        addgroupReturn = (ImageView) findViewById(R.id.add_group_return);
        addgroupReturn.setOnClickListener(this);
        
        addgroupVideo = (ImageView) findViewById(R.id.add_group_video);
        addgroupVideo.setOnClickListener(this);
        
      //将可选内容与ArrayAdapter连接  
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, grouptypes);
      //设置下拉列表风格  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grouptype = (Spinner) findViewById(R.id.add_grouptype_spinner);
      //将adapter添加到spinner中  
        grouptype.setAdapter(adapter);
        grouptype.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				switch(arg2){
				case 0:
					selectedGroupType = FinalName.SQUARE_GAME;
					break;
				case 1:
					selectedGroupType = FinalName.SQUARE_DORM_ANECDOTE;
					break;
				case 2:
					selectedGroupType = FinalName.SQUARE_TRAVEL;
					break;
				case 3:
					selectedGroupType = FinalName.SQUARE_STUDY;
					break;
				}
				//设置显示当前选择的项  
                arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
//				Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
			}
		});
	}
    Handler handler=new Handler(){
    	public void handleMessage(Message msg) {
			// 实例化 Bundle，设置需要传递的参数 
 	        Bundle bundle = new Bundle(); 
 	        bundle.putString("groupname", groupname.getText().toString());
 	        bundle.putString("groupintroduction", groupintroduction.getText().toString());
 	        bundle.putString("groupid", resultGroupId);
 	        bundle.putString("grouptype",grouptypes[selectedGroupType-FinalName.SQUARE_GAME]); 
 			//返回
 	        TalkGroupAddActivity.this.setResult(RESULT_OK, TalkGroupAddActivity.this.getIntent().putExtras(bundle)); 
 			TalkGroupAddActivity.this.finish();
    	}
    };
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.add_group_ok_button:
			if(groupname.getText().toString().equals(""))
				Toast.makeText(this, "请输入群组名称", Toast.LENGTH_SHORT).show();
			else if(groupintroduction.getText().toString().equals(""))
				Toast.makeText(this, "请输入群组介绍", Toast.LENGTH_SHORT).show();
			else if(selectedGroupType == -1) 
				Toast.makeText(this, "请选择群组类别", Toast.LENGTH_SHORT).show();
			else{
				//从本地选择的视频
				if(isLocalVideo)
					imagePath = saveBitmapImage(imageBitmap);
				System.out.println("saveImagePath="+imagePath+",videoPath="+videoPath);
				File file = new File(videoPath);
				File image = new File(imagePath);
				imagename = image.getName();
				videoname = file.getName();
				uploadImage(image);
				uploadFile(file);
			}
			break;
		case R.id.add_group_return:
			finish();
			break;
		case R.id.add_group_video:
			startActivityForResult(new Intent(this,SelectPicPopupWindow.class), VIDEO_ACTIVITY_REQUEST_CODE);
			break;
		case R.id.add_group_getPositionImage:
			String string=positionTextView.getText().toString();
			if(string!=""&&!string.equals(R.string.getposition))
			InitLocation();
			positionTextView.setText(R.string.getposition);
		    mLocationClient.start();
			break;
		}
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    if (requestCode == VIDEO_ACTIVITY_REQUEST_CODE) { 
	        if (resultCode == RESULT_OK) { 
	        	Bundle bundle = data.getExtras(); 
	            videoPath = bundle.getString("videoPath");
	            imagePath = bundle.getString("imagePath");
	            //从本地选择视频
	            if(imagePath.equals("")){
	            	isLocalVideo = true;
	            	imageBitmap = getVideoThumbnail(videoPath, 200, 200, Video.Thumbnails.FULL_SCREEN_KIND);
	            	addgroupVideo.setImageBitmap(imageBitmap);
	            } else {
	            //拍摄视频
	            	isLocalVideo = false;
	            	addgroupVideo.setImageBitmap(getLocalImage(imagePath));
	            }
	        }else if (resultCode == RESULT_CANCELED) { 
	        	//do nothing
	        } 
	    } 
	} 
	
	public Bitmap getVideoThumbnail(String videoPath, int width , int height, int kind){ 
        Bitmap bitmap = null; 
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind); 
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT); 
        return bitmap; 
    }
	
	public Bitmap getLocalImage(String realPath){
		Bitmap bmp = BitmapFactory.decodeFile(realPath);
		return bmp;
	}
	
	public String saveBitmapImage(Bitmap bitmap){
//		String saveImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + System.currentTimeMillis() + ".jpg";
		String saveImagePath = Environment.getExternalStorageDirectory().toString() + "/DCIM" + "/video";
		saveImagePath = createImagePath(TalkGroupAddActivity.this);
		try{
			File file = new File(saveImagePath);
			if(!file.exists())
				file.createNewFile();
			FileOutputStream outStream = null;
			// 缩放图片的尺寸
			float scaleWidth = (float) 200 / bitmap.getHeight();
			float scaleHeight = (float) 200 / bitmap.getHeight();
			Matrix localMatrix = new Matrix();
			localMatrix.postScale(scaleWidth, scaleHeight);
			
			// 产生缩放后的Bitmap对象
			Bitmap	localBitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getHeight(), bitmap.getHeight(),
								localMatrix, true);
			
			ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
			localBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bos2);
				 
			outStream = new FileOutputStream(saveImagePath);
			outStream.write(bos2.toByteArray());
			outStream.flush();
			outStream.close();
			
			bitmap.recycle();
			localBitmap2.recycle();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return saveImagePath;
	}
	
	public String createImagePath(Context context){
		long dateTaken = System.currentTimeMillis();
		String title = "VMS_" + dateTaken;
		String filename = title + ".jpg";
		
		String dirPath = Environment.getExternalStorageDirectory()+"/Android/data/" + context.getPackageName()+"/video";
		File file = new File(dirPath);
		if(!file.exists() || !file.isDirectory())
			file.mkdirs();
		String filePath = dirPath + "/" + filename;
		return filePath;
	}
	
	/**
	 * 上传图片到服务器
	 * @param image 图片文件
	 */
	private void uploadImage(final File image){
			new Thread(new Runnable() {	
				public void run() {
					try {
						Log.i("upLoad","upLoad");
						Log.i("imagenameString",image.getName());
						String sourceid = service.getBindId(image);
						Socket socket = new Socket(FinalUrl.IP, 9900);
			            OutputStream outStream = socket.getOutputStream(); 
			            String head = "Content-Length="+ image.length() + ";filename="+ image.getName() 
			            	+ ";sourceid="+(sourceid!=null ? sourceid : "")+";type=videoimage"+"\r\n";
			            outStream.write(head.getBytes());
			            
			            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());	
						String response = StreamTool.readLine(inStream);
			            String[] items = response.split(";");
						String responseSourceid = items[0].substring(items[0].indexOf("=")+1);
						String position = items[1].substring(items[1].indexOf("=")+1);
						if(sourceid==null){//如果是第一次上传文件，在数据库中不存在该文件所绑定的资源id
							service.save(responseSourceid, image);
						}
						RandomAccessFile fileOutStream = new RandomAccessFile(image, "r");
						fileOutStream.seek(Integer.valueOf(position));
						byte[] buffer = new byte[1024];
						int len = -1;
						int length = Integer.valueOf(position);
						while( (len = fileOutStream.read(buffer)) != -1){
							outStream.write(buffer, 0, len);
							length += len;//累加已经上传的数据长度
							Message msg = new Message();
							msg.getData().putInt("length", length);
							msg.what=1;
//							handler.sendMessage(msg);
						}
						if(length == image.length()) service.delete(image);
						fileOutStream.close();
						outStream.close();
			            inStream.close();
			            socket.close();
			        } catch (Exception e) {                    
			        	
			        }
				}
			}).start();
	}
	
	/**
 	 * 上传视频文件到服务器
 	 * @param file 视频文件
 	 */
	private void uploadFile(final File file) {
			new Thread(new Runnable() {	
				public void run() {
					try {
						int dot = file.getName().indexOf(".");
						if(isLocalVideo)
							videoname = "Group_" + System.currentTimeMillis() + file.getName().substring(dot + 1);
						Log.i("upLoad","upLoad");
						Log.i("filenameString",videoname);
						String sourceid = service.getBindId(file);
						Socket socket = new Socket(FinalUrl.IP, 9900);
			            OutputStream outStream = socket.getOutputStream(); 
			            String head = "Content-Length="+ file.length() + ";filename="+ videoname 
			            	+ ";sourceid="+(sourceid!=null ? sourceid : "")+";type=video"+"\r\n";
			            outStream.write(head.getBytes());
			            
			            PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());	
						String response = StreamTool.readLine(inStream);
			            String[] items = response.split(";");
						String responseSourceid = items[0].substring(items[0].indexOf("=")+1);
						String position = items[1].substring(items[1].indexOf("=")+1);
						if(sourceid==null){//如果是第一次上传文件，在数据库中不存在该文件所绑定的资源id
							service.save(responseSourceid, file);
						}
						RandomAccessFile fileOutStream = new RandomAccessFile(file, "r");
						fileOutStream.seek(Integer.valueOf(position));
						byte[] buffer = new byte[1024];
						int len = -1;
						int length = Integer.valueOf(position);
						while( (len = fileOutStream.read(buffer)) != -1){
							outStream.write(buffer, 0, len);
							length += len;//累加已经上传的数据长度
							Message msg = new Message();
							msg.getData().putInt("length", length);
							msg.what=1;
//							handler.sendMessage(msg);
						}
						if(length == file.length()) service.delete(file);
						fileOutStream.close();
						outStream.close();
			            inStream.close();
			            socket.close();
			            
			            postNewGroupInfo();
			        } catch (Exception e) {
			        	e.printStackTrace();
			        }
				}
			}).start();
	}
	
	/**
 	 * 
 	 * 发送视频信息到服务器
 	 */
 	private void postNewGroupInfo() throws Exception
 	{
 		
 		new Thread(new Runnable() {	
			public void run() {
		 		HashMap<String, String> map=new HashMap<String, String>();
		 		try {
		 			String result=null;
		 			map.put("userid",String.valueOf(FinalUrl.userid));
		 			map.put("username", FinalUrl.username);
		 			map.put("name", groupname.getText().toString());
		 			map.put("description", groupintroduction.getText().toString());
		 			map.put("videoIntroduction", videodescription.getText().toString());
		 			map.put("filename", videoname);
		 			map.put("imagename", imagename);
		 			map.put("videoClassfication", String.valueOf(selectedGroupType));
		 			map.put("videoPosition", positionTextView.getText().toString());
		 			result=HttpUtils.postRequest(FinalUrl.PostCreateGroupUrl, map);
		 			resultGroupId=new JSONObject(result).getString("groupid");
		 			handler.sendMessage(handler.obtainMessage(0));
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}}).start();

 	}
 	
 	 /**  
 		 * 初始化定位设置
 		 */
 		private void InitLocation(){
 			LocationClientOption option = new LocationClientOption();
 			option.setLocationMode(tempMode);//设置定位模式
 			option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
 			int span=5000;
 			option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
 			option.setIsNeedAddress(true);
 			option.setOpenGps(true);
 			mLocationClient.setLocOption(option);
 		}
}
