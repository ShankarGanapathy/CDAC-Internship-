/**
 * 
 */
package com.cdac.iot.server;

import javax.servlet.ServletContextEvent;

import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * @author Aman Kale
 * 19-Feb-2016
 * Config.java
 */

@WebListener
public class Config implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("In contextDestroyed");
		TCPServer.stopServer();
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		System.out.println("In Listener");
		TCPServer tcp=new TCPServer();
		tcp.tcpReadWrite();

	}

}
