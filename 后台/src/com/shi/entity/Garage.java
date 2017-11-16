package com.shi.entity;

public class Garage {
	private String id;
	private String status;
    private String name;
	private String one;
	private String two;
	private String three;
	private String four;
	private String five;
	private String six;
	
	
	public Garage() {
		super();
	}
	
	public Garage(String id,String status){
		this.id=id;
		this.status=status;
	}
	public Garage(String id,String status,String name){
		this.id=id;
		this.status=status;
		this.name=name;
	}
	
	public Garage(String one,String two,String three,String four,String five,String six){
		this.one=one;
		this.two=two;
		this.three=three;
		this.four=four;
		this.five=five;
		this.six=six;
		
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOne() {
		return one;
	}

	public void setOne(String one) {
		this.one = one;
	}

	public String getTwo() {
		return two;
	}

	public void setTwo(String two) {
		this.two = two;
	}

	public String getThree() {
		return three;
	}

	public void setThree(String three) {
		this.three = three;
	}

	public String getFour() {
		return four;
	}

	public void setFour(String four) {
		this.four = four;
	}

	public String getFive() {
		return five;
	}

	public void setFive(String five) {
		this.five = five;
	}

	public String getSix() {
		return six;
	}

	public void setSix(String six) {
		this.six = six;
	}

	


}
