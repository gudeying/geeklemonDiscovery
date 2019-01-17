package cn.geeklemon.test;

import java.util.concurrent.TimeUnit;

import cn.geeklemon.registerparam.PingMsg;
import cn.geeklemon.registerparam.RequestServiceMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ServiceClientTest {
	public static void main(String[] args) {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						 ChannelPipeline p = ch.pipeline();
							p.addLast(new ObjectEncoder());
							p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
					}
				});
			
			Channel channel = bootstrap.connect("127.0.0.1",8899).sync().channel();
			RequestServiceMessage serviceMessage = new RequestServiceMessage();
			
			serviceMessage.setServiceName("TestService");
			channel.writeAndFlush(serviceMessage);
			Thread heartBeatThread = new Thread(new HeartbeatThread());
			heartBeatThread.setPriority(10);
			heartBeatThread.start();

		}catch (Exception e) {
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}

}
