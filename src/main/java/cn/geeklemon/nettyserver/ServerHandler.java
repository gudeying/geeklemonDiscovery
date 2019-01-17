package cn.geeklemon.nettyserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.geeklemon.GeeklemonRegisterServerApplication;
import cn.geeklemon.heartbeat.HeartBeatsHandler;
import cn.geeklemon.registerWork.HandleRegister;
import cn.geeklemon.registerparam.AddressReplyMessage;
import cn.geeklemon.registerparam.AddressRequestMessage;
import cn.geeklemon.registerparam.BaseMessage;
import cn.geeklemon.registerparam.ConsumerMessage;
import cn.geeklemon.registerparam.RegisterMessage;
import cn.geeklemon.registerparam.RequestServiceMessage;
import cn.geeklemon.registerparam.ResponseConsumerMessage;
import cn.geeklemon.serviceholder.ConsumerCenter;
import cn.geeklemon.serviceholder.ServiceCenter;
import cn.geeklemon.util.ServiceUtil;
import cn.geeklemon.zookeeper.ZookeeperRegister;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<BaseMessage> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);
//	public static Map<String, Channel>ChannelPoolMap = new ConcurrentHashMap<>();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {

		String address = ctx.channel().remoteAddress().toString();
		switch (msg.getMsgType()) {
		case PING:
			break;
		case FOR_ADDRESS:
			LOGGER.info("{}请求了服务地址", address);
			if (msg instanceof AddressRequestMessage) {

				AddressRequestMessage message = (AddressRequestMessage) msg;
				String serviceName = message.getServiceName();
				AddressReplyMessage replyMessage = new AddressReplyMessage();
				try {
					replyMessage.setAddress(ServiceUtil.getAddress(serviceName));
					replyMessage.setStatus(1);
				} catch (Exception e) {
					e.printStackTrace();
					replyMessage.setStatus(0);
					replyMessage.setUid("none service");
				}
				ctx.channel().writeAndFlush(replyMessage);
			}
			break;
		case REGISTER:
			LOGGER.info("{}--尝试注册服务", address);
			if (msg instanceof RegisterMessage) {
				RegisterMessage message = (RegisterMessage) msg;
				message.setServiceAddress(ctx.channel().remoteAddress().toString());

				if (null != message.getNote() && message.getNote().equals("zookeeper")) {
					LOGGER.info("{}服务被注册到zookeeper", message.getServiceName());
					// 使用zookeeper进行注册，注册中心不维持地址和心跳服务
					ZookeeperRegister.registerWithZookeeper(address, message);
					break;
				}
				/* 注册服务 */
				HandleRegister.registerApplicationMap(message);
				HandleRegister.addServiceConnecter(address, message);

				String serviceName = message.getServiceName();
				System.out.println(
						serviceName + "新加入的服务地址：" + ServiceCenter.serviceMap.get(serviceName).getServiceAddress());
				LOGGER.info("{}--注册了->{}服务，该类可用服务个数为：{}", address, message.getServiceName(),
						ServiceUtil.getAllServe(serviceName).length);
				// 注册完毕后该channel已无用
//				ctx.channel().close();
			}

			break;
		case CONSUMER:
			LOGGER.info("有消费者注册");
			if (msg instanceof ConsumerMessage) {
				ConsumerMessage message = (ConsumerMessage) msg;
				String consumerAddress = ctx.channel().remoteAddress().toString();
				String[] services = message.getServiceName();
				ConsumerCenter.consumerRecord(consumerAddress, services);
				ResponseConsumerMessage responseMessage = new ResponseConsumerMessage();
				for (String string : services) {
					/* 遍历申请的服务名 */
					responseMessage.setServiceName(string);
					responseMessage.setAddress(ServiceUtil.getAllServe(string));
					ctx.channel().writeAndFlush(responseMessage);
					ConsumerCenter.AddCousumer(string, ctx.channel());
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		super.exceptionCaught(ctx, cause);
		String address = ctx.channel().remoteAddress().toString();
		LOGGER.error("{} will be closed caused by:{}", ctx.channel().remoteAddress(), cause.getMessage());
		ServiceCenter.removeConnecterAndServiceMap(address);
		ctx.channel().close();
	}

//	@Override
//	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//		super.channelInactive(ctx);
//		String address = ctx.channel().remoteAddress().toString();
//		String currentServiceName = GeeklemonRegisterServerApplication.serviceAddressEntities.get(address);
//		GeeklemonRegisterServerApplication.serviceAddressEntities.remove(address);
//		/*服务列表删除*/
//		GeeklemonRegisterServerApplication.serviceMap.get(currentServiceName).removeAddress(address);
//		
//		ctx.channel().close();
//	}

}
