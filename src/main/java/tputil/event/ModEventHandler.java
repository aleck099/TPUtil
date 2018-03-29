package tputil.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import tputil.Statics;
import tputil.db.LastLoc;

public class ModEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
		Statics.tpMgr.check();
		Statics.tpMgr.removeBySourceName(event.player.getName());
		Statics.tpMgr.removeByDestName(event.player.getName());
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
			/*添加死亡坐标*/
			Statics.lastMap.put(player.getName(), new LastLoc(new Vec3d(player.posX, player.posY, player.posZ), player.dimension));
		}
	}

}
