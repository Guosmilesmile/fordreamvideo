package cn.edu.xmu.ForDream.popWindows;

import info.hoang8f.widget.FButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.friend.SwipeAdapter;
import cn.edu.xmu.ForDream.friend.SwipeListView;
import cn.edu.xmu.ForDream.friend.WXMessage;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.HttpUtils;

public class FriendPop extends PopupWindow implements OnClickListener {
	private int resId,positionid,size;
	private EditText filterText;
	private Context mContext;
	private View popView;
	private LayoutInflater inflater;
	private Handler handler;
	private List<WXMessage> data = new ArrayList<WXMessage>();
    private SwipeListView mListView;
    static final int ANIMATION_DURATION = 800;
    private SwipeAdapter mAdapter; 
    String friendid,remark,addfriendid,deletefriendid,add_result;
    FButton findfriend,addfriend,friendifo,morefriend;
	public FriendPop(int resId,Context context, Handler handler, SwipeAdapter mAdapter2, List<WXMessage> data2, int i)
	{
		super(context);
		this.resId = resId;
		mContext = context;
		this.handler=handler;
		mAdapter=mAdapter2;
		data=data2;
		mAdapter.size=i;
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
		//mListView = (SwipeListView)findViewById(R.id.listview);
		findfriend=(FButton) popView.findViewById(R.id.findfriend);
		addfriend=(FButton) popView.findViewById(R.id.addfriend);
		friendifo=(FButton) popView.findViewById(R.id.friendifo);
		morefriend=(FButton) popView.findViewById(R.id.morefriend);
		mListView = (SwipeListView)popView.findViewById(R.id.listview);
		
		
	    filterText = (EditText)popView.findViewById(R.id.search_box);
		filterText.addTextChangedListener(filterTextWatcher);
		filterText.setVisibility(View.GONE);
		 
		 findfriend.setOnClickListener(this);
		 addfriend.setOnClickListener(this);
		 friendifo.setOnClickListener(this);
		 morefriend.setOnClickListener(this);
		 
        initView();

	}
public void delete(final String friendid)
{
	new Thread(new Runnable() {						
		public void run() {
		      
			HashMap<String, String> map=new HashMap<String, String>();
			String result=null;
			map.put("id",friendid);
		    try {
				result=HttpUtils.postRequest(FinalUrl.DeleteFriendUrl,map);
				JSONArray array = new JSONObject(result).getJSONArray("concernlist");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}}).start();	
	    
}
public void addfriend()
{
	new Thread(new Runnable() {						
		public void run() {
		      
			HashMap<String, String> map=new HashMap<String, String>();
			String result=null;
			map.put("username",addfriendid);
			map.put("userid",String.valueOf(FinalUrl.userid));
		    try {
				result=HttpUtils.postRequest(FinalUrl.AddFriendUrl,map);
				Log.i("tag",result);
				JSONObject object=new JSONObject(result);
				JSONObject concernJsonObject=object.getJSONObject("concern");
				JSONObject userJsonObject=concernJsonObject.getJSONObject("user");
				if(object.getString("result").equals("添加好友成功"))
				{
			        String nickname=userJsonObject.getString("nickname");
			        String id=concernJsonObject.getString("id");
			        String userimage=userJsonObject.getString("userimage");
			        String schoolname=userJsonObject.getString("schoolname");
			        String remarkss="暂无备注";
			        WXMessage newfriend=new WXMessage(nickname,schoolname,remarkss,id,userimage);
			        data.add(newfriend);

			        add_result=object.getString("result");
			        handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_ADD_SUCCESS, add_result));
				}
				else
				{
					add_result=object.getString("result");
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_ADD_FAILURE, add_result));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}}).start();	
}
public void changeremark(final String remark, String friendid2)
{
	new Thread(new Runnable() {						
		public void run() {
		      
			HashMap<String, String> map=new HashMap<String, String>();
			String result=null;
			map.put("id",friendid);
			map.put("remarks",remark);
		    try {
				result=HttpUtils.postRequest(FinalUrl.ChangeRemarksUrl,map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}}).start();	
   
}
public void senduserid(final String userid)
{
	new Thread(new Runnable() {						
		public void run() {
			handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SENDUSERID, userid));	      
		}}).start();	
}
    public void initView() {
    	setFocusable(true);
    	mAdapter.mRightWidth= mListView.getRightViewWidth();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				senduserid(data.get(position).getId());
			}
        	
        });
        mAdapter.setOnLeftItemClickListener(new SwipeAdapter.onLeftItemClickListener() {
         public void onLeftItemClick(View v, final int position) {
        	View v2;
         	int i=mListView.getFirstVisiblePosition();
         	v=mListView.getChildAt(position-i);
         	 WXMessage user=data.get(position);
        	 String username=user.getTitle();
             Toast.makeText(mContext, "您删除了 " + username, Toast.LENGTH_SHORT)
                     .show();
             v2=mListView.getChildAt(position);
             WXMessage userid=data.get(position);
             deletefriendid=userid.getId();
             delete(deletefriendid);
             AnimationListener al = new AnimationListener() {
     			public void onAnimationEnd(Animation arg0) {
     				data.remove(position); 
     				mAdapter.notifyDataSetChanged();
     			}
     			public void onAnimationRepeat(Animation animation) {}
     			public void onAnimationStart(Animation animation) {}
     		};
             removeListItem2(v,position,al); 
         }
           });
        mAdapter.setOnRightItemClickListener(new SwipeAdapter.onRightItemClickListener() {
        	
        public void onRightItemClick(View v, final int position) {
            	
            	View v2;
            	int i=mListView.getFirstVisiblePosition();
            	v=mListView.getChildAt(position-i);
                Toast.makeText(mContext, "您点击了 " + position, Toast.LENGTH_SHORT)
                        .show();
                v2=mListView.getChildAt(position);
                AnimationListener al = new AnimationListener() {
        			public void onAnimationEnd(Animation arg0) {
        				data.remove(position); 
        				mAdapter.notifyDataSetChanged();
        			}
        			public void onAnimationRepeat(Animation animation) {}
        			public void onAnimationStart(Animation animation) {}
        		};
                removeListItem2(v,position,al); 
            }
            });
               /*	View v2;
            	int i=mListView.getFirstVisiblePosition();
                Toast.makeText(mContext, "你是第  " + (position+1)+" 猴子",
                Toast.LENGTH_SHORT).show();
                v2=mListView.getChildAt(position-i+1);
                v=mListView.getChildAt(position-i);
               removeListItem(v, position);
              // removeListItem3(v2, position+1); */

        mAdapter.setOnMidItemClickListener(new SwipeAdapter.onMidItemClickListener() {
        	
            public void onMidItemClick(View v, int position) {
   
                //View v2;
            	int i=mListView.getFirstVisiblePosition();
            	v=mListView.getChildAt(position-i);
            	 WXMessage user=data.get(position);
            	String orignalremark=user.getTime();
            	dialog(orignalremark,position);
                //Toast.makeText(mContext, "你是第  " + (position+1)+" 猴子",
                //Toast.LENGTH_SHORT).show();
                //v2=mListView.getChildAt(position-i+1);
              // removeListItem(v, position);
              // removeListItem3(v2, position+1); 
            }
        });
    }
    protected void removeListItem(View rowView, final int positon) { 
        
        final Animation animation = (Animation) AnimationUtils.loadAnimation(rowView.getContext(), R.layout.item_anim); 
        animation.setAnimationListener(new AnimationListener() { 
            public void onAnimationStart(Animation animation) {} 
                    
            public void onAnimationRepeat(Animation animation) {} 
 
            public void onAnimationEnd(Animation animation) { 
                data.remove(positon); 
                mAdapter.notifyDataSetChanged(); 
                animation.cancel(); 
            } 
        }); 
         
 
        rowView.startAnimation(animation); 
    } 
    protected void removeListItem2(final View rowView, final int positon, AnimationListener al) { 
		final int initialHeight = rowView.getMeasuredHeight();

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {

				
					rowView.getLayoutParams().height =initialHeight - (int)(initialHeight * interpolatedTime);
					rowView.requestLayout();
				
				
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		if (al!=null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(ANIMATION_DURATION);
		rowView.startAnimation(anim);
        
      /*  final Animation animation = (Animation) AnimationUtils.loadAnimation(rowView.getContext(), R.layout.item_anim2); 
        animation.setAnimationListener(new AnimationListener() { 
            public void onAnimationStart(Animation animation) {} 
 
            public void onAnimationRepeat(Animation animation) {} 
 
            public void onAnimationEnd(Animation animation) { 
                mAdapter.notifyDataSetChanged(); 
                animation.cancel(); 
            } 
        }); 
         
 
        rowView.startAnimation(animation); */
 
    } 
    protected void removeListItem3(final View rowView,int positon) { 
    	/*final int initialHeight = rowView.getMeasuredHeight();

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {

				
					rowView.getLayoutParams().height =initialHeight - (int)(initialHeight * interpolatedTime);
					rowView.requestLayout();
				
				
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		if (al!=null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(ANIMATION_DURATION);
		rowView.startAnimation(anim);*/
        
       final Animation animation = (Animation) AnimationUtils.loadAnimation(rowView.getContext(), R.layout.item_anim2); 
        animation.setAnimationListener(new AnimationListener() { 
            public void onAnimationStart(Animation animation) {} 
 
            public void onAnimationRepeat(Animation animation) {} 
 
            public void onAnimationEnd(Animation animation) { 
                mAdapter.notifyDataSetChanged(); 
                animation.cancel(); 
            } 
        }); 
         
        rowView.startAnimation(animation);  
    }   
	private class popClick implements View.OnClickListener
	{

		public void onClick(View v) {

		}	
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
	switch(v.getId())
	{
	case R.id.findfriend:
		int i=filterText.getVisibility();
		if(i==View.GONE)
		filterText.setVisibility(View.VISIBLE);
		else
		filterText.setVisibility(View.GONE);
		break;
	case R.id.addfriend:
		addfrienddialog();
		break;
	case R.id.friendifo:
		break;
	case R.id.morefriend:
		break;
	
	}
	}
	protected void dialog(final String orignalremark,final int position) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(mContext); 
		  final EditText mark=new EditText(mContext);
		  mark.setText(orignalremark);
		  builder.setView(mark);
		  builder.setTitle("提示:修改备注");
		  builder.setPositiveButton("确认",  new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int whichButton) {
		    dialog.dismiss();
		    remark=mark.getText().toString();
       	    WXMessage user=data.get(position);
       	    String orignalremark=user.getTime();
         	user.setTime(remark);
         	friendid=user.getId();
         	data.set(position,user);
        	mAdapter.notifyDataSetChanged();
        	changeremark(remark,friendid);
            Toast.makeText(mContext, "修改成功",
            Toast.LENGTH_SHORT).show();
		   }
		  });

		  builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    remark=orignalremark;
		   }
		  });

		  builder.create().show();
		 }
	protected void addfrienddialog() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(mContext); 
		  final EditText mark=new EditText(mContext);
		  builder.setView(mark);
		  builder.setTitle("提示:请输入好友ID");
		  builder.setPositiveButton("确认",  new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int whichButton) {
		    dialog.dismiss();
		  addfriendid=mark.getText().toString();
		  addfriend();
		   }
		  });

		  builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
		 }
	private TextWatcher filterTextWatcher = new TextWatcher() {

	    public void afterTextChanged(Editable s) {
	    }

	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	        mAdapter.getFilter().filter(s);
	    }

	};
}
