package tputil.db;

import net.minecraft.util.math.Vec3d;

public class LastLoc {
	public LastLoc(Vec3d loc, int dimension) {
		this.loc = loc;
		this.dimension = dimension;
	}

	public final Vec3d loc;
	public final int dimension;
	/*
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object an) {
		if (an instanceof LastLoc) {
			return (((LastLoc) an).player.equals(this.player));
		} else
			return false;
	}
	*/
}
