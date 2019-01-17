package cn.geeklemon.registerWork;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.geeklemon.GeeklemonRegisterServerApplication;
import cn.geeklemon.ServiceInfo;
import cn.geeklemon.entity.ServiceAddressEntity;
import cn.geeklemon.registerparam.RegisterMessage;
import cn.geeklemon.serviceholder.ConsumerCenter;
import cn.geeklemon.serviceholder.ServiceCenter;
import cn.geeklemon.util.PathUtil;

public class HandleRegister {
	
	public static void registerApplicationMap (RegisterMessage message) {
		String serviceName = message.getServiceName();
		ServiceInfo serviceInfo = ServiceCenter.serviceMap.get(serviceName);
		String address = PathUtil.formateAddresPath(message);
		
		if (serviceInfo != null) {
			//只注册address
			addServicePrivider(serviceName, address);
			return;
		}
		/*是新的服务，要注册path和地址*/
		//先注册consumercenter
		ConsumerCenter.initCnsumerMap(serviceName);
		addNewService(serviceName, address, message.getPathArr());
	}
	
	
	public static void addServicePrivider(String serviceName,String address) {
		ServiceInfo serviceInfo = ServiceCenter.serviceMap.get(serviceName);
		serviceInfo.addAddress(address);
		
	}
	
	public static void addNewService(String serviceName,String address,String[] paths) {
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setDescription("这是描述信息");
		Set<Map<String, String>> path = new HashSet<>();
		for (String map : paths) {
			Map<String, String> map2 = new HashMap<>();
			//value保存路径的其他信息，暂时没有
			map2.put((String)map, "");
			path.add(map2);
		}
//		serviceInfo.addAllPaths(path);
		/*注册路径*/
		ServiceCenter.serviceMap.put(serviceName, serviceInfo);
		
		/*注册地址*/
		ServiceCenter.serviceMap.get(serviceName).addAddress(address);
	}
	
	
	public static void addServiceConnecter(String connAddress,RegisterMessage message) {
		int port = message.getServicePort();
		String serviceAddress = PathUtil.getIpPortAddressWith(connAddress, port);
		ServiceCenter.addConnecter(connAddress, message.getServiceName(), serviceAddress);
	}
}
