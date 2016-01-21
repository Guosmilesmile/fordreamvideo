package cn.edu.xmu.ForDream.activities;

import info.hoang8f.widget.FButton;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import br.com.dina.ui.model.ViewItem;
import br.com.dina.ui.widget.UIButton;
import br.com.dina.ui.widget.UITableView;
import cn.edu.xmu.ForDream.R;

public class SetActivity extends Activity{
	UITableView tableView;
	FButton exitBtn;
	UIButton accountboundBtn;
	ImageView returnBtn;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set);
        
        tableView = (UITableView) findViewById(R.id.set_tableView);
        TVCustomClickListener tvlistener = new TVCustomClickListener();
    	tableView.setClickListener(tvlistener);
    	createList();
        tableView.commit();
        
        BtnCustomClickListener btnlistener = new BtnCustomClickListener();
        accountboundBtn = (UIButton) findViewById(R.id.account_bound_Button);
        accountboundBtn.addClickListener(btnlistener);
        
        FIBtnCustomClickListener filistener = new FIBtnCustomClickListener();
        exitBtn = (FButton) findViewById(R.id.exit_Button);
        exitBtn.setOnClickListener(filistener);
        
        returnBtn = (ImageView) findViewById(R.id.set_return);
        returnBtn.setOnClickListener(filistener);
	}
	
	private void createList() {
		tableView.addBasicItem("播放和上传设置");
		tableView.addBasicItem("隐私设置");
		tableView.addBasicItem("关于软件");
		tableView.addBasicItem("意见反馈");
		
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.set_custom_view, null);
		ViewItem viewItem = new ViewItem(view);
		tableView.addViewItem(viewItem);
		
    	/*tableView.addBasicItem(R.drawable.user_image, "Example 1", "Summary text 1");
    	tableView.addBasicItem(new BasicItem("Disabled item", "this is a disabled item", false));*/
	}
	
	private class TVCustomClickListener implements br.com.dina.ui.widget.UITableView.ClickListener {

		public void onClick(int index) {
			switch(index){
			//播放和上传设置
			case 0:
				break;
			//隐私设置
			case 1:
				break;
			//关于软件
			case 2:
				break;
			//意见反馈
			case 3:
				break;
			//清除缓存
			case 4:
				break;
			}
		}
		
	}
	
	private class BtnCustomClickListener implements br.com.dina.ui.widget.UIButton.ClickListener {

		public void onClick(View view) {
			switch(view.getId()){
			//帐号绑定事件
			case R.id.account_bound_Button:
				break;
			}
		}
		
	}
	
	private class FIBtnCustomClickListener implements View.OnClickListener{

		public void onClick(View v) {
			switch(v.getId()){
			//退出登录事件
			case R.id.exit_Button:
				finish();
				break;
			//返回上一级事件
			case R.id.set_return:
				jumpActivity(UserSetActivity.class);
				finish();
				break;
			}
		}
		
	}
	
	public void jumpActivity(Class<?> cls) {
		// 查看具体内容
		Intent intent = new Intent(this, cls);
		Bundle b = new Bundle();
        //b.putString("videoid", listItem.get(position).get(key));
        intent.putExtras(b);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
