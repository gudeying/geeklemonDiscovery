package cn.geeklemon.nettyserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.yetus.audience.InterfaceAudience.Private;

import cn.geeklemon.heartbeat.HeartBeatBootThread;
import cn.geeklemon.test.HeartbeatThread;

public class ThreadExcute {
	private static ExecutorService pool = Executors.newSingleThreadExecutor();
	private static ExecutorService heartBeatPool = Executors.newSingleThreadExecutor();
	private static Runnable serviceThread = new ServiceBootThread();
	private static HeartBeatBootThread heartBeatBootThread = new HeartBeatBootThread();
	public static void initService() {
		pool.submit(serviceThread);
		
	}
	public static void startHeartBeat() {
		heartBeatPool.submit(heartBeatBootThread);
	}
	
	public static void shutDownAll() {
		pool.shutdown();
		heartBeatBootThread.shutDown();
		heartBeatPool.shutdown();
	}
	
	
//	public static void main(String[] args)  {
//		ThreadExcute.startHeartBeat();
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("shutDown");
//		
//		ThreadExcute.shutDownAll();
//	}
}
