package com.mactso.hardernaturalhealing;

import com.mactso.hardernaturalhealing.commands.HarderNaturalHealingCommands;
import com.mactso.hardernaturalhealing.config.MyConfig;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;


@Mod("hardernaturalhealing")
public class Main {

	public static final String MODID = "hardernaturalhealing";

	public Main(IEventBus modEventBus, ModContainer container) {
		System.out.println("hardernaturalhealing: Registering Mod.");
		modEventBus.register(this);
		container.registerConfig(ModConfig.Type.COMMON, MyConfig.COMMON_SPEC);
	}

	@SubscribeEvent
	public void preInit(final FMLCommonSetupEvent event) {
		System.out.println("hardernaturalhealing: Registering Handler");
// remove these redundant registrations (since using @Mod.EventBusSubscriber() now);
//		MinecraftForge.EVENT_BUS.register(new PlayerTickHandler());
//		MinecraftForge.EVENT_BUS.register(new PlayerWakeupEventHandler());

	}

	@EventBusSubscriber()
	public static class ForgeEvents {
		@SubscribeEvent
		public static void preInit(final ServerStartingEvent event) {
			System.out.println("hardernaturalhealing: Turn natural regeneration off.");
			((GameRules.BooleanValue) event.getServer().getGameRules()
					.getRule(GameRules.RULE_NATURAL_REGENERATION)).set(false,
					event.getServer());
		}

		@SubscribeEvent
		public static void preInit(final ServerStoppingEvent event) {
			System.out.println("hardernaturalhealing: Turn natural regeneration rule on.");
			((GameRules.BooleanValue) event.getServer().getGameRules()
					.getRule(GameRules.RULE_NATURAL_REGENERATION)).set(true,
					event.getServer());
		}

		@SubscribeEvent
		public static void onCommandsRegistry(final RegisterCommandsEvent event) {
			System.out.println("Happy Trails: Registering Command Dispatcher");
			HarderNaturalHealingCommands.register(event.getDispatcher());
		}
	}


}
