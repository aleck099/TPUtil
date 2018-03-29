package tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import tputil.Statics;
import tputil.db.TpRequest;

import java.util.List;

public class CommandTpdeny extends CommandBase {
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return "tpdeny";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.tpdeny.usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		Statics.tpMgr.check();

		if (sender instanceof EntityPlayerMP) {
			String dname = sender.getName();
			TpRequest req = Statics.tpMgr.getByDestName(dname);

			if (req == null) {
				sender.sendMessage(new TextComponentTranslation("commands.tpdeny.norequest"));
				return;
			}

			List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
			EntityPlayerMP source = null;
			/*查找玩家*/
			for (EntityPlayerMP p : players) {
				if (p.getName().equals(req.playerSource))
					source = p;
			}

			if (source != null) {
				source.sendMessage(new TextComponentTranslation("commands.tpdeny.success.source", dname));
			}

			Statics.tpMgr.removeByDestName(dname);
			sender.sendMessage(new TextComponentTranslation("commands.tpdeny.success.dest"));
		} else {
			sender.sendMessage(new TextComponentTranslation("info.playeronly"));
		}
	}
}
