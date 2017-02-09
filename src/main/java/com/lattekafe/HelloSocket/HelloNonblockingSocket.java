package com.lattekafe.HelloSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class HelloNonblockingSocket {

	public static void main(String[] args) {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(8888));
			// nio에서는 non-blocking도 지원해준다...
			serverSocketChannel.configureBlocking(false);
			
			while(true){
				System.out.println("before accept");
				SocketChannel socketChannel = serverSocketChannel.accept();
				System.out.println("after accept");
				
				if(socketChannel != null){
					ByteBuffer byteBuffer = ByteBuffer.wrap("Hello, world!".getBytes());
					socketChannel.write(byteBuffer);
					socketChannel.close();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
