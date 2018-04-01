package tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import tputil.Statics;

import java.util.List;

public class CommandWarps extends CommandBase {
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public String getName() {
		return "warps";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.warps.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			List<String> alist = Statics.warpsManager.getAll();
			StringBuffer sbuf = new StringBuffer(new TextComponentTranslation("commands.warps.list").getUnformattedText());
			for (String s : alist) {
				sbuf.append("\n-§a " + s);
			}
			sender.sendMessage(new TextComponentString(sbuf.toString()));
		} else {
			int page;
			try {
				page = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				throw new WrongUsageException("commands.warps.usage");
			}

			List<String> sm = Statics.warpsManager.getAll();
			int warps = sm.size(); // 地标总数
			// 计算页数
			int pages = warps / 8 + 1;
			if (warps % 8 == 0)
				pages--;
			if (page > pages - 1) {
				sender.sendMessage(new TextComponentTranslation("commands.warps.outofrage"));
				return;
			}
			// else
			int rgstart = page * 8;
			int rgend = rgstart + 8;
			StringBuffer out = new StringBuffer(new TextComponentTranslation("commands.warps.head", String.valueOf(page) + 1, String.valueOf(pages)).getUnformattedText());
			for (int lo = rgstart; lo < warps && lo < rgend; lo++) {
				out.append("\n- §a" + sm.get(lo));
			}
			sender.sendMessage(new TextComponentString(out.toString()));
		}
	}
}
