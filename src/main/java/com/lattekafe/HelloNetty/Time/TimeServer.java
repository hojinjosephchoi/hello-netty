package com.lattekafe.HelloNetty.Time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	
	private int port;
	
	public TimeServer(int port){
		this.port = port;
	}
	
	public void run() throws Exception{
		
		/**
		 * NioEventLoopGroup은 I/O 작업을 처리하는 멀티쓰레드의 이벤트 루프입니다. 
		 * 네티(Netty)는 다른 종류의 전송을 위해 다양한 EventLoopGroup을 제공합니다. 
		 * 우리는 이 예제에서 서버 사이드 애플리케이션을 구현 중 이며, 두 NioEventLoopGroup 이 사용 될 것 입니다. 
		 * 'boss'는 들어오는 커낵션을 받아들입니다. 
		 * 
		 * 'worker'는 'boss'가 커낵션을 승인 하자마자 승인된 커낵션의 트래픽을 처리하고, 승인된 커넥션을 'worker'에 등록합니다. 
		 * 얼마나 많은 쓰레드를 사용하고, 생성된 채널에 매핑하는 방법은 EventLoopGroup 구현에 따라 달라지며, 생성자를 통해서 구성 할 수 있습니다.
		 */
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			
			/**
			 * ServerBootstrap는 서버를 설정하는 핼퍼 클래스입니다. 
			 * 직접 채널을 사용해서 서버를 설정 할 수 있지만, 대부분의 경우 저걸 할 필요가 없습니다.
			 */
			ServerBootstrap bootstrap = new ServerBootstrap();

			bootstrap.group(bossGroup, workerGroup);
			
			/**
			 * 들어오는 연결을 허용하기 위해 새 채널을 인스턴스화하는 데 사용되는 NioServerSocketChannel 클래스를 사용하도록 지정합니다.
			 */
			bootstrap.channel(NioServerSocketChannel.class);
			
			/**
			 * 여기에 지정된 핸들러는 항상 새로 승인 된 채널에 의해 평가됩니다. 
			 * ChannelInitializer는 사용자가 새 채널을 구성하는 데 도움이 되는 Special Handler입니다. 
			 * DiscardServerHandler와 같은 핸들러를 추가하여 네트워크 응용 프로그램을 구현함으로써 
			 * 새 채널의 ChannelPipeline을 구성하려고 할 가능성이 큽니다. 
			 * 응용 프로그램이 복잡해지면 파이프 라인에 Handler를 더 추가하고 
			 * 결국 익명 클래스를 최상위 클래스로 추출합니다.
			 */
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new TimeServerHandler());
				}
				
			});
			
			/**
			 * 채널 구현과 관련된 매개 변수를 설정할 수도 있습니다. 
			 * 우리는 TCP / IP 서버를 작성하고 있으므로 tcpNoDelay 및 keepAlive와 같은 소켓 옵션을 설정할 수 있습니다. 
			 * 지원되는 ChannelOptions에 대한 개요를 보려면 ChannelOption의 API문서 및 특정 ChannelConfig 구현을 참조하십시오.
			 */
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			/**
			 * option()은 들어오는 연결을 허용하는 NioServerSocketChannel입니다. 
			 * childOption()은 이 경우 NioServerSocketChannel인 상위 ServerChannel에서 허용하는 채널입니다.
			 */
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			/**
			 * 여기서 머신의 모든 NICs(네트워크 인터페이스 카드들)에 xxxx포트에 바인드 합니다. 
			 * 다른 바인드 주소를 여러번 bind() 메서드를 호출 할 수 있습니다.
			 */
			ChannelFuture future = bootstrap.bind(port).sync();
			
			/**
			 * 서버 소켓이 닫힐 때 까지 대기합니다. (이 예제에서는 소켓이 닫힐 일이 없습니다.)
			 */
			future.channel().closeFuture().sync();
			
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			
		}
	}

	public static void main(String[] args) throws Exception{
		int port;
		if(args.length > 0){
			port = Integer.parseInt(args[0]);
		}else{
			port = 8888;
		}
		new TimeServer(port).run();

	}

}
