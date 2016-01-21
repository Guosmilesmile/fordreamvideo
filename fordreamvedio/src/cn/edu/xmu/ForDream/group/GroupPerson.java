package cn.edu.xmu.ForDream.group;

public class GroupPerson {
private String userimage;
private String name;
private String id;
public GroupPerson(String userimage, String name, String id) {
	
	this.userimage = userimage;
	this.name = name;
	this.id = id;
}
public String getUserimage() {
	return userimage;
}
public void setUserimage(String userimage) {
	this.userimage = userimage;
}
public String getNickname() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}

}
