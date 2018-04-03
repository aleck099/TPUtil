package tk.dwcdn.tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import tk.dwcdn.tputil.Statics;
import tk.dwcdn.tputil.db.Location;
import tk.dwcdn.tputil.db.WarpsManager;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandResetwarp extends CommandBase {
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length != 1)
			return Collections.emptyList();

		return Statics.warpsManager.getMatches(args[0]);
	}

	@Override
	public String getName() {
		return "resetwarp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.resetwarp.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			if (args.length != 1)
				throw new WrongUsageException("commands.warp.usage");

			String wname = args[0];
			EntityPlayerMP player = (EntityPlayerMP) sender;

			try {
				Statics.warpsManager.resetWarp(wname, new Location(player.dimension, player.getPositionVector()));
			} catch (WarpsManager.WarpNotFoundException e) {
				sender.sendMessage(new TextComponentTranslation("info.warpnotfound"));
				return;
			}

			sender.sendMessage(new TextComponentTranslation("commands.resetwarp.success"));
		} else {
			sender.sendMessage(new TextComponentTranslation("info.playeronly"));
		}
	}
}
