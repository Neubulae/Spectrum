package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreezingMobBlock extends MobBlock {
	
	// Block: The Block to freeze
	// BlockState: The BlockState when Block is getting frozen
	// Float: The chance to freeze
	public static final Map<Block, Pair<BlockState, Float>> FREEZING_MAP = new HashMap<>() {{
		put(Blocks.SNOW, new Pair<>(Blocks.POWDER_SNOW.getDefaultState(), 0.25F));
		put(Blocks.POWDER_SNOW, new Pair<>(Blocks.SNOW_BLOCK.getDefaultState(), 0.5F));
		put(Blocks.WATER, new Pair<>(Blocks.ICE.getDefaultState(), 1.0F));
		put(Blocks.ICE, new Pair<>(Blocks.PACKED_ICE.getDefaultState(), 0.25F));
		put(Blocks.PACKED_ICE, new Pair<>(Blocks.BLUE_ICE.getDefaultState(), 0.1F));
	}};
	
	public FreezingMobBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		for(Direction direction : Direction.values()) {
			freeze(world, blockPos.offset(direction));
		}
		return true;
	}
	
	public static boolean freeze(@NotNull ServerWorld world, BlockPos blockPos) {
		BlockState sourceState = world.getBlockState(blockPos);
		if(FREEZING_MAP.containsKey(sourceState.getBlock())) {
			Pair<BlockState, Float> recipe = FREEZING_MAP.get(sourceState.getBlock());
			if(recipe.getRight() >= 1.0F || world.random.nextFloat() < recipe.getRight()) {
				// freeze
				world.setBlockState(blockPos, recipe.getLeft());
				world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(recipe.getLeft())); // processed in WorldRenderer processGlobalEvent()
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.freezing_mob_block.tooltip"));
	}
	
}