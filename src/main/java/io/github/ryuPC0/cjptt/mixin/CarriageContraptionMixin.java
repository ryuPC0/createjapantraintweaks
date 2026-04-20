package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.math.Axis;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlsBlock;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.github.ryuPC0.cjptt.Cjptt.LOGGER;

@Mixin(value = CarriageContraption.class,remap = false)
public abstract class CarriageContraptionMixin extends Contraption {
    @Shadow private boolean forwardControls;
    @Shadow private boolean backwardControls;
    @Shadow private Direction assemblyDirection;
    @Unique public Map<BlockPos,Direction> cjptt$extendedconductorSeats;
    @Inject(method = "<init>()V",at = @At(value = "TAIL"))
    void Cjptt$Initializationvariable(CallbackInfo ci){
        this.cjptt$extendedconductorSeats = new HashMap<>();
    }
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

    @Inject(method = "assemble",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/entity/CarriageContraption;inControl(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z",ordinal = 1))
    //@Inject(method = "assemble",at = @At(value = "RETURN",ordinal = 3))
    void Cjptt$assembleextconductorseat(Level world, BlockPos pos, CallbackInfoReturnable<Boolean> cir,@Local(ordinal = 1)BlockPos seatpos,@Local(ordinal = 0)Direction direction){
        //for(BlockPos seatPos : this.getSeats()) {
        //    for(Direction direction : Iterate.directionsInAxis(this.assemblyDirection.getAxis())) {
                BlockPos controlPos = seatpos.relative(direction);
                if(blocks.containsKey(controlPos)) {
                    BlockState blockState = blocks.get(controlPos).state();
                    if (Cjptt.EXTENDED_TRAIN_CONTROLS.has(blockState)) {
                        if(blockState.getValue(ExtendedControlsBlock.FACING) == direction.getOpposite()) {
                            Direction direction1 = assemblyDirection.getClockWise(direction.getAxis());
                            LOGGER.info("{}",direction1);
                            cjptt$extendedconductorSeats.put(seatpos, assemblyDirection.getClockWise(direction.getAxis()));
                        }
                    }
                }
        //    }
        //}
    }
    @Inject(method = "writeNBT",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/entity/ArrivalSoundQueue;serialize(Lnet/minecraft/nbt/CompoundTag;)V"))
    void Cjptt$writeNBTextconseats(boolean spawnPacket, CallbackInfoReturnable<CompoundTag> cir,@Local CompoundTag tag){
        ListTag listTag = NBTHelper.writeCompoundList(cjptt$extendedconductorSeats.entrySet(),(e)->{
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put("Pos", NbtUtils.writeBlockPos(e.getKey()));
            NBTHelper.writeEnum(compoundTag,"Dir", e.getValue());
            return compoundTag;
        });
        tag.put("extendedconductorseats",listTag);
    }
    @Inject(method = "readNBT",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/entity/ArrivalSoundQueue;deserialize(Lnet/minecraft/nbt/CompoundTag;)V"))
    void Cjptt$readNBTextconseats(Level world, CompoundTag nbt, boolean spawnData, CallbackInfo ci){
        NBTHelper.iterateCompoundList(nbt.getList("extendedconductorseats",10),(c)->cjptt$extendedconductorSeats.put(NbtUtils.readBlockPos(c.getCompound("Pos")),NBTHelper.readEnum(c,"Dir", Direction.class)));
    }
}
