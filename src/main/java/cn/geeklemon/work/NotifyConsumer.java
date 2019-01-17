package cn.geeklemon.work;

import java.util.Set;

import cn.geeklemon.registerparam.NotifyMessage;
import cn.geeklemon.serviceholder.ConsumerCenter;
import io.netty.channel.Channel;

public class NotifyConsumer {
	public static void notifyAll(String serviceName,String address) {
		Set<Channel> channels = ConsumerCenter.consumerChannel.get(serviceName);
		NotifyMessage message = new NotifyMessage();
		message.setServiceName(serviceName);
		message.setAddress(address);
		for (Channel channel : channels) {
			channel.writeAndFlush(message);
		}
	}
}
