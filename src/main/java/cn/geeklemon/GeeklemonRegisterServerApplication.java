package cn.geeklemon;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import cn.geeklemon.nettyserver.ThreadExcute;
import cn.geeklemon.zookeeper.ZookeeperRegister;

@SpringBootApplication
@EnableAsync
public class GeeklemonRegisterServerApplication {
	/**
	 * 	根据serviceName获取info
	 */
//	public static ConcurrentHashMap<String, ServiceInfo> serviceMap = new ConcurrentHashMap<>();
	/**
	 * 	根据真实服务地址获取注册过的服务名，ip:port
	 */
//	public static ConcurrentHashMap<String, String> serviceAddressEntities = new ConcurrentHashMap<>();
	
//	public static ConcurrentHashMap<String, String>addressService = new ConcurrentHashMap<>();
	public static void main(String[] args) {
//		SpringApplication.run(GeeklemonRegisterServerApplication.class, args);
		ThreadExcute.initService();
		ZookeeperRegister.initCloudPath("127.0.0.1:2181", "", "", "");
	}
}
