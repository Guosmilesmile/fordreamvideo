package cn.edu.xmu.ForDream.activities;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.adapter.CommentAdapter;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.HttpUtils;
import cn.edu.xmu.ForDream.util.TextWatcherAdapter;

import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

/**
 * 评论页面
 * @author Christy
 *
 */
public class CommentActivity extends FragmentActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener, OnScrollListener{
	private ListView listView;
	private ImageView returnImage;
	private ImageView commentImage;
	private EditText commentText;
	private View footer;
	private ArrayList<HashMap<String, Object>> listItem;
	private CommentClick clickListener;
	private CommentAdapter adapter;
	private String videoid;
	private int pageSize = 4;//每次获取多少条数据
    private int maxpage = 10;//总共有多少页
    private boolean loadfinish = true;
    private int visibleLastIndex = 0;   //最后的可视项索引    
    private int totalItemCount;       // 当前窗口可见项总数
    private boolean isAtTalkGroup;	  //是否在讨论组
    private String encodeCommentText = "";
    RelativeLayout fragmentRelative;
    ImageView showEmojicon;
    boolean isEmojiconShow = false;
    int whichVideo;
    int commentNum=0;
    
	 /** Called when the activity is first created. */
    @TargetApi(11)
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.comment);
        Intent i = this.getIntent();
        Bundle b = i.getExtras();   
        setVideoid(b.get("id").toString());
        isAtTalkGroup = b.getBoolean("isAtGroupTalk");
        whichVideo=b.getInt("whichVideo");
        //Android在4.0之前的版本 支持在主线程中访问网络，但是在4.0以后对这部分程序进行了优化，访问网络的代码不能写在主线程中了，所以加入以下语句
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        footer = getLayoutInflater().inflate(R.layout.loadfooter, null);
        listView=(ListView) findViewById(R.id.comment_listView);
        listItem = getCommentList(1, getVideoid());
        adapter = new CommentAdapter(listItem, R.layout.comment_item, this);
        if(listItem.size() <= 0)
        	Toast.makeText(CommentActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
        else{
	        listView.addFooterView(footer);
	        listView.setAdapter(adapter);
	        listView.setOnScrollListener(this);
        }
        
        //click action
        clickListener=new CommentClick();
        returnImage = (ImageView) findViewById(R.id.comment_return);
        returnImage.setOnClickListener(clickListener);
        commentImage = (ImageView) this.findViewById(R.id.comment_report_btn);
        commentImage.setOnClickListener(clickListener);
        
        commentText = (EditText) findViewById(R.id.comment_report_content);
        commentText.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					encodeCommentText = java.net.URLEncoder.encode(s.toString(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
            }
        });
         
        fragmentRelative = (RelativeLayout) findViewById(R.id.fragment_relative);
        fragmentRelative.setVisibility(View.GONE);
        
        showEmojicon = (ImageView) findViewById(R.id.comment_show_emojicons);
        showEmojicon.setOnClickListener(clickListener);
    }
    
    /**
     * 获取评论列表
     * @param pagenum
     * @param videoid
     * @return
     */
    private ArrayList<HashMap<String, Object>> getCommentList(int pagenum, String videoid){
    	try{
    		HashMap<String, String> postMap = new HashMap<String, String>();

    		String result=null;
    		
    		if(isAtTalkGroup)
    		{
        		postMap.put("uservideogroupid", videoid);
        		postMap.put("userid", String.valueOf(FinalUrl.userid));
        		postMap.put("pageSize", String.valueOf(pageSize));
        		postMap.put("pagenum", String.valueOf(pagenum));
    			result = HttpUtils.getRequest(FinalUrl.GetTalkGroupCommentUrl + mapToURLParam(postMap));
    		}
    		else
    		{
        		postMap.put("videoid", videoid);
        		postMap.put("userid", String.valueOf(FinalUrl.userid));
        		postMap.put("pageSize", String.valueOf(pageSize));
        		postMap.put("pagenum", String.valueOf(pagenum));
    			result = HttpUtils.getRequest(FinalUrl.GetCommentListUrl + mapToURLParam(postMap));
    		}
    		
    		
    		
    		ArrayList<HashMap<String, Object>> date=new ArrayList<HashMap<String, Object>>();
    		JSONArray array = new JSONObject(result).getJSONArray("commentlist");
    		for(int i = 0 ; i < array.length() ; i++){
    			HashMap<String, Object> map = new HashMap<String, Object>(); 
    			map.put("id", array.getJSONObject(i).getString("id"));
    			map.put("message", array.getJSONObject(i).getString("message"));
    			map.put("createtime", array.getJSONObject(i).getString("createtime"));
				JSONObject userjsonObject = array.getJSONObject(i).getJSONObject("user");
				map.put("nickname", userjsonObject.get("nickname"));
				map.put("userimage", HttpUtils.returnBitmap(FinalUrl.WEB+"Upload/UserImage/"+userjsonObject.get("userimage").toString()));
				map.put("commentcanzan", array.getJSONObject(i).getString("commentcanzan").equals("1")? R.drawable.square_item_like : R.drawable.square_item_unlike);
				map.put("supportnum", array.getJSONObject(i).getString("commentzantimes"));
				date.add(map);
			} 
    		return date;
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 提交评论
     * @param videoid
     * @param userid
     * @param message
     * @return
     */
    private String postComment(String videoid, String userid, String message){
    	try{
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("videoid", videoid);
    		map.put("message", message);
    		map.put("userid", userid);
    		HttpUtils.postRequest(FinalUrl.PostCommentUrl, map);
    		return "success";
    	}catch(Exception e){
    		e.printStackTrace();
    		return "fail";
    	}
    }
    
    /**
     * （不明）这个组评论是什么意思
     * @param videoid
     * @param userid
     * @param message
     * @return
     */
    private String postGroupComment(String videoid, String userid, String message){
    	try{
    		HashMap<String, String> map = new HashMap<String, String>();
    		map.put("uservideogroupid", videoid);
    		map.put("message", message);
    		map.put("userid", userid);
    		HttpUtils.postRequest(FinalUrl.PostGroupCommentUrl, map);
    		return "success";
    	}catch(Exception e){
    		e.printStackTrace();
    		return "fail";
    	}
    }
    
    /**
     * 界面控件点击事件方法
     * @author lcf
     *
     */
    private class CommentClick implements View.OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.comment_return: {
	 	        Bundle bundle = new Bundle(); 
	 	        bundle.putInt("whichVideo", whichVideo);
	 	        bundle.putInt("commentNum", commentNum);
	 			//返回
	 	       CommentActivity.this.setResult(RESULT_OK, CommentActivity.this.getIntent().putExtras(bundle)); 
			   CommentActivity.this.finish();
			} break;
			case R.id.comment_report_btn: {
				if(encodeCommentText.equals("")){
					showToastMessage("发表内容不能为空!");
					return;                
				}
				if(isAtTalkGroup){
					postGroupComment(getVideoid(), Integer.toString(FinalUrl.userid), encodeCommentText);
				}else{
					//调用post comment方法
					postComment(getVideoid(), Integer.toString(FinalUrl.userid), encodeCommentText);
				}
				//评论数量加1
				commentNum++;
				
				//update data
				listItem = getCommentList(1, getVideoid());
				adapter = new CommentAdapter(listItem, R.layout.comment_item, getBaseContext());
				listView.setAdapter(adapter);
				commentText.setText("");
				//改变聚点，让软键盘消失
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); 
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
				if(isEmojiconShow){
					fragmentRelative.setVisibility(View.GONE);
					isEmojiconShow = false;
				}
			} break;
			case R.id.comment_show_emojicons:
				if(isEmojiconShow){
					isEmojiconShow = false;
					fragmentRelative.setVisibility(View.GONE);
				}else{
					isEmojiconShow = true;
					fragmentRelative.setVisibility(View.VISIBLE);
				}
				break;
			}
		}
    	
    }
    
    /*public SimpleAdapter getAdapter(){
    	listItem = getCommentList(getVideoid());
    	SimpleAdapter tempAdapter = new SimpleAdapter(this, listItem, R.layout.comment_item,   
                new String[]{"userimage", "nickname", "createtime", "message", "commentcanzan", "supportnum"}, new int[]{R.id.comment_item_autohorImg, R.id.comment_item_authorName, R.id.comment_item_time, R.id.comment_item_content, R.id.comment_item_support, R.id.comment_item_supportNum});
    	
    	tempAdapter.setViewBinder(new ViewBinder() {
			
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if(view instanceof ImageView && data instanceof Drawable){
					ImageView iv = (ImageView)view;
					iv.setImageDrawable((Drawable)data);
					return true;
				}
				
				if(view instanceof ImageView && data instanceof Bitmap){
					ImageView iv = (ImageView)view;
					iv.setImageBitmap((Bitmap)data);
					return true;
				}
				return false;
			}
		});
    	return tempAdapter;
    }*/
    
    //显示提示
  	public void showToastMessage(String msg) {
  		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  	}
    
  	/**
  	 * 将数据变成&间隔的参数形式
  	 * @param map
  	 * @return
  	 */
  	public String mapToURLParam(HashMap<String, String> map){
  		String str = "";
  		try{
	  		for(String key: map.keySet()){
//	  			str += key + "=" + java.net.URLEncoder.encode(map.get(key), "UTF-8") + "&";
	  			str += key + "=" + map.get(key) + "&";
	  		}
	  		if(!str.equals(""))
	  			str = "?" + str.substring(0, str.length()-1);
	  		return str;
  		}catch(Exception e){
  			e.printStackTrace();
  			return null;
  		}
  	}

	public String getVideoid() {
		return videoid;
	}

	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}

	
	/**
	 * 滚动加载剩余项目
	 */
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.totalItemCount = totalItemCount - 1;
		this.visibleLastIndex = firstVisibleItem + visibleItemCount -1;
		if(visibleLastIndex == adapter.getCount()){
			if(totalItemCount > 0){
				//当前页
				int currentpage = this.totalItemCount%pageSize == 0 ? this.totalItemCount/pageSize : this.totalItemCount/pageSize+1;
				final int nextpage = currentpage + 1;//下一页
				if(nextpage <= maxpage && loadfinish){
//					loadfinish = false;
//					listView.addFooterView(footer);
					ArrayList<HashMap<String, Object>> listItemtemp = getCommentList(nextpage, getVideoid());
					if(listItemtemp.size() <= 0)
						loadfinish = false;
					adapter.addItems(listItemtemp);
//					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
//					Toast.makeText(CommentActivity.this, "数据加载:"+nextpage+","+currentpage+","+totalItemCount, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(CommentActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
					listView.removeFooterView(footer);
				}
			}
			else{
				Toast.makeText(CommentActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
				listView.removeFooterView(footer);
			}
		}
	}

	/**
	 * 滚动结束调用
	 */
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		int itemsLastIndex = adapter.getCount() -1; //数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {    
            //如果是自动加载,可以在这里放置异步加载数据的代码    
        }  
	}

	public void onEmojiconBackspaceClicked(View v) {
		EmojiconsFragment.backspace(commentText);
	}

	public void onEmojiconClicked(Emojicon emojicon) {
		EmojiconsFragment.input(commentText, emojicon);
	}
    
  	
}
