package tk.dwcdn.tputil.db;

import net.minecraft.util.math.Vec3d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class WarpsManager {
	private final Path wpath;

	/**
	 * @param directory 地标目录，注意是目录
	 * @throws NullPointerException 如果传入 <b>null</b>
	 */
	public WarpsManager(Path directory) {
		if (directory == null)
			throw new NullPointerException();

		try {
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}
			if (!Files.isDirectory(directory)) {
				Files.delete(directory);
				Files.createDirectories(directory);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.wpath = directory;
	}

	public void removeAll() {
		try {
			for (Path f : wpath) {
				Files.delete(f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重设地标
	 *
	 * @param name 地标名
	 * @param loc  位置
	 * @throws WarpNotFoundException 如果地标不存在
	 */
	public void resetWarp(String name, Location loc) throws WarpNotFoundException {
		try (
				OutputStreamWriter oWriter = new OutputStreamWriter(Files.newOutputStream(wpath.resolve(name)), Charset.forName("utf-8"))
		) {
			oWriter.write(String.valueOf(loc.dimension));
			oWriter.write(':');
			oWriter.write(String.valueOf(loc.position.x));
			oWriter.write(':');
			oWriter.write(String.valueOf(loc.position.y));
			oWriter.write(':');
			oWriter.write(String.valueOf(loc.position.z));
		} catch (FileNotFoundException e) {
			throw new WarpNotFoundException();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重命名地标
	 * @param rawName 原名
	 * @param newName 新地标名
	 * @throws WarpNotFoundException 如果原地标不存在
	 * @throws WarpAlreadyExistsException 如果新地标已存在
	 */
	public void renameWarp(String rawName, String newName) throws WarpNotFoundException, WarpAlreadyExistsException {
		Path de = wpath.resolve(rawName);
		if (!Files.exists(de))
			throw new WarpNotFoundException();

		try {
			Files.move(de, wpath.resolve(newName));
		} catch (FileAlreadyExistsException e) {
			throw new WarpAlreadyExistsException();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除地标
	 *
	 * @param name 地标名
	 * @throws WarpNotFoundException 如果地标不存在
	 */
	public void removeWarp(String name) throws WarpNotFoundException {
		Path f = wpath.resolve(name);
		if (!Files.exists(f))
			throw new WarpNotFoundException();
		if (Files.isDirectory(f)) {
			try {
				Files.delete(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			throw new WarpNotFoundException();
		}

		try {
			Files.delete(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加地标
	 *
	 * @param name 地标名
	 * @param loc  地标坐标
	 * @throws WarpAlreadyExistsException 如果地标已存在
	 */
	public void addWarp(String name, Location loc) throws WarpAlreadyExistsException {
		Path f = wpath.resolve(name);
		if (Files.isDirectory(f))
			try {
				Files.delete(f);
			} catch (IOException e) {
				e.printStackTrace();
			}

		if (Files.exists(f))
			throw new WarpAlreadyExistsException();

		try {
			Files.createFile(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (
				OutputStreamWriter oWriter = new OutputStreamWriter(Files.newOutputStream(f), Charset.forName("utf-8"))
		) {
			oWriter.write(String.valueOf(loc.dimension));
			oWriter.write(':');
			oWriter.write(String.valueOf(loc.position.x));
			oWriter.write(':');
			oWriter.write(String.valueOf(loc.position.y));
			oWriter.write(':');
			oWriter.write(String.valueOf(loc.position.z));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取地标
	 *
	 * @param name 地标名
	 * @return 地标，如果不存在，返回 <b>null</b>
	 */
	public Location getByName(String name) {
		Path f = wpath.resolve(name);
		if (!Files.exists(f))
			return null;

		return readFile(f);
	}

	private static Location readFile(Path f) {
		String buf;
		try {
			List<String> ls = Files.readAllLines(f, Charset.forName("utf-8"));
			if (ls.size() < 1)
				return null;
			buf = ls.get(0);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		String[] args = buf.split(":", 4);
		if (args.length != 4)
			return null;
		/* success */
		return new Location(Integer.valueOf(args[0]), new Vec3d(
				Double.valueOf(args[1]),
				Double.valueOf(args[2]),
				Double.valueOf(args[3])));
	}

	public List<String> getMatches(final String prefix) {
		List<String> ls = new ArrayList<>();
		Stream<Path> fl;
		try {
			fl = Files.list(wpath);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}

		fl.filter(p -> p.getFileName().toString().startsWith(prefix)).forEach(p -> ls.add(p.getFileName().toString()));
		return ls;
	}

	public List<String> getAll() {
		List<String> ls = new ArrayList<>();
		try {
			Files.list(wpath).forEach(p -> ls.add(p.getFileName().toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ls;
	}

	public class WarpAlreadyExistsException extends Exception {
		private static final long serialVersionUID = 2840953256251121506L;
	}

	public class WarpNotFoundException extends Exception {
		private static final long serialVersionUID = 1429184588373857898L;
	}
}
