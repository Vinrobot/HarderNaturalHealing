package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;

@EventBusSubscriber()
public class PlayerWakeupEventHandler {

	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {

		Player e = event.getEntity();

		if (e.level().isClientSide())
			return;

		if (!e.level().isDay())
			return;

		event.getEntity().heal((float) MyConfig.getWakeupHealingAmount());

	}
}
