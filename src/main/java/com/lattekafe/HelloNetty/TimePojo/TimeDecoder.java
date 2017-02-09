package com.lattekafe.HelloNetty.TimePojo;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * ByteToMessageDecoder는 파편화 이슈를 쉽게 처리하도록 만들어 주는 ChannelInboundHandler 의 구현체 입니다.
 * (파편화 이슈 : 4개 바이트가 아닌 1~3개 바이트만 도착하고도 channelRead이벤트가 호출되는 현상)
 *
 */
public class TimeDecoder extends ByteToMessageDecoder {

	/**
	 * 새로운 데이터가 수신되면 언제든지 내부적으로 유지된 누적 버퍼와 함께 호출 합니다.
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		
		System.out.println("Decoder called..");
		
		/**
		 * 누적된 버퍼에 충분한 데이터가 없을 경우 out 에 아무것도 추가되지 않게 결정할 수 있습니다. 
		 * ByteToMessageDecoder는 더 데이터를 수신했을 때, decode()를 호출 할 것입니다.
		 */
		if(in.readableBytes() < 4){
			return;
		}
		
		/**
		 * decode()가 객체를 out에 추가하면, 디코더가 메시지를 성공적으로 디코딩했음을 의미합니다. 
		 * ByteToMessageDecoder는 누적 버퍼의 읽기 부분을 삭제합니다. 
		 * 여러 메시지를 디코딩 할 필요가 없음을 기억하십시오. 
		 * ByteToMessageDecoder는 out에 무엇인가를 추가할 때 까지 decode() 메서드를 계속 호출합니다.
		 */
		out.add(new UnixTime(in.readUnsignedInt()));

	}

}
