package cn.geeklemon.serviceholder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class ConsumerCenter {
	/**
	 * 	服务名称对应的申请消费者的 channel,服务发生变化就可以通知消费者该节点不可用,注册的时候已经把所有服务放入
	 */
	public static final ConcurrentHashMap<String, Set<Channel>> consumerChannel = new ConcurrentHashMap<>();
	/**
	 * 	地址对应申请了哪些服务
	 */
	public static final ConcurrentHashMap<String, Set<String>> consumerAndService = new ConcurrentHashMap<>();
	
	public static void addConsumerSservice(String address,String serviceName) {
		consumerAndService.get(address).add(serviceName);
	}
	
	public static void AddCousumer(String serviceName,Channel channel) {
		consumerChannel.get(serviceName).add(channel);
	}
	
	public static void consumerRecord(String address,String[]services) {
		Set<String> set =  new HashSet<>();
		for (String string : set) {
			set.add(string);
		}
 		consumerAndService.put(address, set);
	}
	
	/**
	 * 	注册服务的时候加入map，便于消费者来的时候直接记录
	 * @param serviceName
	 */
	public static void initCnsumerMap(String serviceName) {
		consumerChannel.put(serviceName, new HashSet<>());
	}
}
