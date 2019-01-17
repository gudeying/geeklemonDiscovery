package cn.geeklemon.serviceholder;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.geeklemon.ServiceInfo;

public class ServiceCenter {
	/**
	 * 	根据serviceName获取info
	 */
	public static final ConcurrentHashMap<String, ServiceInfo> serviceMap = new ConcurrentHashMap<>();
	
	/**
	 * 	记录收到provider注册的地址 (/ip:port)对应提供的服务地址(ip:port)和服务名,以便连接断开时移除服务
	 */
	public static final ConcurrentHashMap<String, List<String>>serviceConnecter = new ConcurrentHashMap<>();

	
	public static final ConcurrentHashMap<String, String>addressService = new ConcurrentHashMap<>();
	
	/**
	 * 	发生生产者强制断开服务时，移除连接信息以及服务表
	 * @param connAddress：断开连接方的地址 /ip:port
	 */
	public static void removeConnecterAndServiceMap(String connAddress) {
		List<String> serviceNameAndAddr = serviceConnecter.get(connAddress);
		serviceConnecter.remove(connAddress);
		String serviceName = serviceNameAndAddr.get(0);
		String serviceAddr = serviceNameAndAddr.get(1);
		serviceMap.get(serviceName).removeAddress(serviceAddr);
	}
	
	/**
	 * 	添加连接信息
	 * @param connAddress：连接注册中心的地址/ip:port
	 * @param serviceName：服务名
	 * @param serviceAddr：服务提供者提供服务的监听地址ip:port
	 */
	public static synchronized void addConnecter(String connAddress,String serviceName,String serviceAddr) {
		List<String> list = new LinkedList<>();
		list.add(serviceName);
		list.add(serviceAddr);
		serviceConnecter.put(connAddress,list);
	}
	
	/**
	 * 	心跳断开移除服务表和连接表
	 * @param serviceName：服务名
	 * @param serviceAddr：服务地址 ip:port
	 */
	public static void removeWithHeartBeat(String serviceName,String serviceAddr) {
		Set<Entry<String, List<String>>> entrySet = serviceConnecter.entrySet();
		for (Entry<String, List<String>> entry : entrySet) {
			List<String>list = entry.getValue();
			String name = list.get(0);
			String address = list.get(1);
			if (name.equals(serviceName)&&address.equals(serviceAddr)) {
				serviceConnecter.remove(entry.getKey());
				break;
			}
		}
		serviceMap.get(serviceName).removeAddress(serviceAddr);
	}
}
