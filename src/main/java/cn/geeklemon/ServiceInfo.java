package cn.geeklemon;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.geeklemon.util.PathUtil;

import java.util.Set;

public class ServiceInfo {
	private String serviceName;
	/**
	 * 	path数组,0是path，1是信息（暂时为空）没有使用zookeeper之前该值没有用
	 */
	private Set<String[]> servicePaths = new HashSet<>();
	/**
	 * 	地址数组，ip:port 形式
	 */
	private Set<String> serviceAddress = new HashSet<>();
	private String description;
	
	
	
	/**
	 * 	
	 * @param address ip:port
	 */
	public void addAddress(String address) {
		serviceAddress.add(address);
	}
	
	/**
	 * 
	 * @param address ip:port
	 */
	public void removeAddress(String address) {
		serviceAddress.remove(address);
	}
	
	public void addAllPaths(Set<Map<String, String>> paths) {
		for (Map<String, String> map : paths) {
			Entry<String, String> pathInfo = map.entrySet().iterator().next();
			String path = pathInfo.getKey();
//			String pathParam = pathInfo.getValue();
			//参数暂时为空
			String pathParam = map.get(path);
			String [] pathArr = {path,pathParam};
			
			servicePaths.add(pathArr);
		}
	}

	public void pathAddOne(String path, String info) {
		String [] pathArr = {path,info};
		servicePaths.add(pathArr);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Set<String[]> getServicePaths() {
		return servicePaths;
	}

	public void setServicePaths(Set<String[]> servicePaths) {
		this.servicePaths = servicePaths;
	}

	public Set<String> getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(Set<String> serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
