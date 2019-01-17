package cn.geeklemon.test;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;

import cn.geeklemon.registerparam.BaseMessage;
import cn.geeklemon.registerparam.MethodsEntity;
import cn.geeklemon.registerparam.MsgType;
import cn.geeklemon.registerparam.RegisterMessage;
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

public class TestRegister {
	public static void main( String[] args) {
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
			
			BaseMessage bMessage =new BaseMessage();
			bMessage.setMsgType(MsgType.REGISTER);
			
			
			RegisterMessage message =  getRegMessage();
			RequestServiceMessage serviceMessage = new RequestServiceMessage();
			serviceMessage.setServiceName("testService");
			serviceMessage.setParams("param1-param2");

			channel.writeAndFlush(serviceMessage);
			Thread heartBeatThread = new Thread(new HeartbeatThread());
			heartBeatThread.setPriority(9);
			heartBeatThread.start();

		}catch (Exception e) {
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
	
	public static RegisterMessage getRegMessage() {
		RegisterMessage registerMessage = new RegisterMessage();
		String methodPath = "/method1-/method2-/method3";
		registerMessage.setMethodPath(methodPath );
		registerMessage.setServiceName("testService");
		return registerMessage;
	}
}
