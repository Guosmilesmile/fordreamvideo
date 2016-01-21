package cn.edu.xmu.ForDream.util;

import android.R.integer;

public class FinalName {
   public final static int POP_MENU=0;							//菜单弹窗
   public final static int POP_POSITION_SQUARE=1;				//位置弹窗
   public final static int POP_POSITION_FRIEND=3;				//位置朋友圈
   public final static int POP_POSITION_TALKGROUP=4;			//位置讨论组
   
   public final static int POP_SEARCH=2;						//搜索弹窗
   public final static int POP_FRIEND=3;	                    //朋友圈弹窗
   public final static int LIKE=1;								//点赞
   public  final static int UNLIKE=2;  							//不点赞
   
   //主线程的handler请求
   public  final static int NOTICE_UPDATE_LISTVIEW=100;	  		//主线程收到消息要更新listView数据
   public  final static int NOTICE_NO_DATE=200;  		 		//主线程收到消息显示没有数据了
   public  final static int NOTICE_LOAD_DATE=300;		 		//得到通知加载哪个区的数据
   public  final static int NOTICE_LOAD_CLASSIFICATION_DATE=400;//得到通知加载哪个区的数据
   public  final static int NOTICE_STOP=1;				 		//得到通知停止播放
   public  final static int NOTICE_AUTO_PLAY=2;			 		//得到通知自动播放
   public  final static int NOTICE_PLAYNUM_ADD=3;				//播放次数+1
   public  final static int NOTICE_ISLIKE=4;			 		//点赞与否通知
   public  final static int NOTICE_ISCONCERN=5;					//关注与否通知
   public  final static int NOTICE_COMMENTJUMP=6;				//通知跳转到评论页面
   public  final static int NOTICE_DANMAKU=7;					//弹幕显示与否
   public  final static int NOTICE_GET_DANMAKU=8;				//弹幕得到通知
   public  final static int NOTICE_STOPPLAYER=9;				//点击播放时候，通知主线程停止播放上一个或者下一个视频
   public  final static int NOTICE_SHARE=10;					//通知分享
   public  final static int NOTICE_REPORT=11;					//通知举报
   public  final static int NOTICE_COLLECT=12;					//通知收藏
   public  final static int NOTICE_LOAD_USERINFO=14;			//载入个人主页信息
   public  final static int NOTICE_LOAD_FRIENDINFO=15;			//载入好友信息
   public  final static int NOTICE_LOAD_GROUPINFO=32;			//获取群组信息

   public  final static int NOTICE_ADD_SUCCESS=30;			//加好友成功
   public  final static int NOTICE_ADD_FAILURE=31;			//加好友失败
   public  final static int NOTICE_ADDGROUP_INFORMATION=33;	    //添加群组信心
   public  final static int NOTICE_SENDUSERID=34;	    //添加群组信心
   public  final static int NOTICE_SENDGROUPID=35;	    //添加群组信心

   public  final static int NOTICE_SHOW_TOASTMESSAGE=16;		//显示吐丝信息
   public  final static int NOTICE_TAG_CHANGE=18;				//通知TAG改变
   public  final static int NOTICE_GET_POPINFO=19;				//得到视频分类泡泡资料
   public  final static int NOTICE_GET_SEARCHDATE=20;			//得到搜索字段的数据
   public  final static int NOTICE_APPLY_GROUP=21;				//申请加入群组通知
   public  final static int NOTICE_JUMPTO_CREATE_GROUP=22;		//通知跳转到群创建页面
   public  final static int NOTICE_UPDATE_DIALOG_ADAPTER=23;	//通知更新讨论组消息窗的适配器
   public final static int NOTICE_ACCEPT_APPLY=24;				//同意群组申请
   public final static int NOTICE_UPDATE_GADAPTER=25;			//通知更新群主适配器
   
   public final static int  NOTICE_PAUSE_ANIMATION=1234;		//通知停止加载按钮
   
   public  final static int CHANDLER_SEARCH_PLAY_WHICH=3;		//得到主线程已经停止播放视频，寻找可以开始自动播放的视频
   public  final static int CHANDLER_CHECK_WHICH_ISPLAYING=4;	//检查正在播放的视频是哪个，暂停的视频是哪个
   
   public final static  int ALL_DATE=0;							//各处所有数据
   public  final static int SQUARE_ALL=1;						//广场高校锦集
   public  final static int SQUARE_MYCOLLEAGUE=2;				//自己学校锦集
   public  final static int SQUARE_GAME=3;						//游戏
   public  final static int SQUARE_DORM_ANECDOTE=4;				//宿舍趣
   public  final static int SQUARE_TRAVEL=5;					//旅游
   public  final static int SQUARE_STUDY=6;						//学霸
   public  final static int SQUARE_SEARCH=7;					//处于搜索状态
   
   public  final static int USERINFO_VIDEO=1;					//个人主页视频
   public  final static int USERINFO_SHARE=2;					//个人主页转发
   public  final static int USERINFO_COMMENT=3;					//个人主页评论
   public  final static int USERINFO_COLLECT=4;					//个人主页收藏

}
