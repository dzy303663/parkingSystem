package com.shi.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shi.base.ResultSetHandler;
import com.shi.entity.Garage;
import com.shi.entity.User;
import com.util.db.JdbcTemplete;
public class ZigbeeDataImpl {

	private JdbcTemplete jdbcTemplete;
	public ZigbeeDataImpl() {
		// TODO Auto-generated constructor stub
		jdbcTemplete = new JdbcTemplete();
	}
	
	public void update(Garage p) {
		// TODO Auto-generated method stub
		String sql = "update garage set id=?,status=?";
		jdbcTemplete.update(sql, p.getId(),p.getStatus());
	}
	
	public void updata1(Garage p){
		String sql="update garage set status=? where id=?";
		jdbcTemplete.update(sql, p.getStatus(),p.getId());
	}
	
	public void add(Garage p) {
		// TODO Auto-generated method stub
		String sql = "insert into garage(id,status)values(?,?)";
		jdbcTemplete.update(sql,p.getId(),p.getStatus());
	}
	
	public Garage findStatus(){
		String sql = "select status from garage";
	return (Garage)jdbcTemplete.query(sql, new ResultSetHandler() {
			
			@Override
			public Object doHandler(ResultSet rs) {
				Garage p = null;
				List list=new ArrayList();
				try {
					while(rs.next()){
						list.add(rs.getString("status"));
						
					}
					p=new Garage();
					p.setOne(list.get(0).toString());
					p.setTwo(list.get(1).toString());
					p.setThree(list.get(2).toString());
					p.setFour(list.get(3).toString());
					p.setFive(list.get(4).toString());
					p.setSix(list.get(5).toString());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return p;
			}
		},null);
	/*	return (Garage)jdbcTemplete.query(sql, new ResultSetHandler() {
			
			
			@Override
			public Object doHandler(ResultSet rs) {
				Garage p=null;
				List list=new ArrayList();
				// TODO Auto-generated method stub
				try {
					if(rs.next()){
				          list.add(rs.getString("status"));
				          
					}
					Garage p=new Garage(list.get(0).toString(),list.get(1).toString(),list.get(2).toString(),list.get(3).toString(),list.get(4).toString(),list.get(5).toString());
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return p;
			}
			
			
		}, null);*/
	
	}
}
