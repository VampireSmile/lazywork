package com.faceRecognition.modelHelper;

public class LoginInfo {
	private int state;
    private String studentid;
    private String role;
    private String faceInfo;
    private String shouldSigned;
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStudentid() {
		return studentid;
	}
	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getFaceInfo() {
		return faceInfo;
	}
	public void setFaceInfo(String faceInfo) {
		this.faceInfo = faceInfo;
	}
	public String getShouldSigned() {
		return shouldSigned;
	}
	public void setShouldSigned(String shouldSigned) {
		this.shouldSigned = shouldSigned;
	}
}
