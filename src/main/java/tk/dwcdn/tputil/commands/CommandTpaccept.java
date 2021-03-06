package tk.dwcdn.tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import tk.dwcdn.tputil.EmptyTeleporter;
import tk.dwcdn.tputil.Statics;
import tk.dwcdn.tputil.db.Location;
import tk.dwcdn.tputil.db.TpRequest;

import java.util.List;

public class CommandTpaccept extends CommandBase {
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return "tpaccept";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.tpaccept.usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		Statics.tpManager.check();

		if (sender instanceof EntityPlayerMP) {
			TpRequest req = Statics.tpManager.getByDestName(sender.getName());
			if (req == null) {
				sender.sendMessage(new TextComponentTranslation("commands.tpaccept.norequest"));
				return;
			}

			List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
			EntityPlayerMP source = null;
			/*查找玩家*/
			for (EntityPlayerMP p : players) {
				if (p.getName().equals(req.playerSource))
					source = p;
			}

			if (source == null) {
				sender.sendMessage(new TextComponentTranslation("commands.tpaccept.invalid"));
				return;
			}

			/*你给我下去*/
			if (source.isRiding()) {
				source.dismountRidingEntity();
			}

			Statics.lastMap.put(source.getName(), new Location(source.dimension, source.getPositionVector()));

			EntityPlayerMP dest = (EntityPlayerMP) sender;
			/*跨纬度*/
			if (source.dimension != dest.dimension)
				server.getPlayerList().transferPlayerToDimension(source, dest.dimension, new EmptyTeleporter(source.getServerWorld()));

			source.setPositionAndUpdate(dest.posX, dest.posY, dest.posZ);
			source.sendMessage(new TextComponentTranslation("commands.tpaccept.success.source"));
			dest.sendMessage(new TextComponentTranslation("commands.tpaccept.success.dest"));
			Statics.tpManager.removeByDestName(req.playerDest);
		} else {
			sender.sendMessage(new TextComponentTranslation("info.playeronly"));
		}
	}
}
