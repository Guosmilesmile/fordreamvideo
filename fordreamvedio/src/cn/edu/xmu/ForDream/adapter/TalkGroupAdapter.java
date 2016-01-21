package cn.edu.xmu.ForDream.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import master.flame.danmaku.controller.DrawHandler.Callback;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuGlobalConfig;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuSurfaceView;

import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.bean.Mymedia;
import cn.edu.xmu.ForDream.util.AsynImageLoader;
import cn.edu.xmu.ForDream.util.DownImage;
import cn.edu.xmu.ForDream.util.DownImage.ImageCallBack;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.HttpUtils;
import cn.edu.xmu.ForDream.util.ImageUtils;
import cn.edu.xmu.ForDream.util.PicUtil;
import cn.edu.xmu.ForDream.util.RoundImageView;







import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TalkGroupAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> list;//在绑定的数据
	private int resource;//绑定的条目界面
	private LayoutInflater inflater;
	private Context context;
	private AsynImageLoader asynImageLoader;
	private Handler handler;
	public TalkGroupAdapter(Context context,List<HashMap<String, Object>> list, int resource,Handler handler) {
		super();
		this.list = list;
		this.resource = resource;
		this.context=context;
		this.handler=handler;
		asynImageLoader = new AsynImageLoader(); 
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		
		return list.size();
	}

	public Object getItem(int position) {
		
		return list.get(position);
	}

	
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		

		Log.i("getview","getview"+position);
  /*
	map.put("id", object.getInt("id"));
			-	map.put("groupname",  object.getString("name"));
			-	map.put("limitnum",  object.getInt("limitnum"));
			-	map.put("description",  object.getString("description"));
			-	map.put("groupimg",  FinalUrl.GroupIMAGE_URL+object.getString("image"));
				map.put("applynum",  object.getInt("applynum"));
				map.put("personnum",  object.getInt("personnum"));
			-	map.put("commenttimes",  object.getInt("commenttimes"));
			-	map.put("creatorname", userjsonObject.getString("nickname"));
			-	map.put("channelname", object.getString("channelname"));
				String positionString=videojsonObject.getString("position");				 				  
	         -   map.put("position", positionString);
	            Mymedia mymedia=new Mymedia(this,handler);
	            map.put("mymedia",mymedia);
	           
	            String time=videojsonObject.get("createtime").toString();
	            time=time.substring(0, time.indexOf(':',time.indexOf(':')+1));
	          -  map.put("time",time);
	           - map.put("videoimg",FinalUrl.VIDEOIMAGE_URL+videojsonObject.getString("videoimage"));
	            map.put("filename", videojsonObject.getString("filename"));
	            map.put("vedioPath",FinalUrl.VIDEO_URL+videojsonObject.get("filename"));
   */
		
		ViewCache cache;
		if(convertView==null){
			 
			 convertView = inflater.inflate(resource, null);//生成条目界面对象
			 cache = new ViewCache();
			 cache.surfaceView = (SurfaceView) convertView.findViewById(R.id.talkgroup_item_surfaceView);
			 cache.groupCreatorTextView = (TextView)convertView.findViewById(R.id.talkgroup_item_group_creator);
			 cache.playNumTextView=(TextView)convertView.findViewById(R.id.talkgroup_item_playNum);
			 cache.groupNameTextView=(TextView) convertView.findViewById(R.id.talkgroup_item_groupName);
			 cache.timeTextView=(TextView) convertView.findViewById(R.id.talkgroup_item_time);
			 cache.commentNumTextView=(TextView) convertView.findViewById(R.id.talkgroup_item_commentNum);
			 cache.introducetTextView=(TextView) convertView.findViewById(R.id.talkgroup_item_introduce);
			 cache.vediocategoryTextView=(TextView)convertView.findViewById(R.id.talkgroup_item_vediocategory);
			 cache.setPositionTextView=(TextView)convertView.findViewById(R.id.talkgroup_item_setpositionTextView);		
			 cache.commentsImageView=(ImageView)convertView.findViewById(R.id.talkgroup_item_comment);			
			 cache.reportImageView=(ImageView)convertView.findViewById(R.id.talkgroup_item_report);
			 cache.vedioimageView=(ImageView)convertView.findViewById(R.id.talkgroup_item_videoimageView);
			 cache.groupImageView=(RoundImageView)convertView.findViewById(R.id.talkgroup_item_groupImg);
			 cache.danmakuimageView=(ImageView)convertView.findViewById(R.id.talkgroup_item_danmaku);
			 cache.limitTextView=(TextView)convertView.findViewById(R.id.talkgroup_item_grouperLimit);
			 cache.personNumTextView=(TextView)convertView.findViewById(R.id.talkgroup_item_grouperNum);
			 cache.applynumTextView=(TextView)convertView.findViewById(R.id.talkgroup_item_applyNum);
			 //弹幕
			 cache.mDanmakuView=(DanmakuSurfaceView)convertView.findViewById(R.id.talkgroup_item_sv_danmaku);
			 cache.applyImageView=(ImageView)convertView.findViewById(R.id.talkgroup_item_apply);
			 convertView.setTag(cache);
		}
		else 
		{
			cache = (ViewCache) convertView.getTag();
		}
		
		HashMap<String, Object> map = list.get(position);
		//下面代码实现数据绑定		
		 cache. playNumTextView.setText(map.get("playtimes").toString());
		 cache.groupNameTextView.setText(map.get("groupname").toString());
		 cache.timeTextView.setText(map.get("time").toString());

		 cache.groupCreatorTextView.setText(map.get("creatorname").toString());
		 cache. commentNumTextView.setText(map.get("commenttimes").toString());
		 cache. introducetTextView.setText(map.get("description").toString());
		 cache.vediocategoryTextView.setText(map.get("channelname").toString());
		 cache.setPositionTextView.setText(map.get("position").toString());
		 cache.limitTextView.setText(map.get("limitnum").toString());
		 cache.applynumTextView.setText(map.get("applynum").toString());
		 cache.personNumTextView.setText(map.get("personnum").toString());
		 
		 asynImageLoader.showImageAsyn(cache.vedioimageView, String.valueOf(map.get("videoimg")), R.drawable.default_video_img); 
		 asynImageLoader.showImageAsyn(cache.groupImageView, String.valueOf(map.get("groupimg")), R.drawable.square_item_defaulthead); 
		 
		 
		 
		 

		 cache.applyImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.talkgroup_item_apply));	
		 cache.commentsImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_comment));	
		 cache.reportImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.talkgroup_item_reporter));
		 cache.danmakuimageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_danmakuclose));
		
		 cache.groupNameTextView.setOnClickListener(new ButtonClick(position,convertView));		 
		 cache.commentsImageView.setOnClickListener(new ButtonClick(position,convertView));
		 cache.applyImageView.setOnClickListener(new ButtonClick(position,convertView));
		 cache.reportImageView.setOnClickListener(new ButtonClick(position,convertView));		
		 cache.surfaceView.setOnClickListener(new ButtonClick(position,convertView));
		 cache.groupImageView.setOnClickListener(new ButtonClick(position,convertView));
		 cache.vedioimageView.setOnClickListener(new ButtonClick(position,convertView));
		 cache.mDanmakuView.setOnClickListener(new ButtonClick(position,convertView));
		 cache.danmakuimageView.setOnClickListener(new ButtonClick(position,convertView));
		 return convertView;
	}
	

	private
	class ButtonClick implements View.OnClickListener
	{
		private int position;
		private View convertView;
		ButtonClick(int position,View convertView)
		{
			this.position=position;
			this.convertView=convertView;
		}
		public void onClick(View v) {

			Log.i("Which Item?",String.valueOf(position));
			int ViewId=v.getId();
			if(ViewId==R.id.talkgroup_item_videoimageView||ViewId==R.id.talkgroup_item_sv_danmaku)
			{
				SurfaceView surfaceView=(SurfaceView) convertView.findViewById(R.id.talkgroup_item_surfaceView);			
				HashMap<String, Object> map = list.get(position);
				 Mymedia mymedia=(Mymedia)map.get("mymedia");
				 mymedia.setPath(map.get("vedioPath").toString());
				 mymedia.setSurfaceView(surfaceView);			
				
				 
				 
				 //如果视频在条目中的id未设置，则设置
				 if(mymedia.getPosition()==-1)
				 {
					 mymedia.setPosition(position);
				 }
				 
				 if(ViewId==R.id.talkgroup_item_videoimageView)
				 {
					 TextView playNumTextView=(TextView) convertView.findViewById(R.id.talkgroup_item_playNum);
					 int curPlayNum=Integer.valueOf(playNumTextView.getText().toString());
					 playNumTextView.setText(String.valueOf(curPlayNum+1)); 
				 }

				 
				 mymedia.mediaplay(v);

			}
			//点击like
			else {
				switch (v.getId()) {
				case R.id.talkgroup_item_apply:
				{
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_APPLY_GROUP,position));
				}
					break;

				case R.id.talkgroup_item_comment:
				{
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_COMMENTJUMP,position));
					
					Log.i("Click On", "comment"+position);
				}
					break;

				case R.id.talkgroup_item_danmaku:
				{
					Log.i("Click On", "DANMAKU"+position);
					DanmakuSurfaceView  mDanmakuView = (DanmakuSurfaceView) convertView.findViewById(R.id.talkgroup_item_sv_danmaku);
					ImageView danmakuImageView=(ImageView)convertView.findViewById(R.id.talkgroup_item_danmaku);
					TextView  danmakuTextView=(TextView)convertView.findViewById(R.id.talkgroup_item_danmakuisshow);
					
					if(danmakuTextView.getText().toString().equals(context.getString(R.string.damaku_close)))
					{
						HashMap<String, Object> result=new HashMap<String, Object>();
						result.put("which", position);
						result.put("mDanmakuView", mDanmakuView);
						result.put("isshow", 1);
						handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_DANMAKU,result));
						danmakuImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_danmaku));
						danmakuTextView.setText(R.string.damaku_show);
					}
					else {
						HashMap<String, Object> result=new HashMap<String, Object>();
						result.put("which", position);
						result.put("mDanmakuView", mDanmakuView);
						result.put("isshow", 0);
						handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_DANMAKU,result));
						danmakuImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_danmakuclose));
						danmakuTextView.setText(R.string.damaku_close);
					}
				}
					break;

				case R.id.talkgroup_item_report:
				{
					Log.i("Click On", "report");
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_REPORT,position));
				}
					break;	

				default:
					break;
				}
			}
		}
	 
	}
	private final class ViewCache{
		public SurfaceView surfaceView;
		public TextView groupCreatorTextView;
		public TextView groupNameTextView;
		public TextView timeTextView;
		public TextView commentNumTextView;
		public TextView introducetTextView;
		public TextView playNumTextView;
		public TextView vediocategoryTextView;
		public TextView setPositionTextView;
		public TextView limitTextView;
		public TextView personNumTextView;
		public TextView applynumTextView;
		public ImageView applyImageView;
		public ImageView commentsImageView;
		public ImageView reportImageView;
		public ImageView vedioimageView;
		public ImageView danmakuimageView;
		
		public RoundImageView groupImageView;
		public DanmakuSurfaceView mDanmakuView;
		
	}
}
