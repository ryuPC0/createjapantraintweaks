package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import io.github.ryuPC0.cjptt.Cjptt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = CarriageContraption.class,remap = false)
public class CarriageContraptionMixin {
    @Shadow private boolean sidewaysControls;

    @Shadow private boolean forwardControls;

    @Shadow private boolean backwardControls;

    @Inject(method = "capture",at = @At(value = "TAIL"))
    void Cjptt$captureExtendedControls(Level world, BlockPos pos, CallbackInfoReturnable<Pair<StructureTemplate.StructureBlockInfo, BlockEntity>> cir,@Local BlockState blockstate)
    {
        if(Cjptt.EXTENDED_TRAIN_CONTROLS.has(blockstate)){
            forwardControls = true;
            backwardControls = true;
        }
    }
}
