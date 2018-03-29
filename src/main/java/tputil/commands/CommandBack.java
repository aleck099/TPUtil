package tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import tputil.EmptyTeleporter;
import tputil.Statics;
import tputil.db.LastLoc;

public class CommandBack extends CommandBase {
	@Override
	public String getName() {
		return "back";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.back.usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			String name = player.getName();
			LastLoc loc = Statics.lastMap.get(name);
			if (loc == null) {
				/*没有传送记录*/
				sender.sendMessage(new TextComponentTranslation("commands.back.notfound"));
				return;
			}
			/*else*/
			Statics.lastMap.put(name, new LastLoc(player.getPositionVector(), player.dimension));

			if (player.dimension != loc.dimension) {
				/*跨维度*/
				server.getPlayerList().transferPlayerToDimension(player, loc.dimension, new EmptyTeleporter(server.getWorld(loc.dimension)));
			}

			/*传送*/
			player.setPositionAndUpdate(loc.loc.x, loc.loc.y, loc.loc.z);
			player.sendMessage(new TextComponentTranslation("commands.back.success"));
		} else {
			sender.sendMessage(new TextComponentTranslation("info.playeronly"));
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}
