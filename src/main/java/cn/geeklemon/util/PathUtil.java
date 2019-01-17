package cn.geeklemon.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.scheduling.annotation.Async;

import cn.geeklemon.registerparam.RegisterMessage;

public class PathUtil {
	
	/**
	 * 	zookeeper的路径处理类
	 * @param message
	 * @return
	 */
	@Async
	public static Set<String> getMethodsPath(RegisterMessage message) {
		String [] paths  =message.getMethodPath().split("-");
		Set<String> sets = new HashSet<>();
		for (String string : paths) {
			System.out.println("/"+message.getServiceName()+string);
			sets.add("/"+message.getServiceName()+"/methods"+string);
		}
		return sets;
	}
	
	
	/**
	 * 	新注册的服务，地址是注册中心监测赋值的，端口是传过来的。
	 * 	
	 * @param message
	 * @return	ip:port
	 */
	public static String formateAddresPath(RegisterMessage message) {
		String ip = message.getServiceAddress().split(":")[0].split("/")[1];
		int port = message.getServicePort();
		
		StringBuilder builder = new StringBuilder();
		builder.append(ip).append(":").append(port);
		return builder.toString();
	}
	
	/**
	 * 	获取不带 端口的ip地址
	 * @param addrStr ip:port
	 * @return	ip
	 */
	public static String getIpAddress(String addrStr) {
		return addrStr.split(":")[0];
	}
	
	public static int getPortByIpAddress(String addrStr) {
		return Integer.parseInt(addrStr.split(":")[1]);
	}
	
	/**
	 * 	把ip和port组合
	 * @param ip
	 * @param port
	 * @return	ip:port
	 */
	public static String getIpPortAddress(String ip,int port) {
		StringBuilder builder = new StringBuilder(ip);
		builder.append(":").append(port);
		return builder.toString();
	}
	
	/**
	 * 	地址和ip拼接
	 * @param address /ip:port ， netty channel 获取到的address
	 * @param port
	 * @return ip:port
	 */
	public static String getIpPortAddressWith(String address,int port) {
		String ip = address.split(":")[0].split("/")[1];
		StringBuilder builder = new StringBuilder(ip);
		builder.append(":").append(port);
		return builder.toString();
	}

}
