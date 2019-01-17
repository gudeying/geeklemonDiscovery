package cn.geeklemon.heartbeat;

public class HeartBeatWithServiceEntity {
	private String serviceName;
	/**
	 * 	当前心跳包对应的服务地址ip:port
	 */
	private String heartBeatOfServiceAddress;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/**
	 * 	当前心跳包对应的服务地址ip:port
	 */
	public String getHeartBeatOfServiceAddress() {
		return heartBeatOfServiceAddress;
	}

	public void setHeartBeatOfServiceAddress(String heartBeatOfServiceAddress) {
		this.heartBeatOfServiceAddress = heartBeatOfServiceAddress;
	}

}
