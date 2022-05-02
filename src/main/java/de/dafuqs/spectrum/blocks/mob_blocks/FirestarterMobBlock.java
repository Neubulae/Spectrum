package de.dafuqs.spectrum.blocks.mob_blocks;

import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestarterMobBlock extends MobBlock {
	
	// Block: The Block to freeze
	// BlockState: The BlockState when Block is getting frozen
	// Float: The chance to freeze
	public static final Map<Block, Pair<BlockState, Float>> BURNING_MAP = new HashMap<>() {{
		put(Blocks.NETHERRACK, new Pair<>(Blocks.MAGMA_BLOCK.getDefaultState(), 0.25F));
		put(Blocks.MAGMA_BLOCK, new Pair<>(Blocks.LAVA.getDefaultState(), 0.5F));
	}};
	
	public FirestarterMobBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean trigger(ServerWorld world, BlockPos blockPos, BlockState state, @Nullable Entity entity, Direction side) {
		for(Direction direction : Direction.values()) {
			causeFire(world, blockPos.offset(direction), direction);
		}
		return true;
	}
	
	public static void causeFire(@NotNull ServerWorld world, BlockPos blockPos, Direction side) {
		BlockState blockState = world.getBlockState(blockPos);
		if (CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)) {
			// light lightable blocks
			world.playSound(null, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			world.setBlockState(blockPos, blockState.with(Properties.LIT, true), 11);
			world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
		} else if(blockState.isIn(BlockTags.ICE)) {
			// smelt ice
			world.playSound(null, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
			world.setBlockState(blockPos, Blocks.WATER.getDefaultState());
		} else if(BURNING_MAP.containsKey(blockState.getBlock())) {
			Pair<BlockState, Float> dest = BURNING_MAP.get(blockState.getBlock());
			if(dest.getRight() >= 1.0F || world.random.nextFloat() < dest.getRight()) {
				// convert netherrack to magma blocks
				world.setBlockState(blockPos, dest.getLeft(), 11);
				world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
			}
		} else {
			// place fire
			if (AbstractFireBlock.canPlaceAt(world, blockPos, side)) {
				world.playSound(null, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
				BlockState blockState2 = AbstractFireBlock.getState(world, blockPos);
				world.setBlockState(blockPos, blockState2, 11);
				world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
			}
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		tooltip.add(new TranslatableText( "block.spectrum.firestarter_mob_block.tooltip"));
	}
	
}