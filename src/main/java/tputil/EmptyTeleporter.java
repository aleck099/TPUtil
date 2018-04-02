package tputil;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * 这个类就是用来搞笑的
 * 谁叫MC的传送机制这么垃圾呢
 */
public class EmptyTeleporter extends Teleporter {

	public EmptyTeleporter(WorldServer worldIn) {
		super(worldIn);
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw) {
	}

	@Override
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
		return false;
	}

	@Override
	public boolean makePortal(Entity entityIn) {
		return false;
	}

	@Override
	public void removeStalePortalLocations(long worldTime) {
	}
}
