package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsHandler;
import com.simibubi.create.content.trains.TrainHUD;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.Config;
import io.github.ryuPC0.cjptt.mixininterface.TrainMixinInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.UUIDUtil;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Inject(method = "renderOverlay",at = @At(value = "INVOKE",target = "Lcom/simibubi/create/foundation/gui/AllGuiTextures;render(Lnet/minecraft/client/gui/GuiGraphics;II)V",ordinal = 4),cancellable = true)
    private static void Cjptt$renderOverlaySpeedText(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height, CallbackInfo ci, @Local PoseStack poseStack, @Local Carriage carriage)
    {
        Train train = carriage.train;
        boolean isextended = false;
        if(Create.RAILWAYS.trains.get(train.id) instanceof TrainMixinInterface ext)
           isextended = ext.cjptt$getisextendedtrain();
        if(isextended || Config.speedtxtrendnotext) {
            double displayspeed = (double) ((int) (train.speed * 720)) / 10;
            displayspeed = displayspeed < 0 ? -displayspeed : displayspeed;
            graphics.drawString(Minecraft.getInstance().font, displayspeed + "km/h", 1, -7, 0xFFFFFF);
        }
        if(isextended) {
            poseStack.popPose();
            ci.cancel();
        }
    }
}
