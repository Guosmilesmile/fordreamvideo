package cn.edu.xmu.ForDream.popWindows;

import info.hoang8f.widget.FButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.edu.xmu.ForDream.R;
import cn.edu.xmu.ForDream.adapter.CheckMessageAdapter;
import cn.edu.xmu.ForDream.friend.SwipeAdapter;
import cn.edu.xmu.ForDream.friend.SwipeListView;
import cn.edu.xmu.ForDream.friend.WXMessage;
import cn.edu.xmu.ForDream.group.GroupAdapter;
import cn.edu.xmu.ForDream.group.GroupEntity;
import cn.edu.xmu.ForDream.group.GroupListView;
import cn.edu.xmu.ForDream.group.GroupPerson;
import android.R.integer;
import cn.edu.xmu.ForDream.util.FinalName;
import cn.edu.xmu.ForDream.util.FinalUrl;
import cn.edu.xmu.ForDream.util.HttpUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View; 
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GroupPop extends PopupWindow implements OnClickListener {
	private int resId;
	private Context mContext;
	private Handler handler;
	private GroupAdapter mAdapter;
	
	//群动态对话框控件和适配器
	private CheckMessageAdapter checkMessageAdapter;
	private ListView checklistView;
	private List<String> checkMessageList;
	private List<String> applyMessageIdList;
	private List<String> applyMessageGroupIdList;
	
	private List<GroupEntity> Gdata;
	private LayoutInflater inflater;
	private List<List<GroupPerson>> mData = new ArrayList<List<GroupPerson>>();
	private View popView;
	private GroupListView mListView;
	private TextView deletegroup,invitefriend;
	static final int ANIMATION_DURATION = 800;
	private Button creategroup,addgroup,searchgroup,managegroup;
	private EditText filterText;
    public GroupPop(int resId,Context mContext,Handler handler,GroupAdapter mAdapter, List<GroupEntity> Gdata,List<List<GroupPerson>> Mdata)
    {
    	super(mContext);
    	this.resId=resId;
    	this.handler=handler;
    	this.mContext=mContext;
    	this.mAdapter=mAdapter;
    	this.Gdata=Gdata;
    	this.mData=Mdata;  
    	inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	init();
    }
    public void changeremark(final String remark,final String groupid)
    {
    	new Thread(new Runnable() {						
    		public void run() {  		      
    			HashMap<String, String> map=new HashMap<String, String>();
    			String result=null;
    			map.put("groupid",groupid);
    			map.put("remarks",remark);
    			map.put("userid",String.valueOf(FinalUrl.userid));
    		    try {
    				result=HttpUtils.postRequest(FinalUrl.EditGroupUrl,map);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}}).start();	
    	
    }
    /**
     * 删除群组
     * **/
    public void deletegroup(final String groupid)
    {
    	new Thread(new Runnable() {						
    		public void run() {  		      
    			HashMap<String, String> map=new HashMap<String, String>();
    			String result=null;
    			map.put("groupid",groupid);
    			map.put("userid",String.valueOf(FinalUrl.userid));
    		    try {
    				result=HttpUtils.postRequest(FinalUrl.DeleteGroupUrl,map);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}}).start();	
    
    }
    /**
     * 删除群成员
     * **/
    public void deletepeople(final String peopleid,final String groupid)
    {
      
    	new Thread(new Runnable() {						
    		public void run() {  		      
    			HashMap<String, String> map=new HashMap<String, String>();
    			String result=null;
    			map.put("userid",peopleid);
    			map.put("groupid",groupid);
    		    try {
    				result=HttpUtils.postRequest(FinalUrl.DeletetGroupUserUrl,map);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}}).start();	
    }
    /**
     * 添加群组
     * **/    
    public void addgroup(final String groupid)
    {
    	new Thread(new Runnable() {						
    		public void run() {
    		      
    			HashMap<String, String> map=new HashMap<String, String>();
    			String result=null;
    			map.put("groupid",groupid);
    			map.put("userid",String.valueOf(FinalUrl.userid));
    		    try {
    				result=HttpUtils.postRequest(FinalUrl.ApplyGroupUrl,map);
    				Log.i("tag",result);
    				JSONObject object=new JSONObject(result);
    			    String add_result=object.getString("result");
    			    Log.i("tag",add_result);
    			    handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_ADD_FAILURE, add_result));

    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

    		}}).start();	
    	
    }
    public void sendgroupid(final String groupid)
    {
    	new Thread(new Runnable() {						
    		public void run() {
   		      
    	 handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_SENDGROUPID, groupid));
    		}}).start();	
    	
    }
    private void init()
    {
    	popView = inflater.inflate(this.resId, null);
		popView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(popView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		//setFocusable(true);
		setOutsideTouchable(true);
		//mListView = (SwipeListView)findViewById(R.id.listview);
		
		mListView = (GroupListView)popView.findViewById(R.id.listview);
		deletegroup=(TextView) popView.findViewById(R.id.item_left_txt);
		invitefriend=(TextView) popView.findViewById(R.id.item_mid_txt);
		
		creategroup=(Button) popView.findViewById(R.id.creategroup);
		addgroup=(Button) popView.findViewById(R.id.joingroup);
		searchgroup=(Button) popView.findViewById(R.id.searchgroup);
		managegroup=(Button) popView.findViewById(R.id.managegroup);
	    filterText = (EditText)popView.findViewById(R.id.search_group);
	    
	    
		filterText.addTextChangedListener(filterTextWatcher);
		filterText.setVisibility(View.GONE);
		
		creategroup.setOnClickListener(this);
		addgroup.setOnClickListener(this);
		searchgroup.setOnClickListener(this);
		managegroup.setOnClickListener(this);
		
		initView();
		
    }
    

    //初始化群组动态数据
    public void initCheckMessage(CheckMessageAdapter checkMessageAdapter,List<String> list)
    {
       	this.checkMessageAdapter=checkMessageAdapter;
    	this.checkMessageList=list;
    	applyMessageIdList=new ArrayList<String>();
    	applyMessageGroupIdList=new ArrayList<String>();
    	 
    	checklistView=null;
    }
  
    public void initView()
    {
    	setFocusable(true);
    	mAdapter.mRightWidth= mListView.getRightViewWidth();
        mListView.setAdapter(mAdapter);

       mAdapter.setOnPictureClickListener(new GroupAdapter.onPictureClickListener() {        	
		public void onPictureClick(int position) {
			// TODO Auto-generated method stub
			Toast.makeText(mContext, "您职责 ", Toast.LENGTH_SHORT)
	        .show();
			    sendgroupid(Gdata.get(position).getId());
		}
        });
        mAdapter.setOnRightItemClickListener(new GroupAdapter.onRightItemClickListener() {        	
        public void onRightItemClick(View v, int position) {
	         Toast.makeText(mContext, "管理", Toast.LENGTH_SHORT)
             .show();   

            }
        });
       mAdapter.setOnCLeftItemClickListener(new GroupAdapter.onCLeftItemClickListener() {
	   public void onCLeftItemClick(View v,final int groupPosition, final int childPosition) {
		View v2;
		
		int i=mListView.getFirstVisiblePosition();
		v=mListView.getChildAt(groupPosition-i+childPosition+1);
		String username=mData.get(groupPosition).get(childPosition).getNickname();
		String peopleid=mData.get(groupPosition).get(childPosition).getId();
		String groupid=Gdata.get(groupPosition).getId();
        Toast.makeText(mContext, "您删除了 " + username, Toast.LENGTH_SHORT)
        .show();
        deletepeople(peopleid, groupid);
        AnimationListener al = new AnimationListener() {
 			public void onAnimationEnd(Animation arg0) {
 				mData.get(groupPosition).remove(childPosition); 
 				mAdapter.notifyDataSetChanged();
 			}
 			public void onAnimationRepeat(Animation animation) {}
 			public void onAnimationStart(Animation animation) {}
 		};
         removeListItem2(v,al); 
     
	   }      
       });
       mAdapter.setOnLeftItemClickListener(new GroupAdapter.onLeftItemClickListener() {
	   public void onLeftItemClick(View v, final int position) {

       	View v2;
     	int i=mListView.getFirstVisiblePosition();
     	v=mListView.getChildAt(position-i);
     	GroupEntity group=Gdata.get(position);
    	String groupname=group.getNickname();
         Toast.makeText(mContext, "您删除了 " + groupname, Toast.LENGTH_SHORT)
                 .show();
        String groupid=group.getId();
        deletegroup(groupid);
        AnimationListener al = new AnimationListener() {
 			public void onAnimationEnd(Animation arg0) {
 				Gdata.remove(position); 
 				mData.remove(position); 
 				mAdapter.notifyDataSetChanged();
 			}
 			public void onAnimationRepeat(Animation animation) {}
 			public void onAnimationStart(Animation animation) {}
 		};
         removeListItem2(v,al); 
     }
            
       });
       /**
        * 邀请事件
        */
       mAdapter.setOnRightItemClickListener(new GroupAdapter.onRightItemClickListener() {
	   public void onRightItemClick(View v, int position) {

	         Toast.makeText(mContext, "邀请", Toast.LENGTH_SHORT)
             .show();
            }
       });
       /**
        * 修改备注事件
        */
       mAdapter.setOnMidItemClickListener(new GroupAdapter.onMidItemClickListener() {
	   public void onMidItemClick(View v, int position) {

   	GroupEntity group=Gdata.get(position);
   	String orignalremark=group.getRemarks();
   	declaredialog(orignalremark,position);
            }
       });         
    }
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.creategroup:
		{
			handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_JUMPTO_CREATE_GROUP));
		}
			break;
		case R.id.joingroup:
			addgroupdialog();
			break;
		case R.id.searchgroup:
		int i=filterText.getVisibility();
		if(i==View.GONE)
		filterText.setVisibility(View.VISIBLE);
		else
		filterText.setVisibility(View.GONE);
			break;
		case R.id.managegroup:
		{
			getMessageDate();
			showMessageDialog();
		}
			break;
		default:
			break;
		
		}
		
	}
	protected void declaredialog(final String orignalremark,final int position) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(mContext); 
		  final EditText mark=new EditText(mContext);
		  mark.setText(orignalremark);
		  builder.setView(mark);
		  builder.setTitle("提示:修改备注");
		  builder.setPositiveButton("确认",  new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int whichButton) {
		    dialog.dismiss();
		    String remark=mark.getText().toString();
      	    GroupEntity groups=Gdata.get(position);
        	groups.setRemarks(remark);
        	String groupid=groups.getId();
           Gdata.set(position,groups);
           mAdapter.notifyDataSetChanged();
       	   changeremark(remark,groupid);
           Toast.makeText(mContext, "修改成功",
           Toast.LENGTH_SHORT).show();
		   }
		  });

		  builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
		 }
	protected void addgroupdialog() {
		 AlertDialog.Builder builder = new AlertDialog.Builder(mContext); 
		  final EditText group=new EditText(mContext);
		  builder.setView(group);
		  builder.setTitle("提示:请输入群组ID");
		  builder.setPositiveButton("确认",  new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int whichButton) {
		  dialog.dismiss();
		  String groupid=group.getText().toString();
		  addgroup(groupid);
		  Toast.makeText(mContext, "请求已发送",
		           Toast.LENGTH_SHORT).show();
		   }
		  });

		  builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
		 }
    protected void removeListItem2(final View rowView, AnimationListener al) { 
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
    }
    private void showMessageDialog()
    {
    	final AlertDialog dialog=new AlertDialog.Builder(mContext).create();    	
    	dialog.show();  	
    	Window window=dialog.getWindow();
    	window.setContentView(R.layout.groupmessage_dialog);
    	WindowManager.LayoutParams wl = window.getAttributes();
    	wl.gravity = android.view.Gravity.LEFT | android.view.Gravity.TOP;
    	wl.x=100;
    	wl.y=100;
    	window.setAttributes(wl);
    	

            checklistView=(ListView)window.findViewById(R.id.groupmessage_dialog_listview);
            checklistView.setAdapter(checkMessageAdapter);
    	Log.i("checklistView", checklistView.toString());

    	Log.i("checkMessageAdapter", checkMessageAdapter.toString());
    	
    	checkMessageAdapter.setAtMessage(true);
    	
    	checkMessageAdapter.notifyDataSetChanged();
    	FButton checkButton=(FButton)window.findViewById(R.id.groupmessage_dialog_check);
    	FButton messageButton=(FButton)window.findViewById(R.id.groupmessage_dialog_message);
    	FButton closeButton=(FButton)window.findViewById(R.id.groupmessage_dialog_close);
    	checkButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Log.i("选择审核申请","选择审核申请");
				loadApplyMessage();
			}
		});
    	messageButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Log.i("选择群组动态","选择群组动态");
				loadMessage();
			}
		});
    	closeButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				checklistView.setAdapter(null);
				dialog.dismiss();
				
			}
		});
    	
    }
    //得到群组动态的数据
    private void getMessageDate()
    {
		new Thread(new Runnable() {						
			public void run() {
				
				
				
				try {
					HashMap<String, String> map=new HashMap<String, String>();
					String result=null;
					map.put("userid", String.valueOf(FinalUrl.userid));
					result=HttpUtils.postRequest(FinalUrl.GetGroupDialogMessageDate, map);
					JSONArray array=new JSONObject(result).getJSONArray("groupnewslist");
					for(int i=0;i<array.length();i++)
					{
						JSONObject object=array.getJSONObject(i);
						checkMessageList.add(object.getString("action"));
					}
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_UPDATE_DIALOG_ADAPTER));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
    //得到群组申请的数据
    private void getApplyDate()
    {
		new Thread(new Runnable() {						
			public void run() {
				
				
				
				try {
					HashMap<String, String> map=new HashMap<String, String>();
					String result=null;
					map.put("userid", String.valueOf(FinalUrl.userid));
					result=HttpUtils.postRequest(FinalUrl.GetGroupDialogApplyDate, map);
					JSONArray array=new JSONObject(result).getJSONArray("applygrouplist");
					for(int i=0;i<array.length();i++)
					{
						JSONObject object=array.getJSONObject(i);
						JSONObject groupoJsonObject=object.getJSONObject("group");
						JSONObject userJsonObject=object.getJSONObject("user");
						String string=userJsonObject.getString("nickname")+"请求加入"+groupoJsonObject.getString("name");
						checkMessageList.add(string);
						applyMessageIdList.add(object.getString("id"));
						applyMessageGroupIdList.add(groupoJsonObject.getString("id"));
						
					}
					handler.sendMessage(handler.obtainMessage(FinalName.NOTICE_UPDATE_DIALOG_ADAPTER));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
    }
    //得到申请组的id
    public String getApplyGroupId(int which)
    {
    	return applyMessageIdList.get(which);
    }
    //得到申请组的id
    public String getGroupId(int which)
    {
    	return applyMessageGroupIdList.get(which);
    }
    //加载群组动态星系
    private void loadMessage()
    {
    	if(!checkMessageAdapter.isAtMessage())
    	{
    		checkMessageAdapter.setAtMessage(true);
    		checkMessageList.clear();
    		getMessageDate();
    	}
    }
    //加载群组申请信息
    private void loadApplyMessage()
    {
    	if(checkMessageAdapter.isAtMessage())
    	{
    		checkMessageAdapter.setAtMessage(false);
    		checkMessageList.clear();
    		getApplyDate();
    	}
    }
    
    //更新消息窗口视图
    public void updateMessageDialog()
    {
    	checkMessageAdapter.notifyDataSetChanged();
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
