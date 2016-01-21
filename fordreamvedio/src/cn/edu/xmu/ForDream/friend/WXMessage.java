package cn.edu.xmu.ForDream.friend;

import android.graphics.Bitmap;

public class WXMessage {
	private String userimage;
	private String title;
	private String msg;
	private String time;
	private String id;
	
	public WXMessage(){
		
	}
	public WXMessage(String title, String msg, String time, String id, String userimage) {
		this.title = title;
		this.msg = msg;
		this.time = time;
		this.id=id;
		this.userimage=userimage;
	}
	
	public String getUserimage() {
		return userimage;
	}
	public void setUserimage(String userimage) {
		this.userimage = userimage;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
