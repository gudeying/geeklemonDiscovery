package cn.geeklemon.registerparam;

public class RequestServiceMessage extends BaseMessage {
	/**
	 * 
	 */
	private String serviceName;
	private String params;
	private String string;

	public RequestServiceMessage() {
		super();
		setMsgType(MsgType.SERVICE);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
