package org.yipuran.json.test.data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Foo
 */
public class Foo{
	private LocalDateTime atime;
	private Timestamp btime;
	public Foo(){}
	public LocalDateTime getAtime(){
		return atime;
	}
	public void setAtime(LocalDateTime atime){
		this.atime = atime;
	}
	public Timestamp getBtime(){
		return btime;
	}
	public void setBtime(Timestamp btime){
		this.btime = btime;
	}
}
