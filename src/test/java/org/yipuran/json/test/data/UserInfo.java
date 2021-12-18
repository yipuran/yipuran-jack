package org.yipuran.json.test.data;

import java.io.Serializable;

public class UserInfo implements Serializable{
	private String firstName;
    private String lastName;
    private String uId;
    public UserInfo(){}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUId() {
		return uId;
	}
	public void setUId(String uId) {
		this.uId = uId;
	}
	@Override
	public String toString() {
		return "UserInfo [firstName=" + firstName + ", lastName=" + lastName + ", uId=" + uId + "]";
	}
}
