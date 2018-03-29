package tputil.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import tputil.Statics;
import tputil.db.Location;
import tputil.db.WarpsManager;

import java.io.IOException;

public class CommandSetwarp extends CommandBase {
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return "setwarp";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.setwarp.usage";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			if (args.length != 1)
				throw new WrongUsageException("commands.setwarp.usage");

			String wname = args[0];
			EntityPlayerMP player = (EntityPlayerMP) sender;
			Location loc = new Location(player.dimension, new Vec3d(player.posX, player.posY, player.posZ));
			try {
				Statics.warpsManager.addWarp(wname, loc);
				sender.sendMessage(new TextComponentTranslation("commands.setwarp.success"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (WarpsManager.WarpAlreadyExistsException e) {
				sender.sendMessage(new TextComponentTranslation("commands.setwarp.warpexists", wname));
			}
		} else {
			sender.sendMessage(new TextComponentTranslation("info.playeronly"));
		}
	}
}
