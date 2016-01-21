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

public class CompanyMediaAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> list;//在绑定的数据
	private int resource;//绑定的条目界面
	private LayoutInflater inflater;
	private Context context;
	private AsynImageLoader asynImageLoader;
	private Handler handler;
	public CompanyMediaAdapter(Context context,List<HashMap<String, Object>> list, int resource,Handler handler) {
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
   public void AddCommentView(int position)
   {
	   
   }
	public View getView(int position, View convertView, ViewGroup parent) {
		

		Log.i("getview","getview"+position);

		
		ViewCache cache;
		if(convertView==null){
			 
			 convertView = inflater.inflate(resource, null);//生成条目界面对象
			 cache = new ViewCache();
			 cache.surfaceView = (SurfaceView) convertView.findViewById(R.id.company_square_item_surfaceView);
			 cache.fromWhoTextView = (TextView)convertView.findViewById(R.id.company_square_item_original_author);
			 cache.playNumTextView=(TextView)convertView.findViewById(R.id.company_square_item_playNum);
			 cache.authorNameTextView=(TextView) convertView.findViewById(R.id.company_square_item_authorName);
			 cache.timeTextView=(TextView) convertView.findViewById(R.id.company_square_item_time);
			 cache.likeNumTextView=(TextView) convertView.findViewById(R.id.company_square_item_likeNum);
			// cache.shareNumTextView=(TextView) convertView.findViewById(R.id.company_square_item_shareNum);
			 cache.commentNumTextView=(TextView) convertView.findViewById(R.id.company_square_item_commentNum);
			 cache.introducetTextView=(TextView) convertView.findViewById(R.id.company_square_item_introduce);
			 cache.vediocategoryTextView=(TextView)convertView.findViewById(R.id.company_square_item_vediocategory);
			 cache.concernTextView=(TextView)convertView.findViewById(R.id.company_square_item_concern_text);
			 cache.setPositionTextView=(TextView)convertView.findViewById(R.id.company_square_item_setpositionTextView);
			 cache.collectTextView= (TextView)convertView.findViewById(R.id.company_square_item_danmakuisshow);
			 cache.schoolTextView=(TextView)convertView.findViewById(R.id.company_square_item_SchoolName);
			 cache.concernImageView=(ImageView)convertView.findViewById(R.id.company_square_item_concern);
			 cache.likeImageView=(ImageView)convertView.findViewById(R.id.company_square_item_like);
			 cache.commentsImageView=(ImageView)convertView.findViewById(R.id.company_square_item_comment);
			 cache.shareImageView=(ImageView)convertView.findViewById(R.id.company_square_item_share);
			 cache.reportImageView=(ImageView)convertView.findViewById(R.id.company_square_item_report);
			 cache.vedioimageView=(ImageView)convertView.findViewById(R.id.company_square_item_videoimageView);
			 cache.authorImageView=(RoundImageView)convertView.findViewById(R.id.company_square_item_autohorImg);
			 cache.danmakuimageView=(ImageView)convertView.findViewById(R.id.company_square_item_danmaku);
			 cache.collectImageView=(ImageView)convertView.findViewById(R.id.company_square_item_collect);
			 cache.moreImageView=(ImageView)convertView.findViewById(R.id.company_square_item_other);
			 //弹幕
			 cache.mDanmakuView=(DanmakuSurfaceView)convertView.findViewById(R.id.company_sv_danmaku);
		
			 convertView.setTag(cache);
		}
		else 
		{
			cache = (ViewCache) convertView.getTag();
		}
		
		HashMap<String, Object> map = list.get(position);
		int isCanlike=Integer.valueOf(String.valueOf(map.get("Iscanlike")));		//是否可以点赞
		int isCanConcern=Integer.valueOf(String.valueOf(map.get("iscanconcern")));	//是否可以关注
		//下面代码实现数据绑定		
		 cache. playNumTextView.setText(map.get("playNum").toString());
		 cache.authorNameTextView.setText(map.get("authorName").toString());
		 cache.timeTextView.setText(map.get("time").toString());
		 cache. likeNumTextView.setText(map.get("likeNum").toString());
		 cache.fromWhoTextView.setText(map.get("fromuser").toString());
		// cache.shareNumTextView.setText(map.get("shareNum").toString());
		 cache. commentNumTextView.setText(map.get("commentNum").toString());
		 cache. introducetTextView.setText(map.get("introduce").toString());
		 cache.vediocategoryTextView.setText(map.get("vediocategory").toString());
		 cache.setPositionTextView.setText(map.get("position").toString());
		 cache.schoolTextView.setText(map.get("schoolname").toString());
		// cache. authorImageView.setImageDrawable((Drawable) map.get("headdrawable"));		
		 //cache.vedioimageView.setImageDrawable((Drawable) map.get("vediodrawable"));
		 asynImageLoader.showImageAsyn(cache.vedioimageView, String.valueOf(map.get("vediodrawable")), R.drawable.default_video_img); 
		 asynImageLoader.showImageAsyn(cache.authorImageView, String.valueOf(map.get("headdrawable")), R.drawable.square_item_defaulthead); 
		 
		 
		 
		 
		 //可以关注，显示未关注
		 if(isCanConcern==1)
		 {
			 cache.concernImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_concernimg));
			 cache.concernTextView.setText(R.string.concern_no);
		 }
		 //不能关注，显示已关注
		 else if(isCanConcern==2)
		 {
			 cache.concernImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_unconcernimg));
			 cache.concernTextView.setText(R.string.concern_yes);
		 }
		 
			//可以点赞,显示未点赞
			if(isCanlike==1)
			{
				 cache.likeImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_like));
			}
			//不能点赞，显示已点赞
			else if(isCanlike==2) {
				 cache.likeImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_unlike));
			}

		 
		 cache.commentsImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_comment));
		 cache.shareImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_share));
		 cache.reportImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_ietm_report));
		 cache.danmakuimageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_danmaku));
		 cache.collectImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_collect));
		 cache.moreImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_more));
		
		 cache.authorNameTextView.setOnClickListener(new ButtonClick(position,convertView));		 
		cache.moreImageView.setOnClickListener(new ButtonClick(position,convertView));
		cache.collectImageView.setOnClickListener(new ButtonClick(position,convertView));
		cache.likeImageView.setOnClickListener(new ButtonClick(position,convertView));
		cache.commentsImageView.setOnClickListener(new ButtonClick(position,convertView));
		cache.shareImageView.setOnClickListener(new ButtonClick(position,convertView));
		cache.reportImageView.setOnClickListener(new ButtonClick(position,convertView));		
		cache.surfaceView.setOnClickListener(new ButtonClick(position,convertView));
		cache.concernImageView.setOnClickListener(new ButtonClick(position,convertView));
		cache.authorImageView.setOnClickListener(new ButtonClick(position,convertView));
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
			if(ViewId==R.id.company_square_item_videoimageView||ViewId==R.id.company_sv_danmaku)
			{
				SurfaceView surfaceView=(SurfaceView) convertView.findViewById(R.id.company_square_item_surfaceView);			
				HashMap<String, Object> map = list.get(position);
				 Mymedia mymedia=(Mymedia)map.get("mymedia");
				 mymedia.setPath(map.get("vedioPath").toString());
				 mymedia.setSurfaceView(surfaceView);			
				
				 
				 
				 //如果视频在条目中的id未设置，则设置
				 if(mymedia.getPosition()==-1)
				 {
					 mymedia.setPosition(position);
				 }
				 
				 if(ViewId==R.id.company_square_item_videoimageView)
				 {
					 TextView playNumTextView=(TextView) convertView.findViewById(R.id.company_square_item_playNum);
					 int curPlayNum=Integer.valueOf(playNumTextView.getText().toString());
					 playNumTextView.setText(String.valueOf(curPlayNum+1)); 
				 }

				 
				 mymedia.mediaplay(v);

			}
			//点击like
			else {
				switch (v.getId()) {
				case R.id.company_square_item_like:
				{
					
					HashMap<String, Object> map = list.get(position);
					int isCanlike=Integer.valueOf(String.valueOf(map.get("Iscanlike")));
					
					Log.i("Click On", "ISlike: "+isCanlike);
					
					HashMap<String, Integer> result=new HashMap<String, Integer>();
					result.put("which", position);
					result.put("isLike", isCanlike);
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_ISLIKE, result));
					
					
					TextView likeNumTextView=(TextView) convertView.findViewById(R.id.company_square_item_likeNum);
					int curlikeNum=Integer.valueOf(likeNumTextView.getText().toString());
					//可以点赞,显示未点赞，则点击后变为已点赞
					if(isCanlike==1)
					{
						//变为不能点赞
						isCanlike=2;
						((ImageView)v).setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_unlike));
						likeNumTextView.setText(String.valueOf(curlikeNum+1));
					}
					//不能点赞,显示已经点赞
					else if(isCanlike==2) {
						
						//变为可以点赞
						isCanlike=1;
						((ImageView)v).setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_like));
						likeNumTextView.setText(String.valueOf(curlikeNum-1));
					}
					
					list.get(position).put("Iscanlike", isCanlike);
					
					Log.i("Click OVER", "ISlike: "+Integer.valueOf(String.valueOf(map.get("Iscanlike"))));
				}
					break;
				case R.id.company_square_item_concern:
				{
					HashMap<String, Object> map = list.get(position);
					int isconcern=Integer.valueOf(String.valueOf(map.get("iscanconcern")));
					
					Log.i("Click On", "iscanconcern: "+position);
					
					HashMap<String, Integer> result=new HashMap<String, Integer>();
					result.put("which", position);
					result.put("isconcern", isconcern);
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_ISCONCERN, result));
					
					
					TextView concernTextView=(TextView) convertView.findViewById(R.id.company_square_item_concern_text);
					//可以关注，点击后显示已关注
					if(isconcern==1)
					{
						//变为不能关注
						isconcern=2;
						((ImageView)v).setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_unconcernimg));
						concernTextView.setText(R.string.concern_yes);
					}
					//不能关注,显示已经未关注
					else if(isconcern==2) {
						
						//变为可以关注
						isconcern=1;
						((ImageView)v).setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_concernimg));
						concernTextView.setText(R.string.concern_no);
					}
					
					list.get(position).put("iscanconcern", isconcern);
					
				}
					break;
				case R.id.company_square_item_comment:
				{
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_COMMENTJUMP,position));
					
					Log.i("Click On", "comment"+position);
				}
					break;

				case R.id.company_square_item_danmaku:
				{
					Log.i("Click On", "DANMAKU"+position);
					DanmakuSurfaceView  mDanmakuView = (DanmakuSurfaceView) convertView.findViewById(R.id.company_sv_danmaku);
					ImageView danmakuImageView=(ImageView)convertView.findViewById(R.id.company_square_item_danmaku);
					TextView  danmakuTextView=(TextView)convertView.findViewById(R.id.company_square_item_danmakuisshow);
					
					if(danmakuTextView.getText().toString().equals(context.getString(R.string.damaku_close)))
					{
						HashMap<String, Object> result=new HashMap<String, Object>();
						result.put("which", position);
						result.put("mDanmakuView", mDanmakuView);
						result.put("isshow", 1);
						handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_DANMAKU,result));
						danmakuImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_danmakuclose));
						danmakuTextView.setText(R.string.damaku_show);
					}
					else {
						HashMap<String, Object> result=new HashMap<String, Object>();
						result.put("which", position);
						result.put("mDanmakuView", mDanmakuView);
						result.put("isshow", 0);
						handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_DANMAKU,result));
						danmakuImageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_danmaku));
						danmakuTextView.setText(R.string.damaku_close);
					}
				}
					break;

				case R.id.company_square_item_authorName:
				{
					Log.i("Click On", "authorName");
				}
					break;
				case R.id.company_square_item_collect:
				{
					Log.i("Click On", "collect");
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_COLLECT,position));
				}
					break;	
				case R.id.company_square_item_report:
				{
					Log.i("Click On", "report");
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_REPORT,position));
				}
					break;	
				case R.id.company_square_item_share:
				{
					Log.i("Click On", "share");
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHARE,position));
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
		public TextView fromWhoTextView;
		public TextView authorNameTextView;
		public TextView timeTextView;
		public TextView likeNumTextView;
	//	public TextView shareNumTextView;
		public TextView commentNumTextView;
		public TextView introducetTextView;
		public TextView playNumTextView;
		public TextView vediocategoryTextView;
		public TextView concernTextView;
		public TextView setPositionTextView;
		public TextView collectTextView;
		public TextView schoolTextView;
		public ImageView concernImageView;
		public ImageView moreImageView;
		public ImageView likeImageView;
		public ImageView commentsImageView;
		public ImageView shareImageView;
		public ImageView collectImageView;
		public ImageView reportImageView;
		public ImageView vedioimageView;
		public ImageView danmakuimageView;
		
		public RoundImageView authorImageView;
		public DanmakuSurfaceView mDanmakuView;
		
	}
}
