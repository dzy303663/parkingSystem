package com.shi.entity;

public class User {
	private int RfidNumber=0;
	private String Name=null;
	private String Password=null;
	private String Tel;
	private String account=null;
	private String time;
	private String locate=null;
	private int cost;
	
	public User()
	{
		super();
	}
	
	public User(int RfidNumber,String name,String password,String Tel,String account,String time,String locate,int cost){
		this.Tel=Tel;
		this.RfidNumber=RfidNumber;
		this.Name=name;
		this.Password=password;
		this.account=account;
		this.locate=locate;
		this.cost=cost;
	}
	
	public User(int cardid, String string) {
		
		// TODO Auto-generated constructor stub
		this.RfidNumber=cardid;
		this.Name=string;
	}

	public String getName(){
		return this.Name;
	}
	
	public void setName(String name){
		this.Name=name;
	}
	
	public String getPassword(){
		return Password;
	}
	
	public void setPassword(String password){
		this.Password=password;
	}
	
	public int getRfidNumber() {
		return RfidNumber;
	}
	public void setRfidNumber(int rfidNumber) {
		this.RfidNumber = rfidNumber;
	}
	
	public String getTel() {
		return Tel;
	}
	
	public void setTel(String tel) {
		this.Tel = tel;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String acconut) {
		this.account = acconut;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocate() {
		return locate;
	}

	public void setLocate(String locate) {
		this.locate = locate;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
}
