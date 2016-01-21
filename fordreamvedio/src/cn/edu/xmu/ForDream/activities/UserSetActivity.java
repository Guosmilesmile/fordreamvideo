package cn.edu.xmu.ForDream.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.model.ViewItem;
import br.com.dina.ui.widget.UITableView;
import cn.edu.xmu.ForDream.R;

public class UserSetActivity extends Activity implements OnClickListener{
	private ImageView returnImageView;
	UITableView tableView;
	UITableView toptableView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_set);
        returnImageView = (ImageView) findViewById(R.id.userset_return);
        returnImageView.setOnClickListener(this);
        
        tableView = (UITableView) findViewById(R.id.user_set_tableView);
        TVCustomClickListener tvlistener = new TVCustomClickListener();
    	tableView.setClickListener(tvlistener);
        
        toptableView = (UITableView) findViewById(R.id.top_user_set_tableView);
        toptableView.setClickListener(new br.com.dina.ui.widget.UITableView.ClickListener() {
			
			public void onClick(int index) {
				switch(index){
				case 0:
					Intent intent3 = new Intent(UserSetActivity.this, SetActivity.class);
					startActivity(intent3);
					finish();
					break;
				}
			}
		});
        createList();
        
        toptableView.commit();
        tableView.commit();
	}

	private void createList() {
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.user_set_custom_view, null);
		ViewItem viewItem = new ViewItem(view);
		toptableView.addViewItem(viewItem);
		
		BasicItem i1 = new BasicItem("草稿箱");
    	i1.setDrawable(R.drawable.user_set_icon7);   	
    	tableView.addBasicItem(i1);
    	BasicItem i2 = new BasicItem("关注");
    	i2.setDrawable(R.drawable.user_set_icon1);   	
    	tableView.addBasicItem(i2);
    	BasicItem i3 = new BasicItem("粉丝");
    	i3.setDrawable(R.drawable.user_set_icon3);   	
    	tableView.addBasicItem(i3);
    	BasicItem i4 = new BasicItem("新的朋友");
    	i4.setDrawable(R.drawable.user_set_icon6);   	
    	tableView.addBasicItem(i4);
    	BasicItem i5 = new BasicItem("手机通讯录好友");
    	i5.setDrawable(R.drawable.user_set_icon7);   	
    	tableView.addBasicItem(i5);
    	BasicItem i6 = new BasicItem("邀请好友");
    	i6.setDrawable(R.drawable.user_set_icon4);   	
    	tableView.addBasicItem(i6);
    	BasicItem i7 = new BasicItem("我的二维码");
    	i7.setDrawable(R.drawable.user_set_icon2);   	
    	tableView.addBasicItem(i7);
    	BasicItem i8 = new BasicItem("设置");
    	i8.setDrawable(R.drawable.user_set_icon5);   	
    	tableView.addBasicItem(i8);
		
    	/*tableView.addBasicItem(R.drawable.user_image, "Example 1", "Summary text 1");
    	tableView.addBasicItem(new BasicItem("Disabled item", "this is a disabled item", false));*/
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.userset_return:
			Intent intent0 = new Intent(this, SquareActivity.class);
			startActivity(intent0);
			finish();
			break;
		/*case R.id.user_set:
			Intent intent1 = new Intent(this, SetActivity.class);
			startActivity(intent1);
			finish();
			break;*/
		default:
			break;
		}
	}
	
	private class TVCustomClickListener implements br.com.dina.ui.widget.UITableView.ClickListener {

		public void onClick(int index) {
			switch(index){
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			}
		}
		
	}
}
