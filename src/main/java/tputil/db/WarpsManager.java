package tputil.db;

import net.minecraft.util.math.Vec3d;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WarpsManager {
	private final File cfg;
	
	/**
	 * 
	 * @param config 地标目录，注意是目录
	 * @throws NullPointerException 如果传入 <b>null</b>
	 */
	public WarpsManager(File config) {
		if (config == null)
			throw new NullPointerException();
		if (config.isFile())
			config.delete();
		if (!config.exists()) {
			config.mkdirs();
			// server.getLogger().info("Dir maked");
		}
		this.cfg = config;
	}
	
	public void removeAll() {
		synchronized (cfg) {
			File[] fl = cfg.listFiles();
			if (fl == null)
				return;
			for (File f : fl) {
				f.delete();
			}
		}
	}
	
	/**
	 * 删除地标
	 * @param name 地标名
	 * @throws WarpNotFoundException 如果地标不存在
	 */
	public void removeWarp(String name) throws WarpNotFoundException {
		synchronized (cfg) {
			File f = new File(cfg, name);
			if (f.isDirectory()) {
				f.delete();
				return;
			}
			if (f.exists()) {
				f.delete();
			} else {
				throw new WarpNotFoundException();
			}
		}
	}
	
	/**
	 * 添加地标
	 * @param name 地标名
	 * @param loc 地标坐标
	 * @throws IOException 如果无法创建
	 * @throws WarpAlreadyExistsException 如果地标已存在
	 */
	public void addWarp(String name, Location loc) throws IOException, WarpAlreadyExistsException {
		synchronized (cfg) {
			File f = new File(cfg, name);
			if (f.isDirectory()) {
				f.delete();
			}
			if (!f.createNewFile()) {
				throw new WarpAlreadyExistsException();
			} else {
				OutputStreamWriter oWriter = new OutputStreamWriter(new FileOutputStream(f), Charset.forName("utf-8"));
				StringBuilder bd = new StringBuilder(String.valueOf(loc.dimension));
				bd.append(':');
				bd.append(loc.position.x);
				bd.append(':');
				bd.append(loc.position.y);
				bd.append(':');
				bd.append(loc.position.z);
				oWriter.write(bd.toString());
				oWriter.close();
			}
		}
	}

	/**
	 * 获取地标
	 * @param name 地标名
	 * @return 地标，如果不存在，返回 <b>null</b>
	 */
	public Location getByName(String name) {
		synchronized (cfg) {
			File[] ls = cfg.listFiles();
			if (ls == null)
				return null;
			for (File f : ls) {
				if (f.isDirectory()) {
					f.delete();
					continue;
				}
				if (f.getName().equals(name))
					return readFile(f);
			}
			return null;
		}
	}
	
	private Location readFile(File f) {
		try {
			InputStreamReader iReader = new InputStreamReader(new FileInputStream(f), Charset.forName("utf-8"));
			char[] buf_ = new char[512];
			int length = iReader.read(buf_);
			iReader.close();
			char[] buf = new char[length];
			System.arraycopy(buf_, 0, buf, 0, length);
			buf_ = null;
			String[] args = String.valueOf(buf).split(":", 4);
			if (args.length != 4)
				return null;
			// success
			return new Location(Integer.valueOf(args[0]), new Vec3d(
					Double.valueOf(args[1]),
					Double.valueOf(args[2]),
					Double.valueOf(args[3])));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getMatches(String prefix) {
		LinkedList<String> ls = new LinkedList<>();
		synchronized (cfg) {
			String[] fl = cfg.list();
			if (fl == null)
				return Collections.emptyList();

			for (String fname : fl) {
				if (fname.startsWith(prefix))
					ls.add(fname);
			}
		}
		return ls;
	}

	public String[] getAll() {
		synchronized (cfg) {
			return cfg.list();
		}
	}
	
	public class WarpAlreadyExistsException extends Exception {
		private static final long serialVersionUID = 2840953256251121506L;
	}
	
	public class WarpNotFoundException extends Exception {
		private static final long serialVersionUID = 1429184588373857898L;
	}
}
