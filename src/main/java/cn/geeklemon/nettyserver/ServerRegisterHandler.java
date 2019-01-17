package cn.geeklemon.nettyserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.geeklemon.registerparam.RegisterMessage;
import cn.geeklemon.util.PathUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerRegisterHandler extends SimpleChannelInboundHandler<RegisterMessage> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RegisterMessage msg) throws Exception {
	
		System.out.println(msg.getMsgType());
		String address = ctx.channel().remoteAddress().toString();
		switch (msg.getMsgType()) {
		case PING:
			break;
		case SERVICE:
			System.out.println(msg.getMsgType());
			break;
		case REGISTER:
			LOGGER.info("{}进行了服务注册",address);
			PathUtil.getMethodsPath(msg);
			LOGGER.info("进行注册工作");
			break;
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		LOGGER.error("{} will be closed caused by:{}",ctx.channel().remoteAddress(),
					cause.getMessage());
		ctx.channel().close();
	}
}
