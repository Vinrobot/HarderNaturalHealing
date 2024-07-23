package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;

@EventBusSubscriber()
public class WakeUpEvents {
	@SubscribeEvent
	public static void onPlayerWakeUp(PlayerWakeUpEvent event) {

		if (event.getEntity().level() instanceof ServerLevel sl) {
			if (sl.getDayTime() % 24000 < 40) {
				event.getEntity().heal((float) MyConfig.getWakeupHealingAmount());
			}
		}
	}
}
