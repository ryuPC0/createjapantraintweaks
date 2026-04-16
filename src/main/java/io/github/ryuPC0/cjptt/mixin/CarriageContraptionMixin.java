package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import io.github.ryuPC0.cjptt.Cjptt;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

@Mixin(value = CarriageContraption.class,remap = false)
public abstract class CarriageContraptionMixin extends Contraption {
    @Shadow private boolean forwardControls;
    @Shadow private boolean backwardControls;
    @Shadow private Direction assemblyDirection;
    @Unique public Set<BlockPos> cjptt$extendedconductorSeats;
    @Inject(method = "capture",at = @At(value = "TAIL"))
    void Cjptt$captureExtendedControls(Level world, BlockPos pos, CallbackInfoReturnable<Pair<StructureTemplate.StructureBlockInfo, BlockEntity>> cir,@Local BlockState blockstate)
    {
        if(Cjptt.EXTENDED_TRAIN_CONTROLS.has(blockstate)){
            forwardControls = true;
            backwardControls = true;
        }
    }
    @Inject(method = "inControl",at = @At(value = "HEAD"), cancellable = true)
    void Cjptt$inControlExtendedControls(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir){
        if(Cjptt.EXTENDED_TRAIN_CONTROLS.has(blocks.get(pos).state())){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "assemble",at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/data/Iterate;directionsInAxis(Lnet/minecraft/core/Direction$Axis;)[Lnet/minecraft/core/Direction;",ordinal = 1,shift = At.Shift.AFTER))
    void Cjptt$assembleextconductorseat(Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir){
        for(BlockPos seatPos : this.getSeats()) {
            for(Direction direction : Iterate.directionsInAxis(this.assemblyDirection.getAxis())) {
                BlockPos controlPos = seatPos.relative(direction);
                if(blocks.containsKey(controlPos))
                    if(Cjptt.EXTENDED_TRAIN_CONTROLS.has(blocks.get(controlPos).state())){
                        cjptt$extendedconductorSeats.add(seatPos);
                    }
            }
        }

    }
}
