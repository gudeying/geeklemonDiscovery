package cn.geeklemon.heartbeat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.geeklemon.GeeklemonRegisterServerApplication;
import cn.geeklemon.nettyserver.ServerHandler;
import cn.geeklemon.serviceholder.ServiceCenter;
import cn.geeklemon.util.PathUtil;
import cn.geeklemon.work.NotifyConsumer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatsHandler extends ChannelInboundHandlerAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);
	/**
	 * 	记录 address 未收到心跳包的次数
	 */
	private static final Map<String, Integer> unPingMap = new ConcurrentHashMap<>();
	/**
	 * 	记录心跳地址（/ip:port）对应的服务信息（serviceName,address）
	 */
	public static final Map<String, HeartBeatWithServiceEntity> addrServiceMap = new ConcurrentHashMap<>();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		String address = ctx.channel().remoteAddress().toString();
		unPingMap.remove(address);
		HeartBeatWithServiceEntity entity = addrServiceMap.get(address);
		String serviceName = entity.getServiceName();
		String serviceAddress = entity.getHeartBeatOfServiceAddress();
		//服务列表清除
//		ServiceCenter.serviceMap.get(serviceName).removeAddress(serviceAddress);
		//连接者删除
		ServiceCenter.removeWithHeartBeat(serviceName, serviceAddress);
		//通知消费者
		NotifyConsumer.notifyAll(serviceName, serviceAddress);
		LOGGER.error("{} channel closed caused by: {}",address,cause.getMessage());
		ctx.channel().close();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				String address = ctx.channel().remoteAddress().toString();
//				LOGGER.info("{}触发读空闲事件-{}",address,unPingMap.get(address));
				if (unPingMap.get(address).intValue() >= 6) {
					LOGGER.info("{}连续6次未收到心跳消息，将移除该节点",address);
					/*zookeeper以及相关服务的移除*/
					unPingMap.remove(address);
					String currServiceName = addrServiceMap.get(address).getServiceName();
					addrServiceMap.remove(address);
					String currrentServiceAddress = addrServiceMap.get(address).getHeartBeatOfServiceAddress();
					String currentIp = address.split(":")[0].split("/")[1];
					/*服务列表删除*/
					ServiceCenter.serviceMap.get(currServiceName).removeAddress(currrentServiceAddress);
					NotifyConsumer.notifyAll(currServiceName, currrentServiceAddress);
					ctx.channel().close();
					
				}else {
					int integer = unPingMap.get(address).intValue() + 1;
					unPingMap.put(address, new Integer(integer));
				}
			}
		}else {
			super.userEventTriggered(ctx, evt);
		}
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String address = ctx .channel().remoteAddress().toString();
		unPingMap.put(address, 0);
		/*msg:serviceName:port*/
		String message = (String)msg;
		String currentServiceName = message.split(":")[0];
		int currentServicePort = Integer.parseInt(message.split(":")[1]);
		String heartBeatOfServiceAddress = PathUtil.getIpPortAddressWith(address, currentServicePort);
		if (!addrServiceMap.containsKey(address)) {
			HeartBeatWithServiceEntity entity = new HeartBeatWithServiceEntity();
			entity.setServiceName(currentServiceName);
			entity.setHeartBeatOfServiceAddress(heartBeatOfServiceAddress);
			addrServiceMap.put(address, entity);
			ServiceCenter.addressService.put(heartBeatOfServiceAddress, currentServiceName);
			
		}
		LOGGER.info("{}收到心跳消息{}",address,msg);
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		String address = ctx .channel().remoteAddress().toString();
		unPingMap.put(address, 0);
		super.channelRegistered(ctx);
		LOGGER.info("{}heartbeat Channel registered",address);
	}
}
