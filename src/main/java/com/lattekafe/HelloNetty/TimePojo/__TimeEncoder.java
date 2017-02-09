package com.lattekafe.HelloNetty.TimePojo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class __TimeEncoder extends MessageToByteEncoder<UnixTime> {

	@Override
	protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf buf) throws Exception {
		System.out.println("Encoded...");
		buf.writeInt((int)msg.value());
		
		
	}

}
