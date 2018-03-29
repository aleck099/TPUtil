package tputil.db;

import net.minecraft.util.math.Vec3d;

public class Location {
	public final int dimension;
	public final Vec3d position;

	public Location(int dimension, Vec3d position) {
		this.dimension = dimension;
		this.position = position;
	}
}
