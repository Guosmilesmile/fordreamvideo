package cn.edu.xmu.ForDream.popWindows;

import cn.edu.xmu.ForDream.R; 
import cn.edu.xmu.ForDream.Enum.PositionEnum;
import cn.edu.xmu.ForDream.util.FinalName;
import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

public class MenuPop extends PopupWindow {
	private int resId;		
	private Context mContext;
	private View popView;
		
	private LayoutInflater inflater;
	private Handler handler;
	
	public MenuPop(int resId,Context context,Handler handler)
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
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		//setFocusable(true);
		setOutsideTouchable(true);
	
		LinearLayout popmenu_choose_square=(LinearLayout)popView.findViewById(R.id.popmenu_choose_square);
    	LinearLayout popmenu_choose_talkgroup=(LinearLayout)popView.findViewById(R.id.popmenu_choose_talkgroup);
    	LinearLayout popmenu_choose_friendgroup=(LinearLayout)popView.findViewById(R.id.popmenu_choose_friendgroup);
    	LinearLayout popmenu_choose_person=(LinearLayout)popView.findViewById(R.id.popmenu_choose_person);
    	LinearLayout popmenu_choose_blink=(LinearLayout)popView.findViewById(R.id.popmenu_choose_blink);
    	popmenu_choose_square.setOnClickListener(new MenuItemClick());
    	popmenu_choose_talkgroup.setOnClickListener(new MenuItemClick());
    	popmenu_choose_friendgroup.setOnClickListener(new MenuItemClick());
    	popmenu_choose_person.setOnClickListener(new MenuItemClick());
    	popmenu_choose_blink.setOnClickListener(new MenuItemClick());
    	
    	 
    /*	getContentView().setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int id=v.getId();
				
				if(id!=R.id.popmenu_choose_square&&id!=R.id.popmenu_choose_talkgroup
					&&id!=R.id.popmenu_choose_friendgroup&&id!=R.id.popmenu_choose_person
					&&id!=R.id.popmenu_choose_blink)
				{
					setFocusable(false);
				}
				else {
					setFocusable(true);
				}
				return true;
			}
		});*/
	}
	public void setCurrentPosition(PositionEnum currentposition)
	{
		ImageView imageView1=(ImageView)popView.findViewById(R.id.popmenu_selectimg1);
		ImageView imageView2=(ImageView)popView.findViewById(R.id.popmenu_selectimg2);
		ImageView imageView3=(ImageView)popView.findViewById(R.id.popmenu_selectimg3);
		ImageView imageView4=(ImageView)popView.findViewById(R.id.popmenu_selectimg4);
		ImageView imageView5=(ImageView)popView.findViewById(R.id.popmenu_selectimg5);
		
		imageView1.setVisibility(View.INVISIBLE);
		imageView2.setVisibility(View.INVISIBLE);
		imageView3.setVisibility(View.INVISIBLE);
		imageView4.setVisibility(View.INVISIBLE);
		imageView5.setVisibility(View.INVISIBLE);
		switch (currentposition) {
		case SQUARE:
		{
			imageView1.setVisibility(View.VISIBLE);
		}
			break;
		case FRIENDGROUP:
		{
			imageView2.setVisibility(View.VISIBLE);
		}
			break;
		case TALKGROUP:
		{
			imageView3.setVisibility(View.VISIBLE);
		}
			break;
		case BLINK:
		{
			imageView4.setVisibility(View.VISIBLE);
		}
			break;
		case PERSON:
		{
			imageView5.setVisibility(View.VISIBLE);
		}
			break;			
		default:
			break;
		}
	}
    private class MenuItemClick implements View.OnClickListener
    {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.popmenu_choose_square:
			{
				Log.i("popmenu_choose_square", "popmenu_choose_square");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_DATE, R.id.popmenu_choose_square));
			}
				break;
			case R.id.popmenu_choose_talkgroup:
			{
				Log.i("popmenu_choose_talkgroup", "popmenu_choose_talkgroup");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_DATE, R.id.popmenu_choose_talkgroup));
			}
				break;
			case R.id.popmenu_choose_friendgroup:
			{
				Log.i("popmenu_choose_friendgroup", "popmenu_choose_friendgroup");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_DATE, R.id.popmenu_choose_friendgroup));
			}
				break;
			case R.id.popmenu_choose_person:
			{
				Log.i("popmenu_choose_person", "popmenu_choose_person");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_DATE, R.id.popmenu_choose_person));
			}
				break;	
			case R.id.popmenu_choose_blink:
			{
				Log.i("popmenu_choose_blink", "popmenu_choose_blink");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_DATE, R.id.popmenu_choose_blink));
			}
				break;
			default:
				break;
			}
		}
    	
    }
}
