package com.lattekafe.HelloNetty.Time;

import io.netty.buffer.ByteBuf;
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
		
		/**
		 * 새 메세지를 보내려면, 메세지를 포함할 새로운 버퍼를 할당 할 필요가 있습니다. 
		 * 우리는 32비트 정수를 작성 할 것이므로 최소 4바이트 용량의 ByteBuf가 필요합니다. 
		 * ChannelHandlerContext.alloc() 를 통해 현재 ByteBufAllocator 를 받으시고 새로운 버퍼를 할당합니다.
		 */
		final ByteBuf time = ctx.alloc().buffer(4);
		time.writeInt((int)(System.currentTimeMillis() / 1000L + 2208988800L));
		
		/**
		 * ChannelHandlerContext.write() (와 writeAndFlush())는 ChannelFuture 를 리턴합니다.
		 * ChannelFuture 는 아직 발생하지 않은 I/O 작업을 나타냅니다. 
		 * 이 의미는 어떤 요청된 작업이 아직 수행되지 않았을 수 있는데, Netty에선 모든 작업이 비동기이기 때문입니다.
		 * 
		 * write() 메서드에 의해 리턴된 ChannelFuture 이 완료한 후 close() 메서드를 호출할 필요가 있고, 
		 * 쓰기 작업이 완료 되어졌을 때, 리스너에게 통지합니다.
		 * 
		 * close()는 또한 즉시 커낵션이 끊어지지 않습니다.
		 */
		final ChannelFuture f = ctx.writeAndFlush(time);
		
//		f.addListener(new ChannelFutureListener(){
//
//			public void operationComplete(ChannelFuture future) throws Exception {
//				assert f == future;
//				ctx.close();
//				
//			}
//			
//		});
		
		/**
		 * 사전에 정의된 리스너를 사용하여 코드를 단순화할 수 있습니다.
		 */
//		f.addListener(ChannelFutureListener.CLOSE);
		
		f.addListener((future) -> {
			assert f == future;
			ctx.close();
		});
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
