package dev.cammiescorner.common.world.features;

import dev.cammiescorner.common.blocks.MistletoeBlock;
import dev.cammiescorner.common.registries.ModBlocks;
import dev.cammiescorner.common.world.features.configs.MistletoeFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class MistletoeFeature extends Feature<MistletoeFeatureConfig> {
	public MistletoeFeature() {
		super(MistletoeFeatureConfig.CODEC);
	}

	@Override
	public boolean generate(FeatureContext<MistletoeFeatureConfig> context) {
		StructureWorldAccess access = context.getWorld();
		BlockPos blockPos = context.getOrigin();

		return generateMistletoe(access, blockPos, context);
	}

	public boolean generateMistletoe(StructureWorldAccess access, BlockPos blockPos, FeatureContext<MistletoeFeatureConfig> context) {
		if(access.isAir(blockPos)) {
			for(Direction direction : Direction.values()) {
				BlockPos offsetPos = blockPos.offset(direction);

				if(direction.getAxis().isHorizontal() && testBlockState(access, offsetPos, context) && MistletoeBlock.shouldConnectTo(access, offsetPos, direction)) {
					BlockPos.Mutable blockPosCopy = blockPos.mutableCopy();

					for(int y = 0; y < access.getRandom().nextBetween(2, 4); y++) {
						if(access.isAir(blockPosCopy)) {
							access.setBlockState(blockPosCopy, ModBlocks.MISTLETOE.get().getDefaultState().with(MistletoeBlock.getFacingProperty(direction), true), Block.NOTIFY_LISTENERS);
							blockPosCopy.move(Direction.DOWN);
						}
						else
							break;
					}

					offsetPos = offsetPos.offset(direction.getAxis() == Direction.Axis.Z ? Direction.WEST : Direction.NORTH);

					if(access.getRandom().nextFloat() < 0.8 && testBlockState(access, offsetPos, context) && MistletoeBlock.shouldConnectTo(access, offsetPos, direction)) {
						blockPosCopy = blockPos.offset(direction.getAxis() == Direction.Axis.Z ? Direction.WEST : Direction.NORTH).mutableCopy();

						for(int y = 0; y < access.getRandom().nextBetween(2, 4); y++) {
							if(access.isAir(blockPosCopy)) {
								access.setBlockState(blockPosCopy, ModBlocks.MISTLETOE.get().getDefaultState().with(MistletoeBlock.getFacingProperty(direction), true), Block.NOTIFY_LISTENERS);
								blockPosCopy.move(Direction.DOWN);
							}
							else
								break;
						}
					}

					offsetPos = offsetPos.offset(direction.getAxis() == Direction.Axis.Z ? Direction.EAST : Direction.SOUTH, 2);

					if(access.getRandom().nextFloat() < 0.8 && testBlockState(access, offsetPos, context) && MistletoeBlock.shouldConnectTo(access, offsetPos, direction)) {
						blockPosCopy = blockPos.offset(direction.getAxis() == Direction.Axis.Z ? Direction.EAST : Direction.SOUTH).mutableCopy();

						for(int y = 0; y < access.getRandom().nextBetween(2, 4); y++) {
							if(access.isAir(blockPosCopy)) {
								access.setBlockState(blockPosCopy, ModBlocks.MISTLETOE.get().getDefaultState().with(MistletoeBlock.getFacingProperty(direction), true), Block.NOTIFY_LISTENERS);
								blockPosCopy.move(Direction.DOWN);
							}
							else
								break;
						}
					}

					return true;
				}
			}
		}

		return false;
	}

	public boolean testBlockState(StructureWorldAccess access, BlockPos pos, FeatureContext<MistletoeFeatureConfig> context) {
		return access.testBlockState(pos, blockState -> blockState.isIn(context.getConfig().validBlocks()));
	}
}
