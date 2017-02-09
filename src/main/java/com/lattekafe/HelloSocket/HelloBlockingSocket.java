package com.lattekafe.HelloSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class HelloBlockingSocket 
{
    public static void main( String[] args )
    {
        
        try{
        	// ServerSocket은 네트워크를 통해 요청이 들어올 때까지 기다린다.(wait)
        	// ServerSocket = Blocking Socket
        	ServerSocket serverSocket = new ServerSocket(8888);
        	
        	while(true){
        		System.out.println("before accept");
        		Socket socket = serverSocket.accept();
        		System.out.println("after accept");
        		try{
        			OutputStream outputStream = socket.getOutputStream();
        			outputStream.write("Hello, world! ".getBytes());
        		}catch(Exception e){
        			e.printStackTrace();
        		}finally{
        			socket.close();
        		}
        	}
        	
        }catch(IOException e){
        	e.printStackTrace();
        }
    }
}
