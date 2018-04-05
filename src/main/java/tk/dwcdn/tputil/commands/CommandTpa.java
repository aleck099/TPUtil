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
import tk.dwcdn.tputil.db.TpRequest;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandTpa extends CommandBase {
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return (args.length == 1) ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
	}

	@Override
	public String getName() {
		return "tpa";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.tpa.usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Statics.tpManager.check();

		if (sender instanceof EntityPlayerMP) {
			if (args.length != 1)
				throw new WrongUsageException("commands.tpa.usage");

			String argName = args[0];
			EntityPlayerMP playerDest = server.getPlayerList().getPlayerByUsername(argName);
			if (playerDest == null) {
				/*目标玩家不存在*/
				sender.sendMessage(new TextComponentTranslation("commands.tpa.playernotfound", argName));
				return;
			}

			if (playerDest == sender) {
				/*不能tp自己*/
				sender.sendMessage(new TextComponentTranslation("commands.tpa.tpself"));
				return;
			}

			Statics.tpManager.request(new TpRequest(sender.getName(), argName, System.currentTimeMillis()));
			sender.sendMessage(new TextComponentTranslation("commands.tpa.success"));
			playerDest.sendMessage(new TextComponentTranslation("commands.tpa.request0", sender.getName()));
			playerDest.sendMessage(new TextComponentTranslation("commands.tpa.request1"));
			playerDest.sendMessage(new TextComponentTranslation("commands.tpa.request2"));
			playerDest.sendMessage(new TextComponentTranslation("commands.tpa.request3", 120));
		} else {
			sender.sendMessage(new TextComponentTranslation("info.playeronly"));
		}
	}
}
