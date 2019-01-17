package cn.geeklemon.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HeartBeatBootThread implements Runnable {
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	@Override
	public void run() {
		
			try {
				ServerBootstrap serverBootstrap = new ServerBootstrap();
				serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
						.childHandler(new HeartBeatsInitializer());
				ChannelFuture channelFuture = serverBootstrap.bind(8898).sync();
				System.out.println("心跳线程启动");
				channelFuture.channel().closeFuture().sync();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
	}
	
	public void shutDown() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

}
