package cn.edu.xmu.ForDream.activities;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.application.LocationApplication;
import cn.edu.xmu.ForDream.popWindows.ChoosePop;
import cn.edu.xmu.ForDream.service.UploadLogService;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.HttpUtils;
import cn.edu.xmu.ForDream.util.StreamTool;

import cn.sharesdk.douban.Douban;
import cn.sharesdk.douban.Douban.ShareParams;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.l;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SubmitActivity extends Activity {
	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private ClickListener clickListener;

	// 定位图标和文字
	private ImageView getPositionImageView;
	private TextView positionTextView;

	// 视频公开与否
	private LinearLayout isLock;
	private ImageView isPublicImage;
	private TextView isPublicTextView;
	
	private LinearLayout isCompany;
	private TextView isCompanyTextView;

	// 分类选择和讨论组
	private LinearLayout addClassification;
	private LinearLayout chooseGroup;

	// 分享平台
	private LinearLayout sinaweiboLayout;
	private LinearLayout tencentweiboLayout;
	private LinearLayout renrenLayout;
	private LinearLayout doubanLayout;
	private TextView sinaweiboTextView;
	private TextView tencentweiboTextView;
	private TextView renrenTextView;
	private TextView doubanTextView;

	// 发送取消
	private TextView sendTextView;
	private TextView cancelTextView;

	// 视频封面
	private ImageView videoImg;

	private ProgressBar uploadbar;
	private TextView resultView;
	private UploadLogService service;
	private String filenameString;
	private String imagenameString;
	private String imagePath;
	private String path;
	private EditText editText;
	private TextView classificationTextView;
	private TextView addShareGroupTextView;
	private HashMap<String, String> channelMap;
	private HashMap<String, String> groupMap;
	private ArrayList<String> classficationlist;
	private ArrayList<String> grouplist;
	private boolean isUpload;
	private boolean sinax = false, tecentx = false, renrenx = false,
			doubanx = false;

	private PopupWindow choose_classfication_popupWindow;
	private PopupWindow choose_group_popWindow;
	private String shareGroupId;
	final static int CLASSFICATION_POP = 1;
	final static int ChooseGroup_POP = 2;
	final static int GET_CLASSFICATION = 10;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.submit_activity);
		mLocationClient = ((LocationApplication) getApplication()).mLocationClient;
		positionTextView = (TextView) findViewById(R.id.submit_positionTextView);
		getPositionImageView = (ImageView) findViewById(R.id.submit_getPositionImage);
		videoImg = (ImageView) findViewById(R.id.submit_videoimg);

		// 定位信息初始化
		((LocationApplication) getApplication()).mLocationResult = positionTextView;

		((LocationApplication) getApplication()).getPositionImageView = getPositionImageView;

		clickListener = new ClickListener();

		shareGroupId = null;
		// socket上传视频所需初始化
		service = new UploadLogService(this);
		Intent i = this.getIntent();
		Bundle b = i.getExtras();
		path = b.getString("path");
		imagePath = b.getString("imagePath");
		isUpload = false;
		System.out.println("path" + path + ",imagePath" + imagePath);

		// 设置封面图片
		Bitmap bmpDefaultPic;
		bmpDefaultPic = BitmapFactory.decodeFile(imagePath);
		videoImg.setImageBitmap(bmpDefaultPic);

		isLock = (LinearLayout) findViewById(R.id.submit_IsLock);
		isPublicImage = (ImageView) findViewById(R.id.submit_IsPublic);
		isPublicTextView = (TextView) findViewById(R.id.submit_isPublicTextView);
		isCompanyTextView = (TextView) findViewById(R.id.submit_isCompanyTextView);
		editText = (EditText) findViewById(R.id.submit_editText1);

		addClassification = (LinearLayout) findViewById(R.id.submit_add_classification);
		chooseGroup = (LinearLayout) findViewById(R.id.submit_choose_group);

		sinaweiboLayout = (LinearLayout) findViewById(R.id.submit_sinaweibo);
		tencentweiboLayout = (LinearLayout) findViewById(R.id.submit_tencentweibo);
		renrenLayout = (LinearLayout) findViewById(R.id.submit_renren);
		doubanLayout = (LinearLayout) findViewById(R.id.submit_douban);

		sinaweiboTextView = (TextView) findViewById(R.id.submit_sinaweiboTextView);
		tencentweiboTextView = (TextView) findViewById(R.id.submit_tencentweiboTextView);
		renrenTextView = (TextView) findViewById(R.id.submit_renrenTextView);
		doubanTextView = (TextView) findViewById(R.id.submit_doubanTextView);

		cancelTextView = (TextView) findViewById(R.id.submit_cancel_textView);
		sendTextView = (TextView) findViewById(R.id.submit_send_textView);
		uploadbar = (ProgressBar) findViewById(R.id.submit_uploadbar);
		resultView = (TextView) findViewById(R.id.submit_processresult);
		isCompany=(LinearLayout) findViewById(R.id.submit_IsCompany);
		classificationTextView = (TextView) findViewById(R.id.submit_classificationTextView);
		addShareGroupTextView = (TextView) findViewById(R.id.submit_addShareGroupTextView);

		sendTextView.setOnClickListener(clickListener);
		cancelTextView.setOnClickListener(clickListener);
		isLock.setOnClickListener(clickListener);
		addClassification.setOnClickListener(clickListener);
		chooseGroup.setOnClickListener(clickListener);
		getPositionImageView.setOnClickListener(clickListener);
		sinaweiboLayout.setOnClickListener(clickListener);
		tencentweiboLayout.setOnClickListener(clickListener);
		renrenLayout.setOnClickListener(clickListener);
		doubanLayout.setOnClickListener(clickListener);
		isCompany.setOnClickListener(clickListener);
		ShareSDK.initSDK(this);

		initVedioClassification();
		
		
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1: {
				uploadbar.setProgress(msg.getData().getInt("length"));
				float num = (float) uploadbar.getProgress()
						/ (float) uploadbar.getMax();
				int result = (int) (num * 100);
				resultView.setText(result + "%");
				if (uploadbar.getProgress() == uploadbar.getMax()) {

					Toast.makeText(SubmitActivity.this, R.string.success, 1)
							.show();
				}
			}
				break;
			// 上传视频
			case 2: {
				try {
					postVedioInfo();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				break;
			case 3: {
				if (sinax)
					showShare(true, "SinaWeibo", false);
				if (tecentx)
					showShare(true, "TencentWeibo", false);
				if (renrenx)
					showShare(true, "Renren", false);
				if (doubanx)
					showShare(true, "Douban", false);

				jumpActivity(SquareActivity.class);
			}
				break;
			case 4: {
				HashMap<String, Integer> map = (HashMap<String, Integer>) msg.obj;
				int whichItem = map.get("whichItem");
				int whichpop = map.get("whichPop");

				if (whichpop == CLASSFICATION_POP) {
					classificationTextView.setText(classficationlist
							.get(whichItem));
					choose_classfication_popupWindow.dismiss();
				} else if (whichpop == ChooseGroup_POP) {
					String chooseString = grouplist.get(whichItem);
					if (addShareGroupTextView
							.getText()
							.toString()
							.equals(getString(R.string.submit_shareGroupOriginalString))) {

						addShareGroupTextView.setText(chooseString);
						shareGroupId = groupMap.get(chooseString);
					} else {
						addShareGroupTextView.setText(addShareGroupTextView
								.getText().toString() + ";" + chooseString);
						shareGroupId = shareGroupId + ";"
								+ groupMap.get(chooseString);
					}

				}

			}
				break;
			case 5: {
				showToastMessage(msg.obj.toString());
			}
				break;
			// 获取到了分类信息，准备获取分组信息
			case GET_CLASSFICATION: {
				initGroup();
			}
			default:
				break;
			}

		}
	};

	private final class ClickListener implements View.OnClickListener,
			PlatformActionListener, Callback {

		public void onClick(View v) {

			switch (v.getId()) {

			case R.id.submit_getPositionImage: {
				String string = positionTextView.getText().toString();
				if (string != "" && !string.equals(R.string.getposition))
					InitLocation();
				positionTextView.setText(R.string.getposition);

				mLocationClient.start();
			}

				break;
			case R.id.submit_IsLock: {
				// Log.i("IsLock", "IsLock");
				if (isPublicTextView.getText().toString()
						.equals(getString(R.string.public_friend))) {
					isPublicTextView
							.setText((getString(R.string.private_friend)));
					isPublicImage
							.setImageResource(R.drawable.submit_unpublicimage);

					isPublicTextView.setTextColor(getResources().getColor(
							R.color.lock_color));

				} else if (isPublicTextView.getText().toString()
						.equals(getString(R.string.private_friend))) {
					isPublicTextView.setText(getString(R.string.public_friend));
					isPublicImage
							.setImageResource(R.drawable.submit_publicimage);
					isPublicTextView.setTextColor(getResources().getColor(
							R.color.unlock_color));

				}

			}
				break;
			case R.id.submit_IsCompany: {
				// Log.i("IsLock", "IsLock");
				if (isCompanyTextView.getText().toString().equals("无")) {
					isCompanyTextView.setText("企业");

				} else if (isCompanyTextView.getText().toString().equals("企业")) {
					isCompanyTextView.setText("高校");

				} else if (isCompanyTextView.getText().toString().equals("高校")) {
					isCompanyTextView.setText("无");

				}

			}

				break;
			case R.id.submit_add_classification: {
				showPopwindow(CLASSFICATION_POP);
				// classificationTextView.setText("游戏");
			}

				break;
			case R.id.submit_choose_group: {
				showPopwindow(ChooseGroup_POP);
			}

				break;
			case R.id.submit_sinaweibo: {
				Platform sina = ShareSDK.getPlatform(SubmitActivity.this,
						SinaWeibo.NAME);
				sina.setPlatformActionListener(this); // 设置分享事件回调
				if (sina.isValid()
						&& sinaweiboTextView.getCurrentTextColor() == getResources()
								.getColor(R.color.choose_color)) {
					sina.removeAccount();
				} else if (sina.isValid()
						&& sinaweiboTextView.getCurrentTextColor() == getResources()
								.getColor(R.color.unchoose_color)) {
					showToastMessage("已授权成功~~~~");
					sinax = true;
				} else {
					showToastMessage("请授权~~~~");
					sina.SSOSetting(true);
					sina.authorize();
					// 执行图文分享
				}
				if (sinaweiboTextView.getCurrentTextColor() == getResources()
						.getColor(R.color.unchoose_color)) {
					sinaweiboTextView.setTextColor(getResources().getColor(
							R.color.choose_color));
				} else {
					sinaweiboTextView.setTextColor(getResources().getColor(
							R.color.unchoose_color));
				}

			}

				break;
			case R.id.submit_tencentweibo: {
				Platform tencentwb = ShareSDK.getPlatform(SubmitActivity.this,
						TencentWeibo.NAME);
				tencentwb.setPlatformActionListener(this); // 设置分享事件回调
				if (tencentwb.isValid()
						&& tencentweiboTextView.getCurrentTextColor() == getResources()
								.getColor(R.color.choose_color)) {
					tencentwb.removeAccount();
				} else if (tencentwb.isValid()
						&& tencentweiboTextView.getCurrentTextColor() == getResources()
								.getColor(R.color.unchoose_color)) {
					showToastMessage("已授权成功~~~~");
					tecentx = true;
				} else {
					showToastMessage("请授权~~~~");
					tencentwb.SSOSetting(true);
					tencentwb.authorize();
					// 执行图文分享
				}
				if (tencentweiboTextView.getCurrentTextColor() == getResources()
						.getColor(R.color.unchoose_color)) {
					tencentweiboTextView.setTextColor(getResources().getColor(
							R.color.choose_color));
				} else {
					tencentweiboTextView.setTextColor(getResources().getColor(
							R.color.unchoose_color));
				}
			}

				break;
			case R.id.submit_renren: {
				Platform renren = ShareSDK.getPlatform(SubmitActivity.this,
						Renren.NAME);
				renren.setPlatformActionListener(this); // 设置分享事件回调
				if (renren.isValid()
						&& renrenTextView.getCurrentTextColor() == getResources()
								.getColor(R.color.choose_color)) {
					renren.removeAccount();
				} else if (renren.isValid()
						&& renrenTextView.getCurrentTextColor() == getResources()
								.getColor(R.color.unchoose_color)) {
					showToastMessage("已授权成功~~~~");
					renrenx = true;
				} else {
					showToastMessage("请授权~~~~");
					renren.SSOSetting(true);
					renren.authorize();
					// 执行图文分享
				}
				if (renrenTextView.getCurrentTextColor() == getResources()
						.getColor(R.color.unchoose_color)) {
					renrenTextView.setTextColor(getResources().getColor(
							R.color.choose_color));
				} else {
					renrenTextView.setTextColor(getResources().getColor(
							R.color.unchoose_color));
				}
			}

				break;

			case R.id.submit_douban: {
				Platform douban = ShareSDK.getPlatform(SubmitActivity.this,
						Douban.NAME);
				douban.setPlatformActionListener(this); // 设置分享事件回调
				if (douban.isValid()
						&& doubanTextView.getCurrentTextColor() == getResources()
								.getColor(R.color.choose_color)) {
					douban.removeAccount();
				} else if (douban.isValid()
						&& doubanTextView.getCurrentTextColor() == getResources()
								.getColor(R.color.unchoose_color)) {
					showToastMessage("已授权成功~~~~");
					doubanx = true;
				} else {
					showToastMessage("请授权~~~~");
					douban.SSOSetting(true);
					douban.authorize();
					// 执行图文分享
				}
				if (doubanTextView.getCurrentTextColor() == getResources()
						.getColor(R.color.unchoose_color)) {
					doubanTextView.setTextColor(getResources().getColor(
							R.color.choose_color));
				} else {
					doubanTextView.setTextColor(getResources().getColor(
							R.color.unchoose_color));
				}
			}

				break;
			// 点击发送
			case R.id.submit_send_textView: {
				Log.d("gy", "click");
				if (classificationTextView
						.getText()
						.toString()
						.equals(getString(R.string.submit_classificationOriginalString))) {
					showToastMessage("请选择视频分类");
				} else if (positionTextView.getText().toString()
						.equals(getString(R.string.getposition))) {
					showToastMessage("正在获取位置信息,稍等片刻。。");
				} else {
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						File file = new File(path);
						filenameString = file.getName();
						File image = new File(imagePath);
						imagenameString = image.getName();
						if (file.exists()) {
							uploadbar.setMax((int) file.length());
							uploadbar.setVisibility(View.VISIBLE);
							uploadImage(image);
							uploadFile(file);
						} else {
							handler.sendMessage(handler.obtainMessage(5,
									getString(R.string.notexsit)));
						}
					} else {

						handler.sendMessage(handler.obtainMessage(5,
								getString(R.string.sdcarderror)));
					}

				}
			}

				break;
			case R.id.submit_cancel_textView: {
				finish();
			}

				break;
			default:
				break;

			}
		}

		public void onComplete(Platform plat, int action,
				HashMap<String, Object> res) {
			Message msg = new Message();
			msg.arg1 = 1;
			msg.arg2 = action;
			msg.obj = plat;
			UIHandler.sendMessage(msg, this);
		}

		public void onError(Platform plat, int action, Throwable t) {
			t.printStackTrace();

			Message msg = new Message();
			msg.arg1 = 2;
			msg.arg2 = action;
			msg.obj = plat;
			UIHandler.sendMessage(msg, this);
		}

		public void onCancel(Platform plat, int action) {
			Message msg = new Message();
			msg.arg1 = 3;
			msg.arg2 = action;
			msg.obj = plat;
			UIHandler.sendMessage(msg, this);
		}

		public boolean handleMessage(Message msg) {
			LoginActivity activity;
			Platform plat = (Platform) msg.obj;
			String text = null;
			switch (msg.arg1) {
			case 1: {
				// 成功
				text = plat.getName() + " 授权成功 ";
				if (plat.getName().equals("SinaWeibo"))
					sinax = true;
				if (plat.getName().equals("TecentWeibo"))
					tecentx = true;
				if (plat.getName().equals("Renren"))
					renrenx = true;
				if (plat.getName().equals("Douban"))
					doubanx = true;
				Toast.makeText(SubmitActivity.this, text, Toast.LENGTH_SHORT)
						.show();
			}
				break;
			case 2: {
				// 失败

				text = plat.getName() + " 授权失败 ";
				Toast.makeText(SubmitActivity.this, text, Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			case 3: {
				// 取消
				text = plat.getName() + " 授权取消 ";

				if (plat.getName().equals("SinaWeibo")) {
					sinax = false;
					sinaweiboTextView.setTextColor(getResources().getColor(
							R.color.unchoose_color));
				}
				if (plat.getName().equals("TecentWeibo")) {
					tecentx = false;
					tencentweiboTextView.setTextColor(getResources().getColor(
							R.color.unchoose_color));
				}
				if (plat.getName().equals("Renren")) {
					renrenx = false;
					renrenTextView.setTextColor(getResources().getColor(
							R.color.unchoose_color));
				}
				if (plat.getName().equals("Douban")) {
					doubanx = false;
					doubanTextView.setTextColor(getResources().getColor(
							R.color.unchoose_color));
				}

				Toast.makeText(SubmitActivity.this, text, Toast.LENGTH_SHORT)
						.show();
				return false;
			}
			}

			return false;
		}
	}

	/**
	 * 
	 * 发送视频信息到服务器
	 */
	private void postVedioInfo() throws Exception {

		new Thread(new Runnable() {
			public void run() {
				HashMap<String, String> map = new HashMap<String, String>();

				try {
					map.put("userid", String.valueOf(FinalUrl.userid));
					map.put("username", FinalUrl.username);
					map.put("filename", filenameString);
					map.put("imagename", imagenameString);
					map.put("videoIntroduction", editText.getText().toString());
					map.put("videoIsPublic", isPublicTextView.getText()
							.toString());
					if (isCompanyTextView.getText().toString().equals("无")) {
						map.put("iscommunty", "0");
					} else if (isCompanyTextView.getText().toString()
							.equals("企业")) {
						map.put("iscommunty", "1");
					} else if (isCompanyTextView.getText().toString()
							.equals("高校")) {
						map.put("iscommunty", "2");
					}
					map.put("videoClassfication", channelMap
							.get(classificationTextView.getText().toString()));
					map.put("videoShareGroup", shareGroupId);
					map.put("videoPosition", positionTextView.getText()
							.toString());
					String result = HttpUtils.postRequest(FinalUrl.LogVideoUrl,
							map);
					handler.sendMessage(handler.obtainMessage(3));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

	/**
	 * 初始化弹出窗口
	 * 
	 * @param which
	 *            初始化哪个弹出窗口
	 */
	private void initPopwindow(int which) {
		switch (which) {
		// 初始化分类弹窗
		case CLASSFICATION_POP: {
			classficationlist = new ArrayList<String>(channelMap.keySet());
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					SubmitActivity.this, R.layout.choose_classfication_item,
					R.id.choose_classfication_item_textView, classficationlist);
			choose_classfication_popupWindow = new ChoosePop(
					R.layout.choose_classfication_popwindow,
					SubmitActivity.this, handler, adapter, CLASSFICATION_POP);

		}
			break;
		case ChooseGroup_POP: {
			grouplist = new ArrayList<String>(groupMap.keySet());
			ArrayAdapter<String> groupadapter = new ArrayAdapter<String>(
					SubmitActivity.this, R.layout.choose_classfication_item,
					R.id.choose_classfication_item_textView, grouplist);
			choose_group_popWindow = new ChoosePop(
					R.layout.choose_classfication_popwindow,
					SubmitActivity.this, handler, groupadapter, ChooseGroup_POP);
		}
		default:
			break;
		}
	}

	/**
	 * 显示弹出窗口
	 * 
	 * @param which
	 *            显示哪个弹出窗口
	 */
	private void showPopwindow(int which) {
		switch (which) {
		case CLASSFICATION_POP: {
			int[] location = new int[2];
			classificationTextView.getLocationOnScreen(location);
			if (choose_classfication_popupWindow == null) {
				initPopwindow(CLASSFICATION_POP);

			}
			Log.i("classificationTextView", classificationTextView.toString());
			Log.i("choose_classfication_popupWindow",
					choose_classfication_popupWindow.toString());
			choose_classfication_popupWindow.showAtLocation(addClassification,
					Gravity.NO_GRAVITY, location[0], location[1]
							+ classificationTextView.getHeight());
		}
			break;
		case ChooseGroup_POP: {
			int[] location = new int[2];
			addShareGroupTextView.getLocationOnScreen(location);
			if (choose_group_popWindow == null) {
				initPopwindow(ChooseGroup_POP);

			}

			choose_group_popWindow.showAtLocation(chooseGroup,
					Gravity.NO_GRAVITY, location[0], location[1]
							+ addShareGroupTextView.getHeight());
		}

		default:
			break;
		}
	}

	/**
	 * 上传视频文件到服务器
	 * 
	 * @param file
	 *            视频文件
	 */
	private void uploadFile(final File file) {

		// if(!isUpload)
		// {
		isUpload = true;

		new Thread(new Runnable() {
			public void run() {
				try {

					Log.i("upLoad", "upLoad");
					Log.i("filenameString", filenameString);
					String sourceid = service.getBindId(file);
					Socket socket = new Socket(FinalUrl.IP, 9900);
					OutputStream outStream = socket.getOutputStream();
					String head = "Content-Length=" + file.length()
							+ ";filename=" + file.getName() + ";sourceid="
							+ (sourceid != null ? sourceid : "")
							+ ";type=video" + "\r\n";
					outStream.write(head.getBytes());

					PushbackInputStream inStream = new PushbackInputStream(
							socket.getInputStream());
					String response = StreamTool.readLine(inStream);
					String[] items = response.split(";");
					String responseSourceid = items[0].substring(items[0]
							.indexOf("=") + 1);
					String position = items[1].substring(items[1].indexOf("=") + 1);
					if (sourceid == null) {// 如果是第一次上传文件，在数据库中不存在该文件所绑定的资源id
						service.save(responseSourceid, file);
					}
					RandomAccessFile fileOutStream = new RandomAccessFile(file,
							"r");
					fileOutStream.seek(Integer.valueOf(position));
					byte[] buffer = new byte[1024];
					int len = -1;
					int length = Integer.valueOf(position);
					while ((len = fileOutStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						length += len;// 累加已经上传的数据长度
						Message msg = new Message();
						msg.getData().putInt("length", length);
						msg.what = 1;
						handler.sendMessage(msg);
					}
					if (length == file.length())
						service.delete(file);
					fileOutStream.close();
					outStream.close();
					inStream.close();
					socket.close();

					handler.sendMessage(handler.obtainMessage(2));
				} catch (Exception e) {

					handler.sendMessage(handler.obtainMessage(5,
							getString(R.string.error)));
				}
			}
		}).start();
		// }

	}

	/**
	 * 上传图片到服务器
	 * 
	 * @param image
	 *            图片文件
	 */
	private void uploadImage(final File image) {
		/*
		 * if(!isUpload) { isUpload=true;
		 */

		new Thread(new Runnable() {
			public void run() {
				try {

					Log.i("upLoad", "upLoad");
					Log.i("imagenameString", imagenameString);
					String sourceid = service.getBindId(image);
					Socket socket = new Socket(FinalUrl.IP, 9900);
					OutputStream outStream = socket.getOutputStream();
					String head = "Content-Length=" + image.length()
							+ ";filename=" + image.getName() + ";sourceid="
							+ (sourceid != null ? sourceid : "")
							+ ";type=videoimage" + "\r\n";
					outStream.write(head.getBytes());

					PushbackInputStream inStream = new PushbackInputStream(
							socket.getInputStream());
					String response = StreamTool.readLine(inStream);
					String[] items = response.split(";");
					String responseSourceid = items[0].substring(items[0]
							.indexOf("=") + 1);
					String position = items[1].substring(items[1].indexOf("=") + 1);
					if (sourceid == null) {// 如果是第一次上传文件，在数据库中不存在该文件所绑定的资源id
						service.save(responseSourceid, image);
					}
					RandomAccessFile fileOutStream = new RandomAccessFile(
							image, "r");
					fileOutStream.seek(Integer.valueOf(position));
					byte[] buffer = new byte[1024];
					int len = -1;
					int length = Integer.valueOf(position);
					while ((len = fileOutStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, len);
						length += len;// 累加已经上传的数据长度
						Message msg = new Message();
						msg.getData().putInt("length", length);
						msg.what = 1;
						// handler.sendMessage(msg);
					}
					if (length == image.length())
						service.delete(image);
					fileOutStream.close();
					outStream.close();
					inStream.close();
					socket.close();

					// handler.sendMessage(handler.obtainMessage(2));
				} catch (Exception e) {

					// handler.sendMessage(handler.obtainMessage(5,getString(R.string.error)));
				}
			}
		}).start();
		// }
	}

	private void showShare(boolean silent, String platform, boolean captureView) {
		final OnekeyShare oks = new OnekeyShare();
		// 分享时Notification的图标和文字
		oks.setNotification(R.drawable.ic_launcher, "VGroup");
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("VGroup");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(FinalUrl.PLAYVIDEO_URL + filenameString);
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我用VGroup分享了视频." + FinalUrl.PLAYVIDEO_URL + filenameString);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("123");
		// oks.setImageUrl(FinalUrl.VIDEOIMAGE_URL+imagenameString);

		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(FinalUrl.PLAYVIDEO_URL + filenameString);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我用VGroup分享了视频.");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("VGroup");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://www.baidu.com");
		oks.setLatitude(23.056081f);
		oks.setLongitude(113.385708f);
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		oks.setDialogMode();
		// 取消自动授权
		oks.disableSSOWhenAuthorize();
		// 隐藏客户端
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
		oks.show(SubmitActivity.this);

	}

	// 从服务器获得数据，获取视频分类
	private void initVedioClassification() {
		channelMap = new HashMap<String, String>();
		new Thread(new Runnable() {
			public void run() {
				String result = HttpUtils
						.getRequest(FinalUrl.GetClassficationUrl);

				JSONArray array;
				try {
					array = new JSONObject(result).getJSONArray("channellist");
					for (int i = 0; i < array.length(); i++) {
						JSONObject channeljsonObject = array.getJSONObject(i);

						channelMap.put(String.valueOf(channeljsonObject
								.get("channelname")), String
								.valueOf(channeljsonObject.get("id")));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				handler.sendMessage(handler.obtainMessage(GET_CLASSFICATION));
			}
		}).start();
	}

	// 从服务器获取群组信息初始化
	private void initGroup() {
		groupMap = new HashMap<String, String>();
		new Thread(new Runnable() {
			public void run() {
				HashMap<String, String> postmapHashMap = new HashMap<String, String>();
				postmapHashMap.put("userid", String.valueOf(FinalUrl.userid));
				String result = null;
				try {
					result = HttpUtils.postRequest(FinalUrl.GetGroupListUrl,
							postmapHashMap);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				JSONArray array;
				try {
					array = new JSONObject(result).getJSONArray("grouplist");
					for (int i = 0; i < array.length(); i++) {
						JSONObject groupjsonObject = array.getJSONObject(i);
						groupMap.put(
								String.valueOf(groupjsonObject.get("name")),
								String.valueOf(groupjsonObject.get("id")));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 显示提示
	public void showToastMessage(String msg) {
		Toast.makeText(SubmitActivity.this, msg, Toast.LENGTH_LONG).show();
	}

	public void jumpActivity(Class<?> cls) {
		Log.i("jumpActivity0", "jumpActivity0000000");
		// 查看具体内容
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	/**
	 * 初始化定位设置
	 */
	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 5000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		mLocationClient.setLocOption(option);
	}

}
