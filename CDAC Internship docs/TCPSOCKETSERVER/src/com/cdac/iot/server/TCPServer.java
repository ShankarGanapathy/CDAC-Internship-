package com.cdac.iot.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {

	private static ServerSocket welcomeSocket = null;
	private static boolean connection=true;	
	protected ExecutorService threadPool = Executors.newFixedThreadPool(100);//created a fixed thread pool of size 100 for handling data from different WiFi nodes
	
	public  void tcpReadWrite()
	{		       
		new Thread()
		{

			@Override
				public void run() {
					
			    	try {
						welcomeSocket = new ServerSocket(9999);
					} catch (IOException e1) {
						
						e1.printStackTrace();
					}
					System.out.println("Started");
					 Socket connectionSocket = null;
			        while (connection) {
			        	try {
			        	
			           
						
							connectionSocket = welcomeSocket.accept();
						} catch (IOException e) {
							
							e.printStackTrace();
						}
			        	
			        threadPool.execute(new ServerThread(connectionSocket));//thread pool
				}
			        threadPool.shutdown();
			}
			
		}.start();
		
	}
	public static void stopServer()
	{
		if(welcomeSocket!=null)
		{
			connection=false;
			try {
				welcomeSocket.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
}
//Class which handles packets from WiFi node

class ServerThread extends Thread
{
	Socket s=null;
	byte[] bytedata=new byte[100];
    
    
    public ServerThread(Socket s) {
		super();
		this.s = s;		
		//System.out.println("New");
	}
    
	public void run()
	{
		try
		{
			InputStream in=s.getInputStream();
				
					in.read(bytedata);		
					System.out.println("DATA--->"+bytesToHex(bytedata) +" IP "+s.getInetAddress().toString().substring(1));	
				
		  
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		 finally {
		        if (s != null) {
		            try {
		                s.close();
		            } catch (IOException e) {
		                // log error just in case
		            }
		        }
		    }
	}
		
	final protected  char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    public  String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            //System.out.println("v-->"+Integer.toHexString(v));
            hexChars[j * 2] = hexArray[v >>> 4];
            //System.out.println("hexChars1"+hexChars);
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            //System.out.println("hexChars2"+hexChars);
        }
        return new String(hexChars);
    }
   
    public void sendResponse(String ip,byte[] data)
	{
		System.out.println("DATA SEND -->"+new String(data));
		Socket s = null;
		try {
			s =  new Socket(ip, 6665);	
			OutputStream out = s.getOutputStream();
			out.write(data);
			out.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
    
}
