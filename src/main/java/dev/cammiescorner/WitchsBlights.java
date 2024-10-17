package dev.cammiescorner;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import dev.cammiescorner.api.Transformation;
import dev.cammiescorner.common.Utils;
import dev.cammiescorner.common.components.RespawnableEffectsComponent;
import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.entities.BeastEntity;
import dev.cammiescorner.common.entities.VampireBeastEntity;
import dev.cammiescorner.common.registries.*;
import dev.upcraft.sparkweave.api.platform.services.RegistryService;
import dev.upcraft.sparkweave.api.scheduler.Tasks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.gen.GenerationStep;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WitchsBlights implements ModInitializer {
	public static final String MOD_ID = "witchsblights";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<Registry<Transformation>> TRANSFORMATIONS_KEY = RegistryKey.ofRegistry(id("transformation"));
	public static final DefaultedRegistry<Transformation> TRANSFORMATIONS = FabricRegistryBuilder.createDefaulted(TRANSFORMATIONS_KEY, id("none")).buildAndRegister();

	public static final Configurator CONFIGURATOR = new Configurator(MOD_ID);

	@Override
	public void onInitialize() {
		CONFIGURATOR.register(ModConfig.class);
		RegistryService registryService = RegistryService.get();

		ModComponentTypes.COMPONENTS.accept(registryService);
		ModSoundEvents.SOUND_EVENTS.accept(registryService);
		ModBlocks.BLOCKS.accept(registryService);
		ModItems.ITEMS.accept(registryService);
		ModEntities.ENTITIES.accept(registryService);
		ModStatusEffects.STATUS_EFFECTS.accept(registryService);
		ModPotions.POTIONS.accept(registryService);
		ModRecipes.RECIPE_SERIALIZERS.accept(registryService);
		ModWorldFeatures.FEATURES.accept(registryService);
		ModTransformations.TRANSFORMATIONS.accept(registryService);
		ModParticles.PARTICLE_TYPES.accept(registryService);

		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(RegistryKeys.PLACED_FEATURE, id("mistletoe")));

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 5, factories -> factories.add(new TradeOffers.Factory() {
			@Nullable
			@Override
			public TradeOffer create(Entity entity, Random random) {
				return new TradeOffers.SellItemFactory(ModItems.ROSARY.get(), 32, 1, 30).create(entity, random);
			}
		}));

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			if(component.getTransformation().isIn(ModTags.DRINKS_BLOOD) && player.isSneaking()) {
				if(!entity.getType().isIn(component.getTransformation().getTargets()))
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600));

				player.getHungerManager().add(2, 0.2f);

				return ActionResult.success(true);
			}

			return ActionResult.PASS;
		});

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			Entity attacker = damageSource.getAttacker();

			if(damageSource.isOf(ModDamageTypes.BLOOD_DRAINED) && attacker instanceof VampireBeastEntity)
				entity.addStatusEffect(Utils.SANGUINE_BLIGHT_I);

			if(entity instanceof ServerPlayerEntity player) {
				RespawnableEffectsComponent incurableEffectsComponent = player.getComponent(ModComponents.RESPAWNABLE_EFFECTS);

				incurableEffectsComponent.clearStatusEffects();

				for(StatusEffectInstance statusEffect : player.getStatusEffects())
					incurableEffectsComponent.addStatusEffect(statusEffect);
			}
		});

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			if(!alive) {
				RespawnableEffectsComponent component = newPlayer.getComponent(ModComponents.RESPAWNABLE_EFFECTS);

				for(StatusEffectInstance statusEffect : component.getEffects())
					newPlayer.addStatusEffect(statusEffect);

				component.clearStatusEffects();
			}
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Tasks.scheduleEphemeral(() -> {
			ServerPlayerEntity player = handler.getPlayer();
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			if(component.isTransformed())
				component.getTransformation().transform(player, component.getTarget());
			else
				component.stopUrging();

			component.unpause();
		}, 3));

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			ServerWorld world = player.getServerWorld();
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			if(component.isTransformed()) {
				List<? extends BeastEntity> beasts = world.getEntitiesByType(TypeFilter.instanceOf(BeastEntity.class), beastEntity -> beastEntity.getOwner() == player);

				for(BeastEntity beast : beasts)
					beast.remove(Entity.RemovalReason.DISCARDED);
			}

			component.pause();
		});

		EntitySleepEvents.ALLOW_SLEEP_TIME.register((player, sleepingPos, vanillaResult) -> {
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			if(component.getTransformation().isIn(ModTags.NOCTURNAL) && !vanillaResult)
				return ActionResult.success(true);

			return ActionResult.PASS;
		});

		EntitySleepEvents.ALLOW_SLEEPING.register((player, sleepingPos) -> {
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			if(component.getTransformation().isIn(ModTags.NOCTURNAL) && !player.getWorld().isDay()) {
				player.sendMessage(Text.translatable("block.witchsblights.bed.is_not_day"), true);
				return PlayerEntity.SleepFailureReason.OTHER_PROBLEM;
			}

			return null;
		});
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}