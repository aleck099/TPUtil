package tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import tputil.Statics;

public class CommandDelwarps extends CommandBase {
	@Override
	public String getName() {
		return "delwarps";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.delwarps.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Statics.warpsManager.removeAll();
		sender.sendMessage(new TextComponentTranslation("commands.delwarps.success"));
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
