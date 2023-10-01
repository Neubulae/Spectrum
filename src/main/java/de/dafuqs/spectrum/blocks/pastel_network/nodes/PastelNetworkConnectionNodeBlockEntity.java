package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import de.dafuqs.spectrum.registries.SpectrumBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class PastelNetworkConnectionNodeBlockEntity extends PastelNetworkNodeBlockEntity {
	
	public PastelNetworkConnectionNodeBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(SpectrumBlockEntities.CONNECTION_NODE, blockPos, blockState);
	}
	
}