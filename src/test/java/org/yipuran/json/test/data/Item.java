package org.yipuran.json.test.data;

/**
 * Item
 */
public class Item{
	private String name;
	private int length;
	public Item() {}
	public Item(String name, int len) {
		this.name = name;
		this.length = len;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public int getLength(){
		return length;
	}
	public void setLength(int length){
		this.length = length;
	}
	@Override
	public String 	toString(){
		return "( name="+name+" length="+length+")";
	}
}
