package tk.dwcdn.tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import tk.dwcdn.tputil.Statics;
import tk.dwcdn.tputil.db.WarpsManager;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandDelwarp extends CommandBase {
	@Override
	public String getName() {
		return "delwarp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.delwarp.usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 1)
			throw new WrongUsageException("commands.delwarp.usage");
		try {
			Statics.warpsManager.removeWarp(args[0]);
			sender.sendMessage(new TextComponentTranslation("commands.delwarp.success"));
		} catch (WarpsManager.WarpNotFoundException e) {
			sender.sendMessage(new TextComponentTranslation("info.warpnotfound", args[0]));
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length != 1)
			return Collections.emptyList();
		return Statics.warpsManager.getMatches(args[0]);
	}
}
