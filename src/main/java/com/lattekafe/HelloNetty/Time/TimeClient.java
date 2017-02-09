package com.lattekafe.HelloNetty.Time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty에서 서버와 클라이언트 사이에 가장 큰 차이점은 Bootstrap 과 Channel 구현이 사용되는 부분입니다.
 */
public class TimeClient {

	public static void main(String[] args) throws Exception {
		
//		String host = args[0];
//		int port = Integer.parseInt(args[1]);
		
		String host = "localhost";
		int port = 8888;
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			
			/**
			 * Bootstrap은 클라이언트 측 또는 비 연결 채널과 같은 non-server 채널을 위한 부분이란 점을 제외하고는 
			 * ServerBootstrap과 유사합니다.
			 */
			Bootstrap bootstrap = new Bootstrap();
			
			/**
			 * 하나의 EventLoopGroup 만 지정하면 'boss'와 'worker'로 모두 사용됩니다. 
			 * 'boss'는 Client-side에서 사용되지 않습니다.
			 */
			bootstrap.group(workerGroup);
			
			/**
			 *  NioSocketChannel은 Client-side 채널을 생성하는데 사용됩니다.
			 */
			bootstrap.channel(NioSocketChannel.class);
			
			/**
			 * Client-side의 SocketChannel에는 parent가 없기 때문에 childOption()을 사용하지 않습니다.
			 */
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.handler(new ChannelInitializer<SocketChannel>(){

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
//					ch.pipeline().addLast(new TimeClientHandler());
					
					/**
					 * ChannelPipeLine에 TimeDecorder 핸들러를 추가 삽입
					 */
					ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
				}
				
			});
			
			/**
			 * 서버에서 bind() 메소드를 호출하듯, 클라이언트에서는 서버에 connect하는 메소드를 호출합니다.
			 */
			ChannelFuture future = bootstrap.connect(host, port).sync();
			
			/**
			 * connection이 닫힐 때까지 대기합니다.
			 */
			future.channel().closeFuture().sync();
			
		} finally {
			
			workerGroup.shutdownGracefully();
			System.out.println("close!!!!");
		}

	}

}
