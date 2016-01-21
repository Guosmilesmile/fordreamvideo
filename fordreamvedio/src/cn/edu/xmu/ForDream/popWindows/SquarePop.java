package cn.edu.xmu.ForDream.popWindows;

import java.util.HashMap;

import com.googlecode.javacv.cpp.opencv_features2d.MSER;

import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.PicUtil;
import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.storage.OnObbStateChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SquarePop extends PopupWindow {
	private int resId;		
	private Context mContext;
	private View popView;
	private LayoutInflater inflater;
	private Handler handler;
	private TextView pop7TextView;
	private TextView pop8TextView;
	private TextView pop9TextView;
	private TextView pop10TextView;
	private TextView pop11TextView;
	private TextView pop12TextView;
	
	public SquarePop(int resId,Context context,Handler handler)
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
		//setFocusable(true);
		
		
		
		setOutsideTouchable(true);
		
		FrameLayout pop1=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop1);
		FrameLayout pop2=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop2);
		FrameLayout pop3=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop3);
		FrameLayout pop4=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop4);
		FrameLayout pop5=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop5);
		FrameLayout pop6=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop6);
		FrameLayout pop7=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop7);
		FrameLayout pop8=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop8);
		FrameLayout pop9=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop9);
		FrameLayout pop10=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop10);
		FrameLayout pop11=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop11);
		FrameLayout pop12=(FrameLayout)popView.findViewById(R.id.square_popwindow_pop12);
		RelativeLayout square_popwindow=(RelativeLayout)popView.findViewById(R.id.square_popwindow_background);
		
		ImageView  pop1Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop1_img);
		ImageView  pop2Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop2_img);
		ImageView  pop3Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop3_img);
		ImageView  pop4Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop4_img);
		ImageView  pop5Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop5_img);
		ImageView  pop6Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop6_img);
		ImageView  pop7Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop7_img);
		ImageView  pop8Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop8_img);
		ImageView  pop9Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop9_img);
		ImageView  pop10Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop10_img);
		ImageView  pop11Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop11_img);
		ImageView  pop12Img=(ImageView)popView.findViewById(R.id.square_popwindow_pop12_img);
		
		pop7TextView=(TextView)popView.findViewById(R.id.square_popwindow_pop7_text);
		pop8TextView=(TextView)popView.findViewById(R.id.square_popwindow_pop8_text);
		pop9TextView=(TextView)popView.findViewById(R.id.square_popwindow_pop9_text);
		pop10TextView=(TextView)popView.findViewById(R.id.square_popwindow_pop10_text);
		pop11TextView=(TextView)popView.findViewById(R.id.square_popwindow_pop11_text);
		pop12TextView=(TextView)popView.findViewById(R.id.square_popwindow_pop12_text);

		
		pop1Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_bluepop));
		pop2Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_deeporangepop));
		pop3Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_yellowpop));
		pop4Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_coffeepop));
		pop5Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_bluepop));
		pop6Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_yellowpop));
		pop7Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_deeporangepop));
		pop8Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_pinkpop));
		pop9Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_pinkpop));
		pop10Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_coffeepop));
		pop11Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_orangepop));
		pop12Img.setImageBitmap(PicUtil.readBitMap(mContext, R.drawable.square_popwindow_greenpop));
		
		PopClick popClick=new PopClick();
		PopLongClick popLongClick=new PopLongClick();
		pop1.setOnLongClickListener(popLongClick);
		pop2.setOnLongClickListener(popLongClick);
		pop3.setOnLongClickListener(popLongClick);
		pop4.setOnLongClickListener(popLongClick);
		pop5.setOnLongClickListener(popLongClick);
		pop6.setOnLongClickListener(popLongClick);
		pop7.setOnLongClickListener(popLongClick);
		pop8.setOnLongClickListener(popLongClick);
		pop9.setOnLongClickListener(popLongClick);
		pop10.setOnLongClickListener(popLongClick);
		pop11.setOnLongClickListener(popLongClick);
		pop12.setOnLongClickListener(popLongClick);
		
		pop1.setOnClickListener(popClick);
		pop2.setOnClickListener(popClick);
		pop3.setOnClickListener(popClick);
		pop4.setOnClickListener(popClick);
		pop5.setOnClickListener(popClick);
		pop6.setOnClickListener(popClick);
		pop7.setOnClickListener(popClick);
		pop8.setOnClickListener(popClick);
		pop9.setOnClickListener(popClick);
		pop10.setOnClickListener(popClick);
		pop11.setOnClickListener(popClick);
		pop12.setOnClickListener(popClick);
		 
	}
	private class PopLongClick implements View.OnLongClickListener
	{

		public boolean onLongClick(View v) {
			//请求加载选定的广场中的类别，例如学霸，游戏等
			switch (v.getId()) {
			case R.id.square_popwindow_pop1:
			{
				
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"视频类别无法编辑哦。"));
			}
				
				break;
			case R.id.square_popwindow_pop2:
			{
				Log.i("LongClickPop","ClickPop2");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"视频类别无法编辑哦。"));
			}
				
				break;
			case R.id.square_popwindow_pop3:
			{
				Log.i("ClickPop","ClickPop3");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"视频类别无法编辑哦。"));
			}
				
				break;
			case R.id.square_popwindow_pop4:
			{
				Log.i("ClickPop","ClickPop4");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"视频类别无法编辑哦。"));
			}
				
				break;
			case R.id.square_popwindow_pop5:
			{
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"视频类别无法编辑哦。"));
			}
				
				break;
			case R.id.square_popwindow_pop6:
			{
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"视频类别无法编辑哦。"));
			}
				
				break;
			case R.id.square_popwindow_pop7:
			{
				
				editTagDialog(1,pop7TextView.getText().toString(), pop7TextView);
			}
				
				break;
			case R.id.square_popwindow_pop8:
			{
				editTagDialog(2,pop8TextView.getText().toString(), pop8TextView);
			}
				
				break;
			case R.id.square_popwindow_pop9:
			{
				editTagDialog(3,pop9TextView.getText().toString(), pop9TextView);
			}
				
				break;
			case R.id.square_popwindow_pop10:
			{
				editTagDialog(4,pop10TextView.getText().toString(), pop10TextView);
			}
				
				break;	
			case R.id.square_popwindow_pop11:
			{
				editTagDialog(5,pop11TextView.getText().toString(), pop11TextView);
			}
				
				break;
			case R.id.square_popwindow_pop12:
			{
				editTagDialog(6,pop12TextView.getText().toString(), pop12TextView);
			}
				
				break;				
			default:
				break;
			}
			return true;
		}
		
	}
	public void  setPopText(int which,String text) {
		
		switch (which) {
		case 1:
		{
			pop7TextView.setText(text);
		}
			
			break;
		case 2:
		{
			pop8TextView.setText(text);
		}
			
			break;
		case 3:
		{
			pop9TextView.setText(text);
		}
			
			break;
		case 4:
		{
			pop10TextView.setText(text);
		}
			
			break;
		case 5:
		{
			pop11TextView.setText(text);
		}
			
			break;
		case 6:
		{
			pop12TextView.setText(text);
		}
			
			break;			
		default:
			break;
		}
	}
	private void editTagDialog(final int which,String Tag,final TextView textView) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(mContext); 
		  final EditText mark=new EditText(mContext);
		  mark.setText(Tag);
		  mark.setHint("请输入您的Tag标签");;
		  builder.setView(mark);
		  builder.setTitle("编辑Tag标签");
		  builder.setPositiveButton("确认",  new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int whichButton) {
			   String newTag=mark.getText().toString();
			   textView.setText(newTag);
			   HashMap<String, String> map=new HashMap<String, String>();
			   map.put("which", String.valueOf(which));
			   map.put("tag", newTag);
			   handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_TAG_CHANGE, map));
		    dialog.dismiss();
		   }
		  });

		  builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
		 }	
	private class PopClick implements View.OnClickListener
	{

		public void onClick(View v) {
			
			//请求加载选定的广场中的类别，例如学霸，游戏等
			switch (v.getId()) {
			case R.id.square_popwindow_pop1:
			{
				
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_CLASSIFICATION_DATE,FinalName.SQUARE_ALL));
			}
				
				break;
			case R.id.square_popwindow_pop2:
			{
				Log.i("ClickPop","ClickPop2");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_CLASSIFICATION_DATE,FinalName.SQUARE_STUDY));
			}
				
				break;
			case R.id.square_popwindow_pop3:
			{
				Log.i("ClickPop","ClickPop3");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_CLASSIFICATION_DATE,FinalName.SQUARE_DORM_ANECDOTE));
			}
				
				break;
			case R.id.square_popwindow_pop4:
			{
				Log.i("ClickPop","ClickPop4");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_CLASSIFICATION_DATE,FinalName.SQUARE_TRAVEL));
			}
				
				break;
			case R.id.square_popwindow_pop5:
			{
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_CLASSIFICATION_DATE,FinalName.SQUARE_GAME));
			}
				
				break;
			case R.id.square_popwindow_pop6:
			{
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_CLASSIFICATION_DATE,FinalName.SQUARE_MYCOLLEAGUE));
			}
				
				break;
			case R.id.square_popwindow_pop7:
			{
				String tag=pop7TextView.getText().toString();
				if(!tag.equals(""))
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_SEARCHDATE,tag));

			}
				
				break;
			case R.id.square_popwindow_pop8:
			{
				String tag=pop8TextView.getText().toString();
				if(!tag.equals(""))
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_SEARCHDATE,tag));
			}
				
				break;
			case R.id.square_popwindow_pop9:
			{
				String tag=pop9TextView.getText().toString();
				if(!tag.equals(""))
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_SEARCHDATE,tag));
			}
				
				break;
			case R.id.square_popwindow_pop10:
			{
				String tag=pop10TextView.getText().toString();
				if(!tag.equals(""))
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_SEARCHDATE,tag));
			}
				
				break;	
			case R.id.square_popwindow_pop11:
			{
				String tag=pop11TextView.getText().toString();
				if(!tag.equals(""))
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_SEARCHDATE,tag));
			}
				
				break;
			case R.id.square_popwindow_pop12:
			{
				String tag=pop12TextView.getText().toString();
				if(!tag.equals(""))
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_SEARCHDATE,tag));
			}
				
				break;				
			default:
				break;
			}
			
		}
		
	}
}
