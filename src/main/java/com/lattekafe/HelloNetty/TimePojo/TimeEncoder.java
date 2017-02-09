package com.lattekafe.HelloNetty.TimePojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TimeEncoder extends ChannelOutboundHandlerAdapter {

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		
		UnixTime m = (UnixTime) msg;
		ByteBuf encoded = ctx.alloc().buffer(4);
		encoded.writeInt((int) m.value());
		
		/**
		 * 원래의 ChannelPromise를 그대로 전달하므로 
		 * Netty는 인코딩 된 데이터가 실제로 와이어(소켓)에 기록 될 때 성공 또는 실패로 표시합니다.
		 * 
		 * ctx.flush()를 호출하지 않았습니다. 
		 * flush() 오퍼레이션을 override하기 위한 void flush (ChannelHandlerContext ctx)라는 별도의 핸들러 메소드가 있습니다.
		 */
		ctx.write(encoded, promise);
	}
	
	

}
