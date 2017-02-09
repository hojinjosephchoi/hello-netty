package com.lattekafe.HelloNetty.TimePojo;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelInboundHandler는 다양한 이벤트 핸들러 함수를 제공하고 오버라이드 할 수 있다.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	
	/**
	 * channelActive() 메서드는 커낵션이 맺어지고 트래픽을 발생할 준비가 되었을 때 불려 질 것입니다. 
	 * 이 메서드에서 현재 시간을 응답하는 32비트 정수를 만듭니다.
	 */
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("[channelActive]");
		
		/**
		 * UnixTime POJO 데이터를 전송합니다.
		 */
		final ChannelFuture f = ctx.writeAndFlush(new UnixTime());
		f.addListener(ChannelFutureListener.CLOSE);
		
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
