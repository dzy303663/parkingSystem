package com.shi.servlet;
import java.io.BufferedInputStream;  
import java.io.BufferedOutputStream;  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.Socket;  
import java.net.UnknownHostException;  

import com.shi.dao.ZigbeeDataImpl;
import com.shi.entity.Garage;
  
public class Client {  
	public String hardvare[];
  
    public void setHardvare(String[] hardvare) {
		this.hardvare = hardvare;
	}

	public String[] getHardvare() {
		return hardvare;
	}

	//��ַʹ�õ�ǰ��������  
    private static String defaultHost = "192.168.123.224";  
    //�������˿�Ĭ��ʹ��12345  
    private static int defaultPort = 8000;   
    //echo���ԵĽ�����־  
    private static final String END_FLAG = "over";  
    //�������ӷ������Ŀͻ���Socket  
    private static Socket connection;  
      
    public Client() throws UnknownHostException, IOException {  
  
    }  
      
    public  void init() throws UnknownHostException, IOException{  
        // TODO Auto-generated method stub  
    	 connection = new Socket(defaultHost, defaultPort);  
        try {  
            /** 
             *  EchoClient�ͻ��˿��������̷ֱ߳���ɲ�ͬ�Ĺ����� 
             *  ��ȡ�߳�ReadThread���ڴӱ�׼���뵱�ж�ȡ�ַ��������Ұ��ַ���д���������� 
             *  д���߳�WriteThread���ڴӷ������˽���Echo�ַ��� 
             * */  
        	System.out.println("main");
            Client client = new Client();  
            ReadThread readThread = client.new ReadThread();  
            WriteThread writThread = client.new WriteThread();  
            //�����߳�  
            writThread.start();  
            readThread.start();  
            //�ȴ��߳̽���  
            writThread.join();  
            readThread.join();  
              
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
    }  
  
    //��ȡ�߳�ReadThread���ڴӱ�׼���뵱�ж�ȡ�ַ��������Ұ��ַ���д����������  
    private class ReadThread extends Thread {  
          
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
            BufferedInputStream bis = null;  
            byte[] buf = new byte[1024];  
            int readByte = -1;  
            String content = null;  
            try {  
                //ͨ���ͻ���Socket��ȡ�ͷ����ͨ�ŵ�InputStream  
                bis = new BufferedInputStream(connection.getInputStream());  
                //��ʼѭ������������д����Ҫ�����Echo����  
                while (true) {  
                    /** 
                     * �ӷ������Ķ�ȡ���ж�ȡ���ݣ� 
                     * ���������û�з���������д�����ݣ���ͻ����޷���ȡ���ݣ���һֱ�����ȴ� 
                     */  
                    readByte = bis.read(buf);  
                    content = new String (buf,0,readByte);
                   // System.out.println("<=== Server Echo : "+content);  
                    Client client=new Client();
                    
                    String[] getDataArry=content.split("/");
                    client.setHardvare(getDataArry);
                    Garage garage=new Garage("","��λ");
                    ZigbeeDataImpl zigbeeData=new ZigbeeDataImpl();
                   
                    
					/*for(int i=0;i<2;i++)
		             {
		                    System.out.println(getDataArry[i]);
		                    System.out.println(getDataArry[1]+"bbbb");
		                 
		             }*/
					String[] getDataArry1=getDataArry[1].split(":");
					garage.setId(getDataArry1[0]);
					String status=null;
					//����ֵ
					if(Integer.parseInt(getDataArry1[1])>400 ){
						 status="��λ";
						 
						
					}else{
						status="��ͣ";
					};
					garage.setStatus(status);
					zigbeeData.updata1(garage);
					for(int i=0;i<2;i++)
		             {
		                    System.out.println(getDataArry1[i]+"aaaa");
		                    System.out.println(getDataArry1[0]+"bbbb");
		                    System.out.println(getDataArry1[1]+"cccc");
		                    
		             }
                    /** 
                     * �ͻ�������������ص��ַ���Ϊover�ͽ����ͻ��� 
                     */  
                    /*if (content.equalsIgnoreCase(END_FLAG)) {  
                        System.out.println(">>> Echo End ! <<<");  
                        break;  
                    }  */
                }  
            } catch (IOException e) {  
                /** 
                 * �����������ܵ�������־��ʱ��END_FLAG��ʶ�ͻ�����Ҫ�رշ��񣬷���˵�д��������رգ� 
                 * ������whileѭ���пͻ��˵Ķ�ȡ�����ڶ�ȡ����ʱ�ͻ��׳��쳣����ʱ˵��������� 
                 */  
                //System.out.println("Server Write Thread is closed, the client Read Thread closed too");  
            } finally {  
                if (bis != null) {  
                    try {  
                        bis.close();  
                    } catch (IOException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
    }  
      
    //д���߳�WriteThread���ڴӷ������˽���Echo�ַ���  
    private class WriteThread extends Thread {  
          
        @Override  
        public void run() {  
            // TODO Auto-generated method stub  
              
            BufferedReader br = null;  
            BufferedOutputStream bos = null;  
            String content = null;  
            byte[] buf = null;  
            try {  
                br = new BufferedReader(new InputStreamReader(System.in));  
                bos = new BufferedOutputStream(connection.getOutputStream());  
                //�ӱ�׼�����л�ȡ�ַ�������д��������������  
                while (true) {  
                    System.out.println("===> Please input :");  
                    //�ӱ�׼�����ж�ȡ��������  
                    content ="start";  
                    buf = content.getBytes();  
                    //����������ȡ����д������  
                    bos.write(buf, 0, buf.length);  
                    bos.flush();  
                    //�ж��Ƿ�Ϊ������ʶ��������д���߳�  
                    if (content.equalsIgnoreCase(END_FLAG)) {  
                        //System.out.println("---> Client echo end!");  
                        break;  
                    }  
                }  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } finally {  
                if (br != null) {  
                    try {  
                        br.close();  
                    } catch (IOException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                }  
                if (bos != null) {  
                    try {  
                        bos.close();  
                    } catch (IOException e) {  
                        // TODO Auto-generated catch block  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
    }  
    /* public static String stringToAscii(String data)
     {
    	 StringBuffer sbu=new StringBuffer();
    	 char[] chars=data.toCharArray();
    	 for(int i=0;i<chars.length;i++)
    	 {
    		 if(i !=chars.length-1)
    		 {
    			 sbu.append((int)chars[i]).append(",");
    		 }
    		 else {
				sbu.append((int)chars[i]);
			}
    	 }
    	 return sbu.toString();
     }*/
    
    
    
}  