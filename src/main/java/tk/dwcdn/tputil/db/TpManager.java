package tk.dwcdn.tputil.db;

import java.util.ArrayList;
import java.util.List;

public class TpManager {
	private final List<TpRequest> soe;
	
	public TpManager() {
		soe = new ArrayList<>();
	}

	public void request(TpRequest req) {
		removeByDestName(req.playerDest);
		removeBySourceName(req.playerDest);
		synchronized (soe) {
			soe.add(req);
		}
	}
	
	/**
	 * 获取传送请求
	 * @param name 目标玩家名
	 * @return 该请求，如果没有请求，返回 null
	 */
	public TpRequest getByDestName(String name) {
		synchronized (soe) {
			for (TpRequest tr : soe) {
				if (tr.playerDest.equals(name))
					return tr;
			}
		}
		return null;
	}
	
	public void removeByDestName(String destName) {
		synchronized (soe) {
			soe.removeIf(req -> req.playerDest.equals(destName));
		}
	}
	
	public void removeBySourceName(String sourceName) {
		synchronized (soe) {
			soe.removeIf(req -> req.playerDest.equals(sourceName));
		}
	}

	/**
	 * 检查传送请求队列，删掉无效的请求
	 */
	public void check() {
		synchronized (soe) {
			long tk = System.currentTimeMillis();
			soe.removeIf(req -> req.time + 120000 < tk);
		}
	}
}
