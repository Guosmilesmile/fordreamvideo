package cn.edu.xmu.ForDream.popWindows;

import java.util.ArrayList;
import java.util.HashMap;

import cn.edu.xmu.ForDream.R;
import cn.sharesdk.framework.l;
import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

public class ChoosePop extends PopupWindow {
	private int resId;		
	private Context mContext;
	private View popView;
	private LayoutInflater inflater;
	private Handler handler;	
	private ListView listView;
	private ArrayList<String> list;
	private ArrayAdapter<String> adapter;
	private int whichPop;
	public ChoosePop(int resId,Context context,Handler handler,ArrayAdapter<String> adapter,int whichPop)
	{
		super(context);
		this.resId = resId;
		mContext = context;
		this.handler=handler;
		this.list=list;
		this.adapter=adapter;
		this.whichPop=whichPop;
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		init();
	}
	private void init() {
		popView = inflater.inflate(this.resId, null);
		popView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		setContentView(popView);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setFocusable(true);
		listView=(ListView)popView.findViewById(R.id.choose_classification_listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ItemClickListener());
	}
	private final class ItemClickListener implements OnItemClickListener
	{

		
		public void onItemClick(AdapterView<?> parent, View view, int position, long id3) {
			
			HashMap<String, Integer> map=new HashMap<String, Integer>();
			map.put("whichPop", whichPop);
			map.put("whichItem", position);
			handler.sendMessage(handler.obtainMessage(4,map));

			Log.i("Choose Item:","Choose Item:"+position);
		}
		
	}
}
