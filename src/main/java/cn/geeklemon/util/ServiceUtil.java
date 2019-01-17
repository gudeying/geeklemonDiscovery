package cn.geeklemon.util;

import java.awt.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import cn.geeklemon.GeeklemonRegisterServerApplication;
import cn.geeklemon.ServiceInfo;
import cn.geeklemon.serviceholder.ServiceCenter;

public class ServiceUtil {
	public static String getAddress(String serviceName) {
		final Map<String, ServiceInfo> serviceMap = ServiceCenter.serviceMap;
		ServiceInfo serviceInfo = serviceMap.get(serviceName);
		Set<String> addrs = serviceInfo.getServiceAddress();
		int length = addrs.size();
		int random = (int) (Math.random() * (length));// 随机的地址模拟负载均衡，待改

		Object[] list = addrs.toArray();
		System.out.println((String) list[0]);
		return (String) list[random];
	}

	public static String[] getAllServe(String serviceName) {
		final Map<String, ServiceInfo> serviceMap = ServiceCenter.serviceMap;
		ServiceInfo serviceInfo = serviceMap.get(serviceName);
		if (serviceInfo == null) {
			return null;
		}
		Set<String> addrs = serviceInfo.getServiceAddress();
		String[] result = new String[addrs.size()];
		result = addrs.toArray(result);
		return result;
	}
}
