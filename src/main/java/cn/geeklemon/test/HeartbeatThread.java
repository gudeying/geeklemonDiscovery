package cn.geeklemon.test;

import java.util.concurrent.TimeUnit;

import cn.geeklemon.registerparam.PingMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class HeartbeatThread implements Runnable{

	@Override
	public void run() {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<Channel>() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						 ChannelPipeline p = ch.pipeline();
						 p.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
					}
				});
			
			Channel channel = bootstrap.connect("127.0.0.1",8898).sync().channel();

			for (;;){
                channel.writeAndFlush("");
               Thread.sleep(60000);
            }

		}catch (Exception e) {
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}


}
