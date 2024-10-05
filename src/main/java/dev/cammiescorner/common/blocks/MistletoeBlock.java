package dev.cammiescorner.common.blocks;

import net.minecraft.block.MapColor;
import net.minecraft.block.VineBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;

public class MistletoeBlock extends VineBlock {
	public MistletoeBlock(Settings settings) {
		super(settings.mapColor(MapColor.DARK_GREEN)
				.replaceable()
				.noCollision()
				.strength(0.2f)
				.sounds(BlockSoundGroup.VINE)
				.burnable()
				.pistonBehavior(PistonBehavior.DESTROY)
		);
	}
}
