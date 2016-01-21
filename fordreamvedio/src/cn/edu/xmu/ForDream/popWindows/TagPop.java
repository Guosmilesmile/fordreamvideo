package cn.edu.xmu.ForDream.popWindows;

import info.hoang8f.widget.FButton;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.util.FinalName;

public class TagPop extends PopupWindow {
	private int resId;		
	private Context mContext;
	private View popView;
	private LayoutInflater inflater;
	private Handler handler;
	private int whichpop;
	private EditText editText;
	
	public TagPop(int resId,Context context, Handler handler)
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
		popView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setContentView(popView);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setFocusable(true);
		setOutsideTouchable(true);
		editText=(EditText)popView.findViewById(R.id.tagpop_edit);
		
		FButton imageButton=(FButton)popView.findViewById(R.id.tagpop_button);
		imageButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String string=editText.getText().toString();
				if(!string.equals(""))
				{
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_TAG_CHANGE,string));
				}
			}
		});
		

	}
	public int getWhichpop() {
		return whichpop;
	}
	public void setWhichpop(int whichpop) {
		this.whichpop = whichpop;
	}
	public void setEdit(String string)
	{
		editText.setText(string);
	}
}
