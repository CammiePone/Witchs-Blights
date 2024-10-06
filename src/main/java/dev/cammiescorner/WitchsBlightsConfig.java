package dev.cammiescorner;

import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;

@ConfigInfo(
		title = "Witch's Blights",
		description = "wa.",
		links = {
				@ConfigInfo.Link(
						value = "https://modrinth.com/mod/witchs-blights",
						icon = "modrinth",
						text = "Modrinth"
				),
				@ConfigInfo.Link(
						value = "https://www.curseforge.com/minecraft/mc-mods/witchs-blights",
						icon = "curseforge",
						text = "Curseforge"
				),
				@ConfigInfo.Link(
						value = "https://github.com/CammiePone/Witchs-Blights",
						icon = "github",
						text = "Github"
				),
				@ConfigInfo.Link(
						value = "https://cammiescorner.dev/discord",
						icon = "gamepad-2",
						text = "Discord"
				)
		}
)
@Config(WitchsBlights.MOD_ID)
public class WitchsBlightsConfig {
	@ConfigEntry(
			type = EntryType.INTEGER,
			id = "untransformIfNoTargetTicks",
			translation = "witchsblights.config.untransformIfNoTargetTicks.name"
	)
	public static int untransformIfNoTargetTicks = 100;

	@ConfigEntry(
			type = EntryType.FLOAT,
			id = "urgingLookStrength",
			translation = "witchsblights.config.urgingLookStrength.name"
	)
	@ConfigOption.Range(min = 0f, max = 100f)
	public static float urgingLookStrength = 1f;

	@ConfigEntry(
			type = EntryType.INTEGER,
			id = "baseVampireUrgingTicks",
			translation = "witchsblights.config.baseVampireUrgingTicks.name"
	)
	public static int baseVampireUrgingTicks = 400;

	@ConfigEntry(
			type = EntryType.DOUBLE,
			id = "baseVampireStageUrgingModifier",
			translation = "witchsblights.config.baseVampireStageUrgingModifier.name"
	)
	public static double baseVampireStageUrgingModifier = 0.25;

	@ConfigEntry(
			type = EntryType.DOUBLE,
			id = "baseVampireBabyUrgingModifier",
			translation = "witchsblights.config.baseVampireBabyUrgingModifier.name"
	)
	public static double baseVampireBabyUrgingModifier = 0.5;

	@ConfigEntry(
			type = EntryType.DOUBLE,
			id = "vampireUrgingRange",
			translation = "witchsblights.config.vampireUrgingRange.name"
	)
	public static double vampireUrgingRange = 8;

	@ConfigEntry(
			type = EntryType.INTEGER,
			id = "baseWerewolfUrgingTicks",
			translation = "witchsblights.config.baseWerewolfUrgingTicks.name"
	)
	public static int baseWerewolfUrgingTicks = 400;

	@ConfigEntry(
			type = EntryType.DOUBLE,
			id = "baseWerewolfStageUrgingModifier",
			translation = "witchsblights.config.baseWerewolfStageUrgingModifier.name"
	)
	public static double baseWerewolfStageUrgingModifier = 0.25;

	@ConfigEntry(
			type = EntryType.DOUBLE,
			id = "baseWerewolfBabyUrgingModifier",
			translation = "witchsblights.config.baseWerewolfBabyUrgingModifier.name"
	)
	public static double baseWerewolfBabyUrgingModifier = 0.5;

	@ConfigEntry(
			type = EntryType.DOUBLE,
			id = "werewolfUrgingRange",
			translation = "witchsblights.config.werewolfUrgingRange.name"
	)
	public static double werewolfUrgingRange = 8;
}
