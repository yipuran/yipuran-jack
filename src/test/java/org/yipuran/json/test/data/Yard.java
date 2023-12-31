package org.yipuran.json.test.data;

public class Yard {
	private String aId;
	private String beta;
	public Yard(){}
	public String getAId() {
		return aId;
	}
	public void setAId(String aId) {
		this.aId = aId;
	}
	public String getBeta() {
		return beta;
	}
	public void setBeta(String beta) {
		this.beta = beta;
	}
	@Override
	public String toString() {
		return "Yard [aId=" + aId + ", beta=" + beta + "]";
	}
}
