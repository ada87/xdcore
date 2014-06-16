package com.xdnote.xdcore.plugin;

import java.sql.ResultSet;

public class PageResultSet {
	private int total_count;
	
	private ResultSet rs;

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	
}
