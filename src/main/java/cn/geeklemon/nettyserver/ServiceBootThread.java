package cn.geeklemon.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ServiceBootThread implements Runnable {
	private int sericePort = 8899;
	public ServiceBootThread() {
	}
	public ServiceBootThread(int port){
		this.sericePort =  port;
	}
	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(new ObjectEncoder());
							p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
							p.addLast(new ServerHandler());
//						p.addLast(new ServerRegisterHandler());
						}
					});
			ChannelFuture channelFuture = serverBootstrap.bind(sericePort).sync();
			ThreadExcute.startHeartBeat();
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			ThreadExcute.shutDownAll();
		}
	}
}
