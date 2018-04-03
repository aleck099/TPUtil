package tk.dwcdn.tputil;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import tk.dwcdn.tputil.db.TpManager;
import tk.dwcdn.tputil.db.WarpsManager;
import tk.dwcdn.tputil.event.ModEventHandler;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		try {
			Statics.warpsManager = new WarpsManager(Paths.get(event.getModConfigurationDirectory().getCanonicalPath(), "warps"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Statics.lastMap = new HashMap<>();
		Statics.tpManager = new TpManager();
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
