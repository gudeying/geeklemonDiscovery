package cn.geeklemon.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;


import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZookeeperService implements Watcher {
	private ZooKeeper zooKeeper;
	private String coonString = "127.0.0.1:2181";
	private final CountDownLatch countDownLatch = new CountDownLatch(1);

	@Override
	public void process(WatchedEvent event) {
		KeeperState keeperState = event.getState();
		EventType eventType = event.getType();
		if (event.getState() == KeeperState.SyncConnected) {
			System.out.println("Watch received event");
			countDownLatch.countDown();
			return;
		}
		else if (eventType == EventType.NodeDeleted) {
			//子节点删除,通知服务不可用
			
		}
		
//		try {
//			zooKeeper.exists("/geeklemon/provider", true);
//		} catch (KeeperException | InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	public ZookeeperService(String connectStr) {
		this.coonString = connectStr;
	}
	public ZookeeperService connect() {
		try {
			zooKeeper = new ZooKeeper(coonString, 3000, this);
			countDownLatch.await();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}

	public String createNode(String path, String data,CreateMode mode) throws Exception {
		return this.zooKeeper.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, mode);
	}

	public List<String> getChildren(String path) throws KeeperException, InterruptedException {
		List<String> children = zooKeeper.getChildren(path, false);
		return children;
	}

	public String getData(String path) throws KeeperException, InterruptedException {
		byte[] data = zooKeeper.getData(path, false, null);
		if (data == null) {
			return "";
		}
		return new String(data);
	}

	public Stat setData(String path, String data) throws KeeperException, InterruptedException {
		Stat stat = zooKeeper.setData(path, data.getBytes(), -1);
		return stat;
	}

	public void deleteNode(String path) throws InterruptedException, KeeperException {
		zooKeeper.delete(path, -1);
	}

	
	public void closeConnection() throws InterruptedException {
		if (zooKeeper != null) {
			zooKeeper.close();
		}
	}
	
	public boolean exites(String path) {
		Stat stat = null;
		try {
			stat = zooKeeper.exists(path, false);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		if (stat == null) {
			return false;
		}
		return true;
	}
}
