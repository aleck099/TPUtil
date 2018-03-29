package tputil.db;

public class TpRequest {
	public TpRequest(String source, String dest, long tick) {
		this.playerDest = dest;
		this.playerSource = source;
		this.time = tick;
	}
	
	public final String playerSource;
	public final String playerDest;
	public final long time; // 请求发起时间
	
}
