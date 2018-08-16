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

public class CommandRenamewarp extends CommandBase {
	@Override
	public String getName() {
		return "renamewarp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 2)
			throw new WrongUsageException("commands.renamewarp.usage");

		try {
			Statics.warpsManager.renameWarp(args[0], args[1]);
		} catch (WarpsManager.WarpNotFoundException e) {
			sender.sendMessage(new TextComponentTranslation("info.warpnotfound", args[0]));
			return;
		} catch (WarpsManager.WarpAlreadyExistsException e) {
			sender.sendMessage(new TextComponentTranslation("commands.renamewarp.warpexists", args[1]));
			return;
		}

		sender.sendMessage(new TextComponentTranslation("commands.renamewarp.success"));
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
