package cn.edu.xmu.ForDream.popWindows;

import info.hoang8f.widget.FButton;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.util.FinalName;

public class SearchPop extends PopupWindow {
	private int resId;		
	private Context mContext;
	private View popView;
	private LayoutInflater inflater;
	private Handler handler;
	
	public SearchPop(int resId,Context context, Handler handler)
	{
		super(context);
		this.resId = resId;
		mContext = context;
		this.handler=handler;
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		popView = inflater.inflate(this.resId, null);
		popView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(popView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setFocusable(true);
		setOutsideTouchable(true);
		final EditText editText=(EditText)popView.findViewById(R.id.searchpopwindow_searchEdit);

		editText.setOnEditorActionListener(new OnEditorActionListener() {  
			 

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_SEARCHDATE,editText.getText().toString()));
				}
				return false;
			}  
	    });  
		
		final FButton searchBtn = (FButton)popView.findViewById(R.id.searchpopwindow_searchimg);
		searchBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//搜索点击事件
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_SEARCHDATE,editText.getText().toString()));
			}
		});
	}
	private class popClick implements View.OnClickListener
	{

		public void onClick(View v) {

		}	
	}
}
