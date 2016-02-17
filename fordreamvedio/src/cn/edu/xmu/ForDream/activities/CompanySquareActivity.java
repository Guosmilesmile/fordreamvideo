package cn.edu.xmu.ForDream.activities;
import info.hoang8f.widget.FButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import master.flame.danmaku.controller.DrawHandler.Callback;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.AcFunDanmakuLoader;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.android.DanmakuGlobalConfig;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.AcFunDanmakuParser;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.Enum.PositionEnum;
import cn.edu.xmu.ForDream.adapter.CheckMessageAdapter;
import cn.edu.xmu.ForDream.adapter.CompanyMediaAdapter;
import cn.edu.xmu.ForDream.adapter.MediaAdapter;
import cn.edu.xmu.ForDream.adapter.TalkGroupAdapter;
import cn.edu.xmu.ForDream.bean.Mymedia;
import cn.edu.xmu.ForDream.friend.SwipeAdapter;
import cn.edu.xmu.ForDream.friend.SwipeListView;
import cn.edu.xmu.ForDream.friend.WXMessage;
import cn.edu.xmu.ForDream.group.GroupAdapter;
import cn.edu.xmu.ForDream.group.GroupEntity;
import cn.edu.xmu.ForDream.group.GroupPerson;
import cn.edu.xmu.ForDream.popWindows.FriendPop;
import cn.edu.xmu.ForDream.popWindows.GroupPop;
import cn.edu.xmu.ForDream.popWindows.MenuPop;
import cn.edu.xmu.ForDream.popWindows.SearchPop;
import cn.edu.xmu.ForDream.popWindows.SquarePop;
import cn.edu.xmu.ForDream.util.AsynImageLoader;
import cn.edu.xmu.ForDream.util.CircularProgressDrawable;
import cn.edu.xmu.ForDream.util.DropDownListView;
import cn.edu.xmu.ForDream.util.DropDownListView.OnDropDownListener;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.FloatingActionButton;
import cn.edu.xmu.ForDream.util.HttpUtils;
import cn.edu.xmu.ForDream.util.PicUtil;

@TargetApi(11)
public class CompanySquareActivity extends Activity {
	private List<WXMessage> data = new ArrayList<WXMessage>();
	private List<GroupEntity> Gdata = new ArrayList<GroupEntity>();
	private List<List<GroupPerson>> Mdata = new ArrayList<List<GroupPerson>>();
    private SwipeListView mListView;
    static final int ANIMATION_DURATION = 800;
    static final int CREATE_ACTIVITY_REQUEST_CODE=1000;
    static final int COMMENT_ACTIVITY_REQUEST_CODE=2000;
    private SwipeAdapter mAdapter; 
    private GroupAdapter gAdapter;
	private RelativeLayout mainlayout; 
	private LayoutInflater inflater;
	private RelativeLayout squareLayout;
	private RelativeLayout friendLayout;
	private RelativeLayout communityLayout;
	//公司
	private RelativeLayout companyLayout;
	private LinearLayout userinfo_mainview;
	private DropDownListView userInfoListView;					//个人主页的listview
	private DropDownListView mainlistView;						//主页面的listview
	private DropDownListView listView;                          //指向当前页面的listview
	private SwipeListView friendlistview;
	private MediaAdapter userInfoadapter;				//个人主页的listview适配器
	private CompanyMediaAdapter mainAdapter;					//主界面的listview的适配器
	private TalkGroupAdapter groupAdapter;					//讨论组的适配器
	private SwipeAdapter swipeadapter;
	private BaseAdapter adapter;		         //当前的适配器
	//private View footer;                           
	private ArrayList<HashMap<String, Object>> listItem;                              
	
	private Handler cHandler;
	
	
	
	//视频TAG标签存放map
	private HashMap<Integer, String> popInfomap;
	//顶部菜单                                 
	private ImageView positionImage; 
	private ImageView selectmenuImageView;
	private ImageView selectpositionImageView;
	private ImageView selectsearchImageView;
                                                                 
	//个人主页选项菜单
	private ImageView selectvideoImageView;
	private ImageView topsearchImageView;
	private ImageView selectcollectImageView;
	private ImageView selectshareImageView;
	private ImageView selectcommentImageView;
	private boolean hasLoadUserInfo;
	
	//个人主页资料
	private SurfaceView surfaceView;
	private Mymedia usermymedia;
	private ImageView videoImageView;
	private ImageView userImageView;
	private TextView usernickname;
	private TextView userschool;
	private TextView userconcerns;
	private TextView userfans;
	private TextView uservideos;
	private TextView usershares;
	private TextView usercomments;
	private TextView usercollects; 
	//图片加载
	private AsynImageLoader asynImageLoader;
	
	
    private int pageSize = 5;//每次获取多少条数据
    private int maxpage = 10;//总共有多少页
    private boolean loadfinish = true;
    private ProgressDialog progressDialog;
    private PopupWindow popupWindowmenu;
    private PopupWindow squarePopupWindow;
    private PopupWindow searchPopupWindow;
    private PopupWindow friendPopupWindow;
    private PopupWindow groupPopupWindow;
    
    private boolean isAutoPlay;
    private PositionEnum current_position;   //当前所处位置,例如广场
    private int curent_positionStringid;	 //目前所处位置的字符串id,R.string.position_XX，用于异步加载数据时候的
    private int current_square_where;		 //当前所处广场位置	
    private int current_userinfo_where;		 //当前所处个人主页位置	
    private int current_talkgroup_where;	 //当前所处讨论组位置	
    private int current_friendgroup_where;	 //当前所处朋友圈位置	
    private boolean isAtGroupTalk;
    
    private boolean isSearch;
    private String tag;
    private IDanmakuView mDanmakuView;
    private BaseDanmakuParser mParser;
    
    //begin FloatingActionButton
    FloatingActionButton ivDrawable;
	Animator currentAnimation;
	CircularProgressDrawable drawable;
	boolean isRepeat = true;
    //end
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        listItem = new ArrayList<HashMap<String, Object>>();
       // footer = getLayoutInflater().inflate(R.layout.loadfooter, null);
        inflater=LayoutInflater.from(this);
        mainlayout=(RelativeLayout) findViewById(R.id.main_layout);
        companyLayout=(RelativeLayout) inflater.inflate(R.layout.company_square_activity_main, null).findViewById(R.id.company_square_layout);
        squareLayout=(RelativeLayout) inflater.inflate(R.layout.square_activity_main, null).findViewById(R.id.square_layout);
        communityLayout=(RelativeLayout) inflater.inflate(R.layout.community, null).findViewById(R.id.community_layout
        		);
        topsearchImageView=(ImageView)findViewById(R.id.topmenu_serachImage);

        //绑定Layout里面的ListView  
        //listView=(ListView) findViewById(R.id.square_listView);
        mainlistView=(DropDownListView) squareLayout.getChildAt(0);
        
        
        //初始化加载框
        progressDialog=new ProgressDialog(this);
        
        //初始化菜单选择条
        selectmenuImageView=(ImageView)findViewById(R.id.topmenu_selectmenubar);
        selectpositionImageView=(ImageView)findViewById(R.id.topmenu_selectpositionbar);
        selectsearchImageView=(ImageView)findViewById(R.id.topmenu_selectsearchbar);       
        selectpositionImageView.setVisibility(View.INVISIBLE);
        selectsearchImageView.setVisibility(View.INVISIBLE);
        selectmenuImageView.setVisibility(View.INVISIBLE);
        topsearchImageView.setVisibility(View.INVISIBLE);

      //FIXME         
        //初始化listview为广场的listview，广场布局加入主布局中
        listView=mainlistView;
        mainlayout.addView(squareLayout);
       
        //初始化图片加载需要的类
        asynImageLoader = new AsynImageLoader(); 

        //初始化群组的适配器
        groupAdapter=new TalkGroupAdapter(this, listItem, R.layout.talkgroup_item,handler);  
        //初始化广场和朋友圈的适配器
        mainAdapter=new CompanyMediaAdapter(this, listItem, R.layout.company_square_item,handler); 
       
        bindSquareAdapter();
        

        //初次进入讨论组和朋友圈，加载所有数据
        current_friendgroup_where=FinalName.ALL_DATE;
        current_talkgroup_where=FinalName.ALL_DATE;
        
        //初始化adapter指向主页面的适配器
        adapter=mainAdapter;
        
        //是否自动播放
        isAutoPlay=false;
        
        //是否在搜索状态
        isSearch=false;
       
        //是否在讨论组发布区判断
        isAtGroupTalk=false;
  
        //初始化位置
        current_position=PositionEnum.SQUARE;
        current_square_where=FinalName.SQUARE_ALL;
        curent_positionStringid=R.string.position_square;
        hasLoadUserInfo=false;
     

       //各个按钮监听
        ImageView menuImage=(ImageView) findViewById(R.id.topmenu_menuImage);
       // Log.i("menuImage",((ImageView)findViewById(R.id.menuImage)).toString());
        positionImage=(ImageView) findViewById(R.id.topmenu_positionImage);
        ImageView searchImage=(ImageView) findViewById(R.id.topmenu_serachImage);
        ImageView cameraImage=(ImageView) findViewById(R.id.topmenu_cameraImage);
        positionImage.setImageResource(R.drawable.topmenu_square_title_img);
        
        MenuClick clickListener=new MenuClick(); 
        menuImage.setOnClickListener(clickListener);
        positionImage.setOnClickListener(clickListener);
        searchImage.setOnClickListener(clickListener);
        cameraImage.setOnClickListener(clickListener);
      

        postPopInfo();
		//开启子线程
		 new ChildThread().start();	
		 
