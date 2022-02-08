package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PigmentPaletteItem extends SpectrumTrinketItem {
	
	private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "midgame/spectrum_midgame");
	
	public PigmentPaletteItem(Settings settings) {
		super(settings);
	}
	
	@Override
	protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.pigment_palette.tooltip"));
	}
	
}