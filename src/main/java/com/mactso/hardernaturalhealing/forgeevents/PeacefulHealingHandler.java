package com.mactso.hardernaturalhealing.forgeevents;

import com.mactso.hardernaturalhealing.config.MyConfig;
import com.mactso.hardernaturalhealing.utility.Utility;
import net.minecraft.world.Difficulty;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.coremod.api.ASMAPI;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

@EventBusSubscriber()
public class PeacefulHealingHandler {

	static float cSat = 0;
	static float cExt = 0;
	static int cFod = 0;
	static int cTim = 0;

	static float sSat = 0;
	static float sExt = 0;
	static int sFod = 0;
	static int sTim = 0;

	static boolean cRegen;
	static boolean sRegen;

	private static Field tickTimer = null;
	private static final Logger LOGGER = LogManager.getLogger();
	static boolean timerAvailable = false;

	// FD: net/minecraft/world/food/FoodData/f_38699_
	// net/minecraft/world/food/FoodData/tickTimer
	static {
		// possible ticktimer line goes here.
		try {
			String name = ASMAPI.mapField("f_38699_");
			tickTimer = FoodData.class.getDeclaredField(name);
			tickTimer.setAccessible(true);
			timerAvailable = true;
		} catch (Exception e) {
			LOGGER.error("XXX Unexpected Reflection Failure balanceBiomeSpawnValues");
		}
	}

	@SubscribeEvent
	public static void onPlayerHealing(PlayerTickEvent.Pre event) {
		onPlayerHealing(event);
	}

	@SubscribeEvent
	public static void onPlayerHealing(PlayerTickEvent.Post event) {
		onPlayerHealing(event);
	}

	public static void onPlayerHealing(PlayerTickEvent event) {

		if (MyConfig.isPeacefulHunger()) {
			FoodData fs = event.getEntity().getFoodData();
			Difficulty difficulty = event.getEntity().level().getDifficulty();
			if (event instanceof PlayerTickEvent.Pre) {
				MyConfig.setDebugLevel(0);
				if (event.getEntity().level().isClientSide()) {
					cRegen = event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
					cSat = fs.getSaturationLevel();
					cExt = fs.getExhaustionLevel();
					cFod = fs.getFoodLevel();
//					cTim = fs.getTickTimer();
					try {
						cTim = (int) tickTimer.get(fs);
					} catch (IllegalArgumentException e) {
						LOGGER.error("Illegal Argument: failed to get client FoodData tickTimer.");
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						LOGGER.error("Illegal Access: failed to get client FoodData tickTimer.");
						e.printStackTrace();
					}
					Utility.debugMsg(2, "(" + event.getEntity().tickCount + ") C START cTim:" + cTim + " cSat:" + cSat
							+ " cExt:" + cExt + " cFod:" + cFod + ".");
				} else {
					sRegen = event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
					sSat = fs.getSaturationLevel();
					sExt = fs.getExhaustionLevel();
					sFod = fs.getFoodLevel();
//					sTim = fs.tickTimer;
					try {
						sTim = (int) tickTimer.get(fs);
					} catch (IllegalArgumentException e) {
						LOGGER.error("Illegal Argument: failed to get server FoodData tickTimer.");
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						LOGGER.error("Illegal Access: failed to get server FoodData tickTimer.");
						e.printStackTrace();
					}

					Utility.debugMsg(2, "(" + event.getEntity().tickCount + ") S START sTim:" + sTim + " sSat:" + sSat
							+ " sExt:" + sExt + " sFod:" + sFod + ".");
				}
			}

			if (event instanceof PlayerTickEvent.Post) {
				if (difficulty == Difficulty.PEACEFUL) {
					MyConfig.setDebugLevel(0);
					if (event.getEntity().level().isClientSide()) {
						Utility.debugMsg(2, "(" + event.getEntity().tickCount + ") C xENDx cTim:" + cTim + " cSat:" + cSat
								+ " cExt:" + cExt + " cFod:" + cFod + ".");
						fs.setFoodLevel(cFod);
					} else {
						Utility.debugMsg(2, "(" + event.getEntity().tickCount + ") S xENDx sTim:" + sTim + " sSat:" + sSat
								+ " sExt:" + sExt + " sFod:" + sFod + ".");
//						fs.foodLevel = sFod;
						if ((sExt > fs.getExhaustionLevel()) && (sSat == 0) && (fs.getFoodLevel() > 0)) {
							fs.setFoodLevel(fs.getFoodLevel() - 1);
						}
						if (fs.getFoodLevel() == 0) {
							if (++sTim > 80) {
								if (event.getEntity().getHealth() > MyConfig.getMinimumStarvationHealth()) {
									event.getEntity().hurt(event.getEntity().damageSources().starve(), 1.0F);
								}
								sTim = 0;
							}
						}
//						fs.tickTimer = sTim;
						try {
							tickTimer.setInt(fs, sTim);
						} catch (IllegalArgumentException e) {
							LOGGER.error("Illegal Argument: failed to update FoodData tickTimer.");
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							LOGGER.error("Illegal Access: failed to update FoodData tickTimer.");
							e.printStackTrace();
						}
					}
				}

			}

		}
	}
}
