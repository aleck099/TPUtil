package tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import tputil.EmptyTeleporter;
import tputil.Statics;
import tputil.db.Location;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandWarp extends CommandBase {
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return "warp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.warp.usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			if (args.length != 1)
				throw new WrongUsageException("commands.warp.usage");

			EntityPlayerMP player = (EntityPlayerMP) sender;
			Location loc = Statics.warpsManager.getByName(args[0]);
			if (loc == null) {
				sender.sendMessage(new TextComponentTranslation("info.warpnotfound", args[0]));
				return;
			}

			/*骑乘时不得tp，否则  哼哼*/
			if (player.isRiding()) {
				player.sendMessage(new TextComponentTranslation("info.ridingcannottp"));
				return;
			}

			Statics.lastMap.put(player.getName(), new Location(player.dimension, player.getPositionVector()));

			if (player.dimension != loc.dimension) {
				try {
					server.getPlayerList().transferPlayerToDimension(player, loc.dimension, new EmptyTeleporter());
				} catch (Throwable e) {
					/*维度无效？或者是什么令人费解的东西？*/
					player.sendMessage(new TextComponentTranslation("commands.warp.failure"));
					return;
				}
			}
			player.setPositionAndUpdate(loc.position.x, loc.position.y, loc.position.z);
			player.sendMessage(new TextComponentTranslation("commands.warp.success"));
		} else {
			sender.sendMessage(new TextComponentTranslation("info.playeronly"));
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length != 1)
			return Collections.emptyList();

		return Statics.warpsManager.getMatches(args[0]);
	}
}
