package de.dafuqs.spectrum.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class UnlockedRecipeToast implements Toast {

    private final Identifier TEXTURE = new Identifier(SpectrumCommon.MOD_ID, "textures/gui/toasts.png");
    private final ItemStack itemStack;
    private final SoundEvent soundEvent = SpectrumSoundEvents.NEW_RECIPE;
    private boolean soundPlayed;

    public UnlockedRecipeToast(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.soundPlayed = false;
    }

    public static void showRecipeToast(MinecraftClient client, ItemStack itemStack) {
        client.getToastManager().add(new UnlockedRecipeToast(itemStack));
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        Text title = new TranslatableText("spectrum.toast.recipe_unlocked.title");
        Text text = this.itemStack.getName();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        manager.drawTexture(matrices, 0, 0, 0, 32, this.getWidth(), this.getHeight());

        manager.getGame().textRenderer.draw(matrices, title, 30.0F, 7.0F, 2105376); // => #2105376: dark gray
        manager.getGame().textRenderer.draw(matrices, text, 30.0F, 18.0F, 0);

        if (!this.soundPlayed && startTime > 0L) {
            this.soundPlayed = true;
            if(this.soundEvent != null) {
                manager.getGame().getSoundManager().play(PositionedSoundInstance.master(this.soundEvent, 1.0F, 1.0F));
            }
        }

        manager.getGame().getItemRenderer().renderInGui(itemStack, 8, 8);
        return startTime >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }

}
