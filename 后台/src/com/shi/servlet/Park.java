package com.shi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;












import com.shi.dao.UserImpl;
import com.shi.dao.ZigbeeDataImpl;
import com.shi.entity.Garage;
import com.shi.entity.User;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class Park extends HttpServlet{
	public Park() throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		System.out.println("服务器启动成功");
		
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		//允许跨域访问
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setContentType("application/json;charser=utf-8"); 
		String method=req.getParameter("method");
		/**
		 * 按账户查找，并转换成JSON传给前端
		 */
		if(method.equals("find")){
			String account=req.getParameter("account");
			char temp[]=account.toCharArray();
			char str2;
			String result="";
			for(int i=0;i<temp.length;i++){
					str2 = (char)(temp[i]-10);
					result = result + str2;
				System.out.println(str2);
			}
			
			System.out.println(result);
			UserImpl impl=new UserImpl();
			User p=null;
			p=impl.findByAccount(result);
			PrintWriter out1 = resp.getWriter(); 
			out1.printf(objectToJson(p));
			out1.close();
		}
		
		//hardvare();
		if(method.equals("rfidData")){
			String id=req.getParameter("id");
			String name=req.getParameter("name");
		}
		
		/*ZigbeeDataImpl impl1=new ZigbeeDataImpl();
		if(method.equals("garageData")){
			Garage q=null;
			q=impl1.findStatus();
			System.out.println(q.getOne()+q.getFive());
		}*/
		/**
		 * 刷卡时间
		 */
		if(method.equals("rfidTime")){
			String id=req.getParameter("card");
			id=id.substring(0, 2);
			int id1=Integer.parseInt(id);
			User p1=null;
			UserImpl impl1=new UserImpl();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");  
			String date1=sdf.format(new Date());
			System.out.println(id);
			impl1.updateTime(id1,date1);
		}
		
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("dopost");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String method=request.getParameter("method");
		
		response.setHeader("Access-Control-Allow-Origin","*");
		//response.setContentType("application/json;charser=utf-8"); 
		String account=request.getParameter("account");
		String password=request.getParameter("password0");
		String tel=request.getParameter("tel");
		String cardid=request.getParameter("cardid");
		String locate=request.getParameter("locate");
		System.out.println(account);
	/*	PrintWriter out = response.getWriter();  
		String data="{\"username\": \"your name\", \"user_json\": {\"username\": \"your name\", \"nickname\": \"your nickname\"}}";
		
		out.append(data);
	
		out.close();*/
		//jsonchangeToObject(data);
		/**
		 * 添加用户
		 */
		if(method.equals("add")){
		User p=new User(Integer.parseInt(cardid),account,password,tel,account,null,null,0);
		UserImpl impl=new UserImpl();
		impl.add(p);
		}
		/**
		 * 根据account查找信息
		 */
		if(method.equals("find")){
		UserImpl impl=new UserImpl();
		User p=null;
		p=impl.findByAccount(account);
		PrintWriter out1 = response.getWriter(); 
		System.out.println(p.getAccount());
		out1.printf(objectToJson(p));
		out1.close();}
		
		/**
		 * 传输APP停车场数据
		 */
		ZigbeeDataImpl impl1=new ZigbeeDataImpl();
		if(method.equals("garageData")){
			Garage q=null;
			q=impl1.findStatus();
			PrintWriter out1 = response.getWriter(); 
			out1.printf(objectToJson(q));
			out1.close();
			System.out.println(q.getOne()+q.getFive());
		}
		/**
		 * 更新停车位置
		 */
		if(method.equals("updateLocate")){
			
			User p=null;
			UserImpl impl=new UserImpl();
			p=impl.findByAccount(account);
			impl.updateLocate(account,locate.toString());
			
			}
		
		/**
		 * 刷卡时间
		 */
		if(method.equals("rfidTime")){
			String id=request.getParameter("cardId");
			User p=null;
			UserImpl impl=new UserImpl();
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");  
			String date1=sdf.format(new Date());
			System.out.println(id);
			impl.updateTime(1407094224,date1);
		}
		/**
		 * 入库时间
		 */
		if(method.equals("balance")){
			User p=null;
			String in_time=request.getParameter("in_time");
			UserImpl impl=new UserImpl();
			p=impl.findByAccount(account);
			if(p.getRfidNumber()==0){
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");  
			Date date0 = null;
			try {
				date0 = sdf.parse(in_time);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Date date1=sdf.parse(sdf.format(new Date()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			impl.updateTime1(account,sdf.format(date0));}
		}
		/**
		 * 出库结算
		 */
		if(method.equals("car_out")){
			account=request.getParameter("account");
			User p=null;
			String in_time=request.getParameter("in_time");
			UserImpl impl=new UserImpl();
			p=impl.findByAccount(account);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");  
			try {
				Date date1=sdf.parse(sdf.format(new Date()));
				Date date0=sdf.parse(p.getTime());
				long l=date1.getTime()-date0.getTime();   
				long day=l/(24*60*60*1000);   
				long hour=(l/(60*60*1000)-day*24);   
				long min=((l/(60*1000))-day*24*60-hour*60);
				int cost=0;
				if(min>30){ cost=(int) (day*24+hour+1)*5;}
				else{cost=(int) (day*24+hour)*5;}
				impl.updateCost(cost, account);
				System.out.println(""+day+"天"+hour+"小时"+min+"分"+cost);  
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrintWriter out1 = response.getWriter(); 
			out1.printf(objectToJson(p));
			out1.close();
			
		}
	
	}
	
	public void hardvare() throws UnknownHostException, IOException{
		hardvare hd=new hardvare();
		hd.start();

		System.out.println("lalalalalalaal");
	}
	
	/**
	 * JSON对象转换成JAVA对象
	 * @param data
	 */
	public void jsonchangeToObject(String data){
			//	JSONArray array=new JSONArray(data);
				JSONArray arry=new JSONArray();
				arry.add(data);
				JSONObject resultobj=arry.optJSONObject(0);
				
			
	}
	
	/**
	 * JAVA对象转换成JOSN
	 * @param p
	 * @return
	 */
	public String objectToJson(Object p){
		JSONObject json=JSONObject.fromObject(p);
		String data1=json.toString();
		System.out.println(data1);
		return data1;
	}
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

	public class hardvare extends Thread{
	
		public hardvare() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {
				Client client;
				client = new Client();
				client.init();
				System.out.println(client.getHardvare()+"aaaaa");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
