package cn.geeklemon.heartbeat;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class HeartBeatsInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		/*
		 * 客户端120s没有写数据，触发读空闲 服务器端7秒没有向客户端写数据触发写空闲 10s服务器和客户端都没有写数据，触发独写空闲
		 */
		pipeline.addLast(new ObjectEncoder());
		pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
		pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
		pipeline.addLast(new HeartBeatsHandler());
	}

}
