package com.lattekafe.HelloNetty.Echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandler는 다양한 이벤트 핸들러 함수를 제공하고 오버라이드 할 수 있다.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 수신된 데이터와 함께 호출. 언제든 새로운 데이터가 클라이언트로부터 수신될 때 호출된다.
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		System.out.println("[channelRead]");
		ctx.write(msg);
		ctx.flush();
	}

	/**
	 * 이벤트 처리하는 동안 발생된 예외로 인한 핸들러의 구현이나 
	 * IO에러로 인한 Netty의 예외 발생할 때 Throwable과 함께 호출
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		
		System.out.println("[exceptionCaught]");
		
		cause.printStackTrace();
		ctx.close();
	}

	
}
