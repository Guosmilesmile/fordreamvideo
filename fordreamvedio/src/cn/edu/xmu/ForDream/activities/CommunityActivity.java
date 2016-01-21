package cn.edu.xmu.ForDream.activities;


import cn.edu.xmu.ForDream.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 社区专区
 * @author Christy
 *
 */
public class CommunityActivity extends Activity{
	private ImageView companyImageView;
	private ImageView schoolImageView;
	private ImageView findfriendImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.community_activity_main);
		//初始化菜单选择条
        companyImageView=(ImageView)findViewById(R.id.community_company_imageview);
        schoolImageView=(ImageView)findViewById(R.id.community_school_imageview);
       findfriendImageView=(ImageView)findViewById(R.id.community_findfriend_imageview);  
       companyImageView.setOnTouchListener(new OnTouchListener() {  
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(CommunityActivity.this, CompanySquareActivity.class);
			startActivity(intent);
			CommunityActivity.this.finish();
			return false;
		}  
       });  
        
        
	}
}
