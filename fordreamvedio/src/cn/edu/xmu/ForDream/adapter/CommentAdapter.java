package cn.edu.xmu.ForDream.adapter;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.HttpUtils;
import cn.edu.xmu.ForDream.util.PicUtil;
import cn.edu.xmu.ForDream.util.RoundImageView;

public class CommentAdapter extends BaseAdapter{
	private List<HashMap<String, Object>> list;
	private int resource;
	private LayoutInflater inflater;
	private Context context;
	
	public CommentAdapter(List<HashMap<String, Object>> list, int resource,
			 Context context) {
		super();
		this.list = list;
		this.resource = resource;
		this.context = context;
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
		if(convertView == null)
			convertView = inflater.inflate(resource, null);
		HashMap<String, Object> map = list.get(position);
		
		RoundImageView userimage = (RoundImageView)convertView.findViewById(R.id.comment_item_autohorImg);
		userimage.setImageBitmap((Bitmap)map.get("userimage"));
		
		TextView nickname = (TextView)convertView.findViewById(R.id.comment_item_authorName);
		nickname.setText(map.get("nickname").toString());
		
		TextView createtime = (TextView)convertView.findViewById(R.id.comment_item_time);
		createtime.setText(map.get("createtime").toString());
		
		TextView message = (TextView)convertView.findViewById(R.id.comment_item_content);
		CharSequence mtemp = null ;
		try {
			mtemp = java.net.URLDecoder.decode(map.get("message").toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		message.setText(mtemp);
		
		TextView supportnum = (TextView)convertView.findViewById(R.id.comment_item_supportNum);
		supportnum.setText(map.get("supportnum").toString());
		
		//commentcanzan
		ImageView commentcanzan = (ImageView)convertView.findViewById(R.id.comment_item_support);
		commentcanzan.setImageBitmap(PicUtil.readBitMap(context, Integer.parseInt(map.get("commentcanzan").toString())));
		commentcanzan.setOnClickListener(new ButtonClick(position, convertView));
		return convertView;
	}

	
	private class ButtonClick implements View.OnClickListener{

		private int position;
		private View convertView;
		ButtonClick(int position,View convertView)
		{
			this.position=position;
			this.convertView=convertView;
		}
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ImageView imageView = (ImageView) v;
			HashMap<String, Object> map = list.get(position);
			Integer integer = Integer.parseInt(map.get("commentcanzan").toString());
			TextView supportnum = (TextView)convertView.findViewById(R.id.comment_item_supportNum);
			switch(integer){
			case R.drawable.square_item_like:
				imageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_unlike));
				integer = R.drawable.square_item_unlike;
				supportnum.setText(String.valueOf(Integer.parseInt(supportnum.getText().toString())+1));
				postCommentLike(map.get("id").toString(), 1);
				break;
			default:
				imageView.setImageBitmap(PicUtil.readBitMap(context, R.drawable.square_item_like));
				integer = R.drawable.square_item_like;
				supportnum.setText(String.valueOf(Integer.parseInt(supportnum.getText().toString())-1));
				postCommentLike(map.get("id").toString(), 2);
				break;
			}
			list.get(position).put("commentcanzan", integer);
		}
		
	}
	
	public String postCommentLike(String commentid, int islike){
		try{
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("commentid", commentid);
    		map.put("islike", String.valueOf(islike));
    		map.put("userid", String.valueOf(FinalUrl.userid));
    		HttpUtils.postRequest(FinalUrl.PostCommentLikeUrl, map);
    		return "success";
    	}catch(Exception e){
    		e.printStackTrace();
    		return "fail";
    	}
	}
	
	public void addItem(HashMap<String, Object> map){
		list.add(map);
	}
	
	public void addItems(List<HashMap<String, Object>> addlist){
		list.addAll(addlist);
	}
}
