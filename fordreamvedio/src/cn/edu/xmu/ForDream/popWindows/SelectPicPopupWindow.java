package cn.edu.xmu.ForDream.popWindows;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.activities.MediaRecorderActivity;

public class SelectPicPopupWindow extends Activity implements OnClickListener{

	private Button btn_take_video, btn_pick_video, btn_cancel;
	private LinearLayout layout;
	private String videoPath = "";
	private static final int PICK_VIDEO_ACTIVITY_REQUEST_CODE = 400;
	private static final int TAKE_VIDEO_ACTIVITY_REQUEST_CODE = 550;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_video_popupwindow);
		btn_take_video = (Button) this.findViewById(R.id.btn_take_video);
		btn_pick_video = (Button) this.findViewById(R.id.btn_pick_video);
		btn_cancel = (Button) this.findViewById(R.id.select_video_btn_cancel);
		
		layout = (LinearLayout) findViewById(R.id.select_video_pop_layout);
		
		//添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
		layout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", 
						Toast.LENGTH_SHORT).show();	
			}
		});
		//添加按钮监听
		btn_cancel.setOnClickListener(this);
		btn_take_video.setOnClickListener(this);
		btn_pick_video.setOnClickListener(this);
	}
	
	//实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_video:
			jumpActivity(MediaRecorderActivity.class);
			break;
		case R.id.btn_pick_video:
			openVideo();
			break;
		case R.id.select_video_btn_cancel:
			setResult(RESULT_CANCELED); 
            this.finish();
			break;
		default:
			break;
		}
	}
	
	//打开本地视频
    public void openVideo(){
    	Intent intent = new Intent();
    	intent.setType("video/*");   
        intent.setAction(Intent.ACTION_GET_CONTENT);   
    	this.startActivityForResult(intent, PICK_VIDEO_ACTIVITY_REQUEST_CODE);
    }
    
    @Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		super.onActivityResult(requestCode, resultCode, data);
 		if (requestCode == PICK_VIDEO_ACTIVITY_REQUEST_CODE) {
 			if (resultCode == RESULT_OK) {
 				Uri uri = data.getData();
 				if(uri != null){
 					videoPath = getRealPathFromURI(uri);
 				}else{
 					Toast.makeText(getApplicationContext(), "提示：获取本地视频失败！", 
 							Toast.LENGTH_SHORT).show();	
 				}
 			}
 			// 实例化 Bundle，设置需要传递的参数 
 	        Bundle bundle = new Bundle(); 
 	        bundle.putString("videoPath", videoPath); 
 	        bundle.putString("imagePath", ""); 
 			//返回
 			SelectPicPopupWindow.this.setResult(RESULT_OK, SelectPicPopupWindow.this.getIntent().putExtras(bundle)); 
 			SelectPicPopupWindow.this.finish();
 		} else if(requestCode == TAKE_VIDEO_ACTIVITY_REQUEST_CODE){
 			if (resultCode == RESULT_OK) {
 				Bundle bundle = data.getExtras(); 
	            videoPath = bundle.getString("videoPath");
	            String imagePath = bundle.getString("imagePath");
	         // 实例化 Bundle，设置需要传递的参数 
	 	        Bundle bundle1 = new Bundle(); 
	 	        bundle1.putString("videoPath", videoPath); 
	 	        bundle1.putString("imagePath", imagePath);
	 			//返回
	 	        this.setResult(RESULT_OK, getIntent().putExtras(bundle1)); 
	 			this.finish();
 			}else if(resultCode == RESULT_CANCELED){
 				//返回
	 			this.setResult(RESULT_CANCELED); 
	 			this.finish();
 			}
 		}
 	}
    
    public String getRealPathFromURI(Uri contentUri){
    	Cursor cursor = null;
        try{
            String[] proj = {MediaStore.Video.Media.DATA};
            ContentResolver cr = this.getContentResolver(); 
            cursor = cr.query(contentUri, proj, null, null, null); 
            if (cursor == null) { 
                return null; 
            } 
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }catch (Exception e){
            return contentUri.getPath();
        }finally{
        	cursor.close();
        }
	}
    
    public void jumpActivity(Class<?> cls) {
		// 查看具体内容
		Intent intent = new Intent(this, cls);
		Bundle b = new Bundle();
        b.putString("videotype", "group");
        intent.putExtras(b);
		startActivityForResult(intent, TAKE_VIDEO_ACTIVITY_REQUEST_CODE);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        // 是否触发按键为back键    
        if (keyCode == KeyEvent.KEYCODE_BACK) { 
            setResult(RESULT_CANCELED); 
            this.finish(); 
            return true; 
        }else { 
            return super.onKeyDown(keyCode, event); 
        } 
    } 
}
