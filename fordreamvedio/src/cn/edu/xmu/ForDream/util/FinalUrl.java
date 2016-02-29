package cn.edu.xmu.ForDream.util;

import android.R.string;


public class FinalUrl {

	public static String IP="10.30.16.46";
//	public static String IP="192.168.95.1";

	public static String WEB="http://"+IP+":8080/ForDream/";
	public static String VIDEO_URL = WEB+"Video/";					   				//视频url
	public static String PLAYVIDEO_URL = WEB+"Video/playvideoonweb?filename=";	
	public static String USERIMAGE_URL = WEB+"Upload/UserImage/";					//图片url
	public static String VIDEOIMAGE_URL = WEB+"Upload/Video/";						//视频图片url
	public static String GroupIMAGE_URL = WEB+"Upload/Group/";
	public static String RegUrl=WEB+"user/reg";										//注册的url
	public static String LoginUrl=WEB+"user/loginto";								//登陆的url
	public static String LogVideoUrl=WEB+"video/upload";							//上传视频的url
	public static String GetVedioUrl=WEB+"video/videolist";							//获取视频的URL
	public static String GetVedioUrlOne=WEB+"video/videolistone";					//获取企业视频的URL
	public static String GetVedioUrlTwo=WEB+"video/videolisttwo";					//获取高校视频的URL
	public static String GetGroupVedioUrl=WEB+"group/getgrouplist";					//获取讨论组分区视频的URL
	public static String GetClassficationUrl=WEB+"video/channellist";				//得到视频分类的url
	public static String PostPlayNumUrl=WEB+"video/playvideo";						//通知服务器播放次数加1url
	public static String PostLikeNumUrl=WEB+"zan/dianzan";							//通知服务器点赞次数加1url
	public static String PostGroupVideoLikeNumUrl=WEB+"group/dianzan";				//通知服务器群组视频点赞次数加1url
	public static String PostConcernNumUrl=WEB+"concern/concern";					//通知服务器关注url
	public static String LoginATUrl=WEB+"user/loginat";								//授权登陆的url
	public static String RegatnATUrl=WEB+"user/atreg";								//授权注册的url
	public static String GetCommentUrl=WEB+"video/comment";							//获取评论
	public static String GetCommentListUrl=WEB+"comment/commentlist";  				//获取视频对应评论的url
	public static String GetTalkGroupCommentUrl=WEB+"commentGroup/commentlist";     //获取讨论组视频评论的url
	public static String GetDanmakuListUrl=WEB+"comment/getcomment";   				//获取视频对应弹幕的url

	public static String GetGroupDanmakuListUrl=WEB+"commentGroup/getcomment";   	//获取讨论组视频对应弹幕的url
	public static String PostCommentUrl=WEB+"comment/videocomment";     			//发表对视频评论的url	
	public static String PostGroupCommentUrl=WEB+"commentGroup/videocomment";		//发表对讨论组视频评论的url
	public static String PostCommentLikeUrl=WEB+"commentzan/dianzan";    			//发表对视频评论的url
	public static String PostShareUrl=WEB+"video/forward";    	        			//转发的url
	public static String PostGroupShareUrl=WEB+"group/forward";    	        		//群组转发的url
	public static String PostReportUrl=WEB+"report/report";    		 				//举报的url
	public static String PostGroupReportUrl=WEB+"report/groupreport";    		 	//举报群组的url
	public static String PostCollectiontUrl=WEB+"collection/collection"; 			//收藏的url
	public static String PostUserinfoUrl=WEB+"user/userinfo"; 					 	//个人信息的url
	public static String PostPopinfoUrl=WEB+"userVideoTag/userVideoTaglist"; 		//个人的pop标签的url
	public static String PostChangePopInfoUrl=WEB+"userVideoTag/editUserVideoTag"; 	//个人的pop标签改变的url
	public static String PostSearchVideoUrl=WEB+"video/searchvideolist"; 	 		//视频搜索的url
	public static String GetConcernListUrl=WEB+"concern/getconcernlist";   			//获取haoyouliebiao的url
	public static String PostApplyGroupUrl=WEB+"group/applygroup";					//申请加入群组
	public static String GetGroupListUrl=WEB+"group/getgrouplistinfoanduserinfo";	
	public static String GetGroupVideoListUrl=WEB+"group/getvideolistbygroup";		//获得某个群组视频
	public static String PostCreateGroupUrl=WEB+"group/creategroup";			     //创建群组
	public static String DeleteGroupUrl=WEB+"group/deletegroup";                     //删除群组
	public static String ApplyGroupUrl=WEB+"group/applygroup";                  //申请群组
	public static String EditGroupUrl=WEB+"group/editgroupremarks";                  //修改群组备注
	public static String DeletetGroupUserUrl=WEB+"group/deletegroupuser"; 
	public static String DeleteFriendUrl=WEB+"concern/deleteconcern";   			  //删除好友的url
	public static String ChangeRemarksUrl=WEB+"concern/changeremarks";  			  //修改备注的url
	public static String AddFriendUrl=WEB+"concern/addconcern";   					  //增加好友的url
	
	public static String GetGroupDialogMessageDate=WEB+"group/getgroupnewslist";	  //得到群组对话框信息数据
	public static String GetGroupDialogApplyDate=WEB+"group/getapplygrouplistbyuserid";	//得到群组对话框申请信息数据
	
	public static String PostDealApplyUrl=WEB+"group/dealapplygroup";				//处理申请请求
	public static int userid=0;//用户id
	public static String username="";//用户名
	public static String usernickname="";//用户昵称
	public static String userimage="";//用户头像路径
}
