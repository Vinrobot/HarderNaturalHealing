package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber()
public class PlayerDeathHandler {

	@SubscribeEvent
	public static void onPlayerDeath(PlayerEvent.Clone event) {
		Player p = event.getEntity();
		int v = MyConfig.getHealthAfterDeath();
		if (event.isWasDeath()) {
			if (MyConfig.getHealthAfterDeath() < 20) {
				p.setHealth(MyConfig.getHealthAfterDeath());
			}
			if (MyConfig.getHungerAfterDeath() < 20) {
				p.getFoodData().setFoodLevel((MyConfig.getHungerAfterDeath()));
				p.getFoodData().setSaturation(0);
				p.getFoodData().setExhaustion(3.9f);

			}
		}
	}
}
