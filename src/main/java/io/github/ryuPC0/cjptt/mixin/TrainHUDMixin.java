package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsHandler;
import com.simibubi.create.content.trains.TrainHUD;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import io.github.ryuPC0.cjptt.Config;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlsHandler;
import io.github.ryuPC0.cjptt.mixininterface.TrainMixinInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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
    @ModifyExpressionValue(method = "renderOverlay",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/actors/trainControls/ControlsHandler;getContraption()Lcom/simibubi/create/content/contraptions/AbstractContraptionEntity;"))
    private static AbstractContraptionEntity Cjptt$renderOverlaygetext(AbstractContraptionEntity original){
        return original == null ? ExtendedControlsHandler.entityref.get() : original;
    }
    @ModifyExpressionValue(method = "renderOverlay",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/contraptions/actors/trainControls/ControlsHandler;getControlsPos()Lnet/minecraft/core/BlockPos;"))
    private static BlockPos Cjptt$renderOverlaygetconpos(BlockPos original){
        return original == null ? ExtendedControlsHandler.controlsPos : original;
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
    @ModifyArg(method = "tick",at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/animation/LerpedFloat;chase(DDLnet/createmod/catnip/animation/LerpedFloat$Chaser;)Lnet/createmod/catnip/animation/LerpedFloat;",ordinal = 1),index = 0)
    private static double Cjptt$tickspeedcheseaccurate(double value,@Local(ordinal = 0) double basevalue){
        return basevalue;
    }
    @ModifyArg(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(DDD)D",ordinal = 0),index = 0)
    private static double Cjptt$tickspeedaccuratefix(double pValue){
        return pValue - 0.05;
    }

}
