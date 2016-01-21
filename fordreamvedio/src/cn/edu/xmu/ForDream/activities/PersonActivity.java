package cn.edu.xmu.ForDream.activities;

import cn.edu.xmu.ForDream.bean.Mymedia;
import cn.edu.xmu.ForDream.util.AsynImageLoader;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonActivity extends Activity {
	//个人主页资料
	private SurfaceView surfaceView;
	private Mymedia usermymedia;
	private ImageView videoImageView;
	private ImageView userImageView;
	private TextView usernickname;
	private TextView userschool;
	private TextView userconcerns;
	private TextView userfans;
	private TextView uservideos;
	private TextView usershares;
	private TextView userlikes;
	private TextView usercollects; 
	//图片加载
	private AsynImageLoader asynImageLoader;
	
	
    private int pageSize = 5;//每次获取多少条数据
    private int maxpage = 10;//总共有多少页
    private boolean loadfinish = true;
    private ProgressDialog progressDialog;
}
