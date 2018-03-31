package tputil;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import tputil.commands.*;

@Mod(modid = TPUtil.MODID, version = TPUtil.VERSION)
public class TPUtil {

	public static final String MODID = "tputil", VERSION = "1.0.4";

	@SidedProxy(serverSide = "tputil.CommonProxy", clientSide = "tputil.CommonProxy$ClientProxy", modId = MODID)
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandBack());
		event.registerServerCommand(new CommandDelwarp());
		event.registerServerCommand(new CommandDelwarps());
		event.registerServerCommand(new CommandSetwarp());
		event.registerServerCommand(new CommandTpa());
		event.registerServerCommand(new CommandTpaccept());
		event.registerServerCommand(new CommandTpdeny());
		event.registerServerCommand(new CommandWarp());
		event.registerServerCommand(new CommandWarps());
		event.registerServerCommand(new CommandResetwarp());
	}

}
