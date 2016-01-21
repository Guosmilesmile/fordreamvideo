package cn.edu.xmu.ForDream.activities;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.util.FinalUrl;

/**
 * 注册页面
 * @author Christy
 *
 */
public class RegActivity extends Activity {
	private final static int REG_SUCCESSED = 0x01;
	private final static int REG_FAILED = 0x02;
	public EditText usernameEditText;
	public EditText passwordEditText;
	public EditText nicknameEditText;
	public EditText schoolnameEditText;
	public Button button;
	public ProgressDialog progressDialog;
	public int regflag=0;//尚未注册
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.reg);
        handler = new MyHandler(this);
        usernameEditText=(EditText)this.findViewById(R.id.reg_username);
        passwordEditText=(EditText)this.findViewById(R.id.reg_password);
        nicknameEditText=(EditText)this.findViewById(R.id.reg_nickname);
        schoolnameEditText=(EditText)this.findViewById(R.id.reg_schoolname);
        //usernameEditText.setText("yun");
        //passwordEditText.setText("123456");
        //nicknameEditText.setText("lala");
        button=(Button)this.findViewById(R.id.reg_btn);
        button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				hideSoftInput(nicknameEditText);
				reg();
			}
		});
        
        
    }
    
    private static class MyHandler extends Handler {
		private RegActivity activity;

		public MyHandler(RegActivity activity) {
			this.activity = activity;
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case REG_SUCCESSED:
				activity.regSuccessedOperator();
				break;
			case REG_FAILED:
				activity.regFailedOperator();
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
     * 注册无网络操作
     */
    public void reg()
    {
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
		
		final String nickname = nicknameEditText.getText().toString();
		if (nickname.equals("")) {
			showToastMessage("昵称不能为空");
			return;
		}
		final String schoolname = schoolnameEditText.getText().toString();
		if (nickname.equals("")) {
			showToastMessage("学校名不能为空");
			return;
		}
		
		Runnable runnable = new Runnable(){
	        
	        public void run() {
	        	boolean success=reg(username,password,nickname,schoolname);
	        	Message msg = handler.obtainMessage();
				msg.what = success ? REG_SUCCESSED : REG_FAILED;
				handler.sendMessage(msg);
	        }
	    };
	    new Thread(runnable).start();
	    progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在注册中...");
        progressDialog.show();
        
    }
    
    
    //短时间显示提示
  	public void showToastMessage(String msg) {
  		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  	}
  	//长时间显示提示
  	public void showToastMessageLongTime(String msg) {
  		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
  	}
  	public void closeProgressDialog() {
  		if(progressDialog!=null)
  			progressDialog.dismiss();
	}
  	private void regSuccessedOperator() {
		closeProgressDialog();
		showToastMessageLongTime("注册成功~~");
	}

	private void regFailedOperator() {
		closeProgressDialog();
		if(regflag==1)
			showToastMessageLongTime("注册失败，该用户名已被注册！");
		else 
			showToastMessageLongTime("注册失败，请检查原因!");
	}
    
    
    /**
     * 注册网络操作
     * @param username
     * @param password
     * @param nickname
     * @param schoolname
     * @return
     */
    public boolean reg(String username,String password,String nickname,String schoolname) {
    	String path = FinalUrl.RegUrl;
    	boolean success = true;
		HttpURLConnection conn = null;
		
			String loginContent = "username=" + username+"&password="+password+"&nickname="+nickname+"&schoolname="+schoolname ;
			URL url;
			try {
				url = new URL(path);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");

				//写入注册信息
				OutputStream output = conn.getOutputStream();
				output.write(loginContent.getBytes());
				output.flush();
				output.close();

				

				//得到html
				InputStream input = conn.getInputStream();
				DataInputStream dis = new DataInputStream(input);
				byte[] buf = new byte[100 * 1024];
				byte[] readBuf = new byte[20 * 1024];
				int len = 0;
				int offset = 0;
				while ((len = dis.read(readBuf)) != -1) {
					System.arraycopy(readBuf, 0, buf, offset, len);
					offset += len;
				}
				String requestResult = new String(buf, 0, offset, "UTF-8");
								
				if(requestResult.contains("注册成功")){
					success=true;
				}
				else if(requestResult.contains("该用户名已被注册"))
				{
					success=false;
					regflag=1;
				}
				else {
					success=false;
				}
				
				dis.close();
				buf = null;
				readBuf = null;
			} catch (Exception e) {
				success = false;
			} finally {
				try {
					conn.disconnect();
				} catch (Exception e) {
				}
			}
		return success;
	
    }
		
    public void hideSoftInput(EditText eText) {
		//一般情况下，都是在点击某个View的时候，如果键盘没有关闭的时候，自动的去隐藏键盘
		final InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);//得到键盘或设置键盘相关信息
		imm.hideSoftInputFromWindow(eText.getWindowToken(), 0);////隐藏软键盘
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//progressDialog.dismiss();
		super.onDestroy();
	}
    
    
}
