package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSign;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(value = Train.class,remap = false)
public class TrainMixin {
    @Shadow public double throttle;
    @Shadow public List<Carriage> carriages;
    @Shadow public Map<UUID, Pair<Integer, Boolean>> cachedObserverFiltering;
    @Inject(method = "lambda$frontSignalListener$6",at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/data/Pair;getFirst()Ljava/lang/Object;",ordinal = 2),cancellable = true)
    void Cjptt$frontsignallistenerThrottle(CallbackInfoReturnable<TravellingPoint.IEdgePointListener> cir, @Local Pair<TrackEdgePoint, Couple<TrackNode>> couple)
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
        }
    }
}
