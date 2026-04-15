package io.github.ryuPC0.cjptt.mixin;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsHandler;
import com.simibubi.create.content.trains.TrainHUD;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TrainHUD.class,remap = false)
public class TrainHUDMixin {
    @Shadow
    private static Carriage getCarriage(){
        AbstractContraptionEntity var1 = ControlsHandler.getContraption();
        if (var1 instanceof CarriageContraptionEntity cce) {
            return cce.getCarriage();
        } else {
            return null;
        }
    }
    @Inject(method = "renderOverlay",at = @At(value = "INVOKE",target = "Lcom/simibubi/create/foundation/gui/AllGuiTextures;render(Lnet/minecraft/client/gui/GuiGraphics;II)V",ordinal = 4))
    private static void Cjptt$renderOverlaySpeedText(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height, CallbackInfo ci)
    {
        Train train = getCarriage().train;
        double displayspeed = (double) ((int)(train.speed * 720))/10;
        displayspeed = displayspeed < 0 ? -displayspeed : displayspeed;
        graphics.drawString(Minecraft.getInstance().font, displayspeed + "km/h",1,-7,0xFFFFFF);
    }
}
