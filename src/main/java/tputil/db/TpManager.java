package tputil.db;

import java.util.ArrayList;
import java.util.List;

public class TpManager {
	private final List<TpRequest> res;
	
	public TpManager() {
		res = new ArrayList<>();
	}

	public void request(TpRequest req) {
		removeByDestName(req.playerDest);
		removeBySourceName(req.playerDest);
		synchronized (res) {
			res.add(req);
		}
	}
	
	/**
	 * 获取传送请求
	 * @param name 目标玩家名
	 * @return 该请求，如果没有请求，返回 null
	 */
	public TpRequest getByDestName(String name) {
		synchronized (res) {
			for (TpRequest tr : res) {
				if (tr.playerDest.equals(name))
					return tr;
			}
		}
		return null;
	}
	
	public void removeByDestName(String destName) {
		synchronized (res) {
			res.removeIf(req -> req.playerDest.equals(destName));
		}
	}
	
	public void removeBySourceName(String sourceName) {
		synchronized (res) {
			res.removeIf(req -> req.playerDest.equals(sourceName));
		}
	}

	/**
	 * 检查传送请求队列，删掉无效的请求
	 */
	public void check() {
		synchronized (res) {
			long tk = System.currentTimeMillis();
			res.removeIf(req -> req.time + 120000 < tk);
		}
	}
}
