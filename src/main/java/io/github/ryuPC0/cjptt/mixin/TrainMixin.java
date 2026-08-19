package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.mixininterface.CarriageContraptionMixinInterface;
import io.github.ryuPC0.cjptt.mixin.accessor.AbstractContraptionEntityAccessor;
import io.github.ryuPC0.cjptt.mixin.accessor.CarriageAccessor;
import io.github.ryuPC0.cjptt.mixininterface.TrainMixinInterface;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSign;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.AbstractLimitRule;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSign;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@SuppressWarnings("ConstantValue")
@Mixin(value = Train.class,remap = false)
public abstract class TrainMixin implements TrainMixinInterface {
    @Shadow public double throttle;
    @Shadow public List<Carriage> carriages;
    @Shadow public Map<UUID, Pair<Integer, Boolean>> cachedObserverFiltering;

    @Shadow public abstract float maxSpeed();

    @Unique public boolean cjptt$extendedtrain;
    @Unique public List<Couple<Double>> cjptt$speedlimitlist;
    @Override
    public boolean cjptt$getisextendedtrain(){
        return cjptt$extendedtrain;
    }
    @Override
    public void cjptt$setisextendedtrain(boolean value){
        cjptt$extendedtrain = value;
    }
    @Override
    public List<Couple<Double>> cjptt$getspeedlimit(){
        return cjptt$speedlimitlist;
    }
    @Inject(method = "<init>",at = @At("TAIL"))
    void Cjptt$inituniquever(UUID id, UUID owner, TrackGraph graph, List<Carriage> carriages, List<Integer> carriageSpacing, boolean doubleEnded, CallbackInfo ci){
        cjptt$speedlimitlist = new ArrayList<>();
    }
    @Inject(method = "lambda$frontSignalListener$6",at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/data/Pair;getFirst()Ljava/lang/Object;",ordinal = 2),cancellable = true)
    void Cjptt$frontsignallistenerThrottle(CallbackInfoReturnable<TravellingPoint.IEdgePointListener> cir, @Local(argsOnly = true) Pair<TrackEdgePoint, Couple<TrackNode>> couple)
    {
        Object patt13732$temp = couple.getFirst();
        if (patt13732$temp instanceof SpeedSign speedSign) {
            if (speedSign.isPrimary(couple.getSecond().getSecond())) {
                //*
                FilterItemStack filter = speedSign.getFilter();
                if (filter.isEmpty()) {
                    throttle = speedSign.Getthrottle();
                    cir.cancel();
                } else {
                    UUID uuid = speedSign.id;
                    int storageVersion = 0;
                    ResourceKey<Level> resourcekey = ((SpeedSign) patt13732$temp).getBlockEntityDimension();
                    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                    Level level = server.getLevel(resourcekey);
                    for (Carriage carriage : carriages)
                        storageVersion += carriage.storage.getVersion();
                    Pair<Integer, Boolean> cachedMatch = cachedObserverFiltering.computeIfAbsent(uuid, $ -> Pair.of(-1, false));
                    boolean shouldActivate = cachedMatch.getSecond();

                    if (cachedMatch.getFirst() == storageVersion) {
                        if (shouldActivate)
                            throttle = speedSign.Getthrottle();
                        cir.cancel();
                    } else {
                        shouldActivate = false;
                        for (Carriage carriage : carriages) {
                            if (shouldActivate)
                                break;

                            IItemHandlerModifiable inv = carriage.storage.getAllItems();
                            if (inv != null) {
                                for (int slot = 0; slot < inv.getSlots(); slot++) {
                                    if (shouldActivate)
                                        break;
                                    ItemStack extractItem = inv.extractItem(slot, 1, true);
                                    if (extractItem.isEmpty())
                                        continue;
                                    shouldActivate |= filter.test(level, extractItem);
                                }
                            }

                            IFluidHandler tank = carriage.storage.getFluids();
                            if (tank != null) {
                                for (int slot = 0; slot < tank.getTanks(); slot++) {
                                    if (shouldActivate)
                                        break;
                                    FluidStack drain = tank.drain(1, IFluidHandler.FluidAction.SIMULATE);
                                    if (drain.isEmpty())
                                        continue;
                                    shouldActivate |= filter.test(level, drain);
                                }
                            }
                        }

                        cachedObserverFiltering.put(uuid, Pair.of(storageVersion, shouldActivate));

                        if (shouldActivate)
                            throttle = speedSign.Getthrottle();
                    }
                }
                //*/
            }
        } else if (patt13732$temp instanceof AdvancedSpeedSign advancedspeedsign)  {
            AbstractLimitRule rule = advancedspeedsign.speed.get(advancedspeedsign.isPrimary(couple.getSecond().getSecond()));
            if(rule != null) {
                rule.ApplyLimit(this);
                cjptt$applythrottle();
            }
        }
    }
    @Unique
    private void cjptt$applythrottle(){
        MutableDouble maxthrottle = new MutableDouble(1.0);
        cjptt$speedlimitlist.forEach((item)-> maxthrottle.setValue(Math.min(item.getFirst() / 20 / maxSpeed(),maxthrottle.getValue())));
        throttle = maxthrottle.getValue();
    }
    @Inject(method = "lambda$backSignalListener$10",at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/data/Pair;getFirst()Ljava/lang/Object;",ordinal = 1),cancellable = true)
    void Cjptt$backsignallistener(Double distance, Pair<TrackEdgePoint,Couple<TrackNode>> couple, CallbackInfoReturnable<Boolean> cir){
        TrackEdgePoint edgePoint = couple.getFirst();
        if(edgePoint instanceof AdvancedSpeedSign advancedSpeedSign){
            AbstractLimitRule rule = advancedSpeedSign.speed.get(advancedSpeedSign.isPrimary(couple.getSecond().getSecond()));
            if(rule != null) {
                rule.ApplyBack(this);
                cjptt$applythrottle();
            }
        }
    }
    @Inject(method = "write",at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;put(Ljava/lang/String;Lnet/minecraft/nbt/Tag;)Lnet/minecraft/nbt/Tag;",ordinal = 0))
    void Cjptt$writeadditionalvariable(DimensionPalette dimensions, CallbackInfoReturnable<CompoundTag> cir,@Local CompoundTag tag){
        tag.putBoolean("extendedtrain",cjptt$extendedtrain);
    }
    @Inject(method = "read",at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/nbt/NBTHelper;iterateCompoundList(Lnet/minecraft/nbt/ListTag;Ljava/util/function/Consumer;)V",ordinal = 1))
    private static void Cjptt$readadditiionalvariable(CompoundTag tag, Map<UUID, TrackGraph> trackNetworks, DimensionPalette dimensions, CallbackInfoReturnable<Train> cir,@Local Train train){
        ((TrainMixinInterface)train).cjptt$setisextendedtrain(tag.getBoolean("extendedtrain"));
    }
}
