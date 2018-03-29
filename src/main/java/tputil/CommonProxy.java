package tputil;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import tputil.db.TpMgr;
import tputil.db.WarpsManager;
import tputil.event.ModEventHandler;

import java.io.File;
import java.util.HashMap;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		Statics.warpsManager = new WarpsManager(new File(event.getModConfigurationDirectory(), "warps"));
		Statics.lastMap = new HashMap<>();
		Statics.tpMgr = new TpMgr();
	}

	public void init() {
		EventBus bus = MinecraftForge.EVENT_BUS;
		bus.register(new ModEventHandler());
	}

	public static class ClientProxy extends CommonProxy {
		@Override
		public void preInit(FMLPreInitializationEvent event) {

		}

		@Override
		public void init() {

		}
	}
}