	        //begin FloatingActionButton
	        ivDrawable = (FloatingActionButton) findViewById(R.id.button_floating_action);
	        ivDrawable.attachToListView(listView);
	        ivDrawable.getBackground().setAlpha(100);
	        ivDrawable.setOnClickListener(new OnClickListener() {
	        
				public void onClick(View v) {
				/*	isRepeat = false;
					currentAnimation = prepareStyle1Animation();
					currentAnimation.end();
					
				*/
					isRepeat=true;
			        currentAnimation = prepareStyle1Animation();
			        currentAnimation.start();
					listView.onDropDown();

				}
			});
	        drawable = new CircularProgressDrawable(getResources().getDimensionPixelSize(R.dimen.drawable_ring_size),
	                getResources().getColor(android.R.color.darker_gray),
	                getResources().getColor(android.R.color.holo_green_light),
	                getResources().getColor(android.R.color.holo_blue_dark));
	        ivDrawable.setImageDrawable(drawable);
	        //end     
	      /*
	        currentAnimation = prepareStyle1Animation();
	        currentAnimation.start();
	       */

	        
	        //添加监听
	        mainlistView.setOnScrollListener(new ScrollListener());
	        //mainlistView.setOnItemClickListener(new ItemClickListener());
	        mainlistView.setOnDropDownListener(new OnDropDownListener() {

	            public void onDropDown() {
	            	Log.i("onDropDown","loadfinish:"+loadfinish);
	            	if(loadfinish)
	            	{
						isRepeat=true;
				        currentAnimation = prepareStyle1Animation();
				        currentAnimation.start();
	            		new GetDataTask(true).execute();
	            	}
	                
	            }
	        });
	        mainlistView.setOnBottomListener(new OnClickListener() {

	            public void onClick(View v) {
	            	Log.i("tOnBottom","loadfinish:"+loadfinish);
	            	if(loadfinish)
	            	{
						isRepeat=true;
				        currentAnimation = prepareStyle1Animation();
				        currentAnimation.start();
	            		new GetDataTask(false).execute();
	            	}
	            }
	        });
	        
	        
       Log.i("ONcreate","OnCreate");
    }
    //begin FloatingActionButton
    private Animator prepareStyle1Animation() {
    	AnimatorSet animation = new AnimatorSet();

        final Animator indeterminateAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.PROGRESS_PROPERTY, 0, 3600);
        indeterminateAnimation.setDuration(3600);

        Animator innerCircleAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.CIRCLE_FILL_PROPERTY, 0f, 1f);
        innerCircleAnimation.setDuration(3600);
        innerCircleAnimation.addListener(new EmptyAnimatorListener() {
            public void onAnimationStart(Animator animation) {
            	//System.out.println("onAnimationStart");
                drawable.setIndeterminate(true);
//                isRepeat = true;
            }

            public void onAnimationEnd(Animator animation) {
            	//System.out.println("onAnimationEnd");
            	indeterminateAnimation.end();
                drawable.setIndeterminate(false);
                drawable.setProgress(0);
                if(isRepeat)
                	handler.sendEmptyMessage(1234);
            }
            
        });

        animation.playTogether(innerCircleAnimation, indeterminateAnimation);
        return animation;
    }
    
    class EmptyAnimatorListener implements Animator.AnimatorListener {
        public void onAnimationStart(Animator animation) {}
        public void onAnimationEnd(Animator animation) {}
        public void onAnimationCancel(Animator animation) {}
        public void onAnimationRepeat(Animator animation) {}
    }
    //end
    
    protected void onResume() {
		super.onResume(); 
		Log.i("onResume","onResume");
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
    
    public void jumpActivity(Class<?> cls) {
		// 查看具体内容
		Intent intent = new Intent(this, cls);
		Bundle b = new Bundle();
        //b.putString("videoid", listItem.get(position).get(key));
        intent.putExtras(b);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		
	}
    /**
     * 跳转到评论页面
     * @param cls		评论页面
     * @param position	哪个视频
     */
    public void jumpCommentActivity(Class<?> cls,int position) {
		// 查看具体内容
		Intent intent = new Intent(this, cls);
		Bundle b = new Bundle();
		//如果在讨论组
		if(isAtGroupTalk&&current_talkgroup_where==FinalName.ALL_DATE)
          {
			b.putString("id", listItem.get(position).get("uservideogroupid").toString());
			b.putBoolean("isAtGroupTalk", true);
          }
		else
			{
				if(isAtGroupTalk)
				{
					b.putString("id", listItem.get(position).get("id").toString());
					b.putBoolean("isAtGroupTalk", true);
				}
				else
				{
					b.putString("id", listItem.get(position).get("id").toString());
					b.putBoolean("isAtGroupTalk", false);
		
				}
				b.putInt("whichVideo", position);
					
			}
        intent.putExtras(b);
		//startActivity(intent);
		startActivityForResult(intent,COMMENT_ACTIVITY_REQUEST_CODE);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
    /**
     * 从网络获取数据
     * @param pagenum					获取的第几页数据
     * @param vedioclassificationid		获取视频种类
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public ArrayList<HashMap<String, Object>> getdate(int pagenum,String position,int vedioclassificationid) throws MalformedURLException, IOException
    {
    
    	Log.i("getdate","getdate:"+vedioclassificationid);
    	ArrayList<HashMap<String, Object>> date=new ArrayList<HashMap<String, Object>>();
    	String result=null;

    	
    	
    	try {
			//是否在搜索状态
        	if(isSearch)
        	{
            	//构造POST请求
            	HashMap<String, String> postMap=new HashMap<String, String>();
            	postMap.put("pageSize", String.valueOf(pageSize));
            	postMap.put("userid", String.valueOf(FinalUrl.userid));
            	postMap.put("pagenum", String.valueOf(pagenum));
            	postMap.put("position", position);
            	postMap.put("tag", tag);
            	
            	result=HttpUtils.postRequest(FinalUrl.PostSearchVideoUrl, postMap);
        	}
        	else {
            	//构造POST请求
            	HashMap<String, String> postMap=new HashMap<String, String>();
            if(isAtGroupTalk)
            {
            	postMap.put("pageSize", String.valueOf(pageSize));
            	postMap.put("userid", String.valueOf(FinalUrl.userid));
            	postMap.put("pagenum", String.valueOf(pagenum));
            	postMap.put("groupid", String.valueOf(vedioclassificationid));
            	result=HttpUtils.postRequest(FinalUrl.GetGroupVideoListUrl, postMap);
            	Log.i("groupDATE",result);
            }
            else {
            	postMap.put("pageSize", String.valueOf(pageSize));
            	postMap.put("username", FinalUrl.username);
            	postMap.put("userid", String.valueOf(FinalUrl.userid));
            	postMap.put("pagenum", String.valueOf(pagenum));
            	postMap.put("vedioclassificationid", String.valueOf(vedioclassificationid));
            	postMap.put("position", position);
            	result=HttpUtils.postRequest(FinalUrl.GetVedioUrlOne, postMap);
            	Log.i("squareDATE",result);
            }

            	
    			}

			JSONArray array = new JSONObject(result).getJSONArray("uservideolist");
			for(int i = 0 ; i < array.length() ; i++){
				JSONObject object=array.getJSONObject(i);
				JSONObject userjsonObject = object.getJSONObject("user");
				JSONObject videojsonObject = object.getJSONObject("video");
				JSONObject fromuserjsonObject = object.getJSONObject("fromuser");
				JSONObject channeljsonObject = object.getJSONObject("channel");								
				HashMap<String, Object> map = new HashMap<String, Object>(); 
				
				  map.put("id",object.getString("id") );
				  map.put("Iscanlike",object.getInt("canzan") );
				  map.put("likeNum", object.getInt("zantimes"));
				  map.put("commentNum",object.getInt("commentnum"));
				  map.put("shareNum", object.getInt("sharenum"));
				  map.put("iscanconcern",object.getInt("canconcern") );
				  String fromuser=fromuser=fromuserjsonObject.getString("nickname");
				  Mymedia mymedia=new Mymedia(this,handler);
				  String positionString=videojsonObject.getString("position");				 				  
	              map.put("position", positionString);
	              map.put("mymedia",mymedia);
	              map.put("fromuser", fromuser);
				  map.put("playNum", videojsonObject.get("playtimes"));
	              map.put("authorName", userjsonObject.get("nickname"));
	              String time=videojsonObject.get("createtime").toString();
	              time=time.substring(0, time.indexOf(':',time.indexOf(':')+1));
	              map.put("time",time);
	              map.put("hasdanmaku", false);
	              
	      		  map.put("vediocategory", channeljsonObject.get("channelname"));
	      		  map.put("userid", userjsonObject.get("id").toString());
	      		  
	      		  Log.i("userimage",userjsonObject.get("userimage").toString());
	      		  map.put("schoolname", userjsonObject.getString("schoolname"));
	      		  map.put("headdrawable",FinalUrl.USERIMAGE_URL+userjsonObject.get("userimage").toString());
	              map.put("vediodrawable", FinalUrl.VIDEOIMAGE_URL+videojsonObject.get("videoimage").toString());
	              map.put("filename", videojsonObject.get("filename"));	              
	              map.put("introduce", videojsonObject.get("description"));
	           // map.put("vedioPath", HttpUtils.VIDEO_URL+videojsonObject.get("filename"));
	              map.put("vedioPath",FinalUrl.VIDEO_URL+videojsonObject.get("filename"));
	              map.put("position", videojsonObject.get("position"));
				
	              
	              date.add(map); 
			} 
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	

    	  return date;
    }
    private ArrayList<HashMap<String, Object>> getTalkGroupDate(int pagenum)
    {
    	ArrayList<HashMap<String, Object>> date=new ArrayList<HashMap<String, Object>>();
    	String result=null;

    	
    	
    	try {
			//是否在搜索状态
        	if(isSearch)
        	{
            	//构造POST请求
            	HashMap<String, String> postMap=new HashMap<String, String>();
            	postMap.put("pageSize", String.valueOf(pageSize));
            	postMap.put("userid", String.valueOf(FinalUrl.userid));
            	postMap.put("pagenum", String.valueOf(pagenum));
            	postMap.put("position", "company");
            	postMap.put("tag", tag);
            	
            	result=HttpUtils.postRequest(FinalUrl.PostSearchVideoUrl, postMap);
        	}
        	else {
            	//构造POST请求
            	HashMap<String, String> postMap=new HashMap<String, String>();
            	postMap.put("pageSize", String.valueOf(pageSize));
            	postMap.put("pagenum", String.valueOf(pagenum));
            	postMap.put("position", "company");
            	result=HttpUtils.postRequest(FinalUrl.GetGroupVedioUrl, postMap);
    			}

			JSONArray array = new JSONObject(result).getJSONArray("grouplist");
			for(int i = 0 ; i < array.length() ; i++){
				JSONObject object=array.getJSONObject(i);
				JSONObject userVideoGroupObject=object.getJSONObject("userVideoGroup");
				JSONObject userjsonObject = object.getJSONObject("user");
				JSONObject videojsonObject = userVideoGroupObject.getJSONObject("video");
				
				HashMap<String, Object> map = new HashMap<String, Object>(); 
				
				map.put("id", object.getString("id"));
				map.put("uservideogroupid", userVideoGroupObject.getInt("id"));
				map.put("groupname",  object.getString("name"));
				map.put("limitnum",  object.getInt("limitnum"));
				map.put("description",  object.getString("description"));
				map.put("groupimg",  FinalUrl.GroupIMAGE_URL+object.getString("image"));
				map.put("applynum",  object.getInt("applynum"));
				map.put("personnum",  object.getInt("personnum"));
				map.put("commenttimes",  object.getInt("commenttimes"));
				map.put("creatorname", userjsonObject.getString("nickname"));
				map.put("channelname", object.getString("channelname"));
				String positionString=videojsonObject.getString("position");				 				  
	            map.put("position", "company");
	            Mymedia mymedia=new Mymedia(this,handler);
	            map.put("hasdanmaku", false);
	            map.put("mymedia",mymedia);
	            
	            String time=videojsonObject.get("createtime").toString();
	            time=time.substring(0, time.indexOf(':',time.indexOf(':')+1));
	            map.put("time",time);
	            map.put("videoimg",FinalUrl.VIDEOIMAGE_URL+videojsonObject.getString("videoimage"));
	            map.put("filename", videojsonObject.getString("filename"));
	            map.put("vedioPath",FinalUrl.VIDEO_URL+videojsonObject.get("filename"));
	            map.put("playtimes",  videojsonObject.getString("playtimes"));
	              date.add(map); 
			} 
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	

    	  return date;
    }

    /**
     * 搁到个人信息
     * @param whichuser	得到哪个用户的个人信息，-1为自己
     */
    private void getUserinfo(final int whichuser)
    {
    	
		new Thread(new Runnable() {						
			public void run() {
				
				HashMap<String, String> map=new HashMap<String, String>();
				String result=null;
				HashMap<String, Object> resultmap=new HashMap<String, Object>();
				if(whichuser==-1)
				{
					map.put("userid", String.valueOf(FinalUrl.userid));
				}
				else {
					map.put("userid", (String)listItem.get(whichuser).get("userid"));
				}
				
				
				try {
					result=HttpUtils.postRequest(FinalUrl.PostUserinfoUrl, map);
					JSONObject object=new JSONObject(result);
					JSONObject userJsonObject=object.getJSONObject("user");
					JSONObject videoJsonObject=userJsonObject.getJSONObject("video");
					
					resultmap.put("fansnum", object.getString("fansnum"));
					resultmap.put("myforwardnum", object.getString("myforwardnum"));
					resultmap.put("myvideonum", object.getString("myvideonum"));
					resultmap.put("concernnum", object.getString("concernnum"));
					resultmap.put("mycollectionnum", object.getString("mycollectionnum"));
					resultmap.put("mycommentnum", object.getString("mycommentnum"));
					
					resultmap.put("nickname", userJsonObject.getString("nickname"));
					resultmap.put("schoolname", userJsonObject.getString("schoolname"));
					resultmap.put("userimage",FinalUrl.USERIMAGE_URL+userJsonObject.getString("userimage"));
					
					resultmap.put("videoimage", FinalUrl.VIDEOIMAGE_URL+videoJsonObject.getString("videoimage"));
					resultmap.put("filename", FinalUrl.VIDEO_URL+videoJsonObject.getString("filename"));
					
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_USERINFO,resultmap));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    private final class ItemClickListener implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			
			Log.i("ItemClickListener","ItemClickListener");
			listView.getItemIdAtPosition(position);
			close_popwindow(popupWindowmenu,FinalName.POP_MENU);
			close_popwindow(searchPopupWindow,FinalName.POP_POSITION_SQUARE);
			close_popwindow(searchPopupWindow,FinalName.POP_SEARCH);
		}
    }

    private final class ScrollListener implements OnScrollListener{
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.i("onScrollStateChanged","onScrollStateChanged");
			
			final int loadtotal = listItem.size();
			
			final int lastItemid;//获取当前屏幕最后Item的ID
			
			if(current_position==PositionEnum.PERSON)
			{
				lastItemid=listView.getLastVisiblePosition();
			}
			else {
				lastItemid=listView.getLastVisiblePosition()+1;
			}
			
			if(scrollState==1)
			{
				close_popwindow(popupWindowmenu,FinalName.POP_MENU);
				close_popwindow(squarePopupWindow,FinalName.POP_POSITION_SQUARE);
				
			}
			Log.i("onScrollStateChanged", "onScrollStateChanged(scrollState="+ scrollState+ ")"+"(lastitemID:"+lastItemid+")" +
					"(total:"+loadtotal+")");

		}
		
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			Log.i("onScroll","onScroll");
			
			if(cHandler!=null)					
				//查看哪个视频正在播放，是否要停止
				cHandler.sendMessage(cHandler.obtainMessage(FinalName.CHANDLER_CHECK_WHICH_ISPLAYING));
		}
    }
    /**
     * 
     * @param p			要关闭的弹窗
     * @param which_pop	要关闭的哪个弹窗
     */
    private void close_popwindow(PopupWindow p,int which_pop)
    {
		
    	
    	//Log.i("close_popwindow", "close_popwindow");
    	
    	switch (which_pop) {
		//要关闭菜单弹窗
    	case FinalName.POP_MENU:
		{
	    	//如果菜单弹窗开着，则关闭
			if(p!=null&&p.equals(popupWindowmenu)&&selectmenuImageView.getVisibility()==View.VISIBLE)
			{
				selectmenuImageView.setVisibility(View.INVISIBLE);
				popupWindowmenu.dismiss();
			}	
		}
			break;
		//要关闭广场弹窗	
		case FinalName.POP_POSITION_SQUARE:
		{
	    	//如果菜单弹窗开着，则关闭
			if(p!=null&&p.equals(squarePopupWindow)&&selectpositionImageView.getVisibility()==View.VISIBLE)
			{
				selectpositionImageView.setVisibility(View.INVISIBLE);
				squarePopupWindow.dismiss();
			}
		}
			break;
		case FinalName.POP_SEARCH:
		{
	    	//如果菜单弹窗开着，则关闭
			if(p!=null&&p.equals(searchPopupWindow)&&selectsearchImageView.getVisibility()==View.VISIBLE)
			{
				selectsearchImageView.setVisibility(View.INVISIBLE);
				searchPopupWindow.dismiss();
			}
		}
			break;
		case FinalName.POP_FRIEND:
		{
	    	//如果朋友圈弹窗开着，则关闭
			if(p!=null&&p.equals(friendPopupWindow)&&selectpositionImageView.getVisibility()==View.VISIBLE)
			{
				selectpositionImageView.setVisibility(View.INVISIBLE);
				friendPopupWindow.dismiss();
			}
		}
			break;	
		case FinalName.POP_POSITION_TALKGROUP:
		{
	    	//如果朋友圈弹窗开着，则关闭
			if(p!=null&&p.equals(groupPopupWindow)&&selectpositionImageView.getVisibility()==View.VISIBLE)
			{
				selectpositionImageView.setVisibility(View.INVISIBLE);
				groupPopupWindow.dismiss();
			}
		}
		break;
		default:
			break;
		}

		
    }
    //主线程消息处理，自动播放，暂停，数据加载
    Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case FinalName.NOTICE_UPDATE_LISTVIEW:		//主线程收到消息要更新listView数据
			{
				listItem.addAll((ArrayList<HashMap<String, Object>>) msg.obj);
				adapter.notifyDataSetInvalidated();//告诉ListView数据已经发生改变，要求ListView更新界面显示
				//if(listView.getFooterViewsCount() > 0) listView.removeFooterView(footer);
				loadfinish = true;
				//selectmenuImageView.setVisibility(View.INVISIBLE);
				cHandler.sendMessage(cHandler.obtainMessage(FinalName.CHANDLER_CHECK_WHICH_ISPLAYING));
			}
				
				break;
			case FinalName.NOTICE_NO_DATE:		//主线程收到消息显示没有数据了
			{
			//	listView.removeFooterView(footer);
				loadfinish = true;
				showToastMessage("没有更多数据了...");
			}
				
				break;
			case FinalName.NOTICE_LOAD_DATE:		//得到通知加载哪个区的数据
			{
				//结束搜索状态
				if(isSearch)
				{
					isSearch=false;
					tag=null;
				}
				
				close_popwindow(popupWindowmenu, FinalName.POP_MENU);
				
				switch ((Integer.valueOf(msg.obj.toString()))) {
				case R.id.popmenu_choose_square:
				{
				}					
					break;
				case R.id.popmenu_choose_friendgroup:
				{

				}					
					break;
				case R.id.popmenu_choose_talkgroup:
				{
				}					
					break;
					
				case R.id.popmenu_choose_person:
				{
				}					
					break;
				case R.id.popmenu_choose_blink:
				{
				}					
				default:
					break;
				}
			}
				
				break;	
			case FinalName.NOTICE_LOAD_USERINFO:
			{

				HashMap<String, Object> map=(HashMap<String, Object>) msg.obj;
				 asynImageLoader.showImageAsyn(videoImageView, String.valueOf(map.get("videoimage")), R.drawable.default_video_img); 
				 asynImageLoader.showImageAsyn(userImageView, String.valueOf(map.get("userimage")), R.drawable.square_item_defaulthead); 
				 usernickname.setText(map.get("nickname").toString());
				 userschool.setText(map.get("schoolname").toString());
				 userconcerns.setText(map.get("concernnum").toString());
				 userfans.setText(map.get("fansnum").toString());
				 uservideos.setText(map.get("myvideonum").toString());
				 usercollects.setText(map.get("mycollectionnum").toString());
				 usershares.setText(map.get("myforwardnum").toString());
				 usercomments.setText(map.get("mycommentnum").toString());
				 usermymedia.setPath(map.get("filename").toString());
				
					
					HashMap<String, Integer> postmap=new HashMap<String, Integer>();
					postmap.put("positionID",curent_positionStringid);
					postmap.put("whereInt",current_userinfo_where);

					//异步加载数据
			        new MyAsyncTask().execute(postmap);

			}
			break;
			case FinalName.NOTICE_LOAD_CLASSIFICATION_DATE:	//加载广场选定分类的数据列表
			{
				//结束搜索状态
				if(isSearch)
				{
					isSearch=false;
					tag=null;
				}
				
				int where=Integer.valueOf(msg.obj.toString());
				close_popwindow(squarePopupWindow, FinalName.POP_POSITION_SQUARE);
				//如果所处广场频道和选择频道相同则不需加载。
				if(current_square_where!=where)
				{
					listItem.clear();

					current_square_where=where;
				/*	//异步加载数据
					HashMap<String, Integer> map=new HashMap<String, Integer>();
					map.put("positionID", curent_positionStringid);
					map.put("whereInt",where);
					new MyAsyncTask().execute(map);
					*/
					listView.onDropDown();
				}
			}
			break;
			case FinalName.NOTICE_STOP:		//主线程收到通知停止播放
			{
				if(loadfinish == true)
				{
					((Mymedia)(msg.obj)).stop();
					cHandler.sendMessage(cHandler.obtainMessage(FinalName.CHANDLER_SEARCH_PLAY_WHICH));
				}
			}				
				break;
			case FinalName.NOTICE_AUTO_PLAY:		//主线程收到通知自动播放
			{
				if(loadfinish == true&&isAutoPlay)
				{
					HashMap<String, Object> result=(HashMap<String, Object>)msg.obj;
					ImageView vedioImageView=(ImageView)result.get("vedioImageView");
					SurfaceView surfaceView=(SurfaceView)result.get("surfaceView");
					Mymedia media=(Mymedia)result.get("mymedia");
					media.setPath(result.get("vedioPath").toString());				
					//Log.i("Main---surfaceView",surfaceView.toString());
					media.setSurfaceView(surfaceView);			
					
					if(media.getPosition()==-1)
					{
						media.setPosition(Integer.valueOf(result.get("position").toString()));
					}
					
					media.mediaplay(vedioImageView);
					//Log.i("Which MediaPlayer?",result.get("position").toString()+" Media: "+media.getMediaPlayer().toString());					
				}

			}
				
				break;
			case FinalName.NOTICE_PLAYNUM_ADD:	//播放视频时候通知服务器播放次数加1
			{
				try {
					int playWhich=Integer.parseInt(msg.obj.toString());
					
					postPlayNumAdd(playWhich);
				} catch (Exception e) {
					// TODO: handle exception
					Log.i("positonString",msg.obj.toString());
				}

				
			}
			break;
			case FinalName.NOTICE_ISLIKE:	//点赞与否通知
			{
				try {
					HashMap<String, Integer> result;
					result=(HashMap<String, Integer>)msg.obj;
					int which=result.get("which");
					int islike=result.get("isLike");
					
					postLikeNumAdd(which, islike);
					
				} catch (Exception e) {
					// TODO: handle exception
					Log.i("positonString",msg.obj.toString());
				}

				
			}
			break;	
			case FinalName.NOTICE_ISCONCERN:	//关注与否通知
			{
				try {
					HashMap<String, Integer> result;
					result=(HashMap<String, Integer>)msg.obj;
					int which=result.get("which");
					int isconcern=result.get("isconcern");
					
					String userid=listItem.get(which).get("userid").toString();

					
					changeAllConcernState(userid,isconcern);

					postConcern(which, isconcern);
					
				} catch (Exception e) {
					// TODO: handle exception
					Log.i("positonString",msg.obj.toString());
				}
			}
			break;
			case FinalName.NOTICE_COMMENTJUMP:	//评论页面跳转
			{
				jumpCommentActivity(CommentActivity.class,Integer.valueOf(String.valueOf(msg.obj)));
			}
			break;
			case FinalName.NOTICE_DANMAKU:		//通知要显示弹幕或者关闭弹幕
			{
				HashMap<String, Object> result;
				result=(HashMap<String, Object>)msg.obj;
				int which=Integer.valueOf(String.valueOf(result.get("which")));
				mDanmakuView=(IDanmakuView)result.get("mDanmakuView");
				int isshow=Integer.valueOf(String.valueOf(result.get("isshow")));
				
				HashMap<String, Object> map=listItem.get(which);
				boolean hasdanmaku=(Boolean)map.get("hasdanmaku");
				
				if(isshow==1)
				{
					//查看是否获取了弹幕
					if(hasdanmaku)
					{
						mDanmakuView.show();
					}
					else {
						postDanmaku(which);
						map.put("hasdanmaku", true);
						
					}
				}
					
				else if(isshow==0)
					{						
							mDanmakuView.hide();					
					}
			}
			break;
			case FinalName.NOTICE_GET_DANMAKU:		//通知弹幕已经获取
			{
				String json=(String)msg.obj;
				Log.i("InputStream",json);
				try {
					
					
					
					initDanmaku(json);
				} catch (IllegalDataException e) {
					
					e.printStackTrace();
				}
			}
			break;
			case FinalName.NOTICE_STOPPLAYER:		//点击播放时候通知停止其他正在播放视频
			{

				int playWhich=Integer.parseInt(msg.obj.toString());
				//如果前一个视频存在
				if(playWhich-1>=0)
				{
					Mymedia mymedia=(Mymedia)listItem.get(playWhich-1).get("mymedia");
					MediaPlayer mediaPlayer=mymedia.getMediaPlayer();
					if(mediaPlayer!=null&&mediaPlayer.isPlaying())
					{
						mymedia.stop();
					}
				}
				//如果下一个视频存在
				if(playWhich+1<listItem.size())
				{
					Mymedia mymedia=(Mymedia)listItem.get(playWhich+1).get("mymedia");
					MediaPlayer mediaPlayer=mymedia.getMediaPlayer();
					if(mediaPlayer!=null&&mediaPlayer.isPlaying())
					{
						mymedia.stop();
					}
				}
			}
			break;
			case FinalName.NOTICE_SHARE:				//转发视频
			{
				int which=Integer.parseInt(msg.obj.toString());
				postShare(which);
				
			}
			break;
			case FinalName.NOTICE_REPORT:				//举报视频
			{
				int which=Integer.parseInt(msg.obj.toString());
				postReport(which,"垃圾视频");
			}
			break;	
			case FinalName.NOTICE_COLLECT:				//收藏视频
			{
				int which=Integer.parseInt(msg.obj.toString());
				postCollect(which);
			}
			break;	
			case FinalName.NOTICE_LOAD_FRIENDINFO:
			{
				
				progressDialog.dismiss();
				mAdapter.notifyDataSetChanged();
			}
			break;
			case FinalName.NOTICE_LOAD_GROUPINFO:
			{
				
				progressDialog.dismiss();
				gAdapter.notifyDataSetChanged();
			}
			break;
			case FinalName.NOTICE_ADDGROUP_INFORMATION:
			{
				showToastMessage(msg.obj.toString());
			}
			break;
			case FinalName.NOTICE_ADD_SUCCESS:
			{
				mAdapter.notifyDataSetChanged();
				showToastMessage(msg.obj.toString());
			}
			break;
			case FinalName.NOTICE_ADD_FAILURE:
			{
				showToastMessage(msg.obj.toString());
			}
			break;

			case FinalName.NOTICE_SHOW_TOASTMESSAGE:	//显示吐丝信息
			{
				showToastMessage(msg.obj.toString());
			}
			break;
			
			case FinalName.NOTICE_TAG_CHANGE:			//通知改变了TAG
			{

				HashMap<String, String> map=(HashMap<String, String>) msg.obj;
				int which=Integer.valueOf(map.get("which").toString());
				String tag=map.get("tag");
				Log.i("NOTICE_TAG_CHANGE",tag+","+which);
				popInfomap.remove(which);
				popInfomap.put(which, tag);
				Log.i("NOTICE_TAG_CHANGE2",popInfomap.get(which));
				postPopChange(which,tag);
				
			}
			break;
			case FinalName.NOTICE_GET_POPINFO:			//得到squarePop中泡泡的信息,可以开始加载视频数据
			{	
				
				/*    HashMap<String, Integer> map=new HashMap<String, Integer>();
		        map.put("positionID", R.string.position_square);
		        map.put("whereInt",FinalName.SQUARE_ALL );
		        new MyAsyncTask().execute(map);
				*/
				listView.onDropDown();
				
			}
			break;
			case FinalName.NOTICE_GET_SEARCHDATE:			//通知加载搜索信息
			{
				isSearch=true;
				tag=msg.obj.toString();
				current_square_where=FinalName.SQUARE_SEARCH;

		        close_popwindow(squarePopupWindow, FinalName.POP_POSITION_SQUARE);
		        close_popwindow(searchPopupWindow, FinalName.POP_SEARCH);
				//异步加载数据
		     /*   HashMap<String, Integer> map=new HashMap<String, Integer>();
		        map.put("positionID",curent_positionStringid);
		        map.put("whereInt",current_userinfo_where);
		        new MyAsyncTask().execute(map);	
		       */
		        listView.onDropDown();
			}
			break;
			//申请加入群组
			case FinalName.NOTICE_APPLY_GROUP:
			{
				int which=Integer.parseInt(msg.obj.toString());
				postApplyGroup(which);
			}
			break;
			case FinalName.NOTICE_JUMPTO_CREATE_GROUP:
			{
				close_popwindow(groupPopupWindow, FinalName.POP_POSITION_TALKGROUP);
				
				startActivityForResult(new Intent(CompanySquareActivity.this,TalkGroupAddActivity.class),CREATE_ACTIVITY_REQUEST_CODE);
				
			}
			break;
			case FinalName.NOTICE_UPDATE_DIALOG_ADAPTER:		//更新讨论组对话框视图
			{
				((GroupPop)groupPopupWindow).updateMessageDialog();
			}
			break;
			case FinalName.NOTICE_ACCEPT_APPLY:
			{
				HashMap<String, String> map=(HashMap<String, String>) msg.obj;
				int which=Integer.valueOf(map.get("which"));
				postAgreeApply(((GroupPop)groupPopupWindow).getApplyGroupId(which),((GroupPop)groupPopupWindow).getGroupId(which),map.get("state"));
				
			}
			break;
			case FinalName.NOTICE_UPDATE_GADAPTER:
			{
				gAdapter.notifyDataSetChanged();
			}
			break;
			case FinalName.NOTICE_PAUSE_ANIMATION:
				currentAnimation = prepareStyle1Animation();
		        currentAnimation.start();
			break;
			case FinalName.NOTICE_SENDUSERID:
			{
				current_friendgroup_where=Integer.valueOf( msg.obj.toString());
				isRepeat=true;
		        currentAnimation = prepareStyle1Animation();
		        currentAnimation.start();
		        close_popwindow(friendPopupWindow, FinalName.POP_POSITION_FRIEND);
				listView.onDropDown();
			}
			break;
			case FinalName.NOTICE_SENDGROUPID:
			{
				Log.i("NOTICE_SENDGROUPID","NOTICE_SENDGROUPID");
				listItem.clear();
				bindSquareAdapter();
				adapter=mainAdapter;
				current_talkgroup_where=Integer.valueOf( msg.obj.toString());
				isRepeat=true;
		        currentAnimation = prepareStyle1Animation();
		        currentAnimation.start();
		        close_popwindow(groupPopupWindow, FinalName.POP_POSITION_TALKGROUP);
				listView.onDropDown();
			}
			break;
			default:
			break;
			}
		
		}

   	
    };
    //关注好友后，改变所有视频该好友的关注状态
	private void changeAllConcernState(String userid, int isconcern) {
		// TODO Auto-generated method stub

		for(int i=0;i<listItem.size();i++)
		{
			if(listItem.get(i).get("userid").equals(userid))
			{
				Log.i("whichUser", "whichUser"+i);
				if(isconcern==1)
					listItem.get(i).put("iscanconcern", 2);
				else {
					listItem.get(i).put("iscanconcern", 1);
				}
			}
		}
		
	} 
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    if (requestCode == CREATE_ACTIVITY_REQUEST_CODE) { 
	    	Log.i("CREATE_ACTIVITY_REQUEST_CODE","CREATE_ACTIVITY_REQUEST_CODE");
	        if (resultCode == RESULT_OK) { 
	        	Bundle bundle = data.getExtras(); 

	           String groupname = bundle.getString("groupname");
	           String groupintroduction = bundle.getString("groupintroduction"); 
	           String groupid = bundle.getString("groupid"); 
	           String grouptype = bundle.getString("grouptype"); 
	           
	           
	           //更新讨论组数据
	           Gdata.add(new GroupEntity("1.jpg", groupname, groupid, String.valueOf(FinalUrl.userid),
	        		   groupintroduction, String.valueOf(FinalUrl.userid), "暂无备注", grouptype));
	           List<GroupPerson> list=new ArrayList<GroupPerson>();
	           list.add(new GroupPerson(FinalUrl.userimage,FinalUrl.usernickname,String.valueOf(FinalUrl.userid)));
	    
	           Mdata.add(list);
	           gAdapter.notifyDataSetChanged();
	        }

	        else if (resultCode == RESULT_CANCELED) { 
//	            Toast.makeText(this, "RESULT_CANCELED ", Toast.LENGTH_SHORT).show(); 
	        	//do nothing
	        } 
	    }
        else if(requestCode==COMMENT_ACTIVITY_REQUEST_CODE)
        {
        	Log.i("COMMENT_ACTIVITY","COMMENT_ACTIVITY");
	        if (resultCode == RESULT_OK) { 
	        	
	        	if(data==null)
	        	{
	        		Log.i("NO DATA","NO DATA");
	        	}
	        	else {
	        		Log.i("YES DATA","YES DATA");
		        	Bundle bundle = data.getExtras(); 

			           int whichVideo = bundle.getInt("whichVideo");
			           int commentNum=bundle.getInt("commentNum");
			           View v=listView.getChildAt(whichVideo);
			           TextView commenTextView=(TextView)v.findViewById(R.id.company_square_item_commentNum);
			           commenTextView.setText(String.valueOf(Integer.valueOf(commenTextView.getText().toString())+commentNum));
				}


	        }

	        else if (resultCode == RESULT_CANCELED) { 
//	            Toast.makeText(this, "RESULT_CANCELED ", Toast.LENGTH_SHORT).show(); 
	        	//do nothing
	        } 
        }
	} 
    
	/**
	 * 通知服务器同意申请
	 * @param groupId	同意的群组ID
	 */
	void postAgreeApply(final String applygroupid,final String groupid,final String state)
	{
		new Thread(new Runnable() {						
			public void run() {
				Log.i("postAgreeApply",applygroupid+","+state);
				HashMap<String, String> postmap=new HashMap<String, String>();
				String result=null;
				postmap.put("applygroupid", applygroupid);				
				postmap.put("state",state);
				Log.i("postmap",postmap.get("applygroupid"));
				try {
					String jsonString=HttpUtils.postRequest(FinalUrl.PostDealApplyUrl, postmap);
					JSONObject object=new JSONObject(jsonString);
					result=object.getString("result");
					handler.sendMessage(handler.obtainMessage
							(FinalName.NOTICE_SHOW_TOASTMESSAGE,result));
			 
					JSONObject userJsonObject=object.getJSONObject("user");
					if(userJsonObject!=null)
					{
						
						
						int location=-1;
						for(int i=0;i<Gdata.size();i++)
						{
							if(Gdata.get(i).getId().equals(groupid))
							{
								location=i;
								break;
							}
						}
						
						if(location!=-1)
						{
							GroupPerson groupPerson=new GroupPerson(userJsonObject.getString("userimage"), userJsonObject.getString("nickname"), object.getString("usergroupid"));
							Mdata.get(location).add(groupPerson);
							handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_UPDATE_GADAPTER));
						}
						
					}
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
	}
    /**
     * 加入哪个群组
     * @param which	群组对应视频的条目号
     */
    void postApplyGroup(final int which)
    {
		new Thread(new Runnable() {						
			public void run() {
				
				HashMap<String, String> postmap=new HashMap<String, String>();
				String result=null;
				postmap.put("groupid", (String)(listItem.get(which).get("id")));				
				postmap.put("userid",String.valueOf(FinalUrl.userid));
				Log.i("groupid,userid",postmap.get("groupid")+","+postmap.get("userid"));
				try {
					result=HttpUtils.postRequest(FinalUrl.PostApplyGroupUrl, postmap);
					handler.sendMessage(handler.obtainMessage
							(FinalName.NOTICE_SHOW_TOASTMESSAGE,new JSONObject(result).getString("result")));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    /**
     * 开启线程通知服务器呗播放视频播放次数加一
     * @param playWhich	被播放的视频
     */
    void postPlayNumAdd(final int playWhich)
    {
		new Thread(new Runnable() {						
			public void run() {
				
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("filename", (String)listItem.get(playWhich).get("filename"));
				
				try {
					HttpUtils.postRequest(FinalUrl.PostPlayNumUrl, map);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    /**
     * 点赞或者取消点赞
     * @param Which		点击哪个视频		
     * @param isLike	点赞=1，取消点赞=2
     */
    void postLikeNumAdd(final int which,final int isLike)
    {
		new Thread(new Runnable() {						
			public void run() {
				
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("id", (String)listItem.get(which).get("id"));
				map.put("userid", String.valueOf(FinalUrl.userid));
				map.put("islike", String.valueOf(isLike));
				try {
					if(isAtGroupTalk)
					{
						HttpUtils.postRequest(FinalUrl.PostGroupVideoLikeNumUrl, map);						
					}
					else
					HttpUtils.postRequest(FinalUrl.PostLikeNumUrl, map);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    void postDanmaku(final int which)
    {
    	
		new Thread(new Runnable() {						
			public void run() {
				String result=null;
				HashMap<String, String> map=new HashMap<String, String>();
				
				try {
					if(isAtGroupTalk)
					{
						if(current_talkgroup_where==FinalName.ALL_DATE)
						{
							map.put("uservideogroupid", String.valueOf(listItem.get(which).get("uservideogroupid")));
							result=HttpUtils.postRequest(FinalUrl.GetGroupDanmakuListUrl, map);
						}
						else
						{
							
							map.put("uservideogroupid", (String)listItem.get(which).get("id"));
							result=HttpUtils.postRequest(FinalUrl.GetGroupDanmakuListUrl, map);
						}
						
					}
					else {
						map.put("id", (String)listItem.get(which).get("id"));
						result=HttpUtils.postRequest(FinalUrl.GetDanmakuListUrl, map);
					}

					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_DANMAKU,result));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    /**
     * 通知服务器关注了该视频发布用户
     * @param which
     * @param isConcern
     */
    void postConcern(final int which,final int isconcern)
    {
		new Thread(new Runnable() {						
			public void run() {
				
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("id", (String)listItem.get(which).get("id"));
				map.put("userid", String.valueOf(FinalUrl.userid));
				map.put("isconcern", String.valueOf(isconcern));
				try {
					HttpUtils.postRequest(FinalUrl.PostConcernNumUrl, map);
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    /**
     * 转发分享视频
     * @param which
     */
    void postShare(final int which)
    {
		new Thread(new Runnable() {						
			public void run() {
				
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("id", (String)listItem.get(which).get("id"));
				map.put("userid", String.valueOf(FinalUrl.userid));
				try {
					if(isAtGroupTalk)
					{
						HttpUtils.postRequest(FinalUrl.PostGroupShareUrl, map);
					}
					else {
						HttpUtils.postRequest(FinalUrl.PostShareUrl, map);
					}
					
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"转发成功"));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    /**
     * 举报视频
     * @param which		举报哪个视频
     * @param msg		举报信息
     */
    void postReport(final int which,final String msg)
    {
		new Thread(new Runnable() {						
			public void run() {
				
				
				HashMap<String, String> map=new HashMap<String, String>();
				


				try {
					if(isAtGroupTalk)
					{
					  map.put("uservideogroupid", String.valueOf(listItem.get(which).get("uservideogroupid")));
					  map.put("userid", String.valueOf(FinalUrl.userid));
					  map.put("reason", msg);	
					  HttpUtils.postRequest(FinalUrl.PostGroupReportUrl, map);
					}
					else
					{
						  map.put("id", (String)listItem.get(which).get("id"));
						  map.put("userid", String.valueOf(FinalUrl.userid));
						  map.put("reason", msg);	
						  HttpUtils.postRequest(FinalUrl.PostReportUrl, map);
					}
					
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"举报成功"));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    
    /**
     * 通知服务器分类信息的改变
     * @param which	哪个泡泡
     */
    void postPopChange(final int which,final String text)
    {
		new Thread(new Runnable() {						
			public void run() {
				
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("tagname",String.valueOf(which) );
				map.put("userid", String.valueOf(FinalUrl.userid));
				map.put("tag",text);
				try {
					HttpUtils.postRequest(FinalUrl.PostChangePopInfoUrl, map);
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"标签编辑成功"));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    /**
     * 得到视频分类泡泡的信息
     */
    void postPopInfo()
    {
		new Thread(new Runnable() {						
			public void run() {

		        popInfomap=new HashMap<Integer, String>();
		        
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("userid", String.valueOf(FinalUrl.userid));
				try {
					HashMap<Integer, String> resultmap=new HashMap<Integer, String>();
					String result=HttpUtils.postRequest(FinalUrl.PostPopinfoUrl, map);
					JSONArray array = new JSONObject(result).getJSONArray("userVideoTaglist");

					for(int i=0;i<array.length();i++)
					{
						resultmap.put(Integer.valueOf(array.getJSONObject(i).getString("tagname")),
								array.getJSONObject(i).getString("tag"));
					}
					
					popInfomap.putAll(resultmap);
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_GET_POPINFO));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    void postCollect(final int which)
    {
		new Thread(new Runnable() {						
			public void run() {
				
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("id", (String)listItem.get(which).get("id"));
				map.put("userid", String.valueOf(FinalUrl.userid));
				try {
					String result=HttpUtils.postRequest(FinalUrl.PostCollectiontUrl, map);
					Log.i("result",result);
					handler.sendMessage(handler.obtainMessage
							(FinalName.NOTICE_SHOW_TOASTMESSAGE,new JSONObject(result).getString("result")));
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}).start();
    }
    //子线程处理各种判断，通知主线程更新
    private  class ChildThread extends Thread {
    	public void run() {
    		//初始化消息循环队列，需要在Handler创建之前
            Looper.prepare();


			
			
			
            cHandler=new Handler(){            	
            	public void handleMessage(Message msg) {
        			 int firstItemid=listView.getFirstVisiblePosition();
        			 int lastItemid = listView.getLastVisiblePosition();//获取当前屏幕最后Item的ID
        			 int itemNum=listItem.size();
            		
     				boolean isplaying=false;
    				int playingItem=-1;

    				//Log.i("Child Receive:","Message:"+msg.what);
    		/*		
    				for(int i=0;i<itemNum;i++)
    				{
     					HashMap<String, Object> map=listItem.get(i);
     					
     					Mymedia media=(Mymedia)map.get("mymedia");
     					MediaPlayer mediaPlayer=media.getMediaPlayer();
     					
     					if(mediaPlayer!=null)
     						{
     						Log.i("Item Play","Item:"+i+",play="+mediaPlayer.isPlaying());
     							
     						}
    				}
    			*/	
    				//遍历寻找正在播放的视频
    				for(int i=0;i<itemNum;i++)
    				{
     					HashMap<String, Object> map=listItem.get(i);
     					
     					Mymedia media=(Mymedia)map.get("mymedia");
     					MediaPlayer mediaPlayer=media.getMediaPlayer();
     					
     					if(mediaPlayer!=null&&mediaPlayer.isPlaying())
     						{
     							isplaying=true;
     							playingItem=i;
     							break;
     						}
    				}
    				

    				
     				//Log.i("isplaying",String.valueOf(isplaying));
     			//	Log.i("playingItem",String.valueOf(playingItem));
     				
     				switch (msg.what) {
     				
     				//得到主线程已经停止播放视频，寻找可以开始自动播放的视频
					case FinalName.CHANDLER_SEARCH_PLAY_WHICH:
					{
						if(isplaying==false)
        				{
        					
        					for(int i=0;i<listView.getChildCount()&&firstItemid+i<itemNum;i++)
        					{
        						View v=listView.getChildAt(i);
        						ImageView vedioImageView=(ImageView)v.findViewById(R.id.company_square_item_videoimageView);	
        						SurfaceView surfaceView=(SurfaceView)v.findViewById(R.id.company_square_item_surfaceView);
        							Rect visibleRect=new Rect();
        							Rect drawRect=new Rect();
        							int location[]={0,0};
        							surfaceView.getGlobalVisibleRect(visibleRect);
        							surfaceView.getDrawingRect(drawRect);
        							
        							surfaceView.getLocationOnScreen(location);
        						//	Log.i("visibleRect",i+"----visibleRect"+visibleRect.height());
        						//	Log.i("location", location[0]+","+location[1]);
        						//	Log.i("drawRect","drawRect"+drawRect.height());
        						if(location[1]>0&&visibleRect.height()==drawRect.height())
        						{
        							
        							//Log.i("Which Will Play?","Which Will Play? "+firstItemid+i);
        							
        							HashMap<String, Object> map=listItem.get(firstItemid+i);
        							
        							HashMap<String, Object> result=new HashMap<String, Object>();
        							Mymedia media=(Mymedia)map.get("mymedia");
        							
        							result.put("vedioPath", map.get("vedioPath"));
        							result.put("vedioImageView", vedioImageView);
        							result.put("surfaceView", surfaceView);
        							result.put("mymedia", media);
        							result.put("position", firstItemid+i);
        							handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_AUTO_PLAY, result));//通知主线程自动播放
        							break;

        						}
        					}
        					
        				}
					}
						break;
						
						//得到主线程发送的如果有加载下一页，已经加载完毕，可以停止播放的消息
					case FinalName.CHANDLER_CHECK_WHICH_ISPLAYING:
					{
						//Log.i("Check PAUSE_PLAY","Check PAUSE_PLAY");
	    				int pauseItem=-1;
	    				boolean ispause=false;
						//检查是否有暂停的视频，有的话停止他
						for(int i=0;i<itemNum;i++)
	    				{
	     					HashMap<String, Object> map=listItem.get(i);
	     					
	     					Mymedia media=(Mymedia)map.get("mymedia");
	     					
	     					if(media.isPause())
	     						{
	     							pauseItem=i;
	     							ispause=true;
	     							break;
	     						}
	    				}
						
						if(ispause&&listItem!=null)
						{
							HashMap<String, Object> map=listItem.get(pauseItem);
            				
            				Mymedia pausemymedia=(Mymedia)map.get("mymedia");
            				
            				
            				SurfaceView surfaceView=(SurfaceView)pausemymedia.getSurfaceView();
            				if(pauseItem<itemNum&&surfaceView!=null)
            				{
            					Rect visibleRect=new Rect();
            					Rect drawRect=new Rect();
            					surfaceView.getGlobalVisibleRect(visibleRect);
            					surfaceView.getDrawingRect(drawRect);
            					//Log.i("visibleRect","visibleRect"+visibleRect.top);
            					if(visibleRect.height()<drawRect.height()/2)
            					{
            						handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_STOP, pausemymedia));
            						//firstmymedia.stop();
            						
            					}
            				}             				
						}
						
						
						
						
						//如果有正在播放的，通知主线程停止播放。
            			if(isplaying==true&&listItem!=null)
            			{
            				
            				HashMap<String, Object> map=listItem.get(playingItem);
            				
            				Mymedia playingmymedia=(Mymedia)map.get("mymedia");
            				
            				
            				SurfaceView surfaceView=(SurfaceView)playingmymedia.getSurfaceView();
            				
            				
            				if(playingItem<itemNum&&surfaceView!=null)
            				{
            					Rect visibleRect=new Rect();
            					Rect drawRect=new Rect();
            					surfaceView.getGlobalVisibleRect(visibleRect);
            					surfaceView.getDrawingRect(drawRect);
            					//Log.i("visibleRect","visibleRect"+visibleRect.top);
            					if(visibleRect.height()<drawRect.height()/2)
            					{
            						handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_STOP, playingmymedia));
            						//firstmymedia.stop();
            						
            					}
            				}           				

            			}
            			//如果自动播放，且没有正在播放，则通知主线程播放
            			else if(isAutoPlay)
            				cHandler.sendMessage(cHandler.obtainMessage(FinalName.CHANDLER_SEARCH_PLAY_WHICH));
					}
						break;
		
					default:
						break;
					}
            		

            	}
                
                
            };
			
          //启动子线程消息循环队列
            Looper.loop();
    	}
    }
 	
    private final class MyAsyncTask extends AsyncTask<HashMap<String,Integer>, integer, ArrayList<HashMap<String, Object>>>
    {

    	//先执行，获取数据
		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(
				HashMap<String,Integer>... params) {			
			ArrayList<HashMap<String, Object>> result=null;
			try {
				//Log.i("MyAsyncTask",getString(params[0].get("positionID"))+","+params[0].get("whereInt"));

				//如果不是讨论组
				if(isAtGroupTalk&&current_talkgroup_where==FinalName.ALL_DATE)
					{
					result=getTalkGroupDate(1);
						
					}
				else {
					result = getdate(1,"company",params[0].get("whereInt"));
				}	
				
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			return result;
		}
		
		//得到数据后更新UI
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> result)
		{
				listItem.clear();
	    		listItem.addAll(result);
	    		adapter.notifyDataSetInvalidated();
	    		
				if(current_position==PositionEnum.PERSON)
				{
			        mainlayout.removeAllViews();
					mainlayout.addView(userinfo_mainview);
				}

				
		       progressDialog.dismiss();		       
		      if(result.size()==0)
		      {
		    	  handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"目前该类没有视频。。"));
		      }
		       
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			progressDialog.setTitle("加载中....");
			progressDialog.show();
		}
    }
    //初始化讨论组
    private void bindGroupTalkAdapter()
    {
      //  mainlistView.addFooterView(footer);//添加页脚(放在ListView最后)             
        mainlistView.setAdapter(groupAdapter);
       // mainlistView.removeFooterView(footer);
    }
    private void bindSquareAdapter()
    {
      //  mainlistView.addFooterView(footer);//添加页脚(放在ListView最后)             
        mainlistView.setAdapter(mainAdapter);
      //  mainlistView.removeFooterView(footer);
    }
    //初始化个人主页
   private void initUserInfo()
   {
	   View userinfo_head=inflater.inflate(R.layout.userinfo_head, null);
	   LinearLayout userinfoview;
	   final FButton userset;
       userinfoview=(LinearLayout)userinfo_head.findViewById(R.id.userinfoview);
       userinfo_mainview=(LinearLayout)inflater.inflate(R.layout.userinfo, null).findViewById(R.id.userinfo_mainview);
       userInfoListView=(DropDownListView)userinfo_mainview.findViewById(R.id.userinfo_listView);
       
       
       userInfoListView.addHeaderView(userinfo_head);
     //  userInfoListView.addFooterView(footer);//添加页脚(放在ListView最后)
       userInfoadapter=new MediaAdapter(CompanySquareActivity.this, listItem, R.layout.company_square_item,handler);  
       userInfoListView.setAdapter(userInfoadapter);
     //  userInfoListView.removeFooterView(footer);  
       userInfoListView.setOnScrollListener(new ScrollListener());
       userInfoListView.setOnItemClickListener(new ItemClickListener());
       userInfoListView.setOnBottomListener(new OnClickListener() {

           public void onClick(View v) {
           	Log.i("tOnBottom","loadfinish:"+loadfinish);
           	if(loadfinish)
           	{
           		new GetDataTask(false).execute();
           	}
           }
       });
       
   	Log.i("initUserInfo","initUserInfo");
          surfaceView=(SurfaceView)userinfoview.findViewById(R.id.userinfo_surfaceView);
		  videoImageView=(ImageView)userinfoview.findViewById(R.id.userinfo_videoimg);
		  userImageView=(ImageView)userinfoview.findViewById(R.id.userpic);
		  usernickname=(TextView)userinfoview.findViewById(R.id.userinfo_username);
		  userschool=(TextView)userinfoview.findViewById(R.id.userinfo_userschool);
		  userconcerns=(TextView)userinfoview.findViewById(R.id.userinfo_concernNum);
		  userfans=(TextView)userinfoview.findViewById(R.id.userinfo_fansNum);
		  uservideos=(TextView)userinfoview.findViewById(R.id.userinfo_videoNum);
		  usershares=(TextView)userinfoview.findViewById(R.id.userinfo_shareNum);
		  usercomments=(TextView)userinfoview.findViewById(R.id.userinfo_commentNum);
		  usercollects=(TextView)userinfoview.findViewById(R.id.userinfo_collectNum);
		  userset=(FButton)userinfoview.findViewById(R.id.userinfo_setting);
		  usermymedia=new Mymedia(CompanySquareActivity.this, handler);
	   
	   
       	selectvideoImageView=(ImageView) userinfoview.findViewById(R.id.userinfo_selectvideobar);
       	selectshareImageView=(ImageView) userinfoview.findViewById(R.id.userinfo_selectsharebar);
       	selectcommentImageView=(ImageView) userinfoview.findViewById(R.id.userinfo_selectcommentbar);
       	selectcollectImageView=(ImageView) userinfoview.findViewById(R.id.userinfo_selectcollectbar);
       	
       	selectvideoImageView.setImageBitmap(PicUtil.readBitMap(CompanySquareActivity.this, R.drawable.topmenu_selectsquare));
       	selectshareImageView.setImageBitmap(PicUtil.readBitMap(CompanySquareActivity.this, R.drawable.topmenu_selectsquare));
       	selectcommentImageView.setImageBitmap(PicUtil.readBitMap(CompanySquareActivity.this, R.drawable.topmenu_selectsquare));
       	selectcollectImageView.setImageBitmap(PicUtil.readBitMap(CompanySquareActivity.this, R.drawable.topmenu_selectsquare));
       	
       	ImageView userinfo_video=(ImageView) userinfoview.findViewById(R.id.userinfo_video);
    	ImageView shareoImageView=(ImageView) userinfoview.findViewById(R.id.userinfo_shareimg);
    	ImageView commentImageView=(ImageView) userinfoview.findViewById(R.id.userinfo_commentimg);
    	ImageView collectImageView=(ImageView) userinfoview.findViewById(R.id.userinfo_collectimg);
    	
    	userinfo_video.setImageBitmap(PicUtil.readBitMap(CompanySquareActivity.this, R.drawable.userinfo_video));
    	shareoImageView.setImageBitmap(PicUtil.readBitMap(CompanySquareActivity.this, R.drawable.userinfo_pushimg));
    	commentImageView.setImageBitmap(PicUtil.readBitMap(CompanySquareActivity.this, R.drawable.userinfo_messageimg));
    	collectImageView.setImageBitmap(PicUtil.readBitMap(CompanySquareActivity.this, R.drawable.square_item_collect));
    	
    	
       	RelativeLayout videoLayout=(RelativeLayout)userinfoview.findViewById(R.id.userinfo_videolayout);
       	RelativeLayout sharelayout=(RelativeLayout)userinfoview.findViewById(R.id.userinfo_sharelayout);
       	RelativeLayout commentlayout=(RelativeLayout)userinfoview.findViewById(R.id.userinfo_commentlayout);
       	RelativeLayout collectlayout=(RelativeLayout)userinfoview.findViewById(R.id.userinfo_collectlayout);
       	UserInfoMenuClick click=new UserInfoMenuClick();
       	videoLayout.setOnClickListener(click);
       	sharelayout.setOnClickListener(click);
       	commentlayout.setOnClickListener(click);
       	collectlayout.setOnClickListener(click);
       	
       /*	userset.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					userset.setBackgroundResource(R.drawable.userinfo_set_press);
				}
				else if(event.getAction()==MotionEvent.ACTION_UP)
				{
					userset.setBackgroundResource(R.drawable.userinfo_set);
				}
				return false;
			}
		});*/
       	userset.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				jumpActivity(UserSetActivity.class);
				finish();
			}
		});
   }
    private class UserInfoMenuClick implements View.OnClickListener
    {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			selectvideoImageView.setVisibility(View.INVISIBLE);
			selectshareImageView.setVisibility(View.INVISIBLE);
			selectcommentImageView.setVisibility(View.INVISIBLE);
			selectcollectImageView.setVisibility(View.INVISIBLE);
			switch (v.getId()) {
			case R.id.userinfo_videolayout:
			{
				Log.i("Userinfo","Select Video");				
				selectvideoImageView.setVisibility(View.VISIBLE);
				if(current_userinfo_where!=FinalName.USERINFO_VIDEO)
				{
					curent_positionStringid=R.string.position_person;
					current_userinfo_where=FinalName.USERINFO_VIDEO;
			        HashMap<String, Integer> map=new HashMap<String, Integer>();
			        map.put("positionID",curent_positionStringid);
			        map.put("whereInt",current_userinfo_where);
					//异步加载数据
			        new MyAsyncTask().execute(map);	
				}
				
			}
			break;
			case R.id.userinfo_sharelayout:
			{
				Log.i("Userinfo","Select Share");
				selectshareImageView.setVisibility(View.VISIBLE);
				
				if(current_userinfo_where!=FinalName.USERINFO_SHARE)
				{
					curent_positionStringid=R.string.position_person;
					current_userinfo_where=FinalName.USERINFO_SHARE;
			        HashMap<String, Integer> map=new HashMap<String, Integer>();
			        map.put("positionID",curent_positionStringid);
			        map.put("whereInt",current_userinfo_where);
					//异步加载数据
			        new MyAsyncTask().execute(map);	
				}
			}
			break;
			case R.id.userinfo_commentlayout:
			{
				selectcommentImageView.setVisibility(View.VISIBLE);
			
			
				if(current_userinfo_where!=FinalName.USERINFO_COMMENT)
				{
					curent_positionStringid=R.string.position_person;
					current_userinfo_where=FinalName.USERINFO_COMMENT;
			        HashMap<String, Integer> map=new HashMap<String, Integer>();
			        map.put("positionID",curent_positionStringid);
			        map.put("whereInt",current_userinfo_where);
					//异步加载数据
			        new MyAsyncTask().execute(map);	
				}
			}
			break;
			case R.id.userinfo_collectlayout:
			{
				selectcollectImageView.setVisibility(View.VISIBLE);
				
				if(current_userinfo_where!=FinalName.USERINFO_COLLECT)
				{
					curent_positionStringid=R.string.position_person;
					current_userinfo_where=FinalName.USERINFO_COLLECT;
			        HashMap<String, Integer> map=new HashMap<String, Integer>();
			        map.put("positionID",curent_positionStringid);
			        map.put("whereInt",current_userinfo_where);
					//异步加载数据
			        new MyAsyncTask().execute(map);	
				}
			}
			break;	
			default:
				break;
			}
		}
    	
    }
    private class MenuClick implements View.OnClickListener
    {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.topmenu_menuImage:
			{
				CompanySquareActivity.this.finish();
			}	
				break;
			case R.id.topmenu_positionImage:
			{
			}	
				break;
			case R.id.topmenu_serachImage:
			{
			}
				

				
				break;
			case R.id.topmenu_cameraImage:
			{
				Log.i("Click ON","camera");
				Intent intent = new Intent(CompanySquareActivity.this, MediaRecorderActivity.class);
				Bundle b = new Bundle();
		        b.putString("videotype", "");
		        b.putString("iscommunty", "1");
		        intent.putExtras(b);
		        startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}	
				break;

			default:
				break;
			}
		}
    	
    }
    private void initPopmenu(int whichpop)
    {
    	switch (whichpop) {
		case FinalName.POP_MENU:
		{
			popupWindowmenu =new MenuPop(R.layout.menupopwindow, CompanySquareActivity.this,handler);   
		}
			break;
		case FinalName.POP_POSITION_SQUARE:
		{
			squarePopupWindow =new SquarePop(R.layout.square_popwindow, CompanySquareActivity.this,handler);   
		}
				break;
		case FinalName.POP_SEARCH:
		{
			searchPopupWindow =new SearchPop(R.layout.searchpopwindow, CompanySquareActivity.this,handler);   
		}
				break;	
		case FinalName.POP_FRIEND:
		{
		    
		    initData();
		    mAdapter = new SwipeAdapter(CompanySquareActivity.this,data);
			friendPopupWindow=new FriendPop(R.layout.activity_main, CompanySquareActivity.this,handler,mAdapter,data,data.size()); 
			getfriendlist();
			progressDialog.setTitle("好友列表加载中....");
			progressDialog.show();
		}
				break;
		case FinalName.POP_POSITION_TALKGROUP:
		{
			initGroupPopDate();
			gAdapter = new GroupAdapter(CompanySquareActivity.this,Gdata,Mdata,120);
			

			
			
			groupPopupWindow=new GroupPop(R.layout.talkgroup,CompanySquareActivity.this,handler, gAdapter, Gdata, Mdata);
			getGroup();
			
			//初始化讨论组中群组动态Dialog的适配器
			List<String> checkMessageList=new ArrayList<String>();
			CheckMessageAdapter checkMessageAdapter=new CheckMessageAdapter(checkMessageList, R.layout.groupmessage_dialog_check_item, CompanySquareActivity.this, handler);
			((GroupPop)groupPopupWindow).initCheckMessage(checkMessageAdapter, checkMessageList);
			
			progressDialog.setTitle("群组信息加载中....");
			progressDialog.show();
		}
				
		default:
			break;
		}
    		

    }
   private void getGroup()
   {
        new Thread(new Runnable() {						
			      public void run() {  	
				HashMap<String, String> map=new HashMap<String, String>();
				String result=null;
				Gdata.removeAll(Gdata);
				Mdata.removeAll(Mdata);
				map.put("userid", String.valueOf(FinalUrl.userid));
				try {					
					result=HttpUtils.postRequest(FinalUrl.GetGroupListUrl,map);
					
					JSONArray grouparray = new JSONObject(result).getJSONArray("grouplist");
					for(int i=0;i<grouparray.length();i++)
					{
						GroupEntity group = null;
						JSONObject createuserjsonObject = grouparray.getJSONObject(i).getJSONObject("user");					
						String id=grouparray.getJSONObject(i).getString("id");
						
						String remarks=grouparray.getJSONObject(i).getString("remarks");
						
						if(remarks.equals("null")||remarks.contains("null"))
						remarks="暂无备注";
						String name=grouparray.getJSONObject(i).getString("name");
						
						String declare=grouparray.getJSONObject(i).getString("description");
						
						String groupimage=grouparray.getJSONObject(i).getString("image");
						
						String createid=createuserjsonObject.getString("id");
						
						String type=grouparray.getJSONObject(i).getString("channelname");
						group=new GroupEntity(groupimage, name, id, createid, declare,String.valueOf(FinalUrl.userid),remarks,type
								);
						Gdata.add(group);
					}
					for(int i=0;i<grouparray.length();i++)
					{
						JSONArray userarray= grouparray.getJSONObject(i).getJSONArray("userlist");
						List<GroupPerson> list = new ArrayList<GroupPerson>();
						for(int j=0;j<userarray.length();j++)
				    	{
							GroupPerson group = null;
							String id=userarray.getJSONObject(j).getString("id");
							Log.i("tag",id);
						    String userimage=userarray.getJSONObject(j).getString("userimage");
						    Log.i("tag",userimage);
						    String nickname=userarray.getJSONObject(j).getString("nickname");
						    Log.i("tag",nickname);
						    group=new GroupPerson(userimage,nickname, id);
							list.add(group);
				    	}
					    Mdata.add(list);						
					}
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_GROUPINFO,Mdata));//通知主线程自动播放		

				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			}).start();	
   
   }
    //初始化讨论组窗口数据
  private void initGroupPopDate() {
  	for(int i=0;i<2;i++){
		GroupEntity group = null;
		if(i%3==0){
			group = new GroupEntity("sss","孙悟空", "清华大学", "天蓬元帅","aaaa","aaa","备注","足球");
		}else if(i%3==1){
			group = new GroupEntity("sss","齐天大圣","孙悟空", "厦门大学","齐天大圣","aaa","备注","足球");
			
		}else{
			group = new GroupEntity("sss","法外狂徒","嘿嘿", "蓝翔技校","法外狂徒","aaa","备注","足球");
		}
		Gdata.add(group);
		}
	for(int i=0;i<2;i++)
	{
    	List<GroupPerson> list = new ArrayList<GroupPerson>();
    	GroupPerson group1 = null;
    	GroupPerson group2 = null;
    	GroupPerson group3 = null;

    	group1 = new GroupPerson("aaa", "清华大学", "天蓬元帅");
    	

		group2 = new GroupPerson("孙悟空", "厦门大学","齐天大圣");
	
		group3= new GroupPerson("嘿嘿", "蓝翔技校","法外狂徒");
		
		   list.add(group1);
		   list.add(group2);
		   list.add(group3);
		   Mdata.add(list);
	}	
	}
/**
   * 
   * @param p	要显示的弹窗对象
   * @param whichPop	显示的哪个弹窗
   */
    private void showPop(PopupWindow p,int whichPop)
    {
    	switch (whichPop) {
    	//显示菜单弹窗
		case FinalName.POP_MENU:
    	{
    		int[] location = new int[2];
    		selectmenuImageView.getLocationOnScreen(location);
    		if(popupWindowmenu==null)
    		{
    			initPopmenu(FinalName.POP_MENU);

    		}
    		((MenuPop)popupWindowmenu).setCurrentPosition(current_position);
			popupWindowmenu.showAtLocation(mainlayout,Gravity.NO_GRAVITY, location[0], location[1]+selectmenuImageView.getHeight());

    	}
			break;
		//显示广场弹窗
		case FinalName.POP_POSITION_SQUARE:
    	{
    		int[] location = new int[2];
    		mainlayout.getLocationOnScreen(location);
    		if(squarePopupWindow==null){
    			initPopmenu(FinalName.POP_POSITION_SQUARE);
    		}
    		squarePopupWindow.showAtLocation(mainlayout,Gravity.NO_GRAVITY, location[0], location[1]);
    	}
			break;
		//显示搜索栏弹窗
		case FinalName.POP_SEARCH:
    	{
    		int[] location = new int[2];
    		selectsearchImageView.getLocationOnScreen(location);
    		if(searchPopupWindow==null)
    		{
    			initPopmenu(FinalName.POP_SEARCH);

    		}
    		searchPopupWindow.showAtLocation(mainlayout,Gravity.NO_GRAVITY, location[0], location[1]+selectsearchImageView.getHeight());
    	}
			break;
		case FinalName.POP_FRIEND:
    	{
    		int[] location = new int[2];
    		mainlayout.getLocationOnScreen(location);
    		if(friendPopupWindow==null)
    		{
    	    initPopmenu(FinalName.POP_POSITION_FRIEND);
    		}
			friendPopupWindow.showAtLocation(mainlayout,Gravity.NO_GRAVITY,location[0], location[1]);
    	}
			break;	
		case FinalName.POP_POSITION_TALKGROUP:{
    		int[] location = new int[2];
    		mainlayout.getLocationOnScreen(location);
    		if(groupPopupWindow==null)
    		{
    	    initPopmenu(FinalName.POP_POSITION_TALKGROUP);
    		}
			groupPopupWindow.showAtLocation(mainlayout,Gravity.NO_GRAVITY,location[0], location[1]);
			
		}
		break;
		default:
			break;
		}
    	

    }
    private BaseDanmakuParser createParser(final String json) throws IllegalDataException {
        
        if(json==null){
            return new BaseDanmakuParser() {
                
                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }
            
        
        AcFunDanmakuLoader loader = (AcFunDanmakuLoader) DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_ACFUN);
        
        loader.loadjson(json);


        BaseDanmakuParser parser = new AcFunDanmakuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }
    void initDanmaku(String json) throws IllegalDataException
    {
        DanmakuGlobalConfig.DEFAULT.setDanmakuStyle(DanmakuGlobalConfig.DANMAKU_STYLE_STROKEN, 3);
        if (mDanmakuView != null) {        	
        	
        	mParser = createParser(json);
            mDanmakuView.setCallback(new Callback() {

                public void updateTimer(DanmakuTimer timer) {

                }

                public void prepared() {
                    mDanmakuView.start();
                }
            });
            mDanmakuView.prepare(mParser);

          //  mDanmakuView.showFPS(true);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }
    void releaseDanmaku()
    {
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
        }
    }
    @Override
 	protected void onDestroy() {
     	
 		close_popwindow(popupWindowmenu, FinalName.POP_MENU);
 		close_popwindow(squarePopupWindow,FinalName.POP_SEARCH);
 		
 		if(current_position==PositionEnum.SQUARE)
 		close_popwindow(squarePopupWindow, FinalName.POP_POSITION_SQUARE);
 		
 		
 		for (Object object : listItem) {
			
 			HashMap<String, Object> map=(HashMap<String, Object>)object;
 				Mymedia mymedia=(Mymedia)(map.get("mymedia"));	
 				if(mymedia.getMediaPlayer()!=null)
 				{
 					mymedia.getMediaPlayer().release();
 					mymedia.setMediaPlayer(null);
 				}

		}
 		super.onDestroy();
 	}
   
    //显示提示
  	public void showToastMessage(String msg) {
  		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  	}
	public static Bitmap readBitMap(Context context, int resId){ 
		  
	BitmapFactory.Options opt = new BitmapFactory.Options(); 	 
	opt.inPreferredConfig = Bitmap.Config.RGB_565; 	 
	opt.inPurgeable = true; 
	opt.inInputShareable = true; 
	opt.inSampleSize=2;
	//获取资源图片 
	 
	InputStream is = context.getResources().openRawResource(resId); 
	return BitmapFactory.decodeStream(is,null,opt); 
	 
	} 
    private void initData() {
		
    	for(int i=0;i<3;i++){
    		WXMessage msg = null;
    		if(i%3==0){
    			msg = new WXMessage("暂无", "暂无", "暂无","11","kingsely.png");
    		}else if(i%3==1){
    			msg = new WXMessage("暂无", "暂无","暂无","11","kingsely.png");
    		}else{
    			msg = new WXMessage("暂无", "暂无","暂无","11","kingsely.png");
    		}
    		
    		data.add(msg);
    	}
	}
    /**
     * 搁到个人信息
     * @param whichuser	得到哪个用户的个人信息，-1为自己
     */
    private void getfriendlist()
    {
    	new Thread(new Runnable() {						
			public void run() {   	
				HashMap<String, String> map=new HashMap<String, String>();
				String result=null;
				WXMessage msg = null;
				String name=null,school=null,beizhu="暂无备注",id=null,userimage=null;
			   data.removeAll(data);
				map.put("userid",String.valueOf(FinalUrl.userid));
				map.put("pagenum", "1");
				map.put("pageSize","5");
				try {
					 result=HttpUtils.postRequest(FinalUrl.GetConcernListUrl,map);
					JSONArray array = new JSONObject(result).getJSONArray("concernlist");
					Log.i("tag",result);
					for(int i = 0 ; i < array.length() ; i++){
						JSONObject concernuserjsonObject = array.getJSONObject(i).getJSONObject("concernuser");
						id=array.getJSONObject(i).getString("id");
						userimage=concernuserjsonObject.getString("userimage");
						 name=concernuserjsonObject.getString("nickname");
						 school=concernuserjsonObject.getString("schoolname");
						 if(array.getJSONObject(i).getString("remarks").equals("null"))
					     beizhu="暂无备注";
						 else
						 beizhu=array.getJSONObject(i).getString("remarks");
		    			msg = new WXMessage(name,school,beizhu,id,userimage);
		    			data.add(msg);
		    			mAdapter.notifyDataSetChanged();
   
					} 
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_LOAD_FRIENDINFO, data));//通知主线程自动播放		

				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
			}).start();	
    }
    private class GetDataTask extends AsyncTask<Void, Void,  ArrayList<HashMap<String, Object>>> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        protected  ArrayList<HashMap<String, Object>> doInBackground(Void... params) {
        	ArrayList<HashMap<String, Object>> result=null;
			int where=1;
			if(current_position==PositionEnum.SQUARE)
				where=current_square_where;
			else if(current_position==PositionEnum.PERSON)
				where=current_userinfo_where;
			else if(current_position==PositionEnum.FRIENDGROUP)
				where=current_friendgroup_where;
			else if(current_position==PositionEnum.TALKGROUP)
				where=current_talkgroup_where;
        	if(isDropDown)
        	{
			
					try {
						if(isAtGroupTalk&&current_talkgroup_where==FinalName.ALL_DATE)
						{
							result=getTalkGroupDate(1);
						
						}
						
						else {
							result = getdate(1,"company",where);
						}
						
					} catch (MalformedURLException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				

        	}
        	else {							
        	int loadtotal = listItem.size();
			//当前页
			int currentpage = loadtotal%pageSize == 0 ? loadtotal/pageSize : loadtotal/pageSize+1;
			final int nextpage = currentpage + 1;//下一页
			if(nextpage <= maxpage && loadfinish){
				loadfinish = false;
				

					try {

						if(isAtGroupTalk&&current_talkgroup_where==FinalName.ALL_DATE)
						{
							result=getTalkGroupDate(nextpage);
						
						}
						
						else {
							result = getdate(nextpage,"company",where);
						}
					} catch (MalformedURLException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
						
						e.printStackTrace();
					}

				}
        	}
            return result;
        }

        @Override
        protected void onPostExecute( ArrayList<HashMap<String, Object>> result) {

        	if(current_position!=PositionEnum.PERSON)
        	{
        		//停止加载圈转动
        		isRepeat = false;
        		currentAnimation = prepareStyle1Animation();
        		currentAnimation.end();
        	}
        	
        	if (isDropDown) {
            	Log.i("DropDown","size:"+result.size());
            	
  		      if(result.size()==0)
  		      {
  		    	  handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SHOW_TOASTMESSAGE,"目前该类没有视频。。"));
  		      }
            	
            	
                listItem.clear();
                listItem.addAll(result);
                adapter.notifyDataSetInvalidated();
                loadfinish = true;
                // should call onDropDownComplete function of DropDownListView at end of drop down complete.
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
                
            } else {
            	Log.i("bottom","size:"+result.size());
            	if(result.size()!=0)
				{
					//通知更新listview
					//handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_UPDATE_LISTVIEW, result));
            		listItem.addAll(result);
            		adapter.notifyDataSetInvalidated();
            		loadfinish = true;
            		cHandler.sendMessage(cHandler.obtainMessage(FinalName.CHANDLER_CHECK_WHICH_ISPLAYING));
				}
				else {
					//通知没有数据可以加载了
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_NO_DATE));
					
				}
                listView.onBottomComplete();
            }
        	

            super.onPostExecute(result);
        }
    }		
 	    
}
