package com.shi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.shi.base.ResultSetHandler;
import com.shi.entity.User;
import com.util.db.JdbcTemplete;
public class UserImpl {
	private JdbcTemplete jdbcTemplete;
	public UserImpl() {
		// TODO Auto-generated constructor stub
		jdbcTemplete = new JdbcTemplete();
	}
	
	public void add(User p) {
		// TODO Auto-generated method stub
		String sql = "insert into user(name,rfidNumber,password,Tel,account,time,locate,cost)values(?,?,?,?,?,?,?,?)";
		jdbcTemplete.update(sql,p.getName(),p.getRfidNumber(),p.getPassword(),p.getTel(),p.getAccount(),p.getTime(),p.getLocate(),p.getCost());
	}

	
	public void update(User p) {
		// TODO Auto-generated method stub
		String sql = "update user set time=?,cost=?";
		jdbcTemplete.update(sql, p.getTime(),p.getCost());
	}
	
	public void updateCost(int cost,String account) {
		// TODO Auto-generated method stub
		String sql = "update user set cost=? where account=?";
		jdbcTemplete.update(sql, cost,account);
	}
	
	public void updateLocate(String account,String locate) {
		// TODO Auto-generated method stub
		String sql = "update user set locate=? where account=?";
		jdbcTemplete.update(sql,locate,account);
	}

	public void updateTime(int rfidCard,String time){
		String sql="update user set time=? where  rfidNumber=?";
		jdbcTemplete.update(sql, time,rfidCard);
	}
	public void updateTime1(String account,String time){
		String sql="update user set time=? where  account=?";
		jdbcTemplete.update(sql, time,account);
	}
	
	public User findByAccount(final String account) {
		// TODO Auto-generated method stub
		String sql = "select * from user where account=?";
		return (User)jdbcTemplete.query(sql, new ResultSetHandler() {
			
			@Override
			public Object doHandler(ResultSet rs) {
				User p = null;
				try {
					if(rs.next()){
						p = new User();
						p.setAccount(account);;
						p.setPassword(rs.getString("password"));
						p.setName(rs.getString("name"));
						p.setTel(rs.getString("Tel"));
						p.setLocate(rs.getString("locate"));
						p.setRfidNumber(rs.getInt("rfidNumber"));
						p.setTime(rs.getString("time"));
						p.setCost(rs.getInt("cost"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return p;
			}
		}, account);
	}

}
