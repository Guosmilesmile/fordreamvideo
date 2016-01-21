package cn.edu.xmu.ForDream.application;

import java.io.File;

import android.app.Application;
import android.app.Service;
import android.os.Environment;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.xmu.ForDream.R;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.yixia.camera.VCamera;
import com.yixia.camera.util.DeviceUtils;

/**
 * 主Application
 */
public class LocationApplication extends Application {
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	public ImageView getPositionImageView;
	public TextView mLocationResult,logMsg;
	public TextView trigger,exit;
	public Vibrator mVibrator;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());
		
		
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		
		// 设置拍摄视频缓存路径
		File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (DeviceUtils.isZte()) {
			if (dcim.exists()) {
				VCamera.setVideoCachePath(dcim + "/Camera/VCameraDemo/");
			} else {
				VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Camera/VCameraDemo/");
			}
		} else {
			VCamera.setVideoCachePath(dcim + "/Camera/VCameraDemo/");
		}
		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);
		// 初始化拍摄SDK，必须
		VCamera.initialize(this);		
	}

	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb2 = new StringBuffer(256);

			sb2.append(location.getAddrStr());
			setMsg(sb2.toString());

		}


	}
	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void setMsg(String str)
	{
		
		mLocationResult.setText(str);
		
		mLocationClient.stop();
		getPositionImageView.setImageResource(R.drawable.submit_getposition2);

	}
	

	
}
