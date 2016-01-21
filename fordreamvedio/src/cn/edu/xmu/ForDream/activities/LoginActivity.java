package cn.edu.xmu.ForDream.activities;

import java.io.DataInputStream; 
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.friend.WXMessage;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.HttpUtils;
import cn.sharesdk.douban.Douban;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class LoginActivity extends Activity implements Callback {
	private final static int LOGIN_SUCCESSED = 0x01;
	private final static int LOGIN_FAILED = 0x02;
	
	private final static int AUTHOLOGIN_SUCCESSED = 0x03;
	private final static int AUTHOLOGIN_FAILED = 0x04;
	
	public EditText usernameEditText;
	public EditText passwordEditText;
	public Button login_button;
	public Button login_reg_button;
	public ProgressDialog progressDialog;
	public int pwdflag = 0;// 尚未注册
	public String reason = "nothing";
	public String message = "nothing";
	public ImageView login_weibo_iv;
	public ImageView login_qq_iv;
	public ImageView login_facebook_iv;
	public ImageView login_weixin_iv;
	String openId,nickname;
	Platform weibo1,weibo2,weibo3,weibo4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		handler = new MyHandler(this);
		usernameEditText = (EditText) this.findViewById(R.id.login_username);
		passwordEditText = (EditText) this.findViewById(R.id.login_password);
		usernameEditText.setText("kingsely");
		passwordEditText.setText("yuntianqing");
		login_button = (Button) this.findViewById(R.id.login_btn);
		login_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hideSoftInput(passwordEditText);
				login();
			}
		});

		login_reg_button = (Button) this.findViewById(R.id.login_reg_btn);
		login_reg_button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hideSoftInput(usernameEditText);
				hideSoftInput(passwordEditText);
				jumpActivity(RegActivity.class); 
				//closeCurrentActivity();//一旦close就没有这个页面了
			}
		});
		// 第三方登陆
		login_weibo_iv = (ImageView) this.findViewById(R.id.login_weibo_iv);
		login_weibo_iv.setOnClickListener(new MyImageviewListener());
		login_qq_iv = (ImageView) this.findViewById(R.id.login_qq_iv);
		login_qq_iv.setOnClickListener(new MyImageviewListener());
		login_facebook_iv = (ImageView) this
				.findViewById(R.id.login_facebook_iv);
		login_facebook_iv.setOnClickListener(new MyImageviewListener());
		login_weixin_iv = (ImageView) this.findViewById(R.id.login_weixin_iv);
		login_weixin_iv.setOnClickListener(new MyImageviewListener());
		
		//开启SDK
		 ShareSDK.initSDK(this);

	}

	private class MyImageviewListener implements OnClickListener, PlatformActionListener, Callback {
		public void onClick(View v) {
			if (v == login_weibo_iv) {
				weibo1= ShareSDK.getPlatform(SinaWeibo.NAME);
				weibo1.setPlatformActionListener(this);
				weibo1.authorize();
			}
			if (v == login_qq_iv) {
				weibo2= ShareSDK.getPlatform(QZone.NAME);
				weibo2.setPlatformActionListener(this);
				weibo2.authorize();
			}
			if (v == login_facebook_iv) {
				weibo3= ShareSDK.getPlatform(Renren.NAME);
				weibo3.setPlatformActionListener(this);
				weibo3.authorize();
			}
			if (v == login_weixin_iv) {
				weibo4= ShareSDK.getPlatform(Douban.NAME);
				weibo4.setPlatformActionListener(this);
				weibo4.authorize();
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
			String text =null;
			switch (msg.arg1) {
				case 1: {
					// 成功
					openId = plat.getDb().getUserId(); // 获取用户在此平台的ID
					nickname = plat.getDb().get("nickname"); //获取用户在此平台的昵称
					text = plat.getName() + " 授权成功 " ;
					Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
					authologin();
				}
				break;
				case 2: {
					// 失败

					text = plat.getName() + " 授权取消 " ;
					Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
					return false;
				}
				case 3: {
					// 取消
					text = plat.getName() + " 授权失败 " ;
					Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
					return false;
				}
			}

			return false;
		}
	}

	private static class MyHandler extends Handler {
		private LoginActivity activity;

		public MyHandler(LoginActivity activity) {
			this.activity = activity;
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case LOGIN_SUCCESSED:
				activity.loginSuccessedOperator();
				break;
			case LOGIN_FAILED:
				activity.loginFailedOperator();
				break;
			case AUTHOLOGIN_SUCCESSED:
				activity.authologinSuccessedOperator();
				break;
			case AUTHOLOGIN_FAILED:
				activity.autologinFailedOperator();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	private MyHandler handler;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	/**
	 * 第三方登录
	 */
	public void authologin() {

		Runnable runnable = new Runnable() {

			public void run() {
				boolean success = authologin(openId,nickname);
				System.out.println("authologin");
				Message msg = handler.obtainMessage();
				msg.what = success ? AUTHOLOGIN_SUCCESSED : AUTHOLOGIN_FAILED;
				handler.sendMessage(msg);

			}
		};
		new Thread(runnable).start();
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在登陆中...");
		progressDialog.show();

	}
	
	/**
	 * 登录操作，无网络访问部分
	 */
	public void login() {
		final String username = usernameEditText.getText().toString();
		if (username.equals("")) {
			showToastMessage("用户名不能为空");
			return;
		}

		final String password = passwordEditText.getText().toString();
		if (password.equals("")) {
			showToastMessage("密码不能为空");
			return;
		}

		Runnable runnable = new Runnable() {

			public void run() {
				boolean success = login(username, password);
				Message msg = handler.obtainMessage();
				msg.what = success ? LOGIN_SUCCESSED : LOGIN_FAILED;
				handler.sendMessage(msg);
			}
		};
		new Thread(runnable).start();
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在登陆中...");
		progressDialog.show();

	}

	/**
	 *  短时间显示提示
	 * @param msg
	 */
	public void showToastMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 *  长时间显示提示
	 * @param msg
	 */
	public void showToastMessageLongTime(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 关闭访问ing图标
	 */
	public void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	/**
	 * 登录成功..系统内部登录
	 */
	private void loginSuccessedOperator() {
		closeProgressDialog();
		showToastMessage("登陆成功~~");
		showToastMessageLongTime("用户名："+FinalUrl.username+",id:"+FinalUrl.userid);
		
		Intent intent = new Intent(this, SquareActivity.class);
		Bundle b = new Bundle();
		b.putString("username", FinalUrl.username);
		b.putInt("userid",FinalUrl.userid);
		intent.putExtras(b);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		//finish();
		//jumpActivity(SquareActivity.class);
	}

	/**
	 * 第三方登录成功....完善资料是什么鬼...
	 */
	private void authologinSuccessedOperator() {
		closeProgressDialog();
		showToastMessage("登陆成功,请完善详细资料~~");
		//showToastMessageLongTime("用户名："+FinalUrl.username+",id:"+FinalUrl.userid);
		
		Intent intent = new Intent(this, AuthoRegActivity.class);
		Bundle b = new Bundle();
		b.putString("username",openId);
		b.putString("nickname",nickname);
		b.putInt("userid",FinalUrl.userid);
		intent.putExtras(b);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		//finish();
		//jumpActivity(SquareActivity.class);
	}
	
	/**
	 * 登录失败操作
	 */
	private void loginFailedOperator() {
		closeProgressDialog();
		if (pwdflag == 1)
			showToastMessageLongTime("登陆失败，密码错误！");
		else
			showToastMessageLongTime("登陆失败," + reason);
	}

	/**
	 * 授权登录操作？？？为什么是登录成功...
	 */
	private void autologinFailedOperator() {
		closeProgressDialog();
		showToastMessage("登陆成功~~");
		showToastMessageLongTime("用户名："+nickname+",id:"+FinalUrl.userid);
		
		Intent intent = new Intent(this, SquareActivity.class);
		Bundle b = new Bundle();
		b.putString("username", openId);
		b.putInt("userid",FinalUrl.userid);
		intent.putExtras(b);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}	
	
	/**
	 * 登录   网络访问
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean login(String username, String password) {
		boolean success = false;
		HashMap<String, String> map=new HashMap<String, String>();
		String result=null;
		map.put("username",username);
		map.put("password",password);
	    try {
			result=HttpUtils.postRequest(FinalUrl.LoginUrl,map);
			Log.i("tag",result);
			JSONObject object=new JSONObject(result);
			if(object.getString("message").equals("登陆成功"))
			{
				success = true;
				FinalUrl.userid=object.getInt("userid");
				FinalUrl.username=object.getString("username");
				FinalUrl.userimage=FinalUrl.USERIMAGE_URL+object.getString("userimage");
				FinalUrl.usernickname=object.getString("nickname");
			} 
			else if (result.contains("登录失败")) {
				reason = object.getString("reason");
				success = false;
				pwdflag = 1;
			} else {
				reason = object.getString("reason");
				success = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;

	}

	
	/**
	 * 第三方登录
	 * @param openId
	 * @param nickname
	 * @return
	 */
	public boolean authologin(String openId,String nickname) {
		boolean success = false;
		HashMap<String, String> map=new HashMap<String, String>();
		String result=null;
		map.put("openId",openId);
		map.put("nickname",nickname);
	    try {
			result=HttpUtils.postRequest(FinalUrl.LoginATUrl,map);
			JSONObject object=new JSONObject(result);
			int i=object.getInt("message");
			if(i==1)
			{
				success = true;
				FinalUrl.userid=object.getInt("userid");;
				FinalUrl.username=openId;
				FinalUrl.userimage=FinalUrl.USERIMAGE_URL+object.getString("userimage");
				FinalUrl.usernickname=object.getString("nickname");
			} else if (i==0) {
				success = false;
				FinalUrl.userid=object.getInt("userid");;
				FinalUrl.username=openId;
				FinalUrl.userimage=FinalUrl.USERIMAGE_URL+object.getString("userimage");
				FinalUrl.usernickname=object.getString("nickname");
			} else {
				reason = object.getString("reason");
				success = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;

	}
	
	/**
	 * 隐藏软键盘
	 * @param eText
	 */
	public void hideSoftInput(EditText eText) {
		// 一般情况下，都是在点击某个View的时候，如果键盘没有关闭的时候，自动的去隐藏键盘
		final InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Activity.INPUT_METHOD_SERVICE);// 得到键盘或设置键盘相关信息
		imm.hideSoftInputFromWindow(eText.getWindowToken(), 0);// //隐藏软键盘
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// progressDialog.dismiss();
		ShareSDK.stopSDK();
		super.onDestroy();
	}

	/**
	 *  页面跳转
	 * @param cls
	 */
	public void jumpActivity(Class<?> cls) {
		// 查看具体内容
		Intent intent = new Intent(this, cls);
		// Bundle b = new Bundle();
		// b.putString("videofilename", videofilename);
		// intent.putExtras(b);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	/**
	 *  关闭当前页面
	 */
	public void closeCurrentActivity() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_out);
	}

	
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
