package cn.geeklemon.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.geeklemon.registerparam.RegisterMessage;
import cn.geeklemon.util.PathUtil;

public class ZookeeperRegister {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegister.class);
	/**
	 * /geeklemoncloud/provider/{servicename}/uid 赋值为真实ip:port
	 * 
	 * @param address:/ip:port
	 * @param message:注册信息
	 */
	public static void registerWithZookeeper(String address, RegisterMessage message) {
		String serviceName = message.getServiceName();
		ZookeeperService service = new ZookeeperService("127.0.0.1:2181").connect();
		int servicePort = message.getServicePort();
		String serviceAddress = PathUtil.getIpPortAddressWith(address, servicePort);
		String namePath = "/geeklemoncloud/provider/" + serviceName;
		String serviceUid = message.getUid();
		String addrPath = namePath + "/" + serviceUid;
		if (service.exites(addrPath)) {
			try {
				service.setData(addrPath, serviceAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			service.closeConnection();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化zookeeper 节点--/geekleoncloud/provider
	 * 
	 * @param connectString
	 * @param cloudPath     ：名称，没有/
	 * @param name
	 * @param password
	 */
	public static void initCloudPath(String connectString, String cloudPath, String name, String password) {
		ZookeeperService zookeeperService = new ZookeeperService(connectString).connect();
		
		String basePath = "/geeklemoncloud";
		String providerPath = "/geeklemoncloud/provider";
		String consumerPath = "/geeklemoncloud/consumer";
	
		try {
			if (!zookeeperService.exites(basePath)) {
				zookeeperService.createNode(basePath, "", CreateMode.PERSISTENT);
			}
			if (!zookeeperService.exites(providerPath)) {
				zookeeperService.createNode(providerPath, "", CreateMode.PERSISTENT);
			}
			if (!zookeeperService.exites(consumerPath)) {
				zookeeperService.createNode(consumerPath, "", CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			zookeeperService.closeConnection();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOGGER.info("zookeeper服务路径初始化完成");
	}
}
