package com.xdnote.xdcore.plugin;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MysqlExecuter {

	private static void setArgs(PreparedStatement ps, Object...args) throws SQLException{
		for(int i=0;i<args.length;i++){
			int index= i+1;
			Object obj = args[i];
			try {
				if(obj instanceof String){
					ps.setString(index, obj.toString());
				}else if(obj instanceof Number){
					ps.setInt(index, (Integer) obj);
				}else if(obj instanceof Date){
					ps.setDate(index, (Date)obj);
				}
			} catch (SQLException e) {
				throw e;
			}
		}
	};
	
	public static boolean execute(String sql, Object...args) throws SQLException {
		MySqlConnectPool pool =MySqlConnectPool.getInstance();
		Connection conn = pool.getConn();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			setArgs(ps,args);
			ps.execute();
		} catch (SQLException e) {
			throw e;
		}finally{
			pool.releaseConn(conn);
		}
		return true;
	}
	public static int insert(String sql, Object...args) throws SQLException{
		MySqlConnectPool pool =MySqlConnectPool.getInstance();
		Connection conn = pool.getConn();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			setArgs(ps,args);
			ps.execute();
			ps =conn.prepareStatement("SELECT LAST_INSERT_ID() AS new_id");
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				return rs.getInt("new_id");
			}
			return 0;
		} catch (SQLException e) {
			throw e;
		}finally{
			pool.releaseConn(conn);
		}
	}
	
	public static ResultSet query(String sql, Object...args) throws SQLException{
		MySqlConnectPool pool =MySqlConnectPool.getInstance();
		Connection conn = pool.getConn();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			setArgs(ps,args);
			ResultSet rs = ps.executeQuery();
			return rs;
		} catch (SQLException e) {
			throw e;
		}finally{
			pool.releaseConn(conn);
		}
	}

	/**
	 * 将一个SQL转换为可回调分页的SQL
	 * */
	private static String toPager(String sql){
		String[] ptns = sql.trim().split("\\s+");
		if(ptns.length>2&&ptns[0].equalsIgnoreCase("SELECT")){
			if(!ptns[1].equalsIgnoreCase("SQL_CALC_FOUND_ROWS")){
				sql = "SELECT SQL_CALC_FOUND_ROWS " + sql.trim().substring(6);
			}
		}
		return sql;
	}
	
	public static PageResultSet queryPage(String sql,int page_no,int page_size, Object...args) throws SQLException {	
		MySqlConnectPool pool =MySqlConnectPool.getInstance();
		Connection conn = pool.getConn();
		PreparedStatement ps;
		PageResultSet  prs = new PageResultSet();
		try {
			String limitsql = toPager(sql)+" LIMIT " + (page_no-1)*page_size + " , " + page_size;
			ps = conn.prepareStatement(limitsql);
			setArgs(ps,args);
			ResultSet rs = ps.executeQuery();
			
			prs.setRs(rs);
			ps =conn.prepareStatement("SELECT FOUND_ROWS() AS total");
			ResultSet trs = ps.executeQuery();
			if(trs.next()){
				prs.setTotal_count(trs.getInt("total"));
			}
		} catch (SQLException e) {
			throw e;
		}finally{
			pool.releaseConn(conn);
		}
		return prs;
	}
	
}
