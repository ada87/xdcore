package com.xdnote.xdcore.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

public class MySqlConnectPool {

	
    //连接失效时长，避免建立过多的conn闲置，或是程序异常没有关闭，这里设置30分钟
    private final static long expriationTime = 30*60*1000;
    
    //sql驱动类，连接信息
    public final static String DRIVER="com.mysql.jdbc.Driver";
    private String dsn,dbname,username,password;
 
    //定义两个HASHTABLE，一个存放正在被使用的连接，一个存放闲置的连接，对象的KEY为连接本身，
    private Hashtable<Connection ,Long> locked=new Hashtable<Connection ,Long>(),
    		unlocked=new Hashtable<Connection ,Long>();
     
    //构造方法，初始化连接需要的帐号密码
    private MySqlConnectPool(String dsn,String dbname, String user,String pass){
        try {
            Class.forName(MySqlConnectPool.DRIVER).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.dsn=dsn;
        this.dbname = dbname;
        this.username=user;
        this.password=pass;
    }
    private static MySqlConnectPool instance = null;
    public static MySqlConnectPool getInstance(){
    	if(instance == null){
    		String dsn = PluginUtil.getConfigValue("MySqlConnectPool.dsn");
    		String dbname = PluginUtil.getConfigValue("MySqlConnectPool.dbname");
    		String username = PluginUtil.getConfigValue("MySqlConnectPool.username");
    		String password = PluginUtil.getConfigValue("MySqlConnectPool.password");
    		instance = new MySqlConnectPool(dsn, dbname, username, password);
    	}
    	return instance;
    }
     
    //生成一个连接
    private Connection createConnection() {
        try {
        	String jUrl = "jdbc:mysql://"+dsn+"/"+dbname+"?"+"user="+username+"&password="+password;
            return DriverManager.getConnection(jUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    //销毁一个连接
    private void expireConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    //验证一个连接是否有效
    private boolean validateConnection(Connection conn) {
        try {
            return conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
     
    //从连接池里面得到一个连接
    public synchronized Connection getConn(){
        long now=System.currentTimeMillis();
        Connection conn=null;
        //如果闲置区有未被使用的连接，则优先取用
        if(unlocked.size()>0){
            //遍历每个连接，如果过期了或失效了则销毁，如果没失效，则转移至使用区并返回
            Enumeration<Connection> em=unlocked.keys();
            while(em.hasMoreElements()){
                conn=em.nextElement();
                long time=unlocked.get(conn);
                if((now-time)>expriationTime){
                    unlocked.remove(conn);
                    expireConnection(conn);
                }else{
                    if(validateConnection(conn)){
                        unlocked.remove(conn);
                        locked.put(conn, now);
                        return conn;
                    }else{
                        unlocked.remove(conn);
                        expireConnection(conn);
                    }
                }
            }
        }
        //创造一个新的连接，并放入使用区，再返回
        conn=createConnection();
        locked.put(conn, now);
        return conn;
    }
 
    //释放一个连接：用完之后把连接从使用区转到闲置区，以便需要的时候再拿出来使用
    public synchronized void releaseConn(Connection conn){
        long now=System.currentTimeMillis();
        locked.remove(conn);
        unlocked.put(conn, now);
    }
     
 
}
