package cn.edu.xmu.ForDream.adapter;

import info.hoang8f.widget.FButton;

import java.util.HashMap;
import java.util.List;

import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.util.FinalName;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CheckMessageAdapter extends BaseAdapter {
	private List<String> date;//在绑定的数据
	private int resource;//绑定的条目界面
	private LayoutInflater inflater;
	private Context context;
	private Handler handler;
	private boolean isAtMessage;
	public CheckMessageAdapter(List<String> date, int resource,
			Context context, Handler handler) {
		super();
		this.date = date;
		this.resource = resource;
		this.context = context;
		this.handler = handler;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		isAtMessage=true;
	}

	public boolean isAtMessage() {
		return isAtMessage;
	}

	public void setAtMessage(boolean isAtMessage) {
		this.isAtMessage = isAtMessage;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return date.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return date.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}



	@SuppressLint("ResourceAsColor")
	public View getView(int position, View convertView, ViewGroup parent) {

		final int which=position;
		Log.i("CheckgetView","CheckgetView"+position);
		final ViewCache cache;
		if(convertView==null){
			convertView = inflater.inflate(resource, null);//生成条目界面对象
			cache = new ViewCache();
			cache.message=(TextView)convertView.findViewById(R.id.groupmessage_dialog_check_messagetext);
			cache.button=(FButton)convertView.findViewById(R.id.groupmessage_dialog_messagebutton);
			convertView.setTag(cache);
		}
		else {
			cache = (ViewCache) convertView.getTag();
		}
		
		cache.message.setText(date.get(position));
		if (isAtMessage) {
			cache.button.setVisibility(View.INVISIBLE);
		}
		else {
			cache.button.setVisibility(View.VISIBLE);
			cache.button.setText("同意");
		}
		cache.button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cache.button.setClickable(false);
				cache.button.setButtonColor(context.getResources().getColor(R.color.fbutton_color_concrete));
				cache.button.setShadowHeight(0);
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("which", String.valueOf(which));
				map.put("state", "2");
				handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_ACCEPT_APPLY, map));
			}
		});
		return convertView;
	}
	private final class ViewCache{
		public TextView message;
		public FButton button;
	}
}
