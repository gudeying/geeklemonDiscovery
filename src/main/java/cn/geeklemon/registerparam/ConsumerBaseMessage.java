package cn.geeklemon.registerparam;

import java.io.Serializable;

public class ConsumerBaseMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MsgType msgType;

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

}
