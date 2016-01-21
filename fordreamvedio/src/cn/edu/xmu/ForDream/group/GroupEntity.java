package cn.edu.xmu.ForDream.group;

public class GroupEntity {
	private String groupimage;
	private String name;
	private String declare;
	private String id;
	private String type;
	private String createid;
	private String remarks;
	private String userid;
	public GroupEntity(String groupimage, String name, String id,String createid,String declare,String userid,String remarks,String type) {
		this.groupimage = groupimage;
		this.name = name;
		this.id = id;
		this.createid=createid;
		this.userid=userid;
		this.declare=declare;
		this.remarks=remarks;
		this.type=type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getDeclare() {
		return declare;
	}
	public void setDeclare(String declare) {
		this.declare = declare;
	}
	public String getCreateid() {
		return createid;
	}
	public void setCreateid(String createid) {
		this.createid = createid;
	}
	public String getGroupimage() {
		return groupimage;
	}
	public void setGroupimage(String groupimage) {
		this.groupimage = groupimage;
	}
	public String getNickname() {
		return name;
	}
	public void setNickname(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
